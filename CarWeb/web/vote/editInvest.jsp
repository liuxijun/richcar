<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/1/7
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>问卷编辑 - <%=IndividualUtils.getInstance().getName()%></title>
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
/*
    .block__list li { cursor: move; }
*/

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

    #editable, #questionEditable {background: #EFEBEF}
    #editable li,#questionEditable li {
      position: relative;
    }

    #editable,#questionEditable{
      margin: 0;
      padding: 0;
      list-style: none;
    }

    #editable i,#questionEditable i {
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

    #editable li:hover i,#questionEditable li:hover i {
      opacity: 1;
    }

    .drag-handle {
      margin-right: 10px;
      font: bold 16px Sans-Serif;
      color: #5F9EDF;
      cursor: move;
      cursor: -webkit-grabbing;  /* overrides 'move' */
    }
    #filter,#question_filter {}
    #filter button,#question_filter button {
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
    #filter button:hover,#question_filter button:hover {
      opacity: 1;
    }

    #filter .block__list,#question_filter .block__list {
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
        <li>系统管理</li><li class="active">问卷投票</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-edit"></i>编辑问卷
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">

        <div class="row page-content-main">
          <form role="form" class="form-horizontal" action="" method="POST" id="investForm">

            <h4>请录入问卷信息</h4>

            <div class="col-xs-12 no-padding">
              <hr>
              <div class="form-group">
                <label class="col-sm-1 control-label no-padding-right filed-need">标题</label>

                <div class="col-sm-11">
                  <input type="text" class="col-sm-6" id="invest-title">
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
                <label class="col-sm-1 control-label no-padding-right filed-need">问题</label>

                <div class="col-sm-11">
                  <div class="container" style="padding-left: 0">
                    <div id="filter">
                      <div class="block__list block__list_words">
                        <ul id="editable">
                        </ul>
                        <button id="addQuestion" type="button">增加问题（+/=）</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="space"></div>
              <button class="btn btn-blue" type="button" id="saveInvest">完成</button>
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
  <div class="modal fade modal-class" id="questionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
       aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left:50%;margin: -200px 0 0 -300px; width:600px">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                  class="sr-only">Close</span></button>
          <h4 class="modal-title" id="modalTitle">编辑问题</h4>
        </div>
        <div class="modal-body">
          <div class="row">
            <div class="col-xs-12 ">
              <form class="form-horizontal">
                <div class="form-group">
                  <label class="col-sm-2 control-label no-padding-right">标题</label>

                  <div class="col-sm-10">
                    <input type="text" class="col-sm-10" id="question-title">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label no-padding-right">选项</label>
                  <div class="col-sm-10">
                    <div class="container" style="padding-left: 0">
                      <div id="question_filter">
                        <div class="block__list block__list_words">
                          <div class="maxOption">最多可选<select id="max-option"><option value="1">1</option></select></div>
                          <ul id="questionEditable">
                            <%--<li>苍井空<i class="js-remove">?</i></li>
                            <li>柚木提娜<i class="js-remove">?</i></li>
                            <li>松岛枫<i class="js-remove">?</i></li>--%>
                          </ul>
                          <div style="margin: 8px 0 20px 20px;height:50px">
                            <input type="text" maxlength="128" size="40" id="option-title">
                            <input class="btn btn-green margin0" type="button" id="addOption" value="添加选项"/>
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>
                <button class="btn btn-blue btn-big2 margin0" type="button" id="doAddQuestion">
                  确定
                </button>
              </form>
            </div>
          </div>
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
<script src="../scripts/invest.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
  var removeOption = '<i class="js-remove">✖</i>',
          dragHandle = '<span class="drag-handle">&#9776;</span>';
  // init drag & drop
  var investEditableList, questionEditableList;
  var _g_id, g_sn = 428, g_questionId = -1,g_investigation;


  /*
  * 编辑操作在保存之前，全部在客户端进行，新添加的问题和选项id使用负数，在li中增加data-id字段，
  * 问题和问卷都在保存时再解析内容，首先将已经不存在的项删除，再重新设置其中的serail
  * */
  jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html: true});

    $('#invest-title, #start-time, #end-time').keydown(function(){
      $(this).closest("div.form-group").removeClass("has-error");
    });

    investEditableList = new Sortable(editable, {
      filter: ".js-remove",
      handle: '.drag-handle',
      onFilter: function (evt) {
        var el = investEditableList.closest(evt.item); // get dragged item
        el && el.parentNode.removeChild(el);
      }
    });
    questionEditableList = new Sortable(questionEditable, {
      filter: ".js-remove",
      handle: '.drag-handle',
      onFilter: function (evt) {
        var el = investEditableList.closest(evt.item); // get dragged item
        el && el.parentNode.removeChild(el);
      }
    });
    // init datapicker
    $('#start-time').datepicker({language: "zh-CN", format: "yyyy-mm-dd"});
    $('#end-time').datepicker({language: "zh-CN", format: "yyyy-mm-dd"});

    $("#addQuestion").click(function(){
      g_questionId = ++g_sn * -1;
      $("#question-title").val("");
      $("#questionEditable").empty();
      $("#questionModal").modal({ backdrop: 'static', keyboard: true, show: true});
    });
    $('#questionModal').on('shown.bs.modal', function() {
      $("#question-title").focus();
    });

    $("#saveInvest").click(function(){
      saveInvest();
    });

    // 添加选项
    $("#addOption").click(function(){
      addQuestionOption();
    });
    // 添加问题
    $("#doAddQuestion").click(function(){
      addQuestion();
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
        if( $("#option-title").is(":focus")){
          event.preventDefault();
          addQuestionOption();
        }
      }else if(event.which == 61 && event.target.tagName.toLowerCase() !== 'input' &&
              event.target.tagName.toLowerCase() !== 'textarea') { // +/=
        event.preventDefault();
        $("#optionTitle").val("");
        $("#optionModal").modal("show");
      }
    });

    initData();
    $("#invest-title").focus();
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
      g_investigation = new Investigation(++g_sn * -1, "", $("#start-time").val(), $("#end-time").val(), []);
    }else{
      // 编辑，ajax获取详细信息
      getInvest(_g_id);
    }
  }

  /**
  * 添加问卷问题
   */
  function addQuestion(){
    // 检查标题
    var t = $.trim($("#question-title").val());
    if( t == ""){
      $("#question-title").focus();
      return;
    }
    // 检查选项，至少2个
    var optionList = $("#questionEditable");
    if(optionList.children().length <= 1){
      alert("问题选项过少！");return;
    }

    // 执行添加操作
    var question = g_investigation.getQuestion(g_questionId) ||
                    new Question(g_questionId, t, 999, [], $("#max-option").val());
    question.clearOption();
    for(var i=0; i<optionList.children().length; i++){
      var el = optionList.children().eq(i);
      if( !el ) continue;
      var id =  el.attr("data-id")? parseInt(el.attr("data-id")) : ++g_sn * -1;
      var title = el.clone().children().remove().end().text();

      question.addOption(new QuestionOption(id, title, i+1));
    }
    question["maxOption"] = $("#max-option").val();
    question["title"] = t;

    // update / append
    if(g_investigation.getQuestion(question["id"])){
      $("#editable li[data-id=" + question["id"] + "]").html(buildQuestionListItem(question));
    }else{
      g_investigation.addQuestion(question);
      $("#editable").append($("<li/>", {
        "data-id": question["id"],
        html : buildQuestionListItem(question)
      }));
    }
    $("#questionModal").modal("hide");
  }

  function addQuestionOption(){
    var t = $.trim($("#option-title").val());
    if( t != ""){
      // add to option panel
      $("#questionEditable").append($("<li/>", {
        html : dragHandle + t + removeOption,
        "data-id" : ++g_sn * -1
      }));

      resetMaxOption();
    }
    $("#option-title").val("");
  }

  function buildQuestionListItem(question){
    if( !question ) return "";
    return dragHandle + '<a href="#" onclick="editQuestion(' +
            question["id"] + ');return false;">' +
            question["title"] + '</a>' + removeOption;
  }

  function editQuestion(id){
    var question = g_investigation.getQuestion(id);
    if( !question ) return;

    g_questionId = question["id"];
    $("#question-title").val(question["title"]);
    $("#questionEditable").empty();
    for(var i=0;i<question.optionList.length; i++){
      $("#questionEditable").append($("<li/>", {
        html : dragHandle + question.optionList[i].title + removeOption,
        "data-id" : question.optionList[i].id
      }));
    }
    resetMaxOption();
    console.info(question["maxOption"]);
    $("#max-option").val(question["maxOption"]);
    $("#questionModal").modal("show");
  }

  function resetMaxOption(){
    // 根据选项个数重置做多选项
    var o = $("#max-option"), s = o.val(),
            count = $("#questionEditable").children().length;

    o.empty();
    for(var i=1; i<=count; i++){
      o.append($("<option/>", {value: i, text: i}));
    }
    o.val(s<=count? s : 1);
  }

  // 清理g_investigation
  function cleanQuestion(){
    var q, i,q_id;
    // 清除已经被删除的question
    if( !g_investigation) return;
    for(i=0; i<g_investigation.questionList.length; i++){
      q = g_investigation.questionList[i];
      if( q && $("#editable li[data-id=" + q["id"] + "]").length == 0){
        // 从questionList中删除
        g_investigation.questionList.splice(i, 1);
        i--;
      }
    }
    // 重新设置question的顺序
    var list = $("#editable");
    for(i=0; i<list.children().length; i++) {

      q_id = list.children().eq(i).attr("data-id");
      q = g_investigation.getQuestion(q_id);
      console.info("qid:" + q_id);
      if( q ){q["serial"] = i+1;}
    }
  }

  function saveInvest(){
    var objTitle = $("#invest-title"), objStartTime = $("#start-time"), objEndTime = $("#end-time");
    if($.trim(objTitle.val()) == ""){
      objTitle.focus();
      objTitle.closest("div.form-group").addClass("has-error");
      return;
    }

    if($.trim(objStartTime.val()) == ""){
      objStartTime.focus();
      objStartTime.closest("div.form-group").addClass("has-error");
      return;
    }
    if($.trim(objEndTime.val()) == ""){
      objEndTime.focus();
      objEndTime.closest("div.form-group").addClass("has-error");
      return;
    }
    if(moment($.trim(objEndTime.val())).isBefore(moment($.trim(objStartTime.val())))){
      objEndTime.focus();
      objEndTime.closest("div.form-group").addClass("has-error");
      alert("结束时间早于开始时间！");
      return;
    }

    // clear question
    cleanQuestion();

    console.info("question:" + serializeQuestion(g_investigation));

    // 保存问卷基本信息
    $.ajax({
      type: "POST",
      url: "/invest/save.action",
      data: {"obj.id": _g_id,
        "obj.title": $.trim(objTitle.val()),
        "obj.startTime": objStartTime.val(),
        "obj.endTime":objEndTime.val(),
        "serializedQuestion" : serializeQuestion(g_investigation)
      },
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);
        if(jsonData && jsonData["success"]){
          location.href = "investigation.jsp";
        }
      }
    });
  }


  // 将问题序列化为json字串
  function serializeQuestion(obj){
    if( !obj ) return '{"questions":[]}';

    return JSON.stringify(obj.questionList);
  }


  // 获取投票详情
  function getInvest(id){
    $.ajax({
      type: "POST",
      url: "/invest/invest!getInvest.action",
      data: {"obj.id": id},
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);
        parseInvestigation(jsonData["data"]);
        showInvestigation();
      }
    });
  }

  /**
  * 将json的问卷解析成对象，放到g_investigation中
* @param jsonData
   */
  function parseInvestigation(jsonData){
    if( !jsonData ) return;

    g_investigation = new Investigation(jsonData["id"],jsonData["title"], moment(jsonData["startTime"]),
            moment(jsonData["endTime"]), []);
    // 解析问题
    var questionList = jsonData["questionList"], question, jsonQuestion, optionList, jsonOption, option;
    for(var i=0; i<questionList.length; i++){
      jsonQuestion = questionList[i];
      if(!jsonQuestion) continue;
      question = new Question(jsonQuestion["id"], jsonQuestion["title"], jsonQuestion["sequence"], [], jsonQuestion["maxOption"]);
      // 选项
      optionList = jsonQuestion["optionList"];
      for(var j=0; j<optionList.length; j++){
        jsonOption = optionList[j];
        if(!jsonOption) continue;
        option = new QuestionOption(jsonOption["id"], jsonOption["title"], jsonOption["sequence"]);
        question.addOption(option);
      }
      g_investigation.addQuestion(question);
    }
  }

  // 显示g_investigation中的数据
  function showInvestigation(){
    if(!g_investigation) return;
    $("#invest-title").val(g_investigation.title);
    $("#start-time").val(moment(g_investigation.startTime).format("YYYY-MM-DD"));
    $("#end-time").val(moment(g_investigation.endTime).format("YYYY-MM-DD"));
    $("#editable").empty();
    for(var i=0; i<g_investigation.questionList.length; i++){
      $("#editable").append($("<li/>", {
        "data-id": g_investigation.questionList[i]["id"],
        html : buildQuestionListItem(g_investigation.questionList[i])
      }));
    }
  }
</script>

</body>
</html>
