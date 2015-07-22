package com.fortune.common.business.base.dao;

import com.fortune.common.business.base.model.Config;
import org.springframework.stereotype.Repository;

/**
 * Created by xjliu on 2015/3/13.
 *
 */
@Repository
public class ConfigDaoAccess extends BaseDaoAccess<Config,String> implements ConfigDaoInterface {
    public ConfigDaoAccess() {
        super(Config.class);
    }
}
