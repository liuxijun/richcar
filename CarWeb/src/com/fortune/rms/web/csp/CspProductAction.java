package com.fortune.rms.web.csp;

import com.fortune.rms.business.csp.logic.logicInterface.CspProductLogicInterface;
import com.fortune.rms.business.csp.model.CspProduct;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/csp")
@ParentPackage("default")
@Action(value="cspProduct")
public class CspProductAction extends BaseAction<CspProduct> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspProductLogicInterface cspProductLogicInterface;
	@SuppressWarnings("unchecked")
	public CspProductAction() {
		super(CspProduct.class);
	}
	/**
	 * @param cspProductLogicInterface the cspProductLogicInterface to set
	 */
    @Autowired
	public void setCspProductLogicInterface(
			CspProductLogicInterface cspProductLogicInterface) {
		this.cspProductLogicInterface = cspProductLogicInterface;
		setBaseLogicInterface(cspProductLogicInterface);
	}
}
