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
//drm��Ϣ
String userId = ParamTools.getParameter(request,"user","");
String password = ParamTools.getParameter(request,"password","");
if(password.equals("xywu")){

}else{
    response.sendRedirect("account/error.jsp?msg=pay_fail");
    return;
}
%>
<html>
    <body>
        <center>
                <a href="GetLicense.jsp?tp=1">���뵥�ε㲥</a><br><br>
                <a href="GetLicense.jsp?tp=2">������µ㲥</a><br>
            </form>
        </center>
    </body>
</html>
