package com.fortune.cars.business.cars.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 2015/9/3.
 *
 */
public class CarItem extends BaseModel{
    private String fieldLabel;
    private String name;
    private String value;
    private String readOnly;
    private boolean allowBlank;
    private String type;
    public CarItem() {
    }

    public CarItem(String name, String fieldLabel, boolean allowBlank, String type, String value) {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.value = value;
        this.allowBlank = allowBlank;
        this.type = type;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }
}
