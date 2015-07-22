package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.PageBean;
import com.fortune.util.SimpleFileInfo;

import java.util.List;
import java.util.Map;

public interface DeviceLogicInterface extends BaseLogicInterface<Device> {
    public static final long DEVICE_TYPE_MMS=1;
    public static final long DEVICE_TYPE_HLS_VOD =2;
    public static final long DEVICE_TYPE_WEB=3;
    public static final long DEVICE_TYPE_ENCODER=4;
    public static final long DEVICE_TYPE_DATABASE=5;
    public static final long DEVICE_TYPE_HLS_LIVE=6;

    public final static int DEVICE_ONLINE = 1;
    public final static int DEVICE_OFFLINE = 5;

    public List<Device> getDevicesOfStatus(int status);
    public List<Device> getDevicesExceptMasterDevice(String masterIp);
    public String getServerRootFromCenter(Long deviceId, Long cspId);
    public String getServerLocalPath(long deviceId, Long cspId);
    public List<Device> getDevicesOfType(long type, long status, long cspId);
    public List<Device> getDevicesByCspId(long cspId);
    public String getDeviceUrlByDeviceName(String name);
    public List<SimpleFileInfo> getFtpFiles(long deviceId, String path, String name, PageBean pageBean, boolean absDir);
    public List<SimpleFileInfo> listFiles(long serverId, long cspId, String dirType, String path, String fileNameRegEx, PageBean pageBean, boolean withSubDir);
    public List<SimpleFileInfo> listFiles(long serverId, long cspId, String path, String fileNameRegEx, PageBean pageBean);
    public List<SimpleFileInfo> listFiles(long serverId, String path, String fileNameRegEx);
    public List<SimpleFileInfo> listFiles(long serverId, String path);
    public  List<Map<String,String>>  listLives(long serverId);
    // added by mlwang, @2015-3-11
    public Device getRandomLiveServer();
    // ****************************************

    Map<Integer,Integer> getServerCount();
}
