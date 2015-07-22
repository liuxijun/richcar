package com.fortune.rms.business.product.model;

import java.util.Date;

/**
 * ServiceProductGift generated by hbm2java
 */
public class ServiceProductGift implements java.io.Serializable {

	private long id;
	private Long serviceProductId;
	private Long giftServiceProductId;
	private Date startTime;
	private Date endTime;

	public ServiceProductGift() {
	}

	public ServiceProductGift(long id) {
		this.id = id;
	}
	public ServiceProductGift(long id, Long serviceProductId,
			Long giftServiceProductId, Date startTime, Date endTime) {
		this.id = id;
		this.serviceProductId = serviceProductId;
		this.giftServiceProductId = giftServiceProductId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getServiceProductId() {
		return this.serviceProductId;
	}

	public void setServiceProductId(Long serviceProductId) {
		this.serviceProductId = serviceProductId;
	}
	public Long getGiftServiceProductId() {
		return this.giftServiceProductId;
	}

	public void setGiftServiceProductId(Long giftServiceProductId) {
		this.giftServiceProductId = giftServiceProductId;
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
