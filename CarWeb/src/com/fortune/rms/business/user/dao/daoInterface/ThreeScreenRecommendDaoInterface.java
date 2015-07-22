package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.ThreeScreenRecommend;
import com.fortune.rms.business.user.model.RecommendNotify;


public interface ThreeScreenRecommendDaoInterface extends BaseDaoInterface<ThreeScreenRecommend,Long> {
    public AllResp addRecommendFromDb(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp);
    public AllResp getAllRecommendFromDb(String userId, Integer userType);
    public AllResp updateOrDeleteRecommendNotifyFromDb(String userId, Integer userType, Integer operationType, Integer status, String recommendedIdList);
    public RecommendNotify getRecommendNotifyFromDb(String result);

    public AllResp addRecommendFromHttp(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp);
    public AllResp getAllRecommendFromHttp(String userId, Integer userType);
    public AllResp updateOrDeleteRecommendNotifyFromHttp(String userId, Integer userType, Integer operationType, Integer status, String recommendedIdList);
    public RecommendNotify getRecommendNotifyFromHttp(String result);
}
