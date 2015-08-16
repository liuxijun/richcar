package com.fortune.cars.business.conduct.logic.logicInterface;

import com.fortune.cars.business.conduct.model.ConductItem;
import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.cars.business.conduct.model.Conduct;

import java.util.List;

public interface ConductLogicInterface extends BaseLogicInterface<Conduct> {
    List<ConductItem> getItems(Integer id,Integer carId);

    List<ConductItem> saveItems(Conduct obj);
}
