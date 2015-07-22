package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.LiveLogDaoInterface;
import com.fortune.rms.business.live.model.LiveLog;
import org.springframework.stereotype.Repository;

/**
 * Created by ÍõÃ÷Â· on 2015/2/26.
 */
@Repository
public class LiveLogDaoAccess extends BaseDaoAccess<LiveLog, Long>
        implements
        LiveLogDaoInterface {
    public LiveLogDaoAccess() {
        super(LiveLog.class);
    }
}
