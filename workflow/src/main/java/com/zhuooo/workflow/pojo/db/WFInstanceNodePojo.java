package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;
import com.zhuooo.workflow.constant.WFNodeStatusEnum;
import com.zhuooo.workflow.constant.WFNodeTypeEnum;

import java.util.List;

@Table("wf_instance_node")
public class WFInstanceNodePojo extends BaseOperationPojo {

    /**
     * wf_template.id
     */
    @Column
    private String templateId;

    /**
     * wf_template_node.id
     */
    @Column
    private String templateNodeId;

    /**
     * wf_instance.id
     */
    @GroupId
    @Column
    private String instanceId;

    /**
     * @see WFNodeTypeEnum#getCode()
     */
    @Column
    private int nodeType;

    /**
     * @see WFNodeStatusEnum#getCode()
     */
    @Column
    private int status;

    List<WFArgumentPojo> arguments;

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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<WFArgumentPojo> getArguments() {
        return arguments;
    }

    public void setArguments(List<WFArgumentPojo> arguments) {
        this.arguments = arguments;
    }
}
