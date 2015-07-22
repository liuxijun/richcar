package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.RoleMenuDaoInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleMenuDaoAccess
		extends
			BaseDaoAccess<RoleMenu, Integer>
		implements
			RoleMenuDaoInterface {

	public RoleMenuDaoAccess() {
		super(RoleMenu.class);
	}

    @SuppressWarnings("unchecked")
    public List<Menu> getMenusOfRole(Integer roleId){
        String hqlStr = "from Menu m";
        if(roleId!=null && roleId>0){
            hqlStr += " where m.id in (select rm.menuId from RoleMenu rm where " +
                    "rm.roleId=" + roleId+
                    ")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRolesOfMenu(Integer menuId){
        String hqlStr = "from Role r";
        if(menuId!=null && menuId>0){
            hqlStr += " where r.id in (select rm.roleId from RoleMenu rm where " +
                    "rm.menuId=" + menuId+
                    ")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }
}
