package com.fortune.rms.web.csp;

import com.fortune.rms.business.csp.logic.logicInterface.CspCspLogicInterface;
import com.fortune.rms.business.csp.model.CspCsp;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/csp")
@ParentPackage("default")
@Action(value="cspCsp")
public class CspCspAction extends BaseAction<CspCsp> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspCspLogicInterface cspCspLogicInterface;
	@SuppressWarnings("unchecked")
	public CspCspAction() {
		super(CspCsp.class);
	}
	/**
	 * @param cspCspLogicInterface the cspCspLogicInterface to set
	 */
    @Autowired
	public void setCspCspLogicInterface(
			CspCspLogicInterface cspCspLogicInterface) {
		this.cspCspLogicInterface = cspCspLogicInterface;
		setBaseLogicInterface(cspCspLogicInterface);
	}
}
