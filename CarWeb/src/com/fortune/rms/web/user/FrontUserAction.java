package com.fortune.rms.web.user;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.user.logic.logicInterface.BookMarkLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.AppConfigurator;
import com.fortune.util.BeanUtils;
import com.fortune.util.JsonUtils;
import com.fortune.util.MD5Utils;
import com.fortune.wsdl.zjlib.ZjLibAuth;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-29
 * Time: 13:05:30
 * ǰ̨�û�����Action
 */
@Namespace("/user")
@ParentPackage("default")
@Action(value = "frontUser")
/*@Results({
        @Result(name = "loginOut",location = "../index.html")
})*/
public class FrontUserAction extends BaseAction<FrontUser> {
    private FrontUserLogicInterface frontUserLogicInterface;
    private UserLoginLogicInterface userLoginLogicInterface;
    private UserFavoritesLogicInterface userFavoritesLogicInterface;
    private UserLogicInterface userLogicInterface;
    private BookMarkLogicInterface bookMarkLogicInterface;
    private ChannelLogicInterface channelLogicInterface;
    private String userIdArray;

    public String getUserIdArray() {
        return userIdArray;
    }

    public void setUserIdArray(String userIdArray) {
        this.userIdArray = userIdArray;
    }

    public void setFrontUserLogicInterface(FrontUserLogicInterface frontUserLogicInterface) {
        this.frontUserLogicInterface = frontUserLogicInterface;
    }

    public void setUserLoginLogicInterface(UserLoginLogicInterface userLoginLogicInterface) {
        this.userLoginLogicInterface = userLoginLogicInterface;
    }

    public void setUserLogicInterface(UserLogicInterface userLogicInterface) {
        this.userLogicInterface = userLogicInterface;
    }

    public void setUserFavoritesLogicInterface(UserFavoritesLogicInterface userFavoritesLogicInterface) {
        this.userFavoritesLogicInterface = userFavoritesLogicInterface;
    }

    public void setBookMarkLogicInterface(BookMarkLogicInterface bookMarkLogicInterface) {
        this.bookMarkLogicInterface = bookMarkLogicInterface;
    }

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public FrontUserAction() {
        super(FrontUser.class);
    }

    @Action(value = "/search")
    public void searchUsers(){
        Long orgId = Long.parseLong(getRequestParam("orgId", "-1"));
        String searchValue = getRequestParam("search", "");
        List<FrontUser> l = frontUserLogicInterface.searchUsers(orgId, searchValue,pageBean);

        directOut(JsonUtils.getListJsonString("users", l, "totalCount", pageBean.getRowCount()));
    }

    @Action(value = "/unauditUsers")
    public void searchUnAuditUsers(){
        String searchValue = getRequestParam("search", "");
        List<FrontUser> l = frontUserLogicInterface.searchUnAuditUsers(searchValue,pageBean);

        directOut(JsonUtils.getListJsonString("users", l, "totalCount", pageBean.getRowCount()));
    }

    /**
     * ����û�����userIdArray�е��û�����Ϊ����״̬
     * @return SUCCESS
     */
    @Action(value = "/auditUsers")
    public String auditUsers(){
        String[] idArray = userIdArray.split(",");
        for(String s: idArray){
            frontUserLogicInterface.updateUserStatus(s, FrontUser.USER_STATUS_NORMAL);
        }
        writeSysLog("����û���" + userIdArray);
        return SUCCESS;
    }

    @Action(value = "/n")
    public String newUser(){
        if( obj == null ){
            setSuccess(false);
            addActionError("�û���ϢΪ��");
            return "success";
        }

        // ���ͬId���Ƿ����
        FrontUser u = new FrontUser();
        u.setUserId(obj.getUserId());
        List<FrontUser> l = frontUserLogicInterface.search(u);
        if( l != null && l.size() > 0){
            setSuccess(false);
            addActionError("ͬID�û��Ѿ�����");
            return "success";
        }

        String realName = obj.getName();
        try {
            realName = java.net.URLDecoder.decode(obj.getName(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.setName(realName);
        obj.setCreateTime(new Date());
        obj.setLastModified(new Date());
        obj.setLogonTimes(0L);
        obj.setStatus(FrontUser.USER_STATUS_NORMAL);
        frontUserLogicInterface.save(obj);
        setSuccess(true);

        writeSysLog("�������û���" + realName);

        return "success";
    }

    @Action(value = "/r")
    public String removeUser(){
        if( obj != null && obj.getUserId() != null){
            // �������������Ϣ
            frontUserLogicInterface.remove(obj);
            writeSysLog("ɾ���û���" + obj.getUserId());
            setSuccess(true);
        }else{
            setSuccess(false);
            addActionError("û���ҵ�Ҫɾ�����û�ID");
        }

        return "success";
    }

    /**
     * ɾ���ܶ��û�
     * @return SUCCESS
     */
    @Action(value = "/br")
    public String batchRemoveUser(){
        String[] idArray = userIdArray.split(",");
        for(String s: idArray){
            FrontUser user = new FrontUser();
            user.setUserId(s);
            frontUserLogicInterface.remove(user);
        }
        writeSysLog("ɾ���û���" + userIdArray);
        return SUCCESS;
    }

    @Action(value = "/g")
    public String getUser(){
        if( obj != null && obj.getUserId() != null){
            obj = frontUserLogicInterface.get(obj.getUserId());
            setSuccess(true);
        }else{
            setSuccess(false);
            addActionError("û���ҵ��û�ID");
        }

        return "success";
    }

    @Action(value = "/m")
    public String modifyUser(){
        if( obj == null ){
            setSuccess(false);
            addActionError("�û���ϢΪ��");
            return "success";
        }

        // ���ͬId���Ƿ����
        FrontUser u = new FrontUser();
        u.setUserId(obj.getUserId());
        List<FrontUser> l = frontUserLogicInterface.search(u);

        String realName = obj.getName();
        try {
            realName = java.net.URLDecoder.decode(obj.getName(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.setName(realName);
        if( l!= null && l.size() > 0){
            u = l.get(0);
            obj.setCreateTime(u.getCreateTime());
            obj.setLogonTimes(u.getLogonTimes());
            obj.setLastModified(new Date());
            obj.setStatus(u.getStatus());
            if( "".equals(obj.getPassword().trim())  ){
                obj.setPassword(u.getPassword());
            }
        }else{
            obj.setCreateTime(new Date());
            obj.setLastModified(new Date());
            obj.setLogonTimes(0L);
            obj.setStatus(FrontUser.USER_STATUS_NORMAL);
        }
        frontUserLogicInterface.save(obj);
        writeSysLog("�޸��û���" + obj.getUserId() + "������Ϣ");
        setSuccess(true);
        return "success";
    }

    @Action(value = "/status")
    public String setUserStatus(){
        if( obj == null ){
            setSuccess(false);
            addActionError("�û���ϢΪ��");
            return "success";
        }

        // ���ͬId���Ƿ����
        FrontUser u = new FrontUser();
        u.setUserId(obj.getUserId());
        List<FrontUser> l = frontUserLogicInterface.search(u);

        if( l==null || l.size() ==0){
            setSuccess(false);
            addActionError("û���ҵ��û�");
        }else{
            frontUserLogicInterface.updateUserStatus(obj.getUserId(), obj.getStatus());
            if( FrontUser.USER_STATUS_LOCKED.equals( obj.getStatus() )){
                writeSysLog("�����û���" + obj.getUserId());
            }else if(FrontUser.USER_STATUS_NORMAL.equals( obj.getStatus())){
                writeSysLog("���û���" + obj.getUserId() + "����Ϊ����״̬");
            }else{
                writeSysLog("���û���" + obj.getUserId() + "����Ϊ" + obj.getStatus());
            }
            setSuccess(true);
        }
        return "success";
    }


    /**
     * ǰ̨�û���¼
     * @return "success"
     */
    @Action(value = "/logon")
    public String frontUserLogon(){
        boolean userLogined = false;
        boolean errorMessageAdded =false;
        FrontUser u = new FrontUser();
        String ip= getRemoteAddr();
        AppConfigurator config=AppConfigurator.getInstance();
        if( obj == null || obj.getUserId() == null || obj.getUserId().isEmpty() ||
                obj.getPassword() == null ){
            setSuccess(false);
            addActionError("��¼��ϢΪ�ջ�������");
        }else{
            String loginType = config.getConfig("system.frontUser.loginType", "local");
            if("zjlib".equals(loginType)){
                log.debug("��֤��ʽ����ͼ����������Ӧ�ӿ�.....");
                u = ZjLibAuth.auth(obj.getUserId(),obj.getPassword(),ip);
                if(u==null){
                    log.warn("��֤���ʧ�ܣ�"+ip+","+obj.getUserId()+",pwd="+obj.getPassword());
                    UserLogin loginLog = new UserLogin(-1, obj.getUserId(), new Date(),
                            -1, ip,obj.getUserId(),
                            "�û�ͨ����ͼSOAP�ӿڵ�¼ʧ��",-1);
                    userLoginLogicInterface.save(loginLog);
                }else{
                    obj = u;
                    userLogined = true;
                    userLoginLogicInterface.save(new UserLogin(-1, u.getUserId(), new Date(),
                                obj.getTypeId(), ip, u.getName(),
                                "�û�ͨ��SOAP�ӿڵ�¼",u.getOrganizationId()));
                }
            }else if("sms".equals(loginType)){
                userLogined = userLogicInterface.loginByPhoneNumber(obj.getUserId(),obj.getPassword(),true);
                if(userLogined){
                    u = obj;
                    u.setPhone(obj.getUserId());
                    u.setTypeId(0L);
                    u.setName(obj.getUserId());
                    u.setLoginType(FrontUser.USER_LOGIN_TYPE_SMS);
                }
            }else if("local".equals(loginType)){
                u.setUserId(obj.getUserId().toLowerCase());
                List<FrontUser> l = frontUserLogicInterface.search(u);
                if( l == null || l.size() == 0){
                    userLogined=false;
                }else{
                    // �ȶ�����
                    String pwd = obj.getPassword();
                    if(pwd!=null&&pwd.length()<32){
                    //��ͼ�����봫�ͣ��������������ͼ������Ҫ��һ���ǲ���32λ�ַ����ȵ�MD5�ַ���
                        try {
                            pwd = MD5Utils.getMD5String(pwd);
                            obj.setPassword(pwd);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                    for(FrontUser user:l){
                        u=user;
                        if( u.getPassword().equals(obj.getPassword())&&u.getUserId().equalsIgnoreCase(obj.getUserId())){
                            // ����ǲ��Ǳ�����
                            if( FrontUser.USER_STATUS_NORMAL.equals(u.getStatus())){
                                // ��¼
                                u = user;
                                userLogined = true;
                                Long type = obj.getTypeId();
                                Long areaId = u.getOrganizationId();
                                if(type==null){
                                    type = 0L;
                                }
                                if(areaId==null){
                                    areaId = -1L;
                                }
                                userLoginLogicInterface.save(new UserLogin(-1, u.getUserId(), new Date(),
                                        type, ip, u.getName(),
                                        "�û�ͨ��ҳ���¼",areaId));
                                break;
                            }else{
                                setSuccess(false);
                                if(  FrontUser.USER_STATUS_LOCKED.equals(u.getStatus())) {
                                    addActionError("�����˺ű�����������ϵ����Ա��");
                                }else if(FrontUser.USER_STATUS_UN_AUDITED.equals(u.getStatus())){
                                    addActionError("�����˺���δͨ����ˣ�");
                                }
                                errorMessageAdded = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(!userLogined){
            setSuccess(false);
            if(!errorMessageAdded){
                addActionError("��֤ʧ�ܣ������Ǵ���ĵ�¼���������룡����ϸ�������ԣ�");
            }
            log.warn("�û���¼ʧ�ܣ�" + obj.getUserId() + "," + obj.getPassword());
            //
        } else {
            session.put(Constants.SESSION_FRONT_USER, u);
            // �����û����Թۿ�������Ƶ����������Session��
            session.put(Constants.SESSION_FRONT_USER_CHANNEL, frontUserLogicInterface.getUserAuthorizedChannel( u) );
            //�����û����Թۿ���һ��Ƶ��,������Session��
            List<Channel> visibleChannelList = frontUserLogicInterface.
                    getTopLevelChannel(frontUserLogicInterface.getUserAuthorizedChannel( u));
            session.put(Constants.SESSION_FRONT_USER_TOP_CHANNEL,visibleChannelList);
            log.debug("�û���" +u.getName()+","+u.getUserId()+"����¼�ɹ�");
            if(obj.getTypeId()==null){
                obj.setTypeId(5L);
            }
            if(u.getOrganizationId()==null){
                u.setOrganizationId(0L);
            }
            obj.setName(u.getName());
        }
        return "success";
    }

    /**
     * ע���������Ƿ�ע���ɹ����ʺ�ajax����
     * @return success
     */
    @Action(value = "/logoff")
    public String frontUserLogoff(){
        if(session != null) {
            session.clear();
            ActionContext context = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest)context.get(StrutsStatics.HTTP_REQUEST);
            request.getSession().invalidate();
        }
        setSuccess(true);
        return SUCCESS;
    }

    /**
     * ���ص�¼�û���Ϣ
     * @return SUCCESS
     */
    public String userInfo(){
        if(session.get(Constants.SESSION_FRONT_USER) == null){
            obj.setUserId("");
            setSuccess(false);
        }else {
            obj = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            setSuccess(true);
        }
        return SUCCESS;
    }

    public String userFrom;

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public void frontUserLoginOut(){
        if(session != null) {
            session.remove(Constants.SESSION_FRONT_USER);
            session.remove(Constants.SESSION_FRONT_USER_TOP_CHANNEL);
            session.remove(Constants.SESSION_FRONT_USER_CHANNEL);
        }

        ActionContext context = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse)context.get(StrutsStatics.HTTP_RESPONSE);
        Cookie cookie = new Cookie("userLoginId","");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        Cookie pwd = new Cookie("userLoginPwd","");
        cookie.setMaxAge(0);
        response.addCookie(pwd);
        try {
            if(userFrom == null) { //PC��δ��userFrom����
                response.sendRedirect("../index.html");
                return;
            }
            response.sendRedirect("../index_"+userFrom+".html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*return "login";*/
    }

    /**

     * ��ȡǰ̨�û����Թۿ��ĵ�һ����Ŀ�б�
     * ѡ��parentIdΪ��������Ŀ�����ֻ��һ�����صڶ����
     */
    @Action(value = "/topLevelChannel")
    public void getTopLevelChannel(){
        List<Channel> visibleChannelList;
        if( session.get(Constants.SESSION_FRONT_USER_CHANNEL) != null){
            //FrontUser frontUser = (FrontUser)session.get(Constants.SESSION_FRONT_USER);
            visibleChannelList = frontUserLogicInterface.getTopLevelChannel(
                    (List)session.get(Constants.SESSION_FRONT_USER_CHANNEL));
        }else{
            visibleChannelList = null;
        }

        directOut(JsonUtils.getListJsonString("channels", visibleChannelList, "totalCount",
                visibleChannelList == null ? 0 : visibleChannelList.size()));
    }

    public String newPwd;

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String frontUserUpdatePwd() {
        if( obj == null || obj.getUserId() == null || obj.getUserId().isEmpty() ||
                obj.getPassword() == null ){
            setSuccess(false);
            addActionError("�û���ϢΪ�ջ�������");
        }else{
            FrontUser u = new FrontUser();
            u.setUserId(obj.getUserId());
            List<FrontUser> l = frontUserLogicInterface.search(u);
            if( l == null || l.size() == 0){
                setSuccess(false);
                addActionError("�û������ڣ�");
            }else{
                // �ȶ�����
                u = l.get(0);
                if( u.getPassword().equals(obj.getPassword())){
                    u.setPassword(newPwd);
                    frontUserLogicInterface.save(u);
                    if(session != null) {
                        session.remove(Constants.SESSION_FRONT_USER);
                        session.remove(Constants.SESSION_FRONT_USER_TOP_CHANNEL);
                        session.remove(Constants.SESSION_FRONT_USER_CHANNEL);
                    }
                }else{
                    setSuccess(false);
                    addActionError("���������,�޸�ʧ�ܣ�");
                }
            }
        }

        return "success";
    }

    public String save() {
        FrontUser frontUser = new FrontUser();
        frontUser.setUserId(obj.getUserId());
        List<FrontUser> list = frontUserLogicInterface.search(frontUser);
        if(list != null && list.size() > 0 ) {
            setSuccess(false);
            addActionError("�û��Ѵ��ڣ�");
        } else {
            frontUser = obj;
          // BeanUtils.setDefaultValue(obj, "Name", "�Ӱ�");
            // frontUser.setName(obj.getName());
            //frontUser.setOrganizationId(0l);
            frontUser.setTypeId(4l);//Ĭ�������û�
            frontUser.setCreateTime(new Date());
            frontUser.setStatus(FrontUser.USER_STATUS_UN_AUDITED);
            frontUserLogicInterface.save(frontUser);
        }

        return "success";
    }

    //��֤ע�� ���û�ID�Ƿ����
    @SuppressWarnings("unused")
    public String checkExists() {
        log.debug("in checkExists.");
        setSuccess(true);
        if (obj != null && obj.getUserId() != null && !obj.getUserId().trim().equals("")) {
            setSuccess(frontUserLogicInterface.isUserExists(obj.getUserId()));
        }else{
            setSuccess(true);
        }
        return Constants.ACTION_VIEW;
    }
    public String checkLoginName(){
        boolean isExisted = frontUserLogicInterface.checkLoginName(obj);
        //JsonUtils jsonUtils=new JsonUtils();
        directOut(String.valueOf(isExisted));
        return null;
    }

    public String getCollectionContents() {
        List<Content> contents = userFavoritesLogicInterface.getContentsOfUser(obj.getUserId(),pageBean);
        String result = "{\"success\":\"true\",\"contents\":"+ JsonUtils.getJsonString(contents)+"}";
        directOut(result);
        return  null;
    }

    public String getPlayLogs() {
        List<Content> contents = bookMarkLogicInterface.getBookMarkOfUser(obj.getUserId(),pageBean);
        String result = "{\"success\":\"true\",\"contents\":"+ JsonUtils.getJsonString(contents)+"}";
        directOut(result);
        return  null;
    }

    /**
     * ����
     */
    @Action(value = "heartBeat")
    public void heartBeat(){
        // client ajax hear beat do nothing
    }

    //�Ӱ�Start
    private String channelIds;

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public String getIndexList() {
        List<Map<String,Object>> contents = frontUserLogicInterface.getIndexList(channelIds,pageBean);
        String result = "{\"success\":\"true\",\"data\":"+ JsonUtils.getJsonString(contents)+"}";
        directOut(result);
        return null;
    }

    private long channelId;

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getColumnList() {
        List<Map<String,Object>> contents = frontUserLogicInterface.getColumnList(channelId,pageBean);
        Channel channel = channelLogicInterface.get(channelId);
        String result = "{\"success\":\"true\",\"data\":"+ JsonUtils.getJsonString(contents)+",\"channelName\":\""+channel.getName()+"\"}";
        directOut(result);
        return null;
    }
    //�Ӱ�End
}
