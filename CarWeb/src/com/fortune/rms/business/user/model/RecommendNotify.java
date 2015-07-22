package com.fortune.rms.business.user.model;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-5-14
 * Time: 13:14:49
 *
 */
public class RecommendNotify {
    private String userId;
    private Integer userType;
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

    public RecommendNotify() {
    }

    public RecommendNotify(String userId, Integer userType, String recommendedId, String contentId, String subContentId, String subContentName, Integer subContentType, String recommender, String recommenderName, String info, String timeStamp, Integer status) {
        this.userId = userId;
        this.userType = userType;
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
}
