package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.MenuDaoInterface;
import com.fortune.common.business.security.model.Menu;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MenuDaoAccess extends BaseDaoAccess<Menu, Long>
		implements
			MenuDaoInterface {

	public MenuDaoAccess() {
		super(Menu.class);
	}

    @SuppressWarnings("unchecked")
    public List<Menu> getMenuOfAdmin(Integer adminId,Integer cspId) {
        String hql = "from Menu m where m.id in (select rm.menuId from RoleMenu rm where rm.roleId in " +
                "( select ar.roleId from AdminRole ar where ar.adminId = " + adminId;
        if(cspId!=null&&cspId>1){
            hql+=" and ar.cspId=" +cspId;
        }
        hql+="))";
        return getHibernateTemplate().find(hql);
    }

    @SuppressWarnings("unchecked")
    public List<Menu> getMenuOfRole(Long roleId) {
        String hql = "from Menu m where m.id in (select rm.menuId from RoleMenu rm where rm.roleId = "+ roleId+")";
        return getHibernateTemplate().find(hql);
    }

    public List<Menu> listFunctionMenus(Menu menu, PageBean pageBean) {
        String hql = "from Menu m where m.parentId!=0";
        List<Object> parameters = new ArrayList<Object>();
        if(menu!=null){
            if(menu.getName()!=null){
                hql+= " and m.name like ?";
                parameters.add("%"+menu.getName()+"%");
            }
            if(menu.getParentId()!=null){
                hql+= " and m.parentId="+menu.getParentId();
            }
            if(menu.getPermissionStr()!=null){
                hql+= " and m.name like ?";
                parameters.add("%"+menu.getPermissionStr()+"%");
            }
            if(menu.getStatus()!=null){
                hql+=" and m.status = "+menu.getStatus();
            }
        }
        try {
            return getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            logger.error("无法进行搜索："+hql);
            e.printStackTrace();
        }
        return new ArrayList<Menu>();
    }
}
