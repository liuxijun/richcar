<%@ page contentType="text/html; charset=gb2312" %><%@ page
        import="org.apache.log4j.Logger,
                java.security.NoSuchAlgorithmException" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="com.fortune.server.message.ServerMessager" %><%@ page
        import="com.fortune.server.message.RtspServerMessager" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="java.util.concurrent.TimeUnit" %><%@ page
        import="java.util.concurrent.ScheduledExecutorService" %><%@ page
        import="java.util.concurrent.Executors" %><%@ page
        import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-5-23
  Time: 上午9:54
  播放连接处理的一些基本方法
--%><%!
    public static String[] serverIps = new String[]{"61.55.145.140","61.55.145.141","61.55.145.142","61.55.145.144"};
    //    String[] gslbServerIps = new String[]{"61.55.145.153","61.55.145.154"};
    String[] gslbServerIps = new String[]{"61.55.144.95","61.55.144.96"};
    static boolean useSlb = false;
    static boolean useFormIp=false;
    private static Logger loggerInUrlUtils = Logger.getLogger("com.fortune.rms.jsp.urlUtils.jsp");
    private static String  getRandomOf(String[] ips){
        int indexId =(int) Math.round(Math.random()*ips.length);
        if(indexId<0)indexId=0;
        if(indexId>=ips.length){
            indexId = ips.length-1;
        }
        return ips[indexId];
    }
    public String regUrl(String url,String clientIp,String tokenPwd){
        String result = url;
        if(result.contains("?")){
            result+="&";
        }else{
            result+="?";
        }
        result+="timestamp="+StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        if(useSlb){
            result = url.replaceAll("hbcdn.openhe.net",getRandomOf(serverIps));
        }
        if(tokenPwd==null||"".equals(tokenPwd)){
            tokenPwd = AppConfigurator.getInstance().getConfig("cdn.tokenPassword","fortune2009");
        }
        String clearUrl = StringUtils.getClearURL(result);
        while(clearUrl.startsWith("/")&&clearUrl.length()>1){
            clearUrl = clearUrl.substring(1);
        }
        String checkURL = clearUrl+"&clientip="+clientIp+tokenPwd;
        try {
            loggerInUrlUtils.debug("计算token使用的URL="+checkURL);
            String checkToken = MD5Utils.getMD5String(checkURL);
            result = result+"&encrypt="+checkToken;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return result;
    }
    public static final int VERIFY_RESULT_ERROR=1000;
    public static final int VERIFY_RESULT_PASSED=0;
    public static final int VERIFY_RESULT_NO_ENCRYPT=VERIFY_RESULT_ERROR+1;
    public static final int VERIFY_RESULT_TIME_OUT=VERIFY_RESULT_ERROR+2;
    public static final int VERIFY_RESULT_ENCRYPT_ERROR=VERIFY_RESULT_ERROR+3;


    public int verifyUrlToken(String url,String clientIp,String tokenPwd){
        int result = VERIFY_RESULT_ENCRYPT_ERROR;
        if(tokenPwd==null||"".equals(tokenPwd)){
            tokenPwd = AppConfigurator.getInstance().getConfig("cdn.tokenPassword","fortune2009");
        }
        String clearUrl = StringUtils.getClearURL(url);
        while(clearUrl.startsWith("/")&&clearUrl.length()>1){
            clearUrl = clearUrl.substring(1);
        }
        try {
            int p=clearUrl.indexOf("&encrypt=");
            if(p>0){
                String token = StringUtils.getParameter(clearUrl, "encrypt");
                String checkURL = clearUrl.substring(0,p)+"&clientip="+clientIp+tokenPwd;
                if(token!=null){
                    String checkToken = MD5Utils.getMD5String(checkURL);
                    if(token.equals(checkToken)){
/*
                        String ipInUrl = StringUtils.getParameter("clientip","");
                        if(clientIp.equals(ipInUrl)){
                        }else{
                            loggerInUrlUtils.error("用户验证IP不相等：请求IP="+clientIp+",颁发证书IP="+ipInUrl);
                        }
*/
                        String timeStamp = StringUtils.getParameter(url,"timestamp");
                        if(timeStamp!=null&&!"".equals(timeStamp.trim())){
                            Date time = StringUtils.string2date(timeStamp,"yyyyMMddHHmmss");
                            Date now = new Date();
                            if(Math.abs(now.getTime()-time.getTime())>AppConfigurator.getInstance().getIntConfig(
                                    "system.security.tokenTimeout",5)*60*1000L){
                                loggerInUrlUtils.error("用户验证请求超时："+clientIp+","+url);
                            }else{
                                return VERIFY_RESULT_PASSED;
                            }
                        }else{
                            //loggerInUrlUtils.error("URL中没有时间戳：clientIP="+clientIp+",url="+url);
                            return VERIFY_RESULT_TIME_OUT;
                        }

                    }else{
                        //loggerInUrlUtils.error("用户证书验证失败：计算出来的证书="+checkToken+",\n计算使用的URL=" +checkURL+/"\n,URL中证书="+token);
                    }
                }else{
                    //loggerInUrlUtils.error("URL中没有证书：clientIp="+clientIp+",url="+url);
                    return VERIFY_RESULT_NO_ENCRYPT;
                }
            }else{
                //loggerInUrlUtils.error("URL中没有证书：clientIp="+clientIp+",url="+url);
                return VERIFY_RESULT_NO_ENCRYPT;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String getTokenPwd(String url,String defaultPwd){
        String host = TcpUtils.getHostFromUrl(url);
        AppConfigurator config = AppConfigurator.getInstance();
        String tokenPwd = config.getConfig("cdn.tokenPassword",defaultPwd);
        String tempPwd =  config.getConfig("cdn.tokenPassword_"+host,null);
        if(tempPwd!=null){
            return tempPwd;
        }
        return tokenPwd;
    }

    public String getGslbUrl(String requestUrl,String remoteAddr){
        String result = null;
        try {
            if(requestUrl!=null){
                //requestUrl =java.net.URLDecoder.decode(requestUrl,"UTF-8");
                //requestUrl =java.net.URLEncoder.encode(requestUrl,"gbk");
                if(!useFormIp){
                    requestUrl = requestUrl.replaceAll("hbcdn.openhe.net",getRandomOf(gslbServerIps));
                }
                String srcIp = TcpUtils.getHostFromUrl(requestUrl);
                TcpUtils client = new TcpUtils();
                int port = TcpUtils.getPortFromUrl(requestUrl);
                if(client.open(srcIp,port)){
                    if(requestUrl.indexOf("?")>0){
                        requestUrl +="&";
                    }else{
                        requestUrl+="?";
                    }
                    requestUrl+="clientIpForCdn="+remoteAddr;
                    String sendMsg = "DESCRIBE " +requestUrl+
                            " RTSP/1.0\n" +
                            "User-Agent: WMPlayer/9.0.0.2991 guid/3300AD50-2C39-46C0-AE0A-5F8407B9BFEA\n" +
                            "Accept: application/sdp\n" +
                            "Accept-Charset: UTF-8, *;q=0.1\n" +
                            "X-Accept-Authentication: Negotiate, NTLM, Digest, Basic\n" +
                            "Accept-Language: zh-CN, *;q=0.1\n" +
                            "CSeq: 1\n" +
                            "Supported: com.microsoft.wm.srvppair, com.microsoft.wm.sswitch, com.microsoft.wm.eosmsg, com.microsoft.wm.predstrm\n" +
                            "\n";
                    client.write(sendMsg);
                    String clientResult = client.readAll();
                    int l = clientResult.indexOf("Location:");
                    if(l>0){
                        l+=9;
                        result = "";
                        char ch = clientResult.charAt(l);
                        int len = clientResult.length();
                        while(ch!='\n'){
                            result+=ch;
                            l++;
                            if(l>=len)break;
                            ch = clientResult.charAt(l);
                        }
                        result = result.trim();
                        //System.out.println("result="+result);
                    }
                }

                if (!"".equals(result)){
                    String dstIp = TcpUtils.getHostFromUrl(result);
                    //dstIp = "61.55.145.45";
                    result = requestUrl.replaceFirst(srcIp, dstIp);
                    //result = requestUrl;
                }
            }

        } catch (Exception e) {
            loggerInUrlUtils.error("处理调度模块时发生异常，无法正确调度："+e.getMessage());
            return null;
        }

        return result;
    }

    public String outPutMediaUrl(String requestFormat,String mediaUrl){
        StringBuilder playList = new StringBuilder();
        if("asx".equals(requestFormat)){
            playList.append("<ASX Version=\"3.0\">\n");
            playList.append("    <ENTRY>\n")
                    .append("\t\t<REF HREF=\"")
                    .append(mediaUrl)
                    .append("\"/>\n")
                    .append("\t</ENTRY>\n");
            playList.append("</ASX>");
        }else if("REDIRECT".equals(requestFormat)){

        }else if("json".equals(requestFormat)){
            //Json格式
            playList.append("{success:true,playUrl:'").append(mediaUrl).append("'}");
        }else if("text".equals(requestFormat)){
            playList.append(mediaUrl);
        }
        return playList.toString();
    }
%>