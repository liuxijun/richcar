<%--
  Created by IntelliJ IDEA.
  User: long
  Date: 13-5-8
  Time: 下午6:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%

    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"getDefaultModule","cspModuleGetDefaultModule");
    }
    String addContentUrl = "../V5/media/mediaView.jsp";
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>选择模版</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <style type="text/css">
        .moduleItem{
            width:100%;
            text-align: center;
            float:left;
            color:blue;
            font-size:12px;
            padding: 5px;
        }
    </style>
    <script language="javascript">
        var dataViewStore = new Ext.data.JsonStore({
            method:'POST',
            url:'/module/module!getCspModuleListV2.action',
            root:'objs',
            fields:['id','name','status','channelId']
        });

        Ext.onReady(function(){
            dataViewStore.load({
                callback :function(records, options, success) {
                    if (success) {
                        if(dataViewStore.getCount()==1){
                            var data = dataViewStore.data.items[0].data;
                            window.location="<%=addContentUrl%>?moduleId="+data.id+"&channelId="+data.channelId;
                        }else if(dataViewStore.getCount()==0){
                            alert("出错啦!暂无模板");
                        }else{
                            var html = "";
                            for(var i= 0,l=dataViewStore.getCount();i<l;i++){
                                var module = dataViewStore.getAt(i).data;
                                html +="<a class='moduleItem' href='../V5/media/mediaView.jsp?keyId=-1&" +
                                        "moduleId=" +module.id;
                                if(module.channelId>0){
                                    html+="&channelId="+module.channelId;
                                }
                                html+="'>" +module.name+
                                        "</a>"
                            }
                            var modeuleWin = new Ext.Window({
                                closeAction:"hide",
                                closable:false,
                                modal:true,
                                resizable:false,
                                plain:true,
                                width:200,

                                title:'录入模板选择',
                                items:[
                                    {
                                        xtype:'panel',
                                        html:html
                                    }
                                ]

                            });
                            modeuleWin.show();
                        }
                    }else{
                        alert("模板加载出错啦！");
                    }
                }
            });
        })
    </script>
</head>
<body>
</body>
</html>