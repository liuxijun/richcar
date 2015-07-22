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
 * ֱ�����Ų���
 */
public class TestLive {
    private String[] urls;
    private Logger logger = Logger.getLogger(getClass());
    private ExecutorService es = Executors.newFixedThreadPool(AppConfigurator.getInstance().getIntConfig(
            "test.workerThreadCount", 5));
    private String phoneNumbers ;
    private String logs="", successLogs ="�ܳɹ����ŵ�Ƶ������";
    private Map<String,String> errorChannels=new HashMap<String,String>();//����Ƶ�����ݣ�key�Ǻ��룬ֵ�Ǵ���Ƶ���б�
    public void init(){
        phoneNumbers = AppConfigurator.getInstance().getConfig("test.sendToPhoneNumbers","15613199253");
        urls = AppConfigurator.getInstance().getConfig("test.liveUrls",
                "http://61.55.145.163/live/hls/1032.m3u8?name=�㽭���ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1010.m3u8?name=�ӱ�����,"+
                        "http://61.55.145.163/live/hls/1001.m3u8?name=��������,"+
                        "http://61.55.145.163/live/hls/1004.m3u8?name=�������ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1015.m3u8?name=�������ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1038.m3u8?name=CCTV6,"+
                        "http://61.55.145.163/live/hls/1002.m3u8?name=�������ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1013.m3u8?name=��������,"+
                        "http://61.55.145.163/live/hls/1027.m3u8?name=�������,"+
                        "http://61.55.145.163/live/hls/1016.m3u8?name=��������,"+
                        "http://61.55.145.163/live/hls/1022.m3u8?name=ɽ�����ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1025.m3u8?name=�������ӣ����壩,"+
                        //"http://61.55.145.163/live/hls/1092.m3u8?name=NBAֱ����2Ƶ��,"+
                        "http://61.55.145.163/live/hls/1136.m3u8?name=CCTV3,"+
                        "http://61.55.145.163/live/hls/1046.m3u8?name=��������,"+
                        "http://61.55.145.163/live/hls/1041.m3u8?name=CCTV13,"+
                        "http://61.55.145.163/live/hls/1048.m3u8?name=CCTV5����,"+
                        "http://61.55.145.163/live/hls/1081.m3u8?name=ŷ��Ժ��,"+
                        "http://61.55.145.163/live/hls/1086.m3u8?name=����Ƶ��,"+
                        "http://61.55.145.163/live/hls/1042.m3u8?name=CCTV14,"+
                        "http://61.55.145.163/live/hls/1084.m3u8?name=����Ƶ��,"+
                        "http://61.55.145.163/live/hls/1039.m3u8?name=CCTV8,"+
                        "http://61.55.145.163/live/hls/1040.m3u8?name=CCTV9,"+
                        "http://61.55.145.163/live/hls/1047.m3u8?name=ŷ������,"+
                        "http://61.55.145.163/live/hls/1043.m3u8?name=�������ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1035.m3u8?name=CCTV-1�����壩,"+
                        "http://61.55.145.163/live/hls/1055.m3u8?name=�����㳡,"+
                        "http://61.55.145.163/live/hls/1053.m3u8?name=��Ц�糡,"+
                        "http://61.55.145.163/live/hls/1056.m3u8?name=�������� ,"+
                        "http://61.55.145.163/live/hls/1054.m3u8?name=��������,"+
                        "http://61.55.145.163/live/hls/1057.m3u8?name=��ʵƵ�� ,"+
                        "http://61.55.145.163/live/hls/1058.m3u8?name=��ӥ��ͨ ,"+
                        "http://61.55.145.163/live/hls/1059.m3u8?name=CCTV2,"+
                        "http://61.55.145.163/live/hls/1007.m3u8?name=�㶫���ӣ����壩,"+
                        "http://61.55.145.163/live/hls/1045.m3u8?name=���������ӣ����壩"
        ).split(",");
    }
    public void onError(String message,String url){
        logger.error("��������"+message+","+url);
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
        logger.debug("����ͨ����"+url);
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
            logger.debug("�������ԣ�"+url);
            ServerMessager messager = new ServerMessager();
            M3U8 m3u8 = new M3U8();
            //logger.debug("�������أ�"+url);
            Map<Integer,Integer> segmentSequences = new HashMap<Integer, Integer>();
            float targetDuration=6.0f;
            int testTimes = 0;
            boolean sequenceChanged = false;
            while(testTimes<5){
                testTimes++;
                logger.debug("���ڽ��е�" +testTimes+
                        "������M3U8��"+url);
                String m3u8Content = messager.postToHost(url,null);
                //logger.debug("���صĽ���ǣ�"+m3u8Content);
                if(m3u8Content!=null&&!"".equals(m3u8Content.trim())){
                    m3u8.addStream(0, 1, url, m3u8Content);
                    List<M3U8Stream> streams = m3u8.getStreams();
                    if(streams==null||streams.size()<=0){
                        onError("m3u8���ݽ�������",url);
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
                                    logger.debug("sequence�����仯���ϴε�sequence="+lastSequence+",���ڵ�sequence="+sequence);
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
                                onError("����segementʱ�����쳣��û��ȡ���κε�Ƭ����Ϣ",url);
                            }else{
                                int startIdx = segments.size()-3;
                                if(startIdx<0){
                                    startIdx=0;
                                }
                                int size = segments.size();
                                logger.debug("�����������е�Ƭ�Σ�startIdx = " +startIdx+
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
                                        //��������5�λ����У�����Ϊʧ����
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
                                logger.debug("�����������е�Ƭ����ϣ�url="+url);
                            }
                        }
                        if(!sequenceChanged){
                            logger.warn("sequenceû�б仯����Ҫ�ȴ�һ�½�����һ���Ĳ��ԣ��ȴ�ʱ���ǣ�"+targetDuration+"�룬url="+url);
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
                onError("�޷�����M3U8�б�", url);
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
                    logger.debug("��"+testTimes+"�β���"+url);
                }
                success = doTest(url);
                if(success){
                    break;
                }
                if(testTimes>0){
                    logger.debug("��"+testTimes+"�β���ʧ�ܣ�"+url);
                }
                try {
                    Thread.sleep(12000L);
                } catch (InterruptedException e) {
                    logger.debug("�޷��ȴ�����������");
                }
            }
            if(!success){
                onError("�����쳣",url);
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
                    //logger.debug("���Է��ʣ�"+url);
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
                        logger.error("HTTP����������"+code+"," +
                                errorMsg);
                    }
                    //data = new String(d);
                    con.disconnect();
                    break;
                } catch (Exception ex) {
                    logger.error("�޷����ӣ�" + url+","+ex.getMessage());
                    //onError("�޷�����",url);
                    result.setResultCode(500);
                    //ex.printStackTrace();
                }
            }
            result.setStopTime(System.currentTimeMillis());
            long duration = result.getStopTime()-result.getStartTime();
            if(duration>30000){
                logger.warn("�˴η���ʱ�������"+duration+"ms,"+result);
            }
/*
            if(duration>0){
                logger.debug("����" +url+"��ϣ�����"+ StringUtils.formatBPS(result.getContentLength() * 8 * 1000 / duration)+",ʱ��" +duration+"ms,��С��"+(result.getContentLength())+"Bytes");
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
                    logger.debug("������ӱ�ע���ˣ����ý��в��ԣ�"+url);
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
                logger.error("��˯ʱ������");
            }
        }
        logs = logs.trim();
        String[] phones = phoneNumbers.split(",");
        File signalFile = new File(AppConfigurator.getInstance().getConfig("test.errorSignalFile","/home/fortune/error"));
        if(!"".equals(logs)){
            logs = "�޷����ţ�"+logs;
            int i=0;
            while(logs.endsWith(",")){
                logs = logs.substring(0,logs.length()-1);
                i++;
                if(i>200){
                    logger.warn("�ѵ����ַ���̫���������ǲ�����,���ȣ�"+logs.length()+",��Ϣ��"+logs);
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
                    //�Ѿ�������5���ˣ��ǾͲ��ٷ�����
                    willSendSms+=",�Ѿ�������"+count+"�Σ������ٷ���֪ͨ��ֱ������ָ���";
                }else if(count>5){
                    logger.debug("���ʹ����Ѿ���"+count+"�Σ������ٴη��ͣ�");
                    willSendSms = null;
                }else{
                    logger.debug("������" +count+
                            "�Σ��������͵Ķ��������ǣ�"+willSendSms);
                    willSendSms = "�����"+count+"�Σ�"+willSendSms;
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
            logger.error("������־��\r\n"+logs);
        }else{
            logger.debug("���Խ������������ź��ļ��������ݽ�������Ƿ��Ͷ��ŵ�"+phoneNumbers);
            if(signalFile.exists()){
                logger.debug("�ź��ļ����ڣ����Ͷ��ţ��ź��ļ��ǣ�"+signalFile.getAbsolutePath());
                for(String phone:phones){
                    sendSMS(phone,"ֱ�������������Ŀǰֱ�������Ѿ���ȫ�ָ���");
                }
                try {
                    String backupFile = signalFile.getAbsolutePath()+"."+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".log";
                    logger.debug("�����޸��ź��ļ�������"+signalFile.getAbsolutePath()+"->"+backupFile);
                    if(!signalFile.renameTo(new File(backupFile))){
                        logger.warn("�޸��ļ��������쳣��"+backupFile);
                    }else{
                        logger.debug("��־�ļ��Ѿ�����Ϊ��"+backupFile);
                    }
                } catch (Exception e) {
                    logger.error("�����޸��ź��ļ���ʱ�����쳣��"+e.getMessage());
                }
            }else{
                logger.debug("������ϣ����跢����Ϣ��");
            }
        }
    }
    /**
     * ���Ͷ���
     * @param phoneNumber �ֻ�����
     * @param message        ��������
     * @return              �Ƿ��ͳɹ�
     *
     */
    public boolean sendSMS(String phoneNumber,String message){
        AppConfigurator config = AppConfigurator.getInstance();
        String smgwUrl = config.getConfig("SGIP_SMGW_URL","http://61.55.144.87/smgw/?resultXmlFormat=true&command=send");
        ServerMessager messager = new ServerMessager();
        logger.debug("�����ͣ�"+phoneNumber+","+smgwUrl+","+message);
        String result = messager.postToHost(smgwUrl,"phoneNumber="+phoneNumber+"&message="+message,config.getConfig("SGIP_SMGW_ENCODING","utf-8"));
        logger.debug("������������Ϣ��"+result);
        return !(result==null||"".equals(result.trim()));
    }

    public static void main(String[] args){
        TestLive testLive =new TestLive();
        testLive.doTest();
    }
}
