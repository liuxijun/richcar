package com.fortune.rms.web.system;

import com.fortune.common.Constants;
import com.fortune.rms.business.system.logic.logicInterface.AreaLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/system")
@ParentPackage("default")
@Action(value="area")
public class AreaAction extends BaseAction<Area> {
	private static final long serialVersionUID = 3243534534534534l;
	private AreaLogicInterface areaLogicInterface;
    private long areaId;

	@SuppressWarnings("unchecked")
	public AreaAction() {
		super(Area.class);
	}
	/**
	 * @param areaLogicInterface the areaLogicInterface to set
	 */
    @Autowired
	public void setAreaLogicInterface(AreaLogicInterface areaLogicInterface) {
		this.areaLogicInterface = areaLogicInterface;
		setBaseLogicInterface(areaLogicInterface);
	}

    public String searchAreaById(){
        obj = areaLogicInterface.get(areaId);
        return Constants.ACTION_VIEW;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

}
