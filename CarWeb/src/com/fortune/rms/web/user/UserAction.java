package com.fortune.rms.web.user;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.UserBuyLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface;
import com.fortune.rms.business.system.model.PhoneRange;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface;
import com.fortune.rms.business.user.model.User;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.AppConfigurator;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import com.fortune.vac.Command;
import com.fortune.vac.ErrorCode;
import com.fortune.vac.VacWorker;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11   Namespace�����ռ�       ParentPackage������
 * Time: ����2:15
 */
@Namespace("/user")
@ParentPackage("default")

@Action(value="user")
@Results({
        @Result(name = "listUser",location = "/system/deviceListFiles.jsp"),
        @Result(name="phoneLogin",location = "/user/phoneLogin.jsp")
})
public class UserAction extends BaseAction<User> {
    UserLogicInterface userLogicInterface;
    public JSONObject jsonList;

    //��֤ע�� ���û�ID�Ƿ����
    @SuppressWarnings("unused")
    public String checkExists() {
        log.debug("in checkExists.");
        setSuccess(true);
        if (obj != null && obj.getLogin() != null && !obj.getLogin().trim().equals("")) {
            setSuccess(userLogicInterface.isUserExists(obj.getLogin()));
        }else{
            setSuccess(true);
        }
        return Constants.ACTION_VIEW;
    }

    //�����û�����Ϣ���޸��������õģ�
//    @SuppressWarnings("unused")
//    public String save() {
//        log.debug("in save");
//        super.save();
//        if(obj!=null&&obj.getIsSystem()!=null&&obj.getIsSystem()==1){
//            adminLogicInterface.saveOperatorRoles(roleIds,obj.getId(),1);
//        }
//        writeSysLog("�����û����û���="+obj.getLogin()+","+obj.getId());
//        return Constants.ACTION_SAVE;
//    }
    public void setUserLogicInterface(UserLogicInterface userLogicInterface) {
        this.userLogicInterface = userLogicInterface;
        setBaseLogicInterface(userLogicInterface);
    }

    public UserAction() {
        super(User.class);
    }

    private ProductLogicInterface productLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private UserBuyLogicInterface userBuyLogicInterface;
    private UserLoginLogicInterface userLoginLogicInterface;
    private PhoneRangeLogicInterface phoneRangeLogicInterface;
    private ClientLogLogicInterface clientLogLogicInterface;

    public void setUserBuyLogicInterface(UserBuyLogicInterface userBuyLogicInterface) {
        this.userBuyLogicInterface = userBuyLogicInterface;
    }

    public void setProductLogicInterface(ProductLogicInterface productLogicInterface) {
        this.productLogicInterface = productLogicInterface;
    }

    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

/*
    public void setVacLogLogicInterface(VacLogLogicInterface vacLogLogicInterface) {
        this.vacLogLogicInterface = vacLogLogicInterface;
    }
*/

    public UserLoginLogicInterface getUserLoginLogicInterface() {
        return userLoginLogicInterface;
    }

    public void setUserLoginLogicInterface(UserLoginLogicInterface userLoginLogicInterface) {
        this.userLoginLogicInterface = userLoginLogicInterface;
    }

    public PhoneRangeLogicInterface getPhoneRangeLogicInterface() {
        return phoneRangeLogicInterface;
    }

    public void setPhoneRangeLogicInterface(PhoneRangeLogicInterface phoneRangeLogicInterface) {
        this.phoneRangeLogicInterface = phoneRangeLogicInterface;
    }

    public ClientLogLogicInterface getClientLogLogicInterface() {
        return clientLogLogicInterface;
    }

    public void setClientLogLogicInterface(ClientLogLogicInterface clientLogLogicInterface) {
        this.clientLogLogicInterface = clientLogLogicInterface;
    }

    // ��ѯ�û���ϸ��Ϣ
//���ַ�json ��ΪString �����������
    public String view(){
        log.debug("in view");
        try {
            if (keyId != null) {
                log.debug("׼����ȡ����,����Ϊ��" + keyId);
                obj = userLogicInterface.getUserById(keyId.longValue());
                log.debug("obj:"+obj);
            }
        } catch (Exception e) {
            log.error("BaseAction��getBaseObjectʱ����ͼ��ȡBean�����쳣��" + e.getMessage());
        }
        String result = JsonUtils.getJsonString(obj,"obj.");
        String t = result.replace(" 00:00:00","") ;
        String res=t.replace("{","{success:\"true\",data:{");
        String res2=res.replace("}","},msg:\"[]\",error:\"[]\"}");
        //���ظ�ҳ��
        directOut(res2);
        return null;
    }

    public String login(){
        obj = userLogicInterface.checkLoginNameAndPassWord(obj);
        if(obj != null){
            session.put("@userId",obj.getUserName());
            session.put("userId",obj.getUserName());

            directOut("true");
        }else{
            directOut("false");
        }
        return null;
    }


    public String checkLoginName(){
        boolean isExisted = userLogicInterface.checkLoginName(obj);
        //JsonUtils jsonUtils=new JsonUtils();
        directOut(String.valueOf(isExisted));
        return null;
    }

    String token;
    public String phoneLogin(){
        setSuccess(userLogicInterface.loginByPhoneNumber(obj.getTel(),obj.getVerifyCode(),true));
        if(isSuccess()) {
            session.put(Constants.USER_PHONE_NUMBER,obj.getTel());
            session.put("userPhone",obj.getTel());
            long status = UserLoginLogicInterface.LOGIN_STATUS_FROM_SMS_VERIFY_CODE;
            String desp = "��ȡ��֤���½";
            AppConfigurator configurator = AppConfigurator.getInstance();
            if(configurator.getBoolConfig("needCheckClient",false)){
                if(UserLogicInterface.USER_STATUS_LOGIN_FROM_CLIENT.equals(obj.getStatus())){
                    token = userLogicInterface.calculateUserToken(obj.getTel());
                    //�ͻ��˵�½
                    status = UserLoginLogicInterface.LOGIN_STATUS_FROM_CLIENT;
                    desp = "�ͻ��˵�½";
                }else{
                    token = "";
                }
            }else{
                token = userLogicInterface.calculateUserToken(obj.getTel());
            }
           // List<PhoneRange> phoneRangeList = phoneRangeLogicInterface.getAll();
            UserLogin userLogin = new UserLogin();
            userLogin.setLogin(obj.getTel());
            userLogin.setTel(obj.getTel());
            PhoneRange phoneRange = phoneRangeLogicInterface.getPhoneRangeOfPhone(StringUtils.string2long(obj.getTel(),-1));
            if(phoneRange!=null){
                userLogin.setAreaId(phoneRange.getAreaId());
            }
            //ҳ���½
            userLogin.setLoginStatus(status);
            userLogin.setDesp(desp);
            userLogin.setLoginTime(new Date());
            userLoginLogicInterface.save(userLogin);
        }else{
            token = "";
            addActionError("������ֻ����������֤�룬����ϸ��飡");
            session.remove(Constants.USER_PHONE_NUMBER);
        }

        return "phoneLogin";
    }

    public String createBuyVerifyCode(){
        String createVerifyCodeResult = userLogicInterface.createVerifyCode(obj.getTel(),true);
        setSuccess(createVerifyCodeResult!=null&&!"".equals(createVerifyCodeResult.trim()));
        addActionMessage(createVerifyCodeResult);
        return "view";
    }
    public String createVerifyCode(){
        String createVerifyCodeResult = userLogicInterface.createVerifyCode(obj.getTel());
        setSuccess(createVerifyCodeResult!=null&&!"".equals(createVerifyCodeResult.trim()));
        addActionMessage(createVerifyCodeResult);
        return "view";
    }
    public JSONObject getJsonList() {
        return jsonList;
    }

    public void setJsonList(JSONObject jsonList) {
        this.jsonList = jsonList;
    }
    //����ֻ��Ż���
    public String cleanUser(){
        session.remove(Constants.USER_PHONE_NUMBER);
        String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"true\",\"objs\":[]}";
        directOut(result);
        return null;
    }

    private String userId;
    private long contentId;
    private String productId;
    private long cspId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCspId(long cspId) {
        this.cspId = cspId;
    }

    private String verifyCode;

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String checkPlayPermissions() {
        setSuccess(userLogicInterface.checkPlayPermissions(userId,contentId));
        return Constants.ACTION_VIEW;
    }

    public String checkPlayPermissions_Pc() {
        setSuccess(userLogicInterface.checkPlayPermissions_Pc(userId,contentId));
        return Constants.ACTION_VIEW;
    }

    private long operationType;

    public void setOperationType(long operationType) {
        this.operationType = operationType;
    }

    public String operateOrder() {
//        setSuccess(true);
        if(verifyCode==null|| !userLogicInterface.verifyCodeForBuy(userId, verifyCode)) {
            setSuccess(false);
            addActionMessage("��֤�����"+verifyCode+",�����¶�����");
            return Constants.ACTION_VIEW;
        }
        int resultCode;
        String operateOrderMsg = "����ʧ�ܣ���ȷ���������ͣ�";
        if(operationType > 0 ) {
            if(operationType == 1) {
                operateOrderMsg = "����ʧ�ܣ����Ժ��ٴγ��Զ�����";
            } else {
                operateOrderMsg = "�˶�ʧ�ܣ����Ժ��ٴγ����˶���";
            }

            if(productId == null || !"".equals(productId.trim())) {
                Csp csp = cspLogicInterface.get(cspId);
                if(csp != null) {
                    log.debug("csp:"+csp.getName()+"spId:"+csp.getSpId());
                    Product product = productLogicInterface.getProductByPayId(productId);
                    if(product!=null){
                        if(ProductLogicInterface.TYPE_FOR_ONCE.equals(product.getType())){
                            operationType = Command.OPERATE_TYPE_VOD;
                        }else if(ProductLogicInterface.TYPE_FOR_MONTH.equals(product.getType())){
                            operationType = Command.OPERATE_TYPE_SUBSCRIBE;
                        }
                        log.debug("׼��������"+product.getName());
                        resultCode = VacWorker.getInstance().operateOrder(userId,productId, operationType, csp.getSpId());
                        if(resultCode == 0) {
                            setSuccess(true);
                            userBuyLogicInterface.saveUserBuyLog(userId,productId,contentId,contentPropertyId,channelId,csp,productLogicInterface.getProductByPayId(productId));
                        } else {
                            setSuccess(false);

                            addActionMessage("����ʧ�ܣ�"+ ErrorCode.getErrorMessage(resultCode)+"��");
                            //addActionMessage(operateOrderMsg);

                        }
                    }
                } else {
                    setSuccess(false);
    //                addActionMessage("����ʧ�ܣ�δ֪�������ṩ�̣�");
                    addActionMessage(operateOrderMsg);
                }
            }else {
                setSuccess(false);
                addActionMessage(operateOrderMsg);
    //            addActionMessage("����ʧ�ܣ�û���ҵ���Ӧ�Ĳ�Ʒ��");
            }
        } else {
            setSuccess(false);
            addActionMessage(operateOrderMsg);
        }
        return Constants.ACTION_VIEW;
    }

    private Long channelId;
    private Long contentPropertyId;
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getContentPropertyId() {
        return contentPropertyId;
    }

    public void setContentPropertyId(Long contentPropertyId) {
        this.contentPropertyId = contentPropertyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
