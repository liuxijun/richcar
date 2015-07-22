package com.fortune.rms.business.log.model;

import java.util.Date;

/**
 * VisitLog generated by hbm2java
 */
public class VisitLog implements java.io.Serializable {

	private long id;
	private Long spId;
	private Long cpId;
	private Long channelId;
	private Long contentId;
	private Long contentPropertyId;
	private String url;
	private String userId;
	private String userIp;
	private Long areaId;
	private Long isFree;
	private Date startTime;
	private Date endTime;
	private Long length;
    private String playerVersion;
    private String UserAgent;
    private Long status;
    private Long avgBandwidth;
    private String sIp;
    private Long bytesSend;
	public VisitLog() {
	}

	public VisitLog(long id) {
		this.id = id;
	}

    public VisitLog(long id, Long spId, Long cpId, Long channelId, Long contentId, Long contentPropertyId, String url,
                    String userId, String userIp, Long areaId, Long free, Date startTime, Date endTime, Long length,
                    String playerVersion, String userAgent, Long status, Long avgBandwidth, String sIp,long bytesSend) {
        this.id = id;
        this.spId = spId;
        this.cpId = cpId;
        this.channelId = channelId;
        this.contentId = contentId;
        this.contentPropertyId = contentPropertyId;
        this.url = url;
        this.userId = userId;
        this.userIp = userIp;
        this.areaId = areaId;
        isFree = free;
        this.startTime = startTime;
        this.endTime = endTime;
        this.length = length;
        this.playerVersion = playerVersion;
        UserAgent = userAgent;
        this.status = status;
        this.avgBandwidth = avgBandwidth;
        this.sIp = sIp;
        this.bytesSend = bytesSend;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getSpId() {
		return this.spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public Long getCpId() {
		return this.cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}
	public Long getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getContentPropertyId() {
		return this.contentPropertyId;
	}

	public void setContentPropertyId(Long contentPropertyId) {
		this.contentPropertyId = contentPropertyId;
	}
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Long getIsFree() {
		return this.isFree;
	}

	public void setIsFree(Long isFree) {
		this.isFree = isFree;
	}
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getLength() {
		return this.length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

    public Long getFree() {
        return isFree;
    }

    public void setFree(Long free) {
        isFree = free;
    }

    public String getPlayerVersion() {
        return playerVersion;
    }

    public void setPlayerVersion(String playerVersion) {
        this.playerVersion = playerVersion;
    }

    public String getUserAgent() {
        return UserAgent;
    }

    public void setUserAgent(String userAgent) {
        UserAgent = userAgent;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getAvgBandwidth() {
        return avgBandwidth;
    }

    public void setAvgBandwidth(Long avgBandwidth) {
        this.avgBandwidth = avgBandwidth;
    }

    public String getsIp() {
        return sIp;
    }

    public void setsIp(String sIp) {
        this.sIp = sIp;
    }

    public Long getBytesSend() {
        return bytesSend;
    }

    public void setBytesSend(Long bytesSend) {
        this.bytesSend = bytesSend;
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
