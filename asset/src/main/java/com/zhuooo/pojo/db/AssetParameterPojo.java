package com.zhuooo.pojo.db;

import com.zhuooo.constant.ParameterRequiredEnum;
import com.zhuooo.constant.ParameterTypeEnum;
import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseNamePojo;

@Table("asset_parameter")
public class AssetParameterPojo extends BaseNamePojo {

    @GroupId
    @Column
    private String templateId;

    /**
     * 形参类型
     *
     * @see ParameterTypeEnum#getCode()
     */
    @Column
    private int type;

    /**
     * 是否必填
     *
     * @see ParameterRequiredEnum#getCode()
     */
    @Column
    private int required;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

}
