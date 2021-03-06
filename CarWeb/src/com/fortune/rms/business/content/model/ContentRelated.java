package com.fortune.rms.business.content.model;

/**
 * ContentRelated generated by hbm2java
 */
public class ContentRelated implements java.io.Serializable {

	private long id;
	private Long contentId;
	private Long relatedId;
	private Long relatedContentId;
	private Long displayOrder;

	public ContentRelated() {
	}

	public ContentRelated(long id) {
		this.id = id;
	}
	public ContentRelated(long id, Long contentId, Long relatedId,
			Long relatedContentId, Long displayOrder) {
		this.id = id;
		this.contentId = contentId;
		this.relatedId = relatedId;
		this.relatedContentId = relatedContentId;
		this.displayOrder = displayOrder;
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
	public Long getRelatedId() {
		return this.relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	public Long getRelatedContentId() {
		return this.relatedContentId;
	}

	public void setRelatedContentId(Long relatedContentId) {
		this.relatedContentId = relatedContentId;
	}
	public Long getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
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
