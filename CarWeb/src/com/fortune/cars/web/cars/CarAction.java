package com.fortune.cars.web.cars;

import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface;
import com.fortune.cars.business.cars.model.Car;
import com.fortune.common.web.base.BaseAction;

@Namespace("/cars")
@ParentPackage("default")
@Action(value = "car")
@SuppressWarnings("unused")
public class CarAction extends BaseAction<Car> {
	private static final long serialVersionUID = 32335395539539l;
	private CarLogicInterface carLogicInterface;
	@SuppressWarnings("unchecked")
	public CarAction() {
		super(Car.class);
	}
	/**
	 * @param carLogicInterface the carLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setCarLogicInterface(CarLogicInterface carLogicInterface) {
		this.carLogicInterface = carLogicInterface;
		setBaseLogicInterface(carLogicInterface);
	}
}
