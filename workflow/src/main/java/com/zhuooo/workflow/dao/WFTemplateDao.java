package com.zhuooo.workflow.dao;

import com.zhuooo.jdbc.dao.BaseCacheDao;
import com.zhuooo.workflow.dao.mapper.WFTemplateMapper;
import com.zhuooo.workflow.pojo.db.WFTemplatePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WFTemplateDao extends BaseCacheDao<WFTemplatePojo> {
    @Autowired
    private WFTemplateMapper wfTemplateMapper;

    public List<WFTemplatePojo> select(String key, Integer type) {
        return wfTemplateMapper.list(key, type);
    }

    public int update(WFTemplatePojo template) {
        return wfTemplateMapper.update(template);
    }

}
