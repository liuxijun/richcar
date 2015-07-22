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
        java.sql.ResultSet"%><%
//drm信息
String cid = ParamTools.getParameter(request,"cid","");
String returnDrm = ParamTools.getParameter(request,"return","");
//cid = java.net.URLDecoder.decode(cid);
//returnDrm = java.net.URLDecoder.decode(returnDrm);
session.setAttribute("DrmCid",cid);
session.setAttribute("DrmReturn",returnDrm);
//    response.sendRedirect(PlayLogic.getDrmSuccessReturnUrl(returnDrm,Constants.PLAY_VALID_TIME));
%>
<html>
    <body>
        <center>
        <br>
        <br>
            <form name=userfrm action="CheckUser.jsp">
                用户：<input type=text name=user> <br>
                密码：<input type=password name=password><br>
                <input type=submit value=提交>              <br>
            </form>
        </center>
    </body>
</html>
