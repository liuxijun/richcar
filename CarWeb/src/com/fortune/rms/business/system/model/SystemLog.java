package com.fortune.rms.business.system.model;

import java.util.Date;

/**
 * SystemLog generated by hbm2java
 */
public class SystemLog implements java.io.Serializable {

	private long id;
	private String systemLogAction;
	private Long adminId;
	private String adminIp;
	private Date logTime;
	private String log;
    private String adminName;

	public SystemLog() {
	}

	public SystemLog(long id) {
		this.id = id;
	}
	public SystemLog(long id, String systemLogAction, Long adminId,
			String adminIp, Date logTime, String log) {
		this.id = id;
		this.systemLogAction = systemLogAction;
		this.adminId = adminId;
		this.adminIp = adminIp;
		this.logTime = logTime;
		this.log = log;
	}

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getSystemLogAction() {
		return this.systemLogAction;
	}

	public void setSystemLogAction(String systemLogAction) {
		this.systemLogAction = systemLogAction;
	}
	public Long getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public String getAdminIp() {
		return this.adminIp;
	}

	public void setAdminIp(String adminIp) {
		this.adminIp = adminIp;
	}
	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
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

}