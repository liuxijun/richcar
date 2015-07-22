<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-6-13
  Time: 18:45:52
  播放连接
--%><%@ page contentType="text/html;charset=GBK" language="java" %><%@page
        import="com.fortune.rms.business.content.logic.logicInterface.*,
        com.fortune.rms.business.system.model.*,
        com.fortune.rms.business.system.logic.logicInterface.*,
        java.io.*,com.fortune.util.*,java.util.*,org.apache.log4j.Logger,java.net.Socket,
        com.fortune.rms.business.content.model.*" %><%
    Logger logger = Logger.getLogger("com.fortune.rms.rmsJsp");
    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean(
            "contentLogicInterface",session.getServletContext());
    long contentId = getLongParameter(request,"contentId",-1);
    String mediaUrl = null;
    if(contentId>0){
        Content content = contentLogic.getCachedContent(contentId);
        if(content!=null){
            Long deviceId = content.getDeviceId();
            if(deviceId!=null&&deviceId>0){
                DeviceLogicInterface deviceLogic = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
                Device device = deviceLogic.get(deviceId);
                if(device!=null){
                    long contentPropertyId = getLongParameter(request,"contentPropertyId",-1);
                    if(contentPropertyId>0){
                        ContentPropertyLogicInterface cpLogic = (ContentPropertyLogicInterface)SpringUtils.getBean(
                                "contentPropertyLogicInterface",session.getServletContext());
                        ContentProperty cp = cpLogic.get(contentPropertyId);
                        if(cp!=null){
                            mediaUrl = device.getUrl()+"/"+cp.getStringValue();
                        }
                    }
                    if(mediaUrl == null){
                        int clipId = getIntParameter(request,"clipId",-1);
                        if(clipId>=0){
                            Object clipsObj = content.getProperties().get("MEDIA_CLIP");
                            if(clipsObj==null){
                                clipsObj = content.getProperties().get("MEDIA_URL");
                            }
                            if(clipsObj!=null){
                                if(clipsObj instanceof List){
                                    List<String> clips = (List<String>)clipsObj;
                                    if(clips.size()<=clipId||clipId<0){
                                        clipId = 0;
                                    }
                                    mediaUrl = device.getUrl()+"/"+clips.get(clipId);
                                }else if(clipsObj instanceof String){
                                    mediaUrl = device.getUrl()+"/"+clipsObj.toString();
                                }
                            }else{
                                logger.error("clipObj is null,id="+clipId);
                            }
                        }else{
                            logger.error("clipId is null:"+clipId);
                        }
                    }else{
                        logger.error("mediaUrl 无法获取："+content);
                    }
                }else{
                    logger.error("device is null:"+deviceId);
                }
            }else{
                logger.error("deviceId is null:"+deviceId);
            }
        }else{
            logger.error("content is null:"+contentId);
        }
    }else{
        logger.error("contentId is null");
    }
/*
    if(mediaUrl!=null){
        mediaUrl = getGslbUrl(mediaUrl,request.getRemoteAddr());
        mediaUrl = java.net.URLDecoder.decode(mediaUrl,"UTF-8");
        if(getIntParameter(request,"hardcache",1)==1){
            mediaUrl = getCenterServerUrl(mediaUrl);
        }
    }else{
        mediaUrl = "errorGetUrl.jpg";
    }
*/
    mediaUrl +="?"+request.getQueryString();
%><ASX version="3.0">
    <Entry>
        <REF  HREF="<%=mediaUrl%>"/>
    </Entry>
</ASX><%!
    public int getIntParameter(HttpServletRequest request,String name,int defaultValue){
        return StringUtils.string2int(request.getParameter(name),defaultValue);
    }
    public long getLongParameter(HttpServletRequest request,String name,long defaultValue){
        return StringUtils.string2long(request.getParameter(name),defaultValue);
    }
    public String getCenterServerUrl(String mediaUrl){
///*
        String[] hardCaches = new String[]{"218.26.171.244"};//,"218.26.171.245"};
//            String[] hardCaches = new String[]{"218.26.171.200","202.99.208.198"};
        int randomIndex =(int) Math.round( Math.random()*hardCaches.length);
        if(randomIndex<0||randomIndex>=hardCaches.length){
           randomIndex = 0;
        }
        if(randomIndex >=0 && randomIndex <hardCaches.length){
            mediaUrl = StringUtils.checkURL( "mms://"+hardCaches[randomIndex]+
                    "/"+ StringUtils.getClearURL(mediaUrl));
        }
        return mediaUrl;
//*/
    }
    public String getGslbUrl(String requestUrl,String remoteAddr){
        String result = "";
        if(requestUrl!=null){
            try {
                requestUrl =java.net.URLDecoder.decode(requestUrl,"UTF-8");
                requestUrl =java.net.URLDecoder.decode(requestUrl,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.err.println(e);
            }
            //System.out.println("requestUrl="+requestUrl);
            TcpUtils client = new TcpUtils();
            String hostAddr = com.fortune.util.TcpUtils.getHostFromUrl(requestUrl);
            if(client.open(hostAddr,com.fortune.util.TcpUtils.getPortFromUrl(requestUrl))){
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
        }
        if("".equals(result)){
            result = getCenterServerUrl(requestUrl);
        }
        return result;
    }
    public class TcpUtils {
        private java.net.Socket sockRequest = null;//new java.net.Socket( MiddleServerAddress, MiddleServerPort );
        private BufferedReader sockIn = null;
        private InputStream socketInStream;
        private PrintStream sockOut = null;
        protected Logger logger = Logger.getLogger(this.getClass());

        public Socket getSockRequest() {
            return sockRequest;
        }

        public void setSockRequest(Socket sockRequest) {
            this.sockRequest = sockRequest;
        }

        public void init(Socket socket){
            try {
                socketInStream = socket.getInputStream();
                sockIn = new BufferedReader(new InputStreamReader(socketInStream));
                sockOut = new PrintStream(socket.getOutputStream());
            } catch (IOException e) {
                logger.error("无法初始化Socket!"+e.getMessage());
            }
        }

        public boolean open(String serverAddr, int serverPort) {
            try {
                //logger.debug("准备打开远端服务器："+serverAddr+":"+serverPort);
                sockRequest = new java.net.Socket(serverAddr, serverPort);
                init(sockRequest);
                //初始化输入输出设备
            } catch (Exception e) {
                //logger.error("无法初始化"+serverAddr+":"+serverPort+","+e.getMessage());
                return false;
            }
            return true;
        }

        public boolean close() {
            try {
                //关闭连接，原来的输入输出会自动关闭
                sockRequest.close();
                return true;
            } catch (Exception e) {
                logger.error("无法关闭Socket："+e.getMessage());
                return false;
            }
        }

        public int write(String msg) {
            if (sockOut != null) {
                sockOut.print(msg);
                return msg.length();
            } else {
                return 0;
            }
        }

        public int writeln(String msg) {
            return write(msg + "\r\n");
        }

        public String read() {
            if (sockIn != null) {
                try {
                    return sockIn.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
            return null;
        }

        public String readAll(String endStr) {
            StringBuffer inputStr = new StringBuffer();
            String temp;
            try {
                while ((temp = sockIn.readLine()) != null) {
                    if (temp.equals(endStr)) {
                        break;
                    }
                    inputStr.append(temp).append("\n");
                }
                inputStr.append("\n");
            } catch (Exception e) {
                logger.error("远程地址"+sockRequest.getInetAddress().getHostAddress()+"无法读取数据："+e.getMessage());
            }
            return inputStr.toString();
        }

        public String readAll() {
            StringBuffer result = new StringBuffer();
            char[] chs = new char[128];
            int dataLength;
            try {
                //boolean hasMoreData = true;
                while (true) {
                    dataLength = sockIn.read(chs);
                    if (dataLength > 0) {
                        result.append(chs, 0, dataLength);
                    }
                    if (dataLength < chs.length) {
                        //hasMoreData=false;
                        break;
                    }
                    chs[0] = '\0';
                }
            } catch (Exception e) {
                logger.error("远程地址"+sockRequest.getInetAddress().getHostAddress()+"无法读取数据："+e.getMessage());
            }
            return result.toString();
        }
        public int read(byte[] dataBuffer,int startPos ,int length){
            try {
                return socketInStream.read(dataBuffer,startPos,length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
        public int read(byte[] dataBuffer){
            return read(dataBuffer,0,dataBuffer.length);
        }

        public void write(byte[] dataBuffer){
            try {
                sockOut.write(dataBuffer);
            } catch (IOException e) {
                logger.error("无法写数据："+e.getMessage());
            }
        }
    }
%>