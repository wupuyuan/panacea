package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.pojo.BasePojo;

public class UserParameterPojo extends BasePojo {

    private String userId;
    private String description;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
