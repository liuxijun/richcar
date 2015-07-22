package com.fortune.rms.business.system.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.system.model.Device;

import java.util.List;

public interface DeviceDaoInterface extends BaseDaoInterface<Device, Long> {
    public List<Device> getDevicesOfStatus(int status);
    public List<Device> getDevicesExceptMasterDevice(String masterIp);
    public List<Device> getDevicesByCspId(long type, long status, long cspId);
    public List<Device> getDevicesByCspId(long cspId);
    public String getDeviceUrlByDeviceName(String name);
    // added by mlwang, @2015-3-6，根据device ip获得设备信息
    public Device getDeviceByIp(String ip, long type);
    // ********************************************************

    List<Object[]> getServerCount();
}