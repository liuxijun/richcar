<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/3/20
  Time: 17:20
  录制任务列表，和live.jsp很相似，可对照
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>录制管理 - <%=IndividualUtils.getInstance().getName()%></title>
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
        <li class="active">录制管理</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-microphone"></i>录制管理

      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">


        <div class="row page-content-main">

          <form role="form" class="form-horizontal">
            <div class="col-xs-12 no-padding movie-info">
              <div class="row">
                <div class="input-group pull-right search-group" style="width:240px;height: 30px; margin: 9px;">
                  <div style="width: 200px;height: 30px;float: left;">
                    <input type="text" id="search_word" placeholder="搜索录制" class="form-control search-query" style="border-bottom-left-radius: 0;">
                  </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                  <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                </div>
              </div>
              <div class="space-6"></div>
              <div class="tabbable">

                <table class="table table-striped table-bordered table-hover table-30">
                  <thead>
                  <tr>
                    <th width="60%"><a href="#" onclick='order_by("name")' title="按标题排序" id="order_by_name">标题</a></th>
                    <th class="center"><a href="#" onclick='order_by("createTime")' title="按创建时间排序" id="order_by_time">创建时间</a></th>
                    <th class="center"><a href="#" onclick='order_by("status")' title="按状态排序" id="order_by_status">状态</a></th>
                    <th class="center"><a href="#" onclick='order_by("type")' title="按类型排序" id="order_by_type">类型</a></th>
                    <th class="center">操作</th>
                  </tr>
                  </thead>

                  <tbody>
                  </tbody>
                </table>

                <div class="row">
                  <div class="col-md-2">
                    <button class="btn btn-green" id="btn-newRecord" type="button">新建录制</button>
                  </div>
                  <div class="col-md-6 col-md-offset-4">
                    <ul class="pagination pull-right" id="page-nav">
                    </ul>
                  </div>
                </div>
              </div>


              <div class="space-10"></div>

            </div>
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
  <!-- basic scripts -->
  <!-- inline scripts related to this page -->
  <script src="../scripts/tmpl.min.js"></script>
  <script src="../scripts/moment.min.js"></script>
  <script type="text/x-tmpl" id="tmpl-recording">
        <tr><td>{%=o.title%}<div class="living_mark"><div class="sk-spinner sk-spinner-wave"><div class="sk-rect1"></div><div class="sk-rect2"></div>
      <div class="sk-rect3"></div><div class="sk-rect4"></div><div class="sk-rect5"></div></div></div>
        </td><td class="center">{%=o.createTime%}</td><td class="center">{%=o.status%}</td><td class="center">{%=o.type%}</td>
        <td><a class="btn btn-xs" data-rel="tooltip" data-original-title="查看详情" data-placement="bottom" href="#"
        onclick="viewRecordDetail({%=o.id%});return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>
        <a class="btn btn-xs" data-rel="tooltip" data-original-title="删除" data-placement="bottom" href="#"
        onclick="removeRecord({%=o.id%});return false;"><i class="ace-icon fa fa-trash bigger-110 icon-only"></i></a>
        <a class="btn btn-danger btn-xs" data-rel="tooltip" data-original-title="停止" data-placement="bottom" href="#"
        onclick="stopRecord({%=o.id%});return false;"><i class="ace-icon fa fa-stop bigger-110 icon-only"></i></a>
        </td></tr>
  </script>
  <script type="text/x-tmpl" id="tmpl-record">
        <tr><td>{%=o.title%}</td><td class="center">{%=o.createTime%}</td><td class="center">{%=o.status%}</td><td class="center">{%=o.type%}</td>
        <td><a class="btn btn-xs" data-rel="tooltip" data-original-title="查看详情" data-placement="bottom" href="#"
        onclick="viewRecordDetail({%=o.id%});return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>
        <a class="btn btn-xs" data-rel="tooltip" data-original-title="删除" data-placement="bottom" href="#"
        onclick="removeRecord({%=o.id%});return false;"><i class="ace-icon fa fa-trash bigger-110 icon-only"></i></a>
        <a class="btn btn-success btn-xs" data-rel="tooltip" data-original-title="启动" data-placement="bottom" href="#"
        onclick="startRecord({%=o.id%});return false;"><i class="ace-icon fa fa-play bigger-110 icon-only"></i></a>
        </td></tr>
  </script>
  <script type="text/x-tmpl" id="tmpl-recording-tr">
        <tr><td>{%=o.title%}
        <div class="living_mark"><div class="sk-spinner sk-spinner-wave"><div class="sk-rect1"></div><div class="sk-rect2"></div>
        <div class="sk-rect3"></div><div class="sk-rect4"></div><div class="sk-rect5"></div></div></div>
        </td><td class="center">{%=o.createTime%}</td><td class="center">{%=o.status%}</td><td class="center">{%=o.type%}</td>
        <td><a class="btn btn-xs" data-rel="tooltip" data-original-title="查看详情" data-placement="bottom" href="#"
        onclick="viewRecordDetail({%=o.id%});return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>
        <a class="btn btn-xs" data-rel="tooltip" data-original-title="删除" data-placement="bottom" href="#"
        onclick="removeLive({%=o.id%});return false;"><i class="ace-icon fa fa-trash bigger-110 icon-only"></i></a>
  </script>
  <script type="text/x-tmpl" id="tmpl-record-tr">
        <tr><td>{%=o.title%}
        </td><td class="center">{%=o.createTime%}</td><td class="center">{%=o.status%}</td><td class="center">{%=o.type%}</td>
        <td><a class="btn btn-xs" data-rel="tooltip" data-original-title="查看详情" data-placement="bottom" href="#"
        onclick="viewRecordDetail({%=o.id%});return false;"><i class="ace-icon fa fa-edit bigger-110 icon-only"></i></a>
        <a class="btn btn-xs" data-rel="tooltip" data-original-title="删除" data-placement="bottom" href="#"
        onclick="removeRecord({%=o.id%});return false;"><i class="ace-icon fa fa-trash bigger-110 icon-only"></i></a>
  </script>
  <script type="text/x-tmpl" id="tmpl-record-stop-op">
        <a class="btn btn-danger btn-xs" data-rel="tooltip" data-original-title="停止" data-placement="bottom" href="#"
        onclick="stopRecord({%=o.id%});return false;"><i class="ace-icon fa fa-stop bigger-110 icon-only"></i></a>
  </script>
  <script type="text/x-tmpl" id="tmpl-record-start-op">
        <a class="btn btn-success btn-xs" data-rel="tooltip" data-original-title="启动" data-placement="bottom" href="#"
        onclick="startRecord({%=o.id%});return false;"><i class="ace-icon fa fa-play bigger-110 icon-only"></i></a>
  </script>

  <script type="text/javascript">
    var page_index = 1;
    var page_size = 15;
    var page_count = 0;
    var order = "createTime";
    var dir = "asc";
    var channelReset = false;

    var _isRoot = <s:if test="#session.sessionOperator.isRoot">true;</s:if><s:else>false;</s:else>

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


      page_index = 1;
      get_record();

      $("#btn-newRecord").click(function(){
        document.location.href = "recordProp.jsp";
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

      $(document).keydown(function( event ) {
        if ( event.which == 13 ) {
          event.preventDefault();
          page_index = 1;
          get_record();
        }
      });
      $("#btn_search").click(function(){
        page_index = 1;
        get_record();
      })
    });

    function get_record(){
      $.ajax({
        type: "POST",
        url: "/live/live!getRecordList.action",
        //dataType: "json",
        dataType: "text",
        //data: {"channelId":ch_id, "c_name":search_value,"c.status": 2, "cc.status":2, "isSpechialExit": null},
        data: {"searchValue":$("#search_word").val(),
          "pageBean.pageIndex": page_index,
          "pageBean.pageSize": page_size,
          "pageBean.orderBy": order,
          "pageBean.orderDir": dir
        },
        success: function(msg) {
          $("tbody").html("");
          var response = eval("(function(){return " + msg + ";})()");
          if(parseInt(response.totalCount) < 0){
            alert("登录已过期，请重新登录！");
            location.href = "../login.jsp";
          }else {
            var tmplName, record;
            for (var n = 0; n < response.recordList.length; n++) {
              var o = response.recordList[n];
              if (o) {
                tmplName = (o.status == 2)? "tmpl-recording-tr": "tmpl-record-tr";
                record = new Record(o.id, o.title, moment(o.createTime).locale("zh-Cn").fromNow(),
                        getStatusName(o.status), getTypeName(o.type));
                s = tmpl(tmplName,record);
                if(parseInt(o.status) == 2 && parseInt(o.type) == 1){ // 单次任务，正在录制
                  s += tmpl("tmpl-record-stop-op", record);
                }else if(parseInt(o.status) == 1 && parseInt(o.type) == 1){// 单次任务，正常状态
                  s += tmpl("tmpl-record-start-op", record);
                }
                $("tbody").append(s + "</td></tr>");
              }
            }
            $('[data-rel=tooltip]').tooltip();

            // reset page nav
            var record_count = parseInt(response.totalCount);
            rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "to_page", record_count);
          }
        }
      }).fail(function(){
        alert("失败了，重新登录试试吧！");
      });
    }

    function viewRecordDetail(id){
      location.href = "recordProp.jsp?id=" + id;
    }

    function to_page(index){
      page_index = index;
      get_record();
    }

    function order_by(v){
      if(v == order){
        dir = (dir == "asc")? "desc" : "asc";
      }else{
        order = v;
        dir = "asc";
      }

      $("#order_by_name").attr("title", (order == "name" && dir == "desc")?  "按名称倒序排序" : "按名称排序");
      $("#order_by_status").attr("title", (order == "status" && dir == "desc")?  "按状态倒序排序" : "按状态排序");
      $("#order_by_time").attr("title", (order == "createTime" && dir == "desc")?  "按发布时间倒序排序" : "按发布时间排序");
      $("#order_by_type").attr("title", (order == "type" && dir == "desc")?  "按类型倒序排序" : "按类型排序");
      page_index = 1;

      get_record();
    }

    // 删除直播
    function removeRecord(id){
      if(!confirm("确定要删除录制任务吗？")) return;

      $.ajax({
        type: "POST",
        url: "/live/live!removeLive.action",
        dataType: "text",
        data: {
          "obj.id" : id
        },
        success: function (msg) {
          var response = eval("(function(){return " + msg + ";})()");
          if(response.success){
            get_record();
          }else{
            alert("删除录制失败：" + $.isArray(response.error)? response.error[0] : response.error);
          }
        }
      });
    }

    // 手动启动直播
    function startRecord(id){
      $.ajax({
        type: "POST",
        url: "/live/live!start.action",
        dataType: "text",
        data: {
          "obj.id" : id
        },
        success: function (msg) {
          var response = eval("(function(){return " + msg + ";})()");
          if(response.success){
            alert("录制启动成功，手动启动录制会导致系统重新设定停止时间，请手动停止。");
            get_record();
          }else{
            alert("启动录制失败：" + response.message);
          }
        }
      });
    }

    // 手动停止录制
    function stopRecord(id){
      $.ajax({
        type: "POST",
        url: "/live/live!stop.action",
        dataType: "text",
        data: {
          "obj.id" : id
        },
        success: function (msg) {
          var response = eval("(function(){return " + msg + ";})()");
          if(response.success){
            alert("停止录制成功。");
            get_record();
          }else{
            alert("停止录制失败：" + response.message);
          }
        }
      });
    }

    // 获取直播类型名字
    function getTypeName(type){
      switch(parseInt(type)){
        case 1: return "单次";break;
        case 2: return "循环"; break;
      }
      return "未知";
    }

    // 获取状态名称
    function getStatusName(status){
      switch(parseInt(status)){
        case 1: return "正常";break;
        case 2: return "正在录制"; break;
        case 9: return "锁定"; break;
        case 0: return "过期"; break;
        case 3: return "正在启动";break;
        case 4: return "正在停止";break;
      }
      return "未知";
    }

    function Record(id, title, createTime, status, type){
      this.id = id;
      this.title = title;
      this.createTime = createTime;
      this.status = status;
      this.type = type;
    }

  </script>
</div>
</body>
</html>

