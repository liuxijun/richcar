package com.fortune.rms.business.system.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.business.system.model.PhoneRange;

public interface PhoneRangeDaoInterface extends BaseDaoInterface<PhoneRange, Long> {
    public Area getAreaOfPhone(Long phone);
    public PhoneRange getPhoneRangeOfPhone(Long phone);
}