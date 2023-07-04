package com.zhuooo.workflow.constant;

public enum WFLineTypeEnum {
    TRUE(1, "满足条件的分支"),
    FALSE(-1, "不满足条件的分支"),
    DEFAULT(0, "默认无意义"),
    ;

    private int code;
    private String desc;

    WFLineTypeEnum(int code, String desc) {
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


    public static WFLineTypeEnum getLineTypeEnum(boolean bool) {
        if (bool) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
