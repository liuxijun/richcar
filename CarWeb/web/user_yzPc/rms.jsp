<%@ page import="com.fortune.rms.business.user.CacheLogic" %><%@ page
        contentType="text/html; charset=UTF-8" %><%@include file="param.jsp"%><%@include
        file="queryString.jsp"%><%
    boolean result = false;
    String userAgent = request.getHeader("userAgent");
    boolean isSafari = false;
    if(userAgent!=null && userAgent.indexOf("Safari")>0){
        isSafari = true;
    }
    String userIp = request.getRemoteAddr();
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
        out.println("{success:false,playUrl:'noPermissions.gif'}");
        return;
    }

    String isFree = "0";
    if ("true".equals( session.getAttribute("play_free_"+contentId+"_"+contentPropertyId ) )){
        isFree = "1";
    } else if ("true".equals( session.getAttribute("play_"+contentId+"_"+contentPropertyId ) )){

    } else {
        //logger.warn("用户没有登录，为了测试，允许其播放！");
        //out.println("认证无效");
        //return;
    }

    CacheLogic cacheLogic= new CacheLogic(request);
    long areaId = cacheLogic.getAreaId(userIp);

    logger.debug("准备获取数据："+queryStr);
    String mediaUrl = cacheLogic.getMediaUrl(contentId, contentPropertyId);
    if(isSafari){
        mediaUrl+=".m3u8";
    }
    mediaUrl+="?" + queryStr + "&userId="+userId + "&userIp="+userIp + "&isFree="+isFree + "&areaId="+areaId;
    String tokenPwd = getTokenPwd(mediaUrl,"cuntv");
    try {
        //logger.debug("调度请求："+mediaUrl);
        String gslbUrl = getGslbUrl(mediaUrl, userIp);
        //logger.debug("调度结果："+gslbUrl);
        mediaUrl = regUrl(gslbUrl,userIp,tokenPwd);
        result = true;
    } catch (Exception e) {
        logger.error("调度过程中发生异常："+e.getMessage());
        mediaUrl = regUrl(mediaUrl,userIp,tokenPwd);
    }
    StringBuilder playList = new StringBuilder();
    String requestFormat=request.getParameter("format");
    if(requestFormat==null){
        requestFormat="json";
    }
    if("asx".equals(requestFormat)){
        response.setContentType("video/x-ms-asf;charset=UTF-8");
        playList.append("<ASX Version=\"3.0\">\n");
        playList.append("    <ENTRY>\n")
                .append("\t\t<REF HREF=\"")
                .append(mediaUrl)
                .append("\"/>\n")
                .append("\t</ENTRY>\n");
        playList.append("</ASX>");
    }else if("REDIRECT".equals(requestFormat)){
        response.sendRedirect(mediaUrl);
        return;
    }else if("json".equals(requestFormat)){
        //Json格式
        playList.append("{success:").append(result).append(",playUrl:'").append(mediaUrl).append("'}");
    }else if("text".equals(requestFormat)){
        playList.append(mediaUrl);
    }
    out.println(playList.toString());
%><%@include file="urlUtils.jsp"%>