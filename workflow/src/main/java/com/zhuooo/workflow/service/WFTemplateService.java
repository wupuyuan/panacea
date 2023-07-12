package com.zhuooo.workflow.service;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.workflow.dao.*;
import com.zhuooo.workflow.pojo.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class WFTemplateService {

    @Autowired
    private WFTemplateDao wfTemplateDao;

    @Autowired
    private WFTemplateNodeDao wfTemplateNodeDao;

    @Autowired
    private WFTemplateLineDao wfTemplateLineDao;

    @Autowired
    private WFParameterDao wfParameterDao;

    @Autowired
    private WFInstanceDao wfInstanceDao;

    /**
     * 保存或更新工作流模板
     *
     * @return uuid 模板编号
     */
    @Transactional
    public String save(WFTemplatePojo template) {

        // 检查key是否已经存在
        WFTemplatePojo existed = this.queryByKey(template.getTemplateKey());

        // 如果不存在则直接插入
        if (existed == null) {
            wfTemplateDao.insert(template);

            if(!CollectionUtils.isEmpty(template.getLines())){
                for(WFTemplateLinePojo line: template.getLines()){
                    line.setTemplateId(template.getId());
                }
            }
            wfTemplateLineDao.insert(template.getLines());

            if(!CollectionUtils.isEmpty(template.getNodes())){
                for(WFTemplateNodePojo node: template.getNodes()){
                    node.setTemplateId(template.getId());
                }
            }
            wfTemplateNodeDao.insert(template.getNodes());

            if(!CollectionUtils.isEmpty(template.getParameters())){
                for(WFParameterPojo parameter: template.getParameters()){
                    parameter.setTemplateId(template.getId());
                }
            }
            wfParameterDao.insert(template.getParameters());

            return template.getId();
        }

        // 如果key已经存在则判断是不是需要更新版本号
        // 如果有对应的实例，则需要更新版本号。(老的版本号生成的实例只能按照老的模板走)
        List<WFInstancePojo> instances = wfInstanceDao.selectGroup(existed.getId());
        if (CollectionUtils.isEmpty(instances)) {
            // 如果没有对应的流程实例，则在原基础上更新

            // 更新主表
            template.setId(existed.getId());
            template.setVersion(existed.getVersion());
            wfTemplateDao.update(template);

            // 删除已有关联的节点、线、形参
            wfTemplateLineDao.deleteGroup(existed.getId());
            wfTemplateNodeDao.deleteGroup(existed.getId());
            wfParameterDao.deleteGroup(existed.getId());

            // 插入
            template.getNodes().stream().forEach(item -> {
                item.setTemplateId(existed.getId());
            });
            template.getLines().stream().forEach(item -> {
                item.setTemplateId(existed.getId());
            });
            template.getParameters().stream().forEach(item -> {
                item.setTemplateId(existed.getId());
            });

        } else {
            // 如果有对应的流程实例，则跟新版本号，已有的流程实例按照老的版本号走
            template.setVersion(existed.getVersion() + 1);
            wfTemplateDao.insert(template);
        }

        wfTemplateLineDao.insert(template.getLines());
        wfTemplateNodeDao.insert(template.getNodes());
        wfParameterDao.insert(template.getParameters());

        return template.getId();
    }

    public List<WFTemplatePojo> list() {
        return wfTemplateDao.select(null, null);
    }

    public WFTemplatePojo queryById(String id) {
        WFTemplatePojo template = wfTemplateDao.selectOne(id);
        setInfo(template);
        return template;
    }

    public WFTemplatePojo queryByKey(String key) {

        List<WFTemplatePojo> list = wfTemplateDao.select(key, null);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        if (list.size() > 1) {
            throw new ZhuoooException(ReturnCode.DAO_MULTI_DATA, "WFTemplatePojo key = " + key);
        }

        WFTemplatePojo template = list.get(0);
        setInfo(template);
        return template;
    }

    private void setInfo(WFTemplatePojo template) {
        if (template != null) {
            template.setLines(wfTemplateLineDao.selectGroup(template.getId()));
            template.setNodes(wfTemplateNodeDao.selectGroup(template.getId()));
            template.setParameters(wfParameterDao.selectGroup(template.getId()));
        }
    }
}
