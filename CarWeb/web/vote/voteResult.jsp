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
  <title>投票结果</title>
  <link rel="stylesheet" href="../style/bootstrap.min.css?v=0.423"/>
  <link rel="stylesheet" href="../style/bootstrap-progressbar-3.0.1.min.css"/>
  <style>
    .progress{margin-bottom: 0}
  </style>
</head>
<body>
<div style="margin: 20px auto;width: 680px">
  <h4 id="voteTitle" class=""></h4>
  <!--投票结果-->
  <div>
    <!--选项得票-->
    <div class="row">
      <div class="col-xs-12 ">
    <table class="table table-striped table-bordered table-hover table-30" id="optionResult">
      <tbody></tbody>
    </table>
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
<script src="../scripts/ajaxEvent.js"></script>
<script>
  jQuery(function ($) {
    var id = $.getQuery("id", -1);
    if( parseInt(id) > 0){
      getVoteResult(id, showStat);
    }
    //$('.progress .progress-bar').progressbar();
  });

  function getVoteResult(id, callback){
    $.ajax({
      type: "POST",
      url: "/vote/vote!voteStat.action",
      dataType: "text",
      data: {"obj.id": id},
      success: function(msg){
        var response = parseJson(msg);
        var optionList = [];
        for(var i=0; i<response.optionList.length; i++){
          var op = response.optionList[i];
          optionList.push(new Option(
                          op["id"], op["title"], op["ticketCount"]
                  )
          );
        }
        if(callback){ callback(new VoteStat(response["id"], response["title"], response["totalTicketCount"], optionList));}
      }
    }).fail(function(){
      alert("呀，查看投票结果失败！");
    });
  }

  /*样式和progressbar冲突，在新页面中显示*/
  function showStat(voteStat){
    if(!voteStat) return;

    $("#voteTitle").html(voteStat.title);
    for(var i=0; i<voteStat.optionList.length; i++){
      var option = voteStat.optionList[i];
      if(option){
        var item = '<tr><td width="50%">' + option.title + '</td><td width="40%">' +
                '<div class="progress">' +
                '<div class="progress-bar ' + getBarColor(i) + '" role="progressbar" data-transitiongoal="' +
                ((voteStat.poll>0)? option.poll*100/voteStat.poll : 0) + '"></div>' +
                '</div>' +
                '</td><td>' + option.poll + '/' + voteStat.poll + '</td></tr>';
        $("#optionResult tbody").append(item);
      }
    }
    $('.progress .progress-bar').progressbar({display_text: 'center'});  //
    $("#voteResultModal").modal("show");
  }

  function VoteStat(id, title,poll, optionList){
    this.id = id;
    this.title = title;
    this.poll = poll;
    this.optionList = optionList || [];
  }
  function Option(id, title, poll){
    this.id = id;
    this.title = title;
    this.poll = poll;
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
