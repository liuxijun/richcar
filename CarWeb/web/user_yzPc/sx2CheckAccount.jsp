<%@ page contentType="text/html; charset=gb2312" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@include
        file="param.jsp" %><%@include file="queryString.jsp" %><%@ page
        import="com.fortune.rms.business.user.SessionUser" %><%@ page
        import="com.fortune.common.Constants" %><%@page
        import="java.util.*,
                com.runway.race.ipm.*" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.product.model.Product" %><%@ page
        import="com.fortune.rms.business.user.CacheLogic" %><%@ page
        import="com.fortune.rms.business.csp.model.Csp" %><%@ page
        import="com.fortune.rms.business.product.model.ServiceProduct" %><%@ page
        import="java.net.URLEncoder" %><%
    boolean ipmNoticeInPlayer = !"false".equals(request.getParameter("noticeInCheckAccount"));
//    String ipmFlag = "";

    //session检查
    SessionUser su = (SessionUser) session.getAttribute(Constants.SESSION_USER);
    //String userId = "";
    String userInfo;
    String sessionId;
    String clientIp = request.getRemoteAddr();

    if (su != null) {
        //userId = su.getUserId();
        userInfo = su.getUserInfo();
        sessionId = su.getSessionId();
    } else {
        //访客无权观看
        response.sendRedirect("account/error.jsp?msg=313");
        return;
    }

    CacheLogic cacheLogic = new CacheLogic(request);
    Content content = cacheLogic.getContent(contentId);
    Csp csp = cacheLogic.getCsp(content.getCspId());

    if (content == null) {
        logger.error("影片片段为空，不能继续：contentPropertyId =" + contentPropertyId);
        out.println("影片片断为空");
        return;
    }

    session.setAttribute(Constants.SESSION_CORPERATION_ID, "" + spId);
    session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + contentPropertyId + "_" + channelId, "true");

    Ipm ipm = new IpmImpl();
    List serviceProducts = cacheLogic.getServiceProduct(contentId, spId);
    if (serviceProducts != null && serviceProducts.size() != 0) {
        for (Object serviceProduct : serviceProducts) {
            ServiceProduct sp = (ServiceProduct) serviceProduct;
            Product product = cacheLogic.getProduct(sp.getId());

            try {

                fileID = content.getName() + " [" + contentPropertyId + "]";

                if (product == null || product.getPrice() == -1) {//当前栏目收费--1,免费--0
                    logger.debug("当前栏目直接播放：" + content.getName() + "(服务=" + product.getName() + "),param=" + queryStr);
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }

                if (product == null) {
                    rootCategoryID = "0";
                } else {
                    rootCategoryID = product.getPayProductNo();
                }
                if ("0".equals(rootCategoryID)) {//所有频道免费
                    if (product != null) {
                        logger.debug("所有频道免费，直接播放：" + content.getName() + "(服务= " + product.getName() + "),param=" + queryStr);
                    } else {
                        logger.debug("所有频道免费，直接播放,product为空,param=" + product);
                    }
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }

                logger.debug("开始检查频道《" + rootCategoryID + "》包月订购情况");
                IpmResult query = ipm.QuerySubscribe(userInfo, sessionId, rootCategoryID, "0", clientIp);
                logger.debug("包月订购《" + rootCategoryID + "》情况检查完毕,返回值：query.getErrorInfo=" + query.getErrorInfo() + "(1为已经包月)");
                if(queryStr.indexOf("ipmFlag=null") > 0)  {
                    queryStr = queryStr.replace("ipmFlag=null","ipmFlag=0");
                }
                String tempQueryStr = queryStr;
                queryStr += "&fileID=" + URLEncoder.encode(URLEncoder.encode(fileID, "UTF-8"), "UTF-8") + "&rootCategoryID=" + rootCategoryID;
                if ("1".equals(query.getErrorInfo())) {//已经包月
                    //queryStr +="&ipmFlag=0";
                    session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + contentPropertyId + "_" + channelId, "true");
                    //todo zbxue 加点播分成
                    if (csp != null && csp.getType() == 1) {
                        String spID = csp.getSpId();
                        if (!ipmNoticeInPlayer) {
                            //ipm.AccountProcessFree(su.getUserInfo(),su.getSessionId(),rootCategoryID,fileID+"|"+spID,"0",clientIp);
                        } else {
//                            ipmFlag = "&ipmFlag=0";
                        }
//            System.out.println("buymedia 包月："+fileID+"|"+spID);
                        fileID = fileID+"|"+spID;
                        queryStr = tempQueryStr+"&fileID=" + URLEncoder.encode(URLEncoder.encode(fileID, "UTF-8"), "UTF-8") + "&rootCategoryID=" + rootCategoryID;
                        logger.debug("大包月播放：param=" + queryStr);
                        session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                        response.sendRedirect("sx6Player.jsp?" + queryStr);
                        return;

                    } else {
                        queryStr +="&ipmFlag=1";
                        if (!ipmNoticeInPlayer) {
                            IpmResult ap = new IpmResult();//ipm.AccountProcessFree(userInfo, sessionId, rootCategoryID, fileID, "1",  clientIp);
                            if (ap.getResultID() == 0) {
                                logger.debug("不通延时知润汇，准备播放：param=" + queryStr);
                                session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                                response.sendRedirect("sx6Player.jsp?" + queryStr);
                                return;
                            } else {
                                logger.error("润慧接口发生异常：resultID=" + ap.getResultID());
                                response.sendRedirect("account/error.jsp?msg=" + ap.getResultID());
                            }
                        } else {
//                            ipmFlag = "&ipmFlag=1";
//                            queryStr += ipmFlag;
                            logger.debug("准备播放：" + queryStr);
                            session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                            response.sendRedirect("sx6Player.jsp?" + queryStr);
                            return;
                        }
                    }
                } else {
                    //没有认购包月，跳转到认购页面
                    String spID = null;
                    if (csp != null && csp.getType() == 1) {
                        spID = csp.getSpId();
                    }
                    String fileIDspID = fileID;
                    if (spID != null) {
                        fileIDspID = fileID + "|" + spID;
                    }
                    logger.debug("查询AAProcess，参数fileID=" + fileIDspID);
                    IpmResult result = ipm.AAProcess(userInfo, sessionId, rootCategoryID, fileIDspID, "0", "0", clientIp);//润汇授权接口
                    if (csp != null && csp.getType() == 1) {
//                        ipmFlag = "&spId=" + spID + "&ipmFlag=0";
                        logger.debug("大包月imp‘" + csp.getName() + "’的点播：" + fileID + "|" + spID);
                    } else {
                        if (csp != null) {
                            logger.debug("不是大包月imp，不用通知润汇：" + csp.getName());
                        } else {
                            logger.debug("不是大包月imp，不用通知润汇，imp为null");
                        }
                    }
                    if (("314".equals(result.getErrorInfo())) || (result.getResultID() == 314)) {
                        //todo zbxue 点播分成
                        logger.debug("允许播放，进入播放页面：参数=" + queryStr);
                        session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                        response.sendRedirect("sx6Player.jsp?" + queryStr);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("error:" + e.getMessage());
            }
        }
        response.sendRedirect("sx3BuyList.jsp?" + queryStr);
        return;
    } else {
        //没有绑定任何服务，准备播放
        session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
        response.sendRedirect("sx6Player.jsp?" + queryStr);
    }

%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.CheckAccountV2");
%>