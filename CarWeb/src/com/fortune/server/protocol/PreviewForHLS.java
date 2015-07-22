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
 * 预览影片，实时编码
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
//            logger.debug("updateWorker时，任务已经刷新："+url);
            return worker.getSourceFileInfo();
        }else{
            logger.warn("updateWorker时发现无法找到这个任务："+url);
            return null;
        }
    }

    /**
     * 定时扫描，发现有超时的，停止这个转码任务
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
                if(now-lastVisitTime>30*1000){//三十秒还没有过使用，就停了它
                    logger.warn("任务超时："+worker.getSourceFileInfo().getName()+",最后一次访问："
                                    +StringUtils.date2string(worker.getLastVisitTime())+",当前时间："+
                                    StringUtils.date2string(now)
                    );
                    worker.clear(worker.getTsPathName());
                    worker.stopWork();
                    sessions.remove(key);
                    removeCount++;
                }
            } catch (Exception e) {
                logger.error("扫描过程中发生异常："+e);
            }
        }
        keys.clear();
        if(removeCount>0){
            logger.debug("清除任务数："+removeCount);
//        }else{
//            logger.debug("没有清除任何任务！");
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
            lastLog+="\r\n"+("文件id输入错误，没有pos参数："+request.getQueryString());
        }else{
            //只有m3u8文件中已经存在了的，才能读到，否则都是没有生成的或者正在生成的
            updateWorker(url);
            long requestId = StringUtils.string2long(fileId,0);
            PreviewWorker worker = querySession(url);
            if(worker==null){
                logger.warn("没有找到任务，尝试重新启动："+url);
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
                        logger.error("尝试进行fileId修正时发生异常：fileID="+fileId+",newId="+newId);
                        newId = 0;
                    }
                    String newIdStr = String.format("%04d",newId);
                    logger.debug("不是一个从头开始的任务，进行fileId的修正：任务启动时间：" +startSeconds+
                            ",输入的fileId="+fileId+",修正后的ID="+newIdStr);
                    fileId = newIdStr;
                }
                if(m3u8.exists()){
                    //logger.debug("m3u8文件存在，检查是否包含文件："+fileId+".ts");
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
                            //如果包含，就说明这个ts文件已经生成了。如果没有，
                            // 就要考虑看看差距有多大，如果超过30秒，就要重新编码
                            String fileName = tsPath+"/"+ fileId+".ts";
                            File tsFile = new File(fileName);
//                            logger.debug("m3u8中包含了这个ts，准备读取文件："+tsFile.getAbsolutePath());
                            if(tsFile.exists()&&(!tsFile.isDirectory())&&tsFile.length()>0){
//                                logger.debug("TS文件存在，准备发送："+tsFile.getAbsolutePath()+",大小："+tsFile.length());
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
                                lastLog+="\r\n"+("ts文件还没有正确生成：name="+tsFile.getAbsolutePath()
                                        +",isDirectory="+tsFile.isDirectory()+",size="+tsFile.length());
                            }
                        }else{
                            if(l>0){
                                M3U8Segment segment = segments.get(l-1);
                                //找到最后一个segment，看看这个ID和当前的ID的差别。如果大于5，就重新启动新的转码任务
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
                                                        logger.debug("请求的文件尚未生成，决定重新启动转码：requestId=" +
                                                                requestId+
                                                                ",requestTsIdFromStartTimeInSession="
                                                                +requestTsIdFromStartTimeInSession+",lastTsIdInM3U8="+segmentTsId);
                                                        //注意requestId，这个是针对全部影片的，而不是当前修改过起始时间的那个requestTsId
                                                        worker.clear(worker.getTsPathName());
                                                        stopSession(url);
                                                        startSession(url,previewRootPathName,(requestId-1)*segmentDuration,segmentDuration);
                                                        worker = querySession(url);
                                                        m3u8 = new File(worker.getM3u8FileName());
                                                        logger.debug("重新初始化m3u8文件："+m3u8.getAbsolutePath());
                                                    }
                                                }else{
                                                    logger.error("请求的TS文件ID错误："+fileId);
                                                }
                                            }else{
                                                logger.error("最后一个segment的ID错误："+segment.getUrl()+",segmentUrl="+segmentUrl);
                                            }
                                        }else{
                                            logger.error("最后一个segment的URL格式不对，没有.ts："+segmentUrl);
                                        }
                                    }else{
                                        logger.error("最后一个segment的URL格式不对，没有/");
                                    }
                                }else{
                                    logger.error("最后一个segment的URL是空！");
                                }
                            }else{
                                logger.error("还没有segment在m3u8文件中："+m3u8FileName);
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
                    lastLog+="\r\n"+("没有找到m3u8文件，无法进行后续操作："+m3u8.getAbsolutePath());
                }
            }else{
                lastLog+="\r\n没有这个任务："+url;
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
            logger.debug("准备启动：url="+url+",previewRootPathName="+previewRootPathName+",startSeconds="+startSeconds);
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
                logger.error("无法获取播放影片的片段数量，只能报错了："+url);
                m3u8Builder.append("#EXTINF:10,").append(enter).append("preview.jsp?command=error&message=SegmentCountIsZero");
            }
            m3u8Builder.append("#EXT-X-ENDLIST").append(enter);
            if(debugMode){
                lastLog = m3u8Builder.toString();
            }else{
                //在这里稍稍等待几秒，直到ts目录下有3个文件
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
                    logger.debug("当前目录下有文件个数："+tsFiles.length+"个,目录为："+tsDir.getAbsolutePath());
                }
                try {
                    out.print(m3u8Builder.toString());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return 0;
            }
        }else {
            logger.error("启动失败:"+lastLog);
        }
        return 1;
    }
    public SimpleFileInfo startSession(String url,String previewRootPathName,long startSeconds,int segmentDuration){
        File sourceFile = new File(url);
        url = sourceFile.getAbsolutePath();
        if(!sourceFile.exists()){
            logger.error("文件不存在："+url);
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
                logger.debug("目录创建成功："+m3u8FilePath.getAbsolutePath());
            }else{
                logger.error("无法创建目录："+m3u8FilePath.getAbsolutePath());
            }
        }else{
            logger.debug("目录已经存在："+m3u8FilePath.getAbsolutePath());
        }
        m3u8FileName = m3u8File.getAbsolutePath();
        //File tsPathFile = new File(tsPathName);
        if(!tsPathFile.exists()){
            if(tsPathFile.mkdirs()){
                logger.debug("创建目录成功："+tsPathFile.getAbsolutePath());
            }else{
                logger.error("无法创建目录："+tsPathFile.getAbsolutePath());
            }
        }else{
            logger.debug("目录已经存在："+tsPathFile.getAbsolutePath());
        }
        tsPathName = tsPathFile.getAbsolutePath()+"";
        SimpleFileInfo sourceFileInfo = new SimpleFileInfo(sourceFile);
        if(!FileUtils.setFileMediaInfo(url, sourceFileInfo)){
            logger.error("无法探查媒体信息："+url);
            return null;
        }else{
            if(sourceFileInfo.getLength()<=0.0f){
                logger.warn("没有正确的获取时长，但已经开始转码，所以文件应该是可以用，但是读取时长等信息出错，为了预览，设置时长为10分钟");
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
                            logger.debug("开启任务超时扫描！");
                            do{
                                try {
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                scanSessions();
                            }while(sessions.size()>0);
                            scanning =false;
                            logger.debug("任务数为0，退出扫描！");
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
        logger.debug("尝试停止："+url);
        File file = new File(url);
        url = file.getAbsolutePath();
        PreviewWorker worker = sessions.get(url);
        if(worker!=null){
            worker.stopWork();
            logger.debug("停止命令已经发送："+url);
            sessions.remove(url);
            return true;
        }else{
            logger.error("没有这个任务："+url);
            for(String key:sessions.keySet()){
                logger.debug("目前任务有："+key);
            }
            return false;
        }
    }

}
