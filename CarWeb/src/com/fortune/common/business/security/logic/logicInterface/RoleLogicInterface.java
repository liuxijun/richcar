package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.PageBean;

import java.util.List;
import java.util.Map;

public interface RoleLogicInterface extends BaseLogicInterface<Role> {
    public static final Integer ROLE_TYPE_ROOT = 1;
    public static final Integer ROLE_TYPE_SYSTEM = 2;
    public static final Integer ROLE_TYPE_CSP = 3;
    public static final Integer ROLE_TYPE_SP = 4;
    public static final Integer ROLE_TYPE_CP = 5;

    public List<Permission> getAllPermissionsWithRoleCheck(Integer roleId);
    public void savePermissionToRole(List<Integer> permissionIds, Integer roleId);
    public List<Role> getRolesOfType(Integer typeId);
    public List<Map<String,Object>> listRoles(String name, PageBean pageBean);
    public List<Menu> getMenusOfRole(Long roleId);

    void saveMenuToRole(List<String> keys, Integer roleid);
}
