package com.zhuooo.pojo.vo;

import com.zhuooo.jdbc.pojo.BaseNamePojo;

public class UserVo extends BaseNamePojo {
    public UserVo() {
    }

    public UserVo(String id, String name) {
        setId(id);
        setName(name);
    }

}
