package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.pojo.BaseOperationPojo;

public class ObjOrganPojo extends BaseOperationPojo {
    private String parentId;

    private int type;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
