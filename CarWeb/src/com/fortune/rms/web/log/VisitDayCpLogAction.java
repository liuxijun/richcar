package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.VisitDayCpLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayCpLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="visitDayCpLog")
public class VisitDayCpLogAction extends BaseAction<VisitDayCpLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private VisitDayCpLogLogicInterface visitDayCpLogLogicInterface;
	@SuppressWarnings("unchecked")
	public VisitDayCpLogAction() {
		super(VisitDayCpLog.class);
	}
	/**
	 * @param visitDayCpLogLogicInterface the visitDayCpLogLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
    @Autowired
	public void setVisitDayCpLogLogicInterface(
			VisitDayCpLogLogicInterface visitDayCpLogLogicInterface) {
		this.visitDayCpLogLogicInterface = visitDayCpLogLogicInterface;
		setBaseLogicInterface(visitDayCpLogLogicInterface);
	}
}
