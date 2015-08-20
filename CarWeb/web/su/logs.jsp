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
            <i class="ace-icon fa fa-user"></i>安全日志
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
        <div class="page-content-area">
            <div class="row page-content-main">

                <div class="input-group pull-right search-group" style="width: 500px;height: 30px; margin: 9px;">
                    <div style="width: 150px;height: 30px;float: left;">
                        <input type="text" id="search_word" placeholder="日志内容" class="form-control" >
                    </div>
                    <div style="width: 100px;height: 30px;float: left;">
                        <input type="text" id="search_admin" placeholder="操作员ID" class="form-control search-query" >
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
                                <th width="50%">日志内容</th>
                                <th class="center">ID
                                    <img  id="v5" style="cursor: pointer;width: 25px;height: 25px;"  onclick='logsList.order_by("adminId")' src="/images/sort_asc.png">
                                    <img id="v6" style="display: none;cursor: pointer;width: 25px;height: 25px;" onclick='logsList.order_by("adminId")' src="/images/sort_desc.png">
                                </th>
                                <th class="center">IP
                                    <img  id="v3" style="cursor: pointer;width: 25px;height: 25px;"onclick='logsList.order_by("adminIp")' src="/images/sort_asc.png">
                                    <img id="v4" style="display: none;cursor: pointer;width: 25px;height: 25px;" onclick='logsList.order_by("adminIp")'src="/images/sort_desc.png">
                                </th>
                                <th class="center">时间
                                        <img  id="v1"style="cursor:pointer;width: 25px;height: 25px;" onclick='logsList.order_by("logTime")' src="/images/sort_asc.png">
                                        <img id="v2" style="display: none;cursor: pointer;width: 25px;height: 25px;" onclick='logsList.order_by("logTime")' src="/images/sort_desc.png">
                                </th>
                            </tr>
                            </thead>

                            <tbody id="logsContain">
                            </tbody>
                        </table>
                    </div>
                    <div class="space-6"></div>
                    <div class="row">
                        <div class="col-md-2"><span class="btn btn-blue" onclick="logsList.exportLogs()">导出当前页日志</span></div>
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
                                <label class="col-sm-3 control-label no-padding-right">日志内容</label>
                                <div class="col-sm-9" style="max-height: 175px;overflow-y:auto">
                                    <span class="col-sm-12" id="systemLogContent"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">管理员</label>
                                <div class="col-sm-9">
                                    <span  class="col-sm-12" id="adminId"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">IP</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="adminIp"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">时间</label>
                                <div class="col-sm-9">
                                    <span class="col-sm-12" id="logTime"></span>
                                </div>
                            </div>
                            <hr>
                            <div class="space"></div>
                            <span class="btn btn-blue" onclick="$('#LogDetailModal').modal('hide');">关闭</span>
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
var page_size = 10;
jQuery(function ($) {
    $('.scrollable').each(function () {
        var $this = $(this);
        $(this).ace_scroll({
            size: $this.data('size') || 100
            //styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
        });
    });
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

    $("#open-movie-title-boxs").click(function () {
        var $a = $(".movie-title-boxs")
        //noinspection JSUndeclaredVariable
        $this = $(this);
        if ($a.is(':visible')) {
            $a.slideUp(300, function () {
                $this.html("查看更多文件");
            });
        } else {
            $a.slideDown(300, function () {
                $this.html("收起");
            });


        }
    });

    $('#id-input-file-1,#id-input-file-2').ace_file_input({
        no_file: '未选择文件 ...',
        btn_choose: '选择',
        btn_change: '选择',
        droppable: false,
        onchange: null,
        thumbnail: false //| true | large
        //whitelist:'gif|png|jpg|jpeg'
        //blacklist:'exe|php'
        //onchange:''
        //
    });


    var DataSourceTree = function (options) {
        this._data = options.data;
        this._delay = options.delay;
    }

    DataSourceTree.prototype.data = function (options, callback) {
        var self = this;
        var $data = null;

        if (!("name" in options) && !("type" in options)) {
            $data = this._data;//the root tree
            callback({ data: $data });
            return;
        }
        else if ("type" in options && options.type == "folder") {
            if ("additionalParameters" in options && "children" in options.additionalParameters)
            {
                $data = options.additionalParameters.children;
            }
            else {
                $data = {}
            }
        }

        if ($data != null)
            setTimeout(function () {
                callback({ data: $data });
            }, parseInt(Math.random() * 500) + 200);
    };

    $(".btn-dropdown").click(function () {
        $("#tree1").slideDown();
    });
    $("#tree1").mouseleave(function () {
        $(this).slideUp();

    });
    var agent = navigator.userAgent.toLowerCase();
    if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
        $('#tasks').on('touchstart', function (e) {
            var li = $(e.target).closest('#tasks li');
            if (li.length == 0)return;
            var label = li.find('label.inline').get(0);
            if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
        });
    $("#btn_search").click(function(){
        logsList.goToPage(1);
    });
    $(function(){
        $("#v1").click(function(){
            $("#v1").hide();
            $("#v2").show()
        });
        $("#v2").click(function(){
            $("#v2").hide();
            $("#v1").show()
        });
    });
    $(function(){
        $("#v3").click(function(){
            $("#v3").hide();
            $("#v4").show()
        });
        $("#v4").click(function(){
            $("#v4").hide();
            $("#v3").show()
        });
    });
    $(function(){
        $("#v5").click(function(){
            $("#v5").hide();
            $("#v6").show()
        });
        $("#v6").click(function(){
            $("#v6").hide();
            $("#v5").show()
        });
    });
    logsList.goToPage(1);
});

var logsList;
logsList = {
    limit:50,
    currentPage:1,
    order:'logTime',
    dir:'desc',
    logs:[],
     renderLogs:function (jsonData) {
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /logsList.limit),
               logsList.currentPage,"logsList.goToPage", jsonData['totalCount']);
        var roles = jsonData['objs'];
        var result = '';

        if (roles != null) {
            logsList.logs = roles;
            var i = 0, l = roles.length;
            for (; i < l; i++) {
                var role = roles[i];
                result +=
                        '<tr>' +
                                '<td><div style="width:500px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">' + role.log + '</div></td>' +
                                '<td class="center">' + role.adminName + '</td>' +
                                '<td class="center">' + role.adminIp + '</td>' +
                                '<td class="center">' + role.logTime + '</td>' +
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


    listLogs:function (toUrl) {
        var _order = "sl.logTime";
        switch (logsList.order) {
            case "adminId":
                _order = "sl.adminId";
                break;
            case "adminIp":
                _order = "sl.adminIp";
                break;
            case "logTime":
                _order = "sl.logTime";
                break;
        }
        $("#order_by_adminId").attr("title", (logsList.order == "adminId" && logsList.dir == "desc") ? "按登录名倒序排序" :
                "按登录名排序");
        $("#order_by_adminIp").attr("title", (logsList.order == "adminIp" && logsList.dir == "desc") ? "按IP倒序排序" :
                "按IP排序");
        $("#order_by_logTime").attr("title", (logsList.order == "logTime" && logsList.dir == "desc") ? "按登录时间倒序排序":
                "按登录时间排序");
        var parameters ={"adminName":$("#search_admin").val(), "log":$("#search_word").val(),
            "startTime":$("#searchStartTime").val(), "stopTime":$("#searchStopTime").val(),
            "pageBean.pageSize":logsList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":logsList.dir,
            limit:logsList.limit, start:(logsList.currentPage - 1) * logsList.limit };
        if(typeof(toUrl)=='undefined'||toUrl==null){
            $.ajax({
                url:'../system/systemLog!list.action',
                data:parameters,
                method:'post',
                dataType:'json',
                success:function (jsonData) {
                    logsList.renderLogs(jsonData);
                }
            });
        }else{
            var i=toUrl.indexOf("?");
            if(i>0){
                toUrl+="&";
            }else{
                toUrl +="?";
            }
            for(var p in parameters){
                if(parameters.hasOwnProperty(p)){
                    toUrl+=p+"="+encodeURI(encodeURI(parameters[p]))+"&";
                }
            }
            window.location.href = toUrl;
        }
    },


    currentLogId:-1,
    showLogDetail:function (index) {
        $("#LogDetailModal").modal('show');
        var data = logsList.logs[index];
        if (data != null) {
            logsList.currentLogId = data.id;
            $("#systemLogContent").html(data.log);
            $("#adminIp").html(data.adminIp);
            $("#adminId").html(data.adminName);
            $("#logTime").html(data.logTime);

        }
    },
    goToPage:function (pageNo) {
        logsList.currentPage = pageNo;
        logsList.listLogs();
    },
    exportLogs:function(){
        logsList.listLogs('../system/systemLog!export.action');
    }
};
</script>

</body>
</html>