<%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-7-11
  Time: 下午5:04
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String contentPropertyId= request.getParameter("pid");
    Logger logger = Logger.getLogger("com.fortune.jsp.getPlayUrl");
    //String hostUrl = "http://192.168.1.25:443/";
    String urlPathHeader = "vod/";
    String hostUrl = "http://" + request.getServerName()+
            ":443/"+urlPathHeader;
    String filePathHeader = "mp4/";
    //
    //获取播放连接
    //
    //
    //
    //
    List<Map<String,String>> urls = new ArrayList<Map<String,String>>();
    Map<String,String> url;
    url = new HashMap<String,String>();
    url.put("name","流畅");
    url.put("url",filePathHeader+"0002.350.mp4");
    url.put("bandwidth","350000");
    urls.add(url);
    url = new HashMap<String,String>();
    url.put("name","高清");
    url.put("url",filePathHeader+"0002.500.mp4");
    url.put("bandwidth","500000");
    urls.add(url);
    url = new HashMap<String,String>();
    url.put("name","超清");
    url.put("bandwidth","1024000");
    url.put("url",filePathHeader+"0002.1024.mp4");
    urls.add(url);
    logger.debug("获取数据库内的播放连接：cpId="+contentPropertyId+",对方IP:"+request.getRemoteAddr());
    String userAgent = request.getHeader("user-agent");
    if(request.getParameter("m3u8")!=null||(userAgent!=null&&userAgent.indexOf("Safari")>0)){
%>#EXTM3U
<%
    for(Map<String,String> aUrl :urls){
        out.println("#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH="+aUrl.get("bandwidth"));
        out.println(hostUrl+aUrl.get("url")+".m3u8");
    }
%><%
        return;
    }else{

    }
%><?xml version="1.0" encoding="utf-8" ?>
<data>
<%--
    <!--<ad type="0" url="http://180.168.69.39/v/jsp.flv" duration="15" link="http://www.baidu.com" /> -->
    <!--<ad type="1" url="http://180.168.69.39/test/xichen/1.jpg" duration="5" link="http://www.baidu.com" /> -->
--%>
    <logo url="http://180.168.69.39/head.jpg" />
    <video title="电影名称" live="0">
        <hosts><%=hostUrl%></hosts>
<%
            for(Map<String,String> aUrl :urls){
                out.println("<path rate=\""+aUrl.get("name")+"\">" +aUrl.get("url")+
                        "</path>");
            }
        %>
    </video>
</data>