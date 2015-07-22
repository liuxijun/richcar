package com.fortune.rms.web.ad;

import com.fortune.rms.business.ad.logic.logicInterface.AdUrlLogicInterface;
import com.fortune.rms.business.ad.model.AdUrl;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/ad")
@ParentPackage("default")
@Action(value="adUrl")
public class AdUrlAction extends BaseAction<AdUrl> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdUrlLogicInterface adUrlLogicInterface;
	@SuppressWarnings("unchecked")
	public AdUrlAction() {
		super(AdUrl.class);
	}
	/**
	 * @param adUrlLogicInterface the adUrlLogicInterface to set
	 */
    @Autowired
	public void setAdUrlLogicInterface(AdUrlLogicInterface adUrlLogicInterface) {
		this.adUrlLogicInterface = adUrlLogicInterface;
		setBaseLogicInterface(adUrlLogicInterface);
	}
}
