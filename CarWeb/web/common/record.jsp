<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.common.business.security.model.Menu" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.common.business.security.logic.logicImpl.MenuChecker" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/1
  Time: 7:53
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    MenuLogicInterface menuLogicInterface = (MenuLogicInterface) SpringUtils.getBean("menuLogicInterface",session.getServletContext());
    String command = request.getParameter("command");

    if(command==null){
        command = "";
    }
    String message = "";
    int guessSelectedId = -1;
    List<Menu> allMenus = menuLogicInterface.getAll();
    if("start".equals(command)){
        MenuChecker.getInstance().refreshAllMenus();
        Map<String,String> visitLogMap = new HashMap<String,String>();
        session.setAttribute("visitLogMap",visitLogMap);
    }else if("stop".equals(command)){
        Map<String,String> visitLogMap =(Map<String,String>) session.getAttribute("visitLogMap");
        session.removeAttribute("visitLogMap");
        session.setAttribute("visitLogSaved",visitLogMap);
    }else if("save".equals(command)){
        int menuId = StringUtils.string2int(request.getParameter("menuId"), -1);
        if(menuId<0){
            message = "菜单ID没有输入！";
        }else{
            String[] urlSelected = request.getParameterValues("url");
            if(urlSelected!=null){
                String urls = "";
                for(String key:urlSelected){
                    if(!"".equals(urls)){
                        urls+=";";
                    }
                    urls+=key;
                }
                Menu m = menuLogicInterface.get(menuId);
                m.setPermissionStr(urls);
                menuLogicInterface.save(m);
            }else{
                message = "没有选择任何url或者还没有记录！";
            }
        }
    }else{
        Map<String,String> visitLogMap =(Map<String,String>) session.getAttribute("visitLogMap");
        if(visitLogMap!=null){
            message = "当前访问记录：<br>";
            for(String key:visitLogMap.keySet()){
                message+="<input type='checkbox' checked=true name='url' value='"+key+"'>"+key;
                for(Menu menu:allMenus){
                    String url = menu.getUrl();
                    if(url==null||"".equals(url.trim())){
                        continue;
                    }
                    if(key.contains(url)){
                        message+=(",可能对应菜单："+menu.getName()+","+menu.getUrl()+","+key);
                        guessSelectedId = menu.getId();
                        //break;
                    }
                }
                message+="<br/>";
            }
        }else{
            message = "尚未启动访问记录！";
        }
    }
%><html>
<head>
    <title>记录访问情况</title>
    <script type="text/javascript">
        function startRecord(){
            doCommand('start');
        }
        function stopRecord(){
            doCommand('stop');
        }
        function saveRecord(){
            doCommand('save');
        }
        function doCommand(command){
            var form = document.forms[0];
            form.command.value = command;
            form.submit();
        }
    </script>
</head>
<body>
    <form method="post" action="record.jsp">
        <table>
            <tr>
                <td>执行结果：</td><td><%=message%></td>
            </tr>
            <tr>
                <td>操作命令：</td>
                <td>
                    <input type="button" value="刷新数据" onclick="doCommand('refresh')">&nbsp;
                    <input type="button" value="启动记录" onclick="startRecord()">&nbsp;
                    <input type="button" value="停止记录" onclick="stopRecord()">&nbsp;
                    <input type="button" value="保存记录" onclick="saveRecord()">&nbsp;
                    <select name="menuId">
                        <%
                            for(Menu menu:allMenus){
                                String selected = "";
                                if(guessSelectedId == menu.getId()){
                                    selected = "selected";
                                }
%>                   <option value="<%=menu.getId()%>" <%=selected%>><%=(menu.getParentId()==null?"":"&nbsp;&nbsp;")+""+menu.getName()%></option>
                        <%
                            }
                        %>

                    </select>
                </td>
            </tr>
        </table>
        <input type="hidden" name="command">
    </form>
</body>
</html>
<%!
    private Logger logger = Logger.getLogger("com.fortune.common.jsp.record.jsp");
%>