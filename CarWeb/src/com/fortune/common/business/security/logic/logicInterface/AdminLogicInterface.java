package com.fortune.common.business.security.logic.logicInterface;

import java.util.List;
import java.util.Map;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.PageBean;

public interface AdminLogicInterface extends BaseLogicInterface<Admin> {
    public static final Integer STATUS_OK = 1;
    public static final Integer STATUS_AUDITING = 3;
    public static final Integer STATUS_LOCKED = 8;
    public static final Integer STATUS_DELETED = 9;
	public boolean login(Admin admin);
	public Map<String,Permission> searchPermission(Integer operatorid);
    public List<Role> getRolesWithCheckOperator(Integer operatorId, Integer cspId);
    public boolean isLoginExists(String login);
    public void saveOperatorRoles(List<Integer> roleIds, Integer operatorId, Integer cspId);

    //返回登陆管理员所有的能使用的Action方法名
    public List getAllTargetByLogin(String login);
    public boolean savePassword(Integer operatorId, String oldPwd, String newPwd);
    public List<Admin> getAdminsOfStatus(Admin admin, PageBean pageBean);
    // added by mlwang @2014-10-30
    public Admin saveAdmin(Admin admin, String roles);
    public Admin newAdmin(Admin admin, String roles);
    public Admin removeAdmin(Admin admin);
    public Admin getAdminDetail(Admin admin);
    public String getAdminGrantChannel(Admin admin);
    // end of added

    List<Menu> getAdminMenus(Admin op);
}
