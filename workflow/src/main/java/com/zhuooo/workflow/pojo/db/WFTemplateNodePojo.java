package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseDeletePojo;
import com.zhuooo.workflow.constant.WFFlowTpyeEnum;
import com.zhuooo.workflow.constant.WFNodeTypeEnum;

@Table("wf_template_node")
public class WFTemplateNodePojo extends BaseDeletePojo {

    /**
     * wf_template.id
     */
    @GroupId
    @Column
    private String templateId;

    /**
     * @see WFNodeTypeEnum#getCode()
     */
    @Column
    private int nodeType;

    /**
     * @see WFFlowTpyeEnum#getCode()
     */
    @Column
    private int flowType = -1;

    /**
     * 条件节点，判断表达式。 数字判断 money>100; 字符判断 id=='123'
     * 执行节点，回调表达式
     */
    @Column
    private String expression;

    /**
     * 前端展示座标
     */
    private String coordinate;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getFlowType() {
        return flowType;
    }

    public void setFlowType(int flowType) {
        this.flowType = flowType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
}
