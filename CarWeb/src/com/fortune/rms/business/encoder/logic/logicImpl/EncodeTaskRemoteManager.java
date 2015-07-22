package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.*;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-29
 * Time: ����9:21
 * �����������
 * ÿ��ϵͳ������������ʵ������Զ�˷����������������ע�ᡢ�������󡢱��롢���Ȼ㱨�������ʾ�����ߵȲ�����
 * �ڱ������У������ٷ������ݿ⡣
 */
@SuppressWarnings("unused")
public class EncodeTaskRemoteManager implements EncodeTaskManagerInterface {
    private static EncodeTaskRemoteManager ourInstance = new EncodeTaskRemoteManager();
    private List<EncodeWorker> workers = new ArrayList<EncodeWorker>();
    private Logger logger = Logger.getLogger(this.getClass());
    private  int workerId = 0;
    private Long encoderId;
    private int workerMaxCount = -1;
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static EncodeTaskRemoteManager getInstance() {
        return ourInstance;
    }
    public void resetWorkers(int workerCount){
        while(workers.size()<workerCount){
            String threadName = "encodeWorkerThread_"+workerId;
            logger.debug("�������������仯������߳�:"+threadName);
            EncodeWorker worker = new EncodeWorker(this);
            worker.setName(threadName);
            workerId++;
            //worker.startTask(requestTask());
            onWorkerIdle(worker);
            worker.start();
            workers.add(worker);
        }
        while(workers.size()>workerCount){
            EncodeWorker worker = workers.get(0);
            workers.remove(worker);
            logger.debug("�������������仯�������߳�:"+worker.getName());
            worker.stopWork();
        }
    }
    public void reloadConfig(){
        workerMaxCount = AppConfigurator.getInstance().getIntConfig("system.encode.workerMaxCount",4);
        resetWorkers(workerMaxCount);
    }

    private EncodeTaskRemoteManager() {
        sendServerMessage("startup",-1L,null);
        reloadConfig();
    }

    public void onThreadTerminated(EncodeWorker worker){
        if(workers.contains(worker)){
            workers.remove(worker);
        }
    }

    public synchronized void onTaskError(EncoderTask task){
        task.setStatus(EncoderTaskLogicInterface.STATUS_ERROR);
        sendServerMessage("error", task.getId(), "status=" + task.getStatus() + "&log=" + task.getEncodeLog());
    }

    public synchronized void onTaskStart(EncoderTask task){
        logger.debug("ת������"+task.getName()+"���Ѿ�������");
        task.setStartTime(new Date());
        task.setProcess(0);
        task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
        sendServerMessage("beforeStart", task.getId(), "status=" + task.getStatus() + "&log=" + task.getEncodeLog());
    }
    public synchronized void onTaskFinished(EncoderTask task){
        logger.debug(task.getName()+":"+task.getSourceFileName()+"-��"+task.getDesertFileName()+"������̽�����");
        Integer taskStatus = task.getStatus();
        if(EncoderTaskLogicInterface.STATUS_ERROR.equals(taskStatus)){

        }else{
                //ԭ�����߼��ƺ���Щ���⣬���û�д������Ϊ�ǳɹ��ˣ��ⲻ��ѧ���������ע����2013.10.28 17��11��by xjliu
//            task.setStatus(EncoderTaskLogicInterface.STATUS_FINISHED);
//            task.setProcess(100);
        }
        if(EncoderTaskLogicInterface.STATUS_FINISHED.equals(taskStatus)){
            task.setProcess(100);
        }
        task.setStopTime(new Date());
        sendServerMessage("finished",task.getId(),"status="+task.getStatus());
    }

    public synchronized void onTaskProcess(EncoderTask task){
        task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
        sendServerMessage("process",task.getId(),"process="+task.getProcess());
    }

    public void onSetLength(EncoderTask task,int length, Date fileDate, long fileSize) {
        sendServerMessage("setDuration",task.getId(),"duration="+length+"&date="+fileDate.getTime()+"&size="+fileSize);
    }

    public void startEncodeTask(EncoderTask task){

    }
    public EncoderTask requestTask(){
        String workInfo = sendServerMessage("get",-1L,"");
        if(workInfo==null){
            logger.error("�ڳ�����������ʱ��������û�з��غ��ʵ����ݣ�");
            return null;
        }
        workInfo = workInfo.trim();
        if("".equals(workInfo)){
            logger.error("�ڳ�����������ʱ��������û�з��غ��ʵ����ݣ�");
            return null;
        }
        //��ȡ����������Ϣ��һ��JSON��ʽ����
        try {
            JSONObject result = JsonUtils.getJsonObj(workInfo);
            int resultCode = result.getInt("result");
            if(resultCode == 200){
                JSONObject task = result.getJSONObject("task");
                if(task!=null){
                    try {
                        EncoderTask encoderTask =(EncoderTask) JSONObject.toBean(task,EncoderTask.class);
                        if(encoderTask!=null){
                            return encoderTask;
                        }
                    } catch (Exception e) {
                        logger.error("��ʽ������ʱ�������쳣��"+e.getMessage()+",JSON�����ǣ�"+task.toString());
                        e.printStackTrace();
                    }
                }else{
                    logger.error("û�л�ȡ������");
                }
            //}else{
            //   logger.debug("�������˷����Ľ�����룺"+resultCode+",û�к��ʵ�����ַ�����");
            }
        } catch (Exception e) {
            logger.error("�޷����л�json:"+workInfo);
            e.printStackTrace();
        }
        return null;
    }
    //�����������û�������ȡ����5����֮�ڲ�����������
    private long lastRequestNullTaskTime = 0L;
    public synchronized void onWorkerIdle(EncodeWorker worker){
        long now = System.currentTimeMillis();
        int maxWaitingSeconds = AppConfigurator.getInstance().getIntConfig("system.encoder.maxWaitingSecondsWhenTaskNull",30);
        if(now-lastRequestNullTaskTime>maxWaitingSeconds*1000L){
            EncoderTask task = requestTask();
            if(task!=null){
                worker.startTask(task);
            }else{
                lastRequestNullTaskTime = now;
            }
        }else{
/*
            logger.debug("�����ϴ�����ʱ�仹��������ʱ�䣬��ˢ������"+
                    StringUtils.date2string(lastRequestNullTaskTime)+",��ȥ��"+
                    (now-lastRequestNullTaskTime)/1000L+"�룬���"
                    +maxWaitingSeconds+"��");
//*/
        }
    }

    public void onSystemStartup() {
       logger.debug("Զ������ʽת�����ģ��������");
    }

    public List<EncodeWorker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<EncodeWorker> workers) {
        this.workers = workers;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }
    
    public void shutdown(){
        sendServerMessage("shutdown",-1L,null);
        for(int i=0,l=workers.size()-1;i<=l;l--){
            EncodeWorker worker=workers.get(l);
            worker.stopWork();
        }
        scheduledExecutorService.shutdown();
        scheduledExecutorService.shutdownNow();
    }

    public synchronized String updateLog(EncoderTask task,String logInfo){
        return sendServerMessage("log",task.getId(),"log="+logInfo);
    }

    public String sendServerMessage(String command,Long id,String parameters){
        ServerMessager messager = new ServerMessager();
        String serverUrl = AppConfigurator.getInstance().getConfig("system.encoder.masterUrl","http://192.168.10.5/interface/encoderSession.jsp");
        if(serverUrl.contains("?")){
            if(!serverUrl.endsWith("&")){
                serverUrl+="&";
            }
        }else{
            serverUrl+="?";
        }
        String result = messager.postToHost(serverUrl+"command="+command+"&taskId="+id,parameters,"UTF-8");
        if(!"get".equals(command)){
            logger.debug("�����������ָ�" +command+"��taskId=" +id+
                    "������������" +parameters+
                    "�����������͹����Ľ����\n"+result);
        }
        return result;
    }

    public void startup(){

    }
    public static void main(String[] args){
        EncodeTaskRemoteManager instance = EncodeTaskRemoteManager.getInstance();
        while(true){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
