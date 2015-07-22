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
 * Time: 上午9:21
 * 编码任务管理
 * 每次系统启动后加载这个实例，向远端服务器发起请求进行注册、任务请求、编码、进度汇报、结果提示、下线等操作。
 * 在本进程中，将不再访问数据库。
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
            logger.debug("并发数量发生变化，添加线程:"+threadName);
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
            logger.debug("并发数量发生变化，减少线程:"+worker.getName());
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
        logger.debug("转码任务《"+task.getName()+"》已经启动！");
        task.setStartTime(new Date());
        task.setProcess(0);
        task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
        sendServerMessage("beforeStart", task.getId(), "status=" + task.getStatus() + "&log=" + task.getEncodeLog());
    }
    public synchronized void onTaskFinished(EncoderTask task){
        logger.debug(task.getName()+":"+task.getSourceFileName()+"-〉"+task.getDesertFileName()+"编码过程结束！");
        Integer taskStatus = task.getStatus();
        if(EncoderTaskLogicInterface.STATUS_ERROR.equals(taskStatus)){

        }else{
                //原来的逻辑似乎有些问题，如果没有错误就认为是成功了，这不科学，下面代码注释于2013.10.28 17：11，by xjliu
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
            logger.error("在尝试请求任务时服务器端没有返回合适的数据！");
            return null;
        }
        workInfo = workInfo.trim();
        if("".equals(workInfo)){
            logger.error("在尝试请求任务时服务器端没有返回合适的数据！");
            return null;
        }
        //获取到的任务信息是一个JSON格式数据
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
                        logger.error("格式化数据时发生了异常："+e.getMessage()+",JSON数据是："+task.toString());
                        e.printStackTrace();
                    }
                }else{
                    logger.error("没有获取到任务");
                }
            //}else{
            //   logger.debug("服务器端发来的结果代码："+resultCode+",没有合适的任务分发下来");
            }
        } catch (Exception e) {
            logger.error("无法序列化json:"+workInfo);
            e.printStackTrace();
        }
        return null;
    }
    //如果任务请求没有任务获取到，5分钟之内不再请求任务
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
            logger.debug("距离上次请求时间还不到警戒时间，不刷新任务："+
                    StringUtils.date2string(lastRequestNullTaskTime)+",过去了"+
                    (now-lastRequestNullTaskTime)/1000L+"秒，最大："
                    +maxWaitingSeconds+"秒");
//*/
        }
    }

    public void onSystemStartup() {
       logger.debug("远端请求方式转码服务模块启动！");
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
            logger.debug("像服务器发送指令：" +command+"，taskId=" +id+
                    "，参数包括：" +parameters+
                    "，服务器发送过来的结果：\n"+result);
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
