`<%@ page import="com.huawei.itellin.spapi.service.MainFactory"%>
`<%@ page import="com.huawei.itellin.spapi.util.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html; charset=gb2312" %>
<%@include file="param.jsp"%>
<%@include file="queryString.jsp"%>
<%
    try{
        String transparentString = "";
        String returnURL = getParameter(request,"return_url","");
        returnURL = rmsUrl+"1login.jsp?"+queryStr;

        String requestValue = MainFactory.generateSPTokenRequestValue(platformId, returnURL, transparentString, BCG_CERT, SP_PRIVATE_KEY);
%>

<HTML>
<HEAD>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
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
<form method="post" action="<%=ccitLogin%>" name="form1">

    <!-- ����requestName��requestValue -->
    <input type="hidden" name="SPTokenRequest" value="<%=requestValue%>" >
</form>

<script language="JavaScript" >
    document.form1.submit();
</script>
</BODY>
</HTML>

<%
    }catch(Exception e){
        e.printStackTrace();
    }
%>