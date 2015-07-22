<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","deviceManage,deviceView");
//        needPermissions(actionHeader,"list","deviceManage,deviceList");
        needPermissions(actionHeader,"save","deviceManage,deviceSave");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="device"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "device";
        nextUrl = "deviceList.jsp";

        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限


            var deviceViewForm = new FortuneViewForm({
                title:'<fts:text name="device"/>基本信息',
                url:'device!save.action',
                saveUrl:'device!save.action',
                viewUrl:'device!view.action',
                 bodyStyle:'padding:5px 5px 0',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'<fts:text name="device.name"/>',name:'obj.name',readOnly:viewReadOnly,allowBlank:false},
                     new Ext.form.ComboBox({
                         fieldLabel:'<fts:text name="device.type"/>',
                         hiddenName:'obj.type',
                         mode:'local',
                         value:1,
                         forceSelection:true,
                         triggerAction:"all",
                         editable:false,
                         store:getDictStore('serverType'),
                         valueField:'value',displayField:'name',
                         emptyText:'请选择...'/* ,
                          listeners: {
                              load:
                                  function(combo) {
                                      combo.setValue(1);
                                         *//*this.combobox.selectIndex=1;*//*
                              }
                          }*/
                     }),
                     {fieldLabel:'<fts:text name="device.ip"/>',name:'obj.ip',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="device.url"/>',name:'obj.url',readOnly:viewReadOnly},
                     {
                         xtype:'radiogroup',
                         fieldLabel:'<fts:text name="device.status"/>',
                         name:'obj.status',
                         items:[
                             {boxLabel:'有效',name:'obj.status',inputValue:"1",checked: true },
                             {boxLabel:'失效',name:'obj.status',inputValue:"2"}
                         ]
                    },
                    {fieldLabel:'<fts:text name="device.ftpPort"/>',name:'obj.ftpPort',readOnly:viewReadOnly}                     ,
                    {fieldLabel:'<fts:text name="device.ftpUser"/>',name:'obj.ftpUser',readOnly:viewReadOnly}                     ,
                    {fieldLabel:'<fts:text name="device.ftpPwd"/>',name:'obj.ftpPwd',readOnly:viewReadOnly}                     ,
                    {fieldLabel:'<fts:text name="device.ftpPath"/>',name:'obj.ftpPath',readOnly:viewReadOnly},
                    {fieldLabel:'监控端口',name:'obj.monitorPort',readOnly:viewReadOnly},
                    {fieldLabel:'最大并发',name:'obj.maxTask',readOnly:viewReadOnly},
                    {fieldLabel:'源文件目录',name:'obj.localPath',readOnly:viewReadOnly}
                    ,{fieldLabel:'输出目录',name:'obj.exportPath',readOnly:viewReadOnly}
                ]
            });

       <%if (request.getParameter("keyId")!=null){%>
                      loadFormAjax();
            <%      }            %>
            deviceViewForm.render(displayDiv);
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