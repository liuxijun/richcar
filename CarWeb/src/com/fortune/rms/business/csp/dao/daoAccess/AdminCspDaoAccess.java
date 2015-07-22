package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.AdminCspDaoInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminCspDaoAccess extends BaseDaoAccess<AdminCsp, Long>
        implements
        AdminCspDaoInterface {

    public AdminCspDaoAccess() {
        super(AdminCsp.class);
    }

    public int setDefaultCsp(int cspId, int adminId) {
        String hql = "update AdminCsp ac set ac.isDefaultCsp=2 where ac.adminId = " + adminId +
                " and ac.cspId!=" + cspId;
        return executeUpdate(hql);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Object[]> searchCspName(AdminCsp adminCsp, PageBean pageBean) {
        String hql ="from AdminCsp  ac,Csp c where ac.cspId=c.id ";
        List<Object> args = new ArrayList<Object>();
        if(adminCsp.getAdminId()!=null ) {
            hql +="  and ac.adminId=?" ;
            args.add(adminCsp.getAdminId());
        }

        if(adminCsp.getCsp()!=null){
            if(adminCsp.getCsp().getName()!=null && !"".equals(adminCsp.getCsp().getName())) {
                hql +=" and c.name like ?" ;
                args.add("%"+adminCsp.getCsp().getName().trim()+"%");
            }
        }
        hql+=" order by ac.id desc";
        List result = null;
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