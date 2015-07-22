package com.fortune.rms.business.log.logic.logicInterface;


import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.DailyStaticsLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DailyStaticsLogLogicInterface extends BaseLogicInterface<DailyStaticsLog> {
    public Map getNetFlowLogsFromVisitLog(Date startDate, Long liveChannelId, List cspIdList);
    public List getBingFaLogsFromVisitLog(Date startDate);
}
