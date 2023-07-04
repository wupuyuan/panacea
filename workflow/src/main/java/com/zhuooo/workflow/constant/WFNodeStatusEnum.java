package com.zhuooo.workflow.constant;

public enum WFNodeStatusEnum {
    PENDING(0, "待审核"),
    APPROVE(1, "通过"),
    REJECT(2, "驳回"),
    ERROR(-1, "异常"),
    CANCEL(-2, "作废"),
    ;

    private int code;
    private String desc;

    WFNodeStatusEnum(int code, String desc) {
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
