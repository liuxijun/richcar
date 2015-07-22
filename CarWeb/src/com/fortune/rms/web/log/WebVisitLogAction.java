package com.fortune.rms.web.log;

import com.fortune.rms.business.log.logic.logicInterface.WebVisitLogLogicInterface;
import com.fortune.rms.business.log.model.WebVisitLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/log")
@ParentPackage("default")
@Action(value="webVisitLog")
public class WebVisitLogAction extends BaseAction<WebVisitLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private WebVisitLogLogicInterface webVisitLogLogicInterface;
	@SuppressWarnings("unchecked")
	public WebVisitLogAction() {
		super(WebVisitLog.class);
	}
	/**
	 * @param webVisitLogLogicInterface the webVisitLogLogicInterface to set
	 */
    @Autowired
	public void setWebVisitLogLogicInterface(
			WebVisitLogLogicInterface webVisitLogLogicInterface) {
		this.webVisitLogLogicInterface = webVisitLogLogicInterface;
		setBaseLogicInterface(webVisitLogLogicInterface);
	}
}
