<%@ page contentType="text/html; charset=GB2312" %><%@ page
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
        cn.sh.guanghua.util.tools.StringTools,
        java.sql.Connection,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager,
        java.util.HashMap,
        java.net.InetAddress"%>
 <%@include file="param.jsp"%>
 <%    //获取影片参数
    String nextUrl = "";
    try{
            String ptype = ParamTools.getParameter(request, "ptype", "");

            if (ptype.equals("garden")) {//购买直播包月频道
                nextUrl = "../garden/player.jsp";
            }

            if (serviceType==Constants.SERVICE_TYPE_DRM){
                String cid = (String)session.getAttribute("DrmCid");
                String returnDrm = (String)session.getAttribute("DrmReturn");
                if(cid!=null&&!"".equals(cid)){
                    response.sendRedirect(PlayLogic.getDrmSuccessReturnUrl(returnDrm,Constants.PLAY_VALID_TIME));
                    return;
                }
            }
            if (nextUrl.equals("")){
                session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
                String playUrl[] = new String[]{"","Player.jsp?"+paramString,"Download.jsp?"+paramString,"Player.jsp?"+paramString,""};
                nextUrl = playUrl[(int)serviceType];
            }

    }catch(Exception e){
        session.setAttribute("ERROR", e.toString());
        e.printStackTrace();
        response.sendRedirect("error.jsp?msg=unexpect_error");
        return;
    }
%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>购买成功</title>
<link href="../../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/b.gif">
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
        location.replace('<%=nextUrl%>');
        document.location.href = '<%=nextUrl%>';
</script>

</body>
</html>
