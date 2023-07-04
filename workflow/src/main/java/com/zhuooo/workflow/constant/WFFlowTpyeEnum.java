package com.zhuooo.workflow.constant;

public enum WFFlowTpyeEnum {
    PARALLEL(1, "并行", "上游任一节点通过，即可到达当前节点"),
    SERIAL(2, "串行", "上游所有节点通过，才能到达当前节点"),
    ;

    private int code;
    private String name;
    private String desc;

    WFFlowTpyeEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
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
