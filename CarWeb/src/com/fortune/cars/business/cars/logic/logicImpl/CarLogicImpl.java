package com.fortune.cars.business.cars.logic.logicImpl;

import com.fortune.cars.business.cars.dao.daoInterface.CarDaoInterface;
import com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface;
import com.fortune.cars.business.cars.model.Car;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        this.baseDaoInterface = (BaseDaoInterface)carDaoInterface;
    }

    @Override
    public boolean login(String phone, String pwd) {
        if(phone==null||"".equals(phone.trim())||pwd==null||"".equals(pwd.trim())){
            return false;
        }
        Car bean = new Car();
        bean.setPhone(phone);
        bean.setPassword(pwd);
        List<Car> cars = search(bean);
        return cars!=null&&cars.size()>0;
    }

    @Override
    public String updatePwd(String phone, String oldPwd, String newPwd) {
        if(oldPwd==null||oldPwd.isEmpty()){
            return "ԭʼ����Ϊ�գ������޸ģ�";
        }
        if(newPwd==null||newPwd.isEmpty()){
            return "�¿���Ϊ�գ������޸ģ�";
        }
        if(login(phone,oldPwd)){
            carDaoInterface.executeUpdate("update car set password='"+newPwd+"' where phone='"+phone+"'");
        }else{
            return "�˺Ŷ�Ӧ�ľɿ���ԣ������޸Ŀ��";
        }
        return null;
    }
}
