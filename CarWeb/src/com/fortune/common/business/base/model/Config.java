package com.fortune.common.business.base.model;

/**
 * Created by xjliu on 2015/3/13.
 * ≈‰÷√–≈œ¢
 */

public class Config extends BaseModel {
    private String name;
    private String value;
    private String source;
    private String description;

    public Config() {
    }

    public Config(String name, String value,String source, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
