package com.zhuooo.workflow.constant;

public enum WFTypeEnum {
    APPLY(1, "申领"),
    ORDER(2, "工单"),
    ;

    private int code;
    private String desc;

    WFTypeEnum(int code, String desc) {
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
