package com.fortune.common.business.base.logic;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.dao.ConfigDaoInterface;
import com.fortune.common.business.base.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/3/13.
 *
 */
@Service("configLogicInterface")
public class ConfigLogicImpl extends BaseLogicImpl<Config> implements ConfigLogicInterface {
    private ConfigDaoInterface configDaoInterface;

    @Autowired
    public void setConfigDaoInterface(ConfigDaoInterface configDaoInterface) {
        this.configDaoInterface = configDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)configDaoInterface;
    }
}
