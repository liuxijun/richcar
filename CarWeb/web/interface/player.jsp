<%@ page import="com.fortune.common.Constants" %><%@ page import="java.security.Principal" %><%@ page import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page import="com.fortune.util.AppConfigurator" %><%@ page import="com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface" %><%@ page import="com.fortune.rms.business.user.model.User" %><%@ page import="com.fortune.util.SpringUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-9-23
  Time: 下午4:48
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String serverName = request.getServerName();
    String serverUrl = "http://"+serverName+":"+request.getServerPort()+request.getContextPath();

    String playUrl=request.getParameter("playUrl");
    if(playUrl==null){
        playUrl=serverUrl+"/user/getPlayUrl.m3u8";
    }
    String defaultPlayBandwidth;
    String ip = request.getRemoteAddr();
    Principal principal = request.getUserPrincipal();
    String userName=null;
    FrontUser frontUser = (FrontUser)(session.getAttribute(Constants.SESSION_FRONT_USER));
    if(frontUser!=null){
        userName = frontUser.getUserId();
    }
    if(principal!=null){
        userName = principal.getName();
    }
    if(null==userName){
        userName= AppConfigurator.getInstance().getConfig("system.default.guestId","fortuneGuestFrom");
    }
    if(userName==null||"".equals(userName.trim())){
        userName = null;
    }
    if(serverName.contains("weichai")){
        //如果是潍柴的IP或者本部的帐号，就给1M的连接，否则就给512k的
        if(ip.contains("219.146.118.")||ip.startsWith("10.")||ip.startsWith("172.")){
            defaultPlayBandwidth="Media_Url_1m";
        }else{
            defaultPlayBandwidth="Media_Url_512k";
        }
/*
    }else if(serverName.contains("siva")||serverName.contains("59.78.")){
        defaultPlayBandwidth="Media_Url_1m";
*/
    }else{
        defaultPlayBandwidth = "Media_Url_Source";
    }
    UserLogicInterface userLogicInterface = (UserLogicInterface) SpringUtils.getBean("userLogicInterface",session.getServletContext());
    String token = userLogicInterface.calculateUserToken(userName);
    String serverAddr = request.getServerName();
    if(serverAddr==null){
        serverAddr = "";
    }
    String mywebTitle = "f3b5de6315e7370d8187ac1ddf5776e1,上海复全网络,http://www.fortune-net.cn";
    //如果是潍柴系统，隐藏所有的版权信息
    if(serverAddr.contains("weichai")||serverAddr.startsWith("10.0.")||serverAddr.startsWith("115.28.233.138")||serverAddr.startsWith("119.191.61.")){
        mywebTitle = "";
    }
    session.setAttribute(Constants.USER_PHONE_NUMBER, userName);
%><%if ("true".equals(request.getAttribute("innerHtmlHeader"))){
%><script type="text/javascript">
<%
}
%>
var ckPlayerLogoPic='null';
var ___ckplayer_element_id='ckplayer_a1';
var listener = {
    play:function(){

    },
    stop:function(){

    }
};
function ckcpt() {
    return '';
}
function ckstyle() { //定义总的风格
    return {
        cpath: '',
        language: '',
        flashvars: '',
        setup: '1,1,1,1,1,2,0,1,2,0,0,1,200,0,2,1,0,1,1,1,2,10,3,0,1,2,3000,0,0,1,0,1,1,1,1,1,1,250,0,90',
        pm_bg: '0x000000,100,230,180',
        logo: ckPlayerLogoPic,
        mylogo: 'null',
        pm_mylogo: '1,1,-100,-55',
        pm_logo: '0,0,0,0',
        control_rel: 'related.swf,related.xml,0',
        control_pv: 'Preview.swf,105,2000',
        pm_repc: '',
        pm_spac: '|',
        pm_fpac: 'file->f',
        pm_advtime: '2,0,-110,10,0,300,0',
        pm_advstatus: '1,2,2,-200,-40',
        pm_advjp: '1,1,2,2,-100,-40',
        pm_padvc: '2,0,-10,-10',
        pm_advms: '2,2,-46,-56',
        pm_zip: '1,1,-20,-8,1,0,0',
        pm_advmarquee: '1,2,50,-60,50,18,0,0x000000,50,0,20,1,15,2000',
        mainfuntion:'',
        flashplayer:'',
        calljs:'ckmarqueeadv',
        myweb: escape('<%=mywebTitle%>'),
        cpt_lights: '0',
        cpt_list: ckcpt()
    };
}
function initCKPlayerEnv() {
    var CKobject = {
        _K_: function(d){return document.getElementById(d);},
        _T_: false,
        _M_: false,
        _I_: null,
        _O_: {},
        uaMatch:function(u,rMsie,rFirefox,rOpera,rChrome,rSafari,rSafari2,mozilla,mobile){
            var match = rMsie.exec(u);

            if (match != null) {
                return {
                    b: 'IE',
                    v: match[2] || '0'
                }
            }
            match = rFirefox.exec(u);
            if (match != null) {
                return {
                    b: match[1] || '',
                    v: match[2] || '0'
                }
            }
            match = rOpera.exec(u);
            if (match != null) {
                return {
                    b: match[1] || '',
                    v: match[2] || '0'
                }
            }
            match = rChrome.exec(u);
            if (match != null) {
                return {
                    b: match[1] || '',
                    v: match[2] || '0'
                }
            }
            match = rSafari.exec(u);
            if (match != null) {
                return {
                    b: match[2] || '',
                    v: match[1] || '0'
                }
            }
            match = rSafari2.exec(u);
            if (match != null) {
                return {
                    b: match[2] || '',
                    v: match[1] || '0'
                }
            }
            match = mozilla.exec(u);
            if (match != null) {
                return {
                    b: match[2] || '',
                    v: match[1] || '0'
                }
            }
            match = mobile.exec(u);
            if (match != null) {
                return {
                    b: match[2] || '',
                    v: match[1] || '0'
                }
            }
            if (match != null) {
                return {
                    b: 'unknown',
                    v: '0'
                }
            }
        },
        browser: function() {
            var u = navigator.userAgent,
                    rMsie = /(msie\s|trident.*rv:)([\w.]+)/,
                    rFirefox = /(firefox)\/([\w.]+)/,
                    rOpera = /(opera).+version\/([\w.]+)/,
                    rChrome = /(chrome)\/([\w.]+)/,
                    rSafari = /version\/([\w.]+).*(safari)/,
                    rSafari2 = /(safari)\/([\w.]+)/,
                    mozilla = /(mozilla)\/([\w.]+)/,
                    mobile = /(mobile)\/([\w.]+)/;
            var b;
            var v;
            var c = u.toLowerCase();
            var d = this.uaMatch(c,rMsie,rFirefox,rOpera,rChrome,rSafari,rSafari2,mozilla,mobile);
            if (d.b) {
                b = d.b;
                v = d.v;
            }
            return {B: b, V: v};
        },
        Platform: function() {
            var w = '';
            var u = navigator.userAgent,
                    app = navigator.appVersion;
            var b = {
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1,
                iPad: u.indexOf('iPad') > -1,
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
                webKit: u.indexOf('AppleWebKit') > -1,
                trident: u.indexOf('Trident') > -1,
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,
                presto: u.indexOf('Presto') > -1,
                mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/),
                webApp: u.indexOf('Safari') == -1
            };
            for (var k in b) {
                if (b[k]) {
                    w = k;
                    break;
                }
            }
            return w;
        },
        isHTML5:function(){
            return !!document.createElement('video').canPlayType;
        },
        getType:function(){
            return this._T_;
        },
        getVideo: function() {
            var v = '';
            var s = this._E_['v'];
            if (s) {
                for (var i = 0; i < s.length; i++) {
                    var a = s[i].split('->');
                    if (a.length >= 1 && a[0] != '') {
                        v += '<source src="' + a[0] + '"';
                    }
                    if (a.length >= 2 && a[1] != '') {
                        v += ' type="' + a[1] + '"';
                    }
                    v += '>';
                }
            }
            return v;
        },
        getVars: function(k) {
            var o=this._A_;
            if (typeof(o) == 'undefined') {
                return null;
            }
            if (k in o) {
                return o[k];
            } else {
                return null;
            }
        },
        getParams: function() {
            var p = '';
            if (this._A_) {
                if (parseInt(this.getVars('p')) == 1) {
                    p += ' autoplay="autoplay"';
                }
                if (parseInt(this.getVars('e')) == 1) {
                    p += ' loop="loop"';
                }
                if (parseInt(this.getVars('p')) == 2) {
                    p += ' preload="metadata"';
                }
                if (this.getVars('i')) {
                    p += ' poster="' + this.getVars('i') + '"';
                }
            }
            return p;
        },
        getpath: function(z) {
            var f='CDEFGHIJKLMNOPQRSTUVWXYZcdefghijklmnopqrstuvwxyz';
            var w=z.substr(0,1);
            if(f.indexOf(w)>-1 && (z.substr(0,4)==w+'://' || z.substr(0,4)==w+':\\')){
                return z;
            }
            var d = unescape(window.location.href).replace('file:///', '');
            var k = parseInt(document.location.port);
            var u = document.location.protocol + '//' + document.location.hostname;
            var l = '',
                    e = '',
                    t = '';
            var s = 0;
            var r = unescape(z).split('//');
            if (r.length > 0) {
                l = r[0] + '//'
            }
            var h = 'http|https|ftp|rtsp|mms|ftp|rtmp|file';
            var a = h.split('|');
            if (k != 80 && k) {
                u += ':' + k;
            }
            for (i = 0; i < a.length; i++) {
                if ((a[i] + '://') == l) {
                    s = 1;
                    break;
                }
            }
            if (s == 0) {
                if (z.substr(0, 1) == '/') {
                    t = u + z;
                } else {
                    e = d.substring(0, d.lastIndexOf('/') + 1).replace('\\', '/');
                    var w = z.replace('../', './');
                    var u = w.split('./');
                    var n = u.length;
                    var r = w.replace('./', '');
                    var q = e.split('/');
                    var j = q.length - n;
                    for (i = 0; i < j; i++) {
                        t += q[i] + '/';
                    }
                    t += r;
                }
            } else {
                t = z;
            }
            return t;
        },
        getXhr: function() {
            var x;
            try {
                x = new ActiveXObject('Msxml2.XMLHTTP');
            } catch(e) {
                try {
                    x = new ActiveXObject('Microsoft.XMLHTTP');
                } catch(e) {
                    x = false;
                }
            }
            if (!x && typeof XMLHttpRequest != 'undefined') {
                x = new XMLHttpRequest();
            }
            return x;
        },
        getX: function(){
            var f='ckstyle()';
            if (this.getVars('x')) {
                f=this.getVars('x')+'()';
            }
            try {
                if (typeof(eval(f)) == 'object') {
                    this._X_ = eval(f);
                }
            } catch(e) {
                try {
                    if (typeof(eval(ckstyle)) == 'object') {
                        this._X_ = ckstyle();
                    }
                } catch(e) {
                    this._X_ = ckstyle();
                }
            }
        },
        getSn: function(s, n) {
            if(n>=0){
                return this._X_[s].split(',')[n];
            }
            else{
                return this._X_[s];
            }
        },
        getUrl: function(L, B) {
            var b = ['get', 'utf-8'];
            if (L && L.length == 2) {
                var a = L[0];
                var c = L[1].split('/');
                if (c.length >= 2) {
                    b[0] = c[1];
                }
                if (c.length >= 3) {
                    b[1] = c[2];
                }
                this.ajax(b[0], b[1], a,
                        function(s) {
                            var C = CKobject;
                            if (s && s != 'error') {
                                var d = '',
                                        e = s;
                                if (s.indexOf('}') > -1) {
                                    var f = s.split('}');
                                    for (var i = 0; i < f.length - 1; i++) {
                                        d += f[i] + '}';
                                        var h = f[i].replace('{', '').split('->');
                                        if (h.length == 2) {
                                            C._A_[h[0]] = h[1];
                                        }
                                    }
                                    e = f[f.length - 1];
                                }
                                C._E_['v'] = e.split(',');
                                if (B) {
                                    C.showHtml5();
                                } else {
                                    C.changeParams(d);
                                    C.newAdr();
                                }
                            }
                        });
            }
        },
        getflashvars: function(s) {
            var v = '',
                    i = 0;
            if (s) {
                for (var k in s) {
                    if (i > 0) {
                        v += '&';
                    }
                    if (k == 'f' && s[k] && ! this.getSn('pm_repc',-1)) {
                        s[k] = this.getpath(s[k]);
                        if (s[k].indexOf('&') > -1) {
                            s[k] = encodeURIComponent(s[k]);
                        }
                    }
                    if (k == 'y' && s[k]) {
                        s[k] = this.getpath(s[k]);
                    }
                    v += k + '=' + s[k];
                    i++;
                }
            }
            return v;
        },
        getparam: function(s) {
            var w = '',
                    v = '',
                    o = {
                        allowScriptAccess: 'always',
                        allowFullScreen: true,
                        quality: 'high',
                        bgcolor: '#000'
                    };
            if (s) {
                for (var k in s) {
                    o[k] = s[k];
                }
            }
            for (var e in o) {
                w += e + '="' + o[e] + '" ';
                v += '<param name="' + e + '" value="' + o[e] + '" />';
            }
            w = w.replace('movie=', 'src=');
            return {
                w: w,
                v: v
            };
        },
        getObjectById: function(s) {
            if (this._T_) {
                return this;
            }
            var X = null,
                    Y = this._K_(s),
                    r = 'embed';
            if (Y && Y.nodeName == 'OBJECT') {
                if (typeof Y.SetVariable != 'undefined') {
                    X = Y;
                } else {
                    var Z = Y.getElementsByTagName(r)[0];
                    if (Z) {
                        X = Z;
                    }
                }
            }
            return X;
        },
        ajax: function(b, u, s, f) {
            var x = this.getXhr();
            var a = [],
                    m = '';
            if (b == 'get') {
                if (s.indexOf('?') > -1) {
                    m = s + '&t=' + new Date().getTime();
                } else {
                    m = s + '?t=' + new Date().getTime();
                }
                x.open('get', m);
            } else {
                a = s.split('?');
                s = a[0],
                        m = a[1];
                x.open('post', s, true);
            }
            x.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            x.setRequestHeader('charset', u);
            if (b == 'post') {
                x.send(m);
            } else {
                x.send(null);
            }
            x.onreadystatechange = function() {
                if (x.readyState == 4) {
                    var g = x.responseText;
                    if (g != '') {
                        f(g);
                    } else {
                        f(null);
                    }
                }
            }
        },
        addListener: function(e, f) {
            var o=CKobject._V_;
            if (o.addEventListener) {
                try{
                    o.addEventListener(e, f, false);
                }
                catch (e) {
                    this.getNot();
                }
            }
            else if (o.attachEvent) {
                try{
                    o.attachEvent('on' + e, f);
                }
                catch(e){
                    this.getNot();
                }
            }
            else {
                o['on' + e] = f;
            }
        },
        removeListener: function( e, f) {
            var o=CKobject._V_;
            if (o.removeEventListener) {
                try{
                    o.removeEventListener(e, f, false);
                }
                catch(e){
                    this.getNot();
                }
            }
            else if (o.detachEvent) {
                try{
                    o.detachEvent('on' + e, f);
                }
                catch(e){
                    this.getNot();
                }
            }
            else {
                o['on' + e] = null;
            }
        },
        Flash: function() {
            var f = false,v = 0;
            if (document.all  || this.browser()['B'].toLowerCase().indexOf('ie')>-1) {
                try {
                    var s = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
                    f = true;
                    var z = s.GetVariable('$version');
                    v = parseInt(z.split(' ')[1].split(',')[0]);
                } catch(e) {}
            } else {
                if (navigator.plugins && navigator.plugins.length > 0) {
                    var s = navigator.plugins['Shockwave Flash'];
                    if (s) {
                        f = true;
                        var w = s.description.split(' ');
                        for (var i = 0; i < w.length; ++i) {
                            if (isNaN(parseInt(w[i]))) continue;
                            v = parseInt(w[i]);
                        }
                    }
                }
            }
            return {
                f: f,
                v: v
            };
        },
        repairParameters:function(v){
            if(v){
                var url = v['a'];
                if(url!=null&&typeof(url)!='undefined'){
                    url = url.replace(/&/g,'%26');
                    v['a']=url;
                }
            }
        },

        embed:function(f,d,i,w,h,b,v,e,p){
            var s=['all'];
            if(b){
                if(this.isHTML5()){
                    this.embedHTML5(d,i,w,h,e,v,s);
                }
                else{
                    this.embedSWF(f,d,i,w,h,v,p);
                }
            }
            else{
                if(this.Flash()['f'] && parseInt(this.Flash()['v'])>10){
                    this.embedSWF(f,d,i,w,h,v,p);
                }
                else if(this.isHTML5()){
                    this.embedHTML5(d,i,w,h,e,v,s);
                }
                else{
                    this.embedSWF(f,d,i,w,h,v,p);
                }
            }
        },
        embedSWF: function(C, D, N, W, H, V, P) {
            this.repairParameters(V);
            if (!N) {
                N = 'ckplayer_a1'
            }
            if (!P) {
                P = {
                    bgcolor: '#FFF',
                    allowFullScreen: true,
                    allowScriptAccess: 'always',
                    wmode:'transparent'
                };
            }
            this._A_=V;
            this.getX();
            var u = 'undefined',
                    g = false,
                    j = document,
                    r = 'http://www.macromedia.com/go/getflashplayer',
                    t = '<a href="' + r + '" target="_blank">请点击此处下载安装最新的flash插件</a>',
                    error = {
                        w: '您的网页不符合w3c标准，无法显示播放器',
                        f: '您没有安装flash插件，无法播放视频，' + t,
                        v: '您的flash插件版本过低，无法播放视频，' + t
                    },
                    w3c = typeof j.getElementById != u && typeof j.getElementsByTagName != u && typeof j.createElement != u,
                    i = 'id="' + N + '" name="' + N + '" ',
                    s = '',
                    l = '';
            P['movie'] = C;
            P['flashvars'] = this.getflashvars(V);
            s += '<object pluginspage="http://www.macromedia.com/go/getflashplayer" ';
            s += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
            s += 'codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,0,0" ';
            s += 'width="' + W + '" ';
            s += 'height="' + H + '" ';
            s += i;
            s += 'align="middle">';
            s += this.getparam(P)['v'];
            s += '<embed ';
            s += this.getparam(P)['w'];
            s += ' width="' + W + '" height="' + H + '" name="' + N + '" id="' + N + '" align="middle" ' + i;
            s += 'type="application/x-shockwave-flash" pluginspage="' + r + '" />';
            s += '</object>';
            if (!w3c) {
                l = error['w'];
                g = true;
            } else {
                if (!this.Flash()['f']) {
                    l = error['f'];
                    g = true;
                } else {
                    if (this.Flash()['v'] < 10) {
                        l = error['v'];
                        g = true;
                    } else {
                        l = s;
                        this._T_=false;
                    }
                }
            }
            if (l) {
                this._K_(D).innerHTML = l;
            }
            if (g){
                this._K_(D).style.color = '#0066cc';
                this._K_(D).style.lineHeight = this._K_(D).style.height;
                this._K_(D).style.textAlign= 'center';
            }
        },
        embedHTML5: function(C, P, W, H, V, A, S) {
            this._E_ = {
                c: C,
                p: P,
                w: W,
                h: H,
                v: V,
                s: S
            };
            this._A_ = A;
            this.getX();
            b = this.browser()['B'],
                    v = this.browser()['V'],
                    x = v.split('.'),
                    t = x[0],
                    m = b + v,
                    n = b + t,
                    w = '',
                    s = false,
                    f = this.Flash()['f'],
                    a = false;
            if (!S) {
                S = ['iPad', 'iPhone', 'ios'];
            }
            for (var i = 0; i < S.length; i++) {
                w = S[i];
                if (w.toLowerCase() == 'all') {
                    s = true;
                    break;
                }
                if (w.toLowerCase() == 'all+false' && !f) {
                    s = true;
                    break;
                }
                if (w.indexOf('+') > -1) {
                    w = w.split('+')[0];
                    a = true;
                } else {
                    a = false;
                }
                if (this.Platform() == w || m == w || n == w || b == w) {
                    if (a) {
                        if (!f) {
                            s = true;
                            break;
                        }
                    }else {
                        s = true;
                        break;
                    }
                }
            }
            if (s) {
                if (V) {
                    var l = V[0].split('->');
                    if (l && l.length == 2 && l[1].indexOf('ajax') > -1) {
                        this.getUrl(l, true);
                        return;
                    }
                }
                this.showHtml5();
            }
        },
        status: function() {
            this._H_ = parseInt(this.getSn('setup', 20));
            var f='ckplayer_status';
            if (this.getSn('calljs', 0)!='') {
                f=this.getSn('calljs', 0);
            }
            try {
                if (typeof(eval(f)) == 'function') {
                    this._J_=eval(f);
                    this._M_=true;
                    return true;
                }
            } catch(e) {
                try {
                    if (typeof(eval(ckplayer_status)) == 'function') {
                        this._J_=ckplayer_status;
                        this._M_=true;
                        return true;
                    }
                } catch(e) {
                    return false;
                }
            }
            return false;
        },
        showHtml5: function() {
            var C = CKobject;
            var p = this._E_['p'],
                    a = this._E_['v'],
                    c = C._E_['c'],
                    b = false;
            var v = '<video controls id="' + p + '" width="' + this._E_['w'] + '" height="' + this._E_['h'] + '"' + C.getParams() + '>' + C.getVideo() + '</video>';
            C._K_(c).innerHTML = v;
            C._K_(c).style.width=this._E_['w'].toString().indexOf('%')>-1?this._E_['w']:this._E_['w']+'px';
            C._K_(c).style.height=this._E_['h'].toString().indexOf('%')>-1?this._E_['h']:this._E_['h']+'px';
            C._K_(c).style.backgroundColor = '#000';
            C._V_ = this._K_(p);
            C._P_ = false;
            C._T_ = true;
            if (C.getVars('loaded')!='') {
                var f=C.getVars('loaded')+'()';
                try {
                    if (typeof(eval(f)) == 'function') {
                        eval(f);
                    }
                } catch(e) {
                    try {
                        if (typeof(eval(loadedHandler)) == 'function') {
                            loadedHandler();
                        }
                    } catch(e) {
                    }

                }
            }
            C.status();
            C.addListener('play', C.playHandler);
            C.addListener('pause', C.playHandler);
            C.addListener('error', C.errorHandler);
            C.addListener('emptied', C.errorHandler);
            C.addListener('loadedmetadata', C.loadedMetadataHandler);
            C.addListener('ended', C.endedHandler);
            C.addListener('volumechange', C.volumeChangeHandler);
        },
        videoPlay: function() {
            if (this._T_) {
                this._V_.play();
            }
        },
        videoPause: function() {
            if (this._T_) {
                this._V_.pause();
            }
        },
        playOrPause: function() {
            if (this._T_) {
                if (this._V_.paused) {
                    this._V_.play();
                } else {
                    this._V_.pause();
                }
            }
        },
        fastNext: function() {
            if (this._T_) {
                this._V_['currentTime'] = this._V_['currentTime'] + 10;
            }
        },
        fastBack: function() {
            if (this._T_) {
                this._V_['currentTime'] = this._V_['currentTime'] - 10;
            }
        },
        changeVolume: function(n) {
            if (this._T_) {
                this._V_['volume'] = n * 0.01;
            }
        },
        videoSeek: function(t) {
            if (this._T_) {
                this._V_['currentTime'] = t;
            }
        },
        newAddress: function(u) {
            var s = [];
            if (u) {
                s = this.isHtml5New(u);
            } else {
                return;
            }
            if (s && this._T_) {
                this.changeParams(u);
                var l = s[0].split('->');
                if (l && l.length == 2 && l[1].indexOf('ajax') > -1) {
                    this.getUrl(l, false);
                    return;
                }
                this._E_['v'] = s;
                this.newAdr();
            }
        },
        quitFullScreen:function() {
            if(document.cancelFullScreen) {
                document.cancelFullScreen();
            }
            else if(document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if(document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }

        },
        changeStatus:function(n){
            this._H_=n;
        },
        newAdr: function() {
            this._V_.pause();
            this._V_['innerHTML'] = this.getVideo();
            this._V_.load();
        },
        isHtml5New: function(s) {
            if (s.indexOf('html5') == -1) {
                return false;
            }
            var a = s.replace(/{/g, '');
            var b = a.split('}');
            var c = '';
            for (var i = 0; i < b.length; i++) {
                if (b[i].indexOf('html5') > -1) {
                    c = b[i].replace('html5->', '').split(',');
                    break;
                }
            }
            return c;
        },
        changeParams: function(f) {
            if (f) {
                var a = f.replace(/{/g, '');
                var b = a.split('}');
                var c = '';
                for (var i = 0; i < b.length; i++) {
                    var d = b[i].split('->');
                    if(d.length == 2){
                        switch(d[0]){
                            case 'p':
                                if(parseInt(d[1]) == 1){
                                    this._V_.autoplay = true;
                                }
                                else if(parseInt(d[1]) == 2){
                                    this._V_.preload = 'metadata';
                                }
                                else{
                                    this._V_.autoplay = false;
                                    if(this._I_!=null){
                                        clearInterval(this._I_);
                                        this._I_=null;
                                    }
                                }
                                break;
                            case 'e':
                                if(parseInt(d[1]) == 1){
                                    this._V_.loop = true;
                                }
                                else{
                                    this._V_.loop = false;
                                }
                                break;
                            case 'i':
                                this._V_.poster = d[1];
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        },
        frontAdPause: function(s) {
            this.getNot();
        },
        frontAdUnload: function() {
            this.getNot();
        },
        changeFace: function(s) {
            this.getNot();
        },
        plugin: function(a, b, c, d, e, f, g) {
            this.getNot();
        },
        videoClear: function() {
            this.getNot();
        },
        videoBrightness: function(s) {
            this.getNot();
        },
        videoContrast: function(s) {
            this.getNot();
        },
        videoSaturation: function(s) {
            this.getNot();
        },
        videoSetHue: function(s) {
            this.getNot();
        },
        videoWAndH: function(a, b) {
            this.getNot();
        },
        videoWHXY: function(a, b, c, d) {
            this.getNot();
        },
        changeFlashvars: function(a) {
            this.getNot();
        },
        changeMyObject: function(a, b) {
            this.getNot();
        },
        getMyObject: function(a, b) {
            this.getNot();
        },
        changeeFace: function() {
            this.getNot();
        },
        changeStyle: function(a, b) {
            this.getNot();
        },
        promptLoad: function() {
            this.getNot();
        },
        promptUnload: function() {
            this.getNot();
        },
        marqueeLoad: function(a,b) {
            this.getNot();
        },
        marqueeClose: function(s) {
            this.getNot();
        },
        getNot: function() {
            var s='The ckplayer\'s API for HTML5 does not exist';
            return s;
        },
        volumeChangeHandler: function() {
            var C = CKobject;
            if (C._V_.muted) {
                C.returnStatus('volumechange:0', 1);
                C._O_['volume'] = 0;
                C._O_['mute'] = true;
            } else {
                C._O_['mute'] = false;
                C._O_['volume'] = C._V_['volume'] * 100;
                C.returnStatus('volumechange:'+C._V_['volume'] * 100, 1);
            }
        },
        endedHandler: function() {
            var C = CKobject;
            var e=parseInt(C.getVars('e'));
            C.returnStatus('ended', 1);
            if(C._I_){
                clearInterval(C._I_);
                C._I_=null;
            }
            if ( e!= 0 && e !=4 && e !=6) {
                return;
            }
            if(e==6){
                this.quitFullScreen();
            }
            var f='playerstop()';
            if (C.getSn('calljs', 2)!='') {
                f=C.getSn('calljs', 2)+'()';
            }
            try {
                if (typeof(eval(f)) == 'function') {
                    eval(f);
                    return;
                }
            } catch(e) {
                try {
                    if (typeof(eval(playerstop)) == 'function') {
                        playerstop();
                        return;
                    }
                } catch(e) {
                    return;
                }
            }
        },
        loadedMetadataHandler: function() {
            var C = CKobject;
            C.returnStatus('loadedmetadata', 1);
            C._O_['totaltime'] = C._V_['duration'];
            C._O_['width'] = C._V_['width'];
            C._O_['height'] = C._V_['height'];
            C._O_['awidth'] = C._V_['videoWidth'];
            C._O_['aheight'] = C._V_['videoHeight'];
            if (C._V_.defaultMuted) {
                C.returnStatus('volumechange:0', 1);
                C._O_['mute'] = true;
                C._O_['volume'] = 0;
            } else {
                C._O_['mute'] = false;
                C._O_['volume'] = C._V_['volume'] * 100;
                C.returnStatus('volumechange:'+C._V_['volume'] * 100, 1);
            }
        },
        errorHandler: function() {
            CKobject.returnStatus('error', 1);
        },
        playHandler: function() {
            var C = CKobject;
            if (C._V_.paused) {
                C.returnStatus('play', 1);
                C.addO('play', false);
                if(C._I_!=null){
                    clearInterval(C._I_);
                    C._I_=null;
                }
            } else {
                C.returnStatus('pause', 1);
                C.addO('play', true);
                if (!C._P_) {
                    C.returnStatus('play', 1);
                    C._P_ = true;
                }
                C._I_ = setInterval(C.playTime, parseInt( C.getSn('setup', 37)));
            }
        },
        returnStatus: function(s, j) {
            var h = s;
            if (this._H_ == 3) {
                h = this._E_['p'] +'->'+ h;
            }
            if (this._M_ && j <= this._H_ ) {
                this._J_(h);
            }
        },
        addO: function(s, z) {
            this._O_[s] = z;
        },
        getStatus: function() {
            return this._O_;
        },
        playTime: function() {
            var C = CKobject;
            var t = C._V_['currentTime'];
            C._O_['time'] = t;
            C.returnStatus('time:' + t, 1);
        }
    };
    window.CKobject = CKobject;
}

function loadedHandler(){
    if(CKobject.getObjectById('ckplayer_a1').getType()){//说明使用html5播放器
        //alert('播放器已加载，调用的是HTML5播放模块');
    }
    else{
        //alert('播放器已加载，调用的是Flash播放模块');
    }
}
function ckplayer_status(str){

}
function getDefaultValue(val,defaultVal){
    if(typeof(val)=="undefined"||val==null){
        return defaultVal;
    }
    return val;
}

function openPlayer(contentId,clipNo,playerContentId,playerId,isLive,width,height,backgroundPic,playDate,dur){
    if(typeof(backgroundPic)!='undefined'&&backgroundPic!=null&&backgroundPic!='null'){
        ckPlayerLogoPic = backgroundPic;
    }else{
        ckPlayerLogoPic = 'null';
    }
    ___ckplayer_element_id = playerId;
    initCKPlayerEnv();
    var playUrl = "<%=playUrl%>?uid="+contentId+"_|_<%=defaultPlayBandwidth%>_|_"+clipNo+"_|_m3u8_|_"+
    new Date().getTime()+"_|_<%=userName%>_|_<%=token%>";
    $.ajax({
        url:'<%=serverUrl%>/user/getLiveUrl.jsp?contentId='+contentId+"&clipNo="+clipNo+"&jsonFormat=true&token=<%=token%>",
        dataType:'json',
        success:function(data){
            var isVod = data['isVod'];
            var url = data['url'];
            var liveType = 1;
            if(url!=null){
                if(typeof(playDate)!='undefined'&&playDate!=null&&playDate!='null'){
                    //替换掉/live/hls为/vod/rc/
                    var p = url.indexOf("/live/hls/");
                    if(p>0){
                        url = url.substring(0,p)+"/vod/rc/"+url.substring(p+10);
                    }
                    p = url.indexOf("/vod/rc/11");
                    if(p>0){
                        url = url.substring(0,p)+"/vod/rc/10"+url.substring(p+10);
                    }
                    if(url.indexOf("?")>0){
                        url+="&";
                    }else{
                        url+="?";
                    }
                    url+="stime="+playDate+"&dur="+dur;
                    liveType = 0;
                }
                url=url.replace(/&/g,"%26");
            }
            if(isVod||url==null||typeof(url)=='undefined'){
                url = "<%=playUrl%>?uid="+contentId+"_|_<%=defaultPlayBandwidth%>_|_"+clipNo+"_|_m3u8_|_"+
                new Date().getTime()+"_|_<%=userName%>_|_<%=token%>";
                liveType = 0;
            }
            playMovie(url,liveType,"<%=serverUrl%>/ckplayer/swf/",playerId,playerContentId,width,height);
        }
    });

    /*
        if(!isLive){
            playMovie(playUrl,0,"<%=serverUrl%>/ckplayer/swf/",playerId,playerContentId,width,height);
    }else{
    }
*/
}
// added by mlwang, player live m3u8 url
function playLiveDirectly(m3u8URL, playerId, containerId, width, height){
    console.info("play live:" + m3u8URL);
    ckPlayerLogoPic = 'null';
    initCKPlayerEnv();
    playMovie(m3u8URL, true, "<%=serverUrl%>/ckplayer/swf/", playerId, containerId, width,height );
}
var ___is_live__ = false;
function playMovie(willPlayUrl,isLive,ckPlayerPath,ckPlayerObjId,ckPlayerContainId,width,height){
    if(isLive == null || isLive<0 || typeof(isLive)=="undefined"){
        if(willPlayUrl.indexOf("/hls/")>0||willPlayUrl.indexOf("/live/")>0||willPlayUrl.indexOf("z.m3u8")>0){
            isLive = 1;
        }else{
            isLive=0;
        }
    }
    ___is_live__ = isLive == 1;
    ckPlayerPath = getDefaultValue(ckPlayerPath,"/ckplayer/swf/");
    ckPlayerContainId =getDefaultValue(ckPlayerContainId,"a1");
    ckPlayerObjId = getDefaultValue(ckPlayerObjId,"ckplayer_"+ckPlayerContainId);
    width = getDefaultValue(width,640);
    height = getDefaultValue(height,480);
    var flashvars={
        f:ckPlayerPath+'m3u8.swf',
        a:willPlayUrl,
        c:0,
        p:1,
        s:4,
        lv:isLive//注意，如果是直播，需设置lv:1
    };

    var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always'};//这里定义播放器的其它参数如背景色（跟flashvars中的b不同），是否支持全屏，是否支持交互
    var video=[willPlayUrl];//'http://movie.ks.js.cn/flv/other/1_0.mp4->video/mp4','http://www.ckplayer.com/webm/0.webm->video/webm','http://www.ckplayer.com/webm/0.ogv->video/ogg'];
    ___ckplayer_element_id = ckPlayerObjId;
    CKobject.embed(ckPlayerPath+'ckplayer.swf',ckPlayerContainId,ckPlayerObjId,width,height,false,flashvars,video,params);

}


/*
 以上代码演示的兼容flash和html5环境的。如果只调用flash播放器或只调用html5请看其它示例
 */
function videoLoadJs(s){
    alert("执行了播放");
}
function playerstop(){
    playUrl(currentPlayUrlIdx,currentPlayServerIdx);
}
var _nn=0;//用来计算实时监听的条数的，超过100条记录就要删除，不然会消耗内存

<%--
function getstart(){
    var a=CKobject.getObjectById('ckplayer_a1').getStatus();
    var ss='';
    for (var k in a){
        ss+=k+":"+a[k]+'\n';
    }
    alert(ss);
}
function ckadjump(){
    alert('这里演示了点击跳过广告按钮后的执行的动作，如果注册会员可以做成直接跳过的效果。');
}

//开关灯
/*
 该文件是从网上收集的方法，用来做开关灯的，相对于原来的程序做了一些改变，兼容了IE10
 */
var Offlights = {
    create: function() {
        return function() {
            this.initialize.apply(this, arguments)
        }
    }
};
var OverLay = Offlights.create();
OverLay.prototype = {
    initialize: function(options) {
        this.SetOptions(options);
        this.browser = (function(ua){
            var a=new Object();
            var b = {
                msie: /msie/.test(ua) && !/opera/.test(ua),
                opera: /opera/.test(ua),
                safari: /webkit/.test(ua) && !/chrome/.test(ua),
                firefox: /firefox/.test(ua),
                chrome: /chrome/.test(ua)
            };
            var vMark = "";
            for (var i in b) {
                if (b[i]) { vMark = "safari" == i ? "version" : i; break; }
            }
            b.version = vMark && RegExp("(?:" + vMark + ")[\\/: ]([\\d.]+)").test(ua) ? RegExp.$1 : "0";
            b.ie = b.msie;
            b.ie6 = b.msie && parseInt(b.version, 10) == 6;
            b.ie7 = b.msie && parseInt(b.version, 10) == 7;
            b.ie8 = b.msie && parseInt(b.version, 10) == 8;
            a.B=vMark;
            a.V=b.version;
            return a;
        })(window.navigator.userAgent.toLowerCase());
        this.isIE = this.browser['B']=='msie' ? true : false;
        this.isIE6 = (this.isIE && this.browser['V']==6)?true:false;
        this._K_ = function(id) {return "string" == typeof id ? document.getElementById(id) : id};
        this.Lay = this._K_(this.options.Lay) || document.body.insertBefore(document.createElement("div"), document.body.childNodes[0]);
        this.Color = this.options.Color;
        this.Opacity = parseInt(this.options.Opacity);
        this.zIndex = parseInt(this.options.zIndex);
        with(this.Lay.style) {
            display = "none";
            zIndex = this.zIndex;
            left = top = 0;
            position = "fixed";
            width = height = "100%"
        }
        if (this.isIE6) {
            this.Lay.style.position = "absolute";
            this._resize = this.Bind(this,
                    function() {
                        this.Lay.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px";
                        this.Lay.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px"
                    });
            this.Lay.innerHTML = ''
        }
    },
    Bind:function(object, fun) {
        return function() {
            return fun.apply(object, arguments)
        }
    },
    Extend :function(destination, source) {
        for (var property in source) {
            destination[property] = source[property]
        }
    },
    SetOptions: function(options) {
        this.options = {
            Lay: null,
            Color: "#000",
            Opacity: 100,
            zIndex: 50
        };
        this.Extend(this.options, options || {})
    },
    Show: function() {
        if (this.isIE6) {
            this._resize();
            window.attachEvent("onresize", this._resize)
        }
        with(this.Lay.style) {
            this.isIE ? filter = "alpha(opacity:" + this.Opacity + ")": opacity = this.Opacity / 100;
            backgroundColor = this.Color;
            display = "block"
        }
    },
    Close: function() {
        this.Lay.style.display = "none";
        if (this.isIE6) {
            window.detachEvent("onresize", this._resize)
        }
    }
};
var LightBox = Offlights.create();
LightBox.prototype = {
    initialize: function(options) {
        this.OverLay = new OverLay(options);
    },
    Show: function(options) {
        this.OverLay.Show();
    },
    Close: function() {
        this.OverLay.Close();
    }
};

var box = new LightBox();
function closelights(){//关灯
    box.Show();
    CKobject._K_('a1').style.width='940px';
    CKobject._K_('a1').style.height='550px';
    CKobject.getObjectById('ckplayer_a1').width=940;
    CKobject.getObjectById('ckplayer_a1').height=550;
}
function openlights(){//开灯
    box.Close();
    CKobject._K_('a1').style.width='600px';
    CKobject._K_('a1').style.height='400px';
    CKobject.getObjectById('ckplayer_a1').width=600;
    CKobject.getObjectById('ckplayer_a1').height=400;
}
--%>
function changePrompt(){
    CKobject.getObjectById('ckplayer_a1').promptUnload();//卸载掉目前的
    CKobject.getObjectById('ckplayer_a1').changeFlashvars('{k->10|20|30}{n->重设提示点一|重设提示点二|重设提示点三}');
    CKobject.getObjectById('ckplayer_a1').promptLoad();//重新加载
}
function addflash(){
    if(CKobject.Flash()['f']){
        CKobject._K_('a1').innerHTML='';
        CKobject.embedSWF('ckplayer.swf','a1','ckplayer_a1','600','400',flashvars,params);
    }
    else{
        alert('该环境中没有安装flash插件，无法切换');
    }
}
function addhtml5(){
    if(CKobject.isHTML5()){
        support=['all'];
        CKobject._K_('a1').innerHTML='';
        CKobject.embedHTML5('a1',___ckplayer_element_id,600,400,video,flashvars,support);
    }
    else{
        alert('该环境不支持html5，无法切换');
    }
}

function addListener(){

    try {
        if(CKobject.getObjectById(___ckplayer_element_id).getType()){//说明使用html5播放器
            CKobject.getObjectById(___ckplayer_element_id).addListener('play',playHandler);
        }
        else{
            CKobject.getObjectById(___ckplayer_element_id).addListener('play','playHandler');
        }
    } catch (e) {
    }
}
function playHandler(){
    alert('因为注册了监听播放，所以弹出此内容，删除监听将不再弹出');
}
function removeListener(){//删除监听事件
    if(CKobject.getObjectById(___ckplayer_element_id).getType()){//说明使用html5播放器
        CKobject.getObjectById(___ckplayer_element_id).removeListener('play',playHandler);
    }
    else{
        CKobject.getObjectById(___ckplayer_element_id).removeListener('play','playHandler');
    }
}
<%if ("true".equals(request.getAttribute("innerHtmlHeader"))){
%></script>
<%
}
%>
