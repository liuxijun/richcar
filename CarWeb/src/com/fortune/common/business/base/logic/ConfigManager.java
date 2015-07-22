package com.fortune.common.business.base.logic;

import com.fortune.common.business.base.model.Config;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

/**
 * Created by xjliu on 2015/3/17
 * 配置管理.从数据库中读取
 */
public class ConfigManager {
    private static ConfigManager ourInstance = new ConfigManager();
    private ConfigLogicInterface configLogicInterface;
    public static ConfigManager getInstance() {
        return ourInstance;
    }

    private ConfigManager() {
        try {
            configLogicInterface = (ConfigLogicInterface) SpringUtils.getBean("configLogicInterface");
        } catch (Exception e) {
            configLogicInterface = (ConfigLogicInterface)SpringUtils.getBeanForApp("configLogicInterface");
        }
    }

    public String getConfig(final String configName, final String defaultValue){
        return (String) CacheUtils.get(configName,"systemConfig",new DataInitWorker(){
           public Object init(Object key,String cacheName){
               try {
                   Config config = configLogicInterface.get(configName);
                   if(config==null){
                       return defaultValue;
                   }
                   String val = config.getValue();
                   if(val==null||"".equals(val)){
                       return defaultValue;
                   }
                   return val;
               } catch (Exception e) {
                   return defaultValue;
               }
           }
        });
    }

    public long getConfig(String configName,long defaultValue){
        return StringUtils.string2long(getConfig(configName,""+defaultValue),defaultValue);
    }

    public int getConfig(String configName,int defaultValue){
        return StringUtils.string2int(getConfig(configName,""+defaultValue),defaultValue);
    }

    public boolean getConfig(String configName,boolean defaultValue){
        String val = getConfig(configName,defaultValue+"");
        if(val==null){
           return defaultValue;
        }
        return "true".equals(val)||"1".equals(val);
    }

    public String resetConfig(String configName){
        clearConfig(configName);
        return getConfig(configName,null);
    }
    public void clearConfig(String configName){
        if(configName==null){
            CacheUtils.clear("systemConfig");
        }else{
            CacheUtils.clear("systemConfig",configName);
        }
    }
}
