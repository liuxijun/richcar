<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 14-12-8
  Time: 上午9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <style type="text/css">
        .selected {
            background-color: #808080;
            color: #000000;
            cursor: pointer;
        }

        .unselected {
            background-color: white;
            color: #808080;
            cursor: pointer;
        }

        .li {
            height: 35px;
            font-size: 13px;
        }

        .public {
            border-width: thin;
            border-style: ridge;
            border-color: #dcdcdc;
            width: 200px;
            height: 300px;
            float: left;
        }
    </style>
</head>
<body class="no-skin">
<%@include file="../inc/displayHeader.jsp" %>
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <%@include file="/inc/displayMenu.jsp" %>
    <div class="main-content">
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
                <li class="active">${folderName}</li>
                <li class="active">${functionName}</li>
            </ul>
        </div>
        <div class="page-header">
            <h1>
                <i class="ace-icon fa fa-user"></i>CSP设备管理
            </h1>
        </div>
        <div class="page-content">
            <div class="page-content-area">
                <div class="row page-content-main">
                    <div class="input-group pull-right search-group" style="width:100px;height: 30px; margin: 9px;">
                        <div style="width: 150px;height: 30px;float: left;">
                            <input type="text" id="search_name" placeholder="名称" class="form-control search-query" >
                        </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                    </div>
                    <div class="col-xs-12 no-padding movie-info">
                        <div class="tabbable">
                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th class="center">名称</th>
                                    <th class="center">是否CP</th>
                                    <th class="center">是否SP</th>
                                    <th class="center">绑定设备</th>
                                    <th class="center">绑定CP</th>
                                    <th class="center">绑定产品</th>
                                    <th class="center">绑定频道</th>
                                    <th class="center">管理模块</th>
                                    <th class="center">审核人</th>
                                    <th class="center">管理</th>
                                </tr>
                                </thead>
                                <tbody id="CspContain">

                                </tbody>
                            </table>
                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2">
                                <a class="btn btn-green btn-big" onclick="cspList.showCspDetail(-1)">新增设备</a>
                            </div>
                            <div class="col-md-6 col-md-offset-4">
                                <ul id="page-nav" class="pagination pull-right"></ul>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div>
    <%@include file="../inc/displayFooter.jsp" %>
    <div id="cspDetailLog"></div>
    <div id="deviceDetailLog"></div>
    <div id="moduleDetailLog"></div>
    <div id="channelDetailLog"></div>
</div>
<script type="text/javascript" src="/su/cspBinding.js"></script>
<%--<%@include file="cspBinding.jsp" %>--%>
<script type="text/javascript">
var page_index = 1;
var page_size = 5;
var cspModuleString = null;
jQuery(function ($) {
    $('.scrollable').each(function () {
        var $this = $(this);
        $(this).ace_scroll({
            size:$this.data('size') || 100
        });
    });
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html:true});
    $('textarea[class*=autosize]').autosize({append:"\n"});
    $(document).ajaxStart(function () {
        $("#loading_container").show();
    });

    $(document).ajaxStop(function () {
        setTimeout(function () {
            $("#loading_container").hide();
        }, 200);

    });
    $('textarea.limited').inputlimiter({
        remText:'%n character%s remaining...',
        limitText:'max allowed : %n.'
    });

    $("#btn_search").click(function () {
        cspList.goToPage(1);
    });
    cspList.goToPage(1);

});
var cspList;
//noinspection JSDuplicatedDeclaration
cspList = {
    limit:10,
    currentPage:1,
    Csps:[],
    order:'id',
    status:[
        {name:"有效", id:"1"},
        {name:"失效", id:"5"}
    ],
    type:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isCp:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isSp:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isCpOnlineAudit:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isCpOfflineAudit:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isSpOnlineAudit:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    isSpOfflineAudit:[
        {name:"是", id:"1"},
        {name:"否", id:"0"}
    ],
    getIsCp:function (cp) {
        return cspList.getTextOfArray(cp, cspList.isCp, 'id', 'name');
    },
    getIsSp:function (sp) {
        return cspList.getTextOfArray(sp, cspList.isSp, 'id', 'name');
    },

    getIsDisplayManager:function(isSp,id){
        var url="/csp/csp!searchCpsBySpId.action"
        if(isSp == 1){
            return '<a style="cursor: pointer" onclick="loadCspName('+id+','+"'"+url+"'"+')">'+'管理'+'</a>';
        }else{
            return '  ';
        }
    },
    getIsDisplayProduct:function(isSp,id){
        var url="/csp/csp!searchProductBySpId.action"
        if(isSp == 1){
            return '<a style="cursor: pointer" onclick="loadProductName('+id+','+"'"+url+"'"+')")">'+'绑定'+'</a>'
        }else{
            return ' ';
        }
    },
    getIsDisplayChannel:function(isSp,id){
     //   var url="/csp/csp!searchProductBySpId.action"
        if(isSp == 1){
            return '<a style="cursor: pointer" onclick="loadChannelBinding('+id+')">'+'绑定'+'</a>'
        }else{
            return ' ';
        }
    },
    renderCsp:function (jsonData) {
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] / cspList.limit),
                cspList.currentPage, "cspList.goToPage", jsonData['totalCount']);
        var Csps = jsonData['objs'];
        var result = '';
        if (Csps != null) {
            var i = 0, l = Csps.length;
            for (; i < l; i++) {
                var csp = Csps[i];
                var url =null;
                result +=
                        '<tr>' +
                                '<td>' + csp.name + '</td>' +
                                '<td class="center">' + cspList.getIsCp(csp['isCp']) + '</td>' +
                                '<td class="center">' + cspList.getIsSp(csp['isSp']) + '</td>' +
                                '<td class="center"><a href="#" onclick="loadDeviceName(' + csp.id + ')">' + '绑定' + '</a></td>' +
                                '<td class="center">'+cspList.getIsDisplayManager(csp['isSp'],csp['id'])+ '</td>'+
                                '<td class="center">'+cspList.getIsDisplayProduct(csp['isSp'],csp['id'])+'</td>' +
                                '<td class="center">'+cspList.getIsDisplayChannel(csp['isSp'],csp['id'])+'</td>' +
                                '<td class="center"><a href="#" onclick="loadModules(' + csp.id + ')">' + '管理' + '</a></td>' +
                                '<td class="center"><a href="#" onclick="cspList.moduleBinding('+csp.id+')">' + '管理' + '</a></td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick=" cspList.showCspDetail(' + csp.id +
                                ');return false;">' +
                                '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                                '  </a><a class="btn btn-grey btn-xs" onclick="cspList.deleteCsp(' + csp.id +
                                ');return false;">' +
                                '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>' +
                                '  </a></td>' +
                                '</tr>';
            }
        }
        $('#CspContain').html(result);
    },

    listCsp:function () {
        $.ajax({
            url:'../csp/csp!list.action',
            data:({"obj.name":$("#search_name").val(),
                limit:cspList.limit, start:(cspList.currentPage - 1) * cspList.limit }),
            method:'post',
            dataType:'json',

            success:function (jsonData) {
                cspList.renderCsp(jsonData);
            }
        });
    },
  //  currentCspId:-1,
    showCspDetail:function (id) {
        cspList.currentCspId = id;
        showDialog({id:'cspDetailModal', renderTo:'cspDetailLog', title:'Csp基本信息',
            items:[
                {fieldLabel:'名称', id:'name', allowBlank:false},
                {fieldLabel:'地址', id:'address'},
                {fieldLabel:'手机', id:'phone'},
                {fieldLabel:'邮箱', id:'email'},
                {fieldLabel:'别名', id:'alias'},
                {fieldLabel:'spId', id:'spId', allowBlank:false},
                {html:'<div class="form-group">'+
                       '<label class="col-sm-3 control-label no-padding-right filed-need">当前状态</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="status">'+
                        '<option value="5">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">大包月用户</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="type">'+
                        '<option value="0">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">是否Cp</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isCp">'+
                        '<option value="0">否</option><option value="1">是</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">是否Sp</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isSp">'+
                        '<option value="0">否</option><option value="1">是</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">Cp审核上线</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isCpOnlineAudit">'+
                        '<option value="0">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">Cp审核下线</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isCpOfflineAudit">'+
                        '<option value="0">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">Sp审核上线</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isSpOnlineAudit">'+
                        '<option value="0">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'+
                        '<div class="form-group">'+
                        '<label class="col-sm-3 control-label no-padding-right filed-need">Sp审核下线</label>'+
                        '<div class="col-sm-7">'+
                        '<select class="form-control" id="isSpOfflineAudit">'+
                        '<option value="0">失效</option><option value="1">有效</option>'+
                        '</select></div></div>'
                }
            ],
            buttons:[
                {text:'确定', cls:'btn-blue', handler:cspList.saveCsp},
                {text:'关闭', style:'margin-left:100px;', handler:function () {
                    $("#cspDetailModal").modal('hide');
                }}
            ]
        });
        if (id > 0) {
            $.ajax({
                url:'/csp/csp!view.action?keyId=' + id,
                dataType:'json',
                success:function (jsonData) {
                    var data = jsonData['data'];
                    if (data != null) {
                        $("#name").val(data['obj.name']);
                        $("#address").val(data['obj.address']);
                        $("#phone").val(data['obj.phone']);
                        $("#email").val(data['obj.email']);
                        $("#alias").val(data['obj.alias']);
                        $("#spId").val(data['obj.spId']);
                        cspList.createSelect('status', cspList.status, data['obj.status']);
                        cspList.createSelect('type', cspList.type, data['obj.type']);
                        cspList.createSelect('isCp', cspList.isCp, data['obj.isCp']);
                        cspList.createSelect('isSp', cspList.isSp, data['obj.isSp']);
                        cspList.createSelect('isCpOnlineAudit', cspList.isCpOnlineAudit, data['obj.isCpOnlineAudit']);
                        cspList.createSelect('isCpOfflineAudit', cspList.isCpOfflineAudit, data['obj.isCpOfflineAudit']);
                        cspList.createSelect('isSpOnlineAudit', cspList.isSpOnlineAudit, data['obj.isSpOnlineAudit']);
                        cspList.createSelect('isSpOfflineAudit', cspList.isSpOfflineAudit, data['obj.isSpOfflineAudit']);

                    }
                }
            });
        }
    },
    deleteCsp:function (id) {
        if (confirm("您确认要删除该记录吗？")) {
            $.ajax({
                url:"/csp/csp!delete.action",
                data:{keyId:id},
                dataType:'json',
                success:function (jsonData) {
                    alert("删除完毕！");
                    cspList.goToPage(1);
                }
            });
        }
    },
    saveCsp:function () {
        var id = cspList.currentCspId;
        if (id == null || typeof(id) == 'undefined' || id == '-1' || id == '0' || id == 0) {
            id = -1;
        }
        if (id != -1 && !confirm("您确认您要保存吗？\r\n")) {
            return;
        }
        var name = $("#name").val();
        var type = $("#type").val();
        var address = $("#address").val();
        var phone = $("#phone").val();
        var status = $("#status").val();
        var email = $("#email").val();
        var alias = $("#alias").val();
        var spId = $("#spId").val();
        var isCp = $("#isCp").val();
        var isSp = $("#isSp").val();
        var isCpOnlineAudit = $("#isCpOnlineAudit").val();
        var isCpOfflineAudit = $("#isCpOfflineAudit").val();
        var isSpOnlineAudit = $("#isSpOnlineAudit").val();
        var isSpOfflineAudit = $("#isSpOfflineAudit").val();
        $.ajax({
            url:'../csp/csp!save.action',
            method:'post',
            data:{
                'obj.id':id,
                'obj.name':name,
                'obj.type':type,
                'obj.status':status,
                'obj.address':address,
                'obj.phone':phone,
                'obj.email':email,
                'obj.alias':alias,
                'obj.spId':spId,
                'obj.isCp':isCp,
                'obj.isSp':isSp,
                'obj.isCpOnlineAudit':isCpOnlineAudit,
                'obj.isCpOfflineAudit':isCpOfflineAudit,
                'obj.isSpOnlineAudit':isSpOnlineAudit,
                'obj.isSpOfflineAudit':isSpOfflineAudit
            },
            dataType:'text',
            success:function () {
                alert("已保存！");
                cspList.goToPage(1);
                $("#cspDetailModal").modal("hide");
            }
        }).fail(function () {
                    alert("失败！");
                });
    },
    moduleBinding:function (id) {
        cspList.currentCspId = id;
        showDialog({id:'moduleModal', renderTo:'moduleDetailLog', title:'绑定', width:500,
            items:[
                {html:'<div>'+
                        '<div class="public" style="overflow-y:scroll; ">' +
                        '<span><p class="center">可选项</p></span>' +
                        '<ul id="moduleName" style="list-style:none;margin:0px; "></ul>' +
                        '</div></div>' +
                        '<div style="width: 30px;height: 300px;float: left;">' +
                        '<button style="margin-top: 100px;margin-left: 10px;" onclick="cspList.moveToRight1()"><img  src="/images/report/g4.jpg"></button>' +
                        '<button style="margin-top: 50px;margin-left: 10px;"onclick="cspList.moveToLeft1()"><img src="/images/report/g3.jpg"></button>' +
                        '</div>' +
                        '<div class="public" style="margin-left: 20px;overflow-y: scroll;">' +
                        '<span><p class="center">已选项</p></span>' +
                        '<span class="btn btn-grey" style="width: 200px" onclick="cspList.empty()">清空</span>' +
                        '<div >' +
                        '<ul id="selectedModules" style="list-style:none;margin:0px;"></ul>' +
                        '</div>' +
                        '</div>'}
            ],
            buttons:[
                {text:'确定', cls:'btn-blue', handler:cspList.saveModules},
                {text:'关闭', style:'margin-left:250px;', handler:function () {
                    $("#moduleModal").modal('hide');
                }}
            ]
        });
        cspList.loadAdminAndCspName(id);
        cspList.loadAdminName(id);
//        cspList.loadModules(id);
//        cspList.loadSelectModules(id);
    },
    modules:[],
    loadAdminAndCspName: function(id) {
    $.ajax({
        url:'/csp/cspAuditor!searchAuditorsByCspId.action?keyId=' + id ,
        dataType:'text',
        type:'post',
        success:function (msg) {
            var response = eval("(function(){return " + msg + ";})()");
            cspList.modules = response.objs;
           cspList.moduleIdAndCspId();
        }
    });
},
    admin:[],
    loadAdminName:function(id){
    $.ajax({
        url:'/security/admin!searchAdmin.action?keyId=' + id,
        type:'post',
        dataType:'text',
        success:function (msg) {
            var response = eval("(function(){return " + msg + ";})()");
            cspList.admin = response.objs;
            cspList.moduleIdAndCspId();
        }
    });
},
    renderModules:function () {
        var moduleNameUl = $("#moduleName");
        var selectedModules = $('#selectedModules');
        moduleNameUl.html('');
        selectedModules.html('');
        var modules = cspList.modules;
        for (var i = 0; i < modules.length; i++) {
            var module = modules[i];
            module['clicked'] = false;
            if (module['selected'] == true) {
                selectedModules.append("<li class='li'  id='" + module['id'] + "'><div class='unselected' id='m" + module['id'] + "'" +
                        "onclick='cspList.selectModule(" + module['id'] +
                        "," + i + ")'>" + module['name'] +
                        "</div></li>");
            }else {
                moduleNameUl.append("<li class='li'><div class='unselected' id='m" + module['id'] + "'" +
                        "onclick='cspList.selectModule(" + module['id'] +
                        "," + i + ")'>" + module['name'] +
                        "</div></li>");
            }
        }
    },
    cspModules:null,
    moduleIdAndCspIds:null,

    moduleIdAndCspId:function () {
        if(cspList.modules==null||cspList.admin==null){
            return;
        }
        var cspModules = cspList.admin;
        var modules = cspList.modules;
        for (var j = 0; j < modules.length; j++) {
            var module = modules[j];
            module['selected']=false;
            for (var i = 0; i < cspModules.length; i++) {
                var cspModule = cspModules[i];
                if (module['id'] == cspModule['moduleId']) {
                    module['selected'] = true;
                    break;
                }
            }
        }
        cspList.renderModules();
    },

    selectModule:function (moduleId, idx) {
        var hasBeenClicked = cspList.modules[idx]['clicked'];
        if (hasBeenClicked) {
            $("#m" + moduleId).removeClass("selected");
            $("#m" + moduleId).addClass("unselected");
            cspList.modules[idx]['clicked'] = false;
        } else {
            $("#m" + moduleId).removeClass("unselected");
            $("#m" + moduleId).addClass("selected");
            cspList.modules[idx]['clicked'] = true;
        }
    },
    moveToRight1:function () {
        var i = 0, l = cspList.modules.length;
        for (; i < l; i++) {
            var d = cspList.modules[i];
            if (d['clicked']) {
                if (!d['selected']) {
                    d['selected'] = true;
                }
            } else {
            }
        }
        cspList.renderModules();
    },
    moveToLeft1:function () {
        var i = 0, l = cspList.modules.length;
        for (; i < l; i++) {
            var d = cspList.modules[i];
            if (d['clicked']) {
                if (d['selected']) {
                    d['selected'] =false;
                } else {
                }
            }
        }
        cspList.renderModules();
    },
    empty:function () {
        $("#selectedModules").empty();
    },
    goToPage:function (pageNo) {
        cspList.currentPage = pageNo;
        cspList.listCsp();
    },
    getTextOfArray:function (val, data, valueField, displayField) {
        var i = 0, l = data.length;
        for (; i < l; i++) {
            var m = data[i];
            if (m[valueField] == val) {
                return m[displayField];
            }
        }
        return '否';
    },
    createSelect:function (id, options, value) {
        value = parseInt(value);
        var selectCmp = $("#" + id);
        selectCmp.html('');
        var i = 0, l = options.length;
        for (; i < l; i++) {
            var option = options[i];
            var selected = false;
            var val = parseInt(option['id']);
            if (val == value) {
                selected = true;

            }
            selectCmp.append($("<option/>", {
                value:val,
                selected:selected,
                text:option.name
            }));
        }
    }
//    loadChannelBinding:function(id){
//        //   var url ="/csp/cspModule!searchModulesByCspId.action"
//        $.ajax({
//            url: '/publish/channel!getChooseChannels.action?keyId='+id,
//            dataType:'text',
//            type:'post',
//            success:function (msg) {
//                var response = eval("(function(){return " + msg + ";})()");
//                Binding = response.objs;
//                renderChannel();
//            }
//        });
//    }

}


</script>

</body>
</html>
