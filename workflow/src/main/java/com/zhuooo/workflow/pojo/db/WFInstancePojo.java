package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;
import com.zhuooo.workflow.constant.WFStatusEnum;

import java.util.Collection;
import java.util.List;

@Table("wf_instance")
public class WFInstancePojo extends BaseOperationPojo {

    /**
     * wf_template.id
     */
    @GroupId
    @Column
    private String templateId;

    /**
     * 实例key, token
     */
    @Column
    private String instanceKey;

    /**
     * @see WFStatusEnum#getCode()
     */
    @Column
    private Integer status;

    private Collection<WFInstanceNodePojo> nodes;

    private Collection<WFInstanceLinePojo> lines;

    private Collection<WFArgumentPojo> args;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getInstanceKey() {
        return instanceKey;
    }

    public void setInstanceKey(String instanceKey) {
        this.instanceKey = instanceKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Collection<WFInstanceNodePojo> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<WFInstanceNodePojo> nodes) {
        this.nodes = nodes;
    }

    public Collection<WFInstanceLinePojo> getLines() {
        return lines;
    }

    public void setLines(Collection<WFInstanceLinePojo> lines) {
        this.lines = lines;
    }

    public Collection<WFArgumentPojo> getArgs() {
        return args;
    }

    public void setArgs(Collection<WFArgumentPojo> args) {
        this.args = args;
    }
}
