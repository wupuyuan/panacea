package com.zhuooo.workflow.constant;

/**
 * 工作流实例状态
 *
 * @author wupuyuan
 * @Date 2020-08-20 14:17
 */
public enum WFStatusEnum {
    DRAFT(0, "草稿"),
    RUNNING(1, "审批中"),
    REJECTED(2, "被驳回"),
    FINISHED(3, "结束"),
    ERROR(-1, "异常"),
    CANCEL(-2, "作废"),
    ;

    //    0:初始化()；1:审批中；2:驳回；3:结束；4:废弃
    private int code;
    private String desc;

    WFStatusEnum(int code, String desc) {
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
