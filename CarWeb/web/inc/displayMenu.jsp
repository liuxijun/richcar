<%@ page import="com.fortune.common.business.security.model.Menu" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.common.Constants" %>
<%@ page import="com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/2
  Time: 8:54
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
%><!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar responsive">
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'fixed')
        } catch (e) {
        }
    </script>
    <div id="sidebar-collapse" class="sidebar-toggle sidebar-collapse">
        <i data-icon2="ace-icon fa fa-angle-double-right" data-icon1="ace-icon fa fa-angle-double-left"
           class="ace-icon fa fa-angle-double-left"></i>
    </div>

    <ul class="nav nav-list">
        <li class="active">
            <a href="../man.jsp">
                <i class="menu-icon fa fa-desktop"></i>
                <span class="menu-text"> 管理首页 </span>
            </a>
            <b class="arrow"></b>
        </li>
        <%
            {
                String requestUrl = request.getServletPath();
                String folderName="";
                String functionName="";
                String defaultWillFirstVisitUrl=null;
                List<Menu>  adminMenus = (List<Menu>) session.getAttribute(Constants.SESSION_ADMIN_MENUS);
                int menuIndex = 0;
                if(adminMenus!=null){
                    String remoteAddr = request.getRemoteAddr();
                    for(int l=adminMenus.size();menuIndex<l;menuIndex++){
                        Menu adminMenu = adminMenus.get(menuIndex);
                        if(isMenuClosed(adminMenu,remoteAddr)){
                            continue;
                        }
                        String cls = " ";
                        if(requestUrl.contains(adminMenu.getUrl())){
                            cls = "active open";
                            folderName = adminMenu.getName();
                        }
                        String parentMenuCls = getStringOfSplitIndex(adminMenu.getStyle(),"\\|",0,null);
                        if(parentMenuCls==null){
                            parentMenuCls = "";
                        }else{
                            parentMenuCls = "fa-"+parentMenuCls;
                        }
        %> <li class="<%=cls%>">
        <a href="#" class="dropdown-toggle">
            <i class="menu-icon fa <%=parentMenuCls%>"></i>
            <span class="menu-text"> <%=adminMenu.getName()%> </span>
            <b class="arrow fa fa-angle-down"></b>
        </a>

        <b class="arrow"></b>

        <ul class="submenu">
            <%
                List<Menu> subMenus = adminMenu.getSubMenus();
                if(subMenus!=null){
                    for(Menu subMenu:subMenus){
                        if(isMenuClosed(subMenu,remoteAddr)){
                            continue;
                        }
                        String subCls=" ";
                        String subUrl = subMenu.getUrl();
/*
                        if(subUrl.contains("/su/")){
                            System.out.println("操作员角色菜单："+subMenu.getName()+",url=" +
                                    subMenu.getUrl()+
                                    ",requestUrl="+requestUrl);
                        }
*/
                        if(subUrl.contains(requestUrl)){
                            subCls = "active";
                            if(defaultWillFirstVisitUrl==null){
                                defaultWillFirstVisitUrl = subMenu.getUrl();
                            }
                        }else{
                            //此菜单下将会访问到的url，包括这个菜单的URL，还有会用的其他功能连接。例如获取栏目列表
                            String willVisitUrls = subMenu.getPermissionStr();
                            if(!"/man.jsp".equals(requestUrl)){
                                if(willVisitUrls!=null && willVisitUrls.contains(requestUrl)){
                                    subCls = "active";
                                }
                            }
                        }
                        if("active".equals(subCls)){
                            functionName = subMenu.getName();
                        }
            %>
            <li class="<%=subCls%>">
                <a href="<%=subMenu.getUrl()%>">
                    <i class="menu-icon fa fa-caret-right"></i>
                    <%=subMenu.getName()%>
                </a>
                <b class="arrow"></b>
            </li>
            <%
                    }
                }
            %>
        </ul>
    </li>
        <%
                    }
                }
                session.setAttribute("defaultWillFirstVisitUrl",defaultWillFirstVisitUrl);
                request.setAttribute("folderName",folderName);
                request.setAttribute("functionName",functionName);
            }
        %></ul>
    <!-- /.nav-list -->
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'collapsed')
        } catch (e) {
        }
    </script>
</div>
<!-- /section:basics/sidebar -->
<%!
    public String getStringOfSplitIndex(String str,String splitor,int index,String defaultVal){
        String result = getStringOfSplitIndex(str,splitor,index);
        return result==null?defaultVal:result;
    }
    public String getStringOfSplitIndex(String str,String splitor,int index){
        if(str==null){
            return null;
        }
        String[] values = str.split(splitor);
        if(values.length>=index+1){
            return values[index];
        }
        return null;
    }
    public boolean isMenuClosed(Menu menu,String remoteAddr){
        if(menu==null){
            return true;
        }
        Integer menuStatus = menu.getStatus();
        if(MenuLogicInterface.STATUS_CLOSED.equals(menuStatus)){
            return true;
        }
        if(MenuLogicInterface.STATUS_DEBUG.equals(menuStatus)){
            if(remoteAddr.startsWith("127.0")||remoteAddr.startsWith("0:")||remoteAddr.startsWith("192.")){
                //如果是测试状态，针对特定类型的主机地址会开放
            }else{
                return true;
            }
        }
        return false;

    }
%>