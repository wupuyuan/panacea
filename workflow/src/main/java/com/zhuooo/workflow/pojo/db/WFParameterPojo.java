package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BasePojo;
import com.zhuooo.constant.ParameterTypeEnum;

@Table("wf_parameter")
public class WFParameterPojo extends BasePojo {
    /**
     * wf_template.id
     */
    @GroupId
    @Column
    private String templateId;

    /**
     * wf_template_node.id
     */
    @Column
    private String templateNodeId;

    /**
     * 形参key
     */
    @Column
    private String parameterKey;

    /**
     * 形参类型
     *
     * @see ParameterTypeEnum#getCode()
     */
    @Column
    private int parameterType;

    /**
     * 形参说明
     */
    @Column
    private String description;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateNodeId() {
        return templateNodeId;
    }

    public void setTemplateNodeId(String templateNodeId) {
        this.templateNodeId = templateNodeId;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public int getParameterType() {
        return parameterType;
    }

    public void setParameterType(int parameterType) {
        this.parameterType = parameterType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
