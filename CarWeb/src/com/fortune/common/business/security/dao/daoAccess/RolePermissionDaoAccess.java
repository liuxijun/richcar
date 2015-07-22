package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.RolePermissionDaoInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RolePermissionDaoAccess
		extends
			BaseDaoAccess<RolePermission, Long>
		implements
			RolePermissionDaoInterface {

	public RolePermissionDaoAccess() {
		super(RolePermission.class);
	}

    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionsOfRole(Integer roleId){
        String hqlStr = "from Permission p";
        if(roleId!=null && roleId>0){
            hqlStr += " where p.id in (select rp.permissionid from RolePermission rp where " +
                    "rp.roleid=" + roleId+
                    ")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRolesOfPermission(Integer permissionId){
        String hqlStr = "from Role r";
        if(permissionId!=null && permissionId>0){
            hqlStr += " where r.id in (select rp.roleid from RolePermission rp where " +
                    "rp.permissionid=" + permissionId+
                    ")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }
}
