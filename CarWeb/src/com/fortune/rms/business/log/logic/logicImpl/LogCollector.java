package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.util.AppConfigurator;
import com.fortune.util.FileUtils;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2014/11/26.
 * 采集日志然后发送
 */
public class LogCollector {
    private AppConfigurator config = AppConfigurator.getInstance();
    private Logger logger = Logger.getLogger(getClass());
    public void scanFiles(String filePath) {
        String fullName;
        logger.debug("正在扫描："+filePath);
//        File root = new File(filePath);
        List<File> files = FileUtils.listDir(filePath, "*_access_log", true);
//        File[] files = root.listFiles();
        if(files==null){
            logger.error("无法列目录："+filePath);
            return;
        }
        //Arrays.sort(files);
        int fileCount = 0;
        int logCount = 0;
        for (File file : files) {
            String nowDate = StringUtils.date2string(new Date(), AppConfigurator.getInstance().getConfig(
                    "system.visitLog.currentSkipDateFormat","yyyy_MM_dd"));//今天生成的日志不处理
            if (file.getName().contains(nowDate)) {
                continue;
            }
            fullName = file.getAbsolutePath();
            fileCount++;
            logCount+=readVisitLog(fullName);
            //file.delete();
        }
        logger.debug("总共处理了"+fileCount+"个文件，传送了"+logCount+"行记录！");
    }
    public String getVisitLogPath() {
        return config.getConfig("system.visitLog.path", "/home/fortune/logs/www/");
    }

    public void scanFiles() {
        scanFiles( getVisitLogPath() );
    }


    public int readVisitLog(String fullName) {

        int sendVisitLogStatus = 0;
        StringBuilder sb = new StringBuilder();
        File file = new File(fullName);
        BufferedReader br = null;
        int count = 0;
        int foundLineCount = 0;
        try {
            logger.debug("正在扫描"+fullName+".....");
            Reader fileReader = new InputStreamReader(new FileInputStream(file),
                    config.getConfig("systme.visitLog.encoding","UTF-8"));
            br = new BufferedReader(fileReader);
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                } else {
                    String visitLogArray[] = line.split(" ");
                    if(visitLogArray.length<7){
                        continue;
                    }
                    int resultCode = StringUtils.string2int(visitLogArray[4],404);
                    if (resultCode==404||resultCode==500||resultCode==302||resultCode==406) {
                        continue;
                    }
                    if(line.contains(".m3u8")||line.contains(".M3U8")||line.contains(".ts")||line.contains(".TS")){
                        count++;
                        //logger.debug("发现第" +count+"行："+line);
                        sb.append(line);
                        sb.append("\r\n");
                    }
                }
                if (count == 100000) {
                    foundLineCount+=count;
                    count = 0;
                    sendVisitLogStatus = sendVisitLog(sb.toString());
                    if(sendVisitLogStatus==0){//如果发送失败，则立即停止处理这个文件
                        break;
                    }
                    sb.setLength(0);
                }
            }
        } catch (FileNotFoundException e) {
            sendVisitLogStatus = -1;
            e.printStackTrace();
        } catch (IOException e) {
            sendVisitLogStatus = -1;
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (count > 0) {
            foundLineCount+=count;
            sendVisitLogStatus = sendVisitLog(sb.toString());
        }
        if (sendVisitLogStatus > 0||foundLineCount==0) {//如果发送错误，或者没有可以发送的内容，则删除
            if (file.delete()) {
                logger.debug("文件"+fullName+"已经删除！如果需要，请检查saved目录中的备份。");
            }else{
                logger.debug("文件"+fullName+"删除失败！可能是正在被 打开！请检查！");
            }
        }else{
            logger.warn("文件"+fullName+"因发送错误，不删除，继续保留，等着下一次发送！");
        }

        logger.debug("文件"+fullName+"扫描完毕，共找到了"+foundLineCount+"行数据，发送"+(sendVisitLogStatus==0?"失败":"成功")+"！");
        return foundLineCount;
    }

    /**
     *
     * @param visitLogsString 日志内容
     * @return 发送的行数
     */
    public int sendVisitLog(String visitLogsString) {
        int sendVisitLogStatus = 1;
        if(visitLogsString==null||"".equals(visitLogsString)){
            logger.warn("准备发送的日志为空！不能继续！");
            return sendVisitLogStatus;
        }
        int length = visitLogsString.length();
        logger.debug("准备发送的日志长度是："+length+"字节");
        String urlStr = config.getConfig("system.visitLog.serverUrl",
                "http://115.28.233.138/interface/saveLog.jsp?logFormat=apache");
        URL url;
        URLConnection con;
        DataOutputStream out = null;
        BufferedReader br = null;
        if(config.getBoolConfig("system.visitLog.saveSendedContent",true)){
            try {
                File saveFile = new File(getVisitLogPath()+"/saved/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")
                        +"_" +Math.round(Math.random()*100000)+".log");
                File path = saveFile.getParentFile();
                if(!path.exists()){
                    if(path.mkdirs()){

                    }
                }
                FileWriter fw = new FileWriter(saveFile,true);
                fw.write(visitLogsString);
                fw.close();
            } catch (IOException e) {
                logger.error("保存备份日志时发生异常："+e.getMessage());
            }
        }
        try {
            logger.debug("正在传送日志文件到服务器" +urlStr+".....");
            url = new URL(urlStr);
            con = url.openConnection();
            byte[] outputString =(visitLogsString.getBytes(config.getConfig("system.visitLog.sendEncoding", "UTF-8")));
            con.setRequestProperty("Content-Length",""+outputString.length);
            con.setRequestProperty("Content-Type","text/plain; charset=utf-8");
            con.setDoOutput(true);
            out = new DataOutputStream(con.getOutputStream());
            out.write(outputString);
            out.flush();
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            for (line = br.readLine(); line != null; line = br.readLine()) {
                logger.debug(line);
            }

        } catch (MalformedURLException e) {
            logger.error("传输日志到服务器发生异常："+e);
            sendVisitLogStatus = 0;
            //e.printStackTrace();
        } catch (IOException e) {
            logger.error("传输日志到服务器发生异常："+e);
            sendVisitLogStatus = 0;
            //e.printStackTrace();
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
                }

            } catch (IOException e) {
                logger.error("传输日志到服务器发生异常："+e);
                //e.printStackTrace();
            }
        }
        return sendVisitLogStatus;
    }

    public static void main(String[] args){
        LogCollector worker = new LogCollector();
        worker.scanFiles();
    }
}
