/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-5-19
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */

function deviceBinding (id,url) {
    currentCspId = id;
    showDialog({id:'deviceModal', renderTo:'deviceDetailLog', title:'绑定', width:600,

        items:[
            {html:'<div class="public">' +
                '<span><p class="center">可选项</p></span>' +
                '<div>' +
                '<ul id="canBeSelectDevices" style=" max-height:300;overflow-y:auto;list-style:none;margin:0px; "></ul>' +
                '</div></div>' +

                '<div style="width: 30px;height: 300px;float: left;">' +
                '<button style="margin-top: 100px;margin-left: 10px;" onclick="moveToRight()"><img src="/images/report/g4.jpg"></button>' +
                '<button style="margin-top: 50px;margin-left: 10px;"onclick="moveToLeft()"><img src="/images/report/g3.jpg"></button>' +
                '</div>' +
                '<div class="public" style="margin-left: 20px">' +
                '<span><p class="center">已选项</p></span>' +
                '<span class="btn btn-grey" style="width: 200px" onclick="empty()">清空</span>' + "<div><ul id=\"selectedDevices\" style=\"max-height:300;overflow-y:auto; list-style:none;margin:0px;width: 400\"></ul>" +
                '</div></div>'
                 }
        ],
        buttons:[
            {text:'确定', cls:'btn-blue',handler:function(){
                saveBinding(url);
            } },
            {text:'关闭', style:'margin-left:250px;', handler:function () {
                $("#deviceModal").modal('hide');
            }}
        ]
    });
                renderDevices();
}
function list(){
    $.ajax({
        url:'../csp/csp!list.action',
        method:'post',
        dataType:'json',
        success:function (jsonData) {
            window.location.reload();
        }
    });
}
var Binding = null;
function loadDeviceName(id) {
    currentCspId = id;
    $.ajax({
        url:'/csp/csp!searchDevicesByCspId.action?keyId=' + id,
        dataType:'text',
        type:'post',
        success:function (msg) {
            var response = eval("(function(){return " + msg + ";})()");
            Binding = response.obj.devices;
            deviceBinding(id);

        }
    });

}
function loadCspName (id,url) {
    currentCspId = id;
    $.ajax({
        url:url +"?keyId="+id ,
        dataType:'text',
        type:'post',
        success:function (msg) {
            var response = eval("(function(){return " + msg + ";})()");
            Binding = response.obj.csps;
            deviceBinding(id,url);
        }
    });
}

function loadProductName (id,url) {
    currentCspId = id;
    $.ajax({
        url:url+"?keyId=" + id,
        dataType:'text',
        type:'post',
        success:function (msg) {
            var response = eval("(function(){return " + msg + ";})()");
            Binding = response.obj.products;
            deviceBinding(id,url);
        }
    });
}

function loadModules(id) {
        $.ajax({
            url:"/module/module!searchModule.action?keyId=" + id,
            type:'post',
            dataType:'text',
            success:function (msg) {
                var response = eval("(function(){return " + msg + ";})()");
                Binding = response.objs;
                loadSelectModules(id);
                moduleIdAndCspId(id);
            }
        });

    }
var CspModules = null;

function loadSelectModules (id,url) {
    var url ="/csp/cspModule!searchModulesByCspId.action"
        $.ajax({
            url:url+"?keyId=" + id,
            dataType:'text',
            type:'post',
            success:function (msg) {
                var response = eval("(function(){return " + msg + ";})()");
                CspModules = response.objs;
                moduleIdAndCspId(id,url);
            }
        });
}
function moduleIdAndCspId(id,url) {
    var cspModules = CspModules;
    var modules = Binding;
  if(modules==null||cspModules==null){
        return;
    }
    for (var j = 0; j < modules.length; j++) {
        var module = modules[j];
        module['selected']=false;
        for (var i = 0; i < cspModules.length; i++) {
            var cspModule = cspModules[i];
            if (module['id'] == cspModule['moduleId']) {
                module['selected'] = true;
                break;
            }
        }
    }
    deviceBinding(id,url);
}

function renderDevices () {
    var canBeSelectDevicesUl = $("#canBeSelectDevices");
    canBeSelectDevicesUl.html('');
    var selectedDevicesUI = $("#selectedDevices");
    selectedDevicesUI.html('');
    var devices =Binding;
    //var devices = Csps;
    for (var i = 0; i < devices.length; i++) {
        var device = devices[i];
        device['clicked'] = false;
        if (device.selected == false) {
            canBeSelectDevicesUl.append("<li class='li'><div class='unselected' id='setBG_" + device['id'] + "' onclick='selectDevice(" + device['id'] +
                "," + i + ")'>" + device['name'] +
                "</div></li>")
        } else {
            selectedDevicesUI.append("<li class='li'id='" + device['id'] + "'><div class='unselected'  id='setBG_" + device['id'] + "' onclick='selectDevice(" + device['id'] +
                "," + i + ")'>" + device['name'] +
                "</div></li>")
        }

    }

}

function saveBinding(url){
    if(url=="/csp/csp!searchCpsBySpId.action"){
        saveCspToCsp();
    }else if( url=="/csp/csp!searchProductBySpId.action"){
         saveProductToCsp();
    }else if(url =="/csp/cspModule!searchModulesByCspId.action"){
         saveModules();
    }else{
        saveDevices();
    }
    }
function saveModules() {
        var device = [];
        var keyId = currentCspId;
        var moduleIdAndCspIdString = "";
        var modules = Binding;
        for (var i = 0; i < modules.length; i++) {
            //noinspection JSDuplicatedDeclaration
            var device = modules[i];
        if (moduleIdAndCspIdString == "") {
            moduleIdAndCspIdString = device['id'];
        } else {
            moduleIdAndCspIdString +=  device['id'];
        }
            var selectedModules = $("#setBG_"  + device['id'] + "");
            if (device['selected'] == false) {
                moduleIdAndCspIdString += "_0";
            } else {
                moduleIdAndCspIdString += "_1";
            }
            moduleIdAndCspIdString += ",";
        }
        moduleIdAndCspIdString = moduleIdAndCspIdString.substr(0, moduleIdAndCspIdString.length - 1);
        $.ajax({
            url:'/csp/csp!saveModuleToCsp.action',
            dataType:'json',
            data:{
               'moduleIdAndCspIdString':moduleIdAndCspIdString,
               'keyId':keyId
            },
            type:'post',
            success:function (msg) {
                alert("已保存！");
                $("#deviceModal").modal("hide");
                list();
            }
        });
}
function saveDevices() {
    var name = $("#name").val();
    var devices = [];
    var id = currentCspId;
    //遍历出selectedDevices中li标签的id
    var subObjs = $("#selectedDevices li").each(function () {
        devices.push(this.id);
    });
    $.ajax({
        url:"/csp/csp!saveDeviceToCsp.action",
        dataType:'json',
        data:{
            'obj.id':id,
            'obj.name':name,
            'deviceIdsString':devices.join(",")
        },
        type:'post',
        success:function (msg) {
            alert("已保存！");
            $("#deviceModal").modal("hide");
        }
    });

}
function saveCspToCsp () {
    var csps = [];
    var id = currentCspId;
    //遍历出selectedDevices中li标签的id
    var subObjs = $("#SelectedDevices li").each(function () {
        csps.push(this.id);
    });
    $.ajax({
        url:"/csp/csp!saveCspToCsp.action",
        dataType:'json',
        data:{
            'obj.id':id,
            'cpIdsString':csps.join(",")
        },
        type:'post',
        success:function (msg) {
            alert("已保存！");
            $("#deviceModal").modal("hide");
            list();
        }
    });

}
function saveProductToCsp () {
    var name = $("#name").val();
    var devices = [];
    var id = currentCspId;
    //遍历出selectedDevices中li标签的id
    var subObjs = $("#selectedDevices li").each(function () {
        devices.push(this.id);
    });
    $.ajax({
        url:'/csp/csp!saveProductToCsp.action',
        dataType:'json',
        data:{
            'obj.id':id,
            'obj.name':name,
            'productIdsString':devices.join(",")
        },
        type:'post',
        success:function (msg) {
            alert("已保存！");
            $("#deviceModal").modal("hide");
        }
    });

}
function selectDevice(deviceId, idx) {
    var hasBeenClicked = Binding[idx]['clicked'];
    if (hasBeenClicked) {
        $("#setBG_" + deviceId).removeClass("selected");
        $("#setBG_" + deviceId).addClass("unselected");
        Binding[idx]['clicked'] = false;
    } else {
        $("#setBG_" + deviceId).removeClass("unselected");
        $("#setBG_" + deviceId).addClass("selected");
        Binding[idx]['clicked'] = true;
    }
}
function empty() {
        $("#selectedDevices").empty();
    }
function moveToRight() {
        var i = 0, l =Binding.length;
        for (; i < l; i++) {
            var d = Binding[i];
            if (d['clicked']) {
                if (!d['selected']) {
                    d['selected'] = true;
                }
            } else {
            }
        }
      renderDevices();
    }
function  moveToLeft() {
        var i = 0, l = Binding.length;
        for (; i < l; i++) {
            var d = Binding[i];
            if (d['clicked']) {
                if (d['selected']) {
                    d['selected'] = false;
                } else {
                }
            }
        }
        renderDevices();
    }



