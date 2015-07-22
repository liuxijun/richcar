package com.fortune.rms.business.user.model;

import com.fortune.util.StringUtils;
import com.fortune.util.XmlUtils;

import java.util.Date;


public class BookMark {
    private long id;
    private String bookMarkId;
    private String contentId;
    private String subContentId;
    private String subContentName;
    private Integer subContentType;
    private Integer bookMarkType;
    private String bookMarkValue;

    private String userId;
    private Integer userType;
    private Integer serviceType;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getBookMarkType() {
        return bookMarkType;
    }

    public void setBookMarkType(Integer bookMarkType) {
        this.bookMarkType = bookMarkType;
    }

    public String getBookMarkId() {
        return bookMarkId;
    }

    public BookMark(org.dom4j.Node node){
        //<BookMark BookMarkValue="26" SubContentID="0" SubContentName="ÓûÄõÃÔ¹¬" BookMarkID="211" ContentID="" />
        bookMarkId = XmlUtils.getValue(node,"BookMarkID","-1");
        id = StringUtils.string2long(bookMarkId,0);
        bookMarkValue = XmlUtils.getValue(node,"BookMarkValue","0");
        subContentName = XmlUtils.getValue(node,"SubContentName","ÔÝÎÞÃû³Æ,bookMarkId="+bookMarkId);
        subContentId =  XmlUtils.getValue(node,"SubContentID","0");
        contentId = XmlUtils.getValue(node,"ContentID","0");
        bookMarkType = XmlUtils.getIntValue(node,"BookMarkType",-1);
        subContentType = XmlUtils.getIntValue(node,"SubContentType",-1);
        
    }
    public void setBookMarkId(String bookMarkId) {
        this.bookMarkId = bookMarkId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getSubContentId() {
        return subContentId;
    }

    public void setSubContentId(String subContentId) {
        this.subContentId = subContentId;
    }

    public String getSubContentName() {
        return subContentName;
    }

    public void setSubContentName(String subContentName) {
        this.subContentName = subContentName;
    }

    public Integer getSubContentType() {
        return subContentType;
    }

    public void setSubContentType(Integer subContentType) {
        this.subContentType = subContentType;
    }

    public String getBookMarkValue() {
        return bookMarkValue;
    }

    public void setBookMarkValue(String bookMarkValue) {
        this.bookMarkValue = bookMarkValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BookMark(String bookMarkId, String contentId, String subContentId,
                    String subContentName, Integer subContentType, Integer bookMarkType, String bookMarkValue) {
        this.bookMarkId = bookMarkId;
        this.contentId = contentId;
        this.subContentId = subContentId;
        this.subContentName = subContentName;
        this.subContentType = subContentType;
        this.bookMarkType = bookMarkType;
        this.bookMarkValue = bookMarkValue;
    }

    public BookMark() {
    }

    public BookMark(String bookMarkId, String contentId, Integer bookMarkType, String bookMarkValue) {
        this.bookMarkId = bookMarkId;
        this.contentId = contentId;
        this.bookMarkType = bookMarkType;
        this.bookMarkValue = bookMarkValue;
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
