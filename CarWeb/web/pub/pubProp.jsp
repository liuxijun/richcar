<%@ page import="com.fortune.util.StringUtils" %><%@ page import="java.util.Date" %><%@ taglib prefix="s" uri="/WEB-INF/tlds/struts-tags.tld" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014-10-9
  Time: 11:09:40
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>视频属性 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <link rel="stylesheet" href="../style/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="../style/fortune.posters.css"/>
    <script src="../scripts/contentDisplay.js"></script>
    <script>
        var showSnapButton = <%=AppConfigurator.getInstance().getBoolConfig("system.showSnapButton",false)%>;
        var selectedFiles = [];
        <s:iterator value="#session.contentFiles" status="contentFileStatus">
        selectedFiles.push({
            fileName:'<s:property value="origFileName"/>',
            url:'<s:property value="path"/>',
            width:'<s:property value="resolutionWidth"/>',
            height:'<s:property value="resolutionHeight"/>',
            thumbPic:'<s:property value="thumbPic"/>',
            length:'<s:property value="duration"/>',
            lengthStr:'<s:property value="durationFormatted"/>'});
        </s:iterator>
    </script>
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

<!-- #section:basics/sidebar -->
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
                <a href="../man.jsp"> 管理首页</a>
            </li>
            <li class="active">发布视频</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-upload"></i>发布视频

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row page-content-main">
                <form role="form" class="form-horizontal"  action="/media/rp-m!saveMediaBaseInfo.action"
                      method="POST" enctype="multipart/form-data" id="movieForm" name="movieForm">

                    <h4>2.设置媒体属性</h4>
                    <div id="selectedFiles">
                        <s:if test="#session.contentFiles != null">
                            <s:iterator value="#session.contentFiles" status="contentFileStatus">
                                <s:if test="#contentFileStatus.first">
                                    <div class="col-xs-12 movie-title-box ">
                                        <div>文件：<b><s:property value="origFileName"/> </b></div>
                                        <hr>
                                        <div> 分辨率：<s:property value="resolutionWidth"/>x<s:property value="resolutionHeight"/> 长度：<s:property value="durationFormatted"/></div>
                                    </div>
                                </s:if>
                                <s:else>
                                    <s:if test="#contentFileStatus.index == 1"><div class="row  movie-title-boxs"></s:if>
                                    <div class="col-xs-12">
                                        <div class="col-xs-12 movie-title-box movie-title-box2">
                                            <div>文件：<b><s:property value="origFileName"/></b></div>
                                            <hr>
                                            <div> 分辨率：<s:property value="resolutionWidth"/>x<s:property value="resolutionHeight"/> 长度：<s:property value="durationFormatted"/></div>
                                        </div>
                                        <div class="space"></div>
                                    </div>
                                    <s:if test="#contentFileStatus.last"></div></s:if>
                                </s:else>
                            </s:iterator>
                            <s:if test="#session.contentFiles.size() > 1">
                                <div class="col-xs-12 no-padding"><a class="btn btn-btn" id="open-movie-title-boxs">查看更多文件</a></div>
                            </s:if>
                        </s:if>
                    </div>
                    <!--
                <div class="col-xs-2 no-padding" >
                    <div class="space"></div>
                    <div class="space"></div>

                    <select id="form-field-select-1" class="form-control">
                        <option value="">电影模板</option>
                        <option value="AL">影视</option>
                        <option value="AK">培训</option>
                        <option value="AZ">会议</option>
                           </select>
                 </div>
                 //-->
                    <div class="col-xs-12 no-padding">
                        <div  id="properties">
                            <hr>
                            <div class="form-group" id="movieTypeBox">
                                <label class="col-sm-3 control-label no-padding-right filed-need">类别</label>

                                <div class="col-sm-3">
                                    <select class="form-control " id="movie-type" name="obj.moduleId" onchange="movieTypeChanged(this.value)">
                                        <!--
                                        <option value="1">普通媒体</option>
                                        <option value="2">视频新闻</option>
                                        <option value="3">图片新闻</option>
                                        //-->
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right filed-need">标题</label>

                                <div class="col-sm-11">
                                    <input type="text" class="col-sm-5" id="movie-name" name="obj.name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right">主创</label>

                                <div class="col-sm-11">
                                    <input type="text" class="col-sm-5" id="movie-actor" name="obj.actors">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right filed-need">海报</label>

                                <div class="col-sm-11">
                                    <div class="col-sm-5 no-padding">
                                        <div class="file-thumb"><img src="../images/gallery/image-2.jpg" id="movie-poster">
                                        </div>
                                        <input type="file" id="id-input-file-poster" name="moviePoster"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right">大海报</label>

                                <div class="col-sm-11">
                                    <div class="col-sm-5 no-padding">
                                        <div class="file-thumb"><img src="../images/gallery/image-2.jpg"
                                                                     id="movie-big-poster"></div>
                                        <input type="file" id="id-input-file-big-poster" name="movieBigPoster"/>
                                        <h6 class="tips">用于首页推荐，建议大小800x640</h6>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right filed-need">时间</label>

                                <div class="col-sm-11">
                                    <input type="text" class="col-sm-5" id="movie-time" name="obj.property3" value="<%=StringUtils.date2string(new Date(),"yyyyMMdd")%>">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1 control-label no-padding-right filed-need">简介</label>

                                <div class="col-sm-11">
                                <textarea class="autosize-transition form-control" style="width: 41.666%"
                                          name="obj.intro"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="space"></div>
                        <button class="btn btn-blue" type="button" id="saveMovie">下一步</button>

                        <button class="btn btn-lightwhite " style="margin-left: 12px;" type="button" id="btn-back">返回</button>
                        <div class="space"></div>
                        <h6 style="color: #9aa7b2;">提交后系统自动转码，转码完成后提交内容管理员审核</h6>

                    </div>

                    <!-- /.row -->
                    <input type="hidden" id="moduleChannelID" name="moduleChannelId" value="-1"/>
                    <s:hidden name="obj.deviceId" id="deviceId"/>
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
<div class="footer">
    <div class="footer-inner">
        <!-- #section:basics/footer -->
        <div class="footer-content">
						<span class="bigger-120">
							© 2014 复全网网络，版权所有
						</span>


        </div>

        <!-- /section:basics/footer -->
    </div>
</div>
    <div id="modalDialog"></div>
<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='../scripts/jquery.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='../scripts/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='scripts/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="../scripts/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="../scripts/excanvas.min.js"></script>
<![endif]-->
<script src="../scripts/jquery-ui.custom.min.js"></script>
<script src="../scripts/jquery.ui.touch-punch.min.js"></script>
<script src="../scripts/chosen.jquery.min.js"></script>
<script src="../scripts/fuelux/fuelux.spinner.min.js"></script>
<%--
<script src="../scripts/date-time/bootstrap-timepicker.min.js"></script>
<script src="../scripts/date-time/moment.min.js"></script>
<script src="../scripts/date-time/daterangepicker.min.js"></script>
<script src="../scripts/date-time/bootstrap-datetimepicker.min.js"></script>
--%>
<script src="../scripts/bootstrap-colorpicker.min.js"></script>
<script src="../scripts/jquery.knob.min.js"></script>
<script src="../scripts/jquery.autosize.min.js"></script>
<script src="../scripts/jquery.inputlimiter.1.3.1.min.js"></script>
<script src="../scripts/jquery.maskedinput.min.js"></script>
<script src="../scripts/bootstrap-tag.min.js"></script>
<script src="../scripts/typeahead.jquery.min.js"></script>

<!-- ace scripts -->
<script src="../scripts/ace-elements.min.js"></script>
<script src="../scripts/ace.min.js"></script>
<script src="../scripts/redex_utils.js"></script>
<script type="text/javascript" src="../scripts/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="../scripts/bootstrap-datetimepicker.zh-CN.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    var currentModuleId = '<s:property value="obj.moduleId"/>';
    if(currentModuleId==''){
        currentModuleId = 503754003;
    }
    var currentModuleChannelID=-1;
    var properties=[];
    currentDeviceId = '<s:property value="obj.deviceId"/>';
    jQuery(function ($) {
        renderSelectedFiles(selectedFiles,'selectedFiles');
        $('textarea[class*=autosize]').autosize({append: "\n"});
        $('textarea.limited').inputlimiter({
            remText: '%n character%s remaining...',
            limitText: 'max allowed : %n.'
        });
        $("#movie-poster").hide();
        $("#movie-big-poster").hide();

        $("#btn-back").click(function(){
            window.location.href="../pub/pubFile.jsp?date="+new Date().getTime()+"&obj.deviceId="+currentDeviceId;
        });

        $("#movie-name").keypress(function() {
            $(this).closest("div.form-group").removeClass("has-error");
        });

        $("#movie-time").keypress(function() {
            $(this).closest("div.form-group").removeClass("has-error");
        });

        // init media type
        $.ajax({
            type: "POST",
            url: "/module/module!list.action",
            dataType: "text",
            //data: {recommendId:$("#channel").val(), keyIds:items},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                if (response.objs) {
                    modules = response['objs'];
                    for (var n = 0; n < modules.length; n++) {
                        var cn = modules[n];
                        var selected = false;
                        if(cn['id']==currentModuleId){
                            selected = true;
                            currentModuleChannelID = cn['channelId'];
                        }
                        $("#movie-type").append($("<option/>", {
                            value: cn.id,
                            text: cn.name,
                            selected:selected
                        }));
                    }
                }
            }
        });
        //初始化模版
        loadModule(currentModuleId);
        $("#open-movie-title-boxs").click(function() {
            var $a = $(".movie-title-boxs");
            $this = $(this);
            if ($a.is(':visible')) {
                $a.slideUp(300, function() {
                    $this.html("查看更多文件");
                });
            } else {
                $a.slideDown(300, function() {
                    $this.html("收起");
                });
            }
        });

        $("#saveMovie").click(function() {
            if (checkForm()) {
                $("#moduleChannelID").val(currentModuleChannelID);
                $("#movieForm").submit();
            }
        });

        function readURL(input, bigPoster) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    //$('#blah').attr('src', e.target.result);
                    if (bigPoster) {
                        $("#movie-big-poster").attr('src', e.target.result).show();
                    } else {
                        $("#movie-poster").attr('src', e.target.result).show();
                    }
                };
                reader.readAsDataURL(input.files[0]);
            }
        }

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
    });

    function checkFormLocal() {
        var r = true;
        var logs = '';
        for(var i= 0,l=properties.length;i<l;i++){
            var property = properties[i];
            if(property['isNull']==1){//如果允许为空，就不检查
                continue;
            }
            var id = 'field_'+i;
            var field = $("#"+id);
            var val = field.val();
            if(val == null||val==''){
                var hasError = true;
                if(property['dataType']==11){
                    //图片，检查是否有截图
                    val = $("#inputFileName_"+id).val();
                    if(val!=null&&val!=''){
                        hasError = false;
                    }
                }
                if(hasError){
                    logs+=(property['name']+"不能为空！");
                    field.closest("div.form-group").addClass("has-error");
                }
            }
        }
        if(logs!=''){
            alert(logs);
            return false;
        }
        return r;
    }
    function appendContentPropertyFieldsLocal(idx,params){
        var result = '';
        for(var p in params){
            if(params.hasOwnProperty(p)){
                result +='<input type="hidden" name="contentProperties['+idx+'].'+p+'" value="'+params[p]+'">\n';
            }
        }
        return result;
    }
    function dateDirectory() {
        if(openSnapWinTime==null){
            openSnapWinTime = new Date();
        }
        var temp = openSnapWinTime;
        var directory = temp.getFullYear() + "/";
        var month = temp.getMonth()+1;
        if(month < 10) {
            month = "0"+month;
        }
        var date = temp.getDate();
        if(date<10){
            date = "0"+date;
        }
        directory = directory + month + "/" + date +"/";
        return directory;
    }

    var picNo = 1;
    function getNewThumbPicFullPath(snapTime,picTypeId){
        picNo ++;
        var now =openSnapWinTime;
        var contentId= $("#obj_id").val();
        if(contentId==null||contentId=="null"||typeof(contentId)=="undefined"){
            contentId=0;
        }
        var result =  "/upload/snap/"+dateDirectory()+""
                +"c"+contentId+"_"+picNo+"_";
        if(snapTime){
            result += snapTime+"_";
        }
        if(picTypeId){
            result+=picTypeId+"_";
        }
        result += now.getHours()+"_"+now.getMinutes()+"_"+now.getSeconds()+"_"+Math.round(Math.random() * 100000) + ".jpg";
        return result;
    }
    function doSaveToPoster(){
        var idx = $("#snapIdx").val();
        var picUrl = $("#thumbPic_"+idx).val();
        var imageObj = $("#clipSnapImage_"+idx);
        var pictureIdx = $("#posters").val();
        var posterObj = pictureProperties[pictureIdx];
        if(confirm("您确认要将此截图保存为"+posterObj['name']+"吗？")){
            var id = posterObj['id'];
            var imageInfoDiv = $("#imageInfoDiv_"+id);
            imageInfoDiv.removeClass('hidden');
            imageInfoDiv.addClass('showing');

            var posterImageObj = $("#"+posterObj['imageId']);
            $("#inputFileName_"+id).val(picUrl);
            var orgObj = document.getElementById("org_"+id);
            if(orgObj!=null){
                orgObj.value = picUrl;
            }
            posterImageObj.show();
            posterImageObj.attr("src",picUrl);
            posterImageObj.attr("width",imageObj.attr("width"));
            posterImageObj.attr("height",imageObj.attr("height"));
            $("#saveToPosterWindowsId").modal('hide');
        }
    }
    function closeSavePosterDialog(){
        $("#saveToPosterWindowsId").modal('hide');
    }
    function saveToPoster(idx){
        var posters = [];
        var i = 0,l=pictureProperties.length;
        for(;i<l;i++){
            var prop = pictureProperties[i];
            posters.push({boxLabel:prop['name'],value:i})
        }
        showDialog({
            title:'另存为海报',id:'saveToPosterWindowsId',renderTo:'modalDialog',
            items:[
                {fieldLabel:'选择还报',id:'posters',type:'select',items:posters}
            ],
            buttons:[
                {
                    id:'doSaveToPosterBtn',
                    cls:'btn-blue',
                    text:'确定',
                    style:'margin-left:100px;',
                    handler:doSaveToPoster
                },
                {
                    text:'放弃',id:'closeDialogButton',style:'margin-left:100px;',handler:closeSavePosterDialog
                }
            ],
            hiddenItems:[
                {id:'snapIdx',value:idx}
            ]
        });
    }
    function imageUrlMayChanged(idx){
        var imageUrl = $("#thumbPic_"+idx);
        var imageObj = $("#clipSnapImage_"+idx);
        $.ajax({
            url:'snapUrlChanged.jsp?idx='+idx,
            data:{snapUrl:imageUrl},
            success:function(){

            }
        });
    }
    var contentName = '';
    function renderSelectedFiles(files,renderTo){
        if(files==null||typeof(files)=='undefined'){
            files = selectedFiles;
        }
        if(renderTo==null||renderTo==''||typeof(renderTo)=='undefined'){
            renderTo = 'selectedFiles';
        }
        var i= 0,l=files.length;
        var html = '';
        var deviceId = $("#deviceId").val();
        var openSnap = true;
        var imageWidth=320;
        for(;i<l;i++){
            var file = files[i];
            var cls = i==0?'':' movie-title-box2';
            if(contentName == ''||contentName==null){
                contentName = file['fileName'];
                if(contentName!=null){
                    var p = contentName.lastIndexOf(".");
                    if(p>0){
                        contentName = contentName.substring(0,p);
                    }
                }
            }
            if(i==1){
                html +='<div class="row  movie-title-boxs">\n';
            }
            if(i>=1){
                html +='<div class="col-xs-12">\n';
            }
            html +='<div class="col-xs-12 movie-title-box'+cls+'">\n'+
            '<div>';
            var clipWidth = parseInt(file['width']);
            var clipHeight = parseInt(file['height']);

            if(openSnap){
                var imageHeight=180;
                var snapTime = 360;
                var length = parseInt(file['length']);
                if(length>360){
                    snapTime = 360;
                }else{
                    snapTime = parseInt(length/2);
                }
                if(clipWidth!=null&&clipHeight!=null){
                    imageHeight = parseInt(imageWidth * clipHeight / clipWidth);
                }
                var thumbPic = file['thumbPic'];
                var picUrl;
                if(thumbPic==null||thumbPic=='null'||thumbPic==''||thumbPic == 'undefined'||typeof(thumbPic)=='undefined'){
                    thumbPic =getNewThumbPicFullPath(snapTime,imageWidth+'x'+imageHeight);
                    picUrl = '<%=request.getContextPath()%>/interface/snapMedia.jsp?startTime='+snapTime+'&deviceId='+deviceId+'&mediaUrl=' +
                    encodeURI(encodeURI(encodeURI(file['url'])))+'&thumbPicUrl='+thumbPic+'&width='+imageWidth+'&height='+imageHeight+"&idx="+i;
                }else{
                    picUrl = thumbPic;
                }

                html +='<img onReadyStateChange="imageUrlMayChanged('+i+')" id="clipSnapImage_'+i+'" src="'+picUrl+'" width="'+imageWidth+'" height="'+imageHeight+'"><br/>';
                if(showSnapButton){
                    html  +='<span style="margin-left:10px;" class="btn btn-red" onclick="openSnapWin('+i+')">重新视频截图</span>';
                }
                html+='<span style="margin-left:10px;" class="btn btn-blue" onclick="saveToPoster('+i+')">另存为海报....</span><br/>文件：<b>'+file['fileName']+'</b><br/>\n';
                html +='<input type="hidden" name="contentFiles['+i+'].thumbPic" id="thumbPic_'+i+'" value="'+thumbPic+'">';
            }else{
                html +='文件：<b>'+file['fileName']+'</b></div><hr><div>\n';
            }
            var lengthStr = file['lengthStr'];
            html +='分辨率：'+file['width']+'x'+file['height']+' 长度：'+lengthStr+'(' +length+
            '秒)'+'</div>\n'+
                '</div>\n'+
                '<div class="space"></div>\n';
            if(i>=1){
                html +='</div>\n';
            }
            if(i==l-1){
                html +='</div>\n';
            }
        }
        if(l>1){
            html +='<div class="col-xs-12 no-padding"><a class="btn btn-btn" id="open-movie-title-boxs">查看更多文件</a></div>\n';
        }
        $("#"+renderTo).html(html);
    }
    var openSnapWinTime = new Date();
    function doSnap(){
//        var idx = $("#snapIdx").val();
        var imageSize = $("#snapImageSize").val();
        var snapTime = $("#snapTime").val();
        var snapUrlObj = $("#snap_url");
        var picUrl = snapUrlObj.val();
        if(picUrl==null||picUrl==''){
            picUrl = getNewThumbPicFullPath(snapTime,imageSize);
            snapUrlObj.val(picUrl);
        }
        var imageWidth = -1;
        var imageHeight = -1;
        if(imageSize!=''&&imageSize!=null){
            var p = imageSize.indexOf("x");
            if(p>0){
                imageWidth = imageSize.substring(0,p);
                imageHeight = imageSize.substring(p+1);
            }
        }
        var deviceId = $("#deviceId").val();
        var url = $("#snapMediaUrl").val();
        var maxImageWidth = 380;
        var snapImageObj = $("#snapImage");

        snapImageObj.attr('src','<%=request.getContextPath()%>/interface/snapMedia.jsp?startTime='+snapTime+'&deviceId='+deviceId+'&mediaUrl=' +
        encodeURI(encodeURI(url))+'&thumbPicUrl='+picUrl+'&width='+imageWidth+'&height='+imageHeight);
        if(imageWidth>maxImageWidth||imageWidth<=0){
            if(imageHeight<0){
                imageHeight=180;
                imageWidth=320;
            }
            imageHeight = imageHeight * maxImageWidth / imageWidth;
            imageWidth = maxImageWidth;
        }
        snapImageObj.attr('height',imageHeight);
        snapImageObj.attr('width',imageWidth);
    }
    function getImgNaturalDimensions(img) {
        var nWidth, nHeight;
        if (img.naturalWidth) { // 现代浏览器
            nWidth = img.naturalWidth;
            nHeight = img.naturalHeight;
        } else { // IE6/7/8
            var imgae = new Image();
            image.src = img.src;
            image.onload = function() {
                callback(image.width, image.height)
            }
        }
        return {width:nWidth,height:nHeight};
    }
    function toPoster(){

    }
    function snapSuccess(){
        var idx = $("#snapIdx").val();
        var url = $("#snap_url").val();
        if(url!=''){
            $("#thumbPic_"+idx).val(url);
            var imageObj = $("#clipSnapImage_"+idx);
            var snapImageObj = $("#snapImage");
            imageObj.attr("src",url);
            imageObj.attr("width",snapImageObj.attr("width"));
            imageObj.attr("height",snapImageObj.attr("height"));
        }else{
            alert('尚未截图');
        }
        $("#snapWindowsId").modal('hide');
    }
    function openSnapWin(idx){
        openSnapWinTime = new Date();
        var snapUrl = $('#thumbPic_'+idx).val();
        var mediaUrl =selectedFiles[idx]['url'];
        showDialog({
            title:'截图对话框',id:'snapWindowsId',renderTo:'modalDialog',
            items:[
                {fieldLabel:'大小',id:'snapImageSize',type:'select',
                    items:[
                        {value:'320x240',boxLabel:'320x240'},
                        {value:'320x180',boxLabel:'320x180'},
                        {value:'640x360',boxLabel:'640x360'},
                        {value:'640x480',boxLabel:'640x480'},
                        {value:'1280x720',boxLabel:'1280x720'},
                        {value:'-1x-1',boxLabel:'原始大小'}
                    ]
                },
                {fieldLabel:'时间（秒）',id:'snapTime',value:'100'},
                {fieldLabel:'图片',id:'snapImage',type:'image',src:snapUrl}
            ],
            hiddenItems:[
                {id:'snapIdx',value:idx},{id:'snap_url',value:''},{id:'snapMediaUrl',value:mediaUrl}
            ],

            buttons:[
                {id:'doSnapBtn',text:'截图',cls:'btn-blue',handler:doSnap},
                {
                    id:'snapSuccessBtn',
                    text:'确定',style:'margin-left:100px;',
                    handler:snapSuccess
                },
                {
                    text:'放弃',id:'closeButton',style:'margin-left:100px;',handler:function(){
                    $("#snapWindowsId").modal('hide');
                }
                }
            ]
        });
        var imageObj = $("#clipSnapImage_"+idx);
        var snapImageObj = $("#snapImage");
        snapImageObj.attr("width",imageObj.attr("width"));
        snapImageObj.attr("height",imageObj.attr("height"));
    }
    var pictureProperties = [];
    function renderProperties(){
        var html = '<hr>';
        var idx = 0,fileIdx=0;
        var frm =  document.getElementById("movieForm");
        for(var i= 0,l=properties.length;i<l;i++){
            var property = properties[i];
            var fieldNeed = ' filed-need';
            if(property['isNull']==1){
                fieldNeed = '';
            }
            var label = property['name'];
            var comment = '';
            var p=label.indexOf("[");
            if(p>0){
                comment = label;
                label = label.substring(0,p);
                p = comment.indexOf("]");
                if(p>0){
                    comment = comment.substring(p+1);
                }else{
                    comment = '';
                }
            }
            var fieldHtml = '<div class="form-group">'+
                    '<label class="col-sm-2 control-label no-padding-right'+fieldNeed+'">' +label+'</label>';
            var dataType = property['dataType'];
            var propertyId = property['id'];
            var columnName = property['columnName'];
            var dataField = 'obj.'+ columnName;
            var isMain = property['isMain'];
            if(isMain!=1){
                dataField = 'contentProperties['+idx+'].stringValue';
            }
            var id='field_'+i;
            var fieldObject = null;
            var value = null;
            if(frm!=null){
                fieldObject = frm.elements[dataField];
                if(fieldObject!=null){
                    value = fieldObject.value;
                }
            }
            if(value==null){
                value = '';
            }
            if(dataType==1||dataType==2||dataType==3) {
                fieldHtml += '<div class="col-sm-10">';
                var type = 'text';
                if (property['isMultiLine'] == 1) {
                    fieldHtml += '<textarea style="height:150px;" class="col-sm-5" id="' + id + '" name="' + dataField + '">' +
                    value + '</textarea>';
                } else {
                    fieldHtml += '<input type="' + type + '" class="col-sm-5" id="' + id + '" name="' + dataField + '" value="' + value + '">\n';
                }
                if (isMain != 1) {
                    fieldHtml += appendContentPropertyFieldsLocal(idx, {id: -1, propertyId: propertyId, intValue: idx});
                }
            }else if(dataType==4){//日期
/*
                fieldHtml+='<div class="input-group date form_date datepickerForInput col-sm-11" data-date="" data-date-format="" data-link-field="' +id+
                '" data-link-format="yyyymmdd">'+
                            '<input class="form-control" size="16" type="text" value="" readonly>'+
                            '<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>'+
                            '<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>'+
                            '</div>'+
                            '<input type="hidden" id="' +id+'" value="" /><br/>';
*/
                fieldHtml += '<div class="col-sm-10">';
                fieldHtml += '<input type="text" class="col-sm-5 datepickerForInput" id="' + id + '" name="' + dataField + '" value="' + value + '">\n';
            }else if(dataType==11){//图片
                fieldHtml+='<div class="col-sm-5">';
                fieldHtml+='<div class="file-thumb"><img style="display:none;width:auto;" src="../images/gallery/image-2.jpg"'+
                ' class="previewImage" id="previewImage_' +id+'"></div>'+
                '<input type="file" class="col-sm-5 inputFileClasses" id="'+id+'" name="files['+fileIdx+']"/>';
                if(comment!=''){
                    fieldHtml +='<h6 class="tips">'+comment+'</h6>';
                }
                fieldHtml +='<input type="hidden" id="inputFileName_'+id+'" name="filesFileName['+fileIdx+']"/>';
                fieldHtml +='<input type="hidden" name="fileProperties['+fileIdx+']" value="' +propertyId+'">\n';
                pictureProperties.push({fieldId:'inputFileName_'+id,imageId:'previewImage_'+id,name:label,propertyId:propertyId,id:id,idx:i});
                fileIdx++;
                //'<h6 class="tips">用于首页推荐，建议大小800x640</h6>';
            }else{
                continue;
            }
            if(1!=isMain){
                idx++;
            }
            fieldHtml+='</div></div>';
            html += fieldHtml;
        }
        $("#properties").html(html);

        var datePicker = $(".datepickerForInput");
        datePicker.datetimepicker({
            language: 'zh-CN',
            format:'yyyymmdd',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            pickTime: false,
//            startView: 2,
            minView: 2,
            forceParse:false}
        );
        datePicker.datetimepicker("update",new Date());
        $(".inputFileClasses").ace_file_input({
            no_file:'未选择文件 ...',
            style:'width:200px;',
            btn_choose:'选择',
            btn_change:'选择',
            droppable:false,
            whitelist:'png|jpg|jpeg',
            before_change:function(files, dropped) {
                if (files.length > 0) {
                    $(this).closest("div.form-group").removeClass("has-error");
                    var file = files[0];
                    if (typeof file === 'string') { // 不预览海报
                    } else if ('File' in window && file instanceof window.File) {
                        var imageObj = $(this).closest("img.previewImage");
                        var id=this.id;
                        if(id!=null&&!(typeof(id)=='undefined')){
                            id = "previewImage_"+id;
                            imageObj = $("#"+id);
                        }
                        $("#inputFileName_"+this.id).val(this.value);
                        readImageFromUploadFile(this, imageObj);
                    }
                }
                return true;
            }
        });
    }
    function readImageFromUploadFile(input,image){
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            //alert($(image).attr("id"));
            reader.onload = function (e) {
                image.attr('src', e.target.result).show();
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
    var modules=[];
    function onMovieTypeChanged(moduleId){
        if(currentModuleId!=moduleId){
            if(confirm("您的修改会可能导致部分数据丢失！您确认要修改媒体的类型吗？")){
                loadModule(moduleId);
                for(var i= 0,l=modules.length;i<l;i++){
                    var module = modules[i];
                    if(module['id']==moduleId){
                        currentModuleChannelID = module['channelId'];
                    }
                }
            }else{
                $("#movie-type").val(currentModuleId);
            }
        }
    }
    function loadModule(moduleId){
        currentModuleId = moduleId;
        getContent(-1,currentModuleId,currentDeviceId,{baseInfoBoxId:'properties',mergeBaseAndPic:true,
            clipInfoBoxId:'hidden','obj.name':contentName,'date':'<%=StringUtils.date2string(new Date(),"yyyyMMdd")%>'});
    }
    function loadModuleLocal(moduleId){
        currentModuleId = moduleId;
        $.ajax({
            url:'/module/property!getPropertiesOfModule.action',
            type:'post',
            dataType: "text",
            data:{"obj.moduleId":moduleId,"obj.status":1},
            success:function(data){
                var response = eval("(function(){return " + data + ";})()");
                properties = response['objs'];
                if (properties) {
                    renderProperties();
                }
            }
        });
    }
</script>
</div>
</body>
</html>
