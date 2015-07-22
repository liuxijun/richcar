<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/11/17
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>访问统计 - <%=IndividualUtils.getInstance().getName()%></title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp" %>
</head>
<body class="no-skin">
<%@include file="../inc/displayHeader.jsp" %>
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
        <li class="active">访问统计</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-bar-chart"></i>访问统计
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
      <div class="page-content-area">
        <div class="row page-content-main">
          <div class="col-md-12">
            <div class="tabbable ">
            <ul class="nav nav-tabs no-border" id="myTab">
              <li class="active">
                <a data-toggle="tab" href="#tab1">
                  视频访问量
                </a>
              </li>

              <li class="">
                <a data-toggle="tab" href="#tab2" onclick="statUtils.toChannelStat();return false;">
                  频道访问量
                </a>
              </li>
              <li class="">
                <a data-toggle="tab" href="#tab3" onclick="statUtils.toConcurrent();return false;">
                  并发量统计
                </a>
              </li>
            </ul>
            <div class="tab-content no-border">
              <div id="tab1" class="tab-pane fade active in">
                <div class="row">
                  <form role="form" class="form-horizontal">
                    <div class="row">
                      <div class="col-md-4">
                        <div class="btn-group">
                          <a class="btn btn-dropdown ">
                            <span id="selectedChannel">所有栏目</span>
                            <i class="ace-icon fa fa-angle-down icon-on-right"></i>
                          </a>

                          <div id="filter-channel" class="tree" ></div>

                        </div>
                      </div>
                      <div class="col-md-8">
                        <div class="btn-group pull-right">
                          <a class="btn  btn-blue2 btn-zixuan border-radius-r-5">自选<i class="ace-icon fa fa-angle-down icon-on-right"></i> </a>

                          <div class="dropdown-menu" id="zixuan"   style="width:360px; display: none;">
                            <form class="form-horizontal">

                              <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right ">开始时间</label>
                                <div class="col-sm-7">
                                  <input type="text" class="col-sm-12" id="filter-startTime"
                                         placeholder="格式YYYY-MM-dd" data-rel="tooltip" data-original-title="">
                                </div>
                              </div>
                              <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right ">结束时间</label>
                                <div class="col-sm-7">
                                  <input type="text" class="col-sm-12" id="filter-endTime"
                                         placeholder="格式YYYY-MM-dd" data-rel="tooltip" data-original-title="">
                                </div>
                              </div>

                              <div class="space"></div>
                              <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right "></label>
                                <div class="col-sm-7">
                                  <button class="btn btn-lightwhite" type="button" id="btnFreeStat">确定 </button>

                                </div>
                              </div>

                            </form>

                          </div>

                        </div>
                        <div class="btn-group pull-right" data-toggle="buttons">
                          <label class="btn btn-blue2 active border-radius-l-5" id="statAll">
                            <input type="radio" name="options" id="option1" autocomplete="off" checked>全部
                          </label>
                          <label class="btn btn-blue2" id="statLastMonth">
                            <input type="radio" name="options"  autocomplete="off"> 上月
                          </label>
                          <label class="btn btn-blue2" id="statLastWeek">
                            <input type="radio" name="options" autocomplete="off"> 上周
                          </label>
                        </div>

                      </div>
                    </div>
                    <div class="space-6"></div>
                    <div class="cos-md-12">
                      <div class="tjsd" id="stat-span">统计时段：全部</div>
                      <div class="space-6"></div>

                      <div class="tabbable">

                        <table class="table table-striped table-bordered table-hover table-30">
                          <thead>
                          <tr>
                            <th   class="center" style="width: 150px;">排名</th>
                            <th width="50%">名称</th>
                            <th class="center">访问量</th>
                          </tr>
                          </thead>

                          <tbody>
                          </tbody>
                        </table>

                      </div>
                      <div class="space-6"></div>
                      <div class="row">
                        <div class="col-md-2">
                          <%--<button class="btn btn-blue">导出</button>--%>
                          <a href="../log/exportVisit.action" class="btn btn-blue" title="导出报表" id="btnExport" target="_blank">
                            导出
                          </a>
                        </div>
                        <div class="col-md-6 col-md-offset-4">
                          <ul class="pagination pull-right" id="page-nav"></ul>
                        </div>
                      </div>

                    </div>
                  </form>
                </div>
              </div>
              <div id="tab2" class="tab-pane fade ">

                <!--栏目估计-->
              </div>
              <div id="tab3" class="tab-pane fade "></div>
            </div>
            <!-- /.page-content-area -->
          </div>
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
      <script src="../scripts/channel.min.js?v=1.423"></script>
      <script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=0.428"></script>     <!-- inline scripts related to this page -->
      <script src="../scripts/moment.min.js"></script>
      <script type="text/javascript">
        //var filter_channel_id = -1;
       jQuery(function ($) {
          $('[data-rel=tooltip]').tooltip();
          $('[data-rel=popover]').popover({html: true});

         $(".btn-dropdown").click(function () {
            $("#filter-channel").slideDown();
          });
          $("#filter-channel").mouseleave(function () {
            $(this).slideUp();

          });

          $(".btn-zixuan").mouseenter(function () {
            $("#zixuan").slideDown();
          });
          $(".btn-zixuan1").mouseenter(function () {
            $("#zixuan1").slideDown();
          });
          $("#zixuan,#zixuan1").mouseleave(function () {
            $(this).slideUp();

          });

         $("#btnFreeStat").click( function(){
           $(".btn-blue2").removeClass("active");
           $(".btn-zixuan").addClass("active");
           statUtils.doFreeStat();
         });

         $(document).ajaxStart(function(){
           $("#loading_container").show();
         });

         $(document).ajaxStop(function(){
           setTimeout(function(){$("#loading_container").hide();}, 200);
         });

         statUtils.init();

         $("#statLastWeek").click(function(){
           statUtils.lastWeek();
         });
         $("#statLastMonth").click(function(){
           statUtils.lastMonth();
         });
         $("#statAll").click(function(){
           statUtils.statAll();
         });

         $(document).keydown(function( event ) {
           if ( event.which == 13 ) {
             if( $("#filter-startTime").is(":focus") || $("#filter-endTime").is(":focus")){
               event.preventDefault();
               statUtils.getItem();
             }
           }
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

        });

        var statUtils = {
          filter_channel_id : -1,
          channelUtils : null,
          pageSize : 50,
          pageIndex : 1,
          __channels : "",
          startTime : null,
          endTime : null,
          __EXPORT_BASE_URL : "../log/exportVisit.action",
          loadChannel : function(){
            $.ajax({
              type: "POST",
              url: "/publish/channel!channelTree.action",
              dataType: "text",
              success: function(msg){
                statUtils.__channels = msg;
                if( statUtils.channelUtils == null){
                  statUtils.channelUtils = new ChannelUtils();
                  statUtils.channelUtils.initByJson(statUtils.__channels);
                }

                statUtils.showFilterTree(statUtils.channelUtils.generateFilterTree());
              }
            });
          },
          showFilterTree : function(data){
            $('#filter-channel').ace_tree({
              dataSource: data ,
              multiSelect:false,
              loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
              'open-icon' : 'ace-icon tree-minus',
              'close-icon' : 'ace-icon tree-plus',
              'selected-icon' : null,
              'unselected-icon' : null,
              'selectable' : true
            });
            $('#filter-channel').on('updated', function(e, result) {
              if(result && result.info && result.info.length > 0){
                $("#selectedChannel").html(result.info[0].name);
                statUtils.filter_channel_id = result.info[0].id;
                statUtils.getItem();
                // trigger refresh
                //get_all_item();
              }
            });
          },

          init : function(){
            statUtils.loadChannel();
            statUtils.getItem();
            statUtils.setExportURL();
          },

          getItem : function(){
            $.ajax({
              type: "POST",
              url: "/log/statVisit.action",
              data : {channelId : statUtils.filter_channel_id, "pageBean.pageSize": statUtils.pageSize,
                "pageBean.pageNo": statUtils.pageIndex, "startTime": statUtils.startTime, "endTime":statUtils.endTime},
              dataType: "text",
              success: function(msg){
                $("tbody").html("");
                //var response = eval("(function(){return " + msg + ";})()");
                var response = JSON.parse(msg);
                var record_count = response.totalCount;
                console.info("count:" + response.totalCount);
                for (var i = 0; i < response.objs.length; i++) {
                  var o = response.objs[i];
                  $("tbody").append(statUtils.buildItem(o, i+1));
                }
                rebuild_page_nav($("#page-nav"), Math.ceil(record_count / statUtils.pageSize), statUtils.pageIndex, "statUtils.toPage", record_count);
                statUtils.setExportURL();
              }
            });
          },

          buildItem : function( obj, index ){
            var itemIndex = parseInt(statUtils.pageSize) * (parseInt(statUtils.pageIndex) - 1) + parseInt(index);
            var output = '<tr><td class="center">' + itemIndex +
                    '</td><td>' + obj.name + '</td><td class="center">' + obj.count + '</td></tr>';
            return output;
          },

          toPage : function( index ){
            statUtils.pageIndex = index;
            statUtils.getItem();
          },

          lastWeek : function(){
            /*get last week stat*/
            var lastMonday = moment().locale("zh-Cn").day(-6).format("l");
            //var lastMonday = moment().locale("zh-Cn").day(-6).format("YYYY-MM-DD HH:mm:ss");
            var lastSunday = moment().locale("zh-Cn").day(0).format("l");

            $("#stat-span").html("统计时段：" + lastMonday + " - " + lastSunday);
            //console.info( lastMonday + "  " + lastSunday);
            statUtils.startTime = lastMonday + " 00:00:00";
            statUtils.endTime = lastSunday + " 23:59:59";
            //console.info(statUtils.startTime);

            statUtils.pageIndex = 1;
            statUtils.getItem();
          },

          lastMonth : function(){
            var m = moment().month();
            var firstDay = moment().locale("zh-Cn").month(m-1).startOf('month').format("l");
            var lastDay = moment().locale("zh-Cn").month(m-1).endOf('month').format("l");

            $("#stat-span").html("统计时段：" + firstDay + " - " + lastDay);
            console.info( firstDay + "  " + lastDay);
            statUtils.startTime = firstDay + " 00:00:00";
            statUtils.endTime = lastDay + " 23:59:59";
            //console.info(statUtils.startTime);
            statUtils.pageIndex = 1;
            statUtils.getItem();
          },

          statAll : function(){
            statUtils.startTime = statUtils.endTime = null;

            $("#stat-span").html("统计时段：全部");
            statUtils.pageIndex = 1;
            statUtils.getItem();
          },

          doFreeStat: function(){
            // 检查输入日期是否合法
            var startTime = $.trim($("#filter-startTime").val());
            var statSpan = "统计时段：";
            if(startTime != ""){
              if( !moment(startTime, "YYYY-MM-DD").isValid()){
                $("#filter-startTime").attr("data-original-title","开始时间不合法");
                $("#filter-startTime").addClass('tooltip-error').tooltip('show');
                return;
              }
              statSpan += moment(startTime, "YYYY-MM-DD").locale("zh-Cn").format("l");
              statUtils.startTime = moment(startTime, "YYYY-MM-DD").locale("zh-Cn").format("l") + " 00:00:00";
              console.info("开始时间:" + statUtils.startTime);
            }else{
              statUtils.startTime = null;
              statSpan += "远古时期";
            }

            var endTime = $.trim($("#filter-endTime").val());
            if( endTime != ""){
              if( !moment(endTime, "YYYY-MM-DD").isValid()){
                $("#filter-endTime").attr("data-original-title","结束时间不合法");
                $("#filter-endTime").addClass('tooltip-error').tooltip('show');
                return;
              }
              statSpan += " - " + moment(endTime, "YYYY-MM-DD").locale("zh-Cn").format("l");
              statUtils.endTime = moment(endTime, "YYYY-MM-DD").locale("zh-Cn").format("l") + " 23:59:59";
              //console.info("结束时间：" + statUtils.endTime);
            }else{
              statUtils.endTime = null;
              statSpan += " - 现在";
            }
            $("#stat-span").html(statSpan);
            statUtils.pageIndex = 1;
            statUtils.getItem();
          },

          setExportURL : function(){
            var _url = statUtils.__EXPORT_BASE_URL;

            _url += "?pageBean.pageSize=" + statUtils.pageSize;
            //str.match(/value$/)
            if(parseInt(statUtils.filter_channel_id) > 0){
              _url += "&channelId=" + statUtils.filter_channel_id;
            }
            if(statUtils.startTime != null){
                _url += "&startTime=" + statUtils.startTime.replace(" ", "%20");
            }
            if(statUtils.endTime != null){
              _url += "&endTime=" + statUtils.endTime.replace(" ", "%20");
            }
            if(statUtils.pageIndex != 1){
              _url += "&pageBean.pageNo=" + statUtils.pageIndex;
            }
            $("#btnExport").attr("href", _url);
            //console.info(_url);
          },

          toChannelStat : function(){
            location.href = "statChannel.jsp";
          },
          toConcurrent : function(){
            location.href = "statConcurrent.jsp";
          }

        };


      </script>
</div>
</div>
</body>
</html>

