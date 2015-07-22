<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/1/7
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>问卷结果</title>
  <link rel="stylesheet" href="../style/bootstrap.min.css?v=0.423"/>
  <link rel="stylesheet" href="../style/bootstrap-progressbar-3.0.1.min.css"/>
  <style>
    .progress{margin-bottom: 0}
  </style>
</head>
<body>
<div style="margin: 20px auto;width: 680px">
  <div class="page-header">
    <h1 id="investTitle"></h1>
  </div>

  <!--问卷结果-->
  <div id="totalStat" style="margin-bottom: 20px;color:darkgray"></div>
  <div id="questionStat"></div>
</div>
</body>
<!--[if !IE]> -->
<script type="text/javascript">
  window.jQuery || document.write("<script src='../scripts/jquery.min.js'>" + "<" + "/script>");
</script>
<!-- <![endif]-->
<!--[if IE]>
<script type="text/javascript">
  window.jQuery || document.write("<script src='../scripts/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script src="../scripts/bootstrap.min.js"></script>
<script src="../scripts/bootstrap-progressbar.min.js"></script>
<script src="../scripts/redex_utils.js"></script>
<script src="../scripts/ajaxEvent.js"></script>
<script src="../scripts/invest.js"></script>
<script src="../scripts/moment.min.js"></script>
<script src="../scripts/moment-duration-format.js"></script>
<script>
  jQuery(function ($) {
    var id = $.getQuery("id", -1);
    if( parseInt(id) > 0){
      getInvestResult(id, showStat);
    }
    //$('.progress .progress-bar').progressbar();
  });

  function getInvestResult(id, callback){
    $.ajax({
      type: "POST",
      url: "/invest/invest!investStat.action",
      dataType: "text",
      data: {"obj.id": id},
      success: function(msg){
        var response = parseJson(msg), investStat = parseInvestStat(response);

        if(callback && investStat){ callback(investStat);}
      }
    }).fail(function(){
      alert("呀，查看投票结果失败！");
    });
  }

  /**
   * 解析json的问卷结果
   */
  function parseInvestStat(jsonData) {
    if (!jsonData) return null;

    var investStat = new InvestigationStat(jsonData["id"], jsonData["title"], jsonData["totalCount"],
            jsonData["avgDuration"]), questionList = jsonData["questionList"],
            i, optionList, j, question, jsonQuestion;
    if (questionList) {
      for(i=0; i<questionList.length; i++){
        jsonQuestion = questionList[i];
        question = new QuestionStat(jsonQuestion["title"]);
        optionList = jsonQuestion["optionList"];
        if(optionList){
          for(j=0; j<optionList.length; j++){
            question.addOption(new OptionStat(optionList[j]["title"], optionList[j]["ticketCount"]));
          }
        }
        investStat.addQuestion(question);
      }
    }

    return investStat;
  }

  /*样式和progressbar冲突，在新页面中显示*/
  function showStat(investStat){
    if(!investStat) return;

    $("#investTitle").html(investStat.title);
    $("#totalStat").html('共<a href="investUser.jsp?id=' + investStat.id + '">' + investStat.count +
        '</a>人完成了问卷，平均用时' +
    (parseInt(investStat.avgDuration)<60 ? investStat.avgDuration + "秒" :
                    moment.duration(investStat.avgDuration, "seconds").format())
    );
    for(var i=0; i<investStat.questionList.length; i++){
      showQuestionStat(investStat.questionList[i], i+1, investStat.count);
    }
    $('.progress .progress-bar').progressbar({display_text: 'center'});  //
  }

  // 显示问题的统计结果
  function showQuestionStat(question, index, poll){
    if( !question ) return;

    var questionHtml = '<div class="tabbable"><b>' + index + ". " + question["title"] +
    '</b><div class="tab-content"><table class="table table-striped table-bordered table-hover">' +
    '<tbody>', option;
    for(var i=0; i<question.optionList.length; i++){
      option = question.optionList[i];
      questionHtml += '<tr><td width="50%">' + option.title + '</td><td width="40%">' +
      '<div class="progress">' +
      '<div class="progress-bar ' + getBarColor(i) + '" role="progressbar" data-transitiongoal="' +
      ((poll>0)? option.count*100/poll : 0) + '"></div>' +
      '</div>' +
      '</td><td>' + option.count + '/' + poll + '</td></tr>';
    }
    $("#questionStat").append(questionHtml + '</tbody></table></div></div>');
  }

  function getBarColor(index){
    switch(index%4){
      case 0:
        return "progress-bar-success";
      case 1:
        return "progress-bar-info";
      case 2:
        return "progress-bar-warning";
      case 3:
        return "progress-bar-error";
    }
    return "progress-bar-success";
  }

</script>

</html>
