package com.fortune.rms.web.user;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;

import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-12-2
 * Time: ÏÂÎç2:42
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/user")
@ParentPackage("default")
@Action(value="/userlogin")
public class UserLoginAction extends BaseAction<UserLogin>{
    private UserLoginLogicInterface userLoginLogicInterface;
    public UserLoginAction() {
        super(UserLogin.class);
    }
    private Date startTime;
    private Date stopTime;
  private String login;
    private String addr;
    private String tel;
    private String desp;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
    private int loginStatus;

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setUserLoginLogicInterface(UserLoginLogicInterface userLoginLogicInterface) {
        this.userLoginLogicInterface = userLoginLogicInterface;
    }
    public String list(){
        obj.setLogin(login);
        obj.setAddr(addr);
        obj.setDesp(desp);
        obj.setTel(tel);
        obj.setLoginStatus(loginStatus);
        objs = userLoginLogicInterface.getAll(obj,startTime,stopTime,pageBean);
       return "list";
    }

}
