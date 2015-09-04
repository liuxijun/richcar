<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-11-5
  Time: 10:07:19
  异常任务
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>异常任务 - <%=IndividualUtils.getInstance().getName()%></title>
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
            <li>当前位置:<a href="../man.jsp">管理首页</a></li><li>发布视频</li><li class="active">内容一览</li>
        </ul>
    </div>
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-warning"></i>内容一览

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
                                <li class="active">
                                    <a href="#task-abort" data-toggle="tab">
                                        异常任务
                                     </a>
                                </li>

                                <li class="">
                                    <a href="#" data-toggle="tab" onclick="to_normal_list();return false;">
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
                                <div class="tab-pane fade active in" id="task-trans-abnormal">
                                    <div class="task-fail">
                                            转码失败任务
                                    </div>
                                    <table class="table table-striped table-bordered table-hover table-30" id="list-trans-abnormal">
                                        <thead>
                                        <tr>
                                            <th width="70%">名称</th>
                                            <th class="center">失败原因</th>
                                            <th class="center">发布时间</th>
                                            <th class="center">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                    
                                    <div class="task-review" id="task-audit-reject">
                                        未通过审核内容
                                    </div>
                                    <table class="table table-striped table-bordered table-hover table-30" id="list-audit-reject">
                                        <thead>
                                        <tr>
                                            <th width="70%">名称</th>
                                            <th class="center">失败原因</th>
                                            <th class="center">发布时间</th>
                                            <th class="center">操作</th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        </tbody>
                                    </table>
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
            <form action="/content/modify.action" method="post" enctype="multipart/form-data" class="form-horizontal" id="modify-form">
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
                        <li class="fade" id="tab_file_list">
                            <a data-toggle="tab" href="#file_panel">
                                视频文件
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="baseinfo_panel" class="tab-pane fade active in">
                            <div class="row page-content-main">
                                <div class="col-xs-12 no-padding">
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
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label filed-need">类别</label>

                                            <div class="col-sm-7">
                                                <select class="form-control " id="movie-type" name="obj.moduleId">
                                                </select>
                                            </div>
                                        </div>
                                </div>
                            </div>

                        </div>
                        <div id="poster_panel" class="tab-pane fade">
                            <div class="row page-content-main">
                                <div class="col-xs-12">
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
                <button class="btn btn-blue" id="btn-confirm-modify" type="button">提交审核</button>
                <button class="btn btn-lightwhite " style="margin-left: 12px;" id="btn_cancel"  type="button">关闭</button>
                <input type="hidden" name="pageBean.orderBy" id="orderBy"/>
                <input type="hidden" name="pageBean.orderDir" id="orderDir"/>
                <input type="hidden" name="pageBean.pageSize" id="pageSize"/>
                <input type="hidden" name="pageBean.orderDir" id="pageNum"/>
                <input type="hidden" name="obj.property5" id="contentChannel"/>
                <input type="hidden" name="obj.userTypes" id="contentUserType"/>
                <input type="hidden" name="obj.id" id="content_id">
                <input type="hidden" name="destPage" value="1">
            </div>
            </form>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>

<!-- inline scripts related to this page -->
<script type="text/javascript" src="../scripts/channel.min.js?v=1.428"></script>

<script type="text/javascript">
    var _isRoot = <s:if test="#session.sessionOperator.isRoot">true;</s:if><s:else>false;</s:else>
    jQuery(function ($) {
        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html:true});

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
        
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });

        abnormalUtils.getTaskList();
        abnormalUtils.init();

        $("#btn_cancel").click(function(){$("#content_model").modal('hide');});
        $("#btn-confirm-modify").click(function(){ abnormalUtils.saveModify(); });

    });

    function to_normal_list(){
       location.href = "pubList.jsp"; 
    }

    function to_channel_content(){
        location.href = "channelContent.jsp";
    }
    var abnormalUtils = {
        ABNORMAL_TYPE_AUDIT_FAILED : 1,
        ABNORMAL_TYPE_TRANS_FAILED : 2,

        auditRejectCount : 0,
        transFailedCount : 0,
        content_id : -1,
        channelReset: false,
        channelUtils: null,
        __grantChannels: "",
        isRoot : _isRoot,

        init:function(){
            abnormalUtils.loadChannel();
            abnormalUtils.loadMovieType();
            abnormalUtils.loadUserType();
        },
        
        getTaskList : function(){
            $.ajax({
                type: "POST",
                url: "/content/abnormal.action",
                dataType: "text",
                success: function(msg) {
                    $("#list-trans-abnormal tbody").html("");
                    $("#list-audit-reject tbody").html("");
                    abnormalUtils.auditRejectCount = 0;
                    abnormalUtils.transFailedCount = 0;

                    var response = eval("(function(){return " + msg + ";})()");
                    var td_html = "";
                    if(  response.contents ){
                        for (var n = 0; n < response.contents.length; n++) {
                            var o = response.contents[n];
                            if (o) {
                                td_html = '<tr><td>' + o.title + '</td>' +
                                          '<td class="center">' + o.message  + '</td>' +
                                          '<td class="center">' + o.createTime + '</td>' +
                                          '<td class="center">';
                                if(o.type == abnormalUtils.ABNORMAL_TYPE_AUDIT_FAILED){
                                    td_html += '<a class="btn btn-info btn-xs" data-rel="tooltip" data-original-title="删除" ' +
                                             'data-placement="bottom" href="#" onclick="abnormalUtils.removeTask(' + o.id + ');return false;">' +
                                             '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i></a>' +
                                            '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-origial-title="编辑" ' +
                                            'data-placement="bottom" href="#" onclick="abnormalUtils.editTask(' + o.id + ');return false;">' +
                                            '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>';
                                }else{
                                    td_html += '<a class="btn btn-info btn-xs" data-rel="tooltip" data-original-title="删除" ' +
                                              'data-placement="bottom" href="#" onclick="abnormalUtils.removeTask(' + o.id + ');return false;">' +
                                              '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i></a>' +
                                              '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-origial-title="重试" ' +
                                              'data-placement="bottom" href="#" onclick="abnormalUtils.retryTask(' + o.taskId + ');return false;">' +
                                              '<i class="ace-icon fa fa-refresh bigger-110 icon-only"></i></a>';
                                }
                                td_html +=  '</td></tr>';

                                if(o.type == abnormalUtils.ABNORMAL_TYPE_AUDIT_FAILED){
                                    abnormalUtils.auditRejectCount++;
                                    $("#list-audit-reject tbody").append(td_html);
                                }else{
                                    abnormalUtils.transFailedCount++;
                                    $("#list-trans-abnormal tbody").append(td_html);
                                }
                            }
                        }
                    }
                    $('[data-rel=tooltip]').tooltip();

                }
            }).fail(function(){
                alert("失败了，重新登录试试吧！");
            });
        },

        editTask: function(id){
            // get content detail
            $("#content_model").modal('show');
            // load content detail
            $.ajax({
                 type: "POST",
                 url: "/content/getContent.action",
                 //dataType: "json",
                 dataType: "text",
                 data: {"obj.id": id},
                 success: function(msg){
                     abnormalUtils.content_id = id;
                     abnormalUtils.channelReset = false;

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
                         $("#content_id").val(id);

                         /*show channels*/
                         //$("#tree-channel").html("");
                         // Looks like Fuelux Tree UI doesn't work when element already has assigned data property
                         delete($('#tree-channel').data().tree);
                         $('#tree-channel').remove();
                         $("#channel-container").append('<div id="tree-channel" class="tree" style="height: 220px;overflow:scroll"></div>');

                         abnormalUtils.channelUtils.setSelectedChannel(response.channels);
                         var treeData = abnormalUtils.channelUtils.generateTreeData();
                         abnormalUtils.showTree(treeData);

                         /*show user type*/
                         abnormalUtils.selectUserType(response.userType);
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
                     }
                 }
            }).fail(function(){
                alert("获取媒体详细信息失败，也许您离开太久了，请重新登录。");
                $("#content_model").modal('hide');
            });
        },

        loadChannel:function(){
            $.ajax({
                type: "POST",
                url: "/publish/channel!channelTree.action",
                //dataType: "json",
                dataType: "text",
                //data: {name: ver},
                success: function(msg){
                    $("#channel-loading").hide();
                    abnormalUtils.channelUtils = new ChannelUtils();
                    abnormalUtils.channelUtils.initByJson(msg);
                    abnormalUtils.loadGrantedChannel();
                }
            });
        },

        loadGrantedChannel : function(){
            $.ajax({
                type: "POST",
                url: "/security/admin!getGrantChannel.action",
                //dataType: "json",
                dataType: "text",
                //data: {name: ver},
                success: function(msg){
                    var json = eval("(function(){return " + msg + ";})()");
                    abnormalUtils.__grantChannels = json.data["obj.serializedChannel"];
                    abnormalUtils.showTree();
                }
            });
        } ,

        showTree : function(){
            var treeDataSource;
            if(!abnormalUtils.isRoot){
                abnormalUtils.channelUtils.setGrantEnabled(true);
                abnormalUtils.channelUtils.setGrantChannel(abnormalUtils.__grantChannels);
            }
            treeDataSource = abnormalUtils.channelUtils.generateTreeData();

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
                         abnormalUtils.channelReset = true;   // 没有在树上操作时，selectedItem取不到，如果没有操作过，使用原来的值
                    });
        },

        selectUserType : function(types){
            if(types){
                var type_array = types.split(",");
                for(var i=0; i<type_array[i]; i++){
                    jQuery("input[name='cb-user-type']").each(function() {
                        if( $.inArray(this.value, type_array) >= 0 ){
                            $(this).attr("checked", "checked");
                            $(this).prop('checked', true);
                        }else{
                            $(this).removeAttr("checked");
                            $(this).prop('checked', false);
                        }
                    });
                }
            }
        },

        loadMovieType : function(){
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
        },

        saveModify: function(){
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
            if(!abnormalUtils.channelReset){
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
                $("#contentUserType").val(userTypeValues.join(","));
            }

            // submit
            $("#modify-form").submit();
        },

        loadUserType:function(){
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
        },

        removeTask:function(id){
            //alert(id);
            //return;
            $.ajax({
                    type: "POST",
                    url: "/V5/media/media!removeMedia.action",
                    dataType: "text",
                    data: {keyIds : id},
                    success: function(msg){
                       abnormalUtils.getTaskList();
                    }
            }).fail(function(){
               alert("删除失败了，重新登录试试吧！");
            });
        },

        retryTask:function(id){
            if(!confirm("您确认要重启这个转码任务吗？")){
                return;
            }
            $.ajax({
                    type: "POST",
                    url: "/encoder/encoderTask!restart.action",
                    dataType: "text",
                    data: {"obj.id" : id},
                    success: function(msg){
                       abnormalUtils.getTaskList();
                    }
            }).fail(function(){
               alert("重启任务失败，重新登录试试吧！");
            });
        }
    };
    
</script>
</div>
</body>
</html>
