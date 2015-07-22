package com.fortune.rms.business.publish.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;

import java.util.List;


public interface SearchLogicInterface
        extends
        BaseLogicInterface<UserHotSearch> {

        public boolean addSearchLog(String searchValue, long userIel);
}
