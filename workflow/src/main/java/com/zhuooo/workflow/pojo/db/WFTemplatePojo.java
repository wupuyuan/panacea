package com.zhuooo.workflow.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;
import com.zhuooo.workflow.constant.WFTypeEnum;

import java.util.List;

@Table("wf_template")
public class WFTemplatePojo extends BaseOperationPojo {

    /**
     * 业务key，唯一校验
     */
    @Column
    private String templateKey;

    /**
     * 流程版本
     */
    @Column
    private int version;

    /**
     * @see WFTypeEnum#getCode()
     */
    @Column
    private int type;

    /**
     * 节点
     */
    private List<WFTemplateNodePojo> nodes;

    /**
     * 连接线
     */
    private List<WFTemplateLinePojo> lines;

    /**
     * 启动参数
     */
    private List<WFParameterPojo> parameters;

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<WFTemplateNodePojo> getNodes() {
        return nodes;
    }

    public void setNodes(List<WFTemplateNodePojo> nodes) {
        this.nodes = nodes;
    }

    public List<WFTemplateLinePojo> getLines() {
        return lines;
    }

    public void setLines(List<WFTemplateLinePojo> lines) {
        this.lines = lines;
    }

    public List<WFParameterPojo> getParameters() {
        return parameters;
    }

    public void setParameters(List<WFParameterPojo> parameters) {
        this.parameters = parameters;
    }
}
