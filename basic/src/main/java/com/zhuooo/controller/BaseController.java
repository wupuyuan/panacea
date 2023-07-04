package com.zhuooo.controller;

import com.zhuooo.jdbc.pojo.BaseOperationPojo;

public class BaseController {
    protected void setOperator(BaseOperationPojo pojo) {
        pojo.setCreatePerson("test");
        pojo.setUpdatePerson("test");
    }
}
