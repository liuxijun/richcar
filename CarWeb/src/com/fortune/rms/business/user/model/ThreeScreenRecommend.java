package com.fortune.rms.business.user.model;

import com.fortune.util.StringUtils;
import com.fortune.util.XmlUtils;


public class ThreeScreenRecommend {
    private long id;
    private String recommendedId;
    private String contentId;
    private String subContentId;
    private String subContentName;
    private Integer subContentType;
    private String recommender;
    private String recommenderName;
    private String info;
    private String timeStamp;
    private Integer status;

    private Integer userType;
    private Integer serviceType;
    private Integer mode;
    private String friendIdList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(String friendIdList) {
        this.friendIdList = friendIdList;
    }

    public ThreeScreenRecommend(String recommendedId, String contentId, Integer status){
        this.recommendedId = recommendedId;
        this.contentId = contentId;
        this.status = status;
    }

    public ThreeScreenRecommend(String recommendedId, String contentId,
                                String subContentId, String subContentName,
                                Integer subContentType, String recommender,
                                String recommenderName, String info,
                                String timeStamp, Integer status) {
        this.recommendedId = recommendedId;
        this.contentId = contentId;
        this.subContentId = subContentId;
        this.subContentName = subContentName;
        this.subContentType = subContentType;
        this.recommender = recommender;
        this.recommenderName = recommenderName;
        this.info = info;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public ThreeScreenRecommend(org.dom4j.Node node){
//<ThreeScreenRecommend Info="Info" SubContentID="Pro201012071822000727" SubContentName="心碎的蜜月"
//             RecommendedID="2649" ContentID="Pro201012071822000727" Recommender="追风"
//              RecommenderName="18603293101" TimeStamp="20110412112457" /> 
        recommendedId = XmlUtils.getValue(node,"RecommendedID","-1");
        id = StringUtils.string2long(recommendedId,0);
        contentId = XmlUtils.getValue(node,"ContentID","-1");
        subContentId =  XmlUtils.getValue(node,"SubContentID","-1");
        subContentName  = XmlUtils.getValue(node,"SubContentName","-1");
        subContentType = XmlUtils.getIntValue(node,"SubContentType",-1);
        subContentId =  XmlUtils.getValue(node,"SubContentID","-1");
        recommender  = XmlUtils.getValue(node,"Recommender","-1");
        recommenderName = XmlUtils.getValue(node,"RecommenderName","-1");
        info =  XmlUtils.getValue(node,"Info","-1");
        timeStamp =  XmlUtils.getValue(node,"TimeStamp","201104000000");
        status =  XmlUtils.getIntValue(node,"Status",-1);
    }
    public String getRecommenderName() {
        return recommenderName;
    }

    public void setRecommenderName(String recommenderName) {
        this.recommenderName = recommenderName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRecommendedId() {
        return recommendedId;
    }

    public void setRecommendedId(String recommendedId) {
        this.recommendedId = recommendedId;
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

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }


    public ThreeScreenRecommend() {
    }
}
