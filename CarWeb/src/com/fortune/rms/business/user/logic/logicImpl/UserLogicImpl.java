package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.UserBuyLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.rms.business.system.logic.logicInterface.ShortMessageLogLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.ShortMessageLog;
import com.fortune.rms.business.user.dao.daoInterface.UserDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.rms.business.user.model.User;
import com.fortune.server.message.ServerMessager;
import com.fortune.smgw.api.sgip.client.SGIPClientWorker;
import com.fortune.smgw.api.sgip.message.SGIPSubmit;
import com.fortune.smgw.api.sgip.message.SGIPSubmitResp;
import com.fortune.smgw.api.sgip.message.body.SGIPSubmitBody;
import com.fortune.util.*;
import com.fortune.vac.VacWorker;
import com.fortune.vac.ip2phone.*;
import com.wo.sdk.message.APIRequest;
import com.wo.sdk.message.AuthorizationHeader;
import com.wo.sdk.openapi.auth.domain.GetTokenResponse;
import com.wo.sdk.openapi.auth.impl.AuthenticateAPIImpl;
import com.wo.sdk.openapi.net.domain.GetUidResponse;
import com.wo.sdk.openapi.net.impl.NetAPIimpl;
import org.apache.axis.AxisFault;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11         User
 * Time: 下午2:19
 */
@Service("userLogicInterface")
public class UserLogicImpl extends BaseLogicImpl<User> implements UserLogicInterface {
    private UserDaoInterface userDaoInterface;
    private ServiceProductLogicInterface serviceProductLogicInterface;
    private ProductLogicInterface productLogicInterface;
    private UserBuyLogicInterface userBuyLogicInterface;
    private ShortMessageLogLogicInterface shortMessageLogLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private SystemLogLogicInterface systemLogLogicInterface;
    @SuppressWarnings("unchecked")
    @Autowired
    public void setUserDaoInterface(UserDaoInterface userDaoInterface) {
        this.userDaoInterface = userDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.userDaoInterface;
    }

    @Autowired
    public void setServiceProductLogicInterface(ServiceProductLogicInterface serviceProductLogicInterface) {
        this.serviceProductLogicInterface = serviceProductLogicInterface;
    }

    @Autowired
    public void setProductLogicInterface(ProductLogicInterface productLogicInterface) {
        this.productLogicInterface = productLogicInterface;
    }


    @Autowired
    public void setShortMessageLogLogicInterface(ShortMessageLogLogicInterface shortMessageLogLogicInterface){
        this.shortMessageLogLogicInterface = shortMessageLogLogicInterface;
    }

    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    @Autowired
    public void setUserBuyLogicInterface(UserBuyLogicInterface userBuyLogicInterface) {
        this.userBuyLogicInterface = userBuyLogicInterface;
    }

    @Autowired
    public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
        this.systemLogLogicInterface = systemLogLogicInterface;
    }

    //验证用户注册是否重复
    public boolean checkLoginName(User user) {
        boolean isExisted = false;
        try {
            List<User> userList = this.userDaoInterface.getObjects(user);
            if(userList!=null && userList.size()>0){
                isExisted = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isExisted;
    }

    public User checkLoginNameAndPassWord(User user) {
        User userTemp = null;
        try {
            List<User> userList = this.userDaoInterface.getObjects(user);
            if(userList!=null && userList.size()==1){
                userTemp = userList.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userTemp;
    }
    public String createVerifyCode(String telephoneNumber) {
        return createVerifyCode(telephoneNumber,false);
    }
    public String createVerifyCode(String telephoneNumber,boolean forBuy) {
        User user = new User();
        user.setTel(telephoneNumber);
        List<User> users = search(user,false);
        int count = users.size();
        if(count<=0){
            //没有这个用户，创建一个
            user.setLogin(telephoneNumber);
            user.setTel(telephoneNumber);
            user.setUserName(telephoneNumber);
            user = save(user);
        }else{
            user = users.get(0);
            if(count>1){
                //把多余的删掉
                int i=1;
                while(i<count){
                    remove(users.get(i));
                    i++;
                }
            }
        }
        String verifyCode = "";
        for(int i=0;i<6;i++){
            long code = Math.round(Math.random()*10);
            if(code>=10){
                code = 9;
            }
            verifyCode+=code;
        }
        user.setVerifyCode(verifyCode);
        logger.debug("校验码(" + user.getTel() + "," + user.getVerifyCode() +
                ")已经生成，准备通过短信网关进行处理....");
        user.setVerifyTime(new Date());
        save(user);
        //todo 向短信网管发消息
        //该部分代码尚未实现
        String returnMessage = "验证码：verifyCode，欢迎您使用河北联通燕赵视界业务！请及时使用，该码将在60秒后失效！";
        if(forBuy){
            returnMessage = "欢迎您使用河北联通燕赵视界业务！购买产品包的验证码：verifyCode！请不要透漏给任何人，该码将在60秒后失效！";
            returnMessage = ConfigManager.getInstance().getConfig("SGIP_MESSAGE_MODULE_FOR_BUY",returnMessage);
        }else{
            returnMessage = ConfigManager.getInstance().getConfig("SGIP_MESSAGE_MODULE",returnMessage);
        }
        returnMessage = returnMessage.replaceAll("verifyCode",verifyCode);
        //如果短信不通，将短信内容返回用户。如果短信发送成功，则只是提示发送成功
        if(sendSMS(telephoneNumber,returnMessage)){
            returnMessage ="已经成功发送校验码到指定的"+telephoneNumber;
        }else{
            returnMessage = ConfigManager.getInstance().getConfig("SGIP_MESSAGE_MODULE_WHEN_FAIL","非常抱歉，短信发送失败。请稍候再试！给您带来不便，敬请谅解！");
            returnMessage = returnMessage.replaceAll("phoneNumber",telephoneNumber);
            returnMessage = returnMessage.replaceAll("verifyCode",verifyCode);
        }
        return returnMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 发送短信
     * @param phoneNumber 手机号码
     * @param message        短信内容
     * @return              是否发送成功
     *
     */
    public boolean sendSMS(String phoneNumber,String message){
        ConfigManager config = ConfigManager.getInstance();
        if(config.getConfig("SGIP_SEND_NATIVE", false)){
            logger.debug("即将发送短信："+phoneNumber+","+message);
            return sendSMSNative(phoneNumber,message);
        }else{
            String smgwUrl = config.getConfig("SGIP_SMGW_URL","http://61.55.144.87/smgw/?resultXmlFormat=true&command=send");
            ServerMessager messager = new ServerMessager();
            logger.debug("请求发送："+phoneNumber+","+smgwUrl+","+message);
            String result = messager.postToHost(smgwUrl,"phoneNumber="+phoneNumber+"&message="+message,config.getConfig("SGIP_SMGW_ENCODING","utf-8"));
            logger.debug("服务器返回信息："+result);
            Element root = XmlUtils.getRootFromXmlStr(result);
            return root != null && XmlUtils.getIntValue(root, "result-code", 1) == 0;
        }
    }
    public boolean sendSMSNative(String phoneNumber,String message){
        //SGIPClient client = SGIPClient.getInstance();
        ConfigManager config = ConfigManager.getInstance();
        SGIPClientWorker client = SGIPClientWorker.getInstance();
/*
        SGIPSubmit submit = new SGIPSubmit();
        SGIPSubmitBody body = submit.getBody();
        Date expireTime = new Date(System.currentTimeMillis()+5*60*1000L);//过期时间，设定为5分钟
        ConfigManager config = ConfigManager.getInstance();
        body.setSPNumber(config.getConfig("SGIP_SP_NUMBER","10655910010"));//接入号
        body.setChargeNumber("000000000000000000000");//该费用由SP支付//phoneNumber);
        body.setUserCount(1);
        if(phoneNumber.startsWith("+86")||phoneNumber.startsWith("086")){
            phoneNumber = phoneNumber.substring(1);
        }
        if(phoneNumber.startsWith("0086")){
            phoneNumber = phoneNumber.substring(2);
        }
        if(!phoneNumber.startsWith("86")){
            phoneNumber= "86"+phoneNumber;
        }
        body.setUserNumber(phoneNumber);
        body.setCorpId(config.getConfig("SGIP_CORP_ID","14294"));//企业代码
        body.setFeeType(1);
        body.setFeeValue("0");
        body.setGivenValue("0");//赠送的话费
        body.setAgentFlag(0);
        body.setMorelatetoMTFlag(0);
        body.setPriority(0);
        body.setExpireTime(StringUtils.date2string(expireTime, "yyMMddHHmmss"));
        //body.setScheduleTime(date2string(now,"yymmddhhmmss")+"032+");//"090621010101"
        body.setScheduleTime(null);//立即发送。\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");//"090621010101"
        body.setReportFlag(1);
        body.setTP_pid(0);
        body.setTP_udhi(0);
        body.setMessageCoding(15);//GBK编码
        try {
            body.setMessageContent(message.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            logger.error("居然不支持GBK编码？不管你信不信，反正我震惊了："+e.getMessage());
        }
        body.setReserve("");
        body.setMessageType(0);
        submit.getHead().setSequenceNo(new SGIPSequenceNo());
*/
        Date now = new Date();
        String spCorpId = config.getConfig("SGIP_CORP_ID","14294");
        String spNumber = config.getConfig("SGIP_SP_NUMBER","10655910010");
        long expireMinutes = config.getConfig("SGIP_EXPIRED_MINUTES", -1);
        SGIPSubmit submit = new SGIPSubmit();
        SGIPSubmitBody body = submit.getBody();
        Date expireTime = new Date(now.getTime()+expireMinutes*60*1000L);//过期时间，设定为5分钟
        body.setSPNumber(spNumber);//接入号
        body.setChargeNumber("000000000000000000000");//该费用由SP支付//phoneNumber);
        body.setUserCount(1);
        if(phoneNumber.startsWith("+86")||phoneNumber.startsWith("086")){
            phoneNumber = phoneNumber.substring(1);
        }
        if(phoneNumber.startsWith("0086")){
            phoneNumber = phoneNumber.substring(2);
        }
        if(!phoneNumber.startsWith("86")){
            phoneNumber= "86"+phoneNumber;
        }
        body.setUserNumber(phoneNumber);
        body.setCorpId(spCorpId);//企业代码
        body.setFeeType(1);
        body.setFeeValue("0");
        body.setGivenValue("0");//赠送的话费
        body.setAgentFlag(0);
        body.setMorelatetoMTFlag(0);
        body.setPriority(0);
        if(expireMinutes>0){
            body.setExpireTime(StringUtils.date2string(expireTime,"yyMMddHHmmss"));
        }else{
            body.setExpireTime(null);
        }
        //body.setScheduleTime(date2string(now,"yymmddhhmmss")+"032+");//"090621010101"
        body.setScheduleTime(null);//立即发送。\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");//"090621010101"
        body.setReportFlag(1); /*该条消息无论最后是否成功都要返回状态报告
                    状态报告标记
                            0-该条消息只有最后出错时要返回状态报告
                            1-该条消息无论最后是否成功都要返回状态报告
                            2-该条消息不需要返回状态报告
                            3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告

                    */
        body.setTP_pid(0);
        body.setTP_udhi(0);
        body.setMessageCoding(15);//GBK编码
        /*
       短消息的编码格式。
               0：纯ASCII字符串
               3：写卡操作
               4：二进制编码
               8：UCS2编码
               15: GBK编码
               其它参见GSM3.38第4节：SMS Data Coding Scheme

       * */

        String result ="将要发送的内容："+message+"\r\n";
        try {
            body.setMessageContent(message.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        body.setReserve("");
        body.setMessageType(0);
        logger.debug("准备发送.....");
        ShortMessageLog shortMessageLog = new ShortMessageLog();
        shortMessageLog.setMessage(message);
        shortMessageLog.setStartTime(new Date());
        shortMessageLog.setSn(submit.getHead().getSequenceNo().toString());
        int respResult = -1;
        int tryTimes = 5;
        while(respResult!=0&&tryTimes>0){
            SGIPSubmitResp rsp6 = client.sendSubmit(submit);
            respResult = rsp6.getBody().getResult();
            tryTimes --;
            if(respResult!=0){
                String tempStr= StringUtils.date2string(new Date())+"发生异常（" +respResult+
                        "），再试一次发送！还可以尝试"+tryTimes+"次！";
                logger.error(tempStr);
                result+=tempStr+"\r\n";
            }
        }
        //client.close();
        shortMessageLog.setStatus((long) respResult);
        shortMessageLog.setSmsIp(config.getConfig("SGIP_IP","127.0.0.1"));
        shortMessageLog.setLog(result+"\n"+body);
        shortMessageLog.setPhoneNumber(phoneNumber);
        shortMessageLogLogicInterface.save(shortMessageLog);
        if(respResult!=0){
            logger.error("发送短信失败！");
        }
/*
        SGIPSubmitResp rsp6 = client.sendSubmit(submit);
        int resultCode = rsp6.getBody().getResult();
        if(resultCode!=0){
            logger.error("发送短信时发生异常："+ SGIPErrorType.errorMsgs.get(resultCode));
        }
*/
        return respResult==0;
    }
    public boolean loginByPhoneNumber(String userTel,String verifyCode,boolean canUseDefaultPwd){
        if(userTel==null||"".equals(userTel.trim())||verifyCode==null||"".equals(verifyCode.trim())){
            return false;
        }
        if(ConfigManager.getInstance().getConfig("hbMobile.notNeedVerifyCode", false)){
            return true;
        }
        if(canUseDefaultPwd){
            if(verifyCode.equals(ConfigManager.getInstance().getConfig("system.hbMobile.defaultDebugVerifyCode","338547"))){
                systemLogLogicInterface.saveMachineLog("手机号码"+userTel+"使用了默认的登录确认码!");
                return true;
            }
        }else{
            //这个是为了支付密码所用，千万注意
            if(verifyCode.equals(ConfigManager.getInstance().getConfig("system.hbMobile.defaultDebugVerifyCodeForBuy","183092"))){
                systemLogLogicInterface.saveMachineLog("手机号码"+userTel+"使用了默认的购买确认码!");
                return true;
            }
        }

        User user = new User();
        user.setTel(userTel);
        user.setVerifyCode(verifyCode);
        List<User> users = search(user,false);
        if(users!=null&&users.size()>0){
            int maxLength = ConfigManager.getInstance().getConfig("hbMobile.smsCodeExpireTimeSeconds", 120);
            user = users.get(0);
            Date now = new Date();
            Date createCodeTime = user.getVerifyTime();
            if(createCodeTime==null){
                createCodeTime = new Date(now.getTime()-(maxLength+10)*1000);
            }
            long length = Math.abs(now.getTime()-createCodeTime.getTime())/1000;
            if(length<maxLength){
                user.setLastLoginTime(new Date());
                user.setVerifyCode("used:"+user.getVerifyCode());
                save(user);
               return true;
            }else{
                logger.error("用户登录时间与分发校验码时间间隔过长，无法登录，发放时间："+StringUtils.date2string(createCodeTime)+",登录时间："+
                    StringUtils.date2string(now)+",相差："+StringUtils.formatTime(length));
            }
        }else{
            logger.warn("帐号或者校验码错误："+userTel+","+verifyCode);
        }
        return false;
    }
    public boolean isUserExists(String login) {
            User searchBean = new User();
            searchBean.setLogin(login);
            List<User> list = search(searchBean,false);
            if (list != null && list.size() > 0) {
                logger.warn("这个用户Login'" + login + "'已经存在！");
                return true;
            } else {
                logger.warn("这个用户Login'" + login + "'不存在！");
                return false;
        }
    }
    //实习sava 方法  保存历史密码和新密码
    public User save(User user){
        Long id = user.getId();
        if ( id > 0) {
            //如果是修改，而且口令没有设置，则用旧的口令设置
            String password = user.getPassWord();
            User op = get(id);
            if (op != null) {
                if (password == null || "".equals(password.trim())||password.equals(op.getPassWord())) {
                    user.setPassWord(op.getPassWord());
                    logger.debug("保存时，口令被置空，使用原来的口令！");
                } else {
                    logger.debug("新口令：'" + password + "'");
                    //设置口令历史
                    String oldPassword = user.getPassWordHistory() + "|||" + op.getPassWord();
                    //如果过长，把前一个口令删了（一个加密长度是35，100能保存3 个密码）
                    while (oldPassword.length() > 100) {
                        int p = oldPassword.indexOf("|||");
                        if (p >= 0) {
                            oldPassword = oldPassword.substring(p + 3);
                        } else {
                            break;
                        }
                    }
                    logger.debug("设置口令，保存口令历史：" + oldPassword);
                    user.setPassWordHistory(oldPassword);
                }
            }
        }
        user = super.save(user);
        logger.debug("成功保存数据！");
        return user;
    }

    //找回历史密码
    public boolean savePassword(Integer operatorId, String oldPwd, String newPwd) {
        User op = get(operatorId);
        boolean result = false;
        if(op.getPassWord().equals(oldPwd)){
            op = setOldPasswordLog(op);
            op.setPassWord(newPwd);
            save(op);
            result = true;
        }
        return result;
    }

    public String getUserTelphoneByIp(String userIp){
        ConfigManager config = ConfigManager.getInstance();
        if(config.getConfig("QUERY_USER_INFO_NATIVE", false)){
            return getUserTelphoneByIpNative(userIp);
        }else{
            String smgwUrl = config.getConfig("QUERY_USER_INFO_URL","http://61.55.144.86:8080/vac/queryUser.jsp?command=ip2phone");
            ServerMessager messager = new ServerMessager();
            String result = messager.postToHost(smgwUrl,"userIp="+userIp);
            Element root = XmlUtils.getRootFromXmlStr(result);
            return root != null ? XmlUtils.getValue(root, "result-code", null):null;
        }
    }
    public String getUserTelphoneByIpNative(String userIp){
        String serviceId = "18000012";
        ConfigManager config = ConfigManager.getInstance();
        serviceId = config.getConfig("QUERY_USER_INFO_SERVICE_ID",serviceId);
        String QUERY_USER_INFO_SERVICE_URL=config.getConfig("QUERY_USER_INFO_SERVICE_URL","http://61.55.156.205:8090/webservices/services/QueryUserInfoServiceApplyHttpPort");
        try {
            QueryUserInfoServiceApplyHttpPortSoapBindingStub queryInterface = new QueryUserInfoServiceApplyHttpPortSoapBindingStub(new URL(QUERY_USER_INFO_SERVICE_URL),null);
            QueryUserInfoRequestUserInfo userInfo=new QueryUserInfoRequestUserInfo(userIp,null,null);
            QueryUserInfoRequestServerInfo serverInfo = new QueryUserInfoRequestServerInfo(serviceId,StringUtils.date2string(new Date(),"yyyyMMddHHmmss"));
            QueryUserInfoRequest queryUserInfoRequest = new QueryUserInfoRequest(userInfo,serverInfo);
            QueryUserInfoRespone response = queryInterface.queryUserInfoServiceApply(queryUserInfoRequest);

            if(response.getServerInfo().hashCode()!=0){
                logger.error("无法获取IP，错误代码："+response.getServerInfo().getResultCode()+","+response.getServerInfo().getDescription());
                return null;
            }else {
                return response.getUserInfo().getUserName();
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            logger.error("无法获取IP对应的手机号："+axisFault.getMessage());
        } catch (MalformedURLException e) {
            logger.error("无法获取IP对应的手机号：" + e.getMessage());
        } catch(java.rmi.RemoteException e){
            logger.error("无法获取IP对应的手机号："+e.getMessage());
        }
        return null;
    }
    public User getUserById(Long id) {
        //SimpleDateFormat sf = new SimpleDateFormat("yyy-mm-dd");
        return userDaoInterface.getUserById(id);
            // user.setBirthdayTemp(sf.format(user.getBirthday()));
        //return user;
    }

    public User setOldPasswordLog(User op){
        //设置口令历史
        String oldPassword = op.getPassWordHistory() + "|||" + op.getPassWord();
        //如果过长，把前一个口令删了
        while (oldPassword.length() > 110) {
            int p = oldPassword.indexOf("|||");
            if (p >= 0) {
                oldPassword = oldPassword.substring(p + 3);
            } else {
                break;
            }
        }
        logger.debug("设置口令，保存口令历史：" + oldPassword);
        op.setPassWordHistory(oldPassword);
        return op;
    }
    //查询所有的人事信息
//    public String list(){
//
//        return Constants.ACTION_LIST;
//    }

    @SuppressWarnings("unchecked")
    public boolean checkPlayPermissions(String userId,final long contentId) {
        //检查白名单
        String whiteList = ConfigManager.getInstance().getConfig("system.buy.whiteList","15613199253;18631118565;18631110681;18631110330;18603211395;18603210858;18632921367;18631110202");
        if(whiteList!=null&&whiteList.contains(userId)){
            return true;
        }
        //检测媒体绑定产品中，是否含有免费产品
        Map<String,List<Product>> cspProductIds = (Map<String,List<Product>>) CacheUtils.get(contentId,"contentProducts4Mobile",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                Map<String,List<Product>> result = new HashMap<String,List<Product>>();
                List<ServiceProduct> serviceProducts =  serviceProductLogicInterface.getServiceProductByContentId(contentId);
                if(serviceProducts != null && serviceProducts.size() > 0) {
                    for(ServiceProduct sp : serviceProducts) {
                        if(sp.getIsFree() != null && sp.getIsFree() == 1) {
                            return true;
                        }
                        Product product = productLogicInterface.get(sp.getProductId());
                        Csp csp  = null;
                        Long cspId = sp.getCspId();
                        if(cspId==null||cspId <=1){
                            //543737280这个ID对应的是河北联通移动版的
                            cspId = ConfigManager.getInstance().getConfig("system.defaultMobileCspId", 543737280L);
                        }
                        if(cspId>=1){
                            csp = (Csp)CacheUtils.get(cspId,"csp",new DataInitWorker(){
                                public Object init(Object key,String cacheName){
                                    try {
                                        return cspLogicInterface.get((Long)key);
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }
                            });
                        }
                        if(product != null &&csp!=null) {
                            //PC产品不参与手机产品鉴权
                            Long mobileProduct = product.getMobileProduct();
                            if(mobileProduct!=null&&mobileProduct==1L) {
                                List<Product> products = result.get(csp.getSpId());
                                if(products == null){
                                    products = new ArrayList<Product>();
                                    result.put(csp.getSpId(),products);
                                }
                                //手机端排除2元产品包，零时办法不是长久之计
                                if (!"8018000401".equals(product.getPayProductNo()) && !"8018000201".equals(product.getPayProductNo()) && !"8018000501".equals(product.getPayProductNo())) {  //手机端排除2元产品包，零时办法不是长久之计
                                    products.add(product);
                                }

                            }
                        }else{
                            if(product==null){
                                logger.error("产品为空："+sp.getProductId());
                            }
                            if(csp==null){
                                logger.error("CSP为空："+sp.getCspId());
                            }
                        }
                    }
                //}else {
                //   logger.debug("没有找到任何产品！");
                }
                return result;
            }
        });
        boolean result = true;
        for(String spId:cspProductIds.keySet()){
            List<Product> products = cspProductIds.get(spId);
            if(products!=null&&products.size()>0){
                List<String> productIds = new ArrayList<String>();
                //先检查是否购买过按次点播并在有效的时间内
                List<String> onceTimeProductIds = new ArrayList<String>();
                for(Product product:products){
                    //包月，按次分开
                    if(ProductLogicInterface.TYPE_FOR_MONTH.equals(product.getType())){
                        productIds.add(product.getPayProductNo());
                    }else{
                        onceTimeProductIds.add(product.getPayProductNo());
                    }
                }
                if(productIds.size()>0){
                    result = false;
                    //包月到vac检查
                    if(checkOrderOfUser(userId,productIds,spId)){
                        return true;
                    }
                }
                if(onceTimeProductIds.size()>0){
                    result = false;
                    //按次到数据库检查
                    if(userBuyLogicInterface.hasBuy(userId,new Date(),contentId,null,null,ProductLogicInterface.TYPE_FOR_ONCE,onceTimeProductIds)){
                        return true;
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public boolean checkPlayPermissions_Pc(String userId,final long contentId) {
        //检查白名单
        String whiteList = ConfigManager.getInstance().getConfig("system.buy.whiteList","15613199253;18631118565;18631110681;18631110330;18603211395;18603210858;18632921367;18631110202");
        if(whiteList!=null&&whiteList.contains(userId)){
            return true;
        }
        //检测媒体绑定产品中，是否含有免费产品
        Map<String,List<Product>> cspProductIds = (Map<String,List<Product>>) CacheUtils.get(contentId,"contentProducts4Pc",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                Map<String,List<Product>> result = new HashMap<String,List<Product>>();
                List<ServiceProduct> serviceProducts =  serviceProductLogicInterface.getServiceProductByContentId(contentId);
                if(serviceProducts != null && serviceProducts.size() > 0) {
                    for(ServiceProduct sp : serviceProducts) {
                        if(sp.getIsFree() != null && sp.getIsFree() == 1) {
                            return true;
                        }
                        Product product = productLogicInterface.get(sp.getProductId());
                        Csp csp  = null;
                        Long cspId = sp.getCspId();
                        if(cspId==null||cspId <=1){
                            //543737280这个ID对应的是河北联通移动版的
                            cspId = ConfigManager.getInstance().getConfig("system.defaultMobileCspId", 543737280L);
                        }
                        if(cspId>=1){
                            csp = (Csp)CacheUtils.get(cspId,"csp",new DataInitWorker(){
                                public Object init(Object key,String cacheName){
                                    try {
                                        return cspLogicInterface.get((Long)key);
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }
                            });
                        }
                        if(product != null &&csp!=null) {
                            //PC产品不参与手机产品鉴权
                            Long mobileProduct = product.getMobileProduct();
                            if(mobileProduct!=null&&mobileProduct==1L) {
                                List<Product> products = result.get(csp.getSpId());
                                if(products == null){
                                    products = new ArrayList<Product>();
                                    result.put(csp.getSpId(),products);
                                }
                                products.add(product);
                            }
                        }else{
                            if(product==null){
                                logger.error("产品为空："+sp.getProductId());
                            }
                            if(csp==null){
                                logger.error("CSP为空："+sp.getCspId());
                            }
                        }
                    }
                    //}else {
                    //   logger.debug("没有找到任何产品！");
                }
                return result;
            }
        });
        boolean result = true;
        for(String spId:cspProductIds.keySet()){
            List<Product> products = cspProductIds.get(spId);
            if(products!=null&&products.size()>0){
                List<String> productIds = new ArrayList<String>();
                //先检查是否购买过按次点播并在有效的时间内
                List<String> onceTimeProductIds = new ArrayList<String>();
                for(Product product:products){
                    //包月，按次分开
                    if(ProductLogicInterface.TYPE_FOR_MONTH.equals(product.getType())){
                        productIds.add(product.getPayProductNo());
                    }else{
                        onceTimeProductIds.add(product.getPayProductNo());
                    }
                }
                if(productIds.size()>0){
                    result = false;
                    //包月到vac检查
                    if(checkOrderOfUser(userId,productIds,spId)){
                        return true;
                    }
                }
                if(onceTimeProductIds.size()>0){
                    result = false;
                    //按次到数据库检查
                    if(userBuyLogicInterface.hasBuy(userId,new Date(),contentId,null,null,ProductLogicInterface.TYPE_FOR_ONCE,onceTimeProductIds)){
                        return true;
                    }
                }
            }
        }
        return result;
    }

    public String calculateUserToken(String phoneNumber) {
        String timeStr = StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        String pwd = ConfigManager.getInstance().getConfig("mobilePhoneTokenPassword", "fortune!@#456");
        try {
            return timeStr+ MD5Utils.getMD5String(timeStr + phoneNumber + pwd);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public boolean verifyCodeForBuy(String userId, String verifyCode) {
        return loginByPhoneNumber(userId,verifyCode,false);
    }

    //查询媒体绑定产品中，用户是否已经订购
    public boolean checkOrderOfUser(String userId,List<String> productIds,String spId) {
        return  VacWorker.getInstance().checkBuy(userId,productIds,UserLogicInterface.VAC_CHECK_PRODUCT_ORDER,spId);
    }
    private void init(){
    }
    @SuppressWarnings("unused")
    public String generateUniKey(HttpServletRequest request){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
    private static final String defaultAppKey = "f7142508e0d869afae6b847da770d40eee154eb9";
    public String getToken(){
        String result;
        ConfigManager config = ConfigManager.getInstance();
        String appKey = config.getConfig("system.security.unicomCenter.appKey",defaultAppKey);//,"23ab78dee4b73c5d92e528a2788ac127e118e087");
        String appSecurity = config.getConfig("system.security.unicomCenter.appSecret","f9d093d8fea100bccd7fea228ea02982d1965763");//,"d68eedb334b2273ff73d138fa7a803d08d70ce35");
        String url = ConfigManager.getInstance().getConfig("system.security.unicomCenter.authenticateUrl", "https://open.wo.com.cn/openapi/authenticate/v1.0");
        APIRequest request = new APIRequest();
        AuthorizationHeader header = new AuthorizationHeader();
        header.setAppKey(appKey);
        header.setAppSecret(appSecurity);
        request.setAuthHeader(header);
        request.setAccept("application/json");
        request.setContentType("application/json");
        request.setUri(url);
        AuthenticateAPIImpl auth = new AuthenticateAPIImpl();
        String logs ="即将访问："+url;
        GetTokenResponse response = auth.getToken(request);
        int statusCode = response.getStatusCode();
        logs +="\n返回状态："+statusCode;
        result = response.getToken();
        logs +="\n返回Token："+result;
        logs+=("\n返回代码：statusCode="+statusCode);
        logs+=("\n返回token:"+response.getToken());
        logs+=("返回数据：\n"+result);
        logger.debug(logs);
        return result;
    }

    public String getUserMobilePhone(String uniKey,String token){
        String url = ConfigManager.getInstance().getConfig("system.security.unicomCenter.getPhoneNumberUrl", "https://open.wo.com.cn/openapi/netplus/getphonenumber/v1.0/")+uniKey;
        APIRequest request = new APIRequest();
        AuthorizationHeader header = new AuthorizationHeader();
        ConfigManager config = ConfigManager.getInstance();
        String appKey = config.getConfig("system.security.unicomCenter.appKey",defaultAppKey);//,"23ab78dee4b73c5d92e528a2788ac127e118e087");
        header.setAppKey(appKey);
        //header.setAppSecret(appSecurity);
        header.setToken(token);
        request.setAuthHeader(header);
        request.setAccept("text/plain");
        request.setContentType("text/plain");
        request.setUri(url);
        NetAPIimpl netApi = new NetAPIimpl();
        logger.debug("即将发起请求："+url);
        String logs ="即将访问："+url;
        GetUidResponse response = netApi.getUid(request);
        int statusCode = response.getStatusCode();
        logs +="\n返回状态："+statusCode;
        logger.debug("返回代码：statusCode="+statusCode);
        String result = null;
        if(statusCode!=200){
            logger.error("无法获取号码："+statusCode+",返回信息如下：\n"+response.getResponseData());
        }else{
            result =  response.getResponseData();
            logs +="\n返回号码："+result;
            if(result!=null){
                result = result.trim();
                int p = result.indexOf(",");
                if(p>0){
                    result = result.substring(0,p);
                }
                if("".equals(result)||"0".equals(result)){
                    result = null;
                }
            }
            logger.debug(logs);
        }
        return result;
    }

}
