package com.zhuooo.workflow.constant;

public enum WFConstantEnum {
    INITIATOR("initiator", "发起人"),
    CANDIDATE("candidate", "候选人"),
    HANDLER("handler", "处理人"),
    SUBMITTER("submitter", "提交人"),
    CLIENT("client", "委托人"),
    DELEGATE("delegate", "加签人"),
    TRUSTEE("trustee", "受托人"),
    ROOT("root", "流程起始节点"),

    ;

    private String code;
    private String desc;

    WFConstantEnum(String code, String desc) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
