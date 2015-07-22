package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.LoadBalanceWorker;
import com.fortune.rms.business.system.logic.logicInterface.SlbLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.system.model.SlbLog;
import com.fortune.server.message.RtspServerMessager;
import com.fortune.util.*;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xjliu on 2014/11/25.
 * 一般的调度
 */
public class GSLB4Hls  implements LoadBalanceWorker {
    public static Long STATUS_OK=2L;
    public static Long STATUS_SHUTDOWN=1L;
    private Logger logger = Logger.getLogger(this.getClass());
    public List<Device> hlsServers;
    private static ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
    private  final SlbLogLogicInterface slbLogLogicInterface;
    private final DeviceLogicInterface deviceLogicInterface;
    private MediaServerMonitor monitorServer;
    public MediaServerMonitor getServer(){
        return monitorServer;
    }
    public GSLB4Hls(){
        slbLogLogicInterface = (SlbLogLogicInterface) SpringUtils.getBeanForApp("slbLogLogicInterface");
        deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBeanForApp("deviceLogicInterface");
        initServers();
        startMonitorServer();
    }


    @SuppressWarnings("unchecked")
    public void initServers(){
        hlsServers = (List<Device>) CacheUtils.get("hls", "hlsServerCache", new DataInitWorker() {
            public Object init(Object keyId, String cacheName) {
                Device device = new Device();
                device.setType(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD);
                device.setMaxTask(9999L);
                device.setStatus((long) DeviceLogicImpl.DEVICE_ONLINE);
                logger.debug("正在刷新服务器列表....");
                List<Device> result = deviceLogicInterface.search(device);
                logger.debug("获得服务器：" + result.size() + "个。");
                return result;
            }
        });
    }
    private void startMonitorServer(){
        Runnable monitorTask = new Runnable() {
            public void run() {
                initServers();
                for(Device device:hlsServers){
                    int port = ConfigManager.getInstance().getConfig("hls.managePort", device.getMonitorPort());
                    Long status = STATUS_OK;
                    RtspServerMessager messager = new RtspServerMessager();
                    String result = messager.getMessage(device.getSafeServerIp(),80,"/html/",null);
                    if(result==null||"".equals(result.trim())){
                        status = STATUS_SHUTDOWN;
                    }
                    device.setStatus(status);
                    int clientCount = 0;
                    try {
                        String xmlContent = messager.queryServerGen(device.getSafeServerIp(),port,0,0);
                        if (xmlContent != null&&xmlContent.contains("<")&&xmlContent.contains(">")) {
                            Element genRoot = XmlUtils.getRootFromXmlStr(xmlContent);
                            List params = genRoot.selectNodes("params/param");
                            if(params!=null){
                                for (Object param1 : params) {
                                    Node param = (Node) param1;
                                    if (param != null) {
                                        String name = XmlUtils.getValue(param, "@n", null);
                                        if (name != null) {
                                            String value = XmlUtils.getValue(param, "@v", null);
                                            if ("clients".equals(name)) {
                                                int clients = StringUtils.string2int(value, -1);
                                                if (clients >= 0) {
                                                    if (clientCount < 0) {
                                                        clientCount = 0;
                                                    }
                                                    clientCount += clients;
                                                }
                                            } else if ("channels".equals(name)) {
                                                //clientCount += StringUtils.string2int(value,-1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.debug("获取信息时发生异常："+e.getMessage());
                    }
                    device.setMaxTask((long)clientCount);
                }
            }
        };
        scheduExec.scheduleWithFixedDelay(monitorTask, 29, 29, TimeUnit.SECONDS);
    }
    public String getBestHlsServerIp(String url,String clientIp){
        Device server = getBestHlsServer(url,clientIp);
        if(server!=null){
            String result= TcpUtils.getHostFromUrl(server.getUrl());
            int port = TcpUtils.getPortFromUrl(server.getUrl());
            if(port!=80){
                result +=":"+port;
            }
            return result;
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public Device getBestHlsServer(String url,String clientIp){
        List<Device> aliveDevices = new ArrayList<Device>();
        for(Device device:hlsServers){
            if(device!=null){
                if(!STATUS_SHUTDOWN.equals(device.getStatus())){
                    aliveDevices.add(device);
                }
            }
        }
        SlbLog log = new SlbLog();
        if(aliveDevices.size()>0){
            aliveDevices = SortUtils.sortArray(aliveDevices, "maxTask", "asc");
            List<Device> bestDevices = new ArrayList<Device>();
            Long taskCount = -1000L;
            for(Device device:aliveDevices){
                Long currentTask = device.getMaxTask();
                if(taskCount<0||taskCount.equals(currentTask)){
                    bestDevices.add(device);
                    taskCount = currentTask;
                }else{
                    break;
                }
            }

            Device device = getRandomDeviceOf(bestDevices);
            logger.debug("调度到：" + device.getIp());
            saveSlbLog(device,url,clientIp,1L);
            return device;
        }
        Device  result = getRandomDeviceOf(hlsServers);
        logger.warn("没有合适可用的服务器！随机返回一个服务器："+result.getName()+","+result.getIp());
        saveSlbLog(result,url,clientIp,2L);
        return result;
    }

    private SlbLog  saveSlbLog(Device device,String url,String clientIp,Long status){
        SlbLog log = new SlbLog();
        log.setClientIp(clientIp);
        log.setUrl(url);
        log.setServerIp(device.getIp());
        log.setStartTime(new Date());
        log.setStatus(status);
        if("saveSlb".equals(device.getExportPath())){
            log = slbLogLogicInterface.save(log);
        }
        return log;

    }
    public static String  getRandomOf(String[] ips){
        int indexId =(int) Math.round(Math.random()*ips.length);
        if(indexId<0)indexId=0;
        if(indexId>=ips.length){
            indexId = ips.length-1;
        }
        return ips[indexId];
    }

    private static Device  getRandomDeviceOf(List<Device> devices){
        int size = devices.size();
        if(size==0)return null;
        int indexId =(int) Math.round(Math.random()*size);
        if(indexId<0)indexId=0;
        if(indexId>=size){
            indexId = size-1;
        }
        return devices.get(indexId);
    }

    public String getHttpGslbUrl(String requestUrl,String remoateAddr){
        String result = null;
        try {
            if(requestUrl!=null){
                result = requestUrl.replaceAll("hls.inhe.net",getBestHlsServerIp(requestUrl, remoateAddr));
            }
        } catch (Exception e) {
            logger.error("处理调度模块时发生异常，无法正确调度："+e.getMessage());
            return null;
        }
        return result;
    }
    public void shutdown(){
        scheduExec.shutdown();
    }
}
