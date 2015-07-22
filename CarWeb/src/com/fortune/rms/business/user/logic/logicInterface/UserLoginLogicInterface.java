package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: ÏÂÎç2:15
 * To change this template use File | Settings | File Templates.
 */
public interface UserLoginLogicInterface extends BaseLogicInterface<UserLogin> {
    public static final long LOGIN_STATUS_FROM_CLIENT=5L;
    public static final long LOGIN_STATUS_FROM_SMS_VERIFY_CODE=4L;
    public static final long LOGIN_STATUS_FROM_HB_INTERFACE=3L;
    public static final long LOGIN_STATUS_FROM_WAP_HEADER=2L;
    public static final long LOGIN_STATUS_FROM_UN_CENTER=1L;
    public static final long LOGIN_STATUS_UNKNOWN=1000L;

   public List<UserLogin> getAll(UserLogin userLogin, Date startTime, Date stopTime, PageBean pageBean);
}
