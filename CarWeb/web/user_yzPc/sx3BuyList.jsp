<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.product.model.ServiceProduct" %>
<%@ page import="com.fortune.rms.business.product.model.Product" %>
<%@ page import="java.util.List" %>
<%@include file="param.jsp"%>
<%@include file="queryString.jsp"%>
<%@page import="java.util.*,java.text.DecimalFormat"%>
<%@ page import="com.fortune.rms.business.user.SessionUser" %>
<%@ page import="com.fortune.common.Constants" %><%
    //获取影片参数
     //session检查
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    if (su == null){
        response.sendRedirect("account/error.jsp?msg=313");
        return;
    }

    CacheLogic cacheLogic= new CacheLogic(request);
    List serviceProducts = cacheLogic.getAllServiceProduct(contentId, spId);

    DecimalFormat df = new DecimalFormat("#0.00");//用于数字格式转换

    int PPVDuration = 24;
%>
<html>
<head>
    <title>媒体购买</title>
    <link href="css/win.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"><style type="text/css">
<!--
a:link {
	color: #FF0000;
}
a:visited {
	color: #FF0000;
}
a:hover {
	color: #FF0000;
}
a:active {
	color: #FF0000;
}
-->
</style></head>
<body leftmargin="00" topmargin="0" onLoad="window.resizeTo(510,360)" >
<table width="500" height="305" border="0" cellpadding="0" cellspacing="0" >
    <tr>
        <td align="center" background="images/account/123.gif">
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
                                                    if (serviceProducts != null && serviceProducts.size() != 0) {
                                                        for (Object serviceProduct1 : serviceProducts) {
                                                            ServiceProduct serviceProduct = (ServiceProduct) serviceProduct1;
                                                            Product product = cacheLogic.getProduct(serviceProduct.getId());
//                                                            Content content = cacheLogic.getContent(contentId);
                                                %>
                                                <table align="center" width="400" class="black">
                                                    <tr>
                                                        <td align="left" colspan="2">
                                                            <%if (serviceProduct.getType().intValue() == 2) { %>
                                                            <strong>您可以购买单次点播</strong>(有效期：<b><%=PPVDuration%>小时</b>)：
                                                            <% } %>
                                                            <%if (serviceProduct.getType().intValue() == 1) { %>
                                                            <strong>您可以购买包月</strong>(有效期：<b>一个月</b>)：
                                                            <% } %>
                                                            <%if (serviceProduct.getType().intValue() == 3) { %>
                                                            <strong>您可以购买礼品包</strong>(有效期：<b><%=PPVDuration%>小时</b>)：
                                                            <% } %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="300"><img src="images/account/unit1.gif"/>
                                                            &nbsp;节目名称：&nbsp; <%=serviceProduct.getName()%>
                                                            &nbsp;<%=df.format(product.getPrice() / 100)%>元
                                                        </td>
                                                        <td width="100">
                                                            <%if (serviceProduct.getType().intValue() == 1) {%>
                                                            <a href="sx4PayConfirm.jsp?<%=queryStr%>&serviceProductId=<%=serviceProduct.getId()%>&ptype=channel"><b><%--<font color="red">点播</font>--%><img
                                                                    border="0" src="images/account/001-1.gif" width="58"
                                                                    height="20"></b></a>
                                                            <% } else { %>
                                                            <a href="sx4PayConfirm.jsp?<%=queryStr%>&serviceProductId=<%=serviceProduct.getId()%>&ptype=media"><b><%--<font color="red">点播</font>--%><img
                                                                    border="0" src="images/account/001-1.gif" width="58"
                                                                    height="20"></b></a>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <%
                                                    }%>
                                                 <table width="400" class="black">
                                                    <tr>
                                                        <td width="400" colspan="2">
                                                            <font color="#FF0000">
                                                                <b>提示：</b></font>本站的包月订购方式为自动续订，如果您在订购当月最后一天22:00时前没有在本站用户服务中心进行本频道的包月退订操作（详见<a
                                                                target="_blank"
                                                                href="http://www.kdsj2.sx.cn/100/help/help-tdff.htm">用户帮助</a>），系统将在次月为您自动续订。
                                                        </td>

                                                    </tr>
                                                </table>
                                                <%
                                                } else {
                                                %>
                                                <table align="center" width="400"  class="black">
                                                    <tr>
                                                        <td width="100%">对不起，系统出现意外情况，暂时不能提供该节目的点播服务。</td>
                                                    </tr>
                                                    <tr>
                                                        <td width="100%" align="center"><a href="javascript:window.close();">关闭</a></td>
                                                    </tr>
                                                </table>
                                                <%}%>
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
</table>
</body>
</html>
