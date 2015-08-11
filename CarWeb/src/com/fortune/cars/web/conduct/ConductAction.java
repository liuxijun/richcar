package com.fortune.cars.web.conduct;

import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductLogicInterface;
import com.fortune.cars.business.conduct.model.Conduct;
import com.fortune.common.web.base.BaseAction;

@Namespace("/conduct")
@ParentPackage("default")
@Action(value = "conduct")
@SuppressWarnings("unused")
public class ConductAction extends BaseAction<Conduct> {
	private static final long serialVersionUID = 3243534534534534l;
	private ConductLogicInterface conductLogicInterface;

	@SuppressWarnings("unchecked")
	public ConductAction() {
		super(Conduct.class);
	}
	/**
	 * @param conductLogicInterface the conductLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setConductLogicInterface(
			ConductLogicInterface conductLogicInterface) {
		this.conductLogicInterface = conductLogicInterface;
		setBaseLogicInterface(conductLogicInterface);
	}
	private Integer carId;
	public String viewItems(){
		if(keyId>0){
			super.view();
		}
		return "";
	}
}
