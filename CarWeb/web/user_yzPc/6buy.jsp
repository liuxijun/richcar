<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.product.model.ServiceProduct" %>
<%@ page import="com.fortune.rms.business.product.model.Product" %>
<%@ page import="com.huawei.itellin.spapi.service.MainFactory" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@include file="param.jsp"%>
<%@include file="queryString.jsp"%>
<%
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
        response.sendRedirect("1login.jsp?"+queryStr);
        return;
    }

    CacheLogic cacheLogic= new CacheLogic(request);
    Content content = cacheLogic.getContent(contentId);

    String returnUrl = rmsUrl+"7buyBack.jsp?"+buyQueryStr;

    Product product = cacheLogic.getProduct(serviceProductId);
    ServiceProduct serviceProduct  = cacheLogic.getServiceProduct(serviceProductId);


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

<%
    if (productType==1){//����


        int fee = product.getPrice().intValue()*100;

        String startTime = StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,1);
        String endTime = StringUtils.date2string(calendar.getTime(),"yyyyMMddHHmmss");
        int buyPeriod = 1;//�������ڣ���
        //buyPeriod = product.getTermTimeUnit();

        String transactionID = MainFactory.getTransactionId(platformId);
        String isAutoExtend = product.getAutoPay().toString();//�Ƿ��Զ�����
        int sessionTimeout = 3600;

        String payProductNo = product.getPayProductNo();
       // String payProductNo = productIdss;
//        String productName = product.getName();
        String productName = serviceProduct.getName();
        String transparentString = payProductNo;
        //��Ʒ����
        // String serviceItemName = product.getName();//Ӧ���ǽ�Ŀ����

//        String requestValue = MainFactory.generateSPServiceOrderByPeriodRequestValue(platformId, payProductNo, productName, fee, startTime,endTime, buyPeriod, transactionID, isAutoExtend, returnUrl, transparentString, BCG_CERT, SP_PRIVATE_KEY);
           logger.debug("buy value->platformId:"+platformId+
                "\npayProductNo��"+payProductNo+"" +
                   "\nproductName:"+productName+
                   "\nfee:"+fee+"" +
                   "\nstartTime:"+startTime+"" +
                   "\nendTime:"+endTime+"" +
                   "\nbuyPeriod:" +buyPeriod+"" +
                   "\ntransactionID:" +transactionID+"" +
                   "\nisAutoExtend:" +isAutoExtend+"" +
                   "\nreturnUrl:" +returnUrl+"" +
                   "\ntransparentString:" +transparentString+"" +
                   "\nBCG_CERT:" +BCG_CERT+"" +
                   "\nSP_PRIVATE_KEY:"+SP_PRIVATE_KEY+"");
          String requestValue = MainFactory.generateSPServiceOrderByPeriodRequestValue(platformId,payProductNo,productName,fee,startTime,endTime,buyPeriod,transactionID,isAutoExtend,returnUrl,transparentString,"0",BCG_CERT, SP_PRIVATE_KEY);
%>

    <!--�����ض����Ŀ���ַ-->
    <form method="post" action="<%=ccitPeriodPay%>" name="form1">
        <!-- ����requestName��requestValue -->
        <input type="hidden" name="SPServiceOrderByPeriodRequest" value="<%=requestValue%>" >
        <!-- �Զ��� ��½ʱ�ķ��ؽӿ� -->
        <input type="hidden" name="return_url" value="<%=returnUrl%>" >
        <input type="hidden" name="sp_id" value="<%=platformId%>" >  <!--δ��¼ʱ���ύ��login.jspʱʹ��-->
    </form>
<%  }    %>

<%
    if (productType==2){//����

//       String subjectName = content.getName();
        String subjectName = serviceProduct.getName();
        String serviceName = subjectName;
        String serviceItemName = subjectName;
        String payProductNo = product.getPayProductNo();
//        String payProductNo = productIdss;
        int fee = (int) (product.getPrice()*100);
//        fee=150;
        String transactionID = MainFactory.getTransactionId(platformId);
        int sessionTimeout = 3600;
        String transparentString = payProductNo;

        String requestValue = MainFactory.generateSPItemRequestValue(platformId, payProductNo, serviceName, serviceItemName,
                transactionID, fee, sessionTimeout, returnUrl, transparentString,"0",BCG_CERT, SP_PRIVATE_KEY);

%>

    <!--�����ض����Ŀ���ַ-->
    <form method="post" action="<%=ccitOnePay%>" name="form1">
        <!-- ����requestName��requestValue -->
        <input type="hidden" name="SPItemRequest" value="<%=requestValue%>" >
        <!-- �Զ��� ��½ʱ�ķ��ؽӿ� -->
        <input type="hidden" name="return_url" value="<%=returnUrl%>&pageType=0" >

    </form>

<%  }    %>


<script language="JavaScript" >
    document.form1.submit();
</script>
</BODY>
</HTML>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.6buy.jsp");

%>
