package com.fortune.smgw.api.sgip;

import com.fortune.util.AppConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

public class SGIPConfig
{
    public int SGIP_CLIENT_TIMEOUT = 30;
    public int SGIP_SERVER_TIMEOUT = 30;
    public int NODE_ID = 30;
    public int QUEUE_NUM = 10000;
   public static int SGIP_MIN_FEEVALUE = 0;
    public static int SGIP_MAX_FEEVALUE = 99999;

    public static String SGIP_TIME_EXT = "032+";

    private static SGIPConfig singleton_instance = null;

    private SGIPConfig()
    {
        AppConfigurator config = AppConfigurator.getInstance();
        SGIP_CLIENT_TIMEOUT = config.getIntConfig("SGIP_CLIENT_TIMEOUT",SGIP_CLIENT_TIMEOUT);
        SGIP_SERVER_TIMEOUT = config.getIntConfig("SGIP_SERVER_TIMEOUT",SGIP_SERVER_TIMEOUT);
        NODE_ID = config.getIntConfig("SGIP_NODE_ID",NODE_ID);
        QUEUE_NUM = config.getIntConfig("SGIP_QUEUE_NUM",QUEUE_NUM);
        SGIP_MIN_FEEVALUE = config.getIntConfig("SGIP_MIN_FEEVALUE",SGIP_MIN_FEEVALUE);
        SGIP_MAX_FEEVALUE = config.getIntConfig("SGIP_MAX_FEEVALUE",SGIP_MAX_FEEVALUE);
        SGIP_TIME_EXT = config.getConfig("SGIP_TIME_EXT",SGIP_TIME_EXT);
    }

    public static synchronized SGIPConfig getInstance()
    {
        if (singleton_instance == null) {
            singleton_instance = new SGIPConfig();
        }
        return singleton_instance;
    }
}