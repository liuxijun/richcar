package com.fortune.server.protocol;

import com.fortune.util.AppConfigurator;
import com.fortune.util.FileUtils;
import com.fortune.util.SimpleFileInfo;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2014/9/8.
 * 预览主线程
 */
public class PreviewWorker extends Thread {
    private Logger logger = Logger.getLogger(getClass());
    private OutputStream os;
    private SimpleFileInfo sourceFileInfo;
    private String m3u8FileName;
    private String tsPathName;
    private long startSeconds;
    private int segmentDuration;
    private long lastVisitTime = 0;

    public PreviewWorker(SimpleFileInfo sourceFileInfo,String m3u8FileName,String tsPathName,
                         long startSeconds,int segmentDuration){
        this.sourceFileInfo = sourceFileInfo;
        this.m3u8FileName = m3u8FileName;
        this.tsPathName = tsPathName;
        this.startSeconds=startSeconds;
        this.segmentDuration = segmentDuration;
    }
    public void processLine(String line){
//        logger.debug(line);
    }
    public void run(){
        //setName("preview:"+sourceFileInfo.getName());
        lastVisitTime = System.currentTimeMillis();
        //假设，ts文件保存在m3u8文件目录下的某个目录里
        String prefix = tsPathName.substring(FileUtils.extractFilePath(m3u8FileName.replace('\\', '/'), "/").length()+1);
        if(!"".equals(prefix)){
            prefix +="/";
        }
        prefix = prefix.replace('\\','/');
        AppConfigurator config = AppConfigurator.getInstance();
        int videoBandwidth = config.getIntConfig("system.tools.preview.videoBandwidth",1024);
        int videoWidth=config.getIntConfig("system.tools.preview.videoWidth",720);
        int videoHeight=config.getIntConfig("system.tools.preview.videoHeight",videoWidth*3/4);
        int audioBandwidth = config.getIntConfig("system.tools.preview.audioBandwidth",96);
        String sourceFileUrl = sourceFileInfo.getName();
        if(sourceFileInfo.getWidth()>0){
            videoHeight = videoWidth * sourceFileInfo.getHeight()/sourceFileInfo.getWidth();
        }else{
            logger.error("没有影片宽度信息，放弃设置："+sourceFileUrl);
        }
        //高度和长度必须是偶数。为了安全起见，设为4的整数倍
        videoWidth = videoWidth - videoWidth %4;
        videoHeight = videoHeight - videoHeight%4;
        //计算带宽情况
        if(sourceFileInfo.getLength()>0){
            float fileBandwidth = sourceFileInfo.getSize()*8/sourceFileInfo.getLength()/1024;
            if(fileBandwidth<(videoBandwidth+audioBandwidth)){
                logger.warn("影片带宽小于" +(videoBandwidth+audioBandwidth)+
                        "Kbps，进行缩减到："+fileBandwidth+"Kbps");
                audioBandwidth = 64;
                videoBandwidth = (int) fileBandwidth-audioBandwidth;
            }else{
                logger.debug("影片带宽为："+Math.round(fileBandwidth)+"kbps");
            }
        }else{
            logger.error("无法获取影片长度信息："+sourceFileUrl);
        }
        String cmd = AppConfigurator.getInstance().getConfig("system.tools.preview.cmdLine",
                "\"C:\\FFMpeg\\bin\\ffmpeg.exe\" -ss %startSeconds% -i \"%sourceFileName%\" -y -vcodec libx264 -profile main -level 30 -vb %videoBandwidth%k" +
                " -s %videoWidth%x%videoHeight% -acodec aac -strict -2 -ab %audioBandwidth%k -ac 2 -ar 48000 " +
                " -flags -global_header -segment_format mpegts -map 0 -map -0:d -f segment " +
                " -segment_list \"%m3u8FileName%\" " +
                " -segment_time %segmentDuration% -segment_list_flags +live " +
                " -segment_list_entry_prefix \"%prefix%\"" +
                " -bsf h264_mp4toannexb  \"%tsPathName%\"");
        cmd = cmd.replace("%sourceFileName%", sourceFileUrl).replace("%videoBandwidth%",videoBandwidth+"");
        cmd = cmd.replace("%videoWidth%",""+videoWidth).replace("%videoHeight%",videoHeight+"")
                .replace("%audioBandwidth%", audioBandwidth + "");
        cmd = cmd.replace("%segmentDuration%",""+segmentDuration);
        cmd = cmd.replace("%m3u8FileName%",m3u8FileName).replace("%startSeconds%", "" + startSeconds)
                .replace("%prefix%", prefix).replace("%tsPathName%", tsPathName + "/%04d.ts");
        logger.debug("尝试执行："+cmd);
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commandLine = new ArrayList<String>();
        //final List<String> result = new ArrayList<String>();
        //String[] allCmd = cmd.split(" ");
        //Collections.addAll(commandLine, allCmd);
        String currentPath = FileUtils.extractFilePath(m3u8FileName,"/");
        builder.directory(new File(currentPath));
        //扫描命令行，分解出一个个的参数
        int i=0,l=cmd.length();
        String param = "";
        boolean paramStart = false;
        while(i<l){
            char ch = cmd.charAt(i);
            if(ch=='"'){
                if(paramStart){
                    paramStart = false;
                    commandLine.add(param);
                }else{
                    paramStart = true;
                }
                param = "";
            }else if(ch==' '){
                if(paramStart){
                    param+=ch;
                }else{
                    if(!"".equals(param)){
                        commandLine.add(param);
                        param = "";
                    }
                }
            }else{
                param += ch;
            }
            i++;
        }
        if(!"".equals(param)){
            commandLine.add(param);
        }
/*
        for(String c:commandLine){
             logger.debug("执行参数："+c);
        }
*/
        logger.debug("启动：" + cmd);
        builder.command(commandLine);
        try {
            Process process = builder.start();
            Date startTime = new Date();
            final InputStream is1 = process.getInputStream();
            final InputStream is2 = process.getErrorStream();
            os = process.getOutputStream();
            new Thread() {
                public void run() {
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(is1));
                    try {
                        String lineB;
                        while ((lineB = br.readLine()) != null) {
                            //logger.debug("inputStream - "+lineB);
                            //result.add(lineB);
                            //result.add(lineB);
                            processLine(lineB);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    BufferedReader br2 = new BufferedReader(new
                            InputStreamReader(is2));
                    try {
                        String lineC;
                        while ((lineC = br2.readLine()) != null) {
                            //logger.debug("errorStream - "+lineC);
                            //result.add(lineC);
                            //result.add(lineC);
                            processLine(lineC);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            //int resultCode = -2;
            RunnerWaiter worker = new RunnerWaiter(process);
            //startTime = new Date();
            worker.start();
            int timeout = 3600000;
            try {
                worker.join(timeout);
                if (worker.exit == null) {
                    logger.error("执行命令超时：" + cmd + ",启动时间：" + StringUtils.date2string(startTime) + "," +
                            "当前时间：" + StringUtils.date2string(new Date()));
                }
            } catch (InterruptedException ex) {
                logger.error("发生中断，可能是超时错误:" +cmd+
                        ",执行启动时间："+StringUtils.date2string(startTime)+",exception="+ex.getMessage());
                worker.interrupt();
                Thread.currentThread().interrupt();
            } finally {
                try {
                    is1.close();
                    is2.close();
                    process.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.debug("执行完毕："+cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        os = null;
        //等待10秒，然后把所有文件删了
    }

    /**
     * 删除所有生成的文件
     */
    public void clear(final String tsPathName){
        new Thread(){
            public void run(){
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File tsFileDir = new File(tsPathName);
                if(tsFileDir.exists()){
                    int deletedFileCount = 0;
                    int totalCount = 0;
                    if(tsFileDir.isDirectory()){
                        File[] tsFiles = tsFileDir.listFiles();
                        if(tsFiles!=null){
                            totalCount=tsFiles.length;
                            for(File tsFile:tsFiles){
                                String name = tsFile.getName();
                                if(".".equals(name)||"..".equals(name)){
                                    totalCount--;
                                    continue;
                                }
                                if(tsFile.delete()){
                                    deletedFileCount++;
                                }else{
                                    logger.debug("文件无法删除："+tsFile.getAbsolutePath());
                                }
                            }
                        }else{
                            logger.error("列目录失败，目录："+tsFileDir.getAbsolutePath());
                        }
                        logger.debug("目录下共有"+totalCount+"个文件，其中"+deletedFileCount+"个文件已经被删除："+tsFileDir.getAbsolutePath());
                        if(tsFileDir.delete()){
                            logger.debug("目录已经删除："+tsFileDir.getAbsolutePath());
                        }else{
                            logger.debug("目录无法删除："+tsFileDir.getAbsolutePath());
                        }
                    }else{
                        logger.error("这不是目录："+tsFileDir.getAbsolutePath());
                    }
                }else{
                    logger.error("目录不存在："+tsFileDir.getAbsolutePath());
                }
                File m3u8File = new File(tsPathName+".m3u8");
                if(m3u8File.exists()){
                    if(m3u8File.delete()){
                        logger.debug("M3U8文件已经删除："+m3u8File.getAbsolutePath());
                    }else{
                        logger.warn("M3U8文件无法删除："+m3u8File.getAbsolutePath());
                    }
                }
            }
        }.start();
    }
    public void stopWork(){
        if(os==null){
            logger.warn("该任务目前不在运行状态："+sourceFileInfo.getName());
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("q");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("无法停止："+e.getMessage());
        }
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    @SuppressWarnings("unused")
    public SimpleFileInfo getSourceFileInfo() {
        return sourceFileInfo;
    }

    @SuppressWarnings("unused")
    public void setSourceFileInfo(SimpleFileInfo sourceFileInfo) {
        this.sourceFileInfo = sourceFileInfo;
    }

    @SuppressWarnings("unused")
    public String getM3u8FileName() {
        return m3u8FileName;
    }

    @SuppressWarnings("unused")
    public void setM3u8FileName(String m3u8FileName) {
        this.m3u8FileName = m3u8FileName;
    }

    @SuppressWarnings("unused")
    public String getTsPathName() {
        return tsPathName;
    }

    @SuppressWarnings("unused")
    public void setTsPathName(String tsPathName) {
        this.tsPathName = tsPathName;
    }

    @SuppressWarnings("unused")
    public long getStartSeconds() {
        return startSeconds;
    }

    @SuppressWarnings("unused")
    public void setStartSeconds(long startSeconds) {
        this.startSeconds = startSeconds;
    }

    @SuppressWarnings("unused")
    public int getSegmentDuration() {
        return segmentDuration;
    }

    @SuppressWarnings("unused")
    public void setSegmentDuration(int segmentDuration) {
        this.segmentDuration = segmentDuration;
    }

    @SuppressWarnings("unused")
    public long getLastVisitTime() {
        return lastVisitTime;
    }

    @SuppressWarnings("unused")
    public void setLastVisitTime(long lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
    private static class RunnerWaiter extends Thread {

        private final Process process;

        private Integer exit=null;

        private RunnerWaiter(Process process) {
            this.process = process;
        }

        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
            }
        }
    }

}
