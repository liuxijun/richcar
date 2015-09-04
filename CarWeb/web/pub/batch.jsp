<%@ page import="com.fortune.rms.business.content.model.ContentFile" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.fortune.util.JsonUtils" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-9
  Time: 10:18:11
  发布视频的选择或上传文件
--%><%@ taglib prefix="s" uri="/struts-tags" %><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    List<ContentFile> contentFiles =(List<ContentFile>) session.getAttribute("contentFiles");

    if(contentFiles==null){
        contentFiles = new ArrayList<ContentFile>(0);
    }
    Content content = (Content) session.getAttribute("media");
    if(content==null){
        content = new Content();
        content.setDeviceId(-1L);
    }
%><!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>批量发布视频 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp"%>
    <link rel="stylesheet" href="../style/uploadify.css"/>
    <script src="pubFile.js" type="text/javascript"></script>
    <script type="text/javascript">
        function loadData(){
            pubFileOptions.loadMovieFiles('/',0,10);
        }
        pubFileOptions.nextPageUrl = "batchNext.jsp";
        pubFileOptions.nextPagePrepareUrl = '../media/rp-m!batchImport.action';
        pubFileOptions.currentDeviceId = '<%=content.getDeviceId()%>';
        pubFileOptions.operator='<s:property value="#session.sessionOperator.id"/>';
        pubFileOptions.cspId = '<s:property value="#session.sessionOperator.cspId"/>';
        pubFileOptions.selectedFileStore = <%=JsonUtils.getJsonString(contentFiles)%>;
    </script>
</head>
<body class="no-skin" onload="loadData()">
<!-- #section:basics/navbar.layout -->
<%@include file="../inc/displayHeader.jsp"%>
<!-- /section:basics/navbar.layout -->
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
            <i class="ace-icon fa fa-upload"></i>批量发布视频
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">
                <h4>选择XML文件</h4>

                <%--<form role="form" class="form-horizontal" id="fileupload" action="do.upload"
                      method="POST" enctype="multipart/form-data">--%>
                <div class="col-xs-12 no-padding movie-info">
                    <div class="tabbable">
                        <ul id="myTab" class="nav nav-tabs">
                            <li class="active">
                                <a href="#upload-file" data-toggle="tab">
                                    上传XML文件
                                </a>
                            </li>
                            <li class="">
                                <a href="#select-file" data-toggle="tab">
                                    选择已上传文件
                                </a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="upload-file">
                                <div style="height:30px;" class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right ">名称</label>
                                    <div class="col-sm-11">
                                        <input type="text" class="col-sm-5">
                                    </div>
                                </div>
                                <div style="height:30px;display:none;" class="form-group" id="streamServerContainer">
                                    <label class="col-sm-1 control-label no-padding-right " for="streamServerUrl">服务器</label>
                                    <div class="col-sm-11">
                                        <select id="streamServerUrl" class="col-sm-5" onchange="pubFileOptions.serverChanged(this.value)"></select><span style="color:#808080"> *如果不能选择，是因为您选择了文件或者上传了文件，请点击“选择已上传文件”的标签继续...</span>
                                    </div>
                                </div>
                                <div style="min-height:330px;" class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right ">文件</label>
                                    <div class="col-sm-11">
                                        <div class="row fileupload-buttonbar">
                                            <div class="row">
                                                <!-- The fileinput-button span is used to style the file input field as button -->

                                                <%--<input type="file" name="upload" id="upload">--%>
                                                <butotn type="file" name="upload" id="upload" class="btn btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span>添加文件</span>
                                                </butotn>
                                                <button type="submit" id="startUpload" class="btn btn-primary start">
                                                    <i class="glyphicon glyphicon-upload"></i>
                                                    <span>启动上传</span>
                                                </button>
                                                <button type="reset" id="cancelUpload" class="btn btn-warning cancel">
                                                    <i class="glyphicon glyphicon-ban-circle"></i>
                                                    <span>取消上传</span>
                                                </button>
                                                <%--
                                                                                                    <button type="button" class="btn btn-danger delete">
                                                                                                        <i class="glyphicon glyphicon-trash"></i>
                                                                                                        <span>删除</span>
                                                                                                    </button>
                                                --%>
                                                <%--<input type="checkbox" class="toggle">--%>
                                                <!-- The global file processing state -->
                                                <span class="fileupload-process"></span>
                                            </div>
                                            <!-- The global progress state -->
                                            <div id="fileQueue" class="uploadify-progress">
                                                <%--
                                                                                                    <!-- The global progress bar -->
                                                                                                    <div class="progress progress-striped active" role="progressbar"
                                                                                                         aria-valuemin="0" aria-valuemax="100">
                                                                                                        <div class="progress-bar progress-bar-success"
                                                                                                             style="width:0%;"></div>
                                                                                                    </div>
                                                                                                    <!-- The extended global progress state -->
                                                                                                    <div class="progress-extended">&nbsp;</div>
                                                --%>
                                            </div>
                                        </div>
                                        <!-- The table listing the files available for upload/download -->
                                        <table role="presentation" class="table table-striped">
                                            <tbody class="files"></tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="select-file">
                                <div id="selectedFiles">
                                    <table class="table table-striped table-bordered table-hover" id="sample-table-2">
                                        <thead>
                                        <tr>
                                            <th width="90%">已经选择的XML文件名称</th>
                                            <th class="center">删除</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                                <div class="btn-group">
                                        <span class="form-control " id="serverSideFileCaption">
                                            服务器端XML文件
                                        </span>
                                </div>
                                <div class="input-group pull-right search-group" style="width: 220px;">
                                    <input type="text" placeholder="" class="form-control search-query" id="search_word" value="*.xml"/>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_file_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                                </div>

                                <div class="space-6"></div>
                                <div>
                                    <table class="table table-striped table-bordered table-hover" id="sample-table-3">
                                        <thead>
                                        <tr><th width="60%" class="sortable" id="name">文件名</th>
                                            <th class="sortable center" id="size" width="20%">信息</th>
                                            <th class="sortable center" id="modifyDate" width="20%">日期</th><th class="center">选择</th>
                                        </tr>
                                        </thead>
                                        <tbody id="serverSideFiles">
                                        </tbody>
                                        <tr><td colspan='4' align='right'><div class="col-md-6 col-md-offset-4"><ul class="pagination pull-right" id="page-nav"></ul></div></td></tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="space-10"></div>
                    <div class="btn btn-blue" id="doTestXml" style="display: none">启动导入</div>
                    <div class="btn btn-blue" id="doImportXml">启动导入</div>
                    <div class="space"></div>
                </div>
                <!-- /.row -->
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
<%@include file="../inc/displayFooter.jsp"%>
<div id="modalDialog"></div>
<!-- inline scripts related to this page -->
<script type="text/javascript">


    jQuery(function ($) {

        $('textarea[class*=autosize]').autosize({append: "\n"});
        $('textarea.limited').inputlimiter({
            remText: '%n character%s remaining...',
            limitText: 'max allowed : %n.'
        });

        $("#open-movie-title-boxs").click(function () {
            var $a = $(".movie-title-boxs");
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

        $("#btn_file_search").click(function(){
            pubFileOptions.filter = $("#search_word").val();
            pubFileOptions.goToPage(1);
        });
        pubFileOptions.init();
        pubFileOptions.renderSelected(pubFileOptions.selectedFileStore);
        $("#doImportXml").click(function(){
            doXml('import');
        });
        $("doTestXml").click(function(){

        });
    });
</script>
<!-- The template to display files available for upload -->
<script>
    function doXml(command){
/*

        if(data==null){
            return;
        }
*/      if(pubFileOptions.collectData(null,true)==null){
            return;
        }
        showDialog({
            renderTo:'modalDialog',
            title:'批量导入处理',
            id:'importXml',
            items:[{
                html:'<div id="importXmlMessage" style="width:100%;height:400px;text-align: left;overflow-y: auto">正在向服务器端发送，请耐心等待....</div>'
            }],
            buttons:[{text:'关闭',handler:function(){
                $("#importXml").modal('hide');
            }}]
        });
        var store = pubFileOptions.selectedFileStore;
        var i= 0,l=store.length,result="";
        for(;i<l;i++){
            var item = pubFileOptions.selectedFileStore[i];
            result+="正在提交请求处理："+item.name+"<br/>";
            var data={importXmlCommand:command,
                fileName:item.path,
                "obj.deviceId":pubFileOptions.currentDeviceId,
                moduleId:10000
            };
            $.ajax({
                url:'../media/rp-m!batchImport.action',
                dataType:'json',
                data:data,
                method:'post',
                success:function(resultData){
                    if(resultData&&resultData['messages']){
                        var messages = resultData['messages'];
                        var resultMsg = "";
                        for(var m= 0,ml=messages.length;m<ml;m++){
                            resultMsg+=messages[m]+"<br/>";
                        }

                        $("#importXmlMessage").append(resultMsg);
                    }else{
                        $("#importXmlMessage").append("服务器端处理发生异常："+resultData['error']);
                    }
                },
                error:function(req,err){
                    $("#importXmlMessage").append("服务器端处理发生异常："+err);
                }
            });
        }

    }
    function renderUploadFiles(o) {
        var result = '';
        for (var i = 0, file; file = o.files[i]; i++) {
            var fileName = decodeURI(file.name);
            result += '<tr class="template-upload fade">' +
                    '<td class="preview"><span class="fade"></span></td>' +
                    '<td class="name"><span>' +fileName+
                    '</span></td>' +
                    '<td class="size"><span>'+pubFileOptions.formatFileSize(file.size)+'</span></td>';
            if (file.error) {
                result += '<td class="error" colspan="2"><span class="label label-important">错误</span></td>';
            } else if (o.files.valid && !i) {
                result += '<td>' +
                        '    <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>' +
                        '</td>' +
                        '<td class="start">';
                if (!o.options.autoUpload) {
                    result += '<button class="btn btn-primary">' +
                            '<i class="icon-upload icon-white"></i>' +
                            '<span>启动</span>' +
                            '</button>';
                }
                result += '</td>';
            } else {
                result += '<td colspan="2"></td>';
            }
            result += '<td class="cancel">';
            if (!i) {
                result += '<button class="btn btn-warning">' +
                        '<i class="icon-ban-circle icon-white"></i>' +
                        '<span>'+locale.fileupload.cancel+'</span>' +
                        '</button>';
            }
            result += '</td></tr>';
        }
    }
</script>
<script src="../scripts/jquery.min.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="../scripts/fileupload/jquery.ui.widget.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="../scripts/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="../scripts/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="../scripts/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS and Bootstrap Image Gallery are not required, but included for the demo -->
<
<script src="../scripts/bootstrap.min.js"></script>
<script src="js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="../scripts/jquery.iframe-transport.js"></script>
<!-- uploadify -->
<script src="../scripts/jquery.uploadify.min.js"></script>
<%--
<!-- The basic File Upload plugin -->
<script src="../scripts/fileupload/jquery.fileupload.js"></script>
<!-- The File Upload file processing plugin -->
<script src="../scripts/fileupload/jquery.fileupload-fp.js"></script>
<!-- The File Upload user interface plugin -->
<script src="../scripts/fileupload/jquery.fileupload-ui.js"></script>
--%>
<!-- The localization script -->
<script src="js/locale.js"></script>
<!-- The main application script -->
<%--
<script src="js/main.js"></script>
--%>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE8+ -->
<!--[if gte IE 8]>
<script src="js/cors/jquery.xdr-transport.js"></script><![endif]-->
<script src="upload.js" charset="GBK"></script>
<script>
    fileExts = "*.xml";
</script>
</body>
</html>
