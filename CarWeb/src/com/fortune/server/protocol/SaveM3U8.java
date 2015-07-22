package com.fortune.server.protocol;

import cn.sh.guanghua.mediastack.dataunit.Server;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.AppConfigurator;
import com.fortune.util.FileUtils;
import com.fortune.util.StringUtils;
import com.fortune.util.net.URLEncoder;
import jxl.common.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by xjliu on 2014/10/12.
 * 保存M3U8，包括媒体文件
 */
public class SaveM3U8 extends Thread {
    private String url;
    private Logger logger = Logger.getLogger(this.getClass());
    private String rootPath;
    private String reportUrl;
    ServerMessager messager = new ServerMessager();
    public SaveM3U8(String url,String rootPath){
        this.url = url;
        this.rootPath = rootPath;
        this.reportUrl = AppConfigurator.getInstance().getConfig("system.movie.fileInterfaceUrl",
                "http://115.28.233.138/interface/files.jsp");
    }

    private Date startTime;
    private Date stopTime;
    private long dataLength=0;
    private long allBandwidth = 0;
    //private long lastFiveSecondsBandwidth = 0;
    private int resultCode=0;
    private float allM3U8Duration = 0.0f;
    private float currentPos = 0.0f;
    private String logs = "";
    public String getLogs(){
        return logs;
    }
    public long getDuration(){
        return stopTime.getTime()-startTime.getTime();
    }
    public int getResultCode() {
        return resultCode;
    }

    public void afterFinished() {
    }

    public void beforeStart() {
        startTime = new Date();
        logs ="";//"\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+"已经启动";
    }

    boolean willStop = false;
    public void shutdownNow(){
        willStop=true;
    }

    public void run(){
        startTime = new Date();
        dataLength = 0;
        ServerMessager messager = new ServerMessager();
        M3U8 m3u8 = new M3U8();
        String m3u8Content = messager.postToHost(url,null);
        if(m3u8Content!=null&&!"".equals(m3u8Content.trim())){
            m3u8.addStream(0,1,url,m3u8Content);

            for(M3U8Stream stream:m3u8.getStreams()){
                if(willStop){
                    break;
                }
                allM3U8Duration = stream.getAllDuration();
                for(M3U8Segment segment:stream.getSegments()){
                    if(willStop){
                        break;
                    }
                    String segmentUrl = segment.getUrl();
                    SessionResponse data = getSegementFromUrl(segmentUrl, null, null);
                    dataLength+=data.getContentLength();
                    if(rootPath!=null){
                        if(saveSegment(rootPath,segment,data)){
                            currentPos += segment.getDuration();
                            reportProcess(allM3U8Duration,currentPos);
                        }else{
                            logger.error("无法保存："+(segment.getUrl()));
                        }
                    }
                    long allDuration = System.currentTimeMillis()-startTime.getTime();
                    if(allDuration>0){
                        allBandwidth = dataLength*8*1000 / allDuration;
                        logs = "Total Bandwidth="+ StringUtils.formatBPS(allBandwidth);
                        logger.debug(logs);
                    }
                }
            }
            m3u8.setRelateUrlType(true);
            if(rootPath==null||"nul".equals(rootPath)||"null".equals(rootPath)){
                return;
            }else{
                FileUtils.writeNew(transUrlToFileName(rootPath,url),m3u8.toString());
            }
        }

        stopTime = new Date();
        long duration = (stopTime.getTime()-startTime.getTime())/1000;
        String secondStr = ""+duration%60;
        while(secondStr.length()<2){
            secondStr = "0"+secondStr;
        }
        logs ="\n"+StringUtils.date2string(startTime)+"->"+StringUtils.date2string(stopTime)+
                ",bandwidth="+allBandwidth+",duration="+(duration/60)+":"+secondStr;
        reportStatus(allM3U8Duration,-1,SynTaskLogicInterface.STATUS_FINISHED,"下载完成");
    }

    public SessionResponse getSegementFromUrl(String url, String postData,String strEncoding) {
        SessionResponse result = new SessionResponse();
        HttpURLConnection con;
        result.setStartTime(System.currentTimeMillis());
        int tryTimes = 0;
        int bufferLength = AppConfigurator.getInstance().getIntConfig("system.hlsProxy.maxBufferLength",1024*3024);
//        TimeWarn timeWarn = new TimeWarn(url);
//        timeWarn.start();
        while(tryTimes<2){
            tryTimes++;
            try {
                URL dataUrl = new URL(URLEncoder.encode(url, "UTF-8"));
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
                int code = con.getResponseCode();
                result.setContentType(con.getContentType());
                result.setResultCode(code);
                byte d[] = new byte[bufferLength];
                int posOfBuffer = 0;
                while(true){
                    if(willStop){
                        break;
                    }
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
                    errorMsg = "HTTP_UNKNOWN:"+code;
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
        if(duration>30000){
            logger.warn("此次访问时间过长："+duration+"ms,"+result);
        }

        if(duration>0){
            logger.debug("Current Download Bandwidth="
                    + StringUtils.formatBPS(result.getContentLength() * 8 * 1000 / duration)+"," +
                    duration+"ms,"+(result.getContentLength())+"Bytes");
        }
        return result;
    }

    private String repairUrl(String url){
        String pos = StringUtils.getParameter(url,"pos");
        String dur = StringUtils.getParameter(url,"dur");
        String result = StringUtils.getClearURL(url);
        int p=result.indexOf("?");
        if(p>0){
            result = result.substring(0,p);
        }
        boolean willAppendTs = false;
        if(pos!=null&&!"".equals(pos)){
            result = result+"_pos_"+pos;
            willAppendTs = true;
        }
        if(dur!=null&&!"".equals(dur)){
            result = result+"_dur_"+dur;
            willAppendTs = true;
        }
        if(willAppendTs){
            result+=".ts";
        }
        return result;
    }
    private String transUrlToFileName(String rootPath,String url){
        return rootPath+"/"+repairUrl(url);
    }
    private boolean saveSegment(String rootPath,M3U8Segment segment,SessionResponse data){
        if(null==rootPath||"nul".equals(rootPath)||"null".equals(rootPath)){
            return true;
        }
        String url = segment.getUrl();
        url = repairUrl(url);
        File file = new File(transUrlToFileName(rootPath,url));
        OutputStream out = null;
        try {
            File parent = file.getParentFile();
            if (!parent.isDirectory()) {
                if(!parent.mkdirs()){
                    logger.error("无法创建目录："+parent.getAbsolutePath());
                }
            }
            int BUFFER_SIZE = 512*1024;
            out = new BufferedOutputStream(new FileOutputStream(file),
                    BUFFER_SIZE);
            for(StreamData sd:data.getResultData()) {
                out.write(sd.getData(), 0, sd.getLength());
            }
            segment.setUrl(url);
        } catch (Exception e) {
            reportStatus(allM3U8Duration,currentPos, SynTaskLogicInterface.STATUS_IO_ERROR,"下载过程中出现异常："+e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public void reportStatus(float allDuration,float currentPos,long status,String message){
        boolean finished = false;
        if(currentPos<0){
            currentPos = allDuration;
            finished = true;
        }
        messager.postToHost(reportUrl,"command=reportPushProcess&url="+url.replace("?","&")+"&allDuration="
                        +allDuration+"&currentPos="+currentPos+"&finished="+finished+"&taskStatus="+status+"&message="+message+
                "&startTime="+StringUtils.date2string(startTime)+"&stopTime="+StringUtils.date2string(stopTime),
                "UTF-8");
    }
    public void reportProcess(float allDuration,float currentPos){
        long status=-1L;
        if(currentPos>=0){
            status = SynTaskLogicInterface.STATUS_RUNNING;
        }
        reportStatus(allDuration,currentPos,status,"设置当前进行进度");
    }
}
