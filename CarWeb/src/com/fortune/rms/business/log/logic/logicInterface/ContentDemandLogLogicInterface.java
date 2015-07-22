package com.fortune.rms.business.log.logic.logicInterface;


import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ContentDemandLogLogicInterface extends BaseLogicInterface<ContentDemandLog> {
    //每天统计用的方法
    public Map getContentDemandFromVisitLog(Date dateStatics, long type, long liveChannelId);
}
