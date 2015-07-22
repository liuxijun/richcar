package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.IP2PhoneLogDaoInterface;
import com.fortune.rms.business.system.model.IP2PhoneLog;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-10-18
 * Time: обнГ4:18
 *
 */
@Repository
public class IP2PhoneLogDaoAccess extends BaseDaoAccess<IP2PhoneLog, Long>
        implements IP2PhoneLogDaoInterface {
    public IP2PhoneLogDaoAccess() {
        super(IP2PhoneLog.class);
    }
}
