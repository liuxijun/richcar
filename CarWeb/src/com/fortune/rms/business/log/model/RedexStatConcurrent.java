package com.fortune.rms.business.log.model;

/**
 * Created by 王明路 on 2014/11/24.
 */
public class RedexStatConcurrent {
    private String timeSpot;
    private long concurrent;

    public String getTimeSpot() {
        return timeSpot;
    }

    public void setTimeSpot(String timeSpot) {
        this.timeSpot = timeSpot;
    }

    public long getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(long concurrent) {
        this.concurrent = concurrent;
    }
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
