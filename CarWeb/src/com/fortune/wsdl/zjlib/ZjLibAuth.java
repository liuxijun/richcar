package com.fortune.wsdl.zjlib;

import com.fortune.rms.business.frontuser.logic.logicInterface.OrganizationLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.frontuser.model.Organization;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.util.AppConfigurator;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2015/6/10.
 * 浙江图书馆认证
 */
public class ZjLibAuth {
    public static Logger logger = Logger.getLogger("com.fortune.wsdl.zjlib.ZjLibAuth");
    public static FrontUser getReturn(ICasAuthServiceServiceStub.RespCas response){
        if(response==null){
            return null;
        }
        FrontUser u = new FrontUser();
        u.setName(response.getFullname());
        AppConfigurator config = AppConfigurator.getInstance();
        String company = response.getCompany();
        long orgId=-1;
        if(company==null||company.isEmpty()){
            orgId = -1;
        }else{
            OrganizationLogicInterface organizationLogicInterface = (OrganizationLogicInterface)
                    SpringUtils.getBeanForApp("organizationLogicInterface");
            List<Organization> organizations = organizationLogicInterface.getOrganizationByName(company);
            if(organizations!=null&&organizations.size()>0){
                orgId = organizations.get(0).getId();
            }
        }
        if(orgId<=0){
            orgId = config.getLongConfig("system.sso.zjLib.defaultUserOrganization",1);
        }
        u.setOrganizationId(orgId);
        u.setUserId(response.getLoginId());
        u.setLastLogon(new Date());
        u.setUnit(response.getUnit());
        u.setGender(StringUtils.string2long(response.getSex(),1));
        u.setBirthday(StringUtils.string2date(response.getBirthday()));
        u.setPhone(response.getPhone());
        u.setCity(response.getAddress());
        u.setMail(response.getEmail());
        u.setStatus(UserLogicInterface.USER_STATUS_OK.longValue());
        u.setSection(response.getNationality());
        u.setLogonTimes(1L);
        long userType = StringUtils.string2long(response.getUserType(), -1);
        if(userType==-1){
            userType =config.getLongConfig("system.sso.zjLib.defaultUserType",1);
        }
        u.setTypeId(userType);
        u.setPassword(response.getToken());
        return u;
    }
    public static String getUrl(){
        AppConfigurator configurator = AppConfigurator.getInstance();
        return configurator.getConfig("system.sso.zjLib.url","http://10.18.17.38:8030/zjgcas/services/casAuth");
    }
    public static FrontUser checkToken(String userId,String token,String userIp){
        try {
            String url = getUrl();
            ICasAuthServiceServiceStub casAuthServiceServiceStub = new ICasAuthServiceServiceStub(url);
            ICasAuthServiceServiceStub.CheckTokenE  checkTokenE = new ICasAuthServiceServiceStub.CheckTokenE();
            try {

                ICasAuthServiceServiceStub.CheckToken checkToken = new ICasAuthServiceServiceStub.CheckToken();
                checkToken.setArg0(token);
                checkToken.setArg1(userIp);
                checkTokenE.setCheckToken(checkToken);
                logger.debug("准备发起请求:"+url);
                com.fortune.wsdl.zjlib.ICasAuthServiceServiceStub.CheckTokenResponseE responseE = casAuthServiceServiceStub.checkToken(checkTokenE);
                ICasAuthServiceServiceStub.RespCas response = responseE.getCheckTokenResponse().get_return();
                String respCode = response.getRespCode();
                if("200".equals(respCode)){
                    logger.debug("验证通过："+response.getFullname());
                    return getReturn(response);
                }else{
                    logger.debug("验证失败："+respCode+","+response.getRespMessage());
                }
            } catch (RemoteException e) {
                logger.error("远端发生异常："+e.getMessage());
                e.printStackTrace();
            }
        } catch (AxisFault axisFault) {
            logger.error("发生Axis异常："+axisFault.getMessage());
            axisFault.printStackTrace();
        }
        return null;
    }
    public static FrontUser auth(String userId,String pwd,String userIp){
        AppConfigurator configurator = AppConfigurator.getInstance();
        String target = getUrl();
        String result = "";
        try {
            ICasAuthServiceServiceStub casAuthServiceServiceStub = new ICasAuthServiceServiceStub(target);
            ICasAuthServiceServiceStub.CasAuthE casAuthE = new ICasAuthServiceServiceStub.CasAuthE();
            try {
                ICasAuthServiceServiceStub.CasAuth casAuth = new ICasAuthServiceServiceStub.CasAuth();
                casAuth.setArg0(userId);
                casAuth.setArg1(pwd);
                casAuth.setArg2(configurator.getConfig("system.sso.zjLib.loginType","2"));
                casAuth.setArg3(userIp);
                casAuth.setArg4(configurator.getConfig("system.sso.zjLib.libCode ", "zjlib"));
                casAuthE.setCasAuth(casAuth);
                com.fortune.wsdl.zjlib.ICasAuthServiceServiceStub.CasAuthResponseE responseE = casAuthServiceServiceStub.casAuth(casAuthE);
                ICasAuthServiceServiceStub.RespCas response = responseE.getCasAuthResponse().get_return();
                String respCode = response.getRespCode();
                if("200".equals(respCode)){
                    logger.debug("验证通过："+response.getFullname());
                    return getReturn(response);
                }else{
                    logger.debug("验证失败："+respCode+","+response.getRespMessage());
                }
            } catch (RemoteException e) {
                logger.error("远端发生异常："+e.getMessage());
                e.printStackTrace();
            }
        } catch (AxisFault axisFault) {
            logger.error("发生Axis异常："+axisFault.getMessage());
            axisFault.printStackTrace();
        }
        return null;
    }
}
