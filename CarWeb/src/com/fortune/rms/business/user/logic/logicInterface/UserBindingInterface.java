package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.rms.business.user.model.AllResp;

public interface UserBindingInterface{
    public AllResp userBindingCheckRequest(String userId, Integer userType);
}
