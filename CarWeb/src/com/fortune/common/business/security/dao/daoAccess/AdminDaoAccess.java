package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.AdminDaoInterface;
import com.fortune.common.business.security.model.Admin;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AdminDaoAccess extends BaseDaoAccess<Admin, Integer>
		implements
        AdminDaoInterface {

	public AdminDaoAccess() {
		super(Admin.class);
	}

    //返回登陆管理员所有的能使用的Action方法名
    public List getAllTargetByLogin(String login){
        String sql ="select sp.target from security_operator so,security_operator_role sor,security_role sr,security_role_permission srp,security_permission sp where so.OPERATORID = sor.OPERATORID and sor.ROLEID =sr.ROLEID and sr.ROLEID = srp.ROLEID and srp.PERMISSIONID = sp.PERMISSIONID and so.LOGIN = '+login+'";
        Session session = getSession();
        SQLQuery query = session.createSQLQuery(sql);
        try {
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            session.close();
        }

        return null;
    }
  
}
