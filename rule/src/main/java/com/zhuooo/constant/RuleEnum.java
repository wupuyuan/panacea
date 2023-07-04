package com.zhuooo.constant;

public enum RuleEnum {
    SUBMITTER("1", "发起人", "发起人自己"),
    LEADER("2", "直属领导", "发起人的直属领导"),
    MANAGER("3", "部门经理", "部门经理"),
    ACCOUNTING("4", "会计", "发起人的直属领导"),
    ;

    private String code;
    private String name;
    private String desc;

    RuleEnum(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static RuleEnum getRule(String code) {
        for (RuleEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
