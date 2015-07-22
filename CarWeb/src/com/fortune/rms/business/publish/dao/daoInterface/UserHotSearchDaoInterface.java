package com.fortune.rms.business.publish.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;

import java.util.List;


public interface UserHotSearchDaoInterface
        extends
            BaseDaoInterface<UserHotSearch,Long>{

    public List<UserHotSearch> getUserHotSearch();
    public List<UserHotSearch> getUpdateHotSearch();
}
