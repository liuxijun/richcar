package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.AreaIpRange;
import com.fortune.util.PageBean;

import java.util.List;

public interface AreaIpRangeLogicInterface
		extends
			BaseLogicInterface<AreaIpRange> {
    public List<AreaIpRange> getAreaIpRange(AreaIpRange areaIpRange, PageBean pageBean);
  
}
