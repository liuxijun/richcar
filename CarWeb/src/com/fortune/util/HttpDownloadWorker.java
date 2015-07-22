package com.fortune.util;

import com.fortune.util.net.URLEncoder;
import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.condition.Http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xjliu on 2014/11/16.
 * 下载http文件的线程
 */
public class HttpDownloadWorker extends Thread {
    private String url;
    private String rootPath;
    private String errorMsg;
    private HttpProcessCaller caller;
    private String desertFileName;
    private Logger logger = Logger.getLogger(getClass());


    public HttpDownloadWorker(String url, String rootPath,String desertFileName, HttpProcessCaller caller) {
        this.url = url;
        this.rootPath = rootPath;
        this.desertFileName = desertFileName;
        this.caller = caller;
    }

    public boolean download(){
        if(desertFileName==null){
            desertFileName = rootPath+"/"+StringUtils.getClearURL(url);
        }
        File desertFile = new File(desertFileName);
        File path = desertFile.getParentFile();
        if(!path.exists()){
            if(!path.mkdirs()){
                String msg = "无法创建目录："+path.getAbsolutePath();
                logger.error(msg);
                if(caller!=null){
                    caller.error(500,msg);
                }
                return false;
            }
        }
        try {
            int bufferLength = 1024*1024;
            HttpURLConnection con;
            URL dataUrl = new URL(URLEncoder.encode(url, "UTF-8"));
            //logger.debug("尝试访问："+url);
            con = (HttpURLConnection) dataUrl.openConnection();
            con.setConnectTimeout(2000);
            con.setReadTimeout(20000);
            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setDoInput(true);
            int fileLength = con.getContentLength();
            if(caller!=null){
                caller.beforeStart(fileLength);
            }
            InputStream is = con.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            logger.debug("准备写文件："+desertFile.getAbsolutePath());
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(desertFile.getAbsoluteFile()));
            //System.out.println("POST返回区域大小："+dis.available());
            int code = con.getResponseCode();
            byte d[] = new byte[bufferLength];
            int downloaded = 0;
            while(true){
                int i= dis.read(d,0,d.length);
                if(i<=0){
                    break;
                }
                os.write(d,0,i);
                downloaded+=i;
                if(caller!=null){
                    caller.callBack(downloaded,fileLength);
                }
            }
            os.flush();
            os.close();
            if(caller!=null){
                caller.finished(downloaded,fileLength);
            }
            if(code==HttpURLConnection.HTTP_OK){
                errorMsg = "HTTP_OK";
            }else if(code == HttpURLConnection.HTTP_BAD_REQUEST){
                errorMsg = "HTTP_BAD_REQUEST";
            }else if(code == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
                errorMsg = "HTTP_CLIENT_TIMEOUT";
            }else if(code == HttpURLConnection.HTTP_NOT_FOUND){
                errorMsg = "HTTP_NOT_FOUND";
            }else{
                errorMsg = "HTTP_UNKNOWN";
            }
            if(code!=HttpURLConnection.HTTP_OK){
                if(caller!=null){
                    caller.error(code,"HTTP请求发生错误："+code+"," +errorMsg);
                }
                logger.error("HTTP请求发生错误："+code+"," +
                        errorMsg);
            }
            //data = new String(d);
            con.disconnect();
        } catch (Exception ex) {
            if(caller!=null){
                caller.error(500,ex.getMessage());
            }
            logger.error("无法连接：" + url+","+ex.getMessage());
        }
        return desertFile.exists();
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public HttpProcessCaller getCaller() {
        return caller;
    }

    public void setCaller(HttpProcessCaller caller) {
        this.caller = caller;
    }

    public void run(){
        download();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
