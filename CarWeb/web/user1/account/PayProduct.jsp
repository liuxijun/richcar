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
     * ��ҳ��������ж���
     * һ�Ƕ�Cookie�е���Ϣ��
     * ���Ǹ��ݶ����Ľ����ʾ��ͬ������
     */
    String userInfo = "";
    String sessionId = "";
    String bingindId = "";

    /*  ��ҳ�������֮һ����Cookie�е���Ϣ��userInfo��sessionId��bindingId */
    userInfo  = getCookieValue(request, "userInfo");
    sessionId = getCookieValue(request, "sessionID");
    bingindId = getCookieValue(request, "bindingID");

        String userIP = InetAddress.getLocalHost().getHostAddress();
        userIP = userIP + " from " + request.getRemoteAddr();

	String categoryId = "";

/*
    if (true){
out.println("��¼�ɹ�<br>");
out.println("<br>userInfo:"+userInfo);
out.println("<br>sessionId:"+sessionId);
return;
	}
*/
	        long channelId = ParamTools.getLongParameter(request, "channel_id", -1);
                Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", channelId);//ý���root��Ŀ
                Calendar c = Calendar.getInstance();
                Date startdate = c.getTime();
                c.add(Calendar.DATE, (int) channel.getMonthLength());
                Date enddate = c.getTime();
                categoryId = channel.getCategoryID();
                Ipm ipm = new IpmImpl();
                IpmResult ap = ipm.OrderMonthService(userInfo, sessionId, categoryId, "0", userIP);
                if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5){//�Ʒѳɹ�,���,icp���



                }else{
                    response.sendRedirect("error.jsp?msg="+ ap.getResultID());
                }

%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����ɹ�</title>
<link href="../../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
  <tbody>
        <tr>
          <td height="72">��</td>
        </tr>
  		<tr>
    		<td align="left" valign="top">
					<table width="100%" class="black" height="110" border="0" bordercolor="#111111" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
							  <td align="center" width="100%" valign="middle" height="90">
								  <p><b><font color="#0000FF">��ϲ��������֧���Ѿ��ɹ�!!</font><b></p>
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
        alert("��ϲ��������֧���Ѿ��ɹ�!!");
</script>

</body>
</html>