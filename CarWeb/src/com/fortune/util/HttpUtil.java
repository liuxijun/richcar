package com.fortune.util;

import com.fortune.rms.business.syn.logic.logicImpl.SynTaskLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.util.config.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-7-1
 * Time: 17:03:25
 * Http下载文件
 */
public class HttpUtil {
    private static Log log = LogFactory.getLog(HttpUtil.class);
    public static HttpURLConnection getHttpURLConnectionInstance(String sURL) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(sURL);
        } catch (MalformedURLException e) {
            log.error("传入的URL不正确:" + e.getMessage());
            e.printStackTrace();
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection == null) {
                throw new IOException();
            }
            httpURLConnection.setRequestProperty("User-Agent", "Internet Explorer");
        } catch (IOException e) {
            log.error("获取HttpURLConnection发生错误:" + e.getMessage());
            e.printStackTrace();
        }

        return httpURLConnection;
    }

    public static void HttpDownFile(String fileUrl, String masterIp, HttpProcessCaller caller)  {
        Config config = new Config();
        String encoding = config.getStrValue("web.encoding", "UTF-8");

        String sURL = "http://" + masterIp  + fileUrl + "";

       
        ServletContext context = ServletActionContext.getServletContext();
        String fullName = context.getRealPath(fileUrl);
        String filePath = FileUtils.extractFilePath(fullName, File.separator);
        String fileName = FileUtils.extractFileName(fullName, File.separator);

        File fileDirectory = new File(filePath);
        if(!fileDirectory.exists()){
            fileDirectory.mkdirs();
        }
        RandomAccessFile oSavedFile = null;
        try {
            oSavedFile = new RandomAccessFile(fullName, "rw");
        } catch (FileNotFoundException e) {
            log.error("文件获取失败:" + e.getMessage());
            e.printStackTrace();
        }
        filePath = fileUrl.substring(0,fileUrl.lastIndexOf("/"));
        sURL = "http://" + masterIp  + filePath +"/"+fileName+ "";
        try{
            sURL = com.fortune.util.net.URLEncoder.encode(sURL,encoding);
        }catch(UnsupportedEncodingException e){

        }
        sURL = sURL.replace("+","%20");
        HttpURLConnection httpURLConnection = getHttpURLConnectionInstance(sURL);
        InputStream input = null;

        try {
            input = httpURLConnection.getInputStream();
        } catch (IOException e) {
            log.error("获取InputStream发生错误:" + e.getMessage());
            e.printStackTrace();
        }
/*
        SynTaskLogicInterface synTaskLogicInterface = null;
        try {
            synTaskLogicInterface = (SynTaskLogicImpl) SpringUtils.getBean("synTaskLogicInterface");
        } catch (Exception e) {
            e.printStackTrace();
        }

*/
        long nStartPos = 0;
        long nEndPos = getFileSize(sURL);
        int nRead = 0;
        byte[] b = new byte[1024];
        try {
            while ((nRead = input.read(b)) > 0) {
                oSavedFile.write(b, 0, nRead);
                nStartPos += nRead;

                //synTaskLogicInterface.updateSynTaskProcess(synTaskId, nStartPos, nEndPos);
                if (caller != null) {
                    caller.callBack(nStartPos, nEndPos);
                }
            }

        } catch (IOException e) {
            log.error("文件读写错误:" + e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                input.close();
                httpURLConnection.disconnect();
                oSavedFile.close();
            }catch(Exception e){
                
            }
        }

    }


    public static long getFileSize(String sURL) {
        long nFileLength = -1;
        try {
            HttpURLConnection httpConnection = getHttpURLConnectionInstance(sURL);
            httpConnection.setRequestProperty("User-Agent", "Internet Explorer");

            int responseCode = httpConnection.getResponseCode();
            if (responseCode >= 400) {

                return -2; // -2 represent access is error
            }
            String sHeader;
            for (int i = 1; ; i++) {
                sHeader = httpConnection.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        nFileLength = Long.parseLong(httpConnection
                                .getHeaderField(sHeader));
                        break;
                    }
                } else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nFileLength;
    }

}
