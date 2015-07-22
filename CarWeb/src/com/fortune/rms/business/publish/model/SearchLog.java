package com.fortune.rms.business.publish.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: wang
 * Date: 13-10-23
 * Time: ÏÂÎç4:58
 * To change this template use File | Settings | File Templates.
 */
public class SearchLog {
    private long id;
    private String searchValue;
    private Date searchTime;
    private long userTel;
    public SearchLog() {

    }
    public SearchLog(long id, String searchValue, Date searchTime, long userTel) {
        this.id = id;
        this.searchValue = searchValue;
        this.searchTime = searchTime;
        this.userTel = userTel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public long getUserTel() {
        return userTel;
    }

    public void setUserTel(long userTel) {
        this.userTel = userTel;
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
