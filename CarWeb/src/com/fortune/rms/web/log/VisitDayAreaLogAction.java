package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.VisitDayAreaLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayAreaLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="visitDayAreaLog")
public class VisitDayAreaLogAction extends BaseAction<VisitDayAreaLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private VisitDayAreaLogLogicInterface visitDayAreaLogLogicInterface;
	@SuppressWarnings("unchecked")
	public VisitDayAreaLogAction() {
		super(VisitDayAreaLog.class);
	}
	/**
	 * @param visitDayAreaLogLogicInterface the visitDayAreaLogLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
    @Autowired
	public void setVisitDayAreaLogLogicInterface(
			VisitDayAreaLogLogicInterface visitDayAreaLogLogicInterface) {
		this.visitDayAreaLogLogicInterface = visitDayAreaLogLogicInterface;
		setBaseLogicInterface(visitDayAreaLogLogicInterface);
	}
}
