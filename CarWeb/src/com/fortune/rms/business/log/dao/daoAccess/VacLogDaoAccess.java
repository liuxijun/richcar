package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VacLogDaoInterface;
import com.fortune.rms.business.log.model.VacLog;
import org.springframework.stereotype.Repository;

@Repository
public class VacLogDaoAccess extends BaseDaoAccess<VacLog,Long> implements VacLogDaoInterface {
    public VacLogDaoAccess() {
        super(VacLog.class);
    }
}
