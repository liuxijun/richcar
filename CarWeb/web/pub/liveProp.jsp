<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/2/27
  Time: 11:23
  新建和编辑直播
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>直播编辑 - <%=IndividualUtils.getInstance().getName()%></title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp" %>
  <link rel="stylesheet" type="text/css" href="../style/bootstrap-datetimepicker.min.css">
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
        <li><a href="live.jsp">直播管理</a></li>
        <li class="active">直播编辑</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-edit"></i>直播属性
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
      <div class="page-content-area">
        <div class="row page-content-main">
          <div class="col-md-12">
            <h4 id="stepTitle">1.直播任务（对应统一转码平台的任务）</h4>
            <div class="tabbable ">
              <ul class="nav nav-tabs no-border" id="myTab" style="display: none">
                <li class="active">
                  <a data-toggle="tab" href="#" onclick="moveToStep(1);return false;">
                    核心设置
                  </a>
                </li>

                <li class="">
                  <a data-toggle="tab" href="#" onclick="moveToStep(2);return false;" >
                    描述信息
                  </a>
                </li>
                <li class="">
                  <a data-toggle="tab" href="#" onclick="moveToStep(3);return false;" >
                    栏目设置
                  </a>
                </li>
              </ul>
              <div class="tab-content no-border">
                <div id="tabStepOne" class="tab-pane fade active in">
                  <div class="row">
                    <form role="form" class="form-horizontal">

                      <div class="tabbable">
                        <ul id="taskTab" class="nav nav-tabs">
                          <li class="active">
                            <a data-toggle="tab" href="#" onclick="showNewTask();return false;">
                              新建任务
                            </a>
                          </li>
                          <li>
                            <a href="#" data-toggle="tab" id="tabExistTask" onclick="showExistTask();return false;">
                              已有任务
                            </a>
                          </li>
                        </ul>

                        <div class="tab-content">
                          <div class="tab-pane fade" id="task-synch">
                            <div><i class="text-info ace-icon fa fa-rss icon-only"></i>直播任务 <i class="text-warning ace-icon fa fa-microphone icon-only"></i>录制任务</div>
                            <table class="table table-striped table-bordered table-hover table-30" id="taskTable">
                              <thead>
                              <tr>
                                <th width="40"></th>
                                <th width="80%">任务</th>
                                <th class="center"></th>
                              </tr>
                              </thead>
                             <tbody>
                              </tbody>
                            </table>
                            <div class="space-6"></div>
                            <div class="row">
                              <div class="col-md-6 col-md-offset-6">
                                <ul class="pagination pull-right" id="task-page-nav"></ul>
                              </div>
                            </div>
                          </div>
                          <div class="tab-pane fade active in" id="task-new">
                            <form class="form-horizontal">
                              <div id="newTaskDetailContainer">
                              <div id="taskDetail" class="row page-content-main">
                                <div class="col-xs-12 no-padding">
                                  <div class="form-group">
                                    <label class="col-sm-2 control-label no-padding-right filed-need">名称</label>
                                    <div class="col-sm-4">
                                      <input type="text" class="col-sm-12" id="taskName" maxlength="64">
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-sm-2 control-label no-padding-right">源</label>
                                    <div class="col-sm-4">
                                      <select class="form-control " id="taskSource">
                                      </select>
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-sm-2 control-label no-padding-right">编码配置</label>
                                    <div class="col-sm-10 checkbox" id="taskTemplateContainer">
                                    </div>
                                    <div class="col-sm-10 col-sm-offset-2 checkbox" id="taskRecordTemplateContainer">
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-sm-2 control-label no-padding-right">直播时间</label>
                                    <div class="col-sm-10" id="taskTimeContainer">
                                      <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-blue2 active border-radius-l-5" id="taskTypeOneTime">
                                          <input type="radio" name="taskTimeType" value="1" autocomplete="off" checked>一次性
                                        </label>
                                        <label class="btn btn-blue2" id="taskTypeLoop">
                                          <input type="radio" name="taskTimeType" value="2" autocomplete="off">定时循环
                                        </label>
                                        <label class="btn btn-blue2 border-radius-r-5" id="taskTypeAllDay">
                                          <input type="radio" name="taskTimeType" value="3" autocomplete="off">全天
                                        </label>
                                      </div>
                                      <!--一次性任务-->
                                      <div class="tab-pane fade active in well" id="oneTimePanel">
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">开始时间</label>
                                          <div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd hh:ii" id="oneTimeStartTime">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                                          </div>
                                        </div>
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">结束时间</label>
                                          <div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd hh:ii" id="oneTimeStopTime">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                                          </div>
                                        </div>
                                        <!--
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right"></label>
                                          <div class="checkbox">
                                            <label><input type="checkbox" class="ace" id="cb-auto-control"><span class="lbl">自动启停</span></label>
                                          </div>
                                        </div>
                                        //-->
                                      </div>
                                      <!--定时重复任务-->
                                      <div class="tab-pane fade well" id="loopPanel">
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">直播时间</label>
                                          <div style="display:inline-block;padding:0" class="col-sm-3">
                                          <div class="input-group date form_datetime" data-date-format="hh:ii" id="loopStartTime" style="margin:0">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                          </div>
                                          </div>
                                          <span style="float:left;line-height:30px;margin: 0 8px 0 8px">至</span>
                                          <div style="display:inline-block;padding:0" class="col-sm-3">
                                          <div class="input-group date form_datetime" data-date-format="hh:ii" id="loopEndTime">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                          </div>
                                          </div>
                                        </div>
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">开始日期</label>
                                          <div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd" id="loopStartDate">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                          </div>
                                        </div>
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">结束日期</label>
                                          <div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd" id="loopEndDate">
                                            <input class="form-control" size="16" type="text" value="">
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                          </div>
                                        </div>
                                        <div class="form-group">
                                          <label class="col-sm-3 control-label no-padding-right">执行</label>
                                          <div class="checkbox">
                                            <label><input type="checkbox" class="ace" name="loopDay" value="1"><span class="lbl">周一</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="2"><span class="lbl">周二</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="3"><span class="lbl">周三</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="4"><span class="lbl">周四</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="5"><span class="lbl">周五</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="6"><span class="lbl" style="color:lightgray">周六</span></label>
                                            <label><input type="checkbox" class="ace" name="loopDay" value="0"><span class="lbl" style="color:lightgray">周日</span></label>                                          </div>
                                        </div>
                                      </div>
                                      <!--全天任务-->
                                      <div class="tab-pane fade well" id="allDayPanel">
                                        全天任务没有属性，起来就行了
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                              </div>
                            </form>
                          </div>
                        </div>
                      </div>
                    </form>
                  </div>
                </div>
                <div id="tabStepTwo" class="tab-pane fade ">
                  <form class="form-horizontal">
                    <div class="row page-content-main">
                      <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right filed-need">标题</label>

                        <div class="col-sm-5">
                          <input type="text" class="col-sm-10" id="liveTitle" maxlength="64" style="display:inline-block">
                          <a href="#" onclick="showRegInstruction();return false;" id="btn-reg-instruction" style="display:none;line-height:30px;margin-left:12px">规则</a>
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">主讲</label>

                        <div class="col-sm-5">
                          <input type="text" class="col-sm-10" id="liveActor" maxlength="64">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right filed-need">海报</label>

                        <div class="col-sm-9">
                          <div class="col-sm-5 no-padding">
                            <div class="file-thumb" id="livePoster"><i></i></div>
                            <input type="file" id="id-input-file-poster"/>
                          </div>
                        </div>
                      </div>
                      <div class="form-group">
                      <label class="col-sm-2 control-label no-padding-right">简介</label>

                      <div class="col-sm-10">
                        <textarea class="autosize-transition form-control" style="width: 41.666%"
                                  id="liveIntro"></textarea>
                      </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2"></label>
                        <div class="checkbox">
                          <label><input type="checkbox" class="ace" id="liveShowForecast"><span class="lbl">显示预告</span></label>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
                <div id="tabStepThree" class="tab-pane fade ">
                  <form class="form-horizontal">
                    <div class="row page-content-main">
                      <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right filed-need">栏目</label>
                        <div id="live_channel" class="tree">

                        </div>
                      </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right filed-need">用户类型</label>
                        <div class="control-group control-group-inline" id="user-type-container">

                        </div>
                      </div>
                      <div class="form-group">
                        <label class="col-sm-2"></label>

                        <div class="checkbox" style="display:none">
                          <label><input type="checkbox" class="ace" id="liveRecord"><span class="lbl">启动录制</span></label>
                        </div>
                        <div class="tjsd col-md-offset-2" style="display:none">
                          录制栏目：直播结束后，录制文件会自动归入所属频道，根据频道设置进入上线或待审状态。使用和直播相同的栏目设置，保持录制栏目设置为空；如录制归入不同栏目，请重新
                          <button class="btn btn-lightwhite " id="btn_record_channel"  type="button">设置</button>
                        </div>

                      </div>
                    </div>
                  </form>
                </div>
              </div>
              <!-- /.page-content-area -->
            </div>
            <div class="space-6"></div>
            <div class="row">
              <div class="col-md-4">
                <button class="btn btn-blue" id="btn-next-step" type="button" style="margin-right:30px;">下一步</button>
                <a href="#" onclick="previewStep();return false;" id="btn-pre-step" style="display:none">上一步</a>
              </div>
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
      <!--任务详情modal-->
      <div class="modal fade modal-class" id="taskDetailModal" tabindex="-1" role="dialog" aria-labelledby="new_channel_title" aria-hidden="true">
        <div class="modal-dialog" style="position: absolute;top: 50%;left: 50%;width: 720px;height: 400px;margin: -200px 0 0 -360px; display:inline-block">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="new_channel_title">任务详情</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">
                <div id="detailContainer"></div>
                <div class="row">
                  <div class="col-md-4 col-md-offset-2">
                    <button class="btn btn-blue btn-big2 margin0" type="button" id="synchroTask">
                  同步
                </button>
                    </div>
                  </div>
              </form>
            </div>
            <div class="modal-footer" style="display: none;">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
          </div>
        </div>
      </div>
      <!--modal end-->
      <!--录制栏目设置Modal-->
      <div class="modal fade modal-class" id="recordChannelModal" tabindex="-1" role="dialog" aria-labelledby="recordChannelTitle" aria-hidden="true">
        <div class="modal-dialog" style="position: absolute;top: 50%;left: 50%;width: 400px;height: 300px;margin: -150px 0 0 -200px; display:inline-block">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="recordChannelTitle">录制栏目</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">
                <div id="record_channel" class="tree" style="min-height: 240px">

                </div>
                <button class="btn btn-blue btn-big2 margin0" type="button" id="liveRecordOk">
                  确定
                </button>
              </form>
            </div>
            <div class="modal-footer" style="display: none;">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
          </div>
        </div>
      </div>
      <!--录制modal结束-->
      <!--名称规则modal说明-->
      <div class="modal fade modal-class" id="instructionModal" tabindex="-1" role="dialog" aria-labelledby="instructionTitle" aria-hidden="true">
        <div class="modal-dialog" style="position: absolute;top: 50%;left: 50%;width: 400px;height: 300px;margin: -200px 0 0 -200px; display:inline-block">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="instructionTitle">规则说明</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">
                <div style="min-height: 240px;line-height: 30px">
                    <ul>
                      <li>直播的名称自动匹配只有在定时循环任务执行时才会生效；</li>
                      <li>{YYYY} 在直播启动时会替换为4位年份，如2009年；</li>
                      <li>{MM} 在直播启动时会替换为2位月份，如04月；</li>
                      <li>{DD} 在直播启动时会替换为2位日，如28日；</li>
                      <li>{E} 在直播启动时会替换为周几，如周一。</li>
                    </ul>
                  <span style="margin-left:20px;display:block">
                  例：<i>{YYYY}年{MM}月{DD}日 新闻联播</i>，在2015-3-27直播启动时会自动命名为“<b>2015年03月27日 新闻联播</b>”；<br/>
                  <i>{E}{MM}.{DD}《走进伪科学》</i>，在2015-3-27直播启动时会自动命名为“<b>周五03.27《走进伪科学》</b>”。
                    </span>
                </div>
                <button class="btn btn-blue btn-big2 margin0" type="button" id="instructionOK">
                  确定
                </button>
              </form>
            </div>
            <div class="modal-footer" style="display: none;">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
          </div>
        </div>
      </div>
      <!--名称规则modal结束-->

      <%@include file="../inc/displayFooter.jsp" %>
      <script src="../scripts/channel.min.js?v=1.423"></script>
      <script src="../scripts/fuelux/fuelux.tree.sel.min.js?v=0.428"></script>     <!-- inline scripts related to this page -->
      <script src="../scripts/moment.min.js"></script>
      <script src="../scripts/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
      <script src="../scripts/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
      <script src="../scripts/tmpl.min.js"></script>
      <script src="../scripts/load-image.all.min.js"></script>
      <script src="../scripts/liveUtils.js"></script>
      <script type="text/x-tmpl" id="tmpl-task-header">
        <tr><td><input type="radio" name="selectTask" value="{%=o.id%}"/></td><td>{%=o.title%}
      </script>
      <script type="text/x-tmpl" id="tmpl-task-tail">
        </td><td><a class="btn btn-xs" data-rel="tooltip" data-original-title="查看详情" data-placement="bottom" href="#"
        onclick="showTaskDetail({%=o.id%});return false;"><i class="ace-icon fa fa-info bigger-110 icon-only"></i></a></td></tr>
      </script>
      <script type="text/x-tmpl" id="tmpl-template">
        <label style="display:inline-block;width:240px">
        <input type="checkbox" class="ace" name="taskTemplates" value="{%=o.id%}" dataType="{%=o.type%}">
        <span class="lbl">{%=o.title%}</span></label>
      </script>
      <script type="text/x-tmpl" id="tmpl-template-record">
        <label style="display:inline-block;width:240px">
        <input type="checkbox" class="ace" name="taskTemplates" value="{%=o.id%}" dataType="{%=o.type%}">
        <span class="lbl" style="color:#6fb3e0">{%=o.title%}</span></label>
      </script>
      <script type="text/x-tmpl" id="tmpl-user-type">
        <div class="checkbox"><label><input type="checkbox" class="ace" name="cb-user-type" checked="checked" value="{%=o.id%}"><span class="lbl">{%=o.name%}</span></label></div>
      </script>
      <script type="text/x-tmpl" id="tmpl-template-detail">
        <div>码流：({%=o.vbandwidth%} + {%=o.abandwidth%}) kbps</br>视频编码：{%=o.vcodec%}</br>音频编码：{%=o.acodec%}</br>视频尺寸：{%=o.size%}</div>
      </script>
      <script type="text/x-tmpl" id="tmpl-mark-live">
        <i class="text-info ace-icon fa fa-rss icon-only"></i>
      </script>
      <script type="text/x-tmpl" id="tmpl-mark-record">
        <i class="text-warning ace-icon fa fa-microphone icon-only"></i>
      </script>

      <script type="text/javascript">
        var global_step = 1;
        var STEP_TITLE_FIRST = "1.直播任务（对应统一转码平台的任务）",
                STEP_TITLE_SECOND = "2.直播描述",
                STEP_TITLE_THIRD = "3.直播栏目";
        var _TREE_NODE_PREFIX = 'NODE_',
                __channels = "",
                __grantChannels = "",
                channelUtils,
                _isRoot = <s:if test="#session.sessionOperator.isRoot">true;</s:if><s:else>false;</s:else>
        var _newTaskInfo = null;  // 缓存新建任务信息，防止查看现有任务时丢失


        jQuery(function ($) {
          $('[data-rel=tooltip]').tooltip();
          $('[data-rel=popover]').popover({html: true});

          // 新建时隐藏切换栏，做成向导样子，编辑时显示tab
          liveUtils.itemId = $.getQuery("id", -1);
          liveUtils.dataInitProgress = 0;
          if(parseInt(liveUtils.itemId) > 0){
            $("#myTab").show();
          }else{
            $("#myTab").hide();
            $("#liveRecord").removeAttr("checked").removeAttr("disabled");
          }

          $("#oneTimeStartTime input").val(moment().locale("zh-Cn").format("YYYY-MM-DD HH:mm"));
          $("#oneTimeStopTime input").val(moment().locale("zh-Cn").add(3,'hour').format("YYYY-MM-DD HH:mm"));
          $('#oneTimeStartTime, #oneTimeStopTime').datetimepicker({
            language:  'zh-CN',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0
          });

          $("#loopStartTime input").val("19:00");
          $("#loopEndTime input").val("19:30");
          $('#loopStartTime, #loopEndTime').datetimepicker({
            language:  'zh-CN',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 1,
            minView: 0,
            maxView: 1,
            forceParse: 0
          });

          $("#loopStartDate input").val(moment().locale("zh-Cn").format("YYYY-MM-DD"));
          $("#loopEndDate input").val(moment().locale("zh-Cn").add(1,'year').format("YYYY-MM-DD"));
          $('#loopStartDate, #loopEndDate').datetimepicker({
            language:  'zh-CN',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0
          });

          $("#loopPanel, #allDayPanel").hide();
          // 时间类型切换
          $("#taskTypeOneTime").click(function(){
            $("#loopPanel, #allDayPanel").removeClass("active").removeClass("in").hide();
            $("#oneTimePanel").addClass("active").addClass("in").show();
            $("#liveRecord").removeAttr("disabled");
            $("#btn-reg-instruction").hide();
            if($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0){
              $(".tjsd").show();
            }else{
              $(".tjsd").hide();
            }
            $("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']").removeAttr("disabled");
          });
          $("#taskTypeLoop").click(function(){
            $("#oneTimePanel, #allDayPanel").removeClass("active").removeClass("in").hide();
            $("#loopPanel").addClass("active").addClass("in").show();
            $("#liveRecord").removeAttr("disabled");
            $("#btn-reg-instruction").show();
            if($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0){
              $(".tjsd").show();
            }else{
              $(".tjsd").hide();
            }
            $("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']").removeAttr("disabled");
          });
          $("#taskTypeAllDay").click(function(){
            $("#allDayPanel").addClass("active").addClass("in").show();
            $("#loopPanel, #oneTimePanel").removeClass("active").removeClass("in").hide();
            // 禁用录制
            $("#liveRecord").removeAttr("checked").attr("disabled", "disabled");
            $(".tjsd").hide();
            // 取消文件保存选项
            $("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']").prop("checked",false).attr("disabled", "disabled");
            $("#btn-reg-instruction").hide();
          });

          $('#id-input-file-poster').ace_file_input({
            no_file:'未选择文件 ...',
            btn_choose:'选择',
            btn_change:'选择',
            droppable:false,
            onchange:null,
            thumbnail:true,
            whitelist:'png',
            //blacklist:'exe|php'
            before_change:function(files, dropped) {
              if (files.length > 0) {
                var file = files[0];

                if (typeof file === 'string') {
                } else if ('File' in window && file instanceof window.File) {
                  displayLocalImage(this,{
                    maxWidth: 480,
                    maxHeight: 270,
                    canvas: true
                  }, $("#livePoster"));
                }
              }
              return true;
            }
          });

          $("#btn-next-step").click(function(){
            nextStep();
          });

          $("#liveRecord").click(function(){
            if($(this).prop('checked')){ $(".tjsd").show();}else{$(".tjsd").hide();}
          });

          // 设置录制栏目
          $("#btn_record_channel").click(function(){
            $("#recordChannelModal").modal("show");
          });

          // 内容变化后，清除可能的错误样式
          $("#taskName, #taskSource, #loopStartTime input,#loopEndTime input, #loopStartDate input," +
          "#loopEndDate input, #liveTitle, #id-input-file-poster").change(function(){
            $(this).closest("div.form-group").removeClass("has-error");
          });
          $("#taskName, #taskSource, #loopStartTime input,#loopEndTime input, #loopStartDate input," +
          "#loopEndDate input, #liveTitle, #id-input-file-poster").keypress(function() {
            $(this).closest("div.form-group").removeClass("has-error");
          });

          $(document).ajaxStart(function(){
            $("#loading_container").show();
          });

          $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
          });

          // 获取信号源列表和配置列表
          liveUtils.getSourceList(showSourceList, retrieveDataError);
          liveUtils.getTemplateList(showTemplateList, retrieveDataError);

          loadChannel();
          loadUserType();

          $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
              if( $("#filter-startTime").is(":focus") || $("#filter-endTime").is(":focus")){
                event.preventDefault();
                //statUtils.getItem();
              }
            }
          });

          $("#synchroTask").click(function(){
            synchronizeTask();
          });

          $("#instructionOK").click(function(){
            $("#instructionModal").modal("hide");
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


        // 同步任务
        function synchronizeTask(){
          var task = gatherTaskInfo();
          task.id = liveUtils.taskId;

          // 找出原来设置的IP和streamName
          var t = liveUtils.getTaskById(task.id);
          task.serverIp = t.serverIp;
          task.streamName = t.streamName;
          task.filePath = t.filePath;
          // 同步任务
          //var recordChecked = !$("#liveRecord").is(":disabled") && $("#liveRecord").prop("checked");
          var recordChecked = ($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0);
          liveUtils.saveTask(task, synchronizeSucceed, retrieveDataError,
                  recordChecked ? liveUtils.TASK_MODE_LIVERECORD:liveUtils.TASK_MODE_LIVE );
        }

        // 同步成功
        function synchronizeSucceed(taskId){
          $("#taskDetailModal").modal("hide");
            reloadThisPage();
        }

        // 新建任务保存
        function createNewTask(){
          var task = gatherTaskInfo();

          console.info("gather data end time:" + task.time.endTime );
          // 保存任务
          //var recordChecked = !$("#liveRecord").is(":disabled") && $("#liveRecord").prop("checked");
          var recordChecked = ($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0);
          liveUtils.saveTask(task, createTaskSucceed, retrieveDataError,
                  recordChecked? liveUtils.TASK_MODE_LIVERECORD:liveUtils.TASK_MODE_LIVE);
        }

        // 新建任务成功回调
        function createTaskSucceed(taskId){
          // 调用保存其他信息
          liveUtils.taskId = taskId;
          liveUtils.saveLive(serializeLive(),liveSaveSucceed, retrieveDataError);
        }

        /*
        * 显示直播信息
        * */
        function showLiveInfo(live){
          if(!live) return;

          liveUtils.itemId = live.id;
          liveUtils.taskId = live.taskId;
          $("#liveTitle").val(live.title);
          $("#cb-auto-control").prop("checked", live.autoControl);
          $("#liveActor").val(live.actor);
          $("#liveIntro").val(live.intro);
          $("#liveShowForecast").prop("checked", live.foreshow);
          $("#liveRecord").prop("checked", live.record);
          // 海报
          //var content = $('<span>').append($('<img>').attr('src', live.poster));
          //$("#livePoster").children().replaceWith(content);
          displayImageURL(live.poster,{
            maxWidth: 480,
            maxHeight: 270,
            canvas: true
          }, $("#livePoster"));
          // 用户类型
          $( "input[name='cb-user-type']").removeAttr("checked");
          for(var i=0; i<live.userTypes.length; i++){
            $( "input[name=cb-user-type][value='" + live.userTypes[i] + "']").prop("checked", true).attr("checked", "checked");
          }
          // 选中直播所属的栏目和录制栏目
          show_tree($("#live_channel"), live.channels);
          show_tree($("#record_channel"), live.recordChannels);
          // 选中对应的任务，如果任务不在第一页，转码请求任务详细信息
          $("#tabExistTask").trigger("click");
          liveUtils.getTask(live.taskId, checkInitTask);
        }

        // 显示并选中该任务
        function checkInitTask(task){
          if(!task) return null;

          // 检查是否已经存在，如果存在，选中并触发选中事件，否则添加到最上面
          liveUtils.addTask(task);
          var reg = "input[name='selectTask'][value='" + task.id + "']";
          if($(reg).length >0){
            $(reg).prop("checked", true).attr("checked", "checked").trigger("click");
          }else{
            var s = tmpl("tmpl-task-header", task);
            if(task.isLive()){
              s += tmpl("tmpl-mark-live", task);
            }
            if(task.isRecord()){
              s += tmpl("tmpl-mark-record", task);
            }
            s+= tmpl("tmpl-task-tail", task);
            $("#taskTable tbody").prepend(s);
            $(reg).prop("checked", true).attr("checked", "checked");
            liveUtils.task = task;
          }
          if(task.time.type == liveUtils.TASK_TYPE_ALL_DAY){
            $("#liveRecord").attr("disabled", "disabled");
          }else{
            $("#liveRecord").removeAttr("disabled");
          }
        }

        /*显示现有任务*/
        function showExistTask(){
          _newTaskInfo = gatherTaskInfo();
          $("#task-new").removeClass("active").removeClass("in");
          $("#task-synch").addClass("active").addClass("in");
          if(liveUtils.task_page_index < 0){
            liveUtils.getTaskList(1, "", showTasks, retrieveDataError);
          }
        }

        // 显示新建任务Panel
        function showNewTask(){
          restoreTask(_newTaskInfo);
          $("#newTaskDetailContainer").append($("#taskDetail"));
          $("#task-synch").removeClass("active").removeClass("in");
          $("#task-new").addClass("active").addClass("in");
        }

        // 显示源列表
        function showSourceList(sourceList){
          if(!sourceList) return;

          var sel = $("#taskSource");
          sel.empty();
          for(var i=0; i<sourceList.length; i++) {
            sel.append($("<option/>", {
              value: sourceList[i].id,
              text: sourceList[i].title
            }));
          }
          liveUtils.dataInitProgress++;
          // 可以加载直播
          if( liveUtils.dataInitProgress >= liveUtils.LIVE_INIT_MIN_PROGRESS &&
                  liveUtils.itemId > 0){
            console.info("progress:" + liveUtils.dataInitProgress);
            liveUtils.loadLive(liveUtils.itemId, showLiveInfo);
          }
        }

        // 显示配置选项
        function showTemplateList(templateList){
          if(!templateList) return;

          var panel = $("#taskTemplateContainer"), i;
          panel.html("");
          // 直播配置
          for(i=0; i<templateList.length; i++){
            //if(templateList[i].type == "file") continue;
            if(templateList[i].type != "ts_over_http") continue;
            panel.append(
                    tmpl( "tmpl-template" ,templateList[i]));
          }
          // 录制文件
          for(i=0; i<templateList.length; i++){
            if(templateList[i].type != "file") continue;
            $("#taskRecordTemplateContainer").append(
                    tmpl( "tmpl-template-record" ,templateList[i]));
          }
          bindEvent();
          // 可以加载直播
          liveUtils.dataInitProgress++;
          if( liveUtils.dataInitProgress >= liveUtils.LIVE_INIT_MIN_PROGRESS &&
                  liveUtils.itemId > 0){
            console.info("progress:" + liveUtils.dataInitProgress);
            liveUtils.loadLive(liveUtils.itemId, showLiveInfo);
          }
        }

        // 显示任务数据，taskList task数组，totalCount总个数
        function showTasks(taskList, totalCount){
          var tab = $("#taskTable tbody"), s, t;
          //tab.empty();
          // 改为清除未选中的任务
          tab.find("input[name='selectTask']:not(:checked)").closest("tr").remove();
          for(var i=0; i<taskList.length; i++) {
            // 检查是否已经存在
            t = taskList[i];
            if(tab.find("input[name='selectTask'][value='" + taskList[i].id + "']").length == 0) {
              s = tmpl("tmpl-task-header", t);
              if(t.isLive()){
                s += tmpl("tmpl-mark-live", t);
              }
              if(t.isRecord()){
                s += tmpl("tmpl-mark-record", t);
              }
              s+= tmpl("tmpl-task-tail", t);
              tab.append(s);
            }
          }
          $('[data-rel=tooltip]').tooltip();
          $("input[name='selectTask']").click(function(){
            liveUtils.task = liveUtils.getTaskById($(this).val());
            console.info("task:" + liveUtils.task.title);
            // 检查task类型，如果是全天类型，禁用录制
            if( liveUtils.task.time.type == liveUtils.TASK_TYPE_ALL_DAY ||
              !liveUtils.task.isRecord()){
              $("#liveRecord").attr("disabled", "disabled");
              $(".tjsd").hide();
            }else{
              $("#liveRecord").removeAttr("disabled");
              $(".tjsd").show();
            }
          });

          rebuild_page_nav($("#task-page-nav"),
                  Math.ceil(totalCount / liveUtils.TASK_PAGE_SIZE),
                  liveUtils.task_page_index, "to_page",
                  totalCount);
        }
        // 从统一转码取数据失败
        function retrieveDataError(message){
          alert(message);
            reloadThisPage();
        }

        /*跳至指定页码的任务列表*/
        function to_page(index) {
          if(parseInt(index) > 0 && parseInt(index) <= liveUtils.task_page_count){
            liveUtils.getTaskList(index, "", showTasks, retrieveDataError);
          }
        }

        // 显示已有任务详情
        function showTaskDetail(id){
          var task = liveUtils.getTaskById(id), i;

          if(!task){
            alert("没有找到对应的任务信息！"); return;
          }
          // 把详情窗口移动到modal中
          $("#detailContainer").append($("#taskDetail"));
          $("#taskName").val(task.title);
          $("#taskSource").val(liveUtils.getSourceIdByURL(task.source));
          task.template = task.template || [];
          $("input[name=taskTemplates]").removeAttr("checked");
          for(i=0; i<task.template.length; i++){
            $("input[name=taskTemplates][value='" + task.template[i].templateId + "']").prop("checked", "checked");
          }

          switch(parseInt(task.time.type)){
            case liveUtils.TASK_TYPE_ONE_TIME:
                    // 把日期和时间拼起来
                    console.info(moment(task.time.startTime).locale("zh-Cn").format("HH:mm") + "   " + task.time.startTime);
                    var s = moment(task.time.startDate).locale("zh-Cn").format("YYYY-MM-DD") + " " +
                            moment(task.time.startTime).locale("zh-Cn").format("HH:mm"),
                         e = moment(task.time.endDate).locale("zh-Cn").format("YYYY-MM-DD") + " " +
                                 moment(task.time.endTime).locale("zh-Cn").format("HH:mm");
              $("#oneTimeStartTime input").val(s);
              $("#oneTimeStopTime input").val(e);
              $("#taskTypeOneTime").trigger("click");
              break;
            case liveUtils.TASK_TYPE_LOOP:
              $("#loopStartDate input").val(moment(task.time.startDate).locale("zh-Cn").format("YYYY-MM-DD"));
              $("#loopEndDate input").val(moment(task.time.endDate).locale("zh-Cn").format("YYYY-MM-DD"));
              $("#loopStartTime input").val(moment(task.time.startTime).locale("zh-Cn").format("HH:mm"));
              $("#loopEndTime input").val(moment(task.time.endTime).locale("zh-Cn").format("HH:mm"));
              // 重复周次
              var weekDay;
              for(i=0; i<7; i++){
                weekDay = $("input[name=loopDay][value='" + i + "']");
                if(task.time.weekDayActive(i)){
                  weekDay.attr("checked", "checked");
                }else{
                  weekDay.removeAttr("checked");
                }
              }
              $("#taskTypeLoop").trigger("click");
              break;
            case liveUtils.TASK_TYPE_ALL_DAY:
              $("#taskTypeAllDay").trigger("click");
              break;
          }

          $("#taskDetailModal").modal("show");
        }

        // 切换步骤/保存
        function nextStep(){
          if(global_step < 3){
            moveToStep(global_step+1);
          }else{
            doSave();
          }
        }

        // 上一步
        function previewStep(){
          if(global_step > 1) moveToStep(global_step-1);
        }

        // 跳至第几步
        function moveToStep(step){
          if(step < 1 || step > 3 ) return;

          global_step = step;
          var panel1st = $("#tabStepOne"), panel2nd = $("#tabStepTwo"), panel3rd = $("#tabStepThree"),
                  btn_next = $("#btn-next-step"), btn_pre = $("#btn-pre-step"), page_title = $("#stepTitle");
          $("#myTab li").removeClass("active");
          switch(step){
            case 1:
              panel2nd.removeClass("active").removeClass("in").hide();
              panel3rd.removeClass("active").removeClass("in").hide();
              panel1st.addClass("active").addClass("in").show();
              btn_pre.hide();
              btn_next.text("下一步");
              // 如果是新建任务，焦点放到标题
              if($("#task-new").hasClass("active")){
                $("#taskName").focus();
              }
              page_title.html(STEP_TITLE_FIRST);
              $("#myTab li").eq(0).addClass("active");
              break;
            case 2:
              panel1st.removeClass("active").removeClass("in").hide();
              panel3rd.removeClass("active").removeClass("in").hide();
              panel2nd.addClass("active").addClass("in").show();
              btn_pre.show();
              $("#liveTitle").focus();
              btn_next.text("下一步");
              page_title.html(STEP_TITLE_SECOND);
              $("#myTab li").eq(1).addClass("active");
              break;
            case 3:
              panel2nd.removeClass("active").removeClass("in").hide();
              panel1st.removeClass("active").removeClass("in").hide();
              panel3rd.addClass("active").addClass("in").show();
              btn_pre.show();
              btn_next.text("保存");
              page_title.html(STEP_TITLE_THIRD);
              $("#myTab li").eq(2).addClass("active");
          }
        }

        // 执行保存操作
        function doSave(){
          if(checkInput()){
            console.info("直播信息检查已经通过，正在准备保存");
            console.info("serialize task");
            if($("#task-new").hasClass("active")){
              console.info("首先将新建任务请求提交给统一转码！");
              createNewTask();
            }else{
              liveUtils.taskId = $("input[name='selectTask']:checked").eq(0).val();
              // 同步任务，确保录制信息一致
              var task = liveUtils.getTaskById(liveUtils.taskId);
              var recordChecked = !$("#liveRecord").is(":disabled") && $("#liveRecord").prop("checked");
              liveUtils.saveTask(task, createTaskSucceed, retrieveDataError,
                      recordChecked? liveUtils.TASK_MODE_LIVERECORD:liveUtils.TASK_MODE_LIVE );
              //liveUtils.saveLive(serializeLive(),liveSaveSucceed, retrieveDataError);
            }
          }
        }

        // 返回列表页面
        function liveSaveSucceed(){
          location.href = "live.jsp";
        }

        // 序列化直播信息，在选择已有任务时，由保存按钮点击触发
        // 在新建任务时，由新建成功事件触发
        function serializeLive(){
          var userTypeStr = ",";
          $( "input[name='cb-user-type']:checked" ).each(function(i){
            //userTypeValues.push( $(this).val());
            userTypeStr += $(this).val() + ",";
          });
          var poster = $("#livePoster").find('img, canvas'),
                  posterData = (poster && ($.trim($("#id-input-file-poster").val()) != "" || liveUtils.itemId>0))? poster.get(0).toDataURL(): null,
                  _needRecord = ($("input[name='taskTimeType']:checked").val() != liveUtils.TASK_TYPE_ALL_DAY) && ($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0);

          return {"id" : liveUtils.itemId,
            "title": $("#liveTitle").val(),
            "taskId": liveUtils.taskId,
            "autoControl": $("#cb-auto-control").prop("checked"),
            "type": $("input[name='taskTimeType']:checked").val(),
            "actor": $("#liveActor").val(),
            "poster": posterData,
            "intro": $("#liveIntro").val(),
            "foreshow": $("#liveShowForecast").prop("checked"),
            "serializedChannel": $('#live_channel').tree('selectedItems'),
            "userTypes": userTypeStr,
            "needRecord":_needRecord,
            "recordChannel":$('#record_channel').tree('selectedItems')
          }
        }

        /* 检查输入
        * 输入完整 返回true
        */
        function checkInput(){
          var missed = false;

          // 首先检查任务
          //console.info($("#task-synch").attr("class") + "  fuck:" + $("input[name='selectTask']:checked").length);
          if($("#task-synch").hasClass("active")){
            // 选择了已有任务
            if($("input[name='selectTask']:checked").length ==0) {
              moveToStep(1);
              alert("请选择直播关联的任务！");
              return false;
            }else{
              // 检查选择的任务是否包含直播配置
              if(!liveUtils.task || !liveUtils.task.isLive()){
                moveToStep(1);
                alert("选择的任务不支持直播，请选择带直播标示的任务或新建任务。");
                return false;
              }
            }
          }else{
            // 检查新建任务是否输入完整
            var textInputList = [$("#taskName")],
                    checkboxList = [$("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_LIVE +"']")];
            var taskType = parseInt($("input[name=taskTimeType]:checked").eq(0).val());
            if(taskType == liveUtils.TASK_TYPE_LOOP){
              // 必须设定开始时间、结束时间、开始日期和执行星期
              checkboxList.push($("input[name='loopDay']"));
              textInputList.push($("#loopStartTime input"));
              textInputList.push($("#loopEndTime input"));
              textInputList.push($("#loopStartDate input"));
              textInputList.push($("#loopEndDate input"));
            }

            missed = textHasEmptyItem(textInputList) || checkboxHasEmptyItem(checkboxList);
            if(missed ){
              moveToStep(1);
              return false;
            }
          }

          //return;
          // 直播描述信息
          missed = (liveUtils.itemId >0 )? textHasEmptyItem([$("#liveTitle")]) : textHasEmptyItem([$("#liveTitle"), $("#id-input-file-poster")]);
          if(missed){
            moveToStep(2);
            return false;
          }

          // 栏目和用户类型
          var liveChannels = $('#live_channel').tree('selectedItems');
          missed = (liveChannels.length == 0);
          if(missed){
            $('#live_channel').closest("div.form-group").addClass("has-error");
          }
          missed = missed || checkboxHasEmptyItem([$("input[name='cb-user-type']")]);
          if(missed){
            moveToStep(3);
            return false;
          }

          return true;
        }

        // 检查list指定的输入控件内容是否为空，如果为空标记为错误，并返回true
        function textHasEmptyItem(list){
          if(!list) return false;

          var hasEmpty = false;
          for(var i=0; i<list.length; i++){
            if(list[i].val() == ""){
              hasEmpty = true;
              list[i].closest("div.form-group").addClass("has-error");
            }
          }
          return hasEmpty;
        }
        // 检查checkbox是否没有勾选内容
        function checkboxHasEmptyItem(list){
          if(!list) return false;

          var hasEmpty = false;
          for(var i=0; i<list.length; i++){
            //console.info("checkbox checked length:" + list[i].parent().find(":checked").length);
            if(list[i].parent().find(":checked").length == 0){
              hasEmpty = true;
              list[i].closest("div.form-group").addClass("has-error");
            }
          }
          return hasEmpty;
        }

        // 收集新建任务信息，暂存
        function gatherTaskInfo(){
          // 配置信息
          var templates = [];
          $( "input[name='taskTemplates']:checked" ).each(function(i){
            templates.push( new TaskTemplate(1, $(this).val(), "", ""));
          });
          // 时间设置
          var weekDay = "";
          for(var i=0; i<7; i++){
            weekDay += ((i==0)? "" : ",");
            weekDay += $("input[name=loopDay][value='" + i + "']").eq(0).prop("checked")? "1" : "0";
          }
          var taskType = parseInt($("input[name=taskTimeType]:checked").eq(0).val()),
                  taskTime = new TaskTime(taskType,
                          taskType == 1? $("#oneTimeStartTime input").val():$("#loopStartDate input").val(),
                          taskType == 1? $("#oneTimeStopTime input").val():$("#loopEndDate input").val(),
                          taskType == 1? $("#oneTimeStartTime input").val():$("#loopStartTime input").val(),
                          taskType == 1? $("#oneTimeStopTime input").val():$("#loopEndTime input").val(),weekDay);

          return new Task(-1, $("#taskName").val(), $("#taskSource").val(), taskTime, templates, "");
        }

        // 将taskInfo恢复到新建任务信息区
        function restoreTask(taskInfo){
          if(!taskInfo) return;
          $("#taskName").val(taskInfo.title);
          $("#taskSource").val(taskInfo.source);
          taskInfo.template = taskInfo.template || [];
          $("input[name=taskTemplates]").removeAttr("checked");
          for(i=0; i<taskInfo.template.length; i++){
            $("input[name=taskTemplates][value='" + taskInfo.template[i].templateId + "']").prop("checked", "checked");
          }
          switch(parseInt(taskInfo.time.type)){
            case liveUtils.TASK_TYPE_ONE_TIME:
              $("#oneTimeStartTime input").val(taskInfo.time.startDate);
              $("#oneTimeStopTime input").val(taskInfo.time.endDate);
              $("#taskTypeOneTime").trigger("click");
              break;
            case liveUtils.TASK_TYPE_LOOP:
              $("#loopStartDate input").val(taskInfo.time.startDate);
              $("#loopEndDate input").val(taskInfo.time.endDate);
              $("#loopStartTime input").val(taskInfo.time.startTime);
              $("#loopEndTime input").val(taskInfo.time.endTime);
              // 重复周次
              var weekDay;
              for(i=0; i<7; i++){
                weekDay = $("input[name=loopDay][value='" + i + "']");
                if(taskInfo.time.weekDayActive(i)){
                  weekDay.attr("checked", "checked");
                }else{
                  weekDay.removeAttr("checked");
                }
              }
              $("#taskTypeLoop").trigger("click");
              break;
            case liveUtils.TASK_TYPE_ALL_DAY:
              $("#taskTypeAllDay").trigger("click");
              break;
          }
        }

        // 显示本地图片文件
        /*
        * input - 文件选择输入框
        * options - 选项
        * panel - 显示的区域
        * */
        function displayLocalImage(input, options,panel) {
          if (input.files && input.files[0]) {
            var loadingImage = loadImage(
                    input.files[0],
                    function (img) {
                      var content;
                      if (!(img.src || img instanceof HTMLCanvasElement)) {
                        content = $('<span>加载图片失败</span>');
                      } else {
                        content = $('<span>').append(img).attr('href', img.src || img.toDataURL());
                      }
                      panel.children().replaceWith(content);
                    },
                    options
            );
            if (!loadingImage) {
              panel.children().replaceWith($('<span>您的浏览器不支持加载本地图片</span>'));
            }
          }
        }

        /**
        * @param url  要显示的图片url
        * @param options 配置
        * @param panel 要显示的未知
         */
        function displayImageURL(url, options,panel) {
          var loadingImage = loadImage(
                  url,
                  function (img) {
                    var content;
                    if (!(img.src || img instanceof HTMLCanvasElement)) {
                      content = $('<span>加载图片失败</span>');
                    } else {
                      content = $('<span>').append(img).attr('href', img.src || img.toDataURL());
                    }
                    panel.children().replaceWith(content);
                  },
                  options
          );
          if( !loadingImage ){
            panel.children().replaceWith($('<span>加载图片失败</span>'));
          }
        }

        // 装载所有栏目
        function loadChannel(){
          $.ajax({
            type: "POST",
            url: "/publish/channel!channelTree.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
              $("#channel-loading").hide();
              channelUtils = new ChannelUtils();
              channelUtils.initByJson(msg);
              loadGrantedChannel();
            }
          });
        }

        // 装载管理员管理范围
        function loadGrantedChannel(){
          $.ajax({
            type: "POST",
            url: "/security/admin!getGrantChannel.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg) {
              var json = eval("(function(){return " + msg + ";})()");
              __grantChannels = json.data["obj.serializedChannel"];
              if (liveUtils.itemId <= 0) {
                show_tree($("#live_channel"), []);
                show_tree($("#record_channel"), []);
              }
              liveUtils.dataInitProgress++;
              // 可以加载直播
              if( liveUtils.dataInitProgress >= liveUtils.LIVE_INIT_MIN_PROGRESS &&
                      liveUtils.itemId > 0){
                liveUtils.loadLive(liveUtils.itemId, showLiveInfo);
              }
            }
          });
        }

        // 显示栏目树
        function show_tree(panel, selected){
          var treeDataSource;
          if(!_isRoot){
            channelUtils.setGrantEnabled(true);
            channelUtils.setGrantChannel(__grantChannels);
          }
          if(selected && selected.length > 0){
            channelUtils.setSelectedChannel(selected);
          }
          treeDataSource = channelUtils.generateTreeData();

          panel.ace_tree({
            dataSource: treeDataSource ,
            multiSelect:true,
            loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon' : 'ace-icon tree-minus',
            'close-icon' : 'ace-icon tree-plus',

            'selected-icon' : 'ace-icon fa fa-check',
            'unselected-icon' : 'ace-icon',
            'selectable' : true
          });
        }

        // 装载用户类型
        function loadUserType(){
          $.ajax({
            type: "POST",
            url: "/user/userType!list.action",
            //dataType: "json",
            dataType: "text",
            //data: {name: ver},
            success: function(msg){
              $("#user-typer-loading").hide();
              var response = eval("(function(){return " + msg + ";})()");

              for(var i=0; i<response.objs.length; i++){
                var type = response.objs[i];
                if(type){
                  $("#user-type-container").append(tmpl("tmpl-user-type", type));
                }
              }
              bindEvent();
              liveUtils.dataInitProgress++;
              // 可以加载直播
              if( liveUtils.dataInitProgress >= liveUtils.LIVE_INIT_MIN_PROGRESS &&
                liveUtils.itemId > 0){
                console.info("progress:" + liveUtils.dataInitProgress);
                liveUtils.loadLive(liveUtils.itemId, showLiveInfo);
              }
            }
          });
        }

        // 显示名字自动匹配规则
        function showRegInstruction(){
          $("#instructionModal").modal("show");
        }

        // 为动态加载的组件增加事件
        function bindEvent(){
          $("input[name='taskTemplates'], input[name='cb-user-type'], input[name='loopDay']").click(function(){
            $(this).closest("div.form-group").removeClass("has-error");
          });
          //检查文件保存类型是否重复
          $("input[name='taskTemplates']").click(function(){
            if($(this).attr("dataType") == liveUtils.TEMPLATE_TYPE_RECORD &&
                    $("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 1){
              $(this).prop("checked", false);
            }
            if($("input[name='taskTemplates'][dataType='" + liveUtils.TEMPLATE_TYPE_RECORD +"']:checked").length > 0){
              $(".tjsd").show();
            }else{
              $(".tjsd").hide();
            }
          });
          // 显示tooltip
          /*
          $("input[name='taskTemplates']").tooltip({
            content:"...",
            open: function(evt, ui) {
              console.info("fuck");
              var id = $( this).val(), template = liveUtils.getTemplateById(id);
              if(template){
                $(this).tooltip('option', 'content', tmpl("tmpl-template-detail", template));
              }
            }
          });
          */
        }
      </script>
    </div>
  </div>
</body>
</html>

