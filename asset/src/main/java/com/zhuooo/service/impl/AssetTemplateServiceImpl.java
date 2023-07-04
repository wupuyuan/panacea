package com.zhuooo.service.impl;

import com.zhuooo.dao.AssetParameterDao;
import com.zhuooo.dao.AssetTemplateDao;
import com.zhuooo.pojo.db.AssetParameterPojo;
import com.zhuooo.pojo.db.AssetTemplatePojo;
import com.zhuooo.pojo.vo.AssetTemplateVo;
import com.zhuooo.service.AssetTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AssetTemplateServiceImpl implements AssetTemplateService {

    @Autowired
    private AssetTemplateDao templateDao;

    @Autowired
    private AssetParameterDao parameterDao;

    @Override
    public String add(AssetTemplatePojo template) {
        templateDao.insert(template);
        templateDao.loadChildren(template.getParentId());
        return template.getId();
    }

    @Override
    public void delete(String id) {
        AssetParameterPojo parameter = parameterDao.selectOne(id);
        if (parameter != null) {
            String templateId = parameter.getTemplateId();
            parameterDao.delete(id);
            parameterDao.loadGroup(templateId);
        }
    }


    @Override
    public AssetTemplateVo get(String id) {
        AssetTemplateVo templateVo = null;
        AssetTemplatePojo template = templateDao.selectOne(id);
        if (template != null) {
            templateVo = new AssetTemplateVo();
            templateVo.setTemplate(template);
            templateVo.setParameters(getParameters(template));
        }
        return templateVo;
    }

    @Override
    public List<AssetTemplatePojo> getChildren(String id) {
        return templateDao.selectChildren(id);
    }

    @Override
    public List<AssetParameterPojo> getParameters(String id) {
        AssetTemplatePojo template = templateDao.selectOne(id);
        return getParameters(template);
    }

    @Override
    public List<AssetParameterPojo> getParameters(AssetTemplatePojo template) {
        LinkedList<AssetParameterPojo> parameters = new LinkedList<>();
        setParameters(template, parameters);
        return parameters;
    }

    @Override
    public String addParameter(AssetParameterPojo parameter) {
        parameterDao.insert(parameter);
        parameterDao.loadGroup(parameter.getTemplateId());
        return parameter.getId();
    }

    private void setParameters(AssetTemplatePojo template, LinkedList<AssetParameterPojo> parameters) {
        if (template == null) {
            return;
        }

        List<AssetParameterPojo> list = parameterDao.selectGroup(template.getId());
        if (!CollectionUtils.isEmpty(list)) {
            parameters.addAll(0, list);
        }

        setParameters(templateDao.selectOne(template.getParentId()), parameters);
    }
}
