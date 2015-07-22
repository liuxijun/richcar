package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.util.PageBean;

import java.util.List;

public interface MenuDaoInterface extends BaseDaoInterface<Menu, Long> {
    public List<Menu> getMenuOfAdmin(Integer adminId, Integer cspId);
    public List<Menu> getMenuOfRole(Long roleId);
    public List<Menu> listFunctionMenus(Menu menu, PageBean pageBean);
}