package com.fortune.rms.business.content.model;

/**
 * ContentServiceProduct generated by hbm2java
 */
public class ContentServiceProduct implements java.io.Serializable {

	private long id;
	private Long contentId;
	private Long serviceProductId;

	public ContentServiceProduct() {
	}

	public ContentServiceProduct(long id) {
		this.id = id;
	}
	public ContentServiceProduct(long id, Long contentId, Long serviceProductId) {
		this.id = id;
		this.contentId = contentId;
		this.serviceProductId = serviceProductId;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getServiceProductId() {
		return this.serviceProductId;
	}

	public void setServiceProductId(Long serviceProductId) {
		this.serviceProductId = serviceProductId;
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
