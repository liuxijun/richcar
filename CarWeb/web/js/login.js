var _LOGIN_TYPE_PC=1;
var _LOGIN_TYPE_PAD=2;
var _LOGIN_TYPE_PHONE=3;
var _LOGIN_TYPE_CLIENT=4;
var _LOGIN_TYPE_UNKNOWN=5;
var displayRegister = true;
var displayUserName = false;
function isRegisterUserName(s){
    var regex =/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,20}$/;
    return regex.exec(s);
}

function isValidateEmail(email) {
    var regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(email);
}

/*11位长的数字串*/
function isCellPhoneNumber(s){
    var regex = /^\d{11}$/;
    return regex.test(s);
}

function trim(strInput){
            if(strInput!=null){
                return strInput.replace(/(^\s*)|(\s*$)/g, "");
            }
            return "";
        }

        function login_(indexUrl,type) {
            var username = document.getElementById("username").value;
            var password = document.getElementById("password").value;
            if(username == null || trim(username) == "" || password == null || trim(password) == "") {
                alert("请输入用户名与密码！");
                return;
            }
            if(typeof(type)=='undefined'||type==null){
                type = _LOGIN_TYPE_PC;
            }
            password = hex_md5(password);
            if(type==_LOGIN_TYPE_PAD){
                //如果是pad，就记录cookie
                saveCookie("userLoginId",username);
                saveCookie("userLoginPwd",password);
            }
            $.ajax({
                url:'/user/frontUser!frontUserLogon.action?refresh='+new Date().getTime(),
                data:{"obj.userId":username,"obj.password":password,dc:new Date().getTime(),"obj.typeId":type},
                method:'post',
                type:'post',
                success:function(msg){
                    var result = eval('('+msg+')');
                    if(result.success) {
                        //alert("登录成功!");
                        var url = window.location.href;
                        if(url.indexOf("login")>=0){
                            url = getParameterFromUrl(null,"nextUrl",null);
                            if(url==null){
                                url =window.location.href;
                                if(url.indexOf('login_pad.')>=0){
                                    url = 'index_pad.html';
                                }else{
                                    url = 'index_phone.html';
                                }
                            }else{
                                var parameters=['parentId','channelId','contentId','videoId','id'];
                                for(var i= 0,l=parameters.length;i<l;i++){
                                    var p = parameters[i];
                                    var v = getParameterFromUrl(null,p,null);
                                    if(v!=null){
                                        if(url.indexOf("?")>=0){
                                            url+="&";
                                        }else{
                                            url+="?";
                                        }
                                        url+=p+"="+v;
                                    }
                                }
                            }
                        }
                        if(url.indexOf("?")>=0){
                            url +="&";
                        }else{
                            url +="?";
                        }
                        window.location.href=url+'d='+new Date().getTime();
                    }else {
                        alert(result.error);
                    }
                }
            });
        }

        var nextUrl = "index";
        function handleKeyPress(evt) {
            var loginBox = document.getElementById('login_win');
            var updatePwdBox = document.getElementById('updatePwd_win');
            var currentUrl = window.location.href;

            var nbr = (window.event)?event.keyCode:evt.which;
            if(nbr == 13) {
              //登录
              if((loginBox && loginBox.style.display == "block") || currentUrl.indexOf('login') > 0) {
                  login_(nextUrl);
              }

              //修改密码
              if(updatePwdBox && updatePwdBox.style.display == "block" || currentUrl.indexOf('updatePwd') > 0) {
                  updatePwd();
              }

            }
        }

        document.onkeydown= handleKeyPress;

        function showLoginBox() {
            $.blockUI({ message: $('#login_win'),
                css:{ marginLeft:     '-200px',marginTop:      '100px'} });
        }

        function closeLoginBox() {
            $.unblockUI({ message: $('#login_win')});
            window.location.href = 'index.html';
        }


        function loginEntrance(deviceType,str) {
            var loginBox = document.getElementById(deviceType);
            if(loginBox) {
                if(deviceType == 'phoneLoginBox') {
                    loginBox.href=str;
                    document.getElementById('updatePwd').style.display="none";
                    if(displayRegister){
                        document.getElementById('register').style.display="block";
                    }else{
                        document.getElementById('register').style.display="none";
                    }
                } else {
                    var unLoginTitle = '<b class="i_user"></b><a href="'+str+'" >登录</a>';
                    if(displayRegister) {
                        unLoginTitle+='<span>|</span><a href="javascript:showReg_win()" class="reg_b">注册</a>';
                    }
                    loginBox.innerHTML = unLoginTitle;
                }
            }
        }

        function showUpdatePwdWin(deviceType) {
            $.blockUI({ message: $('#updatePwd_win'),
                css:{ marginLeft:     '-200px',marginTop:      '30px'} });

            var userIdInput = document.getElementById('update_userId');
            if(userIdInput) {
                userIdInput.value = userId;
            }

        }

        function closeUpdateBox() {
            $.unblockUI({ message: $('#updatePwd_win')});
        }

        function updatePwd() {
            var oldPwd = document.getElementById('old_pwd').value;
            var newPwd = document.getElementById('new_pwd').value;
            var confirmNewPwd = document.getElementById('confirm_new_pwd').value;

            if(oldPwd == null || trim(oldPwd) == '' ) {
                alert('请输入旧密码！');
                return;
            }

            if(newPwd == null || trim(newPwd) == '' ) {
                alert('请输入新密码！');
                return;
            }

            if(confirmNewPwd == null || trim(confirmNewPwd) == '' ) {
                alert('请输入新密码确认！');
                return;
            }

            if(newPwd != confirmNewPwd ) {
                alert('新密输入不一致！');
                return;
            }

            $.ajax({
                url:"/user/frontUser!frontUserUpdatePwd.action?refreshForceTimeTick="+new Date().getTime(),
                method:"post",
                type:'post',
                data:{"obj.userId":userId,"obj.password":hex_md5(oldPwd),"newPwd":hex_md5(newPwd),dc:new Date().getTime()},
                success:function(msg){
                    var result = eval('('+msg+')');
                    if(result.success) {
                        alert("密码修改成功!");
                        var url = window.location.href;
                        if(url.indexOf("updatePwd")>=0){
                            url = getParameterFromUrl(null,"nextUrl",null);
                            if(url==null){
                                url =window.location.href;
                                if(url.indexOf('updatePwd_pad.')>=0){
                                    url = 'index_pad.html';
                                }else{
                                    url = 'index_phone.html';
                                }
                            }else{
                                var parameters=['parentId','channelId','contentId','videoId','id'];
                                for(var i= 0,l=parameters.length;i<l;i++){
                                    var p = parameters[i];
                                    var v = getParameterFromUrl(null,p,null);
                                    if(v!=null){
                                        if(url.indexOf("?")>=0){
                                            url+="&";
                                        }else{
                                            url+="?";
                                        }
                                        url+=p+"="+v;
                                    }
                                }
                            }
                        }
                        if(url.indexOf("?")>=0){
                            url +="&";
                        }else{
                            url +="?";
                        }
                        window.location.href=url+'d='+new Date().getTime();
                    }else {
                        alert(result.error);
                    }
                }
            });
        }


    function showReg_win() {
        $.blockUI({ message: $('#reg_win'),
            css:{ marginLeft:     '-200px',marginTop:      '30px'} });
    }

    function closeReg_win() {
        $.unblockUI({ message: $('#reg_win')});
    }


    function register() {
        var reg_userId = document.getElementById('reg_userId').value;
        var reg_pwd = document.getElementById('reg_pwd').value;
        var reg_pwd_confirm = document.getElementById('reg_pwd_confirm').value;
        var name = document.getElementById('reg_name').value;
        var phone = document.getElementById('reg_phone').value;
        var email = document.getElementById('reg_email').value;
        if(!isRegisterUserName(reg_userId)){
            alert("登录名不合法，请使用长度为5-20位长的字母和数字组合，且首位必须是a-z的字符，不能是数字！");
            return;
        }
        if(email!=null&&trim(email)!=''){
            if(!isValidateEmail(email)){
                alert("email格式不正确！");
                return;
            }
        }
        if(phone != ""&&phone!=null){
            if(!isCellPhoneNumber(phone)){
                alert("电话格式不正确，请输入11位手机号码以便于联系！");
                return;
            }
        }

        if(reg_userId == null || trim(reg_userId) == '' ) {
            alert('请输入用户名！');
            return;
        }

        if(reg_pwd == null || trim(reg_pwd) == '' ) {
            alert('请输入密码！');
            return;
        }

        if(reg_pwd != reg_pwd_confirm) {
            alert('两次密码输入不一致！');
            return;
        }

        if(name==null||name==''||name==' '||name=='  '){
            alert("名字为空！");
            return;
        }
        //name = encodeURI(name);
        var genderRadio = document.getElementsByName('radio');
        var gender = 1;
        if(genderRadio && genderRadio!= null && genderRadio.length > 0) {
            for(var i = 0;i<genderRadio.length;i++) {
                if(genderRadio[i].checked) {
                    gender = genderRadio[i].value;
                    break;
                }
            }
        }

        $.ajax({
            url:'/user/frontUser!save.action?refreshForceTimeTick='+new Date().getTime(),
            data:{"obj.userId":reg_userId,"obj.password":hex_md5(reg_pwd),"obj.name":name,
                "obj.mail":email,"obj.gender":gender,"obj.phone":phone,dc:new Date().getTime(),"obj.organizationId":1078},
            method:'post',
            type:'post',
            success:function(msg){
                var result = eval('('+msg+')');
                if(result.success) {
                    alert("注册成功，请等待管理员审核!");
                    var url = window.location.href;
                    if(url.indexOf("reg_")>=0){
                        url = getParameterFromUrl(null,"nextUrl",null);
                        if(url==null){
                            url =window.location.href;
                            if(url.indexOf('reg_pad')>=0){
                                url = 'index_pad.html';
                            }else{
                                url = 'index_phone.html';
                            }
                        }else{
                            var parameters=['parentId','channelId','contentId','videoId','id'];
                            for(var i= 0,l=parameters.length;i<l;i++){
                                var p = parameters[i];
                                var v = getParameterFromUrl(null,p,null);
                                if(v!=null){
                                    if(url.indexOf("?")>=0){
                                        url+="&";
                                    }else{
                                        url+="?";
                                    }
                                    url+=p+"="+v;
                                }
                            }
                        }
                    }
                    if(url.indexOf("?")>=0){
                        url +="&";
                    }else{
                        url +="?";
                    }
                    window.location.href=url+'d='+new Date().getTime();
                }else {
                    alert(result.error);
                }
            }
        });

    }
var caution=false;
function setCookie(name, value, expires, path, domain, secure)
{
    var curCookie = name + "=" + escape(value) +
        ((expires) ? "; expires=" + expires.toGMTString() : "") +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
    if (!caution || (name + "=" + escape(value)).length <= 4000){
        document.cookie = curCookie;
    }else if (confirm("Cookie exceeds 4KB and will be cut!")){
        document.cookie = curCookie;
    }
}
function getCookie(name)
{
    var prefix = name + "=";
    var cookieStartIndex = document.cookie.indexOf(prefix);
    if (cookieStartIndex == -1)
        return null;
    var cookieEndIndex = document.cookie.indexOf(";", cookieStartIndex + prefix.length);
    if (cookieEndIndex == -1) cookieEndIndex = document.cookie.length;
    return unescape(document.cookie.substring(cookieStartIndex + prefix.length, cookieEndIndex));
}
function deleteCookie(name, path, domain)
{
    if (getCookie(name)){
        document.cookie = name + "=" +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}
function fixDate(date){
    var base = new Date(0);
    var skew = base.getTime();
    if (skew > 0){
        date.setTime(date.getTime() - skew);
    }
}

function getCookieName(cspId,indexId){
    return "csp_"+cspId+"_browseMedia_"+indexId;
}

function saveCookie(cookieName,cookieValue){
    var now = new Date();
    fixDate(now);
    now.setTime(now.getTime() + 365 * 24 * 60 * 60 * 1000);
    setCookie(cookieName,
        cookieValue,
        now,null,null,null);
}
