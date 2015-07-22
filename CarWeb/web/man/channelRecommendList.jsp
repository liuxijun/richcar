<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-14
  Time: 20:58:46
  频道推荐一览页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>频道推荐 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <%@include file="../inc/displayCssJsLib.jsp"%>
</head>
<body class="no-skin">
<%@include file="../inc/displayHeader.jsp"%>
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>

<%@include file="../inc/displayMenu.jsp"%>
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
                            <li>
                                <a data-toggle="tab" href="#" onclick="to_carousel();return false;">
                                    首页轮显
                                </a>
                            </li>

                            <li class="active">
                                <a data-toggle="tab" href="#">
                                    栏目推荐
                                </a>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div id="channel_recommend_list" class="tab-pane active in">
                                <div class="row page-content-main">
                                    <div class="tabbable">

                                        <table class="table table-striped table-bordered table-hover table-30">
                                            <thead>
                                            <tr>
                                                <th width="50%">栏目名称</th>
                                                <th width="50%">推荐视频</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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
<script src="../js/recommends.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    var recommend_array = [];
    var index = 0;

    jQuery(function ($) {
        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html:true});

        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        
        // get channel recommend list
        recommendUtils.getRecommend = renderChannelRecommend;
        recommendUtils.getRecommends('channelRecommend');
    });
    function renderChannelRecommend(){
        $.ajax({
            type: "POST",
            url: "/publish/recommend!search.action",
            //dataType: "json",
            dataType: "text",
            data: {"obj.type": 2},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                recommend_array = [];
                for(var n=0; n<response.objs.length; n++){
                    var cn = response.objs[n];
                    if(cn){
                        recommend_array.push({
                            "channel_id" : cn.channelId,
                            "recommend_id" : cn.id,
                            "name": cn.name
                        });
                    }
                }
                // get channel recommend movie
                if(recommend_array.length > 0){
                    get_recommend_movie();
                }
            }
        });

    }
    function get_recommend_movie(){
        if(index >= recommend_array.length) return;
        var r_id = recommend_array[index].recommend_id;
        $.ajax({
            type: "POST",
            url: "/content/content!spRecommendSearch.action",
            //dataType: "json",
            dataType: "text",
            data: {"cr_recommendId": r_id,c_status:2,sort:"cr_displayOrder",dir:"asc"},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                var td_html = '<tr><td class="color1"><a href="recommendChannel.jsp?recommend_id=' + r_id +
                              '">' + recommend_array[index].name + '</a></td><td><ul>';
                for (var n = 0; n < response.objs.length; n++) {
                    var o = response.objs[n];
                    if (o) {
                        td_html += '<li>' + o.c_name + '</li>';
                    }
                }
                td_html += '</ul></td></tr>';
                $("tbody").append(td_html);
                index++;
                get_recommend_movie();
            }
        });
    }

    function to_carousel(){
        location.href =  "recommend.jsp";
    }
</script>

</body>
</html>
