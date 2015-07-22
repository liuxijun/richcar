package com.fortune.common.web.security;

import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Namespace("/security")
@ParentPackage("default")
@Action(value="role")
public class RoleAction extends BaseAction<Role> {
	private static final long serialVersionUID = 3243534534534534l;
	private RoleLogicInterface roleLogicInterface;
    private List<Integer> permissionIds;
    private String permissionIdsString;
	@SuppressWarnings("unchecked")
	public RoleAction() {
		super(Role.class);
	}
	/**
	 * @param roleLogicInterface the roleLogicInterface to set
	 */
    @Autowired
	public void setRoleLogicInterface(RoleLogicInterface roleLogicInterface) {
		this.roleLogicInterface = roleLogicInterface;
		setBaseLogicInterface(roleLogicInterface);
	}
    public String view(){
        String result = super.view();
        if(obj!=null){
            Integer roleId = obj.getRoleid();
            obj.setPermissions(roleLogicInterface.getAllPermissionsWithRoleCheck(roleId));
            if(roleId==null){
                roleId = -1;
            }
            obj.setMenus(roleLogicInterface.getMenusOfRole(roleId.longValue()));
        }
        return result;
    }
	public String save() {
		super.save();
        if(obj!=null){
            try {
                roleLogicInterface.savePermissionToRole(permissionIds,obj.getRoleid());
                roleLogicInterface.saveMenuToRole(keys,obj.getRoleid());
                writeSysLog("保存角色信息： " + obj.getRoleid() + "," + obj.getName());
            } catch (Exception e) {
                setSuccess(false);
                addActionError("保存'" +obj.getMemo()+
                        "'关联权限时发生错误："+e.getMessage());
            }
        }
		return Constants.ACTION_SAVE;
	}

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public String getPermissionIdsString() {
        return permissionIdsString;
    }

    public void setPermissionIdsString(String permissionIdsString) {
        this.permissionIdsString = permissionIdsString;
        if(permissionIdsString!=null){
            String[] ids = permissionIdsString.split(",");//")
            permissionIds = new ArrayList<Integer>();
            if(ids!=null){
                 for(String id:ids){
                     permissionIds.add(Integer.parseInt(id));
                 }
            }
        }
    }
    private List<Map<String,Object>> roles=null;
    public String listRoles(){
        menus = null;
        roles = roleLogicInterface.listRoles(obj.getName(),pageBean);
        return "list";
    }
    public String getJsonObjs(){
        if(roles!=null){
            return getJsonArray(roles);
        }else if(menus!=null){
            return getJsonArray(menus);
        }
        return super.getJsonObjs();
    }
    private List<Menu> menus;
    public String getMenuOfRole(){
        Integer roleId = obj.getRoleid();
        if(roleId==null){
            roleId = -1;
        }
        roles = null;
        menus =roleLogicInterface.getMenusOfRole(roleId.longValue());
        return "list";
    }

/*
    private List<String> menuIds;
    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }

    public void setMenuIdString(String menuIds) {
        this.menuIds = new ArrayList<String>();
        String[] ids = menuIds.split(",");
        Collections.addAll(this.menuIds, ids);
    }
*/
}
