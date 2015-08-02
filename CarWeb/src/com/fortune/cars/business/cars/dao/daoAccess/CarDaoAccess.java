package com.fortune.cars.business.cars.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.cars.dao.daoInterface.CarDaoInterface;
import com.fortune.cars.business.cars.model.Car;
import org.springframework.stereotype.Repository;

@Repository
public class CarDaoAccess extends BaseDaoAccess<Car, Long>
		implements
			CarDaoInterface {

	public CarDaoAccess() {
		super(Car.class);
	}

}
