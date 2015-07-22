<%@ page contentType="text/html; charset=gbk" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.*,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.mediastack.user.*,
        java.text.DecimalFormat,
        cn.sh.guanghua.cache.CacheManager"%><%
    //获取影片参数
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    //Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channelId);
    long rootChannelID = ParamTools.getLongParameter(request,"rootchannelid",0);
    String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
            clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;

    long userAccountId = ParamTools.getLongParameter(request,"useraccount_id",-1);
    if (userAccountId != -1){
        paramString = paramString + "&useraccount_id=" + userAccountId;
    }
    //session检查
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId = "";
    if (su != null){
        userId = su.getUserId();
    }else{
        response.sendRedirect("error.jsp?msg=313");
        return;
    }

    //检查用户是否有权限观看
    if(!PlayLogic.checkPlayRight(userId,mediaId,channelId,icpId)){
      response.sendRedirect("error.jsp?msg=97");
      return;
    }

    Media media = null;
    MediaIcp mediaIcp = null;
    float price = 0;//原始价格，以分为单位
    float unit = 100;
    float priceNew = 0;//以元为单位
	String mediaName = "";
    DecimalFormat df = new DecimalFormat("#0.00");//用于数字格式转换
    Channel rootChannel = null;//当前栏目的根频道,目前只判断三级
    try{
        mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
        media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
        //媒体的当前频道
        rootChannel = (Channel)CacheManager.getInstance().getFromDB("Channel", rootChannelID);
        mediaName = media.getMediaName();
    }catch(Exception e){
        e.printStackTrace();
    }
    int PPVDuration = 24;
%>
<html>
<head>
    <title>媒体购买</title>
    <link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>
<script>
    function jumpBillHistory(){
       var OpenedWin = window.open("ViewBillingHistory.jsp","Billing","top=10,left=80,width=750,height=600,scrollbar=true");
    }
    function checkAccount(){
       var currentURL = window.location.href;
       var newURL = "CheckAccount.jsp";
       var posOf = currentURL.indexOf("?");
       if(posOf >=0){
          newURL = newURL + currentURL.substring(posOf);
       }
       window.location.href = newURL;
    }
    function jumpActivateCard(){
       var OpenedWin = window.open("CardInfo.html","Card","top=200,left=200,width=300,height=200,scrollbar=true");
       OpenedWin.opener = window;
    }
    function jumpToPack(packId,mediaIcpId){
       var OpenedWin = window.open("PackInfo.jsp?WebService=true&pack_id="+packId+"&mediaicp_id="+mediaIcpId,"History","top=0,left=150,width=650,height=600,scrolling=YES");
    }
    function jumpToHelp(){
       var OpenedWin = window.open("HowToBuy.html","Help","top=00,left=120,width=559,height=394,scrollbar=true");
    }
</script>
<body leftmargin="00" topmargin="0" onload="window.resizeTo(510,305)" >
<table width="500" height="305" border="0" cellpadding="0" cellspacing="0" >
    <tr>
        <td align="center" background="../../images/account/123.gif">
            <table width="500" height="290" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="72"></td>
                </tr>
                <tr>
                    <td align="center" valign="top">
                        <table width="450"  border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="20"></td>
                                <td width="400" align="left" valign="middle">
                                    <table width="400" height="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#D0E22C" class="black">
                                        <tr>
                                            <td bgcolor="#FFFFFF"><strong>请选择您的购买方式</strong></td>
                                        </tr>
                                        <tr>
                                            <td bgcolor="#FFFFFF">
                                                <%
                                                    if(media!=null){
							if(mediaIcp.getMediaPrice()!=9900){
                                                %>
                                                <table align="center" width="400"  class="black">
                                                    <tr>
                                                        <td align="left" colspan="2"><strong>您可以购买该节目的点播</strong>(有效期：<b><%=PPVDuration%>小时</b>)：</td>
                                                    </tr>
                                                <%
                                                        price = mediaIcp.getMediaPrice();
                                                        priceNew = price /unit;
                                                %>
                                                    <tr>
                                                        <td width="300"><img src="../../images/account/unit1.gif"/>&nbsp;节目名称：&nbsp;<%=mediaName%>&nbsp;<%=df.format(priceNew)%>元/次/部(集)</td>

                                                        <td width="100"><a href="PayConfirm.jsp?WebService=true&ptype=media&rootchannelid=<%=rootChannelID%>&id=<%=media.getMediaId()%>&<%=paramString%>"><b><%--<font color="red">点播</font>--%><img border="0" src="../../images/account/001-3.gif" width="58" height="20"></b></td>
                                                    </tr>
                                                </table>
                                                <%
							}
                                                    }else{
                                                %>
                                                <table align="center" width="400"  class="black">
                                                    <tr>
                                                        <td width="100%">对不起，系统出现意外情况，暂时不能提供该节目的点播服务。</td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="center"><a href="javascript:window.close();">关闭</a></td>
                                                    </tr>
                                                </table>
                                                <%
                                                    }
                                                    //频道列表
                                                    if((rootChannel != null) && (!"wyzq".equals(rootChannel.getCategoryID())) && (rootChannel.getLastPrice()>0)){
                                                %>
                                                <table width="400" class="black">
                                                    <tr>
                                                        <td colspan="2"><strong>您还可购买该节目所属的频道包月：</strong></td>
                                                    </tr>
                                                <%
                                                        price = rootChannel.getLastPrice();
                                                        priceNew = price /unit;
                                                %>
                                                    <tr>
                                                        <td width="300">
                                                            <img src="../../images/account/unit1.gif"/>&nbsp;频道名称：&nbsp;<%=rootChannel.getChannelName()%>频道&nbsp;&nbsp;<%=df.format(priceNew)%>元
                                                        </td>

                                                        <td width="100"><a href="PayConfirm.jsp?WebService=true&ptype=channel&id=<%=rootChannel.getChannelId()%>&<%=paramString%>"><%--<b><font color="red">频道订购</font></b>--%><img border="0" src="../../images/account/001-4.gif" width="83" height="20"></a></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="400" colspan="2" height="20">
                                                            　</td>

                                                    </tr>

                                                    <tr>
                                                        <td width="400" colspan="2">
                                                            <font color="#FF0000">
															<b>提示：</b></font>本站频道的包月订购方式为自动续订，如果您在订购当月最后一天22:00时前没有在本站用户服务中心进行本频道的包月退订操作（详见<a target="_blank" href="http://www.kdsj2.sx.cn/100/help/help-tdff.htm">用户帮助</a>），系统将在次月为您自动续订。
                                                        </td>

                                                    </tr>

                                                </table>
                                                <%
						    }
                                                %>
                                            </td>
                                        </tr>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<p class="black"></p>
</body>
</html>
