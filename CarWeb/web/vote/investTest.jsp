<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/1/6
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>问卷测试</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="renderer" content="webkit">
  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="../style/bootstrap.min.css?v=0.423"/>
  <link rel="stylesheet" href="../style/font-awesome.min.css"/>
  <link rel="stylesheet" href="../style/ace-fonts.css"/>
  <link rel="stylesheet" href="../style/ace.min.css?v=1.0" id="main-ace-style"/>
  <!--[if lte IE 9]>
  <link rel="stylesheet" href="../style/ace-part2.min.css"/>
  <![endif]-->
  <link rel="stylesheet" href="../style/ace-skins.min.css"/>
  <link rel="stylesheet" href="../style/ace-rtl.min.css"/>
  <!--[if lte IE 9]>
  <link rel="stylesheet" href="../style/ace-ie.min.css"/>
  <![endif]-->
  <link rel="stylesheet" href="../style/ace2.min.css"/>
  <link rel="stylesheet" href="../style/jquery.fileupload.css">
  <link rel="stylesheet" href="../style/jquery.fileupload-ui.css">
  <link rel="stylesheet" href="../style/doc.min.css?v=2.423.428"/>
  <link rel="stylesheet" href="../style/page.css"/>
  <!-- inline styles related to this page -->
  <!-- ace settings handler -->
  <script src="../scripts/ace-extra.min.js"></script>
  <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
  <!--[if lte IE 8]>
  <script src="../scripts/html5shiv.min.js"></script>
  <script src="../scripts/respond.min.js"></script>
  <![endif]-->
  <style>
    .invest-container{
      margin:10px
    }
    .question_container{
      margin-top: 20px;
    }
  </style>
</head>
<body>
<div class="invest-container">
  <div id="investTitle"></div>
  <div id="invest-question" ></div>


</div>
<button class="btn btn-blue" type="button" id="saveInvest">完成</button>
</body>
<script type="text/javascript">
  window.jQuery || document.write("<script src='../scripts/jquery.min.js'>" + "<" + "/script>");
</script>
<!-- <![endif]-->
<!--[if IE]>
<script type="text/javascript">
  window.jQuery || document.write("<script src='../scripts/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
  if ('ontouchstart' in document.documentElement) document.write("<script src='scripts/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="../scripts/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="../scripts/excanvas.min.js"></script>
<![endif]-->
<script src="../scripts/jquery-ui.custom.min.js"></script>
<script src="../scripts/jquery.ui.touch-punch.min.js"></script>
<script src="../scripts/chosen.jquery.min.js"></script>
<script src="../scripts/fuelux/fuelux.spinner.min.js"></script>
<script src="../scripts/date-time/bootstrap-datepicker.min.js"></script>
<script src="../scripts/date-time/bootstrap-timepicker.min.js"></script>
<script src="../scripts/date-time/moment.min.js"></script>
<script src="../scripts/date-time/daterangepicker.min.js"></script>
<script src="../scripts/date-time/bootstrap-datetimepicker.min.js"></script>
<script src="../scripts/bootstrap-colorpicker.min.js"></script>
<script src="../scripts/jquery.knob.min.js"></script>
<script src="../scripts/jquery.autosize.min.js"></script>
<script src="../scripts/jquery.inputlimiter.1.3.1.min.js"></script>
<script src="../scripts/jquery.maskedinput.min.js"></script>
<script src="../scripts/bootstrap-tag.min.js"></script>
<script src="../scripts/typeahead.jquery.min.js"></script>
<script src="../scripts/fuelux/fuelux.tree.min.js?v=4.23.428"></script>
<script src="../scripts/jquery.nestable.js"></script>
<script src="../scripts/ace-elements.min.js"></script>
<script src="../scripts/ace.min.js"></script>
<script src="../scripts/redex_utils.js"></script>
<script src="../scripts/invest.js"></script>
<script>
  var g_investigation, g_start, g_user = 'wp1001168';
  jQuery(function ($) {
    var investId = $.getQuery("id", -1);
    loadInvestigation(investId, showInvestigation);

    $("#saveInvest").click(function(){
      // 检查是否有问题没选
      var list = $("div.question_container");
      for(var i=0; i<list.length; i++){
        if(list.eq(i).find("input:checked").length == 0){
          alert("有问题没选呢！");
          return;
        }
      }
      saveInvest();
    });
  });

  function loadInvestigation(id, callback){
    $.ajax({
      type: "POST",
      url: "/invest/invest!getInvest.action",
      data: {"obj.id": id},
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);

        parseInvestigation(jsonData["data"]);

        if(callback){
          callback(g_investigation);
        }
      }
    });
  }

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

  function showInvestigation(invest){
    if(!invest) return;

    g_start = new Date();


    $("#investTitle").html(invest.title);
    for(var i=0; i<invest.questionList.length;i++){
      showQuestion(invest.questionList[i], i+1);
    }

    // 绑定选项事件
    $(".question_container div input").click(function(){
      if($(this).closest("div.question_container").find("input:checkbox:checked").length >
              $(this).closest("div.question_container").attr("max-option")){
        $(this).attr("checked", false);
      }

    });
  }

  function showQuestion(question, index){
    if(!question) return;

    console.info(index + "  " + question["title"]);

    $("#invest-question").append('<div id="question_' + question["id"] +
        '" class="question_container" max-option=' + question.maxOption + '/>');
    var optionType = "radio", questionTitle = index + ". " + question["title"], container =  $("#question_" + question["id"]);

    if(parseInt(question.maxOption) > 1){
      questionTitle += "(最多选择" + question.maxOption + "项)";
      optionType = "checkbox";
    }

    container.append("<h4>" + questionTitle + "</h4");

    for(var i=0; i<question.optionList.length; i++){
      container.append('<div><input type="' + optionType + '" value="' + question.optionList[i].id +
      '" name="' + question["id"] + '">' + question.optionList[i].title + '</div>' );
    }
 }

  function saveInvest(){
    var duration = Math.ceil(((new Date()).getTime() - g_start.getTime())/1000);

    console.info("duration:" + duration);

    var investResult = new InvestResult(g_investigation["id"], g_user, duration, moment(g_start).format("YYYY-MM-DD HH:mm:ss"));
    // 获取每个问题的选项
    var list = $("div.question_container"), questionResult, optionList;
    for(var i=0; i<list.length; i++){
      optionList = [];
      list.eq(i).find("input:checked").each(function(){
        optionList.push($(this).val());
      });
      investResult.addQuestionResult(new QuestionResult(
              list.eq(i).attr("id").replace(/question_/g, ""),
              optionList
      ));
    }

    console.info(JSON.stringify(investResult));

    $.ajax({
      type: "POST",
      url: "/invest/invest!doInvest.action",
      data: {
        "serializedInvestResult": JSON.stringify(investResult)
      },
      dataType: "text",
      success: function (msg) {
        var jsonData = JSON.parse(msg);
        if (jsonData.success) {
          alert("搞掂！");
        }
      }
    });
  }

</script>
</html>
