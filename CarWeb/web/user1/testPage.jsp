<%@ page import="cn.sh.guanghua.util.tools.PageHelper,
                 cn.sh.guanghua.util.tools.StringTools,
                 cn.sh.guanghua.util.tools.Config,
                 cn.sh.guanghua.midware.common.ConfigManager"%>
 <%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2006-1-26
  Time: 16:36:40
  To change this template use File | Settings | File Templates.
--%> <%
    long totalMemory = Runtime.getRuntime().totalMemory()/(1024*1024);
    long freeMemory = Runtime.getRuntime().freeMemory()/(1024*1024);
Runtime.getRuntime().gc();
    String serverName = "218.26.171.233";
    /*
    Config conf = ConfigManager.getConfig().node("host");
    if(conf != null){
        serverName  = conf.get("local-name",serverName);
    }
    */
    String[] servers = new String[]{"mediastack.kdsj2.sx.cn","218.26.171.233","218.26.171.234","218.26.171.235","218.26.171.236"};
%>
<%@ page contentType="text/html;charset=GB2312" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body><h1 align="center">系统监测</h1>
  <table align="center" border="1" cellspacing="0" width="600">
     <tr>
         <td align="right" width="150">服务器地址</td>
         <td><%=serverName%></td>
     </tr>
      <tr>
          <td align="right" width="150">当前系统时间</td>
          <td><%=StringTools.date2string(System.currentTimeMillis())%></td>
      </tr>
      <tr>
          <td align="right" width="150">JVM当前内存总量</td>
          <td><%=totalMemory%>MB</td>
      </tr>
      <tr>
          <td align="right" width="150">JVM当前剩余内存</td>
          <td><%=freeMemory%>MB</td>
      </tr>
      <tr>
         <td align="right" width="150">内存图示</td>
          <td >
               <table width="100%" border="0" cellspacing="0">
                    <tr height="20">
                        <td bgcolor="red" width="<%=((totalMemory-freeMemory)*100/totalMemory)%>%"></td>
                        <td align="center" bgcolor="green" width="<%=((freeMemory)*100/totalMemory)%>%"></td>
                    </tr>
               </table>
          </td>
      </tr>
  </table>
              <table align="center">
                  <tr>
                      <%
                            for(int i=0;i<servers.length;i++){
                                %>
                                <td align="center" width="<%=(100/servers.length)%>%"><a href="http://<%=servers[i]%>/user/testPage.jsp?times=<%=System.currentTimeMillis()%>"><%=servers[i]%></a></td>
                                <%
                            }
                      %>
                  </tr>
              </table>
  </body>
</html>