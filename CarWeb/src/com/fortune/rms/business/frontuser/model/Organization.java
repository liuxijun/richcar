package com.fortune.rms.business.frontuser.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-24
 * Time: 17:35:20
 * 前台用户组织
 */
public class Organization {
    private Long id;
    private String name;
    private Long parentId;
    private Long sequence;
    private String channels;        // 关联的频道id，以逗号分隔，不需要序列号

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Organization() {
    }

    public Organization(Long id, String name, Long parentId) {

        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
