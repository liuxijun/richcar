package com.fortune.rms.business.publish.model;

/**
 * RelatedProperty generated by hbm2java
 */
public class RelatedProperty implements java.io.Serializable {

	private long id;
	private Long relatedId;
	private Long propertyId;
	private String propertyValue;

	public RelatedProperty() {
	}

	public RelatedProperty(long id) {
		this.id = id;
	}
	public RelatedProperty(long id, Long relatedId, Long propertyId,
			String propertyValue) {
		this.id = id;
		this.relatedId = relatedId;
		this.propertyId = propertyId;
		this.propertyValue = propertyValue;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getRelatedId() {
		return this.relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	public Long getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	public String getPropertyValue() {
		return this.propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
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
