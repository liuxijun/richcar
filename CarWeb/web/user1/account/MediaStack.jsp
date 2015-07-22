<%@ page contentType="text/html; charset=GBK" %><%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.net.Socket" %><%@ page import="java.io.*" %><%@ page
        import="java.net.InetAddress" %><%@ page
        import="java.net.UnknownHostException" %><%@page
import="java.util.*,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.mediastack.business.normal.PlayList,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.mediastack.dataunit.*,
        java.sql.Connection,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.Statement,
        java.sql.ResultSet,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager" %><%@ page import="cn.sh.guanghua.util.tools.StringTools"%><%

    //获取影片参数
        long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
        long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
        long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
        long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
        long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
        String mediaUrl = ParamTools.getParameter(request,"mu","");
        long serviceType = ParamTools.getLongParameter(request,"service_type",0);
         String paramString = "WebService=true&clip_id=" +
                clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
                "&mu=" + mediaUrl +"&service_type=" + serviceType;
         String userAccountId = ParamTools.getParameter(request,"useraccount_id","");
         if (!"".equals(userAccountId)){
             paramString = paramString + "&useraccount_id=" + userAccountId;
         }

    mediaUrl = Base64.decode(mediaUrl) ;

    String userIp = request.getRemoteAddr();
    boolean isMidware = "true".equals(ConfigManager.getConfig().node("log").get("midware","true"));

    //session检查
    String mediaSessionName = Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId;
    session.setAttribute(mediaSessionName,"true");
    String mediaSession = (String)session.getAttribute(mediaSessionName);
    if (!("true".equals(mediaSession))){
        response.sendRedirect("error.jsp?msg=avalid_session");
        return;
    }
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId = Constants.GUEST_SESSION_NAME;
    if (su != null){
        userId = su.getUserId();
    }

    if (serviceType == Constants.SERVICE_TYPE_LIVING){
        Tvchannel tvchannel = (Tvchannel)CacheManager.getInstance().getFromDB("Tvchannel",channelId);
        mediaUrl = tvchannel.getTvchannelUrl() + "?type=live&cid=" + channelId + "&icp=" + icpId +
           "&uid=" + userId + "&rip=" + userIp;
        String regMediaUrl = PlayLogic.regUrl(mediaUrl,isMidware);
        StringBuffer playList = new StringBuffer();
        playList.append("<ASX Version=\"3.0\">\n");
        playList.append("    <ENTRY>\n")
            .append("\t\t<REF HREF=\"")
            .append(regMediaUrl)
            .append("\"/>\n")
            .append("\t</ENTRY>\n");
        playList.append("</ASX>");
        out.println(playList.toString());

    }else{
        List adList = new ArrayList();

        String regMediaUrl = mediaUrl;

        Media media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);
        long formatType = media.getMediaFormattype();
        //MediaStack本地影片播放
        long serverId;
        long subjectId;
        long impId;
        MediaIcp mi = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);

        serverId = mi.getServerId();
        subjectId = media.getMediaSubjectid();
        impId = media.getMediaImpid();

        MediaClip mc =(MediaClip)CacheManager.getInstance().getFromDB("MediaClip",clipId);
        if (mc != null && mc.getMediaclipMediaid()==media.getMediaId()){
            Server server = (Server)CacheManager.getInstance().getFromDB("Server",serverId);
            mediaUrl =server.getServerUrl()+ mc.getMediaclipUrl();
            //System.out.println(mediaUrl);
        }else{
            if(mc.getMediaclipMediaid()!=media.getMediaId()){
                mediaUrl = "errorMid.wmv";

            }
        }

        mediaUrl = mediaUrl + "?cid=" + channelId + "&mid=" + mediaId + "&icp=" + icpId + "&svr=" + serverId +
           "&imp=" + impId + "&sid=" + subjectId + "&uid=" + userId + "&rip=" + userIp;
        if(Constants.GUEST_SESSION_NAME.equals(userId)){
              mediaUrl = mediaUrl + "&fee=" + Constants.USER_TYPE_GUEST;
        }else{
              mediaUrl = mediaUrl + "&fee=" + Constants.USER_TYPE_ACCOUNT;
        }
        mediaUrl = getGslbUrl(mediaUrl,request.getRemoteAddr());
        mediaUrl = java.net.URLDecoder.decode(mediaUrl,"UTF-8");
        if(ParamTools.getIntParameter(request,"hardcache",1)==1){
            mediaUrl = getCenterServerUrl(mediaUrl);
        }
        regMediaUrl = mediaUrl;// PlayLogic.regUrl(mediaUrl,isMidware);
//        System.out.println("\n"+regMediaUrl);
        PlayList play = PlayLogic.createPlayList(regMediaUrl,0,0,-1,formatType,Constants.MEDIA_TYPE_MOVIE);
        //
        //adList = AdCacheManager.getInstance().getAdListCache(mediaId,channelId,media.getMediaFormattype(),isMidware,icpId,userId,userIp);
        adList = AdCacheManager.getInstance().getAdList(mediaIcpId,media.getMediaFormattype(),isMidware,userId,userIp);
        //System.out.println("channelId="+channelId);
        //System.out.println("mediaId="+mediaId);
        //System.out.println("adList_size="+adList.size());

        List list = PlayLogic.handlePlayList(play,adList);

        if(list==null) list = new ArrayList();
        if(list.size()<=0){
          response.sendRedirect("error.jsp?msg=no_playlist");
          return;
        }

        //设置mimetype
        if(formatType==Constants.MEDIA_FORMAT_TYPE_REAL){
            response.setContentType("application/smil;charset=gb2312");
        }else{
            response.setContentType("video/x-ms-asf;charset=gb2312");
        }

        //生成asx或者smil
         String allAsx = PlayLogic.createPlayString(list,formatType);
         //System.out.println("\n\n"+allAsx);
         out.println(allAsx);
    }
/*    StringBuffer playList = new StringBuffer();
    playList.append("<ASX Version=\"3.0\">\n");
    playList.append("    <ENTRY>\n")
        .append("\t\t<REF HREF=\"")
        .append(regMediaUrl)
        .append("\"/>\n")
        .append("\t</ENTRY>\n");
    playList.append("</ASX>");
    out.println(playList.toString());*/
%><%!
    public String getCenterServerUrl(String mediaUrl){
///*
        String[] hardCaches = new String[]{"218.26.171.244"};//,"218.26.171.245"};
//            String[] hardCaches = new String[]{"218.26.171.200","202.99.208.198"};
        int randomIndex =(int) Math.round( Math.random()*hardCaches.length);
        if(randomIndex<0||randomIndex>=hardCaches.length){
           randomIndex = 0;
        }
        if(randomIndex >=0 && randomIndex <hardCaches.length){
            mediaUrl = StringTools.checkURL( "mms://"+hardCaches[randomIndex]+
                    "/"+ StringTools.getClearURL(mediaUrl));
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
            if(client.open("221.204.214.88",554)){
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