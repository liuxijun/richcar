package com.fortune.server.protocol;

import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by xjliu on 2014/9/7.
 * Ԥ��ӰƬ��ʵʱ����
 */
public class PreviewForHLS {
    private Logger logger = Logger.getLogger(getClass());
    private static final Map<String,PreviewWorker> sessions= new HashMap<String,PreviewWorker>();
    private boolean scanning = false;
    private String lastLog = "";
    private static final PreviewForHLS instance = new PreviewForHLS();
    public static PreviewForHLS getInstance(){
        return instance;
    }
    private PreviewForHLS(){

    }

    public String getLastLog(){
        return lastLog;
    }
    public Map<String, PreviewWorker> getSessions() {
        return sessions;
    }

    public SimpleFileInfo updateWorker(String url){
        PreviewWorker worker = sessions.get(url);
        if(worker!=null){
            worker.setLastVisitTime(System.currentTimeMillis());
//            logger.debug("updateWorkerʱ�������Ѿ�ˢ�£�"+url);
            return worker.getSourceFileInfo();
        }else{
            logger.warn("updateWorkerʱ�����޷��ҵ��������"+url);
            return null;
        }
    }

    /**
     * ��ʱɨ�裬�����г�ʱ�ģ�ֹͣ���ת������
     */
    public void scanSessions(){
        long now = System.currentTimeMillis();
        int removeCount=0;
        List<String> keys = new ArrayList<String>(sessions.size());
        for(String key:sessions.keySet()){
            keys.add(key);
        }
        for(String key:keys){
            try {
                PreviewWorker worker = sessions.get(key);
                long lastVisitTime = worker.getLastVisitTime();
                if(now-lastVisitTime>30*1000){//��ʮ�뻹û�й�ʹ�ã���ͣ����
                    logger.warn("����ʱ��"+worker.getSourceFileInfo().getName()+",���һ�η��ʣ�"
                                    +StringUtils.date2string(worker.getLastVisitTime())+",��ǰʱ�䣺"+
                                    StringUtils.date2string(now)
                    );
                    worker.clear(worker.getTsPathName());
                    worker.stopWork();
                    sessions.remove(key);
                    removeCount++;
                }
            } catch (Exception e) {
                logger.error("ɨ������з����쳣��"+e);
            }
        }
        keys.clear();
        if(removeCount>0){
            logger.debug("�����������"+removeCount);
//        }else{
//            logger.debug("û������κ�����");
        }
    }

    public PreviewWorker querySession(String url){
        return sessions.get(url);
    }

    public int playPreview(String fileId,String url,String previewRootPathName,HttpServletResponse response,
                    HttpServletRequest request,JspWriter out){
        lastLog="";
        int targetDuration = AppConfigurator.getInstance().getIntConfig("system.tools.preview.targetDuration",5);
        if(fileId==null){
            lastLog+="\r\n"+("�ļ�id�������û��pos������"+request.getQueryString());
        }else{
            //ֻ��m3u8�ļ����Ѿ������˵ģ����ܶ�����������û�����ɵĻ����������ɵ�
            updateWorker(url);
            long requestId = StringUtils.string2long(fileId,0);
            PreviewWorker worker = querySession(url);
            if(worker==null){
                logger.warn("û���ҵ����񣬳�������������"+url);
                long startTime = requestId*targetDuration-targetDuration;
                if(startTime<0){
                    startTime = 0;
                }
                SimpleFileInfo fileInfo = startSession(url,previewRootPathName,startTime,targetDuration);
                if(fileInfo == null){
                    worker = null;
                }else{
                    worker = querySession(url);
                }
            }
            if(worker!=null){
                String m3u8FileName = worker.getM3u8FileName();
                String tsPath = worker.getTsPathName();
                File m3u8 = new File(m3u8FileName);
                long startSeconds = worker.getStartSeconds();
                int segmentDuration = worker.getSegmentDuration();
                if(startSeconds>0){
                    long newId = StringUtils.string2int(fileId,0)-(startSeconds/segmentDuration)+1;
                    if(newId<0){
                        logger.error("���Խ���fileId����ʱ�����쳣��fileID="+fileId+",newId="+newId);
                        newId = 0;
                    }
                    String newIdStr = String.format("%04d",newId);
                    logger.debug("����һ����ͷ��ʼ�����񣬽���fileId����������������ʱ�䣺" +startSeconds+
                            ",�����fileId="+fileId+",�������ID="+newIdStr);
                    fileId = newIdStr;
                }
                if(m3u8.exists()){
                    //logger.debug("m3u8�ļ����ڣ�����Ƿ�����ļ���"+fileId+".ts");
                    int tryTimes = 0;
                    while(tryTimes<3){
                        String m3u8Content = FileUtils.readFileInfo(m3u8.getAbsolutePath());
                        M3U8Stream m3U8Stream = new M3U8Stream(0,0,url,m3u8Content);
                        List<M3U8Segment> segments = m3U8Stream.getSegments();
                        int l=segments.size();
                        String tsFileName = "/"+fileId+".ts";
                        boolean tsFileCreated = false;
                        for(int i=l-1;i>=0;i--){
                            M3U8Segment segment = segments.get(i);
                            if(segment.getUrl().contains(tsFileName)){
                                tsFileCreated = true;
                                break;
                            }
                        }

                        if(tsFileCreated){
                            //�����������˵�����ts�ļ��Ѿ������ˡ����û�У�
                            // ��Ҫ���ǿ�������ж���������30�룬��Ҫ���±���
                            String fileName = tsPath+"/"+ fileId+".ts";
                            File tsFile = new File(fileName);
//                            logger.debug("m3u8�а��������ts��׼����ȡ�ļ���"+tsFile.getAbsolutePath());
                            if(tsFile.exists()&&(!tsFile.isDirectory())&&tsFile.length()>0){
//                                logger.debug("TS�ļ����ڣ�׼�����ͣ�"+tsFile.getAbsolutePath()+",��С��"+tsFile.length());
                                try {
                                    OutputStream dataOutput = response.getOutputStream();
                                    response.setHeader("Content-Disposition","attachment; filename="+fileId+".ts");
                                    response.setContentType("application/x-mpegURL");
                                    response.setContentLength((int) tsFile.length());
                                    try {
                                        InputStream   in   =   new FileInputStream(tsFile);
                                        byte[] dataBuffer = new byte[102400];
                                        int readedLength = 1;
                                        while(readedLength>0){
                                            readedLength = in.read(dataBuffer);
                                            dataOutput.write(dataBuffer,0,readedLength);
                                            if(readedLength<dataBuffer.length){
                                                break;
                                            }
                                        }
                                        in.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    dataOutput.flush();
                                    dataOutput.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    logger.error(e.getMessage());
                                }
                                return 0;
                            }else{
                                lastLog+="\r\n"+("ts�ļ���û����ȷ���ɣ�name="+tsFile.getAbsolutePath()
                                        +",isDirectory="+tsFile.isDirectory()+",size="+tsFile.length());
                            }
                        }else{
                            if(l>0){
                                M3U8Segment segment = segments.get(l-1);
                                //�ҵ����һ��segment���������ID�͵�ǰ��ID�Ĳ���������5�������������µ�ת������
                                String segmentUrl = segment.getUrl();
                                if(segmentUrl!=null){
                                    int p=segmentUrl.lastIndexOf("/");
                                    if(p>=0){
                                        segmentUrl = segmentUrl.substring(p+1);
                                        p = segmentUrl.lastIndexOf(".ts");
                                        if(p>0){
                                            segmentUrl = segmentUrl.substring(0,p);
                                            int segmentTsId = StringUtils.string2int(segmentUrl,-2);
                                            if(segmentTsId>=0){
                                                int requestTsIdFromStartTimeInSession = StringUtils.string2int(fileId,-1)-1;
                                                if(requestTsIdFromStartTimeInSession>=0){
                                                    if((requestTsIdFromStartTimeInSession-segmentTsId)>3){
                                                        logger.debug("������ļ���δ���ɣ�������������ת�룺requestId=" +
                                                                requestId+
                                                                ",requestTsIdFromStartTimeInSession="
                                                                +requestTsIdFromStartTimeInSession+",lastTsIdInM3U8="+segmentTsId);
                                                        //ע��requestId����������ȫ��ӰƬ�ģ������ǵ�ǰ�޸Ĺ���ʼʱ����Ǹ�requestTsId
                                                        worker.clear(worker.getTsPathName());
                                                        stopSession(url);
                                                        startSession(url,previewRootPathName,(requestId-1)*segmentDuration,segmentDuration);
                                                        worker = querySession(url);
                                                        m3u8 = new File(worker.getM3u8FileName());
                                                        logger.debug("���³�ʼ��m3u8�ļ���"+m3u8.getAbsolutePath());
                                                    }
                                                }else{
                                                    logger.error("�����TS�ļ�ID����"+fileId);
                                                }
                                            }else{
                                                logger.error("���һ��segment��ID����"+segment.getUrl()+",segmentUrl="+segmentUrl);
                                            }
                                        }else{
                                            logger.error("���һ��segment��URL��ʽ���ԣ�û��.ts��"+segmentUrl);
                                        }
                                    }else{
                                        logger.error("���һ��segment��URL��ʽ���ԣ�û��/");
                                    }
                                }else{
                                    logger.error("���һ��segment��URL�ǿգ�");
                                }
                            }else{
                                logger.error("��û��segment��m3u8�ļ��У�"+m3u8FileName);
                            }
                        }
                        tryTimes ++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    lastLog+="\r\n"+("û���ҵ�m3u8�ļ����޷����к���������"+m3u8.getAbsolutePath());
                }
            }else{
                lastLog+="\r\nû���������"+url;
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.setStatus(404);
        try {
            out.println("<html><body><p>" +lastLog+
                    "</p></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return 1;
    }
    public int startPreview(String localRootPathName,String sourceFileName,String previewRootPathName,
                                      long startSeconds,long deviceId,boolean debugMode,
                                      JspWriter out){
        int targetDuration = AppConfigurator.getInstance().getIntConfig("system.tools.preview.targetDuration",5);
        String url = new File(localRootPathName+"/"+sourceFileName).getAbsolutePath();
        PreviewWorker worker= querySession(url);
        SimpleFileInfo fileInfo;
        if(worker!=null){
            worker.setLastVisitTime(System.currentTimeMillis());
            fileInfo = worker.getSourceFileInfo();
        }else{
            logger.debug("׼��������url="+url+",previewRootPathName="+previewRootPathName+",startSeconds="+startSeconds);
            fileInfo = startSession(url,previewRootPathName,startSeconds,targetDuration);
            worker = querySession(url);
        }
        StringBuilder m3u8Builder = new StringBuilder();
        if(fileInfo!=null&&worker!=null){
            //response.setContentType("text/text");
            String enter="\r\n";
            if(debugMode){
                enter +="<br>";
            }
            m3u8Builder.append("#EXTM3U").append(enter);
            m3u8Builder.append("#EXT-X-VERSION:3").append(enter);
            m3u8Builder.append("#EXT-X-MEDIA-SEQUENCE:0").append(enter);
            m3u8Builder.append("#EXT-X-ALLOW-CACHE:YES").append(enter);
            m3u8Builder.append("#EXT-X-TARGETDURATION:").append(targetDuration).append(
                    ".0").append(enter);
            int i=0;
            int segmentCount = (((int)fileInfo.getLength())+targetDuration-1) / targetDuration;
            int durationLeft = (int)fileInfo.getLength();
            if(segmentCount>0){
                for(;i<segmentCount;i++){
                    int segmentDuration = targetDuration;
                    if(durationLeft<targetDuration){
                        segmentDuration = durationLeft;
                    }
                    durationLeft -= targetDuration;
                    m3u8Builder.append("#EXTINF:").append(segmentDuration).append(",").append(enter);
                    String segmentUrl;
                    try {
                        segmentUrl = "preview.jsp?command=play&pos="+String.format("%04d",i)+
                                "&start="+(i*segmentDuration)+"&deviceId="+deviceId+
                                "&url="+ URLEncoder.encode(URLEncoder.encode(sourceFileName, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        segmentUrl ="preview.jsp?command=play&pos="+String.format("%04d",i)+
                                "&start="+(i*segmentDuration)+"&deviceId="+deviceId+
                                "&url=base64:"+ MD5Utils.bufferToHex(Base64.encode(sourceFileName.getBytes()));
                    }
                    if(debugMode){
                        segmentUrl = "<a href='"+segmentUrl+"'>"+segmentUrl+"</a>";
                    }
                    m3u8Builder.append(segmentUrl).append(enter);
                }
            }else{
                logger.error("�޷���ȡ����ӰƬ��Ƭ��������ֻ�ܱ����ˣ�"+url);
                m3u8Builder.append("#EXTINF:10,").append(enter).append("preview.jsp?command=error&message=SegmentCountIsZero");
            }
            m3u8Builder.append("#EXT-X-ENDLIST").append(enter);
            if(debugMode){
                lastLog = m3u8Builder.toString();
            }else{
                //���������Եȴ����룬ֱ��tsĿ¼����3���ļ�
                File tsDir = new File(worker.getTsPathName());
                File[] tsFiles = tsDir.listFiles();
                int times=0;
                while(tsFiles==null||tsFiles.length<2){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tsFiles = tsDir.listFiles();
                    times ++;
                    if(times>5){
                        break;
                    }
                }
                if(tsFiles!=null){
                    logger.debug("��ǰĿ¼�����ļ�������"+tsFiles.length+"��,Ŀ¼Ϊ��"+tsDir.getAbsolutePath());
                }
                try {
                    out.print(m3u8Builder.toString());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return 0;
            }
        }else {
            logger.error("����ʧ��:"+lastLog);
        }
        return 1;
    }
    public SimpleFileInfo startSession(String url,String previewRootPathName,long startSeconds,int segmentDuration){
        File sourceFile = new File(url);
        url = sourceFile.getAbsolutePath();
        if(!sourceFile.exists()){
            logger.error("�ļ������ڣ�"+url);
            return null;
        }
        String mediaPath = "";
        try {
            mediaPath+= MD5Utils.getMD5String(url + "_" + startSeconds);
        } catch (NoSuchAlgorithmException e) {
            mediaPath += url+"_"+startSeconds;
        }
        File tsPathFile = new File(previewRootPathName+"/"+mediaPath);
        String tsPathName = tsPathFile.getAbsolutePath();
        String m3u8FileName= tsPathName+".m3u8";

        File m3u8File = new File(m3u8FileName);
        File m3u8FilePath = m3u8File.getParentFile();
        if(!m3u8FilePath.exists()){
            if(m3u8FilePath.mkdirs()){
                logger.debug("Ŀ¼�����ɹ���"+m3u8FilePath.getAbsolutePath());
            }else{
                logger.error("�޷�����Ŀ¼��"+m3u8FilePath.getAbsolutePath());
            }
        }else{
            logger.debug("Ŀ¼�Ѿ����ڣ�"+m3u8FilePath.getAbsolutePath());
        }
        m3u8FileName = m3u8File.getAbsolutePath();
        //File tsPathFile = new File(tsPathName);
        if(!tsPathFile.exists()){
            if(tsPathFile.mkdirs()){
                logger.debug("����Ŀ¼�ɹ���"+tsPathFile.getAbsolutePath());
            }else{
                logger.error("�޷�����Ŀ¼��"+tsPathFile.getAbsolutePath());
            }
        }else{
            logger.debug("Ŀ¼�Ѿ����ڣ�"+tsPathFile.getAbsolutePath());
        }
        tsPathName = tsPathFile.getAbsolutePath()+"";
        SimpleFileInfo sourceFileInfo = new SimpleFileInfo(sourceFile);
        if(!FileUtils.setFileMediaInfo(url, sourceFileInfo)){
            logger.error("�޷�̽��ý����Ϣ��"+url);
            return null;
        }else{
            if(sourceFileInfo.getLength()<=0.0f){
                logger.warn("û����ȷ�Ļ�ȡʱ�������Ѿ���ʼת�룬�����ļ�Ӧ���ǿ����ã����Ƕ�ȡʱ������Ϣ����Ϊ��Ԥ��������ʱ��Ϊ10����");
                sourceFileInfo.setLength(10.0f*60);
            }
        }
        stopSession(url);
        PreviewWorker worker = new PreviewWorker(sourceFileInfo,m3u8FileName,tsPathName,startSeconds,segmentDuration);
        worker.start();
        sessions.put(url,worker);
        if(!scanning){
            synchronized (sessions){
                if(!scanning){
                    scanning = true;
                    new Thread(){
                        public void run(){
                            logger.debug("��������ʱɨ�裡");
                            do{
                                try {
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                scanSessions();
                            }while(sessions.size()>0);
                            scanning =false;
                            logger.debug("������Ϊ0���˳�ɨ�裡");
                        }
                    }.start();
                }
            }
        }
        return sourceFileInfo;
    }
    public boolean stopSession(String url){
        if(url==null){
            return false;
        }
        logger.debug("����ֹͣ��"+url);
        File file = new File(url);
        url = file.getAbsolutePath();
        PreviewWorker worker = sessions.get(url);
        if(worker!=null){
            worker.stopWork();
            logger.debug("ֹͣ�����Ѿ����ͣ�"+url);
            sessions.remove(url);
            return true;
        }else{
            logger.error("û���������"+url);
            for(String key:sessions.keySet()){
                logger.debug("Ŀǰ�����У�"+key);
            }
            return false;
        }
    }

}
