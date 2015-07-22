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
  <title>问卷用户</title>
  <%@include file="../inc/displayCssJsLib.jsp"%>
  <link rel="stylesheet" href="../style/bootstrap-progressbar-3.0.1.min.css"/>
  <style>
    thead{background-color: #eff3f8}
  </style>
</head>
<body class="no-skin">
  <div class="page-content">

    <div class="page-content-area">


      <div class="row page-content-main">
<div style="margin: 20px auto;width: 680px" id="userList">
  <div class="tabbable">
    <div class="input-group pull-right search-group" style="width: 220px;">
      <input type="text" placeholder="" class="form-control search-query" id="search_word">
                                     <span class="input-group-btn">
                                         <button class="btn btn-sm" type="button" id="btn_search">
                                           <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                         </button>
                                     </span>
    </div>
    <div class="space-6"></div>
    <table class="table table-striped table-bordered table-hover table-30" id="tabUsers">
      <thead>
      <tr>
        <th>序号</th>
        <th width="25%">
          <a href="#" onclick='order_by("userId")' title="按用户Id排序" id="order_by_userId">登录名</a>
        </th>
        <th width="25%">姓名</th>
        <th>
          <a href="#" onclick='order_by("duration")' title="按耗时排序" id="order_by_duration">耗时</a>
        </th>
        <th>
          <a href="#" onclick='order_by("time")' title="按耗时排序" id="order_by_time">问卷时间</a>
        </th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      </tbody>
    </table>

  </div>
  <div class="space-6"></div>
  <div class="row">
    <div class="col-md-9 col-md-offset-3"><ul class="pagination pull-right" id="page-nav">
    </ul></div>
  </div>
</div>
<div style="margin: 20px auto;width: 680px" id="userDetail">
  <div id="investPanel">
    <h1 id="investTitle"></h1>
    <div id="invest-question" ></div>
  </div>
  <div class="space-6"></div>
  <div class="row">
    <div class="col-md-4">
      <a class="btn btn-blue" id="btnBack">返回</a>
    </div>
  </div>
</div>
</div>
</div>
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
<script src="../scripts/redex_page.min.js"></script>
<script src="../scripts/ajaxEvent.js"></script>
<script src="../scripts/invest.js"></script>
<script src="../scripts/moment.min.js"></script>
<script src="../scripts/moment-duration-format.js"></script>
<script>
  var order = "time";
  var dir = "desc";
  var page_index = 1;
  var page_size = 10;
  var page_count = 0;
  var g_id;

  jQuery(function ($) {
    g_id = $.getQuery("id", -1);
    if( parseInt(g_id) > 0){
      page_index = 1;
      getInvestUser(g_id, showUserList);
    }
    $("#userDetail").hide();
    //$('.progress .progress-bar').progressbar();
    $(document).keydown(function( event ) {
      if ( event.which == 13 ) {
        if( $("#search_word").is(":focus")){
          event.preventDefault();
          getInvestUser(g_id, showUserList);
        }
      }
    });

    $("#btn_search").click(function(){
      getInvestUser(g_id, showUserList);
    });

    $("#btnBack").click(function(){
      backToUserList();
    });
  });

  function getInvestUser(id, callback){
    var _order = "ir.investigateTime";
    switch (order) {
      case "userId": _order = "ir.userId";break;
      case "time": _order = "ir.investigateTime";break;
      case "duration": _order = "ir.duration";break;
    }

    $.ajax({
      type: "POST",
      url: "/invest/invest!investUser.action",
      dataType: "text",
      data: {"obj.id": id,
        "searchWord": $.trim($("#search_word").val()),
        "pageBean.pageSize":page_size,
        "pageBean.pageNo":page_index,
        "pageBean.orderBy":_order,
        "pageBean.orderDir":dir
      },
      success: function(msg){
        var response = parseJson(msg), userList = parseInvestUsers(response["userList"]);
        rebuild_page_nav($("#page-nav"), Math.ceil(response["totalCount"] / page_size), page_index, "to_page", response["totalCount"]);
        if(callback && userList){ callback(userList);}
      }
    }).fail(function(){
      alert("呀，查看问卷用户失败！");
    });
  }

  /**
   * 解析json的问卷结果
   */
  function parseInvestUsers(jsonData) {
    if (!jsonData) return null;

    var userList = [];
    for(var i=0; i<jsonData.length; i++){
      var jsonUser = jsonData[i];
      userList.push(new InvestUser(jsonUser["userId"], jsonUser["name"], jsonUser["duration"], jsonUser["investTime"]));
    }

    return userList;
  }

  function showUserList(userList){
    if(!userList) return;

    $("#tabUsers tbody").empty();
    for(var i=0; i<userList.length; i++){
      $("#tabUsers tbody").append("<tr><td>" + (i+1) + "</td>" +
      "<td>" + userList[i].userId + "</td>" +
      "<td>" + userList[i].name + "</td>" +
      "<td>" + (parseInt(userList[i].duration)<60? userList[i].duration + "秒" : moment.duration(userList[i].duration, "seconds").format()) + "</td>" +
      "<td>" + moment(userList[i].startTime).format("YYYY-MM-DD") + "</td>" +
      "<td><a href='#' onclick='viewDetail(\"" + userList[i].userId + "\");return false;'>详情</a></td>" +
      "</tr>");
    }
  }

  function viewDetail(userId){
    $.ajax({
      type: "POST",
      url: "/invest/invest!getUserInvest.action",
      data: {"obj.id": g_id,"userId": userId },
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);

        showDetail(parseInvestigation(jsonData["data"]));
      }
    });
  }

  function parseInvestigation(jsonData){
    if( !jsonData ) return;

    var investigation = new Investigation(jsonData["id"],jsonData["title"], moment(jsonData["startTime"]),
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
        option = new QuestionOption(jsonOption["id"], jsonOption["title"], jsonOption["sequence"], jsonOption["selected"]);
        question.addOption(option);
      }
      investigation.addQuestion(question);
   }
    return investigation;
  }

  function showDetail(invest){
    if(!invest) return;

    $("#userList").hide();
    $("#userDetail").show();
    $("#investTitle").html(invest.title);
    $("#invest-question").empty();
    for(var i=0; i<invest.questionList.length;i++){
      showQuestion(invest.questionList[i], i+1);
    }
  }

  function showQuestion(question, index){
    if(!question) return;

    $("#invest-question").append('<div id="question_' + question["id"] +
    '" class="question_container" max-option=' + question.maxOption + '/>');
    var optionType = "radio", questionTitle = index + ". " + question["title"], container =  $("#question_" + question["id"]);

    if(parseInt(question.maxOption) > 1){
      questionTitle += "(最多选择" + question.maxOption + "项)";
      optionType = "checkbox";
    }

    container.append("<h4>" + questionTitle + "</h4");

    for(var i=0; i<question.optionList.length; i++){
      var s = '<div><input type="' + optionType + '" value="' + question.optionList[i].id +
              '" name="' + question["id"] + '"';
      if(question.optionList[i].selected){
        s += ' checked="checked"';
      }
      s += ' disabled="disabled">' + question.optionList[i].title + '</div>';
      container.append( s);
    }
  }

  function backToUserList(){
    $("#userDetail").hide();
    $("#userList").show();
  }

  function to_page(index) {
    page_index = index;
    getInvestUser(g_id, showUserList);
  }


  function order_by(v) {
    if (v == order) {
      dir = (dir == "asc") ? "desc" : "asc";
    } else {
      order = v;
      dir = "asc";
    }

    $("#order_by_userId").attr("title", (order == "userId" && dir == "desc") ? "按登录名倒序排序" : "按登录名排序");
    $("#order_by_time").attr("title", (order == "time" && dir == "desc") ? "按问卷时间倒序排序" : "按问卷时间排序");
    $("#order_by_duration").attr("title", (order == "duration" && dir == "desc") ? "按耗时倒序排序" : "按耗时排序");
    page_index = 1;

    getInvestUser(g_id, showUserList);
  }


</script>

</html>
