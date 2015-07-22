package com.fortune.rms.business.syn.model;


import com.fortune.rms.business.system.model.Device;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-6-20
 * Time: 10:13:31
 *
 */
public class SynTask implements java.io.Serializable {

    private long id;
    private Long deviceId;
    private Long synFileId;
    private Long synStatus;
    private Date startTime;
    private Device device;
    private SynFile synFile;
    private Long startPos;
    private Long endPos;
    private Long synLevel;

    public SynTask() {
    }

    public SynTask(long id) {
        this.id = id;
    }

    public SynTask(long id, Long deviceId, Long synFileId, Long synStatus, Date startTime, Device device, SynFile synFile, Long startPos, Long endPos, Long synLevel) {
        this.id = id;
        this.deviceId = deviceId;
        this.synFileId = synFileId;
        this.synStatus = synStatus;
        this.startTime = startTime;
        this.device = device;
        this.synFile = synFile;
        this.startPos = startPos;
        this.endPos = endPos;
        this.synLevel = synLevel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Long synStatus) {
        this.synStatus = synStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
	 * toString return json format string of this bean
	 * @return String
	 */
	public String toString() {
		return com.fortune.util.JsonUtils.getJsonString(this);
	}

	@SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
	public String getSimpleJson() {
		return com.fortune.util.JsonUtils.getJsonString(this, null);
	}

	@SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
	public String getObjJson() {
		return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
	}
    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
   @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
    public SynFile getSynFile() {
        return synFile;
    }

    public void setSynFile(SynFile synFile) {
        this.synFile = synFile;
    }

    public Long getSynFileId() {
        return synFileId;
    }

    public void setSynFileId(Long synFileId) {
        this.synFileId = synFileId;
    }

    public Long getStartPos() {
        return startPos;
    }

    public void setStartPos(Long startPos) {
        this.startPos = startPos;
    }

    public Long getEndPos() {
        return endPos;
    }

    public void setEndPos(Long endPos) {
        this.endPos = endPos;
    }

    public Long getSynLevel() {
        return synLevel;
    }

    public void setSynLevel(Long synLevel) {
        this.synLevel = synLevel;
    }
}
