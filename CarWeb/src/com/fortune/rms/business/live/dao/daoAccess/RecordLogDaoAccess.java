package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.RecordLogDaoInterface;
import com.fortune.rms.business.live.model.RecordLog;
import org.springframework.stereotype.Repository;

/**
 * Created by ÍõÃ÷Â· on 2015/2/26.
 */
@Repository
public class RecordLogDaoAccess extends BaseDaoAccess<RecordLog, Long>
        implements
        RecordLogDaoInterface {
    public RecordLogDaoAccess() {
        super(RecordLog.class);
    }
}
