package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.AreaDemandLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.AreaDemandLogLogicInterface;
import com.fortune.rms.business.log.model.AreaDemandLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("areaDemandLogLogicInterface")
public class AreaDemandLogLogicImpl extends  BaseLogicImpl<AreaDemandLog>
        implements
             AreaDemandLogLogicInterface {
    private AreaDemandLogDaoInterface areaDemandLogDaoInterface;

    /**
     * @param areaDemandLogDaoInterface the areaDemandLogDaoInterface to set
     */
    @Autowired
    public void setAreaDemandLogDaoInterface(
            AreaDemandLogDaoInterface areaDemandLogDaoInterface) {
        this.areaDemandLogDaoInterface = areaDemandLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.areaDemandLogDaoInterface;
    }
    public Map getAreaDemandFromVisitLog(Date dateStatics,long type){
        return areaDemandLogDaoInterface.getAreaDemandFromVisitLog(dateStatics,type);
    }

    public long getAreaDemandByAreaId(Date dateStatics,long areaId,long type,long demandType) {
        return areaDemandLogDaoInterface.getAreaDemandByAreaId(dateStatics,areaId,type,demandType);
    }

}
