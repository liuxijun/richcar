var callBackFunctionWhenSelected;
var currentSelectedAreaNode=null;

function selectArea() {
    openSelectAreaWin(Ext.getCmp("areaName"), Ext.getCmp("obj.areaId"), null);
}

function openSelectAreaWin(displayField,valueField,callBackFunctionIn){
    callBackFunctionWhenSelected = callBackFunctionIn;
    var tree =
    new Ext.tree.TreePanel(
    {
        animate:true,
        useArrows:true,
        //frame: true,
        xtype:'treepanel',
        autoScroll:true,
        loader: new Ext.tree.TreeLoader({dataUrl:'../system/area!getNodes.action',nodeParameter:'parentId'}),
        enableDD:true,
        containerScroll: true,
        border: false,
        width: 350,
        height: 300,
        rootVisible: true,
        dropConfig: {appendOnly:true},
/*
                root:{
                    text: '上汽通用别克',
                    draggable:false, // disable root node dragging
                    id:'-1'
                },
*/
        listeners: {
            click:function(node,e){
                if(e){

                }
                currentSelectedAreaNode = node;
            }
        }
    }
            )       //  */
            ;

    // add a tree sorter in folder mode
   // new Ext.tree.TreeSorter(tree, {folderSort:true});

    // set the root node
    var rootId = "-1";
    var rootText = "全部管辖区域";
    if(operatorAreaId){
        rootId = operatorAreaId;
    }
    if(operatorAreaName){
        rootText = operatorAreaName;
    }
    var root = new Ext.tree.AsyncTreeNode({
        text: rootText,
        draggable:false, // disable root node dragging
        id:rootId
    });
    tree.setRootNode(root);
    var treeWin = new Ext.Window({
        width:360,
        height:375,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'选择区域',
        autoHeight:true,
        items: [tree],
        buttons:[
            {text:'确定',handler:function(){
                if(currentSelectedAreaNode==null){
                    alert("没有选择任何区域！请选择！");
                    return;
                }
                try {
                    if (displayField != null) {
                        displayField.setValue(currentSelectedAreaNode.text);
                    }
                    if (valueField != null) {
                        valueField.setValue(currentSelectedAreaNode.id);
                    }
                    if(callBackFunctionWhenSelected!=null){
                        callBackFunctionWhenSelected.call(this,currentSelectedAreaNode.text,currentSelectedAreaNode.id);
                    }
                } catch(e) {

                }
                treeWin.close();
            }},
            {text:'取消',handler:function(){treeWin.close()}}
        ]
    });
    treeWin.show();
    root.expand(false, /*no anim*/ false);
}