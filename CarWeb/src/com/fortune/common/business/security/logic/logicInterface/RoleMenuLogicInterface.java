package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RoleMenu;

import java.util.List;

public interface RoleMenuLogicInterface
		extends
			BaseLogicInterface<RoleMenu> {
	
	public void removeByRoleId(Integer roleid);
    public List<Menu> getMenusOfRole(Integer roleId);

    public List<Role> getRolesOfMenu(Integer menuId);
    public void saveMenuToRole(List<Integer> menuIds, Integer roleId);
    public void saveRoleToMenu(List<Integer> roleIds, Integer menuId);
    public void saveMenuToRole(List<Integer> menuIds, List<Integer> roleIds);
    public void onMenuDeleted(Integer menuId);
    public void onRoleDeleted(Integer roleId);
}
