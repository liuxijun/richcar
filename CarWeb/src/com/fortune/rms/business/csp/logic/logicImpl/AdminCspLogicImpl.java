package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.logic.logicInterface.AdminRoleLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Role;
import com.fortune.rms.business.csp.dao.daoInterface.AdminCspDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.AdminCspLogicInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("adminCspLogicInterface")
public class    AdminCspLogicImpl extends BaseLogicImpl<AdminCsp>
		implements
			AdminCspLogicInterface {
	private AdminCspDaoInterface adminCspDaoInterface;
    private RoleLogicInterface roleLogicInterface;
    private AdminRoleLogicInterface adminRoleLogicInterface;
	/**
	 * @param adminCspDaoInterface the adminCspDaoInterface to set
	 */
    @Autowired
	public void setAdminCspDaoInterface(
			AdminCspDaoInterface adminCspDaoInterface) {
		this.adminCspDaoInterface = adminCspDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adminCspDaoInterface;
	}
    @Autowired
    public void setRoleLogicInterface(RoleLogicInterface roleLogicInterface) {
        this.roleLogicInterface = roleLogicInterface;
    }
    @Autowired
    public void setAdminRoleLogicInterface(AdminRoleLogicInterface adminRoleLogicInterface) {
        this.adminRoleLogicInterface = adminRoleLogicInterface;
    }
    public AdminCsp save(AdminCsp obj,Integer cspId,List<Integer> roleIds){
        obj = save(obj);
        Integer isDefaultCsp = obj.getIsDefaultCsp();
        if(isDefaultCsp!=null&&isDefaultCsp==1){
            adminCspDaoInterface.setDefaultCsp(cspId,obj.getAdminId());
        }
        adminRoleLogicInterface.saveAdminRoles(roleIds,obj.getAdminId(),cspId);
        return obj;
    }



    public List<Role> getRolesOfCspWithinAdmin(Integer cspId, Admin admin) {
        if(cspId==null){
            cspId = -1;
        }
        if(admin==null){
            admin = new Admin();
        }
        Integer adminId = admin.getId();
        if(adminId == null){
            adminId = -1;
        }
        int roleType = RoleLogicInterface.ROLE_TYPE_CSP;
        Integer isSystem = admin.getIsSystem();
        if(isSystem!=null&&isSystem==1){
            //roleType = RoleLogicInterface.ROLE_TYPE_SYSTEM;
        }
        List<Role> result = roleLogicInterface.getRolesOfType(roleType);
        List<Role> roleSelected =null;
        if(cspId>0){
            roleSelected = adminRoleLogicInterface.getRolesOfAdmin(adminId,cspId);
        }else{
            roleSelected = new ArrayList<Role>();
        }
        for(Role r:result){
            r.setSelected(false);
            for(Role selectedR:roleSelected){
                if(r.getRoleid().equals(selectedR.getRoleid())){
                    r.setSelected(true);
                    roleSelected.remove(selectedR);
                    break;
                }
            }
        }
        return result;
    }

    public void remove(AdminCsp obj){
        adminRoleLogicInterface.removeByAdminCsp(obj.getAdminId(),obj.getCspId());
        adminCspDaoInterface.remove(obj);
    }



    public List<AdminCsp> searchCspName(AdminCsp adminCsp, PageBean pageBean) {
        List<Object[]> tempResult = adminCspDaoInterface.searchCspName(adminCsp, pageBean);
        List<AdminCsp> result = new ArrayList<AdminCsp>();
        for (Object[] objs : tempResult) {
            AdminCsp ac = (AdminCsp) objs[0];
            Csp c= (Csp) objs[1];
           ac.getCsp().setName(c.getName());
            result.add(ac);
        }
        return result;
    }


}
