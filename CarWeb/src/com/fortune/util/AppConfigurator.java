package com.fortune.util;

import com.fortune.common.business.base.logic.ConfigLogicInterface;
import com.fortune.common.business.base.model.Config;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-15
 * Time: 15:38:26
 */
public class AppConfigurator {
    private static AppConfigurator ourInstance=new AppConfigurator();
    private Configuration config;
    private ConfigLogicInterface configLogicInterface;
    public static AppConfigurator getInstance() {
        return ourInstance;
    }

    public static AppConfigurator getInstance(String configFileName) {
        return new AppConfigurator(configFileName);
    }

    private AppConfigurator() {
/*
        try {
            configLogicInterface = (ConfigLogicInterface) SpringUtils.getBean("configLogicInterface");
            config = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        config = new Configuration(null);
        configLogicInterface = null;
    }

    private AppConfigurator(String configFileName) {
        config = new Configuration(configFileName);
    }

    public String getConfig(String name) {
        return getConfig(name, "");
    }

    public String getConfig(final String name, final String defaultValue) {
        if(config==null){//如果没有指定配置文件，就从数据库中读取
            if(configLogicInterface!=null){
                try {
                    Config config = configLogicInterface.get(name);
                    if(config!=null){
                        return config.getValue();
                    }else{
                        return defaultValue;
                    }
                } catch (Exception e) {
                    return defaultValue;
                }

            }else{
                return defaultValue;
            }
/*
            return (String) CacheUtils.get(name,"applicationConfig",new DataInitWorker(){
               public Object init(Object key,String cacheName){
                   try {
                       Config config = configLogicInterface.get((String)key);
                       if(config!=null){
                           return config.getValue();
                       }
                   } catch (Exception e) {

                   }
                   return defaultValue;
               }
            });
*/
        }
        return config.getValue(name, defaultValue);
    }

    public int getIntConfig(String name, int defaultValue) {
        try {
            return Integer.parseInt(getConfig(name, "" + defaultValue));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getLongConfig(String name, long defaultValue) {
        try {
            return Long.parseLong(getConfig(name, "" + defaultValue));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolConfig(String name, boolean defaultValue) {
        String defaultStr = "false";
        if (defaultValue) {
            defaultStr = "true";
        }
        return "true".equals(getConfig(name, defaultStr));
    }
}
