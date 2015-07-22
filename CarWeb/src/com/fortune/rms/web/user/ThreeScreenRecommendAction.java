package com.fortune.rms.web.user;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.user.logic.logicInterface.ThreeScreenRecommendLogicInterface;

import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.ThreeScreenRecommend;
import com.fortune.rms.business.user.model.RecommendNotify;
import com.fortune.threeScreen.common.Constants;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@Namespace("/web")
@ParentPackage("threeScreen")
@Results({
        @Result(name = "add",location = "/common/threeScreenData.jsp"),
        @Result(name = "list",location = "/common/threeScreenData.jsp"),
        @Result(name = "update",location = "/common/threeScreenData.jsp"),
        @Result(name = "recommendNotify",location = "/common/threeScreenData.jsp")
})
@Action(value="recommend")
public class ThreeScreenRecommendAction extends BaseAction<ThreeScreenRecommend> {
    private static final long serialVersionUID = 3243534534534534l;
    private ThreeScreenRecommendLogicInterface threeScreenRecommendLogicInterface;

    public ThreeScreenRecommendLogicInterface getThreeScreenRecommendLogicInterface() {
        return threeScreenRecommendLogicInterface;
    }

    public ThreeScreenRecommendAction() {
        super(ThreeScreenRecommend.class);
    }

    @Autowired
    public void setThreeScreenRecommendLogicInterface(ThreeScreenRecommendLogicInterface threeScreenRecommendLogicInterface) {
        this.threeScreenRecommendLogicInterface = threeScreenRecommendLogicInterface;
        setBaseLogicInterface(threeScreenRecommendLogicInterface);

    }




    public JSONObject jsonList;

    public JSONObject getJsonList() {
        return jsonList;
    }

    public void setJsonList(JSONObject jsonList) {
        this.jsonList = jsonList;
    }

    public String add(){
        int userType = Constants.USER_TYPE_PC;
        if(userId==null){
            userId= (String) session.get("userId");
        }
        AllResp recommendResp = threeScreenRecommendLogicInterface.addRecommend(userId, userType,
                contentId, subContentId, subContentName, subContentType, serviceType,
                mode, friendIdList, info, timeStamp);
        jsonList = JSONObject.fromObject(recommendResp);
//        System.out.println(jsonList);
        return "add";
    }

    public String list(){
        int userType = Constants.USER_TYPE_PC;
        if(userId==null){
            userId= (String) session.get("userId");
        }
        AllResp allRecommendResp = threeScreenRecommendLogicInterface.getAllRecommend(userId, userType);
        jsonList = JSONObject.fromObject(allRecommendResp);
   //   System.out.println(jsonList);
        return "list";
    }

    public String update(){
        //String userId = "18600000002@ad";
        int userType = Constants.USER_TYPE_PC;
        if(userId==null){
            userId= (String) session.get("userId");
        }
        AllResp recommendResp = threeScreenRecommendLogicInterface.updateOrDeleteRecommendNotify(userId,
                userType, operationType, status, recommendedIdList);
        jsonList = JSONObject.fromObject(recommendResp);
//        System.out.println(jsonList);
        log.debug(userId+"更新推荐调用结果："+jsonList.toString());
        return "update";
    }

    public String recommendNotify(){
        String result = (String) session.get("recommendNotifyReq");
        if(result != null || result != ""){
            RecommendNotify recommendNotify = threeScreenRecommendLogicInterface.getRecommendNotify(result);
            jsonList = JSONObject.fromObject(recommendNotify);
        }
//        System.out.println(jsonList);
        return "recommendNotify";
    }

    private String contentId;
    private String subContentName;
    private Integer subContentType;
    private Integer serviceType;
    private Integer mode;
    private String friendIdList;
    private String info;
    private String timeStamp;
    private String subContentId;
    private Integer operationType;
    private Integer status;
    private String recommendedIdList;
    private String playUserId;

    public String getPlayUserId() {
        return playUserId;
    }

    public void setPlayUserId(String playUserId) {
        this.playUserId = playUserId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRecommendedIdList() {
        return recommendedIdList;
    }

    public void setRecommendedIdList(String recommendedIdList) {
        this.recommendedIdList = recommendedIdList;
    }

    public String getSubContentId() {
        return subContentId;
    }

    public void setSubContentId(String subContentId) {
        this.subContentId = subContentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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
}
