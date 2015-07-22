/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 11-7-20
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
var userIp;
var cspId;
var contentId;



function submitComment() {
    var nickname = $("#nickname").val();
    if (nickname == "") {
        alert("请填写昵称");
        return;
    }
    var comments = $("#comments").val();
    if (comments == "") {
        alert("请填写评论");
        return;
    }
    var verifyCodeValue = $("#verifyCode").val();
    if (verifyCodeValue == "") {
        alert("请输入验证码");
        return;
    }
    $.getJSON(
        "/portal/userReview!save.action",
        {
            'obj.cspId':cspId,
            'obj.contentId':contentId,
            'obj.userId':nickname,
            'obj.desp':comments,
            'verifyCode':verifyCodeValue
        },
        function(json) {
            if (!json.success) {
                alert("验证码错误，请重新输入！");
            } else {
                alert("评论成功！");
                reset();
            }
        }
    );

}
function setUserScore() {
    $.getJSON("/portal/userScoring!searchUserScoringCountByContentIdAndCspId.action",
        {   'obj.contentId':contentId,
            'obj.cspId':cspId
        },
        function(json) {
            $("#star_img_5").width(json.score5);
            $("#star_num_5").html("&nbsp;" + json.score5 + "");
            $("#star_img_4").width(json.score4);
            $("#star_num_4").html("&nbsp;" + json.score4 + "");
            $("#star_img_3").width(json.score3);
            $("#star_num_3").html("&nbsp;" + json.score3 + "");
            $("#star_img_2").width(json.score2);
            $("#star_num_2").html("&nbsp;" + json.score2 + "");
            $("#star_img_1").width(json.score1);
            $("#star_num_1").html("&nbsp;" + json.score1 + "");

        });


}

function reset() {
    $("#comments").val("");
}

function jumpToComments() {
    window.location.href = "filmComments.jsp?contentId=" + contentId + "&cspId="+cspId+"";
}


function receiveComments() {
    $.getJSON("/portal/userReview!searchUserReviewsByContentIdAndCspId.action",
        {   'obj.contentId':contentId,
            'obj.cspId':cspId
        },
        function(json) {

            var commentsString = "";
            for (var i = 0; i < json.objs.length; i++) {
                var userIp = json.objs[i].userIp;
                var desp = json.objs[i].desp;
                var time = json.objs[i].time;
                var userId = json.objs[i].userId;
                commentsString += "<li>"
                    + "<a href='../images/grade/moonhare38.gif'  class='userpic' target='_blank'><img src='../images/grade/moonhare38.gif' alt='' title='' /></a>" + "<div class='commentdetail_div'>"
                    + "<p class='commentdetail_txt'>" + desp + "</p>"
                    + "<div class='commentdetail_bar'>"
                    + "<p>" + time + " 昵称为"+userId+"，来自" + userIp + "</p>"
                    + "<div>"
                    + "</div>"
                    + "</div>"
                    + "</li>";
            }

            $("#commentsDiv").html(commentsString);
        });

}

function receiveComments1() {
    $.getJSON("/portal/userReview!searchUserReviewsByContentIdAndCspId.action",
        {   'obj.contentId':contentId,
            'obj.cspId':cspId
        },
        function(json) {

            var commentsString = "";
            for (var i = 0; i < json.objs.length; i++) {
                var userIp = json.objs[i].userIp;
                var desp = json.objs[i].desp;
                var time = json.objs[i].time;
                var userId = json.objs[i].userId;
                commentsString += "<li>"
                    + "<a href='../images/grade/moonhare38.gif'  class='userpic' target='_blank'><img src='../images/grade/moonhare38.gif' alt='' title='' /></a>" + "<div class='commentdetail_div'>"
                    + "<p class='commentdetail_txt'>" + desp + "</p>"
                    + "<div class='commentdetail_bar'>"
                    + "<p>" + time + " 昵称为"+userId+"，来自" + userIp + "</p>"
                    + "<div>"
                    + "</div>"
                    + "</div>"
                    + "</li>";
            }

            $("#commentsDiv").html(commentsString);
        });

}

