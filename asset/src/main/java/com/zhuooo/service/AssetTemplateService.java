package com.zhuooo.service;

import com.zhuooo.pojo.db.AssetParameterPojo;
import com.zhuooo.pojo.db.AssetTemplatePojo;
import com.zhuooo.pojo.vo.AssetTemplateVo;

import java.util.List;

public interface AssetTemplateService {

    String add(AssetTemplatePojo template);

    void delete(String id);

    AssetTemplateVo get(String id);

    List<AssetTemplatePojo> getChildren(String id);

    String addParameter(AssetParameterPojo parameter);

    List<AssetParameterPojo> getParameters(String templateId);

    List<AssetParameterPojo> getParameters(AssetTemplatePojo template);
}
