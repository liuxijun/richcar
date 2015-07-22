package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.encoder.dao.daoInterface.EncoderTaskDaoInterface;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.tools.ant.types.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-28
 * Time: ����11:54
 * ת��������
 */
@Service("encoderTaskLogicInterface")
public class EncoderTaskLogicImpl  extends BaseLogicImpl<EncoderTask> implements EncoderTaskLogicInterface {
    private EncoderTaskDaoInterface encoderTaskDaoInterface;
    private EncoderTemplateLogicInterface encoderTemplateLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private SystemLogLogicInterface systemLogLogicInterface;
    private ContentLogicInterface contentLogicInterface;

    @Autowired
    public void setEncoderTemplateLogicInterface(EncoderTemplateLogicInterface encoderTemplateLogicInterface) {
        this.encoderTemplateLogicInterface = encoderTemplateLogicInterface;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    @SuppressWarnings("unchecked")
    @Autowired
    public void setEncoderTaskDaoInterface(EncoderTaskDaoInterface encoderTaskDaoInterface) {
        this.encoderTaskDaoInterface = encoderTaskDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.encoderTaskDaoInterface;
    }

    @Autowired
    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    @Autowired
    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
        this.systemLogLogicInterface = systemLogLogicInterface;
    }

    public EncoderTask startTask(Content content,ContentProperty clip, long encoderId,long streamServerId,long templateId) {
        EncoderTemplate template = encoderTemplateLogicInterface.get(templateId);
        //Device encoder = deviceLogicInterface.get(encoderId);
        EncoderTask task = new EncoderTask();
        task.setProcess(0);
        task.setStatus(EncoderTaskLogicInterface.STATUS_WAITING);
        task.setTemplateId(templateId);
        task.setTemplate(template);
        task.setClipId(clip.getId());
        task.setEncoderId(encoderId);
        task.setStreamServerId(streamServerId);
        task.setStartTime(new Date());
        String sourceFileName = clip.getStringValue();
        task.setName(content.getName()+"-"+clip.getName()+"-"+template.getTemplateName());
        int p = sourceFileName.lastIndexOf(".");
        String desertFileName = sourceFileName;
        if(p>0){
            //  ͳһ������
            desertFileName = StringUtils.date2string(new Date(),"/yyyy/MM/dd/yyyyMMddHHmmss_"+clip.getContentId()+"_"+clip.getId());//sourceFileName.substring(0,p);
        }
        desertFileName +="."+template.getTemplateCode()+"."+template.getFileFormat();
        task.setDesertFileName(desertFileName);
        task.setSourceFileName(sourceFileName);
        task = save(task);
        //����󷵻ص�task��û��template����
        task.setTemplate(template);
        //EncodeTaskManager.getInstance().startEncodeTask(task);
        return task;
    }
    public boolean urlEquals(String url0,String url1){
        if(url0==null||url1==null)return false;
        File f0 = new File(url0);
        File f1 = new File(url1);
        return f0.getAbsolutePath().equals(f1.getAbsolutePath());
    }
    public String updateLog(EncoderTask task,String logInfo){
        return updateLog(task,logInfo,false);
    }

    public String updateLog(EncoderTask task,String logInfo,boolean updateStatus){
        String oldLog = task.getEncodeLog();
        if(oldLog==null){
            oldLog = "";
        }else{
            oldLog +="\r\n";
        }
        oldLog+=StringUtils.date2string(new Date())+"-"+logInfo;
        int len = oldLog.length();
        if(len>2000){
            oldLog = oldLog.substring(len-2001);
        }
        task.setEncodeLog(oldLog);
        encoderTaskDaoInterface.updateLog(task,updateStatus);
        return oldLog;
    }
    public void taskFinished(EncoderTask task) {
        if(!EncoderTaskLogicInterface.STATUS_FINISHED.equals(task.getStatus())){
            String logInfo = "����ʧ�ܣ����ܽ��к���������";
            logger.debug(logInfo);
            updateLog(task,logInfo,false);
            setContentStatusByClipId(task.getClipId(), ContentLogicInterface.STATUS_ENCODE_ERROR);
            return;
        }
        EncoderTemplate template = task.getTemplate();
        if(template==null){
            String logInfo = "û��ģ����Ϣ���޷����ý��";
            logger.debug(logInfo);
            updateLog(task,logInfo,false);
            return;
        }

        try {
            AppConfigurator  config = AppConfigurator.getInstance();
            ContentProperty cp = null;
            try {
                cp = contentPropertyLogicInterface.get(task.getClipId());
                if(cp!=null&&!urlEquals(task.getSourceFileName(),cp.getStringValue())){
                    //clipId�ڱ�������˵�����������������
                    String logInfo = ("clipId��EncoderTask���ɡ���������˱仯��������Ч����������������source.url="+cp.getStringValue()+",task.url="+task.getSourceFileName()+")");
                    logger.error(logInfo);
                    updateLog(task,logInfo,false);
                    cp = null;
                }
            } catch (Exception e) {
                String logInfo = ("contentPropertyû���ҵ���id="+task.getClipId()+",������ģ��������Ѱ��stringValue="+task.getSourceFileName()+"��ӰƬ��");
                logger.error(logInfo);
                updateLog(task,logInfo,false);
            }
            if(cp == null){
                ContentProperty searchBean = new ContentProperty();
                searchBean.setStringValue(task.getSourceFileName());
                List<ContentProperty> clips = contentPropertyLogicInterface.search(searchBean);
                if(clips!=null&&clips.size()>0){
                    //������������ֵ��/movie/dir1/dir2/movie.mp4
                    //�����ݿ�����/encode/movie/dir1/dir2/movie.mp4
                    //Ҫ��������ֵ���˵�
                    String v1 = task.getSourceFileName();
                    for(ContentProperty clip:clips){
                        String v0 = clip.getStringValue();
                        if(urlEquals(v0,v1)){
                            cp = clip;
                            break;
                        }
                    }
                }
            }
            if(cp==null||cp.getContentId()==null||cp.getContentId()<=0){
                String logInfo = ("û���ҵ�Ƭ����Ϣ���ǳ���Ǹ�����ܼ���������contentProperty="+cp);
                logger.error(logInfo);
                task.setStatus(STATUS_CLIP_LOST);
                updateLog(task, "\r\n" + logInfo, true);
                save(task);
                return ;
            }
            //������û��ԭ���ģ�������ͬ��������ͬ�ģ������滻����
            ContentProperty clip = new ContentProperty();
            clip.setContentId(cp.getContentId());
            clip.setIntValue(cp.getIntValue());
            clip.setPropertyId(template.getPropertyId());
            if(clip.getPropertyId()==null){
                clip.setPropertyId(1L);
            }
            if(clip.getIntValue()==null){
                clip.setIntValue(1L);
            }
            if(clip.getContentId()==null){
                clip.setContentId(1L);
            }
            List<ContentProperty> oldClips = contentPropertyLogicInterface.getContentProperties(clip.getContentId(),
                    clip.getPropertyId(),clip.getIntValue());
            if(oldClips!=null&&oldClips.size()>0){
                clip = oldClips.get(0);
                String logInfo = ("�����оɵ����ݴ��ڣ����Խ�������������"+clip.getName()+","+clip.getStringValue()+"->"+task.getDesertFileName());
                logger.warn(logInfo);
                updateLog(task,logInfo,false);
                //�����ģ�ɾ�˰�
                for(int i=1 ,l=oldClips.size();i<l;i++){
                    ContentProperty oldCp = oldClips.get(i);
                    logInfo = ("����ɾ����������ӣ���Щ�����ظ��ˣ�"+oldCp.getName()+","+oldCp.getStringValue());
                    logger.warn(logInfo);
                    updateLog(task,logInfo,false);
                    //��contentId��intValue��ת������������ݣ���ֹ��������
                    oldCp.setContentId(0-oldCp.getContentId());
                    Long intValue = oldCp.getIntValue();
                    if(intValue == null || intValue == 0){
                        intValue = -1000L;
                    }else{
                        intValue = 0-intValue;
                    }
                    oldCp.setIntValue(intValue);
                    contentPropertyLogicInterface.save(oldCp);
                    //contentPropertyLogicInterface.remove(oldClips.get(i));
                }
            }else{
                try {
                    BeanUtils.copyProperties(clip,cp);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage());
                } catch (InvocationTargetException e) {
                    logger.error(e.getMessage());
                }
                clip.setId(-1);
            }
            clip.setStringValue(task.getDesertFileName());
            clip.setPropertyId(template.getPropertyId());
            String clipName = clip.getName();
            Content content = contentLogicInterface.get(cp.getContentId());
            if(clipName == null||"".equals(clipName.trim())||"null".equals(clipName.trim())){
                clipName = cp.getName();
                if(clipName == null||"".equals(clipName)||"null".equals(clipName.trim())){
                    //ֻ�����е�ת����������˲Ż�����content����һ��״̬����һ��״̬���������ߣ�Ҳ�����Ǵ���
                    clipName = content.getName()+",��"+cp.getIntValue()+"��"+":"+template.getTemplateName();
                }else if(config.getBoolConfig("encoder.whenFinished.addClipNameWithTemplateName",true)){
                    clipName +=":"+template.getTemplateName();
                }
                clip.setName(clipName);
            }
            clip.setExtraData(template.getVWidth()+"X"+template.getVHeight()+","+StringUtils.formatBPS((template.getABitrate()+template.getVBitrate())*1024));
            Integer length = task.getLength();
            if(length!=null&&length>0){
                clip.setLength(length.longValue());
            }
            clip.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_ENCODE_FINISHED);
            clip = contentPropertyLogicInterface.save(clip);


            if(config.getBoolConfig("system.compactMode",false)){
                //��������
                int unFinishedCount = encoderTaskDaoInterface.getUnFinishedTaskCountOfContent(content.getId());
                int allTaskCount =  encoderTaskDaoInterface.getTaskCountOfContent(content.getId(),-1);
                contentLogicInterface.onEncodeTaskFinished(content,allTaskCount,unFinishedCount);
            }
        } catch (Exception e) {
            String logInfo = "û���ҵ�Դͷ���޷��������к�������:"+e.getMessage();
            updateLog(task,logInfo,false);
            logger.error(logInfo);
        }
    }

    public boolean startEncoder(String sourceFile,Long encodeTemplateId){

        return true;
    }
    public EncoderTask createTaskForTemplate(Long clipId,Long templateId){
        return createTaskForTemplate(-1L,clipId,templateId);
    }
    public EncoderTask createTaskForTemplate(Long taskId,Long clipId,Long templateId){
        if(clipId==null||clipId<=0||templateId==null||templateId<=0){
            return null;
        }
        EncoderTask task = null;
        if(taskId>0){
            try {
                task = get(taskId);
            } catch (Exception e) {
                logger.error("�޷���ȡ��IDΪ"+taskId+"�ı�������");
                task = new EncoderTask();
            }
        }
        try {
            if(task==null){
                task = new EncoderTask(-1L);
            }
            ContentProperty cp = contentPropertyLogicInterface.get(clipId);
            Content content = contentLogicInterface.get(cp.getContentId());
            EncoderTemplate template = encoderTemplateLogicInterface.get(templateId);
            return createTaskForTemplate(content,cp,task,template);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("����ת������ʱ�����쳣��"+e.getMessage());
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public EncoderTask createTaskForTemplate(Content content,ContentProperty contentProperty,EncoderTask task,EncoderTemplate template){
        List<Device> encoders = (List<Device>) CacheUtils.get("all","",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                Device encoder = new Device();
                encoder.setType(DeviceLogicInterface.DEVICE_TYPE_ENCODER);
                encoder.setStatus((long)(DeviceLogicInterface.DEVICE_ONLINE));
                return deviceLogicInterface.search(encoder);
            }
        });
        int encoderCount = encoders.size();
        int encoderIndex =(int) Math.round(Math.random()*(encoderCount-1));
        if(encoderIndex<0){
            encoderIndex=0;
        }
        if(encoderIndex>=encoderCount){
            encoderIndex = encoderCount-1;
        }
        Device encoder = encoders.get(encoderIndex);
        String sourceFileName = contentProperty.getStringValue();
        if(sourceFileName==null){
            return null;
        }
        sourceFileName = sourceFileName.replace('\\','/');
        String desertFileName = HzUtils.getFullSpell(sourceFileName);
        String desertPath = FileUtils.extractFilePath(desertFileName,"/");
        String desertName = FileUtils.extractFileName(desertFileName,"/");

        logger.debug("�ļ������˺�Ϊ��"+sourceFileName+"->"+desertFileName+",path="+desertPath+",name="+desertName);
        desertFileName = desertPath+"/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_"+Math.round(Math.random()*100000)+"_"+desertName;
        //ֻ�����ļ����е���26����ĸ�����֣����ߣ����ţ��㣬������ȫ��ȥ��
        String tempFileName = "";
        for(int i=0,l=desertFileName.length();i<l;i++){
            char ch = desertFileName.charAt(i);
            if((ch>='0'&&ch<='9')||(ch>='A'&&ch<='Z')||(ch>='a'&&ch<='z')||ch=='.'||ch=='_'||ch=='-'||ch=='/'||ch=='\\'){
                tempFileName+=ch;
            }else{
                tempFileName+='_';
            }
        }
        desertFileName = tempFileName;
        logger.debug("����������쳣�ַ��������:"+desertFileName);
        //EncoderTask task = new EncoderTask();
        if(task.getId()>0){
            String oldLog = task.getEncodeLog();
            if(oldLog==null){
                oldLog = "";
            }else{
                oldLog+="\r\n";
            }
            task.setEncodeLog(
                    oldLog+StringUtils.date2string(new Date())+"-��������");
        }else{
            task.setEncodeLog(StringUtils.date2string(new Date())+"-��������");
        }
        int i=desertFileName.lastIndexOf(".");
        if(i>0){
            desertFileName = desertFileName.substring(0,i)+"."+template.getTemplateCode()+"."+template.getFileFormat();
        }else{
            desertFileName = desertFileName+"."+template.getTemplateCode()+"."+template.getFileFormat();
        }
        i=0;
        while(desertFileName.startsWith("/")&&desertFileName.length()>1){
            desertFileName = desertFileName.substring(1);
            i++;
            if(i>10){
                break;
            }
        }
        desertFileName = "/encode/"+desertFileName;
        i=0;
        while(desertFileName.contains("//")){
            desertFileName = desertFileName.replace("//","/");
            i++;
            if(i>5){
                break;
            }
        }
        logger.debug("Ŀ���ļ�����������쳣�ַ��������:"+desertFileName);
        Device sourceServer = (Device)CacheUtils.get(content.getDeviceId(),"allDevices",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                try {
                    return deviceLogicInterface.get((Long)key);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        logger.debug("�������ڵ�Դ��������"+sourceServer.getName());
        task.setClipId(contentProperty.getId());
        task.setEncoderId(encoder.getId());
        task.setDesertFileName(desertFileName);
        task.setSourceFileName(sourceFileName);
        Long length = contentProperty.getLength();
        task.setLength(length==null?-1:length.intValue());
        task.setProcess(0);
        task.setStatus(EncoderTaskLogicInterface.STATUS_WAITING);
        task.setEncoder(encoder);
        task.setTemplate(template);
        task.setTemplateId(template.getId());
        task.setStreamServerId(content.getDeviceId());
        task.setStartTime(new Date());
        String name = contentProperty.getName();
        if(name==null||"".equals(name.trim())|| StringUtils.string2int(name,-1)!=-1||"��ƵԴ��ַ".equals(name)){
            name = content.getName();
        }else{
            name = content.getName()+","+name;
        }
        name = template.getTemplateName()+"��"+name+"(" +contentProperty.getIntValue()+
                ")��ת������";
        task.setName(name);
        if(sourceServer!=null){
            String fileName = sourceServer.getLocalPath()+"/"+sourceFileName;
            File sourceFile = new File(fileName);
            if(sourceFile.exists()&&!sourceFile.isDirectory()){
                task.setFileDate(new Date(sourceFile.lastModified()));
                task.setFileSize(sourceFile.length());
            }
        }
        task = save(task);
        contentProperty.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_HAS_ENCODED);
        contentPropertyLogicInterface.save(contentProperty);
        return task;
    }

    public List<EncoderTask> createEncoderTasksForAllTemplate(Content content, ContentProperty contentProperty) {
        List<EncoderTemplate> templates = encoderTemplateLogicInterface.getTemplatesOfModule(content.getModuleId());
        List<EncoderTask> tasks = new ArrayList<EncoderTask>(templates.size());
        //DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
        logger.debug("����"+templates.size()+"��ģ�棬��Ҫ������Ӧ��ת�룡");
        for(EncoderTemplate template:templates){
            EncoderTask bean = new EncoderTask(-1);
            bean.setClipId(contentProperty.getId());
            bean.setTemplateId(template.getId());
            List<EncoderTask> oldTasks = search(bean);
            if(oldTasks!=null&&oldTasks.size()>0){
                boolean hasThisTaskAlready = false;
                for(EncoderTask task:oldTasks){
                    Integer status = task.getStatus();
                    if(STATUS_RUNNING.equals(status)||STATUS_FINISHED.equals(status)||STATUS_WAITING.equals(status)){
                        hasThisTaskAlready = true;
                    }
                }
                if(hasThisTaskAlready){
                    logger.warn("���ת������:" +content.getName()+","+contentProperty.getName()+","+template.getTemplateName()+
                            "���Ѿ����ˣ��������ظ����");
                    continue;
                }
            }
            EncoderTask task = createTaskForTemplate(content,contentProperty,new EncoderTask(-1),template);
            tasks.add(task);
            logger.debug("ģ��"+template.getTemplateName()+"���񴴽����:"+task.getName()+":"+task.getId());
        }
        return tasks;
    }

    public List searchTask(String taskName,String sourceFileName, String contentName, Long encoderId,
                           Long templateId,Long cspId,Integer status,Date beginDate,Date endDate,PageBean pageBean) {
        return encoderTaskDaoInterface.searchTask(taskName,sourceFileName,contentName,encoderId,templateId,cspId,
                status,beginDate,endDate, pageBean);
    }

    /**
     * �������е�ת����Ϣ����������û��������ת��
     * @param clipId ��ǰԴ
     * @return ����б�
     */
    public List<EncoderTask> checkTasksOfClip(Long clipId) {
        List<EncoderTask> result = new ArrayList<EncoderTask>();
        if(clipId==null||clipId<=0){
            return result;
        }
        try {
            ContentProperty cp = contentPropertyLogicInterface.get(clipId);
            Content content = contentLogicInterface.get(cp.getContentId());
            List<EncoderTemplate> templates = encoderTemplateLogicInterface.getTemplatesOfModule(content.getModuleId());
            EncoderTask task = new EncoderTask();
            task.setClipId(clipId);
            List<EncoderTask> tasks = search(task,new PageBean(1,100,"o1.startTime","desc"));
            long id = -1;
            for(EncoderTemplate template:templates){
                boolean foundData = false;
                for(EncoderTask aTask:tasks){
                   if(aTask.getTemplateId().equals(template.getId())){
                       foundData = true;
                       result.add(aTask);
                       break;
                   }
                }
                if(!foundData){
                    task = new EncoderTask(id--);
                    task.setTemplateId(template.getId());
                    task.setClipId(clipId);
                    task.setName(template.getTemplateName()+","+content.getName()+","+cp.getName()+",��"+cp.getIntValue()+"����");
                    task.setStatus(EncoderTaskLogicInterface.STATUS_NOT_EXISTS);
                    result.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("���Լ��ת�����ʱ�����쳣��"+e.getMessage());
        }
        return result;
    }
    public List<EncoderTask> getTasks(Long encoderId,Long templateId,Integer status,PageBean pageBean){
        return getTasks(encoderId,null,templateId,status,pageBean);
    }
    public List<EncoderTask> getTasks(Long encoderId,Long streamServerId,Long templateId,Integer status,PageBean pageBean){
        EncoderTask bean = new EncoderTask();
        bean.setEncoderId(encoderId);
        bean.setTemplateId(templateId);
        bean.setStatus(status);
        bean.setStreamServerId(streamServerId);
        if(pageBean==null){
            pageBean = new PageBean();
        }
        if(pageBean.getOrderBy()==null){
            pageBean.setOrderBy("o1.startTime");
            pageBean.setOrderDir("asc");
        }
        List<EncoderTask> result =  search(bean,pageBean);
        for(EncoderTask task :result){
            try {
                EncoderTemplate template = (EncoderTemplate) CacheUtils.get(task.getTemplateId(),"templateCache",new DataInitWorker(){
                    public Object init(Object key,String cacheName){
                        return encoderTemplateLogicInterface.get((Long)key);
                    }
                });
                task.setTemplate(template);
                Long taskEncoderId = task.getEncoderId();
                if(taskEncoderId!=null){
                    Device encoder = (Device) CacheUtils.get(taskEncoderId,"deviceCache",new DataInitWorker(){
                        public Object init(Object key,String cacheName){
                            return deviceLogicInterface.get((Long) key);
                        }
                    });
                    task.setEncoder(encoder);
                }
                task.setStreamServer((Device) CacheUtils.get(task.getStreamServerId(),"deviceCache",new DataInitWorker(){
                    public Object init(Object key,String cacheName){
                        return deviceLogicInterface.get((Long) key);
                    }
                }));
            } catch (Exception e) {
                logger.error("ת����������������⣺"+task.getName());
            }
        }
        return result;
    }
    public List<EncoderTask> getWaitingTasks(Long encoderId,Long streamServerId, Long templateId, PageBean pageBean){
        List<EncoderTask> result=null;
        boolean hasErrorTasks = true;
        while(hasErrorTasks){
            hasErrorTasks = false;
            result = getTasks(encoderId,streamServerId,templateId,STATUS_WAITING,pageBean);
            if(result!=null&&result.size()>0){
                //��ɨ��һ���Լ���������У��������ظ�������Ļ������޳��������
                for(int i=result.size()-1;i>=0;i--){
                    EncoderTask task0 = result.get(i);
                    Long clipId0= task0.getClipId();
                    Long templateId0=task0.getTemplateId();
                    if(clipId0==null||clipId0<=0||templateId0==null||templateId0<=0){
                        //���������⣬�����������
                        result.remove(task0);
                        hasErrorTasks=true;
                        task0.setStatus(STATUS_CLIP_LOST);
                        updateLog(task0,"\r\n"+StringUtils.date2string(new Date())+" - ���������д���clipId<=0����templateId<=0",true);
                        //save(task0);
                        continue;
                    }
                    for(int j=i-1;j>=0;j--){
                        EncoderTask task1 = result.get(j);
                        Long clipId1=task1.getClipId();
                        Long templateId1=task1.getTemplateId();
                        if(clipId1==null||clipId1<=0||templateId1==null||templateId1<=0){
                            //���������⣬�����������
                            result.remove(task1);
                            i--;
                            hasErrorTasks=true;
                            task1.setStatus(STATUS_CLIP_LOST);
                            updateLog(task1,"\r\n"+StringUtils.date2string(new Date())+" - ���������д���clipId<=0����templateId<=0",true);
                            //save(task1);
                            continue;
                        }
                        if(templateId0.equals(templateId1)&&clipId0.equals(clipId1)){
                            task1.setStatus(STATUS_DUMP_TASK);
                            task1.setStopTime(new Date());
                            updateLog(task1,"\r\n"+StringUtils.date2string(new Date())+" - ���ִ���������������:" +task0.getName()+",id="+task0.getId()+
                                    " �ظ�������ִ�С�",true);
                            //save(task1);
                            result.remove(task1);
                            hasErrorTasks=true;
                            i--;
                        }
                    }
                }
            }
            if(result!=null&&result.size()>0){
                for(int i=result.size()-1;i>=0;i--){
                    EncoderTask task=result.get(i);
                    if(checkDumpTask(task)){
                        hasErrorTasks=true;
                        result.remove(task);
                    }
                }
            }
            if(hasErrorTasks){
                logger.error("���ظ����ߴ����������֣�������½���������");
            }
        }
        return  result;
    }
    public List<EncoderTask> getWaitingTasks(Long encoderId, Long templateId, PageBean pageBean) {
        return getWaitingTasks(encoderId,-1L,templateId,pageBean);
    }

    public List<EncoderTask> getRunningTasks(Long encoderId, Long templateId) {
        PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,null,null);
        return getTasks(encoderId,-1L,templateId,STATUS_RUNNING,pageBean);
    }
    //����Ƿ����ظ�������������ظ������񣬲��Ҹ�����û�б��ɹ�ִ�У��Ͱ������ظ�����������Ϊ�ظ�״̬��������true�����򷵻�false
    public boolean checkDumpTask(EncoderTask task){
        boolean  result = false;
        List<EncoderTask> dumpTasks = getDumpTasks(task);
        if(dumpTasks!=null&&dumpTasks.size()>0){
            //result = true;
            for(EncoderTask dumpTask:dumpTasks){
                Integer taskStatus = dumpTask.getStatus();
                if(STATUS_WAITING.equals(taskStatus)){
                    //���״̬�ǵȴ������������������ȴ�����Ҫ��������task����ִ��
                    dumpTask.setStatus(STATUS_DUMP_TASK);
                    dumpTask.setStopTime(new Date());
                    dumpTask.setEncodeLog(updateLog(dumpTask, "\r\n" + StringUtils.date2string(new Date()) + " - ���ִ���������������:" + task.getName() + ",id=" + task.getId() +
                            " �ظ�������ִ�С�", true));
                    //save(dumpTask);
                }else if(STATUS_RUNNING.equals(taskStatus)||STATUS_FINISHED.equals(taskStatus)){
                    //���״̬ʱ����ִ�л����Ѿ���ɣ���������˴�ִ��
                    task.setStatus(STATUS_DUMP_TASK);
                    task.setStopTime(new Date());
                    task.setEncodeLog(updateLog(task, "\r\n" + StringUtils.date2string(new Date()) + " - ���ִ���������������:" + dumpTask.getName() + ",id=" + dumpTask.getId() +
                            " �ظ�������ִ�С�", true));
                    //save(task);
                    result = true;
                }
            }
        }
        return result;
    }

    public List<EncoderTask> getDumpTasks(EncoderTask task){
        EncoderTask bean = new EncoderTask();
        Long clipId = task.getClipId();
        Long templateId = task.getTemplateId();
        if(clipId==null||templateId==null||clipId<=0||templateId<=0){
            return new ArrayList<EncoderTask>(0);
        }
        bean.setClipId(task.getClipId());
        bean.setTemplateId(task.getTemplateId());
        List<EncoderTask> result = search(bean);
        if(result!=null&&result.size()>0){
            //ȥ����������Լ�
            try {
                String sourceFileName0=task.getSourceFileName();
                if(sourceFileName0!=null){
                    sourceFileName0 = sourceFileName0.trim();
                }else{
                    sourceFileName0="";
                }
                for(int i=result.size()-1;i>=0;i--){
                    EncoderTask dumpTask = result.get(i);
                    if(dumpTask.getId()==task.getId()){
                        result.remove(dumpTask);
                    }else{
                        String sourceFileName = dumpTask.getSourceFileName();
                        //���Դ�ļ������仯������Ϊ�����ظ���������
                        if(sourceFileName!=null){
                            sourceFileName = sourceFileName.trim();
                        }
                        if(!sourceFileName0.equals(sourceFileName)){
                            result.remove(dumpTask);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("��ɾ���������Լ���ʱ�����쳣��"+e.getMessage());
            }
        }else{
            result = new ArrayList<EncoderTask>(0);
        }

        return result;
    }

    public EncoderTask onTaskStart(EncoderTask task){
        //����
        logger.debug("ת������"+task.getName()+"���Ѿ�������");
        task.setStartTime(new Date());
        task.setProcess(0);
        task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
        task = save(task);
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            setContentStatusByClipId(task.getClipId(),ContentLogicInterface.STATUS_ENCODING);
        }
        return task;
    }
    public Map<Integer, Integer> getTaskCount(){
        return getTaskCount(null,null,null);
    }
    public Map<Integer, Integer> getTaskCount(Long encoderId,Long streamServerId,Long cspId) {
        List<Object[]> countList = encoderTaskDaoInterface.getTaskCount(encoderId,streamServerId,cspId);
        Map<Integer,Integer> result = new HashMap<Integer,Integer>();
        for(Object[] row:countList){
            Integer status=StringUtils.string2int(row[0].toString(),-1);
            if(status>=0){
                Integer count = StringUtils.string2int(row[1].toString(),0);
                result.put(status,count);
            }
        }
        return result;
    }

    public Content setContentStatusByClipId(Long clipId,Long status){
        try {
            if(clipId==null||status==null){
                return null;
            }
            ContentProperty cp = contentPropertyLogicInterface.get(clipId);
            if(cp!=null){
                Content content = contentLogicInterface.get(cp.getContentId());
                if(content!=null&&!status.equals(content.getStatus())){
                    //������ǽ���ģʽ����ת��ʧ�ܺ���ʾ
                    boolean compactMode = AppConfigurator.getInstance().getBoolConfig("system.compactMode", false);
                    if(!compactMode){
                        String log = ("ת��״̬�����仯�ˣ�����Ҫ����ý�塶" +content.getName()+
                                "����״̬��"+contentLogicInterface.getStatusString(status)+"������ֻ�ܷ���������ԭ����״̬��"+
                                contentLogicInterface.getStatusString(content.getStatus()));
                        logger.warn(log);
                        systemLogLogicInterface.saveMachineLog(log);
                        return null;
                    }else{
                        String log = ("ת��״̬�����仯����������ý�塶" +content.getName()+
                                "����״̬��" +contentLogicInterface.getStatusString(content.getStatus())+
                                "��״̬��"+contentLogicInterface.getStatusString(status));
                        logger.debug(log);
                        systemLogLogicInterface.saveMachineLog(log);
                    }
                    content.setStatus(status);
                    return contentLogicInterface.save(content);
                }
            }
        } catch (Exception e) {
            logger.error("�޷��޸�ý�����״̬��"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public EncoderTask get(long id){
        EncoderTask result = super.get(id);
        if(result!=null){
            if(result.getTemplate()==null&&result.getTemplateId()!=null){
                result.setTemplate(encoderTemplateLogicInterface.get(result.getTemplateId()));
            }
        }
        return result;
    }
}
