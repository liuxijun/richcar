/**
 * Created by 王明路 on 2014/12/15.
 */
jQuery(function ($) {
    $(document).ajaxStart(function () {
        $("#loading_container").show();
    });

    $(document).ajaxError(function () {
        ajaxErrorHandler();
    });

    $(document).ajaxStop(function () {
        setTimeout(function () {
            $("#loading_container").hide();
        }, 200);
    });
});
function ajaxErrorHandler(){
    $("#loading_container").hide();
    alert("和服务器通信失败，请重新登录！");
    location.href = "../login.jsp";
}

function parseJson(json){
    try{
        return JSON.parse(json);
    }catch(e){
        ajaxErrorHandler();
        return null;
    }
}
