package com.fortune.rms.business.publish.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;

import javax.xml.crypto.Data;
import java.util.List;


public interface SearchDaoInterface
        extends
            BaseDaoInterface<UserHotSearch,Long>{
    public boolean addSearchValue(String searchValue, long userIel, java.util.Date nowTime);
}
