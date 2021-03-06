package com.fortune.rms.business.portal.model;

import java.util.Date;

/**
 * UserScoring generated by hbm2java
 */
public class UserScoring implements java.io.Serializable {

	private long id;
	private Long cspId;
	private Long contentId;
	private String userId;
	private String userIp;
	private Integer score;
	private Long status;
	private Date time;

    private String   cspName;
    private String contentName;

	public UserScoring() {
	}

	public UserScoring(long id) {
		this.id = id;
	}

    public UserScoring(Date time, long id, Long cspId, Long contentId, String userId, String userIp, Integer score, Long status) {
        this.time = time;
        this.id = id;
        this.cspId = cspId;
        this.contentId = contentId;
        this.userId = userId;
        this.userIp = userIp;
        this.score = score;
        this.status = status;
    }

    public String getCspName(){
        return cspName;
    }

    public void setCspName(String cspName){
       this.cspName=cspName;    
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
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
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
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
