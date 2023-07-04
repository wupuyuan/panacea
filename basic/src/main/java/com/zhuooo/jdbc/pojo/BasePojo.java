package com.zhuooo.jdbc.pojo;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.ID;

import java.io.Serializable;

public class BasePojo implements Serializable {
    /**
     * 主键，uuid 32位
     */
    @ID
    @Column
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
