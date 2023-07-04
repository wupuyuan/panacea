package com.zhuooo.jdbc.pojo;

import com.zhuooo.jdbc.annotations.Column;

public class BaseNamePojo extends BasePojo {

    /**
     * 名称
     */
    @Column
    private String name;

    /**
     * 说明
     */
    @Column
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
