package com.fortune.rms.business.log.logic.logicInterface;


import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.AreaDemandLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AreaDemandLogLogicInterface extends BaseLogicInterface<AreaDemandLog> {
    //每天统计用的方法
    public Map getAreaDemandFromVisitLog(Date dateStatics, long type);
    public long getAreaDemandByAreaId(Date dateStatics, long areaId, long type, long demandType);
}
