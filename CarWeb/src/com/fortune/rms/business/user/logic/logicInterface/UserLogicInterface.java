package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.user.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11
 * Time: 下午2:18
 */
public interface UserLogicInterface extends BaseLogicInterface<User> {
    public static final Long VAC_ORDER_PRODUCT = 1l;
    public static final Long VAC_ORDER_PRODUCT_FOR_ONCE_TIME = 4l;
    public static final Long VAC_CHECK_PRODUCT_ORDER = 6l;
    public static final Integer USER_STATUS_OK=1;
    public static final Integer USER_STATUS_OUT_SERVICE=2;
    public static final Integer USER_STATUS_LOCKED=3;
    public static final Integer USER_STATUS_LOGIN_FROM_CLIENT=8000;

    public boolean checkLoginName(User user);
    public User checkLoginNameAndPassWord(User user);
    public String createVerifyCode(String telephoneNumber);
    public String createVerifyCode(String telephoneNumber, boolean forBuy);
    public boolean loginByPhoneNumber(String userTel, String verifyCode, boolean canUseDefaultPwd);
    public boolean isUserExists(String login);
    //设置保存历史密码
    public boolean savePassword(Integer operatorId, String oldPwd, String newPwd);
    public String getUserTelphoneByIp(String userIp);
    public boolean sendSMS(String phoneNumber, String message);
    public User getUserById(Long id);
    //查询所有的用户信息 （为了显示时间）
   // public String list();
    public boolean checkPlayPermissions(String userId, long contentId);
    public boolean checkPlayPermissions_Pc(String userId, long contentId);
    public String calculateUserToken(String phoneNumber);

    boolean verifyCodeForBuy(String userId, String verifyCode);
}
