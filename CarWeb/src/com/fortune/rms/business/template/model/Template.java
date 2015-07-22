package com.fortune.rms.business.template.model;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 2011-6-16
 * Time: 9:47:51
 * To change this template use File | Settings | File Templates.
 */
public class Template  implements java.io.Serializable {

	private long id;
	private Long cspId;
	private String fileName;
	private String name;
	private Long type;
	private Long isSystem;
    private Long pageSize;

    public Template(){

    }


    public Template(long id, Long cspId, String fileName, String name, Long type, Long system, Long pageSize) {
        this.id = id;
        this.cspId = cspId;
        this.fileName = fileName;
        this.name = name;
        this.type = type;
        isSystem = system;
        this.pageSize = pageSize;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCspId() {
        return cspId;
    }

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Long isSystem) {
        this.isSystem = isSystem;
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
