package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.pojo.BasePojo;

public class RefUserOrganPojo extends BasePojo {

    private String userId;

    private String organId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }
}
