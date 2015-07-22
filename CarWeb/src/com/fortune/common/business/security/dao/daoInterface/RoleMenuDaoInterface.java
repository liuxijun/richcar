package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.*;

import java.util.List;

public interface RoleMenuDaoInterface
		extends
			BaseDaoInterface<RoleMenu, Integer> {
    public List<Menu> getMenusOfRole(Integer roleId);
    public List<Role> getRolesOfMenu(Integer menuId);
}