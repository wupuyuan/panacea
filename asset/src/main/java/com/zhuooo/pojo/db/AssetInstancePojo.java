package com.zhuooo.pojo.db;

import com.zhuooo.jdbc.annotations.Column;
import com.zhuooo.jdbc.annotations.GroupId;
import com.zhuooo.jdbc.annotations.Table;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;

@Table("asset_instance")
public class AssetInstancePojo extends BaseOperationPojo {

    /**
     * asset_template.id
     */
    @GroupId
    @Column
    private String templateId;

    @Column
    private String rfId;

    @Column
    private String phyId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRfId() {
        return rfId;
    }

    public void setRfId(String rfId) {
        this.rfId = rfId;
    }

    public String getPhyId() {
        return phyId;
    }

    public void setPhyId(String phyId) {
        this.phyId = phyId;
    }
}
