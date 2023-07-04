package com.zhuooo.workflow.pojo.vo;

import java.io.Serializable;
import java.util.Map;

public class WFRequest implements Serializable {

    /**
     * 某某发起的工作流
     */
    private String name;
    private String templateKey;
    private String instanceKey;

    private String nodeId;

    private String description;

    /**
     * parameter - argument
     */
    private Map<String, String> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getInstanceKey() {
        return instanceKey;
    }

    public void setInstanceKey(String instanceKey) {
        this.instanceKey = instanceKey;
    }


    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
