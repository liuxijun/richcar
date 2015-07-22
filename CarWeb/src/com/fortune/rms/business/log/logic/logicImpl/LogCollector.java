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
 * �ɼ���־Ȼ����
 */
public class LogCollector {
    private AppConfigurator config = AppConfigurator.getInstance();
    private Logger logger = Logger.getLogger(getClass());
    public void scanFiles(String filePath) {
        String fullName;
        logger.debug("����ɨ�裺"+filePath);
//        File root = new File(filePath);
        List<File> files = FileUtils.listDir(filePath, "*_access_log", true);
//        File[] files = root.listFiles();
        if(files==null){
            logger.error("�޷���Ŀ¼��"+filePath);
            return;
        }
        //Arrays.sort(files);
        int fileCount = 0;
        int logCount = 0;
        for (File file : files) {
            String nowDate = StringUtils.date2string(new Date(), AppConfigurator.getInstance().getConfig(
                    "system.visitLog.currentSkipDateFormat","yyyy_MM_dd"));//�������ɵ���־������
            if (file.getName().contains(nowDate)) {
                continue;
            }
            fullName = file.getAbsolutePath();
            fileCount++;
            logCount+=readVisitLog(fullName);
            //file.delete();
        }
        logger.debug("�ܹ�������"+fileCount+"���ļ���������"+logCount+"�м�¼��");
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
            logger.debug("����ɨ��"+fullName+".....");
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
                        //logger.debug("���ֵ�" +count+"�У�"+line);
                        sb.append(line);
                        sb.append("\r\n");
                    }
                }
                if (count == 100000) {
                    foundLineCount+=count;
                    count = 0;
                    sendVisitLogStatus = sendVisitLog(sb.toString());
                    if(sendVisitLogStatus==0){//�������ʧ�ܣ�������ֹͣ��������ļ�
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
        if (sendVisitLogStatus > 0||foundLineCount==0) {//������ʹ��󣬻���û�п��Է��͵����ݣ���ɾ��
            if (file.delete()) {
                logger.debug("�ļ�"+fullName+"�Ѿ�ɾ���������Ҫ������savedĿ¼�еı��ݡ�");
            }else{
                logger.debug("�ļ�"+fullName+"ɾ��ʧ�ܣ����������ڱ� �򿪣����飡");
            }
        }else{
            logger.warn("�ļ�"+fullName+"���ʹ��󣬲�ɾ��������������������һ�η��ͣ�");
        }

        logger.debug("�ļ�"+fullName+"ɨ����ϣ����ҵ���"+foundLineCount+"�����ݣ�����"+(sendVisitLogStatus==0?"ʧ��":"�ɹ�")+"��");
        return foundLineCount;
    }

    /**
     *
     * @param visitLogsString ��־����
     * @return ���͵�����
     */
    public int sendVisitLog(String visitLogsString) {
        int sendVisitLogStatus = 1;
        if(visitLogsString==null||"".equals(visitLogsString)){
            logger.warn("׼�����͵���־Ϊ�գ����ܼ�����");
            return sendVisitLogStatus;
        }
        int length = visitLogsString.length();
        logger.debug("׼�����͵���־�����ǣ�"+length+"�ֽ�");
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
                logger.error("���汸����־ʱ�����쳣��"+e.getMessage());
            }
        }
        try {
            logger.debug("���ڴ�����־�ļ���������" +urlStr+".....");
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
            logger.error("������־�������������쳣��"+e);
            sendVisitLogStatus = 0;
            //e.printStackTrace();
        } catch (IOException e) {
            logger.error("������־�������������쳣��"+e);
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
                logger.error("������־�������������쳣��"+e);
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
