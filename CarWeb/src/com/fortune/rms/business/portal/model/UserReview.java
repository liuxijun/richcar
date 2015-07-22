package com.fortune.rms.business.portal.model;

import java.util.Date;

/**
 * UserReview generated by hbm2java
 */
public class UserReview implements java.io.Serializable {

	private long id;
	private Long cspId;
	private Long contentId;
	private String userId;
	private String userIp;
	private String desp;
    private String contentName;
	private Long status;
	private Date time;
	private Long referId;
    private String cspName;

	public UserReview() {
	}

	public UserReview(long id) {
		this.id = id;
	}
	public UserReview(long id, Long cspId, Long contentId, String userId,
			String userIp, String desp, Long status, Date time, Long referId) {
		this.id = id;
		this.cspId = cspId;
		this.contentId = contentId;
		this.userId = userId;
		this.userIp = userIp;
		this.desp = desp;
		this.status = status;
		this.time = time;
		this.referId = referId;
	}


    public String getCspName() {
        return cspName;
    }

    public void setCspName(String cspName) {
        this.cspName = cspName;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getCspId() {
		return this.cspId;
	}

	public void setCspId(Long cspId) {
		this.cspId = cspId;
	}
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getDesp() {
		return this.desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	public Long getReferId() {
		return this.referId;
	}

	public void setReferId(Long referId) {
		this.referId = referId;
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

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }
}
