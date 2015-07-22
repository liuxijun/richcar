package com.fortune.rms.web.ad;

import com.fortune.rms.business.ad.logic.logicInterface.AdRangeLogicInterface;
import com.fortune.rms.business.ad.model.AdRange;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/ad")
@ParentPackage("default")
@Action(value="adRange")
public class AdRangeAction extends BaseAction<AdRange> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdRangeLogicInterface adRangeLogicInterface;
	@SuppressWarnings("unchecked")
	public AdRangeAction() {
		super(AdRange.class);
	}
	/**
	 * @param adRangeLogicInterface the adRangeLogicInterface to set
	 */
    @Autowired
	public void setAdRangeLogicInterface(
			AdRangeLogicInterface adRangeLogicInterface) {
		this.adRangeLogicInterface = adRangeLogicInterface;
		setBaseLogicInterface(adRangeLogicInterface);
	}
}
