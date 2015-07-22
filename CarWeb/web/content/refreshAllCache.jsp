<%@ page import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page import="com.fortune.util.CacheUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-15
  Time: 上午9:34
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include file="../inc/checkHeader.jsp"%><%
    CacheUtils.clearAll();
%>
<html>
<head>
    <title>刷新所有的页面缓存</title>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        storeConfig.fields = ['id','name','type','ip','url','status','ftpPort','ftpUser','ftpPwd','ftpPath'];
        storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: '/system/device!list.action?obj.type=<%=DeviceLogicInterface.DEVICE_TYPE_WEB%>'});
        storeConfig.baseParams={limit:defaultPageSize};
        searchStore = new FortuneSearchStore(storeConfig);
        var resultStr = "";
        function appendJs(jsUrl){
            var head = document.getElementsByTagName("HEAD")[0];
            var jsFileRef=document.createElement('script');
            jsFileRef.setAttribute("type","text/javascript");
            jsFileRef.setAttribute("src", jsUrl);
            head.appendChild(jsFileRef);
        }
        function refreshFinished(success, serverData) {
            if(success){
                if(serverData.success){
                    resultStr += "服务器处理完毕：服务器"+serverData.name+"已经清空缓存!<br/>\r\n";
                }else{
                    resultStr+=('发生异常:服务器返回错误信息：'+serverData.error)+"<br/>\r\n"
                }
            }else{
                resultStr+="服务器无法接受刷新指令！请手工访问刷新连接！<br/>";
            }
            showMessage(resultStr);

            window.setTimeout("closeMessageBox()",3000);
        }
        function init(){
            showMessage("正在请求刷新服务器列表！");
            searchStore.load({
                callback:function (records, options, success) {
                    if (success) {
                        resultStr = "";
                        var msg = "";
                        var i= 0,l=searchStore.getCount();
                        for(;i<l;i++){
                            msg += "正在向服务器" +searchStore.getAt(i).data.name+
                                    "发起请求...<br/>"
                        }
                        showMessage(msg);
                        for(i= 0;i<l;i++){
                            var data = searchStore.getAt(i).data;
                            appendJs(data.url+"/interface/refreshAllCache.jsp?name="+encodeURI(encodeURI(data.name))+"&time="+new Date().getTime());
                        }
                    }else{
                        Ext.MessageBox.alert("请求刷新服务器列表发生异常！");
                    }
                }
            });
        }
        function showMessage(msg){
            Ext.MessageBox.show({
                msg: msg,
                progressText: '正在处理，请稍候...',
                width:500,
                wait:true,
                waitConfig: {interval:500},
                icon:'ext-mb-info' //custom class in msg-box.html
                //,animEl: 'mb7'
            });
        }
        function closeMessageBox(){
            //Ext.MessageBox.close();
            Ext.MessageBox.alert("刷新完毕！");
        }
        keyFieldId = "id";
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            init();
        });
    </script>
</head>
<body>

</body>
</html>