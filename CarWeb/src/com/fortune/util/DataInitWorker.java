package com.fortune.util;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-11-7
 * Time: 10:23:07
 * 缓存初始化的虚拟类
 */
public class DataInitWorker{
    private Logger logger = Logger.getLogger(this.getClass());
   public Object init(Object key,String cacheName){
     logger.error("发生异常，不应该调用这个方法！");
     return null;
   }
}
