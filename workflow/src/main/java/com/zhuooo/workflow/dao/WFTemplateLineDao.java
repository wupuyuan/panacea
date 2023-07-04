package com.zhuooo.workflow.dao;

import com.zhuooo.jdbc.dao.BaseCacheDao;
import com.zhuooo.workflow.pojo.db.WFTemplateLinePojo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WFTemplateLineDao extends BaseCacheDao<WFTemplateLinePojo> {

    public List<String> selectNextNodeIds(String templateId, String templateNodeId) {
        List<WFTemplateLinePojo> lines = selectGroup(templateId);
        List<String> nextIds = new ArrayList<>();
        for (WFTemplateLinePojo line : lines) {
            if (line.getPrevId().equals(templateNodeId)) {
                nextIds.add(line.getNextId());
            }
        }
        return nextIds;
    }
}
