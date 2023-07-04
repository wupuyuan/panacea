package com.zhuooo.pojo;

import com.zhuooo.constant.RuleItemTypeEnum;
import com.zhuooo.jdbc.pojo.BasePojo;


public class ObjRuleItemPojo extends BasePojo {


    /**
     * 规则编号
     */
    private String ruleId;

    /**
     * @see RuleItemTypeEnum#getCode()
     */
    private int type;

    private String value;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
