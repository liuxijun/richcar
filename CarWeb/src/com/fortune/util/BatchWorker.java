package com.fortune.util;

import org.apache.log4j.Logger;

import javax.annotation.security.RunAs;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-24
 * Time: 上午10:12
 * 执行某个系列工作
 */
public class BatchWorker {
    Logger logger = Logger.getLogger(this.getClass());
    private int count=0;
    private int finishedCount;
    private String name;
    private String log="";
    private long allDuration = 0;
    private long maxDuration = -1;
    private long minDuration = Integer.MAX_VALUE;
    private int threadPoolSize;
    private ExecutorService executor;
    private Date startTime;
    private Date stopTime;
    public BatchWorker(String name,int threadPoolSize){
        this.name = name;
        this.threadPoolSize = threadPoolSize;
        reset();
    }
    public void reset(){
        count = 0;
        finishedCount = 0 ;
        log = "";
        executor = Executors.newFixedThreadPool(threadPoolSize);
        startTime = new Date();
    }
    public boolean isTerminated(){
        try {
            return executor.awaitTermination(2, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("获取线程是否完成时发生错误："+e);
        }
        return executor.isTerminated();
    }
    public boolean isShutdown(){
        return executor.isShutdown();
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void execute(BatchRunnable runnable){
        count++;
        Runnable runner = new Runner(runnable);
        executor.execute(runner);
    }
    public synchronized void oneTaskFinished(Runner runner){
        finishedCount++;
        long duration = runner.getDuration();
        allDuration+=duration;
        if(maxDuration<duration){
            maxDuration = duration;
        }
        if(minDuration>duration){
            minDuration = duration;
        }
        log+=runner.getLog();
        stopTime = new Date();
    }
    public void shutdown(){
        List<Runnable> runnables = executor.shutdownNow();
        for(Runnable runnable:runnables){
            BatchRunnable batchRunnable = (BatchRunnable) runnable;
            batchRunnable.shutdownNow();
        }
        executor.shutdown();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(int finishedCount) {
        this.finishedCount = finishedCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public class Runner implements Runnable{
        BatchRunnable runnable;
        public Runner(BatchRunnable runnable){
           this.runnable = runnable;
        }

        public void run(){
            try {
                runnable.beforeStart();
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                logger.error("批处理执行时发生异常："+e.getMessage());
            }finally {
                runnable.afterFinished();
                oneTaskFinished(this);
            }
        }
        public String getLog(){
            return runnable.getLogs();
        }
        public long getDuration(){
            return runnable.getDuration();
        }
    }

    public long getAllDuration() {
        return allDuration;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public long getMinDuration() {
        return minDuration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }
}
