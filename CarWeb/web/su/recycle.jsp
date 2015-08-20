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

                    <div class="input-group pull-right search-group" style="width: 200px;height: 30px; margin: 9px;">
                        <input type="text" id="search_word" placeholder="名称" class="form-control" >
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
                                    <th width="55px" class="center"><label>
                                        <input type="checkbox" class="ace" name="form-field-checkbox" id="select_all">
                                        <span class="lbl"></span>
                                    </label></th>

                                    <th width="50%"><a href="#" onclick='RecycleList.order_by("name")' title="按名称排序"
                                                       id="order_by_name">名称</a></th>
                                    <th class="center"><a href="#" onclick='RecycleList.order_by("time")' title="按发布时间排序"
                                                          id="order_by_pub_time">发布时间</a></th>
                                    <th class="center"><a href="#" onclick='RecycleList.order_by("status")' title="按状态排序"
                                                          id="order_by_status">状态</a></th>
                                    <th class="align-right"><a href="#" onclick='RecycleList.order_by("visitCount")' title="按访问量排序"
                                                               id="order_by_visit_count">访问量</a></th>
                                    <th class="center"></th>
                                </tr>
                                </thead>

                                <tbody id="contentDetail">
                                </tbody>
                            </table>

                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2">
                                <button class="btn btn-red" id="btn-remove"type="button" onclick="RecycleList.empty()">清空回收站</button>
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
<script src="../scripts/channel.min.js?v=1.428"></script>
<script src="../ckplayer/player.js"></script>
<script src="../scripts/content.status.min.js"></script>
<script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=1.428"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
var order = "time";
var dir = "desc";
var page_index = 1;
var page_size = 10;
var page_count = 0;
var __channels = "";
var treeUtils;
var current_id = -1;
var channelReset = false;
var _STATUS_RECYCLE = 9;
var _STATUS_DELETE = 8;
var _STATUS_WAITING_FOR_AUDIT = 300;
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
                RecycleList.remove_movie(-1);
            }
        }
    });

    $("#btn-remove-single").click(function() {
        if(current_id > 0){
            if(confirm("确定要删除《" + $("#content-name").val() + "》吗？")){
                RecycleList.remove_movie(current_id);
            }
        }
    });

    $(document).ajaxStart(function(){
        $("#loading_container").show();
    });

    $(document).ajaxStop(function(){
        setTimeout(function(){$("#loading_container").hide();}, 200);
    });

    $("#select_all").click(function() {
        var checkboxes = $(this).closest('form').find(':checkbox');
        if ($(this).prop('checked')) {
            checkboxes.prop('checked', true);
        } else {
            checkboxes.prop('checked', false);
        }
    });
    RecycleList.get_all_item();
    $("#btn_search").click(function(){
        RecycleList.get_all_item();
    });
});
var RecycleList={
    isStatus:[
        {name:"回收", id:"9"}
    ],
    get_status_name:function  (status) {
        return RecycleList.getTextOfArray(status,RecycleList.isStatus, 'id', 'name');
    },
    build_item:function (o) {
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
                    ' onclick="show_detail_dialog(' + o.id + ','+ o.status+');return false">' + o.name + '</a></td>' +
                    '<td class="center">' + o.createTime + '</td>' +
                    '<td class="center">' + RecycleList.get_status_name(o.status) + '</td>' +
                    '<td class="align-right">'  + o.allVisitCount + '</td>' +
                    '<td class="align-left"><a class="btn btn-grey btn-xs" onclick="RecycleList.deleteContent('+ o.id+');return false;">'+
                    '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i></a>'
            if(parseInt(o.status) == _STATUS_RECYCLE){
                output += '<a class="btn btn-success btn-xs" data-rel="tooltip" data-original-title="恢复"' +
                        ' data-placement="bottom" href="#" onclick="RecycleList.recycleContent(' +
                        o.id + ');return false;"><i class="ace-icon fa fa-check bigger-110 icon-only"></i></a>';
            }
            output +=  '</td>' +
                    '</tr>';
            return output;
        } else {
            return "";
        }
    },
    get_all_item:function () {
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
        $.ajax({
            type: "post",
            url: "/content/content!getContentByStatus.action",
            //dataType: "json",
            dataType: "text",
            data: {"search":search_word,"startTime":search_startTime,"stopTime":search_stopTime,
                "pageBean.pageSize":page_size,"pageBean.pageNo":page_index,"pageBean.orderBy":_order, "pageBean.orderDir":dir},
            success: function(msg) {
                $("tbody").html("");
                var response = eval("(function(){return " + msg + ";})()");
                var record_count = response.totalCount;

                for (var i = 0; i < response.objs.length; i++) {
                    var o = response.objs[i];
                    $("tbody").append(RecycleList.build_item(o));
                }

                $('[data-rel=tooltip]').tooltip();
                rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "RecycleList.to_page", record_count);
            }
        });
    },
    order_by:function (v) {
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

    RecycleList.get_all_item();
},
    show_detail_dialog: function (id,status){
    $("#detail_modal").modal('show');
    current_id = id;
    // load content detail
    $("#content-poster").attr("src", "/images/no-poster.png");
    $("#content-big-poster").attr("src", "/images/no-poster.png");
    if(status==_STATUS_ONLINE){
        $("#btn-remove-single").hide();
        $("#btn-save").hide();
    }else{
        $("#btn-remove-single").show();
        $("#btn-save").show();
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
                                    '</div></div>');
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
},

remove_movie: function (id){
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
                    RecycleList.get_all_item();
                }
            }).fail(function(){
                        alert("删除失败了，重新登录试试吧！");
                    });
        }
    },

    recycleContent:function (id){
        $.ajax({
            type: "POST",
            url: "/content/content!cpChangeStatus.action",
            //dataType: "json",
            dataType: "text",
            data: {status:_STATUS_WAITING_FOR_AUDIT,keyIds:id},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success){
                    RecycleList.get_all_item();
                }else{
                    alert($.isArray(response.error)? response.error[0] : response.error);
                }
            }
        });
    },
    deleteContent:function(id){
        if(confirm("您确定要永久删除该内容？")){
            $.ajax({
                type: "POST",
                url: "/content/content!deleteContentFile.action",
                dataType: "text",
                data: {status:_STATUS_DELETE,keyIds:id},
                success: function(msg) {
                    var response = eval("(function(){return " + msg + ";})()");
                    if(response.success){
                        RecycleList.get_all_item();
                    }else{
                        alert($.isArray(response.error)? response.error[0] : response.error);
                    }
                }
            });
        }
    },
    getTextOfArray :function (val, data, valueField, displayField) {
        var i = 0, l = data.length;
        for (; i < l; i++) {
            var m = data[i];
            if (m[valueField] == val) {
                return m[displayField];
            }
        }
        return '未知';
    } ,
    to_page:function (index) {
    page_index = index;
   RecycleList.get_all_item();
  }
}

</script>
</div>
</body>
</html>
