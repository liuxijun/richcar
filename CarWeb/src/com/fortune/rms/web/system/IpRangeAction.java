package com.fortune.rms.web.system;

import com.fortune.rms.business.system.logic.logicInterface.IpRangeLogicInterface;
import com.fortune.rms.business.system.model.IpRange;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/system")
@ParentPackage("default")
@Action(value="ipRange")
public class IpRangeAction extends BaseAction<IpRange> {
	private static final long serialVersionUID = 3243534534534534l;
	private IpRangeLogicInterface ipRangeLogicInterface;
	@SuppressWarnings("unchecked")
	public IpRangeAction() {
		super(IpRange.class);
	}
	/**
	 * @param ipRangeLogicInterface the ipRangeLogicInterface to set
	 */
    @Autowired
	public void setIpRangeLogicInterface(
			IpRangeLogicInterface ipRangeLogicInterface) {
		this.ipRangeLogicInterface = ipRangeLogicInterface;
		setBaseLogicInterface(ipRangeLogicInterface);
	}
}
