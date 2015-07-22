package com.fortune.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-24
 * Time: 上午10:09
 * 批处理执行一系列的任务
 */
public class BatchExecutor {
    private static BatchExecutor ourInstance = new BatchExecutor();
    private Map<String,BatchWorker> workers;
    public static BatchExecutor getInstance() {
        return ourInstance;
    }

    private BatchExecutor() {
         workers = new HashMap<String,BatchWorker>();
    }

    public BatchWorker getWorker(String name,int threadPoolSize){
        BatchWorker worker = workers.get(name);
        if(worker == null){
            worker = new BatchWorker(name,threadPoolSize);
            workers.put(name,worker);
        }
        return worker;
    }

    public void shutdown(){
        for(BatchWorker worker:workers.values()){
            worker.shutdown();
        }
    }

    public Map<String, BatchWorker> getWorkers() {
        return workers;
    }

    public void setWorkers(Map<String, BatchWorker> workers) {
        this.workers = workers;
    }
}
