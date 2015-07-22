<%@ page import="com.fortune.rms.business.content.logic.logicInterface.*,
com.fortune.rms.business.content.model.Content,
com.fortune.util.*,java.util.List" %><%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*"%><%
    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    long contentId = StringUtils.string2long(request.getParameter("contentId"),-1);
    Content content = null;
    if(contentId>0){
        content = contentLogic.getCachedContent(contentId);
        ContentRelatedLogicInterface relatedLogic = (ContentRelatedLogicInterface) SpringUtils.getBean("contentRelatedLogicInterface",session.getServletContext());
    }
    if(content==null){
        content = new Content();
        content.setName("");
    }
    String playFile = "rms.jsp?"+request.getQueryString();
%><html>
            <head>
                <meta name="GENERATOR" content="Media Stack XSLT Editor"/>
                <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
                <link href="../css/Style.css" rel="stylesheet"/>
                <script type="text/javascript" src="../../js/jquery.js"></script>
                <script src="../js/cookies.js"></script>
                <script src="../js/playerCommonNew.js"></script>
                <title>ÕýÔÚ²¥·Å--<%=content.getName()%></title>
                        <script src="../js/playerMedia.js"></script>
                        <SCRIPT>
                            HeightBorder = 130;
                            WidthBorder  = 12;
                        </SCRIPT>
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
                            var newUrl = "reportError.jsp?<%=request.getQueryString()%>";
                            window.open(newUrl,"_blank","width=555,height=390,resizable=yes");
                        }
                    }
                    function fullScreen(){
                       changeDisplaySize(3);
                    }
                    var playStartTime = null;
                    function startPlay(){
                        playStartTime = new Date();
                        window.setTimeout("validTimes()",30000);
                    }
                    function validTimes(){
                        var currentUrl =  document.location.search;
                        var i = currentUrl.indexOf("?");
                        if(i>=0&&i<currentUrl.length-1){
                            currentUrl = currentUrl.substring(i+1);
                        }
                        var noticeUrl = "../noticeRunway.jsp?"+currentUrl;
                        jQuery.getJSON(noticeUrl,
                        {},function(jsonData){

                        });
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
                                <img onclick="startReportError()" src="../../images/player/kdsj.gif" width="145" height="27"/>
                            </td>
                            <td width="*" background="../../images/player/line_bg.gif" height="3">¡¡</td>

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
                                <tr height="*">
                                    <td colspan="3">
                                        <object id="program" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2" type="application/x-oleobject" height="100%" standby="Loading Microsoft Windows Media Player components..." width="100%" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">
                                            <param name="uiaMode" value="none"/>
                                            <param name="AutoStart" value="false"/>
                                            <param name="AUTOSIZE" value="true"/>
                                            <param name="stretchToFit" value="true"/>
                                            <PARAM NAME="URL" value="<%=playFile%>"/>
                                        </object>
                                    </td>
                                </tr>
                    </tbody>
                </table>

            </body>
        </html>
<%!
    public String extractFileName(String fileName){
        int i= fileName.lastIndexOf("/");
        if(i>=0){
            fileName = fileName.substring(i+1);
        }
        return fileName;
    }
    public String getClearFileName(String fileName){
        fileName = extractFileName(fileName);
        int i = fileName.lastIndexOf(".");
        if(i>=0){
           return fileName.substring(0,i);
        }
        return fileName;
    }
%>