package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseDeletePojo;
import com.zhuooo.jdbc.pojo.BasePojo;
import com.zhuooo.workflow.constant.WFLineTypeEnum;

@Table("wf_template_line")
public class WFTemplateLinePojo extends BasePojo {
    /**
     * wf_template.id
     */
    @GroupId
    @Column
    private String templateId;

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

    /**
     * 如果前置节点不是条件节点，则为0无意义，如果前置节点是条件节点需要判断
     *
     * @see WFLineTypeEnum#getCode()
     */
    @Column
    private int type;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
