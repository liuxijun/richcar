package com.fortune.rms.business.train.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 14-6-12.
 * 多媒体计算机
 */
public class ShowMachine extends BaseModel{
    private    long id;
    private String name;
    private String sn;
    private String position;
    private String trainName;
    private String trainId;
    private Integer status;
    private Integer type;

    public ShowMachine() {
    }

    public ShowMachine(long id, String name, String sn, String position, String trainName, String trainId, Integer status, Integer type) {
        this.id = id;
        this.name = name;
        this.sn = sn;
        this.position = position;
        this.trainName = trainName;
        this.trainId = trainId;
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
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
