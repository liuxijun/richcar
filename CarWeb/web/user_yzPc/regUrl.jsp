<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-5-23
  Time: 上午9:53
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page
        import="com.fortune.util.StringUtils" %><%
    //http://mediastack.vod.inhe.net/user/regURL.jsp?icpid=xxx&impid=xxx&channelid=xxx&mediaid=xxx&userid=xxx&userip=xxx&fee=x&url=xxxxxx
    long spId=getPrameter(new String[]{"spId","icpid"},-1,request);
    long cpId=getPrameter(new String[]{"cpId","impid"},-1,request);
    long channelId=getPrameter(new String[]{"channelId","channelid"},-1,request);
    long contentId=getPrameter(new String[]{"contentId","mediaid"},-1,request);
    long contentPropertyId=getPrameter(new String[]{"contentPropertyId"},-1,request);
    String requestUrl = request.getParameter("url");
    String clientIp = getPrameter(new String[]{"userip","userIp"},"",request);
    String userId = getPrameter(new String[]{"userid","userId"},"",request);
    String fee = getPrameter(new String[]{"fee"},"",request);

    if(clientIp==null){
        clientIp = request.getRemoteAddr();
    }
    String result = "urlNotInput.jsp?result=404";
    if(requestUrl!=null){
       if(requestUrl.contains("?")){
           requestUrl+="&";
       }else{
           requestUrl+="?";
       }
        requestUrl+="spId="+spId;
        requestUrl+="&cpId="+cpId;
        requestUrl+="&channelId="+channelId;
        requestUrl+="&contentId="+contentId;
        requestUrl+="&contentPropertyId="+contentPropertyId;
        requestUrl+="&userId="+userId;
        requestUrl+="&fee="+fee;
        String tokenPwd = getTokenPwd(requestUrl,"fortuneRMS");
        String gslbUrl = getGslbUrl(requestUrl,clientIp);
        if(gslbUrl!=null){
            result = regUrl(gslbUrl,clientIp,tokenPwd);
        }else{
            result = regUrl(requestUrl,clientIp,tokenPwd);
        }
    }
    String format = request.getParameter("format");
    if(format==null){
        format = "asx";
    }
    if("REDIRECT".equals(format)){
        response.sendRedirect(result);
        return;
     }else if("asx".equals(format)){
        response.setContentType("video/x-ms-asf;charset=UTF-8");
    }
    out.print(outPutMediaUrl(format,result));
%><%@include file="urlUtils.jsp"%><%!
    public long getPrameter(String[] names,long defaultValue,HttpServletRequest request){
        if(names==null) return defaultValue;
        long result=defaultValue;
        for(String name:names){
            result = StringUtils.string2long(request.getParameter(name),defaultValue);
            if(result!=defaultValue){
                return result;
            }
        }
        return result;
    }
    public String getPrameter(String[] names,String defaultValue,HttpServletRequest request){
        if(names==null) return defaultValue;
        String result=defaultValue;
        for(String name:names){
            result = request.getParameter(name);
            if(result!=null){
                return result;
            }
        }
        return result;
    }
%>