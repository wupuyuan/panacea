package com.zhuooo.workflow.service;

import com.zhuooo.constant.ParameterTypeEnum;
import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.pojo.vo.UserVo;
import com.zhuooo.workflow.constant.*;
import com.zhuooo.workflow.dao.*;
import com.zhuooo.workflow.pojo.db.*;
import com.zhuooo.workflow.pojo.vo.WFRequest;
import com.zhuooo.workflow.utils.ConditionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WFInstanceService {

    @Autowired
    private WFInstanceDao wfInstanceDao;

    @Autowired
    private WFInstanceNodeDao wfInstanceNodeDao;

    @Autowired
    private WFInstanceLineDao wfInstanceLineDao;

    @Autowired
    private WFArgumentDao wfArgumentDao;

    @Autowired
    private WFTemplateService wfTemplateService;

    @Autowired
    private WFTemplateNodeDao wfTemplateNodeDao;

    @Autowired
    private WFTemplateLineDao wfTemplateLineDao;

    @Autowired
    private WFParameterDao wfParameterDao;

    @Autowired
    private ExampleService exampleService;

    /**
     * 发起，save + approve
     *
     * @param user    操作人
     * @param request 操作信息
     */
    @Transactional
    public WFInstancePojo start(UserVo user, WFRequest request) {
        WFInstancePojo instance = save(user, request);
        List<WFInstanceNodePojo> nodes = queryPendingNode(instance.getId());
        request.setNodeId(nodes.get(0).getId());
        approve(user, request);
        return instance;
    }

    /**
     * 保存(为草稿)
     *
     * @param user    操作人
     * @param request 操作信息
     */
    @Transactional
    public WFInstancePojo save(UserVo user, WFRequest request) {
        WFTemplatePojo template = wfTemplateService.queryByKey(request.getTemplateKey());
        // 通过instanceKey判断是否已经发起过，如果已经发起了，则直接返回已有的
        if (request.getInstanceKey() != null) {
            WFInstancePojo instance = wfInstanceDao.select(template.getId(), request.getInstanceKey());
            if (instance != null) {
                return instance;
            }
        }

        // 判断定义的参数有没有全部传
        checkParameters(template, request.getFields());

        // 实例表
        WFInstancePojo instance = new WFInstancePojo();
        instance.setTemplateId(template.getId());
        instance.setInstanceKey(request.getInstanceKey());
        instance.setStatus(WFStatusEnum.DRAFT.getCode());
        instance.setCreatePerson(user.getName());
        instance.setUpdatePerson(user.getName());
        if (StringUtils.isEmpty(request.getName())) {
            instance.setName(user.getName() + "发起的" + template.getNodes());
        } else {
            instance.setName(request.getName());
        }
        wfInstanceDao.insert(instance);

        // 保存实参
        List<WFArgumentPojo> arguments = new ArrayList<>();
        // 保存流程的发起人
        arguments.add(new WFArgumentPojo(instance.getId(), WFConstantEnum.ROOT.getCode(), WFConstantEnum.INITIATOR.getCode(), ParameterTypeEnum.STRING.getCode(), user.getId(), user.getName()));
        // 保存流程发起的实参
        if (!CollectionUtils.isEmpty(template.getParameters())) {
            for (WFParameterPojo parameter : template.getParameters()) {
                String value = request.getFields().get(parameter.getParameterKey());
                arguments.add(new WFArgumentPojo(instance.getId(), WFConstantEnum.ROOT.getCode(), parameter.getParameterKey(), parameter.getParameterType(), value, null));
            }
        }
        wfArgumentDao.insert(arguments);

        // 创建节点节点
        WFTemplateNodePojo startNode = wfTemplateNodeDao.selectStartNode(template.getId());
        createNode(user, instance.getId(), startNode);

        return instance;
    }

    /**
     * 同意
     *
     * @param user    操作人
     * @param request 操作信息
     */
    @Transactional
    public void approve(UserVo user, WFRequest request) {
        WFInstanceNodePojo instanceNode = getInstanceNode(request.getNodeId());

        // todo try lock by redis

        checkHandler(user, instanceNode);
        checkCandidate(user, instanceNode);

        // 更新流程实例信息
        auditInstance(user, instanceNode);

        // 更新实例参数
        if (!CollectionUtils.isEmpty(request.getFields())) {
            List<WFParameterPojo> parameters = wfParameterDao.selectGroup(instanceNode.getTemplateId());
            Map<String, Integer> parameterMap = parameters.stream().collect(Collectors.toMap(WFParameterPojo::getParameterKey, WFParameterPojo::getParameterType, (k1, k2) -> k2));
            for (String key : request.getFields().keySet()) {
                int type = parameterMap.containsKey(key) ? parameterMap.get(key) : ParameterTypeEnum.STRING.getCode();
                wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), key, type, request.getFields().get(key), null));
            }
        }

        // 判断当前节点是否结束
        if (isCurrentNodeFinished(instanceNode)) {
            // 更新当前节点信息
            finishCurrentNode(user, instanceNode, request.getDescription());
            // todo callback
            // 创建后续节点
            createNextNode(user, instanceNode);
        }
    }

    /**
     * 驳回，状态类似草稿，由发起人重新提交
     *
     * @param user           操作人
     * @param instanceNodeId 流程节点id
     * @param description    描述
     */
    @Transactional
    public void reject(UserVo user, String instanceNodeId, String description) {
        WFInstanceNodePojo instanceNode = getInstanceNode(instanceNodeId);

        checkHandler(user, instanceNode);
        checkCandidate(user, instanceNode);

        // 更新节点状态
        instanceNode.setStatus(WFNodeStatusEnum.REJECT.getCode());
        instanceNode.setUpdatePerson(user.getName());
        wfInstanceNodeDao.update(instanceNode);
        // 记录操作人
        createHandler(user, instanceNode, WFConstantEnum.HANDLER);

        // 更新其它待审核节点作废(并行)
        List<WFInstanceNodePojo> instanceNodes = wfInstanceNodeDao.select(instanceNode.getInstanceId(), null, null, WFNodeStatusEnum.PENDING.getCode());
        if (!CollectionUtils.isEmpty(instanceNodes)) {
            List<String> ids = instanceNodes.stream().map(WFInstanceNodePojo::getId).collect(Collectors.toList());
            wfInstanceNodeDao.update(ids, WFNodeStatusEnum.CANCEL.getCode(), null, "驳回");
        }

        // 更新流程状态
        WFInstancePojo instance = wfInstanceDao.selectOne(instanceNode.getInstanceId());
        instance.setStatus(WFStatusEnum.REJECTED.getCode());
        instance.setUpdatePerson(user.getName());
        wfInstanceDao.update(instance);

        // todo

        // 驳回至流程创建人，重新创发起人节点
        UserVo initiator = queryInitiator(instanceNode.getInstanceId());
        WFTemplateNodePojo startNode = wfTemplateNodeDao.selectStartNode(instance.getTemplateId());
        createNode(initiator, instance.getId(), startNode);

    }

    /**
     * 作废，只有发起人处理
     *
     * @param user        操作人
     * @param instanceId  流程id
     * @param description 描述
     */
    @Transactional
    public void cancel(UserVo user, String instanceId, String description) {
        WFInstancePojo instance = wfInstanceDao.selectOne(instanceId);

        UserVo initiator = queryInitiator(instance.getId());

        // 已完结的流程不能废弃
        if (instance.getStatus() == WFStatusEnum.FINISHED.getCode()) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_FINISHED, instanceId);
        }

        // 只有发起人能废弃
        if (!initiator.getId().equals(user.getId())) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_PERMISSIONS, instanceId);
        }

        // 更新所有节点为废弃
        List<WFInstanceNodePojo> instanceNodes = wfInstanceNodeDao.selectGroup(instance.getId());
        if (!CollectionUtils.isEmpty(instanceNodes)) {
            List<String> ids = instanceNodes.stream().map(WFInstanceNodePojo::getId).collect(Collectors.toList());
            wfInstanceNodeDao.update(ids, WFNodeStatusEnum.CANCEL.getCode(), null, "作废");
        }

        // 更新流程状态
        instance.setStatus(WFStatusEnum.CANCEL.getCode());
        instance.setUpdatePerson(user.getName());
        instance.setDescription("作废");
        wfInstanceDao.update(instance);
    }

    /**
     * 加签，添加一个节点。
     *
     * @param user           操作人
     * @param targetUser     委托目标人
     * @param instanceNodeId 流程id
     * @param description
     */
    @Transactional
    public void delegate(UserVo user, UserVo targetUser, String instanceNodeId, String description) {
        WFInstanceNodePojo instanceNode = wfInstanceNodeDao.selectOne(instanceNodeId);
        checkHandler(user, instanceNode);
        checkCandidate(user, instanceNode);

        WFTemplateNodePojo templateNode = wfTemplateNodeDao.selectOne(instanceNode.getTemplateNodeId());
        if (templateNode.getNodeType() == WFNodeTypeEnum.ALL_SIGN.getCode()) {
            wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.DELEGATE.getCode(), ParameterTypeEnum.STRING.getCode(), user.getId(), user.getName()));
            wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.TRUSTEE.getCode(), ParameterTypeEnum.STRING.getCode(), targetUser.getId(), targetUser.getName()));
        } else if (templateNode.getNodeType() == WFNodeTypeEnum.COUNTER_SIGN.getCode()) {
            // 当前节点结束
            wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.HANDLER.getCode(), ParameterTypeEnum.STRING.getCode(), user.getId(), user.getName()));
            instanceNode.setStatus(WFNodeStatusEnum.APPROVE.getCode());
            instanceNode.setUpdatePerson(user.getName());
            wfInstanceNodeDao.update(instanceNode);

            // 创建加签节点
            WFInstanceNodePojo delegateNode = createDelegateNode(user, targetUser, instanceNode.getId(), templateNode);
            createInstanceLine(instanceNode, delegateNode);
        } else {
            throw new ZhuoooException(ReturnCode.WORKFLOW_DELEGATE, "Can not delegate at this node");
        }


    }

    /**
     * 委托，不添加新节点
     *
     * @param user           操作人
     * @param targetUser     委托目标人
     * @param instanceNodeId 流程id
     * @param description
     */
    @Transactional
    public void assign(UserVo user, UserVo targetUser, String instanceNodeId, String description) {
        WFInstanceNodePojo instanceNode = wfInstanceNodeDao.selectOne(instanceNodeId);
        checkHandler(user, instanceNode);
        WFArgumentPojo candidate = checkCandidate(user, instanceNode);

        // 候选人 → 委托人
        wfArgumentDao.delete(candidate.getId());
        candidate.setArgumentKey(WFConstantEnum.CLIENT.getCode());
        wfArgumentDao.insert(candidate);

        // 添加受托人(候选人)
        wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.TRUSTEE.getCode(), ParameterTypeEnum.STRING.getCode(), targetUser.getId(), targetUser.getName()));
        wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.HANDLER.getCode(), ParameterTypeEnum.STRING.getCode(), targetUser.getId(), targetUser.getName()));

    }


    /**
     * 查询当前待审批的节点，如果是并行，则会有多个
     *
     * @param instanceId 实例id
     */
    public List<WFInstanceNodePojo> queryPendingNode(String instanceId) {
        List<WFInstanceNodePojo> ret = new ArrayList<>();
        List<WFInstanceNodePojo> nodes = wfInstanceNodeDao.selectGroup(instanceId);
        for (WFInstanceNodePojo node : nodes) {
            if (node.getStatus() == WFNodeStatusEnum.PENDING.getCode()) {
                ret.add(node);
            }
        }
        return ret;
    }

    /**
     * 查询流程详情
     *
     * @param id
     * @return
     */
    public WFInstancePojo detail(String id) {
        WFInstancePojo instance = wfInstanceDao.selectOne(id);
        List<WFInstanceNodePojo> nodes = wfInstanceNodeDao.selectGroup(id);
        List<WFInstanceLinePojo> lines = wfInstanceLineDao.selectGroup(id);
        List<WFArgumentPojo> arguments = wfArgumentDao.selectGroup(id);

        Map<String, List<WFArgumentPojo>> nodeArgumentMap = arguments.stream().collect(Collectors.groupingBy(WFArgumentPojo::getInstanceNodeId));
        for (WFInstanceNodePojo node : nodes) {
            node.setArguments(nodeArgumentMap.get(node.getId()));
        }

        instance.setArgs(nodeArgumentMap.get(WFConstantEnum.ROOT.getCode()));
        instance.setNodes(nodes);
        instance.setLines(lines);
        return instance;
    }

    /**
     * 查询制定角色对应的流程
     */
    public List<WFInstancePojo> list(UserVo user, List<WFConstantEnum> constants, List<Integer> status) {
        List<String> keys = constants.stream().map(WFConstantEnum::getCode).collect(Collectors.toList());
        return wfInstanceDao.selectByArgumentKey(keys, user.getId(), status);
    }


    private WFInstanceNodePojo getInstanceNode(String id) {
        WFInstanceNodePojo instanceNode = wfInstanceNodeDao.selectOne(id);
        if (instanceNode == null) {
            throw new ZhuoooException(ReturnCode.DAO_EMPTY_DATA);
        }

        return instanceNode;
    }

    private void checkParameters(WFTemplatePojo template, Map<String, String> fields) {
        // 校验形参是否都有对应的实参
        List<WFParameterPojo> parameters = wfParameterDao.selectGroup(template.getId());
        if (!CollectionUtils.isEmpty(parameters)) {
            template.setParameters(parameters);

            if (CollectionUtils.isEmpty(fields)) {
                throw new ZhuoooException(ReturnCode.PARAM_NULL, "fields is null!");
            }

            for (WFParameterPojo parameter : parameters) {
                if (!fields.containsKey(parameter.getParameterKey())) {
                    throw new ZhuoooException(ReturnCode.PARAM_ILLEGALITY, "缺失参数：" + parameter.getParameterKey());
                }
            }
        }
    }


    /**
     * 判断当前人有没有审批过
     *
     * @param user         当前人
     * @param instanceNode 待审批的节点
     */
    private void checkHandler(UserVo user, WFInstanceNodePojo instanceNode) {
        List<WFArgumentPojo> handlers = wfArgumentDao.select(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.HANDLER.getCode());

        // 还没人审批
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }

        WFTemplateNodePojo templateNode = wfTemplateNodeDao.selectOne(instanceNode.getTemplateNodeId());
        if (templateNode.getNodeType() == WFNodeTypeEnum.ALL_SIGN.getCode()) {
            // 如果是汇签，判断当前人是不是已经审批过了
            if (findUser(user, handlers) != null) {
                throw new ZhuoooException(ReturnCode.WORKFLOW_ALREADY);
            }
        } else {
            // 如果是会签，只需要一人审批即可，handlers不为空则说明已经审批
            throw new ZhuoooException(ReturnCode.WORKFLOW_ALREADY);
        }
    }

    /**
     * 判断当前人是不是当前节点的候选人
     *
     * @param user         当前人
     * @param instanceNode 当前节点
     */
    private WFArgumentPojo checkCandidate(UserVo user, WFInstanceNodePojo instanceNode) {
        List<WFArgumentPojo> candidates = wfArgumentDao.select(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.CANDIDATE.getCode());
        WFArgumentPojo candidate = findUser(user, candidates);
        if (candidate == null) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_PERMISSIONS);
        }
        return candidate;
    }

    private WFArgumentPojo findUser(UserVo user, List<WFArgumentPojo> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (WFArgumentPojo item : list) {
                if (item.getArgumentValue().equals(user.getId())) {
                    return item;
                }
            }
        }
        return null;
    }

    private boolean isCurrentNodeFinished(WFInstanceNodePojo instanceNode) {
        WFTemplateNodePojo templateNode = wfTemplateNodeDao.selectOne(instanceNode.getTemplateNodeId());

        if (templateNode.getNodeType() == WFNodeTypeEnum.ALL_SIGN.getCode()) {
            // 如果是汇签节点，需要判断是否所有的候选人是不是都审批结束(汇签不能加签)
            List<WFArgumentPojo> arguments = wfArgumentDao.select(instanceNode.getInstanceId(), instanceNode.getId(), null);
            int handlerCount = 0;
            int candidateCount = 0;
            for (WFArgumentPojo argument : arguments) {
                // 候选人
                if (WFConstantEnum.CANDIDATE.getCode().equals(argument.getArgumentKey())) {
                    candidateCount++;
                }

                // 处理人 & 受托人
                if (WFConstantEnum.HANDLER.getCode().equals(argument.getArgumentKey()) || WFConstantEnum.TRUSTEE.getCode().equals(argument.getArgumentKey())) {
                    handlerCount++;
                }
            }
            return handlerCount == candidateCount;
        } else {
            // 如果是会签、加签节点，需要判断是否所有同级节点都已结束。只有自己没结束
            List<WFInstanceNodePojo> nodes = wfInstanceNodeDao.select(instanceNode.getInstanceId(), instanceNode.getTemplateId(), instanceNode.getTemplateNodeId(), WFNodeStatusEnum.PENDING.getCode());
            return (nodes != null && nodes.size() == 1);
        }
    }

    private void finishCurrentNode(UserVo user, WFInstanceNodePojo instanceNode, String description) {
        instanceNode.setUpdatePerson(user.getName());
        instanceNode.setStatus(WFNodeStatusEnum.APPROVE.getCode());
        if (description != null) {
            String descriptionription = instanceNode.getDescription() == null ? "" : instanceNode.getDescription();
            instanceNode.setDescription(descriptionription + "\n" + description);
        }
        wfInstanceNodeDao.update(instanceNode);
    }

    /**
     * 是否要创建后续节点？
     * 1. 如果是串行节点，且上节点没有全部结束，则不创建后续节点。
     * 2. 如果是串行节点，且上节点全部结束，则创建后续节点。
     * 3. 如果是并行节点，则创建后续节点
     */
    private boolean needCreateNextNode(WFInstanceNodePojo instanceNode) {
        boolean bool;
        WFTemplateNodePojo templateNode = wfTemplateNodeDao.selectOne(instanceNode.getTemplateNodeId());
        if (templateNode.getFlowType() == WFFlowTpyeEnum.PARALLEL.getCode()) {
            // 如果是并行节点，需要判断上游节点是否全部结束
            List<WFTemplateLinePojo> lines = wfTemplateLineDao.selectGroup(instanceNode.getTemplateId());
            // 上游节点id集合
            List<String> prevIds = new ArrayList<>();
            for (WFTemplateLinePojo line : lines) {
                if (line.getNextId().equals(templateNode.getId())) {
                    prevIds.add(line.getPrevId());
                }
            }

            List<Integer> status = Arrays.asList(WFNodeStatusEnum.PENDING.getCode(), WFNodeStatusEnum.REJECT.getCode(), WFNodeStatusEnum.ERROR.getCode());
            List<WFInstanceNodePojo> nodes = wfInstanceNodeDao.select(instanceNode.getInstanceId(), instanceNode.getTemplateId(), prevIds, status);
            bool = CollectionUtils.isEmpty(nodes);
        } else {
            // 如果是串行节点，则直接返回true
            bool = true;
        }
        return bool;
    }

    private void createNextNode(UserVo user, WFInstanceNodePojo instanceNode) {
        if (!needCreateNextNode(instanceNode)) {
            return;
        }

        // 找到下游节点的模板节点
        WFTemplateNodePojo templateNode = wfTemplateNodeDao.selectOne(instanceNode.getTemplateNodeId());
        List<WFTemplateLinePojo> lines = wfTemplateLineDao.selectGroup(instanceNode.getTemplateId());
        for (WFTemplateLinePojo line : lines) {
            if (line.getPrevId().equals(templateNode.getId())) {
                WFTemplateNodePojo nextTemplateNode = wfTemplateNodeDao.selectOne(line.getNextId());
                WFInstanceNodePojo nextInstanceNode = createNode(user, instanceNode.getInstanceId(), nextTemplateNode);
                createInstanceLine(line, instanceNode, nextInstanceNode);
            }
        }
    }

    private void auditInstance(UserVo user, WFInstanceNodePojo instanceNode) {
        // 添加当前节点的处理人
        WFTemplateNodePojo initiatorNode = queryInitiatorNode(instanceNode.getTemplateId());
        WFConstantEnum constantEnum;
        if (initiatorNode.getId().equals(instanceNode.getTemplateNodeId())) {
            // 如果是第一个节点
            constantEnum = WFConstantEnum.SUBMITTER;
        } else {
            constantEnum = WFConstantEnum.HANDLER;
        }

        createHandler(user, instanceNode, constantEnum);
        // 更新流状态和更新时间
        WFInstancePojo instance = wfInstanceDao.selectOne(instanceNode.getInstanceId());
        instance.setStatus(WFStatusEnum.RUNNING.getCode());
        instance.setUpdatePerson(user.getName());
        wfInstanceDao.update(instance);
    }

    private void createHandler(UserVo user, WFInstanceNodePojo instanceNode, WFConstantEnum constantEnum) {
        WFArgumentPojo handler = new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), constantEnum.getCode(), ParameterTypeEnum.STRING.getCode(), user.getId(), user.getName());
        wfArgumentDao.insert(handler);
    }

    private WFInstanceNodePojo createNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode) {

        WFNodeTypeEnum nodeTypeEnum = WFNodeTypeEnum.getByCode(templateNode.getNodeType());
        WFInstanceNodePojo ret;
        switch (nodeTypeEnum) {
            case START:
                ret = createStartNode(user, instanceId, templateNode);
                break;
            case END:
                ret = createEndNode(user, instanceId, templateNode);
                break;
            case CONDITION:
                ret = createConditionNode(user, instanceId, templateNode);
                break;
            case COUNTER_SIGN:
            case ALL_SIGN:
            case DELEGATE:
                ret = createAuditNode(user, instanceId, templateNode);
                break;
            default:
                throw new ZhuoooException(ReturnCode.WORKFLOW_NEXT, "未知节点类型：" + templateNode.getId());
        }

        return ret;
    }

    private UserVo queryInitiator(String instanceId) {
        List<WFArgumentPojo> list = wfArgumentDao.select(instanceId, WFConstantEnum.ROOT.getCode(), WFConstantEnum.INITIATOR.getCode());
        UserVo user = new UserVo();
        user.setId(list.get(0).getArgumentValue());
        user.setName(list.get(0).getDescription());
        return user;
    }


    private WFInstanceNodePojo createStartNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode) {
        WFInstanceNodePojo instanceNode = newInstanceNode(user, instanceId, templateNode, WFNodeStatusEnum.APPROVE);
        // todo callback

        // 开始节点的下游节点应该只有一个，即发起人节点
        List<String> nextTemplateNodes = wfTemplateLineDao.selectNextNodeIds(templateNode.getTemplateId(), templateNode.getId());
        if (CollectionUtils.isEmpty(nextTemplateNodes) || nextTemplateNodes.size() != 1) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_ILLEGALITY);
        }

        WFTemplateNodePojo nextTemplateNode = wfTemplateNodeDao.selectOne(nextTemplateNodes.get(0));
        WFInstanceNodePojo nextInstanceNode = createNode(user, instanceId, nextTemplateNode);
        createInstanceLine(instanceNode, nextInstanceNode);
        return instanceNode;
    }


    private WFInstanceNodePojo createEndNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode) {
        WFInstanceNodePojo instanceNode = newInstanceNode(user, instanceId, templateNode, WFNodeStatusEnum.APPROVE);
        // todo callback

        // 更新流程状态为结束
        WFInstancePojo instance = wfInstanceDao.selectOne(instanceId);
        instance.setStatus(WFStatusEnum.FINISHED.getCode());
        instance.setUpdatePerson(user.getName());
        wfInstanceDao.update(instance);
        return instanceNode;
    }

    // 创建人工审批节点
    private WFInstanceNodePojo createAuditNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode) {
        WFInstanceNodePojo instanceNode = newInstanceNode(user, instanceId, templateNode, WFNodeStatusEnum.PENDING);
        // todo callback

        // 创建节点的候选人
        // 根据规则code和流程发起人信息，查询下个节点的处理人
        List<UserVo> candidates = exampleService.queryUsers(templateNode.getExpression(), user);
        if (CollectionUtils.isEmpty(candidates)) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_CANDIDATE);
        }

        for (UserVo candidate : candidates) {
            wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.CANDIDATE.getCode(), ParameterTypeEnum.STRING.getCode(), candidate.getId(), candidate.getName()));
        }

        return instanceNode;
    }

    private WFInstanceNodePojo createDelegateNode(UserVo user, UserVo target, String instanceId, WFTemplateNodePojo templateNode) {
        WFInstanceNodePojo instanceNode = newInstanceNode(user, instanceId, templateNode, WFNodeStatusEnum.PENDING);
        // todo callback

        // 创建节点的候选人
        wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.DELEGATE.getCode(), ParameterTypeEnum.STRING.getCode(), user.getId(), user.getName()));
        wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.TRUSTEE.getCode(), ParameterTypeEnum.STRING.getCode(), target.getId(), target.getName()));
        wfArgumentDao.insert(new WFArgumentPojo(instanceNode.getInstanceId(), instanceNode.getId(), WFConstantEnum.CANDIDATE.getCode(), ParameterTypeEnum.STRING.getCode(), target.getId(), target.getName()));
        return instanceNode;
    }


    private WFInstanceNodePojo createConditionNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode) {
        WFInstanceNodePojo instanceNode = newInstanceNode(user, instanceId, templateNode, WFNodeStatusEnum.APPROVE);
        Map<String, Object> map = queryInstanceArguments(templateNode.getTemplateId(), instanceId);
        boolean bool = ConditionUtils.calculate(templateNode.getExpression(), map);
        WFLineTypeEnum lineType = WFLineTypeEnum.getLineTypeEnum(bool);
        List<WFTemplateLinePojo> lines = wfTemplateLineDao.selectGroup(templateNode.getTemplateId());
        for (WFTemplateLinePojo line : lines) {
            if (line.getPrevId().equals(templateNode.getId()) && line.getType() == lineType.getCode()) {
                WFTemplateNodePojo nextTemplateNode = wfTemplateNodeDao.selectOne(line.getNextId());
                WFInstanceNodePojo nextInstanceNode = createNode(user, instanceId, nextTemplateNode);
                createInstanceLine(line, instanceNode, nextInstanceNode);
            }
        }
        return instanceNode;
    }

    /**
     * 查询流程的实参，需要考虑启动时实参被审批时实参覆盖的情况
     */
    private Map<String, Object> queryInstanceArguments(String templateId, String instanceId) {
        List<WFParameterPojo> parameters = wfParameterDao.selectGroup(templateId);
        Set<String> parameterKey = parameters.stream().map(item -> item.getParameterKey()).collect(Collectors.toSet());
        List<WFArgumentPojo> arguments = wfArgumentDao.selectGroup(instanceId);
        Map<String, Object> ret = new HashMap<>();
        for (WFArgumentPojo argument : arguments) {
            // 如果在形参中定义(不是候选人、审批人这些信息)
            if (parameterKey.contains(argument.getArgumentKey())) {
                // 如果存在多个，则说明有审批时覆盖的参数
                if (ret.containsKey(argument.getArgumentKey())) {
                    if (!WFConstantEnum.ROOT.getCode().equals(argument.getInstanceNodeId())) {
                        setArgument(ret, argument);
                    }
                } else {
                    setArgument(ret, argument);
                }
            }
        }
        return ret;
    }

    private void setArgument(Map<String, Object> map, WFArgumentPojo argument) {
        if (argument.getArgumentType() == ParameterTypeEnum.NUMBER.getCode()) {
            map.put(argument.getArgumentKey(), Double.valueOf(argument.getArgumentValue()));
        } else {
            map.put(argument.getArgumentKey(), argument.getArgumentValue());
        }
    }

    private void createInstanceLine(WFTemplateLinePojo line, WFInstanceNodePojo prevInstanceNode, WFInstanceNodePojo nextInstanceNode) {
        WFInstanceLinePojo instanceLine = new WFInstanceLinePojo(line.getTemplateId(), line.getId(), prevInstanceNode.getInstanceId(), prevInstanceNode.getId(), nextInstanceNode.getId());
        wfInstanceLineDao.insert(instanceLine);
    }

    private void createInstanceLine(WFInstanceNodePojo prevInstanceNode, WFInstanceNodePojo nextInstanceNode) {
        List<WFTemplateLinePojo> lines = wfTemplateLineDao.selectGroup(prevInstanceNode.getTemplateId());
        for (WFTemplateLinePojo line : lines) {
            WFInstanceLinePojo instanceLine = new WFInstanceLinePojo(line.getTemplateId(), line.getId(), prevInstanceNode.getInstanceId(), prevInstanceNode.getId(), nextInstanceNode.getId());
            wfInstanceLineDao.insert(instanceLine);
            break;
        }
    }

    private WFTemplateNodePojo queryInitiatorNode(String templateId) {
        WFTemplateNodePojo startNode = wfTemplateNodeDao.selectStartNode(templateId);
        List<String> nextNodeIds = wfTemplateLineDao.selectNextNodeIds(templateId, startNode.getId());
        if (CollectionUtils.isEmpty(nextNodeIds) || nextNodeIds.size() != 1) {
            throw new ZhuoooException(ReturnCode.WORKFLOW_ILLEGALITY);
        }
        return wfTemplateNodeDao.selectOne(nextNodeIds.get(0));
    }

    private WFInstanceNodePojo newInstanceNode(UserVo user, String instanceId, WFTemplateNodePojo templateNode, WFNodeStatusEnum statusEnum) {
        WFInstanceNodePojo instanceNode = new WFInstanceNodePojo();
        instanceNode.setName(templateNode.getName());
        instanceNode.setInstanceId(instanceId);
        instanceNode.setTemplateId(templateNode.getTemplateId());
        instanceNode.setTemplateNodeId(templateNode.getId());
        instanceNode.setNodeType(templateNode.getNodeType());
        instanceNode.setStatus(statusEnum.getCode());
        instanceNode.setCreatePerson(user.getName());
        instanceNode.setUpdatePerson(user.getName());
        wfInstanceNodeDao.insert(instanceNode);
        return instanceNode;
    }
}
