<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/12/29
  Time: 10:09
  新建和编辑投票
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>投票编辑 - <%=IndividualUtils.getInstance().getName()%></title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp"%>
  <link rel="stylesheet" href="../style/datepicker.css"/>
  <style>
    .block__list {
      padding: 20px 0;
      max-width: 460px;
      margin-top: 0;
      margin-left: 5px;
      background-color: #efefef;
    }
    .block__list-title {
      margin: -20px 0 0;
      padding: 10px;
      text-align: center;
      background: #5F9EDF;
    }
    .block__list li { cursor: move; }

    .block__list_words li {
      background-color: #EFEFEF;
      padding: 10px 40px;
    }
    .block__list_words .sortable-ghost {
      opacity: 0.4;
      background-color: #F4E2C9;
    }

    .block__list_words li:first-letter {
      text-transform: uppercase;
    }

    .block__list_tags {
      padding-left: 30px;
    }

    .block__list_tags:after {
      clear: both;
      content: '';
      display: block;
    }
    .block__list_tags li {
      color: #fff;
      float: left;
      margin: 8px 20px 10px 0;
      padding: 5px 10px;
      min-width: 10px;
      background-color: #5F9EDF;
      text-align: center;
    }
    .block__list_tags li:first-child:first-letter {
      text-transform: uppercase;
    }



    #editable {background: #EFEBEF}
    #editable li {
      position: relative;
    }

    #editable{
      margin: 0;
      padding: 0;
      list-style: none;
    }

    #editable i {
      -webkit-transition: opacity .2s;
      transition: opacity .2s;
      opacity: 0;
      display: block;
      cursor: pointer;
      color: #c00;
      top: 10px;
      right: 40px;
      position: absolute;
      font-style: normal;
    }

    #editable li:hover i {
      opacity: 1;
    }

    #filter {}
    #filter button {
      color: #fff;
      width: 100%;
      border: none;
      outline: 0;
      opacity: .5;
      margin: 10px 0 0;
      transition: opacity .1s ease;
      cursor: pointer;
      background: #87b87f;
      padding: 10px 0;
      font-size: 1em;
    }
    #filter button:hover {
      opacity: 1;
    }

    #filter .block__list {
      padding-bottom: 0;
    }

    .maxOption{
      margin-left: 40px;
    }

    .maxOption select{width: 80px}
  </style>
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
        <li>系统管理</li><li class="active">编辑投票</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-ticket"></i>编辑投票
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">

        <div class="row page-content-main">
          <form role="form" class="form-horizontal" action="" method="POST" id="voteForm">

            <h4>请录入投票信息</h4>

            <div class="col-xs-12 no-padding">
              <hr>
              <div class="form-group">
                <label class="col-sm-1 control-label no-padding-right filed-need">标题</label>

                <div class="col-sm-11">
                  <input type="text" class="col-sm-6" id="vote-title">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label no-padding-right filed-need">时间</label>

                <div class="col-sm-11">
                  <input type="text" class="col-sm-4" id="start-time">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label no-padding-right filed-need">至</label>

                <div class="col-sm-11">
                  <input type="text" class="col-sm-4" id="end-time">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label no-padding-right filed-need">选项</label>

                <div class="col-sm-11">
                  <div class="container" style="padding-left: 0">
                    <div id="filter">
                      <div class="block__list block__list_words">
                        <div class="maxOption">最多可选<select id="max-option"><option value="1">1</option></select></div>
                        <ul id="editable">
                          <%--<li>苍井空<i class="js-remove">✖</i></li>
                          <li>柚木提娜<i class="js-remove">✖</i></li>
                          <li>松岛枫<i class="js-remove">✖</i></li>--%>
                        </ul>

                        <button id="addOption" type="button">增加选项（+/=）</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="space"></div>
              <button class="btn btn-blue" type="button" id="saveVote">完成</button>
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
  <%@include file="../inc/displayFooter.jsp"%>
  <div class="modal fade modal-class" id="optionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left:50%;margin: -80px 0 0 -200px; width:400px;">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
          <h4 class="modal-title" id="modalTitle">编辑选项</h4>
        </div>
        <div class="modal-body">
          <form class="form-inline">
            <input type="text"  class="input-large" id="optionTitle">
            <button class="btn btn-lightwhite btn-big2 margin0" type="button" id="doAddOption">
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
</div>
<!-- basic scripts -->
<script src="../scripts/ajaxEvent.js"></script>
<script src="../scripts/bootstrap-datepicker.js"></script>
<script src="../scripts/bootstrap-datepicker.zh-CN.js" charset="UTF-8"></script>
<script src="../scripts/Sortable.min.js"></script>
<script src="../scripts/moment.min.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
  var optionRemove = '<i class="js-remove">✖</i>';
  // init drag & drop
  var editableList = new Sortable(editable, {
    filter: '.js-remove',
    onFilter: function (evt) {
      var el = editableList.closest(evt.item); // get dragged item
      el && el.parentNode.removeChild(el);
      resetMaxOption();
    }
  });
  var _g_id;

  jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html: true});

    $('#vote-title, #start-time, #end-time').keydown(function(){
      $(this).closest("div.form-group").removeClass("has-error");
    });

    // init datapicker
    $('#start-time').datepicker({language: "zh-CN", format: "yyyy-mm-dd"});
    $('#end-time').datepicker({language: "zh-CN", format: "yyyy-mm-dd"});

    $("#addOption").click(function(){
      $("#optionTitle").val("");
      $("#optionModal").modal("show");
    });
    $('#optionModal').on('shown.bs.modal', function() {
      $("#optionTitle").focus();
    });
    $("#doAddOption").click(function(){
      addVoteOption();
    });
    $("#saveVote").click(function(){
      saveVote();
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
        if( $("#optionTitle").is(":focus")){
          event.preventDefault();
          addVoteOption();
        }
      }else if(event.which == 61 && event.target.tagName.toLowerCase() !== 'input' &&
                event.target.tagName.toLowerCase() !== 'textarea') { // +/=
        event.preventDefault();
        $("#optionTitle").val("");
        $("#optionModal").modal("show");
      }
    });

    initData();
    $("#vote-title").focus();
  });

  // 根据输入初始化数据
  function initData(){
    _g_id = $.getQuery("id", -1);
    if( _g_id < 0 ){
      // 新建
      // 今天
      $("#start-time").val(moment().format("YYYY-MM-DD"));
      // 15天后
      $("#end-time").val(moment().add(15, 'd').format("YYYY-MM-DD"));
    }else{
      // 编辑，ajax获取详细信息
      getVote(_g_id);
    }
  }

  function addVoteOption(){
    var t = $.trim($("#optionTitle").val());
    if( t != ""){
      // add to option panel
      $("#editable").append($("<li/>", {
        html : t + optionRemove
      }));

      $("#optionModal").modal("hide");
      resetMaxOption();
    }
    $("#optionTitle").trigger("blur");
  }

  function resetMaxOption(){
    // 根据选项个数重置做多选项
    var s = $("#max-option").val(),
            count = $("#editable").children().length;

    $("#max-option").empty();
    for(var i=1; i<=count; i++){
      $("#max-option").append($("<option/>", {value: i, text: i}));
    }
    $("#max-option").val(s<=count? s : 1);
  }

  function saveVote(){
    if($.trim($("#vote-title").val()) == ""){
      $("#vote-title").focus();
      $("#vote-title").closest("div.form-group").addClass("has-error");
      return;
    }

    if($.trim($("#start-time").val()) == ""){
      $("#start-time").focus();
      $("#start-time").closest("div.form-group").addClass("has-error");
      return;
    }
    if($.trim($("#end-time").val()) == ""){
      $("#end-time").focus();
      $("#end-time").closest("div.form-group").addClass("has-error");
      return;
    }
    if(moment($.trim($("#end-time").val())).isBefore(moment($.trim($("#start-time").val())))){
      $("#end-time").focus();
      $("#end-time").closest("div.form-group").addClass("has-error");
      alert("结束时间早于开始时间！");
      return;
    }
    if($("#editable").children().length <= 1){
      alert("选项数过少！");
      return;
    }
    // 保存投票基本信息
    $.ajax({
      type: "POST",
      url: "/vote/save.action",
      data: {"obj.id": _g_id,
        "obj.title": $.trim($("#vote-title").val()),
        "obj.startTime": $("#start-time").val(),
        "obj.endTime":$("#end-time").val(),
        "obj.maxOption": $("#max-option").val(),
        "serializedOption" : serializeOption($("#editable"))
      },
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);
        if(jsonData && jsonData["success"]){
          location.href = "vote.jsp";
        }
      }
    });
  }

  // 保存选项
  function saveOption(index, questionId){
    // 读取选项的id，如果没有，设置为-1
    var el = $("#editable").children().eq(index);
    if( !el ) return;

    var id =  el.attr("data-id")? parseInt(el.attr("data-id")) : -1;
    $.ajax({
      type: "POST",
      url: "/vote/vote!saveOption.action",
      data: {"option.id": id,
        "option.title": el.clone().children().remove().end().text(),
        "option.questionId": questionId,
        "option.sequence":index
      },
      dataType: "text",
      success: function(msg){
      }
    });
  }

  // 将选项序列化，格式为{"options": [{"id":-1, "serial": 1, "title":"选项1"},{}]}
  function serializeOption(obj){
    if( !obj ) return '{"options":[]}';

    var el, id, title, optionList = [];
    for(var i=0; i<obj.children().length; i++){
      el = obj.children().eq(i);
      if( !el ) continue;
      id =  el.attr("data-id")? parseInt(el.attr("data-id")) : -1;
      title = el.clone().children().remove().end().text();

      optionList.push(new QuesitonOption(id, title, i+1));
    }

    return JSON.stringify(optionList);
  }

  function QuesitonOption(id, title, serial){
    this.id = id;
    this.title = title;
    this.serial = serial;
  }

  // 获取投票详情
  function getVote(id){
    $.ajax({
      type: "POST",
      url: "/vote/vote!getVote.action",
      data: {"obj.id": id},
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);
        $("#vote-title").val( jsonData.data["title"] );
        $("#start-time").val( moment( jsonData.data["startTime"]).format("YYYY-MM-DD") );
        $("#end-time").val( moment( jsonData.data["endTime"]).format("YYYY-MM-DD") );
        // 选项
        $("#editable").empty();
        var optionList = jsonData.data["optionList"];
        for(var i=0; i<optionList.length; i++){
          $("#editable").append( $("<li/>", {
            html: optionList[i].title + optionRemove,
            "data-id": optionList[i].id
          }));
        }
        resetMaxOption();
        $("#max-option").val(jsonData.data["maxOption"]);
      }
    });
  }
</script>
</body>
</html>