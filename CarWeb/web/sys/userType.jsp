<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-27
  Time: 9:24:31
  用户类型管理
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>用户类型管理 - <%=IndividualUtils.getInstance().getName()%></title>
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
            <li class="active">用户类型管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-users"></i>用户类型管理

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
                             <div class="alert alert-block alert-success" id="hint-4-new">
                                 <button type="button" class="close" data-dismiss="alert">
                                     <i class="ace-icon fa fa-times"></i>
                                 </button>
                                 <i class="ace-icon fa fa-leaf"></i>
                                 您刚刚增加了新的用户类型，如果系统中已经有很多视频，要关联给新增的用户类型，请点<a href="userTypeChannel.jsp" class="btn btn-green">这里</a> 通过栏目关联已有视频。
                             </div>
                             <div class="space-6"></div>
                             <table class="table table-striped table-bordered table-hover table-30">
                                 <thead>
                                 <tr>

                                     <th width="80%">名称</th>
                                     <th class="center">人数</th>
                                     <th class="center">操作</th>
                                 </tr>
                                 </thead>

                                 <tbody>
                                 </tbody>
                             </table>

                         </div>
                        <div class="space-6"></div>
                         <div class="row">
                             <div class="col-md-2"><a class="btn btn-green btn-big" id="new_user_type">新建用户类型 </a></div>
                             <div class="col-md-6 col-md-offset-4"> </div>
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
    <div class="modal fade modal-class" id="altTypeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left:50%;margin: -80px 0 0 -200px; width:400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="altModalTitle">请选择该类型下用户归入的替代类型：</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline">
                    <select class="form-control " id="sel-alt-user-type" style="width: 200px">
                    </select>
                    <button class="btn btn-lightwhite btn-big2 margin0" type="button" id="btnRemoveUserType">
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
 </div>
<div class="modal fade modal-class" id="userTypeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left:50%;margin: -80px 0 0 -200px; width:400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="modalTitle">新建用户类型管理</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline">
                    <input type="text"  class="input-large" id="userTypeName">
                    <button class="btn btn-lightwhite btn-big2 margin0" type="button" id="addUserType">
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


<%@include file="../inc/displayFooter.jsp" %>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    var _userTypeId = -100;
    var _userTypeArray = [];

    jQuery(function ($) {
        $("#hint-4-new").hide();

        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        
        loadUserType();
        $("#new_user_type").click(function(){
            $("#userTypeName").val("");
            $("#userTypeModal").modal('show');
            $('#userTypeModal').on('shown.bs.modal', function() {
                $("#userTypeName").focus();
            });

        });

        $("#addUserType").click(function(){
           doAddUserType(); 
        });

        $("#btnRemoveUserType").click(function(){
           doRemoveUserType();
        });

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                if( $("#userTypeName").is(":focus")){
                    event.preventDefault();
                    doAddUserType();
                }
            }
        });
    });

    function loadUserType(){
        $.ajax({
            type: "POST",
            url: "/user/userType!userTypeList.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
                $("tbody").html("");
                var response = eval("(function(){return " + msg + ";})()");

                _userTypeArray = [];
                for(var i=0; i<response.userTypes.length; i++){
                    var type = response.userTypes[i];
                    if(type){
                        $("tbody").append(buildTypeItem(response.userTypes[i]));
                    }
                }
                $('[data-rel=tooltip]').tooltip();
             }
        });
    }

    function buildTypeItem(o){
        if(o){
            _userTypeArray.push(new UserType(o.userType.id, o.userType.name, o.userCount));
            return  '<tr><td>' + o.userType.name +
                          '</td><td class="center">' + o.userCount + '</td>' +
                          '<td class="center"><a data-placement="bottom" data-original-title="编辑" data-rel="tooltip" ' +
                          'class="btn btn-grey btn-xs"  href="#" onclick="editType(' + o.userType.id +
                         ');return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                         '</a><a class="btn btn-grey btn-xs" data-rel="tooltip" data-original-title="删除" ' +
                         'data-placement="bottom" onclick="removeType(' + o.userType.id +
                         ');return false;"><i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i></a></td></tr>';
        }

        return "";
    }

    function doAddUserType(){
        if( $.trim($("#userTypeName").val()) == ""){
            $("#userTypeName").focus();
            return;
        }

        $("#addUserType").html("正在保存").attr('disabled','disabled');
        $("#new_user_type").attr('disabled','disabled');
        $.ajax({
            type: "POST",
            url: "/user/userType!save.action",
            dataType: "text",
            data: {"obj.id":_userTypeId,"obj.name":$("#userTypeName").val()},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                $("#addUserType").html("确定");
                $("#addUserType").removeAttr('disabled');
                $("#new_user_type").removeAttr('disabled');

                $("#userTypeModal").modal('hide');
                $("#hint-4-new").show();
                loadUserType();
             }
        }).fail(function() {
           alert( "WTF：添加用户类型失败，请重新登录试试。" );
            $("#addUserType").html("确定");
            $("#addUserType").removeAttr('disabled');
            $("#new_user_type").removeAttr('disabled');

            $("#userTypeModal").modal('hide');
        });
    }

    function UserType(id, name, count){
        this.id = id;
        this.name = name;
        this.count = count;
    }
    function editType(id){
        var _name = "";
        for(var i=0; i<_userTypeArray.length; i++){
            if( _userTypeArray[i].id == id){
                _name = _userTypeArray[i].name;
                break;
            }
        }
        _userTypeId = id;
        
        $("#userTypeName").val(_name);
        $("#userTypeModal").modal('show');
        $('#userTypeModal').on('shown.bs.modal', function() {
            $("#userTypeName").focus();
        });
    }

    function resetAltList(id){
        $("#sel-alt-user-type").empty();
        for(var i=0; i<_userTypeArray.length; i++){
            if( _userTypeArray[i].id == id) continue;
            $("#sel-alt-user-type").append($("<option/>", {
                value: _userTypeArray[i].id,
                text: _userTypeArray[i].name
            }));
        }
    }

    function removeType(id){
        _userTypeId = id;
        for(var i=0; i<_userTypeArray.length; i++){
            if( _userTypeArray[i].id == id){
                if( _userTypeArray[i].count >= 0 ){
                    resetAltList(id);
                     $("#altTypeModal").modal('show');
                }
            }
        }
    }

    function doRemoveUserType(){
        $("#removeUserType").html("正在删除").attr('disabled','disabled');
        $("#new_user_type").attr('disabled','disabled');
        $.ajax({
            type: "POST",
            url: "/user/userType!remove.action",
            dataType: "text",
            data: {"obj.id":_userTypeId,"altType":$("#sel-alt-user-type").val()},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                $("#removeUserType").html("确定");
                $("#removeUserType").removeAttr('disabled');
                $("#new_user_type").removeAttr('disabled');

                $("#altTypeModal").modal('hide');
                loadUserType();
             }
        }).fail(function() {
           alert( "WTF：删除用户类型失败，请重新登录试试。" );
            $("#removeUserType").html("确定");
            $("#removeUserType").removeAttr('disabled');
            $("#new_user_type").removeAttr('disabled');

            $("#altTypeModal").modal('hide');
        });
    }
</script>

</body>
</html>
