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
    systemLog.setLog("IF2C�ӿ�:RDN�������ӿ�ʼ����RMS����������");
    systemLogLogicInterface.saveDebugLog(systemLog);
//    logger.debug("����IF2C�ӿ�------start");
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
            systemLog.setLog("IF2C�ӿ�:RDN�������������ݷ�������");
            systemLogLogicInterface.saveDebugLog(systemLog);
            resultResponse = "3|��������";
            e.printStackTrace();
        }
        readResult = new String(readResult.getBytes(), "utf-8");
        final String filePath = request.getRealPath("/")+"/interface/log/";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String fileName ="IF2C"+sf.format(date)+".txt";
        FileUtil.writeNew(filePath,fileName,readResult);
        logger.debug("�ͻ��˴������ݳ���:" + readResult.length() + ",readResult=\n" + readResult);
        systemLog = new SystemLog();
        systemLog.setAdminIp(adminIp);
        systemLog.setLogTime(new Date());
        systemLog.setAdminId(1l);
        systemLog.setLog("IF2C�ӿ�:RDN���������յ������ݳɹ�,�ļ���־���ƣ�"+fileName);
        systemLogLogicInterface.saveDebugLog(systemLog);
        try {
//            readResult = readResult.replace("&", "\n");
        } catch (Exception e) {
            logger.error("�����ַ������̷�������:" + e.getMessage());
        }
//        logger.debug("��������:\n" + readResult);
        try {
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2C�ӿ�:RDN��������ʼ��֤�������ݸ�ʽ����ȷ�ԣ�");
            systemLogLogicInterface.save(systemLog);
            root = XmlUtils.getRootFromXmlStr(readResult);
            if(root!=null){
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF2C�ӿ�:���ݸ�ʽ��֤�ɹ���");
                systemLogLogicInterface.saveDebugLog(systemLog);
                resultResponse = "0|�ɹ�";
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
                systemLog.setLog("IF2C�ӿ�:���ݸ�ʽ��֤ʧ��");
                systemLogLogicInterface.saveDebugLog(systemLog);
                resultResponse = "1|XML�����Ϲ淶";
            }


        } catch (Exception e) {
            resultResponse = "1|XML�����Ϲ淶";
            e.printStackTrace();
        }
        systemLog = new SystemLog();
        systemLog.setAdminIp(adminIp);
        systemLog.setLogTime(new Date());
        systemLog.setAdminId(1l);
        systemLog.setLog("IF2C�ӿ�:�ýӿڹ���������");
        systemLogLogicInterface.saveDebugLog(systemLog);
//        logger.debug("����IF2C�ӿ�------end");
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
            systemLog.setLog("IF2G�ӿ�:��ʼ�����IF2B�ӿ��ϴ����ļ���");
            systemLogLogicInterface.saveDebugLog(systemLog);
//            logger.debug("����IF2G�ӿ�------start");
            String xmlStr = createXmlStr(root);

            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            String fileName ="IF2G"+sf.format(date)+".txt";
            FileUtil.writeNew(filePath,fileName,xmlStr);

            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G�ӿ�:�ļ���������������Ҫ���ص�xml����,�ļ���־���ƣ�"+fileName);
            systemLogLogicInterface.saveDebugLog(systemLog);
            logger.debug("IF2G xml :" + xmlStr);
            postXmlResult(xmlStr,adminIp);
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF2G�ӿ�:�ýӿڹ���������");
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
        logger.debug("��֤�ļ���md5ֵ,�ļ�·��:"+fileUrl);
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
            systemLog.setLog("IF2G�ӿ�:��ʼ��RMS��������������ͬ�������xml������");
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
                    systemLog.setLog("IF2G�ӿ�:���RMS����λ㱨����ķ�����Ϣ��" + line);
                    systemLogLogicInterface.saveDebugLog(systemLog);
                }

            } catch (IOException e) {
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF2G�ӿ�:��RMSϵͳ�㱨�����������");
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