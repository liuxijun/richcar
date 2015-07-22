<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "device";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"saveModuleToCsp","saveModuleCsp");
         needPermissions(actionHeader,"searchModule","SearchModuleBin");
     //   needPermissions(actionHeader,"searchModulesByCspId","cspSearchModulesByCspId");
      //  needPermissions(actionHeader,"list","cspManage,cspSearch");
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
var cspModuleString = null;

function initModuleData() {
    Ext.Ajax.request({
        url:'../csp/cspModule!searchModulesByCspId.action?keyId=' + keyId,
        method:'GET',
        callback:function(opt, success, response) {
            var cspModuleData = Ext.util.JSON.decode(response.responseText);

            if (cspModuleData.objs != null) {
                cspModuleString = cspModuleData.objs;
                if (!buttonChecked) {
                    setCheckButton(cspModuleString);
                }
            }
        }
    });
}
function confirmRadio(id) {
    var checkBoxObj = document.getElementById("c" + id);
    if (checkBoxObj.checked) {
        document.getElementById("m" + id).checked = true;
    } else {
        document.getElementById("m" + id).checked = false;
    }
}
function confirmCheckBox(id) {
    var checkBoxObj = document.getElementById("c" + id);
    if (checkBoxObj.checked) {

    } else {
        document.getElementById("m" + id).checked = false;
    }
}
var buttonChecked = false;
var listDataLoaded = false;

function setChecked(id, checked) {
    if (checked) {
        var obj = document.getElementById(id);
        if (obj != null) {
            obj.checked = checked;
        }
    }
}

function setCheckButton(cspModuleString) {
    if (cspModuleString == null) {
        return;
    }
    if (!listDataLoaded) {
        return;
    }
    for (var i = 0; i < cspModuleString.length; i++) {

        var cspModule = cspModuleString[i];
        var moduleId = cspModule.moduleId;
        var isDefault = cspModule.isDefault;
        if(document.getElementById("c" + moduleId)) {
            document.getElementById("c" + moduleId).checked = true;
            if (isDefault == 1) {
                document.getElementById("m" + moduleId).checked = true;
            }
        }

    }

}
Ext.onReady(function() {
    initModuleData();
    var keyId = <%=request.getParameter("keyId")%>;

    var pageSize = 10;
    var tableWidth = 480;
    var tableHeight = 405;

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
        url: "/module/module!searchModule.action?keyId=" + keyId + "",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            },
            {
                name:'selectStatus'
            },
            {
                name:'isdefault'
            }
        ],
        listeners:{
            load:function(store) {
                buttonChecked = false;
                listDataLoaded = true;
                setCheckButton(cspModuleString);
            }
        }
   }
    );
    // dataListStore.setDefaultSort('id','asc');


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox

    });
    var listGrid = new Ext.grid.EditorGridPanel({
        title:"模板绑定",
        width:tableWidth+20,
        height:tableHeight,
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
                width: 60,
                hidden:true,
                sortable: true,
                align:'center'
                ,
                renderer:
                        function(val, p, row) {
                            return '<input name="id" style= "border:0px " readonly= "true " value="' + row.data.id + '" type="input" />';
                        }
            },
            {
                header: "模板名称",
                dataIndex: 'name',
                align:'left',
                width: 150,
                sortable: true
            },
            {
                header: "选中状态",
                dataIndex: 'selectStatus',
                width: 85,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="c' + row.data.id + '" id="c' + row.data.id + '" type="checkbox" onchange="confirmCheckBox(' + row.data.id + ');" value="1"/>';
                        }
            },
            {
                header: "默认模板",
                dataIndex: 'isDefault',
                width: 75,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            return '<input name="isDefault"  type="radio" id="m' + row.data.id + '" onchange="confirmRadio(' + row.data.id + ');" value="' + row.data.id + '"/>';
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
                text:'模板名称'
            },
            new Ext.ux.form.SearchField({
                id:'searchField',
                store: dataListStore,
                paramName:'obj.name',
                width:280
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

                        var moduleIdAndCspIdString = "";
                        Ext.MessageBox.confirm("请您确认操作", " 更新记录 ", function(btn) {
                            if (btn == "yes") {

                                var keyId1 = "";
                                var ids = document.getElementsByName("id");
                                for (var i = 0; i < ids.length; i++) {
                                    keyId1 = ids[i].value;
                                    if (moduleIdAndCspIdString == "") {
                                        moduleIdAndCspIdString = keyId1;
                                    } else {
                                        moduleIdAndCspIdString += keyId1;
                                    }
                                    var selectStatus = document.getElementById("c" + keyId1);
                                    if (selectStatus.checked) {
                                        moduleIdAndCspIdString += "_1";
                                    } else {
                                        moduleIdAndCspIdString += "_0";
                                    }

                                    moduleIdAndCspIdString += ",";

                                }
                                moduleIdAndCspIdString = moduleIdAndCspIdString.substr(0, moduleIdAndCspIdString.length - 1);
                                var defaultModuleId = "";
                                var isDefault = document.getElementsByName("isDefault");
                                for (var j = 0; j < isDefault.length; j++) {
                                    if (isDefault[j].checked) {
                                        defaultModuleId = isDefault[j].value;
                                    }
                                }

                                var remoteRequestStore = new Ext.data.JsonStore({
                                    method:'POST',
                                    url: "/csp/csp!saveModuleToCsp.action"
                                });
                                remoteRequestStore.reload({
                                    params:{moduleIdAndCspIdString:moduleIdAndCspIdString,keyId:keyId,defaultModuleId:defaultModuleId},
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
                        document.location = '../csp/cspList.jsp';
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