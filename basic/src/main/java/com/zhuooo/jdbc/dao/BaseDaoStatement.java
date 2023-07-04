package com.zhuooo.jdbc.dao;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.jdbc.pojo.BasePojo;

import java.util.ArrayList;
import java.util.List;

public class BaseDaoStatement {

    private String className;

    private Class clazz;

    private String tableName;
    private BaseDaoMappedItem idMappedItem;

    private BaseDaoMappedItem groupMappedItem;

    private BaseDaoMappedItem deletedMappedItem;

    private BaseDaoMappedItem parentMapperItem;

    private String selectOneSql;

    private String selectGroupSql;

    private String selectAllSql;

    private String selectChildrenSql;

    private String insertSql;

    private String deleteOneSql;

    private String deleteGroupSql;


    private String softDeleteOneSql;

    private List<BaseDaoMappedItem> mappedItems = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public BaseDaoMappedItem getIdMappedItem() {
        return idMappedItem;
    }

    public void setIdMappedItem(BaseDaoMappedItem idMappedItem) {
        this.idMappedItem = idMappedItem;
    }

    public BaseDaoMappedItem getGroupMappedItem() {
        return groupMappedItem;
    }

    public void setGroupMappedItem(BaseDaoMappedItem groupMappedItem) {
        this.groupMappedItem = groupMappedItem;
    }

    public BaseDaoMappedItem getDeletedMappedItem() {
        return deletedMappedItem;
    }

    public void setDeletedMappedItem(BaseDaoMappedItem deletedMappedItem) {
        this.deletedMappedItem = deletedMappedItem;
    }

    public BaseDaoMappedItem getParentMapperItem() {
        return parentMapperItem;
    }

    public void setParentMapperItem(BaseDaoMappedItem parentMapperItem) {
        this.parentMapperItem = parentMapperItem;
    }

    public String getSelectOneSql() {
        return selectOneSql;
    }

    public void setSelectOneSql(String selectOneSql) {
        this.selectOneSql = selectOneSql;
    }

    public String getSelectGroupSql() {
        return selectGroupSql;
    }

    public void setSelectGroupSql(String selectGroupSql) {
        this.selectGroupSql = selectGroupSql;
    }

    public String getSelectAllSql() {
        return selectAllSql;
    }

    public void setSelectAllSql(String selectAllSql) {
        this.selectAllSql = selectAllSql;
    }

    public String getSelectChildrenSql() {
        return selectChildrenSql;
    }

    public void setSelectChildrenSql(String selectChildrenSql) {
        this.selectChildrenSql = selectChildrenSql;
    }

    public String getDeleteOneSql() {
        return deleteOneSql;
    }

    public void setDeleteOneSql(String deleteOneSql) {
        this.deleteOneSql = deleteOneSql;
    }

    public String getDeleteGroupSql() {
        return deleteGroupSql;
    }

    public void setDeleteGroupSql(String deleteGroupSql) {
        this.deleteGroupSql = deleteGroupSql;
    }

    public String getSoftDeleteOneSql() {
        return softDeleteOneSql;
    }

    public void setSoftDeleteOneSql(String softDeleteOneSql) {
        this.softDeleteOneSql = softDeleteOneSql;
    }

    public List<BaseDaoMappedItem> getMappedItems() {
        return mappedItems;
    }

    public void setMappedItems(List<BaseDaoMappedItem> mappedItems) {
        this.mappedItems = mappedItems;
    }

    public void addMappedItem(BaseDaoMappedItem mappedItem) {
        this.mappedItems.add(mappedItem);
    }

    public String getId(Object obj) {
        return getValue(idMappedItem, obj);
    }

    public String getGroupId(Object obj) {
        return getValue(groupMappedItem, obj);
    }

    public String getParentId(Object obj) {
        return getValue(parentMapperItem, obj);
    }

    private String getValue(BaseDaoMappedItem mappedItem, Object obj) {
        String ret = null;
        if (mappedItem != null && obj != null) {
            try {
                ret = (String) mappedItem.getField().get(obj);
            } catch (IllegalAccessException e) {
                throw new ZhuoooException(ReturnCode.BASE_DAO_LOAD_CACHE);
            }
        }
        return ret;
    }
}
