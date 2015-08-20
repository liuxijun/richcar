<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 14-12-5
  Time: 上午8:57
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
        <div class="page-header">
            <h1>
                <i class="ace-icon fa fa-user"></i>设备管理
            </h1>
        </div>
        <div class="page-content">
            <div class="page-content-area">
                <div class="row page-content-main">
                    <div class="input-group pull-right search-group" style="width: 500px;height: 30px; margin: 9px;">
                        <div style="width: 150px;height: 30px;float: left;">
                            <input type="text" id="search_name" placeholder="名称" class="form-control search-query" >
                        </div>
                        <div style="width: 100px;height: 30px;float: left;">
                            <input type="text" id="search_ip" placeholder="IP" class="form-control search-query" >
                        </div>
                        <div>
                            <div style="width: 100px;height: 30px;float: left;">
                                <input type="text" id="searchFfpUser" placeholder="FTP端口" class="form-control search-query" >
                            </div>
                            <div style="width: 100px;height: 30px;float: left;">
                                <input type="text" id="searchURL" placeholder="URL路径" class="form-control search-query">
                            </div>
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
                                    <th class="center"><a href="#" onclick='deviceList.order_by("name")'>名称</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("type")'>类型</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("ip")'>IP地址</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("url")'>URL路径</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("status")'>当前状态</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("ftpPort")'>FTP端口</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("ftpUser")'>FTP用户名</a></th>
                                    <th class="center"><a href="#" onclick='deviceList.order_by("ftpPath")'>FTP路径</a></th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="DeviceContain">

                                </tbody>
                            </table>
                        </div>
                            <div class="space-6"></div>
                            <div class="row">
                                <div class="col-md-2">
                                    <a class="btn btn-green btn-big" onclick="deviceList.showDeviceDetail(-1)">新增设备</a>
                                </div>
                                <div class="col-md-6 col-md-offset-4">
                                    <ul  id="page-nav" class="pagination pull-right"></ul>
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
    <!-- Modal -->
    <div class="modal fade modal-film" id="deviceDetailModal" tabindex="-1" role="dialog" aria-labelledby="deviceDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="position: absolute; left: 50%; margin-left: -280px; top: 50%; margin-top: -250px; width: 560px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="deviceDetailModal"> 设备基本信息</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-12 ">
                            <form class="form-horizontal">

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">名称</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="name" class="col-sm-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">类型</label>
                                    <div class="col-sm-9">
                                      <select class="col-sm-12" id="type">
                                      </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">IP地址</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="ip" class="col-sm-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">URL地址</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="url" class="col-sm-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">当前状态</label>
                                    <div class="col-sm-9" >
                                          <select class="col-sm-12" id="status">
                                          </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">FTP端口</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="ftpPort" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">FTP用户名</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="ftpUser" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">FTP密码</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="ftpPwd" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">监控端口</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="monitorPort" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">最大并发</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="maxTask" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">源文件目录</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="localPath" class="col-sm-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label no-padding-right filed-need">输出目录</label>
                                    <div class="col-sm-9">
                                        <input type="text" id="exportPath" class="col-sm-12">
                                    </div>
                                </div>

                                <div class="space"></div>
                                <span class="btn btn-blue" onclick="deviceList.saveDevice(id)">确定 </span>
                           <span class="btn btn-white" style="margin-left:300px" onclick="deviceList.close()">关闭</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" id="id">
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
<script type="text/javascript">
var page_index = 1;
var page_size = 5;
    jQuery(function ($) {
        $('.scrollable').each(function () {
            var $this = $(this);
            $(this).ace_scroll({
                size: $this.data('size') || 100
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

        $("#btn_search").click(function(){
          deviceList.goToPage(1);
        });
          deviceList.goToPage(1);
    });
    var deviceList={
        limit:10,
        currentPage:1,
        device:[],
        order:'id',
        type:[
            {name:"MMS",id:"1"},
            {name:"HLS点播服务器",id:"2"},
            {name:"WEB",id:"3"},
            {name:"转码",id:"4"},
            {name:"数据库",id:"5"},
            {name:"HLS直播服务器",id:"6"}
        ],
        status:[
            {name:"正常使用",id:"1"},
            {name:"失效",id:"5"}
        ],
        close:function(){
            $("#deviceDetailModal").modal('hide');
        },
        getTypeName:function(id){
            return deviceList.getTextOfArray(id,deviceList.type,'id','name');
        },
        getStatusName:function(status){
            return deviceList.getTextOfArray(status,deviceList.status,'id','name');
        },
        renderDevice:function(jsonData){
            rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /deviceList.limit),
                    deviceList.currentPage,"deviceList.goToPage", jsonData['totalCount']);
            var devs = jsonData['objs'];
            var result = '';
            if(devs!=null){
                var i= 0,l=devs.length;
                for(;i<l;i++){
                    var dev = devs[i];
                    result +=
                            '<tr>' +
                                    '<td>' + dev.name+'</td>' +
                                    '<td class="center">' + deviceList.getTypeName(dev['type'])+'</td>' +
                                    '<td class="center">' +dev.ip+'</td>' +
                                    '<td class="center">' +dev.url+'</td>' +
                                    '<td class="center">' +deviceList.getStatusName(dev['status'])+'</td>' +
                                    '<td class="center">' +dev.ftpPort+'</td>' +
                                    '<td class="center">' +dev.ftpUser+'</td>' +
                                    '<td class="center">' +dev.ftpPath+'</td>' +
                                    '<td class="center"><a class="btn btn-grey btn-xs"  onclick=" deviceList.showDeviceDetail(' +dev.id+
                                    ');return false;">'+
                                    '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                    '  </a><a class="btn btn-grey btn-xs" onclick="deviceList.deleteDevice(' +dev.id+
                                    ');return false;">'+
                                    '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                    '  </a></td>'+
                                    '</tr>';
                }
            }
            $('#DeviceContain').html(result);
        },
        order_by:function (v) {
            if (v == deviceList.order) {
                deviceList.dir = ( deviceList.dir == "asc") ? "desc" : "asc";
            } else {
                deviceList.order = v;
                deviceList.dir = "desc";
            }
            deviceList.listDevice();
        },
        listDevice:function(){
            var _order = "o1.name";
            switch (deviceList.order) {
                case "name":
                    _order = "o1.name";
                    break;
                case "type":
                    _order = "o1.type";
                    break;
                case "ip":
                    _order = "o1.ip";
                    break;
                case "url":
                    _order = "o1.url";
                    break;
                case "status":
                    _order = "o1.status";
                    break;
                case "ftpPort":
                    _order = "o1.ftpPort";
                    break;
                case "ftpUser":
                    _order = "o1.ftpUser";
                    break;
                case "ftpPath":
                    _order = "o1.ftpPath";
                    break;
            }
            $.ajax({
                url:'../system/device!list.action',
                data:({"obj.name":$("#search_name").val(),
                    "obj.ip":$("#search_ip").val(),
                    "obj.ftpUser":$("#searchFfpUser").val(),
                    "obj.url":$("#searchURL").val(),
                    "pageBean.pageSize":deviceList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":deviceList.dir,
                    limit:deviceList.limit, start:(deviceList.currentPage - 1) *deviceList.limit }),
                method:'post',
                dataType:'json',

                success:function(jsonData){
                   deviceList.renderDevice(jsonData);
                }
            });
        },
        currentDeviceId:-1,
        showDeviceDetail:function(id){
            deviceList.currentDeviceId=id;
            $("#deviceDetailModal").modal('show');
            $.ajax({
                url:'/system/device!view.action?keyId='+id,
                dataType:'json',
                success:function(jsonData){
                    var data = jsonData['data'];
                    if(data!=null){
                        $("#name").val(data['obj.name']);
                        $("#ip").val(data['obj.ip']);
                        $("#url").val(data['obj.url']);
                        $("#ftpPort").val(data['obj.ftpPort']);
                        $("#ftpUser").val(data['obj.ftpUser']);
                        $("#ftpPwd").val(data['obj.ftpPwd']);
                        $("#ftpPath").val(data['obj.ftpPath']);
                        $("#monitorPort").val(data['obj.monitorPort']);
                        $("#maxTask").val(data['obj.maxTask']);
                        $("#localPath").val(data['obj.localPath']);
                        $("#exportPath").val(data['obj.exportPath']);
                        deviceList.createSelect('type',deviceList.type,data['obj.type']);
                        deviceList.createSelect('status',deviceList.status,data['obj.status']);
                    }
                }
            });
        },
        deleteDevice:function(id){
            if(confirm("您确认要删除该记录吗？")){
                $.ajax({
                    url:"/system/device!delete.action",
                    data:{keyId:id},
                    dataType:'json',
                    success:function(jsonData){
                        alert("删除完毕！");
                        deviceList.goToPage(1);
                    }
                });
            }
        },
        saveDevice:function(id){
            var id =deviceList.currentDeviceId;
            if(id==null||typeof(id)=='undefined'||id=='-1'||id=='0'||id==0){
                id=-1;
            }
            if(id!=-1&&!confirm("您确认您要保存吗？\r\n")){
                return;
            }
            var name = $("#name").val();
            var type =$("#type").val();
            var ip = $("#ip").val();
            var url = $("#url").val();
            var status = $("#status").val();
            var ftpPort = $("#ftpPort").val();
            var ftpUser = $("#ftpUser").val();
            var ftpPath = $("#ftpPath").val();
            var ftpPwd = $("#ftpPwd").val();
            var monitorPort = $("#monitorPort").val();
            var maxTask = $("#maxTask").val();
            var localPath = $("#localPath").val();
            var exportPath = $("#exportPath").val();
            $.ajax({
                url: '../system/device!save.action',
                method: 'post',
                data:{
                    'obj.id':id,
                    'obj.name':name,
                    'obj.type':type,
                    'obj.ip':ip,
                    'obj.url':url,
                    'obj.status':status,
                    'obj.ftpPort':ftpPort,
                    'obj.ftpUser':ftpUser,
                    'obj.ftpPath':ftpPath,
                    'obj.ftpPwd':ftpPwd,
                    'obj.monitorPort':monitorPort,
                    'obj.maxTask':maxTask,
                    'obj.localPath':localPath,
                    'obj.exportPath':exportPath
                },
                dataType:'text',
                success: function(){
                    alert("已保存！");
                    deviceList.goToPage(1);
                    $("#deviceDetailModal").modal("hide");
                }
            }).fail(function(){
                        alert("失败！");
                    });
        },
        goToPage:function(pageNo){
            deviceList.currentPage = pageNo;
            deviceList.listDevice();
        },
        getTextOfArray:function(val,data,valueField,displayField){
            var i= 0,l=data.length;
            for(;i<l;i++){
                var m = data[i];
                if(m[valueField]==val){
                    return m[displayField];
                }
            }
            return '其他';
        },
        createSelect:function(id,options,value){
            value = parseInt(value);
            var selectCmp = $("#"+id);
            selectCmp.html('');
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
            }
        }
    }
</script>
</body>
</html>