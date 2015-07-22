package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.user.dao.daoInterface.UserLoginDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: ÏÂÎç2:17
 * To change this template use File | Settings | File Templates.
 */

@Service("userLoginLogicInterface")
public class UserLoginLogicImpl extends BaseLogicImpl<UserLogin> implements UserLoginLogicInterface {
    private UserLoginDaoInterface userLoginDaoInterface;

    @Autowired
    public void setUserLoginDaoInterface(UserLoginDaoInterface userLoginDaoInterface) {
        this.userLoginDaoInterface = userLoginDaoInterface;
        baseDaoInterface = (BaseDaoInterface) userLoginDaoInterface;
    }


    public List<UserLogin> getAll(UserLogin userLogin, Date startTime, Date stopTime, PageBean pageBean) {
        /*List<Object[]> tempResult = userLoginDaoInterface.getAll(userLogin,startTime,stopTime,pageBean);
        List<UserLogin> result = new ArrayList<UserLogin>();*/

        return userLoginDaoInterface.getAll(userLogin,startTime,stopTime,pageBean);
    }
}
