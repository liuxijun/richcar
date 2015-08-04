package com.fortune.common.business.security.model;

import com.fortune.common.business.base.model.BaseModel;
import java.util.ArrayList;
import java.util.List;



/**
 * Menu generated by hbm2java
 */
public class Menu extends BaseModel {

	private Integer id;
	private String name;
	private String url;
	private Integer permissionId;
	private String permissionStr;
	private Integer parentId;
    private String style;
    private Integer status;
    private String extra;

    private List<Menu> subMenus;
	public Menu() {
	}

	public Menu(Integer id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public Menu(Integer id, String name, String url, Integer permissionId,
			String permissionStr, Integer parentId) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.permissionId = permissionId;
		this.permissionStr = permissionStr;
		this.parentId = parentId;
	}

    public Menu(Integer id, String name, String url, Integer permissionId,
            String permissionStr,String style) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.permissionId = permissionId;
        this.permissionStr = permissionStr;
        this.parentId = -1;
        this.style = style;
    }
    public Menu(Integer id, String name, String url, Integer permissionId,
                String permissionStr) {
        this(id,name,url,permissionId,permissionStr,"");
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionStr() {
		return this.permissionStr;
	}

	public void setPermissionStr(String permissionStr) {
		this.permissionStr = permissionStr;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

    @SuppressWarnings("JpaAttributeTypeInspection")
    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

    public void addSubMenu(Menu menu){
        if(subMenus==null){
            subMenus = new ArrayList<Menu>();
        }
        menu.setParentId(id);
        subMenus.add(menu);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}