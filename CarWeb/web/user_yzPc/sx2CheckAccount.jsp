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

    //session���
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
        //�ÿ���Ȩ�ۿ�
        response.sendRedirect("account/error.jsp?msg=313");
        return;
    }

    CacheLogic cacheLogic = new CacheLogic(request);
    Content content = cacheLogic.getContent(contentId);
    Csp csp = cacheLogic.getCsp(content.getCspId());

    if (content == null) {
        logger.error("ӰƬƬ��Ϊ�գ����ܼ�����contentPropertyId =" + contentPropertyId);
        out.println("ӰƬƬ��Ϊ��");
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

                if (product == null || product.getPrice() == -1) {//��ǰ��Ŀ�շ�--1,���--0
                    logger.debug("��ǰ��Ŀֱ�Ӳ��ţ�" + content.getName() + "(����=" + product.getName() + "),param=" + queryStr);
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }

                if (product == null) {
                    rootCategoryID = "0";
                } else {
                    rootCategoryID = product.getPayProductNo();
                }
                if ("0".equals(rootCategoryID)) {//����Ƶ�����
                    if (product != null) {
                        logger.debug("����Ƶ����ѣ�ֱ�Ӳ��ţ�" + content.getName() + "(����= " + product.getName() + "),param=" + queryStr);
                    } else {
                        logger.debug("����Ƶ����ѣ�ֱ�Ӳ���,productΪ��,param=" + product);
                    }
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }

                logger.debug("��ʼ���Ƶ����" + rootCategoryID + "�����¶������");
                IpmResult query = ipm.QuerySubscribe(userInfo, sessionId, rootCategoryID, "0", clientIp);
                logger.debug("���¶�����" + rootCategoryID + "�����������,����ֵ��query.getErrorInfo=" + query.getErrorInfo() + "(1Ϊ�Ѿ�����)");
                if(queryStr.indexOf("ipmFlag=null") > 0)  {
                    queryStr = queryStr.replace("ipmFlag=null","ipmFlag=0");
                }
                String tempQueryStr = queryStr;
                queryStr += "&fileID=" + URLEncoder.encode(URLEncoder.encode(fileID, "UTF-8"), "UTF-8") + "&rootCategoryID=" + rootCategoryID;
                if ("1".equals(query.getErrorInfo())) {//�Ѿ�����
                    //queryStr +="&ipmFlag=0";
                    session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + contentPropertyId + "_" + channelId, "true");
                    //todo zbxue �ӵ㲥�ֳ�
                    if (csp != null && csp.getType() == 1) {
                        String spID = csp.getSpId();
                        if (!ipmNoticeInPlayer) {
                            //ipm.AccountProcessFree(su.getUserInfo(),su.getSessionId(),rootCategoryID,fileID+"|"+spID,"0",clientIp);
                        } else {
//                            ipmFlag = "&ipmFlag=0";
                        }
//            System.out.println("buymedia ���£�"+fileID+"|"+spID);
                        fileID = fileID+"|"+spID;
                        queryStr = tempQueryStr+"&fileID=" + URLEncoder.encode(URLEncoder.encode(fileID, "UTF-8"), "UTF-8") + "&rootCategoryID=" + rootCategoryID;
                        logger.debug("����²��ţ�param=" + queryStr);
                        session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                        response.sendRedirect("sx6Player.jsp?" + queryStr);
                        return;

                    } else {
                        queryStr +="&ipmFlag=1";
                        if (!ipmNoticeInPlayer) {
                            IpmResult ap = new IpmResult();//ipm.AccountProcessFree(userInfo, sessionId, rootCategoryID, fileID, "1",  clientIp);
                            if (ap.getResultID() == 0) {
                                logger.debug("��ͨ��ʱ֪��㣬׼�����ţ�param=" + queryStr);
                                session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                                response.sendRedirect("sx6Player.jsp?" + queryStr);
                                return;
                            } else {
                                logger.error("��۽ӿڷ����쳣��resultID=" + ap.getResultID());
                                response.sendRedirect("account/error.jsp?msg=" + ap.getResultID());
                            }
                        } else {
//                            ipmFlag = "&ipmFlag=1";
//                            queryStr += ipmFlag;
                            logger.debug("׼�����ţ�" + queryStr);
                            session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                            response.sendRedirect("sx6Player.jsp?" + queryStr);
                            return;
                        }
                    }
                } else {
                    //û���Ϲ����£���ת���Ϲ�ҳ��
                    String spID = null;
                    if (csp != null && csp.getType() == 1) {
                        spID = csp.getSpId();
                    }
                    String fileIDspID = fileID;
                    if (spID != null) {
                        fileIDspID = fileID + "|" + spID;
                    }
                    logger.debug("��ѯAAProcess������fileID=" + fileIDspID);
                    IpmResult result = ipm.AAProcess(userInfo, sessionId, rootCategoryID, fileIDspID, "0", "0", clientIp);//�����Ȩ�ӿ�
                    if (csp != null && csp.getType() == 1) {
//                        ipmFlag = "&spId=" + spID + "&ipmFlag=0";
                        logger.debug("�����imp��" + csp.getName() + "���ĵ㲥��" + fileID + "|" + spID);
                    } else {
                        if (csp != null) {
                            logger.debug("���Ǵ����imp������֪ͨ��㣺" + csp.getName());
                        } else {
                            logger.debug("���Ǵ����imp������֪ͨ��㣬impΪnull");
                        }
                    }
                    if (("314".equals(result.getErrorInfo())) || (result.getResultID() == 314)) {
                        //todo zbxue �㲥�ֳ�
                        logger.debug("�����ţ����벥��ҳ�棺����=" + queryStr);
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
        //û�а��κη���׼������
        session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
        response.sendRedirect("sx6Player.jsp?" + queryStr);
    }

%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.CheckAccountV2");
%>