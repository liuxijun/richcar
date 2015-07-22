<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.*,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.htmlcreator.CreatorHelper,
        cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.cache.CacheManager"%>
<%@include file="param.jsp"%>
<%
    //session检查
    String mediaSession = (String)session.getAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId);
    if (!("true".equals(mediaSession))){
        response.sendRedirect("error.jsp?msg=avalid_session");
        return;
    }
    try{
    Media media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);

    mediaUrl =  Base64.decode(mediaUrl) ;
    	    //MediaStack本地影片播放
    long serverId;
    long subjectId;
    long impId;
    MediaIcp mi = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);

    serverId = mi.getServerId();
    subjectId = media.getMediaSubjectid();
    impId = media.getMediaImpid();

    MediaClip mc =(MediaClip)CacheManager.getInstance().getFromDB("MediaClip",clipId);
    if (mc != null){
        Server server = (Server)CacheManager.getInstance().getFromDB("Server",serverId);
        mediaUrl =server.getServerFtpdownloadurl()+ mc.getMediaclipUrl();
    }

    if (mediaUrl != null){
        String userIp = request.getRemoteAddr();
        SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
        String userId = Constants.GUEST_SESSION_NAME;
        if (su != null){
            userId = su.getUserId();
        }
        String mediaUrlTemp = mediaUrl + "?type=download&cid=" + channelId + "&mid=" + mediaId + "&icp=" + icpId + "&svr=" + serverId +
           "&imp=" + impId + "&sid=" + subjectId + "&uid=" + userId + "&rip=" + userIp;

        //boolean isMidware = "true".equals(ConfigManager.getConfig().node("log").get("midware","true"));
        //String regMediaUrl = PlayLogic.regUrl(mediaUrlTemp,isMidware);

        //mediaUrl = "ftp://"+regMediaUrl+"@"+mediaUrl.substring(6,mediaUrl.length());
        //mediaUrl = regMediaUrl;
        //response.sendRedirect(mediaUrl);
        //return;
    }
    }catch(Exception e){
        e.printStackTrace();
         response.sendRedirect("error.jsp?msg=error_file");
        return;
    }

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>下载</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0" onload="window.resizeTo(560,395)">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/b.gif">
  <tbody>
  <tr>
    <td align="left" valign="top"><table width="559" height="290" border="0" cellpadding="0" cellspacing="0">
        <tbody>
        <tr>
          <td height="72">　</td>
        </tr>
        <tr>
          <td align="center" valign="top"><table width="559" height="218" border="0" cellpadding="0" cellspacing="0">
              <tbody>
              <tr>
                <td width="86">　</td>
                <td width="473" align="left" valign="top"><table width="377" height="201" border="0" cellpadding="2" cellspacing="0" class="black">
                      <tbody>
                      <tr valign="middle">
                        <td height="140" align="center">
                        <%
                        if (!"".equals(mediaUrl) ){
                             out.print("<a href='"+mediaUrl+"'>下载影片</a>");
                        }

                        %>
                        </td>
                      </tr>
                      <tr align="center" valign="middle">
                        <td>提示：下载影片时，请在“下载影片”处，点击鼠标右键，选择“目标另存为”或您电脑中已有的其他下载软件进行下载。
			</td>
                      </tr>
                      <tr align="center" valign="middle">
                        <td>[ <a href="javascript:window.close();">关闭窗口</a> ]</td>
                      </tr>
                    </tbody>
                  </table></td>
              </tr>
            </tbody>
            </table></td>
        </tr>
      </tbody>
      </table></td>
  </tr>
</tbody>
</table>
</body>
</html>


