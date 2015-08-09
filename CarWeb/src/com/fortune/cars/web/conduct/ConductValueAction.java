package com.fortune.cars.web.conduct;

import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductValueLogicInterface;
import com.fortune.cars.business.conduct.model.ConductValue;
import com.fortune.common.web.base.BaseAction;

@Namespace("/conduct")
@ParentPackage("default")
@Action(value = "conductValue")
@SuppressWarnings("unused")
public class ConductValueAction extends BaseAction<ConductValue> {
	private static final long serialVersionUID = 3243534534534534l;
	private ConductValueLogicInterface conductValueLogicInterface;
	@SuppressWarnings("unchecked")
	public ConductValueAction() {
		super(ConductValue.class);
	}
	/**
	 * @param conductValueLogicInterface the conductValueLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setConductValueLogicInterface(
			ConductValueLogicInterface conductValueLogicInterface) {
		this.conductValueLogicInterface = conductValueLogicInterface;
		setBaseLogicInterface(conductValueLogicInterface);
	}
}
