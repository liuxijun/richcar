package com.fortune.rms.business.train.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 14-6-12.
 * 列车线路信息
 */
public class TrainLine  extends BaseModel{
    private long id;
    private Integer parentId;
    private String name;
    private String picture;
    private String description;
    private Integer type;
    private Integer status;
    private Boolean leaf;

    public TrainLine() {
    }

    public TrainLine(long id, Integer parentId, String name, String picture, String description, Integer type, Integer status) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }
}
