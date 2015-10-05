package com.fortune.cars.business.repair.logic.logicImpl;

import com.fortune.cars.business.repair.dao.daoInterface.PartsDaoInterface;
import com.fortune.cars.business.repair.logic.logicInterface.PartsLogicInterface;
import com.fortune.cars.business.repair.model.Parts;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("partsLogicInterface")
public class PartsLogicImpl extends BaseLogicImpl<Parts> implements PartsLogicInterface {
    private PartsDaoInterface partsDaoInterface;

    @Autowired
    public void setPartsDaoInterface(PartsDaoInterface partsDaoInterface) {
        this.partsDaoInterface = partsDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) partsDaoInterface;
    }
}
