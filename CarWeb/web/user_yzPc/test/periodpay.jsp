<%@ page import="com.huawei.itellin.spapi.service.BSSPUserInfo" %>
<%@ page import="com.huawei.itellin.spapi.service.MainFactory" %>
<%@ page import="com.huawei.itellin.spapi.service.SSOValidToken" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=gb2312" %>
<%@include file="param.jsp"%>
<%
    try{

        Object object= request.getSession().getAttribute("userId");
        if(object==null){
            response.sendRedirect("checklogin.jsp");
            return;
        }
        String returnUrl = "http://61.55.144.213/user/test/result.jsp?flag=periodpay";

        int fee = 2;

        String startTime = StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,1);
        String endTime = StringUtils.date2string(calendar.getTime(),"yyyyMMddHHmmss");
        int buyPeriod = 1;//订购周期，月
        //buyPeriod = product.getTermTimeUnit();

        String transactionID = MainFactory.getTransactionId(spid);
        String isAutoExtend = "1";//是否自动续订
        int sessionTimeout = 3600;

        String payNo="05@000openhe@12935080";
        String productName = "电影世界-产品";
        String transparentString = payNo;
        //产品名称
        // String serviceItemName = product.getName();//应该是节目名称

        String requestValue = MainFactory.generateSPServiceOrderByPeriodRequestValue(spid, payNo, productName, fee, startTime,
                endTime, buyPeriod, transactionID, isAutoExtend, returnUrl, transparentString,"0", BCG_CERT, SP_PRIVATE_KEY);
%>
<HTML>
<HEAD>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <META name="GENERATOR" content="IBM WebSphere Studio">
    <META http-equiv="Content-Style-Type" content="text/css">

    <TITLE>requestvaluetobcg.jsp</TITLE>
</HEAD>
<BODY>
<table>
    <tr>
        <td>
            正在重定向，请稍候...<a href="javascript:form1.submit();">submit</a>
        </td>
    </tr>
</table>
<!--设置重定向的目标地址-->
<form method="post" action="<%=ccitPeriodPay%>" name="form1">

    <!-- 设置requestName和requestValue -->
    <input type="hidden" name="SPServiceOrderByPeriodRequest" value="<%=requestValue%>" >
    <!-- 自定义 登陆时的返回接口 -->
    <input type="hidden" name="return_url" value="<%=returnUrl%>" >
    <input type="hidden" name="sp_id" value="<%=spid%>" >  <!--未登录时，提交到login.jsp时使用-->
</form>
<script language="JavaScript" >
    document.form1.submit();
</script>
<%

    }catch(Exception e){
        e.printStackTrace();
    }
%>
</BODY>
</HTML>