package com.zhuooo.workflow.constant;

/**
 * 节点类型
 */
public enum WFNodeTypeEnum {
    START(1, "起点"),
    END(2, "终点"),
    CONDITION(3, "条件节点"),
    COUNTER_SIGN(4, "会签节点"),
    ALL_SIGN(5, "汇签节点"),
    DELEGATE(6, "加签节点");

    private int code;
    private String desc;

    WFNodeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WFNodeTypeEnum getByCode(int code) {
        for (WFNodeTypeEnum typeEnum : WFNodeTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum;
            }
        }
        return null;
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
