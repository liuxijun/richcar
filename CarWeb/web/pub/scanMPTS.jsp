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
    <title>组播扫描</title>
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
        .mptsLive{
            width:90%;
        }
        .mptsLiveList{

        }
        .mptsLiveList li{
            cursor:pointer;
            margin-left: 10px;
            float:left;
            padding: 2px;
            width:450px;
            height:141px;
            border:solid blue 1px;
        }
        .mptsLiveList img{
            float:left;
            width:240px;
            height:135px;
            border:solid green 1px;
        }
        .mptsLiveList span{
            margin-left: 2px;
            float:left;
            width:200px;
            height:20px;
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
                <i class="ace-icon fa fa-user"></i>${functionName}
            </h1>
        </div>
        <!-- /.page-header -->
        <div class="page-content">

            <div class="page-content-area">
                <div class="row page-content-main">
                    <form role="form" class="form-horizontal">
                        <div class="input-group pull-right search-group" style="width:640px;height: 30px; margin: 9px;float:left;">
                            <div style="width:180px;float:left;font-weight: 30px;font-size:22px;">组播地址和端口：</div>
                            <div style="width: 400px;height: 30px;float: left;">
                                <input type="text" id="search_addr" placeholder="例如：225.0.0.1:14500" class="form-control search-query" >
                            </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                        </div>
                        <div class="col-xs-12 no-padding movie-info">
                            <div class="mptsLive">
                                <ul class="mptsLiveList" id="scanResult">

                                </ul>
                            </div>
                            <div class="space-6"></div>
                            <div class="row">
                                <div class="col-md-2"><a class="btn btn-green btn-big" onclick="sourceScan.selectSources()">添加已选</a></div>
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
            sourceScan.initFolders();
            $("#btn_search").click(function(){
                sourceScan.scanMPTS(1);
            });
            sourceScan.goToPage(1);
        });

        var sourceScan={
            limit:10,
            currentPage:1,
            programs:[],
            renderSources:function(jsonData){
                sourceScan.programs = jsonData['programs'];
                var sources = sourceScan.programs;
                var result = '';
                var addr = jsonData['addr'];
                if(addr==null){
                    addr = $("#search_addr").val();
                }
                if(sources!=null){
                    var i= 0,l=sources.length;
                    for(;i<l;i++){
                        var source = sources[i];
                        var name = source['name'];
                        var startTime = source['startTime'];
                        var picName = null;
                        var pid=source['pid'];
                        if(pid!=null&&pid!=''&&pid!='null'){
                            picName = '/upload/snap/mpts/'+addr.replace(/:/,'_')+'-'+pid+".jpg";
                            source['imageSrc']=picName;
                        }
                        source['addr']=addr;
                        if(startTime==null||startTime == 'null'||startTime==''){
                            startTime = '';
                        }else{
                            startTime = '<span>时间：'+parseInt(startTime)+"</span>";
                        }
                        result +='<li onclick="sourceScan.selectProgram('+i+')"><img src="../interface/snapLive.jsp?addr='+
                                addr+'&programId='+pid+'&picFileName='+picName+'&fromClient=true" alt="' +name+'">'+
                                '<span>频道：' + name+'</span>' +
                                '<span>编号：' + pid+'</span>' +
                                startTime;
                        var streams = source['streams'];
                        if(streams!=null){
                            for(var s= 0,sl=streams.length;s<sl;s++){
                                var stream = streams[s];
                                var type = stream['type'];
                                var typeName='';
                                if('audio'==type){
                                    typeName = '音频';
                                }else if('video'==type){
                                    typeName= '视频';
                                }else{
                                    typeName = type;
                                }
                                result+='<span>'+typeName+'：'+stream['name']+'</span>';
                            }
                        }
                        result+='</li>';
                    }
                }
                $('#scanResult').html(result);
            },
            currentProgramIdx:-1,
            selectProgram:function(idx){
                sourceScan.currentProgramIdx=idx;
                var program = sourceScan.programs[idx];
                var imageSrc = program['imageSrc'];
                showDialog({id:'sourceDetailModal',renderTo:'modalDialog',title:'组播源详情',
                    items:[
                        {fieldLabel:'节目截图：',id:'programPic',type:'image',src:imageSrc,width:360,height:240},
                        {fieldLabel:'组播名称：',id:'programName',allowBlank:false,value:program['name']},
                        {fieldLabel:'地址端口：',id:'programAddr',value:program['addr']},
                        {fieldLabel:'节目编号：',id:'programPID',value:program['pid']}
                    ],
                    hiddenItems:[{id:'sourceId'},{id:'sourcePermissionId'}],
                    buttons:[
                        {text:'保存',cls:'btn-blue',handler:sourceScan.saveSource},
                        {text:'关闭',style:'margin-left:100px;',handler:function(){
                            $("#sourceDetailModal").modal('hide');
                        }}]});
            },
            scanMPTS:function(){
                var addr = $("#search_addr").val();
                if(addr==''){
                    alert("请输入组播地址！");
                    return;
                }
                $.ajax({
                    url:'../interface/scanLive.jsp',//source!listFunctionSources.action
                    data:{addr:addr,fromClient:"true"},
                    method:'post',
                    dataType:'json',
                    success:function(jsonData){
                        sourceScan.renderSources(jsonData);
                    }
                });
            },
            listSources:function(){

            },
            saveSource:function(){
                if(!confirm("您确认要保存这个组播作为源以供发布系统进行管理吗？")){
                    return;
                }
                var program = sourceScan.programs[sourceScan.currentProgramIdx];
                var addr = $("#programAddr").val();
                var pid = $("#programPID").val();
                var name = $("#programName").val();
                $.ajax({
                    url: '../interface/source.jsp',
                    method:'post',
                    data:{
                        command:'save',
                        addr:addr,
                        name:name,
                        pid:pid
                    },
                    dataType: 'json',
                    success: function (jsonData) {
                        alert("已经保存！");
                    },
                    error:function(p0,p1,p2){
                        alert("保存发生错误！");
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
            showSourceDetail:function(id){
                if(id>0){
                    $.ajax({
                        url:'/security/source!view.action?keyId='+id,
                        dataType:'json',
                        success:function(jsonData){
                            var data = jsonData['data'];
                            if(data!=null){
                                $("#sourceName").val(data['obj.name']);
                                $("#sourceUrl").val(data['obj.url']);
                                $("#sourceId").val(data['obj.id']);
                                $("#sourcePermissionStr").val(data['obj.permissionStr']);
                                $("#sourcePermissionId").val(data['obj.permissionId']);
                                $("#sourceStyle").val(data['obj.style']);
                                var parentId=1;
                                if(id!=-1){
                                    parentId = data['obj.parentId'];
                                }
                                //$("#sourceParentIdContain").html();
                                sourceScan.createSelect('sourceParentId',sourceScan.sourceFolders,parentId);
                                sourceScan.createSelect('sourceStatus',sourceScan.sourceStatus,data['obj.status']);
                                //$("#sourceStatusContain").html();
                                //$("#sourceStatus").val(data['obj.status']);
                            }
                        }
                    });
                }else{
                    sourceScan.createSelect('sourceParentId',sourceScan.sourceFolders,1);
                    sourceScan.createSelect('sourceStatus',sourceScan.sourceStatus,1);
                }
            },
            goToPage:function(pageNo){
                sourceScan.currentPage = pageNo;
                sourceScan.listSources();
            },
            sourceTypes:[
                {id:'ts',name:'TS'},
                {id:'mpts',name:'组合流MPTS'},
                {id:'hls',name:'hls'},
                {id:'rtsp',name:'RTSP'},
                {id:'wmv',name:'WMV'}
            ],
            sourceStatus:[
                {id:1,name:'正常使用'},
                {id:11,name:'测试，仅用于测试开发环境'},
                {id:10,name:'关闭'}
            ],
            sourceFolders:[{id:1,name:'发布视频'},
                {id:5,name:'视频管理'},
                {id:11,name:'系统管理'},
                {id:20,name:'超户专属'},
                {id:0,name:'功能分类'}
            ],
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
            getStatusName:function(id){
                return sourceScan.getTextOfArray(id,sourceScan.sourceStatus,'id','name');
            },
            getTypeName:function(id){
                return sourceScan.getTextOfArray(id,sourceScan.sourceTypes,'id','name');
            },
            getProgramId:function(source){
                var p0 = source.lastIndexOf("/");
                if(p0>0){
                    var programId= source.substring(p0+1);
                    p0 = programId.indexOf("@");
                    if(p0>0){
                        programId = programId.substring(0,p0)
                    }
                    return programId;
                }
                return '';
            },
            initFolders:function(){

            }
        };

    </script>

</body>
</html>
