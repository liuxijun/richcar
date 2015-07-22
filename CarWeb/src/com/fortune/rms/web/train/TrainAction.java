package com.fortune.rms.web.train;

/**
 * Created by xjliu on 14-6-12.
 *
 */
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.train.logic.logicInterface.TrainLogicInterface;
import com.fortune.rms.business.train.model.Train;
import com.fortune.util.AppConfigurator;
import com.fortune.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Namespace("/train")
@ParentPackage("default")
@Action(value="train")
@Results({

})

public class TrainAction extends BaseAction<Train> {
    private static final long serialVersionUID = 5743534534534534l;
    private TrainLogicInterface trainLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    @SuppressWarnings("unchecked")
    public TrainAction() {
        super(Train.class);
    }
    /**
     * @param trainLogicInterface the trainLogicInterface to set
     */
    public void setTrainLogicInterface(TrainLogicInterface trainLogicInterface) {
        this.trainLogicInterface = trainLogicInterface;
        setBaseLogicInterface(trainLogicInterface);
    }

    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    private String fileName;
    private File uploadFile;
    private List<String> logs;
    private String importType;
    private Long deviceId;

    public String importLog(){
        String logFileName = null;
        if("uploadImport".equals(importType)){
           if(uploadFile!=null){
               if(fileName==null){
                   fileName="";
               }
               String importLogDir = AppConfigurator.getInstance().getConfig("import.baseFileDir","C:/logs/");
               File dstFile = new File(importLogDir+"/"+ StringUtils.date2string(new Date(),"yyyy/MM/dd/HHmmSS")
                       +"_"+fileName.replace('.','_').replace(' ','_')+Math.round(Math.random() * 1000000)+".log");
               File parentPath = dstFile.getParentFile();
               if(!parentPath.exists()){
                   if(!parentPath.mkdirs()){
                       log.error("无法创建目录："+parentPath.getAbsolutePath());
                   }
               }
               uploadFile.renameTo(dstFile);
               logFileName = dstFile.getAbsolutePath();
           }
        }else{
            if(fileName!=null){
                String realDir;
                if(AppConfigurator.getInstance().getBoolConfig("import.useSystemDir",true)||(deviceId==null||deviceId<=0)){
                    realDir = AppConfigurator.getInstance().getConfig("import.baseFileDir", "C:/logs/");
                }else{
                    Device device = deviceLogicInterface.get(deviceId);
                    realDir = device.getLocalPath()+"/";
                }
                logFileName = realDir+"/"+fileName ;
            }
        }
        logs = new ArrayList<String>();
        if(logFileName!=null){
            File logFile = new File(logFileName);
            if(logFile.exists()){
                List<String> result = trainLogicInterface.importLog(logFile);
                for(String r:result){
                    addActionMessage(r);
                }

            }else{
                addActionMessage("文件不存在：" + logFile.getAbsolutePath());
            }
        }else{
            addActionMessage("没有任何文件可以操作！");
        }
        setSuccess(true);
        return "success";
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getJsonLogs(){
        return getJsonArray(logs);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
