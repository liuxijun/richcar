package com.fortune.cars.web.conduct;

import com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface;
import com.fortune.cars.business.cars.model.Car;
import com.fortune.cars.business.conduct.model.ConductItem;
import com.fortune.common.Constants;
import com.fortune.util.BeanUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductLogicInterface;
import com.fortune.cars.business.conduct.model.Conduct;
import com.fortune.common.web.base.BaseAction;

import java.util.Date;
import java.util.List;

@Namespace("/conduct")
@ParentPackage("default")
@Action(value = "conduct")
@SuppressWarnings("unused")
public class ConductAction extends BaseAction<Conduct> {
	private static final long serialVersionUID = 3243534534534534l;
	private ConductLogicInterface conductLogicInterface;
	private CarLogicInterface carLogicInterface;
	@SuppressWarnings("unchecked")
	public ConductAction() {
		super(Conduct.class);
	}
	/**
	 * @param conductLogicInterface the conductLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setConductLogicInterface(
			ConductLogicInterface conductLogicInterface) {
		this.conductLogicInterface = conductLogicInterface;
		setBaseLogicInterface(conductLogicInterface);
	}

	public void setCarLogicInterface(CarLogicInterface carLogicInterface) {
		this.carLogicInterface = carLogicInterface;
	}

	private Integer carId;
	public String view(){
        int id = StringUtils.string2int(keyId,-1);
		if(id>0){
			super.view();
		}
        Car car =null;
        if(carId==null){
            if(obj!=null){
                carId = obj.getCarId();
            }
        }
        if(carId!=null&&carId>0){
            try {
                car = carLogicInterface.get(carId);
            } catch (Exception e) {
                log.error("没找到id对应的car对象：carId="+carId);
            }
        }
        if(obj!=null){
            if(obj.getTitle()==null){
                String title = StringUtils.date2string(new Date())+"检查记录";
                if(car!=null){
                    String carNo = car.getCarNo();
                    if(carNo!=null){
                        title = car.getCarNo()+" "+title;
                    }
                }else{
                    log.error("未发现对应的Car！不能设置默认的");
                }
                obj.setTitle(title);
            }
            Integer objCarId = obj.getCarId();
            if(objCarId==null||objCarId<=0){
                if(car!=null){
                    obj.setCarId(car.getId());
                }else{
                    obj.setCarId(carId);
                }
            }
            Integer miles = obj.getMiles();
            if(miles==null||miles==0){
                if(car!=null&&car.getMileage()!=null){
                    obj.setMiles(car.getMileage());
                }else{
                    obj.setMiles(3000);
                }
            }
            BeanUtils.setDefaultValue(obj,"status",1);
            //BeanUtils.setDefaultValue(obj,"",);
            obj.setItems(conductLogicInterface.getItems(StringUtils.string2int(keyId,-1),obj.getCarId()));
        }
		return Constants.ACTION_VIEW;
	}
	public String save(){
		List<ConductItem> items = obj.getItems();
		BeanUtils.setDefaultValue(obj,"createTime",new Date());
		BeanUtils.setDefaultValue(obj,"status",1);
		BeanUtils.setDefaultValue(obj,"carId",-1);
		String result = super.save();
		obj.setItems(items);
		obj.setItems(conductLogicInterface.saveItems(obj));
		return result;
	}
}
