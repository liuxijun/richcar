<%@ page import="com.fortune.common.business.security.model.Admin" %><%@ taglib prefix="s" uri="/struts-tags" %><%
    Admin operatorLogin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
    if(operatorLogin==null){
        response.sendRedirect("login.jsp");
        return;
    }
    int unauditUserCount=getIntSqlResult("select count(*) from car where status=2");
%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="/inc/displayCssJsLib.jsp"%>
    <style type="text/css">
        .main-upload{
        }
        .main-film{
            background-color:#4ecc6e;
        }
        .main-cog{
            background-color:#feb448
        }
        .main-user{
            background-color:#9564e2;
        }
    </style>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<%@include file="/inc/displayHeader.jsp"%>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>

<%@include file="/inc/displayMenu.jsp"%>
<div class="main-content">
    <!-- #section:basics/content.breadcrumbs -->
    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>

        <ul class="breadcrumb">
            <li class="active">
                当前位置:
                <a href="man.jsp"> 管理首页</a>
            </li>
        </ul>
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-desktop"></i>管理首页

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="alert alert-block alert-success">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>

                        <i class="ace-icon fa fa-check green"></i>

                        <%--<%=AppConfigurator.getInstance().getConfig("system.welcomeMessage","欢迎来到Redex Server,管理中心。")%>--%>
                        欢迎来到<%=IndividualUtils.getInstance().getName()%>
                    </div>

                    <div class="row">
                        <div class="col-xs-12   widget-container-col">
                            <!-- #section:custom/widget-box -->
                            <div class="widget-box widget-box1">
<%
    if(operatorLogin!=null&&operatorLogin.getId()==1){//只有root可见
%>
                                <div class="widget-header">
                                    <h5 class="widget-title">系统状态</h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <ul>
<%--
                                            <li><i class="ace-icon fa fa-play-circle-o"></i>
                                                <span class="red3">“<%=liveServerCount%>”</span> 台直播服务器， <span class="red3">“<%=liveChannelCount%>”</span>
                                                直播进行中”。<%
                                                    if(liveChannelCount>0){
%><a class="btn btn-btn">查看直播</a><%
                                                    }
                                                %>
                                            </li>
                                            <li><i class="ace-icon fa fa-tasks"></i>
                                                <span class="red3">“<%=encoderCount%>”</span> 台文件转码服务器， <span class="red3">“<%=runningTaskCount%>”</span>
                                                个转码任务进行中， <span class="red3">“<%=waitingTaskCount%>” </span> 个在排队。
                                                <span class="red3">“<%=finishedCount%>” </span> 个已经完成。
                                                <span class="red3">“<%=otherCount%>” </span> 个异常。
                                                <a class="btn btn-btn" href="sys/encodeTasks.jsp">转码任务</a>
                                            </li>
                                            <li><i class="ace-icon fa fa-eye"></i>
                                                共<span class="red3">“<%=vodServerCount%>”</span>媒体服务器，共<span class="red3">“<%=licenseCount%>”</span> 授权
                                                <a class="btn btn-btn" href="sys/sysInfo.jsp">系统一览</a>
                                            </li>
    --%>
                                            <li><i class="ace-icon fa fa-user"></i>
                                                共
                                                <%=getIntSqlResult("select count(*) from car")%>用户，
                                                <span class="red3" style="font-size:2.0em"><%=unauditUserCount%></span>待审
                                                <a class="btn btn-btn" href="cars/cars.jsp">查看用户</a>
                                            </li>
<%--
                                            <li><i class="ace-icon fa fa-pie-chart"></i>
                                                共 <span class="red3"> “<%=totalStorage%>GB”</span> 可用， <span class="red3">“<%=usedStorage%>GB”</span>
                                                已用， 使用率 <span class="green3">“<%=usedStorage*100/totalStorage%>%”</span></li>
--%>
                                        </ul>

                                    </div>
                                </div>

                                <%
    }
%>                            </div>
                        </div>
                    </div>
                    <div class="space"></div>
                    <div class="row">
<%
    {
        List<Menu>  adminMenus = (List<Menu>) session.getAttribute(Constants.SESSION_ADMIN_MENUS);
        if(adminMenus!=null){
            String remoteAddr = request.getRemoteAddr();
            for(Menu adminMenu:adminMenus){
                if(isMenuClosed(adminMenu,remoteAddr)){
                    continue;
                }
                String parentMenuCls1 = getStringOfSplitIndex(adminMenu.getStyle(),"\\|",0,"");
                String parentMenuCls2 = getStringOfSplitIndex(adminMenu.getStyle(),"\\|",1,"");
%>
                        <div class="col-xs-3   widget-container-col">
                        <!-- #section:custom/widget-box -->
                        <div class="widget-box widget-box2">
                            <div class="widget-header" style="<%=parentMenuCls2%>">
                                <h5 class="widget-title"><a href="#"><i class="menu-icon fa fa-<%=parentMenuCls1%>"></i><%=adminMenu.getName()%></a> </h5>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main">
                                    <div class="row">
                                        <div class="col-xs-12"><%=""%>
                                        </div>
                                        <%
                                            List<Menu> subMenus = adminMenu.getSubMenus();
                                            if(subMenus!=null){
                                                for(Menu subMenu:subMenus){
                                                    if(isMenuClosed(subMenu,remoteAddr)){
                                                        continue;
                                                    }
                                                    String menuStyle = "", bubble = "";
                                                    if("前台用户管理".equals(subMenu.getName()) && unauditUserCount > 0){
                                                        menuStyle = "style='overflow:visible'";
                                                        bubble = "<div class=\"menu_bubble\">" + unauditUserCount +"</div>";
                                                    }
                                        %>
                                        <div class="col-xs-6"><a class="btn btn-btn" <%=menuStyle%> href="<%=subMenu.getUrl()%>"><%=subMenu.getName()%>
                                            <%=bubble%></a></div><%
                                                }
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /section:custom/widget-box -->
                    </div>

<%
            }
        }
    }
%>
                    </div>

                    <div>
                        <p>&nbsp;</p>
                    </div>

                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.page-content-area -->
    </div>
    <!-- /.page-content -->
</div>
<!-- /.main-content -->


<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div>
<!-- /.main-container -->
<%@include file="/inc/displayFooter.jsp"%>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function ($) {



        //Android's default browser somehow is confused when tapping on label which will lead to dragging the task
        //so disable dragging when clicking on label
        var agent = navigator.userAgent.toLowerCase();
        if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
            $('#tasks').on('touchstart', function (e) {
                var li = $(e.target).closest('#tasks li');
                if (li.length == 0)return;
                var label = li.find('label.inline').get(0);
                if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
            });


        $(".menu_bubble").show();
    })
</script>

</body>
</html>
<%@include file="admin/sqlBase.jsp"%>