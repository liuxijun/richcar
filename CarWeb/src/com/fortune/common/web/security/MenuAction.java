package com.fortune.common.web.security;

import com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
@Namespace("/security")
@ParentPackage("default")
@Action(value="menu")
public class MenuAction extends BaseAction<Menu> {
	private static final long serialVersionUID = 3243534534534534l;
    private MenuLogicInterface menuLogicInterface;
	@SuppressWarnings("unchecked")
	public MenuAction() {
		super(Menu.class);
	}
	/**
	 * @param menuLogicInterface the menuLogicInterface to set
	 */
	public void setMenuLogicInterface(MenuLogicInterface menuLogicInterface) {
		this.menuLogicInterface = menuLogicInterface;
		setBaseLogicInterface(menuLogicInterface);
	}

    public String listFunctionMenus(){
        objs = menuLogicInterface.listFunctionMenus(obj,pageBean);
        return "list";
    }

    public String listFolderMenus(){
        objs = menuLogicInterface.listFolderMenus();
        return "list";
    }

}
