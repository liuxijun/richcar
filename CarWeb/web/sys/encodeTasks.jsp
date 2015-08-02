<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    boolean debugMode = false;
    {
        String remoteAddrForCheckDebugMode = request.getRemoteAddr();
        if(remoteAddrForCheckDebugMode.startsWith("127.0")||remoteAddrForCheckDebugMode.startsWith("140.206.48.130")
                ||remoteAddrForCheckDebugMode.startsWith("192.168.")||
                remoteAddrForCheckDebugMode.startsWith("172.16.")||
                remoteAddrForCheckDebugMode.startsWith("0:")){
            debugMode = true;
        }
    }
%><!DOCTYPE html>
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
                <i class="ace-icon fa fa-tasks"></i>${functionName}
            </h1>
        </div>
        <!-- /.page-header -->
        <div class="page-content">

            <div class="page-content-area">
                <div class="row page-content-main">
                    <div class="input-group pull-right search-group" style="width: 450px;height: 30px; margin: 9px;">
                        <div style="width: 100px;height: 30px;float: left;">
                            <input style="border-radius:15px" type="text" id="search_taskName" placeholder="任务名" class="form-control search-query" >
                        </div>
                        <div style="width: 100px;height: 30px;float: left;" >
                            <select id="search_status" class="form-control search-query" style="height: 35px;overflow-y: auto;">
                            </select>
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
                    <form role="form" class="form-horizontal">
                        <div class="col-xs-12 no-padding movie-info">
                            <div class="tabbable">
                                <table class="table table-striped table-bordered table-hover table-30">
                                    <thead>
                                    <tr>
                                        <th width="50%" ><a href="#" onclick='taskList.order_by("taskName")' title="按任务名排序">任务名</a></th>
                                        <th class="center" id="taskProcessHeader" ><a href="#" onclick='taskList.order_by("taskProcess")'>完成进度</a></th>
                                        <th class="center" id="taskStartTimeHeader"><a href="#" onclick='taskList.order_by("taskStartTime")'>加入时间</a></th>
                                        <th class="center" id="taskStopTimeHeader"><a href="#" onclick='taskList.order_by("taskStoptTime")'>完成时间</a></th>
                                        <th class="center" id="taskEncoderHeader"><a href="#" onclick='taskList.order_by("desertFileName")'>转码器</a></th>
                                        <th class="center" id="taskStatusHeader"><a href="#"  onclick='taskList.order_by("taskStatus")'>当前状态</a></th>
                                        <th class="center" >操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="tasksContain">
                                    </tbody>
                                </table>
                            </div>
                            <div class="space-6"></div>
                            <div class="row">
                            <%--    <div class="col-md-2"><a class="btn btn-green btn-big" onclick="taskList.showTaskDetail(-1,5);return false;">新建角色 </a></div>--%>
                                 <div class="col-md-7 col-md-offset-5">
                                       <ul id="page-nav" class="pagination pull-right"></ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.row -->
                    </form>
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
    <div class="modal fade modal-film" id="taskDetailModal" tabindex="-1" role="dialog" aria-labelledby="taskDetailModalLabel"
         aria-hidden="true">
        <div class="modal-dialog"
             style="position: absolute; left: 50%; margin-left: -280px; top: 50%; margin-top: -250px; width: 560px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                            class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="taskDetailModalLabel">任务详情</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <form class="form-horizontal">
                            <div class="col-xs-12 ">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">任务名称</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskName"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">任务状态</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskStatus"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">加入时间</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskStartTime"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">完成时间</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskStopTime"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">原始文件</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskSourceFileName"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right">目标文件</label>
                                    <div class="col-sm-9">
                                        <span class="col-sm-12" id="taskDesertFileName"></span>
                                    </div>
                                </div><% if(debugMode){%><div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">日志</label>
                                <div class="col-sm-9" style="max-height: 200px;overflow:auto;">
                                    <span class="col-sm-12" id="taskLog"></span>
                                </div>
                            </div><%
                                }
                            %>
                                <div class="space"></div>
                                <span class="btn btn-blue" onclick="taskList.close()">关闭窗口</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="btn btn-blue" onclick="taskList.restartTask()">重启任务</span>
                            </div>
                            <input type="hidden" name="taskId" id="taskId">
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
                $data = options.additionalParameters.children;
            else $data = {};//no data
        }

        if ($data != null)//this setTimeout is only for mimicking some random delay
            setTimeout(function () {
                callback({ data: $data });
            }, parseInt(Math.random() * 500) + 200);

        //we have used static data here
        //but you can retrieve your data dynamically from a server using ajax call
        //checkout examples/treeview.html and examples/treeview.js for more info
    };

    $(".btn-dropdown").click(function () {
        $("#tree1").slideDown();
    });
    $("#tree1").mouseleave(function () {
        $(this).slideUp();

    });
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
    $("#btn_search").click(function(){
        taskList.goToPage(1);
    });
    taskList.initSearchStatusSelectItems();
    taskList.goToPage(1);
});

var taskList={
    limit:10,
    currentPage:1,
    statusNames:[
        {name:'所有状态',value:"-1"},
        {name:"正在转码",value:"1"},
        {name:"队列中",value:"2"},
        {name:"执行完毕",value:"3"},
        {name:"执行错误",value:"4"},
        {name:"文件不存在",value:"805"},
        {name:"源文件太小",value:"405"},
        {name:"格式不认识",value:"500"},
        {name:"起始时间异常",value:"501"},
        {name:"文件残缺",value:"502"},
        {name:"输出文件错误",value:"504"},
        {name:"未知错误",value:"505"},
        {name:"命令行错误",value:"506"},
        {name:"输出文件时间长度错误",value:"507"},
        {name:"发生异常",value:"508"},
        {name:"发生IO异常",value:"509"},
        {name:"影片源丢失",value:"801"},
        {name:"重复的任务",value:"802"},
        {name:"发生错误",value:"804"},
        {name:"被取消",value:"806"}
    ],
    initSearchStatusSelectItems:function(){
        var selectCmp=$("#search_status");
        var options = taskList.statusNames;
        var i= 0,l=options.length;
        var value = "-1";
        for(;i<l;i++){
            var option = options[i];
            var selected = false;
            var val = parseInt(option['value']);
            if(val==value){
                selected = true;

            }
            selectCmp.append($("<option/>", {
                value: val,
                selected:selected,
                text: option.name
            }));
//            result+='<option value="'+option['value']+'" ' +selected+'>'+option['name']+'</option>';
        }
    },
    getTaskStatus:function(status){
        return taskList.getTextOfArray(status+"",taskList.statusNames,"value","name");
    },
    renderTask:function (jsonData) {
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /taskList.limit),
                taskList.currentPage, "taskList.goToPage", jsonData['totalCount']);
        var tasks = jsonData['objs'];
        var result = '';
        if(tasks!=null){
            var i= 0,l=tasks.length;
            for(;i<l;i++){
                var task = tasks[i];
                result +=
                        '<tr>' +
                                '<td>' + task['et_DOT_name']+'</td>' +
                                '<td class="center">' + task['et_DOT_process']+'</td>' +
                                '<td class="center">' + task['et_DOT_startTime']+'</td>' +
                                '<td class="center">' + task['et_DOT_stopTime']+'</td>' +
                                '<td class="center">' + task['d_DOT_name']+'</td>' +
                                '<td class="center">' + taskList.getTaskStatus(task['et_DOT_status'])+'</td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick="taskList.showTaskDetail(' +task['et_DOT_id']+
                                ');return false;">'+
                                '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                '  </a></td>'+
                                '</tr>';
            }
        }
        $('.tasksContain').html(result);

    },
    order_by:function (v) {
        if (v == taskList.order) {
            taskList.dir = ( taskList.dir == "asc") ? "desc" : "asc";
        } else {
            taskList.order = v;
            taskList.dir = "desc";
        }
        taskList.listTasks();
    },
    listTasks:function(){
        var _order = "et.stopTime";
        switch (taskList.order) {
            case "taskName":
                _order = "et.name";
                break;
            case "taskStatus":
                _order = "et.status";
                break;
            case "taskStartTime":
                _order = "et.startTime";
                break;
            case "taskStopTime":
                _order = "et.stopTime";
                break;
            case "taskProcess":
                _order = "et.process";
                break;

            case "desertFileName":
                _order = "et.desertFileName";
                break;
            default:
                    _order = "et.startTime";
                    taskList.dir = "desc";
                break;
        }
        $.ajax({
            url:'../encoder/encoderTask!searchTask.action',
            data:({"obj.name":$("#search_taskName").val(), "obj.status":$("#search_status").val(),
                "beginDay":$("#searchStartTime").val(), "endDay":$("#searchStopTime").val(),
                "pageBean.pageSize":taskList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":taskList.dir,
                limit:taskList.limit,start:(taskList.currentPage-1)*taskList.limit}),

            dataType:'json',
            method:'post',
            success:function(jsonData){
                taskList.renderTask(jsonData);
            }
        });
    },
    currentTaskId:-1,
    currentTaskType:-1,
    close:function(){
        $("#taskDetailModal").modal('hide');
    },
    restartTask:function(){
        if(!confirm("确认重新启动这个任务吗？")){
           return false;
        }
        var taskId = $("#taskId").val();
        if(taskId==null||taskId<=0||typeof(taskId)=='undefined'){
           taskId=taskList.currentTaskId;
        }
        $.ajax({
            url:'/encoder/encoderTask!restart.action?keyId='+taskId,
            method:'post',
            data:{
            },
            dataType:'json',
            success:function(jsonData){
                alert("重启指令已经受理！");
                taskList.goToPage(1);
                $("#taskDetailModal").modal('hide');
            }
        });
        return true;
    },
    showTaskDetail:function(id,type){
        taskList.currentTaskId=id;
        type = parseInt(type);
        taskList.currentTaskType=type;
        $("#taskDetailModal").modal('show');
        $.ajax({
            url:'/encoder/encoderTask!view.action?keyId='+id,
            dataType:'json',
            success:function(jsonData){
                var data = jsonData['data'];
                var menusInfo = '';
                if(data!=null){
                    $("#taskName").html(data['obj.name']);
                    $("#taskStartTime").html(data['obj.startTime']);
                    $("#taskStopTime").html(data['obj.stopTime']);
                    $("#taskLog").html('<pre>'+data['obj.encodeLog']+'</pre>');
                    $("#taskProcess").html(data['obj.process']);
                    $("#taskSourceFileName").html(data['obj.sourceFileName']);
                    $("#taskDesertFileName").html(data['obj.desertFileName']);
                    taskList.currentTaskId = data['obj.id'];
                    $("#taskId").val(taskList.currentTaskId);
                    taskList.currentTaskType = data['obj.type'];
                    var status =data['obj.status'];
                    var statusText = taskList.getTaskStatus(status);
                    $("#taskStatus").html(statusText);
                }
            }
        });
    },
    goToPage:function(pageNo){
        taskList.currentPage = pageNo;
        taskList.listTasks();
    } ,
    getTextOfArray:function(val,data,valueField,displayField){
        var i= 0,l=data.length;
        for(;i<l;i++){
            var m = data[i];
            if(m[valueField]==val){
                return m[displayField];
            }
        }
        return '未知';
    }
};

</script>
</body>
</html>
