package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VacLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VacLogLogicInterface;
import com.fortune.rms.business.log.model.VacLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("vacLogLogicInterface")
public class VacLogLogicImpl extends BaseLogicImpl<VacLog> implements VacLogLogicInterface{
    private VacLogDaoInterface vacLogDaoInterface;
    /**
     * @param vacLogDaoInterface the VacLogDaoInterface to set
     */
    @Autowired
    public void setVacLogDaoInterface(
            VacLogDaoInterface vacLogDaoInterface) {
        this.vacLogDaoInterface = vacLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.vacLogDaoInterface;
    }

}
