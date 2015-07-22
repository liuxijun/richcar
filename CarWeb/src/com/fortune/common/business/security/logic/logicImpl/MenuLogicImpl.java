package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.MenuDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleMenuLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Menu;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("menuLogicInterface")
public class MenuLogicImpl extends BaseLogicImpl<Menu>
		implements
			MenuLogicInterface {
	private MenuDaoInterface menuDaoInterface;
    private RoleMenuLogicInterface roleMenuLogicInterface;
	/**
	 * @param menuDaoInterface the menuDaoInterface to set
	 */

    @Autowired
    public void setMenuDaoInterface(MenuDaoInterface menuDaoInterface) {
		this.menuDaoInterface = menuDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.menuDaoInterface;
	}

    @Autowired
    public void setRoleMenuLogicInterface(RoleMenuLogicInterface roleMenuLogicInterface) {
        this.roleMenuLogicInterface = roleMenuLogicInterface;
    }

    public List<Menu> getMenuOfAdmin(Admin admin) {
        if(admin==null){
            return null;
        }
        Integer cspId = admin.getCspId();

        //如果是紧凑模式，忽略cspId这个搜索项
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            cspId=-1;
        }
        List<Menu> menus = menuDaoInterface.getMenuOfAdmin(admin.getId(),cspId);
        List<Menu> result = new ArrayList<Menu>();
        while(menus.size()>0){
            Menu menu=menus.get(0);
            Integer parentId = menu.getParentId();
            if(parentId==null||parentId<=0){
                result.add(menu);
                menus.remove(menu);
                getChildOf(menu, menus);
            }else{
                try {
                    Menu parentMenu =get(parentId);
                    if(parentMenu!=null){
                        result.add(parentMenu);
                        getChildOf(parentMenu,menus);
                    }
                } catch (Exception e) {
                    logger.error("无法初始化菜单项："+menu.getName());
                    break;
                }
            }
        }
        return result;
    }

    public void saveMenuToRole(List<String> ids, Integer roleId) {
        List<Integer> menuIds = new ArrayList<Integer>();
        for(String id:ids){
            int menuId = StringUtils.string2int(id,-1);
            if(menuId>0){
                menuIds.add(menuId);
            }
        }
        roleMenuLogicInterface.saveMenuToRole(menuIds,roleId);
    }

    public List<Menu> getMenuOfRole(Long roleId) {
        List<Menu> result = getAll();
        List<Menu> roleMenus = menuDaoInterface.getMenuOfRole(roleId);
        for(Menu selected:roleMenus){
            for(Menu menu:result){
                if(menu.getId().equals(selected.getId())){
                    menu.setExtra("selected");
                    break;
                }
            }
        }
        return result;
    }

    public List<Menu> listFunctionMenus(Menu menu, PageBean pageBean) {
        List<Menu> result = menuDaoInterface.listFunctionMenus(menu,pageBean);
        for(Menu m:result){
            Integer parentId = m.getParentId();
            if(parentId!=null&&parentId>0){
                try {
                    Menu parent = get(parentId);
                    m.setExtra(parent.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("无法获取菜单的父节点！");
                }
            }
        }
        return  result;
    }

    public List<Menu> listFolderMenus(){
        Menu bean = new Menu();
        bean.setParentId(0);
        return search(bean);
    }

    public List<Menu> getChildOf(Menu parentMenu,List<Menu> menus){
        Integer id = parentMenu.getId();
        for(Menu subMenu :menus){
            Integer subParentId = subMenu.getParentId();
            if(subParentId!=null){
                if(subParentId.equals(id)){
                    parentMenu.addSubMenu(subMenu);
                }
            }
        }
        List<Menu> result = parentMenu.getSubMenus();
        for(Menu m:result){
            menus.remove(m);
        }
        return result;
    }
}
