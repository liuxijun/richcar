package com.fortune.util;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-1-19
 * Time: 17:19:14
 * �߳�����
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
     * �źŵƴ���
     * @param resourceName �źŵ�����
     * @param timeOut  ��ʱʱ�䣬����Ϊ��λ
     * @param interval  �����������Ϊ��λ
     * @return    0��û������ֻ�е�ǰ�̡߳�1���Ϲ������ȴ��������߳�
     * @throws Exception  ��ʱ���������
     */
    public int acquire(String resourceName,int timeOut,int interval)throws Exception{
        int result=0;
        if(resourceName == null){
            throw new Exception("ResourceName is empty!");
        }
        logger.debug("'"+resourceName+"'�����");
        if(lockMap.containsKey(resourceName)){
            logger.debug("'"+resourceName+"'�����ڣ�");
            result = 1;
        }else{
            logger.debug("'"+resourceName+"'�������ڣ�");
        }
        while(timeOut>0 && lockMap.containsKey(resourceName)){
            logger.debug("��������"+resourceName+",����ʱ��"+timeOut);
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
        logger.debug("'"+resourceName+"'���ͷš�");
        lockMap.remove(resourceName);
    }
}
