package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.pojo.BaseNamePojo;

public class UserArgumentPojo extends BaseNamePojo {

    private String userId;

    /**
     * user_parameter.id
     */
    private String parameterId;

    private String value;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
