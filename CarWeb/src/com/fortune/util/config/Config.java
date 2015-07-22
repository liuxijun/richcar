package com.fortune.util.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {
    private static final String configName = "/rms.properties";
    private static Properties config = new Properties();
    static{
        try{
           new Config();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public Config(){
        try{
            config.load(new FileInputStream(Config.class.getResource(configName).getFile()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Config.getStrValue("property/A0102",""),从第二级开始的
    public static String getStrValue(String key,String defaultStr){
        String result = config.getProperty(key);
        if (result == null){
            return defaultStr;
        }
        return result;
    }

    public static int getIntValue(String key,int defaultInt){
        String result = config.getProperty(key);
        if (result == null){
            return defaultInt;
        }
        try{
            int i = Integer.parseInt(result);
            return i;
        }catch(Exception e){
            return defaultInt;
        }
    }

    public static void main(String args[]){
        System.out.println(config.getProperty("master.ip"));
    }

}