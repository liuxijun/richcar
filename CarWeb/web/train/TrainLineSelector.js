var callBackFunctionWhenSelected;
var selectMode="line";
var currentSelectedTrainLineNode=null;

function selectTrainLineForAd() {
    selectMode = "any";
    openSelectTrainLineWin(Ext.getCmp("obj.lineName"), Ext.getCmp("obj.lineId"), null);
}
function selectTrainLine() {
    selectMode = "line";
    openSelectTrainLineWin(Ext.getCmp("trainLineName"), Ext.getCmp("obj.trainLineId"), null);
}
function selectTrain(){
    selectMode = "train";
    openSelectTrainLineWin(Ext.getCmp("obj.trainName"), Ext.getCmp("obj.trainId"), null);
}

function getLevel(currentNode){
    var result = 0;
    while(currentNode!=null&&currentNode.parentNode!=null){
        result++;
        if(result>10){
            break;
        }
        currentNode = currentNode.parentNode;
    }
    return result;
}

function isLeaf(currentNode){
    return (currentNode.leaf);
}
function openSelectTrainLineWin(displayField,valueField,callBackFunctionIn){
    callBackFunctionWhenSelected = callBackFunctionIn;
    var tree = new Ext.tree.TreePanel(
    {
        animate:true,
        useArrows:true,
        //frame: true,
        xtype:'treepanel',
        autoScroll:true,
        loader: new Ext.tree.TreeLoader({dataUrl:'../train/trainLine!getNodes.action?selectMode='+selectMode,nodeParameter:'parentId'}),
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
                currentSelectedTrainLineNode = node;
            }
        }
    }
            )       //  */
            ;

    // add a tree sorter in folder mode
   // new Ext.tree.TreeSorter(tree, {folderSort:true});

    // set the root node
    var rootId = "-1";
    var rootText = "全部线路";
/*
    if(operatorTrainLineId){
        rootId = operatorTrainLineId;
    }
    if(operatorTrainLineName){
        rootText = operatorTrainLineName;
    }
*/
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
        title:'选择线路',
        autoHeight:true,
        items: [tree],
        buttons:[
            {text:'确定',handler:function(){
                if(currentSelectedTrainLineNode==null){
                    alert("没有选择任何线路！请选择！");
                    return;
                }
                var currentLevel = getLevel(currentSelectedTrainLineNode);
                if(!isLeaf(currentSelectedTrainLineNode)){
                    if("train"==selectMode){
                        alert("只能选择具体的列车");
                        return;
                    }else if("any"==selectMode){
                    }else if("line"==selectMode){
                        alert("只能选择最终的线路节点！");
                        return;
                    }
                }
                try {
                    if (displayField != null) {
                        displayField.setValue(currentSelectedTrainLineNode.text);
                    }
                    if (valueField != null) {
                        valueField.setValue(currentSelectedTrainLineNode.id);
                    }
                    if(callBackFunctionWhenSelected!=null){
                        callBackFunctionWhenSelected.call(this,currentSelectedTrainLineNode.text,currentSelectedTrainLineNode.id);
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