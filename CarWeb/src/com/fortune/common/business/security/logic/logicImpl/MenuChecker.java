package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.SpringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xjliu on 2014/11/1.
 * 检查用户是否有权利访问某个菜单
 */
public class MenuChecker {
    private static MenuChecker ourInstance = new MenuChecker();
    private Map<String,List<Menu>> allSystemProtectedUrls=new HashMap<String,List<Menu>>();
    private MenuLogicInterface menuLogicInterface;
    public static MenuChecker getInstance() {
        return ourInstance;
    }

    private MenuChecker() {
        menuLogicInterface =(MenuLogicInterface) SpringUtils.getBeanForApp("menuLogicInterface");
        refreshAllMenus();
    }

    public Map<String,List<Menu>> formatMenus(List<Menu> menus){
        Map<String,List<Menu>> result = new HashMap<String,List<Menu>>();
        if(menus==null||menus.size()<=0){
            return result;
        }
        for(Menu menu:menus){
            String urls = menu.getPermissionStr();
            if(urls!=null&&!"".equals(urls)&&!"#".equals(urls)){
                String[] allUrls = urls.split(";");
                for(String url:allUrls){
                    List<Menu> urlMapMenus = result.get(url);
                    if(urlMapMenus==null){
                        urlMapMenus = new ArrayList<Menu>();
                    }
                    urlMapMenus.add(menu);
                    result.put(url,urlMapMenus);
                }
            }
            result.putAll(formatMenus(menu.getSubMenus()));
        }
        return result;
    }
    public void refreshAllMenus(){
        if(allSystemProtectedUrls!=null){
            allSystemProtectedUrls.clear();
        }
        allSystemProtectedUrls = formatMenus( menuLogicInterface.getAll());
    }
    public boolean needCheckUrl(String url){
        return allSystemProtectedUrls.containsKey(url);
    }
}
