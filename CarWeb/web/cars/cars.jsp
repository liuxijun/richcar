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
    <title>管理首页-车辆管理</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <style type="text/css">
              .hover{
                  cursor:pointer;
                  background: white;
                  color:#000000;
              }
        .asc{
            background: url('/images/sort_asc.png') no-repeat 50px;

        }
        .desc{
            background: url('/images/sort_desc.png') no-repeat 50px;
        }
    </style>
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
            <i class="ace-icon fa fa-car"></i>${functionName}
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
        <div class="page-content-area">
            <div class="row page-content-main">

                <form role="form" class="form-horizontal">
                    <div class="input-group pull-right search-group" style="width:640px;height: 30px; margin: 9px;float:left;">
                        <div style="width:120px;float:left;font-weight: 30px;font-size:22px;">车牌号码：</div>
                        <div style="width:150px;height: 30px;float: left;">
                            <input type="text" id="search_no" placeholder="例如：吉A09928" class="form-control" >
                        </div>
                        <div style="width:100px;float:left;font-weight: 30px;font-size:22px;">，联系人：</div>
                        <div style="width: 150px;height: 30px;float: left;">
                            <input type="text" id="search_user" placeholder="联系人" class="form-control search-query" >
                        </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                    </div>

                    <div class="col-xs-12 no-padding movie-info">

                        <div class="tabbable">
                            <div style="width:520px;float:left;font-weight: 30px;font-size:22px;" id="currentParentName"></div><br/>
                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th width="50%" align="center><a href="#" onclick='list.order_by("carNo")'>车牌</a></th>
                                    <th width="10%" align="center"><a href="#" onclick='list.order_by("user")'>联系人</a></th>
                                    <th class="center"><a href="#" onclick='list.order_by("createTime")'>添加日期</a></th>
                                    <th class="center">上次保养日期</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="contains">

                                </tbody>
                            </table>

                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2"><a class="btn btn-green btn-big" href="carView.jsp?keyId=-1">新增条目</a></div>
                            <div class="col-md-6 col-md-offset-4">
                                <ul  id="page-nav" class="pagination pull-right">
                            </ul></div>
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
    <div id="modalDialog">
    </div>
    <!-- inline scripts related to this page -->
<script type="text/javascript">
 var page_index = 1;
 var page_size = 10;
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
    };

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
            else $data = {};//no data
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
//    list.initFolders();
    $("#btn_search").click(function(){
       list.goToPage(1);
    });
    list.goToPage(1);
});
var list={
    limit:10,
    currentPage:1,
    render:function(jsonData){
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /list.limit),
                list.currentPage,"list.goToPage", jsonData['totalCount']);
        var objs = jsonData['objs'];
        var result = '';
        if(objs!=null){
            var i= 0,l=objs.length;
            for(;i<l;i++){
                var obj = objs[i];
                result +=
                        '<tr>' +
                                '<td>' + obj['carNo']+'</td>' +
                            '<td class="center">' + obj['userId']+'</td>' +
                                '<td class="center">' + obj['createTime']+'</td>' +
                                '<td class="center">'+obj['maintainTimes']+'</td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick="list.showDetail(' +obj['id']
                        +',\''+obj['carNo']+'\''+');return false;">'+
                                '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                '  </a><a class="btn btn-grey btn-xs" onclick="list.delete('+obj['id']+
                                '\''+obj['carNo']+'\');return false;">'+
                                '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                '  </a></td>'+
                                '</tr>';
            }
        }
        $('#dictContain').html(result);
    },
    order_by:function (v) {
        if (v == list.order) {
            list.dir = ( list.dir == "asc") ? "desc" : "asc";
        } else {
            list.order = v;
            list.dir = "desc";
        }
        list.list();
    },
    list:function(){
        var _order = "o1.code";
        switch (list.order) {
            case "name":
                _order = "o1.name";
                break;
            case "parentCode":
                _order = "o1.parentCode";
                break;
        }
        $.ajax({
            url:'../cars/car!list.action',//menu!listFunctionMenus.action
            data:({"obj.name":$("#search_name").val(),
                "obj.code":$("#search_code").val(),
                "obj.parentCode":list.currentParentCode,
                "pageBean.pageSize":list.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":list.dir,
                limit:list.limit, start:(list.currentPage - 1) * list.limit }),
            method:'post',
            dataType:'json',

            success:function(jsonData){
                  list.render(jsonData);
            }
        });
    },
    currentDictCode:'',
    delete:function(id,status){
        if(confirm("您确认要删除这个字典条目吗？？")){
            $.ajax({
                url:'../cars/cars!delete.action',
                data:{keyId:id},
                dataType:'json',
                success:function(jsonData){
                    alert("删除完毕！");
                    list.goToPage(1);
                }
            });
        }
    },
    save:function(){
        var code = $("#dictCode").val();//list.currentMenuId;//
        if(code==''||code==' '||code==null||typeof(code)=='undefined'||code=='-1'||code=='0'||code==0){
            alert("代码和名字是必须录入的，请仔细检查！");
            return;
        }
        var name = $("#dictName").val();
        if(name==''||name==null){
            alert("代码和名字是必须录入的，请仔细检查！");
            return;
        }
        if(!confirm("您确认要保存吗？" )){
            return;
        }

        var dictDesp = $("#dictDesp").val();
        var dictParentCode = $("#dictParentCode").val();
        $.ajax({
            url:'../cars/cars!save.action',
            method:'post',
            data:{
                'obj.code':code,
                'obj.name':name,
                'obj.parentCode':dictParentCode,
                'obj.desp':dictDesp,
                'willCreateNew':list.willCreateItem
            },
            dataType:'json',
            success:function(jsonData){
                var success = jsonData['success'];
                if(success==true||success=='true'){
                    alert("已经保存！");
                    list.goToPage(1);
                    $("#menuDetailModal").modal('hide');
                }else{
                    alert("保存中发生异常："+jsonData['error'][0]);
                }
            }
        });
        return true;
    },
    createSelect:function(id,options,value){
        value = parseInt(value);
        var selectCmp = $("#"+id);
        selectCmp.html('');
//        var result = '<select name="'+id+'" id="'+id+'" value="'+value+'"  class="col-sm-12">';
        var i= 0,l=options.length;
        for(;i<l;i++){
            var option = options[i];
            var selected = false;
            var val = parseInt(option['id']);
            if(val==value){
                selected = true;

            }
            selectCmp.append($("<option/>", {
                value: val,
                selected:selected,
                text: option.name
            }));
//            result+='<option value="'+option['value']+'" ' +selected+'>'+option['name']+'</option>';
        }
//        result +='</select>';
//        return result;
    },
    willCreateItem:false,
    showDetail:function(code,name){
        if(code==null||code==-1){
            list.willCreateItem = true;
        }else{
            list.willCreateItem = false;
        }
        list.currentDictCode=code;
        showDialog({id:'dictDetailModal',renderTo:'modalDialog',title:'条目详情',
            items:[
                {fieldLabel:'条目名称：',id:'dictName',allowBlank:false,value:name},
                {fieldLabel:'条目代码：',id:'dictCode',allowBlank:false,value:code},
                {fieldLabel:'上级条目：',id:'dictParentName',value:list.currentParentName},
                {fieldLabel:'描述详情：',id:'dictDesp'}
            ],
            hiddenItems:[{id:'dictParentCode',value:list.currentParentCode}],
            buttons:[
                {text:'确定',cls:'btn-blue',handler:list.save},
                {text:'关闭',style:'margin-left:100px;',handler:function(){
                $("#dictDetailModal").modal('hide');
            }}]});
        if(!list.willCreateItem){
            $.ajax({
                url:'/config/menu!view.action?keyId='+id,
                dataType:'json',
                success:function(jsonData){
                    var data = jsonData['data'];
                    if(data!=null){
                        $("#dictName").val(data['obj.name']);
                        $("#dictCode").val(data['obj.code']);
                        $("#dictParentCode").val(data['obj.parentCode']);
                        $("#dictDesp").val(data['obj.desp']);
                    }
                }
            });
        }
    },
    goToPage:function(pageNo){
        list.currentPage = pageNo;
        list.list();
    },
    getTextOfArray:function(val,data,valueField,displayField){
        var i= 0,l=data.length;
        for(;i<l;i++){
            var m = data[i];
            if(m[valueField]==val){
                return m[displayField];
            }
        }
        return '未知';
    },
    getDesp:function(desp){
        return desp;
    },
    currentParentName:'系统',
    currentParentCode:'systemRoot',
    getFolderName:function(parentCode){
        if(parentCode=='systemRoot'){
            return '';
        }
        return list.currentParentName;
    }
};

</script>

</body>
</html>
