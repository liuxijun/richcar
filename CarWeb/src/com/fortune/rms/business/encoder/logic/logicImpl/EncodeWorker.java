package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.AppConfigurator;
import com.fortune.util.MediaUtils;
import com.fortune.util.MediaUtilsCallback;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-29
 * Time: ����9:26
 * ���빤��ִ����
 */
public class EncodeWorker extends  Thread implements MediaUtilsCallback{
    private EncodeTaskManagerInterface manager;
    private EncoderTask task;
    private boolean working = true;
    private  Logger logger = Logger.getLogger(this.getClass());

    public void onLog(String log){
        manager.updateLog(task,log);
    }

    public EncodeWorker(EncodeTaskManagerInterface manager){
        this.manager = manager;

    }
    
    public void startTask(EncoderTask task){
        this.task = task;
    }

    public void run(){
        logger.debug("���������߳�����");
        AppConfigurator config = AppConfigurator.getInstance();
        while(working){
            if(task!=null){
                //��������
                MediaUtils mu = new MediaUtils();
                EncoderTemplate template = task.getTemplate();
                if(template!=null){
                    Device device = task.getStreamServer();
                    //��linuxƽ̨�ϲ���������ֱ��new File(*).getAbsolutePath()��ȡĿ¼����Ϊ����
                    // �ᵼ�·�б�ܿ�ͷ�Ĺ���ʽĿ¼�������⣬û�취��ȡ���ļ�
                    String sourceFileName = task.getSourceFileName();
                    String desertFileName = task.getDesertFileName();
                    if(device!=null&&device.getLocalPath()!=null){
                        sourceFileName = device.getLocalPath()+"/"+sourceFileName;
                        desertFileName = device.getExportPath()+"/"+desertFileName;
                    }
                    String logInfo = ("����"+task.getName()+"������:"+sourceFileName+"->"+desertFileName);
                    logger.debug(logInfo);
                    manager.updateLog(task,logInfo);
                    manager.onTaskStart(task);
                    int videoBitrate =  template.getVBitrate().intValue();
                    String profile = "baseline";
                    int level = 21;
                    if(videoBitrate>=650){
                        profile = "high_profile";
                        level = 31;
                    }else if(videoBitrate>=450){
                        profile = "main_profile";
                        level = 30;
                    }
                    int result = MediaUtils.ERROR_UNKNOWN;
                    int tryTimes = 0;
                    while(true){
                        try {
                            result = mu.encode(sourceFileName,desertFileName,task.getName(),template.getVEncoderType(),profile,level,
                                    template.getVBitrate().intValue(),
                                    template.getVKeyframeInterval().intValue(),template.getVFrameRate().intValue(),
                                    template.getVWidth().intValue(),template.getVHeight().intValue(),
                                    template.getACodec(),template.getABitrate().intValue(),
                                    template.getASampleRate().intValue(),
                                    template.getAChannel().intValue(),0,
                                    config.getIntConfig("system.encoder.maxTime",72000)
                                    ,this,false,template.getFileFormat());
                        } catch (Exception e) {
                            String errorLog = "ת������з����쳣��"+e.getMessage();
                            logger.error(errorLog);
                            manager.updateLog(task,errorLog);
                            e.printStackTrace();
                        }
                        tryTimes++;
                        manager.updateLog(task,"��" +tryTimes+
                                "�ν���ת������ִ�н����"+MediaUtils.errorCodes.get(result));
                        //���ת��ʧ�ܣ�����5�Σ���κ����
                        if(tryTimes>5){
                            break;
                        }
                        if(result==MediaUtils.SUCCESS
                                ||result==MediaUtils.ERROR_FILE_NOT_EXISTS
                                ||result==MediaUtils.ERROR_FILE_SIZE_TOO_SMALL
                                ||result == MediaUtils.ERROR_SKIP){
                            break;
                        }
                    }

                    int status;
                    if(result==MediaUtils.ERROR_FILE_NOT_EXISTS){
                        status = EncoderTaskLogicInterface.STATUS_NOT_EXISTS;
                    }else{
                        status = (result == MediaUtils.SUCCESS||result==MediaUtils.ERROR_SKIP)?
                                EncoderTaskLogicInterface.STATUS_FINISHED:result;
                    }
                    //manager.updateLog(task,mu.getLastLog());
                    task.setStatus(status);
                }else{
                    String logInfo =("����"+task.getName()+"��ʧ��:������ϢΪ�գ��޷����б��������");
                    logger.error(logInfo);
                    task.setStatus(EncoderTaskLogicInterface.STATUS_ERROR);
                    manager.updateLog(task,logInfo);
                    manager.onTaskError(task);
                }
                //mu.encodeMedia()
                manager.onTaskFinished(task);
                task = null;
            }else{
                int maxSleepTimes = AppConfigurator.getInstance().getIntConfig("system.encoder.threadIdleWaitingSeconds",1);
                while(maxSleepTimes>=0){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        logger.debug("��˯������");
                    }
                    if(canWorkNow()){
                        break;
                    }
                    maxSleepTimes--;
                }
                manager.onWorkerIdle(this);
            }
        }
        logger.debug("���������߳�ֹͣ");
        manager.onThreadTerminated(this);
    }
    public boolean canWorkNow(){
        return task != null;
    }
    public void stopWork(){
       working = false;
    }

    public void breakTask(){

    }

    public EncoderTask getTask(){
        return task;
    }

    public void onProcess(int process,int pos,int size,int cpu,int memory){
        if(task==null){
            logger.error("Task is NULL!!Can't set Process data now!");
            return;
        }
        int oldProcess = task.getProcess();
        if(process!=oldProcess){//����1%�Ľ��ȵ�����֪ͨ
            //logger.debug("������ȣ�"+task.getDesertFileName()+":"+task.getProcess()+"%");
            task.setProcess(process);
            manager.onTaskProcess(task);
        }
    }

    public void onSetLength(int length,Date fileDate,long fileSize){
        task.setFileDate(fileDate);
        task.setFileSize(fileSize);
        task.setLength(length);
        manager.onSetLength(task,length,fileDate,fileSize);
    }
}
