package com.zhuooo.constant;

public enum ParameterRequiredEnum {
    YES(1, "必填"),
    NO(0, "非必填"),
    ;

    private int code;
    private String desc;

    ParameterRequiredEnum(int code, String desc) {
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
