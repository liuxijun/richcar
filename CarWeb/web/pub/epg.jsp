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
  <title>EPG列表</title>
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
        <i class="ace-icon fa fa-user"></i>${functionName}
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">


        <div class="row page-content-main">

          <form role="form" class="form-horizontal">
            <div class="input-group pull-right search-group" style="width:350px;height: 30px; margin: 9px;">
              <div style="width: 100px;height: 30px;float: left;">
                <input type="text" id="search_name" placeholder="功能名称" class="form-control search-query" >
              </div>
              <div style="width: 100px;height: 30px;float: left;">
                <select id="search_parentId" class="form-control search-query" >
                  <option value="" onclick='epgList.goToPage(1)'>分类</option>
                  <option value="1" onclick='epgList.goToPage(1)'>发布视频</option>
                  <option value="5" onclick='epgList.goToPage(1)'>视频管理</option>
                  <option value="11" onclick='epgList.goToPage(1)'>系统管理</option>
                  <option value="20" onclick='epgList.goToPage(1)'>超户专属</option>
                  <option value="0" onclick='epgList.goToPage(1)'>功能分类</option>
                </select>
              </div>
              <div style="width: 100px;height: 30px;float: left;">
                <select id="search_status" class="form-control search-query" >
                  <option value="" onclick='epgList.goToPage(1)'>状态</option>
                  <option value="1" onclick='epgList.goToPage(1)'>正常</option>
                  <option value="11" onclick='epgList.goToPage(1)'>测试，仅用于测试开发环境</option>
                  <option value="10" onclick='epgList.goToPage(1)'>关闭</option>
                </select>
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
                    <th width="50%"><a href="#" onclick='epgList.order_by("epgName")'>功能名</a></th>
                    <th class="center"><a href="#" onclick='epgList.order_by("parentId")'>功能分类</a></th>
                    <th class="center"><a href="#" onclick='epgList.order_by("status")'>当前状态</a></th>
                    <th class="center">操作</th>
                  </tr>
                  </thead>
                  <tbody id="epgsContain">

                  </tbody>
                </table>

              </div>
              <div class="space-6"></div>
              <div class="row">
                <div class="col-md-2"><a class="btn btn-green btn-big" onclick="epgList.showMenuDetail(-1)">新增功能</a></div>
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
    <div class="modal fade modal-film" id="epgDetailModal" tabindex="-1" role="dialog"
         aria-labelledby="LogDetailModal" aria-hidden="true">

      <div class="modal-dialog"
           style="position: absolute; left: 50%; margin-left: -280px; top: 50%; margin-top: -250px; width: 580px;">

        <div class="modal-content">

          <div class="modal-header">

            <button type="button" class="close" data-dismiss="model"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>

            <h4 class="model-title" id="epgDetailModalLabel">功能详情</h4>

          </div>
          <div class="model-body">

            <div class="row">

              <div class="col-xs-12 ">

                <form class="form-horizontal" id="formId_4">

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right filed-need">功能名称：</label>

                    <div class="col-sm-9">

                      <input type="text" id="epgName" name="epgName" class="col-sm-12" value="">
                    </div>

                  </div>

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right">起始连接：</label>

                    <div class="col-sm-9">

                      <input type="text" id="epgUrl" name="epgUrl" class="col-sm-12" value="">
                    </div>

                  </div>

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right">辅助连接：</label>

                    <div class="col-sm-9">

                      <input type="text" id="epgPermissionStr" name="epgPermissionStr"
                             class="col-sm-12" value=""></div>

                  </div>

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right">显示风格：</label>

                    <div class="col-sm-9">

                      <input type="text" id="epgStyle" name="epgStyle" class="col-sm-12"
                             value=""></div>

                  </div>

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right filed-need">功能分类：</label>

                    <div class="col-sm-9">

                      <select class="col-sm-12" id="epgParentId" name="epgParentId" value="">

                      </select>

                    </div>

                  </div>

                  <div class="form-group">

                    <label class="col-sm-3 control-label no-padding-right filed-need">功能状态：</label>

                    <div class="col-sm-9">

                      <select class="col-sm-12" id="epgStatus" name="epgStatus" value="">

                      </select>

                    </div>

                  </div>

                  <div class="space"></div>

                  <span class="btn btn-blue" id="buttonId_5">确定</span>

                  <span class="btn" id="buttonId_6" style="margin-left:100px;">关闭</span>

                  <input type="hidden" name="epgId" id="epgId"/>

                  <input type="hidden" name="epgPermissionId" id="epgPermissionId"/>

                </form>

              </div>

            </div>

            <div class="model-footer" style="display: none;">

              <button type="button" class="btn btn-default" data-dismiss="model">Close</button>

              <button type="button" class="btn btn-primary">Save changes</button>

            </div>

          </div>

        </div>

      </div>
    </div>
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
      epgList.initFolders();
      $("#btn_search").click(function(){
        epgList.goToPage(1);
      });
      epgList.goToPage(1);
    });
    var epgList={
      limit:10,
      currentPage:1,

      renderEpgs:function(jsonData){
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /epgList.limit),
                epgList.currentPage,"epgList.goToPage", jsonData['totalCount']);
        var epgs = jsonData['objs'];
        var result = '';
        if(epgs!=null){
          var i= 0,l=epgs.length;
          for(;i<l;i++){
            var epg = epgs[i];
            result +=
                    '<tr>' +
                    '<td>' + epg.name+'</td>' +
                    '<td class="center">' + epgList.getFolderName(epg['parentId'])+'</td>' +
                    '<td class="center">' + epgList.getStatusName(epg['status'])+'</td>' +
                    '<td class="center"><a class="btn btn-grey btn-xs"  onclick="epgList.showMenuDetail(' +epg.id+','+epg.type+
                    ');return false;">'+
                    '          <i class="ace-icon fa fa-edit bigger-110 icon-only"></i>'+
                    '  </a><a class="btn btn-grey btn-xs" onclick="epgList.deleteMenu(' +epg.id+
                    ','+epg.status+');return false;">'+
                    '          <i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                    '  </a></td>'+
                    '</tr>';
          }
        }
        $('#epgsContain').html(result);
      },
      order_by:function (v) {
        if (v == epgList.order) {
          epgList.dir = ( epgList.dir == "asc") ? "desc" : "asc";
        } else {
          epgList.order = v;
          epgList.dir = "desc";
        }
        epgList.listEpgs();
      },
      listEpgs:function(){
        var _order = "o1.id";
        switch (epgList.order) {
          case "epgName":
            _order = "o1.name";
            break;
          case "epgStatus":
            _order = "o1.status";
            break;
          case "epgParentId":
            _order = "o1.parentId";
            break;
        }
        $.ajax({
          url:'../live/epg!list.action',//epg!listFunctionEpgs.action
          data:({"obj.name":$("#search_name").val(),
            "obj.parentId":$("#search_parentId").val(),
            "obj.status":$("#search_status").val(),
            "pageBean.pageSize":epgList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order, "pageBean.orderDir":epgList.dir,
            limit:epgList.limit, start:(epgList.currentPage - 1) * epgList.limit }),
          method:'post',
          dataType:'json',

          success:function(jsonData){
            epgList.renderEpgs(jsonData);
          }
        });
      },
      currentMenuId:-1,
      currentMenuType:-1,
      deleteMenu:function(id,status){
        var epgType = parseInt(status);
        if(confirm("您确认要删除这个菜单吗？\r\n所有的菜单一旦删除，就再也找不回来了！如果您只是想隐藏，可以选择将其关闭！\r\n" +
                        "再次警示，删除了，就没有了！可能会导致系统不可用！\r\n虽然您有超户权利，但是想恢复正常还是要找我这个开发人员！\r\n您真的确认要删除么？")&&
                confirm("基于系统安全，我们再次郑重提示：\r\n除非您知道这样做的后果，否则请不要选择[确定/OK],请选择[取消/Cancel]")){

          $.ajax({
            url:'/live/epg!delete.action',
            data:{keyId:id},
            dataType:'json',
            success:function(jsonData){
              alert("删除完毕！");
              epgList.goToPage(1);
            }
          });
        }
      },
      saveMenu:function(){
        var epgId = $("#epgId").val();//epgList.currentMenuId;//
        if(epgId==null||typeof(epgId)=='undefined'||epgId=='-1'||epgId=='0'||epgId==0){
          epgId=-1;
        }
        if(epgId!=-1&&!confirm("您确认您要保存吗？不正确的功能修改，会导致系统不可用！\r\n" +
                        "如果功能修改错误，会导致管理员访问后台时功能的异常和错误！\r\n" +
                        "如果您知道自己在做什么，请选择[确定]，否则请选择[取消]！")){
          return;
        }
        var epgName=$("#epgName").val().trim();
        var epgUrl = $("#epgUrl").val();
        var epgPermissionStr = $("#epgPermissionStr").val();
        var epgParentId = $("#epgParentId").val();
        var epgStyle = $("#epgStyle").val();
        var epgStatus = $("#epgStatus").val();
        var epgPermissionId = $("#epgPermissionId").val();
        if(epgName==''){
          alert("请输入功能名称");
          return false;
        }
        if(epgUrl==''){
          alert("请输入访问URL");
          return;
        }
        $.ajax({
          url:'/live/epg!save.action?keyId='+epgId,
          method:'post',
          data:{
            'obj.id':epgId,
            'obj.name':epgName,
            'obj.url':epgUrl,
            'obj.status':epgStatus,
            'obj.style':epgStyle,
            'obj.permissionStr':epgPermissionStr,
            'obj.permissionId':epgPermissionId,
            'obj.parentId':epgParentId
          },
          dataType:'json',
          success:function(jsonData){
            alert("已经保存！");
            epgList.goToPage(1);
            $("#epgDetailModal").modal('hide');
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
      showMenuDetail:function(id){
        epgList.currentMenuId=id;
        showDialog({id:'epgDetailModal',renderTo:'modalDialog',title:'功能详情',
          items:[
            {fieldLabel:'功能名称：',id:'epgName',allowBlank:false},
            {fieldLabel:'起始连接：',id:'epgUrl'},
            {fieldLabel:'辅助连接：',id:'epgPermissionStr'},
            {fieldLabel:'显示风格：',id:'epgStyle'},
            {fieldLabel:'功能分类：',id:'epgParentId',type:'select',allowBlank:false},
            {fieldLabel:'功能状态：',id:'epgStatus',type:'select',allowBlank:false}
          ],
          hiddenItems:[{id:'epgId'},{id:'epgPermissionId'}],
          buttons:[
            {text:'确定',cls:'btn-blue',handler:epgList.saveMenu},
            {text:'关闭',style:'margin-left:100px;',handler:function(){
              $("#epgDetailModal").modal('hide');
            }}]});
        if(id>0){
          $.ajax({
            url:'/live/epg!view.action?keyId='+id,
            dataType:'json',
            success:function(jsonData){
              var data = jsonData['data'];
              if(data!=null){
                $("#epgName").val(data['obj.name']);
                $("#epgUrl").val(data['obj.url']);
                $("#epgId").val(data['obj.id']);
                $("#epgPermissionStr").val(data['obj.permissionStr']);
                $("#epgPermissionId").val(data['obj.permissionId']);
                $("#epgStyle").val(data['obj.style']);
                var parentId=1;
                if(id!=-1){
                  parentId = data['obj.parentId'];
                }
                //$("#epgParentIdContain").html();
                epgList.createSelect('epgParentId',epgList.epgFolders,parentId);
                epgList.createSelect('epgStatus',epgList.epgStatus,data['obj.status']);
                //$("#epgStatusContain").html();
                //$("#epgStatus").val(data['obj.status']);
              }
            }
          });
        }else{
          epgList.createSelect('epgParentId',epgList.epgFolders,1);
          epgList.createSelect('epgStatus',epgList.epgStatus,1);
        }
      },
      goToPage:function(pageNo){
        epgList.currentPage = pageNo;
        epgList.listEpgs();
      },
      epgStatus:[
        {id:1,name:'正常使用'},
        {id:11,name:'测试，仅用于测试开发环境'},
        {id:10,name:'关闭'}
      ],
      epgFolders:[{id:1,name:'发布视频'},
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
        return epgList.getTextOfArray(id,epgList.epgStatus,'id','name');
      },
      getFolderName:function(parentId){
        return epgList.getTextOfArray(parentId,epgList.epgFolders,'id','name');
      },
      initFolders:function(){
        $.ajax({
          url:'/live/epg!listFolderEpgs.action',
          dataType:'json',
          success:function(data){
            var folders = data['objs'];
            if(folders!=null){
              folders.push({id:0,name:'功能分类'});
              epgList.epgFolders=folders;
            }
          }
        });
      }
    };

  </script>

</body>
</html>
