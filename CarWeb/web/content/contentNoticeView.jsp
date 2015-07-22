<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"save","contentNoticeManage,contentNoticeSave");
        needPermissions(actionHeader,"view","contentNoticeManage,contentNoticeView");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>走马灯信息管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        <%
            long adminId = 0;
            if (admin != null && admin.getCspId()!=null){
                adminId = admin.getId();
            }
        %>
        var adminId = <%=adminId%>;
        actionHeader = "contentNotice";
        nextUrl = "contentNoticeManagement.jsp";

        function saveData(adminId){
            if(getCmpValue("obj.adminId")==null||getCmpValue("obj.adminId")==0){
                setCmpValue("obj.adminId",adminId);
            }
            saveAllData();
//            saveFormAjax();
        }
        //判断保存时的状态为上线或者下线
        function saveAllData(){
           if(getCmpValue("obj.status")==1){
               saveFormAjax();
           }else{
               //保存为上线
               var dataArray={};
               var createTime = getCmpValue("obj.createTime");
               var createTime1 ;
               var onlineTime = getCmpValue("obj.onlineTime");
               var onlineTime1 ;
               var offlineTime = getCmpValue("obj.offlineTime");
               var offlineTime1;
               if(createTime!=""){
                   createTime1 = createTime.format('Y-m-d H:i:s');
               }
               if(onlineTime!=""){
                   onlineTime1 = onlineTime.format('Y-m-d H:i:s');
               }
               if(offlineTime!=""){
                   offlineTime1 = offlineTime.format('Y-m-d H:i:s');
               }
               dataArray["keyId"] = keyId;
               dataArray["obj.content"] = getCmpValue("obj.content") ;
               dataArray["obj.status"] = getCmpValue("obj.status");
               dataArray["obj.createTime"] = createTime1;
               dataArray["obj.onlineTime"] = onlineTime1;
               dataArray["obj.offlineTime"] = offlineTime1;
               dataArray["obj.adminId"] = getCmpValue("obj.adminId");
               Ext.Ajax.request({
                   url:'/content/contentNotice!saveAllData.action',
                   params:dataArray,
                   callback : function(opt, success, response) {
                       if (success) {
                           Ext.Msg.alert("提示", "操作成功！");
                           window.location="../content/contentNoticeManagement.jsp";
                       }
                   }
               });
           }
        }
       var defaultViewFormButtons = [
            {
                text:'保存数据',
                handler:function(){saveData(adminId)},
                action:'save',
                id:'save'
            },
            {
                text:'重新加载',
                handler:loadFormAjax,
                action:'view'
            }
        ];
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var contentNoticeViewForm = new FortuneViewForm({
                title:'走马灯信息管理',
                width:500,
                url:'contentNotice!save.action',
                saveUrl :'contentNotice!save.action',
                viewUrl:'contentNotice!view.action',
                bodyStyle:'padding: 5px 5px 0',
                items:[
                    {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                    {fieldLabel:'<fts:text name="内容"/>',
                        width:400,
                        id:'obj.content',
                        xtype:'textarea',
                        name:'obj.content',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    },
                    {
                        fieldLabel : '状态',
                        hiddenName : 'obj.status',
                        width:400,
                        allowBlank : false,
                        xtype : 'combo',
                        editable : false,
                        mode : 'local',
                        triggerAction : 'all',
                        store : [['1', '下线'], ['2', '上线']],
//                        store:[['1', '下线']],
                        value : 1
                    },
                    {fieldLabel:'<fts:text name="创建时间"/>',
                        width:400,
                        name:'obj.createTime',
                        id:'obj.createTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    },
                    {fieldLabel:'<fts:text name="上线时间"/>',
                        width:400,
                        name:'obj.onlineTime',
                        id:'obj.onlineTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    },
                    {fieldLabel:'<fts:text name="下线时间"/>',
                        width:400,
                        name:'obj.offlineTime',
                        id:'obj.offlineTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    },
                    {fieldLabel:'<fts:text name="操作员"/>',
                        width:400,
                        id:'obj.adminId',
                        name:'obj.adminId',
                        inputType:'hidden',
                        readOnly:viewReadOnly
                    }
                ],
                buttons:defaultViewFormButtons
            });
            loadFormAjax();
            contentNoticeViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }/*,{
                 func:initAreaStore,
                 done:false,
                 flag:'initAreaStore'
                 }*/
            ],
                    initDisplay);
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>