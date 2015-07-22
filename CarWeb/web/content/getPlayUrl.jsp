<%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="com.fortune.util.net.URLEncoder" %><%@ page
        import="com.fortune.rms.business.system.logic.logicImpl.MediaServerMonitor" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@
        page contentType="text/html;charset=UTF-8" language="java" %><%
    final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
    final ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    String userAgent = request.getHeader("user-agent");
    boolean isIOS =request.getParameter("m3u8")!=null||(userAgent!=null&&userAgent.indexOf("iPhone")>0)||(userAgent!=null&&userAgent.indexOf("iPad")>0);
    Logger logger = Logger.getLogger("com.fortune.jsp.content.manage.getPlayUrl.jsp");
    if(isIOS){
        response.setContentType("application/x-mpegURL");
    }
    List<Map<String,String>> urls = new ArrayList<Map<String,String>>();
   // String mediaUrl = request.getParameter("pid");
    String mediaUrl = (String)session.getAttribute("pid");
    long deviceId = StringUtils.string2long(request.getParameter("uid"),-1);
    if(deviceId<=1){
        //这个deviceId是假的，需要查找contentId中的设备ID
        long contentId = StringUtils.string2long((String)session.getAttribute("contentId"),-1L);
        if(contentId>0){
            try {
                Content content = contentLogicInterface.get(contentId);
                deviceId = content.getDeviceId();
            } catch (Exception e) {

            }
        }
    }
    Device contentDevice =(Device)CacheUtils.get(deviceId,"device",new DataInitWorker(){
        public Object init(Object keyId,String cacheName){
            try {
                return deviceLogicInterface.get((Long)keyId);
            } catch (Exception e) {
                return null;
            }
        }
    });
    String serverUrl = contentDevice.getUrl();
    int i=0;
    while(serverUrl.endsWith("/")){
        serverUrl = serverUrl.substring(0,serverUrl.length()-1);
        i++;
        if(i>10){
            break;
        }
    }
    while(mediaUrl.startsWith("/")&&mediaUrl.length()>2){
        mediaUrl=mediaUrl.substring(1);
    }
    mediaUrl =  mediaUrl.replaceAll("//","/");
    String fullUrl =serverUrl+"/"+mediaUrl;
    {
        Map<String,String> aUrl = new HashMap<String,String>();
        aUrl.put("url", MediaServerMonitor.getInstance().getHttpGslbUrl(fullUrl,request.getRemoteAddr()));
        aUrl.put("name","测试");
        aUrl.put("bandwidth",""+1024*1024L);
        logger.debug("url="+aUrl.get("url"));
        urls.add(aUrl);
    }
    if(isIOS){
       logger.debug("输出M3U8");
       StringBuilder builder = new StringBuilder();
       builder.append("#EXTM3U\n");
       for(Map<String,String> aUrl :urls){
           builder.append("#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=").append(aUrl.get("bandwidth")).append("\n");
           //builder.append(URLEncoder.encode(aUrl.get("url"),"UTF-8") + "\n");
           builder.append(aUrl.get("url")).append("\n");
       }
       logger.debug("获取连接过程结束:\n"+builder.toString());
       out.println(builder.toString());
       return;
    }else{
    }
    int isLive = fullUrl.indexOf("/live/")>0?1:0;
    String contentName = "测试";
    String hosts = "";
    StringBuilder xmlBuilder = new StringBuilder();
    xmlBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n").append("<data>\n")
            .append("<video title=\"").append(contentName).append("\" live=\"").append(isLive).append("\">\n");

    if(urls.size()>3){
        logger.warn("太多的内容!");
    }
    for(Map<String,String> aUrl :urls){
        String name = aUrl.get("name");
        i++;
        if("超清".equals(name)){
            continue;
        }
        logger.debug("当前name="+name);
        String url = aUrl.get("url");
        String playUrl = getClipUrl(url);//+"?contentId=35605875&contentPropertyId=46691400&channelId=-1&&spId=-1&cpId=15905690&userId=13817722586&userIp=0:0:0:0:0:0:0:1&isFree=0&areaId=-1&timestamp=20130620122214&encrypt=3b8b225549b5564fb2b2a2db91867314";
        if("".equals(hosts)){
            hosts = getHostUrl(url);
        }
        logger.debug("播放链接："+url+"->"+playUrl);
        playUrl = URLEncoder.encode(playUrl,"UTF-8");
        playUrl = regUrl(playUrl,request.getRemoteAddr(),null);
        xmlBuilder.append("<path rate=\"").append(name).append("\">")
                .append(playUrl).append("</path>\n");
    }
    xmlBuilder.append("<hosts>").append(hosts).append("</hosts>\n").append(" </video>\n</data>");
    logger.debug(xmlBuilder.toString());
    out.print(xmlBuilder.toString());
%><%!
    //Logger logger = Logger.getLogger("com.fortune.jsp.getPlayUrl");
    public String getBanwidthName(String propertyName){
        return propertyName.split(":")[0];
    }

    public String getBanwidthNameFromCode(String propertyName,String propertyCode){
        String result = "流畅";
        if("Media_Url_1M".equals(propertyCode)){
            result ="高清";
        }else if("Media_Url_2M".equals(propertyCode)){
            result ="超清";
        }else if("Media_Url_4M".equals(propertyCode)){
            result ="超清";
        }else if("Media_Url_Source".equals(propertyCode)){
            result ="超清";
        }else if("Media_Url_384k".equals(propertyCode)){
            result ="流畅";
        }else if("Media_Url_512k".equals(propertyCode)){
            result ="标清";
        }else if("Media_Url_768k".equals(propertyCode)){
            result ="高清";
        }else if("FLASH_2M_URL".equals(propertyCode)){
            result ="超清";
        }else if("FLASH_1M_URL".equals(propertyCode)){
            result ="超清";
        }else if("FLASH_512K_URL".equals(propertyCode)){
            result ="标清";
        }else{
            result =getBanwidthName(propertyName);
        }
        return result;
    }

    public String getHostUrl(String url){
        if(url!=null){
            int p = url.indexOf("/vod/");
            if(p>0){
                //return url.substring(0,p+5);
            }
            String clearUrl = StringUtils.getClearURL(url);
            return url.substring(0,url.indexOf(clearUrl));
        }else{
            return null;
        }
    }
    public String getClipUrl(String url){
        if(url!=null){
            int p = url.indexOf("/vod/");
            if(p>0){
                //return url.substring(p+5);
            }
            url = StringUtils.getClearURL(url);
            return url;
        }else{
            return null;
        }
    }
    public float getAndroidVersion(String userAgent){
        String androidStr = "Android";
        boolean isAndroid = userAgent.contains(androidStr);
        //如果是安卓版本4.0以上，还是使用m3u8方式
        float androidVersion =2.3f;
        if(isAndroid){
            //检查安卓版本
            int p = userAgent.indexOf(androidStr);
            if(p>0){
                String verStr = userAgent.substring(p+androidStr.length());
                p = verStr.indexOf(";");
                if(p>0){
                    verStr = verStr.substring(0,p);
                }
                p = verStr.lastIndexOf(".");
                if(p>0){
                    verStr = verStr.substring(0,p);
                }
                try {
                    androidVersion = Float.parseFloat(verStr.trim());
                } catch (NumberFormatException e) {
                    System.err.println("无法正确转换安卓版本信息："+verStr+",userAgent="+userAgent);
                }
                if(androidVersion>=4.0f){
                    isAndroid = false;
                }
            }
        }
        return androidVersion;
    }
%><%@include file="../user/urlUtils.jsp"%>