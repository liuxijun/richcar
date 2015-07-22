
function showSelectTreeDialog(dispObj,idObj,queryUrl,queryMod,diagControlInfo){

    if(!diagControlInfo) diagControlInfo = "dialogWidth=550px; dialogHeight=450px; help=no; scroll=yes; status=no;";
    var returnVal = showModalDialog( queryUrl, queryMod, diagControlInfo );
    if (typeof(returnVal) != "undefined" ) {
          setToThisWindow(returnVal,dispObj,idObj);
    }
}

function showTree(displayDiv,objType){
    var aForm = document.forms[0];
    var aEle = aForm.elements["obj." + objType + "Id"];
    if(aEle && displayDiv){
        showSelectTreeDialog(displayDiv,aEle,objType+"Action!tree.action",objType,null);
    }else{
        alert("没有合适的组件" + objType+
              "接收数据，无法进行后续操作！\n请与开发人员联系！谢谢！");
    }
}

function selectSubject(displayDiv){
    var aForm = document.forms[0];
    var aEle =aForm.elements["obj.subjectId"];
    showSelectTreeDialog(displayDiv,aEle,
                         "../media/subjectAction!tree.action","subject",null);
}
function selectParentSubject(displayDiv){
    var aForm = document.forms[0];
    var aEle =aForm.elements["obj.parentId"];
    showSelectTreeDialog(displayDiv,aEle,
                         "../media/subjectAction!tree.action","subject",null);
}
function selectParentNode(displayDiv){
    var aForm = document.forms[0];
    var aEle =aForm.elements["obj.parentId"];
    showSelectTreeDialog(displayDiv,aEle,
                         "../media/nodeAction!tree.action?multiSelect=false","node",null);
}

function selectOrg(orgDisplayDiv){
    var aForm = document.forms[0];
    var aEle =aForm.elements["obj.orgId"];
    showSelectTreeDialog(orgDisplayDiv,aEle,
                         "../organization/orgAction!tree.action","org",null);
}
function selectParentOrg(orgDisplayDiv){
    var aForm = document.forms[0];
    var aEle =aForm.elements["obj.parentId"];
    showSelectTreeDialog(orgDisplayDiv,aEle,
                         "../organization/orgAction!tree.action","org",null);
}

function setToThisWindow(inputStr,displayDiv,id) {
  var   strarray=new   Array();
  strarray[0]=inputStr.split("|")[0];
  strarray[1]=inputStr.split("|")[1];
  displayDiv.innerHTML = strarray[0];
  id.value = strarray[1];
  return true;
}
