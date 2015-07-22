package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.user.dao.daoInterface.UserLoginDaoInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: &#x4e0b;&#x5348;2:21
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class UserLoginDaoAccess extends BaseDaoAccess<UserLogin,Long> implements UserLoginDaoInterface{

    public UserLoginDaoAccess() {
        super(UserLogin.class);
    }


    public List<UserLogin> getAll(UserLogin userLogin, Date startTime, Date stopTime, PageBean pageBean) {
        String hql = " from UserLogin ul where 1=1";
        List<Object> args = new ArrayList<Object>();
        if(userLogin.getLogin()!=null && !"".equals(userLogin.getLogin().trim())) {
            hql +=" and ul.login like ?" ;
            args.add("%"+userLogin.getLogin().trim()+"%");
        }
        if(userLogin.getLoginStatus()>0){
            hql+=" and ul.loginStatus = "+userLogin.getLoginStatus();
        }
        if(startTime!=null){
            hql +=" and ul.loginTime > ?";
            args.add(startTime);
        }
        if(stopTime!=null){
            hql +=" and ul.loginTime <= ?";
            args.add(stopTime);
        }
        List result;
        try {
            if (args.size() > 0) {
                result = getObjects(hql, args.toArray(), pageBean);
            } else {
                result = getObjects(hql,null,pageBean);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<UserLogin>();
    }
}

