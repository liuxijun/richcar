<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-13
  Time: 14:35:54
  任务一览/ 正常任务/异常任务/栏目内容
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    System.out.println("Come on baby");
%><!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>任务一览 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <link rel="stylesheet" href="../style/fortune.posters.css"/>
    <style type="text/css">
        .movie-poster li{ display: inline-block;width: 160px; height: 120px; margin: 10px 17px 10px 0;position: relative;}
        .movie-poster li img{ width: 160px; height: 120px;}
        .movie-poster li h5{ text-align: center; line-height: 20px;}
    </style>
    <link rel="stylesheet" href="../style/bootstrap-datetimepicker.min.css"/>
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

<!-- #section:basics/sidebar -->
<%@include file="/inc/displayMenu.jsp" %>
<!-- /section:basics/sidebar -->
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
            <a href="../man.jsp">管理首页</a>
        </li>
        <li>发布视频</li>
        <li class="active">内容一览</li>
    </ul>
    <!-- /.breadcrumb -->


    <!-- /section:basics/content.searchbox -->
</div>

<!-- /section:basics/content.breadcrumbs -->
<div class="page-header">
    <h1>
        <i class="ace-icon fa fa-upload"></i>内容一览

    </h1>
</div>
<!-- /.page-header -->
<div class="page-content">

    <div class="page-content-area">


        <div class="row page-content-main">

            <form role="form" class="form-horizontal">


                <div class="col-xs-12 no-padding movie-info">


                    <div class="tabbable">
                        <ul id="myTab" class="nav nav-tabs">
                            <li>
                                <a href="#" data-toggle="tab" onclick="to_abnormal();return false;">
                                    异常任务
                                </a>
                            </li>

                            <li class="active">
                                <a href="#" data-toggle="tab">
                                    正常任务
                                </a>
                            </li>
                            <li class="">
                                <a href="#" data-toggle="tab" onclick="to_channel_content();return false;">
                                    栏目内容
                                </a>
                            </li>


                        </ul>

                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="task-normal">
                                <table class="table table-striped table-bordered table-hover table-30">
                                    <thead>
                                    <tr>
                                        <th width="70%"><a href="#" onclick='order_by("name")' title="按名称排序" id="order_by_name">名称</a></th>
                                        <th class="center"><a href="#" onclick='order_by("status")' title="按状态排序" id="order_by_status">状态</a></th>
                                        <th class="center"><a href="#" onclick='order_by("createTime")' title="按发布时间排序" id="order_by_time">发布时间</a></th>
                                        <th class="center">操作</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    </tbody>
                                </table>

                                <div class="row">
                                    <div class="col-md-2"></div>
                                    <div class="col-md-6 col-md-offset-4">
                                        <ul class="pagination pull-right" id="page-nav">
                                        </ul>
                                    </div>
                                </div>

                            </div>

                            <div class="tab-pane fade" id="class-content">
                                栏目内容
                            </div>


                        </div>
                    </div>
                    <div class="space-10"></div>


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

<div class="modal fade modal-film" id="content_model" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: relative; top: 40px;width: 560px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">设置</h4>
            </div>
            <form action="/media/modifyContent.action" method="post" enctype="multipart/form-data" class="form-horizontal" id="modify-form">
            <div class="modal-body">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="contentTab">
                        <li class="active">
                            <a data-toggle="tab" href="#baseinfo_panel" id="tab_baseinfo">
                                视频信息
                            </a>
                        </li>
                        <li class="">
                            <a data-toggle="tab" href="#poster_panel" id="tab_poster">
                                海报
                            </a>
                        </li>
                        <li class="">
                            <a data-toggle="tab" href="#channel_panel" id="tab_channel">
                                栏目
                            </a>
                        </li>
                        <li class="" id="tab_file_list">
                            <a data-toggle="tab" href="#file_panel">
                                视频文件
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="baseinfo_panel" class="tab-pane fade active in">
                            <div class="row page-content-main">
                                <div class="col-xs-12 no-padding" id="baseInfoBox">
                                    <div class="form-group" id="movieTypeBox">
                                        <label class="col-sm-3 control-label filed-need">类别</label>

                                        <div class="col-sm-7">
                                            <select class="form-control " id="movie-type" name="obj.moduleId">
                                            </select>
                                        </div>
                                    </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label filed-need">名称</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="col-sm-12" id="content-name" name="obj.name">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">主创</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="col-sm-12" id="content-actor" name="obj.actors">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label filed-need">时间</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="col-sm-12" id="content-time" name="obj.property3">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">简介</label>
                                            <div class="col-sm-9">
                                                <textarea
                                                        style="  overflow: hidden; word-wrap: break-word; resize: horizontal; height: 69px;"
                                                        class="autosize-transition form-control" id="content-intro" name="obj.intro"></textarea>
                                            </div>
                                        </div>
                                </div>
                            </div>

                        </div>
                        <div id="poster_panel" class="tab-pane fade">
                            <div class="row page-content-main">
                                <div class="col-xs-12" id="picInfoBox">
                                    <h5>海报</h5>
                                    <div class="file-thumb"><img src="" id="content-poster" style="max-height:200px">
                                    </div>
                                    <input type="file" id="id-input-file-poster" name="moviePoster"/>
                                    <h5>大海报</h5>
                                    <div class="file-thumb"><img src="" id="content-big-poster" style="max-height:200px"></div>
                                    <input type="file" id="id-input-file-big-poster" name="movieBigPoster"/>
                                    <h6 class="tips">用于首页推荐，建议大小800x640</h6>
                                </div>
                            </div>
                        </div>
                        <div id="channel_panel" class="tab-pane fade">
                            <div class="row page-content-main">

                                <div class="col-xs-12">
                                    <h5>选择媒体栏目</h5>

                                    <div id="channel-container" style="margin:0;padding:0">
                                    <div id="tree-channel" class="tree" style="height: 220px;overflow:scroll"></div>
                                    </div>
                                    <hr>
                                    <h5>限定用户类型</h5>

                                    <div class="control-group control-group-inline" id="user-type-container">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="file_panel" class="tab-pane fade file-list">
                            <div class="row page-content-main">
                                <div class="col-xs-12 ">
                                    <div class="row" style="background-color: #494e5b; height: 200px;">
                                    </div>
                                </div>
                                <div class="space"></div>
                                <div class="col-xs-12 " id="file-container" style="height: 200px;overflow:auto">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="space"></div>
                <button class="btn btn-blue" id="btn-confirm-modify" type="button">确认修改</button>
                <button class="btn btn-lightwhite " style="margin-left: 12px;" id="btn_cancel"  type="button">关闭</button>
            </div>
                <input type="hidden" name="obj.id" id="content_id">
                <input type="hidden" name="channelIds" id="contentChannel"/>
                <input type="hidden" name="obj.userTypes" id="contentUserType"/>
                <input type="hidden" name="nextPageFlag" value="1">
            </form>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<!-- basic scripts -->

<script src="../scripts/channel.min.js?v=1.423"></script>
<script src="../scripts/content.status.min.js?v=1"></script>
<script src="../scripts/contentDisplay.js"></script>
<script type="text/javascript" src="../scripts/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="../scripts/bootstrap-datetimepicker.zh-CN.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
     var page_index = 1;
     var page_size = 15;
     var page_count = 0;
     var order = "createTime";
     var dir = "asc";
     var channelReset = false;

     var _TREE_NODE_PREFIX = 'NODE_';
     var __channels = "";
     var __grantChannels = "";
     var treeUtils;
     var _isRoot = <s:if test="#session.sessionOperator.isRoot">true;</s:if><s:else>false;</s:else>

    jQuery(function ($) {
        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html:true});

        $("#tab_file_list").hide();
        $("#btn_cancel").click(function(){$("#content_model").modal('hide');});
        $("#btn-confirm-modify").click(function(){ save_modify(); });

        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        

        page_index = 1;
        get_content();

        // 初始化栏目树
        loadChannel();
        // 初始化用户类型
        loadUserType();
        // 初始化类型
        loadMovieType();
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
        $('#id-input-file-poster').ace_file_input({
            no_file:'未选择文件 ...',
            btn_choose:'选择',
            btn_change:'选择',
            droppable:false,
            //onchange:null,
            //thumbnail:small,
            //thumbnail:false //| true | large
            whitelist:'png|jpg|jpeg',
            //blacklist:'exe|php'
            before_change:function(files, dropped) {
                if (files.length > 0) {
                    $(this).closest("div.form-group").removeClass("has-error");
                    var file = files[0];

                    if (typeof file === 'string') { // 不预览海报
                    } else if ('File' in window && file instanceof window.File) {
                        readURL(this, false);
                    }
                }
                return true;
            }
        });

        $('#id-input-file-big-poster').ace_file_input({
            no_file:'未选择文件 ...',
            btn_choose:'选择',
            btn_change:'选择',
            droppable:false,
            onchange:null,
            //thumbnail:small,
            //thumbnail:false //| true | large
            whitelist:'png|jpg|jpeg',
            //blacklist:'exe|php'
            before_change:function(files, dropped) {
                if (files.length > 0) {
                    var file = files[0];

                    if (typeof file === 'string') { // 不预览海报
                    } else if ('File' in window && file instanceof window.File) {
                        readURL(this, true);
                    }
                }
                return true;
            }
        });

        function readURL(input, bigPoster) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    //$('#blah').attr('src', e.target.result);
                    if (bigPoster) {
                        $("#content-big-poster").attr('src', e.target.result).show();
                    } else {
                        $("#content-poster").attr('src', e.target.result).show();
                    }
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    });

    function get_content(){
        var start_index = (page_index - 1) * page_size;

        $.ajax({
            type: "POST",
            url: "/content/normalContent.action",
            //dataType: "json",
            dataType: "text",
            //data: {"channelId":ch_id, "c_name":search_value,"c.status": 2, "cc.status":2, "isSpechialExit": null},
            data: {"isSpechialExit": null, start:start_index, limit:page_size, sort:order, dir:dir},
            success: function(msg) {
                $("tbody").html("");
                var response = eval("(function(){return " + msg + ";})()");
                var td_html = "";
                for (var n = 0; n < response.objs.length; n++) {
                    var o = response.objs[n];
                    if (o) {
                        var tipTitle="查看";
                        if(o.status!=_STATUS_ONLINE){
                            tipTitle = "编辑";
                        }
                        tipTitle+= "《"+o['c_name']+"》";
                        td_html = '<tr><td><a href="#"  data-rel="tooltip" data-original-title="' +tipTitle+
                                '"' +
                                ' onclick="show_edit_dialog(' + o['c_id'] + ','+ o['c_status']+','+ o['c_moduleId']+','+ o['c_deviceId']+');return false">' + o.c_name + '</a></td>' +
                                  '<td class="center">' + get_status_name(o['c_status']) + '</td>' +
                                  '<td>' + o['c_createTime'] + '</td>' +
                                  '<td class="center">';
                        if(parseInt(o.c_status) == _STATUS_OFFLINE){
                            td_html += '<a class="btn btn-info btn-xs" data-rel="tooltip" data-original-title="上线" data-placement="bottom" href="#" onclick="online(' + o.c_id + ');return false;"><i class="ace-icon fa fa-square-o bigger-110 icon-only"></i></a>';
                        }
                        if(parseInt(o.c_status) != _STATUS_ONLINE){
                            td_html += '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-original-title="编辑" data-placement="bottom" href="#" onclick="show_edit_dialog(' + o['c_id']+ ','+ o['c_status']+','+ o['c_moduleId']+','+ o['c_deviceId']+ ');return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>';
                        }
                        if(parseInt(o.c_status) == _STATUS_ONLINE){
                            td_html += '<a class="btn btn-warning btn-xs" data-rel="tooltip" data-original-title="下线" data-placement="bottom" href="#" onclick="offline(' + o.c_id + ');return false;"><i class="ace-icon fa fa-check-square-o bigger-110 icon-only"></i></a>';
                            //td_html += '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-original-title="追集" data-placement="bottom"><i class="ace-icon fa fa-times-circle fa-rotate-45  bigger-110 icon-only"></i></a>';
                        }
                        td_html += '</td></tr>';
                        $("tbody").append(td_html);
                    }
                }
                $('[data-rel=tooltip]').tooltip();
                
                // reset page nav
                var record_count = parseInt(response.totalCount);
                var page_count = Math.ceil(record_count / page_size);
                rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "to_page", record_count);
            }
        }).fail(function(){
            alert("失败了，重新登录试试吧！");
        });
    }

     function to_page(index){
         page_index = index;
         get_content();
     }

    function order_by(v){
        if(v == order){
            dir = (dir == "asc")? "desc" : "asc";
        }else{
            order = v;
            dir = "asc";
        }

        $("#order_by_name").attr("title", (order == "name" && dir == "desc")?  "按名称倒序排序" : "按名称排序");
        $("#order_by_status").attr("title", (order == "status" && dir == "desc")?  "按状态倒序排序" : "按状态排序");
        $("#order_by_time").attr("title", (order == "createTime" && dir == "desc")?  "按发布时间倒序排序" : "按发布时间排序");
        page_index = 1;

        get_content();
    }

    function online(id){
       $.ajax({
            type: "POST",
            url: "/content/content!cpChangeStatus.action",
            //dataType: "json",
            dataType: "text",
            data: {status:_STATUS_WATING_ONLINE,keyIds:id},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success){
                    get_content();
                }else{
                    alert($.isArray(response.error)? response.error[0] : response.error);
                }
            }
       });
    }

    function offline(id){
        $.ajax({
             type: "POST",
             url: "/content/content!cpChangeStatus.action",
             //dataType: "json",
             dataType: "text",
             data: {status:_STATUS_WATING_OFFLINE,keyIds:id},
             success: function(msg) {
                 var response = eval("(function(){return " + msg + ";})()");
                 if(response.success){
                     get_content();
                 }else{
                     alert($.isArray(response.error)? response.error[0] : response.error);
                 }
             }
        });
    }
     var useNewMethod=true;
    function show_edit_dialog(id,status,moduleId,deviceId){
        $("#content_model").modal('show');
        // load content detail
        if(status==_STATUS_ONLINE){
            $("#btn-confirm-modify").hide();
        }else{
            $("#btn-confirm-modify").show();
        }
        if(useNewMethod){
            getContent(id,moduleId,deviceId);
            return;
        }

        $.ajax({
             type: "POST",
             url: "/content/getContent.action",
             //dataType: "json",
             dataType: "text",
             data: {"obj.id": id},
             success: function(msg){
                 $("#content_id").val(id);
                 channelReset = false;
                 
                 var response = eval("(function(){return " + msg + ";})()");
                 if(response){
                     $("#content-poster").attr("src", response.poster);
                     $("#content-big-poster").attr("src", (response.bigPoster == null)? "/images/no-poster.png" : response.bigPoster);
                     $("#content-name").val(response.name);
                     $("#content-actor").val(response.actor);
                     $("#content-intro").val(response.intro);
                     $("#movie-type").val(response.moduleId);
                     $("#content-time").val(response.activityTime);
                     $("#contentChannel").val(response.channels);

                     /*show channels*/
                     //$("#tree-channel").html("");
                     // Looks like Fuelux Tree UI doesn't work when element already has assigned data property
                     delete($('#tree-channel').data().tree);
                     $('#tree-channel').remove();
                     $("#channel-container").append('<div id="tree-channel" class="tree" style="height: 220px;overflow:scroll"></div>');

                     if(!treeUtils){
                         treeUtils = new ChannelUtils();
                         treeUtils.initByJson(__channels);
                     }
                     treeUtils.setSelectedChannel(response.channels);
                     //var treeData = treeUtils.generateTreeData();
                     show_tree();

                     /*show user type*/
                     select_user_type(response.userType);
                     /*show files*/
                     for(var i=0; i<response.fileList.length; i++){
                         var f = response.fileList[i];
                         if(f){
                             $("#file-container").append('<div class="row">' +
                                        '<div class="col-md-11">' + f.name + '</div>' +
                                        '<div class="col-md-1 text-right cursor">'+
                                        '<i class="fa fa-play-circle-o"></i></div></div>');
                         }
                     }

                     // save info for submit
                     //console.log("id:" + id);
                     $("#orderBy").val(order);
                     $("#orderDir").val(dir);
                     $("#pageSize").val(page_size);
                     $("#pageNum").val(page_index);
                 }
             }
        }).fail(function(){
            alert("获取媒体详细信息失败，也许您离开太久了，请重新登录。");
            $("#content_model").modal('hide');
        });
    }

     function loadUserType(){
         $("#user-typer-loading").show();
         $.ajax({
             type: "POST",
             url: "/user/userType!list.action",
             //dataType: "json",
             dataType: "text",
             //data: {name: ver},
             success: function(msg){
                 var response = eval("(function(){return " + msg + ";})()");

                 for(var i=0; i<response.objs.length; i++){
                     var type = response.objs[i];
                     if(type){
                         $("#user-type-container").append('<div class="checkbox"><label><input type="checkbox" class="ace" name="cb-user-type" ' +
                                                          'value="' + type.id + '">' +
                                                          '<span class="lbl">' + type.name + '</span></label></div>');
                     }
                 }
              }
         });
     }
     
     function loadChannel(){
         $("#channel-loading").show();
         $.ajax({
             type: "POST",
             url: "/publish/channel!channelTree.action",
             dataType: "text",
             success: function(msg){
                 __channels = msg;
                 treeUtils = new ChannelUtils();
                 treeUtils.initByJson(__channels);

                 //var treeDataSource = treeUtils.generateTreeData();
                 //show_tree(treeDataSource);
                 loadGrantedChannel();
              }
         });
     }

     function loadGrantedChannel(){
         $.ajax({
             type: "POST",
             url: "/security/admin!getGrantChannel.action",
             //dataType: "json",
             dataType: "text",
             //data: {name: ver},
             success: function(msg){
                 var json = eval("(function(){return " + msg + ";})()");
                 __grantChannels = json.data["obj.serializedChannel"];
                 show_tree();
             }
         });
     }

     function show_tree(){
         var treeDataSource;
         if(!_isRoot){

             treeUtils.setGrantEnabled(true);
             treeUtils.setGrantChannel(__grantChannels);
         }
         treeDataSource = treeUtils.generateTreeData();
         $('#tree-channel').ace_tree({
             dataSource: treeDataSource ,
             multiSelect:true,
             loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
             'open-icon' : 'ace-icon tree-minus',
             'close-icon' : 'ace-icon tree-plus',

             'selected-icon' : 'ace-icon fa fa-check',
             'unselected-icon' : 'ace-icon',
             'selectable' : true
         });
         $('#tree-channel')
                 .on('updated', function(e, result) {
                      channelReset = true;   // 没有在树上操作时，selectedItem取不到，如果没有操作过，使用原来的值
                 });
     }
     
    function loadMovieType(){
        $.ajax({
            type: "POST",
            url: "/module/module!list.action",
            dataType: "text",
            //data: {recommendId:$("#channel").val(), keyIds:items},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if (response.objs) {
                    for (var n = 0; n < response.objs.length; n++) {
                        var cn = response.objs[n];
                        $("#movie-type").append($("<option/>", {
                            value: cn.id,
                            text: cn.name
                        }));
                    }
                }
            }
        });
    }


     mustSelectUserTypes = <%=AppConfigurator.getInstance().getBoolConfig("system.mustSelectUserTypes",true)%>;
    function save_modify_old(){
        // 检查输入
        var r = true;
        if ($.trim($("#content-name").val()) == "") {
            $("#content-name").closest("div.form-group").addClass("has-error");
            r = false;
        }

        if ($.trim($('#content-time').val()) == "") {
            $('#content-time').closest("div.form-group").addClass("has-error");
            r = false;
        }

        if(!r){
            $("#tab_baseinfo").tab('show');
            return;
        }

        var channelItems = $('#tree-channel').tree('selectedItems');
        var channel_array = "";
        if(!channelReset){
            channel_array = $("#contentChannel").val();
        }else{
            if(channelItems && channelItems.length>0 ){
                for(var i=0; i<channelItems.length; i++){
                    channel_array += (i == 0)? + channelItems[i].id : "," + channelItems[i].id;
                }
                //alert(channel_array);
                $("#contentChannel").val(channel_array);
            }
        }
        if( channel_array == ""){
            alert("请设置视频所属栏目！");
            $("#tab_channel").tab('show');
            return;
        }
        if($( "input[name='cb-user-type']:checked" ).length == 0){
            alert("请选择可以观看该视频的用户类型！");
            $("#tab_channel").tab('show');
            return;
        }else{
            var userTypeValues = [];
            $( "input[name='cb-user-type']:checked" ).each(function(i){
                userTypeValues.push( $(this).val());
             });
            $("#contentUserType").val(','+userTypeValues.join(",") + ',');
        }

        // submit
        $("#modify-form").submit();
    }

    function to_abnormal(){
        location.href = "abnormal.jsp";
    }
    function to_channel_content(){
        location.href = "channelContent.jsp";
    }
</script>
</div>
</body>
</html>
