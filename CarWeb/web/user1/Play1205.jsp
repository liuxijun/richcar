<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.mediastack.dataunit.MediaIcp,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.dataunit.Channel,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        java.sql.Connection,
        java.sql.ResultSet,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.dataunit.IcpMediaInfo,
        cn.sh.guanghua.cache.CacheManager,
        cn.sh.guanghua.mediastack.dataunit.Tvchannel,
        cn.sh.guanghua.mediastack.business.normal.ChannelLogic,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.util.tools.StringTools"%><%@ include file="cookies.jsp" %><%

     /**
     * 本页面的任务有二：
     * 一是读Cookie中的信息、
     * 二是根据读到的结果显示不同的内容
     */
    String userInfo = "";
    String sessionId = "";
    String bingindId = "";

    /*  本页面的任务之一：读Cookie中的信息：userInfo、sessionId、bindingId */
    userInfo  = getCookieValue(request, "userInfo");
    sessionId = getCookieValue(request, "sessionID");
    bingindId = getCookieValue(request, "bindingID");

try{
    //获取影片参数
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
            clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;

    //drm信息
    String cid = ParamTools.getParameter(request,"cid","");
    String returnDrm = ParamTools.getParameter(request,"return","");
    String openFlag = ParamTools.getParameter(request,"openflag","");    
    if(cid!=null&&!"".equals(cid)){
        if (openFlag.equals("true")){//弹出窗口
            session.setAttribute("LICENSE_RETURN",returnDrm);
            returnDrm =  "/user/account/drmreturn.jsp";
        }
    
        try{
            String sqlStr = "select mc.mediaclip_id as clipid,mi.media_id as mediaid,mi.icp_id as icpid,mi.mediaicp_id as mediaicp_id,mi.media_channelid as channelid from media_icp mi,media m,media_clip mc where m.media_id=mc.mediaclip_mediaid and mc.mediaclip_cid = ? and m.media_id=mi.media_id and mi.media_published = " + Constants.MEDIA_PUBLISHED;
            PreparedStatement pstm = DbTool.getInstance().getPreparedStatement(sqlStr);
            pstm.setString(1,cid);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                mediaIcpId =  rs.getLong("mediaicp_id");
                clipId = rs.getLong("clipid");
                channelId = rs.getLong("channelid");
                icpId = rs.getLong("icpid");
                mediaId = rs.getLong("mediaid");
                serviceType = Constants.SERVICE_TYPE_DRM;
                paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
                           clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
                           "&mu=" + mediaUrl +"&service_type=" + serviceType;
            }else{
                response.sendRedirect(PlayLogic.getDrmSuccessReturnUrl(returnDrm,Constants.PLAY_VALID_TIME));
                return;
            }
            rs.close();
            Connection conn = pstm.getConnection();
            pstm.close();
            conn.close();
            session.setAttribute("DrmCid",cid);
            session.setAttribute("DrmReturn",returnDrm);
        }catch(Exception e){
            response.sendRedirect(PlayLogic.getDrmSuccessReturnUrl(returnDrm,Constants.PLAY_VALID_TIME));
            return;
        }
    }

    //读取MediaIcp
    //当是媒体点播，媒体下载，DRM时，得到channel_id,如果频道改变的时候，频道包月是有问题
    MediaIcp mediaIcp = null;
    if (serviceType == Constants.SERVICE_TYPE_DOWN
            || serviceType == Constants.SERVICE_TYPE_MEDIA
            || serviceType == Constants.SERVICE_TYPE_DRM){
        mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
        if (mediaIcpId == -1 && mediaIcp == null){
            response.sendRedirect("account/error.jsp?msg=99&WebService=true");
            return;
        }
        mediaId = mediaIcp.getMediaId();

        paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
                   clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
                   "&mu=" + mediaUrl +"&service_type=" + serviceType ;
    }

    //检查ip是否合法
    String ip=request.getRemoteAddr();
    int result = PlayLogic.checkIcpIp(ip,icpId);
    if(result == 0){
          response.sendRedirect("account/error.jsp?msg=98&WebService=true");
          return;
    }
    if (serviceType != Constants.SERVICE_TYPE_LIVING){
        if(!PlayLogic.checkIp(ip,channelId)){
            response.sendRedirect("account/error.jsp?msg=98&WebService=true");
            return;
        }
    }
    if (serviceType == Constants.SERVICE_TYPE_DOWN){
        if(!PlayLogic.checkIp(ip,channelId)){
            response.sendRedirect("account/error.jsp?msg=98&WebService=true");
            return;
        }else{
            session.setAttribute(Constants.SESSION_CORPERATION_ID,""+icpId);
            session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
            response.sendRedirect("account/Download.jsp?"+paramString);
            return;
        }
    }

//免费影片（费用为0）的影片直接播放

    if(mediaIcp!=null&&mediaIcp.getMediaPrice()==0){
	if(serviceType == Constants.SERVICE_TYPE_MEDIA){
	    session.setAttribute(Constants.SESSION_CORPERATION_ID,""+icpId);
	    session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
            response.sendRedirect("account/Player.jsp?"+paramString);
	    return;

	}
    }

    //用户登录成功
    if  (userInfo != null
                 && !userInfo.equals("")
                 && !userInfo.equals("null")
                 && sessionId != null
                 && !sessionId.equals("")
                 && !sessionId.equals("null"))
    {
        SessionUser su = new SessionUser();
        su.setUserInfo(userInfo);
        su.setSessionId(sessionId);
        //得到用户id
        su.setUserId(StringTools.getSubString(userInfo,"|",5));
        session.setAttribute(Constants.SESSION_USER,su);
        response.sendRedirect("account/CheckAccount.jsp?" + paramString);
        return;
    }else{
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv='Content-Language' content='zh-cn'>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=gb2312'>");
        out.println("<title></title>");
        out.println("<style>td{font-size:14px;}</style>");
        out.println("</head>");
        out.println("<body topmargin='0'' leftmargin='0' onload='window.resizeTo(510,305)'>");
        out.println("<table border='0'' width='500'' cellpadding='0' style='border-collapse: collapse' height='305' background='../../images/account/123.gif' align='center'>");
        out.println("	<tr><td height='10'></td></tr>");
        out.println("	<tr>");
        out.println("		<td align='right' width='185'>"); 
		out.println("<img src='http://kdsj2.sx.cn/yqts.jpg' width='94' height='87'><img width='25' height='1' border='0'");
        out.println("		</td>");
        out.println("		<td style='line-height:200%;'>");
        out.println("　　您所点播节目为本网站收费内容，<br>请到<a href='http://www.kdsj2.sx.cn' target='_blank'>宽带三晋II网站首页</a>成功登录后，<br>再进行观看。");
        out.println("		</td>");
        out.println("	</tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}catch(Exception e){
    e.printStackTrace();
}

%>