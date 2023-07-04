package com.zhuooo.workflow.test;

import com.alibaba.fastjson.JSONObject;
import com.zhuooo.constant.ParameterTypeEnum;
import com.zhuooo.constant.RuleEnum;
import com.zhuooo.utils.UuidUtils;
import com.zhuooo.workflow.constant.*;
import com.zhuooo.workflow.pojo.db.WFParameterPojo;
import com.zhuooo.workflow.pojo.db.WFTemplateLinePojo;
import com.zhuooo.workflow.pojo.db.WFTemplateNodePojo;
import com.zhuooo.workflow.pojo.db.WFTemplatePojo;
import com.zhuooo.workflow.pojo.vo.WFRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester {

    private static String templateKey = "claim";
    private static String templateId = "c26c1c28a41246c3ab5b3c10e139ca75";

    public static void main(String[] args) {
//        createTemplate();
        createSaveInstance();
    }

    public static void createTemplate() {
        WFTemplatePojo templatePojo = new WFTemplatePojo();

        templatePojo.setId(templateId);
        templatePojo.setName("报销流程");
        templatePojo.setTemplateKey(templateKey);
        templatePojo.setVersion(0);
        templatePojo.setType(WFTypeEnum.APPLY.getCode());
        templatePojo.setDeleted(0);
        templatePojo.setDescription("演示用报销流程");
        templatePojo.setCreatePerson("123");
        templatePojo.setUpdatePerson("123");


        List<WFTemplateNodePojo> nodes = new ArrayList<>();

        List<WFTemplateLinePojo> lines = new ArrayList<>();


        WFTemplateNodePojo start = new WFTemplateNodePojo();
        {
            start.setId(UuidUtils.generateUuid());
            start.setName("开始");
            start.setDeleted(0);
            start.setTemplateId(templatePojo.getId());
            start.setNodeType(WFNodeTypeEnum.START.getCode());
            start.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            start.setExpression("");
            start.setDescription("");
            nodes.add(start);
        }

        WFTemplateNodePojo submmit = new WFTemplateNodePojo();
        {
            submmit.setId(UuidUtils.generateUuid());
            submmit.setName("提交报销单");
            submmit.setDeleted(0);
            submmit.setTemplateId(templatePojo.getId());
            submmit.setNodeType(WFNodeTypeEnum.COUNTER_SIGN.getCode());
            submmit.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            submmit.setExpression(RuleEnum.SUBMITTER.getCode());
            submmit.setDescription("");
            nodes.add(submmit);
        }

        WFTemplateNodePojo leader = new WFTemplateNodePojo();
        {
            leader.setId(UuidUtils.generateUuid());
            leader.setName("直属领导审批");
            leader.setDeleted(0);
            leader.setTemplateId(templatePojo.getId());
            leader.setNodeType(WFNodeTypeEnum.COUNTER_SIGN.getCode());
            leader.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            leader.setExpression(RuleEnum.LEADER.getCode());
            leader.setDescription("");
            nodes.add(leader);
        }

        WFTemplateNodePojo condition = new WFTemplateNodePojo();
        {
            condition.setId(UuidUtils.generateUuid());
            condition.setName("金额判断");
            condition.setDeleted(0);
            condition.setTemplateId(templatePojo.getId());
            condition.setNodeType(WFNodeTypeEnum.CONDITION.getCode());
            condition.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            condition.setExpression("money >= 100");
            condition.setDescription("");
            nodes.add(condition);
        }

        WFTemplateNodePojo manager = new WFTemplateNodePojo();
        {
            manager.setId(UuidUtils.generateUuid());
            manager.setName("部门经理审批");
            manager.setDeleted(0);
            manager.setTemplateId(templatePojo.getId());
            manager.setNodeType(WFNodeTypeEnum.ALL_SIGN.getCode());
            manager.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            manager.setExpression(RuleEnum.MANAGER.getCode());
            manager.setDescription("");
            nodes.add(manager);
        }

        WFTemplateNodePojo accounting = new WFTemplateNodePojo();
        {
            accounting.setId(UuidUtils.generateUuid());
            accounting.setName("会计审批");
            accounting.setDeleted(0);
            accounting.setTemplateId(templatePojo.getId());
            accounting.setNodeType(WFNodeTypeEnum.COUNTER_SIGN.getCode());
            accounting.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            accounting.setExpression(RuleEnum.ACCOUNTING.getCode());
            accounting.setDescription("");
            nodes.add(accounting);
        }

        WFTemplateNodePojo end = new WFTemplateNodePojo();
        {
            end.setId(UuidUtils.generateUuid());
            end.setName("结束");
            end.setDeleted(0);
            end.setTemplateId(templatePojo.getId());
            end.setNodeType(WFNodeTypeEnum.END.getCode());
            end.setFlowType(WFFlowTpyeEnum.SERIAL.getCode());
            end.setExpression("");
            end.setDescription("");
            nodes.add(end);
        }

        WFTemplateLinePojo l1 = new WFTemplateLinePojo();
        {
            l1.setPrevId(start.getId());
            l1.setNextId(submmit.getId());
            l1.setTemplateId(templatePojo.getId());
            l1.setType(WFLineTypeEnum.DEFAULT.getCode());
            lines.add(l1);
        }

        WFTemplateLinePojo l2 = new WFTemplateLinePojo();
        {
            l2.setPrevId(submmit.getId());
            l2.setNextId(leader.getId());
            l2.setTemplateId(templatePojo.getId());
            l2.setType(WFLineTypeEnum.DEFAULT.getCode());
            lines.add(l2);
        }

        WFTemplateLinePojo l3 = new WFTemplateLinePojo();
        {
            l3.setPrevId(leader.getId());
            l3.setNextId(condition.getId());
            l3.setTemplateId(templatePojo.getId());
            l3.setType(WFLineTypeEnum.DEFAULT.getCode());
            lines.add(l3);
        }

        WFTemplateLinePojo l4 = new WFTemplateLinePojo();
        {
            l4.setPrevId(condition.getId());
            l4.setNextId(manager.getId());
            l4.setTemplateId(templatePojo.getId());
            l4.setType(WFLineTypeEnum.TRUE.getCode());
            lines.add(l4);
        }

        WFTemplateLinePojo l5 = new WFTemplateLinePojo();
        {
            l5.setPrevId(condition.getId());
            l5.setNextId(accounting.getId());
            l5.setTemplateId(templatePojo.getId());
            l5.setType(WFLineTypeEnum.FALSE.getCode());
            lines.add(l5);
        }

        WFTemplateLinePojo l6 = new WFTemplateLinePojo();
        {
            l6.setPrevId(manager.getId());
            l6.setNextId(accounting.getId());
            l6.setTemplateId(templatePojo.getId());
            l6.setType(WFLineTypeEnum.DEFAULT.getCode());
            lines.add(l6);
        }

        WFTemplateLinePojo l7 = new WFTemplateLinePojo();
        {
            l7.setPrevId(accounting.getId());
            l7.setNextId(end.getId());
            l7.setTemplateId(templatePojo.getId());
            l7.setType(WFLineTypeEnum.DEFAULT.getCode());
            lines.add(l7);
        }


        List<WFParameterPojo> parameters = new ArrayList<>();
        WFParameterPojo parameterPojo = new WFParameterPojo();
        parameterPojo.setId(UuidUtils.generateUuid());
        parameterPojo.setTemplateId(templatePojo.getId());
        parameterPojo.setTemplateNodeId(WFConstantEnum.ROOT.getCode());
        parameterPojo.setParameterKey("money");
        parameterPojo.setParameterType(ParameterTypeEnum.NUMBER.getCode());
        parameterPojo.setDescription("报销金额");
        parameters.add(parameterPojo);

        templatePojo.setNodes(nodes);
        templatePojo.setLines(lines);
        templatePojo.setParameters(parameters);

        System.out.println(JSONObject.toJSONString(templatePojo));
    }

    public static void createSaveInstance() {
        WFRequest request = new WFRequest();
        request.setName("XX发起的报销流程" + System.currentTimeMillis());
        request.setTemplateKey(templateKey);
        request.setInstanceKey("baoxiao");
        Map<String, String> fields = new HashMap<>();
        fields.put("money", "100");
        request.setFields(fields);

        System.out.println(JSONObject.toJSONString(request));
    }
}
