package com.zhuooo.jdbc.pojo;

import com.zhuooo.jdbc.annotations.Column;

import java.sql.Timestamp;

public class BaseOperationPojo extends BaseDeletePojo {

    /**
     * 创建时间
     */
    @Column
    private Timestamp createTime;

    /**
     * 创建人
     */
    @Column
    private String createPerson;

    /**
     * 更新时间
     */
    @Column
    private Timestamp updateTime;

    /**
     * 更新人
     */
    @Column
    private String updatePerson;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

}
