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
<%@ page import="com.fortune.common.Constants" %>
<%
    //获取影片参数
    String ptype = getParameter(request, "ptype", "");

    DecimalFormat df = new DecimalFormat("#0.00");//用于数字格式转换

     //session检查
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId;
    //String userInfo ="";
    //String sessionId = "";
    if (su != null){
        userId = su.getUserId();
        //userInfo = su.getUserInfo();
        //sessionId = su.getSessionId();
    }else{
        response.sendRedirect("account/error.jsp?msg=313");
        return;
    }

    CacheLogic cacheLogic = new CacheLogic(request);

    Product product = cacheLogic.getProduct(serviceProductId);
    String productDesc = "购买：" + product.getName();

    double price = product.getPrice();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>购买媒体确认</title>
<link href="style/win.css" rel="stylesheet" type="text/css" />
<script src="js/changePic.js" language="JavaScript"></script>
<script language="JavaScript">
    function doPayRequest(){
        divWaiting.style.visibility = 'visible';
        form1.submit();
    }
</script>
</head>

<body leftmargin="00" topmargin="0">
<div style="visibility:hidden;POSITION: absolute; HEIGHT: 150px;WIDTH:450px; LEFT: 25px; TOP: 70px" name="divWaiting" id="divWaiting">
<table height="100%" width="100%" border="1" cellPadding="1" cellSpacing="0" borderColor="#a4B6E7">
    <tr>
        <td width="100%" height="100%" bgcolor="white" align="center">请稍候......<img src="images/account/biao3.gif"/>
        </td>
    </tr>
</table>
</div>

<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="images/account/123.gif">
    <tbody>
        <tr>
            <td height="72"></td>
        </tr>
        <tr>
            <td align="left" valign="top">
            <center>

                <table width="400" class="black">
                    <tr>
                        <td align="left" width="100%" colspan="2">你选择了&nbsp;&nbsp;<font color="blue"><%=product.getName()%></font></td>
                    </tr>
                </table>
                <font color="#990000">
                <table width="400"  class="black" style="bold:true;color:#990000">
                <tr>
                  <td align="right" width="60">价格：</td>
                  <td align="left" width="*"><%=df.format(product.getPrice()/100)%>(元)</td>
                </tr>
                <tr>
                  <td align="right"  width="60">描述：</td>
                  <td align="left"  width="*"><%=productDesc%></td>
                </tr>
                </table>
</font>

<table width="400" class="black">
<tr>
  <td align="left" width="100%" colspan="2">购买请按“购买”按钮，取消请按“取消”按钮</td>
</tr>
</table>                                  

<form name="form1" method="post" action="sx5PayBack.jsp" onsubmit="return form1_onsubmit()">
    <table width="379" border="0" cellpadding="3" cellspacing="0" class="black">
        <tr>
            <td width="163" align="right" valign="middle">
                <a href="javascript:doPayRequest()" ><img name="Image12" border="0" src="images/account/001-1.gif" width="58" height="20"/></a>
            </td>
            <td width="36" align="left" valign="top"></td>
            <td width="180" align="left" valign="middle">
                <a href="javascript:history.go(-1)" ><img name="Image13" border="0" src="images/account/001-2.gif" width="58" height="20"/></a>
            </td>
        </tr>
    </table>


    <%--<input type="hidden" name="icpid" value="<%=icpId%>">--%>
    <input type="hidden" name="spId" value="<%=spId%>">
    <input type="hidden" name="cpId" value="<%=cpId%>">
    <input type="hidden" name="userid" value="<%=userId%>">
    <input type="hidden" name="productdes" value="<%=productDesc%>">
    <input type="hidden" name="price" value="<%=price%>">
    <input type="hidden" name="type" value="<%=ptype%>">
    <%--<input type="hidden" name="category_id" value="<%=categoryId%>">--%>
    <%--<input type="hidden" name="mediaicp_id" value="<%=mediaIcpId%>">--%>
    <%--<input type="hidden" name="clip_id" value="<%=clipId%>">--%>
    <input type="hidden" name="contentId" value="<%=contentId%>">
    <%--<input type="hidden" name="icp_id" value="<%=icpId%>">--%>
    <input type="hidden" name="channelId" value="<%=channelId%>">
    <input type="hidden" name="contentPropertyId" value="<%=contentPropertyId%>">
    <%--<input type="text" name="mu" value="<%=mediaUrl%>">--%>
    <input type="hidden" name="serviceProductId" value="<%=serviceProductId%>">
    <%--<input type="hidden" name="service_type" value="<%=serviceType%>">--%>
    <%--<input type="hidden" name="rootchannelid" value="<%=channelId%>">--%>
</form>
</center>
            </td>
        </tr>
    </tbody>
</table>

</body>
</html>