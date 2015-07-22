package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.SpringUtils;
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
 */
@SuppressWarnings("unused")
public class EncodeTaskManager implements EncodeTaskManagerInterface {
    private static EncodeTaskManager ourInstance = new EncodeTaskManager();
    private Queue<EncoderTask> willTasks = new LinkedList<EncoderTask>();
    private Queue<EncoderTask> workingTasks = new LinkedList<EncoderTask>();
    private List<EncodeWorker> workers = new ArrayList<EncodeWorker>();
    private EncoderTaskLogicInterface taskLogic;
    private DeviceLogicInterface deviceLogic;
    private Logger logger = Logger.getLogger(this.getClass());
    private  int workerId = 0;
    private Long encoderId;
    private int workerMaxCount = -1;
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static EncodeTaskManager getInstance() {
        return ourInstance;
    }
    public void resetWorkers(int workerCount){
        while(workers.size()<workerCount){
            String threadName = "encodeWorkerThread_"+workerId;
            logger.debug("�������������仯������߳�:"+threadName);
            EncodeWorker worker = new EncodeWorker(this);
            worker.setName(threadName);
            workerId++;
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
        if(encoderId!=null&&encoderId>0){
            try {
                Device encoder = deviceLogic.get(encoderId);
                Long maxTask = encoder.getMaxTask();
                if(maxTask!=null){
                    workerMaxCount = maxTask.intValue();
                }
            } catch (Exception e) {
                logger.error("�޷���ȡ��������Ϣ:"+e.getLocalizedMessage());
            }

        }
        if(workerMaxCount <=0){
            workerMaxCount = AppConfigurator.getInstance().getIntConfig("system.encode.workerMaxCount",4);
        }
        resetWorkers(workerMaxCount);
    }
    
    private EncodeTaskManager() {
        try {
            taskLogic = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface");
            deviceLogic = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface");
            //��������
            encoderId = (long)(AppConfigurator.getInstance().getIntConfig("system.encoder.encoderId",-1));  //snapMedia.jsp��Ҳ�õ������
            if(encoderId>0){
                logger.debug("�������ת������IDΪ��"+encoderId);
            }else{
                logger.debug("��������ת�����ã�ֱ���˳�");
                return;
            }
            //��������ִ�е�������ִ��������
            List<EncoderTask> runningTasks = taskLogic.getRunningTasks(encoderId,-1L);
            willTasks.clear();
            if(runningTasks!=null&&runningTasks.size()>0){
                logger.debug("��������Ѱ���ϴ�һֱ�����е����񣬼������������������"+runningTasks.size()+"����");
                for(EncoderTask runningTask:runningTasks){
                    logger.debug("��������"+runningTask.getName());
                    willTasks.offer(runningTask);
                }
                reloadConfig();
            }else{
                logger.debug("������û���ҵ���Ҫ���������ı�������");
            }
            Runnable scheduleTask = new Runnable() {
                public void run(){
                    try {
                        if(encoderId>0){
                            PageBean pageBean = new PageBean(0,workerMaxCount*2,"o1.startTime","asc");
                            List<EncoderTask> tasks = taskLogic.getWaitingTasks(encoderId,null,pageBean);
                            if(tasks!=null&&tasks.size()>0){
                              //  logger.debug("ˢ�������б�");
                                willTasks.clear();
                                for(EncoderTask task:tasks){
                              //      logger.debug("ˢ�£�"+task.getName());
                                    willTasks.offer(task);
/*
                                    if(taskLogic.checkDumpTask(task)){
                                        logger.warn("�������Ѿ���ִ�й������ظ�ִ�У�"+task.getName());
                                    }else{
                                    }
*/
                                }
                            }else{
                                //logger.debug("û������");
                            }
                        }else{
                            //logger.debug("encoderIdû�����ã�����ˢ�������б�");
                        }
                        reloadConfig();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            //ÿ��30�룬ˢ��һ�������б�
            scheduledExecutorService.scheduleWithFixedDelay(scheduleTask, 30, 30, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.debug("�޷���ʼ��encoderTaskLogicInterface!");
            taskLogic = null;
        }
        reloadConfig();
    }

    public void onThreadTerminated(EncodeWorker worker){
        if(workers.contains(worker)){
            workers.remove(worker);
        }
    }

    public synchronized void onTaskError(EncoderTask task){
        task.setStatus(EncoderTaskLogicInterface.STATUS_ERROR);
        taskLogic.save(task);
    }

    public synchronized void onTaskStart(EncoderTask task){
        taskLogic.onTaskStart(task);
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
        taskLogic.save(task);
        taskLogic.taskFinished(task);
        workingTasks.remove(task);
        if(workingTasks.size()<workerMaxCount){
            resetWorkers(workerMaxCount);
        }
    }

    public synchronized void onTaskProcess(EncoderTask task){
        task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
        taskLogic.save(task);
    }

    public void onSetLength(EncoderTask task,int length, Date fileDate, long fileSize) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startEncodeTask(EncoderTask task){
        willTasks.offer(task);
    }

    public synchronized void onWorkerIdle(EncodeWorker worker){
        if(!willTasks.isEmpty()){
            EncoderTask task = willTasks.poll();
            logger.debug("׼���������룺"+task.getName());
            worker.startTask(task);
/*
            task.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
            taskLogic.save(task);
*/
            workingTasks.offer(task);
        }
    }

    public void onSystemStartup() {
    }

    public Queue<EncoderTask> getWillTasks() {
        return willTasks;
    }

    public void setWillTasks(Queue<EncoderTask> willTasks) {
        this.willTasks = willTasks;
    }

    public Queue<EncoderTask> getWorkingTasks() {
        return workingTasks;
    }

    public void setWorkingTasks(Queue<EncoderTask> workingTasks) {
        this.workingTasks = workingTasks;
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
        for(int i=0,l=workers.size()-1;i<=l;l--){
            EncodeWorker worker=workers.get(l);
            worker.stopWork();
        }
        scheduledExecutorService.shutdown();
        scheduledExecutorService.shutdownNow();
    }

    public synchronized String updateLog(EncoderTask task,String logInfo){
        return taskLogic.updateLog(task,logInfo);
    }
}
