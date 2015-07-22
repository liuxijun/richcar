<%@ page import="org.apache.log4j.Logger,
                 org.dom4j.Element,
                 com.fortune.util.XmlUtils,
                 org.dom4j.Node,java.util.*,
                 com.fortune.util.MD5Utils,
                 java.security.NoSuchAlgorithmException,
                 java.net.URL,java.io.*,
                 com.fortune.util.config.Config,
                 java.net.HttpURLConnection,
                 com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface,
                 com.fortune.rms.business.system.model.SystemLog,
                 com.fortune.util.SpringUtils" %><%@ page
        import="java.text.SimpleDateFormat" %><%@ page
        import="com.fortune.util.FileUtil" %><%@page
        contentType="text/html;charset=GBK" language="java"%><%
    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();
    Element root = null;
    Logger logger = Logger.getLogger("com.fortune.jsp.IF2CService.jsp");
    SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
    SystemLog systemLog;
    final String adminIp = request.getRemoteAddr();
    systemLog = new SystemLog();
    systemLog.setAdminIp(adminIp);
    systemLog.setLogTime(new Date());
    systemLog.setAdminId(1l);
    systemLog.setLog("IF2C接口:RDN服务器接开始接收RMS发来的请求！");
    systemLogLogicInterface.saveDebugLog(systemLog);
//    logger.debug("调用IF2C接口------start");
//    int resultCode = -1;
    String resultResponse = "";
    if (dataLength > 0) {
        byte[] dataBuffer = new byte[dataLength];
        int readedLength = is.read(dataBuffer);
        String readResult = "";
        int readed = 0;
        try {
            while (readed < dataLength && readedLength > 0) {
                readResult = readResult + new String(dataBuffer, 0, readedLength);
                readed += readedLength;
                readedLength = is.read(dataBuffer);
            }
            if(readedLength>0){
                readResult = readResult + new String(dataBuffer,0,readedLength);
                readed+=readedLength;
            }

        } catch (Exception e) {
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2C接口:RDN服务器接收数据发生错误！");
            systemLogLogicInterface.saveDebugLog(systemLog);
            resultResponse = "3|其他错误";
            e.printStackTrace();
        }
        readResult = new String(readResult.getBytes(), "utf-8");
        final String filePath = request.getRealPath("/")+"/interface/log/";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String fileName ="IF2C"+sf.format(date)+".txt";
        FileUtil.writeNew(filePath,fileName,readResult);
        logger.debug("客户端传来数据长度:" + readResult.length() + ",readResult=\n" + readResult);
        systemLog = new SystemLog();
        systemLog.setAdminIp(adminIp);
        systemLog.setLogTime(new Date());
        systemLog.setAdminId(1l);
        systemLog.setLog("IF2C接口:RDN服务器接收到的数据成功,文件日志名称："+fileName);
        systemLogLogicInterface.saveDebugLog(systemLog);
        try {
//            readResult = readResult.replace("&", "\n");
        } catch (Exception e) {
            logger.error("处理字符串过程发生意外:" + e.getMessage());
        }
//        logger.debug("调用数据:\n" + readResult);
        try {
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2C接口:RDN服务器开始验证接收数据格式的正确性！");
            systemLogLogicInterface.save(systemLog);
            root = XmlUtils.getRootFromXmlStr(readResult);
            if(root!=null){
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF2C接口:数据格式验证成功！");
                systemLogLogicInterface.saveDebugLog(systemLog);
                resultResponse = "0|成功";
                final Element rootTemp = root;
                Runnable runner = new Runnable() {
                    public void run() {
                        IF2GService(rootTemp,filePath,adminIp);
                    }
                };
                Thread t = new Thread(runner);
                t.start();
            }else{
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF2C接口:数据格式验证失败");
                systemLogLogicInterface.saveDebugLog(systemLog);
                resultResponse = "1|XML不符合规范";
            }


        } catch (Exception e) {
            resultResponse = "1|XML不符合规范";
            e.printStackTrace();
        }
        systemLog = new SystemLog();
        systemLog.setAdminIp(adminIp);
        systemLog.setLogTime(new Date());
        systemLog.setAdminId(1l);
        systemLog.setLog("IF2C接口:该接口工作结束！");
        systemLogLogicInterface.saveDebugLog(systemLog);
//        logger.debug("调用IF2C接口------end");
    }
%><%=resultResponse
%><%!
    Logger logger = Logger.getLogger("com.fortune.jsp.IF2GService.jsp");
    public void IF2GService(Element root,String filePath,String adminIp){
        try {
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G接口:开始检查由IF2B接口上传的文件！");
            systemLogLogicInterface.saveDebugLog(systemLog);
//            logger.debug("调用IF2G接口------start");
            String xmlStr = createXmlStr(root);

            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            String fileName ="IF2G"+sf.format(date)+".txt";
            FileUtil.writeNew(filePath,fileName,xmlStr);

            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G接口:文件检查结束！生成需要返回的xml数据,文件日志名称："+fileName);
            systemLogLogicInterface.saveDebugLog(systemLog);
            logger.debug("IF2G xml :" + xmlStr);
            postXmlResult(xmlStr,adminIp);
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G接口:该接口工作结束！");
            systemLogLogicInterface.saveDebugLog(systemLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String createXmlStr(Element root){
        boolean md5Status = false;
        String fileUrl="";
        String subContentID="";
        String checkSum="";
        String regPlatform="";
        String Result="";
        String Description="";
        String CDNURI="";
        String xmlStr="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        String streamingNo = XmlUtils.getValue(root,"@StreamingNo","");
        String timeStamp = XmlUtils.getValue(root,"@TimeStamp","");
        xmlStr+="<FilePushResult StreamingNo=\""+streamingNo+"\" TimeStamp=\""+timeStamp+"\">";
        List fileInfos = root.selectNodes("FileInfo");
        for (Object node : fileInfos) {
            Node fileInfoNode = (Node) node;
            fileUrl = XmlUtils.getValue(fileInfoNode, "@FileUrl", "");
            subContentID = XmlUtils.getValue(fileInfoNode, "@SubContentID", "");
            checkSum = XmlUtils.getValue(fileInfoNode, "@Checksum", "");
            regPlatform = XmlUtils.getValue(fileInfoNode, "@RegPlatform", "");
            md5Status = checkFileMd5(fileUrl,checkSum);
            if(md5Status){
                Result = "0";
            }else{
                Result = "2";
            }
            xmlStr+="<FileInfo SubContentID=\""+subContentID+"\" Result=\""+Result+"\" Description=\""+Description+"\" CDNURI=\""+fileUrl+"\"></FileInfo>";

        }
        xmlStr+="</FilePushResult>";
        return xmlStr;
    }

    public boolean checkFileMd5(String fileUrl,String md5){
        logger.debug("验证文件的md5值,文件路径:"+fileUrl);
        String ftpRoot = Config.getStrValue("ftp.root","");
        boolean temp = false;
        File file = new File(ftpRoot+fileUrl);
        if(file.exists()){
            try {
                String newMd5 = MD5Utils.getFileMD5String(file);
                if(newMd5.equals(md5)){
                    temp = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public void postXmlResult(String xmlStr,String adminIp) {
        SystemLogLogicInterface  systemLogLogicInterface = null;
        try {
            systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G接口:开始向RMS服务器返回数据同步结果的xml数据流");
            systemLogLogicInterface.saveDebugLog(systemLog);
            String urlStr = Config.getStrValue("IF2G.url","");
            URL url;
            HttpURLConnection con;
            DataOutputStream  printout = null;
            BufferedReader br = null;
            try {
                byte[] xmlData = xmlStr.getBytes("UTF-8");
                url = new URL(urlStr);
                con =(HttpURLConnection) url.openConnection();
                con.addRequestProperty("content-type","text/xml;charset=UTF-8");
                con.setRequestProperty("Content-length",String.valueOf(xmlData.length));
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                printout = new DataOutputStream(con.getOutputStream());
                printout.write(xmlData);
                printout.flush();


                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                for (line = br.readLine(); line != null; line = br.readLine()) {
               logger.debug("IF2G result:"+line);
                    systemLog = new SystemLog();
                    systemLog.setAdminIp(adminIp);
                    systemLog.setLogTime(new Date());
                    systemLog.setAdminId(1l);
                    systemLog.setLog("IF2G接口:获得RMS对这次汇报结果的反馈信息：" + line);
                    systemLogLogicInterface.saveDebugLog(systemLog);
                }

            } catch (IOException e) {
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF2G接口:向RMS系统汇报结果发生错误！");
                e.printStackTrace();
            } finally {

                try {
                    if (printout != null) {
                        printout.close();

                    }
                    if (br != null) {
                        br.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


%>