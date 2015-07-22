<%@ page import="java.io.InputStream" %><%@ page import="org.apache.log4j.Logger" %>><%@ page
        import="com.fortune.rms.business.log.model.VisitLog" %><%@ page
        import="com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%@ page import="java.text.ParseException" %><%@ page
        import="java.util.Date" %><%@ page import="com.fortune.util.StringUtils" %><%--
  Created by IntelliJ IDEA.
  User: Alen
  Date: 11-8-5
  Time: 下午5:27
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%

    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();

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
        readResult = readResult.trim();
        logger.debug("客户端传来数据长度：" + readResult.length() + ",readResult=\n" + readResult);
        try {
//            readResult =  readResult.replace("&","\n");
        } catch (Exception e) {
//            logger.error("处理字符串过程中发生意外：" + e.getMessage());
        }
        //logger.debug("调用数据：“"+readResult+"”");
        try {
            List<VisitLog> visitLogs = dateProcess(readResult);
            logger.debug("visitLogs length:"+visitLogs.size());
            if(visitLogs.size()!=0){
                saveVisitLogs(visitLogs);
            }else{
//                logger.warn("没有日志可以保存！");
            }
        } catch (Exception e) {
            logger.error("日志保存失败："+e.getMessage());
        }

    }else{

        resultCode = 404;
//        logger.error("没有数据输入");
    }
//    logger.debug("处理完毕，resultCode="+resultCode);

%>resultCode=<%=resultCode%><%!
    private Logger logger = Logger.getLogger("com.fortune.jsp.saveLog.jsp") ;
    public List<VisitLog> dateProcess(String readResult) throws ParseException {
        List<VisitLog> visitLogs = null;
        if(readResult!=null){
            visitLogs = new ArrayList<VisitLog>();
            String visitLogsArray[] = readResult.split("\n");
            if(visitLogsArray.length!=0){
                String userIp=null;
                String date=null;
                String time=null;
                String duration=null;
                String status=null;
                String playVersion=null;
                String userAgent=null;
                String avgBandWith=null;
                String url=null;
                String sIp=null;

                for (String aVisitLogsArray : visitLogsArray) {

                    String visitLogArray[] = aVisitLogsArray.split(" ");
                    if(visitLogArray.length<52){
                        logger.warn("visitLogArray length:"+visitLogArray.length);
                        continue;
                    }
                    VisitLog visitLog = new VisitLog();
                    try {
                        userIp = visitLogArray[0];
                        date = visitLogArray[1];
                        time = visitLogArray[2];
                        duration = visitLogArray[6];
                        status = visitLogArray[8];
                        playVersion = visitLogArray[10];
                        userAgent = visitLogArray[12];
                        avgBandWith = visitLogArray[21];
                        sIp = visitLogArray[40];
                        url = visitLogArray[47];
                    } catch (Exception e) {
                        logger.error("获取visitLogArray出错："+e.getMessage());
                    }

                    try {
                        long spId = checkType(getValueByUrlKey(url, "spId"));
                        if(spId==-1){
                            spId = checkType(getValueByUrlKey(url, "icp"));
                        }
                        visitLog.setSpId(spId);
                        long cpId = checkType(getValueByUrlKey(url, "cpId"));
                        if(cpId==-1){
                            cpId = checkType(getValueByUrlKey(url, "imp"));
                        }
                        visitLog.setCpId(cpId);
                        long channelId = checkType(getValueByUrlKey(url, "channelId"));
                        if(channelId==-1){
                            channelId = checkType(getValueByUrlKey(url, "cid"));
                        }
                        visitLog.setChannelId(channelId);
                        long contentId = checkType(getValueByUrlKey(url, "contentId"));
                        if(contentId==-1){
                            contentId = checkType(getValueByUrlKey(url, "mid"));
                        }
                        visitLog.setContentId(contentId);

                        visitLog.setContentPropertyId(checkType(getValueByUrlKey(url, "contentPropertyId")));
                        visitLog.setUrl(url);
                        String userId = getValueByUrlKey(url, "userId");
                        if(userId == null){
                            userId = getValueByUrlKey(url, "uid");
                        }
                        visitLog.setUserId(userId);
                        visitLog.setUserIp(userIp);
                        visitLog.setAreaId(checkType(getValueByUrlKey(url, "areaId")));
                        visitLog.setIsFree(checkType(getValueByUrlKey(url, "isFree")));
                        visitLog.setStartTime(StringUtils.string2date(date + " " + time));
                        visitLog.setEndTime(StringUtils.string2date(timeCalculation(date + " " + time, duration, "add")));
//                   logger.debug("url="+visitLog.getUrl()+",startTime="+StringUtils.date2string(visitLog.getStartTime())+",endTime="+StringUtils.date2string(visitLog.getEndTime()));
                        visitLog.setLength(checkType(duration));
                        visitLog.setStatus(checkType(status));
                        visitLog.setPlayerVersion(playVersion);
                        visitLog.setUserAgent(userAgent);
                        visitLog.setAvgBandwidth(checkType(avgBandWith));
                        visitLog.setsIp(sIp);
                    } catch (Exception e) {

                        logger.error("初始化visitLog数据出错：“" +aVisitLogsArray+
                                "”错误信息是："+e.getMessage());
                    }
                    visitLogs.add(visitLog);

                }

            }
        }
        return visitLogs;
    }

    public void saveVisitLogs(List<VisitLog> visitLogs) throws Exception {
        VisitLogLogicInterface visitLogLogicInterface =(VisitLogLogicInterface) SpringUtils.getBean("visitLogLogicInterface");
        visitLogLogicInterface.JDBCSaveVisitLogs(visitLogs);
    }

    public String getValueByUrlKey(String url,String key){

        String value=null;
        int index = url.indexOf("?");
        if(index!=-1){
            String[] param = url.substring(url.indexOf("?")+1).split("&");
            for (String aParam : param) {
                if (aParam.split("=")[0].equals(key)) {
                    try {
                        value = aParam.split("=")[1];
                    } catch (Exception e) {
                        value = null;
                    }
                }
            }
        }
        return value;

    }

    public  String timeCalculation(String time,String second,String operator) throws ParseException {
        String result="";
        Date date= StringUtils.string2date(time);
        Long startTime=date.getTime();
        Long endTime = 0l;
        try{
            endTime = Long.parseLong(second)*1000;
        }catch (Exception e){
            endTime = 0l;
        }
        long resultTemp=0;
        if(operator.equals("add")){
            resultTemp=startTime+endTime;
        }
        result =StringUtils.date2string(resultTemp);
        return result;
    }

    public static Long checkType(String obj){
        return StringUtils.string2long(obj,-1);
    }

%>
