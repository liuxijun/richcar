<%@ page import="com.fortune.util.CommandRunner" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.io.*" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="java.util.Date" %><%@ page import="java.net.HttpURLConnection" %><%@ page import="java.net.URL" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/7/3
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String addr = request.getParameter("addr");
    AppConfigurator appConfigurator = AppConfigurator.getInstance();
    String fromClient = request.getParameter("fromClient");
    String programId = request.getParameter("programId");
    String result = "";
    String picFileName = request.getParameter("picFileName");
    if(picFileName == null){
        picFileName = "/upload/snap/"+ StringUtils.date2string(new Date(),"yyyy/MM/dd/HHmmss")+"_"+Math.round(Math.random()*100000)+".jpg";
    }
    String snapFileName = application.getRealPath(picFileName);
    File picFile = new File(snapFileName);
    if(picFile.exists()){
        logger.warn("图片存在，先移除："+picFile.getAbsolutePath());
        if(picFile.delete()){
            logger.debug("删除文件成功！");
        }else{
            logger.error("无法删除图片！出问题了："+picFile.getAbsolutePath());
        }
    }else{
        File parent = picFile.getParentFile();
        if(!parent.exists()){
            logger.warn("目录不存在："+parent.getAbsolutePath());
            if(parent.mkdirs()){
                logger.debug("目录创建成功："+parent.getAbsolutePath());
            }else{
                logger.error("无法创建目录："+parent.getAbsolutePath());
            }
        }
    }
    if("true".equals(fromClient)){
        String serverUrl = appConfigurator.getConfig("system.live.snap.serverUrl","http://192.168.1.88:18080/interface/snapLive.jsp");
        logger.debug("客户端" + request.getRemoteAddr()+"("+session.getAttribute("admin")+")"+
                "请求"+picFileName+"需要到远端服务器去拉："+serverUrl);
        if(serverUrl.contains("?")){
            serverUrl+="&";
        }else{
            serverUrl+="?";
        }
        String url = serverUrl+"addr=" + addr+"&programId="+programId+"&picFileName="+picFileName;
        logger.debug("准备远程访问："+url);
        URL dataUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
        logger.debug("连接成功："+url);
        con.setRequestMethod("GET");
        con.setDoOutput(false);
        con.setDoInput(true);
        InputStream is = con.getInputStream();
        File snapFile = new File(snapFileName);
        File snapFilePath = snapFile.getParentFile();
        if(!snapFilePath.exists()){
            if(!snapFilePath.mkdirs()){
                logger.warn("目录创建失败："+snapFilePath.getAbsolutePath());
            }
        }
        DataInputStream dis = new DataInputStream(is);
        logger.debug("尝试读取数据："+url);
        //System.out.println("POST返回区域大小："+dis.available());
        int length = dis.available();
        if(length<=1024){
            length = 1024;
        }
        byte d[] = new byte[length];
        int code = con.getResponseCode();
        OutputStream outputStream = null;
        if(code== HttpURLConnection.HTTP_OK){
            outputStream = new FileOutputStream(snapFile);
                //logger.debug("HTTP请求完成："+url);
        }else{
            logger.error("HTTP请求发生错误："+code);
        }
        long fileSize = 0;
        String remoteLogs = "";
        while(true){
            int i= dis.read(d);
            if(i<=0){
                break;
            }
            fileSize+=i;
            if(outputStream!=null){
                outputStream.write(d,0,i);
            }else{
                remoteLogs+=new String(d,0,i,"UTF-8");
            }
        }
        con.disconnect();
        if(outputStream!=null) {
            outputStream.close();
            logger.debug("图片" + picFileName+
                    "从“" + serverUrl+
                    "”拉取完毕，保存为：" +snapFile.getAbsolutePath()+
                    "，大小："+StringUtils.formatBytes(fileSize));
        }else{
            logger.warn("远端生成文件异常："+remoteLogs);
            out.println(remoteLogs);
            return;
        }
    }else{
        if("true".equals(request.getParameter("test"))){
            String picUrl = appConfigurator.getConfig("system.live.snap.testPicPath","F:\\pic\\pid\\")+programId+".jpg";
            logger.debug("测试环境，直接返回图片路径："+picUrl);
            snapFileName = picUrl ;
        }else{
            String commandLine = appConfigurator.getConfig("system.live.snap.cmdLine",
                    "/usr/local/bin/ffmpeg -ss 0 -i 'udp://%addr%?localaddr=192.168.2.88' -map 0:p:%programId% -an -frames 1 -f image2 -y \"%snapFileName%\"");
            commandLine = commandLine.replace("%addr%",addr);
            commandLine = commandLine.replace("%programId%",programId);
            commandLine = commandLine.replace("%snapFileName%",snapFileName);
            CommandRunner runner = new CommandRunner();
            logger.debug("准备执行命令行："+commandLine);
            String snapLogs = (runner.run(commandLine,"/home/fortune/"));
            logger.debug("命令行调用返回结果："+snapLogs);
        }
    }
    try {
        picFile= new File(snapFileName);
        if(picFile.exists()){
            OutputStream jpegOut = response.getOutputStream();
            response.setContentType("image/jpeg");
            response.setHeader("Pragma","No-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setContentLength((int)picFile.length());
            response.setDateHeader("Expires", 0);
            try {
                InputStream in   =   new FileInputStream(picFile);
                byte[] dataBuffer = new byte[102400];
                int readedLength = 1;
                while(readedLength>0){
                    readedLength = in.read(dataBuffer);
                    jpegOut.write(dataBuffer,0,readedLength);
                    if(readedLength<dataBuffer.length){
                        break;
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            jpegOut.flush();
            jpegOut.close();
            return;
        }else{
            result = "生成"+picFileName+"错误！<br/>"+result;
            logger.error("因为某些原因生成文件错误，生成日志："+result);
        }
    } catch (Exception e) {
        logger.error("发生异常："+e.getMessage());
        result += "<br/>异常："+e.getMessage();
        e.printStackTrace();
    }
    response.setStatus(404);
%><html>
<head>
    <title>出大事儿了！</title>
</head>
<body>
<%=result%>
</body>
</html><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.snapLive.jsp");
%>