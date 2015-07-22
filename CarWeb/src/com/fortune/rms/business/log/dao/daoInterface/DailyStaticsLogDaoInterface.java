package com.fortune.rms.business.log.dao.daoInterface;


import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.DailyStaticsLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DailyStaticsLogDaoInterface extends BaseDaoInterface<DailyStaticsLog,Long> {
    public Map getNetFlowLogsFromVisitLog(Date startDate, Long liveChannelId, List cspIdList);
    public List getAllTimeDataFromVisitLog(Date startDate);
}
