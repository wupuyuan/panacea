package com.zhuooo.pojo.vo;

import com.zhuooo.pojo.db.AssetInstancePojo;
import com.zhuooo.pojo.db.AssetTemplatePojo;
import com.zhuooo.response.ZhuoooFieldValue;

import java.util.List;
import java.util.Map;

public class AssetInstanceVo<P, A> {

    private AssetInstancePojo instance;

    private AssetTemplatePojo template;

    private Map<String, String> values;

    public AssetInstancePojo getInstance() {
        return instance;
    }

    public void setInstance(AssetInstancePojo instance) {
        this.instance = instance;
    }

    public AssetTemplatePojo getTemplate() {
        return template;
    }

    public void setTemplate(AssetTemplatePojo template) {
        this.template = template;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
