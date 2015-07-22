package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.VisitDaySpLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDaySpLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="visitDaySpLog")
public class VisitDaySpLogAction extends BaseAction<VisitDaySpLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private VisitDaySpLogLogicInterface visitDaySpLogLogicInterface;
	@SuppressWarnings("unchecked")
	public VisitDaySpLogAction() {
		super(VisitDaySpLog.class);
	}
	/**
	 * @param visitDaySpLogLogicInterface the visitDaySpLogLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
    @Autowired
	public void setVisitDaySpLogLogicInterface(
			VisitDaySpLogLogicInterface visitDaySpLogLogicInterface) {
		this.visitDaySpLogLogicInterface = visitDaySpLogLogicInterface;
		setBaseLogicInterface(visitDaySpLogLogicInterface);
	}
}
