package com.fortune.util;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-1-19
 * Time: 17:19:14
 * 线程助手
 */
public class ThreadUtils {
    private static ThreadUtils ourInstance = new ThreadUtils();
    private Map<String,String> lockMap = new HashMap<String,String>();
    private Logger logger = Logger.getLogger(getClass());

    public static ThreadUtils getInstance(){
        return ourInstance;
    }

    public int acquire(String resourceName)throws Exception{
        return acquire(resourceName,10000,100);
    }

    public int acquire(String resourceName,int timeOut)throws Exception{
        return acquire(resourceName,timeOut,100);
    }

    /**
     * 信号灯处理
     * @param resourceName 信号灯名字
     * @param timeOut  超时时间，毫秒为单位
     * @param interval  检查间隔，毫秒为单位
     * @return    0，没有锁，只有当前线程。1，上过锁，等待过其他线程
     * @throws Exception  超时或参数错误
     */
    public int acquire(String resourceName,int timeOut,int interval)throws Exception{
        int result=0;
        if(resourceName == null){
            throw new Exception("ResourceName is empty!");
        }
        logger.debug("'"+resourceName+"'锁检查");
        if(lockMap.containsKey(resourceName)){
            logger.debug("'"+resourceName+"'锁存在！");
            result = 1;
        }else{
            logger.debug("'"+resourceName+"'锁不存在！");
        }
        while(timeOut>0 && lockMap.containsKey(resourceName)){
            logger.debug("正在锁定"+resourceName+",倒计时："+timeOut);
            Thread.sleep(interval);
            timeOut-=interval;
        }
        if(timeOut<=0){
            throw new Exception("Time out");
        }
        lockMap.put(resourceName,resourceName);
        return result;
    }

    public void release(String resourceName){
        logger.debug("'"+resourceName+"'锁释放。");
        lockMap.remove(resourceName);
    }
}
