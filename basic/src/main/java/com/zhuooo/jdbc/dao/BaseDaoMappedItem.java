package com.zhuooo.jdbc.dao;

import java.lang.reflect.Field;

public class BaseDaoMappedItem {

    public BaseDaoMappedItem(String fieldName, String columnName, Field field) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.field = field;
    }

    /**
     * pojo中的字段名，驼峰
     */
    private String fieldName;

    /**
     * 数据库的列名，下划线
     */
    private String columnName;

    /**
     * pojo中的字段
     */
    private Field field;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
