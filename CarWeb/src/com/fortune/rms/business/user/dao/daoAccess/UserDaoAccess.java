package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.user.dao.daoInterface.UserDaoInterface;
import com.fortune.rms.business.user.model.User;
import com.fortune.util.SpringUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11
 * Time: ÏÂÎç2:22
 */
@Repository
public class UserDaoAccess extends BaseDaoAccess<User, Long> implements UserDaoInterface {

    public UserDaoAccess() {
        super(User.class);
    }

    public static Connection getConnection()
            throws Exception {

        return DataSourceUtils.getConnection((DataSource) SpringUtils.getBean("dataSource"));
    }


    public boolean checkLoginName(User user) {
        boolean isExisted = false;
        String hql = "from User where userName ='" + user.getUserName() + "'";
        List<User> users = this.getHibernateTemplate().find(hql);
        if(users!=null && users.size()>0){
            isExisted = true;
        }

        return isExisted;
    }

    public User getUserById(Long id) {
        return this.get(id);
    }
}
