<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-10-31
  Time: 16:04:42
  To change this template use File | Settings | File Templates.
--%><%@ page import="java.io.InputStream,java.util.Date,com.fortune.util.*,
    com.fortune.util.StringUtils,com.fortune.rms.business.system.logic.logicInterface.*,
    com.fortune.rms.business.system.model.*,
    com.fortune.rms.business.system.logic.logicInterface.*,
    com.fortune.rms.business.system.model.*,
    com.fortune.rms.business.content.logic.logicInterface.*,
    com.fortune.rms.business.content.model.*,
    org.dom4j.Element,org.dom4j.Node,java.util.Map,
    java.util.HashMap,java.util.List" %><%@ page
        import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.server.message.ServerMessager" %>
<%@ page import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %>
<%@ page import="com.fortune.rms.business.publish.model.Channel" %>
<%@ page
        contentType="text/html;charset=GBK" language="java"  %><%
    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();
    int resultCode = -1;
    String message = "",error="";
    Map<String,Map<String,String>> outParams = new HashMap<String,Map<String,String>>();
    Map<String,String> result = new HashMap<String,String>();
    outParams.put("",result);
    String command = "noAnyCommand";
    if(dataLength>0){
        byte[] dataBuffer = new byte[dataLength];
        int readedLength = is.read(dataBuffer);
        String readResult =new String(dataBuffer,0,readedLength);
        //logger.debug("�ͻ��˴������ݣ�"+readResult);
        try {
            readResult =  readResult.replace("&","\n");
        } catch (Exception e) {
            logger.error("�����ַ��������з������⣺"+e.getMessage());
        }
        //logger.debug("�������ݣ�\n"+readResult);
        try {
            Element root = XmlUtils.getRootFromXmlStr(readResult.trim());
            if(root!=null){
                String type = getReqType(root);
                if(!"encoder".equals(type)){

                }
                command = getTaskTarget(root);
                if(command==null||"".equals(command)){
                    command = request.getParameter("command");
                }
                Map<String,String> params = getParameters(root);
                int missionId = StringUtils.string2int(params.get("missionId"),-1) ;
                Map<String,String> header = new HashMap<String,String>();
                outParams.put("header",header);
                //header.put("command",command);
                header.put("missionId",""+missionId);
                ServletContext context = session.getServletContext();
                DeviceLogicInterface deviceLogic = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",
                        context);
                ChannelLogicInterface channelLogic = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface");
                ContentLogicInterface mediaLogic = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",
                        context);
                logger.debug("command="+command);
                if("afterStoped".equals(command)){
                    logger.debug("����ֹͣ");
                    Map<String,String> ftpServer = new HashMap<String,String>();
                    outParams.put("ftp",ftpServer);
                    if("true".equals(params.get("notNeedPublish"))){
                        //�������Ҫ����
                        ftpServer.put("ftpServerReady","-1");
                        int mediaId=StringUtils.string2int(params.get("mediaId"),-1);
                        logger.debug("��ý�岻��Ҫ������ʹ֮����:"+mediaId);
                        if(mediaId>0){
                            try {
                                Content media = mediaLogic.get(mediaId);
                                if(media!=null){
                                    //�ҵ�ý�壬ʹ֮����
                                    //todo ����ý����뻹û��ʵ��
                                    //mediaLogic.save(media);
                                }
                            } catch (Exception e) {
                                logger.error("��������з����쳣��"+e.getMessage());
                                e.printStackTrace();
                            }
                        }else{
                            logger.error("mediaIdû�����룬�޷����߲�����");
                        }
                    }else{
                        Integer deviceId=null;
                        getMediaServer(deviceId,ftpServer,deviceLogic,params);
                    }
                }else if("syncData".equals(command)){//�ͻ�������
                    logger.debug("׼������ͬ�����ݸ��ͻ���");
                    Map<String,String> channels=new HashMap<String,String>();
                    outParams.put("channels",channels);
                    Channel searchBean = new Channel();
                    searchBean.setCspId(AppConfigurator.getInstance().getLongConfig("default.cspId",520));
                    List<Channel> channelItems = channelLogic.search(searchBean);
                    if(channelItems!=null){
                        channels.put("channelCount",""+channelItems.size());
                        for(int i=0;i<channelItems.size();i++){
                            Channel channel = channelItems.get(i);
                            channels.put("channel_"+i+"_name",channel.getName());
                            channels.put("channel_"+i+"_id",""+channel.getId());
                        }
                    }
                }else if("publish".equals(command)){
                    Map<String,String> ftpServer = new HashMap<String,String>();
                    outParams.put("ftp",ftpServer);
                    getMediaServer(-1,ftpServer,deviceLogic,params);
                }else if("beforeStart".equals(command)){
                    Device server = null;
                    Integer serverId;
                    //�����ֱ������ô��׼�������ݿ�����Ӽ�¼
                    Map<String,String> mediaInfo = new HashMap<String,String>();
                    outParams.put("media",mediaInfo);
                    if("true".equals(params.get("live"))){
                        logger.debug("������Ϊֱ����׼��ý���������Ϣ....");
                        if(server==null){
                            server = deviceLogic.getMediaServer();
                        }
                        if(server!=null){
                            //todo ���պ�����߼�������Ӧ�û�ȡ���ͻ��˴����Ķ˿ڣ�Ȼ��֪ͨ�������˵��ͻ��˸ö˿�
                            //todo ��ȡ���ݣ�
                            mediaInfo.put("pushToServer","true");
                            mediaInfo.put("serverName",server.getIp());
                            mediaInfo.put("serverPort",""+8080);
                            Content media = getMediaFromParams(params,mediaLogic,channelLogic,deviceLogic);
                            media.setDigiRightUrl(params.get("url"));
                            try {
                                media.setDeviceId(server.getId());
                                String mediaUrl = media.getDigiRightUrl();
                                if(mediaUrl==null||"".equals(mediaUrl.trim())){
                                    mediaUrl=(StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_"+Math.round(Math.random()*1000000));
                                }
                                media.setDigiRightUrl(mediaUrl);
                                media = mediaLogic.save(media);
                                logger.debug("�½���ý�壺"+media.getName()+",ID="+media.getId());
                                mediaInfo.put("liveUrl",media.getDigiRightUrl());
                                mediaInfo.put("mediaUrl",""+mediaUrl);
                                mediaInfo.put("mediaId",media.getId()+"");
                                mediaInfo.put("name",media.getName());
                                mediaInfo.put("deviceId",""+server.getId());
                            } catch (Exception e) {
                                logger.error("������Ϣʱ�����쳣��"+e.getMessage());
                            }
                        }
                    }else{
                        logger.debug("�������������ֱ�����͵Ȳ���");
                    }
                }else if("afterStarted".equals(command)){
                }else if("onHeart".equals(command)){//����
                    String msg=params.get("message");
                }else if("onError".equals(command)){
                }else if("beforeUploadStart".equals(command)){
                }else if("afterUploadFinished".equals(command)){
                    logger.debug("command="+command);
                    if("false".equals(params.get("uploadResult"))){
                        logger.error("�ϴ�ʧ��");
                    }else{
                        logger.debug("�ϴ����");
                        try {
                            Content media = getMediaFromParams(params,mediaLogic,channelLogic,deviceLogic);
                            media.setCreateTime(new Date());
                            media = mediaLogic.save(media);
                            createDefaultPoster();
                            Map<String,String> mediaInfo = new HashMap<String,String>();
                            outParams.put("media",mediaInfo);
                            mediaInfo.put("mediaId",""+media.getId());
                            mediaInfo.put("name",media.getName());
                        } catch (Exception e) {
                            logger.error("����ý��ʱ�����쳣��"+e.getMessage());
                        }
                    }
                }else if("schedule".equals(command)){
                    header.put("msDeviceId","-1");
                }else if("delete".equals(command)){
                }else if("start".equals(command)||"restart".equals(command)){
                }else if("stop".equals(command)){
                }else if("cancelUpload".equals(command)){
                }else if(null == command){
                    logger.error(" Can't get command(�޷��������)\n"+readResult);
                }
            }else{
                error=("û���������ݣ�"+readResult);
                resultCode = 404;
                logger.error("doc rootû�г�ʼ���ɹ���");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }else{
        error="not data input";
        resultCode = 404;
        logger.error("û����������");
    }
    result.put("resultCode",""+resultCode);
    result.put("message",java.net.URLEncoder.encode(message,"utf-8"));
    result.put("error",java.net.URLEncoder.encode(error,"utf-8"));
    result.put("time",StringUtils.date2string(new Date()));
    if("true".equals(request.getParameter("xmlFormat"))){
        out.print(getReturnXml("res","1.0",command,outParams));
    }else{
        out.print(getParameterIni(command,outParams));
    }
%><%!
    Logger logger = Logger.getLogger("com.fortune.redex.interface.service.jsp");
    public boolean getMediaServer(Integer deviceId,Map<String,String> ftpServer,DeviceLogicInterface deviceLogic,Map<String,String> params){
        boolean result = false;
        Device server=null;
        if(deviceId!=null&&deviceId>0){
            server = deviceLogic.get(deviceId);
        }
        if(server==null){
            server = deviceLogic.getMediaServer();
        }
        if(server!=null){
            result = true;
            ftpServer.put("mediaId",params.get("mediaId"));
            ftpServer.put("ftpServerReady","1");
            ftpServer.put("deviceId",""+server.getDeviceId());
            ftpServer.put("ftpIp",server.getIp());
            ftpServer.put("ftpPort",""+server.getFtpPort());
            ftpServer.put("ftpUser",server.getFtpUser());
            ftpServer.put("ftpPwd",server.getFtpPassword());
            ftpServer.put("ftpPath",server.getFtpPath()+"/"+StringUtils.date2string(new Date(),
                    "yyyy/MM/dd"));
            ftpServer.put("channelId",params.get("channelId"));
        }else{
            ftpServer.put("ftpServerReady","-1");
        }
        return result;
    }

    public Content getMediaFromParams(Map<String,String> params,
                                    ContentLogicInterface mediaLogic,
                                    ChannelLogicInterface channelLogic,
                                    DeviceLogicInterface deviceLogic){
        String channelId;
        Integer mediaId;
        String mediaName;
        String description;
        String mediaPost;
        Date offlineTime;
        Date onlineTime;
        Integer mediaLevel;
        String channelName = params.get("channelName");
        channelId = params.get("channelId");
        Channel channel = null;
        if(channelId!=null&&!"".equals(channelId)){
            channel = channelLogic.get(channelId);

        }
        if(channelName==null||"".equals(channelName.trim())){
            if(channel!=null){
                channelName = channel.getName();
            }
        }
        if(channelName==null){
            channelName="Ƶ������";
        }
        String defaultName=channelName;
        if(defaultName==null||"".equals(defaultName.trim())){
            defaultName = "${fileName}";
        }else{
            defaultName = defaultName+"-${date}";
        }
        if(mission==null){
            logger.debug("missionΪ�գ�ȡȱʡֵ");
            mediaName = defaultName;
            description = params.get("desc");
            if(channel==null){
                mediaPost = "../images/default.jpg";
            }else{
                mediaPost = channel.getPoster();
            }
            offlineTime = StringUtils.string2date("2020-01-01 00:00:00");
            onlineTime = new Date();
            mediaLevel = 1;
            fromSource = MediaLogicInterface.MEDIA_FROM_SOURCE_RECORD;
        }else{
            channelId = mission.getChannelId();
            mediaName = mission.getMediaName();
            if(mediaName==null||"".equals(mediaName)){
                mediaName = mission.getName();
                if(mediaName!=null){
                    int p=mediaName.lastIndexOf("-");
                    if(p>=0){
                        mediaName = mediaName.substring(p+1);
                    }
                    mediaName += "-${date}";
                }
            }
            description = mission.getMediaDesp();
            mediaPost = mission.getMediaPost();
            logger.debug("mission���գ�"+mediaName+":"+description+":"+mediaPost);
            if(mediaPost!=null&&mediaPost.indexOf("images/default.jpg")>=0){
                if(channel!=null){
                    mediaPost = channel.getPoster();
                }
            }
            onlineTime = mission.getOnlineTime();
            offlineTime = mission.getOfflineTime();
            mediaLevel = mission.getMediaLevel();
            mission.setStatus(EncodeMissionLogicInterface.ENCODE_STATUS_STANDBY);
            mission.setRuningMsg(StringUtils.date2string(new Date())+"-������ɣ��������״̬");
            if(channelId==null) channelId = params.get("channelId");
            if(mediaName==null){
                logger.debug("ý������Ϊ�գ�ȡȱʡֵ��"+defaultName);
                mediaName = defaultName;
            }
            if(description==null) description = params.get("desc");
            if(mediaPost == null) mediaPost = "default.jpg";
            Integer encodeType = mission.getEncodeType();
            if(encodeType!=null){
                if(EncodeMissionLogicInterface.ENCODE_TYPE_ENCODE==encodeType){
                    fromSource = MediaLogicInterface.MEDIA_FROM_SOURCE_ENCODE;
                }else if(EncodeMissionLogicInterface.ENCODE_TYPE_RECORD==encodeType){
                    fromSource = MediaLogicInterface.MEDIA_FROM_SOURCE_RECORD;
                }else{
                    fromSource = MediaLogicInterface.MEDIA_FROM_SOURCE_RECORD;
                }

            }
        }
        if(params.get("mediaName")!=null&&!"".equals(params.get("mediaName").trim())){
            mediaName = params.get("mediaName");
        }
        logger.debug("ý�������趨��"+mediaName);
        mediaId = StringUtils.string2int(params.get("mediaId"),-1);
        Media media=null;
        if(mediaId>0){
            try {
                media = mediaLogic.get(mediaId);
                //����ϴα����ʱ�򱣴������֣������ϴ�����Ϊ��
                if(media!=null&&media.getName()!=null){
                    mediaName = media.getName();
                }
            } catch (Exception e) {
                logger.error("û���ҵ�ý�壺"+mediaId);
            }
        }else{
        }
        if(media==null){
            media = new Media();
        }
        media.setBitRate(1.0f*StringUtils.string2int(params.get("bitrate"),0));
        media.setChannelId(channelId);
        media.setClipHeight(StringUtils.string2int(params.get("clipHeight"),0));
        media.setClipWidth(StringUtils.string2int(params.get("clipWidth"),0));
        media.setCodec(params.get("codec"));
        media.setCreateTime(new Date());
        media.setDescription(description);
        logger.debug("ӰƬ������"+description);
        int deviceId = StringUtils.string2int(params.get("deviceId"),-1);
        Device server;
        if(deviceId>0){
            server = deviceLogic.get(deviceId);
        }else{
            server = deviceLogic.getMediaServer();
            if(server!=null){
                deviceId = server.getDeviceId();
            }
        }
        media.setDeviceId(deviceId);
        String url = params.get("url");
        if(server!=null&&url!=null){//url�а�����ftp��·������Ҫ����һ��
            String ftpPath = server.getFtpPath();
            if(ftpPath!=null&&!"".equals(ftpPath)){
                int ftpPathPos = url.indexOf(ftpPath);
                if(ftpPathPos>=0){
                    url = url.substring(ftpPathPos+ftpPath.length());
                }
            }
        }
        media.setUrl(url);

        media.setFromSource(fromSource);
        BeanUtils.setDefaultValue(media,"ftpFlag",MediaLogicInterface.MEDIA_FTP_FLAG_READY);
        BeanUtils.setDefaultValue(media,"pubFlag",MediaLogicInterface.MEDIA_PUB_FLAG_ONLINE);
        BeanUtils.setDefaultValue(media,"status",MediaLogicInterface.MEDIA_STATUS_OK);
        BeanUtils.setDefaultValue(media,"subjectId", 0);
        BeanUtils.setDefaultValue(media,"typeId",MediaLogicInterface.MEDIA_TYPE_VOD);
        BeanUtils.setDefaultValue(media,"hits", 0);
        media.setLength(StringUtils.string2long(params.get("duration") ,0)*1000L);
        media.setLevelId(mediaLevel);
        String fileName = params.get("fileName");
        if(fileName == null){
            fileName = url;
        }
        if(fileName!=null){
            fileName = fileName.replace("\\","/");
            fileName = FileUtils.extractFileName(fileName,"/");
            int posOfDot = fileName.indexOf(".");
            if(posOfDot>0){
                fileName = fileName.substring(0,posOfDot);
            }
            //�Զ����ɵ��ļ�������"-_-"�ָ����ں�ʱ��
            posOfDot = fileName.indexOf("-_-");
            if(posOfDot>0){
                fileName = fileName.substring(0,posOfDot);
            }
            mediaName = mediaName.replace("${fileName}",fileName);
        }
        if(mediaName==null){
            mediaName = defaultName;
        }
        mediaName = mediaName.replace("${channelName}",channelName);
        mediaName = mediaName.replace("${time}",StringUtils.date2string(new Date(),"HH:mm:ss"));
        mediaName = mediaName.replace("${date}",StringUtils.date2string(new Date(),"yyyy-MM-dd"));
        mediaName = mediaName.replace("${hour}",StringUtils.date2string(new Date(),"HH"));
        mediaName = mediaName.replace("${min}",StringUtils.date2string(new Date(),"mm"));
        mediaName = mediaName.replace("${sec}",StringUtils.date2string(new Date(),"ss"));
        media.setName(mediaName);
        media.setOfflineDate(offlineTime);
        if(onlineTime==null){
            onlineTime = new Date();
        }else{
            if(onlineTime.getTime()<System.currentTimeMillis()){
                onlineTime = new Date();
            }
        }
        media.setOnlineDate(onlineTime);
        media.setPoster1(mediaPost);
        //media = mediaLogic.save(media);
        return media;
    }
    public String getParameterIni(String target, Map<String,Map<String,String>> params) {
        StringBuffer result = new StringBuffer();
        if (params != null) {
            result.append("command=").append(target).append("\r\n");
            for (String key : params.keySet()) {
                result.append("##  ").append(key).append("\r\n");
                Map<String,String> parameters = params.get(key);
                for(String subKey:parameters.keySet()){
                    String value = parameters.get(subKey);
                    if (value == null) value = "";
                    if(!"".equals(key.trim())){
                        //result.append(key).append(".");
                    }
                    result.append(subKey.replace("-","_")).append("=")
                            .append(value).append("\r\n");
                }
            }
        }
        return result.toString();
    }

    public String getReturnXml(String header,String version,String xmlContent,Map<String,Map<String,String>> params){
        StringBuffer xmlStr = new StringBuffer();

        if(xmlContent==null){
            xmlContent = "";
        }
        if(params==null){
            params = new HashMap<String,Map<String,String>>();
        }
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        xmlStr.append("<").append(header).append(" version=\"").append(version).append("\">\r\n");
        xmlStr.append(xmlContent);
        for(String key:params.keySet()){
            if(!"".equals(key)){
                xmlStr.append("\t<").append(key).append(">\n");
            }
            Map<String,String> param = params.get(key);
            for(String subKey:param.keySet()){
                xmlStr.append("\t\t<").append(subKey).append(">");
                xmlStr.append(StringUtils.escapeXMLTags(param.get(subKey)));
                xmlStr.append("</").append(subKey).append(">\n");
            }
            if(!"".equals(key)){
                xmlStr.append("\t</").append(key).append(">\r\n");
            }
        }
        xmlStr.append("</").append(header).append(">\r\n");
        return xmlStr.toString();
    }
    public String getAttribute(Element root,String nodeName,String attributeName){
        Node node = root.selectSingleNode(nodeName);
        if(node!=null){
            return node.valueOf("@"+attributeName);
        }
        return null;
    }
    public String getReqType(Element root){
        Node task = root.selectSingleNode("task");
        if(task == null){
            task = root.selectSingleNode("event");
        }
        if(task!=null){
            return task.valueOf("@type");
        }
        return null;
    }
    ///*
    public String getTaskTarget(Element root){
        return getAttribute(root,"task","target");
    }
    //*/
    public Map<String,String> getParameters(Node root){
        Map<String,String> result = new HashMap<String,String>();
        Node task = root.selectSingleNode("task");
        if(task == null){
            task = root.selectSingleNode("event");
        }
        List params = task.selectNodes("param");
        if(params!=null){
            for(Object obj:params){
                Node param = (Node) obj;
                if(param!=null){
                    String name = param.valueOf("@n");
                    String value= param.valueOf("@v");
                    if(name!=null){
                        result.put(name,value);
                    }
                }
            }
        }
        return result;
    }
    public void createDefaultPoster(){
        Thread task = new Thread(){
            public void run(){
                Date date = new Date(System.currentTimeMillis()-24*60*60*1000L);
                String url= AppConfigurator.getInstance().getConfig("system.tools.snap.serverUrl","http://192.168.5.8")+"/interface/createIndexThumb.jsp";//?startTime="+StringUtils.date2string(date).replace(" ","%20");
                ServerMessager messager = new ServerMessager();
                logger.debug("��������ͼ���ý����"+messager.postToHost(url,"startTime="+StringUtils.date2string(date)));
            }
        };
        task.start();
    }
%>
