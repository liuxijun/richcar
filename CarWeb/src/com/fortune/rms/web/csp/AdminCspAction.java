package com.fortune.rms.web.csp;

import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Role;
import com.fortune.rms.business.csp.logic.logicInterface.AdminCspLogicInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.BeanUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Namespace("/csp")
@ParentPackage("default")
@Action(value="adminCsp")
public class AdminCspAction extends BaseAction<AdminCsp> {
    private static final long serialVersionUID = 3243534534534534l;
    private AdminCspLogicInterface adminCspLogicInterface;
    private AdminLogicInterface adminLogicInterface;

    @SuppressWarnings("unchecked")
    public AdminCspAction() {
        super(AdminCsp.class);
    }

    /**
     * @param adminCspLogicInterface the adminCspLogicInterface to set
     */
    @Autowired
    public void setAdminCspLogicInterface(
            AdminCspLogicInterface adminCspLogicInterface) {
        this.adminCspLogicInterface = adminCspLogicInterface;
        setBaseLogicInterface(adminCspLogicInterface);
    }

    @Autowired
    public void setAdminLogicInterface(AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
    }

    public String view() {
        if (keyId > 0) {
            obj = adminCspLogicInterface.get(keyId);
        } else {
            if(obj==null){
                obj = new AdminCsp();
            }
        }
        if (obj.getAdminId() != null && obj.getAdmin() == null) {
            obj.setAdmin(adminLogicInterface.get(obj.getAdminId()));
        }
        if (obj.getAdmin() == null) {
            obj.setAdmin(new Admin());
        }
        Admin objAdmin = obj.getAdmin();
        roles = adminCspLogicInterface.getRolesOfCspWithinAdmin(obj.getCspId(), objAdmin);
        obj.getAdmin().setRoles(roles);
        BeanUtils.setDefaultValue(obj,"isDefaultCsp",1);
        return "view";
    }

    public String list(){
        objs=adminCspLogicInterface.searchCspName(obj,pageBean);
        return "list";
    }


    public String save(){
        obj = adminCspLogicInterface.save(obj,obj.getCspId(),roleIds);
        writeSysLog("管理员绑定角色,adminId="+obj.getAdminId()+",cspId="+obj.getCspId());
        return "save";
    }

    public List<Role> roles;
    public List<Integer> roleIds;

    @SuppressWarnings("unused")
    public List<Role> getRoles() {
        return roles;
    }

    @SuppressWarnings("unused")
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @SuppressWarnings("unused")
    public String getRolesJson() {
        return getJsonArray(roles);
    }

    @SuppressWarnings("unused")
    public List<Integer> getRoleIds() {
        return roleIds;
    }

    @SuppressWarnings("unused")
    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
