package com.fortune.common.web.ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-17
 * Time: 16:37:03
 * Action与权限的对应关系
 */
public class ActionPermission  implements java.io.Serializable {
    private String action;
    private List<String> needPermissions=new ArrayList<String>();

    public ActionPermission() {
        needPermissions=new ArrayList<String>();
    }

    public ActionPermission(String action, List<String> needPermissions) {
        this.action = action;
        this.needPermissions = needPermissions;
    }

    public ActionPermission(String action, String needPermissionStrings) {
        this.action = action;
        this.needPermissions=new ArrayList<String>();
        if(needPermissionStrings!=null){
            needPermissions.addAll(Arrays.asList(needPermissionStrings.split(",")));
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getNeedPermissions() {
        return needPermissions;
    }

    public void setNeedPermissions(List<String> needPermissions) {
        this.needPermissions = needPermissions;
    }

    public boolean hasPermission(String permission){
        for(String s:needPermissions){
            if(s.equals(permission)){
                return true;
            }
        }
        return false;
    }
}
