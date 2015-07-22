package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.PermissionDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.PermissionLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RolePermissionLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("permissionLogicInterface")
public class PermissionLogicImpl extends BaseLogicImpl<Permission>
		implements
			PermissionLogicInterface {

    private PermissionDaoInterface permissionDaoInterface;

    private RolePermissionLogicInterface rolePermissionLogic;


    /**
	 * @param permissionDaoInterface the permissionDaoInterface to set
	 */
    @Autowired
	public void setPermissionDaoInterface(
			PermissionDaoInterface permissionDaoInterface) {
		this.permissionDaoInterface = permissionDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.permissionDaoInterface;
	}
    @Autowired
    public void setRolePermissionLogic(RolePermissionLogicInterface rolePermissionLogic) {
        this.rolePermissionLogic = rolePermissionLogic;
    }

    public List<Role> getRolesWitchPermissionCheck(Integer permissionId){
        List<Role> all = rolePermissionLogic.getRolesOfPermission(-1);
        List<Role> permissionRoles = new ArrayList<Role>();
        if(permissionId!=null && permissionId>0){
            permissionRoles = rolePermissionLogic.getRolesOfPermission(permissionId);
        }
        for(Role r :all){
            r.setSelected(false);
            for(Role r1:permissionRoles){
                if(r.getRoleid().intValue()==r1.getRoleid().intValue()){
                    r.setSelected(true);
                    permissionRoles.remove(r1);
                    break;
                }
            }
        }
        return all;
    }

    public void saveRoleToPermission(List<Integer> roleIds,Integer permissionId){
        rolePermissionLogic.saveRoleToPermission(roleIds,permissionId);
    }

    public List<Permission> getPermissionOfOperator(Integer operatorId,Integer cspId) {
        return permissionDaoInterface.getPermissionOfOperator(operatorId,cspId);
    }

    public void remove(Permission object){
        if(object!=null){
            rolePermissionLogic.onPermissionDeleted(object.getPermissionId());
            super.remove(object);
        }
    }
}
