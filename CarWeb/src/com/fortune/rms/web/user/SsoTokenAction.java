package com.fortune.rms.web.user;

import com.fortune.common.web.base.FortuneAction;
import com.fortune.rms.business.user.logic.logicInterface.SsoTokenLogicInterface;
import com.fortune.util.AppConfigurator;
import org.apache.struts2.convention.annotation.*;


/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-5-9
 * Time: 18:25:59
 *
 */
@Namespace("/web")
@ParentPackage("threeScreen")
@Results({
        @Result(name = "userBindSso",location = "/common/userBindSso.jsp"),
        @Result(name = "userLoginSso",location = "/common/userLoginSso.jsp")
})
@Action(value="ssoToken-*")
public class SsoTokenAction extends FortuneAction {
    private static final long serialVersionUID = 3243534534534534l;
    private SsoTokenLogicInterface ssoTokenLogicInterface;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String userBindSso(){
        String key =  AppConfigurator.getInstance().getConfig("ssoKey.key");
        ssoUserToken = ssoTokenLogicInterface.userBindSso(userId,this.getUrl(),key);
        return "userBindSso";
    }

    public String userLoginSso(){
        String key =  AppConfigurator.getInstance().getConfig("ssoKey.key");
        ssoUserToken  = ssoTokenLogicInterface.userLogicSso(userId,this.getUrl(),key);
        return "userLoginSso";
    }
    public String ssoUserToken;

    public String getSsoUserToken() {
        return ssoUserToken;
    }

    public void setSsoUserToken(String ssoUserToken) {
        this.ssoUserToken = ssoUserToken;
    }

    public SsoTokenLogicInterface getSsoTokenLogicInterface() {
        return ssoTokenLogicInterface;
    }

    public void setSsoTokenLogicInterface(SsoTokenLogicInterface ssoTokenLogicInterface) {
        this.ssoTokenLogicInterface = ssoTokenLogicInterface;
    }
}
