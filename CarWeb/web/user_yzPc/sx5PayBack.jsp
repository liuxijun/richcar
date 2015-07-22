<%@ page
contentType="text/html; charset=GBK" %><%@ page
import="java.util.List,
        java.net.InetAddress,
        com.runway.race.ipm.IpmResult,
        com.runway.race.ipm.Ipm,
        com.runway.race.ipm.IpmImpl"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.product.model.Product" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@ page import="com.fortune.rms.business.csp.logic.logicImpl.CspLogicImpl" %>
<%@ page import="com.fortune.rms.business.csp.model.Csp" %>
<%@ include file="param.jsp"%>
<%@ include file="queryString.jsp"%>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="java.util.*,java.text.DecimalFormat"%>
<%@ page import="com.fortune.rms.business.user.SessionUser" %>
<%@ page import="com.fortune.common.Constants" %>
<%
    //数据收集，发送至山西服务器扣费
    SessionUser su = (SessionUser) session.getAttribute(Constants.SESSION_USER);
    String userId = su.getUserId();
    String sessionId = su.getSessionId();
    String userInfo = su.getUserInfo();

    //session检查
    if (userId == null && userId.equals("")) {
        response.sendRedirect("account/error.jsp?msg=avalid_session");
        return;
    }

    UserLogic userLogic = new UserLogic(request);

//    DecimalFormat df = new DecimalFormat("#0.00");//用于数字格式转换
    //获取影片参数
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.PayMediaRequest_bak");

    CacheLogic cacheLogic = new CacheLogic(request);
    Product product = cacheLogic.getProduct(serviceProductId);
    Content content = cacheLogic.getContent(contentId);

    Csp csp = cacheLogic.getCsp(content.getCspId());

    try{
        String type = getParameter(request, "type", "");

        String price =  getParameter(request, "price", "0");
        double fee = Double.valueOf(price);

        String categoryId;
        String clientIp = request.getRemoteAddr();
        String userIP = InetAddress.getLocalHost().getHostAddress();
        userIP = userIP + " from " + request.getRemoteAddr();

        //山西项目，根据不同的购买类型，调用不同的接口，
        try{
            if(type.equals("media")){//购买单个媒体，调用润汇的按次计费接口
                logger.debug("按此购买单个媒体文件");

                fileID = content.getName()+" ["+contentPropertyId+"]";
//按次也将spID送上
//                /*
                try {
                    if(csp!=null && csp.getType() == 1){
                       fileID = fileID+"|"+csp.getSpId();
                        logger.debug(csp.getName()+" 是大包月提供商，SPID="+csp.getSpId());
                    }else{
                        logger.debug(csp.getName()+" 不是大包月提供商，忽略SPID信息！");
                    }
                } catch (Exception e) {
                    logger.error("无法获取IMP信息："+e.getMessage());
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                */

                Ipm ipm = new IpmImpl();

                categoryId = product.getPayProductNo();
                logger.debug("查询购买情况接口AAProcess：fileID="+fileID+",categoryId="+categoryId);
                IpmResult result = ipm.AAProcess(userInfo, sessionId, categoryId, fileID, "0", "0", clientIp);
                if(result.getResultID() == 0){
                    logger.debug("需要购买，调用购买接口AccountProcessClick：fileID="+fileID+",categoryId="+categoryId);
                    IpmResult ap = ipm.AccountProcessClick(userInfo, sessionId, categoryId, fileID, "0", "0", userIP);
                    if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5 || ap.getErrorInfo().equals("314")){//计费成功,免费,icp免费
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"购买成功,返回值="+ap.getResultID());
                         session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                         userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);
                    }else{
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"购买失败,返回值="+ap.getResultID());
                        response.sendRedirect("account/error.jsp?msg="+ap.getResultID());
                    }
                }else if(result.getResultID() == 314){
                    logger.debug("fileID="+fileID+",categoryId="+categoryId+"已经购买，返回="+result.getResultID());
                    logger.debug("尝试播放，pramString="+queryStr);
                    //paramString +="&fileID="+fileID;
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }else{
                    response.sendRedirect("account/error.jsp?msg="+result.getResultID());
                }
            }else if (type.equals("channel")){//购买频道，调用润汇的包月接口
                categoryId = product.getPayProductNo();
                Ipm ipm = new IpmImpl();
                IpmResult ap = ipm.OrderMonthService(userInfo, sessionId,categoryId, "0", userIP);
                if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5){//计费成功,免费,icp免费
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);
                }else{
                    response.sendRedirect("account/error.jsp?msg="+ ap.getResultID());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }catch(Exception e){
        session.setAttribute("ERROR", e.toString());
        e.printStackTrace();
        response.sendRedirect("account/error.jsp?msg=unexpect_error");
        return;
    }
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>购买成功</title>
<link href="css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="images/account/123.gif">
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
								  <p><b><font color="#0000FF">恭喜您，您的支付已经成功!!</font></b></p>
  							  </td>
							</tr>
						</tbody>
					</table>
			</td>
		</tr>
	</tbody>
</table>
<script language="javascript" type="text/javascript">
        alert("恭喜您，您的支付已经成功!!");
        location.replace("sx6Player.jsp?<%=queryStr%>");
        document.location.href = "sx6Player.jsp?<%=queryStr%>";
</script>

</body>
</html>
