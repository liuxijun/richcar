package com.fortune.rms.web.media;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentFile;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-10
 * Time: 14:12:18
 * RedexProʹ�õ�media��ط���
 */
@Namespace("/media")
@ParentPackage("default")
@Action(value = "rp-m")
@Results({
        @Result(name = "mediaBaseInfo",location = "/pub/pubChannel.jsp"),
        @Result(name = "mediaProp",location = "/pub/pubProp.jsp")
})

public class RedexProMediaAction extends BaseAction<Content> {
    private File moviePoster;
    private String moviePosterFileName;
    private ContentLogicInterface contentLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private PropertyLogicInterface propertyLogicInterface;

    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public void setPropertyLogicInterface(PropertyLogicInterface propertyLogicInterface) {
        this.propertyLogicInterface = propertyLogicInterface;
    }

    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    public RedexProMediaAction() {
        super(Content.class);
    }

    public String getMoviePosterFileName() {
        return moviePosterFileName;
    }

    public void setMoviePosterFileName(String moviePosterFileName) {
        this.moviePosterFileName = moviePosterFileName;
    }

    public String getMovieBigPosterFileName() {
        return movieBigPosterFileName;
    }

    public void setMovieBigPosterFileName(String movieBigPosterFileName) {
        this.movieBigPosterFileName = movieBigPosterFileName;
    }

    public File getMovieBigPoster() {
        return movieBigPoster;
    }

    public void setMovieBigPoster(File movieBigPoster) {
        this.movieBigPoster = movieBigPoster;
    }

    public File getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(File moviePoster) {
        this.moviePoster = moviePoster;
    }

    private File movieBigPoster;
    private String movieBigPosterFileName;

    // added by mlwang @2014-10-10�������ļ���Ϣ��ת����������ҳ��
    @SuppressWarnings("unchecked")
    public String mediaProp(){
        List<ContentFile> fileList = (List<ContentFile>)session.get("contentFiles");
        if(fileList==null){
            fileList = new ArrayList<ContentFile>(5);
            session.put("contentFiles", fileList);
        }
        return "mediaProp";
    }
    public List<String> importXml(boolean checkOnly,String fullFileName,Long cspId,String file,String cspFilePath,
                                  ContentLogicInterface contentLogicInterface,Admin admin,Long deviceId,Long moduleId,
                                  String xmlEncoding,String webAppRoot,String clientIp){
        List<String> list = contentLogicInterface.importXml(fullFileName, cspId, admin.getId(), deviceId, moduleId, xmlEncoding,
                webAppRoot,cspFilePath,file,checkOnly);
        //����������߳̽��д����Ͳ��ܼ򵥵ĵ���writeSysLog�����������������Լ����³�ʼ��һ��systemLogLogicInterface������
        SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBeanForApp("systemLogLogicInterface");
        if(list!=null){
            String logHeader;
            if(checkOnly){
                if(list.size()>0){
                    logHeader = "���XML:"+fullFileName+"\r\n";
                }else{
                    logHeader = "���XML:"+fullFileName+",���δ֪��";
                }
            }else{
                logHeader = "����xml��"+ fullFileName+"...\r\n";
            }
            String resultStr = logHeader;
            int count=list.size();
            int start = 1;
            int end=0;
            for(String str:list){
                int l = resultStr.length();
                int l0 = str.length();
                end++;
                if((l+l0)>1990){
                    systemLogLogicInterface.saveLog(clientIp,admin,getClass().getName(),"("+start+"-"+end+"/"+count+")"+resultStr);
                    resultStr = logHeader;
                    start = end+1;
                }
                resultStr +="\r\n"+str;
            }
            systemLogLogicInterface.saveLog(clientIp,admin,getClass().getName(),"("+start+"-"+end+"/"+count+")"+resultStr);
        }
        return list;
    }
    //����������xml�ļ�
    @SuppressWarnings("deprecation")
    public String batchImport() {
        String xmlFileName = null;
        //�õ���ǰ��½�˵�ID
        //Admin admin = (Admin) session.get(Constants.SESSION_ADMIN);
        ActionContext ctx = ServletActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        Integer checkCspId = admin.getCspId();
        if(checkCspId==null||checkCspId<=1){
            checkCspId = AppConfigurator.getInstance().getIntConfig("system.defaultCspId",2);
        }
        final Long cspId = checkCspId.longValue();
        //����cspId��csp���ҵ���Ӧ��csp����
        try {
            xmlFileName = URLDecoder.decode(fileName, "UTF-8");//���������ļ���������
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String fullFileName;
        Device device = deviceLogicInterface.get(obj.getDeviceId());
        fullFileName = device.getLocalPath()+"/"+xmlFileName;
        final boolean checkOnly="checkOnly".equals(importXmlCommand);
        final File xmlFile = new File(fullFileName);
        if(!xmlFile.exists()){
            log.debug("�ļ������ڣ����Ե������������أ�"+xmlFile.getAbsolutePath());
            contentLogicInterface.downloadFile(device, fileName,xmlFile.getAbsolutePath());
            log.debug("���ع�����ϣ����ؽ����"+xmlFile.exists()+","+xmlFile.getAbsolutePath());
        }
        final String finalXmlFileName = xmlFileName;
        final Long finalDeviceId= device.getId();
        final String webAppRoot = ServletActionContext.getRequest().getRealPath("/");
        final Admin a = admin;
        final String finalXmlEncoding = xmlEncoding;
        final Long finalModuleId = moduleId;
        final ContentLogicInterface finalContentLogicInterface = contentLogicInterface;
        final String clientIp = request.getRemoteAddr();
        final String cspFilePath = admin.getLogin();
        List<String> result;
        if(xmlFile.exists()){
            if((!checkOnly)&&xmlFile.length()>100*1024){//�ļ�����100KB���ں�̨�������
                result = new ArrayList<String>();
                Thread task = new Thread(){
                    public void run(){
                        //��ΪcheckOnly������϶���false�����Ե�һ��������ֱ��дfalse
                        importXml(false,xmlFile.getAbsolutePath(),cspId,finalXmlFileName,cspFilePath,
                                finalContentLogicInterface,a,finalDeviceId,finalModuleId,finalXmlEncoding,webAppRoot,
                                clientIp);
                    }
                };
                task.start();
                result.add("����XML�ϴ������Ѿ�תΪ��̨��������ӰƬ������ȷ�ϵ���������");
            }else{
                result = importXml(checkOnly,xmlFile.getAbsolutePath(),cspId,xmlFileName,cspFilePath,
                        finalContentLogicInterface,a,finalDeviceId,finalModuleId,finalXmlEncoding,webAppRoot,clientIp);
            }
        }else{
            result = new ArrayList<String>();
            result.add("XML�ļ�û���ҵ���"+finalXmlFileName);
        }
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("messages", result, "totalCount", result.size()));
        return null;
    }

    private String decodeString(String string){
        if(string==null){
            return "";
        }
        String result = string;
        int i=0;
        while(result.contains("%")){
            try {
                result = URLDecoder.decode(result,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e);
            }
            i++;
            if(i>10){
                break;
            }
        }
        return result;
    }
    public String mediaPropPrepare(){
        long clipNo = 1;
        if(contentFiles!=null){
            for(ContentFile contentFile:contentFiles){
                if(contentFile!=null){
                    contentFile.setName(decodeString(contentFile.getName()));
                    contentFile.setPath(decodeString(contentFile.getPath()));
                    contentFile.setSeq(clipNo);
                    clipNo++;
                    contentFile.setOrigFileName(decodeString(contentFile.getOrigFileName()));
                }
            }
        }
        List<ContentFile> uploadFiles =(List<ContentFile> ) session.get("uploadFiles");
        if(uploadFiles!=null){
            for(ContentFile f:uploadFiles){
                f.setSeq(clipNo);
                clipNo++;
                contentFiles.add(f);
            }
        }
        session.put("media",obj);
        session.put("contentFiles", contentFiles);
        setSuccess(true);
        return "success";
    }

    private List<ContentFile> contentFiles=new ArrayList<ContentFile>();

    private Content setDefaultValues(Content obj){
        BeanUtils.setDefaultValue(obj,"status",ContentLogicInterface.STATUS_CP_ONLINE);
        BeanUtils.setDefaultValue(obj,"createTime",new Date());
        BeanUtils.setDefaultValue(obj,"validStartTime",new Date());
        BeanUtils.setDefaultValue(obj,"validEndTime",new Date(System.currentTimeMillis()+10*365*24*3600*1000L));
        BeanUtils.setDefaultValue(obj, "cspId", admin.getCspId().longValue());
        BeanUtils.setDefaultValue(obj, "contentAuditId", 1L);
        BeanUtils.setDefaultValue(obj, "creatorAdminId", admin.getId().longValue());
        BeanUtils.setDefaultValue(obj, "moduleId", 10000L);
        BeanUtils.setDefaultValue(obj,"allVisitCount",0L);
        BeanUtils.setDefaultValue(obj,"monthVisitCount",0L);
        BeanUtils.setDefaultValue(obj,"weekVisitCount",0L);
        return obj;
    }
    public void saveFiles(){
        if(filesFileName!=null){
            HttpServletRequest request = ServletActionContext.getRequest();
            String saveToFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
            int i=0,l=filesFileName.size();
            //һ������£�files��filesFileName����������ĳ�����һ���ģ������һ���������������
            for(;i<l;i++){
                File file = null;
                try {
                    file = files.get(i);
                } catch (Exception e) {
                    log.warn("�ϴ��ļ�Ϊ�գ�����i="+i);
                }
                String fileName = filesFileName.get(i);
                if((file!=null&&file.length()>0)||(fileName!=null&&!"".equals(fileName.trim()))){//������ϴ������н�ͼ���ݣ���ͼ���ļ���������fileName��
                    String data = fileProperties.get(i);
                    if(data==null){
                        continue;
                    }
                    String[] propertyIdAndCPIdx= data.split(",");
                    Long propertyId = StringUtils.string2long(propertyIdAndCPIdx[0],-1);
                    if(propertyId==-1){
                        continue;
                    }
                    String urlName;
                    if(file!=null&&file.length()>0){//������ϴ����ļ�
                        log.debug("�ϴ��������ˣ�"+fileName);
                        if(fileName==null){
                            fileName = "";
                        }
                        fileName = System.currentTimeMillis()+fileName;
                        try {
                            fileName = URLEncoder.encode(fileName.replace(' ','_'),"UTF-8").replace("%","_");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        FileUtils.copy(file,request.getRealPath(saveToFilePath),fileName);
                        urlName = saveToFilePath+fileName;
                    }else{
                        log.debug("��ͼ�������ˣ�"+fileName);
                        urlName = fileName;
                    }
                    Property property = propertyLogicInterface.get(propertyId);
                    if(property!=null){
                        Byte isMain = property.getIsMain();
                        if(isMain!=null&&isMain.intValue()==1){
                            //Ҫ�ŵ������У�Ҳ����obj��
                            String fieldName = property.getColumnName();
                            if(fieldName!=null){
                                BeanUtils.setProperty(obj,fieldName,urlName);
                                log.debug("�ļ�����Ϊ��"+urlName+",ͬʱ���õ�content��"+fieldName+"�У�");
                            }
                        }else{
                            //Ҫ�ŵ�contentProperty��
                            int idx = -1;
                            if(propertyIdAndCPIdx.length>1){
                                idx = StringUtils.string2int(propertyIdAndCPIdx[1],idx);
                            }
                            if(idx>=0&&idx<contentProperties.size()&&contentProperties.get(idx)!=null){
                                ContentProperty cp = contentProperties.get(idx);
                                cp.setStringValue(urlName);
                                log.debug("�ļ�����Ϊ��"+urlName+",���浽һ�����е�contentProperty��id=" +cp.getId()+
                                        "��,��Ӧ������:"+property.getName()+","+property.getCode());
                            }else{
                                ContentProperty cp = new ContentProperty();
                                cp.setPropertyId(propertyId);
                                cp.setStringValue(urlName);
                                contentProperties.add(cp);
                                log.debug("�ļ�����Ϊ��"+urlName+",���½���һ��contentProperty��Ӧ������:"+property.getName()+","+property.getCode());
                            }
                        }
                    }else{
                        log.error("û���ҵ����ԣ�"+propertyId);
                    }
                }else{
                    log.warn("�ļ�û���ϴ�");
                }
            }
        }
        /**
         * ����һ�߿ͻ��˴��������ݣ������ͼƬ����û�����룬�ʹ��б���ȥ��
         */
        for(int i=contentProperties.size()-1;i>=0;i--){
            ContentProperty cp=contentProperties.get(i);
            if(cp==null){
                contentProperties.remove(i);
                continue;
            }
            Long propertyId = cp.getPropertyId();
            if(propertyId!=null&&propertyId>0){
                Property property = propertyLogicInterface.get(propertyId);
                if(PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(property.getDataType())){
                    String stringValue = cp.getStringValue();
                    if(stringValue==null||"".equals(stringValue)){
                        contentProperties.remove(cp);
                    }
                }
            }
        }
    }
    // redexProʹ�ã�����ý�������Ϣ
    @SuppressWarnings("unchecked")
    public String saveMediaBaseInfo(){
        // ���溣���ʹ󺣱�
        saveFiles();
/*
//        String posterPath = savePoster(false);
//        String bigPosterPath = savePoster(true);
        if(obj != null){
            obj.setPost1Url(posterPath);
            if( !"".equals(bigPosterPath) ){
                 obj.setPost2Url(bigPosterPath);
            }
        }
*/
        session.put("media", obj);
        session.put("contentProperties",contentProperties);
        return "mediaBaseInfo";
    }

    private String savePoster(boolean isBig){
        if(isBig && (movieBigPoster == null)) return "";

        HttpServletRequest request = ServletActionContext.getRequest();
        String postfix = FileUtils.getFileExtName( isBig ? movieBigPosterFileName :moviePosterFileName );
        String saveToFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
        String fullFileName;
        File posterFile;

        do{
            fullFileName = String.format("%s%s.%s", saveToFilePath, RandomStringUtils.randomAlphanumeric(8), postfix);
            posterFile = new File(request.getRealPath(fullFileName));
        }while(posterFile.exists());

        boolean result = (FileUtils.copy( isBig? movieBigPoster : moviePoster, posterFile.getParentFile().getAbsolutePath(), posterFile.getName()) != null);

        return result? (fullFileName) : "";
    }

    public List<ContentFile> getContentFiles() {
        return contentFiles;
    }

    public void setContentFiles(List<ContentFile> contentFiles) {
        this.contentFiles = contentFiles;
    }
    @SuppressWarnings("unchecked")
    @Action(value = "/saveMedia",results = {
            @Result(name = "success",location = "/common/jsonMessages.jsp")
    },interceptorRefs = {@InterceptorRef(value = "defaultStack")})
    public String setMediaChannel(){
        Content content = (Content)session.get("media");

        if(obj != null && content != null) {
            content = setDefaultValues(content);
            String userTypes = obj.getUserTypes();
            if( userTypes != null ){
                if(!userTypes.endsWith(",")){
                    userTypes += ",";
                }
                if(!userTypes.startsWith(",")){
                    userTypes=","+userTypes;
                }
            }
            content.setUserTypes( userTypes );
            content.setStatus(ContentLogicInterface.STATUS_WAITING);
            // ���÷�����
            Admin admin = (Admin) session.get(Constants.SESSION_ADMIN);
            content.setCreatorAdminId((admin == null)? -1L: admin.getId());

            // ��Session��ȡ��ý���ļ���Ϣ,���浽ContentProperty��
            List<ContentProperty> contentProperties =(List<ContentProperty>) session.get("contentProperties");
            if(contentProperties==null){
                contentProperties = new ArrayList<ContentProperty>();
            }
            List<ContentFile> fileList = (List)session.get("contentFiles");
            long index = 1;
            for(ContentFile cf : fileList){
                ContentProperty p = new ContentProperty();
                p.setName(cf.getName());
                p.setIntValue(index++);
                p.setStringValue(cf.getPath());
                p.setThumbPic(cf.getResolutionWidth() + "x" + cf.getResolutionHeight());
                p.setLength(cf.getDuration());
                p.setPropertyId(1L);    // must be 1
                contentProperties.add(p);
            }


            // ����ý����Ϣ
            obj = contentLogicInterface.save(content);
            //���Ƶ��������Ϣ
            contentLogicInterface.checkPublishChannels(obj,channelIds,true);
            // �����ļ���Ϣ
            contentPropertyLogicInterface.saveClips(contentProperties, obj, -1);

            writeSysLog("��������ӰƬ��" + obj.getName());
            session.remove("contentFiles");
            session.remove("contentProperties");
            setSuccess(true);
        }else{
            setSuccess(false);
            addActionError("��Ƶ��ϢΪ��!");
        }

        return ActionSupport.SUCCESS;
    }
    /**
     * @return "modified"
     * �޸���Ƶý����Ϣ������������Ϣ����������Ŀ���û�����
     */
    @Action(value = "/modifyContent",results = {
            @Result(name = "modified",location = "/pub/toList.jsp")
    },interceptorRefs = {@InterceptorRef(value = "defaultStack")})
    public String modifyContent(){
        log.debug("conent id:" + obj.getId());
        session.put("redirect_index", nextPageFlag);
        saveFiles();
        // ����id��ȡ����
        Content content = contentLogicInterface.get(obj.getId());
        if(content != null){
            BeanUtils.copyPropertiesIfDesertIsNull(content,obj);
            String userTypes = obj.getUserTypes();
            if( userTypes != null && !userTypes.endsWith(",")){
                userTypes += ",";
            }
            if( userTypes != null && !userTypes.startsWith(",")){
                userTypes = "," + userTypes;
            }
            obj.setUserTypes( userTypes );

            // ���ԭ������˱��ܾ��������ύ���
            if( ContentLogicInterface.STATUS_AUDIT_REJECTED.equals(content.getStatus()) ){
                obj.setStatus( ContentLogicInterface.STATUS_WAITING_FOR_AUDIT);
            }

            // ����ý����Ϣ
            content = contentLogicInterface.save(obj);
            writeSysLog("�޸���Ƶ��" + content.getName() + "��Ϣ");
            contentPropertyLogicInterface.saveClips(contentProperties,content,-1);
            // ������Ŀ��Ϣ
            log.debug("Ƶ��������飺" + channelIds);
            contentLogicInterface.checkPublishChannels(content,channelIds);
        }

        return "modified";
    }

    public String test(){
        return ActionSupport.SUCCESS;
    }
    private int nextPageFlag=2;
    private String fileName;
    private String xmlEncoding;
    private String channelIds;
    private Long moduleId;
    private Long moduleChannelId;
    private String importXmlCommand;
    private List<ContentProperty> contentProperties=new ArrayList<ContentProperty>();
    private List<File> files;
    private List<String> fileProperties;
    // /����ϴ��ļ������ͼ���
    private List<String> filesContentType;
    // ����ϴ��ļ����ļ�������
    private List<String> filesFileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getXmlEncoding() {
        return xmlEncoding;
    }

    public void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getImportXmlCommand() {
        return importXmlCommand;
    }

    public void setImportXmlCommand(String importXmlCommand) {
        this.importXmlCommand = importXmlCommand;
    }

    public List<ContentProperty> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(List<ContentProperty> contentProperties) {
        this.contentProperties = contentProperties;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<String> getFileProperties() {
        return fileProperties;
    }

    public void setFileProperties(List<String> fileProperties) {
        this.fileProperties = fileProperties;
    }

    public List<String> getFilesContentType() {
        return filesContentType;
    }

    public void setFilesContentType(List<String> filesContentType) {
        this.filesContentType = filesContentType;
    }

    public List<String> getFilesFileName() {
        return filesFileName;
    }

    public void setFilesFileName(List<String> filesFileName) {
        this.filesFileName = filesFileName;
    }

    public Long getModuleChannelId() {
        return moduleChannelId;
    }

    public void setModuleChannelId(Long moduleChannelId) {
        this.moduleChannelId = moduleChannelId;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public int getNextPageFlag() {
        return nextPageFlag;
    }

    public void setNextPageFlag(int nextPageFlag) {
        this.nextPageFlag = nextPageFlag;
    }
}

