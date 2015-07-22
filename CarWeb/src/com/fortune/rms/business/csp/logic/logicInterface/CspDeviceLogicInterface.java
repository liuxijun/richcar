package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.rms.business.system.model.Device;

import java.util.List;

public interface CspDeviceLogicInterface extends BaseLogicInterface<CspDevice> {
      public List<Device> getDeviceOfCsp(long cspId);
      public List<Device> getDevicesByCspId(long cspId);
      public void saveDeviceToCsp(List<Long> deviceIds, long cspId); 
}
