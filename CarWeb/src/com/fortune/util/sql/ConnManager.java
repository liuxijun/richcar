package com.fortune.util.sql;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-8
 * Time: 20:32:15
 * To change this template use File | Settings | File Templates.
 */
public class ConnManager {
    private static final String configName = "/jdbc.properties";
    private static Properties properties = new Properties();
    private static String driverClass = "";
    private static String url = "";
    private static String username = "";
    private static String password = "";

    static{
        try{
            properties.load(new FileInputStream(ConnManager.class.getResource(configName).getFile()));
            driverClass = properties.getProperty("jdbc.driverClassName");
            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");

            Class.forName(driverClass);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConn(){
        try{
            //连接数据库
            return java.sql.DriverManager.getConnection(url, username, password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
