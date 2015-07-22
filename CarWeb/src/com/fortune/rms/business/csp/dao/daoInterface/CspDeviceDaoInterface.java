package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.rms.business.system.model.Device;

import java.util.List;

public interface CspDeviceDaoInterface
		extends
			BaseDaoInterface<CspDevice, Long> {
      public List<Device> getDeviceOfCsp(long cspId);
      public List<Device> getDevicesByCspId(long cspId);
}