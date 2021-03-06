<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/1/7
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>问卷管理 - <%=IndividualUtils.getInstance().getName()%></title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp"%>

</head>
<body class="no-skin">
<%@include file="../inc/displayHeader.jsp"%>
<div class="main-container" id="main-container">
  <script type="text/javascript">
    try {
      ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
  </script>

  <%@include file="../inc/displayMenu.jsp"%>
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
        <li>系统管理</li><li class="active">问卷管理</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-list-alt"></i>问卷管理
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">


        <div class="row page-content-main">

          <form role="form" class="form-horizontal">
            <div class="col-md-12">
              <div class="tabbable ">
                <div class="tab-content no-border">
                  <!--<div class="col-xs-12 no-padding movie-info" id="allPanel">//-->
                  <div class="tab-pane active">
                    <div class="btn-group" style="height: 30px"></div>
                    <div class="input-group pull-right search-group" style="width: 220px;">
                      <input type="text" placeholder="" class="form-control search-query" id="search_word">
                                     <span class="input-group-btn">
                                         <button class="btn btn-sm" type="button" id="btn_search_audit">
                                           <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                         </button>
                                     </span>
                    </div>
                    <div class="space-6"></div>
                    <div class="tabbable">

                      <table class="table table-striped table-bordered table-hover table-30" id="tabInvest">
                        <thead>
                        <tr>
                          <th class="center"><input type="checkbox" id="select_all_invest"></th>
                          <th width="20%">
                            <a href="#" onclick='order_by("title")' title="按名称排序" id="order_by_title">名称</a>
                          </th>
                          <th>
                            <a href="#" onclick='order_by("time")' title="按修改时间排序" id="order_by_time">修改时间</a>
                          </th>
                          <td>问卷数</td>
                          <td>题目数</td>
                          <th class="center">
                            <a href="#" onclick='order_by("status")' title="按状态排序" id="order_by_status">状态</a>
                          </th>
                          <th class="center">
                            有效期
                          </th>
                          <th class="center">操作</th>
                        </tr>
                        </thead>

                        <tbody>
                        </tbody>
                      </table>

                    </div>
                    <div class="space-6"></div>
                    <div class="row">
                      <div class="col-md-4">
                        <a class="btn btn-green" id="btnNewInvest">创建问卷</a>
                        <a class="btn btn-red" id="btnRemoveInvest">删除</a>
                      </div>
                      <div class="col-md-5 col-md-offset-3"><ul class="pagination pull-right" id="page-nav">
                      </ul></div>
                    </div>
                  </div>
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
  <%@include file="../inc/displayFooter.jsp"%>
<script src="../scripts/ajaxEvent.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
  var order = "time";
  var dir = "desc";
  var page_index = 1;
  var page_size = 20;
  var page_count = 0;
  var _INVEST_STATUS_ONLINE = 1;
  var _INVEST_STATUS_OFFLINE = 2;
  var _INVEST_TIME_STATUS_ACTIVE = 1;  // 激活
  var _INVEST_TIME_STATUS_EXPIRED = 2;  // 过期
  var _INVEST_TIME_STATUS_NOT_OPEN = 3;  // 未开启

  jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html:true});
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

    $("#btnNewInvest").click(function(){
      location.href = "editInvest.jsp";
    });

    $(document).keydown(function( event ) {
      if ( event.which == 13 ) {
        if( $("#search_word").is(":focus")){
          event.preventDefault();
          get_all_invest();
        }
      }
    });

    $("#btn_search").click(function(){
      get_all_invest();
    });

    $("#select_all_invest").click(function(){
      var checkboxes= $("#tabInvest tbody input:checkbox");
      if(checkboxes){
        if ($(this).prop('checked')) {
          checkboxes.prop('checked', true);
        } else {
          checkboxes.prop('checked', false);
        }
      }
    });

    $("#btnRemoveInvest").click(function(){
      // 删除选中
      batchRemove();
      $("#select_all_invest").prop("checked", false);
    });

    get_all_invest();
  });



  function to_page(index) {
    page_index = index;
    get_all_invest();
  }


  function order_by(v) {
    if (v == order) {
      dir = (dir == "asc") ? "desc" : "asc";
    } else {
      order = v;
      dir = "asc";
    }

    $("#order_by_title").attr("title", (order == "title" && dir == "desc") ? "按标题倒序排序" : "按标题排序");
    $("#order_by_time").attr("title", (order == "time" && dir == "desc") ? "按修改时间倒序排序" : "按修改时间排序");
    $("#order_by_status").attr("title", (order == "status" && dir == "desc") ? "按状态倒序排序" : "按状态排序");
    page_index = 1;

    get_all_invest();
  }


  function get_all_invest() {
    var _order = "invest.lastModified";
    switch (order) {
      case "title": _order = "invest.title";break;
      case "time": _order = "invest.lastModified";break;
      case "status": _order = "invest.status";break;
    }
    var search_word = $.trim($("#search_word").val());
    $.ajax({
      type: "POST",
      url: "/invest/search.action",
      dataType: "text",
      data: {"searchWord":search_word,"pageBean.pageSize":page_size,"pageBean.pageNo":page_index,"pageBean.orderBy":_order, "pageBean.orderDir":dir},
      success: function(msg) {
        $("#tabInvest tbody").html("");
        var response = parseJson(msg)
        var record_count = response.totalCount;

        for (var i = 0; i < response.investList.length; i++) {
          var o = response.investList[i];
          $("#tabInvest tbody").append(build_item(o));
        }

        $('[data-rel=tooltip]').tooltip();
        rebuild_page_nav($("#page-nav"), Math.ceil(record_count / page_size), page_index, "to_page", record_count);
      }
    });
  }


  function getStatusName(status){
    switch(parseInt(status)){
      case _INVEST_STATUS_ONLINE: return "在线";break;
      case _INVEST_STATUS_OFFLINE: return "下线";break;
      default:
    }
    return "未知";
  }

  function build_item(o) {
    if (o) {
      var output = '<tr><td class="center"><input type="checkbox" value="' + o.id + '"></td>' +
              '<td>' + o.title + '</td>' +
              '<td>'+ o.lastModified + '</td>' +
              '<td class="center">' + numberWithCommas(o.paperCount) + '</td>' +
              '<td class="center">' + numberWithCommas(o.questionCount) + '</td>' +
              '<td class="center">' + getStatusName(o.status)+ '</td>' +
              '<td class="center">' + moment(o.startTime).format("YYYY-MM-DD") + "至" + moment(o.endTime).format("YYYY-MM-DD") +  '</td>' +
              '<td class="center">' +
              '<a data-placement="bottom" ' +
              'data-original-title="结果" data-rel="tooltip" class="btn btn-grey btn-xs" ' +
              'href="investResult.jsp?id=' + o.id + '" target="_blank">' +
              '<i class="ace-icon fa fa-bar-chart bigger-110 icon-only"></i>' +
              '</a>' +
              '<a data-placement="bottom" ' +
              'data-original-title="编辑" data-rel="tooltip" class="btn btn-grey btn-xs" ' +
              'href="#" onclick="editInvest(\'' + o.id + '\');return false">' +
              '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
              '</a>';
      if(parseInt(o.status) == _INVEST_STATUS_OFFLINE){
        output += '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-original-title="上线" ' +
        'data-placement="bottom" href="#" onclick="online(' + o.id +
        ');return false;"><i class="ace-icon fa fa-square-o bigger-110 icon-only"></i></a>';
      }
      if(parseInt(o.status) == _INVEST_STATUS_ONLINE){
        output += '<a class="btn btn-grey btn-xs" data-rel="tooltip" data-original-title="下线" ' +
        'data-placement="bottom" href="#" onclick="offline(' + o.id +
        ');return false;"><i class="ace-icon fa fa-check-square-o bigger-110 icon-only"></i></a>';
      }

      output +=  '<a class="btn btn-grey btn-xs" data-rel="tooltip" ' +
      'data-original-title="删除" data-placement="bottom" href="#" ' +
      'onclick="removeInvest(\'' + o.id + '\');return false">' +
      '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>' +
      '</a> </td></tr>';
      //console.info(output);
      return output;
    } else {
      return "";
    }
  }

  function setStatus(id , m){
    // m 1 上线 2 下线
    $.ajax({
      type: "POST",
      url: "/invest/invest!setStatus.action",
      dataType: "text",
      data: {"obj.id": id, "obj.status":m},
      success: function(msg){
        var response = parseJson(msg);
        if(response.success){
          get_all_invest();
        }else{
          //alert("创建用户失败：" + $.isArray(response.error)? response.error[0] : response.error);
          alert("设置问卷状态失败：" +  response.error);
        }
      }
    }).fail(function(){
      alert("呀，设置问卷状态失败！");
    });
  }

  function online(id){
    setStatus(id, _INVEST_STATUS_ONLINE);
  }

  function offline(id){
    setStatus(id, _INVEST_STATUS_OFFLINE);
  }

  function batchRemove(){
    var ids = getSelectedArray();
    if(ids.length == 0){
      alert("请选择要删除的问卷");
      return;
    }else{
      if(!confirm("真的要删除这" + ids.length + "个问卷吗？")) return;
    }

    doRemove(ids.join(","));
  }

  // 删除选中问卷
  function removeInvest(id){
    if(!confirm("确定要删除这个问卷吗？")) return;
    doRemove(id);
  }

  // 执行删除请求
  function doRemove(ids){
    $.ajax({
      type: "POST",
      url: "/invest/invest!removeInvests.action",
      dataType: "text",
      data: {"investIdArray": ids},
      success: function(msg){
        var response = parseJson(msg);
        if(response.success){
          get_all_invest();
        }else{
          alert("删除问卷失败：" +  response.error);
        }
      }
    }).fail(function(){
      alert("呀，删除问卷失败！");
    });
  }

  function getSelectedArray(){
    var checkboxes= $("#tabInvest tbody input:checkbox:checked");
    var idArray = [];
    checkboxes.each(function(){
      idArray.push(this.value);
    });

    return idArray;
  }

  function editInvest(id){
    location.href = "editInvest.jsp?id=" + id;
  }

</script>

</body>
</html>


