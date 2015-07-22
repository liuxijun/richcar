<%@ page import="com.fortune.util.StringUtils" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.content.logic.logicImpl.ContentPropertyLogicImpl" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-7-11
  Time: 下午5:04
  获取FLASH播放连接
--%><%@ page contentType="text/xml;charset=UTF-8" language="java" %><%
    long contentPropertyId= StringUtils.string2long(request.getParameter("pid"), -1);
    Logger logger = Logger.getLogger("com.fortune.jsp.getPlayUrl");
    ContentPropertyLogicInterface contentPropertyLogic = (ContentPropertyLogicInterface) SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    PropertyLogicInterface propertyLogic = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
    ContentProperty cp = contentPropertyLogic.get(contentPropertyId);
    Content content = contentLogic.getCachedContent(cp.getContentId());
    ContentProperty searchBean = new ContentProperty();
    searchBean.setContentId(cp.getContentId());
    searchBean.setIntValue(cp.getIntValue());
    List<Map<String,String>> urls = new ArrayList<Map<String,String>>();
    List<ContentProperty> clips = contentPropertyLogic.search(searchBean);
    String hostUrl = "";//content.getDeviceId();
    for(ContentProperty clip:clips){
        Property property = propertyLogic.get(clip.getPropertyId());
        if(PropertyLogicInterface.DATA_TYPE_FLV.equals(property.getDataType())){
            Map<String,String> url = new HashMap<String,String>();
            String urlValue = hostUrl + clip.getStringValue();
            urlValue +="?contentId=&spId=&cpId=&";
            //调度，授权
            url.put("url",urlValue);
            url.put("name",getBanwidthName(property.getName()));
            urls.add(url);
        }
    }
    logger.debug("获取数据库内的播放连接：cpId="+contentPropertyId+",对方IP:"+request.getRemoteAddr());
%><?xml version="1.0" encoding="utf-8" ?>
<data>
    <logo url="http://180.168.69.39/head.jpg" />
    <!--<ad type="0" url="http://180.168.69.39/v/jsp.flv" duration="15" link="http://www.baidu.com" /> -->
    <!--<ad type="1" url="http://180.168.69.39/test/xichen/1.jpg" duration="5" link="http://www.baidu.com" /> -->
    <video title="<%=content.getName()%>" live="0">
        <hosts><%=getHostUrl(hostUrl)%></hosts>
        <%
            for(Map<String,String> aUrl :urls){
                out.println("<path rate=\""+aUrl.get("name")+"\">" +getClipUrl(aUrl.get("url"))+
                        "</path>");
            }
        %>
    </video>
</data><%!
    public String getBanwidthName(String propertyName){
        //propertyName = "流畅：512k连接";
        return "流畅";
    }
    public String getHostUrl(String url){
        //url = "http://192.168.1.25:443/vod/mp4/zszn.512k.mp4?contentId=&spId=&cpId=....."
        return "http://192.168.1.25:443/";
    }
    public String getClipUrl(String url){
        //url = "http://192.168.1.25:443/vod/mp4/zszn.512k.mp4?contentId=&spId=&cpId=....."
        return "vod/mp4/zszn.512k.mp4";
    }
%>