package com.fortune.cars.business.conduct.logic.logicImpl;

import com.fortune.cars.business.conduct.dao.daoInterface.ConductValueDaoInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductValueLogicInterface;
import com.fortune.cars.business.conduct.model.ConductValue;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("conductValueLogicInterface")
public class ConductValueLogicImpl extends BaseLogicImpl<ConductValue> implements ConductValueLogicInterface  {
    private ConductValueDaoInterface conductValueDaoInterface;

    @Autowired
    public void setConductDaoInterface(ConductValueDaoInterface conductValueDaoInterface) {
        this.conductValueDaoInterface = conductValueDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) conductValueDaoInterface;
    }
}
