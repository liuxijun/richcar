<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2015/3/26
  Time: 15:37
  后台直播播放器
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
  <style>
    .playBox {
      width:860px;
      height:525px;
      float:left;
      background: #000000;
    }
    input{
      width: 800px;
    }
    b{
      font-size: 12px;
      font-weight: normal;
    }
  </style>
</head>
<body>
<h1></h1>
<div style="margin: 8px 0 12px 0">
  <b>播放链接：</b><input type="text" id="uri"/>
</div>
<div class="playBox" id="movieContainer">

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
<script>
  // 视频播放器，核心代码，切勿改动
  document.write('<script src="../interface/player.jsp?sn='+new Date().getTime()+'">\x3C/script>');
</script>
<script src="../scripts/redex_utils.js"></script>
<script>
  var liveId = -1;
  jQuery(function ($) {
    liveId = $.getQuery("id", -1);
    if(parseInt(liveId) > 0){
      getLiveInfo(liveId);
      playLive(liveId);
    }

    $( "#uri" ).focus(function() {
      $(this).select();
    });
  });

  function getLiveInfo(id){
    $.ajax({
      type: "POST",
      url: "/live/live!loadLive.action",
      //dataType: "json",
      dataType: "text",
      data: {"obj.id": id},
      success: function(msg) {
        var response = eval("(function(){return " + msg + ";})()");
        $("h1").html(response.data["title"]);
      }
    })
  }

  function playLive(id){
    var container = $("#movieContainer"), width = container.width(), height = container.height();
    $.ajax({
      type: "POST",
      url: "/live/live!liveURL.action",
      //dataType: "json",
      dataType: "text",
      data: {"obj.id": id},
      success: function(msg) {
        var response = eval("(function(){return " + msg + ";})()");
        if(response.succeed){
          console.info("url:" + response.url);
          $("#uri").val(response.url);
          playLiveDirectly(response.url, "RedexLivePlayer", "movieContainer",width,height);
        }
      }
    })
  }
</script>
</html>
