package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: обнГ2:19
 * To change this template use File | Settings | File Templates.
 */

public interface UserLoginDaoInterface
        extends
         BaseDaoInterface<UserLogin,Long> {
    public List<UserLogin> getAll(UserLogin userLogin, Date startTime, Date stopTime, PageBean pageBean);
}
