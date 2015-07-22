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
    <title>投票测试</title>
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
    .vote-container{
      margin: 10px;
    }

    .vote-option{
      margin: 10px 0;
    }
    .vote-option div{
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
<div class="vote-container">
  <div id="voteTitle"></div>
  <div class="vote-option"></div>


</div>
<button class="btn btn-blue" type="button" id="saveVote">完成</button>
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
<script>
  var g_vote, g_start, g_user = 'wpS0008';
  jQuery(function ($) {
    var voteId = $.getQuery("id", -1);
    loadVote(voteId, showVote);

    $("#saveVote").click(function(){
      if($("div.vote-option input:checked").length == 0){
        alert("好歹选一个");
      }else{
        saveVote();
      }
    });
  });

  function loadVote(id, callback){
    $.ajax({
      type: "POST",
      url: "/vote/vote!getVote.action",
      data: {"obj.id": id},
      dataType: "text",
      success: function(msg){
        var jsonData = JSON.parse(msg);

        var voteOptions = [];
        var optionList = jsonData.data["optionList"];
        for(var i=0; i<optionList.length; i++){
          voteOptions.push(new Option(optionList[i].id, optionList[i].title));
        }
        g_vote = new Vote(id, jsonData.data["title"], voteOptions, jsonData.data["maxOption"]);
        if(callback){
          callback(g_vote);
        }
      }
    });
  }

  function showVote(vote){
    if(!vote) return;

    g_start = new Date();


    $("#voteTitle").html(vote.title);
    var optionType = "radio";
    if(parseInt(vote.maxOption) > 1){
      $("#voteTitle").append("(最多选择" + vote.maxOption + "项)");
      optionType = "checkbox";
    }

    for(var i=0; i<vote.optionList.length; i++){
      $(".vote-option").eq(0).append('<div><input type="' + optionType + '" value="' + vote.optionList[i].id +
            '" name="' + vote.id + '">' + vote.optionList[i].title + '</div>' );
    }

    $(".vote-option div input").click(function(){
      if($(this).closest("div.vote-option").find("input:checkbox:checked").length >
              $(this).closest("div.vote-option").attr("max-option")){
        //alert($(this).closest("div.vote-option").find("input:checkbox:checked").length);
        $(this).attr("checked", false);
      }
      //alert(this.value);
    });

    $(".vote-option").eq(0).attr("max-option", vote.maxOption);
  }

  function saveVote(){
    var duration = Math.ceil(((new Date()).getTime() - g_start.getTime())/1000);

    console.info("duration:" + duration);

    // 获得选项id
    var optionIds = [];
    $("div.vote-option input:checked").each(function(){
      optionIds.push($(this).val());
    });

    console.info("select:" + optionIds.join(","));


    $.ajax({
      type: "POST",
      url: "/vote/vote!doVote.action",
      data: {
        "obj.id": g_vote.id,
        "userId": g_user,
        "startTime": moment(g_start).format("YYYY-MM-DD HH:mm:ss"),
        "duration": duration,
        "selectedOptions": optionIds.join(",")
      },
      dataType: "text",
      success: function (msg) {
        var jsonData = JSON.parse(msg);
        if (jsonData.success) {
          alert("投好了");
        }
      }
    });

  }

  function Vote(id, title, optionList, maxOption){
    this.id = id;
    this.title = title;
    this.optionList = optionList;
    this.maxOption = maxOption;
  }

  function Option(id, title){
    this.id = id;
    this.title = title;
  }
</script>
</html>
