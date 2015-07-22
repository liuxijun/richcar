<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/5/1
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String pathName = request.getParameter("path");
    String root = "/home/fortune/movie/";
    if(pathName==null){
        pathName = "/";
    }
    File path = new File(root+pathName);
    File[] files=null;
    if(path.exists()){
        files=path.listFiles();
    }
%>
<html>
<head>
    <title>列目录<%=pathName%></title>
    <script type="text/javascript">
        var currentPath = '<%=pathName%>';
        function goToDir(dir){
            var frm=document.forms[0];
            if(dir=='..'){
                if(currentPath=='/'){
                    return;
                }
                var p = currentPath.lastIndexOf('/');
                if(p>0){
                    currentPath = currentPath.substring(0,p);
                }
            }else{
                currentPath = currentPath + '/'+dir;
            }
            frm.path.value=currentPath;
            frm.submit();
        }
    </script>
</head>
<body>
<form name="pathViewerFrm" action="?" method="post">
    <table>
        <tr>
            <td><label for="pathName">目录</label></td>
            <td><input id="pathName" type="text" name="path" value="<%=pathName%>"><input type="submit" value="列目录"></td>
        </tr>
        <tr>
            <td>文件列表</td>
            <td><%
                if(files!=null){
%><table>
                <tr>
                    <th width="400">文件名</th>
                    <th width="50">类型</th>
                    <th width="100">大小</th>
                    <th width="200">日期</th>
                </tr>
                <%
                    for(File file:files){
                        String name =file.getName();
                        name = new String(name.getBytes("UTF-8"),"UTF-8");
                        %><tr>
                <td style="cursor: pointer;color:blue" onclick="goToDir('<%=name%>')"><%=name%></td>
                <td><%=file.isDirectory()?"DIR":"FILE"%></td>
                <td><%=file.length()%></td>
                <td><%=StringUtils.date2string(file.lastModified())%></td>
                        </tr><%
                    }
                %>
</table>
                <%
                }
            %>

            </td>
        </tr>
    </table>
</form>
</body>
</html>
