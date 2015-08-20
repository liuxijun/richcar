<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
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
<%@include file="/inc/displayMenu.jsp" %>
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
                <a href="../man.jsp"> 网站首页</a>
            </li>
            <li class="active">${folderName}</li><li class="active">${functionName}</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-user"></i>角色管理

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">


                    <div class="col-xs-12 no-padding movie-info">

                        <div class="tabbable">

                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th width="50%">角色名</th>
                                    <th class="center">管理员个数</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody class="rolesContain">
                                </tbody>
                            </table>
                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2"><a class="btn btn-green btn-big" onclick="roleList.showRoleDetail(-1,5);return false;">新建角色 </a></div>
                            <div class="col-md-6 col-md-offset-4">
                                <ul id="page-nav" class="pagination pull-right">
                                </ul>
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
    <div id="modalDialog"></div>
</div>
<!-- inline scripts related to this page -->
<script type="text/javascript">


    jQuery(function ($) {
        $('.scrollable').each(function () {
            var $this = $(this);
            $(this).ace_scroll({
                size: $this.data('size') || 100
                //styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
            });
        });
        $('[data-rel=tooltip]').tooltip();
        $('[data-rel=popover]').popover({html: true});
        $('textarea[class*=autosize]').autosize({append: "\n"});
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
        $('textarea.limited').inputlimiter({
            remText: '%n character%s remaining...',
            limitText: 'max allowed : %n.'
        });

        $("#open-movie-title-boxs").click(function () {
            var $a = $(".movie-title-boxs")
            $this = $(this);
            if ($a.is(':visible')) {
                $a.slideUp(300, function () {
                    $this.html("查看更多文件");
                });
            } else {
                $a.slideDown(300, function () {
                    $this.html("收起");
                });


            }
        });

        $('#id-input-file-1,#id-input-file-2').ace_file_input({
            no_file: '未选择文件 ...',
            btn_choose: '选择',
            btn_change: '选择',
            droppable: false,
            onchange: null,
            thumbnail: false //| true | large
            //whitelist:'gif|png|jpg|jpeg'
            //blacklist:'exe|php'
            //onchange:''
            //
        });


        var DataSourceTree = function (options) {
            this._data = options.data;
            this._delay = options.delay;
        }

        DataSourceTree.prototype.data = function (options, callback) {
            var self = this;
            var $data = null;

            if (!("name" in options) && !("type" in options)) {
                $data = this._data;//the root tree
                callback({ data: $data });
                return;
            }
            else if ("type" in options && options.type == "folder") {
                if ("additionalParameters" in options && "children" in options.additionalParameters)
                    $data = options.additionalParameters.children;
                else $data = {}//no data
            }

            if ($data != null)//this setTimeout is only for mimicking some random delay
                setTimeout(function () {
                    callback({ data: $data });
                }, parseInt(Math.random() * 500) + 200);

            //we have used static data here
            //but you can retrieve your data dynamically from a server using ajax call
            //checkout examples/treeview.html and examples/treeview.js for more info
        };

        $(".btn-dropdown").click(function () {
            $("#tree1").slideDown();
        });
        $("#tree1").mouseleave(function () {
            $(this).slideUp();

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

        roleList.goToPage(1);
    });
    var roleList={
        limit:10,
        currentPage:1,
        listRoles:function(parameters){
            $.ajax({
                url:'../security/role!listRoles.action',
                data:parameters,
                dataType:'json',
                success:function(jsonData){
                    rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /roleList.limit),
                            roleList.currentPage, "roleList.goToPage", jsonData['totalCount']);
                    var roles = jsonData['objs'];
                    var result = '';
                    if(roles!=null){
                        var i= 0,l=roles.length;
                        for(;i<l;i++){
                            var role = roles[i];
                            result +=
                                 '<tr>' +
                                    '<td>' + role.name+'</td>' +
                                    '<td class="center">' + role.count+'</td>' +
                                    '<td class="center"><a class="btn btn-grey btn-xs"  onclick="roleList.showRoleDetail(' +role.id+','+role.type+
                                         ');return false;">'+
                                    '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                    '  </a><a class="btn btn-grey btn-xs" onclick="roleList.deleteRole(' +role.id+
                                         ','+role.type+');return false;">'+
                                    '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                    '  </a></td>'+
                                 '</tr>';
                        }
                    }
                    $('.rolesContain').html(result);

                }
            });
        },
        currentRoleId:-1,
        currentRoleType:-1,
        deleteRole:function(id,type){
            var roleType = parseInt(type);
            if(roleType == 1||roleType==2||roleType==3){//1是超户，2是媒体管理，3是系统管理
                alert("系统预定义角色，不能删除");
                return false;
            }
            if(confirm("您确认要删除这个角色吗？")){
                $.ajax({
                    url:'/security/role!delete.action',
                    data:{keyId:id},
                    dataType:'json',
                    success:function(jsonData){
                        alert("删除过程结束！");
                        roleList.goToPage(1);
                    }
                });
            }
        },
        saveRole:function(){
            var roleName=$.trim($("#roleName").val());
            var roleId = $("#roleId").val();//roleList.currentRoleId;//
            var roleMemo = $("#roleMemo").val();
            var roleType = $("#roleType").val();

            if(roleType==1||roleType==2||roleType==3){
                alert("系统预定义角色，不能修改。");
                return false;
            }
            if(roleName==''){
                alert("请输入角色名称");
                return false;
            }
            var checkedMenus =$( "input[name='role-menu-checkbox']:checked" );
            if(checkedMenus.length == 0){
                alert("请为角色选择权限！");
                return false;
            }

            var selectedRoles = [];
            var selectedIds = '';
            checkedMenus.each(function(i){
                if(selectedIds!=''){
                    selectedIds+=",";
                }
                selectedIds += ( $(this).val());
            });
            if(selectedIds==''){
                alert("请至少选择一个权限！否则这个角色不会被保存！");
                return false;
            }
            if(roleId==null||typeof(roleId)=='undefined'){
                roleId=-1;
            }
            $.ajax({
                url:'/security/role!save.action?keyId='+roleId,
                method:'post',
                data:{
                    'obj.roleid':roleId,
                    'obj.name':roleName,
                    'obj.memo':roleMemo,
                    'obj.type':roleType,
                    'keyIds':selectedIds
                },
                dataType:'json',
                success:function(jsonData){
                    alert("已经保存！");
                    roleList.goToPage(1);
                    $("#roleDetailModal").modal('hide');
                }
            });
            return true;
        },
        showRoleDetail:function(id,type){
            roleList.currentRoleId=id;
            type = parseInt(type);
            roleList.currentRoleType=type;
            //$("#roleDetailModal").model('show');
            showDialog({
                id:'roleDetailModal',
                renderTo:'modalDialog',
                title:'角色详情',
                items:[
                    {id:'roleName',fieldLabel:'角色名称',allowBlank:false},
                    {id:'adminMenus',fieldLabel:'角色权限',type:'div',cls:'control-group control-group-inline'},
                    {id:'roleDesp',type:'textarea',fieldLabel:'角色描述'}
                ],
                hiddenItems:[{id:'roleId'}],
                buttons:[
                    {text:'确定',cls:'btn-blue',handler:roleList.saveRole},
                    {text:'关闭',style:'margin-left:100px;',handler:function(){
                        $("#roleDetailModal").modal('hide');
                    }}]
            });
            if(type==1||type==2||type==3){
                $("#roleDetailModalLabel").html("查看预定义角色详情（无法修改）");
            }else{
                $("#roleDetailModalLabel").html("角色详情");
            }
            $.ajax({
                url:'/security/role!view.action?keyId='+id,
                dataType:'json',
                success:function(jsonData){
                    var data = jsonData['data'];
                    var menusInfo = '';
                    if(data!=null){
                        var roleName = $("#roleName");
                        roleName.val(data['obj.name']);
                        $("#roleId").val(data['obj.roleid']);
                        roleList.currentRoleId = data['obj.id'];
                        roleList.currentRoleType = data['obj.type'];
                        $("#roleMemo").val(data['obj.memo']);
                        $("#roleType").val(data['obj.type']);
                        var menus = data['obj.menus'];
                        var i= 0,l=menus.length;
                        var outputCount = 0;
                        var parentMenus = [];
                        for(;i<l;i++){
                            var parent = menus[i];
                            var pid = parseInt(parent.parentId);
                            if(pid==0){
                                parentMenus.push(parent);
                            }
                        }
                        var pi= 0,pl=parentMenus.length;
                        for(;pi<pl;pi++){
                            var p = parentMenus[pi];
                            var ppid=parseInt(p['id']);
                            menusInfo+='<div style="width:100%;color:gray;margin-top:10px;">'+p['name']+'下的功能</div>';
                            outputCount=0;
                            for(i=0;i<l;i++){
                                var menu = menus[i];
                                var parentId = parseInt(menu.parentId);
                                if(parentId==0||parentId!=ppid){
                                    continue;
                                }
                                outputCount++;
                                if(outputCount%4==1&&outputCount!=1){
                                    //menusInfo+="</tr><tr>";
                                }
                                var checked = "";
                                if(menu['extra']=='selected'){
                                    checked = " checked='checked' ";
                                }
                                menusInfo += '<div class="checkbox"><label>' +
                                '<input ' +checked+' type="checkbox" class="ace" name="role-menu-checkbox"' +
                                ' value="' +menu.id + '"><span class="lbl">' + menu.name + '</span></label></div>';
                            }
                        }
                    }
                    menusInfo +="";
                    $("#adminMenus").html(menusInfo);
                }
            });
        },
        goToPage:function(pageNo){
            roleList.currentPage = pageNo;
            roleList.listRoles({limit:roleList.limit,start:(pageNo-1)*roleList.limit});
        }
    };

</script>

</body>
</html>
