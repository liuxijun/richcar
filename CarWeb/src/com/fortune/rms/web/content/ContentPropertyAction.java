package com.fortune.rms.web.content;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentCsp;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.syn.logic.logicImpl.SynFileLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.util.*;
import com.fortune.util.config.Config;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentProperty")
public class ContentPropertyAction extends BaseAction<ContentProperty> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private TemplateLogicInterface templateLogicInterface;
    private SynTaskLogicInterface synTaskLogicInterface;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private Long moduleId = 5000L;
    private SynFileLogicInterface synFileLogicInterface;

    @SuppressWarnings("unchecked")
    public ContentPropertyAction() {
        super(ContentProperty.class);
    }

    /**
     * @param contentPropertyLogicInterface the contentPropertyLogicInterface to set
     */
    @Autowired
    public void setContentPropertyLogicInterface(
            ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
        setBaseLogicInterface(contentPropertyLogicInterface);
    }

    @Autowired
    public void setTemplateLogicInterface(TemplateLogicInterface templateLogicInterface) {
        this.templateLogicInterface = templateLogicInterface;
    }

    @Autowired
    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    @Autowired
    public void setSynFileLogicInterface(SynFileLogicInterface synFileLogicInterface) {
        this.synFileLogicInterface = synFileLogicInterface;
    }

    @Autowired
    public void setContentRecommendLogicInterface(ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
    }

    @Autowired
    public void setSynTaskLogicInterface(SynTaskLogicInterface synTaskLogicInterface) {
        this.synTaskLogicInterface = synTaskLogicInterface;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    @Autowired
    public void setContentLogicInterface(
            ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    private File uploadFile;
    private File uploadZipFile;
    private String localFileName;
    private int saveToDatabase = 0;
    private String oriFilePath;
    private String uploadZipFileNames;//上传压缩包的地址

    public String getUploadZipFileNames() {
        return uploadZipFileNames;
    }

    public File getUploadZipFile() {
        return uploadZipFile;
    }

    public void setUploadZipFile(File uploadZipFile) {
        this.uploadZipFile = uploadZipFile;
    }

    public void setUploadZipFileNames(String uploadZipFileNames) {
        this.uploadZipFileNames = uploadZipFileNames;
    }

    public String saveToZipImageFile = "";
    public String saveToZipHtmlFile = "";

    private boolean savePoster() {
        HttpServletRequest request = ServletActionContext.getRequest();
        if (localFileName == null || "".equals(localFileName)) {
            localFileName = request.getParameter("localFileName");
        }
        int i=0;
        while(localFileName.contains("%")&&i<5){
            try {
                i++;
                localFileName = URLDecoder.decode(localFileName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        log.debug("localFileName=" +localFileName+
                ",request.QueryString=" + request.getQueryString());
        String postfix = FileUtils.getFileExtName(localFileName);
        String saveToFileName = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" +
                HzUtils.getFullSpell(FileUtils.extractFileName(localFileName, "/")).replace(".", "_") + new Date().getTime() % 10000 + "_" + Math.round(Math.random() * 1000000) + "." + postfix;
        String tempStr = "";
        for(char ch:saveToFileName.toCharArray()){
            //过滤，只保留'a-z','A-Z','0-9','/','.',其他的统统过滤掉
            if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='/'||ch=='.'||ch=='_'||ch=='-'){
                tempStr+=ch;
            }else{
                tempStr+="_";
            }
        }
        saveToFileName=tempStr;
        saveToZipImageFile = saveToFileName;
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
        obj.setLength(saveToFile.length());
        obj.setExtraData(fileInfo.getWidth() + "x" + fileInfo.getHeight());
        obj.setStringValue(saveToFileName);
        return result;
    }

    //同步zip影片信息
    public boolean zipUpload() {
        boolean isShow = true;
        //根据用户的Id得到zip保存的位置
        String urlFileName = "/";
        Map<String, Object> session = (Map<String, Object>) ActionContext.getContext().getSession();
        String alias = "";
        long cspId = -1;
        String zipFileName = "";
        int isRoot = 0;
        long synLevel = 0l;
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                cspId = admin.getCspId();
                isRoot = admin.getIsRoot();
            }
        }
        if (cspId > 0) {
            synLevel = 1;
            alias = this.templateLogicInterface.getCspAliasByCspId(cspId);
            if (alias != null) {
                if (!"wasu".equals(alias)&&!"bestv".equals(alias)&&!"voole".equals(alias)) {
                    isShow = false;
                }
                urlFileName += alias; //文件上传保存的位置
            }
            //记录文件保存的目录位置
            zipFileName = urlFileName;
        } else {
            setSuccess(false);
        }
        ServletContext context = ServletActionContext.getServletContext();
        String targetFileName = "";
        if (uploadZipFile != null) {
            String targetDirectory = "d:/temp/";
            if (context != null) {
                targetDirectory = context.getRealPath(urlFileName);
            }


            File aFile = new File(uploadZipFileNames);
            targetFileName = aFile.getName();


            urlFileName += "/" + targetFileName;
            try {
                org.apache.commons.io.FileUtils.forceMkdir(new File(targetDirectory));
                File target = new File(targetDirectory, targetFileName);
                org.apache.commons.io.FileUtils.copyFile(uploadZipFile, target);
                String fileType = targetFileName.split("\\.")[1];
                if (fileType.equals("zip")) {
                    ZipUtils.unZip(target.toString(), targetDirectory + "\\");
                    //得到html 路径
                    ZipFile zipFile = new ZipFile(target.toString(), "GBK"); //以“GBK”编码创建zip文件，用来处理winRAR压缩的文件。
                    Enumeration emu = zipFile.getEntries();
                    while (emu.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) emu.nextElement();
                        String fileZipName = entry.getName();
                        String[] fileZipTypes = fileZipName.split("\\.");
                        if (fileZipTypes.length >= 2) {
                            //如果类型是html  那就是首页推荐的页面地址
                            String fileZipType = fileZipName.split("\\.")[1];
                            if (fileZipType.equals("html")) {
                                Config config = new Config();
                                //直接连接这个地址就可找到对于的html
                                String masterAddress = config.getStrValue("master.address", "http://tv.inhe.net");
                                saveToZipHtmlFile = masterAddress + zipFileName + "/" + fileZipName;
                                break;
                            }
                        }
                    }
                }
            } catch (IOException ioe) {
                log.error("发生异常" + ioe.getMessage());
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String md5;
        SynFile synFile = new SynFile();
        try {
            md5 = MD5Utils.getFileMD5String(uploadZipFile);
            synFile.setMd5(md5);
        } catch (IOException ioe) {
            log.error("发生异常" + ioe.getMessage());
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            log.error("发生异常" + e.getMessage());
            e.printStackTrace();
        }
        synFile.setSpId(cspId);
        synFile.setName(targetFileName);
        synFile.setType(SynFileLogicImpl.SYNFILE_ADD);
        synFile.setStartTime(new Date());
        synFile.setUrl(urlFileName);
        synFile = synFileLogicInterface.save(synFile);
        synTaskLogicInterface.addSynTask(synFile.getId(), synLevel);
        writeSysLog("保存上传文件： " + synFile.getName() + ",cspId=" + cspId);
        return isShow;
    }

    //同步zip, 海报，和保存影片信息
    public String saveZip() {
        String resourceKey = "contentId=" + saveToZipHtmlFile;
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        try {
            threadUtils.acquire(resourceKey);
            setSuccess(savePoster());
            //上传zip 文件（文件初始地址）
            boolean isShow = zipUpload();
            if (saveToDatabase == 1) {
                try {
                    long cspId = -1;
                    cspId = admin.getCspId();
                    String contentName = getRequestParam("name", "");
                    String mainFocus = getRequestParam("mainFocus", "");
                    String localFileName = obj.getStringValue();
                    Content c = new Content();
                    c.setName(contentName);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String nowTime = df.format(new Date());// new Date()为获取当前系统时间
                    Date d = df.parse(nowTime);
                    c.setCreateTime(d);
                    c.setCspId(cspId);
                    //前台判断，在前台的轮转显示（点击的时候是连接不是播放）。
                    c.setIsSpecial(1L);
                    //状态是2 的才可以被前台搜索到
                    if (isShow) {
                        c.setStatus(2l);
                    } else {
                        c.setStatus(1L);
                    }
                    c.setPost1Url(saveToZipHtmlFile);
                    c.setPost2Url(saveToZipImageFile);
                    c.setModuleId(5000L);
                    Content content = contentLogicInterface.save(c);
                    //写入Content_Property表中。
                    ContentProperty cp = new ContentProperty();
                    cp.setPropertyId(5095L);
                    cp.setContentId(content.getId());
                    cp.setStringValue(localFileName);
                    contentPropertyLogicInterface.save(cp);
                    cp.setPropertyId(5086L);
                    cp.setContentId(content.getId());
                    cp.setStringValue(mainFocus);
                    contentPropertyLogicInterface.save(cp);
                    //添加到csp关系表。contentCsp里面的status必须为2，前台才能查到。
                    ContentCsp contentCsp = new ContentCsp();
                    contentCsp.setContentId(content.getId());
                    if (isShow) {
                        contentCsp.setStatus(2L);
                    } else {
                        contentCsp.setStatus(1L);
                    }
                    contentCsp.setCspId(cspId);
                    contentCspLogicInterface.save(contentCsp);
                    return Constants.ACTION_SAVE;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        return "false";
    }

    public String updatePoster() {
        setSuccess(savePoster());
        return "success";
    }

    public String uploadPoster() {
        if (obj.getId() > 0 && saveToDatabase == 1) {
            try {
                ContentProperty tempObj = contentPropertyLogicInterface.get(obj.getId());
                if (tempObj != null) {
                    if (null != obj.getName()) {
                        tempObj.setName(obj.getName());
                    }
                    obj = tempObj;
                }
            } catch (Exception e) {
                log.error("提供了id，但却没有找到contentProperty：id=" + obj.getId());
            }
        }
        setSuccess(savePoster());
        if (obj.getId() > 0 || saveToDatabase == 1) {
            obj = contentPropertyLogicInterface.save(obj);
        }
        writeSysLog("上传海报：" + obj.getStringValue() + "," + obj.getExtraData());
        return "success";
    }

    public String addNewContentRecommend() {
        setSuccess(savePoster());
        if (saveToDatabase == 1) {
            try {
                long cspId = -1;
                cspId = admin.getCspId();
                String contentName = getRequestParam("name", "");
                String url = getRequestParam("url", "");
                String mainFocus = getRequestParam("mainFocus", "");
                String localFileName = obj.getStringValue();
                //long recommendId = getRequestIntParam("recommendId", -1);
                Content c = new Content();
                c.setName(contentName);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String nowTime = df.format(new Date());// new Date()为获取当前系统时间
                Date d = df.parse(nowTime);
                c.setCreateTime(d);
                c.setCspId(cspId);
                //前台判断，在前台的轮转显示（点击的时候是连接不是播放）。
                c.setIsSpecial(2L);
                c.setPost1Url(url);
                c.setPost2Url(localFileName);
                c.setModuleId(5000L);
               /* contentLogicInterface.save(c);
                List<Content> list = HibernateUtils.findAll(this.baseLogicInterface.getSession(), "from Content where name = '" + contentName + "'");
                if (list.size() > 0) {*/
                    /*Content content = list.get(0); */
                    Content content = contentLogicInterface.save(c);
                    ContentProperty cp = new ContentProperty();
                    cp.setPropertyId(5095L);
                    cp.setContentId(content.getId());
                    cp.setStringValue(localFileName);
                    contentPropertyLogicInterface.save(cp);
                    cp.setPropertyId(5086L);
                    cp.setContentId(content.getId());
                    cp.setStringValue(mainFocus);
                    contentPropertyLogicInterface.save(cp);
                    //添加到csp关系表。contentCsp里面的status必须为2，前台才能查到。
                    ContentCsp contentCsp = new ContentCsp();
                    contentCsp.setContentId(content.getId());
                    contentCsp.setStatus(1L);
                    contentCsp.setCspId(cspId);
                    contentCspLogicInterface.save(contentCsp);
                  /*  ContentRecommend cr = new ContentRecommend();
                    cr.setContentId(content.getId());
                    cr.setRecommendId(recommendId);
                    //新增的顺序都为0
                    cr.setDisplayOrder(0L);
                    contentRecommendLogicInterface.save(cr);*/
                    return Constants.ACTION_SAVE;
              /*  }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "false";
    }

    public String getOriFilePath() {
        return oriFilePath;
    }

    public void setOriFilePath(String oriFilePath) {
        this.oriFilePath = oriFilePath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFiles(File uploadFiles) {
        this.uploadFile = uploadFiles;
    }

    private Byte dataType;
    private List<ContentProperty> contentProperties;
    private Content content;
    private String handleMethod; //处理方式
    private String handlePicFile; //处理海报路径

    public String getHandleMethod() {
        return handleMethod;
    }

    public void setHandleMethod(String handleMethod) {
        this.handleMethod = handleMethod;
    }

    public String getHandlePicFile() {
        return handlePicFile;
    }

    public void setHandlePicFile(String handlePicFile) {
        this.handlePicFile = handlePicFile;
    }

    private boolean fillEmptyData=true ;
    public String list() {
        objs = contentPropertyLogicInterface.getContentPropertiesByDataType(moduleId, obj.getContentId(), dataType, fillEmptyData);
        pageBean.setRowCount(objs.size());
        return "list";
    }

    public Byte getDataType() {
        return dataType;
    }

    public void setDataType(Byte dataType) {
        this.dataType = dataType;
    }

    public List<ContentProperty> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(List<ContentProperty> contentProperties) {
        this.contentProperties = contentProperties;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public Map picStore;

    public Map getPicStore() {
        return picStore;
    }

    public void setPicStore(Map picStore) {
        this.picStore = picStore;
    }

    public String saveAll() {
        log.debug("saveAll");
        log.debug(content);
        //先判断是否有海报处理
        if ("auto_handle".equals(handleMethod)) {
            //根据handlePicFile 处理缺少海报 obj.getContentId()海报模版
            contentProperties = contentPropertyLogicInterface.fillEmptyPoster(handlePicFile, content.getModuleId(), contentProperties, admin.getCspId());
        }
        Content ct = new Content();
        ct.setId(obj.getContentId());
        ct.setDeviceId(content.getDeviceId());
        ct.setCspId(Long.parseLong(admin.getCspId().toString()));
        ct.setModuleId(content.getModuleId());
        ct.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);
        ct.setCreatorAdminId(admin.getId().longValue());
        ct.setCreateTime(content.getCreateTime());
        ct.setWeekVisitCount(0L);
        ct.setMonthVisitCount(0L);
        ct.setAllVisitCount(0L);
        ct.setIsSpecial(0L);
        if (ct.getValidEndTime() == null || ct.getValidEndTime().before(ct.getCreateTime())) {
            ct.setValidEndTime(new Date(System.currentTimeMillis() + 3650 * 24 * 3600 * 1000L));
        }
        content = contentPropertyLogicInterface.saveClips(contentProperties, ct, -1L);
        writeSysLog("编辑后保存媒体详细信息：" + content.getName() + ",id=" + content.getId());
        return "success";

    }

    public int getSaveToDatabase() {
        return saveToDatabase;
    }

    public void setSaveToDatabase(int saveToDatabase) {
        this.saveToDatabase = saveToDatabase;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isFillEmptyData() {
        return fillEmptyData;
    }

    public void setFillEmptyData(boolean fillEmptyData) {
        this.fillEmptyData = fillEmptyData;
    }


    //用于延安下载
    public String getContentDownLoadUrl() {
        String url = "";
        String fujian ="";
        String result = "";
        Content c = contentLogicInterface.get(obj.getContentId());
        if(c != null) {
            Device device = deviceLogicInterface.get(c.getDeviceId());
            if(device != null){
                ContentProperty contentProperty = contentPropertyLogicInterface.getContentDownLoadUrl(obj.getContentId(),obj.getIntValue(),obj.getPropertyId());
                //movie是tomcat配置的虚拟目录
                if (contentProperty !=null){
                     url = device.getUrl()+ contentProperty.getStringValue();
                     if(contentProperty.getStringValue() != null){
                           fujian ="yes";
                     }else{
                         fujian="no";
                     }
            }
        }
            result = "{\"success\":\"true\",\"url\":\""+url+"\",\"stringValue\":\""+fujian+"\"}";
        }

        directOut(result);
        return  null;
    }


}
