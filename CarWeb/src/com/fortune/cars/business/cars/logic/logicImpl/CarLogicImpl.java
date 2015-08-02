package com.fortune.cars.business.cars.logic.logicImpl;

import com.fortune.cars.business.cars.dao.daoInterface.CarDaoInterface;
import com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface;
import com.fortune.cars.business.cars.model.Car;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xjliu on 2015/7/26.
 *
 */
@Service("carLogicInterface")
public class CarLogicImpl  extends BaseLogicImpl<Car> implements CarLogicInterface {
    private CarDaoInterface carDaoInterface;

    @Autowired
    public void setCarDaoInterface(CarDaoInterface carDaoInterface) {
        this.carDaoInterface = carDaoInterface;
    }
}
