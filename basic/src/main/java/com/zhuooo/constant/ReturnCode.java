package com.zhuooo.constant;

public enum ReturnCode {
    SUCCESS(0, "成功"),
    UNKNOWN(-1, "未知错误"),
    BASE_DAO_STATEMENT(50, "BASE_DAO_STATEMENT 解析错误"),
    BASE_DAO_EXECUTE(51, "BASE_DAO 执行错误"),
    BASE_DAO_DUPLICATE(52, "BASE_DAO 执行错误"),
    BASE_DAO_PRARM(53, "BASE_DAO 入参错误"),
    BASE_DAO_LOAD_CACHE(54, "BASE_CACHE_DAO 加载缓存错误"),
    DAO_MULTI_DATA(60, "查询重复数据"),
    DAO_EMPTY_DATA(61, "查询空数据"),

    PARAM_NULL(101, "参数为空"),
    PARAM_ILLEGALITY(101, "参数非法"),
    PARAM_REQUIRED(102, "必填项为空"),


    // 500 ~ 600 工作流错误
    WORKFLOW_ALREADY(500, "当前节点已经审批"),
    WORKFLOW_PERMISSIONS(501, "无权审批"),
    WORKFLOW_NEXT(502, "创建下游节点失败"),
    WORKFLOW_ILLEGALITY(503, "流程模板不合法"),
    WORKFLOW_FINISHED(504, "流程已完结"),
    WORKFLOW_DELEGATE(505, "加签失败"),
    WORKFLOW_CANDIDATE(506, "没有找到该节点的候选人"),

    // 1000
    RULE_EMPTY(1001,"没有对应的规则"),

    // 5000
    LOGIN_USER(2001,"用户未登录"),


    ;

    // 1000
    ;
    private int code;

    private String info;

    ReturnCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
