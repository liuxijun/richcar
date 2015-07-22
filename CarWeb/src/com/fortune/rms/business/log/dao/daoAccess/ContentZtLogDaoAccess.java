package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.ContentZtLogDaoInterface;
import com.fortune.rms.business.log.model.ContentZtLog;
import org.springframework.stereotype.Repository;

@Repository
public class ContentZtLogDaoAccess extends BaseDaoAccess<ContentZtLog,Long>
        implements
        ContentZtLogDaoInterface {

    public ContentZtLogDaoAccess() {
        super(ContentZtLog.class);
    }
}
