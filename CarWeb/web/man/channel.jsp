<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-20
  Time: 16:56:17
  频道管理页面
--%><%@ page contentType="text/html;charset=UTF-8"
             language="java" %><%@ taglib
        prefix="s" uri="/struts-tags" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>栏目管理 - <%=IndividualUtils.getInstance().getName()%></title>
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
            <li>视频管理</li>
            <li class="active">栏目管理</li>
        </ul>
    </div>
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-sitemap"></i>栏目管理
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">

                    <div class="alert alert-block alert-warning">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>

                        <i class="ace-icon fa fa-info-circle blue bigger-120"></i>

                        通过鼠标拖动可改变栏目父子关系及前后顺序；<br/>
                        <span style="margin-left:20px;display:block">编辑完成后点<b>应用</b>按钮保存。<br/>
                        新增的栏目，应用后，如果需要为栏目设置可以观看的组织，可以点<a href="../sys/channelOrganization.jsp">这里</a> 。
                        </span>
                    </div>
                    

                     <div class="col-xs-12 no-padding">
                          <div class="btn-group">
                         <a class="btn btn-green btn-big" id="add_root_channel">
                             添加根栏目
                         </a>
                         </div>

                         <div class="btn-group pull-right">
                             <button class="btn btn-lightwhite btn-big" id="apply_modify" type="button">
                                 应用
                             </button>
                         </div>

                         <div class="row">
                             <div class="col-sm-12">
                                 <div class="bb" id="channel_nestable">
                                     
                                 </div>
                             </div>


                         </div>

                     </div>

                         <div class="space-6"></div>
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
<%@include file="../inc/displayFooter.jsp" %>
<!-- Modal -->
<div class="modal fade modal-class" id="newModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: relative; top: 320px; left: 50%; margin: -80px 0 0 -200px; width:400px;display:inline-block">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="new_channel_title">添加栏目-子栏目</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline">
                    <input type="text"  class="input-large" id="channel_name">
                    <button class="btn btn-lightwhite btn-big2 margin0" type="button" id="add_channel">
                        确定
                    </button>
                </form>
            </div>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade modal-class" id="modifyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: relative; top: 320px; left: 50%; margin: -80px 0 0 -200px; width:400px;display:inline-block">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel2">编辑栏目</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline">
                    <input type="text"  class="input-large" id="channel_name_m">
                    <button class="btn btn-lightwhite btn-big2 margin0" type="button" id="save_modify">
                        确定
                    </button>
                </form>
            </div>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<!-- basic scripts -->
<script src="../scripts/channel.min.js?v=2.04.28"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    var treeUtils;
    var parentChannelId = 0;
    var unsavedModify = false;
    jQuery(function($){
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                if( $("#channel_name").is(":focus")){
                    event.preventDefault();
                    doAdd();
                }else if( $("#channel_name_m").is(":focus")){
                    event.preventDefault();
                    doMod();
                }
            }
        });

        loadChannel();

        $('[data-rel="tooltip"]').tooltip();

        //so disable dragging when clicking on label
        var agent = navigator.userAgent.toLowerCase();
        if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
            $('#tasks').on('touchstart', function (e) {
                var li = $(e.target).closest('#tasks li');
                if (li.length == 0)return;
                var label = li.find('label.inline').get(0);
                if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
            });

        $("#add_root_channel").click(function(){
            add_first_lv_channel();
        });

        $("#add_channel").click(function(){ doAdd(); });

        $("#save_modify").click(function(){ doMod() });

        $("#apply_modify").click(function(){
           apply_modify(); 
        });
        /*
        $(window).on('beforeunload', function(){
            if(unsavedModify) return "有尚未保存的栏目编辑，确定要离开吗？";
        });*/
        window.onbeforeunload = function(evt) {
            if(unsavedModify){
                var message = '有尚未保存的栏目编辑，确定要离开吗？';
                if (typeof evt == 'undefined') {
                    evt = window.event;
                }
                if (evt) {
                    evt.returnValue = message;
                }
                return message;
            }
        }
    });

    function doAdd(){
        if( $.trim($("#channel_name").val()) == ""){
            $("#channel_name").focus();
            return;
        }
        // do add
        do_add_child_channel(parentChannelId,  $("#channel_name").val());
        $("#newModal").modal('hide');
    }

    function doMod(){
        if( $.trim($("#channel_name_m").val()) == ""){
            $("#channel_name_m").focus();
            return;
        }
        // do add
        do_modify_channel(parentChannelId,  $("#channel_name_m").val());
        $("#modifyModal").modal('hide');
    }
    function loadChannel(){
            $.ajax({
                type: "POST",
                url: "/publish/channel!channelTree.action",
                dataType: "text",
                success: function(msg){
                    treeUtils = new ChannelUtils();
                    treeUtils.initByJson( msg );

                    //var treeDataSource = treeUtils.generateTreeData();
                    $("#channel_nestable").html(treeUtils.generateNestableData());
                    //$("#channel_nestable").append(treeUtils.generateNestableData());
                    enable_item_op();
                    $('#channel_nestable').nestable({
                        listNodeName: "ol", itemNodeName: "li", rootClass: "bb", listClass: "bb-list", itemClass: "bb-item", dragClass: "bb-dragel", handleClass: "bb-handle", collapsedClass: "bb-collapsed", placeClass: "bb-placeholder", noDragClass: "bb-nodrag", emptyClass: "bb-empty"
                    }).nestable('collapseAll').on('change', function() {
                        set_unsaved_modify();
                    });
                 }
            });
    }

    function add_child_channel(pid){
        //alert(pid);
        parentChannelId = pid;
        $("#new_channel_title").html("添加栏目 - 子栏目");
        $("#channel_name").val("");
        $("#newModal").modal('show');
        $('#newModal').on('shown.bs.modal', function() {
            $("#channel_name").focus();
        });

        return false;
    }

    function do_add_child_channel(pid, name){
        var s = "";
        if( pid != 0){
            // 子栏目
            var item = $("[data-id='" + pid + "']").eq(0);
            if( item ){
                //alert(item.next().attr("data-id"));
                s = treeUtils.generateNewChannel(pid, name);
                var children_container = item.children("ol").eq(0);
                //console.info(children_container.length);
                if( children_container && children_container.length > 0){
                    //console.info(children_container.length);
                    children_container.append(s);
                }else{
                    item.append('<ol class="bb-list">' + s + '</ol>');
                }
            }
        }else{
            // 根栏目
            s = treeUtils.generateNewChannel(pid, name);
            var channel_root = $("#channel_nestable > ol.bb-list").eq(0);
            if( channel_root && channel_root.length > 0){
                channel_root.append(s);
            }
        }
        enable_item_op();
        //item.append(s);
        $('#channel_nestable').nestable({
                    listNodeName: "ol", itemNodeName: "li", rootClass: "bb", listClass: "bb-list", itemClass: "bb-item", dragClass: "bb-dragel", handleClass: "bb-handle", collapsedClass: "bb-collapsed", placeClass: "bb-placeholder", noDragClass: "bb-nodrag", emptyClass: "bb-empty"
                }).on('change', function() {
                    set_unsaved_modify();
                });

        set_unsaved_modify();
        //console.info('$('#channel_nestable').nestable('serialise));
    }
    function enable_item_op(){
        // 通过禁止操作区里的拖动，实现按钮的可点击
        $('.bb-handle a').on('mousedown', function(e){
                e.stopPropagation();
            });
    }

    function switch_channel_audit_flag(id){
        // 切换免审状态，修改data-flag的值和图标
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            var flag = item.attr("data-flag");
            item.attr("data-flag", parseInt(flag) == 1? 0 : 1);

            // 修改图标
            var audit_flag = item.find(".action-buttons a:first-child").eq(0);
            if(audit_flag){
                if( audit_flag.hasClass("green")){
                    audit_flag.addClass("blue").removeClass("green");
                    audit_flag.attr("title", "取消频道免审");
                }else{
                    audit_flag.addClass("green").removeClass("blue");                    
                    audit_flag.attr("title", "设置频道免审");
                }

                var icon = audit_flag.find("i").eq(0);
                if(icon){
                    if(icon.hasClass("fa-flag")){
                        icon.addClass("fa-flag-o").removeClass("fa-flag");
                    }else{
                        icon.addClass("fa-flag").removeClass("fa-flag-o");
                    }
                }
            }

            set_unsaved_modify();
        }
    }

    function edit_channel(id){
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            $("#channel_name_m").val(decodeURI(item.attr("data-name")));
            $("#modifyModal").modal("show");
            $('#modifyModal').on('shown.bs.modal', function() {
                $("#channel_name_m").focus();
            });
            parentChannelId = id;
        }
    }

    function do_modify_channel(id, name){
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            item.attr("data-name", encodeURI(name));
            var name_panel = item.find(".bb-handle span").eq(0);
            if(name_panel){
                name_panel.html(name);
            }
            set_unsaved_modify();
        }
    }

    function remove_channel(id){
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            var children_container = item.children("ol").eq(0),
                confirm_msg = (children_container && children_container.length > 0)? "确定要删除频道吗？删除会同步删除其子频道！":"确定要删除频道吗？";
            if(confirm(confirm_msg)){
                item.remove();
                set_unsaved_modify();
            }
        }
    }

    function add_first_lv_channel(){
        $("#new_channel_title").html("添加栏目 - 根栏目");
        parentChannelId = 0;
        $("#channel_name").val("");
        $("#newModal").modal('show');
        $('#newModal').on('shown.bs.modal', function() {
            $("#channel_name").focus();
        });
    }
    
    function set_unsaved_modify(){
        unsavedModify = true;
        $("#apply_modify").removeClass("btn-lightwhite").addClass("btn-blue");
    }

    function reset_unsaved_modify(){
        unsavedModify = false;
        $("#apply_modify").removeClass("btn-blue").addClass("btn-lightwhite");
    }

    function apply_modify(){
        //console.info(JSON.stringify($('#channel_nestable').nestable('serialise')));
        if(unsavedModify){
            $("#apply_modify").html("正在应用");
            $.ajax({
                type: "POST",
                url: "/publish/channel!synchronize.action",
                data: {"serialisedChannel": JSON.stringify($('#channel_nestable').nestable('serialise'))},
                dataType: "text",
                success: function(msg){
                    var response = eval("(function(){return " + msg + ";})()");
                    if( response.success ){
                        // 保存成功
                        reset_unsaved_modify();
                    }else{
                        alert($.isArray(response.error)? response.error[0] : response.error);
                    }
                    $("#apply_modify").html("应用");
                 }
            }).fail(function(){
                alert("糟糕，同步失败了！");
                $("#apply_modify").html("应用");
            });
        }
    }

</script>
</div>
</body>
</html>
