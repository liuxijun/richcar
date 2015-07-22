package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.AdminRoleDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.AdminRoleLogicInterface;
import com.fortune.common.business.security.model.AdminRole;
import com.fortune.common.business.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("adminRoleLogicInterface")
public class AdminRoleLogicImpl extends BaseLogicImpl<AdminRole>
		implements
        AdminRoleLogicInterface {

    private AdminRoleDaoInterface adminRoleDaoInterface;



    /**
	 * @param adminRoleDaoInterface the adminRoleDaoInterface to set
	 */
    @Autowired
	public void setAdminRoleDaoInterface(
			AdminRoleDaoInterface adminRoleDaoInterface) {
		this.adminRoleDaoInterface = adminRoleDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adminRoleDaoInterface;
	}

	
	public void removeByAdminId(Integer adminId) {
		AdminRole object = new AdminRole();
		object.setAdminId(adminId);
		try {
			adminRoleDaoInterface.removeByObject(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void removeByAdminCsp(Integer adminId, int cspId) {
        List<AdminRole> datas = getAdminRoleOf(adminId,cspId);
        if(datas!=null){
            for(AdminRole ar:datas){
                this.adminRoleDaoInterface.remove(ar);
            }
        }
    }

    public List<Role> getRolesOfAdmin(int operatorId,int cspId){
        return adminRoleDaoInterface.getRolesOfOperator(operatorId,cspId);
    }

    public List<AdminRole> getAdminRoleOf(int adminId,int cspId){
        AdminRole object = new AdminRole();
        object.setAdminId(adminId);
        object.setCspId(cspId);
        List<AdminRole> adminRoles = null;
        try {
            adminRoles = this.adminRoleDaoInterface.getObjects(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminRoles;
    }
    public  List<AdminRole> getRolesOfAdmin(int operatorId){
        AdminRole object = new AdminRole();
        object.setAdminId(operatorId);
        List<AdminRole> adminRoles = null;
        try {
            adminRoles = this.adminRoleDaoInterface.getObjects(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminRoles;
    }
    public void saveAdminRoles(List<Integer> roleIds,Integer operatorId,int cspId){
        List<AdminRole> oldRoles = getAdminRoleOf(operatorId,cspId);
        if(oldRoles!=null){
            if(roleIds!=null){
                for(AdminRole or:oldRoles){
                    boolean roleFound = false;
                    for(Integer roleId:roleIds){
                        if(or.getRoleId().equals(roleId)){
                            roleFound = true;
                            logger.debug("�������role��" +roleId+
                                    "�Ѿ����Ƹ�����û���:"+operatorId);
                            roleIds.remove(roleId);
                            break;
                        }
                    }
                    if(!roleFound){
                        //ɾ�����
                        logger.debug("�������Ƹ�������û�ID=" +operatorId+
                                "������ɾ����roleId="+or.getRoleId());
                        adminRoleDaoInterface.remove(or);
                    }
                }
            }
        }
        if(roleIds!=null){
            for(Integer roleId:roleIds){
                logger.debug("�½��󶨹�ϵ���û�ID"+operatorId+"��roleId="+roleId);
                AdminRole or = new AdminRole(-1,operatorId,roleId,cspId);
                adminRoleDaoInterface.save(or);
            }
        }
    }
}
