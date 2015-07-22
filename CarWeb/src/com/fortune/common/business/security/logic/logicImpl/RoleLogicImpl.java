package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.RoleDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RolePermissionLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleLogicInterface")
public class RoleLogicImpl extends BaseLogicImpl<Role>
		implements
			RoleLogicInterface {

    private RoleDaoInterface roleDaoInterface;

    private RolePermissionLogicInterface rolePermissionLogicInterface;

    private MenuLogicInterface menuLogicInterface;
	/**
	 * @param roleDaoInterface the roleDaoInterface to set
	 */
    @Autowired
	public void setRoleDaoInterface(RoleDaoInterface roleDaoInterface) {
		this.roleDaoInterface = roleDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.roleDaoInterface;
	}
    @Autowired
    public void setRolePermissionLogicInterface(RolePermissionLogicInterface rolePermissionLogicInterface) {
        this.rolePermissionLogicInterface = rolePermissionLogicInterface;
    }

    @Autowired
    public void setMenuLogicInterface(MenuLogicInterface menuLogicInterface) {
        this.menuLogicInterface = menuLogicInterface;
    }

    public List<Permission> getAllPermissionsWithRoleCheck(Integer roleId){
        List<Permission> all = rolePermissionLogicInterface.getPermissionsOfRole(-1);

        List<Permission> rolePermissions = new ArrayList<Permission>();
        if(roleId!=null && roleId>0){
            rolePermissions = rolePermissionLogicInterface.getPermissionsOfRole(roleId);
        }
        for(Permission p :all){
            p.setSelected(false);
            for(Permission p1:rolePermissions){
                if(p.getPermissionId().intValue()==p1.getPermissionId().intValue()){
                    p.setSelected(true);
                    rolePermissions.remove(p1);
                    break;
                }
            }
        }
        return all;
    }
    public void savePermissionToRole(List<Integer> permissionIds,Integer roleId){
        rolePermissionLogicInterface.savePermissionToRole(permissionIds,roleId); 
    }

    public void saveMenuToRole(List<String> menuIds,Integer roleId){
        menuLogicInterface.saveMenuToRole(menuIds,roleId);
    }
    public List<Role> getRolesOfType(Integer typeId) {
        Role bean = new Role();
        bean.setType(typeId);
        return search(bean);
    }

    public List<Map<String, Object>> listRoles(String name,PageBean pageBean) {
        Role bean =new Role();
        bean.setName(name);
        List<Role> roles = search(bean,pageBean);
        List<Map<String,Object>> countResult= roleDaoInterface.getAdminCountOfRoles(roles);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(Role role:roles){
            Map<String,Object> m=new HashMap<String,Object>();
            m.put("id",role.getRoleid());
            m.put("name",role.getName());
            m.put("type",role.getType());
            m.put("memo",role.getMemo());
            int count = 0;
            for(Map<String,Object> countMap:countResult){
                if(role.getRoleid().equals(countMap.get("id"))){
                    Object oc = countMap.get("count");
                    if(oc!=null){
                        count = StringUtils.string2int(oc.toString(),0);
                    }
                    countResult.remove(countMap);
                    break;
                }
            }
            m.put("count",count);
            result.add(m);
        }
        return result;
    }

    public void remove(Role object){
        if(object!=null){
            rolePermissionLogicInterface.onRoleDeleted(object.getRoleid());
            super.remove(object);
        }
    }

    public List<Menu> getMenusOfRole(Long roleId){
        List<Menu> result = menuLogicInterface.getMenuOfRole(roleId);
        //将数据进行简化，permissionStr太大了
        for(Menu m:result){
            m.setPermissionStr(null);
        }
        return result;
    }
}
