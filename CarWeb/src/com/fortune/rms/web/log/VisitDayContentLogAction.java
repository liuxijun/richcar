package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.VisitDayContentLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayContentLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="visitDayContentLog")
public class VisitDayContentLogAction extends BaseAction<VisitDayContentLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private VisitDayContentLogLogicInterface visitDayContentLogLogicInterface;
	@SuppressWarnings("unchecked")
	public VisitDayContentLogAction() {
		super(VisitDayContentLog.class);
	}
	/**
	 * @param visitDayContentLogLogicInterface the visitDayContentLogLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
    @Autowired
	public void setVisitDayContentLogLogicInterface(
			VisitDayContentLogLogicInterface visitDayContentLogLogicInterface) {
		this.visitDayContentLogLogicInterface = visitDayContentLogLogicInterface;
		setBaseLogicInterface(visitDayContentLogLogicInterface);
	}
}
