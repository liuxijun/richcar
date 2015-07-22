package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.VisitDayChannelLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayChannelLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="visitDayChannelLog")
public class VisitDayChannelLogAction extends BaseAction<VisitDayChannelLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private VisitDayChannelLogLogicInterface visitDayChannelLogLogicInterface;
	@SuppressWarnings("unchecked")
	public VisitDayChannelLogAction() {
		super(VisitDayChannelLog.class);
	}
	/**
	 * @param visitDayChannelLogLogicInterface the visitDayChannelLogLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
    @Autowired
	public void setVisitDayChannelLogLogicInterface(
			VisitDayChannelLogLogicInterface visitDayChannelLogLogicInterface) {
		this.visitDayChannelLogLogicInterface = visitDayChannelLogLogicInterface;
		setBaseLogicInterface(visitDayChannelLogLogicInterface);
	}
}
