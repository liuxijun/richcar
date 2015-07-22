package com.fortune.rms.business.syn.model;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-6-20
 * Time: 10:18:57
 * To change this template use File | Settings | File Templates.
 */
public class SynFile implements  java.io.Serializable {

    private long id;
    private String md5;
    private String name;
    private String type;
    private Date startTime;
    private String url;
    private long spId;

    public SynFile() {
    }

    public SynFile(long id) {
        this.id = id;
    }

    public SynFile(long id, String md5, String name, String type, Date startTime, String url) {
        this.id = id;
        this.md5 = md5;
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
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
