package com.fortune.server.protocol;

import com.fortune.common.web.config.SystemConfig;
import com.fortune.server.HlsProxy;

import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-1-22
 * Time: 上午9:38
 *
 */
public class HttpProcessor  extends Thread{

    protected String remotePeerIp;
    protected Logger logger = Logger.getLogger(this.getClass());
    protected HlsProxy peerServer;
    protected String requestUrl;
    protected String parameters;
    boolean responseHeaderSeted = false;
    private HttpExchange httpExchange;
    public HttpProcessor(HlsProxy ps, HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.peerServer = ps;
    }
    public void run() {
        try {
            remotePeerIp = httpExchange.getRemoteAddress().getAddress().getHostAddress();
            requestUrl = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null) {
                requestUrl += "?" + query;
            }
            //logger.debug(requestUrl);
            String command = StringUtils.getParameter(requestUrl, "command");
//            logger.debug("Method:" + command);
//            logger.debug("客户端IP:"+httpExchange.getRemoteAddress().getAddress().getHostAddress());
            InputStream in = httpExchange.getRequestBody(); //获得输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String temp;
            parameters = "";
            while ((temp = reader.readLine()) != null) {
                parameters += temp;
            }
            OutputStream outStream = httpExchange.getResponseBody();
            process(command);
            if(!responseHeaderSeted){
                sendBuffer(new SessionResponse(200,this.getXmlMsg(404,"Command Not Found:"+requestUrl)));
            }
            outStream.close();
            httpExchange.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void sendBuffer(SessionResponse sessionResponse){
        if (sessionResponse == null) {
            sessionResponse = new SessionResponse(404,"ERROR NOT FOUND");
            sessionResponse.setContentType("text/json");
            sessionResponse.setStartTime(0);
            sessionResponse.setStopTime(0);
            byte[] json = "{resultCode:404,message:'Not found'}".getBytes();
            sessionResponse.addResultData(json.length,json);
        }
        if(sessionResponse.getContentType()==null){
            sessionResponse.setContentType("text/html");
        }

        List<StreamData> dataList = sessionResponse.getResultData();
        try {
            httpExchange.getResponseHeaders().set("Content-Type",sessionResponse.getContentType());
            httpExchange.sendResponseHeaders(sessionResponse.getResultCode(), sessionResponse.getContentLength());
            responseHeaderSeted = true;
            for(StreamData data:dataList){
                httpExchange.getResponseBody().write(data.getData(),0,data.getLength());
            }
        } catch (Exception e) {
            logger.error("发送数据到"+remotePeerIp+"时发生异常："+e.getMessage()+",数据详情：\n"+sessionResponse.toString());
            //e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public String getRemotePeerIp() {
        return remotePeerIp;
    }

    @SuppressWarnings("unused")
    public void setRemotePeerIp(String remotePeerIp) {
        this.remotePeerIp = remotePeerIp;
    }

    @SuppressWarnings("unused")
    public HlsProxy getPeerServer() {
        return peerServer;
    }

    @SuppressWarnings("unused")
    public void setPeerServer(HlsProxy peerServer) {
        this.peerServer = peerServer;
    }

    public void transferToPeer(String requestUrl){
        String url = AppConfigurator.getInstance().getConfig("system.hls.proxy.sourceServer","http://192.168.1.25:8080");
        while(url.endsWith("/")){
            url = url.substring(0,url.length()-1);
        }
        while(requestUrl.startsWith("/")&&requestUrl.length()>1){
            requestUrl = requestUrl.substring(1);
        }
        url = url+"/"+requestUrl;
        int p = url.indexOf("&tokenekot=");
        String key = url;
        if(p>0){
            key = key.substring(0,p);
        }
        final String visitUrl = url;
        SessionResponse response;
        if((!visitUrl.contains(".m3u8"))&&AppConfigurator.getInstance().getBoolConfig("system.hls.proxy.useCache",false)){
            response = (SessionResponse) CacheUtils.get(key,"httpLiveServerCache",new DataInitWorker(){
                public Object init(Object key,String cacheName){
                    return postToHost(visitUrl,null,null);
                    //return result;
                }
            });
        }else{
            response = postToHost(visitUrl,null,null);
        }
        if(response!=null&&response.getResultCode()!=HttpURLConnection.HTTP_OK){
            logger.debug("请求返回错误："+visitUrl+"\n"+response);
        }
        if(visitUrl.contains(".mp4.m3u8")){
            String m3u8 = new String(response.getResultData().get(0).getData(),0,(int)response.getContentLength());
            int length0=m3u8.length();
            m3u8 = m3u8.trim();
            int length1=m3u8.length();
            m3u8=m3u8.replaceAll("\n","\r\n");
            m3u8=m3u8.replaceAll("\r\r\n","\r\n");
            m3u8=m3u8.replaceAll("\r\n","\n");
            m3u8+="\n";
            int length2 = m3u8.length();
            logger.debug("请求"+visitUrl+"返回结果：\n原始长度："+length0+",Trim后："+length1+",替换后："+length2+"\n"+m3u8);
            byte[] dataBuffer = m3u8.getBytes();
            response.getResultData().clear();
            response.addResultData(dataBuffer.length,dataBuffer);
        }
//        logger.debug("将请求："+url);
        sendBuffer(response);
    }

    public void process(String command) {
/*
        command = StringUtils.getParameter(requestUrl, "command");
//*/
//        logger.debug("请求连接："+requestUrl);
        if (parameters != null) {
            parameters = parameters.trim();
        }
//        logger.debug("command:" + command);
//        logger.debug("parameter:" + parameters);
        if ("stop".equals(command)) {
            stopServer(requestUrl);
        }else if(requestUrl.startsWith("/ep-get/manage")){
            sendBuffer(new SessionResponse(200, getXmlMsg(200, "Stop command unknown:"+requestUrl)));
        } else {
            transferToPeer(requestUrl);
        }
    }

    /**
     * 停止服务
     *
     * @param url 参数
     */
    public void stopServer(String url) {
        String password = StringUtils.getParameter(url, "password");
        if ("127.0.0.1".equals(remotePeerIp)||(password != null &&
                password.equals(AppConfigurator.getInstance().getConfig("p2p.manage.password","fortune!@#456")))) {
            peerServer.setStop();
            sendBuffer(new SessionResponse(200, getXmlMsg(200, "Stop command has been gotten!!!")));
        } else {
            sendBuffer(new SessionResponse(200, getXmlMsg(404, "Error password or userId")));
        }
    }
    public String getXmlMsg(int code,String message){
        return "<?xml version=\"1.0\"?><hls code=\"" + code+
                "\">"+message+"</hls>";
    }
    public class TimeWarn extends Thread{
        private boolean shouldStop = false;
        private String downloadUrl = "not set";
        public void finished(){
            shouldStop = true;
        }
        public TimeWarn(String downloadUrl){
            this.downloadUrl = downloadUrl;
            this.setDaemon(false);
        }
        public void run(){
            long startTime = System.currentTimeMillis();

            while(!shouldStop){
                try {
                    sleep(1000);
                    long now = System.currentTimeMillis();
                    logger.warn("下载连接已经尝试了"+(now-startTime)+"毫秒："+downloadUrl);
                } catch (InterruptedException e) {
                    long now = System.currentTimeMillis();
                    logger.debug("下载计时结束："+(now-startTime)+"毫秒："+downloadUrl);
                    break;
                }
            }
        }
    }
    public SessionResponse postToHost(String url, String postData,String strEncoding) {
        SessionResponse result = new SessionResponse();
        HttpURLConnection con;
        result.setStartTime(System.currentTimeMillis());
        int tryTimes = 0;
        int bufferLength = AppConfigurator.getInstance().getIntConfig("system.hls.proxy.maxBufferLength",1024*3064);
        TimeWarn timeWarn = new TimeWarn(url);
        timeWarn.start();
        while(tryTimes<2){
            tryTimes++;
            try {
                URL dataUrl = new URL(URLEncoder.encode(url,"UTF-8"));
                //logger.debug("尝试访问："+url);
                con = (HttpURLConnection) dataUrl.openConnection();
                con.setConnectTimeout(2000);
                con.setReadTimeout(20000);
                if(postData==null || postData.trim().equals("")){
                    con.setRequestMethod("GET");
                    con.setDoOutput(false);
                    con.setDoInput(true);
                }else{
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Proxy-Connection", "Keep-Alive");
                    OutputStream os = con.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    byte[] dataBuffer;
                    if(strEncoding!=null){
                        dataBuffer = postData.getBytes(strEncoding);
                    }else{
                        dataBuffer = postData.getBytes();
                    }
                    dos.write(dataBuffer);
                    dos.flush();
                    dos.close();
                }

                InputStream is = con.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                //System.out.println("POST返回区域大小："+dis.available());
                int code = con.getResponseCode();
                result.setContentType(con.getContentType());
                result.setResultCode(code);
                byte d[] = new byte[bufferLength];
                int posOfBuffer = 0;
                while(true){
                    int i= dis.read(d,posOfBuffer,d.length-posOfBuffer);
                    if(i<=0){
                        break;
                    }
                    posOfBuffer += i;
                    if(posOfBuffer==d.length){
                        result.addResultData(d.length,d);
                        d = new byte[bufferLength];
                        posOfBuffer = 0;
                    }
//                if(i<d.length){
//                    break;
//                }
                }
                if(posOfBuffer>0){
                    result.addResultData(posOfBuffer,d);
                }
                String errorMsg;
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
                    logger.error("HTTP请求发生错误："+code+"," +
                            errorMsg);
                }
                //data = new String(d);
                con.disconnect();
                break;
            } catch (Exception ex) {
                logger.error("无法连接：" + url+","+ex.getMessage());
                result.setResultCode(500);
                //ex.printStackTrace();
            }
        }
        result.setStopTime(System.currentTimeMillis());
        long duration = result.getStopTime()-result.getStartTime();
        if(duration>1000){
            logger.warn("此次访问时间过长："+duration+"ms,"+result);
        }
        timeWarn.finished();
        timeWarn.interrupt();
        return result;
    }

}

