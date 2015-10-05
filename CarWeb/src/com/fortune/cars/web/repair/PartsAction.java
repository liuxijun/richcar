package com.fortune.cars.web.repair;

import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.repair.logic.logicInterface.PartsLogicInterface;
import com.fortune.cars.business.repair.model.Parts;
import com.fortune.common.web.base.BaseAction;

@Namespace("/repair")
@ParentPackage("default")
@Action(value = "parts")
@SuppressWarnings("unused")
public class PartsAction extends BaseAction<Parts> {
	private static final long serialVersionUID = 3243534534534534l;
	private PartsLogicInterface partsLogicInterface;
	@SuppressWarnings("unchecked")
	public PartsAction() {
		super(Parts.class);
	}
	/**
	 * @param partsLogicInterface the partsLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setPartsLogicInterface(PartsLogicInterface partsLogicInterface) {
		this.partsLogicInterface = partsLogicInterface;
		setBaseLogicInterface(partsLogicInterface);
	}
}
