<%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@page
        import="com.fortune.util.AppConfigurator,com.fortune.util.SpringUtils" %><%@ page
        import="java.io.*" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="java.net.URLDecoder" %><%@ page
        import="java.net.URL" %><%@ page
        import="java.net.HttpURLConnection" %><%@ page
        import="org.apache.commons.io.FileUtils" %><%@ page
        import="com.fortune.util.MediaUtils" %><%@ page
        import="java.util.Date" %><%@ page
        import="java.net.URLEncoder" %><%
    int cutTime = StringUtils.string2int(request.getParameter("startTime"),60);
    long deviceId =  StringUtils.string2long(request.getParameter("deviceId"), 0);
    String picFileName = request.getParameter("thumbPicUrl");
    String mediaUrl = request.getParameter("mediaUrl");
    int times = 0;
    if(mediaUrl!=null){
        while(times<5&&mediaUrl.contains("%")){
            mediaUrl = URLDecoder.decode(mediaUrl,"UTF-8");
            times++;
            logger.debug("第" +times+
                    "次解码，当前文件链接："+mediaUrl);
        }
    }
    logger.debug("解码后文件链接："+mediaUrl);
    //检查是否是本机，如果是本机任务，则截图，否则就下载一个截图，然后传给客户端
    AppConfigurator appConfig = AppConfigurator.getInstance();
    String result = "";
    long localDeviceId = appConfig.getIntConfig("system.encoder.encoderId",-1); //EncodeTaskManager.java中也用到了这个
    DeviceLogicInterface deviceLogic = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
    Device device;
    String snapFileName = appConfig.getConfig("system.tools.snap.dir", application.getRealPath("/"))+picFileName ;
    String sourceFile;
    int width = StringUtils.string2int(request.getParameter("width"),-1);
    int height = StringUtils.string2int(request.getParameter("height"),-1);
    snapFileName = snapFileName.replace("/",File.separator);
    try {
        device = deviceLogic.get(deviceId);
        boolean remoteMode =localDeviceId!=deviceId&&request.getParameter("fromServer")==null;
        remoteMode = remoteMode && !device.getUrl().startsWith("http://serverIP");
        if(remoteMode){
            logger.debug("客户端" + request.getRemoteAddr()+"("+session.getAttribute("admin")+")"+
                    "请求"+picFileName+"需要到远端服务器去拉："+device.getUrl());
            String snapServerUrl = AppConfigurator.getInstance().getConfig("snap.remoteServerUrl",device.getUrl()/*"http://61.55.144.81"*/);
            String serverAddr = request.getServerName();
            if("10.0.66.11".equals(serverAddr)||"119.191.61.22".equals(serverAddr)){
                snapServerUrl = snapServerUrl.replace("http://hls.weichai.com/in","http://localhost");
            }else{
                snapServerUrl = snapServerUrl.replace("http://hls.weichai.com/in","http://119.191.61.22:8080");
                snapServerUrl = snapServerUrl.replace("http://hls.weichai.com/out","http://120.27.29.191");
                snapServerUrl = snapServerUrl.replace("http://hls.inhe.net","http://61.55.144.81");
            }
            String url = snapServerUrl+"/interface/snapMedia.jsp?fromServer=true&deviceId="+deviceId+
                    "&startTime="+cutTime+"&date="+(new Date()).getTime()+"&width="+width+"&height="+height+
                    "&thumbPicUrl="+picFileName+"&mediaUrl="+ URLEncoder.encode(URLEncoder.encode(mediaUrl, "UTF-8"),"UTF-8");
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
            OutputStream outputStream = new FileOutputStream(snapFile);
            DataInputStream dis = new DataInputStream(is);
            logger.debug("尝试读取数据："+url);
            //System.out.println("POST返回区域大小："+dis.available());
            int code = con.getResponseCode();
            byte d[] = new byte[dis.available()];
            long fileSize = 0;
            while(true){
                int i= dis.read(d);
                if(i<=0){
                    break;
                }
                fileSize+=i;
                outputStream.write(d,0,i);
            }
            outputStream.close();
            logger.debug("图片" + picFileName+
                    "从“" + device.getName()+
                    "”拉取完毕，大小："+StringUtils.formatBytes(fileSize));
            if(code==HttpURLConnection.HTTP_OK){
                //logger.debug("HTTP请求完成："+url);
            }else{
                logger.error("HTTP请求发生错误："+code);
            }
            con.disconnect();
        }else{
            while(mediaUrl.contains("%")){
                mediaUrl = URLDecoder.decode(mediaUrl,"UTF-8");
            }
            logger.debug("将要处理的文件连接："+mediaUrl);
            if(device!=null){
                sourceFile = new File(device.getLocalPath()+"/"+mediaUrl).getAbsolutePath();
                MediaUtils mu = new MediaUtils();
                logger.debug("将要处理的源文件："+sourceFile);
                if(width<=0||height<=0){
                    mu.snap(sourceFile, snapFileName, cutTime);
                }else{
                    mu.snapContentPosterFromMediaFile(sourceFile,snapFileName,width,height,cutTime);
                }
                //logger.debug();
            }
            //logger.debug("将会保存的文件名："+snapFileName);
        }
    } catch (Exception e) {
        logger.debug("准备获取服务器信息时发生错误："+e.getMessage());
        result += "<br/>异常："+e.getMessage();
    }
    try {
        File picFile= new File(snapFileName);
        if(picFile.exists()){
            OutputStream jpegOut = response.getOutputStream();
            response.setContentType("image/jpeg");
            response.setHeader("Pragma","No-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setContentLength((int)picFile.length());
            response.setDateHeader("Expires", 0);
            try {
                InputStream   in   =   new FileInputStream(picFile);
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

%><html>
<head>
    <title>截图错误！</title>
</head>
<body>
<%=result%>
</body>
</html><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.snapMedia.jsp");
    public String snap(String fileName,String picFileName,  int cutTime) throws Exception{
        if(picFileName==null||"".equals(picFileName)){
            return "";
        }
        AppConfigurator appConfig = AppConfigurator.getInstance();
        //logger.debug("将会保存的文件名："+picFileName);
        //最终文件名。因为mplayer生成的都是00000001.jpg，所以需要我们处理一下。
        String snapToolsExeCmdLine = appConfig.getConfig("system.tools.snap.cmdLine",
                "C:\\FFModules\\Encoder\\mplayer.exe" +
                        " -nosound -vo jpeg -frames 1 -ss %startTime% \"%sourceFile%\"");

        String result="";
        try {

            //fileName=fileName.replace("/",File.separator);
            //logger.debug("将要截取的文件为："+fileName);
            File sourceFile = new File(fileName);
            File picFile = new File(picFileName);
            if(sourceFile.exists()){

                String desertPicFileName = picFile.getAbsolutePath();
                String picFilePath = picFile.getParentFile().getAbsolutePath();
                String snapDirName = picFilePath+File.separator;//appConfig.getConfig("snap.Dir", application.getRealPath("/"))+picFilePath+ File.separator ;
                //snapDirName = snapDirName.replace("/",File.separator);
                //snapDirName = snapDirName.replace("\\",File.separator);
                String snapTempDirName = snapDirName+"temp_"+System.currentTimeMillis()+"_"+Math.round(Math.random()*10000000);
                String autoPicFileName = snapTempDirName + File.separator +"00000001.jpg";
                //logger.debug("自动生成的文件名："+autoPicFileName);
                File snapDir = new File(snapTempDirName);
                if(!snapDir.exists()){
                    logger.debug("目录不存在，正在生成...："+snapTempDirName);
                    if(snapDir.mkdirs()){
                        //logger.debug("创建目录成功："+snapDir.getAbsolutePath());
                    }else{
                        logger.warn("创建目录失败："+snapDir.getAbsolutePath());
                    }
                }
                String sourceFileName = sourceFile.getAbsolutePath().replace("\\","\\\\");
                String commandLine =snapToolsExeCmdLine.replaceAll("%startTime%", "" + cutTime).replaceAll("%sourceFile%", sourceFileName);
                logger.debug("将要执行的命令行："+commandLine);
                if(run(commandLine,snapTempDirName)==0){
                    picFile= new File(autoPicFileName);
                    if(picFile.exists()){
                        //response.sendRedirect(picFileName);
                        File desertPicFile = new File(desertPicFileName);
                        if(desertPicFile.exists()){
                            if(desertPicFile.delete()){
                                logger.debug("原来的文件删除，做替换："+desertPicFile.getAbsolutePath());
                            }else{
                                logger.warn("删除失败，后续操作可能无法完成："+desertPicFile.getAbsolutePath());
                            }
                        }

                        logger.debug("准备复制：["+picFile.getAbsolutePath()+"]->["+desertPicFile.getAbsolutePath()+"]");
                        try {
                            FileUtils.copyFile(picFile, desertPicFile);
                        } catch (IOException e) {
                            
                            logger.error("源文件" +picFile.getAbsolutePath()+"（"+StringUtils.formatBytes(picFile.length())+")"+
                                    "无法文件复制到"+desertPicFile.getAbsolutePath()+","+e.getLocalizedMessage());
                        }
                        if(picFile.delete()&&snapDir.delete()){
                            //logger.debug("临时文件删除完成："+snapDir.getAbsolutePath());
                        }else{
                            logger.error("临时文件删除："+snapDir.getAbsolutePath());
                        }
                        return "文件创建成功："+desertPicFile.getAbsolutePath();
                    }else{
                        result = "生成"+picFileName+"错误："+result;
                        logger.error("因为某些原因生成文件错误，生成日志："+result);
                    }
                }else{
                    logger.error("运行过程中发生异常！");
                }
            }else{
                logger.error("WMV文件不存在："+fileName);
                result = "WMV文件不存在："+fileName;
            }
        } catch (Exception e) {
            logger.error("发生异常："+e.getMessage());
            result += "异常："+e.getMessage();
            //e.printStackTrace();
        }
        throw  new Exception(result);
    }
    public void processLine(String line){
        logger.debug(line);
    }
    public int run(String sCommandLine,String currentDirName) {
        int result =0;
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            File currentDir = new File(currentDirName);
            Process process;
            if(currentDir.exists()&& currentDir.isDirectory()){
                process = runtime.exec(sCommandLine,null,currentDir);
            }else{
                process = runtime.exec(sCommandLine);
            }
            final InputStream is1 = process.getInputStream();
            final InputStream is2 = process.getErrorStream();
            new Thread() {
                public void run() {
                    BufferedReader br = new BufferedReader( new
                            InputStreamReader(is1));
                    try {
                        String lineB;
                        while ((lineB = br.readLine()) != null ){
                            //logger.debug(lineB);
                            processLine(lineB);
                            //result.add(lineB);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    BufferedReader br2 = new BufferedReader( new
                            InputStreamReader(is2));
                    try {
                        String lineC;
                        while ( (lineC = br2.readLine()) != null){
                            logger.error(lineC);
                            processLine(lineC);
                            //result.add(lineC);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            try {
                result = process.waitFor();
                is1.close();
                is2.close();
                logger.debug("执行完毕！");
            } catch (InterruptedException e) {
                result = -2;
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        return result;
    }
%>