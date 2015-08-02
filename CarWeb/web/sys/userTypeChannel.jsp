<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/12/8
  Time: 10:55
  为用户类型选择观看栏目，此功能不是正常的操作流程，正常影片应在发布时独立设置可观看用户类型。
  此功能为新增加的用户类型，为便于为新增类型设置观看影片而设计，会为新的用户类型选择栏目下所有的影片。
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>用户类型栏目关联 - <%=IndividualUtils.getInstance().getName()%></title>
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
        <li>系统管理</li>
        <li href="../sys/userType.jsp">用户类型管理</li>
        <li class="active">管理栏目</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-plug"></i>用户类型关联栏目

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
                用户类型观看视频范围，应以视频发布时选择的用户类型设置为准，此功能为便于新增用户类型关联影片，建议不用用于其他用途。
              </div>
              <div class="space-6"></div>
              <div class="btn-group">
                <!--<select class="form-control " id="filter-channel">
                    <option value="-1">所有栏目</option>
                </select>//-->
                  <select class="form-control " id="select-user-type">
                    <option value="-1">请选择用户类型</option>
                  </select>
              </div>

              <div class="space-6"></div>
              <div class="tabbable">
                <div id="channel-container" style="margin:0;padding-left:8px;min-height: 200px;border:1px solid #DDDADA">
                  <div id="tree-channel" class="tree"></div>
                </div>

              </div>
              <div class="space-6"></div>
              <div class="action-buttons">
                <a class="gray" href="#" title="全选" id="btn_select_all">
                  <i class="ace-icon fa fa-square-o"> 全选</i>
                </a>
              </div>
              <div class="row">
                <div class="col-md-2">
                  <button class="btn btn-blue" id="btn-confirm" type="button">确定</button>
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
  <script src="../scripts/channel.min.js?v=1.423"></script>
  <script src="../scripts/fuelux/fuelux.tree.min.js?v=0.428"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
  var treeUtils;
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

    $("#btn_select_all").click(function (e) {
      selectAllChannel();
      setTimeout('$("#btn_select_all").scrollToMe()', 100);
      e.stopPropagation();
    });

    // 切换用户类型
    $("#select-user-type").change(function(){
      if( parseInt($("#select-user-type").val()) > 0){
        // 重置栏目树
        if( treeUtils ) {
          show_tree(treeUtils.generateTreeData());
        }
      }
    });

    $("#btn-confirm").click(function(){
      referenceChannelToUserType();
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
    loadUserType();
  });

  function selectAllChannel(){
    var mark = $("#btn_select_all i").hasClass("fa-square-o") ? 1 : 2;

    treeUtils.selectAllMark = mark;
    show_tree(treeUtils.generateTreeData());

    if(mark == 1){
      $("#btn_select_all i").removeClass("fa-square-o").addClass("fa-check-square");
    }else{
      $("#btn_select_all i").removeClass("fa-check-square").addClass("fa-square-o");
    }
  }

  function show_tree(data){
    delete($('#tree-channel').data().tree);
    $('#tree-channel').remove();
    $("#channel-container").append('<div id="tree-channel" class="tree"></div>');
    $('#tree-channel').ace_tree({
      dataSource: data ,
      multiSelect:true,
      loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
      'open-icon' : 'ace-icon tree-minus',
      'close-icon' : 'ace-icon tree-plus',
      'selected-icon' : 'ace-icon fa fa-check',
      'unselected-icon' : 'ace-icon',
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
        }
      }
    });
  }

  function loadUserType(){
    $.ajax({
      type: "POST",
      url: "/user/userType!list.action",
      //dataType: "json",
      dataType: "text",
      //data: {name: ver},
      success: function(msg){
        var response = JSON.parse(msg);

        for(var i=0; i<response.objs.length; i++){
          var type = response.objs[i];
          if(type){
            $("#select-user-type").append($("<option/>").attr("value", type.id).text(type.name));
          }
        }
      }
    });
  }

  function referenceChannelToUserType(){
    // check user type
    if( parseInt($("#select-user-type").val()) < 0){
      alert("请选择要设置的用户类型");
      return;
    }

    // check channel selection
    var channelItems = $('#tree-channel').tree('selectedItems');
    var channelIdArray = [];
    if( !channelItems || channelItems.length ==0){
      alert("请选择要关联《" + $("#select-user-type option:selected").text() + "》的频道！");
      return;
    }else{
      for(var i=0; i<channelItems.length; i++){
        channelIdArray.push(channelItems[i].id);
      }
    }

    var channelArray = channelItems.join(",");
    $.ajax({
      type: "POST",
      url: "/user/userType!referenceChannel.action",
      dataType: "text",
      data: {"obj.id": parseInt($("#select-user-type").val()), "channels": channelIdArray.join(",")},
      success: function(msg){
        var response = JSON.parse(msg);
        if(response.success){
          alert("已经为《" + $("#select-user-type option:selected").text() + "》关联所选频道内容！" );
        }else{
          alert("关联频道内容出错！");
        }
      }
    });
  }

</script>


</body>
</html>

