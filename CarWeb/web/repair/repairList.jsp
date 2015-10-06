<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    int type = StringUtils.string2int(request.getParameter("type"), 1);

%><!DOCTYPE html>
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
    var type = <%=type%>;
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
                    <div class="input-group pull-right search-group" style="width:316px;height: 30px; margin: 9px;float:left;">
                        <div style="width:120px;float:left;line-weight:30px;font-size:22px;">车牌号码：</div>
                        <div style="width:150px;height: 30px;float: left;">
                            <input type="text" id="search_no" placeholder="例如：吉A09928" class="form-control" >
                        </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                    </div>

                    <div class="col-xs-12 no-padding movie-info">

                        <div class="tabbable">
                            <div style="width:520px;float:left;line-weight: 30px;font-size:22px;" id="currentParentName"></div><br/>
                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th width="50%" align="center"><a href="#" onclick='list.order_by("carNo")'>车牌号码</a></th>
                                    <th width="20%" align="center"><a href="#" onclick='list.order_by("status")'>状态</a></th>
                                    <th width="20%"  align="center"><a href="#" onclick='list.order_by("createTime")'>日期</a></th>
                                    <th align="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="contains">

                                </tbody>
                            </table>

                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2"><a class="btn btn-green btn-big" href="repairView.jsp?type=<%=type%>&keyId=-1">新增</a></div>
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
 var itemName;
 var displayFault = false;
 function setByType(type){
     if(type==null||typeof(type)=='undefined'){
         type = <%=type%>;
     }
     switch(type){
         case 2:
             itemName = "维修项目";
             break;
         case 3:
             itemName = "维修项目";
             break;
         default:
             itemName = "养护项目";
             break;
     }
     displayFault = type==2;
 }

 var list={
    limit:10,
    currentPage:1,
    status:[{text:'已经完成',value:3},{text:'排队中',value:1},{text:'正在进行中',value:2}],
    truncateTime:function(val){
        if(val!=null&&val.length>10){
            return val.substring(0,10);
        }
        if(val==null){
            return '';
        }
        return val;
    },
    render:function(jsonData){
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /list.limit),
                list.currentPage,"list.goToPage", jsonData['totalCount']);
        var objs = jsonData['objs'];
        var result = '';
        if(objs!=null){
            var i= 0,l=objs.length;
            for(;i<l;i++){
                var obj = objs[i];
                //var eTime = list.truncateTime(obj['motEtime']);
                var createTime = (obj['createTime']);
                result +=
                        '<tr>' +
                                '<td><a href="repairView.jsp?keyId=' +obj['id']+'">'/*+obj['fileId']+':'*/+ obj['carNo']+'</a></td>' +
                                '<td class="center">'+list.getTextOfArray(obj['status'],list.status,'value','text')+'</td>' +
                                '<td class="center">' + createTime+'</td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  href="repairView.jsp?type=<%=type%>keyId=' +obj['id']+'">'+
                                '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                '  </a><a class="btn btn-grey btn-xs" onclick="list.deleteItem('+obj['id']+
                                ',\''+obj['carNo']+'\');return false;">'+
                                '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                '  </a></td>'+
                                '</tr>';
            }
        }
        $('#contains').html(result);
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

        var _order = list.order;
        if(_order==null||typeof(_order)=='undefined'){
            _order = "id";
            list.dir="desc";
        }
        _order = "o1."+_order;
        $.ajax({
            url:'../repair/repair!list.action',//menu!listFunctionMenus.action
            data:({"obj.type":type,
                "obj.carNo":$("#search_no").val(),
                "pageBean.pageSize":list.limit,
                "pageBean.pageNo":page_index,
                "pageBean.orderBy":_order,
                "pageBean.orderDir":list.dir,
                limit:list.limit, start:(list.currentPage - 1) * list.limit }),
            method:'post',
            dataType:'json',
            success:function(jsonData){
                  list.render(jsonData);
            }
        });
    },
    currentDictCode:'',
    willCreateItem:false,
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
    deleteItem:function(id,carNo){
        if(confirm("您确认要删除这条"+itemName+"记录吗？？\n车牌为："+carNo)){
            $.ajax({
                url:'../repair/repair!delete.action',
                data:{keyId:id},
                dataType:'json',
                success:function(jsonData){
                    alert("删除完毕！");
                    list.goToPage(1);
                }
            });
        }
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
setByType(type);
</script>

</body>
</html>
