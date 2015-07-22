<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>影片打分</title></head>
<link rel="stylesheet" type="text/css" href="../css/grade.css">
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/grade.js"></script>
<script type="text/javascript">
    userIp = '<%=request.getRemoteAddr()%>';
    cspId = request("cspId");
    contentId = request("contentId");
    status = request("status");

$(document).ready(function() {
    setUserScore();
    $("input[name='score']").click(function() {
       var userId = $('#nickname').val();
        if(userId == ""|| userId==null){
            userId = "guest";
        }
        status = "0";

        var score = $(this).val();
        var cookieValue = getCookie(contentId);
        if (cookieValue == null) {
            SetCookie(contentId, userIp);
            $.post("/portal/userScoring!save.action",
                {   'obj.cspId':cspId ,
                    'obj.contentId':contentId,
                    'obj.status':status,
                    'obj.score':score,
                    'obj.userId':userId
                },
                function(data) {
                    alert("感谢您的评分!");
                    $("input[name='score']").attr("checked", false);
                    setUserScore();
                });

        } else {
            alert("您已经为本片打过分了");
            $("input[name='score']").attr("checked", false);
        }

    });

});

</script>
<body>
<div class="message_01">
    <span class="message_01_1">影迷们的评论</span><span class="message_01_2"><a onclick="jumpToComments();" style="cursor:pointer">查看所有评论&gt;&gt;</a></span>
</div>
<div class="message_02">
    <div class="message_02_1">影迷们觉得这部片子:</div>

    <div class="message_02_2">
        <span class="message_02_2_1"><img src="../images/grade/star_bg.gif" height="13px" width="0px"
                                          id="star_img_5"><a id="star_num_5"> 0</a></span>
    </div>
    <div class="message_02_3">
        <span class="message_02_2_1"><img src="../images/grade/star_bg.gif" height="13px" width="0px"
                                          id="star_img_4"><a id="star_num_4"> 0</a></span>
    </div>
    <div class="message_02_4">
        <span class="message_02_2_1"><img src="../images/grade/star_bg.gif" height="13px" width="0px"
                                          id="star_img_3"><a id="star_num_3"> 0</a></span>
    </div>
    <div class="message_02_5">
        <span class="message_02_2_1"><img src="../images/grade/star_bg.gif" height="13px" width="0px"
                                          id="star_img_2"><a id="star_num_2"> 0</a></span>
    </div>
    <div class="message_02_6">
        <span class="message_02_2_1"><img src="../images/grade/star_bg.gif" height="13px" width="0px"
                                          id="star_img_1"><a id="star_num_1"> 0</a></span>
    </div>

        <div class="message_02_7">我也来评价一下：
            <input name="score" value="5" type="radio"><img src="../images/grade/star_5_1.gif">
            <input name="score" value="4" type="radio"><img src="../images/grade/star_4_1.gif">
            <input name="score" value="3" type="radio"><img src="../images/grade/star_3_1.gif">
            <input name="score" value="2" type="radio"><img src="../images/grade/star_2_1.gif">
            <input name="score" value="1" type="radio"><img src="../images/grade/star_1_1.gif">
        </div>

    <div class="box_con" id="List_showCm_0" >
    <ul class="commentdetail1" id="commentsDiv">
        <li><a href='../images/grade/moonhare38.gif'  class='userpic' target='_blank'><img src='../images/grade/moonhare38.gif' alt='' title='' /></a>
            <div class='commentdetail_div'>
                <p class='commentdetail_txt'>desp</p>
                <div class='commentdetail_bar'>
                    <p>time  昵称为userId，来自userIp</p>
                </div>
            </div>
        </li>

    </ul>
</div>

    <form>
        <div class="message_02_10">
            <div class="message_02_10_1"><span class="message_02_10_1_2">称呼</span> <input name="nickName" id="nickname"
                                                                                          class="message_02_10_1_1"
                                                                                          size="25" type="text">

                <span class="message_02_10_1_3"> <img src="../images/grade/dian_3.gif"> <a href="http://v.inhe.net/2008/comment/agreement/" target="_blank">阅读评论协议&gt;&gt;</a></span>
            </div>
            <div class="message_02_10_2">评论</div>
            <div class="message_02_10_4"><textarea cols="80" rows="8" id="comments" name="comment"
                                                   class="message_02_10_2_1"></textarea></div>
            <div class="message_02_10_3"><span class="bold">验证码：</span><input name="verifyCode" type="text" id="verifyCode" style="width:120px;"/><a href="#" title="点击更新验证码！"><img src="../../security/verifyPic.jsp"
                                        id="verifyPic"
                                        onclick="refreshVerifyPic()"
                                        alt="验证码" style="vertical-align:middle" width="50" height="22"/></a><a
                        href="#"><img src="../images/grade/button_1.gif" align="top" border="0" height="36"
                                      width="122" onclick="submitComment();"></a>　<a href="#"><img
                        src="../images/grade/button_2.gif" onclick="reset();" align="top" border="0" height="36"
                        width="51"></a></div>
        </div>


    </form>
</div>
<input name="contentId" value="595680788" type="hidden">
<input name="cspId" value="981" type="hidden">
<input name="status" value="17884" type="hidden">
<input name="userId" value="596114802" type="hidden">
</body>
</html>

<script type="text/javascript">
    $(document).ready(function() {
        receiveComments1();
    });

</script>