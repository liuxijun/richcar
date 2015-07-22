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
    //��ȡӰƬ����
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
    //session���
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId = "";
    if (su != null){
        userId = su.getUserId();
    }else{
        response.sendRedirect("error.jsp?msg=313");
        return;
    }

    //����û��Ƿ���Ȩ�޹ۿ�
    if(!PlayLogic.checkPlayRight(userId,mediaId,channelId,icpId)){
      response.sendRedirect("error.jsp?msg=97");
      return;
    }

    Media media = null;
    MediaIcp mediaIcp = null;
    float price = 0;//ԭʼ�۸��Է�Ϊ��λ
    float unit = 100;
    float priceNew = 0;//��ԪΪ��λ
	String mediaName = "";
    DecimalFormat df = new DecimalFormat("#0.00");//�������ָ�ʽת��
    Channel rootChannel = null;//��ǰ��Ŀ�ĸ�Ƶ��,Ŀǰֻ�ж�����
    try{
        mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
        media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
        //ý��ĵ�ǰƵ��
        rootChannel = (Channel)CacheManager.getInstance().getFromDB("Channel", rootChannelID);
        mediaName = media.getMediaName();
    }catch(Exception e){
        e.printStackTrace();
    }
    int PPVDuration = 24;
%>
<html>
<head>
    <title>ý�幺��</title>
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
                                            <td bgcolor="#FFFFFF"><strong>��ѡ�����Ĺ���ʽ</strong></td>
                                        </tr>
                                        <tr>
                                            <td bgcolor="#FFFFFF">
                                                <%
                                                    if(media!=null){
							if(mediaIcp.getMediaPrice()!=9900){
                                                %>
                                                <table align="center" width="400"  class="black">
                                                    <tr>
                                                        <td align="left" colspan="2"><strong>�����Թ���ý�Ŀ�ĵ㲥</strong>(��Ч�ڣ�<b><%=PPVDuration%>Сʱ</b>)��</td>
                                                    </tr>
                                                <%
                                                        price = mediaIcp.getMediaPrice();
                                                        priceNew = price /unit;
                                                %>
                                                    <tr>
                                                        <td width="300"><img src="../../images/account/unit1.gif"/>&nbsp;��Ŀ���ƣ�&nbsp;<%=mediaName%>&nbsp;<%=df.format(priceNew)%>Ԫ/��/��(��)</td>

                                                        <td width="100"><a href="PayConfirm.jsp?WebService=true&ptype=media&rootchannelid=<%=rootChannelID%>&id=<%=media.getMediaId()%>&<%=paramString%>"><b><%--<font color="red">�㲥</font>--%><img border="0" src="../../images/account/001-3.gif" width="58" height="20"></b></td>
                                                    </tr>
                                                </table>
                                                <%
							}
                                                    }else{
                                                %>
                                                <table align="center" width="400"  class="black">
                                                    <tr>
                                                        <td width="100%">�Բ���ϵͳ���������������ʱ�����ṩ�ý�Ŀ�ĵ㲥����</td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="center"><a href="javascript:window.close();">�ر�</a></td>
                                                    </tr>
                                                </table>
                                                <%
                                                    }
                                                    //Ƶ���б�
                                                    if((rootChannel != null) && (!"wyzq".equals(rootChannel.getCategoryID())) && (rootChannel.getLastPrice()>0)){
                                                %>
                                                <table width="400" class="black">
                                                    <tr>
                                                        <td colspan="2"><strong>�����ɹ���ý�Ŀ������Ƶ�����£�</strong></td>
                                                    </tr>
                                                <%
                                                        price = rootChannel.getLastPrice();
                                                        priceNew = price /unit;
                                                %>
                                                    <tr>
                                                        <td width="300">
                                                            <img src="../../images/account/unit1.gif"/>&nbsp;Ƶ�����ƣ�&nbsp;<%=rootChannel.getChannelName()%>Ƶ��&nbsp;&nbsp;<%=df.format(priceNew)%>Ԫ
                                                        </td>

                                                        <td width="100"><a href="PayConfirm.jsp?WebService=true&ptype=channel&id=<%=rootChannel.getChannelId()%>&<%=paramString%>"><%--<b><font color="red">Ƶ������</font></b>--%><img border="0" src="../../images/account/001-4.gif" width="83" height="20"></a></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="400" colspan="2" height="20">
                                                            ��</td>

                                                    </tr>

                                                    <tr>
                                                        <td width="400" colspan="2">
                                                            <font color="#FF0000">
															<b>��ʾ��</b></font>��վƵ���İ��¶�����ʽΪ�Զ�������������ڶ����������һ��22:00ʱǰû���ڱ�վ�û��������Ľ��б�Ƶ���İ����˶����������<a target="_blank" href="http://www.kdsj2.sx.cn/100/help/help-tdff.htm">�û�����</a>����ϵͳ���ڴ���Ϊ���Զ�������
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
