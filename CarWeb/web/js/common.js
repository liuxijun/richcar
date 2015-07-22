function confirmDelete(keyId){
    if (confirm("您确认删除id为" + keyId + "的数据吗？")) {
        var myForm = document.forms[0];
        myForm.action = actionUrl+"-" + keyId + "!delete.action";
        myForm.submit();
    }
}
function createNew(){
    window.location.replace(actionUrl+"!add.action");
}
function updateById(keyId){
    window.location.replace(actionUrl+"-" + keyId + "!update.action");
}
function deleteSelected(){
    if(confirm("您确认要删除选择的数据吗？")){
        searchForm.action=actionUrl+"!deleteSelected.action";
        searchForm.submit();
    }
}
function selectAll(selectedAll){
    var myForm = document.forms[0];
    var i;
    //alert("all:"+selectedAll);
    for(i=0;i<myForm.elements.length;i++){
        var ele = myForm.elements[i];
        if(ele.type == "checkbox"){
            ele.checked = selectedAll;
        }
    }
}

/**
 * 得到字符串的字符长度（一个汉字占两个字符长）
 */
function getStringLength(str) {
    if (!str && typeof(str)!='string'){
        return 0;
    }
    // 在GBK编码里，除了ASCII字符，其它都占两个字符宽
    //alert(typeof(str));
    var len = 0;
    for (var i=0; i<str.length; i++) {
        if (str.charCodeAt(i)>127 || str.charCodeAt(i)==94) {
            len += 2;
        } else {
            len ++;
        }
    }
    return len;
    //return str.replace(/[^\x00-\xff]/g, 'xx').length;
}

/**
 * 非负整数
 */
function checkNumber(str) {
    //var r = /^\\d+$/;
    var r =  /^(-|\+)?\d+$/;
    return r.test(str);
}