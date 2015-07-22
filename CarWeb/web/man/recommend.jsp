<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-13
  Time: 15:07:08
  栏目推荐管理页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>首页轮显推荐 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <style type="text/css">
        .movie-lists li{ display: inline-block;width: 175px; height: 150px; margin: 10px 17px 10px 0;position: relative;cursor:move;vertical-align:top}
        .movie-lists li img{ width: 175px; height: 120px;}
        .movie-lists li h5{ text-align: center; line-height: 20px;}
        .movie-lists li span{ position: absolute; top: -16px; right: -16px;cursor:pointer}
        .recommended h5{color: #eeeeee}
    </style>
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
            <li>视频管理</li>
            <li class="active">推荐管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-film"></i>推荐管理

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

    <div class="page-content-area">

        <div class="row page-content-main">
            <div class="col-xs-12 no-padding">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" href="#">
                                首页轮显
                            </a>
                        </li>

                        <li>
                            <a data-toggle="tab" href="#" onclick="to_channel_recommend();return false;">
                                栏目推荐
                            </a>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div id="main-page-carousel" class="tab-pane fade active in">
                             <div class="row page-content-main">
                                    <div class="col-xs-12 no-padding movie-info">
                                        <div class="space-6"></div>
                                        <div class="widget-box ui-sortable-handle">
                                            <div class="widget-header">
                                                <h5 class="widget-title">推荐视频</h5>

                                                <div class="widget-toolbar">
                                                    最多10项
                                                </div>
                                            </div>

                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    <div class="row">
                                                    <ul class="movie-lists" id="recommend_items" style="min-height:200px">
                                                        <!-- recommend item model
                                                        <li>
                                                           <img src="../images/gallery/image-3.jpg"><h5>摩纳哥王妃</h5>
                                                         <span class="fa-stack fa-lg">
                                                           <i class="fa fa-circle fa-stack-2x"></i>
                                                           <i class="fa fa-times fa-stack-1x fa-inverse"></i>
                                                         </span>
                                                         </li>
                                                        //-->
                                                    </ul>
                                                        </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="space-6"></div>
                                        <div class="widget-box ui-sortable-handle">
                                            <div class="widget-header">
                                                <h5 class="widget-title">选择视频</h5>
                                            </div>

                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="input-group pull-right search-group" style="width: 220px;">
                                                                <input type="text" placeholder="" class="form-control search-query" id="search_word">
                                                                <span class="input-group-btn">
                                                                    <button class="btn btn-sm" type="button" id="btn_search">
                                                                        <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                                                    </button>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div class="input-group pull-right" style="width: 220px;">
                                                           <label><input type="checkbox" class="ace" id="cb-big-poster" checked="checked"><span class="lbl">仅显示有大海报的视频</span></label>
                                                        </div>
                                                    </div>

                                                    <ul class="movie-lists" id="to_be_selected_items" style="min-height:200px">
                                                    </ul>

                                                    <div class="row">
                                                        <div class="col-md-2"></div>
                                                        <div class="col-md-6 col-md-offset-4">
                                                            <ul class="pagination pull-right" id="page-nav">
                                                            </ul>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>


                                    </div>
                            </div>
                            <button class="btn btn-blue" type="button" id="btn-submit">保存修改</button>
                        </div>
                        <div id="channel-recommend" class="tab-pane fade">
                        </div>
                    </div>
                </div>
                <!-- /.page-content-area -->
            </div>
            </div>
            <!-- /.page-content -->
        </div>
        <!-- /.main-content -->


        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
</div>
<%@include file="../inc/displayFooter.jsp" %>
<script src="../js/recommends.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    var recommend_array = [];
    var recommend_id=-1;
    var page_index = 1;
    var page_size = 20;

    jQuery(function ($) {
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        
        $('.dialogs').ace_scroll({
            size: ($(".film-lists2 li").width()+20)*($(".film-lists2 li").length),
            horizontal :true
        });

        // drag & dorp, sortable
        var remove_recommend = '<span class="fa-stack fa-lg"><i class="fa fa-circle fa-stack-2x"></i><i class="fa fa-times fa-stack-1x fa-inverse"></i></span>';
        $("#recommend_items").sortable({
                connectWith: ".movie-lists",
            receive: function( event, ui ) {
                // 增加删除按钮
                ui.item.append(remove_recommend);
                reset_span_click();
            }
        }).disableSelection();

        $("#to_be_selected_items").sortable({
                connectWith: ".movie-lists",
                items: '> li:not(.recommended)',
                receive: function( event, ui ) {
                    // 从推荐里边拖出来，把删除按钮去掉
                    ui.item.find("span.fa-stack").remove();
            }
        }).disableSelection();

        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html:true});
        recommendUtils.getRecommend = get_recommend;
        recommendUtils.getRecommends(<%=request.getParameter("recommendId")%>);
        // get carousel recommend id
/*
        $.ajax({
            type: "POST",
            url: "/publish/recommend!search.action",
            //dataType: "json",
            dataType: "text",
            data: {"obj.type": 1},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                var recommendList='';
                for(var n=0; n<response.objs.length; n++){
                    var cn = response.objs[n];
                    if(cn){
                        if(recommend_id==null||recommend_id<0){
                            recommend_id = cn['id'];
                        }
                        recommendList+='';
                    }
                }
                if(parseInt(recommend_id) < 0)  recommend_id = response.objs[0].id;

                // 请求现有的推荐列表
                get_recommend(recommend_id);
            }
        });
*/

        function get_recommend(r_id) {
            var bigPosterOnley = recommendUtils.isSlider;
            $("#cb-big-poster").attr("checked",bigPosterOnley);
            $.ajax({
                type: "POST",
                url: "/content/content!spRecommendSearch.action",
                //dataType: "json",
                dataType: "text",
                data: {"cr_recommendId": r_id,sort:"cr_displayOrder",dir:"asc","c_status":<%=ContentLogicInterface.STATUS_CP_ONLINE%>},
                success: function(msg) {
                    $("#recommend_items").html("");
                    var response = eval("(function(){return " + msg + ";})()");
                    for (var n = 0; n < response.objs.length; n++) {
                        var o = response.objs[n];
                        if (o) {
                            var item = '<li id="' + o.c_id + '"><img src="' + o.c_post1Url + '"><h5>' + o.c_name + '</h5><span class="fa-stack fa-lg cursor">' +
                                       '<i class="fa fa-circle fa-stack-2x"></i><i class="fa fa-times fa-stack-1x fa-inverse"></i></li>';
                            $("#recommend_items").append(item);
                        }
                    }
                    reset_span_click();
                    get_to_be_selected_item();
                }
            });
        }

        function reset_span_click(){
            $(".movie-lists li span").on('click',function(){
                 $(this).closest("li").remove();
            });
        }
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

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                event.preventDefault();
                do_search();
            }
        });

        $("#btn_search").click(function() {
           do_search();
        });

        $("#btn-submit").click(function() {
           do_submit();
        });

        function do_search(){
            page_index = 1;
            get_to_be_selected_item();
        }
    });
    function to_page(page_no){
        page_index = parseInt(page_no);
        get_to_be_selected_item();
    }
    
    function get_to_be_selected_item(){
        var search_value = $.trim($("#search_word").val());
        var start_index = (page_index - 1) * page_size;
        var big_poster_mark = $("#cb-big-poster").is(':checked')? 1: 0;

        $.ajax({
            type: "POST",
            url: "/content/searchContent.action",
            //dataType: "json",
            dataType: "text",
            //data: {"channelId":ch_id, "c_name":search_value,"c.status": 2, "cc.status":2, "isSpechialExit": null},
            data: {"c_name":search_value,"c_status": 2, "cc_status":2, "isSpechialExit": null, start:start_index, limit:page_size, bigPoster: big_poster_mark},
            success: function(msg) {
                $("#to_be_selected_items").html("");
                var response = eval("(function(){return " + msg + ";})()");
                for (var n = 0; n < response.objs.length; n++) {
                    var o = response.objs[n];
                    if (o) {
                        var s_class = has_recommended(o.c_id) ? "recommended" : "";
                        var item = '<li id="' + o.c_id +  '" class="' + s_class + '"><img src="' + o.c_post1Url + '"><h5>' + o.c_name + '</h5></li>';

                        $("#to_be_selected_items").append(item);
                    }
                }

                // reset page nav
                $("#page-nav").html("");
                var record_count = parseInt(response.totalCount);
                var page_count = Math.ceil(record_count / page_size);
                if(page_count > 1){
                    $("#page-nav").append((page_index == 1)?'<li class="disabled"><a href="#">首页</a></li>':
                            '<li><a href="#" onclick="to_page(1);return false;">首页</a></li>');
                    $("#page-nav").append((page_index == 1)? '<li class="disabled"><a href="#">上一页</a></li>':
                                           '<li><a href="#" onclick="to_page(' + (page_index -1) + ');return false;">上一页</a></li>');
                    var start_page = page_index - 1;
                    var end_page = start_page + 5;
                    if(end_page > page_count){ end_page = page_count; start_page = end_page - 4;}
                    if(start_page < 1){ start_page = 1; end_page = (start_page+4>page_count)? page_count:start_page+4;}

                    // display page index
                    for(var i=start_page; i<=end_page; i++){
                        $("#page-nav").append( (i==page_index)? '<li class="active"><a href="#">' + i + '</a></li>' :
                                                '<li><a href="#" onclick="to_page(' + i +   ');return false;">' + i + '</a></li>');
                    }

                    $("#page-nav").append((page_index == page_count)? '<li class="disabled"><a href="#">下一页</a></li>':
                                           '<li><a href="#" onclick="to_page(' + (page_index +1) + ');return false;">下一页</a></li>');
                    $("#page-nav").append((page_index == page_count)?'<li class="disabled"><a href="#">尾页</a></li>':
                                          '<li><a href="#" onclick="to_page(' + page_count + ');return false;">尾页</a></li>');

                }
            }
        });
    }

    function do_submit(){
        $("#btn-submit").html("正在保存...").attr('disabled','disabled');
        $("#btn-back").attr('disabled','disabled');

        // 顺序取出recommend_items中的id
        var items = $("#recommend_items").sortable("toArray").join(",");
        recommend_id = recommendUtils.selectedRecommendId;
        $.ajax({
            type: "POST",
            url: "/content/contentRecommend!saveContentRecommends.action",
            dataType: "text",
            data: {recommendId:recommend_id, keyIds:items},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if (response.success) {
                    $("#btn-submit").html("保存成功");
                    setTimeout(function() {
                        $("#btn-submit").html("保存修改");
                        $("#btn-submit").html("保存修改").removeAttr('disabled');
                        $("#btn-back").removeAttr('disabled');
                    }, 1000);
                }else{
                    alert($.isArray(response.error)? response.error[0] : response.error);
                    $("#btn-submit").html("保存修改").removeAttr('disabled');
                    $("#btn-back").removeAttr('disabled');
                }
             }
        }).fail(function() {
           alert( "WTF：和服务器通信错误，一准是服务器页面报错了。" );
            $("#btn-submit").html("保存修改").removeAttr('disabled');
            $("#btn-back").removeAttr('disabled');
        });
    }

    function has_recommended(item_id){
        var items = $("#recommend_items").sortable("toArray");
        // by confused by === , iterator myself
        for(var i=0; i<items.length; i++){
            if(items[i] == item_id) return true;
        }

        return false;
    }
    function to_channel_recommend(){
        location.href = "channelRecommendList.jsp"; 
    }
</script>


</div>
</div>
</body>
</html>
