var DataSourceTree = function(options) {
    this._data 	= options.data;
    this._delay = options.delay;
};

DataSourceTree.prototype.data = function(options, callback) {
    var self = this;
    var $data = null;

    if(!("name" in options) && !("type" in options)){
        $data = this._data;//the root tree
        callback({ data: $data });
        return;
    }
    else if("type" in options && options.type == "folder") {
        if("additionalParameters" in options && "children" in options.additionalParameters)
            $data = options.additionalParameters.children;
        else $data = {}//no data
    }

    if($data != null)//this setTimeout is only for mimicking some random delay
        setTimeout(function(){callback({ data: $data });} , parseInt(Math.random() * 50) + 20);

    //we have used static data here
    //but you can retrieve your data dynamically from a server using ajax call
    //checkout examples/treeview.html and examples/treeview.js for more info
};

function in_array(v,a){
    // like jquery, $.inArray, no type compare, return true/false
    if(!a || a.length == 0) return false;
    for(var i=0; i<a.length; i++){
        if(a[i] == v) return true;
    }

    return false;
}

function isRegisterUserName(s){
    var regex =/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,20}$/;
    return regex.exec(s);
}

function isValidateEmail(email) {
    var regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(email);
}

/*11位长的数字串*/
function isCellPhoneNumber(s){
    var regex = /^\d{11}$/;
    return regex.test(s);
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/*global setting func*/
function _g_fn_show_setting(){
    $("#_admin_setting_dialog").show();
    $("#_global_admin_name").select();
}

function _g_fn_close_setting(){
    $("#_admin_setting_dialog").hide();
}

function _g_fn_save_setting(adminId){
    if(!$.trim( $("#_global_admin_name").val())){
        $("#_global_admin_name").attr("data-original-title","请输入姓名");
        $("#_global_admin_name").addClass('tooltip-error').tooltip('show');
        return;
    }

    if(!!$("#_global_admin_password").val()){
        if($("#_global_admin_password").val() != $("#_global_admin_retype_password").val()){
            $("#_global_admin_retype_password").attr("data-original-title","密码不匹配");
            $("#_global_admin_retype_password").addClass('tooltip-error').tooltip('show');
            return;
        }
    }

    var pwd = (!!$("#_global_admin_password").val())? hex_md5($("#_global_admin_password").val()):"";
    //submit
    $.ajax({
        type: "POST",
        url: "/security/admin!modSelf.action",
        //dataType: "json",
        dataType: "text",
        data: {'obj.id':adminId,'obj.realname':$.trim( $("#_global_admin_name").val()),"obj.password":pwd},
        //data: {name: ver},
        success: function(msg){
            var response = eval("(function(){return " + msg + ";})()");
            if (response.success) {
                $("#_global_href_name").html($.trim( $("#_global_admin_name").val()));
                $("#_admin_setting_dialog").hide();
            } else {
                var errorMsg = response.error[0];
                alert(errorMsg);
            }
         }
    });
}

jQuery.fn.extend({
    scrollToMe: function () {
        var x = jQuery(this).offset().top + 100;
        jQuery('html,body').animate({scrollTop: x}, 500);
    }});

var globalSn=1;
function isArray(arrayObj){
    try {
        return Object.prototype.toString.apply(arrayObj) === "[object Array]";
    } catch (e) {
        return false;
    }
}
function getParameter(parameters,name,defVal){
    var r=parameters[name];
    if(r==null||typeof(r)=='undefined'){
        return defVal;
    }
    return r;
}
function showGrid(parameters){
    var gridId = getParameter(parameters,'id','gridId_'+(globalSn++));
    var columns = getParameter(parameters,'columns',[]);
    var result = '';
    for(var i= 0,l=columns.length;i<l;i++){

    }
}

function showDialog(parameters){
    var dialogId = parameters['id'];
    if(dialogId==null){
        dialogId = 'dialogId_'+globalSn;
        globalSn++;
    }
    var formId = parameters['formId'];
    if(formId==null){
        formId = 'formId_'+globalSn;
        globalSn++;
    }
    var title = parameters['title'];
    if(title==null){
        title='对话框';
    }
    var width = parameters['width'];
    if(width == null){
        width = 560;
    }
    var result = '<div class="modal fade modal-film" id="' +dialogId+'" tabindex="-1" role="dialog" aria-labelledby="menuDetailModalLabel" aria-hidden="true">\r\n'+
            ' <div class="modal-dialog" style="position: absolute; left: 50%; margin-left: -280px; top: 50%; margin-top: -250px; width: ' +width+'px;">\r\n'+
            '   <div class="modal-content">\r\n'+
            '    <div class="modal-header">\r\n'+
            '       <button type="button" class="close" data-dismiss="modal" id="closeButton"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>\r\n'+
            '\t<h4 class="modal-title" id="menuDetailModalLabel">' +title+'</h4>\r\n'+
            '    </div>';
    result+='    <div class="modal-body">\r\n'+
            '      <div class="row">\r\n'+
            '\t<div class="col-xs-12 ">\r\n'+
            '\t    <form class="form-horizontal" id="' +formId+'">\r\n';

    var items = parameters['items'];
    if(items!=null&&typeof(items)!='undefined'&&isArray(items)){
        var i= 0,l=items.length;
        for(;i<l;i++){
            var item = items[i];
            var html = item['html'];
            if(typeof(html)!='undefined'&&html!=null){
                result+=html;
                continue;
            }
            var fieldLabel = item['fieldLabel'];
            if(fieldLabel==null){
                fieldLabel = '';
            }
            var  fieldId = item['id'];
            if(fieldId == null){
                fieldId = item['name'];
            }
            if(fieldId == null){
                fieldId = 'fieldId_'+globalSn;
                globalSn++;
            }
            var fieldName = item['name'];
            if(fieldName == null){
                fieldName = fieldId;
            }
            var type = item['type'];
            if(type==null){
                type = item['xtype'];
                if(type==null){
                    type = 'text';
                }
            }
            var allowBlank = item['allowBlank'];
            if(typeof(allowBlank)=='undefined'||allowBlank==null){
                allowBlank = true;
            }else{
            }
            var cls = item['cls'];
            if(cls == null){
                cls='';
            }else{
                cls = ' '+cls;
            }
            var style = item['style'];
            if(style==null){
                style = '';
            }else{
                style = ' style="'+style+'"';
            }

            result+='\t      <div class="form-group">\r\n'+
                '\t\t<label class="col-sm-3 control-label no-padding-right';
            if(!allowBlank){
                result+=' filed-need';
            }
            result +='">' +fieldLabel+'</label>\r\n';
            result +='\t\t<div class="col-sm-9">\r\n';
            var value = item['value'];
            if(value==null){
                value = '';
            }
            if(type=='text'){
                result +='\t\t  <input type="'+type+'" id="'+fieldId+'" name="'+fieldName+'"' +
                    ' class="col-sm-12' +cls+
                    '" value="' +value+'" ' +style+
                    '>';
            }else if(type=='select'){
                var handler = item['handler'];
                if(handler!=null&&handler!=''){
                    handler = ' onchange="'+handler+'(this.value)"';
                }
                result +='\t\t  <select class="col-sm-12' +cls+
                    '" id="'+fieldId+'" name="'+fieldName+'" value="' +value+'"'
                    +style+handler+
                    '>\r\n';
                var subItems = item['items'];
                if(subItems&&isArray(subItems)){
                    var k= 0,kl=subItems.length;
                    for(;k<kl;k++){
                        var si = subItems[k];
                        var sv = si['value'];
                        var selected = '';
                        if(sv==value){
                            selected = ' selected="true"';
                        }
                        result+='\t\t      <option value="' +si['value']+'"' +selected+
                            '>' +si['boxLabel']+'</option>\r\n';
                    }
                }
                result+='\t\t    </select>\r\n';
            }else if(type=='checkboxGroup'||type=='radioGroup'){
                //result +='<\r\n'+type+' id="'+fieldId+'" name="'+fieldName+'" >\r\n'+item['boxLabel'];
                var groupItems = item['items'];
                result+='\t\t    ';
                if(groupItems&&isArray(groupItems)){
                    var g= 0,gl=groupItems.length;
                    for(;g<gl;g++){
                        var gi = groupItems[k];
                        var gv = gi['value'];
                        var checked = '';
                        if(gv==value){
                            checked = ' checked="true"';
                        }
                        result+='<'+type+' value="' +si['value']+'"' +checked+
                            ' name="'+fieldName+'">' +si['boxLabel']+'</'+type+'>';
                    }
                }
                result+='\r\n';
            }else if(type=='image'){
                var imgH = item['height'];
                var imgW = item['width'];
                if(imgH!=null&&imgH!=''&&!(typeof(imgH)=='undefined')){
                    imgH = ' height="'+imgH+'"';
                }
                if(imgW!=null&&imgW!=''&&!(typeof(imgW)=='undefined')){
                    imgW = ' width="'+imgW+'"';
                }
                result+='\t\t    <img id="'+fieldId+'" '+imgH+imgW+' src="'+item['src']+'">\r\n';
            }else if(type=='textarea'){
                var height = item['height'];
                if(height==null){
                    height = 100;
                }
                result+='\t\t    <'+type+' id="'+fieldId+'" name="'+fieldName+'" class="col-sm-12' +cls+
                    '"' + style+
                    ' style="height:' +height+
                    'px">'+value+'</'+type+">\r\n";
            }else{
                var divHeight = item['height'];
                if(divHeight==null){
                    divHeight = '';
                }else{
                    if(style==''){
                        style=' style="height:'+height+'px"';
                    }else{
                        style = style.substring(0,style.length-1)+";height:"+height+'px"';
                    }
                }
                if(cls!=''){
                    cls = 'class="'+cls.substring(1)+'"';
                }
                result+='\t\t    <'+type+'" id="'+fieldId+'" name="'+fieldName+'"' +cls+
                    style+'>'+value+'</'+type+">\r\n";
            }
            result+='\t\t</div>\r\n\t      </div>\r\n';
        }
    }
    result+='\t\t<div class="space"></div>\r\n';
    var buttons = parameters['buttons'];
    if(buttons!=null&&isArray(buttons)){
        var idx= 0,len=buttons.length;
        for(;idx<len;idx++){
            var button = buttons[idx];
            var buttonId = button['id'];
            if(buttonId==null){
                buttonId = 'buttonId_'+globalSn;
                globalSn++;
                button['id']=buttonId;
            }

            var extraCls = button['cls'];
            if(extraCls == null){
                extraCls='';
            }else{
                extraCls = ' '+extraCls;
            }
            var extraStyle = button['style'];
            if(extraStyle==null){
                extraStyle = '';
            }else{
                extraStyle = ' style="'+extraStyle+'"';
            }
            result +='\t\t<span class="btn'+extraCls+'" id="' +buttonId+'" '+extraStyle+'>'+button['text']+'</span>\r\n';
        }
    }
    var hiddenItems = parameters['hiddenItems'];
    if(hiddenItems!=null&&isArray(hiddenItems)){
        var h= 0,hlen=hiddenItems.length;
        for(;h<hlen;h++){
            var hidden = hiddenItems[h];
            var hid = hidden['id'];
            if(hid==null){
                hid = 'hiddenId_'+globalSn;
                globalSn++;
            }
            var val = hidden['value'];
            if(val==null||typeof(val)=='undefined'){
                val = '';
            }else{
                val = ' value="'+val+'"';
            }
            result +='\t\t<input type="hidden" name="' +hid+'" id="' +hid+'"'+val+'/>\r\n';
        }
    }
    result += '\t   </form>\r\n\t </div>\r\n       </div>\r\n'+
              '\t\t  <div class="modal-footer" style="display: none;">\r\n'+
              '\t\t      <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>\r\n'+
              '\t\t      <button type="button" class="btn btn-primary">Save changes</button>\r\n'+
              '\t\t  </div>\r\n'+
              '\t      </div>\r\n'+
              '\t  </div>\r\n</div>';
    var renderTo = parameters['renderTo'];
    if(renderTo==null){
        renderTo = 'renderToEleId_'+globalSn;
        globalSn++;
    }
    //alert(result);
    $("#"+renderTo).html(result);
    $("#"+dialogId).modal('show');
    $("#closeButton").click(function(){
        $("#"+dialogId).modal("hide");
    });
    if(buttons!=null){
        idx = 0;len = buttons.length;
        for(;idx<len;idx++){
            var buttonObj = buttons[idx];
            var clickHandler = buttonObj['handler'];
            if(clickHandler!=null&&clickHandler!=''&&!(typeof(clickHandler)=='undefined')){
                $("#"+buttonObj.id).click(clickHandler);
            }
        }
    }
}

// 从url中解析参数值
(function($){
    $.getQuery = function( query, defaultValue ) {
        query = query.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
        var expr = "[\\?&]"+query+"=([^&#]*)";
        var regex = new RegExp( expr );
        var results = regex.exec( window.location.href );
        if( results !== null ) {
            //return results[1];
            return decodeURIComponent(results[1].replace(/\+/g, " "));
        } else {
            return defaultValue;
        }
    };
})(jQuery);
