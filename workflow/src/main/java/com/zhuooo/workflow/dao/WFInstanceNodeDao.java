package com.zhuooo.workflow.dao;

import com.zhuooo.jdbc.dao.BaseDao;
import com.zhuooo.workflow.dao.mapper.WFInstanceNodeMapper;
import com.zhuooo.workflow.pojo.db.WFInstanceNodePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class WFInstanceNodeDao extends BaseDao<WFInstanceNodePojo> {
    @Autowired
    private WFInstanceNodeMapper wfInstanceNodeMapper;

    public List<WFInstanceNodePojo> select(String instanceId, String templateId, String templateNodeId, Integer status) {
        return select(instanceId, templateId, Collections.singletonList(templateNodeId), Collections.singletonList(status));
    }

    public List<WFInstanceNodePojo> select(String instanceId, String templateId, List<String> templateNodeIds, List<Integer> status) {
        return wfInstanceNodeMapper.select(instanceId, templateId, templateNodeIds, status);
    }

    public int update(WFInstanceNodePojo node) {
        return wfInstanceNodeMapper.update(node);
    }

    public int update(List<String> ids, Integer status, Integer deleted, String description) {
        return wfInstanceNodeMapper.updateBatch(ids, status, deleted, description);
    }
}
