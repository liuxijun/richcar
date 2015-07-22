package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.business.system.model.PhoneRange;

public interface PhoneRangeLogicInterface extends BaseLogicInterface<PhoneRange> {
    public Area getAreaOfPhone(Long phone);
    public PhoneRange getPhoneRangeOfPhone(Long phone);
    public Long getAreaIdOfPhone(Long phone);
}
