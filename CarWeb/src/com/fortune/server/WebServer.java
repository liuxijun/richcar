package com.fortune.server;

import com.fortune.util.config.Config;
import com.fortune.util.FtpUtils;

import java.io.FileInputStream;
import java.io.File;

public class WebServer {


    public static void putFile(String path,String fileName){
        int webCount = Config.getIntValue("web.count",0);
        int ftpPort = Config.getIntValue("web.ftp.port",0);
        String ftpUsername = Config.getStrValue("web.ftp.username","");
        String ftpPassword = Config.getStrValue("web.ftp.password","");
        String ftpPath = Config.getStrValue("web.ftp.path","");

        for (int i=0; i<webCount; i++){
            try{
                String serverIp = Config.getStrValue("web.ip"+(i+1),"");
                if (fileName.indexOf("/")>-1){
                    ftpPath = fileName.substring(0,fileName.lastIndexOf("/")+1);
                    //fileName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
                }

                FtpUtils ftpUtils = new FtpUtils();
                ftpUtils.connectServer(serverIp,ftpPort,ftpUsername,ftpPassword,"/");
                ftpUtils.createDirectories(ftpPath);
                //ftpUtils.changeDirectory(ftpPath);
                //System.out.println(ftpUtils.getPWD());
                //System.out.println(fileName);

                ftpUtils.uploadFile(path,fileName);
                ftpUtils.closeServer();
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
    public static void main(String args[]){
        try{
            String path = "E:\\fortune_rms5\\02_Source\\web\\/upload/2011/07/25/164010_79-fu,piben.jpg";
            String fileName = "/upload6/2011/07/25/164010_79-fu,piben.jpg";
            WebServer.putFile(path,fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}