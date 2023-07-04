package com.zhuooo.constant;

public enum RuleTypeEnum {

    OR(1, "交集"),
    AND(2, "并集"),
    ;

    private int code;
    private String desc;

    RuleTypeEnum(int code, String desc) {
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
