package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.SystemLogDaoInterface;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class SystemLogDaoAccess extends BaseDaoAccess<SystemLog, Long>
		implements
			SystemLogDaoInterface {

	public SystemLogDaoAccess() {
		super(SystemLog.class);
	}


    @SuppressWarnings("unchecked")
    public List<Object[]> getSystemLogAll(SystemLog systemLog,Date startTime,Date stopTime, PageBean pageBean) {
        String hql = " from SystemLog  sl,Admin a where sl.adminId=a.id ";
        List<Object> args = new ArrayList<Object>();
        if(systemLog.getAdminName()!=null && !"".equals(systemLog.getAdminName().trim())) {
            hql +="  and a.login like ?" ;
            args.add("%"+systemLog.getAdminName().trim()+"%");
        }

        if(systemLog.getLog()!=null && !"".equals(systemLog.getLog().trim())) {
            hql +=" and sl.log like ?" ;
            args.add("%"+systemLog.getLog().trim()+"%");
        }

        if(startTime!=null){
             hql +=" and sl.logTime > ?";
              args.add(startTime);
         }
        if(stopTime!=null){
            hql +=" and sl.logTime <= ?";
            args.add(stopTime);
        }
   /*     hql+=" order by sl.id desc";*/
        List result;
        try {
            if (args.size() > 0) {
                result = getObjects(hql, args.toArray(), pageBean);
            } else {
                result = getObjects(hql, null, pageBean);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Object[]>();
    }
}
