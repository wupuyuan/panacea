package com.zhuooo.workflow.dao;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.jdbc.dao.BaseCacheDao;
import com.zhuooo.workflow.constant.WFNodeTypeEnum;
import com.zhuooo.workflow.pojo.db.WFTemplateNodePojo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WFTemplateNodeDao extends BaseCacheDao<WFTemplateNodePojo> {
    public WFTemplateNodePojo selectStartNode(String templateId) {
        List<WFTemplateNodePojo> nodes = selectGroup(templateId);
        for (WFTemplateNodePojo node : nodes) {
            if (WFNodeTypeEnum.START.getCode() == node.getNodeType()) {
                return node;
            }
        }
        throw new ZhuoooException(ReturnCode.WORKFLOW_ILLEGALITY, "Can not find start node by: " + templateId);
    }
}
