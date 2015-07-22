package com.fortune.rms.web.encoder;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Namespace("/encoder")
@ParentPackage("default")
@Action(value="encoderTask")
@Results({
        @Result(name = "searchTask",location = "/encoder/searchTaskJson.jsp")
})
public class EncoderTaskAction extends BaseAction<EncoderTask> {

    public EncoderTaskAction() {
        super(EncoderTask.class);
    }

   private EncoderTaskLogicInterface encoderTaskLogicInterface;

    public void setEncoderTaskLogicInterface(EncoderTaskLogicInterface encoderTaskLogicInterface) {
        this.encoderTaskLogicInterface = encoderTaskLogicInterface;
        setBaseLogicInterface(encoderTaskLogicInterface);
    }


    private List tasks;
    private String contentName;
    private Date beginDay;
    private Date endDay;

    public String searchTask(){
        Integer status = obj.getStatus();
        Long cspId = null;
        if(admin!=null){
            if(admin.getCspId()!=null){
                cspId = admin.getCspId().longValue();
                if(cspId<=1L){//如果是超级用户，id是1，必须将其设置成小于0的数才不会影响后续的搜索
                    cspId = -1L;
                }
            }
        }

        tasks = encoderTaskLogicInterface.searchTask(obj.getName(),obj.getSourceFileName(),contentName,
                 obj.getEncoderId(),obj.getTemplateId(),cspId,status,beginDay,endDay,pageBean);
        return "searchTask";
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String checkTask(){
        objs = encoderTaskLogicInterface.checkTasksOfClip(obj.getClipId());
        if(objs!=null){

            for(EncoderTask task:objs){

            }
        }
        return "list";
    }
    private List<EncoderTask> encodeTasks;
    public String setTaskStatus(){
        if(keyId!=null&&keyId>0){
            try {
                Integer status=obj.getStatus();
                obj = encoderTaskLogicInterface.get(StringUtils.string2long(keyId.toString(),-1));
                if(obj!=null){
                    obj.setStatus(status);
                    encoderTaskLogicInterface.save(obj);
                }
//                encoderTaskLogicInterface.updateLog(obj,"设置状态："+EncoderTaskLogicInterface.);
                setSuccess(true);
                addActionMessage("命令处理完毕，任务《" +obj.getName()+
                        "》进入等待状态！");
            } catch (Exception e) {
                String logInfo = "发生异常："+e.getMessage();
                log.error(logInfo);
                setSuccess(false);
                addActionError(logInfo);
            }
        }else{
            setSuccess(false);
            addActionError("没有合适的启动参数，keyId="+keyId);
        }
        return "view";
    }

    public String restart(){
        obj.setStatus(EncoderTaskLogicInterface.STATUS_WAITING);
        setTaskStatus();
        String logInfo;
        if(isSuccess()){
            logInfo = "重新启动任务";
            encoderTaskLogicInterface.updateLog(obj,logInfo);
        }else{
            logInfo = "重新启动任务失败！";
        }
        writeSysLog(logInfo+","+obj.getName());
        return "view";
    }

    public String cancel(){
        obj.setStatus(EncoderTaskLogicInterface.STATUS_CANCEL);
        setTaskStatus();
        String logInfo;
        if(isSuccess()){
            logInfo = "取消任务排队";
            encoderTaskLogicInterface.updateLog(obj,logInfo);
        }else{
            logInfo = "取消任务失败！";
        }
        writeSysLog(logInfo+","+obj.getName());
        return "view";
    }

    public String startTask(){
        String logInfo = "";
        String userLog = "";
        if(encodeTasks!=null){
            for(EncoderTask task:encodeTasks){
                if(task==null){
                    continue;
                }
                Long clipId = task.getClipId();
                Long templateId = task.getTemplateId();
                long id = task.getId();
                task = encoderTaskLogicInterface.createTaskForTemplate(id,clipId,templateId);
                String msg = "创建";
                if(id>0){
                    msg = "更新";
                }
                if(task==null){
                    msg +="任务失败：clipId="+clipId+","+templateId;
                }else{
                    msg +="任务成功："+task.getName();
                }
                logInfo +=msg+"\r\n";
                userLog +=msg+"<br/>";
            }
            if("".equals(logInfo)){
                setSuccess(false);
                logInfo = "试图创建转码任务时发生异常，没有任何输入参数，输入数组长度为0！";
            }
        }else{
            setSuccess(false);
            logInfo = "试图创建转码任务时发生异常，没有任何输入参数，输入数组为空！！";
        }
        writeSysLog(logInfo);
        if(isSuccess()){
            addActionMessage(userLog);
        }else{
            addActionError(logInfo);
        }
        return "success";
    }
    public String getTaskJson(){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(Object o:tasks){
            if(o instanceof Object[]){
                Object[] allData=(Object[])o;
                Map<String,Object> row = new HashMap<String, Object>();
                for(Object data:allData){
                    if(data instanceof EncoderTask){
                        EncoderTask task = (EncoderTask) data;
                        row.put("id",task.getId());
                        row.put("sourceFileName",task.getSourceFileName());
                        row.put("desertFileName",task.getDesertFileName());
                        row.put("encoderId",task.getEncoderId());
                        row.put("startTime",StringUtils.date2string(task.getStartTime()));
                        row.put("stopTime", StringUtils.date2string(task.getStopTime()));
                        row.put("status",task.getStatus());
                        row.put("process",task.getProcess());
                        row.put("clipId",task.getClipId());
                        row.put("name",task.getName());
                    }else if(data instanceof Device){
                        Device device = (Device) data;
                        row.put("deviceName",device.getName());
                    }else if(data instanceof Content){
                        Content content = (Content) data;
                        row.put("contentName",content.getName());
                    }else if(data instanceof EncoderTemplate){
                        EncoderTemplate template = (EncoderTemplate) data;
                        row.put("templateName",template.getTemplateName());
                    }
                }
                result.add(row);
            }
            
        }
        return JsonUtils.getListJsonString("objs",result,"totalCount",pageBean.getRowCount());
    }
    public String getTaskJsonCanOrder(){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(Object o:tasks){
            if(o instanceof Object[]){
                Object[] allData=(Object[])o;
                Map<String,Object> row = new HashMap<String, Object>();
                for(Object data:allData){
                    if(data instanceof EncoderTask){
                        EncoderTask task = (EncoderTask) data;
                        row.put("et_DOT_id",task.getId());
                        row.put("et_DOT_sourceFileName",task.getSourceFileName());
                        row.put("et_DOT_desertFileName",task.getDesertFileName());
                        row.put("et_DOT_encoderId",task.getEncoderId());
                        row.put("et_DOT_startTime",StringUtils.date2string(task.getStartTime()));
                        row.put("et_DOT_stopTime", StringUtils.date2string(task.getStopTime()));
                        row.put("et_DOT_status",task.getStatus());
                        row.put("et_DOT_process",task.getProcess());
                        row.put("et_DOT_clipId",task.getClipId());
                        row.put("et_DOT_name",task.getName());
                    }else if(data instanceof Device){
                        Device device = (Device) data;
                        row.put("d_DOT_name",device.getName());
                    }else if(data instanceof Content){
                        Content content = (Content) data;
                        row.put("c_DOT_name",content.getName());
                    }else if(data instanceof EncoderTemplate){
                        EncoderTemplate template = (EncoderTemplate) data;
                        row.put("t_DOT_templateName",template.getTemplateName());
                    }
                }
                result.add(row);
            }

        }
        return JsonUtils.getListJsonString("objs",result,"totalCount",pageBean.getRowCount());
    }

    public List<EncoderTask> getEncodeTasks() {
        return encodeTasks;
    }

    public void setEncodeTasks(List<EncoderTask> encodeTasks) {
        this.encodeTasks = encodeTasks;
    }

    public Date getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(Date beginDay) {
        this.beginDay = beginDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }
}
