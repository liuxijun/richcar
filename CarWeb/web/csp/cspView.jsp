<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%><%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"view","cspManage,cspView");
      // needPermissions(actionHeader,"list","cspManage,cspSearch");
        needPermissions(actionHeader,"save","cspManage,cspSave");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="csp"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "csp";
        nextUrl = "cspList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var cspViewForm = new FortuneViewForm({
                title:'<fts:text name="csp"/>基本信息',
                url:'csp!save.action',
                saveUrl:'csp!save.action',
                viewUrl:'csp!view.action',
                bodyStyle:'padding:5px 5px 0',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'-1'
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="csp.name"/>',
                        name:'obj.name',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="csp.address"/>',
                        name:'obj.address',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="csp.phone"/>',
                        name:'obj.phone',
                        readOnly:viewReadOnly
                    },
                    {
                        fieldLabel:'<fts:text name="csp.email"/>',
                        name:'obj.email',
                        readOnly:viewReadOnly
                    },
                    {
                        fieldLabel:'别名',
                        name:'obj.alias',
                        readOnly:viewReadOnly
                    },
                     {
                        fieldLabel:'spId',
                        name:'obj.spId',
                        readOnly:viewReadOnly
                    }, 
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.status"/>',
                        name:'obj.status',
                        items:[
                            {
                                boxLabel:'有效',
                                name:'obj.status',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'失效',
                                name:'obj.status',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.type"/>',
                        name:'obj.type',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.type',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.type',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isCp"/>',
                        name:'obj.isCp',
                        items:[
                            {                     
                                boxLabel:'是',
                                name:'obj.isCp',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isCp',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isSp"/>',
                        name:'obj.isSp',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isSp',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isSp',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isCpOnlineAudit"/>',
                        name:'obj.isCpOnlineAudit',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isCpOnlineAudit',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isCpOnlineAudit',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isCpOfflineAudit"/>',
                        name:'obj.isCpOfflineAudit',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isCpOfflineAudit',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isCpOfflineAudit',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isSpOnlineAudit"/>',
                        name:'obj.isSpOnlineAudit',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isSpOnlineAudit',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isSpOnlineAudit',
                                inputValue:"0"
                            }
                        ]
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="csp.isSpOfflineAudit"/>',
                        name:'obj.isSpOfflineAudit',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isSpOfflineAudit',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isSpOfflineAudit',
                                inputValue:"0"
                            }
                        ]
                    }
                ]
            });

     <%if (request.getParameter("keyId")!=null){%>
                    loadFormAjax();
        <%      }            %>
            cspViewForm.render(displayDiv);
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