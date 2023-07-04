package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.pojo.BasePojo;


public class ObjRulePojo extends BasePojo {

    private String parentId;

    private String type;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
