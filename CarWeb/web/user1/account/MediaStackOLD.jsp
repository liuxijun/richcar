<%@ page contentType="text/html; charset=GBK" %><%@page
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
    String mediaSession = (String)session.getAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId);
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
        if(ParamTools.getIntParameter(request,"hardcache",0)==1){
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
        }
        regMediaUrl = PlayLogic.regUrl(mediaUrl,isMidware);
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
%>