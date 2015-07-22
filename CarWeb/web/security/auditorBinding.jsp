<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%><%
    //设置页面显示基本信息
    String actionHeader = "device";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"saveAuditorToCsp","cspSaveAuditorToCspList");
        needPermissions(actionHeader,"searchAdmin","adminSearchAdmin");
//        needPermissions(actionHeader,"searchAuditorsByCspId","cspToAuditorList");
     //   needPermissions(actionHeader,"list","cspManage,cspSearch");
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" type="text/css" href="../resources/css/ext-all.css"/>
<script type="text/javascript" src="../ext/ext-base.js"></script>
<script type="text/javascript" src="../ext/ext-all.js"></script>
<script type="text/javascript" src="../ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="../ext/ux/ux-all.js"></script>
<script language="javascript">
var keyId = <%=request.getParameter("keyId")%>;

var cspAuditorString = null;

function initAuditorData() {
    Ext.Ajax.request({
        url:'../csp/cspAuditor!searchAuditorsByCspId.action?keyId=' + keyId,
        method:'GET',
        callback:function(opt, success, response) {
            var cspAuditorData =  Ext.util.JSON.decode(response.responseText);

            if (cspAuditorData.objs != null) {
                cspAuditorString = cspAuditorData.objs;
                if (!buttonChecked) {
                    setCheckButton(cspAuditorString);
                }
            }
            //functionDone('initAuditorData');
        }
    });
}
var buttonChecked = false;
var listDataLoaded = false;
function setChecked(id,checked){
    if(checked){
        var obj = document.getElementById(id);
        if(obj!=null){
            obj.checked = checked;
        }
    }
}
function setCheckButton(cspAuditorString) {
    if (cspAuditorString == null) {
        return;
    }
    if (!listDataLoaded) {
        return;
    }
    for (var i = 0; i < cspAuditorString.length; i++) {

        var cspAuditor = cspAuditorString[i];

        var adminId = cspAuditor.adminId;
//        alert(adminId);
        setChecked("c"+adminId+"1", cspAuditor.spOnline!=0);
        setChecked("c"+adminId+"2", cspAuditor.spOffline!=0);
        setChecked("c"+adminId+"3", cspAuditor.cpOnline!=0);
        setChecked("c"+adminId+"4", cspAuditor.cpOffline!=0);
    }
    buttonChecked = true;

}
Ext.onReady(function() {
    initAuditorData();
    var keyId = <%=request.getParameter("keyId")%>;
    // alert(keyId);
    var pageSize = 12;
    var tableWidth = 630;
    var tableHeight = 410;

    var actionUrl = "/csp/csp";


    //刷新列表
    function loadData() {
        var searchField = Ext.getCmp('searchField');
        if (searchField.getValue() == '') {
            dataListStore.removeAll();
            dataListStore.reload({params:{start:0,limit:pageSize}});
            listGrid.getBottomToolbar().updateInfo();
        } else {
            searchField.onTrigger2Click();
        }
    }

    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url:"/security/admin!searchAdmin.action?keyId=" + keyId + "",
        baseParams:{limit:12},
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'login'
            },
            {
                name:'spOnline'
            },
            {
                name:'spOffline'
            },
            {
                name:'cpOnline'
            },
            {
                name:'cpOffline'
            }
        ],
        listeners:{
            load:function(store) {
                buttonChecked = false;
                listDataLoaded = true;
                setCheckButton(cspAuditorString);
            }
        }
    });
    // dataListStore.setDefaultSort('id','asc');
    var cspAuditorStore = new Ext.data.JsonStore({

        method:'POST',
        url: "/csp/csp!searchCspAuditor.action",
        root:'cspAuditors'
    });


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox

    });
    var listGrid = new Ext.grid.EditorGridPanel({
        title:"审核人绑定",
        width:tableWidth,
        height:tableHeight+40,
        store: dataListStore,

        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [

            {
                header: "ID",
                dataIndex: 'id',
                width: 30,
                hidden:true,
                sortable: true,
                align:'center',
                renderer:
                        function(val, p, row) {
                            return '<input name="id" style= "border:0px " readonly= "true " value="' + row.data.id + '" type="input" />';
                        }
            },
            {
                header: "审核人名称",
                dataIndex: 'login',
                width: 90,
                sortable: true,
                align:'center'
            },
            {
                header: "SP上线",
                dataIndex: 'spOnline',
                width: 90,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="c' + row.data.id + '" id="c' + row.data.id + '1" type="checkbox" value="1"/>';
                        }
            },
            {
                header: "SP下线",
                dataIndex: 'spOffline',
                width: 90,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="c' + row.data.id + '" id="c' + row.data.id + '2" type="checkbox" value="2"/> ';
                        }
            },
            {
                header: "CP上线",
                dataIndex: 'cpOnline',
                width: 90,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="c' + row.data.id + '" id="c' + row.data.id + '3"  type="checkbox" value="3"/> ';
                        }
            },
            {
                header: "CP下线",
                dataIndex: 'cpOffline',
                width: 90,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="c' + row.data.id + '" id="c' + row.data.id + '4" type="checkbox" value="4"/> ';
                        }
            },


            {
                header: "审核人确认",
                dataIndex: 'id',
                width: 90,
                hidden:true,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {

                            return '<a href=\'javascript:test1(' + row.data.id + ');\'>修改</a>';
                        }
            }
        ],

        tbar:new Ext.Toolbar({items:[
            {
                text:'搜索: '
            },
            {
                text:'审核人名称'
            },
            new Ext.ux.form.SearchField({
                id:'searchField',
                store: dataListStore,
                paramName:'obj.login',
                width:320
            })
        ]}
                ),

        bbar:new Ext.PagingToolbar({
            pageSize: pageSize,
            store: dataListStore,
            displayInfo: true,
            displayMsg: '结果数据 {0} - {1} of {2}',
            emptyMsg: "没有数据",
            items:[

                {
                    text:'提交数据',
                    handler:function(row) {

                        var adminIdAndCspIdString = "";
                        Ext.MessageBox.confirm("请您确认操作", " 更新记录 ", function(btn) {
                            if (btn == "yes") {

                                var keyId1 = "";
                                var ids = document.getElementsByName("id");
                                for (var i = 0; i < ids.length; i++) {
                                    keyId1 = ids[i].value;
                                    if (adminIdAndCspIdString == "") {
                                        adminIdAndCspIdString = keyId1;
                                    } else {
                                        adminIdAndCspIdString += keyId1;
                                    }
                                    var checks = document.getElementsByName("c" + keyId1);

                                    for (var j = 0; j < checks.length; j++) {
                                        if (checks[j].checked) {
                                            adminIdAndCspIdString += "_" + checks[j].value;
                                        }
                                    }
                                    adminIdAndCspIdString += ",";

                                }
                                adminIdAndCspIdString = adminIdAndCspIdString.substr(0, adminIdAndCspIdString.length - 1);
                                var remoteRequestStore = new Ext.data.JsonStore({
                                    method:'POST',
                                    url: "/csp/csp!saveAuditorToCsp.action"
                                });
                                remoteRequestStore.reload({
                                    params:{adminIdAndCspIdString:adminIdAndCspIdString,keyId:keyId},
                                    callback :
                                            function(records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    //loadData();
                                                    Ext.MessageBox.alert('提示', '操作成功');
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                                }

                                            }
                                });
                            }
                        });
                    }
                },
                {
                    text:'返回',
                    handler:function() {
                        document.location = '/csp/cspList.jsp';
                    }
                }
            ]
        })
    });


    listGrid.render('display');
    dataListStore.load({
        params:{start:0, limit:pageSize}
    });

    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action, id) {
        var viewHtml = '<iframe  frameborder="0" width="100%" height="100%" src="propertyView.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId + '"></iframe>';
        var viewWin = new Ext.Window({
            //title:"文件列表",
            //x:0,
            y:20,
            width:tableWidth - 50,
            height:tableHeight - 50,
            closeAction:"hide",
            closable:true,
            bodyStyle:"padding:0px",
            plain:true,
            //layout:'fit',
            //collapsible:true,
            //plain: false,
            //resizable: true,

            html : viewHtml,
            listeners:{
                "show":function() {//alert("显示");
                },
                "hide":function() {
                    loadData();
                },
                "close":function() {//alert("关闭");
                }
            }
        })
        viewWin.show();
    }
})


</script>
</head>
<body onload="">
<table align="center" width="660">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>