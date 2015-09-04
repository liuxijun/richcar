<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014-10-9
  Time: 11:09:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>设置视频栏目 - <%=IndividualUtils.getInstance().getName()%></title>
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
                <a href="../man.jsp"> 管理首页</a>
            </li>
            <li class="active">发布视频</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-upload"></i>发布视频

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">
                <h4>3.设置媒体栏目</h4>
                <div class="col-xs-12 movie-title-box">
                    <div>媒体：<b><s:property value="obj.name"/> </b></div>
                    <hr>
                    <div>
                        <s:if test="#session.contentFiles != null && #session.contentFiles.size() > 0">文件：<s:property value="#session.contentFiles.get(0).origFileName"/> <s:if test="#session.contentFiles.size() > 1">等<s:property value="#session.contentFiles.size()"/>个文件</s:if></s:if>  
                    </div>
                </div>
                <form action="/media/rp-m!setMediaChannel.action" method="post" id="media-channel-form">
                <div class="col-xs-12">
                    <h5>选择媒体栏目</h5>
                    <div id="tree-channel" class="tree">
                        <div id="channel-loading" style="color: #9aa7b2;">正在加载...</div>
                    </div>
                    <hr>
                    <h5>限定用户类型</h5>
                    <div class="control-group control-group-inline" id="user-type-container">
                        <div id="user-type-loading" style="color: #9aa7b2;">正在加载...</div>
                    </div>
                    <div class="space"></div>
                    <button class="btn btn-blue" type="button" id="btn-submit">提交保存</button>

                    <button class="btn btn-lightwhite " style="margin-left: 12px;" id="btn-back" type="button">返回 </button>
                    <div class="space"></div>
                    <h6 style="color: #9aa7b2;">提交后，如果含有视频文件，系统自动转码，转码完成后提交内容管理员审核</h6>
                    <input type="hidden" name="obj.property5" value="" id="media-channels"/>
                    <input type="hidden" name="obj.userTypes" value="" id="media-user-type"/>
                    <s:hidden name="obj.deviceId"/>
                </div>
                </form>
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
<script type="text/javascript" src="../scripts/channel.min.js?v=1.428"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    var _TREE_NODE_PREFIX = 'NODE_';
    var __channels = "";
    var __grantChannels = "";
    var channelUtils;
    var _isRoot = <s:if test="#session.sessionOperator.isRoot">true;</s:if><s:else>false;</s:else>
    jQuery(function ($) {
        $("#user-type-loading").hide();
        $("#channel-loading").show();
        $("#btn-back").click(function(){
            history.back();
        });
        loadChannel();
        loadUserType();

        $('#tree-channel')
                .on('updated', function(e, result) {
                    //result.info  >> an array containing selected items
                    //result.item
                    __channels = "";
                    for(var i=0; i<result.info.length; i++){
                        __channels += (i>0)? "," + result.info[i].id : result.info[i].id;
                    }
                    //result.eventType >> (selected or unselected)
                })
                .on('selected', function(e) {
                })
                .on('unselected', function(e) {
                })
                .on('opened', function(e) {
                })
                .on('closed', function(e) {
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

        $("#btn-submit").click(function(){
           var items = $('#tree-channel').tree('selectedItems');

           // 检查是否至少选择了一个栏目
           if(__channels == ""){
               alert("请为视频设置所属的栏目！");
               return;
           }
           if(useUserType&&$( "input[name='cb-user-type']:checked" ).length == 0){
               alert("请选择可以观看该视频的用户类型！");
               return;
           }
            $("#btn-submit").html("正在保存...").attr('disabled','disabled');
            $("#btn-back").attr('disabled','disabled');

            //var userTypeValues = [];
            var userTypeStr = ",";
            $( "input[name='cb-user-type']:checked" ).each(function(i){
                //userTypeValues.push( $(this).val());
                userTypeStr += $(this).val() + ",";
             });
            //var userTypeStr = userTypeValues.join(",");
            //if( userTypeStr != "") userTypeStr += ",";
            $.ajax({
                type: "POST",
                url: "/rp-m/saveMedia.action",
                dataType: "text",
                data: {"channelIds":__channels, "obj.userTypes":userTypeStr,"obj.deviceId":'<s:property value="obj.deviceId"/>'},
                success: function(msg){
                    var response = eval("(function(){return " + msg + ";})()");
                    if (response.success) {
                        $("#btn-submit").html("保存成功");
                        setTimeout(function() {
                            location.href = "/pub/pubList.jsp";
                        }, 1000);
                    }else{
                        alert($.isArray(response.error)? response.error[0] : response.error);
                        $("#btn-submit").html("提交转码").removeAttr('disabled');
                        $("#btn-back").removeAttr('disabled');
                    }
                 }
            });
        });
    });

    var useUserType = true;
    function loadUserType(){
        $("#user-typer-loading").show();
        $.ajax({
            type: "POST",
            url: "/user/userType!list.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
                $("#user-typer-loading").hide();
                var response = eval("(function(){return " + msg + ";})()");
                var userTypes = response['objs'];
                useUserType = userTypes!=null&&userTypes.length>0;
                for(var i=0; i<userTypes.length; i++){
                    var type = userTypes[i];
                    if(type){
                        $("#user-type-container").append('<div class="checkbox"><label><input type="checkbox" class="ace" name="cb-user-type" checked="checked"' +
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
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
                $("#channel-loading").hide();
                channelUtils = new ChannelUtils();
                __channels="<s:property value="moduleChannelId"/>";
                channelUtils.setSelectedChannel(__channels);
                channelUtils.initByJson(msg);
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
            channelUtils.setGrantEnabled(true);
            channelUtils.setGrantChannel(__grantChannels);
        }
        treeDataSource = channelUtils.generateTreeData();

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
    }
</script>

</body>
</html>