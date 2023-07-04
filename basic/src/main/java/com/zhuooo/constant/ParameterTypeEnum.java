package com.zhuooo.constant;

public enum ParameterTypeEnum {
    STRING(1, "字符串"),
    NUMBER(2, "数字"),
    TIME(3, "时间"),
    FILE(4, "文件"),
    ASSET(5, "资产"),
    COMPANY(6, "公司"),
    ROLE(7, "角色"),
    RANGE(8, "范围"),
    ;

    private int code;
    private String desc;

    ParameterTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
