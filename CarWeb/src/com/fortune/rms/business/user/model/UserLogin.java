package com.fortune.rms.business.user.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-4
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
public class UserLogin {
    private long id; //主键
    private String  login;
    private Date loginTime;
    private long loginStatus;//登陆方式
    private String  addr;
    private String  tel;
    private String desp;
    private long areaId;

    public UserLogin(){
    }

    public UserLogin(long id,String login,Date loginTime,long loginStatus,String addr,String tel,String desp,long areaId){
        this.id = id;
        this.login = login;
        this.loginTime = loginTime;
        this.loginStatus = loginStatus;
        this.addr = addr;
        this.tel = tel;
        this.desp = desp;
        this.areaId = areaId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public long getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(long loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    //转换成json  方便页面显示
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
