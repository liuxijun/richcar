<%@ page import="java.io.InputStream" %><%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.log.model.VisitLog" %><%@ page
        import="com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface" %><%@ page
        import="java.text.ParseException" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="java.util.*" %><%@ page
        import="java.io.UnsupportedEncodingException" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.PhoneRange" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="java.net.URLDecoder" %><%@ page
        import="com.fortune.rms.business.ad.logic.logicInterface.AdLogLogicInterface" %><%--
  Created by IntelliJ IDEA.
  User: Alen
  Date: 11-8-5
  Time: 下午5:27
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%

    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();
    if(dataLength<=0){
        dataLength = 1024*1024;
        logger.warn("dataLength is zero!set it to default size:"+dataLength);
    }else{
        logger.debug("dataLength="+dataLength);
    }
    sIp = request.getRemoteAddr();
    int resultCode=0;
    if(dataLength>0){
        byte[] dataBuffer = new byte[dataLength];
        int readedLength = is.read(dataBuffer);
        String readResult = "";
        int readed = 0;
        try {
            while(readedLength>0){
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
        VisitLogLogicInterface visitLogLogicInterface =(VisitLogLogicInterface) SpringUtils.getBean("visitLogLogicInterface",session.getServletContext());
        ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
        logger.debug("客户端传来数据长度：" + readed);
        if("".equals(readResult)){
            resultCode = 500;
            logger.error("输入数据为空！");
        } else if(readResult.contains("<?xml")){
            //logger.debug("客户端传来数据长度：" + readResult.length() + ",readResult=\n" + readResult);
            Element root = XmlUtils.getRootFromXmlStr(readResult);
            String command = getRepType(root);
            Map<String,String> params = getParameters(root);
            params.put("serverIp", request.getRemoteAddr());
            if("disconnect".equals(command)){
                saveMediaLog(visitLogLogicInterface,params,contentLogicInterface,session);
            }else{
                //logger.debug("不用处理的命令参数："+command);
            }
        }else{
            try {
                boolean apacheFormat = "apache".equals(request.getParameter("logFormat"));
                List<VisitLog> visitLogs;
                if(apacheFormat){
                    visitLogs = apacheAccessLogProcess(readResult);
                }else{
                    visitLogs = dateProcess(readResult,session);
                }
                if(visitLogs.size()!=0){
                    saveVisitLogs(visitLogLogicInterface,visitLogs);
                }else{
                    logger.warn("没有日志可以保存！");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }else{

        resultCode = 404;
        logger.error("没有数据输入");
    }
    //logger.debug("处理完毕，resultCode="+resultCode);

%>resultCode=<%=resultCode%><%!
    private String sIp;
    private Logger logger = Logger.getLogger("com.fortune.jsp.saveLog.jsp") ;
    public List<String> splitApacheLog(String log){
        List<String> result = new ArrayList<String>();
        int i=0,l=log.length();
        String a = null;
        while(i<l){
            char ch = log.charAt(i);
            if(ch==' '){
                if(null!=a){
                    result.add(a);
                    a=null;
                }
            }
            while(ch==' '&&i<l-1){
                i++;
                ch = log.charAt(i);
            }
            if(i>=l){
                break;
            }
            if(ch=='"'){
                i ++;
                a="";
                if(i>=l){
                    break;
                }
                ch = log.charAt(i);
                while(ch!='"'){
                    a+=ch;
                    i++;
                    if(i>=l){
                        break;
                    }
                    ch=log.charAt(i);
                }
                result.add(a);
                a=null;
            }else{
                if(a==null){
                    a = "";
                }
                a+=ch;
            }
            i++;
        }
        return result;
    }
    public List<VisitLog> apacheAccessLogProcess(String readResult){
        List<VisitLog> visitLogs = new ArrayList<VisitLog>();
        Map<String,VisitLog> logs=new HashMap<String,VisitLog>();
        if(readResult!=null){
            String visitLogsArray[] = readResult.split("\n");
            if(visitLogsArray.length!=0){
                String userIp;
                String date;
                String time;
                String duration="10";
                String status;
                String playVersion;
                String userAgent;
                String avgBandWith;
                String url;
                //String sIp="";
                String userId;
                for (String aVisitLogsArray : visitLogsArray) {

                    List<String> visitLogArray = splitApacheLog(aVisitLogsArray);
                    url = visitLogArray.get(6);
                    if(url.startsWith("GET ")){
                        url = url.substring(4);
                    }
                    int p=url.indexOf(" HTTP/");
                    if(p>0){
                        url = url.substring(0,p).trim();
                    }
                    String token = StringUtils.getParameter(url,"encrypt");
                    VisitLog visitLog=null;
                    date = visitLogArray.get(2);
                    time = visitLogArray.get(3);
                    userIp = visitLogArray.get(0);
                    Date now = StringUtils.string2date(date+" "+time);
                    if(token!=null&&!"".equals(token)){
                        visitLog = logs.get(token);
                    }else{
                        String m3u8FileName = url;
                        p = m3u8FileName.indexOf("?");
                        if(p>0){
                            m3u8FileName = m3u8FileName.substring(0,p);
                        }
                        String extName = FileUtils.getFileExtName(m3u8FileName);
                        if("ts".equals(extName)){
                            m3u8FileName = FileUtils.extractFilePath(m3u8FileName,"/");
                            p = m3u8FileName.lastIndexOf("_");
                            if(p>0){
                                m3u8FileName = m3u8FileName.substring(0,p)+"."+m3u8FileName.substring(p+1)+".M3U8";
                            }
                        }
                        token = userIp+m3u8FileName.toLowerCase();
                        visitLog = logs.get(token);
                        if(visitLog!=null){
                            Date endTime = visitLog.getEndTime();
                            if(endTime !=null){
                                //如果没有token，就检查上一个和当前这条的时间差异，如果超过1分钟就认为是新的一条记录
                                if(now.getTime()-endTime.getTime()>60*1000){
                                    visitLog.setEndTime(new Date(endTime.getTime()+10));
                                    visitLog = null;
                                    logs.remove(token);
                                }
                            }
                        }
                    }
                    status = visitLogArray.get(4);
                    playVersion = visitLogArray.get(7);
                    userAgent = visitLogArray.get(8);
                    if(visitLog==null){
                        visitLog = new VisitLog();
                        avgBandWith = "0";
                        userId = getValueByUrlKey(url,"userId");
                        visitLog.setSpId(checkType(getValueByUrlKey(url, "spId")));
                        if(userAgent==null||"-".equals(userAgent)){
                            userAgent = StringUtils.getParameter(url,"userAgent");
                        }
                        visitLog.setStartTime(StringUtils.string2date(date + " " + time));
                        visitLog.setCpId(checkType(getValueByUrlKey(url, "cpId")));
                        visitLog.setChannelId(checkType(getValueByUrlKey(url, "channelId")));
                        visitLog.setContentId(checkType(getValueByUrlKey(url, "contentId")));
                        visitLog.setAreaId(checkType(getValueByUrlKey(url,"areaId")));
                        visitLog.setLength(checkType(getValueByUrlKey(url,"length")));
                        visitLog.setAvgBandwidth(checkType(getValueByUrlKey(url,"avgBandwidth")));
                        visitLog.setContentPropertyId(checkType(getValueByUrlKey(url, "contentPropertyId")));
                        visitLog.setUrl(url);
                        visitLog.setUserId(userId);
                        visitLog.setUserIp(userIp);
                        visitLog.setIsFree(checkType(getValueByUrlKey(url, "isFree")));
                        visitLog.setEndTime(StringUtils.string2date(timeCalculation(date + " " + time, duration, "add")));
                        visitLog.setLength(checkType(duration));
                        visitLog.setStatus(checkType(status));
                        visitLog.setsIp(sIp);
                        visitLog.setBytesSend(StringUtils.string2long(visitLogArray.get(5),0));
                        if(playVersion==null||"".equals(playVersion)||"-".equals(playVersion)){
                            playVersion = getValueByUrlKey(url,"remoteType");
                        }
                        if(playVersion!=null&&playVersion.length()>99){
                            playVersion = playVersion.substring(0,98);
                        }
                        visitLog.setPlayerVersion(playVersion);
                        visitLog.setUserAgent(userAgent);
                        visitLog.setAvgBandwidth(checkType(avgBandWith));
                        visitLog.setsIp(sIp);
                        visitLogs.add(visitLog);
                        logs.put(token,visitLog);
                    }else{
                        //结束时间要往后加10秒，当作下载的时间。这个日志里的是发生的时间
                        visitLog.setEndTime(new Date(StringUtils.string2date(date + " " + time).getTime()+10000));
                        visitLog.setLength((visitLog.getEndTime().getTime()-visitLog.getStartTime().getTime())/1000);
                        visitLog.setBytesSend(visitLog.getBytesSend()+StringUtils.string2long(visitLogArray.get(5),0));
                    }
                    //logger.debug("url="+visitLog.getUrl()+",startTime="+StringUtils.date2string(visitLog.getStartTime())+",endTime="+StringUtils.date2string(visitLog.getEndTime()));
                }

            }
        }
        return visitLogs;
    }
    public List<VisitLog> dateProcess(String readResult,HttpSession session) throws ParseException {
        List<VisitLog> visitLogs = null;
        if(readResult!=null){
           visitLogs = new ArrayList<VisitLog>();
           String visitLogsArray[] = readResult.split("\n");
           if(visitLogsArray.length!=0){
               String userIp;
               String date;
               String time;
               String duration;
               String status;
               String playVersion;
               String userAgent;
               String avgBandWith;
               String url;
//               String sIp;
               String userId;

               for (String aVisitLogsArray : visitLogsArray) {
                   VisitLog visitLog = new VisitLog();
                   String visitLogArray[] = aVisitLogsArray.split(" ");
                   userIp = visitLogArray[0];
                   date = visitLogArray[1];
                   time = visitLogArray[2];
                   duration = visitLogArray[6];
                   status = visitLogArray[8];
                   playVersion = visitLogArray[10];
                   userAgent = visitLogArray[12];
                   avgBandWith = visitLogArray[21];
                   if(visitLogArray.length>40){
                       sIp = visitLogArray[40];
                   }else{
                       sIp="";
                   }
                   if(visitLogArray.length>47){
                       url = visitLogArray[47];
                   }else{
                       url = visitLogArray[4];
                   }
                   userId = getValueByUrlKey(url,"userId");
                   visitLog.setSpId(checkType(getValueByUrlKey(url, "spId")));
                   if(userAgent==null||"-".equals(userAgent)){
                       userAgent = StringUtils.getParameter(url,"userAgent");
                   }
                   visitLog.setCpId(checkType(getValueByUrlKey(url, "cpId")));
                   visitLog.setChannelId(checkType(getValueByUrlKey(url, "channelId")));
                   visitLog.setContentId(checkType(getValueByUrlKey(url, "contentId")));
                   visitLog.setContentPropertyId(checkType(getValueByUrlKey(url, "contentPropertyId")));
                   visitLog.setUrl(url);
                   visitLog.setUserId(getValueByUrlKey(url, "user_id"));
                   visitLog.setUserIp(userIp);
                   boolean isPhoneNumRequestAreaId = false;
                   Long userPhone = StringUtils.string2long(userId,-1);
                   if(userId!=null&&!"null".equals(userId)&&userPhone>0){
                       PhoneRangeLogicInterface phoneRangeLogicInterface = (PhoneRangeLogicInterface) SpringUtils.getBean("phoneRangeLogicInterface",session.getServletContext());
                       PhoneRange pr = phoneRangeLogicInterface.getPhoneRangeOfPhone(userPhone);
                       if(pr!=null){
                           visitLog.setAreaId(pr.getAreaId());
                           isPhoneNumRequestAreaId = true;
                       }
                   }
                   if(!isPhoneNumRequestAreaId){
                       visitLog.setAreaId(checkType(getValueByUrlKey(url, "areaId")));
                   }
                   visitLog.setIsFree(checkType(getValueByUrlKey(url, "isFree")));
                   visitLog.setStartTime(StringUtils.string2date(date + " " + time));
                   visitLog.setEndTime(StringUtils.string2date(timeCalculation(date + " " + time, duration, "add")));
                   visitLog.setLength(checkType(duration));
                   visitLog.setStatus(checkType(status));
                   visitLog.setPlayerVersion(playVersion);
                   visitLog.setUserAgent(userAgent);
                   visitLog.setAvgBandwidth(checkType(avgBandWith));
                   visitLog.setsIp(sIp);
                   visitLogs.add(visitLog);
                   //logger.debug("url="+visitLog.getUrl()+",startTime="+StringUtils.date2string(visitLog.getStartTime())+",endTime="+StringUtils.date2string(visitLog.getEndTime()));
               }

           }
        }
        return visitLogs;
    }

    public void saveVisitLogs(VisitLogLogicInterface visitLogLogicInterface,List<VisitLog> visitLogs) throws Exception {
/*
        for(VisitLog log:visitLogs){
            visitLogLogicInterface.save(log);
        }
*/
        visitLogLogicInterface.JDBCSaveVisitLogs(visitLogs);
    }

    public String getValueByUrlKey(String url,String key){
        String value = StringUtils.getParameter(url, key);
        if(value == null){
            value = StringUtils.getParameter(url, key.toLowerCase());
            if(value==null){
                value = StringUtils.getParameter(url,key.replace("_",""));
            }
        }
        return value;
    }

    @SuppressWarnings("unused")
    public String getValueByUrlKeyV0(String url,String key){

        String value=null;
        int index = url.indexOf("?");
        if(index!=-1){
            String[] param = url.substring(url.indexOf("?")+1).split("&");
            for (String aParam : param) {
                if (aParam.split("=")[0].equals(key)) {
                    value = aParam.split("=")[1];
                }
            }
        }
        return value;

    }

    public  String timeCalculation(String time,String second,String operator) {
        String result;
        Date date= StringUtils.string2date(time);
        Long startTime=date.getTime();
        Long endTime = Long.parseLong(second)*1000;
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
    public String getAttribute(Element root,String nodeName,String attributeName){
        Node node = root.selectSingleNode(nodeName);
        if(node!=null){
            return node.valueOf("@"+attributeName);
        }
        return null;
    }

    public String getRepType(Element root){
        Node task = root.selectSingleNode("task");
        if(task == null){
            task = root.selectSingleNode("event");
        }
        if(task!=null){
            return task.valueOf("@type");
        }
        return null;
    }
/*
    public String getTaskTarget(Element root){
        return getAttribute(root,"task","target");
    }
*/
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

    @SuppressWarnings("unchecked")
    public void saveMediaLog(VisitLogLogicInterface visitLogLogicInterface,Map<String,String> params ,
                             ContentLogicInterface contentLogicInterface,HttpSession session){
        VisitLog visitLog = new VisitLog();
        String url = params.get("url");
        
        if(url==null){
            url = "";
        }else{
            try {
                url = java.net.URLDecoder.decode(url,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(url.contains("fortuneTestUrl")){
                return ;
            }
        }
        Date startTime = StringUtils.string2date(params.get("startTime"));
        Date endTime = StringUtils.string2date(params.get("endTime"));
        if(startTime == null || startTime.getTime()==0){
            return;
        }
        long length = (endTime.getTime() - startTime.getTime()) / 1000;
        if(length<=0){
            return;
        }
/*
        log.setUserIp(params.get("remoteIP"));
        log.setContentPropertyId(StringUtils.getLongParameter(url, "clipId", -1));
        log.setStartTime(startTime);
        log.setEndTime(endTime);
        log.setId(-1L);
        log.setUrl(url);
        log.setContentId(StringUtils.getLongParameter(url, "media_id", -1));
        log.setChannelId(StringUtils.getLongParameter(url, "channel_id", -1));
        //log.setOrgId(StringUtils.getLongParameter(url,"orgId",-1));
        //log.set(StringUtils.getLongParameter(url,"mediaId",-1));
        //log.setStbId(StringUtils.getLongParameter(url,"stbId",-1));
        //log.setStbSn(StringUtils.getParameter(url,"stbSn"));
        log.setUserId(StringUtils.getParameter(url, "user_id"));
*/
        //mms://stream-media.bbn.com.cn/bbn6/byhoo/by11d02//mlysyynt_1000.wmv?cid=221&mid=1800030255&icp=180&svr=-1&imp=-1&sid=-1&uid=GHMS_HB_03_Guest&rip=61.50.249.105&fee=0
        long spId = StringUtils.string2long(getValueByUrlKey(url,"spId"),-1);
        long cpId = StringUtils.string2long(getValueByUrlKey(url, "cpId"), -1);
        long contentId = StringUtils.string2long(getValueByUrlKey(url,"contentId"),-1);
        //从url里拿到手机号
        String userId = getValueByUrlKey(url,"userId");
        if(spId<=0){
            spId = cpId;
        }
        visitLog.setSpId(spId);
        visitLog.setCpId(cpId);
        long channelId = StringUtils.string2long(getValueByUrlKey(url, "channelId"), -1);
        if(channelId<0){
            if(channelId<=0){
                try {
                    Channel channel=contentLogicInterface.getContentBindChannel(-1L,contentId);
                    if(channel!=null){
                        channelId = channel.getId();
                    }
                } catch (Exception e) {
                   // logger.error("无法获取频道："+e.getMessage());
                }
            }
        }
        if("true".equals(StringUtils.getParameter(url,"isAd"))||"true".equals(StringUtils.getParameter(url,"isad"))){
            AdLogLogicInterface adLogLogicInterface = (AdLogLogicInterface) SpringUtils.getBean("adLogLogicInterface",session.getServletContext());
            adLogLogicInterface.createAdLog(StringUtils.string2long(getValueByUrlKey(url,"adid"),-1L),
                    StringUtils.string2long(getValueByUrlKey(url,"adRangeId"),-1L),
                    contentId,channelId,spId);
            return;
        }

        visitLog.setChannelId(channelId);
        visitLog.setContentId(contentId);
        visitLog.setContentPropertyId(checkType(getValueByUrlKey(url, "contentPropertyId")));
        visitLog.setUrl(url);
        visitLog.setUserId(getValueByUrlKey(url, "userId"));
        String ip = params.get("remoteIP");
        if(null==ip||"".equals(ip)||"127.0.0.1".equals(ip)||ip.startsWith("192.168.")||ip.startsWith("61.55.145.")){
            ip = getValueByUrlKey(url,"userIp");
        }
        visitLog.setUserIp(ip);
        boolean isPhoneNumRequestAreaId = false;
        //用手机号进行所属区域的判断。
        if(userId!=null&&!"null".equals(userId)){
            final PhoneRangeLogicInterface phoneRangeLogicInterface = (PhoneRangeLogicInterface) SpringUtils.getBean("phoneRangeLogicInterface",session.getServletContext());
            List<PhoneRange> list = (List<PhoneRange>) CacheUtils.get("allPhoneRange","allPhoneRange",new DataInitWorker(){
                public Object init(Object key,String cacheName){
                    PhoneRange bean =new PhoneRange();
                    PageBean pageBena = new PageBean(0,Integer.MAX_VALUE,"o1.phoneTo","asc");
                    List<PhoneRange> result;
                    try {
                        result = phoneRangeLogicInterface.search(bean,pageBena);
                    } catch (Exception e) {
                        result = phoneRangeLogicInterface.getAll();
                    }
                    return  result;
                }
            });

            for(PhoneRange phoneRange:list){
                if(Long.parseLong(userId)>phoneRange.getPhoneFrom()&&Long.parseLong(userId)<phoneRange.getPhoneTo()){
                    visitLog.setAreaId(phoneRange.getAreaId());
                    isPhoneNumRequestAreaId = true;
                    break;
                }
            }
        }
        if(!isPhoneNumRequestAreaId){
            visitLog.setAreaId(checkType(getValueByUrlKey(url, "areaId")));
        }
//        visitLog.setAreaId(checkType(getValueByUrlKey(url, "areaId")));
        visitLog.setIsFree(checkType(getValueByUrlKey(url, "fee")));
        visitLog.setStartTime(startTime);
        visitLog.setEndTime(endTime);
        visitLog.setLength(length);
        visitLog.setStatus(checkType(params.get("statecode")));
        String remoteType = params.get("remoteType");
        if(remoteType==null){
            remoteType=getValueByUrlKey(url, "remoteType");
        }
        if(remoteType!=null){
            try {
                remoteType = URLDecoder.decode(remoteType,"UTF-8");
                remoteType = remoteType.replace("PERCENT","%");
                remoteType = remoteType.replace("DIV","/");
                remoteType = remoteType.replace("LEFT","(");
                remoteType = remoteType.replace("RIGHT",")");
                remoteType = remoteType.replace("SPACE"," ");
            } catch (UnsupportedEncodingException e) {

            }
        }
        visitLog.setPlayerVersion(remoteType);
        visitLog.setUserAgent(remoteType);
        long bytesSend = StringUtils.string2long(params.get("bytes_send"),0);
        if(bytesSend>=0&&length>0){
           visitLog.setAvgBandwidth(bytesSend*8/ length);
        }
        visitLog.setBytesSend(bytesSend);
        visitLog.setsIp(params.get("serverIp"));
        //logger.debug("保存日志："+visitLog.toString());
        visitLogLogicInterface.save(visitLog);
    }

%>
