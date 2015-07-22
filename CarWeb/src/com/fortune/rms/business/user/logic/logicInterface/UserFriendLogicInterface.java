package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.rms.business.user.model.AllResp;

public interface UserFriendLogicInterface {
    public AllResp getAllFriend(String userId, Integer userType);
}
