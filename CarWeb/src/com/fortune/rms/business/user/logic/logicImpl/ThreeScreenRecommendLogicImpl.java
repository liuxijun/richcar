package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.user.dao.daoInterface.ThreeScreenRecommendDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.ThreeScreenRecommendLogicInterface;
import com.fortune.rms.business.user.model.ThreeScreenRecommend;
import com.fortune.rms.business.user.model.RecommendNotify;
import com.fortune.rms.business.user.model.AllResp;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("friendRecommendLogicInterface")
public class ThreeScreenRecommendLogicImpl extends BaseLogicImpl<ThreeScreenRecommend> implements ThreeScreenRecommendLogicInterface {

    private ThreeScreenRecommendDaoInterface threeScreenRecommendDaoInterface;

    @Autowired
    public void setThreeScreenRecommendDaoInterface(ThreeScreenRecommendDaoInterface threeScreenRecommendDaoInterface) {
        this.threeScreenRecommendDaoInterface = threeScreenRecommendDaoInterface;
        baseDaoInterface = (BaseDaoInterface) threeScreenRecommendDaoInterface;
    }

    public AllResp addRecommend(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp) {
        if(ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)){
            return this.threeScreenRecommendDaoInterface.addRecommendFromDb(userId,userType,contentId,subContentId,subContentName,subContentType,serviceType,mode,friendIdList,info,timeStamp);
        }else{
            return this.threeScreenRecommendDaoInterface.addRecommendFromHttp(userId,userType,contentId,subContentId,subContentName,subContentType,serviceType,mode,friendIdList,info,timeStamp);
        }
    }

    @SuppressWarnings("unchecked")
    public AllResp getAllRecommend(String userId, Integer userType) {
        if(ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)){
            return this.threeScreenRecommendDaoInterface.getAllRecommendFromDb(userId,userType);
        }else{
            return this.threeScreenRecommendDaoInterface.getAllRecommendFromHttp(userId,userType);
        }
    }

    public AllResp updateOrDeleteRecommendNotify(String userId, Integer userType, Integer operationType, Integer status, String recommendedIdList) {
        if(ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)){
            return this.threeScreenRecommendDaoInterface.updateOrDeleteRecommendNotifyFromDb(userId,userType,operationType,status,recommendedIdList);
        }else{
            return this.threeScreenRecommendDaoInterface.updateOrDeleteRecommendNotifyFromHttp(userId,userType,operationType,status,recommendedIdList);
        }
    }

    public RecommendNotify getRecommendNotify(String result) {

        Element doc = com.fortune.util.XmlUtils.getRootFromXmlStr(result);
        List list = doc.elements();
        RecommendNotify recommendNotify =new RecommendNotify();
        recommendNotify.setUserId(list.get(0).toString());
        recommendNotify.setUserType(Integer.valueOf(list.get(1).toString()));
        return recommendNotify;
    }

}
