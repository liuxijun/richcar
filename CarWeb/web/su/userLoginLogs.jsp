<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<%@include file="../inc/displayHeader.jsp" %>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>
<%@include file="/inc/displayMenu.jsp" %>
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
            <li>
                当前位置:
                <a href="../man.jsp"> 网站首页</a>
            </li>
            <li class="active">${folderName}</li><li class="active">${functionName}</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-user"></i>用户登陆日志
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
        <div class="page-content-area">
            <div class="row page-content-main">

                <div class="input-group pull-right search-group" style="width:500px;height: 30px; margin: 9px;">
                    <div style="width: 150px;height: 30px;float: left;">
                        <input type="text" id="searchLogin" placeholder="用户ID" class="form-control search-query" >
                    </div>
                    <div style="width: 100px;height: 30px;float: left;">
                       <select id="searchloginStatus" class="form-control search-query">
                           <option value="" onclick="logsList.goToPage(1)">全部</option>
                           <option value="1"onclick="logsList.goToPage(1)">PC</option>
                           <option value="2" onclick="logsList.goToPage(1)">PAD</option>
                           <option value="3" onclick="logsList.goToPage(1)">PHONE</option>
                           <option value="4" onclick="logsList.goToPage(1)">OA一键登录</option>
                           <option value="5" onclick="logsList.goToPage(1)">其他</option>

                       </select>
                     <%--  <input type="text" placeholder="登陆方式" class="form-control search-query" >--%>
                    </div>
                    <div>
                        <div style="width: 100px;height: 30px;float: left;">
                            <input type="text" id="searchStartTime" placeholder="起始时间" class="form-control search-query" >
                        </div>
                        <div style="width: 100px;height: 30px;float: left;">
                            <input type="text" id="searchStopTime" placeholder="截止时间" class="form-control search-query">
                        </div>
                    </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                </div>
                <div class="col-xs-12 no-padding movie-info">
                    <div class="tabbable">
                        <table class="table table-striped table-bordered table-hover table-30">
                            <thead>
                            <tr>
                                <th class="center"><a href="#" onclick='logsList.order_by("login")'>用户ID</a></th>
                                <th class="center"><a href="#" onclick='logsList.order_by("addr")'>地址</a></th>
                                <th class="center"><a href="#" onclick='logsList.order_by("login_status")'>登陆方式</a></th>
                                <th class="center"><a href="#" onclick='logsList.order_by("tel")'>姓名</a></th>
                                <th class="center"><a href="#" onclick='logsList.order_by("loginTime")'>时间</a></th>
                            </tr>
                            </thead>

                            <tbody id="logsContain">
                            <tr>
                                <td class="center">456546</td>
                                <td class="center"> 223.166.67.100</td>
                                <td class="center">123</td>
                                <td class="center">123</td>
                                <td class="center">2014-9-9 18:32</td>

                            </tr>

                            <tr>
                                <td class="center">456546</td>
                                <td class="center"> 223.166.67.100</td>
                                <td class="center">123</td>
                                <td class="center">123</td>
                                <td class="center">2014-9-9 18:32</td>
                            </tr>
                            <tr>
                                <td class="center">456546</td>
                                <td class="center"> 223.166.67.100</td>
                                <td class="center">123</td>
                                <td class="center">123</td>
                                <td class="center">2014-9-9 18:32</td>
                            </tr>
                            <tr>
                                <td class="center">456546</td>
                                <td class="center"> 223.166.67.100</td>
                                <td class="center">123</td>
                                <td class="center">123</td>
                                <td class="center">2014-9-9 18:32</td>
                            </tr>
                            <tr>
                                <td class="center">456546</td>
                                <td class="center"> 223.166.67.100</td>
                                <td class="center">123</td>
                                <td class="center">123</td>
                                <td class="center">2014-9-9 18:32</td>
                            </tr>


                            </tbody>
                        </table>
                    </div>
                    <div class="space-6"></div>
                    <div class="row">
                        <div class="col-md-2"> </div>
                        <div class="col-md-6 col-md-offset-4">
                            <ul id="page-nav" class="pagination pull-right">
                                <li class="disabled">
                                    <a href="#">
                                        首页
                                    </a>
                                </li>
                                <li class="disabled">
                                    <a href="#">
                                        上一步
                                    </a>
                                </li>
                                <li class="active">
                                    <a href="#">1</a>
                                </li>

                                <li>
                                    <a href="#">2</a>
                                </li>

                                <li>
                                    <a href="#">3</a>
                                </li>

                                <li>
                                    <a href="#">4</a>
                                </li>

                                <li>
                                    <a href="#">5</a>
                                </li>

                                <li>
                                    <a href="#">
                                        下一步
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        尾页
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
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
<%@include file="../inc/displayFooter.jsp" %>
<!-- Modal -->
<%--suppress HtmlUnknownAttribute --%>
<div class="modal fade modal-film" id="LogDetailModal" tabindex="-1" role="dialog" aria-labelledby="LogDetailModal"
     aria-hidden="true">
    <div class="modal-dialog"
         style="position: absolute; left: 50%; margin-left: -280px; top: 50%; margin-top: -250px; width: 560px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="LogDetailModalLabel">内容详情</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <form class="form-horizontal">
                        <div class="col-xs-12">
                            <div class="form-group"  >
                                <label class="col-sm-3 control-label no-padding-right">用户ID</label>
                                <div class="col-sm-9" style="max-height: 175px;overflow-y:auto">
                                    <span class="col-sm-12" id="login"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">地址</label>
                                <div class="col-sm-9">
                                    <span  class="col-sm-12" id="addr"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">联系电话</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="tel"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">时间</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="loginTime"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">登陆方式</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="loginStatus"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">描述</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="desp"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="space"></div>
                            <span class="btn btn-blue" onclick="$('#LogDetailModal').modal('hide');">关闭</span>
                            <input type="hidden" id="areaId">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="display: none;">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Save changes</button>
        </div>
    </div>
</div>
</div>
<!-- inline scripts related to this page -->
<script type="text/javascript">
var page_index = 1;
var page_size = 5;
jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html: true});
    $('textarea[class*=autosize]').autosize({append: "\n"});
    $(document).ajaxStart(function(){
        $("#loading_container").show();
    });

    $(document).ajaxStop(function(){
        setTimeout(function(){$("#loading_container").hide();}, 200);
    });


    $('textarea.limited').inputlimiter({
        remText: '%n character%s remaining...',
        limitText: 'max allowed : %n.'
    });

    $(".btn-dropdown").click(function () {
        $("#tree1").slideDown();
    });
    $("#tree1").mouseleave(function () {
        $(this).slideUp();

    });
    $("#btn_search").click(function(){
        logsList.goToPage(1);
    });
    logsList.goToPage(1);
});

var logsList;
logsList = {
    limit:10,
    dir:'desc',
    currentPage:-1,
    logs:[],
    loginStatus:[
        {name:"PC",value:"1"},
        {name:"PAD",value:"2"},
        {name:"PHONE",value:"3"},
        {name:"OA一键登录",value:"4"}
    ],
    getLoginStatus:function(loginStatus){
        return logsList.getTextOfArray(loginStatus+"",logsList.loginStatus,"value","name");
    },
    renderLogs:function (jsonData) {
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /logsList.limit),
                logsList.currentPage,"logsList.goToPage", jsonData['totalCount']);
        var users = jsonData['objs'];
        var result = '';

        if (users != null) {
            logsList.logs = users;
            var i = 0, l = users.length;
            for (; i < l; i++) {
                var user = users[i];
                result +=
                        '<tr>' +
                                '<td class="center">' + user.login + '</td>' +
                                '<td class="center">' + user.addr + '</td>' +
                              /*  '<td class="center">' + user.loginStatus + '</td>' +*/
                                '<td class="center">' + logsList.getLoginStatus(user['loginStatus'])+'</td>' +
                                '<td class="center">' + user.tel + '</td>' +
                                '<td class="center">' + user.loginTime + '</td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick="logsList.showLogDetail(' + i +
                                ');return false;">' +
                                '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                                '</td>' +
                                '</tr>';
            }
        }
        $('#logsContain').html(result);
    },
    order_by:function (v) {
        if (v == logsList.order) {
            logsList.dir = ( logsList.dir == "asc") ? "desc" : "asc";
        } else {
            logsList.order = v;
            logsList.dir = "desc";
        }
        logsList.listLogs();
    },
    listLogs:function () {
        var _order = "ul.loginTime";

        switch (logsList.order) {
            case "login":
                _order = "ul.login";
                break;
            case "addr":
                _order = "ul.addr";
                break;
            case "loginStatus":
                _order = "ul.loginStatus";
                break;
            case "tel":
                _order = "ul.tel";
                break;
            case "loginTime":
                _order = "ul.loginTime";
                break;

        }
        $.ajax({
            url:'/user/userlogin!list.action',
            data:({"startTime":$("#searchStartTime").val(),
                "stopTime":$("#searchStopTime").val(),
                "login":$("#searchLogin").val(),
               "loginStatus":$("#searchloginStatus").val(),
                "pageBean.pageSize":logsList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":logsList.dir,
                limit:logsList.limit, start:(logsList.currentPage - 1) * logsList.limit }),
            method:'post',
            dataType:'json',
            success:function (jsonData) {
                logsList.renderLogs(jsonData);
            }
        });
    },
    currentLogId:-1,
    showLogDetail:function (index) {
        $("#LogDetailModal").modal('show');
        var data = logsList.logs[index];
        if (data != null) {
            logsList.currentLogId = data.id;
            $("#login").html(data.login);
            $("#addr").html(data.addr);
            $("#tel").html(data.tel);
            $("#desp").html(data.desp);
            $("#areaId").html(data.areaId);
         /*   $("#loginStatus").html(data.loginStatus);*/
            $("#loginTime").html(data.loginTime);
            var loginStatus =data.loginStatus;
            var statusText = logsList.getLoginStatus(loginStatus);
            $("#loginStatus").html(statusText);
        }
    },
    goToPage:function (pageNo) {
        logsList.currentPage = pageNo;
        logsList.listLogs();
    },
     getTextOfArray:function(val,data,valueField,displayField){
     var i= 0,l=data.length;
     for(;i<l;i++){
     var m = data[i];
     if(m[valueField]==val){
     return m[displayField];
     }
     }
     return '其他';
     }
};
</script>

</body>
</html>