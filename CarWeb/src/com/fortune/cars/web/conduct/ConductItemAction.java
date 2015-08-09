package com.fortune.cars.web.conduct;

import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductItemLogicInterface;
import com.fortune.cars.business.conduct.model.ConductItem;
import com.fortune.common.web.base.BaseAction;

@Namespace("/conduct")
@ParentPackage("default")
@Action(value = "conductItem")
@SuppressWarnings("unused")
public class ConductItemAction extends BaseAction<ConductItem> {
	private static final long serialVersionUID = 3243534534534534l;
	private ConductItemLogicInterface conductItemLogicInterface;
	@SuppressWarnings("unchecked")
	public ConductItemAction() {
		super(ConductItem.class);
	}
	/**
	 * @param conductItemLogicInterface the conductItemLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setConductItemLogicInterface(
			ConductItemLogicInterface conductItemLogicInterface) {
		this.conductItemLogicInterface = conductItemLogicInterface;
		setBaseLogicInterface(conductItemLogicInterface);
	}
}
