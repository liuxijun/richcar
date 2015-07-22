package com.fortune.util;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-1-13
 * Time: 9:06:19
 * 缓存助手
 */
public class CacheUtils<E> {
/*
    private static CacheUtils instance=new CacheUtils();
    public static CacheUtils getInstance(){
        return instance;
    }

    private CacheUtils(){

    }

*/
    public static Logger logger = Logger.getLogger("com.fortune.util.CacheUtils");
    public static final String EMPTY_VALUE_STRING ="EMPTY_VALUE_STRING_20130428";
    public static void clearAll(){
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.clearAll();
    }

    public static void clear(String cacheName){
        clear(cacheName,null);
    }

    public static void clear(String cacheName,Object key){
        CacheManager cacheManager = CacheManager.getInstance();
        if(cacheManager.cacheExists(cacheName)){
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                if(key!=null){
/*
                    if(cache.isKeyInCache(key)){
                    }
*/
                    cache.remove(key);
                }else{
                    cache.removeAll();
                    //cacheManager.removeCache(cacheName);
                }
            }
        }
    }


    public static synchronized Cache getCache(String cacheName){
        CacheManager cacheManager = CacheManager.getInstance();
        AppConfigurator config = AppConfigurator.getInstance();
        long timeToLive = config.getLongConfig("cache.timeToLive", 3600);//默认保存10分钟
        long timeToIdle = config.getLongConfig("cache.timeToIdle", 3600);
        Cache cache = cacheManager.getCache(cacheName);
        if(cache == null){
            cache = new Cache(cacheName,1024,false,false,timeToLive,timeToIdle);
            try {
                cacheManager.addCache(cache);
            } catch (IllegalStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (CacheException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return cache;
    }

    public static Object get(Object objKey,String cacheName,DataInitWorker worker){
        Object result = null;
        if(objKey!=null){
            Cache cache = getCache(cacheName);
            Element ele = cache.get(objKey);
            if(ele!=null){
                try {
                    return  ele.getValue();
                } catch (Exception e) {
                    logger.error("在获取缓存后的内容时发生异常："+e.getMessage(),e);
                }
            }
            logger.debug("没有在缓存里找到内容，重新初始化到内存中："+cacheName+"<----"+objKey);
            ThreadUtils tu = ThreadUtils.getInstance();
            try {
                tu.acquire(cacheName+"_"+objKey);
                ele = cache.get(objKey);
                if(ele!=null){
                    Object value = ele.getValue();
                    if(value instanceof String &&EMPTY_VALUE_STRING.equals(value)){
                        return null;
                    }else{
                        return value;
                    }
                }
                result = worker.init(objKey,cacheName);
                if(result!=null){
                    ele = new Element(objKey,result);
                    cache.put(ele);
                }else{
                    Configuration config = new Configuration("/fortune_application.properties");
                    if("true".equals(config.getValue("cache.keepNullCacheStatus","true"))){
                        ele = new Element(objKey,null);
                        cache.put(ele);
                        logger.warn("数据初始化也没有成功，但根据配置，还是将其空值保存在缓存中："+cacheName+"<----"+objKey);
                    }else{
                        logger.error("数据初始化也没有成功："+cacheName+"<----"+objKey);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tu.release(cacheName+"_"+objKey);
            }

        }
        return result;
    }
}
