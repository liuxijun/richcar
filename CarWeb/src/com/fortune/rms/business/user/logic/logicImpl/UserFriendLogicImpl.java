package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.user.dao.daoInterface.UserFriendDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserFriendLogicInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.UserFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userFriendLogicInterface")
public class UserFriendLogicImpl extends BaseLogicImpl<UserFriend> implements UserFriendLogicInterface {
    private UserFriendDaoInterface userFriendDaoInterface;

    @Autowired
    public void setUserFriendDaoInterface(UserFriendDaoInterface userFriendDaoInterface) {
        this.userFriendDaoInterface = userFriendDaoInterface;
        baseDaoInterface = (BaseDaoInterface)userFriendDaoInterface;
    }

    @SuppressWarnings("unchecked")
    public AllResp getAllFriend(String userId, Integer userType) {
        if(ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)){
            return this.userFriendDaoInterface.getAllFriendFromDb(userId,userType);
        }else{
            return this.userFriendDaoInterface.getAllFriendFromHttp(userId,userType);
        }
    }
}
