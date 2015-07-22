package com.fortune.rms.web.media;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.*;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.*;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xjliu on 14-6-18.
 * 媒体管理
 */
@Namespace("/V5/media")
@ParentPackage("default")
@Action(value = "media")
@Results({
        @Result(name = "listFiles",location = "/system/deviceListFiles.jsp"),
        @Result(name = "listLives",location = "/system/deviceListLives.jsp")
})
public class MediaAction  extends BaseAction<Content> {
    private static final long serialVersionUID = 3243512334534534l;
    private ContentLogicInterface contentLogicInterface;
    private ChannelLogicInterface channelLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private ContentChannelLogicInterface contentChannelLogicInterface;
/*
    private PropertyLogicInterface propertyLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
*/

    public MediaAction() {
        super(Content.class);
    }

    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
        setBaseLogicInterface(contentLogicInterface);
    }

    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }

    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    private File uploadFile;
    private File uploadFile2;
    private String localFileName;
    private String localFileName2;
    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public File getUploadFile2() {
        return uploadFile2;
    }

    public void setUploadFile2(File uploadFile2) {
        this.uploadFile2 = uploadFile2;
    }

    public String getLocalFileName2() {
        return localFileName2;
    }

    public void setLocalFileName2(String localFileName2) {
        this.localFileName2 = localFileName2;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String listMedia(){
        Long channelId = obj.getProperty1();
        if(channelId==null){
            channelId = -1L;
        }
        objs = contentLogicInterface.getContentsOfChannelAndCp(channelId,obj.getCspId(),obj.getName(),
                obj.getActors(),obj.getDirectors(),
                obj.getStatus(),-1,pageBean);
        return "list";
    }
    public String listLives(){
        long channelId = AppConfigurator.getInstance().getLongConfig("system.compactMode.defaultLiveChannelId", 15884423L);
        obj.setProperty1(channelId);
        return listMedia();
    }
    private Content setDefaultValues(Content obj){
        BeanUtils.setDefaultValue(obj,"status",ContentLogicInterface.STATUS_CP_ONLINE);
        BeanUtils.setDefaultValue(obj,"createTime",new Date());
        BeanUtils.setDefaultValue(obj,"validStartTime",new Date());
        BeanUtils.setDefaultValue(obj,"validEndTime",new Date(System.currentTimeMillis()+10*365*24*3600*1000L));
        BeanUtils.setDefaultValue(obj, "cspId", admin.getCspId().longValue());
        BeanUtils.setDefaultValue(obj, "contentAuditId", 1L);
        BeanUtils.setDefaultValue(obj, "creatorAdminId", admin.getId().longValue());
        BeanUtils.setDefaultValue(obj, "moduleId", 5000L);
        com.fortune.util.BeanUtils.setDefaultValue(obj,"allVisitCount",0L);
        com.fortune.util.BeanUtils.setDefaultValue(obj,"monthVisitCount",0L);
        com.fortune.util.BeanUtils.setDefaultValue(obj,"weekVisitCount",0L);

        return obj;
    }
    public String view(){
        if(null != keyId &&(!"-1".equals(""+keyId))){
            super.view();
        }
        obj = setDefaultValues(obj);
        Long property1 = obj.getProperty1();
        //在紧凑模式下，property1是频道ID，如果这个字段没有设置值，就要从ContentChannel中复制过来
        if(property1==null||property1<=0){
            ContentChannel cc = new ContentChannel();
            cc.setContentId(obj.getId());
            List<ContentChannel> data = contentChannelLogicInterface.search(cc);
            if(data!=null&&data.size()>0){
                property1 = data.get(0).getChannelId();
            }
            obj.setProperty1(property1);
        }
        return "view";
    }

    private boolean savePoster(String localFileName,File uploadFile,String propertyName) {
        if(uploadFile==null){
            return false;
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        if (localFileName == null || "".equals(localFileName)) {
            localFileName = request.getParameter("localFileName");
        }

        if (localFileName == null || "".equals(localFileName)) {
            localFileName = StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".jpg";
        }
        String postfix = FileUtils.getFileExtName(localFileName);
        String saveToFileName = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" +
                HzUtils.getFullSpell(FileUtils.extractFileName(localFileName, "/")).replace(".", "_") + new Date().getTime() % 10000 + "_" + Math.round(Math.random() * 1000000) + "." + postfix;
        String tempStr = "";
        for(char ch:saveToFileName.toCharArray()){
            //过滤，只保留'a-z','A-Z','0-9','/','.',其他的统统过滤掉
            if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='/'||ch=='.'||ch=='_'||ch=='-'){
                tempStr+=ch;
            }
        }
        saveToFileName=tempStr;
//        saveToZipImageFile = saveToFileName;
        localFileName = saveToFileName;
        File saveToFile = new File(request.getRealPath(saveToFileName));
        boolean result = (FileUtils.copy(uploadFile, saveToFile.getParentFile().getAbsolutePath(), saveToFile.getName()) != null);
        //上传成功后，需要同步到其他的几台机器上
        try {
            JsUtils syncUtils = new JsUtils();
            syncUtils.saveAndPushSynFile(saveToFile.getName(), saveToFile.getAbsolutePath(), saveToFileName, admin.getCspId());
        } catch (Exception e) {
            log.error("无法同步文件：" + saveToFileName);
        }
        SimpleFileInfo fileInfo = new SimpleFileInfo(saveToFile.getAbsolutePath(), saveToFile.length(),
                new Date(saveToFile.lastModified()), false, FileType.image);
        FileUtils.setFileMediaInfo(saveToFile.getAbsolutePath(), fileInfo);
        BeanUtils.setProperty(obj,propertyName,saveToFileName);
        //obj.setPost1Url(saveToFileName);
        return result;
    }

    public String saveAll(){
//        obj = setDefaultValues(obj);
        Integer cspId = admin.getCspId();
        if(cspId==null){
            cspId = 1;
        }
        if(obj.getCspId()==null){
            obj.setCspId(cspId.longValue());
        }
        obj = contentPropertyLogicInterface.saveClips(contentProperties, obj, -1L);
        String logMessage = "编辑后保存媒体详细信息：" + obj.getName() + ",id=" + obj.getId();
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            List<Channel> channels = contentLogicInterface.checkPublishChannels(obj,contentProperties);
            if(channels.size()>0){
                for(Channel channel:channels){
                    logMessage+=",发布到频道：("+channel.getId()+")"+channel.getName();
                }
            }else{
                logMessage+=",没有发布到任何频道";
            }
            clearLocalCache();
        }
        writeSysLog(logMessage);
        return "save";
    }

    public String save(){
        //savePoster(localFileName,uploadFile,"post1Url");
        //savePoster(localFileName2,uploadFile2,"post2Url");
        obj = setDefaultValues(obj);
        obj = contentLogicInterface.save(obj);
        Long channelId = obj.getProperty1();
        if(channelId==null){
            channelId =  AppConfigurator.getInstance().getLongConfig("system.compactMode.defaultLiveChannelId", 15884423L);
        }
        Channel channel;
        if(channelId>0){
            channel = channelLogicInterface.get(channelId);
        }else{
            channel = new Channel();
            channel.setName("没有设置栏目");
        }
        String logMsg = "保存直播《"+obj.getName()+"》";
        if(ContentLogicInterface.STATUS_CP_ONLINE.equals(obj.getStatus())){
            logMsg +="并发布上线到栏目“" +channel.getName()+
                    "”！";
            //只发布到一个频道，所以这里要进行调整
            List<ContentChannel> channels = contentChannelLogicInterface.getContentPublishedChannels(obj.getId());
            for(ContentChannel cc:channels){
                Long databaseChannelId = cc.getChannelId();
                if(databaseChannelId!=null){
                    if(!databaseChannelId.equals(channelId)){
                        contentChannelLogicInterface.remove(cc);
                    }
                }else{
                    //垃圾数据，需要清除！
                    contentChannelLogicInterface.remove(cc);
                }
            }
            contentCspLogicInterface.publishContent(obj.getId(),obj.getCspId(),channelId);
        }else{
            logMsg +="并取消在栏目“" +channel.getName()+
                    "”发布！";
            //取消发布的时候，不删除频道信息。否则会导致页面搜索时无法搜索到。所以在调用下面函数时channelId设置为-1
            contentCspLogicInterface.setStatus(obj.getId(), obj.getCspId(),-1,ContentCspLogicInterface.STATUS_OFFLINE);
        }
        contentPropertyLogicInterface.saveClips(contentProperties, obj, -1);
        writeSysLog(logMsg);
        obj.setIntro("");//如果intro中有html标签，extJs在某些时候会莫名其妙的报错！
        clearLocalCache();
        return "save";
    }

    public String saveLive(){
        //直播的节目，没有设置contentProperty，在这里要手工处理一下，方便以后的播放
        boolean urlIsSetted = false;
        ContentProperty cp = new ContentProperty();
        cp.setContentId(obj.getId());
        Property property = contentLogicInterface.getPropertyByCode("Media_Url_Source");
        if(property!=null){//先尝试用Media_Url_Source去找，如果没有这个属性，就取默认的
            cp.setPropertyId(property.getId());
        }else{//没有找到源属性，就设置这个
            cp.setPropertyId(AppConfigurator.getInstance().getLongConfig("system.import.default.LiveMediaUrlPropertyId",676496266L));
        }
        savePoster(localFileName,uploadFile,"post1Url");
        obj.setModuleId(10000L);
        if(obj.getId()>0){
            //不是新建的
            contentProperties = contentPropertyLogicInterface.search(cp);
            if(contentProperties.size()>0){
                cp = contentProperties.get(0);
                contentProperties.clear();
                cp.setIntValue(1L);
                cp.setThumbPic(obj.getPost1Url());
                cp.setName("直播频道");
                cp.setStringValue(obj.getActors());
                contentProperties.add(cp);
                urlIsSetted = true;
            }
        }
        if(!urlIsSetted){
            contentProperties = new ArrayList<ContentProperty>(1);
            cp.setIntValue(1L);
            cp.setThumbPic(obj.getPost1Url());
            cp.setName("直播频道");
            cp.setStringValue(obj.getActors());
            contentProperties.add(cp);
        }
        //writeSysLog("保存直播频道："+obj.getName()+","+obj.getActors());
        //clearLocalCache();
        return save();
    }


    public String setContentsStatus(Long status){
        long cspId=admin.getCspId();
        String result = "";
        for(String key:keys){
            long id = StringUtils.string2long(key,-1);
            if(id>0){
                Content content = contentLogicInterface.get(id);
                Long oldContentStatus = content.getStatus();
                Long willSetContentCspStatus=ContentLogicInterface.STATUS_CP_ONLINE.equals(status)?
                        ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED:ContentCspLogicInterface.STATUS_OFFLINE;
                result = result+"\n设置《"+content.getName()+"》，当前状态："+contentLogicInterface.getStatusString(oldContentStatus)+",准备设置为："+
                    contentLogicInterface.getStatusString(status);
                if(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE.equals(oldContentStatus)&&ContentLogicInterface.STATUS_CP_ONLINE.equals(status)){
                    result+="\n媒体《"+content.getName()+"》正在转码，因此其状态可能在后续的设置中有危险，因此不设置其上线，设置为新资源！";
                    willSetContentCspStatus = ContentCspLogicInterface.STATUS_NEW;
                }else{
                    content.setStatus(status);
                }
                result+=";";
                contentLogicInterface.save(content);
                ContentCsp cc = new ContentCsp();
                cc.setContentId(id);
                //上下线操作并不是只针对一个csp，所以这里不能设置cspId
                //cc.setCspId(cspId);
                List<ContentCsp> data = contentCspLogicInterface.search(cc);
                if(data!=null&&data.size()>0){
                    for(ContentCsp contentCsp:data){
                        Long contentCspStatus;
                        Long oldStatus = contentCsp.getStatus();
                        cspId=contentCsp.getCspId();
                        Csp csp = cspLogicInterface.get(cspId);
                        if(ContentLogicInterface.STATUS_CP_ONLINE.equals(status)){
                            //如果原来是下线的资源，就设置其状态为要设置的状态，否则保持。
                            if(ContentCspLogicInterface.STATUS_OFFLINE.equals(oldStatus)||
                                    ContentCspLogicInterface.STATUS_NEW.equals(oldStatus)||
                                    ContentCspLogicInterface.STATUS_ONLINE.equals(oldStatus)) {
                                contentCspStatus = willSetContentCspStatus;
                                result += "SP(" + csp.getName() +
                                        ")发布上线;";
                            }else{
                                result+="SP状态保持原来的状态;"+contentCspLogicInterface.getStatusString(oldStatus);
                                contentCspStatus = oldStatus;
                            }
                        }else{
                            result+="SP(" +csp.getName()+
                                    ")改为下线;";
                            contentCspStatus = willSetContentCspStatus;
                        }
                        contentCsp.setStatus(contentCspStatus);
                        contentCsp.setStatusTime(new Date());
                        contentCspLogicInterface.save(contentCsp);
                    }
                }else{
                    result+="，该资源没有发布到任何CSP";
                }
            }
        }
        writeSysLog(result);
        clearLocalCache();
        return result;
    }
    public String clearLocalCache(){
        CacheUtils.clearAll();
        ActionContext ctx = ServletActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        final int port = request.getServerPort();
        Thread thread=new Thread(){
            public void run(){
                ServerMessager messager = new ServerMessager();
                messager.getMessage("127.0.0.1",port,"/interface/refreshAllCache.jsp",null);
            }
        };
        thread.start();
        return "success";
    }
    public String publishMedia(){
        setContentsStatus(ContentLogicInterface.STATUS_CP_ONLINE);
        return "success";
    }

    /**
     * 审核未通过
     * @return "success"
     */
    public String rejectMedia(){
        setContentsStatus(ContentLogicInterface.STATUS_AUDIT_REJECTED);
        return "success";
    }

    /**
     * 删除 added by mlwang @2014-10-24
     * @return "success"
     */
    public String removeMedia(){
        setContentsStatus(ContentLogicInterface.STATUS_DELETE);
        return "success";
    }

    public String unPublishMedia(){
        setContentsStatus(ContentLogicInterface.STATUS_CP_OFFLINE);
        return "success";
    }

    private Map<String,Object> exportResult;
    public String exportMedia(){
        return "export";
    }


    public String publishLive(){
        return publishMedia();
    }

    public String unPublishLive(){
        return unPublishMedia();
    }
    private List<ContentProperty> contentProperties;

    public List<ContentProperty> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(List<ContentProperty> contentProperties) {
        this.contentProperties = contentProperties;
    }


}
