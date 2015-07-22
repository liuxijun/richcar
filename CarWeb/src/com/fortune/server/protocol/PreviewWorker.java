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
 * Ԥ�����߳�
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
        //���裬ts�ļ�������m3u8�ļ�Ŀ¼�µ�ĳ��Ŀ¼��
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
            logger.error("û��ӰƬ�����Ϣ���������ã�"+sourceFileUrl);
        }
        //�߶Ⱥͳ��ȱ�����ż����Ϊ�˰�ȫ�������Ϊ4��������
        videoWidth = videoWidth - videoWidth %4;
        videoHeight = videoHeight - videoHeight%4;
        //����������
        if(sourceFileInfo.getLength()>0){
            float fileBandwidth = sourceFileInfo.getSize()*8/sourceFileInfo.getLength()/1024;
            if(fileBandwidth<(videoBandwidth+audioBandwidth)){
                logger.warn("ӰƬ����С��" +(videoBandwidth+audioBandwidth)+
                        "Kbps��������������"+fileBandwidth+"Kbps");
                audioBandwidth = 64;
                videoBandwidth = (int) fileBandwidth-audioBandwidth;
            }else{
                logger.debug("ӰƬ����Ϊ��"+Math.round(fileBandwidth)+"kbps");
            }
        }else{
            logger.error("�޷���ȡӰƬ������Ϣ��"+sourceFileUrl);
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
        logger.debug("����ִ�У�"+cmd);
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commandLine = new ArrayList<String>();
        //final List<String> result = new ArrayList<String>();
        //String[] allCmd = cmd.split(" ");
        //Collections.addAll(commandLine, allCmd);
        String currentPath = FileUtils.extractFilePath(m3u8FileName,"/");
        builder.directory(new File(currentPath));
        //ɨ�������У��ֽ��һ�����Ĳ���
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
             logger.debug("ִ�в�����"+c);
        }
*/
        logger.debug("������" + cmd);
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
                    logger.error("ִ�����ʱ��" + cmd + ",����ʱ�䣺" + StringUtils.date2string(startTime) + "," +
                            "��ǰʱ�䣺" + StringUtils.date2string(new Date()));
                }
            } catch (InterruptedException ex) {
                logger.error("�����жϣ������ǳ�ʱ����:" +cmd+
                        ",ִ������ʱ�䣺"+StringUtils.date2string(startTime)+",exception="+ex.getMessage());
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
                logger.debug("ִ����ϣ�"+cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        os = null;
        //�ȴ�10�룬Ȼ��������ļ�ɾ��
    }

    /**
     * ɾ���������ɵ��ļ�
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
                                    logger.debug("�ļ��޷�ɾ����"+tsFile.getAbsolutePath());
                                }
                            }
                        }else{
                            logger.error("��Ŀ¼ʧ�ܣ�Ŀ¼��"+tsFileDir.getAbsolutePath());
                        }
                        logger.debug("Ŀ¼�¹���"+totalCount+"���ļ�������"+deletedFileCount+"���ļ��Ѿ���ɾ����"+tsFileDir.getAbsolutePath());
                        if(tsFileDir.delete()){
                            logger.debug("Ŀ¼�Ѿ�ɾ����"+tsFileDir.getAbsolutePath());
                        }else{
                            logger.debug("Ŀ¼�޷�ɾ����"+tsFileDir.getAbsolutePath());
                        }
                    }else{
                        logger.error("�ⲻ��Ŀ¼��"+tsFileDir.getAbsolutePath());
                    }
                }else{
                    logger.error("Ŀ¼�����ڣ�"+tsFileDir.getAbsolutePath());
                }
                File m3u8File = new File(tsPathName+".m3u8");
                if(m3u8File.exists()){
                    if(m3u8File.delete()){
                        logger.debug("M3U8�ļ��Ѿ�ɾ����"+m3u8File.getAbsolutePath());
                    }else{
                        logger.warn("M3U8�ļ��޷�ɾ����"+m3u8File.getAbsolutePath());
                    }
                }
            }
        }.start();
    }
    public void stopWork(){
        if(os==null){
            logger.warn("������Ŀǰ��������״̬��"+sourceFileInfo.getName());
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("q");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("�޷�ֹͣ��"+e.getMessage());
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
