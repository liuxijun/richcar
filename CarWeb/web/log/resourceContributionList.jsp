<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
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
        var cpStore = new Ext.data.JsonStore({
            method:'POST',
            autoLoad:true,
            url:"/csp/csp!searchAllCp.action",
            root:'objs',
            totalProperty:'totalCount',
            fields:['id', 'name']
        });
        function initDisplay() {
            var tree = new Ext.tree.TreePanel(
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

            var channelViewForm = new Ext.form.FormPanel({
                title:'资源贡献统计',
                border:true,
                url:'channel!view.action',
                saveUrl:'channel!save.action',
                width:600,
                frame:false,

                items:[
                    new Ext.Toolbar({

                        items:[ new Ext.form.FieldSet({
                                    title:'搜索栏',
                                    width:600,
                                    items:[
                                        {
                                            baseCls:'x-plain',
                                            layout:'column',
                                            items:[
                                                {
                                                    columnWidth:.5,//第一列
                                                    layout:"form",
                                                    baseCls: 'x-plain',
                                                    items:[{

                                                        xtype:"datefield",
                                                        fieldLabel: '开始时间',
                                                        name: 'startTime',
                                                        id:'startTime',
                                                        format:'Y-m-d',
                                                        labelWidth: 60,
                                                        width:100
                                                },{

                                                    xtype:"textfield",
                                                    fieldLabel: '资源名称',
                                                    name: 'contentName',
                                                    id:'contentName',
                                                    labelWidth: 60,
                                                    width:100
                                                },{

                                                    xtype:"combo",
                                                    fieldLabel: '来源组织',
                                                    name: 'cpId',
                                                    hiddenName: 'cpId',
                                                    labelWidth: 60,

                                                    emptyText:'请选择...',
                                                    store:cpStore ,
                                                    valueField:'id',
                                                    displayField:'name',
                                                    forceSelection: true,
                                                    triggerAction: 'all',
                                                    selectOnFocus:true
                                                }
                                                ]
                                            },
                                                {
                                                baseCls:'x-plain',
                                                columnWidth:.5,//第二列
                                                layout:"form",
                                                items:[{

                                                    xtype:"datefield",
                                                    fieldLabel: '结束时间',
                                                    name: 'endTime',
                                                    id:'endTime',
                                                    format:'Y-m-d',
                                                    labelWidth: 60,
                                                    width:100
                                                },
                                                    {
                                                        fieldLabel: '所有频道',
                                                        labelWidth: 60,
                                                        width:100 ,
                                                        id: 'channelSelect',
                                                        name: 'channelSelect',
                                                        xtype:'radiogroup',
                                                        items:[
                                                            {boxLabel: '是',name: 'channelSelect', inputValue:1},
                                                            {boxLabel: '否',name: 'channelSelect', checked: true,inputValue:0}
                                                        ]
                                                    },
                                                    {

                                                        fieldLabel: '点播时长',
                                                        id:'playTime',
                                                        labelWidth: 60,
                                                        width:160,
                                                        id:'playTimeSelect',
                                                        name: 'playTimeSelect',
                                                        xtype:'radiogroup',
                                                        items:[
                                                            {boxLabel: '全部',name: 'playTimeSelect', inputValue:1 },
                                                            {boxLabel: '大于10分钟',name: 'playTimeSelect', checked: true,inputValue:0}
                                                        ]
                                                    }

                                                ]
                                            },{
                                                baseCls: 'x-btn-over',

                                                layout:"form",
                                                items:[{

                                                    text: '查询',
                                                    xtype:'button',
                                                    minWidth:60,
                                                    listeners:{
                                                        "click":function()
                                                        {
                                                            var startTime = Ext.getCmp("startTime").value;
                                                            var endTime = Ext.getCmp("endTime").value;
                                                            if(startTime==undefined||endTime==undefined){
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

                                                                return ;
                                                            }
                                                            var contentName = Ext.getCmp("contentName").getValue();
                                                            //alert(contentName);
                                                            var channelSelect = Ext.getCmp('channelSelect').getValue().inputValue;
                                                            //var spId = Ext.getCmp('spId').getValue();
                                                            //alert(spId);
                                                            //var playTimeSelect = Ext.getCmp('playTimeSelect').getValue().inputValue;
                                                            //alert(playTimeSelect);
                                                            var nodes = tree.getChecked();
                                                            if(channelSelect==0){
                                                                if(nodes == ""){
                                                                    alert("请选择需要查询的频道！");
                                                                    return ;
                                                                }

                                                            }


                                                            var channelsAndLeafs = [];
                                                            if(nodes!=""){
                                                                for(var i=0;i<nodes.length;i++){
                                                                    channelsAndLeafs.push(nodes[i].id+"-"+nodes[i].leaf);
                                                                }
                                                            }
                                                            var form = channelViewForm.getForm().getEl().dom;
                                                            form.action = '/log/visitLog!getResourceContributionCount.action?channelsAndLeafs='+channelsAndLeafs+'&start=1&limit=12';
                                                            form.method = 'POST';
                                                            form.target = "_blank" ;
                                                            form.submit();
                                                        }
                                                    }
                                                }]
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