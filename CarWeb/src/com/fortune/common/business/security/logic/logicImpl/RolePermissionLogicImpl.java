package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.RolePermissionDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.RolePermissionLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("rolePermissionLogicInterface")
public class RolePermissionLogicImpl extends BaseLogicImpl<RolePermission>
        implements
        RolePermissionLogicInterface {

    private RolePermissionDaoInterface rolePermissionDaoInterface;


    /**
     * @param rolePermissionDaoInterface the rolePermissionDaoInterface to set
     */
    @Autowired
    public void setRolePermissionDaoInterface(
            RolePermissionDaoInterface rolePermissionDaoInterface) {
        this.rolePermissionDaoInterface = rolePermissionDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.rolePermissionDaoInterface;
    }


    public void removeByRoleId(Integer roleid) {
        RolePermission object = new RolePermission();
        object.setRoleid(roleid);
        try {
            rolePermissionDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Role> getRolesOfPermission(Integer permissionId) {
        return rolePermissionDaoInterface.getRolesOfPermission(permissionId);
    }

    public List<Permission> getPermissionsOfRole(Integer roleId) {
        return rolePermissionDaoInterface.getPermissionsOfRole(roleId);
    }

    public void savePermissionToRole(List<Integer> permissionIds, Integer roleId){
        if(permissionIds==null){
            permissionIds = new ArrayList<Integer>();
        }
        RolePermission object = new RolePermission();
        object.setRoleid(roleId);
        List<RolePermission> oldPermissions = super.search(object);
        if (oldPermissions != null) {
            for (RolePermission rp : oldPermissions) {
                boolean permissionFound = false;
                for (Integer permissionId : permissionIds) {
                    if (rp.getPermissionid().equals(permissionId)) {
                        permissionFound = true;
                        logger.debug("发现这个permissionId：" + permissionId +
                                "已经复制给这个roleId:" + roleId);
                        permissionIds.remove(permissionId);
                        break;
                    }
                }
                if (!permissionFound) {
                    //删了这个
                    logger.debug("曾经复制给了这个roleId=" + roleId +
                            "，现在删除permissionId=" + rp.getPermissionid());
                    rolePermissionDaoInterface.remove(rp);
                }
            }
        }
        for (Integer permissionId : permissionIds) {
            if (permissionId != null) {
                logger.debug("新建绑定关系：permissionID" + permissionId + "，roleId=" + roleId);
                RolePermission rp = new RolePermission(-1, roleId, permissionId);
                rolePermissionDaoInterface.save(rp);
            }
        }
    }

    public void saveRoleToPermission(List<Integer> roleIds, Integer permissionId){
        if(roleIds == null){
            roleIds = new ArrayList<Integer>();
        }
        RolePermission object = new RolePermission();
        object.setPermissionid(permissionId);
        List<RolePermission> oldPermissions = super.search(object);
        if (oldPermissions != null) {
            for (RolePermission rp : oldPermissions) {
                boolean permissionFound = false;
                for (Integer roleId : roleIds) {
                    if (rp.getRoleid().equals(roleId)) {
                        permissionFound = true;
                        logger.debug("发现这个permissionId：" + permissionId +
                                "已经复制给这个roleId:" + roleId);
                        roleIds.remove(roleId);
                        break;
                    }
                }
                if (!permissionFound) {
                    //删了这个
                    logger.debug("曾经复制给了这个roleId=" + permissionId +
                            "，现在删除permissionId=" + rp.getPermissionid());
                    rolePermissionDaoInterface.remove(rp);
                }
            }
        }
        for (Integer roleId : roleIds) {
            if (permissionId != null) {
                logger.debug("新建绑定关系：permissionID" + permissionId + "，roleId=" + roleId);
                RolePermission rp = new RolePermission(-1, roleId, permissionId);
                rolePermissionDaoInterface.save(rp);
            }
        }
    }

    public void savePermissionToRole(List<Integer> permissionIds, List<Integer> roleIds) {
        if(permissionIds==null){
            permissionIds = new ArrayList<Integer>();
        }
        if(roleIds == null){
            roleIds = new ArrayList<Integer>();
        }
        RolePermission object = new RolePermission();
        for(Integer roleId:roleIds){
            for (Integer permissionId : permissionIds) {
                if (permissionId != null) {
                    logger.debug("新建绑定关系：permissionID" + permissionId + "，roleId=" + roleId);
                    RolePermission rp = new RolePermission(-1, roleId, permissionId);
                    rolePermissionDaoInterface.save(rp);
                }
            }
        }
    }

    public void onPermissionDeleted(Integer permissionId) {
        RolePermission object = new RolePermission();
        object.setPermissionid(permissionId);
        try {
            rolePermissionDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRoleDeleted(Integer roleId) {
        RolePermission object = new RolePermission();
        object.setRoleid(roleId);
        try {
            rolePermissionDaoInterface.removeByObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
