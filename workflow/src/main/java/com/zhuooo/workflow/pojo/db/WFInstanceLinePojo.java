package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseDeletePojo;
import com.zhuooo.jdbc.pojo.BasePojo;

@Table("wf_instance_line")
public class WFInstanceLinePojo extends BasePojo {

    public WFInstanceLinePojo(String templateId, String templateLineId, String instanceId, String prevId, String nextId) {
        this.templateId = templateId;
        this.templateLineId = templateLineId;
        this.instanceId = instanceId;
        this.prevId = prevId;
        this.nextId = nextId;
    }

    public WFInstanceLinePojo() {

    }

    /**
     * wf_template.id
     */
    @Column
    private String templateId;

    /**
     * wf_template_node.id
     */
    @Column
    private String templateLineId;

    /**
     * wf_instance.id
     */
    @GroupId
    @Column
    private String instanceId;

    /**
     * 前置节点
     */
    @Column
    private String prevId;

    /**
     * 后置节点
     */
    @Column
    private String nextId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateLineId() {
        return templateLineId;
    }

    public void setTemplateLineId(String templateLineId) {
        this.templateLineId = templateLineId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPrevId() {
        return prevId;
    }

    public void setPrevId(String prevId) {
        this.prevId = prevId;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }
}
