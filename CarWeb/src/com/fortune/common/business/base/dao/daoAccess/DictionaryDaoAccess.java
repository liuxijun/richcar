package com.fortune.common.business.base.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.dao.ConfigDaoInterface;
import com.fortune.common.business.base.dao.daoInterface.DictionaryDaoInterface;
import com.fortune.common.business.base.model.Config;
import com.fortune.common.business.base.model.Dictionary;
import org.springframework.stereotype.Repository;

/**
 * Created by xjliu on 2015/3/13.
 *
 */
@Repository
public class DictionaryDaoAccess extends BaseDaoAccess<Dictionary,String> implements DictionaryDaoInterface {
    public DictionaryDaoAccess() {
        super(Dictionary.class);
    }
}
