<%@ page import="com.huawei.itellin.spapi.service.BSSPUserInfo" %>
<%@ page import="com.huawei.itellin.spapi.service.MainFactory" %>
<%@ page import="com.huawei.itellin.spapi.service.SSOValidToken" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html; charset=gb2312" pageEncoding="gb2312" %>
<%@include file="param.jsp"%>
<%

    try{
        Object object= request.getSession().getAttribute("userId");
        if(object==null){
            response.sendRedirect("checklogin.jsp");
            return;
        }

        String payNo = "05@000openhe@40981585";//ý��㲥
        String returnUrl = "http://61.55.144.213/user/test/result.jsp?flag=onepay";

        String subjectName ="ý��";

        String serviceName = subjectName;
        String serviceItemName = subjectName;
        int fee = 50;
        String transactionID = MainFactory.getTransactionId(spid);
        int sessionTimeout = 3600;
        String transparentString = payNo;
        String requestValue = MainFactory.generateSPItemRequestValue(spid, payNo, serviceName, serviceItemName,
                transactionID, fee, sessionTimeout, returnUrl, transparentString,"0", BCG_CERT, SP_PRIVATE_KEY);
%>

<HTML>
<HEAD>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <META name="GENERATOR" content="IBM WebSphere Studio">
    <META http-equiv="Content-Style-Type" content="text/css">
    <LINK href="theme/Master.css" rel="stylesheet" type="text/css">
    <TITLE>onepay.jsp</TITLE>
</HEAD>
<BODY>
<table>
    <tr>
        <td>
            ���Ժ�...
        </td>
    </tr>
</table>
<!--�����ض����Ŀ���ַ-->
<form method="post" action="<%=ccitOnePay%>" name="form1">
    <!-- ����requestName��requestValue -->
    <input type="hidden" name="SPItemRequest" value="<%=requestValue%>" >
    <!-- �Զ��� ��½ʱ�ķ��ؽӿ� -->
    <input type="hidden" name="return_url" value="<%=returnUrl%>" >
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