package com.fortune.rms.business.product.model;

public class ServiceProductChannel {
    public long id;
    public long serviceProductId;
    public long channelId;

    public ServiceProductChannel() {
    }

    public ServiceProductChannel(long id, long serviceProductId, long channelId) {
        this.id = id;
        this.serviceProductId = serviceProductId;
        this.channelId = channelId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceProductId() {
        return serviceProductId;
    }

    public void setServiceProductId(long serviceProductId) {
        this.serviceProductId = serviceProductId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
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
