
//多选频道 相关操作
var mediaNodes = new Array;

function selectNode(nodeDisplayDivName){
    var diagControlInfo = "dialogWidth=550px; dialogHeight=450px; help=no; scroll=yes; status=no;";
    var returnVal = showModalDialog( "../media/nodeAction!tree.action", "node", diagControlInfo );
    if (typeof(returnVal) != "undefined" ) {
        addMultiNodeToThisWindow(returnVal);
        displayNodes(nodeDisplayDivName);
    }
}


function addMultiNodeToThisWindow(inputStr){
    var items = inputStr.split("|");
    var i;
    for(i=0;i<items.length;i++){
        var item = items[i].split(",");
        addNode(item[1],item[0],0,0);
    }

}


function Node(name,id,orderNumber,recommended){
    this.name=name;
    this.id=id;
    this.orderNumber = orderNumber;
    this.recommended = recommended;
}
function addNode(name,id,orderNum,recommended){
    mediaNodes[mediaNodes.length]=new Node(name,id,orderNum,recommended);
}
function deleteNode(index){
    deleteFromArray(mediaNodes,index);
    displayNodes("mediaNodesDiv");
}
function displayNodes(displayDivName) {
    var resultStr = "<table><tr><td width='100'>频道名</td><td width='20'></td>" +
                    "<td width='40'  align='center'>状态</td>" +
                    "<td width='20'>&nbsp;</td><td width='80' align='center'>排序序号</td>" +
                    "<td align='center'>删除</td></tr>\n";
    var i;
    for (i = 0; i < mediaNodes.length; i++) {
        var node = mediaNodes[i];
        resultStr += "<tr><td>" + node.name + "</td><td>&nbsp;</td><td  align='center'>" +
                     getRecommendSelect('mediaNodes['+i+'].recommended',node.recommended);

        resultStr +=
        "</td><td></td>" +
        "<td align='center'><input style='width:50px' type='text' name='mediaNodes[" + i + "].orderNum'" +
        " value='" + node.orderNumber +
        "'></td><td align='center'>" +
        "<a href='javascript:deleteNode(" + i + ")'>删除</a></td></tr><input type='hidden'" +
        " name='mediaNodes[" + i + "].nodeId' value='" + node.id + "'>" +
        "\n";
    }
    var displayDiv = document.getElementById(displayDivName);
    if (!displayDiv) {
    } else {
        displayDiv.innerHTML = resultStr;
    }
}
function getRecommendSelect(eleName,recommended){
    var resultStr = "<select  class='inputNumber' name='"+eleName+"'>\n";
    var recommendedLevels = "停用(-1),普通(0),一般推荐(1),醒目推荐(2),置顶推荐(3)".split(",");
    var i;
    if(recommended>=recommendedLevels.length){
        recommended = recommendedLevels.length-1;
    }
    for(i=0;i<recommendedLevels.length;i++){
        resultStr += "<option value='"+(i-1)+"'";
        if((i-1)==(recommended)){
            resultStr += " selected=true ";
        }
        resultStr += ">"+recommendedLevels[i]+"</option>\n";
    }
    resultStr += "</select>";
    return resultStr;
}
