<%@ page import="com.fortune.server.message.ServerMessager" %><%@ page
        import="com.fortune.server.protocol.M3U8" %><%@ page
        import="com.fortune.server.protocol.M3U8Stream" %><%@ page
        import="com.fortune.server.protocol.M3U8Segment" %><%@ page
        import="com.fortune.server.protocol.SessionResponse" %><%@ page
        import="java.net.HttpURLConnection" %><%@ page
        import="java.net.URL" %><%@ page
        import="com.fortune.util.net.URLEncoder" %><%@ page
        import="java.io.OutputStream" %><%@ page
        import="java.io.DataOutputStream" %><%@ page
        import="java.io.InputStream" %><%@ page
        import="java.io.DataInputStream" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.util.Date" %><%@ page
        import="com.fortune.util.*" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-7-16
  Time: 上午8:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command = request.getParameter("command");
    String result = "";
    final BatchWorker worker = BatchExecutor.getInstance().getWorker("scanContentsFileOnly",1000);
    String workerLog = "";
    int count = 0;
    int finishedCount = 0;
    String url = request.getParameter("testUrl");
    if(url==null)url="http://10.0.66.11/in/encode/recommend/20141008tanxigang.512K_640x480.mp4.m3u8?version=1&keepblank=true&contentId=198&contentPropertyId=2608&channelId=474431602&&spId=-1&cpId=2&userId=testBench&userIp=172.17.104.24&isFree=0&areaId=-1&remoteType=MozillaDIV5.0SPACELEFTWindowsSPACENTSPACE6.1%3BSPACEWOW64%3BSPA&timestamp=20141121144416&encrypt=23cf06acc0f00f1af043df22359fe9cf";
    int addThreadNumber = StringUtils.string2int(request.getParameter("threadNumber"),1);

    if("add".equals(command)){
        if(addThreadNumber>0){
            String[] urls = url.split(" ");
            int threadCount = 0;
            for(String a:urls){
                for(int i=0;i<addThreadNumber;i++){
                    BatchRunnable runnable = getRunnable(a);
                    worker.execute(runnable);
                    threadCount++;
                }
            }
            result = "增加了"+threadCount+"个线程进行测试！";
        }
    }else if("stop".equals(command)){
        worker.shutdown();
        worker.reset();
    }
    if(worker!=null){
        String log = worker.getLog();
        int logLength = log.length();
        if(logLength>800000){
            workerLog = log.substring(logLength-800000);
        }else{
            workerLog = log;
        }
        String info = ""+StringUtils.date2string(worker.getStartTime())+"-启动测试\r\n";
        info+="测试时长："+worker.getAllDuration();
        count = worker.getCount();
        workerLog = info+"\r\n"+workerLog;
        finishedCount = worker.getFinishedCount();
    }
    int totalLength =640;
    int finishedLength =0;
    if(count>0){
        finishedLength = totalLength * finishedCount/count;
    }
    int waitingLength = totalLength - finishedLength;

%>
<html>
<head>
    <style type="text/css">
        .inputBox{
            width:<%=totalLength%>px;
        }
    </style>
    <title>带宽测试</title>
    <script>
        function doCommand(command){
            var testForm = document.forms[0];
            testForm.command.value = command;
            testForm.submit();
        }
        function addThread(){
            if(confirm("确认增加线程进行测试吗？")){
                doCommand('add')
            }
        }
        function stopAll(){
            if(confirm("确认停止所有测试吗？")){
                doCommand('stop')
            }
        }
        function refresh(){
            doCommand('refresh');
        }
    </script>
</head>
<body>
<form action="?test=<%=System.currentTimeMillis()%>" method="post" id="testForm" name="testForm">
    <table>
        <tr>
            <td colspan="2"><%=result%></td>
        </tr>
        <tr>
            <td>增加线程：</td><td><input type="text" value="<%=addThreadNumber%>" name="threadNumber" class="inputBox"></td>
        </tr>
        <tr>
            <td>测试链接：</td><td><input type="text" value="<%=url%>" name="url" class="inputBox"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="button" value="启动测试" onclick="addThread()">&nbsp;
            <input type="button" value="刷新日志" onclick="refresh()">&nbsp;
            <input type="button" value="停止测试" onclick="stopAll()"> </td>
        </tr>
    </table>
    <input type="hidden" value="" name="command">
</form>
<table width="<%=totalLength%>" border="1" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2"><%=finishedCount%>/<%=count%>&nbsp;&nbsp;&nbsp;&nbsp;<a href="?command=list&time=<%=System.currentTimeMillis()%>">刷新</a></td>
    </tr>
    <tr bgcolor="red">
        <td bgcolor="#adff2f" width="<%=finishedLength%>"></td>
        <td width="<%=waitingLength%>">&nbsp;</td>
    </tr>
</table>
<label for="logs">日志信息：</label><br/>
<textarea id="logs" cols="80" rows="80" style="width:<%=totalLength%>px;height:360px;"><%=workerLog%></textarea>

</body>
</html><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.testM3U8");
    public static long downloadedBytes=0;
    public synchronized void initBandwidthInfo(long downloadedBytes,long duration){

    }
    public BatchRunnable getRunnable(final String url){
        return new BatchRunnable() {
            private Date startTime;
            private Date stopTime;
            private long dataLength=0;
            private long allBandwidth = 0;
            //private long lastFiveSecondsBandwidth = 0;
            private int resultCode=0;
            private String logs = "";
            public String getLogs(){
                return logs;
            }
            public long getDuration(){
                return stopTime.getTime()-startTime.getTime();
            }
            public int getResultCode() {
                return resultCode;
            }

            public void afterFinished() {
            }

            public void beforeStart() {
                startTime = new Date();
                logs ="";//"\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+"已经启动";
            }

            boolean willStop = false;
            public void shutdownNow(){
                willStop=true;
            }
            public void run(){
                startTime = new Date();
                dataLength = 0;
                ServerMessager messager = new ServerMessager();
                M3U8 m3u8 = new M3U8();
                String m3u8Content = messager.postToHost(url,null);
                if(m3u8Content!=null&&!"".equals(m3u8Content.trim())){
                    m3u8.addStream(0,1,url,m3u8Content);
                    for(M3U8Stream stream:m3u8.getStreams()){
                        if(willStop){
                            break;
                        }
                        float m3u8Duration = stream.getAllDuration();
                        if(m3u8Duration<=0){
                            m3u8Duration = 100;
                        }
                        float currentPos = 0.0f;
                        for(M3U8Segment segment:stream.getSegments()){
                            if(willStop){
                                break;
                            }
                            Date segmentStartTime = new Date();
                            Date willStopTime = new Date(segmentStartTime.getTime()+(long)(segment.getDuration()*1000));
                            String segmentUrl = segment.getUrl();
                            logger.debug("准备下载segment："+segmentUrl);
                            SessionResponse data = getSegementFromUrl(segmentUrl, null, null);
                            currentPos += segment.getDuration();
                            logger.debug("完成下载，进度：" +Math.round(currentPos/m3u8Duration)+"%，segment："+segmentUrl);
                            Date segmentStopTime = new Date();
                            while(willStopTime.getTime()>segmentStopTime.getTime()-500){
                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                segmentStopTime = new Date();
                            }
                            dataLength+=data.getContentLength();
                            long allDuration = System.currentTimeMillis()-startTime.getTime();
                            if(allDuration>0){
                                allBandwidth = dataLength*8*1000 / allDuration;
                                logs = "Total Bandwidth="+ StringUtils.formatBPS(allBandwidth);
                                logger.debug(logs);
                            }
                        }

                    }
                }

                stopTime = new Date();
                long duration = (stopTime.getTime()-startTime.getTime())/1000;
                String secondStr = ""+duration%60;
                while(secondStr.length()<2){
                    secondStr = "0"+secondStr;
                }
                logs ="\n当前线程启动时间："+StringUtils.date2string(startTime)+"->截至时间："+StringUtils.date2string(stopTime)+
                        ",平均带宽："+allBandwidth+",测试时常："+(duration/60)+":"+secondStr;
            }

            public SessionResponse getSegementFromUrl(String url, String postData,String strEncoding) {
                SessionResponse result = new SessionResponse();
                HttpURLConnection con;
                result.setStartTime(System.currentTimeMillis());
                int tryTimes = 0;
                int bufferLength = AppConfigurator.getInstance().getIntConfig("system.hlsProxy.maxBufferLength",1024*3064);
//        TimeWarn timeWarn = new TimeWarn(url);
//        timeWarn.start();
                while(tryTimes<2){
                    tryTimes++;
                    try {
                        URL dataUrl = new URL(URLEncoder.encode(url, "UTF-8"));
                        //logger.debug("尝试访问："+url);
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
                        //System.out.println("POST返回区域大小："+dis.available());
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
                            logger.error("HTTP请求发生错误："+code+"," +
                                    errorMsg);
                        }
                        //data = new String(d);
                        con.disconnect();
                        break;
                    } catch (Exception ex) {
                        logger.error("无法连接：" + url+","+ex.getMessage());
                        result.setResultCode(500);
                        //ex.printStackTrace();
                    }
                }
                result.setStopTime(System.currentTimeMillis());
                long duration = result.getStopTime()-result.getStartTime();
                if(duration>1000){
                    //logger.warn("此次访问时间过长："+duration+"ms,"+result);
                }

                if(duration>0){
                    logger.debug("Current Download Bandwidth="
                            +StringUtils.formatBPS(result.getContentLength()*8*1000/duration)+"," +
                            duration+"ms,"+(result.getContentLength())+"Bytes");
                }
                return result;
            }

        };
    }


%>
