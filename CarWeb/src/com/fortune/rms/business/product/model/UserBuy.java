package com.fortune.rms.business.product.model;

import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;

/**
 * UserBuy generated by hbm2java
 */
public class UserBuy extends BaseModel {

	private long id;
	private String userId;
	private String userIp;
	private Long spId;
	private Long channelId;
	private Long contentId;
    private Long areaId;
    private Long contentPropertyId;
    private String productId;
	private Long serviceProductId;
	private Long isGift;
	private Double price;
	private Date startTime;
	private Date endTime;
	private Date buyTime;
    private Long cpId;
    private Long serviceProductType;

    public Long getServiceProductType() {
        return serviceProductType;
    }

    public void setServiceProductType(Long serviceProductType) {
        this.serviceProductType = serviceProductType;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public UserBuy() {
	}

	public UserBuy(long id) {
		this.id = id;
	}

    public UserBuy(long id, String userId, String userIp, Long spId, Long channelId, Long contentId,Long areaId,
                   Long contentPropertyId, String productId, Long serviceProductId, Long isGift, Double price, Date startTime, Date endTime, Date buyTime, Long cpId, Long serviceProductType) {
        this.id = id;
        this.userId = userId;
        this.userIp = userIp;
        this.spId = spId;
        this.channelId = channelId;
        this.contentId = contentId;
        this.areaId = areaId;
        this.contentPropertyId = contentPropertyId;
        this.productId = productId;
        this.serviceProductId = serviceProductId;
        this.isGift = isGift;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.buyTime = buyTime;
        this.cpId = cpId;
        this.serviceProductType = serviceProductType;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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
	public Long getSpId() {
		return this.spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
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
        return contentPropertyId;
    }

    public void setContentPropertyId(Long contentPropertyId) {
        this.contentPropertyId = contentPropertyId;
    }

    public Long getServiceProductId() {
		return this.serviceProductId;
	}

	public void setServiceProductId(Long serviceProductId) {
		this.serviceProductId = serviceProductId;
	}
	public Long getIsGift() {
		return this.isGift;
	}

	public void setIsGift(Long isGift) {
		this.isGift = isGift;
	}

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
	public Date getBuyTime() {
		return this.buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}
