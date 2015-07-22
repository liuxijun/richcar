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
 * ý�����
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
        //�ڽ���ģʽ�£�property1��Ƶ��ID���������ֶ�û������ֵ����Ҫ��ContentChannel�и��ƹ���
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
            //���ˣ�ֻ����'a-z','A-Z','0-9','/','.',������ͳͳ���˵�
            if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='/'||ch=='.'||ch=='_'||ch=='-'){
                tempStr+=ch;
            }
        }
        saveToFileName=tempStr;
//        saveToZipImageFile = saveToFileName;
        localFileName = saveToFileName;
        File saveToFile = new File(request.getRealPath(saveToFileName));
        boolean result = (FileUtils.copy(uploadFile, saveToFile.getParentFile().getAbsolutePath(), saveToFile.getName()) != null);
        //�ϴ��ɹ�����Ҫͬ���������ļ�̨������
        try {
            JsUtils syncUtils = new JsUtils();
            syncUtils.saveAndPushSynFile(saveToFile.getName(), saveToFile.getAbsolutePath(), saveToFileName, admin.getCspId());
        } catch (Exception e) {
            log.error("�޷�ͬ���ļ���" + saveToFileName);
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
        String logMessage = "�༭�󱣴�ý����ϸ��Ϣ��" + obj.getName() + ",id=" + obj.getId();
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            List<Channel> channels = contentLogicInterface.checkPublishChannels(obj,contentProperties);
            if(channels.size()>0){
                for(Channel channel:channels){
                    logMessage+=",������Ƶ����("+channel.getId()+")"+channel.getName();
                }
            }else{
                logMessage+=",û�з������κ�Ƶ��";
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
            channel.setName("û��������Ŀ");
        }
        String logMsg = "����ֱ����"+obj.getName()+"��";
        if(ContentLogicInterface.STATUS_CP_ONLINE.equals(obj.getStatus())){
            logMsg +="���������ߵ���Ŀ��" +channel.getName()+
                    "����";
            //ֻ������һ��Ƶ������������Ҫ���е���
            List<ContentChannel> channels = contentChannelLogicInterface.getContentPublishedChannels(obj.getId());
            for(ContentChannel cc:channels){
                Long databaseChannelId = cc.getChannelId();
                if(databaseChannelId!=null){
                    if(!databaseChannelId.equals(channelId)){
                        contentChannelLogicInterface.remove(cc);
                    }
                }else{
                    //�������ݣ���Ҫ�����
                    contentChannelLogicInterface.remove(cc);
                }
            }
            contentCspLogicInterface.publishContent(obj.getId(),obj.getCspId(),channelId);
        }else{
            logMsg +="��ȡ������Ŀ��" +channel.getName()+
                    "��������";
            //ȡ��������ʱ�򣬲�ɾ��Ƶ����Ϣ������ᵼ��ҳ������ʱ�޷��������������ڵ������溯��ʱchannelId����Ϊ-1
            contentCspLogicInterface.setStatus(obj.getId(), obj.getCspId(),-1,ContentCspLogicInterface.STATUS_OFFLINE);
        }
        contentPropertyLogicInterface.saveClips(contentProperties, obj, -1);
        writeSysLog(logMsg);
        obj.setIntro("");//���intro����html��ǩ��extJs��ĳЩʱ���Ī������ı���
        clearLocalCache();
        return "save";
    }

    public String saveLive(){
        //ֱ���Ľ�Ŀ��û������contentProperty��������Ҫ�ֹ�����һ�£������Ժ�Ĳ���
        boolean urlIsSetted = false;
        ContentProperty cp = new ContentProperty();
        cp.setContentId(obj.getId());
        Property property = contentLogicInterface.getPropertyByCode("Media_Url_Source");
        if(property!=null){//�ȳ�����Media_Url_Sourceȥ�ң����û��������ԣ���ȡĬ�ϵ�
            cp.setPropertyId(property.getId());
        }else{//û���ҵ�Դ���ԣ����������
            cp.setPropertyId(AppConfigurator.getInstance().getLongConfig("system.import.default.LiveMediaUrlPropertyId",676496266L));
        }
        savePoster(localFileName,uploadFile,"post1Url");
        obj.setModuleId(10000L);
        if(obj.getId()>0){
            //�����½���
            contentProperties = contentPropertyLogicInterface.search(cp);
            if(contentProperties.size()>0){
                cp = contentProperties.get(0);
                contentProperties.clear();
                cp.setIntValue(1L);
                cp.setThumbPic(obj.getPost1Url());
                cp.setName("ֱ��Ƶ��");
                cp.setStringValue(obj.getActors());
                contentProperties.add(cp);
                urlIsSetted = true;
            }
        }
        if(!urlIsSetted){
            contentProperties = new ArrayList<ContentProperty>(1);
            cp.setIntValue(1L);
            cp.setThumbPic(obj.getPost1Url());
            cp.setName("ֱ��Ƶ��");
            cp.setStringValue(obj.getActors());
            contentProperties.add(cp);
        }
        //writeSysLog("����ֱ��Ƶ����"+obj.getName()+","+obj.getActors());
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
                result = result+"\n���á�"+content.getName()+"������ǰ״̬��"+contentLogicInterface.getStatusString(oldContentStatus)+",׼������Ϊ��"+
                    contentLogicInterface.getStatusString(status);
                if(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE.equals(oldContentStatus)&&ContentLogicInterface.STATUS_CP_ONLINE.equals(status)){
                    result+="\ný�塶"+content.getName()+"������ת�룬�����״̬�����ں�������������Σ�գ���˲����������ߣ�����Ϊ����Դ��";
                    willSetContentCspStatus = ContentCspLogicInterface.STATUS_NEW;
                }else{
                    content.setStatus(status);
                }
                result+=";";
                contentLogicInterface.save(content);
                ContentCsp cc = new ContentCsp();
                cc.setContentId(id);
                //�����߲���������ֻ���һ��csp���������ﲻ������cspId
                //cc.setCspId(cspId);
                List<ContentCsp> data = contentCspLogicInterface.search(cc);
                if(data!=null&&data.size()>0){
                    for(ContentCsp contentCsp:data){
                        Long contentCspStatus;
                        Long oldStatus = contentCsp.getStatus();
                        cspId=contentCsp.getCspId();
                        Csp csp = cspLogicInterface.get(cspId);
                        if(ContentLogicInterface.STATUS_CP_ONLINE.equals(status)){
                            //���ԭ�������ߵ���Դ����������״̬ΪҪ���õ�״̬�����򱣳֡�
                            if(ContentCspLogicInterface.STATUS_OFFLINE.equals(oldStatus)||
                                    ContentCspLogicInterface.STATUS_NEW.equals(oldStatus)||
                                    ContentCspLogicInterface.STATUS_ONLINE.equals(oldStatus)) {
                                contentCspStatus = willSetContentCspStatus;
                                result += "SP(" + csp.getName() +
                                        ")��������;";
                            }else{
                                result+="SP״̬����ԭ����״̬;"+contentCspLogicInterface.getStatusString(oldStatus);
                                contentCspStatus = oldStatus;
                            }
                        }else{
                            result+="SP(" +csp.getName()+
                                    ")��Ϊ����;";
                            contentCspStatus = willSetContentCspStatus;
                        }
                        contentCsp.setStatus(contentCspStatus);
                        contentCsp.setStatusTime(new Date());
                        contentCspLogicInterface.save(contentCsp);
                    }
                }else{
                    result+="������Դû�з������κ�CSP";
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
     * ���δͨ��
     * @return "success"
     */
    public String rejectMedia(){
        setContentsStatus(ContentLogicInterface.STATUS_AUDIT_REJECTED);
        return "success";
    }

    /**
     * ɾ�� added by mlwang @2014-10-24
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
