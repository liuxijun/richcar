<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*"%>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@include
        file="param.jsp" %>
<%@ page import="com.fortune.rms.business.user.SessionUser" %>
<%@ page import="com.fortune.common.Constants" %>
<%@include file="queryString.jsp" %>
<%@ page import="com.fortune.rms.business.midware.RegMidware" %><%
    //session检查
    String mediaSession = (String)session.getAttribute(Constants.USER_MEDIA_SESSION_HEAD + contentPropertyId + "_" + channelId);
    if (!("true".equals(mediaSession))){
        response.sendRedirect("error.jsp?msg=313");
        return;
    }

    String userIp = request.getRemoteAddr();

    SessionUser su = (SessionUser) session.getAttribute(Constants.SESSION_USER);
    String userId = su.getUserId();
    if (userId == null) {
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

    CacheLogic cacheLogic= new CacheLogic(request);
    Content content = cacheLogic.getContent(contentId);

    long areaId = cacheLogic.getAreaId(userIp);

    String mediaUrl = cacheLogic.getMediaUrl(contentId, contentPropertyId)
            + "?" + queryStr + "&userId="+userId + "&userIp="+userIp + "&isFree="+isFree + "&areaId="+areaId;

    RegMidware regMidware = new RegMidware();
    mediaUrl = regMidware.regUrl(mediaUrl, userIp);

//    String playFile = "rms.jsp?"+request.getQueryString();


//    //获取影片播放文件
//    String playFile = ConfigManager.getConfig().node("player").get("playfile","MediaStack.jsp");
////    String playFile = "http://mediastack.kdsj2.sx.cn:8080/user/account/asx.jsp";
//    playFile = playFile + "?"+paramString;


    long mediaForm = Constants.MEDIA_FORMAT_TYPE_MICROSOFT;
//     Media  media = null;//
//     if (serviceType == Constants.SERVICE_TYPE_LIVING){
//         mediaForm = Constants.MEDIA_FORMAT_TYPE_MICROSOFT;
//         media = new Media();
//         media.setMediaName("直播节目.....");
//     }else{
//        media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);
//        mediaForm = media.getMediaFormattype();
//     }
//    if(media != null){
//        if(media.getMediaImpid()==129){
//           if(!ParamTools.getBoolParameter(request,"playNow",false)){
//               SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
//               String userId = Constants.GUEST_SESSION_NAME;
//               if (su != null){
//                   userId = su.getUserId();
//               }
//               if(userId == null || "".equals(userId)){
//                   userId = "sx_kdsj2_guest";
//
//               }
//               String returnUrl = java.net.URLEncoder.encode("http://mediastack.kdsj2.sx.cn/user/account/Player.jsp?"+request.getQueryString()+"&playNow=1");
//               String sooTvUrl = "http://www.sootv.com/PreDeliver/preDeliver.aspx";
//               MediaClip mc =(MediaClip)CacheManager.getInstance().getFromDB("MediaClip",clipId);
//               String contentId = getClearFileName(mc.getMediaclipUrl());
//               String code="",passwd="pd^#e!I98";
//               MD5 md5worker = new MD5();
//               String timestamp = ""+((new Date()).getTime()/1000);
//               String fromSP="020";
//               String uid="";
//
//               code = md5worker.getMD5ofStr(userId+"$"+passwd+"$"+timestamp).toUpperCase();
//               //?ContentId=XXXXX&code=XXXX&uid=XXXX&returl=XXXXX&timestamp=XXXX&from=XXX";
//               sooTvUrl = sooTvUrl+"?ContentId=" + contentId+"&code="+code+"&uid="+userId+
//                       "&timestamp="+timestamp+
//                       "&from="+fromSP+"&returl="+returnUrl;
//               response.sendRedirect(sooTvUrl+"?");
//               //out.println("<html><body><a href=\""+sooTvUrl+"\">继续"+sooTvUrl+"</a></body></html>");
//               return;
//           }
//        }
//    }
 %>        <html>
            <head>
                <meta name="GENERATOR" content="Media Stack XSLT Editor"/>
                <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
                <script type="text/javascript" src="../js/jquery.js"></script>
                <link href="style/Style.css" rel="stylesheet"/>
                <script type="text/javascript" src="js/cookies.js"></script>
                <script type="text/javascript" src="js/playerCommonNew.js"></script>
                <title>正在播放--<%=content.getName()%></title>
                    <%if (mediaForm == Constants.MEDIA_FORMAT_TYPE_REAL){%>
                        <script type="text/javascript" src="js/playerReal.js"></script>
                        <SCRIPT>
                            HeightBorder = 125;
                            WidthBorder  = 10;
                        </SCRIPT>
                    <%}else{%>
                        <script type="text/javascript" src="js/playerMedia.js"></script>
                        <SCRIPT>
                            HeightBorder = 130;
                            WidthBorder  = 12;
                        </SCRIPT>
                    <%}%>
                <script>
                    SetPosStart  = true;
                    var trackball = null;
                    var statsInfo = null;
                    var reportErrorStart = false;
                    function orgSize(){
                       changeDisplaySize(1);
                    }
                    function doubleSize(){
                       changeDisplaySize(2);
                    }
                    function keyControl(){
                        if (window.event.keyCode=='g' && window.event.ctrlKey){
                            reportErrorStart = true;
                        }
                    }
                    function startReportError(){
                        if(reportErrorStart){
                            var newUrl = "reportError.jsp?<%=queryStr%>";
                            window.open(newUrl,"_blank","width=555,height=390,resizable=yes");
                        }
                    }
                    function fullScreen(){
                       changeDisplaySize(3);
                    }
                    var playStartTime = null;
                    function startPlay(){
                        if(playStartTime!=null){
                            return;
                        }
                        playStartTime = new Date();
                        window.setTimeout("validTimes()",60*1000);
                    }
                    function validTimes(){
                        var nowTime = new Date();
                        if((nowTime.getTime()-playStartTime.getTime())>30*1000){
                            var currentUrl =  document.location.search;
                            var i = currentUrl.indexOf("?");
                            if(i>=0&&i<currentUrl.length-1){
                                currentUrl = currentUrl.substring(i+1);
                            }
                            var noticeUrl = "account/noticeRunway.jsp?"+currentUrl;
                            jQuery.getJSON(noticeUrl,
                            {
                               fcTime:nowTime.getTime()
                            },function(jsonData){

                            });
                        }
                    }
                </script>
                <SCRIPT LANGUAGE = "JScript"  FOR ="program" EVENT = "playStateChange(NewState)">
                switch (NewState){
                  case 1:
//                   myText.value = "Stopped";
                   break;
                  case 2:
//                   myText.value = "Paused";
                   break;
                  case 3:
                   startPlay();
                   break;
                  // Other cases go here.
                  default:
//                   myText.value = "";
                }        
                </SCRIPT>
            </head>
            <body bgcolor="#FFFFFF" onload="javascript:expand()" leftmargin="0" topmargin="0" onkeypress="keyControl()">
                <table border="0" width="100%" id="table1" height="100%" bgcolor="#FFFFFF" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr height="15">

                            <td width="66" height="3">
                                <img onclick="startReportError()" src="images/player/kdsj-1.gif" width="145" height="27"/>
                            </td>
                            <td width="*" background="images/player/line_bg.gif" height="3">　</td>

                            <td width="63" background="images/player/line_bg.gif" height="3">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tbody>
                                        <tr>
                                            <td width="21%" height="26">
                                                <img name="Image5" border="0" src="images/player/1x_gray.gif" width="20" height="27"
                                                    onclick="orgSize();"
                                                    onMouseOut="show_n(this)" onMouseOver="show_o(this)"/>
                                            </td>
                                            <td width="54%" height="26">
                                                <img name="Image5" border="0" src="images/player/2x_gray.gif" width="20" height="27"
                                                    onclick="doubleSize();"
                                                    onMouseOut="show_n(this)" onMouseOver="show_o(this)"/>
                                            </td>
                                            <td width="25%" height="26">
                                                <img name="Image5" border="0" src="images/player/da_gray.gif" width="23" height="27"
                                                    onclick="fullScreen();"
                                                    onMouseOut="show_n(this)" onMouseOver="show_o(this)"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    <%if (mediaForm == Constants.MEDIA_FORMAT_TYPE_REAL){%>
                                <tr height="*">
                                    <td colspan="3">
                                        <object id="realImage" type="audio/x-pn-realaudio-plugin" height="100%" width="100%" classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">
                                            <PARAM NAME="controls" VALUE="ImageWindow"/>
                                            <PARAM NAME="console" VALUE="newMovie0001"/>
                                            <PARAM NAME="AUTOSTART" VALUE="-1"/>
                                            <PARAM NAME="SRC" value="<%=mediaUrl%>"/>
                                            <param name="BACKGROUNDCOLOR" value="#000000"/>
                                        </object>
                                    </td>
                                </tr>
                                <tr height="60">
                                    <td colspan="3">
                                        <object id="realImageControl" type="audio/x-pn-realaudio-plugin" height="100%" width="100%" classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">
                                            <PARAM NAME="controls" VALUE="all"/>
                                            <PARAM NAME="console" VALUE="newMovie0001"/>
                                        </object>
                                    </td>
                                </tr>
                    <%}else{%>
                                <tr height="*">
                                    <td colspan="3">
                                        <object id="program" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2" type="application/x-oleobject" height="100%" standby="Loading Microsoft Windows Media Player components..." width="100%" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">
                                            <param name="uiaMode" value="none"/>
                                            <param name="AutoStart" value="false"/>
                                            <param name="AUTOSIZE" value="true"/>
                                            <param name="stretchToFit" value="true"/>
                                            <PARAM NAME="URL" value="<%=mediaUrl%>"/>
                                        </object>
                                    </td>
                                </tr>
                    <%}%>
                    </tbody>
                </table>

            </body>
        </html>
<%--<%!--%>
    <%--public String extractFileName(String fileName){--%>
        <%--int i= fileName.lastIndexOf("/");--%>
        <%--if(i>=0){--%>
            <%--fileName = fileName.substring(i+1);--%>
        <%--}--%>
        <%--return fileName;--%>
    <%--}--%>
    <%--public String getClearFileName(String fileName){--%>
        <%--fileName = extractFileName(fileName);--%>
        <%--int i = fileName.lastIndexOf(".");--%>
        <%--if(i>=0){--%>
           <%--return fileName.substring(0,i);--%>
        <%--}--%>
        <%--return fileName;--%>
    <%--}--%>
<%--%>--%>