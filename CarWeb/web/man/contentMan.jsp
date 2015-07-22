<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-23
  Time: 16:44:57
  视频管理
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>视频管理 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <link rel="stylesheet" href="../style/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="../style/fortune.posters.css"/>
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
            <li class="active">视频管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-film"></i>视频管理

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
        <div class="page-content-area">
            <div class="row page-content-main">
                <form role="form" class="form-horizontal">
                    <div class="col-xs-12 no-padding movie-info">
                        <div class="btn-group">
                            <a class="btn btn-dropdown btn-lg" >
                                <span id="selectedChannel" style="font-size:0.75em">所有栏目</span>
                                <i class="ace-icon fa fa-angle-down icon-on-right"></i>
                            </a>
                            <div id="filter-channel" class="tree" style="min-width: 240px"></div>
                        </div>
                        <div class="input-group pull-right search-group" style="width:240px;height: 30px; margin: 9px;">
                            <div style="width: 200px;height: 30px;float: left;">
                                <input type="text" id="search_word" placeholder="名称" class="form-control search-query" style="border-bottom-left-radius: 0;">
                            </div>
<%--
                            <div>
                                <div style="width:100px;height: 30px;float: left;">
                                    <input type="text" id="search_startTime" placeholder="起始时间" class="form-control search-query" >
                                </div>
                                <div style="width:100px;height: 30px;float: left;">
                                    <input type="text" id="search_stopTime" placeholder="截止时间" class="form-control search-query">
                                </div>
                            </div>
--%>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                        </div>

                        <div class="space-6"></div>
                        <div class="tabbable">

                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th width="55px" class="center"><label>
                                        <input type="checkbox" class="ace" name="form-field-checkbox" id="select_all">
                                        <span class="lbl"></span>
                                    </label></th>

                                    <th width="50%"><a href="#" onclick='order_by("name")' title="按名称排序"
                                                       id="order_by_name">名称</a></th>
                                    <th class="center"><a href="#" onclick='order_by("time")' title="按发布时间排序"
                                                          id="order_by_pub_time">发布时间</a></th>
                                    <th class="center"><a href="#" onclick='order_by("status")' title="按状态排序"
                                                          id="order_by_status">状态</a></th>
                                    <th class="align-right"><a href="#" onclick='order_by("visitCount")' title="按访问量排序"
                                                          id="order_by_visit_count">访问量</a></th>
                                    <th class="center"></th>
                                </tr>
                                </thead>

                                <tbody>
                                </tbody>
                            </table>

                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2">
                                <button class="btn btn-red" id="btn-remove" type="button">删除</button>
                            </div>
                            <div class="col-md-6 col-md-offset-4">
                                <ul class="pagination pull-right" id="page-nav"></ul>
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
<div id="float_player" style="position:fixed;top:100px;left:0;width:340px;height:260px;display:none">
    <div id="fl_player" style="width:320px;height:240px;background:black;margin:20px 0 0 0"></div>
    <i class="ace-icon fa fa-times-circle bigger-240" style="position:absolute; right:0;top:0;color:#3a87ad;cursor:pointer;z-index:9999" id="close_player"></i>
</div>
<div class="modal fade modal-film" id="detail_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="position: relative; top: 40px;width: 560px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">视频详情</h4>
            </div>
            <div class="modal-body">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" href="#panel-baseinfo"  id="tab_baseinfo">
                                视频信息
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#panel-poster">
                                海报
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#panel-channel">
                                栏目
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#panel-files">
                                视频文件
                            </a>
                        </li>


                    </ul>

                    <form action="/media/modifyContent.action" method="post" enctype="multipart/form-data" name="modify-form" id="modify-form" class="form-horizontal">
                    <div class="tab-content">
                        <div id="panel-baseinfo" class="tab-pane active in">
                            <div class="row page-content-main">
                                <div class="col-xs-12 no-padding" id="baseInfoBox">
                                    <!--<form class="form-horizontal">-->
                                    <div class="form-group" id="movieTypeBox">
                                        <label class="col-sm-3 control-label no-padding-right filed-need">类别</label>

                                        <div class="col-sm-9" >
                                            <select class="form-control col-sm-12" id="movie-type" name="obj.moduleId" onchange="movieTypeChanged(this.value)">
                                            </select>
                                        </div>
                                    </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right filed-need">名称</label>

                                            <div class="col-sm-10">
                                                <input type="text" class="col-sm-12" id="content-name" name="obj.name">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">主创</label>

                                            <div class="col-sm-10">
                                                <input type="text" class="col-sm-12" id="content-actor" name="obj.actors">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right filed-need">时间</label>
                                            <div class="col-sm-10">
                                                <input type="text" class="col-sm-12" id="content-time" name="obj.property3">
                                            </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">简介</label>

                                            <div class="col-sm-10">
                                                <textarea
                                                        style="  overflow: hidden; word-wrap: break-word; resize: horizontal; height: 69px;"
                                                        class="autosize-transition form-control" id="content-intro" name="obj.intro"></textarea>
                                            </div>
                                        </div>
                                        <div class="space"></div>
                                    <!--</form>-->
                                </div>
                            </div>

                        </div>
                        <div id="panel-poster" class="tab-pane fade">
                            <div class="row page-content-main">
                                <div class="col-xs-12" id="picInfoBox">
                                    <h5>海报</h5>
                                    <div class="file-thumb"><img src="" id="content-poster" style="max-height:200px">
                                    </div>
                                    <input type="file" id="id-input-file-poster" name="moviePoster"/>
                                    <h5>大海报</h5>
                                    <div class="file-thumb"><img src="" id="content-big-poster" style="max-height:200px"></div>
                                    <input type="file" id="id-input-file-big-poster" name="movieBigPoster"/>
                                </div>
                            </div>
                        </div>
                        <div id="panel-channel" class="tab-pane fade">
                            <div class="row page-content-main">

                                <div class="col-xs-12">
                                    <h5>媒体栏目</h5>
                                    <div id="channel-container" style="margin:0;padding:0">
                                        <div id="tree-channel" class="tree" style="height: 220px;overflow:scroll"></div>
                                    </div>
                                    <hr>
                                    <h5>限定用户类型</h5>

                                    <div class="control-group control-group-inline" id="user-type-container">
                                    </div>
                                    <div class="space"></div>

                                </div>
                            </div>
                        </div>

                        <div id="panel-files" class="tab-pane fade file-list">
                            <div class="row page-content-main">
                                <div class="col-xs-12 " id="content-file-container" style="height:400px;overflow:auto">
                                </div>
                            </div>
                        </div>

                    </div>
                        <input type="hidden" name="obj.id" id="content_id">
                        <input type="hidden" name="channelIds" id="contentChannel"/>
                        <input type="hidden" name="obj.userTypes" id="contentUserType"/>
                        <input type="hidden" name="nextPageFlag" value="2">
                    </form>
                </div>
                <div class="space"></div>
                <button class="btn btn-blue" id="btn-save" type="button">保存</button>
                <button class="btn btn-red" id="btn-remove-single" type="button">删除</button>
                <button class="btn btn-lightwhite " style="margin-left: 12px;" id="btn_cancel"  type="button">关闭</button>
            </div>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<script src="../scripts/channel.min.js?v=1.428"></script>
    <script type="text/javascript" src="../scripts/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script src="../scripts/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="../ckplayer/player.js"></script>
<script src="../scripts/content.status.min.js"></script>
<script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=1.428"></script>
<script src="../scripts/contentDisplay.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
var _TREE_NODE_PREFIX = 'NODE_';
var AUDIT_TYPE_ONLINE = 1;
var AUDIT_TYPE_APPEND = 2;
var order = "time";
var dir = "desc";
var page_index = 1;
var page_size = 15;
var page_count = 0;
var __channels = "";
var treeUtils;
var channelReset = false;
var filter_channel_id = -1;

jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html:true});
    $(".btn-dropdown").click(function(){
        $("#filter-channel").slideDown();
    });
    $("#filter-channel").mouseleave(function(){
        $(this).slideUp();
    });

    $("#float_player").hide();

    $("#btn-remove").click(function() {
        // 收集选中的content_id
        if($( "input[name='rp-item-checkbox']:checked" ).length > 0){
            if(confirm("确定要删除这" + $( "input[name='rp-item-checkbox']:checked" ).length + "个电影吗？")){
                remove_movie(-1);
            }
        }
    });

    $("#btn-remove-single").click(function() {
        if(current_id > 0){
            if(confirm("确定要删除《" + $("#content-name").val() + "》吗？")){
                remove_movie(current_id);
            }
        }
    });

    $(document).ajaxStart(function(){
        $("#loading_container").show();
    });

    $(document).ajaxStop(function(){
        setTimeout(function(){$("#loading_container").hide();}, 200);
    });
    
    // init pop up player position
    init_pop_up();
    $( window ).resize(function() {
        init_pop_up();    
    });
    $('#close_player').click(function(){
        $("#float_player").hide();
    });
    $('#detail_modal').on('hide.bs.modal', function (e) {
        $("#float_player").hide();
    });

    $("#select_all").click(function() {
        var checkboxes = $(this).closest('form').find(':checkbox');
        if ($(this).prop('checked')) {
            checkboxes.prop('checked', true);
        } else {
            checkboxes.prop('checked', false);
        }
    });

    get_all_item();

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

    loadChannel();
    loadMovieType();
    loadUserType();

    $(document).keydown(function( event ) {
        if ( event.which == 13 ) {
            if( $("#search_word").is(":focus")){
                event.preventDefault();
                get_all_item();
            }
        }
    });

    $("#btn_search").click(function(){
         get_all_item();
    });

/*    $( "#filter-channel" ).change(function(){
        get_all_item();
    });*/

    $( "#btn_audit_reject").click(function(){
        if(current_id > 0){ reject_movie(current_id);}
    });

    $('#btn-save').click(function(){
         save_modify();
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
    $("#btn_cancel").click(function(){$("#detail_modal").modal('hide');});
});

function get_all_item() {
    var _order = "c.createTime";
    switch (order) {
        case "name": _order = "c.name";break;
        case "time": _order = "c.createTime";break;
        case "status": _order = "c.status";break;
        case "visitCount" : _order = "c.allVisitCount";break;
    }
    var search_word = $("#search_word").val().trim();
    var search_startTime = $("#search_startTime").val();
    var search_stopTime = $("#search_stopTime").val();
    //var channel_id = $("#filter-channel").val();
    $.ajax({
        type: "POST",
        url: "/content/getList.action",
        //dataType: "json",
        dataType: "text",
        data: {"channelId":filter_channel_id,"search":search_word,"startTime":search_startTime,"stopTime":search_stopTime,"pageBean.pageSize":page_size,"pageBean.pageNo":page_index,"pageBean.orderBy":_order, "pageBean.orderDir":dir},
        success: function(msg) {
            $("tbody").html("");
            var response = eval("(function(){return " + msg + ";})()");
            var record_count = response.totalCount;

            for (var i = 0; i < response.objs.length; i++) {
                var o = response.objs[i];
                $("tbody").append(build_item(o));
            }

            $('[data-rel=tooltip]').tooltip();
            rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "to_page", record_count);
        }
    });
}

function build_item(o) {
    if (o) {
        var tipTitle="查看";
        if(o.status!=_STATUS_ONLINE){
            tipTitle = "编辑";
        }
        tipTitle+= "《"+o.name+"》";
        var output = '<tr><td class="center">' +
                     '<label><input type="checkbox" class="ace" name="rp-item-checkbox" value=' + o.id + '>' +
                     '<span class="lbl"></span>' +
                     '</label></td>' +
                     '<td class="color1"><a href="#"  data-rel="tooltip" data-original-title="' +tipTitle+
                '"' +
                ' onclick="show_detail_dialog(' + o.id + ','+ o.status+','+ o.moduleId+','+ o.deviceId+');return false">' + o.name + '</a></td>' +
                     '<td class="center">' + o.createTime + '</td>' +
                     '<td class="center">' + get_status_name(o.status) + '</td>' +
                     '<td class="align-right">'  + o.allVisitCount + '</td>' +
                     '<td class="align-left">';
                 if(parseInt(o.status) == _STATUS_UN_AUDIT){
                     output += '<a class="btn btn-success btn-xs" data-rel="tooltip" data-original-title="审核"' +
                               ' data-placement="bottom" href="#" onclick="audit_movie(' +
                               o.id + ');return false;"><i class="ace-icon fa fa-check bigger-110 icon-only"></i></a>';
                 }
                 if(parseInt(o.status) == _STATUS_OFFLINE){
                     output += '<a class="btn btn-info btn-xs" data-rel="tooltip" data-original-title="上线" ' +
                               'data-placement="bottom" href="#" onclick="online(' + o.id +
                               ');return false;"><i class="ace-icon fa fa-square-o bigger-110 icon-only"></i></a>';
                 }
                 if(parseInt(o.status) == _STATUS_ONLINE){
                     output += '<a class="btn btn-warning btn-xs" data-rel="tooltip" data-original-title="下线" ' +
                               'data-placement="bottom" href="#" onclick="offline(' + o.id +
                               ');return false;"><i class="ace-icon fa fa-check-square-o bigger-110 icon-only"></i></a>';
                 }
                if(parseInt(o.status) != _STATUS_ONLINE){
                    output += '<a class="btn btn-gray btn-xs" data-rel="tooltip" data-original-title="编辑" ' +
                              'data-placement="bottom" href="#" onclick="show_detail_dialog(' + o.id + ','+ o.status+','+ o.moduleId+','+ o.deviceId+
                              ');return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>';
                }
                 output +=  '</td></tr>';

        return output;
    } else {
        return "";
    }
}

function to_page(index) {
    page_index = index;
    get_all_item();
}

function order_by(v) {
    if (v == order) {
        dir = (dir == "asc") ? "desc" : "asc";
    } else {
        order = v;
        dir = "asc";
    }

    $("#order_by_name").attr("title", (order == "name" && dir == "desc") ? "按名称倒序排序" : "按名称排序");
    $("#order_by_pub_time").attr("title", (order == "time" && dir == "desc") ? "按发布时间倒序排序" : "按发布时间排序");
    $("#order_by_status").attr("title", (order == "status" && dir == "desc") ? "按状态倒序排序" : "按状态排序");
    $("#order_by_visit_count").attr("title", (order == "visitCount" && dir == "desc") ? "按访问量倒序排序" : "按访问量排序");
    page_index = 1;

    get_all_item();
}
var useNewMethod=true;
mustSelectUserTypes = <%=AppConfigurator.getInstance().getBoolConfig("system.mustSelectUserTypes",true)%>;
function show_detail_dialog(id,status,moduleId,deviceId){
    $("#detail_modal").modal('show');
    current_id = id;
    // load content detail
    $("#content-poster").attr("src", "/images/no-poster.png");
    $("#content-big-poster").attr("src", "/images/no-poster.png");
    if(status==_STATUS_ONLINE){
        $("#btn-remove-single").hide();
        $("#btn-save").hide();
        $("#movie-type").attr("readonly","true");
    }else{
        $("#btn-remove-single").show();
        $("#btn-save").show();
        $("#movie-type").removeAttr("readonly");
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
                 if(response.success){
                     $("#content-poster").attr("src", response.poster);
                     $("#content-big-poster").attr("src", (response.bigPoster == null) ? "/images/no-poster.png" : response.bigPoster);
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

                     if (!treeUtils) {
                         treeUtils = new ChannelUtils();
                         treeUtils.initByJson(__channels);
                     }
                     treeUtils.setSelectedChannel(response.channels);
                     var treeData = treeUtils.generateTreeData();
                     show_tree(treeData);

                     /*show user type*/
                     select_user_type(response.userType);
                     /*show files*/
                     $("#content-file-container").html("");
                     for (var i = 0; i < response.fileList.length; i++) {
                         var f = response.fileList[i];
                         if (f) {
                             $("#content-file-container").append('<div class="row">' +
                                                                 '<div class="col-md-11">' + f.name + '</div>' +
                                                                 '<div class="col-md-1 text-right cursor">' +
                                                                 '<i class="fa fa-play-circle-o" onclick="play_url(\'' +
                                                                 f.url + '\',' + response.deviceId + ',' + id + ')">' +
                                                                 '</i></div></div>');
                         }
                     }
                 }else{
                    alert("获取视频详细信息失败：" + $.isArray(response.error)? response.error[0] : response.error);
                 }
             }
         }
    }).fail(function(){
        alert("获取媒体详细信息失败，也许您离开太久了，请重新登录。");
        $("#detail_modal").modal('hide');
    });
}

function init_pop_up(){
    var l = ($(window).width() - 560)/2 > 340? ($(window).width() - 560)/2 - 340 : 0;
    var t = ($(window).height() > 260)? ($(window).height() - 260)/2 : 0;
    $("#float_player").css({left:l,top:t});
}
function remove_movie(id){
    var remove_movie_id = "";
    if( id > 0){
        remove_movie_id = id;
    }else{
        var selected_movie_array = [];
        $( "input[name='rp-item-checkbox']:checked" ).each(function(i){
            selected_movie_array.push( $(this).val());
         });
        remove_movie_id = selected_movie_array.join(',');
    }

    if( remove_movie_id != ""){
        $("#detail_modal").modal('hide');
        $.ajax({
                type: "POST",
                url: "/V5/media/media!removeMedia.action",
                dataType: "text",
                data: {keyIds : remove_movie_id},
                success: function(msg){
                   get_all_item();
                }
        }).fail(function(){
           alert("删除失败了，重新登录试试吧！");
        });
    }
}

function audit_movie(id){
    if( id > 0){
        $.ajax({
                type: "POST",
                url: "/V5/media/media!publishMedia.action",
                dataType: "text",
                data: {keyIds : id},
                success: function(msg){
                   get_all_item();
                }
        }).fail(function(){
           alert("审核失败了，重新登录试试吧！");
        });
    }
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
                get_all_item();
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
                 get_all_item();
             }else{
                 alert($.isArray(response.error)? response.error[0] : response.error);
             }
         }
    });
}

</script>


</div>
</body>
</html>
