package com.fortune.rms.web.system;

import com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface;
import com.fortune.rms.business.system.model.PhoneRange;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/system")
@ParentPackage("default")
@Action(value="phoneRange")
public class PhoneRangeAction extends BaseAction<PhoneRange> {
    private static final long serialVersionUID = 3243534534534534l;
    private PhoneRangeLogicInterface phoneRangeLogicInterface;
    @SuppressWarnings("unchecked")
    public PhoneRangeAction() {
        super(PhoneRange.class);
    }
    /**
     * @param phoneRangeLogicInterface the ipRangeLogicInterface to set
     */
    @Autowired
    public void setPhoneRangeLogicInterface(
            PhoneRangeLogicInterface phoneRangeLogicInterface) {
        this.phoneRangeLogicInterface = phoneRangeLogicInterface;
        setBaseLogicInterface(phoneRangeLogicInterface);
    }

}