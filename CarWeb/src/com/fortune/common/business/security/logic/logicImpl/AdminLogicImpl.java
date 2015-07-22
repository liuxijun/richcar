package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.AdminChannelDaoInterface;
import com.fortune.common.business.security.dao.daoInterface.AdminDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.*;
import com.fortune.common.business.security.model.*;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("adminLogicInterface")
public class AdminLogicImpl extends BaseLogicImpl<Admin> implements
        AdminLogicInterface {

    private AdminDaoInterface adminDaoInterface;

    private AdminRoleLogicInterface adminRoleLogicInterface;

//    private RolePermissionLogicInterface rolePermissionLogicInterface;

    private PermissionLogicInterface permissionLogicInterface;
    private MenuLogicInterface menuLogicInterface;

    private RoleLogicInterface roleLogicInterface;
    private AdminChannelDaoInterface adminChannelDaoInterface;

    @Autowired
    public void setAdminChannelDaoInterface(AdminChannelDaoInterface adminChannelDaoInterface) {
        this.adminChannelDaoInterface = adminChannelDaoInterface;
    }
//    private CspLogicInterface cspLogicInterface;
    @Autowired
    public void setPermissionLogicInterface(
            PermissionLogicInterface permissionLogicInterface) {
        this.permissionLogicInterface = permissionLogicInterface;
    }


    /**
    /**
     * @param adminDaoInterface the adminDaoInterface to set
     */
    @Autowired
    public void setAdminDaoInterface(AdminDaoInterface adminDaoInterface) {
        this.adminDaoInterface = adminDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.adminDaoInterface;
    }
    @Autowired
    public void setAdminRoleLogicInterface(AdminRoleLogicInterface adminRoleLogicInterface) {
        this.adminRoleLogicInterface = adminRoleLogicInterface;
    }
    @Autowired
    public void setRoleLogicInterface(RoleLogicInterface roleLogicInterface) {
        this.roleLogicInterface = roleLogicInterface;
    }

    @Autowired
    public void setMenuLogicInterface(MenuLogicInterface menuLogicInterface) {
        this.menuLogicInterface = menuLogicInterface;
    }

    public Map<String, Permission> searchPermission(Integer operatorid) {
        Map<String, Permission> map = new HashMap<String, Permission>();
        List<Permission> list = permissionLogicInterface.getPermissionOfOperator(operatorid,-1);
        if (list != null)
            for (Permission p : list) {
                if (p != null) {
                    map.put(p.getTarget(), p);
                }
            }
        return map;

    }

    @Autowired
    public void setOperatorRoleLogicInterface(
            AdminRoleLogicInterface adminRoleLogicInterface) {
        this.adminRoleLogicInterface = adminRoleLogicInterface;
    }

    public boolean login(Admin admin) {
        Admin searchBean = new Admin();
        String password = admin.getPassword();
        String userLogin = admin.getLogin();
        if(password==null||"".equals(password.trim())||
                userLogin==null||"".equals(userLogin.trim())){
            logger.error("登录错误，空的账号或者口令：" + admin.getLogin() + "," + admin.getPassword());
            return false;
        }
        searchBean.setLogin(admin.getLogin());
       //searchBean.setPassword(admin.getPassword());
        List<Admin> list = search(searchBean, false);
        boolean loginSuccess = false;
        if(list!=null&&list.size()>0){
            for(Admin a:list){
                if(a.getLogin().equalsIgnoreCase(userLogin)){
                    if(password.equalsIgnoreCase(a.getPassword())){
                        loginSuccess = true;
                        break;
                    }else{
                        logger.debug("登录不对："+password+"!="+a.getPassword());
                    }
                }else{
                    logger.debug("哇，好像啊！难道是同胞胎？"+userLogin+".."+a.getLogin());
                }
            }
        }
        if(!loginSuccess){
            logger.error("登录错误，可能是错误的账号或者口令：" + admin.getLogin() + "," + admin.getPassword());
            return false;
        }
        Admin op = list.get(0);
        if (op == null || op.getStatus() == null || !op.getStatus().equals(STATUS_OK)) {
            logger.error("登录错误，账号可能是被锁定：" + admin.getLogin());
            return false;
        }
        try {

            BeanUtils.copyProperties(admin,op);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
/*  用户权限控制机制发生变化，绑定在不同的csp上，权限不同
        AdminPermission sBean = new AdminPermission();
        sBean.setAdminId(op.getId());
        Map<String, Permission> myPermission = new HashMap<String,Permission>();
        try {
            myPermission.putAll(searchPermission(op.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (op.getLogin().equals("root")) {
            List<Permission> plist = permissionLogicInterface.getAll();
            for (Permission ps : plist) {
                //Permission ps = aPlist;
                myPermission.put(ps.getTarget(), ps);
            }
        }
        admin.setPermissions(myPermission);
//        */
        op.setLastlogintime(new Date());
        logger.debug("用户‘" + op.getRealname() +
                "’登录成功!");
        update(op);
        return true;
    }

    public boolean isLoginExists(String login) {
        Admin searchBean = new Admin();
        searchBean.setLogin(login);
        List<Admin> list = search(searchBean,false);
        if (list != null && list.size() > 0) {
            logger.warn("这个用户Login'" + login + "'已经存在！");
            return true;
        } else {
            logger.warn("这个用户Login'" + login + "'不存在！");
            return false;
        }
    }

    public Admin save(Admin admin){
        Integer id = admin.getId();
        if (id != null && id > 0) {
            //如果是修改，而且口令没有设置，则用旧的口令设置
            String password = admin.getPassword();
            Admin op = get(id);
            if (op != null) {
                if (password == null || "".equals(password.trim())||password.equals(op.getPassword())) {
                    admin.setPassword(op.getPassword());
                    logger.debug("保存时，口令被置空，使用原来的口令！");
                } else {
                    logger.debug("新口令：'" + password + "'");
                    //设置口令历史
                    String oldPassword = admin.getOldpasswordlog() + "|||" + op.getPassword();
                    //如果过长，把前一个口令删了
                    while (oldPassword.length() > 100) {
                        int p = oldPassword.indexOf("|||");
                        if (p >= 0) {
                            oldPassword = oldPassword.substring(p + 3);
                        } else {
                            break;
                        }
                    }
                    logger.debug("设置口令，保存口令历史：" + oldPassword);
                    admin.setOldpasswordlog(oldPassword);
                }
            }
        }
        admin.setModifydate(new Date());
        admin = super.save(admin);
        logger.debug("成功保存数据！");
        return admin;
    }




    /**
     * 将所有的role返回，如果管理员没有权限，则设置selected为false，否则为true
     * @param operatorId 操作员id
     * @return   角色列表
     */
    public List<Role> getRolesWithCheckOperator(Integer operatorId,Integer cspId){
        int roleType = RoleLogicInterface.ROLE_TYPE_CSP;
        if(cspId<=0||cspId==1){
            roleType = RoleLogicInterface.ROLE_TYPE_SYSTEM;
        }
        List<Role> result = roleLogicInterface.getRolesOfType(roleType);
        if(operatorId!=null && operatorId>0){
            List<Role> operatorRoles = adminRoleLogicInterface.getRolesOfAdmin(operatorId,cspId);
            if(result!=null){
                for(Role role:result){
                    role.setSelected(false);
                    for(Role or:operatorRoles){
                       if(role.getRoleid().intValue()==or.getRoleid().intValue()){
                           role.setSelected(true);
                       }
                   }
                }
            }
        }
        return result;
    }

    public void saveOperatorRoles(List<Integer> roleIds,Integer operatorId,Integer cspId){
        this.adminRoleLogicInterface.saveAdminRoles(roleIds,operatorId,cspId);
    }

    //返回登陆管理员所有的能使用的Action方法名
    public List getAllTargetByLogin(String login){
        return adminDaoInterface.getAllTargetByLogin(login);
    }

    public boolean savePassword(Integer operatorId, String oldPwd, String newPwd) {
        Admin op = get(operatorId);
        boolean result = false;
        if(op.getPassword().equals(oldPwd)){
            op = setOldPasswordLog(op);
            op.setPassword(newPwd);
            save(op);
            result = true;
        }
        return result;
    }
     public Admin setOldPasswordLog(Admin op){
         //设置口令历史
         String oldPassword = op.getOldpasswordlog() + "|||" + op.getPassword();
         //如果过长，把前一个口令删了
                    while (oldPassword.length() > 100) {
                        int p = oldPassword.indexOf("|||");
                        if (p >= 0) {
                            oldPassword = oldPassword.substring(p + 3);
                        } else {
                            break;
                        }
                    }
         logger.debug("设置口令，保存口令历史：" + oldPassword);
         op.setOldpasswordlog(oldPassword);
         op.setModifydate(new Date());
         return op;
     }

    public List<Admin> getAdminsOfStatus(Admin admin, PageBean pageBean) {
        String hql = "(status=" + AdminLogicInterface.STATUS_OK + " or status=" + AdminLogicInterface.STATUS_LOCKED + ")";
        List<Admin> adminList = search(admin, pageBean, hql);
        // added by mlwang @2014-10-30，增加管理员角色id序列
        List<AdminRole>  adminRoleList = adminRoleLogicInterface.getAll();
        for( AdminRole adminRole : adminRoleList){
            attachRoleToAdmin(adminList, adminRole);
        }
        return adminList;
    }

    private void attachRoleToAdmin(List<Admin> adminList, AdminRole adminRole){
        if(adminList == null || adminList.size() == 0) return;
        if(adminRole == null) return;

        for(Admin a: adminList){
            if( a.getId().equals(adminRole.getAdminId())){
                a.addRole(adminRole.getRoleId().longValue());
            }
        }
    }

    /**
     * 保存管理员信息：姓名和密码，及角色
     * @param admin 管理员对象
     * @param roles 角色字符串
     * @return 修改后的管理员对象
     */
    public Admin saveAdmin(Admin admin, String roles){
        // 保存基本信息和角色
        Admin oldObject = adminDaoInterface.get(admin.getId());
        if( oldObject == null) return null;

        String realName = admin.getRealname();
        try {
            realName = java.net.URLDecoder.decode(admin.getRealname(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String channels = admin.getSerializedChannel();

        oldObject.setRealname(realName);
        if( admin.getPassword() != null && !admin.getPassword().isEmpty()){
            oldObject.setPassword(admin.getPassword());
        }
        oldObject.setModifydate(new Date());
        oldObject.setCspId(1);
        admin = adminDaoInterface.save(oldObject);

        // 保存角色信息
        if(!admin.getLogin().equals("root")) {
            List<AdminRole> rolesOfAdmin = adminRoleLogicInterface.getRolesOfAdmin(admin.getId());
            if (roles != null) {
                String[] newRoleIds = roles.split(",");
                for (String newRoleId : newRoleIds) {
                    Integer roleId = StringUtils.string2int(newRoleId, -1);
                    boolean roleFound = false;
                    if (roleId > 0) {
                        for (int l = rolesOfAdmin.size() - 1; l >= 0; l--) {
                            AdminRole ar = rolesOfAdmin.get(l);
                            if (roleId.equals(ar.getRoleId())) {
                                rolesOfAdmin.remove(l);
                                roleFound = true;
                                break;
                            }
                        }
                        if (!roleFound) {
                            AdminRole ar = new AdminRole(-1, admin.getId(), roleId, 2);
                            ar = adminRoleLogicInterface.save(ar);
                        }
                    }
                }
            }
            //剩下的角色绑定关系要删除
            for (AdminRole ar : rolesOfAdmin) {
                adminRoleLogicInterface.remove(ar);
            }

/*
        adminRoleLogicInterface.removeByAdminId(admin.getId());
        if(roles !=null && !roles.isEmpty()){
            String[] l = roles.split(",");
            for(String r : l){
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(admin.getId());
                adminRole.setRoleId(Integer.parseInt(r));
                adminRole.setCspId(2);
                adminRoleLogicInterface.save(adminRole);
            }
        }
*/

            adminChannelDaoInterface.removeByAdmin(admin.getId().longValue());
            if (channels != null && !channels.isEmpty()) {
                String[] l = channels.split(",");
                for (String c : l) {
                    AdminChannel adminChannel = new AdminChannel();
                    adminChannel.setAdminId(admin.getId().longValue());
                    adminChannel.setChannelId(Long.parseLong(c));
                    adminChannelDaoInterface.save(adminChannel);
                }
            }
        }

        return admin;
    }

    /**
     * 新建挂你呀
     * @param admin 管理员信息，包括登录名、姓名和密码
     * @param roles 管理员角色
     * @return 新建的管理员对象
     */
    public Admin newAdmin(Admin admin, String roles){
        if( admin == null) return null;

        // 检查同名管理员是否存在
        Admin o = new Admin();
        o.setLogin(admin.getLogin());
        List<Admin> list = search(o, false);
        if( list != null && list.size() > 0) return null;


        String realName = admin.getRealname();
        try {
            realName = java.net.URLDecoder.decode(admin.getRealname(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        admin.setRealname(realName);

        // 频道
        String channels = admin.getSerializedChannel();

        admin.setIsRoot(0);
        admin.setModifydate(new Date());
        admin.setLastlogintime(new Date(0));
        admin.setCspId(2);
        admin.setIsSystem(1);
        admin.setStatus( AdminLogicInterface.STATUS_OK);
        admin = adminDaoInterface.save(admin);

        if(roles !=null && !roles.isEmpty()){
            String[] l = roles.split(",");
            for(String r : l){
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(admin.getId());
                adminRole.setRoleId(Integer.parseInt(r));
                adminRole.setCspId(1);
                adminRoleLogicInterface.save(adminRole);
            }
        }
        
        if(channels !=null && !channels.isEmpty()){
            String[] l = channels.split(",");
            for(String c : l){
                AdminChannel adminChannel = new AdminChannel();
                adminChannel.setAdminId(admin.getId().longValue());
                adminChannel.setChannelId( Long.parseLong( c));
                adminChannelDaoInterface.save(adminChannel);
            }
        }

        return admin;
    }

    public Admin removeAdmin(Admin admin){
        //adminRoleLogicInterface.removeByAdminId(admin.getId());
        //adminDaoInterface.remove(admin.getId());
        Admin oldObject = adminDaoInterface.get(admin.getId());
        if( oldObject == null) return null;

        oldObject.setModifydate(new Date());
        oldObject.setStatus( AdminLogicInterface.STATUS_DELETED);
        return adminDaoInterface.save(oldObject);
    }

    /**
     * 查询管理员详细信息，包括基本信息和关联的
     * @param admin 管理员信息，设置了id或Login都可以查询
     * @return 管理员信息
     */
    public Admin getAdminDetail(Admin admin){
        PageBean pageBean = new PageBean();

        String hql = "(status=" + AdminLogicInterface.STATUS_OK + " or status=" + AdminLogicInterface.STATUS_LOCKED + ") and login='" + admin.getLogin() + "'";
        List<Admin> adminList = search(admin, pageBean, hql);

        if( adminList == null || adminList.size() == 0) return null;
        
        admin = adminList.get(0);
        // added by mlwang @2014-10-30，增加管理员角色id序列
        List<AdminRole> adminRoleList = adminRoleLogicInterface.getRolesOfAdmin(admin.getId());
        //List<Role>  adminRoleList = adminRoleLogicInterface.getRolesOfAdmin(admin.getId(), 1);
        for( AdminRole adminRole : adminRoleList){
            //attachRoleToAdmin(adminList, adminRole);
            admin.addRole(adminRole.getRoleId().longValue());
        }

        List<AdminChannel> channelList = adminChannelDaoInterface.getListByAdmin(admin.getId().longValue());
        for(AdminChannel c : channelList){
            admin.addChannel(c.getChannelId());
        }
        // end of added
        return admin;
    }

    public List<Menu> getAdminMenus(Admin op) {
        return menuLogicInterface.getMenuOfAdmin(op);
    }

    /**
     * get发布管理员可选择的栏目列表
     * @param admin 管理员
     * @return 栏目列表，逗号分隔字串
     */
    public String getAdminGrantChannel(Admin admin){
        List<AdminChannel> channelList = adminChannelDaoInterface.getListByAdmin(admin.getId().longValue());
        String grantChannelStr = "";
        for(AdminChannel c : channelList){
            grantChannelStr += grantChannelStr.isEmpty()? c.getChannelId() : "," + c.getChannelId();
        }

        return grantChannelStr;
    }
}
