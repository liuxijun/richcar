package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.csp.logic.logicInterface.CspDeviceLogicInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.rms.business.system.dao.daoInterface.DeviceDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.server.message.LiveHttpMessager;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("deviceLogicInterface")
public class DeviceLogicImpl extends BaseLogicImpl<Device>
		implements
			DeviceLogicInterface {
    private DeviceDaoInterface deviceDaoInterface;
    private CspDeviceLogicInterface cspDeviceLogicInterface;
	/**
	 * @param deviceDaoInterface the deviceDaoInterface to set
	 */
    @Autowired
	public void setDeviceDaoInterface(DeviceDaoInterface deviceDaoInterface) {
		this.deviceDaoInterface = deviceDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.deviceDaoInterface;
	}

    @Autowired
    public void setCspDeviceLogicInterface(CspDeviceLogicInterface cspDeviceLogicInterface) {
        this.cspDeviceLogicInterface = cspDeviceLogicInterface;
    }

    public List<Device> getDevicesOfStatus(int status) {
        return this.deviceDaoInterface.getDevicesOfStatus(status);
    }

    public List<Device> getDevicesExceptMasterDevice(String masterIp) {
        return this.deviceDaoInterface.getDevicesExceptMasterDevice(masterIp);
    }

    public List<Device> getDevicesOfType(long type, long status,long cspId) {
        return deviceDaoInterface.getDevicesByCspId(type,status,cspId);
    }

    public List<Device> getDevicesByCspId(long cspId) {
        return this.deviceDaoInterface.getDevicesByCspId(cspId);
    }

    public String getDeviceUrlByDeviceName(String name) {
        return this.deviceDaoInterface.getDeviceUrlByDeviceName(name);
    }
    public static String getServerMessage(String parameters){
        ServerMessager messager = new ServerMessager();
        return messager.postToHost(AppConfigurator.getInstance().getConfig("system.movie.fileInterfaceUrl",
                        "http://127.0.0.1:8080/interface/files.jsp"),
                parameters,"UTF-8");

    }

    /**
     * 向中心服务器询问服务器的本地根目录
     * @param deviceId 服务器id
     * @param cspId  cspId
     * @return 服务器的根目录
     */
    public String getServerRootFromCenter(Long deviceId,Long cspId){
        String serverData = getServerMessage("command=getServerLocalPath&deviceId="+deviceId+"&cspId="+cspId);
        String result = null;
        if(serverData!=null&&!"".equals(serverData.trim())){
            JSONObject jsonObject = JsonUtils.getJsonObj(serverData);
            result = jsonObject.getString("rootPath");
        }
        if(result==null||"".equals(result.trim())){
            return AppConfigurator.getInstance().getConfig("system.movie.pathName","/home/fortune/movie/");
        }
        return  result;
    }

    public String getServerLocalPath(long serverId,Long cspId){
        String serverPath=null;
        if(serverId<=0){
            serverPath = null;
            List<Device> streamServers;
            if(cspId==null||cspId<=1){
                //这是超级用户或者小版本用户，允许列出全部
                cspId=-1L;
            }
            streamServers = getDevicesOfType(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD,DeviceLogicInterface.DEVICE_ONLINE,cspId);
            if(streamServers!=null&&streamServers.size()==1){
                serverPath = streamServers.get(0).getLocalPath();
            }
        }else{
            try{
                Device server = get(serverId);
                if(server!=null){
                    serverPath  = server.getLocalPath();
                }
            }catch(Exception e){
                logger.error("没有找到服务器:"+serverId);
            }
        }
        logger.debug("serverId= " + serverId+
                ",serverPath="+serverPath);
        if(serverPath == null){
            serverPath = ConfigManager.getInstance().getConfig("system.movie.pathName","c:/movie/");
        }
        return serverPath;
    }

    public List<SimpleFileInfo> getFtpFiles(long deviceId,String path,String name,PageBean pageBean,boolean absDir){
        FtpUtils ftpUtils = new FtpUtils();

        List<SimpleFileInfo> result = new ArrayList<SimpleFileInfo>();
        if(deviceId<0){
            logger.warn("没有输入deviceId，直接返回！");
            return result;
        }
        Device encoder = get(deviceId);
        if(encoder==null){
            return result;
        }
        if(pageBean == null){
            pageBean = new PageBean(0,Integer.MAX_VALUE,"","");
        }
        try {
            boolean connectResult = ftpUtils.connectServer(encoder.getSafeServerIp(),encoder.getFtpPort().intValue(),encoder.getFtpUser(),
                    encoder.getFtpPwd(),encoder.getFtpPath());
            //修正一下orderby
            if(!connectResult){
                logger.error("无法连接到FTP服务器："+encoder.getName()+","+encoder.getIp()+":"
                        +encoder.getFtpPort()+",login:"+encoder.getFtpUser()+"/"+encoder.getFtpPwd());
                return result;
            }
            String orderBy = pageBean.getOrderBy();
            if(orderBy!=null && !"".equals(orderBy)){
                if(orderBy.startsWith("o1.")){
                    logger.debug("order by 已经被修正！原来的："+orderBy);
                    orderBy = orderBy.substring(3);
                }
            }
            if(path==null){
                path = "";
            }
            if(!absDir){
                String serverPath = encoder.getFtpPath();
                if(serverPath==null||"".equals(serverPath.trim())){
                    serverPath = "";
                }
                if(!serverPath.endsWith("/")){
                    serverPath=serverPath+"/";
                }
                path = serverPath+path;
            }
            while(path.contains("//")){
                path = path.replace("//","/");
            }
            logger.debug("准备列取目录："+path);
            SearchResult<FTPFile> files = ftpUtils.getFileListEx(path,name,orderBy,pageBean.getOrderDir(),pageBean.getStartRow(),
                    pageBean.getPageSize());
            if(files!=null){
                for(FTPFile file:files.getRows()){
                    result.add(new SimpleFileInfo(file.getName(),file.getSize(),file.getTimestamp().getTime(),
                            file.isDirectory(),FileUtils.getFileType(file.getName())));
                }
                pageBean.setRowCount(files.getRowCount());
            }
        } catch (IOException e) {
            logger.error("连接到【ID=" +deviceId+
                    "‘"+encoder.getName()+"(" +encoder.getIp()+":"+ encoder.getFtpPort()+
                    ")’】发生错误："+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("连接到【ID=" +deviceId+
                    "‘"+encoder.getName()+"(" +encoder.getIp()+":"+ encoder.getFtpPort()+
                    ")’】发生错误："+e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                ftpUtils.closeServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<SimpleFileInfo> listRemoteFiles(long deviceId,long cspId,String ip,int port,String path,String fileNameRegEx,PageBean pageBean,boolean withSubDir){
        List<SimpleFileInfo> results = new ArrayList<SimpleFileInfo>();
        ServerMessager messager = new ServerMessager();
        try {
            path = URLEncoder.encode(URLEncoder.encode(path, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String remoteResult = messager.postToHost("http://"+ip+":"+port+"/interface/files.jsp?command=list&filePath="+path+"&filter="
                +fileNameRegEx+"&order="+pageBean.getOrderBy()+"&dir="+pageBean.getOrderDir()+"&deviceId="+deviceId+"&cspId="+cspId+
                "&start="+pageBean.getStartRow()+"&limit="+pageBean.getPageSize(),null,"UTF-8");
        logger.debug("服务器端返回："+remoteResult);
        if(remoteResult!=null&&!"".equals(remoteResult)){
            JSONObject result = JsonUtils.getJsonObj(remoteResult);
            int total = result.getInt("total");
            if(total >= 0){
                pageBean.setRowCount(total);
                logger.debug("共有数据："+total+"个");
                JSONArray objs = result.getJSONArray("objs");
                if(objs!=null){
                    for(int i=0,l=objs.size();i<l;i++){
                        JSONObject jsonObject = objs.getJSONObject(i);
                        try {
//                            logger.debug("尝试恢复："+jsonObject.toString());
                            SimpleFileInfo file =(SimpleFileInfo) JSONObject.toBean(jsonObject,SimpleFileInfo.class);
                            if(file!=null){
                                file.setModifyDate(StringUtils.string2date(jsonObject.getString("modifyDate")));
                                results.add(file);
                            }
                        } catch (Exception e) {
                            logger.error("格式化数据时发生了异常："+e.getMessage()+",JSON数据是："+jsonObject.toString());
                            e.printStackTrace();
                        }
                    }
                }else{
                    logger.error("没有获取到任务");
                }
            }else{
                logger.debug("服务器" +ip+":"+port+
                        "发来的数据："+remoteResult+",没有任何文件");
            }
        }else{
            logger.error("无法获取远端数据");
        }
        return results;
    }

    public List<SimpleFileInfo> listFiles(long serverId,long cspId,String dirType, String path, String fileNameRegEx, PageBean pageBean,boolean withSubDir) {
        boolean listRemoteSide = ConfigManager.getInstance().getConfig("system.movie.listRemoteMode", false);
        List<SimpleFileInfo> results = new ArrayList<SimpleFileInfo>();
        if(listRemoteSide){
            Device server = get(serverId);
            if(server!=null){
                return (listRemoteFiles(serverId,cspId,server.getSafeServerIp(),server.getMonitorPort(),path,fileNameRegEx,pageBean,withSubDir));
            }
            return results;
        }

        if(path==null){
            path = "";
        }
        path = getServerLocalPath(serverId,-1L)+"/" + path;
        if(fileNameRegEx==null||"".equals(fileNameRegEx)){
            fileNameRegEx = "*";
        }
        if(pageBean==null){
            pageBean = new PageBean();
            pageBean.setPageSize(Integer.MAX_VALUE);
        }
        String orderBy = pageBean.getOrderBy();
        if(orderBy==null||"".equals(orderBy.trim())){
            orderBy = "name";
            pageBean.setOrderDir("asc");
        }
        if(orderBy.startsWith("o1.")){
            orderBy = orderBy.substring(3);
        }
        List<SimpleFileInfo> tempResults = FileUtils.listFiles(path, fileNameRegEx, orderBy, pageBean.getOrderDir(), withSubDir);

        pageBean.setRowCount(tempResults.size());
        logger.debug("列取了目录'" +path+
                "'下的'" + fileNameRegEx+
                "'数据，条目为："+pageBean.getRowCount()+",排序："+pageBean.getOrderBy()+","+pageBean.getOrderDir()+"," +
                "start:"+pageBean.getStartRow()+","+pageBean.getPageSize());
        for(int i=pageBean.getStartRow();i<pageBean.getStartRow()+pageBean.getPageSize();i++){
            if(i<0){
                i=0;
            }
            if(i>=tempResults.size()){
                break;
            }
            SimpleFileInfo fileInfo = tempResults.get(i);
            String fileName =path+"/"+fileInfo.getName();
            FileUtils.setFileMediaInfo(fileName,fileInfo);
            results.add(tempResults.get(i));
        }
        return results;
    }

    public List<SimpleFileInfo> listFiles(long serverId,long cspId, String path, String fileNameRegEx, PageBean pageBean) {
        return listFiles(serverId,cspId,"ad",path,fileNameRegEx,pageBean,false);
    }
    public List<SimpleFileInfo> listFiles(long serverId, String path, String fileNameRegEx) {
        return listFiles(serverId,-1, path, fileNameRegEx, new PageBean(0, Integer.MAX_VALUE, null, null));
    }

    public List<SimpleFileInfo> listFiles(long serverId, String path) {
        return listFiles(serverId,path,null);
    }

    public  List<Map<String,String>>  listLives(long serverId){
        Device server = get(serverId);
        if(server!=null){
            LiveHttpMessager liveHttpMessager = new LiveHttpMessager();
            return liveHttpMessager.getLives(server.getSafeServerIp(),server.getMonitorPort(),server.getFtpPwd());
        }
        return new ArrayList<Map<String,String>>(0);
    }

    public Map<Integer, Integer> getServerCount() {
        List<Object[]> countList = deviceDaoInterface.getServerCount();
        Map<Integer,Integer> result = new HashMap<Integer,Integer>();
        for(Object[] row:countList){
            Integer type=StringUtils.string2int(row[0].toString(),-1);
            if(type>=0){
                Integer count = StringUtils.string2int(row[1].toString(),0);
                result.put(type,count);
            }
        }
        return result;
    }

    /**
     * 随机选择一个直播服务器，用于分配给统一转码使用
     * @return 选中的Device，如果没有合适的，返回null
     */
    public Device getRandomLiveServer(){
        List<Device> deviceList = deviceDaoInterface.getDevicesByCspId(DEVICE_TYPE_HLS_LIVE, DEVICE_ONLINE, -1);
        if(deviceList == null || deviceList.size() == 0) return null;

        Random r = new Random((new Date()).getTime());
        int index = r.nextInt() % deviceList.size();
        return deviceList.get(index);
    }

    public Device save(Device device){
        Device result = super.save(device);
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            CspDevice cspDevice = new CspDevice();
            cspDevice.setDeviceId(result.getId());
            cspDevice.setCspId(2L);
            List<CspDevice> cspDevices = cspDeviceLogicInterface.search(cspDevice);
            if(cspDevices.size()<=0){
                cspDeviceLogicInterface.save(cspDevice);
            }
        }
        return result;
    }
}
