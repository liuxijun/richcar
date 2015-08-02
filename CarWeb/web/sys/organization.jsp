<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-27
  Time: 14:23:55
  组织管理
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>组织管理 - <%=IndividualUtils.getInstance().getName()%></title>
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
                <a href="#"> 网站首页</a>
            </li>
            <li class="active">管理首页</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-sitemap"></i>组织管理

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">


                     <div class="col-xs-12 no-padding">
                          <div class="btn-group">
                         <a class="btn btn-blue btn-big"  href="#" onclick="addFirstLvOrg();return false;">
                             添加根组织
                          </a>

                         </div>
                         <div class="btn-group pull-right">
                             <button class="btn btn-lightwhite btn-big" id="apply_modify" type="button">
                                 应用
                             </button>
                         </div>


                         <div class="row">
                             <div class="col-sm-12">
                                 <div class="bb" id="org_nestable">
                                 </div>
                             </div>
                         </div>
                     </div>
                         <div class="space-6"></div>
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
<div class="modal fade modal-class" id="editOrgModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left: 50%; margin-left: -200px; margin-top: -200px; width:400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="editOrgModalLabel">设置组织</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="col-xs-12" style="margin: 0 0 20px 0">
                        <div class="row">
                            <h5>名称</h5>
                            <div>
                                <input type="text" class="col-sm-12" id="org-name">
                            </div>
                         </div>
                        <div class="space"></div>
                         <div class="row" >
                             <h5 style="display:inline">组织用户可以观看的栏目</h5>
                             <div class="pull-right action-buttons">
                                      <a class="gray" href="#" title="全选" id="btn_select_all">
                                          <i class="ace-icon fa fa-square-o">全选</i>
                                       </a>
                               </div>
                             
                          </div>
                          <div id="channel-container" class="row">
                                <div id="channel-tree" class="tree"></div>
                          </div>
                        <div class="space"></div>
                    </div>
                    <button class="btn btn-blue btn-big2 margin0" type="button" id="saveOrg">
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
<script src="../scripts/channel.min.js?id=3.423"></script>
<script src="../scripts/organization.min.js?v=1.423"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    var unsavedModify = false;
    var parentOrgId = 0;    // reserved for root node's parent
    var channelUtils, orgUtils;
    var _selectedChannels = "";
    var channelReset = false;
    var mode = "new";
    
    jQuery(function($){
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                if( $("#org-name").is(":focus")){
                    event.preventDefault();
                    doSaveOrganization();
                }
            }
        });

        loadChannel();

        loadOrganization();

        $("#saveOrg").click(function(){
            doSaveOrganization();
        });

        $("#btn_select_all").click(function(e){
            // select all / unselect all
            selectAllChannel();
             e.stopPropagation();    
        });
        //so disable dragging when clicking on label
        var agent = navigator.userAgent.toLowerCase();
        if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
            $('#tasks').on('touchstart', function (e) {
                var li = $(e.target).closest('#tasks li');
                if (li.length == 0)return;
                var label = li.find('label.inline').get(0);
                if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
            });

        $("#apply_modify").click(function(){
            apply_modify();    
        });
        
        window.onbeforeunload = function(evt) {
            if(unsavedModify){
                var message = '有尚未保存的组织编辑，确定要离开吗？';
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

    function loadOrganization(){
        $.ajax({
            type: "POST",
            url: "/user/org!orgTree.action",
            dataType: "text",
            success: function(msg){
                orgUtils = new OrganizationUtils();
                orgUtils.initByJson( msg );

                $("#org_nestable").html(orgUtils.generateNestableData());
                $('#org_nestable').nestable({
                    listNodeName: "ol", itemNodeName: "li", rootClass: "bb", listClass: "bb-list", itemClass: "bb-item", dragClass: "bb-dragel", handleClass: "bb-handle", collapsedClass: "bb-collapsed", placeClass: "bb-placeholder", noDragClass: "bb-nodrag", emptyClass: "bb-empty"
                }).nestable('collapseAll').on('change', function() {
                    set_unsaved_modify();
                });
                enable_item_op();
             }
        });
    }
    function enable_item_op(){
        // 通过禁止操作区里的拖动，实现按钮的可点击
        $('.bb-handle a').on('mousedown', function(e){
                e.stopPropagation();
            });
    }
    function set_unsaved_modify(){
        unsavedModify = true;
        $("#apply_modify").removeClass("btn-lightwhite").addClass("btn-blue");
    }

    function loadChannel(){
            $.ajax({
                type: "POST",
                url: "/publish/channel!channelTree.action",
                dataType: "text",
                success: function(msg){
                    channelUtils = new ChannelUtils();
                    channelUtils.initByJson( msg );
                 }
            });
    }

    function addFirstLvOrg(){
        parentOrgId = 0;
        _selectedChannels = "";
        // 新建使用手工选择的频道
        channelReset = true;
        mode = "new";
        showEditModal("",_selectedChannels);
    }

    function showEditModal(name,preSelected){
        $("#btn_select_all i").removeClass("fa-check-square").addClass("fa-square-o");
        $("#org-name").val(name);
        $("#editOrgModal").modal('show');
        $('#editOrgModal').on('shown.bs.modal', function() {
            $("#org-name").focus();
        });

        delete($('#channel-tree').data().tree);
        $('#channel-tree').remove();
        $("#channel-container").append('<div id="channel-tree" class="tree" style="height: 220px;overflow:scroll"></div>');

        channelUtils.setSelectedChannel(preSelected);
        _selectedChannels = preSelected;
        showTree(channelUtils.generateTreeData());
    }

    function showTree(data){
        $('#channel-tree').ace_tree({
            dataSource: data ,
            multiSelect:true,
            loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon' : 'ace-icon tree-minus',
            'close-icon' : 'ace-icon tree-plus',
            'selected-icon' : 'ace-icon fa fa-check',
            'unselected-icon' : 'ace-icon',
            'selectable' : true
        });
        $('#channel-tree')
                .on('updated', function(e, result) {
                     channelReset = true;   // 没有在树上操作时，selectedItem取不到，如果没有操作过，使用原来的值
                });
    }

    function doSaveOrganization(){
        if( $.trim($("#org-name").val()) == ""){
            $("#org-name").focus();
            return;
        }

        var channelItems = $('#channel-tree').tree('selectedItems');
        var channel_array = "";
        if(  channelReset ){
            if(channelItems && channelItems.length>0 ){
                for(var i=0; i<channelItems.length; i++){
                    channel_array += (i == 0)? + channelItems[i].id : "," + channelItems[i].id;
                }
                //alert(channel_array);
                _selectedChannels = channel_array;
            }
        }

        if(_selectedChannels == ""){
            if( !confirm("没有为组织绑定栏目，确定不给他们看任何内容吗？") ){
                 return;
            }
        }

        if(mode == "edit"){
            do_modify_org(parentOrgId, $.trim($("#org-name").val()), _selectedChannels);
        }else{
            addToNestable(parentOrgId, $.trim($("#org-name").val()), _selectedChannels);
        }
    }

    function do_modify_org(id, name,channels){
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            item.attr("data-name", encodeURI(name));
            var name_panel = item.find(".bb-handle span").eq(0);
            if(name_panel){
                name_panel.html(name);
            }
            item.attr("data-channels", channels);
            set_unsaved_modify();
        }
        $("#editOrgModal").modal('hide');
    }

    function addToNestable(pid, name, channels){
        var s = "";
        if( pid != 0){
            // 子栏目
            var item = $("[data-id='" + pid + "']").eq(0);
            if( item ){
                s = orgUtils.generateNewOrganization(pid, name, channels);
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
            s = orgUtils.generateNewOrganization(pid, name, channels);
            var org_root = $("#org_nestable > ol.bb-list").eq(0);
            if( org_root && org_root.length > 0){
                org_root.append(s);
            }
        }

        $("#editOrgModal").modal('hide');
        enable_item_op();
        set_unsaved_modify();
    }

    function selectAllChannel(){
        var mark = $("#btn_select_all i").hasClass("fa-square-o") ? 1 : 2;
        delete($('#channel-tree').data().tree);
        $('#channel-tree').remove();
        $("#channel-container").append('<div id="channel-tree" class="tree" style="height: 220px;overflow:scroll"></div>');

        channelUtils.selectAllMark = mark;
        showTree(channelUtils.generateTreeData());
        setTimeout(function(){
            channelUtils.selectAllMark = 0;
        }, 1000);
        //$( "#channel-tree" ).trigger( "update" );
        if( mark == 1){
            var array = channelUtils.getAllChildrenNode();
            _selectedChannels = "";
            if( array ){
                var idArray = [];
                for(var i=0; i<array.length; i++){
                   if( array[i]) idArray.push(array[i].id);
                }
                _selectedChannels = idArray.join(",");
            }
        }else{
            _selectedChannels = "";
        }

        if(mark == 1){
            $("#btn_select_all i").removeClass("fa-square-o").addClass("fa-check-square");
        }else{
            $("#btn_select_all i").removeClass("fa-check-square").addClass("fa-square-o");            
        }
    }

    function add_child_org(pid){
        parentOrgId = pid;
        _selectedChannels = "";
        // 新建使用手工选择的频道
        channelReset = true;
        mode = "new";
        showEditModal("",_selectedChannels);
    }

    function edit_org(id){
        // 从nestable里获取名称和channels
        var item = $("[data-id='" + id + "']").eq(0);
        if(item){
            var name = decodeURI(item.attr("data-name"));
            parentOrgId = id;
            //console.info( item.attr("data-channels"));
            mode = "edit";
            console.info("原选中channel：" + item.attr("data-channels"));
            showEditModal(name, item.attr("data-channels"));
            channelReset = false;
        }
    }

    function remove_org(id){
        var item = $("[data-id='" + id + "']").eq(0);
        if( item ){
            var name = decodeURI(item.attr("data-name"));
            var userCount = decodeURI(item.attr("data-user-count"));
            var children_container = item.children("ol").eq(0);

            if( userCount > 0 ){
                alert( name + "有" + userCount + "个用户，不能删除！");
            }else if(children_container && children_container.length > 0){
                alert( name + "非空，不能删除！");
            }else if( confirm("确定要删除 " + name + " 吗？" ) ){
                item.remove();
                set_unsaved_modify();   
            }
        }
    }

    function reset_unsaved_modify(){
        unsavedModify = false;
        $("#apply_modify").removeClass("btn-blue").addClass("btn-lightwhite");
    }

    function apply_modify(){
        //console.info(JSON.stringify($('#org_nestable').nestable('serialise')));
        if(unsavedModify){
            $("#apply_modify").html("正在应用");
            $.ajax({
                type: "POST",
                url: "/user/org!synchronize.action",
                data: {"serialisedOrg": JSON.stringify($('#org_nestable').nestable('serialise'))},
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
