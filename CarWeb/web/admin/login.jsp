<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-9-22
  Time: 16:28:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    if(request.getParameter("oldLogin")==null){
        response.sendRedirect("../index.jsp");
        return;
    }
%><html>
<head>
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="../resources/css/ext-all.css"/>
    <script type="text/javascript" src="../ext/ext-base.js"></script>
    <script type="text/javascript" src="../ext/ext-all.js"></script>
    <script src="../ext/ext-lang-zh_CN.js" type="text/javascript"></script>
    <script type="text/javascript" src="../ext/ux/ux-all.js"></script>
    <script type="text/javascript" src="../js/md5.js"></script>
    <script type="text/javascript" src="../js/ExtValidator.js"></script>
    <script type="text/javascript" src="../js/ExtUtils.js"></script>
    <script type="text/javascript" src="../js/ExtDefaultStore.js"></script>
    <script type="text/javascript" src="../js/ExtFortuneCom.js"></script>
    <script type="text/javascript" src="../js/FortuneUtils.js"></script>
    <script type="text/javascript">
        var login;
        function doLogin(){
            var passwordObj = Ext.getCmp("obj.password");
            if(passwordObj!=null){
                passwordObj.setValue(hex_md5(passwordObj.getValue()));
            }
            login.getForm().submit({
                method:'POST',
                //waitTitle:'链接中...',
                //waitMsg:'发送数据...',
                success:function(form, action) {
                    obj = Ext.util.JSON.decode(action.response.responseText);
                    if (obj.success == 'true' || obj.success == true) {
                        window.location = "../admin/index.jsp";
                    } else {
                        Ext.Msg.alert('Login Failed!', obj.errors.reason);
                    }
                    //win.close();
                },
                failure:function(form, action) {
                    passwordObj = Ext.getCmp("obj.password");
                    if(passwordObj!=null){
                        passwordObj.setValue("");
                    }
                    if (action.failureType == 'server') {
                        obj = Ext.util.JSON.decode(action.response.responseText);
                        Ext.Msg.alert('登录失败', '<div style="padding:20px,20px,20px,1px;font-size:15px;color:red"><br/>错误的账号或口令<br>请查正后再试!!</div>');
                    } else {
                        Ext.Msg.alert('Warning!', 'Authentication server is unreachable : ' + action.response.responseText);
                    }
                    //login.getForm().reset();
                }
            });
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            login = new Ext.FormPanel({
                labelWidth:80,
                url:'../security/login.action',
                frame:true,
                title:'请登录',
                labelAlign:'right',
                defaults:{width:200,labelAlign:'right'},
                defaultType:'textfield',
                //             monitorValid:true,	// Specific attributes for the text fields for username / password.
                // // The "name" attribute defines the name of variables sent to the server.
                items:[
                    {
                        fieldLabel:'用户名称',
                        labelAlign:'right',
                        name:'obj.login',
                        //value:'root',
                        allowBlank:false,
                        listeners : {
                            specialkey : function(field, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    //焦点跳到口令
                                    doLogin();
                                }             
                            }
                        }
                    },
                    {
                        fieldLabel:'登录口令',
                        labelAlign:'right',
                        name:'obj.password',
                        id:'obj.password',
                        inputType:'password',
                        //value:'123456',
                        allowBlank:false,
                        listeners : {
                            specialkey : function(field, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    doLogin();
                                }
                            }
                        }
                    },{
                        fieldLabel:'证码信息',
                        name:'validCode'
                    },new Ext.form.ComboBox({
                        fieldLabel:'用户类型',
                        mode:'local',
                        forceSelection:true,
                        triggerAction:"all",editable:false,
                        valueField:'value',displayField:'name',
                        hiddenName: 'userType',
                        store:new Ext.data.SimpleStore({
                    fields:['name','value'],
                    data:[['SGM上汽通用','sgm'],['RPC区域推广经理','rpc'],
                        ['RFS大区经理功能','rfs'],['MAC区域经理','mac'],['LSS零售商','lss']]
                }),
                        emptyText:'请选择...'
                    })

                ],// All the magic happens after the user clicks the button
                buttons:[
                    {
                        text:'登录',
                        formBind: true,
                        // Function that fires when user clicks the button
                        handler:doLogin
                    }
                ]
            });
            var logoPanel = new Ext.Panel({
                baseCls : 'x-plain',
                id : 'login-logo',
                width:240,
                height:50,
                region : 'center'
            });
            // This just creates a window to wrap the login form.
            //  The login object is passed to the items collection.
            var win = new Ext.Window({
                layout:'fit',
                width:350,
                height:200,
                closable: false,
                resizable: false,
                plain: true,
                border: false,
                items: [login]
            });
            win.show();
        });
    </script>
</head>
<body></body>
</html>