package com.fortune.rms.web.system;

import com.fortune.common.Constants;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Namespace("/system")
@ParentPackage("default")
@Action(value="systemLog")
public class SystemLogAction extends BaseAction<SystemLog> {
	private static final long serialVersionUID = 3243534534534534l;
    private String adminName;
    private String log ;

	public SystemLogAction() {
		super(SystemLog.class);
	}
	/**
	 * @param systemLogLogicInterface the systemLogLogicInterface to set
	 */
	public void setSystemLogLogicInterface(
			SystemLogLogicInterface systemLogLogicInterface) {
		this.systemLogLogicInterface = systemLogLogicInterface;
		setBaseLogicInterface(systemLogLogicInterface);
	}
    @SuppressWarnings("UnusedDeclaration")
    public String getAdminName() {
        return adminName;
    }
    @SuppressWarnings("UnusedDeclaration")
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String export(){
        obj.setAdminName(adminName);
        obj.setLog(log);
        objs=systemLogLogicInterface.export(obj,startTime,stopTime,pageBean);
        return null;
    }
    public String list(){
        obj.setAdminName(adminName);
        obj.setLog(log);
        objs=systemLogLogicInterface.getSystemLogAll(obj,startTime,stopTime,pageBean);
        return "list";
    }

    private Date startTime;
    private Date stopTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String view(){
        obj=systemLogLogicInterface.getSystemLogId(obj.getId());
        return Constants.ACTION_VIEW;
    }

    public void setSort(String sort){
        if("adminName".equals(sort)){
            sort = "a.login";
        }else{
            sort = "sl."+sort;
        }
        super.setSort(sort);
    }
}
