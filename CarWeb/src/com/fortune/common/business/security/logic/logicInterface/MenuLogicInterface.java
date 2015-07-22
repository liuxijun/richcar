package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Menu;
import com.fortune.util.PageBean;

import java.util.List;

public interface MenuLogicInterface extends BaseLogicInterface<Menu> {
    public static final Integer STATUS_CLOSED = 10;
    public static final Integer STATUS_DEBUG = 11;
    public static final Integer STATUS_OK = 1;

    public List<Menu> getMenuOfAdmin(Admin admin);
    public void saveMenuToRole(List<String> menuIds, Integer roleId);
    public List<Menu> getMenuOfRole(Long roleId);

    public List<Menu> listFunctionMenus(Menu menu, PageBean pageBean);
    public List<Menu> listFolderMenus();
}
