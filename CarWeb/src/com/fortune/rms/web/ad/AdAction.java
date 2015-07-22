package com.fortune.rms.web.ad;

import com.fortune.rms.business.ad.logic.logicInterface.AdLogicInterface;
import com.fortune.rms.business.ad.model.Ad;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/ad")
@ParentPackage("default")
@Action(value="ad")
public class AdAction extends BaseAction<Ad> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdLogicInterface adLogicInterface;
	@SuppressWarnings("unchecked")
	public AdAction() {
		super(Ad.class);
	}
	/**
	 * @param adLogicInterface the adLogicInterface to set
	 */
	@Autowired
	public void setAdLogicInterface(AdLogicInterface adLogicInterface) {
		this.adLogicInterface = adLogicInterface;
		setBaseLogicInterface(adLogicInterface);
	}
}
