package com.zhuooo.constant;

public enum DeleteEnum {
    YES(1, "删除"),
    NO(0, "未删除"),
    ;

    private int code;
    private String desc;

    DeleteEnum(int code, String desc) {
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
