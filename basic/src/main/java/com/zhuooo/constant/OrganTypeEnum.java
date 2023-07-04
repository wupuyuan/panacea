package com.zhuooo.constant;

public enum OrganTypeEnum {
    COUNTRY(-1, "全国"),
    ROOT(0, "总公司"),
    COMPANY(1, "子公司"),
    DEPARTMENT(2, "部门"),
    PRECINCT(3, "管理区"),
    area(4, "区域"),
    ;

    private int code;
    private String desc;

    OrganTypeEnum(int code, String desc) {
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
