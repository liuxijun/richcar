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
        needPermissions(actionHeader,"list","deviceManage,deviceList");
        needPermissions(actionHeader,"save","deviceManage,deviceSave");
    }
%><%@ page import="com.fortune.util.StringUtils" %>
<%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="usruser"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript" src="../js/md5.js"></script>
    <script type="text/javascript">
        actionHeader = "user";
        nextUrl = "userList.jsp";
         //用户注册密码加密
        formOptions.beforePost = function() {

            var newPasswordObj = Ext.getCmp("newPassword");
            var passwordObj = Ext.getCmp("passWord");
            var oldPasswordObj=Ext.getCmp("passWordHistory");
            if (passwordObj != null && newPasswordObj != null && newPasswordObj.getValue() != '') {
                var newPwd = hex_md5(newPasswordObj.getValue());
                //alert(newPwd);
                passwordObj.setValue(newPwd);
                //跟换新的密码！！！ (下面的这个如果不注视历史密码有问题)
               // oldPasswordObj.setValue(passwordObj.getValue());
                //alert(passwordObj.getValue());
                newPasswordObj.setValue("");
                var confirmPasswordObj = Ext.getCmp("confirmPassword");
                if (confirmPasswordObj != null) {
                    confirmPasswordObj.setValue("");
                }
            } else {
                //alert("passwordObj is null or newPasswordObj is null:"+passwordObj+","+newPasswordObj);
            }
        };

        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限

            var deviceViewForm = new FortuneViewForm({
                title:'<fts:text name="usruser.usr"/>基本信息',
                url:'user!save.action',
                saveUrl:'user!save.action',
                viewUrl:'user!view.action',
                 bodyStyle:'padding:5px 5px 0',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}  ,
                     {fieldLabel:'<fts:text name="usruser.login"/>',
                         name:'obj.login',
                         allowBlank:false,
                         readOnly:modifyOldData,
                         blankText:'ID名不能为空',
                         id:'obj.login',
                         vtype:modifyOldData ? '' : 'login',
                         invalidText: '已经存在的ID名'
                        },

                      new FortuneCombo({fieldLabel:'用户状态',
                             hiddenName:'obj.status',
                             store: getDictStore('userStatus')

                            }),

                     {fieldLabel:'<fts:text name="usruser.birthday"/>',
                         xtype: 'datefield',
                         format:'Y-m-d',
                        name:'obj.birthday',
                         //页面初始化就格式化时间
                        // groupRenderer: Ext.util.Format.dateRenderer('M y'),

                         sortable:true,
                         readOnly:viewReadOnly


                     },
                     {fieldLabel:'<fts:text name="usruser.username"/>',name:'obj.userName',readOnly:viewReadOnly},
                     {
                         xtype:'radiogroup',
                         fieldLabel:'<fts:text name="usruser.gernate"/>',
                         name:'obj.gernate',
                         items:[

                             {boxLabel:'男',name:'obj.gernate',inputValue:"男",checked: true },
                             {boxLabel:'女',name:'obj.gernate',inputValue:"女"}
                         ]
                     },
                     //inputType:'hidden',
                    {fieldLabel:'用户密码',inputType:'hidden',id:'passWord',name:'obj.passWord',readOnly:viewReadOnly},
                    {fieldLabel:'历史密码',inputType:'hidden',id:'passWordHistory',name:'obj.passWordHistory',readOnly:viewReadOnly},
                    {
                        fieldLabel:'<fts:text name="usruser.password"/>',
                        name:'obj.newPassword',
                        inputType:'password',
                        id:'newPassword',
                        allowBlank:modifyOldData,
                        blankMessage:'口令不能为空'
                    },
                     {
                        fieldLabel:'<fts:text name="usruser.repeatPassWord"/>',
                        name:'confirmPassword',
                        id:'confirmPassword',
                        inputType:'password',
                        allowBlank:modifyOldData,
                        blankMessage:'口令不能为空',
                        vtype:'confirmPassword',
                        confirmTo:'newPassword',
                        vtypeText:"两次密码不一致！"
                    },


                     {fieldLabel:'<fts:text name="usruser.email"/>',name:'obj.email',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="usruser.addr"/>',name:'obj.addr',readOnly:viewReadOnly},
                    {fieldLabel:'<fts:text name="usruser.tel"/>',name:'obj.tel',readOnly:viewReadOnly}

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