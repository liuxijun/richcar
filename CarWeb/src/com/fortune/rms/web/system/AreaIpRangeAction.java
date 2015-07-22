package com.fortune.rms.web.system;

import com.fortune.rms.business.system.logic.logicInterface.AreaIpRangeLogicInterface;
import com.fortune.rms.business.system.model.AreaIpRange;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/system")
@ParentPackage("default")
@Action(value="areaIpRange")
public class AreaIpRangeAction extends BaseAction<AreaIpRange> {
	private static final long serialVersionUID = 3243534534534534l;
	private AreaIpRangeLogicInterface areaIpRangeLogicInterface;
	@SuppressWarnings("unchecked")
	public AreaIpRangeAction() {
		super(AreaIpRange.class);
	}
	/**
	 * @param areaIpRangeLogicInterface the areaIpRangeLogicInterface to set
	 */
    @Autowired
	public void setAreaIpRangeLogicInterface(
			AreaIpRangeLogicInterface areaIpRangeLogicInterface) {
		this.areaIpRangeLogicInterface = areaIpRangeLogicInterface;
		setBaseLogicInterface(areaIpRangeLogicInterface);
	}

    public String list() {
         objs = areaIpRangeLogicInterface.getAreaIpRange(obj,pageBean);
         return "list";
     }

}
