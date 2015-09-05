package com.fortune.cars.business.cars.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.cars.business.cars.model.Car;

public interface CarLogicInterface extends BaseLogicInterface<Car> {
    boolean login(String phone,String pwd);

    String updatePwd(String phone, String oldPwd, String newPwd);
}
