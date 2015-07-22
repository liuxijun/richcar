<%@ page
contentType="text/html; charset=GBK" %><%@ page
import="cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        java.text.SimpleDateFormat,
        java.util.Date,java.util.Calendar,
        java.util.List,
        cn.sh.guanghua.util.tools.StringTools,
        java.sql.Connection,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager,
        java.util.HashMap,
        java.net.InetAddress,
        com.runway.race.ipm.IpmResult,
        com.runway.race.ipm.Ipm,
        com.runway.race.ipm.IpmImpl"%><%@ include file="./cookies.inc" %>


<%

        SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);


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

        String userIP = InetAddress.getLocalHost().getHostAddress();
        userIP = userIP + " from " + request.getRemoteAddr();

	String categoryId = "";

/*
    if (true){
out.println("登录成功<br>");
out.println("<br>userInfo:"+userInfo);
out.println("<br>sessionId:"+sessionId);
return;
	}
*/
	        long channelId = ParamTools.getLongParameter(request, "channel_id", -1);
                Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", channelId);//媒体的root栏目
                Calendar c = Calendar.getInstance();
                Date startdate = c.getTime();
                c.add(Calendar.DATE, (int) channel.getMonthLength());
                Date enddate = c.getTime();
                categoryId = channel.getCategoryID();
                Ipm ipm = new IpmImpl();
                IpmResult ap = ipm.OrderMonthService(userInfo, sessionId, categoryId, "0", userIP);
                if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5){//计费成功,免费,icp免费



                }else{
                    response.sendRedirect("error.jsp?msg="+ ap.getResultID());
                }

%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>购买成功</title>
<link href="../../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
  <tbody>
        <tr>
          <td height="72">　</td>
        </tr>
  		<tr>
    		<td align="left" valign="top">
					<table width="100%" class="black" height="110" border="0" bordercolor="#111111" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
							  <td align="center" width="100%" valign="middle" height="90">
								  <p><b><font color="#0000FF">恭喜您，您的支付已经成功!!</font><b></p>
  							  </td>
							</tr>
                            </tr>
						</tbody>
					</table>
			</td>
		</tr>
	</tbody>
</table>
<script language="javascript">
        alert("恭喜您，您的支付已经成功!!");
</script>

</body>
</html>