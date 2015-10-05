package com.fortune.cars.web.repair;

import com.fortune.cars.business.repair.model.Parts;
import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.repair.logic.logicInterface.RepairLogicInterface;
import com.fortune.cars.business.repair.model.Repair;
import com.fortune.common.web.base.BaseAction;

import java.util.List;

@Namespace("/repair")
@ParentPackage("default")
@Action(value = "repair")
@SuppressWarnings("unused")
public class RepairAction extends BaseAction<Repair> {
	private static final long serialVersionUID = 3243534534534534l;
	private RepairLogicInterface repairLogicInterface;
	@SuppressWarnings("unchecked")
	public RepairAction() {
		super(Repair.class);
	}
	/**
	 * @param repairLogicInterface the repairLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setRepairLogicInterface(
			RepairLogicInterface repairLogicInterface) {
		this.repairLogicInterface = repairLogicInterface;
		setBaseLogicInterface(repairLogicInterface);
	}

	public String save(){
		return super.save();
	}
}
