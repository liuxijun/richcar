<%@ page contentType="text/html; charset=gb2312" %><%@ page
        import="com.fortune.rms.business.user.CacheLogic" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@include
        file="param.jsp"%><%@include file="queryString.jsp"%><%

    String userIp = request.getRemoteAddr();
    String mediaUrl="001";
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
        userId = "anonymous";
    }
    
    String isFree = "0";
    if ("true".equals( session.getAttribute("play_free_"+contentId+"_"+contentPropertyId ) )){
        isFree = "1";
    } else if ("true".equals( session.getAttribute("play_"+contentId+"_"+contentPropertyId ) )){

    } else {
        out.println("认证无效");
        return;
    }

    Content content = null;
    try {
        CacheLogic cacheLogic= new CacheLogic(request);
        content = cacheLogic.getContent(contentId);

        long areaId = cacheLogic.getAreaId(userIp);

        mediaUrl = cacheLogic.getMediaUrl(contentId, contentPropertyId)
                + "?" + queryStr + "&userId="+userId + "&userIp="+userIp + "&isFree="+isFree + "&areaId="+areaId;
        String tokenPwd = getTokenPwd(mediaUrl,"fortuneRMS");
        try {
            String gslbMediaUrl = getGslbUrl(mediaUrl,userIp);
            if(null==gslbMediaUrl){//没调度，发生问题了
                mediaUrl = mediaUrl.replaceAll("hbcdn.openhe.net",getRandomOf(gslbServerIps));
            }else{
                mediaUrl=gslbMediaUrl;
            }
        } catch (Exception e) {
            logger.error("试图调度时发生异常："+e.getMessage());
        }
        mediaUrl = regUrl(mediaUrl,userIp,tokenPwd);
    } catch (Exception e) {
        logger.error("初始化播放器时发生异常："+mediaUrl+"："+e.getMessage());
    }

%><html>
            <head>
                <meta name="GENERATOR" content="Media Stack XSLT Editor"/>
                <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
                <title>正在播放--<%=content.getName()%></title>

            </head>
            <body bgcolor="#FFFFFF"  leftmargin="0" topmargin="0" onkeypress="keyControl()">
                <table border="0" width="100%" id="table1" height="100%" bgcolor="#FFFFFF" cellspacing="0" cellpadding="0">
                    <tbody>
                                <tr height="*">
                                    <td colspan="3">
                                        <object id="program" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2" type="application/x-oleobject" height="100%" standby="Loading Microsoft Windows Media Player components..." width="100%" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">
                                            <param name="uiaMode" value="none"/>
                                            <param name="AutoStart" value="true"/>
                                            <param name="AUTOSIZE" value="true"/>
                                            <param name="stretchToFit" value="true"/>
                                            <PARAM NAME="URL" value="<%=mediaUrl%>"/>
                                        </object>
                                    </td>
                                </tr>
                    </tbody>
                </table>

            </body>
        </html>
<%@include file="urlUtils.jsp"%>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.yzPlayer.jsp");
%>