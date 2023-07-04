package com.zhuooo.jdbc.pojo;

import com.zhuooo.constant.DeleteEnum;
import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.Deleted;

public class BaseDeletePojo extends BaseNamePojo {
    /**
     * @see DeleteEnum#getCode()
     */
    @Deleted
    @Column
    private int deleted;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
