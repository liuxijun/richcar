package com.fortune.cars.business.conduct.logic.logicImpl;

import com.fortune.cars.business.conduct.dao.daoInterface.ConductItemDaoInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductItemLogicInterface;
import com.fortune.cars.business.conduct.model.ConductItem;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("conductItemLogicInterface")
public class ConductItemLogicImpl extends BaseLogicImpl<ConductItem> implements ConductItemLogicInterface  {
    private ConductItemDaoInterface conductItemDaoInterface;

    @Autowired
    public void setConductDaoInterface(ConductItemDaoInterface conductItemDaoInterface) {
        this.conductItemDaoInterface = conductItemDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) conductItemDaoInterface;
    }
}
