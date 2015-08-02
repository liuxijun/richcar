<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-30
  Time: 14:06:50
  管理员管理
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理员管理 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <style type="text/css">
        .tipContent{
            width: 360px; height:150px; background-color: #fff; border:1px solid #5a9ec2;  position: relative;
        }
        .tipContent:before {
            display: block;
            content: "";
            position: absolute;
             top: -11px;
            border-top: 1px solid #5a9ec2;
            border-left: 1px solid #5a9ec2;
            background: #FFF;
            width: 20px;
            height: 20px;;
           left:30px;
            transform: rotate(45deg);
            -webkit-transform: rotate(45deg);
            -ms-transform: rotate(45deg);
         }
    </style>
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
            <li>
                当前位置:
                <a href="../man.jsp"> 管理首页</a>
            </li>
            <li>系统管理</li>
            <li class="active">管理员管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-user-md"></i>管理员管理

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">


                     <div class="col-xs-12 no-padding movie-info">

                         <div class="space-6"></div>
                         <div class="tabbable">

                             <table class="table table-striped table-bordered table-hover table-30">
                                 <thead>
                                 <tr>
                                     <th width="25%">
                                         <a href="#" onclick='order_by("id")' title="按登录名排序" id="order_by_id">登录名</a>
                                     </th>
                                     <th width="25%">
                                         <a href="#" onclick='order_by("name")' title="按姓名排序" id="order_by_name">姓名</a>
                                     </th>
                                     <th class="center">角色</th>
                                     <th class="center">
                                         <a href="#" onclick='order_by("logon")' title="按登录时间排序" id="order_by_logon">上次登录</a>
                                     </th>
                                     <th class="center">
                                         <a href="#" onclick='order_by("status")' title="按状态排序" id="order_by_status">状态</a>
                                     </th>
                                     <th class="center">操作</th>
                                 </tr>
                                 </thead>
                                 <tbody>
                                 </tbody>
                             </table>

                         </div>
                        <div class="space-6"></div>
                         <div class="row">
                             <div class="col-md-2"><a class="btn btn-green btn-big" id="btn-new-admin">创建新管理员</a></div>
                             <div class="col-md-6 col-md-offset-4"><ul class="pagination pull-right" id="page-nav">
                             </ul></div>
                         </div>
                    </div>
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
<div class="modal fade modal-film" id="adminEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left:50%;margin: -240px 0 0 -280px; width:560px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">管理员</h4>
            </div>
            <div class="modal-body">
                <div class="row">                                <div class="col-xs-12 ">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">登录名</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" id="m_userId" maxlength="64">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" maxlength="128" id="m_name">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">密码</label>
                            <div class="col-sm-9">
                                <input type="password" class="col-sm-12" id="m_password">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">确认密码</label>
                            <div class="col-sm-9">
                                <input type="password" class="col-sm-12" id="m_password_retype">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">角色</label>
                            <div class="col-sm-9">
                                <div class="control-group control-group-inline" id="role-panel">
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="channel-panel">
                            <label class="col-sm-3 control-label"></label>
                            <div class="col-sm-9">
                                <div class="tipContent" id="channel-container">
                                    <div class="row" style="margin:12px 0 10px 8px">
                                             <a class="gray" href="#" title="全选" id="btn_select_all">
                                                 <i class="ace-icon fa fa-square-o bigger-120">全选</i>
                                              </a>
                                    </div>
                                    <div id="channel-tree" class="tree" style="height:110px;overflow:scroll;"></div>
                                </div>
                            </div>
                        </div>
                        <div class="space"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"></label>
                            <button class="btn btn-blue" id="btn-save-admin" type="button">确定</button>
                            <button class="btn btn-white" id="btn-save-cancel" style="margin-left: 20px;" type="button" onclick="$('#adminEditModal').modal('hide')">关闭</button>
                            <button class="btn btn-white" style="margin-left: 80px;" id="btn_lock"  type="button">锁定 </button>
                            <button class="btn btn-danger" style="margin-left: 12px;" id="btn_delete" type="button">删除 </button>
                        </div>
                     </form>
                </div> </div>
            </div>
             </div>
            <div class="modal-footer" style="display: none;">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
<script src="../scripts/channel.min.js?v=1.423"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    var order = "time";
    var dir = "desc";
    var page_index = 1;
    var page_size = 15;
    var page_count = 0;
    var user_id = "";
    var user_org = "";
    var user_status = _USER_STATUS_NORMAL;
    var _USER_STATUS_NORMAL = 1;
    var _USER_STATUS_LOCKED = 8;
    var roleArray = [];
    var channelUtils;
    var _selectedChannels;
    var channelReset = false;
    var _RESERVED_PUB_ROLE_ID = 2;

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
        $('#m_userId, #m_name, #m_password, #m_password_retype').keydown(function(){
           //$('#m_user_id').closest("div.form-group").removeClass("has-error");
            $(this).closest("div.form-group").removeClass("has-error");
        });

        $('#adminEditModal').on('shown.bs.modal', function() {
            $("#m_userId").focus();
        });

        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        
        loadRole();

        loadChannel();

        $("#btn-new-admin").click(function(){
            newAdmin();
        });

        $("#btn-save-admin").click(function(){
            saveAdmin();
        });
        
        $("#btn_delete").click(function(){
            if(user_id != ""){
                removeAdmin(user_id);
            }
        });

        $("#btn_lock").click(function(){
            if(user_id != ""){
                lockAdmin(user_id, (user_status == _USER_STATUS_NORMAL)? _USER_STATUS_LOCKED :_USER_STATUS_NORMAL);
            }
        });
        $("#btn_select_all").click(function(e){
            // select all / unselect all
             selectAllChannel();
             e.stopPropagation();
        });
        
    });

    function loadRole(){
        $.ajax({
            type: "POST",
            url: "/security/role!list.action",
            dataType: "text",
            data: {"pageBean.orderBy": "o1.id", "pageBean.orderDir":"asc"},
            success: function(msg){
                var response = JSON.parse(msg);
                $("#role-panel").html("");
                var buildRoleItem = function(o){
                    if (o) {
                        if( parseInt(o.type) != 1 ) // 超户不显示
                        {
                            var output = '<div class="checkbox"><label>' +
                               '<input type="checkbox" class="ace" name="admin-role-checkbox" value="' +
                               o.roleid + '"><span class="lbl">' + o.name + '</span></label></div>';
                            //console.info(output);
                            roleArray.push(new Role(o.roleid, o.name));
                            return output;
                        }
                    }
                    return "";
                };

                for(var i=0;i<response.objs.length; i++){
                    $("#role-panel").append(buildRoleItem(response.objs[i]));
                }
                $( "input[name='admin-role-checkbox']" ).click(function(){
                   if( $(this).val() == _RESERVED_PUB_ROLE_ID ){
                       if( $(this).prop("checked") ){
                           $("#channel-container").show();
                       }else{
                           $("#channel-container").hide();
                       }
                   }
                });
                get_all_item();
             }
        });
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

    function selectAllChannel(){
        var mark = $("#btn_select_all i").hasClass("fa-square-o") ? 1 : 2;
        delete($('#channel-tree').data().tree);
        $('#channel-tree').remove();
        $("#channel-container").append('<div id="channel-tree" class="tree" style="height: 120px;overflow:scroll"></div>');

        channelUtils.selectAllMark = mark;
        showTree(channelUtils.generateTreeData());
        //setTimeout(function(){
        //    channelUtils.selectAllMark = 0;
        //}, 1000);
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

    function showTree(data){
        delete($('#channel-tree').data().tree);
        $('#channel-tree').remove();
        $("#channel-container").append('<div id="channel-tree" class="tree" style="height: 120px;overflow:scroll"></div>');

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


    function get_all_item(){
        var _order = "o1.modifydate";
        switch (order) {
            case "id": _order = "o1.userId";break;
            case "name": _order = "o1.name";break;
            case "logon": _order = "o1.lastlogintime";break;
            case "status": _order = "o1.status";break;
        }
        $.ajax({
            type: "POST",
            url: "/security/admin!searchAdmin.action",
            dataType: "text",
            data: {"pageBean.pageSize":page_size,"pageBean.pageNo":page_index,"pageBean.orderBy":_order, "pageBean.orderDir":dir},
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
    
    function getStatusName(status){
        switch(parseInt(status)){
        case _USER_STATUS_NORMAL: return "正常";break;
        case _USER_STATUS_LOCKED: return "锁定";break;
        default:
        }
        return "未知";
    }

    function build_item(o) {
        if (o) {
           var output = '<tr><td class="color1">' + o.login + '</td>' +
                        '<td>' + o.realname +'</td>' +
                        '<td class="center">' + getRolesName(o) + '</td>' +
                        '<td class="center">' + o.lastlogintime + '</td>' +
                        '<td class="center">' + getStatusName(o.status) + '</td>' +
                        '<td class="center"><a data-placement="bottom" data-original-title="编辑" ' +
                        'data-rel="tooltip" class="btn btn-grey btn-xs" ' +
                        'href="#" onclick="editAdmin(\'' + o.login + '\');return false">' +
                        '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>';
            if( parseInt(o.isRoot) != 1 ){
                output += '<a class="btn btn-grey btn-xs" data-rel="tooltip" ' +
                        'data-original-title="删除" data-placement="bottom" ' +
                        'href="#" onclick="removeAdmin(\'' + o.id + '\');return false">' +
                        '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>' +
                        '</a>';
            }
            output += '</td></tr>';
            //console.info(output);
            return output;
        } else {
            return "";
        }
    }

    function getRolesName(o){
        if(parseInt(o.isRoot) == 1){
            return "超级管理员";
        }else{
            //console.info(o.realname + " roles:" + o.serializedRole);
            var roles = o.serializedRole.split(",");
            var rolesName = "";
            for(var i=0;i<roles.length; i++){
                rolesName += (i==0)? getRoleNameById(roles[i]) : "," +getRoleNameById(roles[i]);
            }

            return rolesName;
        }
    }

    function getRoleNameById(id){
        for(var i=0; i<roleArray.length; i++){
            if(roleArray[i].id == id) return roleArray[i].name;
        }
        return "";
    }
    function order_by(v) {
        if (v == order) {
            dir = (dir == "asc") ? "desc" : "asc";
        } else {
            order = v;
            dir = "asc";
        }

        $("#order_by_id").attr("title", (order == "id" && dir == "desc") ? "按登录名倒序排序" : "按登录名排序");
        $("#order_by_name").attr("title", (order == "name" && dir == "desc") ? "按姓名倒序排序" : "按姓名排序");
        $("#order_by_logon").attr("title", (order == "logon" && dir == "desc") ? "按登录时间倒序排序" : "按登录时间排序");
        $("#order_by_status").attr("title", (order == "status" && dir == "desc") ? "按状态倒序排序" : "按状态排序");
        page_index = 1;

        get_all_item();
    }

    function newAdmin(){
        user_id = "";
        $("#m_userId").removeAttr("disabled");
        $("#m_password").removeAttr("placeholder");
        $("#m_userId, #m_name, #m_password, #m_password_retype").val("");
        $( "input[name='admin-role-checkbox']" ).removeAttr("checked");
        $("#btn_lock").hide();
        $("#btn_delete").hide();
        $("#btn_select_all i").removeClass("fa-check-square").addClass("fa-square-o");

        $("#channel-container").hide();
        channelReset = false;
        channelUtils.setSelectedChannel("");
        _selectedChannels = "";
        showTree(channelUtils.generateTreeData());
        $("#adminEditModal").modal("show");
    }

    function editAdmin(adminId){
        $.ajax({
            type: "POST",
            //url: "/security/admin!searchAdmin.action",
            url: "/security/admin!getAdminDetail.action",
            dataType: "text",
            data: {"obj.login": adminId},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success ){
                    user_id = response.data["obj.id"];
                    $("#m_userId").val(adminId).attr('disabled','disabled');
                    $("#m_name").val(response.data["obj.realname"]);
                    if( parseInt(response.data["obj.isRoot"]) == 1){
                        $("#btn_delete").hide();
                    }else{
                        $("#btn_delete").show()
                    }
                    $("#m_password").val("");
                    $("#m_password").attr("placeholder", "不修改密码时保持输入框为空");
                    $("#m_password_retype").val("");
                    $("#btn_lock").show();
                    user_status = response.data["obj.status"];
                    $("#btn_lock").html(user_status == _USER_STATUS_NORMAL? "锁定":"解锁");

                    selectRole(response.data["obj.serializedRole"], response.data["obj.isRoot"]);
                    if(response.data["obj.isRoot"]){
                        $("#btn_lock").attr("disabled", "disabled");
                    }else{
                        $("#btn_lock").removeAttr("disabled");
                    }
                    channelReset = false;
                    channelUtils.setSelectedChannel(response.data["obj.serializedChannel"]);
                    _selectedChannels = response.data["obj.serializedChannel"];
                    showTree(channelUtils.generateTreeData());

                    $("#btn_select_all i").removeClass("fa-check-square").addClass("fa-square-o");
                    $("#adminEditModal").modal("show");

                }else{
                    //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                    alert("获取用户信息失败：" +  response.error);
                }
             }
        }).fail(function(){
            alert("呀，获取用户信息失败！");
        });
    }

    function selectRole(serialized, isRoot){
        // 根据roleArray选中指定的角色
        $( "input[name='admin-role-checkbox']" ).removeAttr("checked");
        var roleArray = serialized.split(",");
        var hasPubRole = false;
        if( roleArray && roleArray.length>0){
            //alert(serialized + " len:" + roleArray.length);
            jQuery("input[name='admin-role-checkbox']").each(function() {
                //alert($(this).val() + "  " + roleArray[0]);
                if( parseInt(isRoot) == 1 || in_array($(this).val(), roleArray) ){
                    //console.info("set selected");
                    $(this).attr("checked", "checked");
                    $(this).prop('checked', true);
                }
            });

            $("#channel-container").hide();
            if(parseInt(isRoot) == 1){
                jQuery("input[name='admin-role-checkbox']").attr("disabled", "disabled");
            }else{
                jQuery("input[name='admin-role-checkbox']").removeAttr("disabled");
                for(var i=0; i<roleArray.length; i++){
                    if( roleArray[i] == _RESERVED_PUB_ROLE_ID){
                        $("#channel-container").show();
                    }
                }
            }
        }
    }
    
    function saveAdmin(){
        var hasError = false;
        if( !isRegisterUserName($.trim($("#m_userId").val()))){
            $('#m_userId').closest("div.form-group").addClass("has-error");
            hasError = true;
        }

        if($.trim($("#m_name").val()) == ""){
            $('#m_name').closest("div.form-group").addClass("has-error");
            hasError = true;
        }

        if( user_id == ""){ // create new
            if($("#m_password").val() == ""){
                $('#m_password').closest("div.form-group").addClass("has-error");
                hasError = true;
            }
        }

        if($("#m_password_retype").val() != $("#m_password").val()){
            $('#m_password_retype').closest("div.form-group").addClass("has-error");
            hasError = true;
        }

        // 检查角色
        if($( "input[name='admin-role-checkbox']:checked" ).length == 0){
               alert("请为管理员选择角色！");
               return;
           }

        var selectedRoles = [];
        var hasPubRole = false;
        $( "input[name='admin-role-checkbox']:checked" ).each(function(i){
                selectedRoles.push( $(this).val());
                if( $(this).val() == _RESERVED_PUB_ROLE_ID && $(this).is(":enabled")){
                    hasPubRole = true;
                }
             });
        if( hasPubRole){
            // 检查是否选择了频道
            var channelItems = $('#channel-tree').tree('selectedItems');
            var channel_array = [];
            //console.info( _selectedChannels);
            if(  channelReset ){
                if(channelItems && channelItems.length>0 ){
                    for(var i=0; i<channelItems.length; i++){
                        channel_array.push( channelItems[i].id);
                    }
                    //alert(channel_array);
                    _selectedChannels = channel_array.join(",");
                }
            }
            if( $.trim(_selectedChannels) == ""){
                alert("请为视频发布员选择可以发布的栏目！");
                return;
            }
        }

        var url = (user_id == "")? "/security/admin!newAdmin.action": "/security/admin!saveAdmin.action";
        var pwd = (user_id == "" || $.trim($("#m_password").val()).length>0)? hex_md5($("#m_password").val()):"";
        $.ajax({
            type: "POST",
            url: url,
            dataType: "text",
            data: {"obj.id": user_id,
                "obj.login": $.trim($("#m_userId").val()),
                "obj.realname": encodeURI($.trim($("#m_name").val())),
                "obj.password": pwd,
                "serializedRole": selectedRoles.join(","),
                "obj.serializedChannel": _selectedChannels
            },
            success: function(msg){
                //var response = JSON.parse(msg);
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success){
                    $("#adminEditModal").modal("hide");
                    order = "";
                    page_index = 1;
                    get_all_item();
                }else{
                    alert("创建用户失败，可能同名的管理员已经存在！");
                    //alert("设置管理员失败：" +  response.error);
                }
             }
        }).fail(function(){
            alert("呀，设置管理员失败！");
        });

    }
    function Role(id,name){
        this.id = id;
        this.name = name;
    }

    function removeAdmin(id){
        if( confirm("确认要删除管理员吗？") ){
            $.ajax({
                type: "POST",
                url: "/security/admin!removeAdmin.action",
                dataType: "text",
                data: {"obj.id": id},
                success: function(msg){
                    var response = eval("(function(){return " + msg + ";})()");
                    if(response.success){
                        $("#adminEditModal").modal("hide");
                        get_all_item();
                    }else{
                        //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                        alert("删除用户失败：" +  response.error);
                    }
                 }
            }).fail(function(){
            });
        }
    }
    function lockAdmin(id, status){
        var url = (parseInt(status) == _USER_STATUS_NORMAL)? "/security/admin!unlock.action":"/security/admin!lock.action";
            $.ajax({
                type: "POST",
                url: url,
                dataType: "text",
                data: {"keyId": id},
                success: function(msg){
                    var response = eval("(function(){return " + msg + ";})()");
                    if(response.success){
                        $("#adminEditModal").modal("hide");
                        get_all_item();
                    }else{
                        //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                        alert("设置管理员失败：" +  response.error);
                    }
                 }
            }).fail(function(){
            });
    }

</script>

</body>
</html>
