<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"getOrganizationContributionCount","organizationContributionCount");
        needPermissions(actionHeader,"getChannels","logGetChannelList");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script language="javascript">
        nextUrl = "areaTree.jsp";
        actionHeader = "channel";
        var currentNode = null;
        var formButtons = checkFunctions([
            {
                columnWidth:.4,
                //第三列
                text: '查询',
                xtype:'button',
                minWidth:60,
                action:'getOrganizationContributionCount',
                listeners:{
                    "click":function()
                    {
                        var startTime = Ext.getCmp("startTime").value;
                        var endTime = Ext.getCmp("endTime").value;
                        if (startTime == undefined || endTime == undefined) {
                            Ext.MessageBox.show({
                                title:"标题",
                                msg:"请选择需要查询的时间段",
                                buttons:{"ok":"我不再显示OK了"},
                                width:500,
                                icon:Ext.MessageBox.INFO,
                                closable:false



                                // prompt:true
                                // multiline:true
                            });

                            return;
                        }
                        var nodes = tree.getChecked();
                        if (nodes == "") {
                            Ext.MessageBox.alert("请选择需要查询的频道！");
                            return;
                        }
                        var channelsAndLeafs = [];
                        for (var i = 0; i < nodes.length; i++) {

                            channelsAndLeafs.push(nodes[i].id + "-" + nodes[i].leaf);

                        }
                        var form = channelViewForm.getForm().getEl().dom;
                        form.action = '/log/visitLog!getOrganizationContributionCount.action?channelsAndLeafs=' + channelsAndLeafs + '';
                        form.method = 'POST';
                        form.target = "_blank";
                        form.submit();
                    }
                }
            }
        ]);

        function initDisplay() {
            tree = new Ext.tree.TreePanel(
                    {
                        animate:true,
                        useArrows:true,
                        //frame: true,
                        xtype:'treepanel',
                        autoScroll:true,
                        loader:new Ext.tree.TreeLoader({dataUrl:'/publish/channel!getChannels.action', nodeParameter:'parentId'}),
                        enableDD:true,
                        containerScroll:true,
                        border:false,
                        width:550,
                        height:300,
                        rootVisible:true,
                        dropConfig:{appendOnly:true}
                    }
            );
            tree.on('checkchange', function(node, checked) {
                node.expand();
                node.attributes.checked = checked;
                if(checked){
                    node.eachChild(function(child) {
                        child.ui.toggleCheck(false);
                        child.attributes.checked = false;
                        child.ui.checkbox.disabled=true;
                       // child.fireEvent('checkchange', child, checked);
                    });
                }else{
                    node.eachChild(function(child) {
                        child.ui.checkbox.disabled=false;
                    });
                }

            });
            var root = new Ext.tree.AsyncTreeNode({
                text:'中国联通',
                draggable:false, // disable root node dragging
                id:'-1'
            });
            tree.setRootNode(root);

            // render the tree
            //tree.render('displayDiv');

             channelViewForm = new Ext.form.FormPanel({
                title:'组织贡献统计',
                border:true,
                url:'channel!view.action',
                saveUrl:'channel!save.action',

                width:550,
                frame:false,

                items:[
                    new Ext.Toolbar({

                        items:[ new Ext.form.FieldSet({
                                    title:'搜索栏',
                                    width:535,
                                    height:65,
                                    items:[
                                        {
                                            baseCls:'x-plain',
                                            layout:'column',
                                            items:[{
                                                layout:"form",
                                                baseCls: 'x-plain',
                                                labelWidth: 80,
                                                items:[{
                                                    columnWidth:.3,//第一列
                                                    xtype:"datefield",
                                                    fieldLabel: '　　开始时间',
                                                    name: 'startTime',
                                                    id:'startTime',
                                                    format:'Y-m-d',
                                                    labelWidth: 60,
                                                    width:100
                                                }]
                                            },{
                                                baseCls:'x-plain',
                                                layout:"form",
                                                labelWidth: 90,
                                                items:[{
                                                    columnWidth:.3,//第二列
                                                    xtype:"datefield",
                                                    fieldLabel: '　　　结束时间',
                                                    name: 'endTime',
                                                    id:'endTime',
                                                    format:'Y-m-d',
                                                    labelWidth: 60,
                                                    width:100
                                                }]
                                            },{
                                                baseCls: 'x-btn-over',

                                                layout:"form",
                                                items:formButtons
                                            }]
                                        }
                                    ]
                                }
                        )]
                    }),
                    tree
                ]
            });
            //loadFormAjax();
            channelViewForm.render(displayDiv);
//            root.expand(false,false);
        }
        Ext.onReady(function () {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],
                    initDisplay);
        });
    </script>
</head>
<body>
<table align="center" width="660" >
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>