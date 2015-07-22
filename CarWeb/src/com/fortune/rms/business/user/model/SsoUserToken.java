package com.fortune.rms.business.user.model;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-4-25
 * Time: 14:05:35
 * To change this template use File | Settings | File Templates.
 */
public class SsoUserToken {
    private String userId;
    private String provinceNo;
    private String returnUrl;
    private String expiredTimeStamp_SSO;
    private String expiredTimeStamp_UE;

    public SsoUserToken(String userId, String returnUrl, String expiredTimeStamp_SSO, String expiredTimeStamp_UE) {
        this.userId = userId;
        this.returnUrl = returnUrl;
        this.expiredTimeStamp_SSO = expiredTimeStamp_SSO;
        this.expiredTimeStamp_UE = expiredTimeStamp_UE;
    }

    public SsoUserToken(String userId, String provinceNo, String returnUrl, String expiredTimeStamp_SSO, String expiredTimeStamp_UE) {

        this.userId = userId;
        this.provinceNo = provinceNo;
        this.returnUrl = returnUrl;
        this.expiredTimeStamp_SSO = expiredTimeStamp_SSO;
        this.expiredTimeStamp_UE = expiredTimeStamp_UE;
    }

    public SsoUserToken() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvinceNo() {
        return provinceNo;
    }

    public void setProvinceNo(String provinceNo) {
        this.provinceNo = provinceNo;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getExpiredTimeStamp_SSO() {
        return expiredTimeStamp_SSO;
    }

    public void setExpiredTimeStamp_SSO(String expiredTimeStamp_SSO) {
        this.expiredTimeStamp_SSO = expiredTimeStamp_SSO;
    }

    public String getExpiredTimeStamp_UE() {
        return expiredTimeStamp_UE;
    }

    public void setExpiredTimeStamp_UE(String expiredTimeStamp_UE) {
        this.expiredTimeStamp_UE = expiredTimeStamp_UE;
    }
}
