<%@ page import="org.dom4j.Element,
                 com.fortune.util.XmlUtils,
                 org.apache.log4j.Logger,
                 java.util.List,
                 org.dom4j.Node,
                 com.fortune.util.config.Config,
                 java.net.URL,
                 java.net.HttpURLConnection,
                 java.io.*,
                 com.fortune.rms.business.content.model.Content,
                 com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface,
                 com.fortune.util.SpringUtils,
                 com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface,
                 com.fortune.rms.business.content.model.ContentProperty,
                 java.text.SimpleDateFormat,
                 com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface,
                 com.fortune.rms.business.system.model.SystemLog,
                 java.util.Date" %><%@ page import="com.fortune.util.FileUtil" %><%@ page import="java.util.Calendar" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface" %><%@ page import="com.fortune.rms.business.content.model.ContentCsp" %><%@ page import="com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface" %><%@ page import="com.fortune.rms.business.csp.model.Csp" %><%@        page contentType="text/html;charset=GBK" language="java"
        %><%
    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();
    Element root = null;
    Logger logger = Logger.getLogger("com.fortune.jsp.IF1AService.jsp");
    SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
    final String adminIp = request.getRemoteAddr();
    SystemLog systemLog;
    systemLog = new SystemLog();
    systemLog.setAdminIp(adminIp);
    systemLog.setLogTime(new Date());
    systemLog.setAdminId(1l);
    systemLog.setLog("IF1A接口:RDN服务器接开始接收RMS发来的请求！");
    systemLogLogicInterface.saveDebugLog(systemLog);

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
            if (readedLength > 0) {
                readResult = readResult + new String(dataBuffer, 0, readedLength);
                readed += readedLength;
            }

        } catch (Exception e) {
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF1A接口:RDN服务器接收数据发生错误！");
            systemLogLogicInterface.saveDebugLog(systemLog);
            resultResponse = "3|其他错误";
            e.printStackTrace();
        }
        readResult = new String(readResult.getBytes(), "utf-8");
        final String filePath = request.getRealPath("/") + "/interface/log/";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String fileName = "IF1A" + sf.format(date) + ".txt";
        FileUtil.writeNew(filePath, fileName, readResult);
        logger.debug("客户端传来数据长度:" + readResult.length() + ",readResult=\n" + readResult);
        systemLog = new SystemLog();
        systemLog.setAdminIp(adminIp);
        systemLog.setLogTime(new Date());
        systemLog.setAdminId(1l);
        systemLog.setLog("IF1A接口:RDN服务器接收到的数据成功,文件日志名称：" + fileName);
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
            systemLog.setLog("IF1A接口:RDN服务器开始验证接收数据格式的正确性！");
            systemLogLogicInterface.save(systemLog);
            root = XmlUtils.getRootFromXmlStr(readResult);
            if (root != null) {
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF1A接口:数据格式验证成功！");
                systemLogLogicInterface.saveDebugLog(systemLog);

                resultResponse = "0|成功";
                final Element rootTemp = root;
                Runnable runner = new Runnable() {
                    public void run() {
                        IF1BService(rootTemp, filePath, adminIp);
                    }
                };
                Thread t = new Thread(runner);
                t.start();
            } else {
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF1A接口:数据格式验证失败");
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
        systemLog.setLog("IF1A接口:该接口工作结束！");
        systemLogLogicInterface.saveDebugLog(systemLog);
        logger.debug("resultResponse:" + resultResponse);
    }
%><%=resultResponse
%><%!
    Logger logger = Logger.getLogger("com.fortune.jsp.IF1AService.jsp");

    public void IF1BService(Element root, String filePath, String adminIp) {
        try {
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF1B接口:开始同步数据库信息！");
            systemLogLogicInterface.saveDebugLog(systemLog);
            String xmlStr = synData(root, adminIp);

//        xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<SendContentsResult StreamingNo=\"2012021709130172\" TimeStamp=\"20120217091325\">\n" +
//                "<content ContentID=\"4107200020120216006600\" Result=\"0\" Description=\"\">\n" +
//                "<subcontent SubContentID=\"4107200020120216006601\" Result=\"0\" SubContentUrl=\"/Video/20120217/lsmhybznyqxzlxz_h263_8425624.3gp\" Description=\"成功\">\n" +
//                "</subcontent>\n" +
//                "<subcontent SubContentID=\"4107200020120216006602\" Result=\"0\" SubContentUrl=\"/Video/20120217/lsmhybznyqxzlxz_h264_8425627.3gp\" Description=\"成功\">\n" +
//                "</subcontent>\n" +
//                "</content>\n" +
//                "</SendContentsResult>";

            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
            String fileName = "IF1B" + sf.format(date) + ".txt";
            FileUtil.writeNew(filePath, fileName, xmlStr);
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF1B接口:数据同步结束！，生成需要返回的xml数据,文件日志名称：" + fileName);
            systemLogLogicInterface.saveDebugLog(systemLog);
            logger.debug("IF1B xml :" + xmlStr);
            postXmlResult(xmlStr, adminIp);
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF1B接口:该接口工作结束！");
            systemLogLogicInterface.saveDebugLog(systemLog);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String synData(Element root, String adminIp) {
        String xmlResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
        String streamingNo = XmlUtils.getValue(root, "@StreamingNo", "");
        String timeStamp = XmlUtils.getValue(root, "@TimeStamp", "");
        String OPFlag = XmlUtils.getValue(root, "@OPFlag", "");
        xmlResult += "<SendContentsResult StreamingNo=\"" + streamingNo + "\" TimeStamp=\"" + timeStamp + "\">\r\n";
        List contents = root.selectNodes("content");
        String contentId;
        String drmPacked;
        String submitTime;
        String copyRightOwnerCN;
        String copyRightOwnerEN;
        String name;
        String description = "";
        String cpName;
        String cpId;
        int contentResult = -1;
        for (Object node : contents) {
            Node content = (Node) node;
            contentId = XmlUtils.getValue(content, "@ContentID", "");
            drmPacked = XmlUtils.getValue(content, "@DRMPacked", "");
            submitTime = XmlUtils.getValue(content, "@SubmitTime", "");
            copyRightOwnerCN = XmlUtils.getValue(content, "@CopyrightOwnerCN", "");
            copyRightOwnerEN = XmlUtils.getValue(content, "@CopyrightOwnerEN", "");
            name = XmlUtils.getValue(content, "@Name", "");
            description = XmlUtils.getValue(content, "@Description", "");
            cpName = XmlUtils.getValue(content, "@CPName", "");
            cpId = XmlUtils.getValue(content, "@CPID", "");

            try {
                CspLogicInterface cspLogicInterface = (CspLogicInterface) SpringUtils.getBean("cspLogicInterface");
                List<Csp> csps = cspLogicInterface.getCspByCpCode(cpId);

                if (csps != null && csps.size() > 0) {
                    cpId = csps.get(0).getSpId();
                }else{
                    cpId = Config.getStrValue("cms.cpid", "11054540");
                }
            } catch (Exception e) {
                logger.error("获取cpId 出现错误：" + e.getMessage());
            }

            contentResult = synContent(OPFlag, contentId, drmPacked, submitTime, copyRightOwnerCN, copyRightOwnerEN, name, description, cpName, cpId, adminIp);
            xmlResult += "<content ContentID=\"" + contentId + "\" Result=\"" + contentResult + "\" Description=\"\">\r\n";
            List subContents = content.selectNodes("subcontent");
            String subContentId;
            String subContentSize;
            String subContentFormat;
            String subContentName;
            String getMethod;
            String subContentURL;
            String mimeType;
            String modelList;
            String bitRate;
            String serviceType;
            String md5Info;
            int subContentsResult = -1;
            for (Object node1 : subContents) {
                Node subContent = (Node) node1;
                subContentId = XmlUtils.getValue(subContent, "@SubContentID", "");
                subContentSize = XmlUtils.getValue(subContent, "@SubContentSize", "");
                subContentFormat = XmlUtils.getValue(subContent, "@SubContentFormat", "");
                subContentName = XmlUtils.getValue(subContent, "@SubContentName", "");
                getMethod = XmlUtils.getValue(subContent, "@GetMethod", "");
                subContentURL = XmlUtils.getValue(subContent, "@SubContentURL", "");
                mimeType = XmlUtils.getValue(subContent, "@MimeType", "");
                modelList = XmlUtils.getValue(subContent, "@ModelList", "");
                bitRate = XmlUtils.getValue(subContent, "@BitRate", "");
                serviceType = XmlUtils.getValue(subContent, "@ServiceType", "");
                md5Info = XmlUtils.getValue(subContent, "@MD5info", "");
                subContentsResult = synSubContent(OPFlag, contentId, subContentId, subContentSize, subContentFormat, subContentName, getMethod, subContentURL, mimeType, modelList, bitRate, serviceType, md5Info, adminIp);
                if (subContentsResult == 0) {
                    List copyrights = subContent.selectNodes("copyrights");

                    for (Object node2 : copyrights) {
                        Node copyright = (Node) node2;
                        List copyrights1 = copyright.selectNodes("copyright");
                        String effectiveTime;
                        String expireTime;
                        for (Object node3 : copyrights1) {
                            Node copyright1 = (Node) node3;
                            effectiveTime = XmlUtils.getValue(copyright1, "@EffectiveTime", "");
                            expireTime = XmlUtils.getValue(copyright1, "@ExpireTime", "");
                            subContentsResult = synCopyRight(OPFlag, effectiveTime, expireTime, contentId);
                        }

                    }
                }
                if (subContentsResult == 0) {
                    description = "执行成功";
                } else if (subContentsResult == 1) {
                    description = "修改内容已存在";
                } else if (subContentsResult == 2) {
                    description = "修改内容不存在";
                } else if (subContentsResult == 3) {
                    description = "删除内容不存在";
                } else if (subContentsResult == 4) {
                    description = "服务器处理异常";
                } else if (subContentsResult == 5) {
                    description = "其它错误";
                } else {
                    description = "失败";
                }
                xmlResult += "<subcontent SubContentID=\"" + subContentId + "\" Result=\"" + subContentsResult + "\" SubContentUrl=\"" + subContentURL + "\" Description=\"" + description + "\">\r\n</subcontent>\r\n";


            }
            xmlResult += "</content>\r\n";
        }
        xmlResult += "</SendContentsResult>";
        return xmlResult;
    }

    public void postXmlResult(String xmlStr, String adminIp) {
        SystemLogLogicInterface systemLogLogicInterface = null;
        try {
            systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;
            systemLog = new SystemLog();
            systemLog.setAdminIp(adminIp);
            systemLog.setLogTime(new Date());
            systemLog.setAdminId(1l);
            systemLog.setLog("IF1B接口:开始向RMS服务器返回数据同步结果的xml数据流");
            systemLogLogicInterface.saveDebugLog(systemLog);

            String urlStr = Config.getStrValue("IF1B.url", "");

            URL url;
            HttpURLConnection con;
            DataOutputStream printout = null;
            BufferedReader br = null;
            try {
                byte[] xmlData = xmlStr.getBytes("UTF-8");
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.addRequestProperty("content-type", "text/xml;charset=UTF-8");
                con.setRequestProperty("Content-length", String.valueOf(xmlData.length));
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
                    logger.debug("IF1B result:" + line);
                    systemLog = new SystemLog();
                    systemLog.setAdminIp(adminIp);
                    systemLog.setLogTime(new Date());
                    systemLog.setAdminId(1l);
                    systemLog.setLog("IF1B接口:获得RMS对这次汇报结果的反馈信息：" + line);
                    systemLogLogicInterface.saveDebugLog(systemLog);
                }

            } catch (IOException e) {
                systemLog = new SystemLog();
                systemLog.setAdminIp(adminIp);
                systemLog.setLogTime(new Date());
                systemLog.setAdminId(1l);
                systemLog.setLog("IF1B接口:向RMS系统汇报结果发生错误！");
                systemLogLogicInterface.saveDebugLog(systemLog);
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

    public int synContent(String OPFlag, String contentId, String drmPacked, String submitTime, String copyRightOwnerCN, String copyRightOwnerEN, String name, String description, String cpName, String cpId, String adminIp) {
        SystemLogLogicInterface systemLogLogicInterface = null;

        int result = -1;
        try {
            systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;


//        logger.debug("同步Content数据---start");
//        logger.debug("ContentId:" + contentId);


            try {
                ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface");
                ContentCspLogicInterface contentCspLogicInterface = (ContentCspLogicInterface) SpringUtils.getBean("contentCspLogicInterface");
                CspLogicInterface cspLogicInterface = (CspLogicInterface) SpringUtils.getBean("cspLogicInterface");
                Content content = new Content();
                content.setContentId(contentId);
                content.setName(name);
                content.setStatus(1l);
                content.setCreateTime(new Date());
                content.setIntro(description);
                ContentCsp contentCsp = new ContentCsp();
               // Csp csp = new Csp();
                //数据封装
                boolean exist = false;
                if (OPFlag.equals("1")) {
                    exist = contentLogicInterface.existContentId(contentId);
                    if (exist) {
                    } else {
                        systemLog = new SystemLog();
                        systemLog.setAdminIp(adminIp);
                        systemLog.setLogTime(new Date());
                        systemLog.setAdminId(1l);
                        systemLog.setLog("IF1B接口:向Content表写入数据:" + content.toString());
                        systemLogLogicInterface.saveDebugLog(systemLog);
//                        csp.setName(cpName);
//                        csp.setSpId(cpId);
//                        if (cspLogicInterface.existSpId(csp.getSpId())) {
//                            csp = cspLogicInterface.getCspBySpId(csp.getSpId());
//                        } else {
//                            csp = cspLogicInterface.save(csp);
//
//                        }
                        content.setModuleId(5000l);
                        content.setCspId(Long.parseLong(cpId));
                        content = contentLogicInterface.save(content);
                        contentCsp.setContentId(content.getId());
                        contentCspLogicInterface.save(contentCsp);


                    }
                } else if (OPFlag.equals("2")) {
                    exist = contentLogicInterface.existContentId(contentId);
                    if (exist) {
                        systemLog = new SystemLog();
                        systemLog.setAdminIp(adminIp);
                        systemLog.setLogTime(new Date());
                        systemLog.setAdminId(1l);
                        systemLog.setLog("IF1B接口:更新Content表数据:" + content.toString());
                        systemLogLogicInterface.saveDebugLog(systemLog);
                        contentLogicInterface.updateContentByContentId(content);
                    }
                } else if (OPFlag.equals("3")) {
                    exist = contentLogicInterface.existContentId(contentId);
                    if (exist) {
                        systemLog = new SystemLog();
                        systemLog.setAdminIp(adminIp);
                        systemLog.setLogTime(new Date());
                        systemLog.setAdminId(1l);
                        systemLog.setLog("IF1B接口:删除Content表数据:" + content.toString());
                        systemLogLogicInterface.saveDebugLog(systemLog);
                        contentLogicInterface.removeContentByContentId(content);
                    }
                }
                result = 0;

            } catch (Exception e) {
                result = 2;
                System.out.println(e.getMessage() + "发成错误");
                logger.error("" + e.getMessage());
            }

        } catch (Exception e) {
            result = 2;
            e.printStackTrace();
        }

//        logger.debug("同步Content数据---end");
        return result;
    }

    public int synSubContent(String OPFlag, String contentId, String subContentId, String subContentSize, String subContentFormat, String subContentName, String getMethod, String subContentURL, String mimeType, String modelList, String bitRate, String serviceType, String md5Info, String adminIp) {
//        logger.debug("同步subContent数据---start");
//        logger.debug("subContentId:" + subContentId);
        int result = -1;
        try {
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
            SystemLog systemLog;
            try {
                ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface) SpringUtils.getBean("contentPropertyLogicInterface");
                ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface");
                ContentProperty contentProperty = new ContentProperty();
                if (contentLogicInterface.existContentId(contentId)) {
                    Content content = contentLogicInterface.getContent(contentId);
                    if (content != null && content.getId() > 0) {
                        contentProperty.setContentId(content.getId());
                    }
                }
                contentProperty.setSubContentId(subContentId);
                contentProperty.setStringValue(subContentURL);
                contentProperty.setPropertyId(5053l);
                contentProperty.setName(subContentName);
                //数据封装
                boolean exist = false;
                if (OPFlag.equals("1")) {
                    exist = contentPropertyLogicInterface.existsSubContentId(contentProperty.getSubContentId());
                    if (exist) {
                        result = 1;
                    } else {
                        systemLog = new SystemLog();
                        systemLog.setAdminIp(adminIp);
                        systemLog.setLogTime(new Date());
                        systemLog.setAdminId(1l);
                        systemLog.setLog("IF1B接口:向ContentProperty表写入数据:" + contentProperty.toString());
                        systemLogLogicInterface.saveDebugLog(systemLog);
                        contentPropertyLogicInterface.save(contentProperty);
                        result = 0;
                    }
                } else if (OPFlag.equals("2")) {
                    exist = contentPropertyLogicInterface.existsSubContentId(contentProperty.getSubContentId());
                    if (exist) {
                        systemLog = new SystemLog();
                        systemLog.setAdminIp(adminIp);
                        systemLog.setLogTime(new Date());
                        systemLog.setAdminId(1l);
                        systemLog.setLog("IF1B接口:更新ContentProperty表数据:" + contentProperty.toString());
                        systemLogLogicInterface.saveDebugLog(systemLog);
                        contentPropertyLogicInterface.updateContentPropertyBySubContentId(contentProperty);
                        result = 0;
                    } else {
                        result = 2;
                    }
                } else if (OPFlag.equals("3")) {
                    systemLog = new SystemLog();
                    systemLog.setAdminIp(adminIp);
                    systemLog.setLogTime(new Date());
                    systemLog.setAdminId(1l);
                    systemLog.setLog("IF1B接口:删除ContentProperty表数据:" + contentProperty.toString());
                    systemLogLogicInterface.saveDebugLog(systemLog);
                    contentPropertyLogicInterface.updateContentPropertyBySubContentId(contentProperty);

                    exist = contentPropertyLogicInterface.existsSubContentId(contentProperty.getSubContentId());
                    if (exist) {
                        contentPropertyLogicInterface.removeContentPropertyBySubContentId(contentProperty);
                        result = 0;
                    } else {
                        result = 3;
                    }
                } else {
                    result = 5;
                }


            } catch (Exception e) {
                result = 4;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        logger.debug("同步subContent数据---end");
        return result;
    }

    public int synCopyRight(String OPFlag, String effectiveTime, String expireTime, String contentId) {
//        logger.debug("同步版权-----start");
        int result = -1;
        try {
            ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface");
            Content content = contentLogicInterface.getContent(contentId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            content.setValidStartTime(sf.parse(effectiveTime));
            content.setValidEndTime(sf.parse(expireTime));
            contentLogicInterface.update(content);
            result = 0;
        } catch (Exception e) {
            result = 2;
            logger.error(e.getMessage());
        }
//        logger.debug("同步版权-----end");
        return result;
    }
%>