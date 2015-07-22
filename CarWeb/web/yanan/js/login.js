/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-19
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
document.write('<script src="js/getSession.jsp?date="+new Date().getTime()+"><'+'/script>');

jQuery(function($){
    $("#doLogin").click(function() {
        logon();
    });

    var userId = user==null?null:user.userId;

   $(function() {
       if(userId != null){
           $("#v1").hide();
           $("#v2").show();
           document.getElementById('showLogin').innerHTML="欢迎"+userId+"访问本网站！";
       } else{
           $("#v2").hide();
           $("#v1").show();
       }
   });

});

function logon() {
    if($("#loginId").val() == ""){
        alert("请输入用名");
        return;
    }

    if($("#passWord").val() == ""){
        alert("请输入密码");
        return;
    }
    login();
}
function login(){
    var loginId = $("#loginId").val();
    var passWord = $("#passWord").val();
    passWord = hex_md5(passWord);
    $.ajax({
        type: "POST",
        url: "/user/logon!frontUserLogon.action",
        dataType: "text",
        data: {'obj.userId':loginId,'obj.password':passWord},
        success: function(msg){
            var response = eval("(function(){return " + msg + ";})()");
            if (response.success) {
                alert("登陆成功！");
                window.location.reload();
            } else {
                var errorMsg = response.error[0];
                if(errorMsg.indexOf("密码") >= 0){
                    alert("帐号或者密码输入错误");
                }
                if(errorMsg.indexOf("用户") >= 0){
                    alert("用户不存在,请确定注册后再登陆！");
                }
            }
        }
    });
}

function logOut(){
    if(confirm('确认退出吗？')){
        $.ajax({
            url: '/user/frontUser!frontUserLoginOut.action',
            type:'post',
            success:function(){
                window.location.reload();
            }
        });


    }

}

