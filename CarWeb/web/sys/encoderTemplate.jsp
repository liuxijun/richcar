<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-23
  Time: 16:44:57
  视频管理
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>视频管理 - <%=IndividualUtils.getInstance().getName()%></title>
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

<table style="margin:0 auto" width="660" >
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</div>
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
            <li class="active">系统管理</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-film"></i>编码配置

        </h1>
    </div>
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">

                <form role="form" class="form-horizontal">


                    <div class="col-xs-12 no-padding movie-info">

                        <div class="tabbable">

                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>


                                    <th width="50%">配置名称</th>
                                    <th class="center">分辨率</th>
                                    <th class="center">视频码流(Kbps)</th>
                                    <th class="center">音频码流(Kbps)</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>

                                <tbody id="templatesContain">
                                <tr>


                                    <td class="color1">超清 </td>
                                    <td class="center">1280x720</td>
                                    <td class="center">1,200</td>
                                    <td class="center">128 </td>
                                </tr>
                                <tr>


                                    <td class="color1">超清 </td>
                                    <td class="center">1280x720</td>
                                    <td class="center">1,200</td>
                                    <td class="center">128 </td>
                                </tr>
                                <tr>


                                    <td class="color1">超清 </td>
                                    <td class="center">1280x720</td>
                                    <td class="center">1,200</td>
                                    <td class="center">128 </td>
                                </tr>


                                </tbody>
                            </table>

                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2"><a class="btn btn-green btn-big" onclick="templateList.showTempDetail(-1)">新增配置 </a>
                            </div>

                        </div>


                    </div>

                    <!-- /.row -->
                </form>
            </div>
            <!-- /.page-content-area -->
        </div>
        <!-- /.page-content -->
        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div>
    <!-- /.main-container -->
    <%@include file="../inc/displayFooter.jsp" %>
    <!-- Modal -->
    <div class="modal fade modal-film" id="templateDetailModal" tabindex="-1" role="dialog" aria-labelledby="templateDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog" style=" position: absolute;  width: 560px; margin-top: -160px; top: 50%; left:50%; margin-left: -280px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="templateDetailModalLabel">新增配置</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                       <div class="col-xs-12 ">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">配置名称</label>
                                <div class="col-sm-7">
                                        <input type="text" class="col-sm-12" id="templateName" name="templateName" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">模板代码</label>
                                <div class="col-sm-7">
                                    <input type="text" class="col-sm-12" id="templateCode" name="templateCode" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">文件格式</label>
                                <div class="col-sm-7">
                                    <select class="form-control" id="fileFormat">
                                           <option value="m3u8">m3u8</option>
                                           <option value="mp4">mp4</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">宽</label>
                                <div class="col-sm-7">
                                    <select class="form-control " id="VWidth" name="VWidth">
                                        <option value="172">172</option>
                                        <option value="320">320</option>
                                        <option value="640">640</option>
                                        <option value="720">720</option>
                                        <option value="1280">1280</option>
                                        <option value="1920">1920</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">高</label>
                                <div class="col-sm-7">
                                    <select class="form-control " id="VHeight" name="VHeight">
                                        <option value="144">114</option>
                                        <option value="240">240</option>
                                        <option value="360">360</option>
                                        <option value="480">480</option>
                                        <option value="540">540</option>
                                        <option value="720">720</option>
                                        <option value="1080">1080</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">视频码流（kbps）</label>
                                <div class="col-sm-7">
                                    <select class="form-control" id="VBitrate" name="VBitrate">
                                        <option value="128">128</option>
                                        <option value="256">256</option>
                                        <option value="374">374</option>
                                        <option value="758">758</option>
                                        <option value="1024">1024</option>
                                        <option value="1500">1500</option>
                                        <option value="2048">2048</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">音频码流（kbps）</label>
                                <div class="col-sm-7">
                                    <select class="form-control" id="ABitrate" name="ABitrate">
                                        <option value="8">8</option>
                                        <option value="32">32</option>
                                        <option value="64">64</option>
                                        <option value="96">96</option>
                                        <option value="128">128</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right filed-need">对应模板</label>
                                <div class="col-sm-7">
                                    <select class="form-control " id="propertyId">
                                    </select>
                                </div>
                            </div>
                            <div class="space"></div>
                            <button class="btn btn-blue" type="button"  onclick="templateList.saveTemplate(id)">确定 </button>
                            <input type="hidden" id="id">
                            <input type="hidden" id="VKeyframeInterval">
                            <input type="hidden" id="VFrameRate">
                            <input type="hidden" id="VFixedQp">
                            <input type="hidden" id="AType">
                            <input type="hidden" id="ASampleRate">
                            <input type="hidden" id="VEncoderType">
                            <input type="hidden" id="ACodec">
                            <input type="hidden" id="AChannel">
                            <input type="hidden" id="VMaxQp">

                            <button class="btn btn-lightwhite " style="margin-left: 12px;">删除 </button>
                            <span class="btn btn-lightwhite" style="margin-left: 20px;" onclick="$('#templateDetailModal').modal('hide');">关闭</span>
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
 jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html:true});
     $(document).ajaxStart(function(){
         $("#loading_container").show();
     });

     $(document).ajaxStop(function(){
         setTimeout(function(){$("#loading_container").hide();}, 200);
     });
    templateList.goToPage(1);
     templateList.loadPropertyId();
 });

var templateList={
    limit:10,
    currentPage:1,
     temps:[],
    renderTemps:function(jsonData){
        var temps = jsonData['objs'];
        var result = '';
        if(temps!=null){
            var i= 0,l=temps.length;
            for(;i<l;i++){
                var temp = temps[i];
                result +=
                        '<tr>' +
                                '<td>' + temp.templateName+'</td>' +
                                '<td class="center">' +temp.VWidth +'x'+ temp.VHeight+'</td>' +
                                '<td class="center">' +temp.VBitrate+'</td>' +
                                '<td class="center">' + temp.ABitrate+'</td>' +
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick="templateList.showTempDetail(' +temp.id+
                                ');return false;">'+
                                '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                                '  </a><a class="btn btn-grey btn-xs" onclick="templateList.deleteTemps(' +temp.id+
                                ');return false;">'+
                                '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                '  </a></td>'+
                                '</tr>';
            }
        }
        $('#templatesContain').html(result);
     //   console.info(result);
    },
    loadPropertyId:function (){
     $.ajax({
         type: "POST",
         url: "/module/property!getPropertyIdsByDataType.action",
         //dataType: "json",
         dataType: "text",
         //data: {name: ver},
         success: function(msg){
             var response = eval("(function(){return " + msg + ";})()");

             for(var i=0; i<response.objs.length; i++){
                 var type = response.objs[i];
                 if(type){
                     $("#propertyId").append($("<option/>", {
                         value: type.id,
                         text: type.name
                     }));
                 }
             }
         }
     });
 },
    listTemplate:function(){
        $.ajax({
            url:'/encoder/encoderTemplate!list.action',//menu!listFunctionMenus.action
            data:({limit:templateList.limit, start:(templateList.currentPage - 1) * templateList.limit }),
            method:'post',
            dataType:'json',
            success:function(jsonData){
                templateList.renderTemps(jsonData);
            }
        });
    },

    currentTempId:-1,

    showTempDetail:function(id){
     templateList.currentTempId=id;
       // alert(templateList.currentMenuId);
     $("#templateDetailModal").modal('show');
     $.ajax({
         url:'/encoder/encoderTemplate!view.action?keyId='+id,
         dataType:'json',
         success:function(jsonData){
             var data = jsonData['data'];
             if(data!=null){
                 $("#templateName").val(data['obj.templateName']);
                 $("#VWidth").val(data['obj.VWidth']);
                 $("#VHeight").val(data['obj.VHeight'])
                 $('#VBitrate').val(data['obj.VBitrate']);
                 $("#ABitrate").val(data['obj.ABitrate']);
                 $('#AChannel').val(data['obj.AChannel']);
                 $("#ACodec").val(data['obj.ACodec']);
                 $('#templateCode').val(data['obj.templateCode']);
                 $("#fileFormat").val(data['obj.fileFormat']);
                 $('#VEncoderType').val(data['obj.VEncoderType']);
                 $("#ASampleRate").val(data['obj.ASampleRate']);
                 $('#AType').val(data['obj.AType']);
                 $("#VFixedQp").val(data['obj.VFixedQp']);
                 $('#VFrameRate').val(data['obj.VFrameRate']);
                 $("#VKeyframeInterval").val(data['obj.VKeyframeInterval']);
                 $('#VMaxQp').val(data['obj.VMaxQp']);
                 $("#propertyId").val(data['obj.propertyId']);
             }
         }
     });
 },
    deleteTemps:function(id){
        if(confirm("您确认要删除该记录吗？")){
            $.ajax({
                url:"/encoder/encoderTemplate!delete.action",
                data:{keyId:id},
                dataType:'json',
                success:function(jsonData){
                    alert("删除完毕！");
                    templateList.goToPage(1);
                }
            });
        }
    },
    saveTemplate:function(id){
        if(id==null||typeof(id)=='undefined'||id=='-1'||id=='0'||id==0){
            id=-1;
        }
        if(id!=-1&&!confirm("您确认您要保存吗？\r\n")){
            return;
        }
         var id =templateList.currentTempId;
                var templateName=$("#templateName").val();
                var VWidth = $("#VWidth").val();
                var VHeight =$("#VHeight").val();
                var VBitrate = $("#VBitrate").val();
                var ABitrate = $("#ABitrate").val();
                var AChannel = $("#AChannel").val();
                var ACodec = $("#ACodec").val();
                var templateCode = $("#templateCode").val();
                var fileFormat = $("#fileFormat").val();
                var VEncoderType = $("#VEncoderType").val();
                var ASampleRate = $("#ASampleRate").val();
                var AType = $("#AType").val();
                var VFixedQp = $("#VFixedQp").val();
                var VFrameRate = $("#VFrameRate").val();
                var VKeyframeInterval = $("#VKeyframeInterval").val();
                var VMaxQp = $("#VMaxQp").val();
                var propertyId = $("#propertyId").val();
                if(templateName==''){
                    alert("请输入名称");
                    return false;
                }
                if(templateCode ==''){
                    alert("请输入模板代码");
                    return false;
                }
        $.ajax({
            url: '../encoder/encoderTemplate!save.action',
            method: 'post',
            data:{
                'obj.id':id,
                'obj.templateName':templateName,
                'obj.VWidth':VWidth,
                'obj.VHeight':VHeight,
                'obj.VBitrate':VBitrate,
                'obj.ABitrate':ABitrate,
                'obj.AChannel':AChannel,
                'obj.ASampleRate':ASampleRate,
                'obj.AType':AType,
                'obj.templateCode':templateCode,
                'obj.VFixedQp':VFixedQp,
                'obj.VFrameRate':VFrameRate,
                'obj.VKeyframeInterval':VKeyframeInterval,
                'obj.ACodec':ACodec,
                'obj.fileFormat':fileFormat,
                'obj.VEncoderType':VEncoderType,
                'obj.VMaxQp':VMaxQp,
                'obj.propertyId':propertyId
            },
            dataType:'text',
            success: function(){
                alert("模版已经保存！");
                templateList.goToPage(1);
               $("#templateDetailModal").modal("hide");
            }
        }).fail(function(){
                    alert("失败！");
                });
            },
    goToPage:function(pageNo){
        templateList.currentPage = pageNo;
        templateList.listTemplate();
    }
}
</script>
 </body>
</html>