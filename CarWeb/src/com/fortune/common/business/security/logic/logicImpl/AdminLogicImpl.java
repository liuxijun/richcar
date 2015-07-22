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
            logger.error("��¼���󣬿յ��˺Ż��߿��" + admin.getLogin() + "," + admin.getPassword());
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
                        logger.debug("��¼���ԣ�"+password+"!="+a.getPassword());
                    }
                }else{
                    logger.debug("�ۣ����񰡣��ѵ���ͬ��̥��"+userLogin+".."+a.getLogin());
                }
            }
        }
        if(!loginSuccess){
            logger.error("��¼���󣬿����Ǵ�����˺Ż��߿��" + admin.getLogin() + "," + admin.getPassword());
            return false;
        }
        Admin op = list.get(0);
        if (op == null || op.getStatus() == null || !op.getStatus().equals(STATUS_OK)) {
            logger.error("��¼�����˺ſ����Ǳ�������" + admin.getLogin());
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
/*  �û�Ȩ�޿��ƻ��Ʒ����仯�����ڲ�ͬ��csp�ϣ�Ȩ�޲�ͬ
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
        logger.debug("�û���" + op.getRealname() +
                "����¼�ɹ�!");
        update(op);
        return true;
    }

    public boolean isLoginExists(String login) {
        Admin searchBean = new Admin();
        searchBean.setLogin(login);
        List<Admin> list = search(searchBean,false);
        if (list != null && list.size() > 0) {
            logger.warn("����û�Login'" + login + "'�Ѿ����ڣ�");
            return true;
        } else {
            logger.warn("����û�Login'" + login + "'�����ڣ�");
            return false;
        }
    }

    public Admin save(Admin admin){
        Integer id = admin.getId();
        if (id != null && id > 0) {
            //������޸ģ����ҿ���û�����ã����þɵĿ�������
            String password = admin.getPassword();
            Admin op = get(id);
            if (op != null) {
                if (password == null || "".equals(password.trim())||password.equals(op.getPassword())) {
                    admin.setPassword(op.getPassword());
                    logger.debug("����ʱ������ÿգ�ʹ��ԭ���Ŀ��");
                } else {
                    logger.debug("�¿��'" + password + "'");
                    //���ÿ�����ʷ
                    String oldPassword = admin.getOldpasswordlog() + "|||" + op.getPassword();
                    //�����������ǰһ������ɾ��
                    while (oldPassword.length() > 100) {
                        int p = oldPassword.indexOf("|||");
                        if (p >= 0) {
                            oldPassword = oldPassword.substring(p + 3);
                        } else {
                            break;
                        }
                    }
                    logger.debug("���ÿ�����������ʷ��" + oldPassword);
                    admin.setOldpasswordlog(oldPassword);
                }
            }
        }
        admin.setModifydate(new Date());
        admin = super.save(admin);
        logger.debug("�ɹ��������ݣ�");
        return admin;
    }




    /**
     * �����е�role���أ��������Աû��Ȩ�ޣ�������selectedΪfalse������Ϊtrue
     * @param operatorId ����Աid
     * @return   ��ɫ�б�
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

    //���ص�½����Ա���е���ʹ�õ�Action������
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
         //���ÿ�����ʷ
         String oldPassword = op.getOldpasswordlog() + "|||" + op.getPassword();
         //�����������ǰһ������ɾ��
                    while (oldPassword.length() > 100) {
                        int p = oldPassword.indexOf("|||");
                        if (p >= 0) {
                            oldPassword = oldPassword.substring(p + 3);
                        } else {
                            break;
                        }
                    }
         logger.debug("���ÿ�����������ʷ��" + oldPassword);
         op.setOldpasswordlog(oldPassword);
         op.setModifydate(new Date());
         return op;
     }

    public List<Admin> getAdminsOfStatus(Admin admin, PageBean pageBean) {
        String hql = "(status=" + AdminLogicInterface.STATUS_OK + " or status=" + AdminLogicInterface.STATUS_LOCKED + ")";
        List<Admin> adminList = search(admin, pageBean, hql);
        // added by mlwang @2014-10-30�����ӹ���Ա��ɫid����
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
     * �������Ա��Ϣ�����������룬����ɫ
     * @param admin ����Ա����
     * @param roles ��ɫ�ַ���
     * @return �޸ĺ�Ĺ���Ա����
     */
    public Admin saveAdmin(Admin admin, String roles){
        // ���������Ϣ�ͽ�ɫ
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

        // �����ɫ��Ϣ
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
            //ʣ�µĽ�ɫ�󶨹�ϵҪɾ��
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
     * �½�����ѽ
     * @param admin ����Ա��Ϣ��������¼��������������
     * @param roles ����Ա��ɫ
     * @return �½��Ĺ���Ա����
     */
    public Admin newAdmin(Admin admin, String roles){
        if( admin == null) return null;

        // ���ͬ������Ա�Ƿ����
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

        // Ƶ��
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
     * ��ѯ����Ա��ϸ��Ϣ������������Ϣ�͹�����
     * @param admin ����Ա��Ϣ��������id��Login�����Բ�ѯ
     * @return ����Ա��Ϣ
     */
    public Admin getAdminDetail(Admin admin){
        PageBean pageBean = new PageBean();

        String hql = "(status=" + AdminLogicInterface.STATUS_OK + " or status=" + AdminLogicInterface.STATUS_LOCKED + ") and login='" + admin.getLogin() + "'";
        List<Admin> adminList = search(admin, pageBean, hql);

        if( adminList == null || adminList.size() == 0) return null;
        
        admin = adminList.get(0);
        // added by mlwang @2014-10-30�����ӹ���Ա��ɫid����
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
     * get��������Ա��ѡ�����Ŀ�б�
     * @param admin ����Ա
     * @return ��Ŀ�б����ŷָ��ִ�
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
