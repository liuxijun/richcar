<%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.user.CacheLogic" %><%@ page
        import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %><%@ page
        import="com.fortune.rms.business.encoder.model.EncoderTemplate" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="com.fortune.util.net.URLEncoder" %><%@ page
        import="com.fortune.rms.business.system.logic.logicImpl.MediaServerMonitor" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@include
        file="queryString.jsp"%><%@
        page contentType="text/html;charset=UTF-8" language="java" %><%
    String token = request.getParameter("token");
    String phone = request.getParameter("phone");
    if(token!=null&&phone!=null){
        if(verifyToken(token,phone)){
            session.setAttribute(Constants.USER_PHONE_NUMBER,phone);
        }
    }
    final ContentPropertyLogicInterface contentPropertyLogic = (ContentPropertyLogicInterface) SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    PropertyLogicInterface propertyLogic = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
    boolean jsonFormat = "true".equals(request.getParameter("jsonFormat"));
    boolean singleM3U8Only = "true".equals(request.getParameter("singleM3U8Only"));
    contentPropertyId= StringUtils.string2long(request.getParameter("pid"),-1);
    String userAgent = request.getHeader("user-agent");
    String clientType = request.getParameter("clientType");
    if(clientType==null){
        clientType = "";
    }
    if(userAgent==null){
        userAgent = "";
    }
    boolean isIOS =(userAgent!=null&&userAgent.indexOf("iPhone")>0)||(userAgent!=null&&userAgent.indexOf("iPad")>0);
    String useM3U8=request.getParameter("m3u8");

    boolean m3u8Format = isIOS||useM3U8!=null||"m3u8".equals(clientType);
//    logger.debug("iOS:--"+isIOS);
//    logger.debug("m3u8Format="+m3u8Format+",isIOS="+isIOS+",useM3U8="+useM3U8+",clientType="+clientType);
    boolean isAndroid = userAgent.contains("Android");
    if(!isAndroid){
        //isAndroid = true;
    }
//    Logger logger = Logger.getLogger("com.fortune.jsp.getPlayUrl");
    String userIp = request.getRemoteAddr();
    if(jsonFormat){
        singleM3U8Only=true;
        contentId = StringUtils.string2long(request.getParameter("contentId"),-1);
        int clipId = StringUtils.string2int(request.getParameter("clipId"),1);
        String bandwidth = request.getParameter("bandwidth");
        if(bandwidth==null){
            bandwidth = "Media_Url_Source";
        }
        contentPropertyId = guessContentPropertyId(contentId,
                clipId,bandwidth,
                contentPropertyLogic,propertyLogic);
        if(contentPropertyId<=0){
            String result = (getJsonResult(false,null,"无法获取到媒体连接信息，可能该集正在转码，请选择其他集或者带宽！"));
            urlLogger.error(result);
            urlLogger.error("目前无法获取到contentPropertyId！contentId="+contentId+",clipId="+clipId+",bandwidth="+bandwidth);
            out.println(result);
            return;
        }
        urlLogger.debug("获取到的contentPropertyId是"+contentPropertyId);
    }else{
        urlLogger.debug("这不是jsonFormat请求");
    }
    if(m3u8Format&&!jsonFormat){
        response.setContentType("application/x-mpegURL");
    }
    String userId = (String) session.getAttribute(Constants.USER_PHONE_NUMBER);
    if(userId==null){
        userId = (String)session.getAttribute("userId");
    }

    String isFree = "0";
    if ("true".equals( session.getAttribute("play_free_"+contentId+"_"+contentPropertyId ) )){
        isFree = "1";
    } else if ("true".equals( session.getAttribute("play_"+contentId+"_"+contentPropertyId ) )){

    } else {
        //logger.warn("用户没有登录，为了测试，允许其播放！");
        //out.println("认证无效");
        //return;
    }
    CacheLogic cacheLogic= new CacheLogic(request);
    long areaId = cacheLogic.getAreaId(userIp);
    List<Map<String,String>> urls = new ArrayList<Map<String,String>>();
    String contentName = "";
    final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
    EncoderTemplateLogicInterface  templateInterface = (EncoderTemplateLogicInterface) SpringUtils.getBean("encoderTemplateLogicInterface",session.getServletContext());
    //如果singleM3U8Only是真，则不输出多码流m3u8文件
    urlLogger.debug("pid="+contentPropertyId+",singleM3U8Only="+singleM3U8Only);
    int isLive = 0;
    if(contentPropertyId>0){
        ContentProperty cp = (ContentProperty) CacheUtils.get(contentPropertyId,"contentPropertyCache",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                try {
                    long id = (Long) key;
                    return contentPropertyLogic.get(id);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        Content content = contentLogic.getCachedContent(cp.getContentId());
        if(contentId<=0){
            contentId = content.getId();
        }
        ContentProperty searchBean = new ContentProperty();
        searchBean.setContentId(cp.getContentId());
        searchBean.setIntValue(cp.getIntValue());
        Long deviceId = content.getDeviceId();
        Device contentDevice =(Device)CacheUtils.get(deviceId,"device",new DataInitWorker(){
            public Object init(Object keyId,String cacheName){
                return deviceLogicInterface.get((Long)keyId);
            }
        });

        String hostUrl =contentDevice.getUrl();
        List<ContentProperty> clips = new ArrayList<ContentProperty>(0);
        if(singleM3U8Only){
            urlLogger.debug("只需要这一个："+cp.getName()+","+cp.getStringValue());
            clips.add(cp);
        }else{
            clips.addAll(contentPropertyLogic.getContentPropertiesByCache(searchBean));
            urlLogger.debug("一共加了："+clips.size()+"个，intValue="+cp.getIntValue());
        }
        //List<ContentProperty> clips = contentPropertyLogic.search(searchBean);
        for(ContentProperty clip:clips){
            if(contentId<=0){
                contentId = clip.getContentId();
            }
            //Property property = propertyLogic.get(clip.getPropertyId());
            Property property = propertyLogic.getPropertyByCache(clip.getPropertyId());
            Byte dataType = property.getDataType();
            if(PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType)||
                    PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType)){
                Map<String,String> url = new HashMap<String,String>();
                String urlValue = clip.getStringValue();
                if(urlValue==null||"".equals(urlValue.trim())){
                    //logger.warn("居然是空的数据进来了："+clip);
                    continue;
                }
                urlValue = urlValue.replace("//","/");
                while(urlValue.startsWith("/")){
                    urlValue = urlValue.substring(1);
                }
                while(hostUrl.endsWith("/")){
                    hostUrl = hostUrl.substring(0,hostUrl.length()-1);
                }

                urlValue = hostUrl +"/"+ urlValue;
                //如果是rtsp和http格式，vod字样要去掉
                if("rtsp".equals(clientType)||"http".equals(clientType)){
                    urlValue = urlValue.replace("/vod/","/");
                }
                if(urlValue.contains("/live/")){
                   //如果是直播，如果是用flash播放器的话，要把其中hls字眼去掉
                    if(!m3u8Format){
                        urlValue = urlValue.replace("/live/hls","/live");
                        //把频道编号加1000
                        //先获取频道编号
                        int p = urlValue.lastIndexOf("/");
                        if(p>0){
                            int tubeNumber = StringUtils.string2int(urlValue.substring(p+1),-1);
                            if(tubeNumber>0){
                                tubeNumber +=1000;
                                urlValue = urlValue.substring(0,p)+"/"+tubeNumber;
                            }
                        }
                    }else{
                        if(userAgent.indexOf("iPad")>0){
                            //如果是iPAD，用高清流
                            int p = urlValue.lastIndexOf("/");
                            if(p>0){
                                int tubeNumber = StringUtils.string2int(urlValue.substring(p+1),-1);
                                if(tubeNumber>0){
                                    tubeNumber +=100;
                                    urlValue = urlValue.substring(0,p)+"/"+tubeNumber;
                                }
                            }
                        }
                    }
                    isLive = 1;
                }
                //urlValue=URLEncoder.encode(urlValue.trim(),"UTF-8");
                if(m3u8Format){
                    urlValue +=".m3u8";
                }
                if(channelId<=0){
                    try {
                        Channel channel=contentLogic.getContentBindChannel(-1L,contentId);
                        if(channel!=null){
                            channelId = channel.getId();
                        }else{
                            urlLogger.warn("无法获取媒体绑定的频道：contentId="+contentId);
                        }
                    } catch (Exception e) {
                        urlLogger.error("无法获取频道："+e.getMessage());
                    }
                }
                if(channelId<=0){
                    urlLogger.warn("无法获取频道ID,queryString="+request.getQueryString());
                }
                if(spId<=0){
                    spId = cpId;
                }
                queryStr = "contentId="+content.getId()+"&contentPropertyId="+contentPropertyId+"&channelId="+channelId+"&"+"&spId="+spId+"&cpId="+content.getCspId();
                urlValue+="?" + queryStr + "&userId="+userId + "&userIp="+userIp + "&isFree="+isFree + "&areaId="+areaId
                        +"&remoteType="+encodeAgent(userAgent)
                ;
                String tokenPwd = getTokenPwd(urlValue,"fortune2009");

                //调度，授权
                try {
                    urlLogger.debug("调度请求："+urlValue);
                    String gslbUrl = MediaServerMonitor.getInstance().getHttpGslbUrl(urlValue,userIp);
                    urlLogger.debug("调度结果："+gslbUrl);
                    if(gslbUrl.contains("hls.inhe.net")){
                        gslbUrl = gslbUrl.replaceAll("hls.inhe.net",getRandomOf(new String[]{"61.55.145.162",
                                "61.55.145.163",
                                "61.55.145.164",
                                "61.55.145.165",
                                "61.55.145.183",
                                "61.55.145.184",
                                "61.55.145.185",
                                "61.55.145.186"}));
                    }
                    if("rtsp".equals(clientType)){
                        gslbUrl = gslbUrl.replace("http://","rtsp://");
                    }else if("http".equals(clientType)){
                        gslbUrl = "http://"+TcpUtils.getHostFromUrl(gslbUrl)+":8080"+StringUtils.getClearURL(gslbUrl);
                    }
                    urlValue = regUrl(gslbUrl,userIp,tokenPwd);
                    //result = true;
                } catch (Exception e) {
                    urlLogger.error("调度过程中发生异常："+e.getMessage());
                    urlValue = regUrl(urlValue,userIp,tokenPwd);
                }
                boolean putToMap=true;
                if(putToMap){
                    url.put("url",urlValue);
                    url.put("name",getBanwidthNameFromCode(property.getName(),property.getCode()));
                    url.put("contentPropertyId",""+clip.getId());
                    url.put("clipNo",""+clip.getIntValue());
                    long propertyId = property.getId();
                    EncoderTemplate template = templateInterface.getEncoderTemplateOfProperty(propertyId);
                    long bandwidth;
                    if(template!=null){
                        bandwidth = ((template.getVBitrate()+template.getABitrate())*1024);
                    }else {
                        String propertyCode = property.getCode();
                        if("Media_Url_1M".equals(propertyCode)){
                            bandwidth=1024*1024;
                        }else if("Media_Url_2M".equals(propertyCode)){
                            bandwidth=2048*1024;
                        }else if("Media_Url_4M".equals(propertyCode)){
                            bandwidth=4096*1024;
                        }else if("Media_Url_Source".equals(propertyCode)){
                            bandwidth=1200*1024;
                        }else if("Media_Url_384k".equals(propertyCode)){
                            bandwidth=384*1024;
                        }else if("Media_Url_512k".equals(propertyCode)){
                            bandwidth=512*1024;
                        }else if("Media_Url_768k".equals(propertyCode)){
                            bandwidth=768*1024;
                        }else if("FLASH_2M_URL".equals(propertyCode)){
                            bandwidth=2000*1024;
                        }else if("FLASH_1M_URL".equals(propertyCode)){
                            bandwidth=1024*1024;
                        }else if("FLASH_512K_URL".equals(propertyCode)){
                            bandwidth=512*1024;
                        }else{
                            bandwidth=368*1024;
                        }

                    }
                    if(bandwidth<=0){
                        bandwidth=360*1024;
                    }
                    url.put("bandwidth",""+bandwidth);
                    //如果是安卓设备，是不支持多重m3u8的。只把最低码流的给他
                    if(isAndroid&&m3u8Format){
                        if(urls.size()==0){
                            urls.add(url);
                        }else{
                            Map<String,String> old = urls.get(0);
                            if(old==null){
                                urls.add(url);
                            }else{
                                int oldBandwidth = StringUtils.string2int(url.get("bandwidth"),0);
                                if(oldBandwidth>bandwidth&&bandwidth>300*1024){
                                    //只有大于300K，并且小于已经保存的，才会替换当前的媒体链接
                                    urls.clear();
                                    urls.add(url);
                                }
                            }
                        }
                    }else{
                        urls.add(url);
                    }
                }

            }
        }
        contentName = content.getName();
        //排序之前，把当前contentPropertyId对应片段优先
        urlLogger.debug("可以用的URL有："+urls.size()+"个");
        if(m3u8Format){
            com.fortune.util.SortUtils.sortArray(urls,"bandwidth","asc");
        }else{
            Map<String,String> url= null;
            for(int idx=0,l=urls.size();idx<l;idx++){
                url = urls.get(idx);
                String urlContentPropertyId = url.get("contentPropertyId");
                if(contentPropertyId==StringUtils.string2long(urlContentPropertyId,-1)){
                    urls.remove(idx);
                    break;
                }else{
                    url = null;
                }
            }
            com.fortune.util.SortUtils.sortArray(urls,"bandwidth","asc");
            if(url!=null){
                urls.add(0,url);
            }
        }
        //logger.debug("获取数据库内的播放连接：contentPropertyId="+contentPropertyId);
    }

    //
    //获取播放连接
    //
    //
    //
    //
    //logger.debug("获取数据库内的播放连接：clipId="+clipId);
    if(jsonFormat){
        out.print(getJsonResult(true,urls,""));
        return;
    }
    if(m3u8Format){
        urlLogger.debug("输出M3U8");
       StringBuilder builder = new StringBuilder();
       builder.append("#EXTM3U\n");
        if(request.getParameter("testOnly")!=null||AppConfigurator.getInstance().getBoolConfig("notReady",false)){
            urls.clear();
            Map<String,String> url384 = new HashMap<String,String>();
            url384.put("url","http://61.55.144.112/vod/voole/encode/movie/mp4/爱笑会议室-20120112-900.384k.mp4.m3u8");
            //url.put("url","http://192.168.1.28:8080/vod/mp4/kxgg-512.mp4.m3u8");
            url384.put("name","testUrl");
            url384.put("bandwidth",""+(384*1024L));
            urls.add(url384);
            Map<String,String> url512k = new HashMap<String,String>();
            url512k.put("url", "http://61.55.144.112/vod/voole/encode/movie/mp4/爱笑会议室-20120112-900.512k.mp4.m3u8");
            //url.put("url","http://192.168.1.28:8080/vod/mp4/kxgg-512.mp4.m3u8");
            url512k.put("name", "testUrl");
            url512k.put("bandwidth", "" + (512 * 1024L));
            urls.add(url512k);
            Map<String,String> url750 = new HashMap<String,String>();
            url750.put("url","http://61.55.144.112/vod/voole/encode/movie/mp4/爱笑会议室-20120112-900.768k.mp4.m3u8");
            //url750.put("url","http://192.168.1.28:8080/vod/mp4/科学怪狗-750.mp4.m3u8");
            url750.put("name","testUrl750");
            url750.put("bandwidth",""+(750*1024L));
            urls.add(url750);
/*
            Map<String,String> url1200 = new HashMap<String,String>();
            url1200.put("url","http://61.55.144.112/vod/voole/movie/mp4/爱笑会议室-20120112-900.mp4.m3u8");
            //url1200.put("url","http://192.168.1.28:8080/vod/mp4/科学怪狗-1200.mp4.m3u8");
            url1200.put("name","testUrl750");
            url1200.put("bandwidth",""+(1200*1024L));
            urls.add(url1200);
*/
        }
       for(Map<String,String> aUrl :urls){
           builder.append("#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=").append(aUrl.get("bandwidth")).append("\n");
           //builder.append(URLEncoder.encode(aUrl.get("url"),"UTF-8") + "\n");
           builder.append(aUrl.get("url")).append("\n");
       }
       urlLogger.debug("获取连接过程结束:\n"+builder.toString());
       out.println(builder.toString());
       return;
    }else{

    }

    String hosts = "";
    StringBuilder xmlBuilder = new StringBuilder();
    xmlBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n").append("<data>\n")
            .append("<video title=\"").append(contentName).append("\" live=\"").append(isLive).append("\">\n");
    boolean testOnly = "true".equals(request.getParameter("testOnly"));
    String[] testUrls = new String[]{"kxgg-512.mp4","科学怪狗-750.mp4","科学怪狗-1200.mp4"};
    int i=0;
    for(Map<String,String> aUrl :urls){
        String url = aUrl.get("url");
        i++;
        if(i>3){
            continue;
        }
        String playUrl = getClipUrl(url);
        if("".equals(hosts)){
            hosts = getHostUrl(url);
        }
        if(testOnly){
            playUrl = testUrls[i];
            i++;
            if(i>=testUrls.length){
                i = 0;
            }
        }
//        logger.debug("播放链接："+url+"->"+playUrl);
        playUrl = URLEncoder.encode(playUrl,"UTF-8");
        xmlBuilder.append("<path rate=\"").append(aUrl.get("name")).append("\">")
                .append(playUrl).append("</path>\n");
    }
    if(testOnly){
        hosts = "http://192.168.1.28:8080/vod/";
    }else{
        //hosts = "http://61.55.144.114/vod/";
    }

    xmlBuilder.append("<hosts>").append(hosts).append("</hosts>\n").append(" </video>\n</data>");
    urlLogger.debug(xmlBuilder.toString());
    out.print(xmlBuilder.toString());
%><%!
    Logger urlLogger = Logger.getLogger("com.fortune.rms.jsp.getPlayUrl");
    public String getBanwidthName(String propertyName){
        return propertyName.split(":")[0];
    }

    public String getJsonResult(boolean success,List<Map<String,String>> urls,String message){
        Map<String,Object> result= new HashMap<String,Object>();
        result.put("success",success);
        if(message==null){
            message = "";
        }
        result.put("error",new String[]{message});
        if(urls!=null){
            for(Map<String,String> aUrl :urls){
                result.put("url",aUrl.get("url"));
            }
        }
        return (JsonUtils.getJsonString(result));
    }
    public String getBanwidthNameFromCode(String propertyName,String propertyCode){
        String result;
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
                return url.substring(0,p+5);
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
                return url.substring(p+5);
            }
            url = StringUtils.getClearURL(url);
            return url;
        }else{
            return null;
        }
    }
    @SuppressWarnings("unused")
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
                    //isAndroid = false;
                }
            }
        }
        return androidVersion;
    }
    public String encodeAgent(String userAgent){
        userAgent = userAgent.replace("%","PERCENT");
        userAgent = userAgent.replace("/","DIV");
        userAgent = userAgent.replace("(","LEFT");
        userAgent = userAgent.replace(")","RIGHT");
        userAgent = userAgent.replace(" ","SPACE");
        //userAgent = userAgent.replace("/","DIV");
        return userAgent;
    }
    public Long getClipId(ContentProperty sourceClip,String propertyCode,PropertyLogicInterface propertyLogicInterface,
                          ContentPropertyLogicInterface contentPropertyLogicInterface){
        if(sourceClip!=null){
            if("Media_Url_Source".equals(propertyCode)){
                return sourceClip.getId();
            }else{
                Property property = propertyLogicInterface.getByCode(propertyCode);
                if(property!=null){
                    ContentProperty searchBean = new ContentProperty();
                    searchBean.setPropertyId(property.getId());
                    searchBean.setIntValue(sourceClip.getIntValue());
                    searchBean.setContentId(sourceClip.getContentId());
                    List<ContentProperty> clips = contentPropertyLogicInterface.search(searchBean);
                    if(clips!=null&&clips.size()>0){
                        ContentProperty clip = clips.get(0);
                        if(clip!=null){
                            return clip.getId();
                        }
                    }
                }
            }
        }
        return -1L;
    }
    public long guessContentPropertyId(final long contentId,final int willClipId,final  String propertyCode,
                                       final ContentPropertyLogicInterface contentPropertyLogicInterface,
                                       final PropertyLogicInterface propertyLogicInterface){
        if(contentId<=0||willClipId<0||propertyCode==null){
            urlLogger.error("输入参数不正常：contentId="+contentId+",clipId="+willClipId+",propertyCode="+propertyCode);
            return -1L;
        }
        String key = "contentId"+contentId+"_clipId"+willClipId+"_p"+propertyCode;
        Object result =  CacheUtils.get(key,"contentPropertyCache",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                Long result = -1L;
                Property sourceProperty = propertyLogicInterface.getByCode("Media_Url_Source");
                if(sourceProperty!=null&&sourceProperty.getId()>0){
                    ContentProperty cp = new ContentProperty();
                    cp.setContentId(contentId);
                    cp.setPropertyId(sourceProperty.getId());
                    PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.intValue","asc");
                    urlLogger.debug("准备查询clips：contentId="+contentId+",propertyId="+cp.getPropertyId());
                    List<ContentProperty> sourceClips = contentPropertyLogicInterface.search(cp, pageBean);
                    if(sourceClips!=null){
                        urlLogger.debug("查询clips结果：contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount="+sourceClips.size());
                        int clipCount = sourceClips.size();
                        int clipIntValue = willClipId;
                        if(clipCount>0){
                            //如果影片的第一集从0开始，那么，就要将clipId减去1，因为默认是从1开始的
                            ContentProperty firstClip = sourceClips.get(0);
                            if(firstClip!=null){
                                Long firstIntValue = firstClip.getIntValue();
                                if(firstIntValue!=null){
                                    if(firstIntValue==0){
                                        urlLogger.warn("第一集从0开始的，应该从1开始");
                                        clipIntValue--;
                                    }else{
                                        urlLogger.debug("第一集的起始集数是："+firstIntValue);
                                    }
                                }else{
                                    urlLogger.error("第一集的起始集数为空，这是错误的！");
                                }
                            }else{
                                urlLogger.error("第一集是空的，发生严重问题！");
                            }
                            //如果只有一集
                            if(clipCount==1){
                                return getClipId(sourceClips.get(0), propertyCode,propertyLogicInterface,contentPropertyLogicInterface);
                            }else{
                            }
                            //扫描一遍所有的影片源片段，防止在缺集的情况下出现异常
                            for(ContentProperty clip:sourceClips){
                                Long intValue = clip.getIntValue();
                                if(intValue==null){
                                    intValue = 1L;
                                }
                                if(clipCount<intValue){
                                   clipCount = intValue.intValue();
                                }
                                if(clipIntValue==intValue.intValue()){
                                    return getClipId(clip, propertyCode,propertyLogicInterface,contentPropertyLogicInterface);
                                }
                            }
                        }else{
                            urlLogger.error("没有找到任何的影片播放源数据：contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount=0");
                        }
                    }else{
                        urlLogger.error("没有找到任何的影片播放源数据：contentId="+contentId+",propertyId="+cp.getPropertyId());
                    }
                }
                return result;
            }
        });
        if(result ==null){
            return -1L;
        }
        if(result instanceof Long){
            return (Long) result;
        }
        if(result instanceof Integer){
            return ((Integer)result).longValue();
        }
        return StringUtils.string2long(result.toString(),-1L);
    }
    public boolean verifyToken(String token,String phone){
        String pwd = AppConfigurator.getInstance().getConfig("mobilePhoneTokenPassword","fortune!@#456");
        boolean result = false;
        if(token!=null&&phone!=null&&pwd!=null){
            if(token.length()==46){
                String timeStr = token.substring(0,14);
                String lastToken = token.substring(14);
                urlLogger.debug("lastToken="+lastToken+",timeStr="+timeStr);
                try {
                    String calToken = MD5Utils.getMD5String(timeStr+phone+pwd);
                    if(lastToken.equals(calToken)){
                        result = true;
                    }else{
                        urlLogger.error("校验token失败："+calToken+"!="+lastToken);
                    }
                } catch (NoSuchAlgorithmException e) {
                }
            }else{
                urlLogger.error("Token长度异常："+token.length()+"!=46");
            }
        }else{
            urlLogger.error("参数缺失：token="+token+",phone="+phone+",pwd="+pwd);
        }
        return result;
    }

%><%@include file="urlUtils.jsp"%>