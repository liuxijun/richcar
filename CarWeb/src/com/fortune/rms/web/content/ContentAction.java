package com.fortune.rms.web.content;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.logic.logicInterface.*;
import com.fortune.rms.business.content.model.*;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyContentLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import com.fortune.rms.business.syn.logic.logicImpl.SynFileLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.timer.DelayRunner;

import com.fortune.util.*;
import com.fortune.util.config.Config;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.valueOf;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "content")

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ContentAction extends BaseAction<Content> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentLogicInterface contentLogicInterface;
    private PropertyLogicInterface propertyLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private ContentChannelLogicInterface contentChannelLogicInterface;
    private ContentServiceProductLogicInterface contentServiceProductLogicInterface;
    private ContentAuditLogicInterface contentAuditLogicInterface;
    private ContentRelatedLogicInterface contentRelatedLogicInterface;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private RelatedPropertyContentLogicInterface relatedPropertyContentLogicInterface;
    private EncoderTemplateLogicInterface encoderTemplateLogicInterface;
    private RecommendLogicInterface  recommendLogicInterface;
    private SynFileLogicInterface synFileLogicInterface;
    private SynTaskLogicInterface synTaskLogicInterface;

    public void setEncoderTemplateLogicInterface(EncoderTemplateLogicInterface encoderTemplateLogicInterface) {
        this.encoderTemplateLogicInterface = encoderTemplateLogicInterface;
    }

    // added by mlwang @2014-10-22
    private File moviePoster;
    private String moviePosterFileName;
    private File movieBigPoster;
    private String movieBigPosterFileName;
    private int destPage;   // modifyContentִ�����Ҫת����ҳ������ 1 pubList.jsp 2 contentMan.jsp

    public int getDestPage() {
        return destPage;
    }

    public void setDestPage(int destPage) {
        this.destPage = destPage;
    }
    // end of add

    public File getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(File moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMoviePosterFileName() {
        return moviePosterFileName;
    }

    public void setMoviePosterFileName(String moviePosterFileName) {
        this.moviePosterFileName = moviePosterFileName;
    }

    public File getMovieBigPoster() {
        return movieBigPoster;
    }

    public void setMovieBigPoster(File movieBigPoster) {
        this.movieBigPoster = movieBigPoster;
    }

    public String getMovieBigPosterFileName() {
        return movieBigPosterFileName;
    }

    public void setMovieBigPosterFileName(String movieBigPosterFileName) {
        this.movieBigPosterFileName = movieBigPosterFileName;
    }

    public ChannelLogicInterface getChannelLogicInterface() {
        return channelLogicInterface;
    }

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public PropertyLogicInterface getPropertyLogicInterface() {
        return propertyLogicInterface;
    }

    public void setPropertyLogicInterface(PropertyLogicInterface propertyLogicInterface) {
        this.propertyLogicInterface = propertyLogicInterface;
    }

    private ChannelLogicInterface channelLogicInterface;
    //private PropertySelectLogicInterface propertySelectLogicInterface;
    private Csp csp;
    private Content content;

    public void setContent(Content content) {
        this.content = content;
    }

    public ContentAction() {
        super(Content.class);
    }

    public void setContentLogicInterface(
            ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
        setBaseLogicInterface(contentLogicInterface);
    }

    public void setRecommendLogicInterface(RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
    }

    public void setSynFileLogicInterface(SynFileLogicInterface synFileLogicInterface) {
        this.synFileLogicInterface = synFileLogicInterface;
    }

    public void setSynTaskLogicInterface(SynTaskLogicInterface synTaskLogicInterface) {
        this.synTaskLogicInterface = synTaskLogicInterface;
    }

/*
    @Autowired
    public void setPropertySelectLogicInterface(PropertySelectLogicInterface propertySelectLogicInterface) {
        this.propertySelectLogicInterface = propertySelectLogicInterface;
    }
*/

    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }

    public void setContentServiceProductLogicInterface(ContentServiceProductLogicInterface contentServiceProductLogicInterface) {
        this.contentServiceProductLogicInterface = contentServiceProductLogicInterface;
    }

    public void setContentAuditLogicInterface(ContentAuditLogicInterface contentAuditLogicInterface) {
        this.contentAuditLogicInterface = contentAuditLogicInterface;
    }

    public void setContentRelatedLogicInterface(ContentRelatedLogicInterface contentRelatedLogicInterface) {
        this.contentRelatedLogicInterface = contentRelatedLogicInterface;
    }

    public void setContentRecommendLogicInterface(ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
    }

    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public void setRelatedPropertyContentLogicInterface(RelatedPropertyContentLogicInterface relatedPropertyContentLogicInterface) {
        this.relatedPropertyContentLogicInterface = relatedPropertyContentLogicInterface;
    }

    public Csp getCsp() {
        return csp;
    }

    public void setCsp(Csp csp) {
        this.csp = csp;
    }

    private File uploadFile;
    private String uploadFileLocal;
    private String fileAddress;
    private Long deviceId;
    private Long moduleId;
    private String xmlEncoding;
    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileLocal() {
        return uploadFileLocal;
    }

    public void setUploadFileLocal(String uploadFileLocal) {
        this.uploadFileLocal = uploadFileLocal;
    }

    public String uploadFile() {
        String urlFileName = "";
        if (uploadFile != null) {
            String targetDirectory = "d:/temp/";
            urlFileName = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd");
            ServletContext context = ServletActionContext.getServletContext();
            if (context != null) {
                targetDirectory = context.getRealPath(urlFileName);
            }
            File aFile = new File(uploadFileLocal);
            //String targetFileName =  StringUtils.date2string(new Date(), "HHmmss") + "_" + aFile.getName()  ;
            String targetFileName = aFile.getName();
            targetFileName = HzUtils.getFullSpell(targetFileName.substring(0, targetFileName.indexOf(".", targetFileName.length() - 5)))
                    + targetFileName.substring(targetFileName.indexOf(".", targetFileName.length() - 5));
            targetFileName = StringUtils.date2string(new Date(), "HHmmss") + "_" + targetFileName;
            targetFileName = StringUtils.replace(targetFileName, " ", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ������
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ�����
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "?", "");  //ȥ���ո�
            targetFileName = StringUtils.replace(targetFileName, "��", "");  //ȥ���ո�
            urlFileName += "/" + targetFileName;
            try {
                //log.debug("Ŀ���ļ���" + targetDirectory + "\\" + targetFileName);
                org.apache.commons.io.FileUtils.forceMkdir(new File(targetDirectory));
                File target = new File(targetDirectory, targetFileName);
                org.apache.commons.io.FileUtils.copyFile(uploadFile, target);
                //log.debug("�������ӣ�" + urlFileName);
            } catch (IOException ioe) {
                log.error("�����쳣��" + ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        writeSysLog("�����ϴ��ļ� " + urlFileName);
        super.addActionMessage(urlFileName);
        return Constants.ACTION_SAVE;
    }

    public String getFtpList() {
        log.debug("in getFtpList");
        try {

            String url = getRequestParam("url", "");
            String orderBy = getRequestParam("orderBy", "Name");
            String orderDir = getRequestParam("orderBy", "asc");
            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);

            long deviceId = getRequestIntParam("deviceId", -1);
            if (deviceId > -1) {
                Device device = deviceLogicInterface.get(deviceId);

                FtpUtils ftp = new FtpUtils();
                ftp.connectServer(device.getSafeServerIp(), device.getFtpPort().intValue(), device.getFtpUser(), device.getFtpPwd(), device.getFtpPath());

                //url = new String(url.getBytes("GBK"),"iso-8859-1");//������������Ŀ¼
                SearchResult sr = ftp.getFileListEx(url, "", orderBy, orderDir, startNo, pageSize);
                ftp.closeServer();

                String result = "{ \"totalCount\":\"" + sr.getRowCount() + "\", \"objs\":[";
                for (int i = 0; i < sr.getRows().size(); i++) {
                    FTPFile file = (FTPFile) sr.getRows().get(i);
                    result += "{" +
                            "\"name\":\"" + file.getName() + "\"," +
                            "\"type\":\"" + file.getType() + "\"," +
                            "\"size\":\"" + file.getSize() + "\"," +
                            "\"date\":\"" + StringUtils.date2string(file.getTimestamp().getTime()) + "\"" +
                            "},";
                }
                result = result.substring(0, result.length() - 1);
                result += "]}";

                directOut(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getContents() {
        try {
            String keyIds = getRequestParam("keyIds", "");
            String sqlTable = "select c.id,c.name from " +
                    "com.fortune.rms.business.content.model.Content c" +
                    " where c.id in(" + keyIds + ")";

            SearchResult<Object[]> searchResult = searchObjects(sqlTable, null);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String viewOnly() {
        log.debug("in viewOnly");
        try {
            if (keyId != null) {
                Content content = contentLogicInterface.get(StringUtils.string2long(keyId,-1));
                List<ContentProperty> outputContentProperty = contentPropertyLogicInterface.getContentPropertiesByDataType(content.getModuleId(),content.getId(),null,false,true);
                JsonUtils jsonUtils = new JsonUtils();
                directOut(jsonUtils.getListJson("objs",outputContentProperty, "totalCount", outputContentProperty.size()));

            }
        } catch (Exception e) {
            log.error("BaseAction��prepareʱ����ͼ��ȡBean�����쳣��" + e.getMessage());
            e.printStackTrace();
        }
        return null;

    }

    public String updateVisitCountAndScoreStatus(){
        log.debug("in updateVisitCountAndScoreStatus");
        try{
            String logInfo = admin.getLogin()+"("+admin.getRealname()+")";
            String userOperation = "";
            String contentNames = "";
            String userOperation1 = "";
            String contentIds = getRequestParam("contentId","");
            String type = getRequestParam("type","");
            if(!"".equals(contentIds)){
                String contentIdss[] = contentIds.split(",");

            if("visitCount".equals(type)) {
                userOperation +="���е������Ԥ����";
                String weekVisitCount = getRequestParam("weekVisitCount","-1");
                String monthVisitCount = getRequestParam("monthVisitCount","-1");
                String allVisitCount = getRequestParam("allVisitCount","-1");

                    for(String contentId1:contentIdss){
                        Long contentId = StringUtils.string2long(contentId1,-1);
                        Content c = contentLogicInterface.get(contentId);
                        if(!"0".equals(weekVisitCount)){
                            c.setWeekVisitCount(StringUtils.string2long(weekVisitCount,-1));
                        }
                        if(!"0".equals(monthVisitCount)){
                            c.setMonthVisitCount(StringUtils.string2long(monthVisitCount,-1));
                        }
                        if(!"0".equals(allVisitCount)){
                            c.setAllVisitCount(StringUtils.string2long(allVisitCount,-1));
                        }
                        c.setVisitCountStatus(1L);
                        contentNames +="��"+c.getName()+"��,id="+c.getId()+"��";
                        contentLogicInterface.save(c);
                }
                userOperation1 += "���ܵ������Ϊ��"+weekVisitCount+",�µ������Ϊ��"+monthVisitCount + ",�ܵ������Ϊ��"+allVisitCount+"��";
                logInfo += userOperation + contentNames +userOperation1;
                writeSysLog(logInfo);
                return Constants.ACTION_SAVE;
            }

             if("score".equals(type)){
                 userOperation +="�������ָ�Ԥ����";
                String score = getRequestParam("score","");
                String scoreCount = getRequestParam("scoreCount","");

                for(String contentId1: contentIdss){
                    Long contentId = StringUtils.string2long(contentId1,-1);
                    Content c = contentLogicInterface.get(contentId);
                    if(!"0".equals(score)){
                        c.setScore(Double.parseDouble(score));
                    }
                    if(!"0".equals(scoreCount)){
                        c.setScoreCount(StringUtils.string2long(scoreCount,-1));
                    }
                    c.setScoreStatus(1L);
                    contentNames +="��"+c.getName()+"��,id="+c.getId()+"��";
                    contentLogicInterface.save(c);
                }
                userOperation1 += "���ָܷ�Ϊ��"+scoreCount+",ƽ���ָ�Ϊ��"+score+"��";
                 logInfo += userOperation + contentNames +userOperation1;
                 writeSysLog(logInfo);
                return Constants.ACTION_SAVE;
              }

                if("removeVisitCountStatus".equals(type)){
                    userOperation +="ȡ���㲥����Ԥ����";
                    for(String contentId1 : contentIdss){
                        Long contentId = StringUtils.string2long(contentId1,-1);
                        Content c = contentLogicInterface.get(contentId);
                        c.setVisitCountStatus(0L);
                        contentNames +="��"+c.getName()+"��,id="+c.getId()+"��";
                        contentLogicInterface.save(c);
                    }
                    userOperation1 += "���Ѹ�Ԥ��Ϊδ��Ԥ��";
                    logInfo += userOperation + contentNames +userOperation1;
                    writeSysLog(logInfo);
                    return Constants.ACTION_DELETE;
                }


                if("removeScoreStatus".equals(type)){
                    userOperation +="ȡ�����ָ�Ԥ����";
                    for(String contentId1 : contentIdss){
                        Long contentId = StringUtils.string2long(contentId1,-1);
                        Content c = contentLogicInterface.get(contentId);
                        c.setScoreStatus(0L);
                        contentNames +="��"+c.getName()+"��,id="+c.getId()+"��";
                        contentLogicInterface.save(c);
                    }
                    userOperation1 += "���Ѹ�Ԥ��Ϊδ��Ԥ��";
                    logInfo += userOperation + contentNames +userOperation1;
                    writeSysLog(logInfo);
                    return Constants.ACTION_DELETE;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String view() {
        log.debug("in view");
        try {
            if (keyId != null) {
                log.debug("׼����ȡ����,����Ϊ��" + keyId);
                Content content = getBaseObject(keyId);
                long moduleId = content.getModuleId();
                List propertyList = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from Property p where p.moduleId=" + moduleId + " order by displayOrder asc");
                HashMap<Long, Property> propertyMap = new HashMap<Long, Property>();
                for (Object aPropertyList1 : propertyList) {
                    Property p = (Property) aPropertyList1;
                    propertyMap.put(p.getId(), p);
                }

                List contentPropertyList = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentProperty cp where cp.contentId=" + content.getId() + " order by id asc");

                List<ContentProperty> outputContentProperty = new ArrayList<ContentProperty>();

                for (Object aPropertyList : propertyList) {
                    Property p = (Property) aPropertyList;
                    String str;
                    if (p.getIsMain() != null && p.getIsMain() == 1 && p.getColumnName() != null) {
                        str = BeanUtils.getProperty(content, p.getColumnName());
                        if (str != null && !"".equals(str)) {
                            if (p.getIsMerge() != null && p.getIsMerge() == 1) {
                                String ss[] = str.split(";");
                                if (ss.length > 0) {
                                    for (String s : ss) {
                                        ContentProperty cp = new ContentProperty();
                                        cp.setPropertyId(p.getId());
                                        cp.setStringValue(s);
                                        outputContentProperty.add(cp);
                                    }
                                }
                            } else {
                                ContentProperty cp = new ContentProperty();
                                cp.setPropertyId(p.getId());
                                cp.setStringValue(str);
                                outputContentProperty.add(cp);
                            }
                        }
                    }
                }

                for (Object aContentPropertyList : contentPropertyList) {
                    ContentProperty cp = (ContentProperty) aContentPropertyList;
/* todo ȥ��intvalue
                    if (cp.getStringValue()==null || "".equals(cp.getStringValue())){
                        cp.setStringValue(""+cp.getIntValue());
                    }
*/
                    String str = cp.getStringValue();
                    Property p = propertyMap.get(cp.getPropertyId());
                    if (p.getIsMerge() != null && p.getIsMerge() == 1) {
                        String ss[] = str.split(";");
                        if (ss.length > 0) {
                            for (String s : ss) {
                                ContentProperty cp1 = new ContentProperty();
                                cp1.setPropertyId(p.getId());
                                cp1.setStringValue(s);
                                cp1.setExtraData(cp.getExtraData());
                                outputContentProperty.add(cp1);
                            }
                        }
                    } else {
                        ContentProperty cp1 = new ContentProperty();
                        cp1.setPropertyId(p.getId());
                        cp1.setStringValue(str);
                        cp1.setExtraData(cp.getExtraData());
                        outputContentProperty.add(cp1);

                    }
                }

                JsonUtils jsonUtils = new JsonUtils();

                directOut(jsonUtils.getListJson("objs", outputContentProperty, "totalCount", outputContentProperty.size()));

            }
        } catch (Exception e) {
            log.error("BaseAction��prepareʱ����ͼ��ȡBean�����쳣��" + e.getMessage());
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    public String save() {
        log.debug("in save");
        try {
            HttpServletRequest request = ServletActionContext.getRequest();

            long moduleId = getRequestIntParam("moduleId", 0);

          long cpId = admin.getCspId();

            Content content = new Content();

            List propertyList = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from Property p where p.moduleId=" + moduleId + " order by displayOrder asc");

            String action = getRequestParam("action", "");

            //s �޸�ɾ��, content��Ĳ�������ֵ, ɾ��content_property�ļ�¼
            if ("view".equals(action)) {
                long keyId = getRequestIntParam("keyId", 0);
                if (keyId > 0) {
                    content = contentLogicInterface.get(keyId);
                    //�޸�ʱ, �����content������ֵ, ��content_property�ļ�¼
                    for (Object aPropertyList : propertyList) {
                        Property p = (Property) aPropertyList;
                        if (p.getIsMain() != null && p.getIsMain() == 1 && p.getColumnName() != null) {
                            BeanUtils.setProperty(content, p.getColumnName(), null);
                        }
                    }
                    contentPropertyLogicInterface.removeByContentId(keyId);
                }
                //e
            } else {
                //�½�
                content.setCreateTime(new Date());
                content.setStatus(1l);
                content.setModuleId(moduleId);
                content.setCreatorAdminId(admin.getId().longValue());
                content.setCreateTime(new Date());
                content.setCspId(cpId);
                content = contentLogicInterface.save(content);

/*             //���´����Ѿ�����contentLogicInterface.save�����д���
                List<Csp> spList = new ArrayList<Csp>();
                Csp cp = cspLogicInterface.get(cpId);
                if (cp != null && cp.getIsSp() != null && cp.getIsSp() == 1) {
                    spList.add(cp);
                }
                //���˵��ظ������ݣ��������п��ܱ���2�����ݣ�
                List moreCp = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from Csp c where c.id in (select cc.masterCspId from CspCsp cc where cc.cspId=" + cpId + ")");
                Csp csp = null;
                for (int i = 0; i < moreCp.size(); i++) {
                    csp = (Csp) moreCp.get(i);
                    if (csp != null && csp.getId() != cp.getId()) {
                        spList.add(csp);
                    }
                }
                //��Ӹ�sp�Ĺ�ϵ
                if (spList != null) {
                    for (Object aSpList : spList) {
                        Csp sp = (Csp) aSpList;
                        ContentCsp cc = new ContentCsp();
                        cc.setContentId(content.getId());
                        cc.setCspId(sp.getId());
                        cc.setStatus(0l);
                        cc.setStatusTime(new Date());
                        contentCspLogicInterface.save(cc);
                    }
                }
*/
            }

            for (Object aPropertyList : propertyList) {
                Property p = (Property) aPropertyList;

                String paramValues[] = request.getParameterValues("" + p.getId());
                Byte dataType = p.getDataType();
                if (dataType != null && (dataType == 8 || dataType == 9 || dataType == 10)) {//wmv �ļ�
                    paramValues = request.getParameterValues("" + p.getId() + "_extra");
                }

                if (paramValues != null && paramValues.length > 0) {
                    for (String str : paramValues) {
                        if (str != null && !"".equals(str)) {
                            if (p.getIsMain() != null && p.getIsMain() == 1 && p.getColumnName() != null) {
                                if (p.getIsMerge() != null && p.getIsMerge() == 1 && BeanUtils.getProperty(content, p.getColumnName()) != null) {
                                    str = BeanUtils.getProperty(content, p.getColumnName()) + ";" + str;
                                }
                                BeanUtils.copyProperty(content, p.getColumnName(), str);
                            } else {
                                ContentProperty cp = new ContentProperty();
                                cp.setContentId(content.getId());
                                cp.setPropertyId(p.getId());
                                switch (p.getDataType()) {
/* todo ȥ��intvalue
                                    case 3:
                                    case 5:
                                    case 6:
                                    case 7:cp.setIntValue(Long.parseLong(str));
                                        break;
*/
                                    case 8: //wmv
                                    case 9:
                                    case 10:
                                        String str1 = str.substring(0, str.indexOf("###"));
                                        String str2 = str.substring(str.indexOf("###") + 3, str.length());
                                        int po = str2.indexOf("###");
                                        if (po > 0) {
                                            String intValue = str2.substring(0, po);
                                            cp.setIntValue(StringUtils.string2long(intValue, 0));
                                        }
                                        cp.setStringValue(str1);
                                        cp.setExtraData(str2);
                                        break;
                                    case 11://photo
                                        cp.setStringValue(str);

                                        String photoCopy = Config.getStrValue("web.photo.copy", "true");
                                        if ("true".equals(photoCopy)) {
                                            ServletContext context = ServletActionContext.getServletContext();
                                            if (context != null) {
                                                String rootPath = context.getRealPath("/");
                                                SynFile synFile = new SynFile();
                                                File aFile;
                                                String md5;
                                                try {
                                                    aFile = new File(rootPath + str);
                                                    if (aFile.exists()) {
                                                        md5 = MD5Utils.getFileMD5String(aFile);
                                                        synFile.setMd5(md5);
                                                        String targetFileName = aFile.getName();
                                                        synFile.setName(targetFileName);
                                                        synFile.setType(SynFileLogicImpl.SYNFILE_ADD);
                                                        synFile.setStartTime(new Date());
                                                        synFile.setUrl(str);
                                                        synFile = synFileLogicInterface.save(synFile);
                                                        synTaskLogicInterface.addSynTask(synFile.getId(), 0);
                                                    } else {
                                                        log.error("�ļ�������");
                                                    }

                                                } catch (IOException ioe) {
                                                    log.error("�����쳣" + ioe.getMessage());
                                                    ioe.printStackTrace();
                                                } catch (NoSuchAlgorithmException e) {
                                                    log.error("�����쳣" + e.getMessage());
                                                    e.printStackTrace();
                                                }
//                                              WebServer.putFile(rootPath + str, str);
                                            }
                                        }

                                        break;
                                    default:
                                        cp.setStringValue(str);
                                        break;
                                }
                                contentPropertyLogicInterface.save(cp);
                            }
                        }
                    }
                }
            }

            contentLogicInterface.save(content);

            obj = content;
            writeSysLog("����" + guessName(obj));
            super.addActionMessage("�ɹ��������ݣ�");
        } catch (Exception e) {
            super.addActionError("�������ݷ����쳣��" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_SAVE;
    }

    private String parameterMsg;
    private String errorMsg;

    public String getParameterMsg() {
        return parameterMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String inputContentName() {
        try {
            long channelId = getRequestIntParam("channelId", 0);
            String[] names = obj.getName().split("\r\n"); //���ݻس��ָ�û������
            String contentMsg = "";

            if (names.length > 0) {
                for (String name : names) {
                    //����content
                    Content content = new Content();
                    content.setName(name);
                    content.setStatus(2l);
                    content.setCreateTime(new Date());
                    content.setCspId(obj.getCspId());
                    content = contentLogicInterface.save(content);

                    if (content != null) {
                        //���sp�Ĺ�ϵ
                        ContentCsp cCsp = new ContentCsp();
                        cCsp.setContentId(content.getId());
                        cCsp.setCspId(obj.getCspId());
                        cCsp.setStatus(2l);
                        cCsp.setStatusTime(new Date());
                        cCsp = contentCspLogicInterface.save(cCsp);
                        log.debug(cCsp.getId());

                        //���Ƶ����ϵ
                        ContentChannel cc = new ContentChannel();
                        cc.setContentId(content.getId());
                        cc.setChannelId(channelId);
                        cc = contentChannelLogicInterface.save(cc);
                        log.debug(cc.getId());
                        contentMsg += name + "����������" + "?cid=" + channelId + "&mid=" + content.getId() + "&spId=" + content.getCspId() + "<br>";

                        /* contentLogicInterface.remove(content.getId());*/

                    }
                }
            }

            setSuccess(true);
            parameterMsg = contentMsg;
        } catch (Exception e) {
            setSuccess(false);
            errorMsg = "����¼��ʧ�ܣ�";
        }
        return "contentParameter";
    }


    public String searchContentParameter() {
        long channelId = getRequestIntParam("searchChannelId", 0);
        String name = getRequestParam("obj.name", "");
        long contentCspStatus = getRequestIntParam("contentCspStatus", -1);

        List<Map> list = contentLogicInterface.searchContentParameter(name, channelId, obj.getCspId(), contentCspStatus, pageBean);

        String result = "{totalCount:" + pageBean.getRowCount() + ",objs:" + getJsonArray(list) + "}";

        directOut(result);
        return null;
    }


    public String cpChangeStatus() {
        log.debug("in cpChangeStatus");
        String dealMessage = "";
        long status = getRequestIntParam("status", 0);
        if (keys != null) {
            for (String aKey : this.keys) {
                try {
                    Content content = contentLogicInterface.get(new Long(aKey));
                    writeSysLog(contentLogicInterface.setContentStatus(content,status));
                    dealMessage += aKey;
                } catch (Exception e) {
                    log.error("����ӰƬ��Դ״̬ʱ�����쳣��"+e.getMessage());
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }


    private String contentIdArray;

    public void setContentIdArray(String contentIdArray) {
        this.contentIdArray = contentIdArray;
    }


    public String doCpChangeStatus() {
        log.debug("in cpChangeStatus");
        String dealMessage = "";
        long status = getRequestIntParam("status", 0);
        String[] contentIds = contentIdArray.split(",");
        if (contentIds != null && contentIds.length > 0) {
            for (String aKey : contentIds) {
                try {
                    Content content = contentLogicInterface.get(new Long(aKey));
                    writeSysLog(contentLogicInterface.setContentStatus(content,status));
                    dealMessage += aKey;
                } catch (Exception e) {
                    log.error("����ӰƬ��Դ״̬ʱ�����쳣��"+e.getMessage());
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }

    private long spId;
    private long status;

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String spDelete() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_DELETE, keys);
    }

    public String contentHidden() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_HIDDEN, keys);
    }


    public String spWait_audit() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_WAIT_AUDIT, keys);
    }

    public String spApplyOffline() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_APPLY_OFFLINE, keys);
    }

    public String spRecycle() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_RECYCLE, keys);
    }

    public String spOffline() {
        return setContentCspStatus(spId, ContentCspLogicInterface.STATUS_OFFLINE, keys);
    }

    public String spChangeStatus() {
        return setContentCspStatus(spId, status, keys);
    }

    private String setContentCspStatus(long spId, long status, List<String> contentIds) {
        log.debug("in spChangeStatus");
        String dealMessage = "";
        String operaUse;
        long initialStatusNum;
        String initialStatus;
        Csp sp = cspLogicInterface.get(spId);
        operaUse = admin.getLogin()+"("+admin.getRealname()+")��SP��"+sp.getName()+"id:"+sp.getId()+"��";
        if (contentIds != null) {
            for (String aKey : contentIds) {
                try {
                    ContentCsp cc = contentCspLogicInterface.get(new Long(aKey));
                    initialStatusNum = cc.getStatus();
                    initialStatus = contentCspLogicInterface.getStatusString(initialStatusNum);
                    String logInfo = operaUse+"�޸�SP��Դ״̬:";
                    if (status == ContentLogicInterface.STATUS_WAIT_TO_ONLINE || status == ContentLogicInterface.STATUS_WAIT_TO_OFFLINE) {
                        if (status ==  ContentLogicInterface.STATUS_WAIT_TO_ONLINE) {
                            if (sp.getIsSpOnlineAudit() != null && sp.getIsSpOnlineAudit() == 1) {
                                ContentAudit ca = new ContentAudit();
                                ca.setContentId(cc.getContentId());
                                ca.setType(ContentAuditLogicInterface.AUDIT_TYPE_SP_ONLINE);
                                ca.setStatus(ContentCspLogicInterface.STATUS_ONLINE);
                                ca.setApplyTime(new Date());
                                ca.setResult(ContentAuditLogicInterface.AUDIT_RESULT_WAITING);
                                ca = contentAuditLogicInterface.save(ca);
                                cc.setContentAuditId(ca.getId());
                                logInfo +="�������ߴ���״̬";
                            } else {
                                logInfo +="SP����ֱ�����ߣ���Ҫ�������з�������";
                                cc.setStatus(ContentCspLogicInterface.STATUS_ONLINE);//����ֱ������
                                cc.setStatusTime(new Date());
                            }
                        }
                        if (status == 6) {
                            if (sp.getIsSpOfflineAudit() != null && sp.getIsSpOfflineAudit() == 1) {
                                ContentAudit ca = new ContentAudit();
                                ca.setContentId(cc.getContentId());
                                ca.setType(ContentAuditLogicInterface.AUDIT_TYPE_SP_OFFLINE);
                                ca.setStatus(ContentCspLogicInterface.STATUS_OFFLINE);
                                ca.setApplyTime(new Date());
                                ca.setResult(ContentAuditLogicInterface.AUDIT_RESULT_WAITING);
                                ca = contentAuditLogicInterface.save(ca);
                                logInfo +="�������ߴ���״̬��";
                                cc.setContentAuditId(ca.getId());
                            } else {
                                logInfo +="SP����ֱ�����ߣ�";
                                cc.setStatus(ContentCspLogicInterface.STATUS_OFFLINE);//����ֱ������
                                cc.setStatusTime(new Date());
                            }
                        }
                    } else {
                        logInfo +="���������߻����������ߣ�ֱ�Ӳ�����"+contentCspLogicInterface.getStatusString(status)+",status=" +
                                status+",";
                        cc.setStatus(status);
                        cc.setStatusTime(new Date());
                    }

                    contentCspLogicInterface.save(cc);

                    try {
                        Csp csp = cspLogicInterface.get(spId);
                        Content c = contentLogicInterface.get(cc.getContentId());
                        String operationStr = contentCspLogicInterface.getStatusString(status);
                        String resultStr=contentCspLogicInterface.getStatusString(cc.getStatus());
                        logInfo +=csp.getName()+"������Դ��"+c.getName()+"����contentId="+c.getId()+"�����������"+"����ǰ״̬Ϊ��"+initialStatus+",status="+initialStatusNum+"���ĺ�״̬Ϊ��" +operationStr+
                                "�����״̬Ϊ��"+resultStr+"��������Ϣ��"+logInfo;
                        writeSysLog(logInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("׼���޸�״̬��־ʱ�����쳣��"+e.getMessage());
                        writeSysLog("�޸�SP��Դ״̬:spId=" + spId + ",id=" + aKey + ",status=" + status+",������Ϣ��"+logInfo);
                    }
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }

//    public String getPublish() {
//
//        log.debug("in getPublish");
//        String dealMessage = "";
//        ContentCsp contentCsp = contentCspLogicInterface.get(new Long(keyId));
//        long contentId = contentCsp.getContentId();
//        long cspId = contentCsp.getCspId();
//        try {
///*
//            String sql="select * from Content_Channel cc where cc.content_Id="+contentId+" and cc.channel_Id in " +
//                    "(select c.id from Channel c where c.csp_Id="+cspId+") union select * from Content_Channel cc where cc.content_Id="+contentId+" and cc.channel_Id in (select ccl.channel_Id from CSP_CHANNEL ccl where ccl.csp_Id="+cspId+")";
//            List list1 = HibernateUtils.sqlFindAll(this.contentLogicInterface.getSession(), sql);
//*/
//            List list1 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentChannel cc where cc.contentId=" + contentId) ;//+ " and cc.channelId in (select c.id from Channel c where c.cspId=" + cspId + ")");
//            List list2 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentRecommend cr where cr.contentId=" + contentId + " and cr.recommendId in (select r.id from Recommend r where r.cspId=" + cspId + ")");
//            List list3 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentServiceProduct csp where csp.contentId=" + contentId + " and csp.serviceProductId in (select sp.id from ServiceProduct sp where sp.cspId=" + cspId + ")");
//
//            JsonUtils jsonUtils = new JsonUtils();
//            String output = "{success:\"true\",ContentChannel:" + jsonUtils.getListJson(list1) + ",ContentRecommend:" + jsonUtils.getListJson(list2) + ",ContentServiceProduct:" + jsonUtils.getListJson(list3) + "}";
//            directOut(output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
        public String getPublish(){
            log.debug("in getPublish");
            try {
            Long contentId =content.getId();
            List list1 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), this.contentLogicInterface.getContentChannel(contentId)) ;//+ " and cc.channelId in (select c.id from Channel c where c.cspId=" + cspId + ")");
            List list2 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), this.contentLogicInterface.getContentRecommend(contentId));
           // List list2 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentRecommend cr where cr.contentId=" + content.getId() + " and cr.recommendId in (select r.id from Recommend r where r.cspId=" + csp.getId() + ")");
            List list3 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), this.contentLogicInterface.getContentServiceProduct(contentId));
           //List list3 = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentServiceProduct csp where csp.contentId=" + content.getId() + " and csp.serviceProductId in (select sp.id from ServiceProduct sp where sp.cspId=" + csp.getId() + ")");
            JsonUtils jsonUtils = new JsonUtils();
            String output = "{success:\"true\",ContentChannel:" + jsonUtils.getListJson(list1) + ",ContentRecommend:" + jsonUtils.getListJson(list2) + ",ContentServiceProduct:" + jsonUtils.getListJson(list3) + "}";
            directOut(output);
            log.debug(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return  null;
        }

    /*
    public String changePublishSet(){
        log.debug("in changePublishSet");

        try {
            long keyId = getRequestIntParam("keyIds",0);
            ContentCsp contentCsp = contentCspLogicInterface.get(new Long(keyId));
            long contentId = contentCsp.getContentId();
            long cspId = contentCsp.getCspId();
            HibernateUtils.deleteAll(this.contentLogicInterface.getSession(),"delete from ContentChannel cc where cc.contentId="+contentId+" and cc.channelId in (select c.id from Channel c where c.cspId="+cspId+")");
            HibernateUtils.deleteAll(this.contentLogicInterface.getSession(),"delete from ContentRecommend cr where cr.contentId="+contentId+" and cr.recommendId in (select r.id from Recommend r where r.cspId="+cspId+")");
            HibernateUtils.deleteAll(this.contentLogicInterface.getSession(),"delete from ContentServiceProduct csp where csp.contentId="+contentId+" and csp.serviceProductId in (select sp.id from ServiceProduct sp where sp.cspId="+cspId+")");

            return publish();
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    */
    @SuppressWarnings("unchecked")
    public String deleteContentFile(){
        log.debug("in deleteContentFile");
        String contentIds = getRequestParam("keyIds","");
        try{
             if(!"".equals(contentIds)&&null!=contentIds){
                 String contentIdss[] = contentIds.split(",");
                 String info;
                 String deleteContentInfo;
                 String logInfo = admin.getLogin()+"("+admin.getRealname()+")";
                 for(String contentId:contentIdss){
                     info = "";
                     String sourceFilePath;
                     Content content = contentLogicInterface.get(StringUtils.string2long(contentId,-1));
                     info += "ɾ��IDΪ"+contentId+"��ӰƬ��Ϊ��"+content.getName()+"����";
                     long deviceId = content.getDeviceId();
                     Device device = deviceLogicInterface.get(deviceId);
                     String filePath  = device.getExportPath();
                     //����ʵ���ļ�λ��
                     List<ContentProperty> sourceCP = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentProperty where contentId = '"+contentId+"' and propertyId in (15884979,15884965,15884981,15884973)");
                     //ɾ��ʵ���ļ���Դ�ļ���384K��512K��768K��
                     if(sourceCP.size()>0){
                         for(ContentProperty cp:sourceCP){
                             deleteContentInfo = "";
                              Property p = propertyLogicInterface.get(cp.getPropertyId());
                             /*
                               ���Ҹ������Ƿ�������������Դ��ʹ�ã���ֹSP�������ʹ����ͬ�������ӵ���Դ����ɾ������һ���������У���鿴����Content��status�Ƿ�Ϊ9��
                               ��ȫ��Ϊ9.��ɾ��������ָ��������ļ�������ֻɾ�����Ӧ��ϵ����ɾ��ʵ���ļ���
                             */
                             List<ContentProperty> checkStringValue = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentProperty where stringValue = '"+cp.getStringValue()+"'");
                             if(checkStringValue.size()>1){
                                 boolean needDeleteFile = true;
                                 for(ContentProperty cp1:checkStringValue){
                                     Content content1 = contentLogicInterface.get(cp1.getContentId());
                                     if(9!=content1.getStatus()){
                                         needDeleteFile = false;
                                         deleteContentInfo += p.getName()+"��"+cp.getIntValue()+"����" +cp.getStringValue()+"ʧ��(ӰƬ�ļ���������Դʹ�ã����ܽ���ɾ������)��";
                                         break;
                                     }
                                 }
                                 if(needDeleteFile){
                                     //ƴ�ļ����ڷ���������λ�ã�����ɾ��
                                     sourceFilePath = filePath +"/" + cp.getStringValue();
                                     sourceFilePath = sourceFilePath.replace("/",File.separator);
                                     boolean deleteFile =  deleteFile(sourceFilePath);
                                     if(deleteFile){
                                         deleteContentInfo += p.getName()+"��"+cp.getIntValue()+"����" +cp.getStringValue()+"�ɹ�";
                                     }
                                 }
                             }else{
                                 sourceFilePath = filePath +"/"+ cp.getStringValue();
                                 sourceFilePath = sourceFilePath.replace("/",File.separator);
                                 boolean deleteFile =  deleteFile(sourceFilePath);
                                 if(deleteFile){
                                     deleteContentInfo += p.getName()+"��"+cp.getIntValue()+"����" +cp.getStringValue()+"�ɹ�";
                                 }else{
                                     deleteContentInfo += p.getName()+"��"+cp.getIntValue()+"����" +cp.getStringValue()+"ʧ�ܣ��ļ������ڣ���";
                                 }
                             }
                             writeSysLog(logInfo+info+deleteContentInfo);
                         }
                     }
                     //��ʼɾ�����ϵ
                     List<ContentCsp> cc = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentCsp where contentId = '"+contentId+"'");
                     if(cc.size()>0){
                         info += "ɾ����ӦContentCsp��"+cc.size()+"��";
                         for(ContentCsp contentCsp:cc){
                             long ccId = contentCsp.getId();
                             contentCspLogicInterface.remove(ccId);
                         }
                     }
                     List<ContentChannel> ch = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentChannel where contentId = '"+contentId+"'");
                     if(ch.size()>0){
                         info += "ɾ����ӦContentChannel��"+ch.size()+"��";
                         for(ContentChannel contentChannel:ch){
                             long chId = contentChannel.getId();
                             contentChannelLogicInterface.remove(chId);
                         }
                     }
                     List<ContentProperty> cp = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentProperty where contentId = '"+contentId+"'");
                     if(cp.size()>0){
                         info += "ɾ����ӦContentProperty��"+cp.size()+"�ţ�";
                         for(ContentProperty contentProperty:cp){
                             long cpId = contentProperty.getId();
                             contentPropertyLogicInterface.remove(cpId);
                         }
                     }
                     List<ContentRecommend> cr = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentRecommend where contentId = '"+contentId+"'");
                     if(cr.size()>0){
                         info += "ɾ����ӦContentRecommend��"+cc.size()+"�ţ�";
                         for(ContentRecommend contentRecommend:cr){
                             long crId = contentRecommend.getId();
                             contentRecommendLogicInterface.remove(crId);
                         }
                     }
                     contentLogicInterface.remove(content);
                     info += "ɾ��Content��ɾ��IDΪ"+contentId+"��������Դ��ɡ�";
                     logInfo += info;
                     writeSysLog(logInfo);
                 }
                 return  Constants.ACTION_DELETE;
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    public String cpSearch() {
        try {

            String sqlTable = "select " +
                    "c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url" +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c" +
                    " where 1=1 ";

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.status", "status12", "<", ""},
                    {"c.status", "status8", "=", ""},
                    {"c.status", "status9", "=", ""},
                    {"c.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"},
            };


            SearchResult<Object[]> seachResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, seachResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String spSearch() {
        try {
            SearchCondition searchCondition = new SearchCondition();
            long cspId = getRequestIntParam("cc_cspId",-1);
            int status = getRequestIntParam("cc_status",-1);
            int isSpecialExit=getRequestIntParam("isSpecialExit",0);
            String fieldStr="c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount,c.visitCountStatus," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.score,c.scoreCount,c.scoreStatus";
            String tableStr=" com.fortune.rms.business.content.model.Content c";
            String whereStr=" 1=1 ";
            if(cspId>1||status>=0){
                fieldStr +=",cc.id,cc.cspId,cc.status,cc.statusTime,sp.name ";
                tableStr +=",com.fortune.rms.business.content.model.ContentCsp cc," +
                        "com.fortune.rms.business.csp.model.Csp sp ";
                whereStr +=" and c.id=cc.contentId and cc.cspId = sp.id ";
                if(cspId>1){
                    whereStr +=" and cc.cspId = "+cspId;
                }
                if(status>=0){
                    whereStr +=" and cc.status="+status;
                }
                if(isSpecialExit>0){
                    whereStr +=" and c.isSpecial="+isSpecialExit;
                }
            }
            String sqlTable = "select " +
                     fieldStr+
                    " from " + tableStr+
                    " where"+whereStr;
            long channelId = getRequestIntParam("channelId", -1);
            if(channelId>0){
                List list = channelLogicInterface.getSonChannelId(channelId);
                String channelIds= ""+channelId+",";
                if(list.size()>0){
                    for(Object o :list){
                        Long channelId1 = StringUtils.string2long(o.toString(),-1);
                        channelIds += channelId1+",";
                        List list1 = channelLogicInterface.getSonChannelId(channelId1);
                        if(list.size()>0){
                            for(Object o1 :list1){
                                channelIds += o1.toString()+",";
                            }
                        }
                    }
                }
                channelIds = channelIds.substring(0,channelIds.length()-1);
                if (channelIds.length() > 0) {
                    sqlTable += " and c.id in(select ch.contentId from com.fortune.rms.business.content.model.ContentChannel ch where ch.channelId in  (" + channelIds + ")) ";
                }
            }

            long visitCountStatus = getRequestIntParam("visitCountStatus",-1);
            if(visitCountStatus>0){
                    sqlTable += " and c.visitCountStatus >= " + visitCountStatus;
            }

            long scoreStatus = getRequestIntParam("scoreStatus",-1);
            if(scoreStatus>0){
                    sqlTable += " and c.scoreStatus >= " + scoreStatus;
            }

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public SearchResult<Object[]> searchObjects(String sqlTable, String whereParams[][]) {
        HashMap<String, String> tables = new HashMap<String, String>();

        if (sqlTable != null && !"".equals(sqlTable)) {
            String tempStr = new String(sqlTable.getBytes());
            tempStr = tempStr.replace(',', ' ');
            String sqlTables[] = tempStr.split(" ");
            for (int i = 0; i < sqlTables.length; i++) {
                if (i > 0) {
                    tables.put(sqlTables[i], sqlTables[i - 1]);
                }
            }
        }

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSqlStr(sqlTable);
        if (whereParams != null) {
            for (String[] param : whereParams) {
                if (param.length == 5 && !"".equals(param[4])) {
                    String tempStr = new String(param[4].getBytes());
                    tempStr = tempStr.replace(',', ' ');
                    String sqlTables[] = tempStr.split(" ");
                    for (int i = 0; i < sqlTables.length; i++) {
                        if (i > 0) {
                            tables.put(sqlTables[i], sqlTables[i - 1]);
                        }
                    }
                }


                String tableName = param[0].substring(0, param[0].indexOf('.'));
                tableName = tables.get(tableName);
                String propertyName = param[0].substring(param[0].indexOf('.') + 1, param[0].length());
                String inputParamName = param[1];
                if ("".equals(param[1])) {
                    inputParamName = param[0].replace('.', '_');
                }
                String inputParamValue = getRequestParam(inputParamName, "");
                if (!"".equals(inputParamValue)) {
                    try {
                        ClassMetadata cm = this.contentLogicInterface.getSession().getSessionFactory().getClassMetadata(Class.forName(tableName));
                        Type propertyType = cm.getPropertyType(propertyName);
                        Object pValue = null;
                        if (propertyType instanceof LongType) { //Hibernate.LONG;
                            pValue = new Long(inputParamValue);
                        } else if (propertyType instanceof IntegerType) { //Hibernate.INTEGER;
                            pValue = new Integer(inputParamValue);
                        } else if (propertyType instanceof BigDecimalType) {
                            pValue = new BigDecimal(inputParamValue);
                        } else if (propertyType instanceof DateType || propertyType instanceof TimestampType) {
                            String dateFormat = "yyyy-MM-dd HH:mm:ss";
                            if (inputParamValue.length() == 10) {
                                //dateFormat = "yyyy-MM-dd";
                                inputParamValue += " " + param[3];
                            }
                            Date date = StringUtils.string2date(inputParamValue, dateFormat);
                            pValue = new Timestamp(date.getTime());
                        } else if (propertyType instanceof StringType) {
                            pValue = inputParamValue;
                        } else if (propertyType instanceof BigIntegerType) {
                            pValue = new BigInteger(inputParamValue);
                        }

                        if (param.length == 5 && !"".equals(param[4])) {
                            String hql = param[4];
                            hql = hql.replaceFirst("[?]", param[0] + " " + param[2] + " ?");
                            if ("like".equals(param[2])) {
                                searchCondition.appendAndSqlCondition(hql, param[3].replaceFirst("[?]", "" + pValue), propertyType);
                            } else {
                                searchCondition.appendAndSqlCondition(hql, pValue, propertyType);
                            }
                        } else {
                            if ("like".equals(param[2])) {
                                searchCondition.appendAndSqlCondition(param[0] + " " + param[2] + " ?", param[3].replaceFirst("[?]", "" + pValue), propertyType);
                            } else {
                                searchCondition.appendAndSqlCondition(param[0] + " " + param[2] + "?", pValue, propertyType);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        String sql = searchCondition.getSqlStr();
        Object[] params = searchCondition.getObjectArrayParamValues();
        Type[] paramTypes = searchCondition.getTypeArray();

        try {
            int count = HibernateUtils.findCount(this.contentLogicInterface.getSession(), sql, params, paramTypes);

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by " + orderBy + " " + orderDir;
            }

            List list1 = HibernateUtils.findList(this.contentLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRowCount(count);
            searchResult.setRows(list1);

            return searchResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchObjectsJbon(String sqlTable, SearchResult<Object[]> searchResult) {
        //ȡ����ͷ
        String columns[];
        if (sqlTable.contains(" from ")) {
            sqlTable = sqlTable.substring(0, sqlTable.indexOf(" from "));
        }
        sqlTable = sqlTable.substring(6, sqlTable.length());
        sqlTable = sqlTable.replace('.', '_');
        sqlTable = sqlTable.trim();
        columns = sqlTable.split(",");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("totalCount", searchResult.getRowCount());
        List<Map<String, Object>> objs = new ArrayList<Map<String, Object>>();
        List list1 = searchResult.getRows();
        if (list1 != null && list1.size() > 0) {
            for (Object aList1 : list1) {
                Map<String, Object> row = new HashMap<String, Object>();
                Object[] obj = (Object[]) aList1;
                for (int j = 0; j < columns.length; j++) {
                    String column = columns[j];
                    Object colData = obj[j];
                    if (colData != null && colData instanceof Date) {
                        row.put(column, StringUtils.date2string((Date) colData));
                    } else {
                        row.put(column, colData);
                    }
                }
                objs.add(row);
            }
        }
        data.put("objs", objs);
        return JsonUtils.getJsonString(data);
    }


    public String cpAuditSearch() {
        try {
            String sqlTable = "select " +
                    "ca.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "ca.applyTime " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentAudit ca," +
                    "com.fortune.rms.business.csp.model.CspAuditor cadmin " +
                    " where c.contentAuditId=ca.id and c.cspId=cadmin.cspId and c.status<8 and ca.result=0 ";

            String[][] params = new String[][]{
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"},
                    {"c.status", "", "<>", ""},
                    {"ca.type", "", "=", ""},
                    {"cadmin.adminId", "", "=", ""},
                    {"cadmin.cpOnline", "", "=", ""},
                    {"cadmin.cpOffline", "", "=", ""},
                    {"cadmin.spOnline", "", "=", ""},
                    {"cadmin.spOffline", "", "=", ""}
            };


            SearchResult<Object[]> seachResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, seachResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String spAuditSearch() {
        try {

            String sqlTable = "select " +
                    "ca.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "cc.cspId,cc.status,cc.statusTime," +
                    "ca.applyTime " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentCsp cc," +
                    "com.fortune.rms.business.content.model.ContentAudit ca," +
                    "com.fortune.rms.business.csp.model.CspAuditor cadmin " +
                    " where c.id=cc.contentId and cc.contentAuditId=ca.id and cc.cspId=cadmin.cspId and c.status=2  and cc.status<8  and ca.result=0 ";

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"cc.cspId", "", "=", ""},
                    {"cc.status", "", "<>", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"},
                    {"ca.type", "", "=", ""},
                    {"cadmin.adminId", "", "=", ""},
                    {"cadmin.cpOnline", "", "=", ""},
                    {"cadmin.cpOffline", "", "=", ""},
                    {"cadmin.spOnline", "", "=", ""},
                    {"cadmin.spOffline", "", "=", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String contentAudit() {
        log.debug("in content Audit");
        String dealMessage = "";
        if (keys != null) {
            long adminId = getRequestIntParam("adminId", 0);
            long result = getRequestIntParam("result", 0);
            String resultMsg = getRequestParam("resultMsg", "");
            for (String aKey : this.keys) {
                try {

                    ContentAudit ca = contentAuditLogicInterface.get(new Long(aKey));


                    if ((ca.getType() == 1 || ca.getType() == 2) && result == 1) {
                        Content content = contentLogicInterface.get(ca.getContentId());
                        content.setStatus(ca.getStatus());
                        content.setStatusTime(new Date());

                        contentLogicInterface.save(content);

                        if (ca.getType() == 1) {//cp�������
                            //cp����, �޸�sp��ӰƬ״̬Ϊ0, ��Ƭ
                            HibernateUtils.executeUpdate(this.contentLogicInterface.getSession(), "update ContentCsp cc set cc.status=0 where cc.contentId=" + content.getId());
                        }
                    }

                    if ((ca.getType() == 3 || ca.getType() == 4) && result == 1) {
                        List ccList = HibernateUtils.findAll(this.contentLogicInterface.getSession(), "from ContentCsp cc where cc.contentAuditId=" + aKey);
                        if (ccList != null && ccList.size() > 0) {
                            ContentCsp cc = (ContentCsp) ccList.get(0);
                            cc.setStatus(ca.getStatus());
                            cc.setStatusTime(new Date());

                            contentCspLogicInterface.save(cc);
                        }
                    }

                    ca.setResult(result);
                    ca.setResultMsg(resultMsg);
                    ca.setAuditAdminId(adminId);
                    ca.setAuditTime(new Date());

                    contentAuditLogicInterface.save(ca);

                    writeSysLog("�޸�SP��Դ״̬:spId=" + spId + ",contentId=" + aKey + ",status=" + status);
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }

    public String cpAuditResultSearch() {
        try {

            String sqlTable = "select " +
                    "ca.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "ca.type,ca.applyTime,ca.result,ca.resultMsg,ca.auditAdminId,ca.auditTime " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentAudit ca" +
                    " where c.contentAuditId=ca.id ";

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"},
                    {"ca.type", "", "=", ""},
                    {"ca.result", "", "=", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String spAuditResultSearch() {
        try {

            String sqlTable = "select " +
                    "ca.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "cc.id,cc.cspId,cc.status,cc.statusTime," +
                    "ca.type,ca.applyTime,ca.result,ca.resultMsg,ca.auditAdminId,ca.auditTime " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentCsp cc," +
                    "com.fortune.rms.business.content.model.ContentAudit ca" +
                    " where c.id=cc.contentId and cc.contentAuditId=ca.id ";

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"cc.cspId", "", "=", ""},
                    {"cc.status", "", "!=", "-1"},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"},
                    {"ca.type", "", "=", ""},
                    {"ca.result", "", "=", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateDigitalRightBySearch() {
        log.debug("in updateDigitalRightBySearch");
        String sqlTable = "select c.id,c.name from " +
                "com.fortune.rms.business.content.model.Content c" +
                " where c.status<3 ";

        String[][] params = new String[][]{
                {"c.cspId", "", "=", ""},
                {"c.name", "", "like", "%?%"},
                {"c.status", "", "=", ""},
                {"c.createTime", "startDate", ">=", "00:00:00"},
                {"c.createTime", "endDate", "<=", "23:59:59"},
        };


        SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);
        List contentIds = searchResult.getRows();
        for (Object contentId : contentIds) {
            Object objs[] = (Object[]) contentId;
            keys.add(valueOf(objs[0]));

        }
        return updateDigitalRight();
    }

    public String updateDigitalRight() {
        log.debug("in updateDigitalRight");
        String dealMessage = "";
        if (keys != null) {
            for (String aKey : this.keys) {
                try {
                    String rightStartTime = getRequestParam("rightStartTime", "");
                    String rightEndTime = getRequestParam("rightEndTime", "");
                    String rightUrl = getRequestParam("rightUrl", "");

                    Content c = contentLogicInterface.get(new Long(aKey));
                    c.setValidStartTime(StringUtils.string2date(rightStartTime, "yyyy-MM-dd"));
                    c.setValidEndTime(StringUtils.string2date(rightEndTime, "yyyy-MM-dd"));
                    c.setDigiRightUrl(rightUrl);

                    contentLogicInterface.save(c);

                    writeSysLog("�޸���Դ��Ȩ״̬:��" + c.getName()+"��"+rightStartTime+"->"+rightEndTime+",֤�飺"+rightUrl);
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        return Constants.ACTION_SAVE;
    }

    public String spRecommendSearch() {
        try {

            String fieldStr = "cr.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.isSpecial," +
                    "cr.recommendId,cr.channelId,cr.displayOrder";
            String cspId = getRequestParam("cc_cspId",null);
            String cspStatus = getRequestParam("cc_status",null);
            if(cspId!=null||cspStatus!=null){
                fieldStr += ",cc.cspId,cc.status,cc.statusTime ";
            }
            String sqlTable = "select " +
                    fieldStr  +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentRecommend cr ";
            if(cspId!=null||cspStatus!=null){
                 sqlTable += ",com.fortune.rms.business.content.model.ContentCsp cc ";
            }
            sqlTable +=" where c.id=cr.contentId ";
            if(cspId !=null||cspStatus!=null){
                sqlTable +=" and c.id=cc.contentId";
                if(cspId!=null){
                    sqlTable +=" and cc.cspId="+cspId;
                }
                if(cspStatus!=null){
                    sqlTable +=" and cc.status="+cspStatus;
                }
            }

            String[][] params = new String[][]{
                    {"c.status", "", "=", ""},
                    {"cr.recommendId", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"cr.channelId", "", "=", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);
            //����ǰ̨չʾ��˳��
            for(int j = 0 ;j<searchResult.getRows().size();j++){
               Object[] o =  searchResult.getRows().get(j);
               if(o[23]==null){
                   o[23]=j+1;
               }
               if(Long.parseLong(o[23].toString())!=j+1){
                   o[23]=j+1;
               }
            }
            //���ݿ����displayOrder
            String resetDisplayOrder = resetDisplayOrder(searchResult.getRows());

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String resetDisplayOrder(List<Object[]> list){
        try{
            for(int i = 0 ;i<list.size();i++){
                Object[] o = list.get(i);
                Long contentRecommendId =  Long.parseLong(o[0].toString());
                ContentRecommend cr = contentRecommendLogicInterface.get(contentRecommendId);
                cr.setDisplayOrder((long)i+1);
                contentRecommendLogicInterface.save(cr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //�ı���ʾ˳��
//    public String changeRecommendDisplayOrder() {
//        log.debug("in changeRecommendDisplayOrder");
//        String recommmendId = "";
//        try {
//            String uploadData = getRequestParam("uploadData", "");
//            if (!"".equals(uploadData)) {
//                String ss[] = uploadData.split(",");
//                for (String s : ss) {
//                    if (s != null && s.length() > 0) {
//                        String sss[] = s.split("_");
//                        long key = Long.parseLong(sss[0]);
//                        long displayOrder = Long.parseLong(sss[1]);
//                        ContentRecommend cr = contentRecommendLogicInterface.get(key);
//                        cr.setDisplayOrder(displayOrder);
//                        recommmendId = cr.getRecommendId().toString();
//                        contentRecommendLogicInterface.save(cr);
//                    }
//                }
//            }
//            writeSysLog("����" + guessName(obj));
//            super.addActionMessage("�ɹ��������ݣ�");
//        } catch (Exception e) {
//            super.addActionError("�������ݷ����쳣��" + e.getMessage());
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        JsUtils jsUtils = new JsUtils();
//        long cspId = ((Admin) ActionContext.getContext().getSession().get("sessionOperator")).getCspId();
//        String fullFilePath = ServletActionContext.getServletContext().getRealPath("/");
//        if (!"".equals(recommmendId)) {
//            try {
//                jsUtils.createRecommendJsFile("index_" + recommmendId, cspId, fullFilePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        return Constants.ACTION_SAVE;
//    }

   //�ı���ʾ˳��
    public String changeRecommendDisplayOrder(){
        log.debug("in changeRecommendDisplayOrder");
        String keyIds =  getRequestParam("keyIds","");
        String type = getRequestParam("type","");
        String keyIdss[] = keyIds.split("_");
        try{
        for(String s:keyIdss){
            String ss[] = s.split(",");
            long recommendId = Long.parseLong(ss[0]);
            long contentRecommendId = Long.parseLong(ss[1]);
            long displayOrder = Long.parseLong(ss[2]);
            if(displayOrder!=-1){
                //����
                if("-1".equals(type)){
                    displayOrder = displayOrder-1;
                    List<ContentRecommend> list = contentRecommendLogicInterface.getContentRecommendByDisplayOrder(displayOrder,recommendId);
                    if(list.size()>0){
                        ContentRecommend cr = list.get(0);
                        cr.setDisplayOrder(displayOrder+1);
                        contentRecommendLogicInterface.save(cr);
                    }
                    ContentRecommend cr1 = contentRecommendLogicInterface.get(contentRecommendId);
                    cr1.setDisplayOrder(displayOrder);
                    contentRecommendLogicInterface.save(cr1);
                }
                //����
                if("1".equals(type)){
                    displayOrder = displayOrder+1;
                    List<ContentRecommend> list = contentRecommendLogicInterface.getContentRecommendByDisplayOrder(displayOrder,recommendId);
                    if(list.size()>0){
                        ContentRecommend cr = list.get(0);
                        cr.setDisplayOrder(displayOrder-1);
                        contentRecommendLogicInterface.save(cr);
                    }
                    ContentRecommend cr1 = contentRecommendLogicInterface.get(contentRecommendId);
                    cr1.setDisplayOrder(displayOrder);
                    contentRecommendLogicInterface.save(cr1);
                }
            }
        }
            CacheUtils.clearAll();
            return Constants.ACTION_SAVE;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //����Ƶ��
    public String changeRecommendChannel() {
        log.debug("in changeRecommendChannel");
        //String recommendId = "";
        try {
            String uploadData = getRequestParam("uploadData", "");
            if (!"".equals(uploadData)) {
                String ss[] = uploadData.split(",");
                for (String s : ss) {
                    if (s != null && s.length() > 0) {
                        String sss[] = s.split("_");
                        long key = Long.parseLong(sss[0]);
                        long channelId = Long.parseLong(sss[1]);
                        ContentRecommend cr = contentRecommendLogicInterface.get(key);
                        cr.setChannelId(channelId);
                        //                recommendId = cr.getRecommendId().toString();
                        contentRecommendLogicInterface.save(cr);
                    }
                }
            }
            writeSysLog("����" + guessName(obj));
            super.addActionMessage("�ɹ��������ݣ�");
        } catch (Exception e) {
            super.addActionError("�������ݷ����쳣��" + e.getMessage());
            e.printStackTrace();
        }
        CacheUtils.clearAll();
        return Constants.ACTION_SAVE;
    }

    public String addRecommendContent() throws Exception {

        log.debug("in addRecommendContent");
        String dealMessage = "";
        if (keys != null) {
            long recommendType = getRequestIntParam("recommendType", 0);
            long recommendId = getRequestIntParam("recommendId", 0);
            long channelId = getRequestIntParam("channelId", 0);
            for (String aKey : this.keys) {
                try {
                    if(channelId == 0){
                        ContentRecommend cr = new ContentRecommend();
//                    if (recommendType == 2) {
                        cr.setChannelId(channelId);
//                    }
                        cr.setRecommendId(recommendId);
                        cr.setDisplayOrder(0L);
                        cr.setContentId(new Long(aKey));

                        contentRecommendLogicInterface.save(cr);
                        Content c= contentLogicInterface.get(cr.getContentId());
                        Recommend recommend = recommendLogicInterface.get(recommendId);
                        writeSysLog("������Դ�Ƽ���Ϣ����ý�塶" +c.getName()+"��(id:" +c.getId()+
                                ")�󶨵��Ƽ���"+recommend.getName());
                        dealMessage += aKey;
                    }
                    if(recommendId == 0){
                        ContentRecommend cr = new ContentRecommend();
                        cr.setChannelId(channelId);
                        cr.setRecommendId(recommendId);
                        cr.setDisplayOrder(0L);
                        cr.setContentId(new Long(aKey));

                        contentRecommendLogicInterface.save(cr);
                        Content c = contentLogicInterface.get(cr.getContentId());
                        Channel channel = channelLogicInterface.get(channelId);
                        writeSysLog("������ԴƵ���Ƽ���Ϣ����ý�塶" +c.getName()+"��(id:"+c.getId()+
                                ")�󶨵�Ƶ���Ƽ���"+channel.getName());
                        dealMessage += aKey;
                    }
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
//        String recommendId = "index_" + valueOf(getRequestIntParam("recommendId", 0));
//        long cspId = ((Admin) ActionContext.getContext().getSession().get("sessionOperator")).getCspId();
//        String fullFilePath = ServletActionContext.getServletContext().getRealPath("/");
//        DelayRunner.getInstance().startSession(recommendId, cspId, fullFilePath);
        CacheUtils.clearAll();
        return Constants.ACTION_SAVE;
    }

    //����Ƽ���ϵ
    public String deleteRecommendContent() {
        log.debug("in deleteRecommendContent");
        String dealMessage = "";
        if (keys != null) {
            for (String aKey : this.keys) {
                try {
                    long crId = StringUtils.string2long(aKey,-1);
                    if(crId>0){
                        ContentRecommend cr = contentRecommendLogicInterface.get(crId);
                        contentRecommendLogicInterface.remove(cr);
                    }
                    //HibernateUtils.deleteAll(this.contentLogicInterface.getSession(), "delete from ContentRecommend cr where cr.id=" + aKey);
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
//        String recommendId = "index_" + valueOf(getRequestIntParam("recommendId", 0));
//        long cspId = ((Admin) ActionContext.getContext().getSession().get("sessionOperator")).getCspId();
//        String fullFilePath = ServletActionContext.getServletContext().getRealPath("/");
//        DelayRunner.getInstance().startSession(recommendId, cspId, fullFilePath);
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }
    //ɾ���ϴ��ļ�����Ϣ
    @SuppressWarnings("unchecked")
    public String deleteZipRecord() {
        log.debug("in deleteZipRecord");
        String dealMessage = "";
        if (keys != null) {
            for (String aKey : this.keys) {

                    long cId = StringUtils.string2long(aKey,-1);
                    if(cId>0){
                        try {
                                //����contentIdΪxʱ��contentProperty��İ󶨹�ϵ��ɾ����
                                List<ContentProperty> list = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentProperty where contentId = " + cId);
                                if(list.size()>0){
                                    for(ContentProperty cp :list){
                                        contentPropertyLogicInterface.remove(cp);
                                    }
                                }
                                //����contentIdΪxʱ��contentCsp��İ󶨹�ϵ��ɾ����
                                List<ContentCsp> list1 = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from ContentCsp where contentId = " + cId);
                                if(list1.size()>0){
                                    for(ContentCsp cc : list1){
                                        contentCspLogicInterface.remove(cc);
                                    }
                                }
                           int list2 = HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from Content where id = " + cId);
                            if(list2>0){
                                log.debug("Content������ɾ���ɹ���");
                            }
                        } catch (Exception e) {
                            writeSysLog("ɾ���ϴ��ļ���Ϣ��content=" + cId);
                        }
                    }
                    dealMessage += aKey;
                }
            }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ�ɾ����Դ(" + dealMessage + ")");
            setSuccess(true);
        }
        return Constants.ACTION_DELETE;
    }
    public String spRelatedSearch() {
        try {

            String sqlTable = "select " +
                    "rpc.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "cc.cspId,cc.status,cc.statusTime," +
                    "rpc.relatedPropertyId,rpc.displayOrder " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentCsp cc," +
                    "com.fortune.rms.business.publish.model.RelatedPropertyContent rpc " +
                    " where c.id=cc.contentId and c.id=rpc.contentId ";

            String[][] params = new String[][]{
                    {"cc.cspId", "", "=", ""},
                    {"c.status", "", "=", ""},
                    {"cc.status", "", "=", ""},
                    {"rpc.relatedPropertyId", "", "=", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //ר���ҳ
    public String getContentsBySpecial(){
        HttpServletRequest request = ServletActionContext.getRequest();
        int start = StringUtils.string2int(request.getParameter("start"),0);
        int limit = StringUtils.string2int(request.getParameter("limit"),7);
        int butNum= StringUtils.string2int(request.getParameter("butColor"),1);
        if(limit<=0){
            limit =7;
        }
        PageBean pageBeanAll = new PageBean(1,1000,request.getParameter("orderBy"),request.getParameter("orderDir"));
        PageBean pageBean = new PageBean((start+limit)/limit,limit,request.getParameter("orderBy"),request.getParameter("orderDir"));
        if(pageBean.getOrderBy()==null){
            pageBean.setOrderBy("id");
            pageBean.setOrderDir("DESC");
        }
        //��ҳ��ѯ�ķ���
        List<Content> contentList = contentLogicInterface.getContentsBySpecial(pageBean);
        List<Content> contentAll = contentLogicInterface.getContentsBySpecial(pageBeanAll);
        int pageCount;
        if(contentAll.size()%limit==0){
            pageCount=contentAll.size()/limit;
        }else{
            pageCount=contentAll.size()/limit+1;
        }
        String result;
        int  totalCount=0;
        if(contentList.size()>0){
        result="{\"success\":\"true\",\"objs\":[";
            for (Content aContentList : contentList) {
                ++totalCount;
                String time = aContentList.getCreateTime().toString();
                String createTime[];
                createTime = time.split(" ");
                result += "{" +
                        "\"id\":\"" + aContentList.getId() + "\"," +
                        "\"createTime\":\"" + createTime[0] + "\"," +
                        "\"moveUrl\":\"" + aContentList.getPost1Url() + "\"," +
                        "\"postUrl\":\"" + "http://itv.inhe.net" + aContentList.getPost2Url() + "\"," +
                        "\"name\":\"" + aContentList.getName() + "\"" +
                        "},";
            }
         result = result.substring(0, result.length() - 1);
         result +="],\"totalCount\":\"" + totalCount + "\",\"contentAll\":\"" + contentAll.size() + "\",\"pageCount\":\"" + pageCount + "\",\"butNum\":\"" + butNum + "\"}";
        }else{
            result="{\"success\":\"false\",\"objs\":[]}";
        }
        directOut(result);
        return null;
    }
    public String changeRelatedDisplayOrder() {
        log.debug("in changeRelatedDisplayOrder");
        try {
            String uploadData = getRequestParam("uploadData", "");
            if (!"".equals(uploadData)) {
                String ss[] = uploadData.split(",");
                for (String s : ss) {
                    if (s != null && s.length() > 0) {
                        String sss[] = s.split("_");
                        long key = Long.parseLong(sss[0]);
                        long displayOrder = Long.parseLong(sss[1]);
                        RelatedPropertyContent rpc = relatedPropertyContentLogicInterface.get(key);
                        rpc.setDisplayOrder(displayOrder);
                        relatedPropertyContentLogicInterface.save(rpc);
                    }
                }
            }
            writeSysLog("����" + guessName(obj));
            super.addActionMessage("�ɹ��������ݣ�");
        } catch (Exception e) {
            super.addActionError("�������ݷ����쳣��" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String recommendId = "index_" + valueOf(getRequestIntParam("recommendId", 0));
        long cspId = ((Admin) ActionContext.getContext().getSession().get("sessionOperator")).getCspId();
        String fullFilePath = ServletActionContext.getServletContext().getRealPath("/");
        DelayRunner.getInstance().startSession(recommendId, cspId, fullFilePath);
        CacheUtils.clearAll();
        return Constants.ACTION_SAVE;
    }

    //����Ƽ�ӰƬ
    public String addRelatedContent() {
        log.debug("in addRelatedContent");
        String dealMessage = "";
        if (keys != null) {
            for (String aKey : this.keys) {
                try {

                    long relatedPropertyId = getRequestIntParam("relatedPropertyId", 0);

                    RelatedPropertyContent rpc = new RelatedPropertyContent();
                    rpc.setRelatedPropertyId(relatedPropertyId);
                    rpc.setContentId(new Long(aKey));

                    relatedPropertyContentLogicInterface.save(rpc);
                    Content c = contentLogicInterface.get(rpc.getContentId());
                    writeSysLog("�����Դ�����Ϣ:ý�塶" + c.getName()+"��");
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_SAVE;
    }

    public String deleteRelatedContent() {
        log.debug("in deleteRelatedContent");
        String dealMessage = "";
        if (keys != null) {
            for (String aKey : this.keys) {
                try {
                    HibernateUtils.deleteAll(this.contentLogicInterface.getSession(), "delete from RelatedPropertyContent rpc where rpc.id=" + aKey);
                    writeSysLog("ɾ����Դ�����Ϣ:contentId=" + aKey);
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        CacheUtils.clearAll();
        return Constants.ACTION_DELETE;
    }

    public String searchRelatedContent() {
        try {
            String propertyValue = getRequestParam("propertyValue", "");
            String sqlTable = "select " +
                    "cc.id,c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url," +
                    "cc.cspId,cc.status,cc.statusTime " +
                    " from " +
                    "com.fortune.rms.business.content.model.Content c," +
                    "com.fortune.rms.business.content.model.ContentCsp cc " +
                    " where c.id=cc.contentId and c.status=2 " + propertyValue + " ";

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"cc.cspId", "", "=", ""},
                    {"cc.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ContentProperty> contentProperties;

    public String saveCourse() {
        if (uploadFile != null) {
            obj.setPost1Url(saveTo("d:\\temp\\", true));
        }
        //if(obj.getCreateTime()==null) obj.setCreateTime(new Date());
        //if(obj.getCspId()==null) obj.setCspId(admin.getCspId().longValue());
        //if(obj.getContentAuditId()==null) obj.setContentAuditId(1L);
        //if(obj.getCreatorAdminId()==null) obj.setCreatorAdminId(admin.getId().longValue());
/*
        Long moduleId = obj.getModuleId();
        if(moduleId==null||moduleId==0L)  obj.setModuleId(5000L);  // todo ǿ������
        Long deviceId= obj.getDeviceId();
        if(deviceId==null||deviceId==0L) obj.setDeviceId(15879285L);
        Long status = obj.getStatus();
        if(status==null||status==0L) obj.setStatus(ContentLogicInterface.STATUS_WAITING);
*/
        com.fortune.util.BeanUtils.setDefaultValue(obj, "createTime", new Date());
        com.fortune.util.BeanUtils.setDefaultValue(obj, "cspId", admin.getCspId().longValue());
        com.fortune.util.BeanUtils.setDefaultValue(obj, "contentAuditId", 1L);
        com.fortune.util.BeanUtils.setDefaultValue(obj, "creatorAdminId", admin.getId().longValue());
        com.fortune.util.BeanUtils.setDefaultValue(obj, "moduleId", 5000L);
        com.fortune.util.BeanUtils.setDefaultValue(obj, "deviceId", 15879285L);
        com.fortune.util.BeanUtils.setDefaultValue(obj, "status", ContentLogicInterface.STATUS_WAITING);
        obj = contentLogicInterface.save(obj);
        contentCspLogicInterface.setStatus(obj.getId(), obj.getCspId(), -1, obj.getStatus());
        contentPropertyLogicInterface.saveClips(contentProperties, obj, encoderId);
        return "save";
    }

    public String viewCourse() {
        return super.view();
    }

    public String listCourse() {
        //objs = contentLogicInterface.list(obj.getCspId(),obj.getStatus(),AppConfigurator.getInstance().getLongConfig("rms.auto.defaultChannelId",15878100),pageBean);
        return super.search();
    }

    public String saveContentCspStatus() {
        log.debug("in saveContentCspStatus");
        String dealMessage = "";
        if (keys != null) {
            if (obj.getCspId() == null || obj.getCspId() <= 0) {
                obj.setCspId(admin.getCspId().longValue());
            }
            for (String aKey : this.keys) {
                try {
                    Content ob = contentLogicInterface.get(StringUtils.string2long(aKey, -1));
                    if (ContentLogicInterface.STATUS_WAITING.equals(ob.getStatus())) {
                        super.addActionMessage("�μ�(" + dealMessage + ")�����޸ģ���Ϊ���ڵȴ�ת�룡");
                        continue;
                    }
                    writeSysLog("���á�" + ob.getName() + "����״̬Ϊ��" + obj.getStatus());
                    ob.setStatus(obj.getStatus());
                    contentCspLogicInterface.setStatus(ob.getId(), obj.getCspId(),
                            AppConfigurator.getInstance().getIntConfig("rms.auto.defaultChannelId", -1), obj.getStatus());
                    //ͬ��һ�£�������ͬ��
                    contentLogicInterface.save(ob);
                    if (!"".equals(dealMessage)) {
                        dealMessage += ",";
                    }
                    dealMessage += ob.getName();
                } catch (Exception e) {
                    super.addActionError("�޷��ҵ�����Content" +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�д����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ�����ѡ�������(" + dealMessage + ")");
            setSuccess(true);
        }

//        baseLogicInterface.remove(keyId);
        CacheUtils.clearAll();
        return "success";
    }

    public String publishCourse() {
        obj.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
        return saveContentCspStatus();
    }

    public String unpublishCourse() {
        obj.setStatus(ContentCspLogicInterface.STATUS_OFFLINE);
        return saveContentCspStatus();
    }

    public List<ContentProperty> getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(List<ContentProperty> contentProperties) {
        this.contentProperties = contentProperties;
    }

    private long encoderId;

    public long getEncoderId() {
        return encoderId;
    }

    public void setEncoderId(long encoderId) {
        this.encoderId = encoderId;
    }

    public String saveTo(String targetDirectory, boolean dateFormateSubDir) {

        String urlFileName = "/upload/";
        if (dateFormateSubDir) {
            urlFileName += StringUtils.date2string(new Date(), "yyyy/MM/dd");
        }
        ServletContext context = ServletActionContext.getServletContext();
        if (context != null) {
            targetDirectory = context.getRealPath(urlFileName);
        }
        //��ȡ�ļ�����
        String targetFileName = uploadFile.getName();
        if (uploadFileLocal != null) {
            File aFile = new File(uploadFileLocal);
            targetFileName = aFile.getName();
        }
        //String targetFileName =  StringUtils.date2string(new Date(), "HHmmss") + "_" + aFile.getName()  ;
        int dotPos = targetFileName.indexOf(".", targetFileName.length() - 5);
        targetFileName = HzUtils.getFullSpell(targetFileName.substring(0, dotPos))
                + targetFileName.substring(dotPos);
        targetFileName = StringUtils.date2string(new Date(), "HHmmss") + "_" + targetFileName;
        targetFileName = StringUtils.replace(targetFileName, " ", "");  //ȥ���ո�

        urlFileName += "/" + targetFileName;
        try {
            //log.debug("Ŀ���ļ���" + targetDirectory + "\\" + targetFileName);
            org.apache.commons.io.FileUtils.forceMkdir(new File(targetDirectory));
            File target = new File(targetDirectory, targetFileName);
            org.apache.commons.io.FileUtils.copyFile(uploadFile, target);
            //log.debug("�������ӣ�" + urlFileName);
        } catch (IOException ioe) {
            log.error("�����쳣��" + ioe.getMessage());
            ioe.printStackTrace();
        }
        return urlFileName;
    }

    public List<String> importXml(boolean checkOnly,String fullFileName,Long cspId,String file,String cspFilePath,
                                  ContentLogicInterface contentLogicInterface,Admin admin,Long deviceId,Long moduleId,
                                  String xmlEncoding,String webAppRoot,String clientIp){
        List<String> list = contentLogicInterface.importXml(fullFileName, cspId, admin.getId(), deviceId, moduleId, xmlEncoding,
                webAppRoot,cspFilePath,file,checkOnly);
        //����������߳̽��д����Ͳ��ܼ򵥵ĵ���writeSysLog�����������������Լ����³�ʼ��һ��systemLogLogicInterface������
        SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface)SpringUtils.getBeanForApp("systemLogLogicInterface");
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
    public String saveXmlMessage() {
        String xmlFileName = null;
        //�õ���ǰ��½�˵�ID
        //Admin admin = (Admin) session.get(Constants.SESSION_ADMIN);
        ActionContext ctx = ServletActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        final Long cspId = (long)admin.getCspId();
        //����cspId��csp���ҵ���Ӧ��csp����
        Csp csp = cspLogicInterface.getCspByCspId(cspId);
        final String cspFilePath = csp.getAlias();
        try {
            xmlFileName = java.net.URLDecoder.decode(fileAddress, "UTF-8");//���������ļ���������
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String fullFileName;
        String urlFile="";
/*      2013��6��17���޸��߼�����device�л�ȡ���յ��ļ�Ŀ¼λ��  */
        AppConfigurator config = AppConfigurator.getInstance();
        if(config.getBoolConfig("importXml.useSystemDir",false)){
            if (cspFilePath != null && !"".equals(cspFilePath)) {
                fullFileName = AppConfigurator.getInstance().getConfig("importXml.baseFileDir", "E:/baseXmlDir/") + cspFilePath + xmlFileName;
            }else{
                fullFileName = AppConfigurator.getInstance().getConfig("importXml.baseFileDir", "E:/baseXmlDir/") + xmlFileName;
            }
        }else{
            Device device = deviceLogicInterface.get(deviceId);
            fullFileName = device.getLocalPath()+"/"+xmlFileName;
        }
        //String xmlStr = FileUtils.readFileInfo(fullFileName, "UTF-8");
        final boolean checkOnly="checkOnly".equals(importXmlCommand);
        final File xmlFile = new File(fullFileName);
        final String finalXmlFileName = xmlFileName;
        final Long finalDeviceId= deviceId;
        final String webAppRoot = ServletActionContext.getRequest().getRealPath("/");
        final Admin a = admin;
        final String finalXmlEncoding = xmlEncoding;
        final Long finalModuleId = moduleId;
        final ContentLogicInterface finalContentLogicInterface = contentLogicInterface;
        final String clientIp = request.getRemoteAddr();
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
        directOut(jsonUtils.getListJson("objs", result, "totalCount", result.size()));
        return null;
    }

    //��ѯ�ϴ����û��ϴε�ѹ������Ϣ
    public String spZipSearch(){
        try {
            SearchCondition searchCondition = new SearchCondition();
            long cspId = getRequestIntParam("cc_cspId",-1);
            int status = getRequestIntParam("cc_status",-1);
            String fieldStr="c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount,c.visitCountStatus," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.score,c.scoreCount,c.scoreStatus";
            String tableStr=" com.fortune.rms.business.content.model.Content c";
            String whereStr=" 1=1 ";
            if(cspId>1||status>=0){
                if(cspId>1){
                    whereStr +="and c.post1Url is not null and c.cspId = "+cspId;
                }
                if(status>=0){
                    whereStr +=" and c.status="+status;
                }
            }
            String sqlTable = "select " +
                    fieldStr+
                    " from " + tableStr+
                    " where"+whereStr+" order by c.createTime DESC";
            SearchResult<Object[]> searchResult = searchObjects(sqlTable, null);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getXmlEncoding() {
        return xmlEncoding;
    }

    public void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    private  String importXmlCommand;

    public String getImportXmlCommand() {
        return importXmlCommand;
    }

    public void setImportXmlCommand(String importXmlCommand) {
        this.importXmlCommand = importXmlCommand;
    }

    /**
     * add by mlwang @2014-10-15
     * ����Լ�����������״̬����Ƶ��¼
     * @return null
     */
    @Action(value = "normalContent")
    public String getNormalContentByCreator() {
        try {
            SearchCondition searchCondition = new SearchCondition();
            long cspId = getRequestIntParam("cc_cspId",-1);
            int status = getRequestIntParam("cc_status",-1);
            int isSpecialExit=getRequestIntParam("isSpecialExit",0);
            String fieldStr="c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount,c.visitCountStatus," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.score,c.scoreCount,c.scoreStatus";
            String tableStr=" com.fortune.rms.business.content.model.Content c";
            // mod by mlwang @2014-10-15��ֻ��ѯ���˷�����
            long creatorId = ((Admin)session.get(Constants.SESSION_ADMIN)).getId();
            String whereStr=" c.creatorAdminId=" + creatorId;
            // ֻ��ʾ�ȴ�ת�롢����ת�롢�ȴ���ˡ����ߺ�����״̬������
            whereStr += " and (c.status in(" + ContentLogicInterface.STATUS_WAITING_FOR_ENCODE +
                    "," + ContentLogicInterface.STATUS_ENCODING +
                    "," + ContentLogicInterface.STATUS_WAITING_FOR_AUDIT +
                    "," + ContentLogicInterface.STATUS_CP_ONLINE +
                    "," + ContentLogicInterface.STATUS_CP_OFFLINE + "))";
            String sqlTable = "select " +
                     fieldStr+
                    " from " + tableStr+
                    " where"+whereStr;
            long channelId = getRequestIntParam("channelId", -1);
            if(channelId>0){
                List list = channelLogicInterface.getSonChannelId(channelId);
                String channelIds= ""+channelId+",";
                if(list.size()>0){
                    for(Object o :list){
                        Long channelId1 = StringUtils.string2long(o.toString(),-1);
                        channelIds += channelId1+",";
                        List list1 = channelLogicInterface.getSonChannelId(channelId1);
                        if(list.size()>0){
                            for(Object o1 :list1){
                                channelIds += o1.toString()+",";
                            }
                        }
                    }
                }
                channelIds = channelIds.substring(0,channelIds.length()-1);
                if (channelIds.length() > 0) {
                    sqlTable += " and c.id in(select ch.contentId from com.fortune.rms.business.content.model.ContentChannel ch where ch.channelId in  (" + channelIds + ")) ";
                }
            }

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * add by mlwang @2014-10-15
     * ��ҳ�����Ƽ���ѯʹ�ã�֧�ִ󺣱�����
     * @return null
     */
    @Action(value = "searchContent")
    public String searchContent() {
        try {
            SearchCondition searchCondition = new SearchCondition();
            long cspId = getRequestIntParam("cc_cspId",-1);
            int status = getRequestIntParam("cc_status",-1);
            int isSpecialExit=getRequestIntParam("isSpecialExit",0);
            String fieldStr="c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount,c.visitCountStatus," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.score,c.scoreCount,c.scoreStatus";
            String tableStr=" com.fortune.rms.business.content.model.Content c";
            String whereStr=" 1=1 ";
            long bigPoster = getRequestIntParam("bigPoster",-1);

            if(bigPoster > 0){
                whereStr += " and (c.post2Url is not null) ";
            }
            if(cspId>1||status>=0){
                fieldStr +=",cc.id,cc.cspId,cc.status,cc.statusTime,sp.name ";
                tableStr +=",com.fortune.rms.business.content.model.ContentCsp cc," +
                        "com.fortune.rms.business.csp.model.Csp sp ";
                whereStr +=" and c.id=cc.contentId and cc.cspId = sp.id ";
                if(cspId>1){
                    whereStr +=" and cc.cspId = "+cspId;
                }
                if(status>=0){
                    whereStr +=" and cc.status="+status;
                }
                if(isSpecialExit>0){
                    whereStr +=" and c.isSpecial="+isSpecialExit;
                }
            }
            String sqlTable = "select " +
                     fieldStr+
                    " from " + tableStr+
                    " where"+whereStr;
            long channelId = getRequestIntParam("channelId", -1);
            if(channelId>0){
                List list = channelLogicInterface.getSonChannelId(channelId);
                String channelIds= ""+channelId+",";
                if(list.size()>0){
                    for(Object o :list){
                        Long channelId1 = StringUtils.string2long(o.toString(),-1);
                        channelIds += channelId1+",";
                        List list1 = channelLogicInterface.getSonChannelId(channelId1);
                        if(list.size()>0){
                            for(Object o1 :list1){
                                channelIds += o1.toString()+",";
                            }
                        }
                    }
                }
                channelIds = channelIds.substring(0,channelIds.length()-1);
                if (channelIds.length() > 0) {
                    sqlTable += " and c.id in(select ch.contentId from com.fortune.rms.business.content.model.ContentChannel ch where ch.channelId in  (" + channelIds + ")) ";
                }
            }

            long visitCountStatus = getRequestIntParam("visitCountStatus",-1);
            if(visitCountStatus>0){
                    sqlTable += " and c.visitCountStatus >= " + visitCountStatus;
            }

            long scoreStatus = getRequestIntParam("scoreStatus",-1);
            if(scoreStatus>0){
                    sqlTable += " and c.scoreStatus >= " + scoreStatus;
            }

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * add by mlwang @2014-10-16
     * ��ȡý�����ϸ��Ϣ������������Ϣ�����ơ�����������*2��ʱ�䡢��顢ģ��
     * Ҳ����������Ŀ�������û���
     * ���ļ��б�
     */
    @Action(value = "/getContent")
    public void getContentDetail(){
        RedexContentDetail contentDetail = new RedexContentDetail();

        // ������Ϣ
        Content content = contentLogicInterface.get(obj.getId());
        if(content != null ){
            // ������Ŀ
            List<Channel> channelList = contentChannelLogicInterface.getChannelsByContentId(obj.getId());
            // �û�����Ϣ�ݴ���content��property6�У��ö��ŷָ�
            List<ContentProperty> propertyList = contentPropertyLogicInterface.getContentPropertiesByDataType(content.getModuleId(), obj.getId(),
                    PropertyLogicInterface.DATA_TYPE_MP4, false);
            List<EncoderTemplate> templateList = encoderTemplateLogicInterface.getAll();
            // stringValue url name name intValue����˳��

            // ��content���Ը�����Ӧ�� contentDetail�ֶ�
            contentDetail.setId( content.getId() );
            contentDetail.setName( content.getName() );
            contentDetail.setActor( content.getActors() );
            contentDetail.setCreateTime( content.getCreateTime() );
            contentDetail.setActivityTime( content.getProperty3() );
            contentDetail.setIntro( content.getIntro() );
            contentDetail.setModuleId( content.getModuleId() );
            contentDetail.setDeviceId( content.getDeviceId() );
            contentDetail.setPoster( content.getPost1Url() );
            contentDetail.setBigPoster( content.getPost2Url() );
            contentDetail.setUserType( content.getUserTypes() );    // �û�����
            // ƴ��Ƶ��
            String channelIds = "";
            for(Channel ch : channelList){
                channelIds += "".equals(channelIds)? ch.getId() : "," +ch.getId();
            }
            contentDetail.setChannels(channelIds);

            // ���ļ���Ϣ�Ž�ȥ
            // added by mlwang��ֻ���һ�ֱ���
            Long encoderPropId = -1L;
            if(templateList != null && templateList.size() > 0){
                 encoderPropId = ((EncoderTemplate)templateList.get(0)).getPropertyId();
            }
            for(ContentProperty prop : propertyList){
                if( encoderPropId > 0 && !encoderPropId.equals(prop.getPropertyId())) continue;
                RedexContentFile f = new RedexContentFile(prop.getId(), prop.getIntValue(), prop.getName(), prop.getStringValue());
                contentDetail.addContentFile(f);
            }
            contentDetail.sortFiles();
        }

        directOut(contentDetail.getObjJson());

        //return ActionSupport.SUCCESS;
    }

    /**
     * add by mlwang @2014-10-16
     * ������еĴ�����Ƶ�����б�
     */
    @Action(value = "/unaudit")
    public void getUnAuditContentList(){
        Long channelId = Long.parseLong(getRequestParam("channelId", "-1"));
        String searchValue = getRequestParam("search", "");
        List<RedexAuditContent> l = contentLogicInterface.getUnAudtiContent(channelId, searchValue,pageBean);

        directOut(JsonUtils.getListJsonString("objs", l, "totalCount", pageBean.getRowCount()));
        //return ActionSupport.SUCCESS;
    }

    /**
     * ��ȡ��ǰ����Ա�������쳣���񣬰�����˲�ͨ����ת��ʧ�ܵ�����
     */
    @Action(value = "/abnormal")
    public void getAbnormalContent(){

        String output;
        if(session.get(Constants.SESSION_ADMIN) != null){
            Admin admin = (Admin)session.get(Constants.SESSION_ADMIN);
            List<ContentAbnormal> list = contentLogicInterface.getAbnormalContentList(admin.getId());
            output = JsonUtils.getListJsonString("contents", list, "totalCount", list.size());
        }else{
            output = "{\"success\":false,\"error\":\"��¼�ѹ��ڣ������µ�¼\"}";
        }

        directOut(output);
    }

    /**
     * add by mlwang @2014-10-23
     * ��ѯ������Ƶ����
     */
    private Date startTime;
    private Date stopTime;

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Action(value = "/getList")
    public void getContentList(){
        log.debug(pageBean.getOrderBy());
        Long channelId = Long.parseLong(getRequestParam("channelId", "-1"));
        String searchValue = getRequestParam("search", "");
        List<Content> l = contentLogicInterface.getContentList(channelId, searchValue, startTime, stopTime, pageBean);

        directOut(JsonUtils.getListJsonString("objs", l, "totalCount", pageBean.getRowCount()));
        //return ActionSupport.SUCCESS;
    }

    /**
     * @return "modified"
     * �޸���Ƶý����Ϣ������������Ϣ����������Ŀ���û�����
     */
    @Action(value = "/modify",results = {
            @Result(name = "modified",location = "/pub/toList.jsp")
    },interceptorRefs = {@InterceptorRef(value = "defaultStack")})
    public String modifyContent(){
        log.debug("conent id:" + obj.getId());
        session.put("redirect_index", destPage);

        // ���溣���ʹ󺣱�
        String posterPath = "";
        String bigPosterPath = "";
        try{
            posterPath = savePoster(false);
            bigPosterPath = savePoster(true);
        }catch(Exception e){ }

        // ����id��ȡ����
        Content content = contentLogicInterface.get(obj.getId());
        if(content != null){
            content.setName( obj.getName() );
            content.setActors( obj.getActors() );
            content.setProperty3( obj.getProperty3() );
            content.setIntro( obj.getIntro() );
            content.setModuleId( obj.getModuleId() );
            content.setProperty5( obj.getProperty5() );
            String userTypes = obj.getUserTypes();
            if( userTypes != null && !userTypes.endsWith(",")){
                userTypes += ",";
            }
            if( userTypes != null && !userTypes.startsWith(",")){
                userTypes = "," + userTypes;
            }
            content.setUserTypes( userTypes );
            if( !"".equals(posterPath) ){
                content.setPost1Url(posterPath);
            }
            if( !"".equals(bigPosterPath) ){
                content.setPost2Url(bigPosterPath);
            }

            // ���ԭ������˱��ܾ��������ύ���
            if( ContentLogicInterface.STATUS_AUDIT_REJECTED.equals(content.getStatus()) ){
                content.setStatus( ContentLogicInterface.STATUS_WAITING_FOR_AUDIT);
            }

            // ����ý����Ϣ
            content = contentLogicInterface.save(content);

            writeSysLog("�޸���Ƶ��" + content.getName() + "��Ϣ");

            // ������Ŀ��Ϣ
            log.debug("channel:" + content.getProperty5());
            contentLogicInterface.checkPublishChannels(content,content.getProperty5());
        }
        CacheUtils.clearAll();
        return "modified";
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

    /**
     * added by mlwang @2014-11-7
     * ��������Ա�ɼ�����Ŀ�������ݣ������root���ǾͿ����е�
     */
    @Action(value = "/getChannelList")
    public String adminChannelContentList(){
        Admin admin = (Admin)session.get(Constants.SESSION_ADMIN);

        try {
            SearchCondition searchCondition = new SearchCondition();
            int isSpecialExit=getRequestIntParam("isSpecialExit",0);
            String fieldStr="c.id,c.name,c.actors,c.directors,c.creatorAdminId,c.createTime,c.cspId," +
                    "c.moduleId,c.deviceId,c.status,c.statusTime," +
                    "c.digiRightUrl,c.validStartTime,c.validEndTime,c.allVisitCount,c.monthVisitCount,c.visitCountStatus," +
                    "c.weekVisitCount,c.intro,c.post1Url,c.post2Url,c.score,c.scoreCount,c.scoreStatus";
            String tableStr=" com.fortune.rms.business.content.model.Content c";
            // mod by mlwang @2014-11-7��ֻ��ѯ�����ܹ������Ŀ
            long creatorId = ((Admin)session.get(Constants.SESSION_ADMIN)).getId();
            String whereStr= "";
            // ֻ��ʾ�ȴ�ת�롢����ת�롢�ȴ���ˡ����ߺ�����״̬������
            whereStr += " (c.status in(" + ContentLogicInterface.STATUS_WAITING_FOR_ENCODE +
                    "," + ContentLogicInterface.STATUS_ENCODING +
                    "," + ContentLogicInterface.STATUS_WAITING_FOR_AUDIT +
                    "," + ContentLogicInterface.STATUS_CP_ONLINE +
                    "," + ContentLogicInterface.STATUS_CP_OFFLINE + "))";
            String sqlTable = "select " +
                     fieldStr+
                    " from " + tableStr+
                    " where"+whereStr;
            long channelId = getRequestIntParam("channelId", -1);
            if(channelId>0){
                List list = channelLogicInterface.getSonChannelId(channelId);
                String channelIds= ""+channelId+",";
                if(list.size()>0){
                    for(Object o :list){
                        Long channelId1 = StringUtils.string2long(o.toString(),-1);
                        channelIds += channelId1+",";
                        List list1 = channelLogicInterface.getSonChannelId(channelId1);
                        if(list.size()>0){
                            for(Object o1 :list1){
                                channelIds += o1.toString()+",";
                            }
                        }
                    }
                }
                channelIds = channelIds.substring(0,channelIds.length()-1);
                if (channelIds.length() > 0) {
                    sqlTable += " and c.id in(select ch.contentId from com.fortune.rms.business.content.model.ContentChannel ch where ch.channelId in  (" + channelIds + ")) ";
                }
            }

            if(admin.getIsRoot() != 1){
                sqlTable += " and c.id in ( select cc.contentId from " +
                    "ContentChannel cc where cc.channelId in(" +
                    "select ac.channelId from AdminChannel ac" +
                    " where ac.adminId=" + admin.getId() + "))";
            }

            String[][] params = new String[][]{
                    {"c.cspId", "", "=", ""},
                    {"c.status", "", "=", ""},
                    {"c.name", "", "like", "%?%"},
                    {"c.createTime", "startDate", ">=", "00:00:00"},
                    {"c.createTime", "endDate", "<=", "23:59:59"}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String getPicContent() {
        List<Content> list = contentLogicInterface.getPicContent(474431645,503754004);//��ȡͨ�������ͼƬ����
        String result = "{\"success\":\"true\",\"contents\":"+ JsonUtils.getJsonString(list)+"}";
        directOut(result);
        return  null;
    }
    public void getContentByStatus(){
        log.debug(pageBean.getOrderBy());
        String searchValue = getRequestParam("search", "");
        List<Content> l = contentLogicInterface.getContentsByStatus(status,searchValue,startTime,stopTime,pageBean);

        directOut(JsonUtils.getListJsonString("objs", l, "totalCount", pageBean.getRowCount()));
    }

    private  String channelIds;

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public String getContentsOfChannelIds() {
        String result = "";
        if(channelIds != null) {
            Map<String,List<Content>> map = contentLogicInterface.getContentsOfChannelIds(channelIds,pageBean);
            result = "{\"success\":\"true\",\"data\":"+ JsonUtils.getJsonString(map)+"}";
        }

        directOut(result);
        return null;
    }
}
