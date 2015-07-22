package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.ThreeScreenRecommend;
import com.fortune.rms.business.user.model.RecommendNotify;


public interface ThreeScreenRecommendLogicInterface extends BaseLogicInterface<ThreeScreenRecommend> {
    public AllResp addRecommend(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp);
    public AllResp getAllRecommend(String userId, Integer userType);
    public AllResp updateOrDeleteRecommendNotify(String userId, Integer userType, Integer operationType, Integer status, String recommendedIdList);
    public RecommendNotify getRecommendNotify(String result);
}
