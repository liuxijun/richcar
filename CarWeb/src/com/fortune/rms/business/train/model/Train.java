package com.fortune.rms.business.train.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 14-6-12.
 *
 */
public class Train extends BaseModel{
    private long id;
    private String name;
    private Integer trainLineId;
    private String sn;
    private String trainCode;
    private String description;
    private Integer status;
    private Integer type;

    public Train() {
    }

    public Train(long id, String name, Integer trainLineId, String sn, String trainCode, String description, Integer status, Integer type) {
        this.id = id;
        this.name = name;
        this.trainLineId = trainLineId;
        this.sn = sn;
        this.trainCode = trainCode;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTrainLineId() {
        return trainLineId;
    }

    public void setTrainLineId(Integer trainLineId) {
        this.trainLineId = trainLineId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
