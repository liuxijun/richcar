<%@ page import="org.apache.log4j.Logger" %><%@ page
        import="java.text.ParseException,cn.sh.guanghua.util.tools.StringTools" %><%@ page
        import="java.util.Date" %><%@ page
        import="java.io.*,java.util.ArrayList" %><%@ page
        import="java.util.List" %><%@ page
        import="java.sql.Connection" %><%@ page
        import="java.sql.PreparedStatement" %><%@ page
        import="java.sql.SQLException" %><%@ page
        import="cn.sh.guanghua.mediastack.dataunit.MediaLog" %><%@ page
        import="cn.sh.guanghua.util.pooldb.DbTool" %><%--
  Created by IntelliJ IDEA.
  User: Alen
  Date: 11-8-5
  Time: 下午5:27
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%

    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();

    Logger logger = Logger.getLogger("com.fortune.jsp.saveLog.jsp") ;
    int resultCode=0;
    if(dataLength>0){
        byte[] dataBuffer = new byte[dataLength];
        int readedLength = is.read(dataBuffer);
        String readResult = "";
        int readed = 0;
        try {

            while(readed<dataLength&&readedLength>0){
                readResult = readResult + new String(dataBuffer,0,readedLength);
                readedLength = is.read(dataBuffer);
                readed+=readedLength;
            }
            if(readedLength>0){
                readResult = readResult + new String(dataBuffer,0,readedLength);
                readed+=readedLength;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //logger.debug("客户端传来数据长度：" + readResult.length() + ",readResult=\n" + readResult);
        try {
//            readResult =  readResult.replace("&","\n");
        } catch (Exception e) {
            logger.error("errorOnProcessString 处理字符串过程中发生意外：" + e.getMessage());
        }
        //logger.debug("调用数据：“"+readResult+"”");
        try {
            dateProcess(readResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }else{

        resultCode = 404;
        logger.error("404没有数据输入");
    }
    logger.debug("Finished!处理完毕，resultCode="+resultCode);

%>resultCode=<%=resultCode%><%!
    private static int totalSaved = 0;
    public String getParameter(String url,String parameterName,String defaultVal){
        String result = StringTools.getParameter(url,parameterName);
        if(result==null||"".equals(result)){
            return defaultVal;
        }
        return result;
    }
    public long getLongParameter(String url,String parameterName,long defaultVal){
        return StringTools.string2long(getParameter(url,parameterName,""+defaultVal),defaultVal);
    }
    public void dateProcess(String readResult) throws ParseException {
        if(readResult!=null){
/*
            Config config = cn.sh.guanghua.mediastack.common.ConfigManager.getConfig().node("middleware");
            String midwareIp = "218.26.171.234";
            int port = 8200;
            if(config!=null){
                midwareIp=config.get("address",midwareIp);
                port = config.getInt("port");
            }
*/
           String visitLogsArray[] = readResult.split("\n");
           if(visitLogsArray.length!=0){
               logger.debug("Has got "+visitLogsArray.length+" rows!");
               String userIp;
               String date;
               String time;
               String duration;
/*
               String status;
               String playVersion;
               String userAgent;
               String avgBandWith;
*/
               String url;
               String sIp;
               List mediaLogs = new ArrayList();
               for (int i=0;i<visitLogsArray.length;i++) {
                   String aVisitLogsArray = visitLogsArray[i];
                   String visitLogArray[] = aVisitLogsArray.split(" ");
                   userIp = visitLogArray[0];
                   date = visitLogArray[1];
                   time = visitLogArray[2];
                   duration = visitLogArray[6];
/*
                   status = visitLogArray[8];
                   playVersion = visitLogArray[10];
                   userAgent = visitLogArray[12];
                   avgBandWith = visitLogArray[21];
*/
                   sIp = visitLogArray[40];
                   url = visitLogArray[47];
                   int intDuration = StringTools.string2int(duration,0);
                   Date startTime = StringTools.string2date(date + " " + time);
                   Date stopTime = new Date(startTime.getTime()+intDuration*1000);
                   if(intDuration<3){
                       // logger.warn("播放持续时间太短，可能会被拒绝：st="+StringTools.date2string(startTime)+",et="+StringTools.date2string(stopTime));
                       continue;
                   }
                   MediaLog medialog = new MediaLog();
                   //设置MediaId
                   medialog.setMedialogMediaid(getLongParameter(url,"mid",-1));
                   medialog.setMedialogUserid(getParameter(url,"uid","Guest"));
                   medialog.setMedialogMediaurl(url);
                   medialog.setMedialogUserip(userIp);
                   medialog.setMedialogStarttime(startTime.getTime());
                   medialog.setMedialogEndtime(stopTime.getTime());
                   medialog.setMedialogEndreason(0);
                   medialog.setMedialogIcpid(getLongParameter(url,"icp",-1));
                   medialog.setMedialogChannelid(getLongParameter(url,"cid",-1));
                   medialog.setServerId(getLongParameter(url,"svr",-1));
                   medialog.setImpId(getLongParameter(url,"imp",-1));
                   medialog.setSubjectId(getLongParameter(url,"sid",-1));
                   medialog.setOrderLength(intDuration);
                   medialog.setUserType(getLongParameter(url,"fee",-1));
                    medialog.setServerIp(sIp);
                   mediaLogs.add(medialog);
                    //medialog.setReqsn(reqsn);
               }
               logger.debug("Will save "+mediaLogs.size()+" rows!");
               this.JDBCSaveVisitLogs(mediaLogs);
           }else{
               logger.warn("No any data in '"+readResult+"'");
           }
        }else{
            logger.error("readResult is null!");
        }
    }
    
    protected Logger logger = Logger.getLogger("com.fortune.jsp.saveLog.jsp");
    public void JDBCSaveVisitLogs(List  visitLogs) {
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con =  DbTool.getInstance().getConnection();
            String prepareSQL = "INSERT INTO MEDIA_LOG("+
                                "MEDIALOG_MEDIAID,MEDIALOG_ID,MEDIALOG_USERID,MEDIALOG_MEDIAURL,"+
                                "MEDIALOG_STARTTIME,MEDIALOG_ENDTIME,MEDIALOG_USERIP,"+
                                "MEDIALOG_ENDREASON,MEDIALOG_ICPID,MEDIALOG_CHANNELID,"+
                                "MEDIALOG_IMPID,MEDIALOG_SERVERID,MEDIALOG_SUBJECTID,MEDIALOG_ORDERLENGTH,"+
                                "USER_TYPE,SERVER_IP"+
                                ")VALUES("+
                                "?,";
            prepareSQL = prepareSQL +"seq_medialog_id.nextval,";
            prepareSQL = prepareSQL + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pStmt = con.prepareStatement(prepareSQL);
            for (int i=0;i<visitLogs.size();i++) {
                MediaLog medialog = (MediaLog) visitLogs.get(i);
                pStmt.setLong(1,medialog.getMedialogMediaid());
                pStmt.setString(2,medialog.getMedialogUserid());
                pStmt.setString(3,medialog.getMedialogMediaurl());
                pStmt.setLong(4,medialog.getMedialogStarttime());
                pStmt.setLong(5,medialog.getMedialogEndtime());
                pStmt.setString(6,medialog.getMedialogUserip());
                pStmt.setLong(7,medialog.getMedialogEndreason());
                pStmt.setLong(8,medialog.getMedialogIcpid());
                pStmt.setLong(9,medialog.getMedialogChannelid());
                pStmt.setLong(10,medialog.getImpId());
                pStmt.setLong(11,medialog.getServerId());
                pStmt.setLong(12,medialog.getSubjectId());
                pStmt.setLong(13,medialog.getOrderLength());
                pStmt.setLong(14,medialog.getUserType());
                pStmt.setString(15,medialog.getServerIp());
                //pStmt.setString(16,medialog.getReqsn());
                pStmt.addBatch();
            }
            pStmt.executeBatch();
            totalSaved+=visitLogs.size();
            logger.debug("batchSaved(已经批量保存)"+visitLogs.size()+" this time(条记录),total saved(累计保存了：)"+totalSaved+"条记录！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStmt != null) {
                    pStmt.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

%>
