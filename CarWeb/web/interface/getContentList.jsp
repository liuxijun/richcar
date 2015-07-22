<%@ page import="com.fortune.rms.business.publish.model.Channel" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-6-3
  Time: 上午11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface",session.getServletContext());
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    List<Channel> channels = channelLogicInterface.getCspChannel(1);
    out.println("影片名\t频道\tCSP\t连接");
    for(Channel channel:channels){
        List<Content> contents = contentLogicInterface.list(1L, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED,channel.getId(),new PageBean(0,24,null,null));
        for(Content content:contents){
            Map<String,Object> properties = content.getProperties();
            Object clipsObj = properties.get("Media_Url_Source");
            if(clipsObj instanceof List){
                List<Map<String,String>> clips = (List<Map<String,String>>) clipsObj;
                for(Map<String,String> clip:clips){
                    String url = clip.get("url");
                    out.println(content.getName()+"\t"+channel.getName()+"("+channel.getId()+")\t"+content.getCspId()+"\t"+url);
                }
            }
        }
    }
%>