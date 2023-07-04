package com.zhuooo.pojo.vo;

import com.zhuooo.pojo.db.AssetParameterPojo;
import com.zhuooo.pojo.db.AssetTemplatePojo;

import java.io.Serializable;
import java.util.List;

public class AssetTemplateVo implements Serializable {

    private AssetTemplatePojo template;

    private List<AssetParameterPojo> parameters;

    public AssetTemplatePojo getTemplate() {
        return template;
    }

    public void setTemplate(AssetTemplatePojo template) {
        this.template = template;
    }

    public List<AssetParameterPojo> getParameters() {
        return parameters;
    }

    public void setParameters(List<AssetParameterPojo> parameters) {
        this.parameters = parameters;
    }
}
