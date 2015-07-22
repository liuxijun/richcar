<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic" %><%

    //获取影片信息
    long mediaId = ParamTools.getLongParameter(request,"mediaid",-1);
    long icpId =ParamTools.getLongParameter(request,"icpid",-1);
    long impId =ParamTools.getLongParameter(request,"impid",-1);
    long channelId = ParamTools.getLongParameter(request,"channelid",-1);
    long feeType =ParamTools.getLongParameter(request,"fee",Constants.USER_TYPE_GUEST);
    String mediaUrl = ParamTools.getParameter(request,"url","");
    String userId = ParamTools.getParameter(request,"userid","");
    String userIp = ParamTools.getParameter(request,"userip","");

    boolean isMidware = "true".equals(ConfigManager.getConfig().node("log").get("midware","true"));

    String regMediaUrl =mediaUrl;
    mediaUrl = mediaUrl + "?cid=" + channelId + "&mid=" + mediaId + "&icp=" + icpId + "&svr=-1" +
               "&imp=" + impId + "&sid=-1&uid=" + userId + "&rip=" + userIp + "&fee=" + feeType;
    regMediaUrl = PlayLogic.regUrl(mediaUrl,isMidware);

    StringBuffer playList = new StringBuffer();
    playList.append("<ASX Version=\"3.0\">\n");
    playList.append("    <ENTRY>\n")
        .append("\t\t<REF HREF=\"")
        .append(regMediaUrl)
        .append("\"/>\n")
        .append("\t</ENTRY>\n");
    playList.append("</ASX>");
    out.println(playList.toString());
%>