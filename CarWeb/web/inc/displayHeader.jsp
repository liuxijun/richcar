<%@ page import="com.fortune.util.AppConfigurator" %>
<%@ page import="com.fortune.util.IndividualUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/2
  Time: 9:13
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="navbar" class="navbar navbar-default">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="navbar-container" id="navbar-container">
        <!-- #section:basics/sidebar.mobile.toggle -->
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>

        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <small>
                    <img src="../<%=AppConfigurator.getInstance().getConfig("system.headerPageLogo","images/logo_redex.png")%>" alt="logo">
                    <%--<img src="<%=IndividualUtils.getInstance().getLogoURL()%>" alt="logo" style="max-height: 60px">--%>
                </small>
            </a>

            <!-- /section:basics/navbar.layout.brand -->

            <!-- #section:basics/navbar.toggle -->

            <!-- /section:basics/navbar.toggle -->
        </div>

        <!-- #section:basics/navbar.dropdown -->
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav" style="text-align:right">
                <!-- #section:basics/navbar.user_menu -->
                <li>
                    <a href="#" onclick="_g_fn_show_setting();return false;" id="_global_href_name" style="text-decoration: underline;" title="点击修改姓名和密码"><s:property value="#session.sessionOperator.realname"/></a>
                </li>
                <li><a href="#" onclick="if(confirm('确认退出吗？')){window.location.href='../security/logout.action'}" class="btn btn-power" title="注销"><i
                        class="fa fa-power-off  fa-2x"></i></a></li>
            </ul>
        </div>
        <div id="_admin_setting_dialog">
            <div class="_mini_dialog">
                <div class="row">
                    <form>
                        <div class="form-group">
                        <span class="block input-icon input-icon-left">
                            <input type="text" class="form-control" placeholder="用户名"
                                   data-rel="tooltip" data-original-title=""
                                   id="_global_admin_name" style="width: 320px"
                                   value="<s:property value="#session.sessionOperator.realname"/>"
                                    />
                            <i class="ace-icon fa fa-bookmark"></i>
                        </span>
                        </div>
                        <div class="form-group">
                        <span class="block input-icon input-icon-left">
                            <input type="password" class="form-control" placeholder="不修改密码时保持输入框为空"
                                   data-rel="tooltip" data-original-title=""
                                   id="_global_admin_password" style="width: 320px"/>
                            <i class="ace-icon fa fa-lock"></i>
                        </span>
                        </div>
                        <div class="form-group">
                        <span class="block input-icon input-icon-left">
                            <input type="password" class="form-control" placeholder=""
                                   data-rel="tooltip" data-original-title=""
                                   id="_global_admin_retype_password" style="width: 320px"/>
                            <i class="ace-icon fa fa-lock"></i>
                        </span>
                        </div>
                        <div class="form-group">
                             <a class="btn btn-blue" id="_global_btn_admin_do" onclick="_g_fn_save_setting(<s:property value="#session.sessionOperator.id"/>);return false;">确定</a>
                             <a class="btn btn-white" id="_global_btn_admin_close" onclick="_g_fn_close_setting();return false;">关闭</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- /section:basics/navbar.dropdown -->
    </div>
    <!-- /.navbar-container -->
</div>
