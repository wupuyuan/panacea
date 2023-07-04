package com.zhuooo.dao;

import com.zhuooo.dao.mapper.AssetInstanceMapper;
import com.zhuooo.jdbc.dao.BaseDao;
import com.zhuooo.pojo.db.AssetInstancePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetInstanceDao extends BaseDao<AssetInstancePojo> {

    @Autowired
    private AssetInstanceMapper assetInstanceMapper;

    public List<AssetInstancePojo> selectByTemplateId(String templateId) {
        return assetInstanceMapper.selectByTemplateId(templateId);
    }
}
