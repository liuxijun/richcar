<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.*,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.htmlcreator.CreatorHelper,
        cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.cache.CacheManager"%><%
    //��ȡӰƬ����
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    //Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channelId);
    //String categoryId = channel.getCategoryID();
    String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&media_id="+ mediaId +"&clip_id=" +
            clipId + "&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;

    //session���
    String mediaSession = (String)session.getAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId);
    if (!("true".equals(mediaSession))){
        response.sendRedirect("error.jsp?msg=313");
        return;
    }
    //��ȡӰƬ�����ļ�
    String playFile = ConfigManager.getConfig().node("player").get("playfile","MediaStack.jsp");
    playFile = playFile + "?"+paramString;


    long mediaForm = Constants.MEDIA_FORMAT_TYPE_MICROSOFT;
     Media  media = null;//
     if (serviceType == Constants.SERVICE_TYPE_LIVING){
         mediaForm = Constants.MEDIA_FORMAT_TYPE_MICROSOFT;
         media = new Media();
         media.setMediaName("ֱ����Ŀ.....");
     }else{
        media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);
        mediaForm = media.getMediaFormattype();
     }
 %>        <html>
            <head>
                <meta name="GENERATOR" content="Media Stack XSLT Editor"/>
                <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
                <link href="../../css/Style.css" rel="stylesheet"/>
                <script src="../../js/cookies.js"></script>
                <script src="../../js/playerCommonNew.js"></script>
                <title>���ڲ���--<%=media.getMediaName()%></title>
                    <%if (mediaForm == Constants.MEDIA_FORMAT_TYPE_REAL){%>
                        <script src="../../js/playerReal.js"></script>
                        <SCRIPT>
                            HeightBorder = 125;
                            WidthBorder  = 10;
                        </SCRIPT>
                    <%}else{%>
                        <script src="../../js/playerMedia.js"></script>
                        <SCRIPT>
                            HeightBorder = 130;
                            WidthBorder  = 12;
                        </SCRIPT>
                    <%}%>
                <script>
                    SetPosStart  = true;
                    var trackball = null;
                    var statsInfo = null;
                    function orgSize(){
                       changeDisplaySize(1);
                    }
                    function doubleSize(){
                       changeDisplaySize(2);
                    }

                    function fullScreen(){
                       changeDisplaySize(3);
                    }
                </script>
            </head>
            <body bgcolor="#FFFFFF" onload="javascript:expand()" leftmargin="0" topmargin="0">
                <table border="0" width="100%" id="table1" height="100%" bgcolor="#FFFFFF" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr height="15">

                            <td width="66" height="3">
                                <img src="../../images/player/kdsj.gif" width="145" height="27"/>
                            </td>
                            <td width="*" background="../../images/player/line_bg.gif" height="3">��</td>

                            <td width="63" background="../../images/player/line_bg.gif" height="3">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tbody>
                                        <tr>
                                            <td width="21%" height="26">
                                                <img name="Image5" border="0" src="../../images/player/1x_gray.gif" width="20" height="27"
                                                    onclick="orgSize();"
                                                    onMouseOut="show_n(this)" onMouseOver="show_o(this)"/>
                                            </td>
                                            <td width="54%" height="26">
                                                <img name="Image5" border="0" src="../../images/player/2x_gray.gif" width="20" height="27"
                                                    onclick="doubleSize();"
                                                    onMouseOut="show_n(this)" onMouseOver="show_o(this)"/>
                                            </td>
                                            <td width="25%" height="26">
                                                <img name="Image5" border="0" src="../../images/player/da_gray.gif" width="23" height="27"
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
                                            <PARAM NAME="SRC" value="<%=playFile%>"></PARAM>
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
                                            <PARAM NAME="URL" value="<%=playFile%>"></PARAM>
                                        </object>
                                    </td>
                                </tr>
                    <%}%>
                    </tbody>
                </table>
            </body>
        </html>
