<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/12/9
  Time: 14:35
  为栏目选择组织，用于新增了栏目后为栏目增加可以观看的组织
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>栏目组织关联 - <%=IndividualUtils.getInstance().getName()%></title>
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
          <a href="../man.jsp">管理首页</a>
        </li>
        <li>视频管理</li>
        <li href="../man/channel.jsp">栏目管理</li>
        <li class="active">栏目关联</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-random"></i>栏目关联组织

      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
      <div class="page-content-area">
        <div class="row page-content-main">
          <form role="form" class="form-horizontal">
            <div class="col-xs-12 no-padding movie-info">
              <div class="alert alert-block alert-warning">
                <button type="button" class="close" data-dismiss="alert">
                  <i class="ace-icon fa fa-times"></i>
                </button>
                <i class="ace-icon fa fa-warning"></i>
                该功能用于新建栏目关联组织，建议不用用于其他用途。正常为组织设置观看栏目应从
                <a href="organization.jsp">组织管理</a>组织详情中进行。
              </div>
              <div class="space-6"></div>
              <div class="btn-group">
                <!--<select class="form-control " id="filter-channel">
                    <option value="-1">所有栏目</option>
                </select>//-->
                <select class="form-control " id="select-channel">
                  <option value="-1">请选择栏目</option>
                </select>
              </div>

              <div class="space-6"></div>
              <div class="tabbable">
                <div id="org-container" style="margin:0;padding-left:8px;min-height: 200px;border:1px solid #DDDADA">
                  <div id="tree-org" class="tree"></div>
                </div>

              </div>
              <div class="space-6"></div>
              <div class="action-buttons">
                <a class="gray" href="#" title="包含子组织" id="btn_include_children">
                  <i class="ace-icon fa fa-check-square"> 同时关联子组织</i>
                </a>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <button class="btn btn-green" id="btn-confirm" type="button">确定</button>
                  <button class="btn btn-blue" id="btn-ref-all" type="button">关联所有组织</button>
                </div>
                <div class="col-md-6 col-md-offset-4">
                  <ul class="pagination pull-right" id="page-nav"></ul>
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
</div>
<!-- /.main-container -->
<%@include file="../inc/displayFooter.jsp" %>
<script src="../scripts/channel.js?v=1.423"></script>
<script src="../scripts/organization.js?v=1.423"></script>
<script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=0.428"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
  var treeUtils, orgUtils;
  var __channels;

  jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html: true});

    $(document).ajaxStart(function () {
      $("#loading_container").show();
    });

    $(document).ajaxStop(function () {
      setTimeout(function () {
        $("#loading_container").hide();
      }, 200);
    });

    $("#btn_include_children").click(function (e) {
      switchIncludeChildren();
      e.stopPropagation();
    });

    // 切换用户类型
    $("#select-channel").change(function(){
      if( parseInt($("#select-channel").val()) > 0){
        // 重置组织树
        if( orgUtils ) {
          show_tree(orgUtils.generateTreeData());
        }
      }
    });

    $("#btn-confirm").click(function(){
      referenceChannelToOrg();
    });
    $("#btn-ref-all").click(function(){
      referenceChannelToAllOrg();
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

    loadChannel();
    loadOrganization();
  });

  function loadOrganization(){
    $.ajax({
      type: "POST",
      url: "/user/org!orgTree.action",
      dataType: "text",
      success: function(msg){
        orgUtils = new OrganizationUtils();
        orgUtils.initByJson( msg );
      }
    });
  }

  function switchIncludeChildren(){
    var mark = $("#btn_include_children i").hasClass("fa-square-o") ? 1 : 2;

    if(mark == 1){
      $("#btn_include_children i").removeClass("fa-square-o").addClass("fa-check-square");
    }else{
      $("#btn_include_children i").removeClass("fa-check-square").addClass("fa-square-o");
    }
  }

  function show_tree(data){
    delete($('#tree-org').data().tree);
    $('#tree-org').remove();
    $("#org-container").append('<div id="tree-org" class="tree"></div>');
    $('#tree-org').ace_tree({
      dataSource: data ,
      multiSelect:false,
      loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
      'open-icon' : 'ace-icon tree-minus',
      'close-icon' : 'ace-icon tree-plus',
      'selected-icon' : null,
      'unselected-icon' : null,
      'selectable' : true
    });
  }

  function loadChannel() {
    $.ajax({
      type: "POST",
      url: "/publish/channel!channelTree.action",
      dataType: "text",
      success: function(msg){
        __channels = msg;
        if( treeUtils == null){
          treeUtils = new ChannelUtils();
          treeUtils.initByJson(__channels);
          var leafChannelList = treeUtils.getLeafChannel();
          for(var i=0;i<leafChannelList.length; i++){
            $("#select-channel").append($("<option/>").attr("value", leafChannelList[i].id).text(leafChannelList[i].name));
          }
        }
      }
    });
  }

  function referenceChannelToOrg(){
    // check channel
    if( parseInt($("#select-channel").val()) < 0){
      alert("请选择要设置的栏目");
      return;
    }

    // check org
    var org = $('#tree-org').tree('selectedItems');
    if( !org || org.length ==0){
      alert("请选择要关联《" + $("#select-channel option:selected").text() + "》的组织！");
      return;
    }

    var childrenIncluded =  $("#btn_include_children i").hasClass("fa-check-square");
    $.ajax({
      type: "POST",
      url: "/user/org!referenceChannelOrg.action",
      dataType: "text",
      data: {"obj.id": org[0].id, "channelId": $("#select-channel").val(), "childrenIncluded":childrenIncluded},
      success: function(msg){
        var response = JSON.parse(msg);
        if(response.success){
          alert( org[0].name + "现在可以观看栏目《" + $("#select-channel option:selected").text() + "》的视频了！" );
        }else{
          alert("关联频道和组织时出错，是不是该重新登录了！");
        }
      }
    });
  }

  function referenceChannelToAllOrg(){
    if( parseInt($("#select-channel").val()) < 0){
      alert("请选择要设置的栏目");
      return;
    }

    $.ajax({
      type: "POST",
      url: "/user/org!referenceChannelAllOrg.action",
      dataType: "text",
      data: {"channelId": $("#select-channel").val()},
      success: function(msg){
        var response = JSON.parse(msg);
        if(response.success){
          alert( "现在所有的用户可以观看栏目《" + $("#select-channel option:selected").text() + "》的视频了！" );
        }else{
          alert("关联频道和组织时出错，是不是该重新登录了！");
        }
      }
    });
  }
</script>


</body>
</html>

