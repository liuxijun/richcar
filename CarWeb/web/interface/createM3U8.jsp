<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.fortune.server.message.ServerMessager" %>
<%@ page import="java.net.URL" %>
<%@ page import="com.fortune.util.AppConfigurator" %>
<%@ page import="com.fortune.util.net.URLEncoder" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.server.protocol.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.fortune.util.FileUtils" %>
<%@ page import="com.fortune.util.SimpleFileInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-7-18
  Time: 上午9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String m3u8Url = request.getParameter("m3u8Url");
    if(m3u8Url==null){
        m3u8Url = "http://192.168.1.25:8080/vod/ad/yahaCoffee.mp4.m3u8";
    }
    String m3u8SaveFileName = request.getParameter("m3u8FileName");
    if(m3u8SaveFileName==null||"".equals(m3u8SaveFileName.trim())||"true".equals(request.getParameter("autoSaveFileName"))){
        String pathName = m3u8Url;
        int p= m3u8Url.indexOf("/vod/");
        if(p>0){
            pathName = m3u8Url.substring(p+5);
        }else {
            p = m3u8Url.indexOf("/live/");
            if(p>0){
               pathName = m3u8Url.substring(p+6);
            }
        }
        if(pathName.contains("/")){
            pathName = FileUtils.extractFilePath(pathName,"/");
        }else{
            pathName = "";
        }
        pathName +="/"+ FileUtils.extractFileName(m3u8Url,"/");
        if(isWindows()){
            m3u8SaveFileName = "C:/temp/"+pathName;
        }else{
            m3u8SaveFileName = "/home/fortune/movie/m3u8/"+pathName;
        }
    }
    String tsFileName = request.getParameter("tsFileName");
    if(tsFileName==null||"".equals(tsFileName.trim())||"true".equals(request.getParameter("autoSaveTsFileName"))){
        tsFileName = FileUtils.extractFileName(m3u8Url,"/");
        int p=tsFileName.indexOf(".");
        if(p>0){
            tsFileName = tsFileName.substring(0,p);
        }
        tsFileName=tsFileName+"Ts/"+tsFileName+"%04d.ts";
        //tsFileName = FileUtils.extractFilePath(m3u8SaveFileName,"/")+"/"+tsFileName;
    }
    String command = request.getParameter("command");
    if("start".equals(command)){
        saveM3U8(m3u8Url,m3u8SaveFileName,tsFileName);
    }
%>
<html>
<head>
    <title>生成TS文件</title>
    <style type="text/css">
        .allInput{
            width:400px;
        }
        .leftInput{
            width:100px;
        }
        .rightInput{
            width:300px;
        }
    </style>
</head>
<body>
    <form action="?" method="post">
        <table>
            <tr>
                <td><label for="m3u8Url">URL</label></td><td><input id="m3u8Url" type="text" name="m3u8Url" class="allInput" value="<%=m3u8Url%>"></td>
            </tr>
            <tr>
                <td><label for="saveFileName">保存M3U8文件</label></td>
                <td><select name="autoSaveFileName" class="leftInput">
                    <option value="true" selected>自动指定</option>
                    <option value="false">手动指定</option>
                </select><input id="saveFileName" type="text" name="m3u8FileName" class="rightInput" value="<%=m3u8SaveFileName%>"></td>
            </tr>
            <tr>
                <td><label for="saveTsFileName">保存TS文件</label></td>
                <td><select name="autoSaveTsFileName" class="leftInput">
                    <option value="true" selected>自动指定</option>
                    <option value="false">手动指定</option>
                </select><input id="saveTsFileName" type="text" name="tsFileName" class="rightInput" value="<%=tsFileName%>"></td>
            </tr>
            <%
                if("start".equals(command)){
%>
            <tr>
                <td>信息汇总：</td><td>
                启动时间：<%=StringUtils.date2string(startTime)%>，
                完成时间：<%=StringUtils.date2string(stopTime)%>，<br/>
                耗时：<%=StringUtils.formatTime((stopTime.getTime()-startTime.getTime())/1000)%>
                使用带宽：<%=StringUtils.formatBPS(allBandwidth)%>，
                下载流量：<%=StringUtils.formatBytes(dataLength)%></td>
            </tr>
            <%
                }
            %>
            <tr>
                <td colspan="2">
                    <input type="submit" value="启动生成">
                </td>
            </tr>
        </table>
        <input type="hidden" name="command" value="start">
    </form>
</body>
</html>
<%!
    private Date startTime,stopTime;
    private boolean willStop = false;
    private long allBandwidth;
    private long dataLength;
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.createM3U8.jsp");
    public void saveM3U8(String url, String m3u8FileName, String tsFileName) {

        String logs = "";
        startTime = new Date();

        dataLength = 0;
        ServerMessager messager = new ServerMessager();
        M3U8 m3u8 = new M3U8();
        String m3u8Content = messager.postToHost(url, null);
        if (m3u8Content != null && !"".equals(m3u8Content.trim())) {
            m3u8.addStream(0, 1, url, m3u8Content);
            int tsFileIndex = 0;
            File m3u8File = new File(m3u8FileName);
            String m3u8Path = m3u8File.getParentFile().getAbsolutePath();
            for (M3U8Stream stream : m3u8.getStreams()) {
                if (willStop) {
                    break;
                }
                for (M3U8Segment segment : stream.getSegments()) {
                    if (willStop) {
                        break;
                    }
                    String segmentUrl = segment.getUrl();
                    SessionResponse data = getSegementFromUrl(segmentUrl, null, null);
                    if (data.getResultCode() == 200) {

                        String saveToTsFileName = String.format(tsFileName, tsFileIndex++);
                        segment.setUrl(saveToTsFileName);
                        try {
                            File tsFile = new File(m3u8Path+"/"+saveToTsFileName);
                            File path = tsFile.getParentFile();
                            if(!path.exists()){
                                if(path.mkdirs()){
                                    logger.debug("目录"+path.getAbsolutePath()+"不存在，创建成功！");
                                }else{
                                    logger.error("目录"+path.getAbsolutePath()+"不存在，创建失败！");
                                }
                            }else{
                                logger.debug("目录"+path.getAbsolutePath()+"存在，无需创建！");
                            }
                            FileOutputStream writer = new FileOutputStream(tsFile);
                            for (StreamData streamData : data.getResultData()) {
                                try {
                                    writer.write(streamData.getData(), 0, streamData.getLength());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            writer.close();
                            SimpleFileInfo fileInfo = new SimpleFileInfo(tsFile);
                            if(FileUtils.setFileMediaInfo(tsFile.getAbsolutePath(),fileInfo)){
                                if(fileInfo.getLength()>0){
                                    segment.setDuration(fileInfo.getLength());
                                    if(stream.getTargetDuration()<segment.getDuration()){
                                        stream.setTargetDuration(segment.getDuration());
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dataLength += data.getContentLength();
                    long allDuration = System.currentTimeMillis() - startTime.getTime();
                    if (allDuration > 0) {
                        allBandwidth = dataLength * 8 * 1000 / allDuration;
                        logs = "Total Bandwidth=" + StringUtils.formatBPS(allBandwidth);
                        logger.debug(logs);
                    }
                }

            }
            FileUtils.writeNew(m3u8Path,m3u8File.getName(),m3u8.toString());
        }

        stopTime = new Date();
        long duration = (stopTime.getTime() - startTime.getTime()) / 1000;
        String secondStr = "" + duration % 60;
        while (secondStr.length() < 2) {
            secondStr = "0" + secondStr;
        }
        logs = "\n" + StringUtils.date2string(startTime) + "->" + StringUtils.date2string(stopTime) +
                ",bandwidth=" + allBandwidth + ",duration=" + (duration / 60) + ":" + secondStr;
    }

    public SessionResponse getSegementFromUrl(String url, String postData, String strEncoding) {
        SessionResponse result = new SessionResponse();
        HttpURLConnection con;
        result.setStartTime(System.currentTimeMillis());
        int tryTimes = 0;
        int bufferLength = AppConfigurator.getInstance().getIntConfig("system.hlsProxy.maxBufferLength", 1024 * 3064);
//        TimeWarn timeWarn = new TimeWarn(url);
//        timeWarn.start();
        while (tryTimes < 2) {
            tryTimes++;
            try {
                URL dataUrl = new URL(URLEncoder.encode(url, "UTF-8"));
                //logger.debug("尝试访问："+url);
                con = (HttpURLConnection) dataUrl.openConnection();
                con.setConnectTimeout(2000);
                con.setReadTimeout(20000);
                if (postData == null || postData.trim().equals("")) {
                    con.setRequestMethod("GET");
                    con.setDoOutput(false);
                    con.setDoInput(true);
                } else {
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Proxy-Connection", "Keep-Alive");
                    OutputStream os = con.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    byte[] dataBuffer;
                    if (strEncoding != null) {
                        dataBuffer = postData.getBytes(strEncoding);
                    } else {
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
                while (true) {
                    if (willStop) {
                        break;
                    }
                    int i = dis.read(d, posOfBuffer, d.length - posOfBuffer);
                    if (i <= 0) {
                        break;
                    }
                    posOfBuffer += i;
                    if (posOfBuffer == d.length) {
                        result.addResultData(d.length, d);
                        d = new byte[bufferLength];
                        posOfBuffer = 0;
                    }
//                if(i<d.length){
//                    break;
//                }
                }
                if (posOfBuffer > 0) {
                    result.addResultData(posOfBuffer, d);
                }
                String errorMsg;
                if (code == HttpURLConnection.HTTP_OK) {
                    errorMsg = "HTTP_OK";
                } else if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
                    errorMsg = "HTTP_BAD_REQUEST";
                } else if (code == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    errorMsg = "HTTP_CLIENT_TIMEOUT";
                } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                    errorMsg = "HTTP_NOT_FOUND";
                } else {
                    errorMsg = "HTTP_UNKNOWN";
                }
                if (code != HttpURLConnection.HTTP_OK) {
                    logger.error("HTTP请求发生错误：" + code + "," +
                            errorMsg);
                }
                //data = new String(d);
                con.disconnect();
                break;
            } catch (Exception ex) {
                logger.error("无法连接：" + url + "," + ex.getMessage());
                result.setResultCode(500);
                //ex.printStackTrace();
            }
        }
        result.setStopTime(System.currentTimeMillis());
        long duration = result.getStopTime() - result.getStartTime();
        if (duration > 1000) {
            //logger.warn("此次访问时间过长："+duration+"ms,"+result);
        }

        if (duration > 0) {
            logger.debug("Current Download Bandwidth="
                    + StringUtils.formatBPS(result.getContentLength() * 8 * 1000 / duration) + "," +
                    duration + "ms," + (result.getContentLength()) + "Bytes");
        }
        return result;
    }
        public boolean isWindows(){
            String osName = System.getProperties().getProperty("os.name");
            if (osName == null) {
                osName = "";
            }
            return osName.toLowerCase().contains("windows");
        }

%>