<%@ page import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page import="com.fortune.util.SpringUtils" %><%@ page import="com.fortune.rms.business.publish.model.Channel" %><%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/6/9
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean(
            "channelLogicInterface",request.getServletContext());
    long systemRootId = channelLogicInterface.getSystemChannelId();
    List<Channel> channels = channelLogicInterface.getChannelList(systemRootId);
    String sql = "";
    for(Channel channel:channels){
        long channelId = channel.getId();
        String ids = channelLogicInterface.getAllChildIds(channelId);
        String name =channel.getName();
        if(name.toLowerCase().contains("vip")){
            continue;
        }
        String code = channel.getCode();
        if("live".equals(code)||"zt".equals(code)||"bd".equals(code)){
            continue;
        }
        sql+="--处理频道“"+name+"”下所有子频道媒体转到“" +name+
                "”下\n";
        sql = sql+("update CONTENT_CHANNEL set channel_id="+channelId+" where channel_id in ("+ids+");\n");
        sql+="--删除“" +name+
                "”所有的子频道\n";
        sql+="delete from CHANNEL where id in("+ids+");\n";
    }
%><html>
<head>
    <title></title>
</head>
<body>
<pre>
<%=sql%>
</pre>
</body>
</html>
