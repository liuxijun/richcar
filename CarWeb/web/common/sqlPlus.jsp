<%
/**
 * @author：xjliu
 */
%><%@ page import="java.text.SimpleDateFormat,
                   java.text.ParseException,
                   java.sql.Statement,
                   java.sql.SQLException,
                   java.sql.Connection,
                   java.util.HashMap,
                   java.util.Date,
                   java.util.Map,
                   java.util.Enumeration,
                   java.sql.*,java.util.*,
                   java.util.Date,
                   org.hibernate.cfg.Configuration" %><%@ page import="com.fortune.util.StringUtils" %><%@ page
 contentType="text/html;charset=UTF-8" %><%
    if(session.getAttribute("sessionOperator")==null){
        response.sendRedirect("../admin/login.jsp");
        return;
    }

//定义数据访问变量
    Date startTime = new Date();
    String sql = request.getParameter("sql");
    jdbcDriver = request.getParameter("jdbcDriver");
    jdbcUrl = request.getParameter("jdbcUrl");
    jdbcUser = request.getParameter("jdbcUser");
    jdbcPwd = request.getParameter("jdbcPwd");
    String[] jdbcDrivers=new String[]{
            "com.mysql.jdbc.Driver",
            "oracle.jdbc.driver.OracleDriver",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver"};
    String[] jdbcUrls = new String[]{"jdbc:mysql://localhost:3306/doshow","jdbc:sqlserver://12.110.250.36:1030;databasename=mdnb10d04;selectMethod=cursor"};
    if(jdbcDriver == null){
        //jdbcDriver = "com.mysql.jdbc.Driver";
    }
    if(jdbcUrl == null){
        //jdbcUrl = "jdbc:mysql://localhost:3306/doshow";
    }
    if(jdbcUser==null){
        //jdbcUser = "doshow";
    }
    if(jdbcPwd==null){
        //jdbcPwd = "doshow";
    }
    if ("".equals(sql) || sql == null) {
        if("updateDesc".equals(request.getParameter("command"))){
        }else{
            sql = "select sysdate from dual";
        }
    }
//显示参数
    List results = executeSql(sql,jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
    long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
    long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
    long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
    Date endTime = new Date();
%>        <html>
            <head>
                <meta name="GENERATOR" content="Media Stack XSLT Editor"/>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <link rel="stylesheet" href="/css/styles.css"/>
                <title>数据库接口</title>
            </head>
            <body>
            <%
                if(results!=null){
                    for (Object result : results) {
                        if (result instanceof Map) {

                        } else if (result instanceof Object[]) {
                            Object[] dataResult = (Object[]) result;
                            String[] colNames = (String[]) dataResult[0];
                            List dataRows = (List) dataResult[1];
            %>
            <table border="1" cellspacing="0">
                <tr bgcolor="#F0F0F0">
                    <td>Rows</td>
                    <%
                        for (String colName1 : colNames) {
                    %>
                    <td><%=colName1%>
                    </td>
                    <%
                        }
                    %>
                </tr>
                <%
                    for (int row = 0; row < dataRows.size(); row++) {
                        Map aRow = (Map) dataRows.get(row);
                %>
                <tr>
                    <td>/*<%=row%>*/
                    </td>
                    <%
                        for (String colName : colNames) {
                            String data = (String) aRow.get(colName);
                            if (data == null) {
                                data = "";
                            }
                    %>
                    <td><%=data%>
                    </td>
                    <%
                        }
                    %>
                </tr>
                <%
                    }
                %>
            </table>
            <%
            } else if (result instanceof String) {
            %><p><%=result%>
            </p><%
            } else {
            %><p><%=result%>
            </p><%
                        }
                    }
                }
            %>
                <form method="post" action="sqlPlus.jsp">
                    <table width="90%">
                        <tr>
                            <td width="100%" colspan="2">
                                页面启动时间：<%=date2string(startTime.getTime())%>
                            </td>
                        </tr>
                        <tr>
                            <td width="100%" colspan="2">
                                页面结束时间：<%=date2string(endTime.getTime())%>
                            </td>
                        </tr>
                        <tr>
                            <td width="100%" colspan="2">
                                页面处理时间：<%=(endTime.getTime()-startTime.getTime())%>ms
                            </td>
                        </tr>
                        <tr>
                            <td width="100%" colspan="2">
                                内存信息(F/M/T)：<%=freeMemory%>M/<%=maxMemory%>M/<%=totalMemory%>M
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="submit"/> &nbsp;&nbsp;&nbsp;
                                <input type="reset"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="120px">JDBC驱动</td>
                            <td width="*">
                            <select name="jdbcDriver">
                               <%
                                   for(String driverName:jdbcDrivers){
                                       out.print("<option value='"+driverName+"'");
                                       if(driverName.equals(jdbcDriver)){
                                           out.print(" selected ");
                                       }
                                       out.println(">"+driverName+"</option>");
                                   }
                               %>
                            </select>
                            </td>
                        </tr>
                        <tr>
                            <td>URL</td>
                            <td><input type="text" name="jdbcUrl" value="<%=jdbcUrl%>" style="width:400px"></td>
                        </tr>
                        <tr>
                            <td>账号</td>
                            <td><input type="text" name="jdbcUser" value="<%=jdbcUser%>" style="width:400px"></td>
                        </tr>
                        <tr>
                            <td>口令</td>
                            <td><input type="text" name="jdbcPwd" value="<%=jdbcPwd%>" style="width:400px"></td>
                        </tr>
                        <tr>
                            <td width="100%" colspan="2">
                                <textarea name="sql" style="width:100%;height:400"><%=sql%></textarea>
                            </td>
                        </tr>
                    </table>
                </form>
            </body>
        </html>
<%@include file="../admin/sqlBase.jsp"%>