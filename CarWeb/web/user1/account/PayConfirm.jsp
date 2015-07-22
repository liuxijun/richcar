<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.*,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.util.tools.ParamTools,
        java.text.NumberFormat,
        java.text.DecimalFormat,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager,
        com.runway.race.ipm.IpmResult,
        com.runway.race.ipm.Ipm,
        com.runway.race.ipm.IpmImpl"%><%
    //获取影片参数
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);//媒体所在栏目的id
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    long rootChannelID = ParamTools.getLongParameter(request,"rootchannelid",0);
    /*String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
            clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;*/

    //session检查
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId = "";
    //String userInfo ="";
    //String sessionId = "";
    if (su != null){
        userId = su.getUserId();
        //userInfo = su.getUserInfo();
        //sessionId = su.getSessionId();
    }else{
        response.sendRedirect("error.jsp?msg=313");
        return;
    }

    //String clientIp = request.getRemoteAddr();

    String productDesc = "";

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>购买媒体确认</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
    <script src="../../js/changePic.js" language="JavaScript"></script>
<script language="JavaScript">
    function doPayRequest(){
        divWaiting.style.visibility = 'visible';
        form1.submit();
    }
</script>
</head>

<body leftmargin="00" topmargin="0">
<div style="visibility:hidden;POSITION: absolute; HEIGHT: 150px;WIDTH:450px; LEFT: 50px; TOP: 150px" name="divWaiting" id="divWaiting">
<table height="100%" width="100%" border="1" cellPadding="1" cellSpacing="0" borderColor="#a4B6E7">
    <tr>
        <td width="100%" height="100%" bgcolor="white" align="center">请稍候......<img src="../../images/account/biao3.gif"/>
        </td>
    </tr>
</table>
</div>

<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
    <tbody>
        <tr>
            <td height="72"></td>
        </tr>
        <tr>
            <td align="left" valign="top">
            <center>
<%
    String ptype =  ParamTools.getParameter(request,"ptype","");
    //产品id：可能是频道id、媒体id
    long id = ParamTools.getLongParameter(request,"id",-1);
    Icp icp = new Icp();
    icp.setIcpId(icpId);

    float price = 0;//原始价格，以分为单位
    float unit = 100;
    float priceNew = 0;//以元为单位
    String categoryId = "";
    DecimalFormat df = new DecimalFormat("#0.00");//用于数字格式转换

    if(ptype.equals("media")){//购买媒体
        Media media = null;
        MediaIcp mediaIcp = null;
        mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
        media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
        //MediaClip mediaClip = (MediaClip)CacheManager.getInstance().getFromDB("MediaClip", clipId);
        //String fileID = media.getMediaName()+ mediaClip.getMediaclipVolume();
        //Ipm ipm = new IpmImpl();
        //Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", rootChannelID);


        /*IpmResult result = ipm.AAProcess(userInfo, sessionId, channel.getCategoryID(), fileID, "0", "0", clientIp);
        if(result.getResultID() == 314){
            response.sendRedirect("Player.jsp?"+paramString);
            return;
        }*/

        productDesc = "购买点播："+media.getMediaName();
        price = mediaIcp.getMediaPrice();
        priceNew = price /unit;

        if(priceNew <0){
            priceNew = 0;
        }
%>
                <table width="400" class="black">
                    <tr>
                        <td align="left" width="100%" colspan="2">你选择了媒体&nbsp;&nbsp;<font color="blue"><%=media.getMediaName()%></font></td>
                    </tr>
                </table>
                <font color="#990000">
                <table width="400"  class="black" style="bold:true;color:#990000">
                <tr>
                  <td align="right" width="60">价格：</td>
                  <td align="left" width="*"><%=df.format(priceNew)%>(元)</td>
                </tr>
                <tr>
                  <td align="right"  width="60">描述：</td>
                  <td align="left"  width="*"><%=productDesc%></td>
                </tr>
                <tr>
                  <td align="right"  width="60">主角：</td>
                  <td align="left"  width="*"><%=media.getMediaActors()%></td>
                </tr>
                <tr>
                  <td align="right"  width="60">导演：</td>
                  <td align="left"  width="*"><%=media.getMediaDirectors()%></td>
                </tr>
                <tr>
                  <td align="right"  width="60">公司：</td>
                  <td align="left"  width="*"><%=media.getMediaCorporation()%></td>
                </tr>

                </table>
</font>
<%
    }else if(ptype.equals("channel")){//购买频道,已经是根频道
        Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", id);//此id为频道id
        //String categoryId = channel.getCategoryID();//购买栏目标示
        price = channel.getLastPrice();
        priceNew = price /unit;
        if(priceNew<0){
            priceNew = 0;
        }
        productDesc = "购买频道："+channel.getChannelName();
%>
<table width="400"  class="black">
<tr>
  <td align="left" width="100%" colspan="2">你选择了频道&nbsp;&nbsp;<font color="blue"><%=channel.getChannelName()%></font></td>
</tr>
</table>
<table width="400" class="black" style="bold:true;color:#990000">
<tr>
  <td align="right" width="60">价格：</td>
  <td align="left" width="*"><%=df.format(priceNew)%>(元)</td>
</tr>
<tr>
  <td align="right"  width="60">描述：</td>
  <td align="left"  width="*"><%=productDesc%></td>
</tr>
</table>
<%
    }
%>

<table width="400" class="black">
<tr>
  <td align="left" width="100%" colspan="2">购买请按“购买”按钮，取消请按“取消”按钮</td>
</tr>
</table>

<form name="form1" method="post" action="PayMediaRequest_bak.jsp" onsubmit="return form1_onsubmit()">
    <table width="379" border="0" cellpadding="3" cellspacing="0" class="black">
        <tr>
            <td width="163" align="right" valign="middle">
                <a href="javascript:doPayRequest()" ><img name="Image12" border="0" src="../../images/account/001-1.gif" width="58" height="20"/></a>
            </td>
            <td width="36" align="left" valign="top"></td>
            <td width="180" align="left" valign="middle">
                <a href="javascript:history.go(-1)" ><img name="Image13" border="0" src="../../images/account/001-2.gif" width="58" height="20"/></a>
            </td>
        </tr>
    </table>


    <input type="hidden" name="icpid" value="<%=icpId%>">
    <input type="hidden" name="userid" value="<%=userId%>">
    <input type="hidden" name="productdes" value="<%=productDesc%>">
    <input type="hidden" name="amount" value="<%=price%>">
    <input type="hidden" name="type" value="<%=ptype%>">
    <input type="hidden" name="id" value="<%=id%>">
    <input type="hidden" name="category_id" value="<%=categoryId%>">
    <input type="hidden" name="mediaicp_id" value="<%=mediaIcpId%>">
    <input type="hidden" name="clip_id" value="<%=clipId%>">
    <input type="hidden" name="media_id" value="<%=mediaId%>">
    <input type="hidden" name="icp_id" value="<%=icpId%>">
    <input type="hidden" name="channel_id" value="<%=channelId%>">
    <input type="hidden" name="mu" value="<%=mediaUrl%>">
    <input type="hidden" name="service_type" value="<%=serviceType%>">
    <input type="hidden" name="rootchannelid" value="<%=rootChannelID%>">
</form>
</center>


            </td>
        </tr>
    </tbody>
</table>

</body>
</html>