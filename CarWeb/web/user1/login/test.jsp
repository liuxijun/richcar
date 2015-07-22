<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.mediastack.dataunit.MediaIcp,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.dataunit.Channel,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        java.sql.Connection,
        java.sql.ResultSet,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.dataunit.IcpMediaInfo,
        cn.sh.guanghua.cache.CacheManager,
        cn.sh.guanghua.mediastack.dataunit.Tvchannel,
        cn.sh.guanghua.mediastack.business.normal.ChannelLogic,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.util.tools.StringTools"%><%@ include file="../cookies.jsp" %><%

     /**
     * 本页面的任务有二：
     * 一是读Cookie中的信息、
     * 二是根据读到的结果显示不同的内容
     */
    String userInfo = "";
    String sessionId = "";
    String bingindId = "";

    /*  本页面的任务之一：读Cookie中的信息：userInfo、sessionId、bindingId */
    userInfo  = getCookieValue(request, "userInfo");
    sessionId = getCookieValue(request, "sessionID");
    bingindId = getCookieValue(request, "bindingID");

try{

    //用户登录成功
    if  (userInfo != null
                 && !userInfo.equals("")
                 && !userInfo.equals("null")
                 && sessionId != null
                 && !sessionId.equals("")
                 && !sessionId.equals("null"))
    {
out.println("登录成功<br>");

out.println("<br>userInfo:"+userInfo);
out.println("<br>sessionId:"+sessionId);

    }else{
out.println("登录失败");
    }
}catch(Exception e){
    e.printStackTrace();
}

%>