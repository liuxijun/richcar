package com.fortune.common.business.base.model;

import java.util.List;

/**
 * Created by xjliu on 2015/7/25.
 * ×Öµä
 */
public class Dictionary extends BaseModel {
    private String code;
    private String name;
    private String desp;
    private String parentCode;
    private List<Dictionary> items;
    public Dictionary() {
    }

    public Dictionary(String code) {
        this.code = code;
    }

    public Dictionary(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public List<Dictionary> getItems() {
        return items;
    }

    public void setItems(List<Dictionary> items) {
        this.items = items;
    }
}
