package com.fortune.midware;

import com.fortune.util.AppConfigurator;
import com.fortune.util.MD5Utils;
import com.fortune.util.sql.SqlUtils;
import com.fortune.util.cache.Cache;
import com.fortune.util.config.Config;
import com.fortune.util.StringUtils;
import com.fortune.util.sql.ConnManager;
import org.apache.log4j.Logger;

import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DoThread extends Thread {
    private Socket sockRequest;
    private BufferedReader sockIn;
    private PrintStream sockOut;
    private String clientServerIp;
    public  static long lThreadNumber;
    //ͬ����
    private static Object oLock = new Object();
    private static long lKey = 0;
    private StartUp startup;

    //cache server list
    private static Cache cache = new Cache(50000000,Config.getIntValue("midware.validtime",2)*1000);
    
    public class CloseSocketTask extends TimerTask{
        private Timer timer;
        public CloseSocketTask(Timer timer){
            this.timer = timer;
        }
        public void run(){
            sockOut.println("yes timeOut");
            closeSocket();
            timer.cancel();
        }
    }

    public DoThread(Socket socket, StartUp startup) {
        this.sockRequest = socket;
        this.startup = startup;
        synchronized(oLock){
            //logger.error("count:"+lThreadNumber);
            lThreadNumber++;
        }

        try {
            this.sockIn = new BufferedReader(new InputStreamReader(this.sockRequest.getInputStream()));
            this.sockOut = new PrintStream(this.sockRequest.getOutputStream());
            clientServerIp = sockRequest.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String regsid(String url){
        try{
            //Ψһ��ʾ��һ��Key
            long lMyKey = 0;
            //Ϊ�˱���ͬ������������������ͬʱ����
            synchronized (oLock) {
                lKey++;
                lMyKey = lKey;
            }

            if (url.indexOf("?")>-1){
                url += "&";
            } else {
                url += "?";
            }

            String startTime = StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
            url += "startTime="+startTime+"&key="+lMyKey;

            AuthCache ac = new AuthCache();
            ac.key = lMyKey;
            ac.url = url;

            cache.add(url, ac);

        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
    Logger logger = Logger.getLogger(getClass());
    public boolean verifyToken(String url,String clientIp){
        if(url==null){
            return false;
        }
        if(url.indexOf("://")>0){
            url = StringUtils.getClearURL(url);
        }
        int i=url.indexOf("&encrypt=");
        if(i<0){
            i=url.indexOf("&amp;encrypt=");
        }else{

        }
        if(i<0){
            logger.debug("û�м�����Ϣ��ֱ�ӷ���ȱʡ��֤��Ϣ");
            return AppConfigurator.getInstance().getBoolConfig("cdn.playerAccess.defaultAuth",true);
        }
        String token = StringUtils.getParameter(url,"encrypt");
        String noEncryptUrl = url.substring(0,i);
        try {
            String tokenPwd = AppConfigurator.getInstance().getConfig("cdn.tokenPassword","fortune2011");
            String checkUrl = noEncryptUrl+"&clientip="+clientIp+tokenPwd;
            logger.debug("verifyURL="+checkUrl);
            String checkToken = MD5Utils.getMD5String(checkUrl);
            if(checkToken!=null && checkToken.equals(token)){
                //ʱ���У��
                Date timestamp = StringUtils.string2date(StringUtils.getParameter(url,"timestamp"),"yyyyMMddHHmmss");
                if(Math.abs(System.currentTimeMillis()-timestamp.getTime())<
                        AppConfigurator.getInstance().getIntConfig("cdn.tokenCheckTimestampLimit",3600)*1000){
                    return true;
                }else{
                    logger.debug("ʱ�䲻�ԣ���������޶���"+StringUtils.date2string(timestamp));
                }
            }else{
                logger.debug("���ܼ��㲻ƥ�䣬�ܾ��û�����"+checkToken+"!="+token);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5���㷢���쳣��"+e.getMessage());
        }
        return false;
    }
    public boolean login(String url){
        try{
            if (cache.get(url)!=null){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //ͬ����
    private static Object oLogLock = new Object();
    private static List[] logLists = new ArrayList[]{new ArrayList(),new ArrayList()};
    private static int usedList=0;
    private static int backList=0;    
    private static long lastSaveTime = 0;    

    public void insert(String url){
        if (url == null){
            return;
        }
        try{
            long spId = StringUtils.getLongParameter(url, "spId", -1l);
            long cpId = StringUtils.getLongParameter(url, "cpId", -1l);
            long channelId = StringUtils.getLongParameter(url, "channelId", -1l);
            long contentId = StringUtils.getLongParameter(url, "contentId", -1l);
            long contentPropertyId = StringUtils.getLongParameter(url, "contentPropertyId", -1l);
            String mediaUrl = url.substring(0,url.indexOf("?"));
            String userId = StringUtils.getStringParameter(url, "userId", "");
            String userIp = StringUtils.getStringParameter(url, "userIp", "");
            long areaId = StringUtils.getLongParameter(url, "areaId", -1l);
            long isFree = StringUtils.getLongParameter(url, "isFree", -1l);
            String sStartTime = StringUtils.getStringParameter(url, "startTime", "");
            Date startTime = StringUtils.string2date(sStartTime,"yyyyMMddHHmmss");
            Date endTime = new Date();
            long length = (long)(endTime.getTime() - startTime.getTime())/1000;

            Object params[] = new Object[]{spId,cpId,channelId,contentId,contentPropertyId,
                    mediaUrl,userId,userIp,areaId,isFree,startTime,endTime,length};
            String sql = "insert into visit_log(id,sp_id,cp_id,channel_id,content_id,content_property_id," +
                    "url,user_id,user_ip,area_id,is_free,start_time,end_time,length) values (fortune_log_seq.nextval," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?)";

            synchronized(oLogLock){
                //��ӽ��б�
                logLists[usedList].add(params);
                if(logLists[usedList].size() > 2 || (System.currentTimeMillis() - lastSaveTime)>30000){
                    //����б�ﵽ���ƣ�����ʱ�䳬�����ƣ����б������еļ�¼д�����ݿ�
                    lastSaveTime = System.currentTimeMillis();
                    //һ����ʼд�����б����ת���������б����á�
                    usedList ++;
                    if(usedList >= logLists.length){
                        usedList = 0;
                    }
                }
            }
            if (backList!=usedList){
                long backList1 = backList;
                backList = usedList;
                SqlUtils.executePreparedBatch(sql, logLists[(int)backList1], ConnManager.getConn());
                logLists[(int)backList1].clear();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * �����߳�
     */
    public void run() {

        boolean socketClosed = false;
        //1����֮�ڣ�ϵͳ����Ӧ��ֱ�ӷ�������
        Timer timer = new Timer(false);
        timer.schedule(new CloseSocketTask(timer), Config.getIntValue("midware.timeout",1) *1000);

        try {
            String sReadLine = sockIn.readLine();
            logger.debug("Input command : "+sReadLine);
            if (sReadLine == null) {
                return;
            }

            String commands[] = sReadLine.split(" ");

            if (commands==null || commands.length < 2) {
                logger.error("û�����" + sReadLine);
                return;
            }

            if ("regsid".equals(commands[0])){
                sockOut.println( regsid(commands[1]) );
            } else if ("login".equals(commands[0])){
                if (verifyToken(commands[1],commands[2])){
                    sockOut.println( "yes bye" );
                }else{
                    sockOut.println( "not bye" );
                }
            } else if ("insert".equals(commands[0])){
                sockOut.println("roger");
                //�ر�socket, ֱ�ӷ���, �ٴ�����־.
                timer.cancel();
                closeSocket();
                socketClosed = true;

                insert(commands[1]);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!socketClosed){
            timer.cancel();
            closeSocket();
        }
    }


    /**
     * �رճ�Աsocket
     */
    private void closeSocket() {
        //logger.error(sockRequest.isConnected()+":"+sockRequest.isClosed());

        synchronized(oLock){
            lThreadNumber--;
        }
        
        try{
            sockRequest.shutdownInput();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            sockRequest.shutdownOutput();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(sockIn != null){
                sockIn.close();
                sockIn = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(sockOut!=null){
                sockOut.close();
                sockOut = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            if(sockRequest!=null){
                sockRequest.close();
                sockRequest = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }
}