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
        int buyPeriod = 1;//�������ڣ���
        //buyPeriod = product.getTermTimeUnit();

        String transactionID = MainFactory.getTransactionId(spid);
        String isAutoExtend = "1";//�Ƿ��Զ�����
        int sessionTimeout = 3600;

        String payNo="05@000openhe@12935080";
        String productName = "��Ӱ����-��Ʒ";
        String transparentString = payNo;
        //��Ʒ����
        // String serviceItemName = product.getName();//Ӧ���ǽ�Ŀ����

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
            �����ض������Ժ�...<a href="javascript:form1.submit();">submit</a>
        </td>
    </tr>
</table>
<!--�����ض����Ŀ���ַ-->
<form method="post" action="<%=ccitPeriodPay%>" name="form1">

    <!-- ����requestName��requestValue -->
    <input type="hidden" name="SPServiceOrderByPeriodRequest" value="<%=requestValue%>" >
    <!-- �Զ��� ��½ʱ�ķ��ؽӿ� -->
    <input type="hidden" name="return_url" value="<%=returnUrl%>" >
    <input type="hidden" name="sp_id" value="<%=spid%>" >  <!--δ��¼ʱ���ύ��login.jspʱʹ��-->
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