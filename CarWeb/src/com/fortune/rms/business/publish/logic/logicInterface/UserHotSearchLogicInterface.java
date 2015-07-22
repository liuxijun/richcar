package com.fortune.rms.business.publish.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;

import java.util.List;


public interface UserHotSearchLogicInterface
        extends
        BaseLogicInterface<UserHotSearch> {

    public List<UserHotSearch> getUserHotSearch();
}
