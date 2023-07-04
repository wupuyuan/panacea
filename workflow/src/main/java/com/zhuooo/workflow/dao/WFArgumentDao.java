package com.zhuooo.workflow.dao;

import com.zhuooo.jdbc.dao.BaseDao;
import com.zhuooo.workflow.dao.mapper.WFArgumentMapper;
import com.zhuooo.workflow.pojo.db.WFArgumentPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WFArgumentDao extends BaseDao<WFArgumentPojo> {
    @Autowired
    private WFArgumentMapper wfArgumentMapper;

    public List<WFArgumentPojo> select(String instanceId, String instanceNodeId, String key) {
        return wfArgumentMapper.select(instanceId, instanceNodeId, key);
    }

}
