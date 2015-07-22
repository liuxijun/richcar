package com.fortune.test.Testor;

import com.fortune.server.message.ServerMessager;
import com.fortune.server.protocol.*;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xjliu on 2015/3/30.
 * 直播播放测试
 */
public class TestLive {
    private String[] urls;
    private Logger logger = Logger.getLogger(getClass());
    private ExecutorService es = Executors.newFixedThreadPool(AppConfigurator.getInstance().getIntConfig(
            "test.workerThreadCount", 5));
    private String phoneNumbers ;
    private String logs="", successLogs ="能成功播放的频道包括";
    private Map<String,String> errorChannels=new HashMap<String,String>();//错误频道数据，key是号码，值是错误频道列表
    public void init(){
        phoneNumbers = AppConfigurator.getInstance().getConfig("test.sendToPhoneNumbers","15613199253");
        urls = AppConfigurator.getInstance().getConfig("test.liveUrls",
                "http://61.55.145.163/live/hls/1032.m3u8?name=浙江卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1010.m3u8?name=河北卫视,"+
                        "http://61.55.145.163/live/hls/1001.m3u8?name=安徽卫视,"+
                        "http://61.55.145.163/live/hls/1004.m3u8?name=东方卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1015.m3u8?name=江苏卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1038.m3u8?name=CCTV6,"+
                        "http://61.55.145.163/live/hls/1002.m3u8?name=北京卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1013.m3u8?name=湖北卫视,"+
                        "http://61.55.145.163/live/hls/1027.m3u8?name=天津卫视,"+
                        "http://61.55.145.163/live/hls/1016.m3u8?name=江西卫视,"+
                        "http://61.55.145.163/live/hls/1022.m3u8?name=山东卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1025.m3u8?name=深圳卫视（高清）,"+
                        //"http://61.55.145.163/live/hls/1092.m3u8?name=NBA直播第2频道,"+
                        "http://61.55.145.163/live/hls/1136.m3u8?name=CCTV3,"+
                        "http://61.55.145.163/live/hls/1046.m3u8?name=风云足球,"+
                        "http://61.55.145.163/live/hls/1041.m3u8?name=CCTV13,"+
                        "http://61.55.145.163/live/hls/1048.m3u8?name=CCTV5高清,"+
                        "http://61.55.145.163/live/hls/1081.m3u8?name=欧美院线,"+
                        "http://61.55.145.163/live/hls/1086.m3u8?name=动漫频道,"+
                        "http://61.55.145.163/live/hls/1042.m3u8?name=CCTV14,"+
                        "http://61.55.145.163/live/hls/1084.m3u8?name=生活频道,"+
                        "http://61.55.145.163/live/hls/1039.m3u8?name=CCTV8,"+
                        "http://61.55.145.163/live/hls/1040.m3u8?name=CCTV9,"+
                        "http://61.55.145.163/live/hls/1047.m3u8?name=欧洲足球,"+
                        "http://61.55.145.163/live/hls/1043.m3u8?name=湖南卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1035.m3u8?name=CCTV-1（高清）,"+
                        "http://61.55.145.163/live/hls/1055.m3u8?name=动漫秀场,"+
                        "http://61.55.145.163/live/hls/1053.m3u8?name=欢笑剧场,"+
                        "http://61.55.145.163/live/hls/1056.m3u8?name=旅游卫视 ,"+
                        "http://61.55.145.163/live/hls/1054.m3u8?name=劲爆体育,"+
                        "http://61.55.145.163/live/hls/1057.m3u8?name=纪实频道 ,"+
                        "http://61.55.145.163/live/hls/1058.m3u8?name=金鹰卡通 ,"+
                        "http://61.55.145.163/live/hls/1059.m3u8?name=CCTV2,"+
                        "http://61.55.145.163/live/hls/1007.m3u8?name=广东卫视（高清）,"+
                        "http://61.55.145.163/live/hls/1045.m3u8?name=黑龙江卫视（高清）"
        ).split(",");
    }
    public void onError(String message,String url){
        logger.error("发生错误："+message+","+url);
        String phones = StringUtils.getParameter(url,"phoneNumbers");
        if(phones==null||"".equals(phones)){
            phones = phoneNumbers;
        }else{
            phones = phoneNumbers+","+phones;
        }
        if(url!=null){
            String name = StringUtils.getParameter(url,"name");
            if(name!=null&&!"".equals(name.trim())){
                url = name;
            }else{
                int p=url.lastIndexOf("/");
                if(p>0){
                    url = url.substring(p+1);
                }
                p = url.lastIndexOf(".m3u8");
                if(p>0){
                    url = url.substring(0,p);
                }
            }
        }
        String[] numbers = phones.split(",");
        for(String number:numbers){
            String errorLogs = errorChannels.get(number);
            if(errorLogs==null){
                errorLogs = url;
            }else{
                errorLogs+=","+url;
            }
            errorChannels.put(number,errorLogs);
        }
        if(!logs.contains(","+url+",")){
            logs+=url+",";
        }
    }
    public void onSuccess(String url){
        String name = StringUtils.getParameter(url,"name");
        if(name!=null){
            successLogs +=","+name;
        }
        logger.debug("测试通过："+url);
    }
    public void tryToPlayLive(String url){
        TestWorker worker = new TestWorker(url);
        es.execute(worker);
    }
    public class TestWorker implements Runnable{
        private Date startTime,stopTime;
        private long dataLength;
        private String url;
        private boolean willStop;
        private float allM3U8Duration;
        private Logger logger = Logger.getLogger(getClass());
        public TestWorker(String url){
            if(url!=null){
                url = url.trim();
            }
            this.url = url;
            this.willStop = false;
        }
        public boolean doTest(String url){
            dataLength = 0;
            logger.debug("开启测试："+url);
            ServerMessager messager = new ServerMessager();
            M3U8 m3u8 = new M3U8();
            //logger.debug("尝试下载："+url);
            Map<Integer,Integer> segmentSequences = new HashMap<Integer, Integer>();
            float targetDuration=6.0f;
            int testTimes = 0;
            boolean sequenceChanged = false;
            while(testTimes<5){
                testTimes++;
                logger.debug("正在进行第" +testTimes+
                        "次下载M3U8："+url);
                String m3u8Content = messager.postToHost(url,null);
                //logger.debug("下载的结果是："+m3u8Content);
                if(m3u8Content!=null&&!"".equals(m3u8Content.trim())){
                    m3u8.addStream(0, 1, url, m3u8Content);
                    List<M3U8Stream> streams = m3u8.getStreams();
                    if(streams==null||streams.size()<=0){
                        onError("m3u8内容解析错误",url);
                        break;
                    }else{
                        for(M3U8Stream stream:m3u8.getStreams()){
                            Integer lastSequence = segmentSequences.get(stream.getProgramId());
                            if(targetDuration<stream.getTargetDuration()){
                                targetDuration = stream.getTargetDuration();
                            }
                            Integer sequence = stream.getMediaSequence();
                            if(lastSequence==null){
                                sequenceChanged = false;
                                lastSequence = sequence;
                            }else{

                                if(!lastSequence.equals(sequence)){
                                    logger.debug("sequence发生变化，上次的sequence="+lastSequence+",现在的sequence="+sequence);
                                    lastSequence = sequence;
                                    sequenceChanged = true;
                                    break;
                                }
                            }
                            segmentSequences.put(stream.getProgramId(),lastSequence);
                            if(willStop){
                                break;
                            }
                            allM3U8Duration = stream.getAllDuration();
                            List<M3U8Segment> segments = stream.getSegments();
                            if(segments==null||segments.size()<=0){
                                onError("下载segement时发生异常，没有取到任何的片段信息",url);
                            }else{
                                int startIdx = segments.size()-3;
                                if(startIdx<0){
                                    startIdx=0;
                                }
                                int size = segments.size();
                                logger.debug("尝试下载所有的片段，startIdx = " +startIdx+
                                        ",size=" +size+
                                        ",url="+url);
                                for(;startIdx<size;startIdx++){
                                    M3U8Segment segment = segments.get(startIdx);
                                    if(willStop){
                                        break;
                                    }
                                    String segmentUrl = segment.getUrl();
                                    SessionResponse data = null;
                                    int segmentDownloadTimes = 0;
                                    while(data==null||data.getContentLength()<=0){
                                        data = getSegementFromUrl(segmentUrl, null, null);
                                        segmentDownloadTimes ++;
                                        //尝试下载5次还不行，就认为失败了
                                        if(segmentDownloadTimes>5){
                                            break;
                                        }
                                    }
                                    if(data==null||data.getContentLength()<=0){
                                        return false;
                                    }else{
                                        dataLength+=data.getContentLength();
                                    }
                                }
                                logger.debug("尝试下载所有的片段完毕，url="+url);
                            }
                        }
                        if(!sequenceChanged){
                            logger.warn("sequence没有变化，需要等待一下进行下一波的测试，等待时间是："+targetDuration+"秒，url="+url);
                            try {
                                Thread.sleep((int)(targetDuration*1000L));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(sequenceChanged){
                        onSuccess(url);
                        return true;
                    }
/*
            } else {
                onError("无法下载M3U8列表", url);
*/
                }else{
                    break;
                }
            }
            return false;
        }
        public void run(){
            startTime = new Date();
            int testTimes = 0;
            boolean success=true;
            while(testTimes<5){
                testTimes++;
                if(testTimes>0){
                    logger.debug("第"+testTimes+"次测试"+url);
                }
                success = doTest(url);
                if(success){
                    break;
                }
                if(testTimes>0){
                    logger.debug("第"+testTimes+"次测试失败："+url);
                }
                try {
                    Thread.sleep(12000L);
                } catch (InterruptedException e) {
                    logger.debug("无法等待，被吵醒了");
                }
            }
            if(!success){
                onError("测试异常",url);
            }
            stopTime = new Date();
        }
        public SessionResponse getSegementFromUrl(String url, String postData,String strEncoding) {
            SessionResponse result = new SessionResponse();
            HttpURLConnection con;
            result.setStartTime(System.currentTimeMillis());
            int tryTimes = 0;
            int bufferLength = AppConfigurator.getInstance().getIntConfig("system.hlsProxy.maxBufferLength",1024*3024);
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
                    //onError("无法连接",url);
                    result.setResultCode(500);
                    //ex.printStackTrace();
                }
            }
            result.setStopTime(System.currentTimeMillis());
            long duration = result.getStopTime()-result.getStartTime();
            if(duration>30000){
                logger.warn("此次访问时间过长："+duration+"ms,"+result);
            }
/*
            if(duration>0){
                logger.debug("下载" +url+"完毕，带宽："+ StringUtils.formatBPS(result.getContentLength() * 8 * 1000 / duration)+",时长" +duration+"ms,大小："+(result.getContentLength())+"Bytes");
            }
*/
            return result;
        }
        public void report(){

        }

    }
    public void doTest(){
        init();

        for(String url:urls){
            if(url!=null){
                url = url.trim();
                if(url.startsWith("//")){
                    logger.debug("这个链接被注释了，不用进行测试："+url);
                    continue;
                }
                tryToPlayLive(url);
            }
        }
        es.shutdown();
        while(!es.isTerminated()){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.error("沉睡时被唤醒");
            }
        }
        logs = logs.trim();
        String[] phones = phoneNumbers.split(",");
        File signalFile = new File(AppConfigurator.getInstance().getConfig("test.errorSignalFile","/home/fortune/error"));
        if(!"".equals(logs)){
            logs = "无法播放："+logs;
            int i=0;
            while(logs.endsWith(",")){
                logs = logs.substring(0,logs.length()-1);
                i++;
                if(i>200){
                    logger.warn("难道是字符串太长？反正是不正常,长度："+logs.length()+",消息："+logs);
                    break;
                }
            }
            String willSendSms = logs;
            logs+=","+successLogs;
            if(!signalFile.exists()){
                FileUtils.writeNew(signalFile.getParentFile().getAbsolutePath(),signalFile.getName(),StringUtils.date2string(new Date())+" - "+logs);
            }else{
                int count = FileUtils.getLineCount(signalFile);
                if(count==5){
                    //已经发送了5次了，那就不再发送了
                    willSendSms+=",已经发送了"+count+"次，将不再发送通知，直至服务恢复！";
                }else if(count>5){
                    logger.debug("发送次数已经是"+count+"次，无需再次发送！");
                    willSendSms = null;
                }else{
                    logger.debug("错误了" +count+
                            "次，即将发送的短信内容是："+willSendSms);
                    willSendSms = "错误第"+count+"次，"+willSendSms;
                }
                FileUtils.writeContinue(signalFile.getParentFile().getAbsolutePath(), signalFile.getName(),"\r\n"+ StringUtils.date2string(new Date()) + " - " + logs);
            }
            if(willSendSms!=null){
                for(String phone:phones){
                    String tempMsg = willSendSms;
                    while(tempMsg.length()>70){
                        String msg = tempMsg.substring(0,70);
                        sendSMS(phone,msg);
                        tempMsg = tempMsg.substring(71);
                    }
                    if(tempMsg.length()>0){
                        sendSMS(phone,tempMsg);
                    }
                }
            }
            logger.error("错误日志：\r\n"+logs);
        }else{
            logger.debug("测试结果正常，检查信号文件，并根据结果决定是否发送短信到"+phoneNumbers);
            if(signalFile.exists()){
                logger.debug("信号文件存在，发送短信，信号文件是："+signalFile.getAbsolutePath());
                for(String phone:phones){
                    sendSMS(phone,"直播检测结果表明，目前直播服务已经完全恢复！");
                }
                try {
                    String backupFile = signalFile.getAbsolutePath()+"."+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".log";
                    logger.debug("尝试修改信号文件名，从"+signalFile.getAbsolutePath()+"->"+backupFile);
                    if(!signalFile.renameTo(new File(backupFile))){
                        logger.warn("修改文件名发生异常："+backupFile);
                    }else{
                        logger.debug("日志文件已经改名为："+backupFile);
                    }
                } catch (Exception e) {
                    logger.error("尝试修改信号文件名时发生异常："+e.getMessage());
                }
            }else{
                logger.debug("测试完毕，无需发送消息！");
            }
        }
    }
    /**
     * 发送短信
     * @param phoneNumber 手机号码
     * @param message        短信内容
     * @return              是否发送成功
     *
     */
    public boolean sendSMS(String phoneNumber,String message){
        AppConfigurator config = AppConfigurator.getInstance();
        String smgwUrl = config.getConfig("SGIP_SMGW_URL","http://61.55.144.87/smgw/?resultXmlFormat=true&command=send");
        ServerMessager messager = new ServerMessager();
        logger.debug("请求发送："+phoneNumber+","+smgwUrl+","+message);
        String result = messager.postToHost(smgwUrl,"phoneNumber="+phoneNumber+"&message="+message,config.getConfig("SGIP_SMGW_ENCODING","utf-8"));
        logger.debug("服务器返回信息："+result);
        return !(result==null||"".equals(result.trim()));
    }

    public static void main(String[] args){
        TestLive testLive =new TestLive();
        testLive.doTest();
    }
}
