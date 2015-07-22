package com.fortune.common.web.security;

import java.util.Date;
import java.util.List;

import com.fortune.common.business.security.logic.logicImpl.MenuChecker;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.util.AppConfigurator;
import com.fortune.util.BeanUtils;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;

@Namespace("/security")
@ParentPackage("default")
@Action(value="admin",results = {
        @Result(name = "success",location = "/common/jsonMessages.jsp")
})
public class AdminAction extends BaseAction<Admin> {
    private static final long serialVersionUID = 3243534534534534l;
    private AdminLogicInterface adminLogicInterface;
    private List<Integer> roleIds;
    private List<Role> roles;
    private String verifyCode;

    @SuppressWarnings("unchecked")
    public AdminAction() {
        super(Admin.class);
    }

    /**
     * @param adminLogicInterface the adminLogicInterface to set
     */

    public void setAdminLogicInterface(
            AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
        setBaseLogicInterface(adminLogicInterface);
    }

    @Action(value = "/logout",results = {
            @Result(name = "success",location = "/security/logout.jsp")
    },interceptorRefs = {@InterceptorRef(value = "defaultStack")})
    public String logout() {
        if (session != null) {
            if(admin!=null){
                writeSysLog("用户退出系统，" + admin.getLogin()+","+admin.getRealname()+",时间：" + StringUtils.date2string(new Date())+"，IP："+getRemoteAddr());
            }else{
                writeSysLog("未知的用户退出系统,时间：" + StringUtils.date2string(new Date())+"，IP："+getRemoteAddr());
            }
            session.remove(Constants.SESSION_ADMIN);
            session.remove(Constants.SESSION_ADMIN_PERMISSION);
            session.clear();
        }
        return SUCCESS;
    }

    @Action(value = "/login",results = {
            @Result(name = "input",location = "/index.jsp"),
            @Result(name = "success",location = "/common/jsonMessages.jsp"),
            @Result(name = "error",location = "/common/jsonMessages.jsp")
    },interceptorRefs = {@InterceptorRef(value = "defaultStack")})
    public String login() {
        log.debug("logon");

        Admin op = obj;
        AppConfigurator config = AppConfigurator.getInstance();
        if(config.getBoolConfig("needCheckVerifyCode",false)){
            if(config.getConfig("autoLoginDebugTime","fortuneDebugTimeAutoInput").equals(verifyCode)){

            }else{
                if((verifyCode==null|| !verifyCode.equalsIgnoreCase((String)session.get("verifyCode")))){
                    //验证码不对
                    setSuccess(false);
                    addActionError("验证码输入错误!");
                    //通知进行称却输出
                    return SUCCESS;
                }
            }
        }
        boolean result= adminLogicInterface.login(op);
        setSuccess(result);
        if(result){
            if((op.getIsRoot()!=null&&op.getIsRoot()==1)||(op.getIsSystem()!=null&&op.getIsSystem()==1)){
                if(config.getBoolConfig("system.compactMode",false)){
                    op.setCspId(2);  //紧凑模式下，设置为默认的那个csp的id
                }else{
                    op.setCspId(1);
                }
            }else{
                op.setCspId(-1);
            }
            admin = op;
            session.put(Constants.SESSION_ADMIN, op);
            session.put(Constants.SESSION_ADMIN_PERMISSION,op.getPermissions());
            try {
                List<Menu> menus = adminLogicInterface.getAdminMenus(op);
                session.put(Constants.SESSION_ADMIN_MENUS,adminLogicInterface.getAdminMenus(op));
                session.put(Constants.SESSION_ADMIN_CAN_VISIT_URLS, MenuChecker.getInstance().formatMenus(menus));
            } catch (Exception e) {
                log.error("用户登录菜单处理失败："+e.getMessage());
                e.printStackTrace();
            }
            writeSysLog("用户:" +op.getLogin()+","+op.getRealname()+
                    "于" + StringUtils.date2string(new Date())+"在"+getRemoteAddr()+
                    "登录成功！");
        }else{
            writeSysLog("发现用户登录失败：" + op.getLogin()+",时间：" + StringUtils.date2string(new Date())+"，IP："+getRemoteAddr());
            addActionError("帐号或者口令输入错误!");
        }
/*
        HttpSession httpSession=ServletActionContext.getRequest().getSession();
        if(httpSession!=null){
            httpSession.setAttribute(Constants.SESSION_ADMIN, op);
            httpSession.setAttribute(Constants.SESSION_ADMIN_PERMISSION, op.getPermissions());
        }
*/
        return SUCCESS;
    }

    @SuppressWarnings("unused")
    public String checkExists() {
        log.debug("in checkExists.");
        setSuccess(true);
        if (obj != null && obj.getLogin() != null && !obj.getLogin().trim().equals("")) {
            setSuccess(adminLogicInterface.isLoginExists(obj.getLogin()));
        }else{
            setSuccess(true);
        }
        return Constants.ACTION_VIEW;
    }

    @SuppressWarnings("unused")
    public String viewSelf(){
        keyId = admin.getId();
        String result = super.view();
        if(obj!=null){
            obj.setPassword("");
        }
        return result;
    }


    @SuppressWarnings("unused")
    public String modifySelf(){
       Admin op = adminLogicInterface.get(admin.getId());
        if(op!=null){
            op.setRealname(obj.getRealname());
            op.setTelephone(obj.getTelephone());
            adminLogicInterface.save(op);
            writeSysLog("操作员保存自己的基本数据，名字："+op.getRealname()+",电话："+op.getTelephone());
        }
        return SUCCESS;
    }

    @SuppressWarnings("unused")
    public String view(){
        String result ="view";
        if(keyId>0){
            obj = adminLogicInterface.get(keyId);
        }else{

        }
        BeanUtils.setDefaultValue(obj,"isSystem",1);
        BeanUtils.setDefaultValue(obj,"isRoot",2);
        BeanUtils.setDefaultValue(obj,"status",AdminLogicInterface.STATUS_OK);
        if(obj!=null&&obj.getIsSystem()!=null&&obj.getIsSystem()==1){
            roles = adminLogicInterface.getRolesWithCheckOperator(keyId,1);
        }
        //如果是查看用户，口令字段置空
        if(obj!=null){
            obj.setPassword("");
            obj.setRoles(roles);
            BeanUtils.setDefaultValue(obj,"areaId", admin.getAreaId());
//            obj.getArea();
        }
        return result;
    }

    @SuppressWarnings("unused")
    public boolean saveOperator(Admin admin){
        log.debug("in save");
        try {
            adminLogicInterface.save(admin);
            String message = "用户'"+ admin.getRealname()+"("+ admin.getLogin()+")'保存成功";
            log.debug(message);
            this.addActionMessage(message);
            return (true);
        } catch (Exception e) {
            String errorMsg = ("用户'"+ admin.getRealname()+"("+ admin.getLogin()+")'保存发生异常："+e.getMessage());
            log.error(errorMsg);
            this.addActionError(errorMsg);
            return(false);
        }
    }

    /**
     * 获取所有管理员的信息
     */
    public String searchAdmin() {
        //  String sqlCondtion ="1=1";
        pageBean.setOrderBy("id");
        objs =adminLogicInterface.getAdminsOfStatus(obj, pageBean);
        return Constants.ACTION_LIST;
    }

    private String saveUserStatus(Admin op,Integer status){
        op.setStatus(status);
        setSuccess(saveOperator(op));
        return SUCCESS;
    }
    public String newPasswordMD5;

    public String getNewPasswordMD5() {
        return newPasswordMD5;
    }

    public void setNewPasswordMD5(String newPasswordMD5) {
        this.newPasswordMD5 = newPasswordMD5;
    }

    public String savePassword(){
        boolean savePasswordSuccessed = adminLogicInterface.savePassword(admin.getId(),
                obj.getPassword(),newPasswordMD5);
        setSuccess(savePasswordSuccessed);
        //boolean savePasswordSuccessed = isSuccess();
        String resultStr;
        if(savePasswordSuccessed){
            resultStr = "修改密码成功";
           addActionMessage(resultStr);
        }else{
            resultStr ="修改密码错误";
           addActionError(resultStr);
        }
        writeSysLog(resultStr);
        return SUCCESS;
    }

  

    @SuppressWarnings("unused")
    public String lock(){
        log.debug("in lock");
        obj = this.getBaseObject(keyId);
        writeSysLog("锁定用户："+obj.getLogin()+","+obj.getRealname()+","+obj.getId());
        return saveUserStatus(obj,AdminLogicInterface.STATUS_LOCKED);
    }

    @SuppressWarnings("unused")
    public String lockSelected(){
        log.debug("in lockSelected");
        for(String key:keys){
            Admin op = getBaseObject(key);
            writeSysLog("锁定用户："+op.getLogin()+","+op.getRealname()+","+op.getId());
            saveUserStatus(op,AdminLogicInterface.STATUS_LOCKED);
        }
        return SUCCESS;
    }

    @SuppressWarnings("unused")
    public String unlock(){
        log.debug("in unlock");
        obj = this.getBaseObject(keyId);
        writeSysLog("解锁用户："+obj.getLogin()+","+obj.getRealname()+","+obj.getId());
        return saveUserStatus(obj,AdminLogicInterface.STATUS_OK);
    }

    @SuppressWarnings("unused")
    public String unlockSelected(){
        log.debug("unlockSelected");
        for(String key:keys){
            Admin op = getBaseObject(key);
            writeSysLog("解锁用户："+op.getLogin()+","+op.getRealname()+","+op.getId());
            saveUserStatus(op,AdminLogicInterface.STATUS_OK);
        }
        return SUCCESS;
    }

    public String save() {
        log.debug("in save");
        super.save();
        if(obj!=null&&obj.getIsSystem()!=null&&obj.getIsSystem()==1){
             adminLogicInterface.saveOperatorRoles(roleIds,obj.getId(),0);
        }
        writeSysLog("保存用户：用户名="+obj.getLogin()+","+obj.getId());
        return Constants.ACTION_SAVE;
    }

    @SuppressWarnings("unused")
    public List<Integer> getRoleIds() {
        return roleIds;
    }

    @SuppressWarnings("unused")
    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    @SuppressWarnings("unused")
    public List<Role> getRoles() {
        return roles;
    }

    @SuppressWarnings("unused")
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    private String serializedRole;

    public String getSerializedRole() {
        return serializedRole;
    }

    public void setSerializedRole(String serializedRole) {
        this.serializedRole = serializedRole;
    }

    public String saveAdmin(){
        if( obj != null){
            obj = adminLogicInterface.saveAdmin(obj, serializedRole);
            writeSysLog("保存对管理员的修改："+obj.getRealname()+","+obj.getLogin());
        }
        return SUCCESS;
    }
  
    public String newAdmin(){
        if( obj != null){
            Admin a = adminLogicInterface.newAdmin(obj, serializedRole);
            if( a == null){
                setSuccess(false);
                //addActionError("同登录名管理员已经存在/存在过！");
                //setData()
            }
        }
        return SUCCESS;
    }

    public String removeAdmin(){
        if( obj != null){
            obj = adminLogicInterface.removeAdmin(obj);
            writeSysLog("删除管理员："+obj.getRealname()+","+obj.getLogin());
        }
        return SUCCESS;
    }

    /**
     * 查询管理员详情，包括发布管理员可以发布的内容的栏目列表
     * @return "success"
     */
    public String getAdminDetail(){
        if( obj != null){
            obj = adminLogicInterface.getAdminDetail(obj);
            setSuccess(true);
        }else{
            setSuccess(false);
        }

        return SUCCESS;
    }

    /**
     * 获得当前管理员的可发布栏目列表
     * @return SUCCESS
     */
    public String getGrantChannel(){
        Admin a = (Admin)session.get(Constants.SESSION_ADMIN);
        if( a != null ){
            obj.setSerializedChannel(adminLogicInterface.getAdminGrantChannel(a));
        }
        return SUCCESS;
    }

    /**
     * 管理员自己修改姓名和密码，如果密码为空，保留原密码
     * @return "success"
     */
    public String modSelf(){
       Admin op = null;
        try{
        op = adminLogicInterface.get(obj.getId());
        }catch(Exception e){
            // do nothing
        }
        if(op!=null){
            op.setRealname(obj.getRealname());
            if(obj.getPassword() != null && !obj.getPassword().isEmpty()){
                op.setPassword(obj.getPassword());
            }
            adminLogicInterface.save(op);
            Admin sessionOp = (Admin)session.get(Constants.SESSION_ADMIN);
            if(sessionOp != null){
                sessionOp.setRealname(op.getRealname());
            }
            setSuccess(true);
            writeSysLog("操作员保存自己的数据，名字："+op.getRealname()+",电话："+op.getTelephone());
        }else{
            setSuccess(false);
            addActionError("老大，没找到你！");
        }
        return SUCCESS;
    }
}
