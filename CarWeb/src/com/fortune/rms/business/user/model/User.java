package com.fortune.rms.business.user.model;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11
 * Time: ����2:17
 */
public class User {
    private long id; //����
    private String  login;      //��¼id
    private Date birthday;
    //private String  birthdayTemp;
    private String  gernate;   //�Ա�
    private String  passWord;     //����
    private String verifyCode;
    private Date verifyTime;
    private Date lastLoginTime;
    private String  passWordHistory;   //������ʷ
    private String  userName;//�û�����
    private String  email;
    private String  addr;
    private String  tel;
    private Integer  status ;      //״̬��1-����  2-����   3-����

    public User() {
    }

    public User(Integer status, long id, String login, Date birthday,
                String gernate, String passWord,
                String passWordHistory, String userName,
                String email, String addr, String tel,String verifyCode,
                Date verifyTime,Date lastLoginTime) {
        this.status = status;
        this.id = id;
        this.login = login;
        this.birthday = birthday;
        this.gernate = gernate;
        this.passWord = passWord;
        this.passWordHistory = passWordHistory;
        this.userName = userName;
        this.email = email;
        this.addr = addr;
        this.tel = tel;
        this.verifyCode = verifyCode;
        this.verifyTime = verifyTime;
        this.lastLoginTime = lastLoginTime;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;

    }
//    public void setBirthday(String birthday)throws Exception {
//        this.birthday = new SimpleDateFormat("yyyy-MM-dd").parse("birthday");
//         System.out.print(birthday+"sssssssssssssssssssssssssssss----------------");
//    }

    public String getGernate() {
        return gernate;
    }

    public void setGernate(String gernate) {
        this.gernate = gernate;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWordHistory() {
        return passWordHistory;
    }

    public void setPassWordHistory(String passWordHistory) {
        this.passWordHistory = passWordHistory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    //ת����json  ����ҳ����ʾ
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

