package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.RoleMenuDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleMenuLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("roleMenuLogicInterface")
public class RoleMenuLogicImpl extends BaseLogicImpl<RoleMenu>
        implements
        RoleMenuLogicInterface {

    private RoleMenuDaoInterface roleMenuDaoInterface;


    /**
     * @param roleMenuDaoInterface the roleMenuDaoInterface to set
     */
    @Autowired
    public void setRoleMenuDaoInterface(
            RoleMenuDaoInterface roleMenuDaoInterface) {
        this.roleMenuDaoInterface = roleMenuDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.roleMenuDaoInterface;
    }


    public void removeByRoleId(Integer roleid) {
        RoleMenu object = new RoleMenu();
        object.setRoleId(roleid);
        try {
            roleMenuDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Role> getRolesOfMenu(Integer menuId) {
        return roleMenuDaoInterface.getRolesOfMenu(menuId);
    }

    public List<Menu> getMenusOfRole(Integer roleId) {
        return roleMenuDaoInterface.getMenusOfRole(roleId);
    }

    public void saveMenuToRole(List<Integer> menuIds, Integer roleId){
        if(menuIds==null){
            menuIds = new ArrayList<Integer>();
        }
        RoleMenu object = new RoleMenu();
        object.setRoleId(roleId);
        List<RoleMenu> oldMenus = super.search(object);
        if (oldMenus != null) {
            for (RoleMenu rp : oldMenus) {
                boolean menuFound = false;
                for (Integer menuId : menuIds) {
                    if (rp.getMenuId().equals(menuId)) {
                        menuFound = true;
                        logger.debug("发现这个menuId：" + menuId +
                                "已经复制给这个roleId:" + roleId);
                        menuIds.remove(menuId);
                        break;
                    }
                }
                if (!menuFound) {
                    //删了这个
                    logger.debug("曾经复制给了这个roleId=" + roleId +
                            "，现在删除menuId=" + rp.getMenuId());
                    roleMenuDaoInterface.remove(rp);
                }
            }
        }
        for (Integer menuId : menuIds) {
            if (menuId != null) {
                logger.debug("新建绑定关系：menuID" + menuId + "，roleId=" + roleId);
                RoleMenu rp = new RoleMenu(-1, roleId, menuId);
                roleMenuDaoInterface.save(rp);
            }
        }
    }

    public void saveRoleToMenu(List<Integer> roleIds, Integer menuId){
        if(roleIds == null){
            roleIds = new ArrayList<Integer>();
        }
        RoleMenu object = new RoleMenu();
        object.setMenuId(menuId);
        List<RoleMenu> oldMenus = super.search(object);
        if (oldMenus != null) {
            for (RoleMenu rp : oldMenus) {
                boolean menuFound = false;
                for (Integer roleId : roleIds) {
                    if (rp.getRoleId().equals(roleId)) {
                        menuFound = true;
                        logger.debug("发现这个menuId：" + menuId +
                                "已经复制给这个roleId:" + roleId);
                        roleIds.remove(roleId);
                        break;
                    }
                }
                if (!menuFound) {
                    //删了这个
                    logger.debug("曾经复制给了这个roleId=" + menuId +
                            "，现在删除menuId=" + rp.getMenuId());
                    roleMenuDaoInterface.remove(rp);
                }
            }
        }
        for (Integer roleId : roleIds) {
            if (menuId != null) {
                logger.debug("新建绑定关系：menuID" + menuId + "，roleId=" + roleId);
                RoleMenu rp = new RoleMenu(-1, roleId, menuId);
                roleMenuDaoInterface.save(rp);
            }
        }
    }

    public void saveMenuToRole(List<Integer> menuIds, List<Integer> roleIds) {
        if(menuIds==null){
            menuIds = new ArrayList<Integer>();
        }
        if(roleIds == null){
            roleIds = new ArrayList<Integer>();
        }
        RoleMenu object = new RoleMenu();
        for(Integer roleId:roleIds){
            for (Integer menuId : menuIds) {
                if (menuId != null) {
                    logger.debug("新建绑定关系：menuID" + menuId + "，roleId=" + roleId);
                    RoleMenu rp = new RoleMenu(-1, roleId, menuId);
                    roleMenuDaoInterface.save(rp);
                }
            }
        }
    }

    public void onMenuDeleted(Integer menuId) {
        RoleMenu object = new RoleMenu();
        object.setMenuId(menuId);
        try {
            roleMenuDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRoleDeleted(Integer roleId) {
        RoleMenu object = new RoleMenu();
        object.setRoleId(roleId);
        try {
            roleMenuDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
