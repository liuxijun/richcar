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
 * Time: 上午11:54
 * 转码任务处理
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
            //  统一用日期
            desertFileName = StringUtils.date2string(new Date(),"/yyyy/MM/dd/yyyyMMddHHmmss_"+clip.getContentId()+"_"+clip.getId());//sourceFileName.substring(0,p);
        }
        desertFileName +="."+template.getTemplateCode()+"."+template.getFileFormat();
        task.setDesertFileName(desertFileName);
        task.setSourceFileName(sourceFileName);
        task = save(task);
        //保存后返回的task会没有template属性
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
            String logInfo = "编码失败，不能进行后续操作！";
            logger.debug(logInfo);
            updateLog(task,logInfo,false);
            setContentStatusByClipId(task.getClipId(), ContentLogicInterface.STATUS_ENCODE_ERROR);
            return;
        }
        EncoderTemplate template = task.getTemplate();
        if(template==null){
            String logInfo = "没有模版信息，无法添加媒体";
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
                    //clipId在保存后发生了调整，必须重新搜索
                    String logInfo = ("clipId在EncoderTask生成、保存后发生了变化，不再有效！必须重新搜索（source.url="+cp.getStringValue()+",task.url="+task.getSourceFileName()+")");
                    logger.error(logInfo);
                    updateLog(task,logInfo,false);
                    cp = null;
                }
            } catch (Exception e) {
                String logInfo = ("contentProperty没有找到：id="+task.getClipId()+",将进行模糊搜索，寻找stringValue="+task.getSourceFileName()+"的影片。");
                logger.error(logInfo);
                updateLog(task,logInfo,false);
            }
            if(cp == null){
                ContentProperty searchBean = new ContentProperty();
                searchBean.setStringValue(task.getSourceFileName());
                List<ContentProperty> clips = contentPropertyLogicInterface.search(searchBean);
                if(clips!=null&&clips.size()>0){
                    //可能有这样的值，/movie/dir1/dir2/movie.mp4
                    //但数据库里有/encode/movie/dir1/dir2/movie.mp4
                    //要把这样的值过滤掉
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
                String logInfo = ("没有找到片短信息！非常抱歉！不能继续操作！contentProperty="+cp);
                logger.error(logInfo);
                task.setStatus(STATUS_CLIP_LOST);
                updateLog(task, "\r\n" + logInfo, true);
                save(task);
                return ;
            }
            //看看有没有原来的，属性相同，集数相同的，将其替换掉！
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
                String logInfo = ("发现有旧的数据存在，尝试进行数据修正："+clip.getName()+","+clip.getStringValue()+"->"+task.getDesertFileName());
                logger.warn(logInfo);
                updateLog(task,logInfo,false);
                //其他的，删了吧
                for(int i=1 ,l=oldClips.size();i<l;i++){
                    ContentProperty oldCp = oldClips.get(i);
                    logInfo = ("正在删除错误的连接，这些链接重复了："+oldCp.getName()+","+oldCp.getStringValue());
                    logger.warn(logInfo);
                    updateLog(task,logInfo,false);
                    //将contentId和intValue反转，保留这个数据，防止出现问题
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
                    //只有所有的转码任务都完成了才会设置content的下一步状态。下一步状态可能是上线，也可能是待审
                    clipName = content.getName()+",第"+cp.getIntValue()+"集"+":"+template.getTemplateName();
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
                //发布上线
                int unFinishedCount = encoderTaskDaoInterface.getUnFinishedTaskCountOfContent(content.getId());
                int allTaskCount =  encoderTaskDaoInterface.getTaskCountOfContent(content.getId(),-1);
                contentLogicInterface.onEncodeTaskFinished(content,allTaskCount,unFinishedCount);
            }
        } catch (Exception e) {
            String logInfo = "没有找到源头，无法继续进行后续操作:"+e.getMessage();
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
                logger.error("无法获取到ID为"+taskId+"的编码任务！");
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
            logger.error("创建转码任务时发生异常："+e.getMessage());
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

        logger.debug("文件名过滤后为："+sourceFileName+"->"+desertFileName+",path="+desertPath+",name="+desertName);
        desertFileName = desertPath+"/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_"+Math.round(Math.random()*100000)+"_"+desertName;
        //只保留文件名中的下26个字母，数字，划线，减号，点，其他的全部去掉
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
        logger.debug("处理掉各种异常字符，结果是:"+desertFileName);
        //EncoderTask task = new EncoderTask();
        if(task.getId()>0){
            String oldLog = task.getEncodeLog();
            if(oldLog==null){
                oldLog = "";
            }else{
                oldLog+="\r\n";
            }
            task.setEncodeLog(
                    oldLog+StringUtils.date2string(new Date())+"-重启任务");
        }else{
            task.setEncodeLog(StringUtils.date2string(new Date())+"-创建任务");
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
        logger.debug("目标文件处理掉各种异常字符，结果是:"+desertFileName);
        Device sourceServer = (Device)CacheUtils.get(content.getDeviceId(),"allDevices",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                try {
                    return deviceLogicInterface.get((Long)key);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        logger.debug("任务所在的源服务器："+sourceServer.getName());
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
        if(name==null||"".equals(name.trim())|| StringUtils.string2int(name,-1)!=-1||"视频源地址".equals(name)){
            name = content.getName();
        }else{
            name = content.getName()+","+name;
        }
        name = template.getTemplateName()+"《"+name+"(" +contentProperty.getIntValue()+
                ")》转码任务";
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
        logger.debug("共有"+templates.size()+"个模版，需要进行相应的转码！");
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
                    logger.warn("这个转码任务:" +content.getName()+","+contentProperty.getName()+","+template.getTemplateName()+
                            "，已经有了，不用再重复添加");
                    continue;
                }
            }
            EncoderTask task = createTaskForTemplate(content,contentProperty,new EncoderTask(-1),template);
            tasks.add(task);
            logger.debug("模版"+template.getTemplateName()+"任务创建完毕:"+task.getName()+":"+task.getId());
        }
        return tasks;
    }

    public List searchTask(String taskName,String sourceFileName, String contentName, Long encoderId,
                           Long templateId,Long cspId,Integer status,Date beginDate,Date endDate,PageBean pageBean) {
        return encoderTaskDaoInterface.searchTask(taskName,sourceFileName,contentName,encoderId,templateId,cspId,
                status,beginDate,endDate, pageBean);
    }

    /**
     * 返回所有的转码信息，包括可能没有启动的转码
     * @param clipId 当前源
     * @return 结果列表
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
                    task.setName(template.getTemplateName()+","+content.getName()+","+cp.getName()+",第"+cp.getIntValue()+"集，");
                    task.setStatus(EncoderTaskLogicInterface.STATUS_NOT_EXISTS);
                    result.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("尝试检查转码情况时发生异常："+e.getMessage());
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
                logger.error("转码任务的配置有问题："+task.getName());
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
                //先扫描一遍自己的这个队列，发现有重复的任务的话，就剔除这个任务
                for(int i=result.size()-1;i>=0;i--){
                    EncoderTask task0 = result.get(i);
                    Long clipId0= task0.getClipId();
                    Long templateId0=task0.getTemplateId();
                    if(clipId0==null||clipId0<=0||templateId0==null||templateId0<=0){
                        //数据有问题，跳过这个数据
                        result.remove(task0);
                        hasErrorTasks=true;
                        task0.setStatus(STATUS_CLIP_LOST);
                        updateLog(task0,"\r\n"+StringUtils.date2string(new Date())+" - 任务数据有错误，clipId<=0或者templateId<=0",true);
                        //save(task0);
                        continue;
                    }
                    for(int j=i-1;j>=0;j--){
                        EncoderTask task1 = result.get(j);
                        Long clipId1=task1.getClipId();
                        Long templateId1=task1.getTemplateId();
                        if(clipId1==null||clipId1<=0||templateId1==null||templateId1<=0){
                            //数据有问题，跳过这个数据
                            result.remove(task1);
                            i--;
                            hasErrorTasks=true;
                            task1.setStatus(STATUS_CLIP_LOST);
                            updateLog(task1,"\r\n"+StringUtils.date2string(new Date())+" - 任务数据有错误，clipId<=0或者templateId<=0",true);
                            //save(task1);
                            continue;
                        }
                        if(templateId0.equals(templateId1)&&clipId0.equals(clipId1)){
                            task1.setStatus(STATUS_DUMP_TASK);
                            task1.setStopTime(new Date());
                            updateLog(task1,"\r\n"+StringUtils.date2string(new Date())+" - 发现此任务与其他任务:" +task0.getName()+",id="+task0.getId()+
                                    " 重复，放弃执行。",true);
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
                logger.error("有重复或者错误的任务出现，因此重新进行搜索！");
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
    //检查是否是重复的任务，如果是重复的任务，并且该任务没有被成功执行，就把其他重复的内容设置为重复状态，并返回true，否则返回false
    public boolean checkDumpTask(EncoderTask task){
        boolean  result = false;
        List<EncoderTask> dumpTasks = getDumpTasks(task);
        if(dumpTasks!=null&&dumpTasks.size()>0){
            //result = true;
            for(EncoderTask dumpTask:dumpTasks){
                Integer taskStatus = dumpTask.getStatus();
                if(STATUS_WAITING.equals(taskStatus)){
                    //如果状态是等待，则不让这个任务继续等待。但要求来检查的task继续执行
                    dumpTask.setStatus(STATUS_DUMP_TASK);
                    dumpTask.setStopTime(new Date());
                    dumpTask.setEncodeLog(updateLog(dumpTask, "\r\n" + StringUtils.date2string(new Date()) + " - 发现此任务与其他任务:" + task.getName() + ",id=" + task.getId() +
                            " 重复，放弃执行。", true));
                    //save(dumpTask);
                }else if(STATUS_RUNNING.equals(taskStatus)||STATUS_FINISHED.equals(taskStatus)){
                    //如果状态时正在执行或者已经完成，则建议放弃此次执行
                    task.setStatus(STATUS_DUMP_TASK);
                    task.setStopTime(new Date());
                    task.setEncodeLog(updateLog(task, "\r\n" + StringUtils.date2string(new Date()) + " - 发现此任务与其他任务:" + dumpTask.getName() + ",id=" + dumpTask.getId() +
                            " 重复，放弃执行。", true));
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
            //去掉队列里的自己
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
                        //如果源文件发生变化，就认为不是重复的任务了
                        if(sourceFileName!=null){
                            sourceFileName = sourceFileName.trim();
                        }
                        if(!sourceFileName0.equals(sourceFileName)){
                            result.remove(dumpTask);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("在删除队列中自己的时候发生异常："+e.getMessage());
            }
        }else{
            result = new ArrayList<EncoderTask>(0);
        }

        return result;
    }

    public EncoderTask onTaskStart(EncoderTask task){
        //设置
        logger.debug("转码任务《"+task.getName()+"》已经启动！");
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
                    //如果不是紧凑模式，在转码失败后不提示
                    boolean compactMode = AppConfigurator.getInstance().getBoolConfig("system.compactMode", false);
                    if(!compactMode){
                        String log = ("转码状态发生变化了，本来要设置媒体《" +content.getName()+
                                "》到状态："+contentLogicInterface.getStatusString(status)+"，现在只能放弃！保持原来的状态："+
                                contentLogicInterface.getStatusString(content.getStatus()));
                        logger.warn(log);
                        systemLogLogicInterface.saveMachineLog(log);
                        return null;
                    }else{
                        String log = ("转码状态发生变化，所以设置媒体《" +content.getName()+
                                "》从状态：" +contentLogicInterface.getStatusString(content.getStatus())+
                                "到状态："+contentLogicInterface.getStatusString(status));
                        logger.debug(log);
                        systemLogLogicInterface.saveMachineLog(log);
                    }
                    content.setStatus(status);
                    return contentLogicInterface.save(content);
                }
            }
        } catch (Exception e) {
            logger.error("无法修改媒体编码状态："+e.getMessage());
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
