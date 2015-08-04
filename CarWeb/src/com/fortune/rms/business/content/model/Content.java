package com.fortune.rms.business.content.model;

import com.fortune.rms.business.user.model.BookMark;

import java.util.*;

/**
 * Content generated by hbm2java
 */
public class Content implements java.io.Serializable {

	private long id;
	private String name;
	private String actors;
	private String directors;
	private Long creatorAdminId;
	private Date createTime;
	private Long cspId;
	private Long moduleId;
	private Long deviceId;
	private Long status;
	private Date statusTime;
	private Long contentAuditId;
	private String digiRightUrl;
	private Date validStartTime;
	private Date validEndTime;
	private Long allVisitCount;
	private Long monthVisitCount;
	private Long weekVisitCount;
    private Double score;
    private Long scoreCount;
	private String intro;
	private String post1Url;
	private String post2Url;
	private Long property1;
	private Long property2;
	private String property3;
	private String property4;
	private String property5;
	private String property6;
	private String property7;
	private String property8;
    private String contentId;
    private Long isSpecial;
    private Long scoreStatus;
    private Long visitCountStatus;
    private String userTypes;
    private Map<String,Object> properties=new HashMap<String,Object>();
    private BookMark bookMark=new BookMark();
	public Content() {
	}

	public Content(long id) {
		this.id = id;
	}

    public Content(long id, String name, String actors, String directors,
                   Long creatorAdminId, Date createTime, Long cspId, Long moduleId, Long deviceId,
                   Long status, Date statusTime, Long contentAuditId, String digiRightUrl, Date validStartTime,
                   Date validEndTime, Long allVisitCount, Long monthVisitCount, Long weekVisitCount, Double score,
                   Long scoreCount, String intro, String post1Url, String post2Url, Long property1, Long property2,
                   String property3, String property4, String property5, String property6, String property7,
                   String property8,String userTypes,  String contentId, Long isSpecial, Long scoreStatus, Long visitCountStatus) {
        this.userTypes = userTypes;
        this.id = id;
        this.name = name;
        this.actors = actors;
        this.directors = directors;
        this.creatorAdminId = creatorAdminId;
        this.createTime = createTime;
        this.cspId = cspId;
        this.moduleId = moduleId;
        this.deviceId = deviceId;
        this.status = status;
        this.statusTime = statusTime;
        this.contentAuditId = contentAuditId;
        this.digiRightUrl = digiRightUrl;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.allVisitCount = allVisitCount;
        this.monthVisitCount = monthVisitCount;
        this.weekVisitCount = weekVisitCount;
        this.score = score;
        this.scoreCount = scoreCount;
        this.intro = intro;
        this.post1Url = post1Url;
        this.post2Url = post2Url;
        this.property1 = property1;
        this.property2 = property2;
        this.property3 = property3;
        this.property4 = property4;
        this.property5 = property5;
        this.property6 = property6;
        this.property7 = property7;
        this.property8 = property8;
        this.contentId = contentId;
        this.isSpecial = isSpecial;
        this.scoreStatus = scoreStatus;
        this.visitCountStatus = visitCountStatus;
    }

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getActors() {
		return this.actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getDirectors() {
		return this.directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public Long getCreatorAdminId() {
		return this.creatorAdminId;
	}

	public void setCreatorAdminId(Long creatorAdminId) {
		this.creatorAdminId = creatorAdminId;
	}
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCspId() {
		return this.cspId;
	}

    public void setCspId(Long cspId) {
		this.cspId = cspId;
	}
	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public Date getStatusTime() {
		return this.statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}
	public Long getContentAuditId() {
		return this.contentAuditId;
	}

    public Long getIsSpecial() {
        return this.isSpecial;
    }

    public void setIsSpecial(Long isSpecial) {
        this.isSpecial = isSpecial;
    }


    public void setContentAuditId(Long contentAuditId) {
		this.contentAuditId = contentAuditId;
	}
	public String getDigiRightUrl() {
		return this.digiRightUrl;
	}

	public void setDigiRightUrl(String digiRightUrl) {
		this.digiRightUrl = digiRightUrl;
	}
	public Date getValidStartTime() {
		return this.validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}
	public Date getValidEndTime() {
		return this.validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}
	public Long getAllVisitCount() {
		return this.allVisitCount;
	}

	public void setAllVisitCount(Long allVisitCount) {
		this.allVisitCount = allVisitCount;
	}
	public Long getMonthVisitCount() {
		return this.monthVisitCount;
	}

	public void setMonthVisitCount(Long monthVisitCount) {
		this.monthVisitCount = monthVisitCount;
	}
	public Long getWeekVisitCount() {
		return this.weekVisitCount;
	}

	public void setWeekVisitCount(Long weekVisitCount) {
		this.weekVisitCount = weekVisitCount;
	}

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(Long scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public Long getVisitCountStatus() {
        return visitCountStatus;
    }

    public void setVisitCountStatus(Long visitCountStatus) {
        this.visitCountStatus = visitCountStatus;
    }

    public Long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Long scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getPost1Url() {
		return this.post1Url;
	}

	public void setPost1Url(String post1Url) {
		this.post1Url = post1Url;
	}
	public String getPost2Url() {
		return this.post2Url;
	}

	public void setPost2Url(String post2Url) {
		this.post2Url = post2Url;
	}
	public Long getProperty1() {
		return this.property1;
	}

	public void setProperty1(Long property1) {
		this.property1 = property1;
	}
	public Long getProperty2() {
		return this.property2;
	}

	public void setProperty2(Long property2) {
		this.property2 = property2;
	}
	public String getProperty3() {
		return this.property3;
	}

	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	public String getProperty4() {
		return this.property4;
	}

	public void setProperty4(String property4) {
		this.property4 = property4;
	}
	public String getProperty5() {
		return this.property5;
	}

	public void setProperty5(String property5) {
		this.property5 = property5;
	}
	public String getProperty6() {
		return this.property6;
	}

	public void setProperty6(String property6) {
		this.property6 = property6;
	}
	public String getProperty7() {
		return this.property7;
	}

	public void setProperty7(String property7) {
		this.property7 = property7;
	}
	public String getProperty8() {
		return this.property8;
	}

	public void setProperty8(String property8) {
		this.property8 = property8;
	}

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public BookMark getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
    }
}