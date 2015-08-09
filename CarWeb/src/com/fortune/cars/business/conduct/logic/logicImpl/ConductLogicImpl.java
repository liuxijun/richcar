package com.fortune.cars.business.conduct.logic.logicImpl;

import com.fortune.cars.business.conduct.dao.daoInterface.ConductDaoInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductLogicInterface;
import com.fortune.cars.business.conduct.model.Conduct;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("conductLogicInterface")
public class ConductLogicImpl extends BaseLogicImpl<Conduct> implements ConductLogicInterface  {
    private ConductDaoInterface conductDaoInterface;

    @Autowired
    public void setConductDaoInterface(ConductDaoInterface conductDaoInterface) {
        this.conductDaoInterface = conductDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) conductDaoInterface;
    }
}
