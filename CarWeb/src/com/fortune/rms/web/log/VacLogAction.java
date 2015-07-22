package com.fortune.rms.web.log;


import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.log.model.VacLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

@Namespace("/log")
@ParentPackage("default")
@Action(value="vacLog")
public class VacLogAction extends BaseAction<VacLog> {
    public VacLogAction() {
        super(VacLog.class);
    }


}
