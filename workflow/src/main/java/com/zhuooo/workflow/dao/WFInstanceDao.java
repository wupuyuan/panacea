package com.zhuooo.workflow.dao;

import com.zhuooo.jdbc.dao.BaseDao;
import com.zhuooo.workflow.constant.WFConstantEnum;
import com.zhuooo.workflow.dao.mapper.WFInstanceMapper;
import com.zhuooo.workflow.pojo.db.WFInstancePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WFInstanceDao extends BaseDao<WFInstancePojo> {

    @Autowired
    private WFInstanceMapper wfInstanceMapper;

    public WFInstancePojo select(String templateId, String instanceKey) {
        return wfInstanceMapper.selectByInstanceKey(templateId, instanceKey);
    }

    public List<WFInstancePojo> selectByArgumentKey(List<String> keys, String value, List<Integer> status) {
        return wfInstanceMapper.selectByArgument(keys, value, status);
    }

    public int update(WFInstancePojo instance) {
        return wfInstanceMapper.update(instance);
    }

}
