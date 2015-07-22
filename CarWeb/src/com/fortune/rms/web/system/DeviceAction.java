package com.fortune.rms.web.system;

import com.fortune.common.Constants;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.midware.RegMidware;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.*;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Namespace("/system")
@ParentPackage("default")
@Action(value="device")
@Results({
        @Result(name = "listFiles",location = "/system/deviceListFiles.jsp"),
        @Result(name = "listLives",location = "/system/deviceListLives.jsp")
})

@SuppressWarnings("unused")
public class DeviceAction extends BaseAction<Device> {
	private DeviceLogicInterface deviceLogicInterface;
    private ContentLogicInterface contentLogicInterface;
	@SuppressWarnings("unchecked")
	public DeviceAction() {
		super(Device.class);
	}
	/**
	 * @param deviceLogicInterface the deviceLogicInterface to set
	 */

    public void setDeviceLogicInterface(
			DeviceLogicInterface deviceLogicInterface) {
		this.deviceLogicInterface = deviceLogicInterface;
		setBaseLogicInterface(deviceLogicInterface);
	}

    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public String getCspDevice(){
        long cspId = getRequestIntParam("cpId",0);
        try{
//            objs = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from Device d where d.status=1 and d.id in (select cd.deviceId from CspDevice cd where cd.cspId="+cspId+")");
        objs = deviceLogicInterface.getDevicesByCspId(cspId);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Constants.ACTION_LIST;
    }

    public String getContentRegUrl(){
        HttpServletRequest request = ServletActionContext.getRequest();
        String userIp = request.getRemoteAddr();

        long contentId = getRequestIntParam("contentId",0);
        String url = getRequestParam("url","");
        try{
            Content content = contentLogicInterface.get(contentId);
            if (content!=null && content.getDeviceId()!=null){
                Device device = deviceLogicInterface.get(content.getDeviceId());
                if (device!=null){
                    RegMidware regMidware = new RegMidware();
                    url = device.getUrl() + url;
                    url = regMidware.regUrl(url, userIp);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        writeSysLog("保存视频链接： "+url);
        super.addActionMessage(url);
        return Constants.ACTION_SAVE;
    }

    public String getDeviceRegUrl(){
        HttpServletRequest request = ServletActionContext.getRequest ();
        String userIp = request.getRemoteAddr();

        long deviceId = getRequestIntParam("deviceId",0);
        String url = getRequestParam("url","");
        try{
            Device device = deviceLogicInterface.get(deviceId);
            if (device!=null){
                url = device.getUrl() + url;
            }
//            RegMidware regMidware = new RegMidware();
//            url = regMidware.regUrl(url, userIp);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        writeSysLog("保存设备链接： "+url);
        super.addActionMessage(url);
        return Constants.ACTION_SAVE;
    }

    @SuppressWarnings("unchecked")
    public String listDevices(){
        objs = (List<Device>) CacheUtils.get("allDevice|=|"+pageBean+"|=|"+obj,"allDevice",
                new DataInitWorker(){
                    public Object init(Object key,String cacheName){
                        log.debug("正在初始化设备列表");
                        String[] keyStr = key.toString().split("|=|");
                        JsonUtils jsonUtils = new JsonUtils();
                        PageBean pageInfo = (PageBean) jsonUtils.getObjectFromJsonString(PageBean.class,keyStr[1]);
                        Device device = (Device) jsonUtils.getObjectFromJsonString(Device.class,keyStr[2]);
                        return deviceLogicInterface.search(device,pageInfo);
                    }
                }
        );
        return "list";
    }
    public String filePath;
    public String filter;
    
    List<SimpleFileInfo> files;

    public String listFtpFiles(){
        files = deviceLogicInterface.getFtpFiles(obj.getId(),filePath, filter,pageBean,false);
        return "listFiles";
    }
    public String listFiles() {
//        files = deviceLogicInterface.listFiles(obj.getId(),"",filePath, filter,pageBean,false);
        files = deviceLogicInterface.getFtpFiles(obj.getId(),filePath, filter,pageBean,false);
        return "listFiles";
    }

    public String listLocalFiles() {
        if(obj.getId()>0){
            try {
                obj = deviceLogicInterface.get(obj.getId());
            } catch (Exception e) {
                obj.setId(-1);
                e.printStackTrace();
            }
        }
        files = null;
        if(obj.getId()<=0){
            List<Device> streamServers;
            Integer cspId = admin.getCspId();
            if(cspId==null||cspId<=1){
                //这是超级用户或者小版本用户，允许列出全部
                cspId=-1;
            }
            long browseServerType = AppConfigurator.getInstance().getLongConfig("system.encoder.browseServerType",DeviceLogicInterface.DEVICE_TYPE_HLS_VOD);
            streamServers = deviceLogicInterface.getDevicesOfType(browseServerType,DeviceLogicInterface.DEVICE_ONLINE,cspId);
            if(streamServers==null){
                streamServers = new ArrayList<Device>();
            }
            streamServers.addAll(deviceLogicInterface.getDevicesOfType(DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE,DeviceLogicInterface.DEVICE_ONLINE,cspId));
            if(streamServers.size()>0){
                for(Device device:streamServers){
                    SimpleFileInfo file = new SimpleFileInfo("服务器："+device.getName(),device.getId(),new Date(),true,FileType.streamServer);
                    obj = device;
                    if(files==null){
                        files = new ArrayList<SimpleFileInfo>();
                    }
                    files.add(file);
                }
            }
        }
        //如果只有一个服务器或者一个都没有，就直接尝试列取目录
        if(files==null||files.size()==1){
            obj = deviceLogicInterface.get(obj.getId());
            if(obj!=null){
                if(DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE==obj.getType()){
                    lives = deviceLogicInterface.listLives(obj.getId());
                    return "listLives";
                }else{
                    files = deviceLogicInterface.listFiles(obj.getId(),admin.getCspId(),"",filePath, filter,pageBean,false);
                }
            }
        }
        if(files==null){
            files = new ArrayList<SimpleFileInfo>(0);
        }
//        files = deviceLogicInterface.getFtpFiles(obj.getId(),filePath, filter,pageBean,false);
        return "listFiles";
    }

    public String getFilesJson() {
        return JsonUtils.getListJsonString("objs", files, "totalCount", pageBean.getRowCount());
    }

    public String listWebFiles(){
        ServletContext application = ServletActionContext.getServletContext();
        String basePath = obj.getLocalPath();
        if("page".equals(basePath)||"upload".equals(basePath)||"snap".equals(basePath)||"post".equals(basePath)){
            if(filter==null){
                filter = "*";
            }
            files = FileUtils.listFiles(application.getRealPath("/" +basePath+
                    "/"+filePath), filter, pageBean, false);
        }else{
            files = new ArrayList<SimpleFileInfo>(1);
            files.add(new SimpleFileInfo("目录名无效："+basePath,0,new Date(),false,FileType.doc));
        }
        return "listFiles";
    }
    private List<Map<String,String>> lives;
    public String listLives(){
        lives = deviceLogicInterface.listLives(obj.getId());
        return "listLives";
    }
    public String getLivesJson(){
        return JsonUtils.getListJsonString("objs", lives, "totalCount", lives.size());
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        if(filePath!=null){
            int repeatTimes = 0;
            while(filePath.contains("%")){
                try {
                    filePath = URLDecoder.decode(filePath,"UTF-8");
                } catch(Exception e) {
                    log.error(e);
                }
                repeatTimes++;
                if(repeatTimes>=5){
                    break;
                }
            }
        }
        this.filePath = filePath;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<SimpleFileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<SimpleFileInfo> list) {
        this.files = list;
    }
}
