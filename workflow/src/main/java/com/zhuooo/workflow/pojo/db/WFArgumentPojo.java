package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BasePojo;
import com.zhuooo.constant.ParameterTypeEnum;

@Table("wf_argument")
public class WFArgumentPojo extends BasePojo {


    public WFArgumentPojo() {

    }

    public WFArgumentPojo(String instanceId, String instanceNodeId, String argumentKey, int argumentType, String argumentValue, String description) {
        this.instanceId = instanceId;
        this.instanceNodeId = instanceNodeId;
        this.argumentKey = argumentKey;
        this.argumentType = argumentType;
        this.argumentValue = argumentValue;
        this.description = description;
    }

    /**
     * wf_instance.id
     */
    @GroupId
    @Column
    private String instanceId;

    /**
     * wf_instance_node.id
     */
    @Column
    private String instanceNodeId;

    /**
     * @see WFParameterPojo#getParameterKey() ()
     */
    @Column
    private String argumentKey;

    /**
     * @see ParameterTypeEnum#getCode()
     */
    @Column
    private int argumentType;

    @Column
    private String argumentValue;

    @Column
    private String description;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceNodeId() {
        return instanceNodeId;
    }

    public void setInstanceNodeId(String instanceNodeId) {
        this.instanceNodeId = instanceNodeId;
    }

    public int getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(int argumentType) {
        this.argumentType = argumentType;
    }

    public String getArgumentKey() {
        return argumentKey;
    }

    public void setArgumentKey(String argumentKey) {
        this.argumentKey = argumentKey;
    }

    public String getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
