package com.fortune.cars.business.conduct.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.conduct.dao.daoInterface.ConductDaoInterface;
import com.fortune.cars.business.conduct.model.Conduct;
import com.fortune.util.PageBean;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ConductDaoAccess extends BaseDaoAccess<Conduct, Long>
		implements
			ConductDaoInterface {

	public ConductDaoAccess() {
		super(Conduct.class);
	}

	public List<Map<String,Object>> search(String carNo,String userId,Date startTime,Date stopTime,PageBean pageBean){
        String hql = "select c.carNo,c.userId,cd.createTime from Car c,Conduct cd where c.id=cd.carId";
        List<Object> parameters = new ArrayList<Object>();
        List<Map<String,Object>> resultMap= new ArrayList<Map<String, Object>>();
        try {
            if(carNo!=null&&!carNo.trim().equals("")){
                hql+=" and c.carNo like ?";
                parameters.add("%"+carNo+"%");
            }
            if(userId!=null&&!userId.trim().equals("")){
                hql+=" and c.userId like ?";
                parameters.add(userId);
            }
            List<Object> result = search(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
