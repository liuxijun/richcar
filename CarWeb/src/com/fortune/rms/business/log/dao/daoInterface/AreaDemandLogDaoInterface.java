package com.fortune.rms.business.log.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.AreaDemandLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AreaDemandLogDaoInterface extends BaseDaoInterface<AreaDemandLog, Long> {
    public Map getAreaDemandFromVisitLog(Date dateStatics, long type);
    public long getAreaDemandByAreaId(Date dateStatics, long areaId, long type, long demandType);

}