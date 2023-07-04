package com.zhuooo.constant;

public enum RuleItemTypeEnum {
    DEFAULT_DEPARTMENT(0, "发起人"),
    DEFAULT_PERSON(1, "发起部门人"),
    DEPARTMENT(2, "指定部门"),
    ROLE(3, "指定角色"),
    INSPECTION(4, "巡检"),
    指标(5, "指标"),

    ;

    private int code;
    private String desc;

    RuleItemTypeEnum(int code, String desc) {
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
