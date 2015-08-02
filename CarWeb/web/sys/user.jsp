<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014-10-28
  Time: 15:13:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>用户管理 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
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
            <li>系统管理</li><li class="active">用户管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-user"></i>用户管理
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">
                    <div class="col-md-12">
                    <div class="tabbable ">
                        <ul class="nav nav-tabs no-border" id="myTab" onclick="get_all_item()">
                            <li class="active">
                                <a data-toggle="tab" href="#allPanel">
                                    所有用户
                                </a>
                            </li>

                            <li class="">
                                <a data-toggle="tab" href="#unAuditPanel" onclick="get_unaudit_item()">
                                    <div class="red_bubble" id="count_bubble"></div>
                                    待审用户
                                </a>
                            </li>
                        </ul>
                        <div class="tab-content no-border">
                            <!--<div class="col-xs-12 no-padding movie-info" id="allPanel">//-->
                            <div class="tab-pane active in" id="allPanel">
                                <div class="btn-group">
                                    <a class="btn btn-dropdown btn-lg" >
                                        <span id="selectedOrg" style="font-size:0.75em">所有组织</span>
                                        <i class="ace-icon fa fa-angle-down icon-on-right"></i>
                                    </a>
                                    <div id="filter-org" class="tree" style="min-width: 240px"></div>
                                </div>
                                <div class="input-group pull-right search-group" style="width: 220px;">
                                    <input type="text" placeholder="" class="form-control search-query" id="search_word">
                             <span class="input-group-btn">
                                 <button class="btn btn-sm" type="button" id="btn_search">
                                     <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                 </button>
                             </span>
                                </div>


                                <div class="space-6"></div>
                                <div class="tabbable">

                                    <table class="table table-striped table-bordered table-hover table-30" id="tabAllUser">
                                        <thead>
                                        <tr>
                                            <th></th>
                                            <th width="25%">
                                                <a href="#" onclick='order_by("id")' title="按登录名排序" id="order_by_id">登录名</a>
                                            </th>
                                            <th width="25%">
                                                <a href="#" onclick='order_by("name")' title="按姓名排序" id="order_by_name">姓名</a>
                                            </th>
                                            <th class="center">
                                                <a href="#" onclick='order_by("gender")' title="按性别排序" id="order_by_gender">性别</a>
                                            </th>
                                            <th class="center">
                                                <a href="#" onclick='order_by("org")' title="按组织排序" id="order_by_org">组织</a>
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
                                    <div class="col-md-6 col-md-offset-6"><ul class="pagination pull-right" id="page-nav">
                                    </ul></div>
                                </div>
                                <div class="space-6"></div>
                                <div class="row">
                                    <div class="col-md-3"><a class="btn btn-green btn-big" id="newUser">创建用户 </a></div>
                                    <a class="btn btn-red btn-big" id="btnRemoveUser">删除</a>
                                    <a class="btn btn-blue btn-big" id="importUser">导入用户 </a>
                                </div>
                            </div>
                            <div id="unAuditPanel" class="tab-pane fade">
                                <div class="btn-group" style="height: 30px"></div>
                                <div class="input-group pull-right search-group" style="width: 220px;">
                                    <input type="text" placeholder="" class="form-control search-query" id="search_word_audit">
                                     <span class="input-group-btn">
                                         <button class="btn btn-sm" type="button" id="btn_search_audit">
                                             <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                         </button>
                                     </span>
                                </div>
                                <div class="space-6"></div>
                                <div class="tabbable">

                                    <table class="table table-striped table-bordered table-hover table-30" id="tabAuditUser">
                                        <thead>
                                        <tr>
                                            <th class="center"><input type="checkbox" id="select_all_audit"></th>
                                            <th width="25%">
                                                <a href="#" onclick='order_by_audit("id")' title="按登录名排序" id="order_by_id_audit">登录名</a>
                                            </th>
                                            <th width="25%">
                                                <a href="#" onclick='order_by_audit("name")' title="按姓名排序" id="order_by_name_audit">姓名</a>
                                            </th>
                                            <th class="center">
                                                <a href="#" onclick='order_by_audit("gender")' title="按性别排序" id="order_by_gender_audit">性别</a>
                                            </th>
                                            <th class="center">
                                                <a href="#" onclick='order_by_audit("org")' title="按组织排序" id="order_by_org_audit">组织</a>
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
                                    <div class="col-md-6 col-md-offset-6"><ul class="pagination pull-right" id="page-nav-audit">
                                    </ul></div>
                                </div>
                                <div class="space-6"></div>
                                <div class="row">
                                    <a class="btn btn-blue btn-big" id="btnAuditUser">通过</a>
                                    <a class="btn btn-red btn-big" id="btnRemoveUnAuditUser">删除</a>
                                </div>
                            </div>
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
<%@include file="../inc/displayFooter.jsp"%>
<!-- Modal -->
<div class="modal fade modal-film" id="userEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: relative; top: 20px; width: 560px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">用户信息</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12 ">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">登录名(5-20)</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" id="m_user_id" maxlength="64">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">密码</label>
                            <div class="col-sm-9">
                                <input type="password" class="col-sm-12" id="m_password" maxlength="64">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">确认密码</label>
                            <div class="col-sm-9">
                                <input type="password" class="col-sm-12" id="m_password_retype" maxlength="64">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" id="m_name" maxlength="128">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">性别</label>
                            <div class="col-sm-7">
                                <select class="form-control " id="m_gender">
                                    <option value="1">男</option>
                                    <option value="0">女</option>
                                 </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">邮件地址</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" id="m_email" maxlength="64">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">手机号码</label>
                            <div class="col-sm-9">
                                <input type="text" class="col-sm-12" id="m_cell_phone" maxlength="64">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">类型</label>
                            <div class="col-sm-7">
                                <select class="form-control " id="m_userType">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right filed-need">组织</label>
                            <div class="col-sm-9" id="m_org_container">
                                <div id="user-org" class="tree"></div>

                            </div>
                        </div>


                        <div class="space"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"></label>
                            <div class="col-sm-9">
                                <button class="btn btn-blue" id="btn_edit_ok" type="button">确定 </button>
                                <button class="btn btn-white" style="margin-left: 20px;"  id="btn_edit_cancel" type="button">关闭 </button>
                                <button class="btn btn-white" style="margin-left: 20px;" id="btn_lock"  type="button">锁定 </button>
                                <button class="btn btn-danger" style="margin-left: 20px;" id="btn_delete" type="button">删除 </button>
                             </div>
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
<!-- basic scripts -->
<script src="../scripts/organization.min.js?v=1.423"></script>
<script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=1.423"></script>
<script src="../scripts/ajaxEvent.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    var orgUtils = null;
    var order = "time";
    var orderAudit = "time";
    var dir = "desc";
    var dirAudit = "desc";
    var page_index = 1;
    var page_index_audit = 1;
    var page_size = 15;
    var page_count = 0;
    var page_count_audit = 0;
    var user_id = "";
    var user_org = "";
    var user_status = _USER_STATUS_NORMAL; 
    var filter_org_id = -1;
    var show_bubble = true;
    var _USER_STATUS_NORMAL = 1;
    var _USER_STATUS_LOCKED = 9;
    var _USER_STATUS_UN_AUDITED = 0;

    jQuery(function ($) {
        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html:true});
        $(".btn-dropdown").click(function(){
            $("#filter-org").slideDown();
        });
        $("#filter-org").mouseleave(function(){
            $(this).slideUp();
        });

        loadOrganization();
        loadUserType();
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

        $("#newUser").click(function(){
            newFrontUser();
        });
        $("#importUser").click(function(){
            window.location.href="importFrontUsers.jsp";
        });

        $("#btn_edit_ok").click(function(){
            doSaveFrontUser();
        });

        $('#m_user_id, #m_name, #m_password, #m_password_retype,#m_email,#m_cell_phone').keydown(function(){
            $(this).closest("div.form-group").removeClass("has-error");
        });

        $('#userEditModal').on('shown.bs.modal', function() {
            $("#m_user_id").focus();
        });

        $("#btn_edit_cancel").click(function(){
            $("#userEditModal").modal('hide');
        });
        $("#btn_delete").click(function(){
            if(user_id != ""){
                removeUser(user_id);
            }
        });

        $("#btn_lock").click(function(){
            if(user_id != ""){
                var s;
                setStatus(user_id, (user_status == _USER_STATUS_NORMAL)? _USER_STATUS_LOCKED :_USER_STATUS_NORMAL);
            }
        });

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                if( $("#search_word").is(":focus")){
                    event.preventDefault();
                    get_all_item();
                }else if($("#search_word_audit").is(":focus")){
                    event.preventDefault();
                    get_unaudit_item();
                }
            }
        });

        $("#btn_search").click(function(){
             get_all_item();
        });

        $("#btn_search_audit").click(function(){
            get_unaudit_item();
        });

        $("#select_all_audit").click(function(){
            var checkboxes= $("#tabAuditUser tbody input:checkbox");
            if(checkboxes){
                if ($(this).prop('checked')) {
                    checkboxes.prop('checked', true);
                } else {
                    checkboxes.prop('checked', false);
                }
            }
        });

        $("#btnAuditUser").click(function(){
            // 审核选中
            doAudit();
        });
        $("#btnRemoveUser, #btnRemoveUnAuditUser").click(function(){
            // 删除选中
            doRemove();
        });
    });

    function loadOrganization(){
        $.ajax({
            type: "POST",
            url: "/user/org!orgTree.action",
            dataType: "text",
            success: function(msg){
                if(orgUtils == null){
                    orgUtils = new OrganizationUtils();
                    orgUtils.initByJson( msg );
                }

                orgUtils.setSelectedOrganization("");
                //showFilterTree(orgUtils.generateTreeData());
                showFilterTree(orgUtils.generateFilterTree());

                get_all_item();
                get_unaudit_item();
             }
        });
    }
    
    function showFilterTree(data){
        $('#filter-org').ace_tree({
            dataSource: data ,
            multiSelect:false,
            loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon' : 'ace-icon tree-minus',
            'close-icon' : 'ace-icon tree-plus',
            'selected-icon' : null,
            'unselected-icon' : null,
            'selectable' : true
        });
        $('#filter-org').on('updated', function(e, result) {
            if(result && result.info && result.info.length > 0){
                $("#selectedOrg").html(result.info[0].name);
                filter_org_id = result.info[0].id;
                // trigger refresh
                get_all_item();
             }
        });
    }

    function showUserOrgTree(data){
        delete($('#user-org').data().tree);
        $('#user-org').remove();
        $("#m_org_container").append('<div id="user-org" class="tree" style="height: 160px;overflow:scroll"></div>');

        $('#user-org').ace_tree({
            dataSource: data ,
            multiSelect:false,
            loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon' : 'ace-icon tree-minus',
            'close-icon' : 'ace-icon tree-plus',
            'selected-icon' : null,
            'unselected-icon' : null,
            'selectable' : true
        });
        $('#user-org').on('updated', function(e, result) {
            if(result && result.info && result.info.length > 0){
                user_org = result.info[0].id;
                $("#user-org").closest("div.form-group").removeClass("has-error");
             }
        });
    }

    function loadUserType(){
        $.ajax({
            type: "POST",
            url: "/user/userType!list.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
                var response = parseJson(msg);

                for(var i=0; i<response.objs.length; i++){
                    var type = response.objs[i];
                    if(type){
                        $("#m_userType").append($("<option/>", {
                            value: type.id,
                            text: type.name
                        }));
                    }
                }
             }
        });
    }

    function to_page(index) {
        page_index = index;
        get_all_item();
    }

    function to_page_audit(index) {
        page_index_audit = index;
        get_unaudit_item();
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
        $("#order_by_gender").attr("title", (order == "gender" && dir == "desc") ? "按性别倒序排序" : "按姓名排序");
        $("#order_by_org").attr("title", (order == "org" && dir == "desc") ? "按组织倒序排序" : "按组织排序");
        $("#order_by_status").attr("title", (order == "status" && dir == "desc") ? "按状态倒序排序" : "按状态排序");
        page_index = 1;

        get_all_item();
    }

    function order_by_audit(v) {
        if (v == orderAudit) {
            dirAudit = (dirAudit == "asc") ? "desc" : "asc";
        } else {
            orderAudit = v;
            dirAudit = "asc";
        }

        $("#order_by_id_audit").attr("title", (orderAudit == "id" && dirAudit == "desc") ? "按登录名倒序排序" : "按登录名排序");
        $("#order_by_name_audit").attr("title", (orderAudit == "name" && dirAudit == "desc") ? "按姓名倒序排序" : "按姓名排序");
        $("#order_by_gender_audit").attr("title", (orderAudit == "gender" && dirAudit == "desc") ? "按性别倒序排序" : "按姓名排序");
        $("#order_by_org_audit").attr("title", (orderAudit == "org" && dirAudit == "desc") ? "按组织倒序排序" : "按组织排序");
        page_index = 1;

        get_unaudit_item();
    }

    function get_all_item() {
        var _order = "u.lastModified";
        switch (order) {
            case "id": _order = "u.userId";break;
            case "name": _order = "u.name";break;
            case "gender": _order = "u.gender";break;
            case "org": _order = "u.organizationId";break;
            case "time": _order = "u.lastModified";break;
            case "status": _order = "u.status";break;
        }
        var search_word = $.trim($("#search_word").val());
        $.ajax({
            type: "POST",
            url: "/user/search.action",
            //dataType: "json",
            dataType: "text",
            data: {"orgId":filter_org_id,"search":search_word,"pageBean.pageSize":page_size,"pageBean.pageNo":page_index,"pageBean.orderBy":_order, "pageBean.orderDir":dir},
            success: function(msg) {
                $("#tabAllUser tbody").html("");
                var response = eval("(function(){return " + msg + ";})()");
                var record_count = response.totalCount;

                for (var i = 0; i < response.users.length; i++) {
                    var o = response.users[i];
                    $("#tabAllUser tbody").append(build_item(o));
                }

                $('[data-rel=tooltip]').tooltip();
                rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "to_page", record_count);
            }
        });
    }

    function get_unaudit_item(){
        $("#select_all_audit").prop("checked",false);
        var _order = "u.lastModified";
        switch (orderAudit) {
            case "id": _order = "u.userId";break;
            case "name": _order = "u.name";break;
            case "gender": _order = "u.gender";break;
            case "org": _order = "u.organizationId";break;
            case "time": _order = "u.lastModified";break;
        }
        var search_word = $.trim($("#search_word_audit").val());
        $.ajax({
            type: "POST",
            url: "/user/unauditUsers.action",
            //dataType: "json",
            dataType: "text",
            data: {"search":search_word,
                "pageBean.pageSize":page_size,
                "pageBean.pageNo":page_index_audit,
                "pageBean.orderBy":_order,
                "pageBean.orderDir":dirAudit},
            success: function(msg) {
                $("#tabAuditUser tbody").html("");
                var response = eval("(function(){return " + msg + ";})()");
                var record_count = response.totalCount;

                if(show_bubble){
                    $("#count_bubble").html(record_count).show();
                    //setTimeout('$("#count_bubble").hide()', 5000);
                    //show_bubble = false;
                }

                for (var i = 0; i < response.users.length; i++) {
                    var o = response.users[i];
                    $("#tabAuditUser tbody").append(build_item_audit(o));
                }

                $('[data-rel=tooltip]').tooltip();
                rebuild_page_nav($("#page-nav-audit"), Math.ceil(record_count / page_size), page_index_audit, "to_page_audit", record_count);
            }
        });
    }

    function getGender(g){
        switch(parseInt(g)){
        case 1: return "男";break;
        case 0: return "女";break;
        default:return "未知";
        }
    }
    function getStatusName(status){
        switch(parseInt(status)){
        case _USER_STATUS_NORMAL: return "正常";break;
        case _USER_STATUS_LOCKED: return "锁定";break;
        case _USER_STATUS_UN_AUDITED: return "待审"; break;
        default:
        }
        return "未知";
    }
    function getOrgName(orgId){
        if(orgUtils){
            var org = orgUtils.findOrganization(orgId);
            if(org) return org.name;
        }

        return "";
    }
    function build_item(o) {
        if (o) {
           var output ='<tr><td><input type="checkbox" name="normal_user" value="' + o.userId + '"></td><td>' + o.userId + '</td>' +
                          '<td>'+ o.name + '</td>' +
                          '<td class="center">' + getGender(o.gender) +'</td>' +
                          '<td class="center">'+ getOrgName(o.organizationId) + '</td>' +
                          '<td class="center">' + getStatusName(o.status)+ '</td>' +
                          '<td class="center"><a data-placement="bottom" ' +
                          'data-original-title="编辑" data-rel="tooltip" class="btn btn-grey btn-xs" ' +
                          'href="#" onclick="editUser(\'' + o.userId + '\');return false">' +
                           '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                           '</a><a class="btn btn-grey btn-xs" data-rel="tooltip" ' +
                           'data-original-title="删除" data-placement="bottom" href="#" ' +
                            'onclick="removeUser(\'' + o.userId + '\');return false">' +
                           '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>' +
                           '</a> </td></tr>';
            //console.info(output);
            return output;
        } else {
            return "";
        }
    }

    function build_item_audit(o){
        if (o) {
            var output =
                    '<tr><td class="center"><input type="checkbox" name="unaudit_user" value="' + o.userId + '"></td>' +
                    '<td>' + o.userId + '</td>' +
                    '<td>'+ o.name + '</td>' +
                    '<td class="center">' + getGender(o.gender) +'</td>' +
                    '<td class="center">'+ getOrgName(o.organizationId) + '</td>' +
                    '<td class="center"><a data-placement="bottom" ' +
                    'data-original-title="编辑" data-rel="tooltip" class="btn btn-grey btn-xs" ' +
                    'href="#" onclick="editUser(\'' + o.userId + '\');return false">' +
                    '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                    '</a><a class="btn btn-grey btn-xs" data-rel="tooltip" ' +
                    'data-original-title="删除" data-placement="bottom" href="#" ' +
                    'onclick="removeUser(\'' + o.userId + '\');return false">' +
                    '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>' +
                    '</a> </td></tr>';
            //console.info(output);
            return output;
        } else {
            return "";
        }
    }

    function removeUser(userId){
        if( confirm("确认要删除" + userId + "吗？") ){
            $.ajax({
                type: "POST",
                url: "/user/r.action",
                dataType: "text",
                data: {"obj.userId": userId},
                success: function(msg){
                    var response = eval("(function(){return " + msg + ";})()");
                    if(response.success){
                        if($("#tabAllUser").is(":visible")) {
                            $("#userEditModal").modal("hide");
                            get_all_item();
                        }else{
                            get_unaudit_item();
                        }
                    }else{
                        //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                        alert("删除用户失败：" +  response.error);
                    }
                 }
            }).fail(function(){
            });
        }
    }

    function newFrontUser(){
        // 初始化数据，显示编辑窗口
        user_id = "";
        $("#m_user_id").removeAttr("disabled");
        $("#m_user_id").val("");
        $("#m_name").val("");
        $("#m_password").val("");
        $("#m_password").removeAttr("placeholder");
        $("#m_password_retype").val("");
        $("#m_gender").val(1);
        $("#btn_lock").hide();
        $("#btn_delete").hide();
        $("#userEditModal").modal("show");
        if(orgUtils){
            /*
            delete($('#user-org').data().tree);
            $('#user-org').remove();
            $("#m_org_container").append('<div id="user-org" class="tree" style="height: 160px;overflow:scroll"></div>');
            */
            orgUtils.setSelectedOrganization("");
            user_org = "";
            showUserOrgTree(orgUtils.generateTreeData());
        }
    }

    function doSaveFrontUser(){
        // 检查输入
        var hasError = false;
        if( !isRegisterUserName($.trim($("#m_user_id").val()))){
            $('#m_user_id').closest("div.form-group").addClass("has-error");
            alert("登录名不合法，请使用长度为5-20位长的字母和数字组合！");
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

        if( user_org == ""){
            $("#user-org").closest("div.form-group").addClass("has-error");
            hasError = true;
        }

        var mail = jQuery.trim($("#m_email").val());
        if(mail != ""){
            console.info(mail);
            if(!isValidateEmail(mail)){
                $("#m_email").closest("div.form-group").addClass("has-error");
                hasError = true;
            }
        }

        var cellPhone = jQuery.trim($("#m_cell_phone").val());
        if(cellPhone != ""){
            if(!isCellPhoneNumber(cellPhone)){
                $("#m_cell_phone").closest("div.form-group").addClass("has-error");
                hasError = true;
            }
        }

        if(hasError) return;

        var url = (user_id == "")? "/user/n.action": "/user/m.action";
        var pwd = (user_id == "" || $.trim($("#m_password").val()).length>0)? hex_md5($("#m_password").val()):"";
        $.ajax({
            type: "POST",
            url: url,
            dataType: "text",
            data: {"obj.userId": $.trim($("#m_user_id").val()),
                "obj.name": encodeURI($.trim($("#m_name").val())),
                "obj.password": pwd,
                "obj.gender": $("#m_gender").val(),
                "obj.typeId": $("#m_userType").val(),
                "obj.mail": mail,
                "obj.phone": cellPhone,
                "obj.organizationId": user_org},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success){
                    order = "time";
                    page_index = 1;
                    $("#userEditModal").modal("hide");
                    get_all_item();
                }else{
                    //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                    alert("创建用户失败：" +  response.error);
                }
             }
        }).fail(function(){
            alert("呀，创建用户失败！");
        });
    }

    function editUser(userId){
        $.ajax({
            type: "POST",
            url: "/user/g.action",
            dataType: "text",
            data: {"obj.userId": userId},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if(response.success){
                    user_id = userId;
                    $("#m_user_id").val(userId).attr('disabled','disabled');
                    $("#m_name").val(response.data.name);
                    $("#m_password").val("");
                    $("#m_password").attr("placeholder", "不修改密码时保持输入框为空");
                    $("#m_password_retype").val("");
                    $("#m_gender").val(response.data.gender);
                    $("#m_email").val(response.data.mail);
                    $("#m_cell_phone").val(response.data.phone);
                    $("#btn_lock").show();
                    user_status = response.data.status;
                    switch(user_status){
                        case _USER_STATUS_LOCKED: $("#btn_lock").html("解锁");break;
                        case _USER_STATUS_NORMAL: $("#btn_lock").html("锁定");break;
                        case _USER_STATUS_UN_AUDITED: $("#btn_lock").html("审核");
                    }
                    $("#btn_delete").show();
                    $("#m_userType").val(response.data.typeId);
                    $("#userEditModal").modal("show");
                    if(orgUtils){
/*
                        delete($('#user-org').data().tree);
                        $('#user-org').remove();
                        $("#m_org_container").append('<div id="user-org" class="tree" style="height: 160px;overflow:scroll"></div>');
*/
                        orgUtils.setSelectedOrganization(response.data.organizationId);
                        user_org = response.data.organizationId;
                        showUserOrgTree(orgUtils.generateTreeData());
                    }

                }else{
                    //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                    alert("获取用户信息失败：" +  response.error);
                }
             }
        }).fail(function(){
            alert("呀，获取用户信息失败！");
        });
    }

    function setStatus(userId, m){
        // m 1 unlock 9 lock
        $.ajax({
            type: "POST",
            url: "/user/status.action",
            dataType: "text",
            data: {"obj.userId": userId, "obj.status":m},
            success: function(msg){
                var response = parseJson(msg);
                if(response.success){
                    $("#userEditModal").modal("hide");
                    if($("#tabAllUser").is(":visible")) {
                        get_all_item();
                    }else{
                        get_unaudit_item();
                    }
                }else{
                    //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
                    alert("设置用户状态失败：" +  response.error);
                }
             }
        }).fail(function(){
            alert("呀，设置用户状态失败！");
        });
    }

    function doAudit(){
        var ids = getSelectedArray();
        if(ids.length == 0){
            alert("请选择要审核通过的用户");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/user/auditUsers.action",
            dataType: "text",
            data: {"userIdArray": ids.join(",")},
            success: function(msg){
                var response = parseJson(msg);
                if(response.success){
                    get_unaudit_item();
                }else{
                    alert("审核用户失败：" +  response.error);
                }
            }
        }).fail(function(){
            alert("呀，审核用户状态失败！");
        });
    }

    function doRemove(){
        var ids = getSelectedArray();
        if(ids.length == 0){
            alert("请选择要删除的用户");
            return;
        }else{
            if(!confirm("真的要删除这" + ids.length + "个用户吗？")) return;
        }

        $.ajax({
            type: "POST",
            url: "/user/br.action",
            dataType: "text",
            data: {"userIdArray": ids.join(",")},
            success: function(msg){
                var response = parseJson(msg);
                if(response.success){
                    if($("#tabAllUser").is(":visible")) {
                        get_all_item();
                    }else{
                        get_unaudit_item();
                    }

                }else{
                    alert("删除用户失败：" +  response.error);
                }
            }
        }).fail(function(){
            alert("呀，删除用户失败！");
        });
    }

    function getSelectedArray(){
        var checkboxes;
        if($("#tabAllUser").is(":visible")) {
            checkboxes = $("#tabAllUser tbody input:checkbox:checked");
        }else{
            checkboxes = $("#tabAuditUser tbody input:checkbox:checked");
        }
        var idArray = [];
        checkboxes.each(function(){
           idArray.push(this.value);
        });

        return idArray;
    }
</script>

</body>
</html>
