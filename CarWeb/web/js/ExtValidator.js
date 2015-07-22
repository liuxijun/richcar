///*
//校验登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串
Ext.form.VTypes['loginVal'] = /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$/;
Ext.form.VTypes['stbVal'] = /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){3,8}$/;
//Ext.form.VTypes['loginMask'] = /[A-Za-z0-9]/;//限制输入
Ext.form.VTypes['loginText'] = '格式有误：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串';//错误信息

var isExists = false;
var o;//用作参数信息
var checkText;
Ext.form.VTypes['login'] = function(v){//校验用户的login是否合适
   if(actionHeader == "admin" || actionHeader == "user"){
       var result = Ext.form.VTypes['loginVal'].test(v);
       window.status = '开始校验login'+new Date();
       if(!result){
           Ext.form.VTypes['loginText'] = '格式有误：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串';//错误信息
           window.status = '输入有问题'+new Date();
           ReturnValue(false);
       }
   }
    if(actionHeader == "admin" || actionHeader == "user" ||  actionHeader=="stb"){//检查这个login是否重复
        if(actionHeader=="stb"){
            o="obj.serialNo";
            checkText="系列";
        }
        if(actionHeader == "admin" || actionHeader == "user"){
            o="obj.login";
            checkText="用户";
        }
        if(!modifyOldData){//新建时才做这个检查
            Ext.Ajax.request({//通过Ajax来进行验证，注意，这是一个异步的过程！所以要调用内置的ReturnValue
                url:getActionUrl('checkExists',o+'='+v),
                //params:parameters,
                callback : function(opt, success, response) {
                    //alert(response.responseText);
                    try{
                        var obj = Ext.util.JSON.decode(response.responseText);
                        //alert(obj.success);
                        if (obj.success=="true"){//用户存在，有问题！
                            Ext.form.VTypes['loginText'] = '该'+checkText+'已经存在';//错误信息
                            result = false;
                            window.status = checkText+'“'+v+'”已经存在！'+new Date();
                            ReturnValue(false);
                        } else {
                            window.status =checkText+'“'+v+'”不存在！'+new Date();
                            ReturnValue( true );
                        }
                    }catch(e){
                        //alert(e.description);
                        ReturnValue(false);
                    }
                }

            });
        }else{
            window.status = '修改数据，无需校验。'+new Date();
            ReturnValue(true);
        }
    }else{
        window.status = '只有operator和user需要校验！'+new Date();
        ReturnValue(true);
    }
 //}
    function ReturnValue(ok) {// 此方法必须放方法里面里面。
        isExists = ok;
    }
    return isExists;
};

//目录
Ext.form.VTypes['pathVal'] = /^[a-zA-Z0-9/_.:\-\\]{0,19}$/;///^[a-zA-Z0-9][/._]{1,20}$/;
//Ext.form.VTypes['loginMask'] = /[A-Za-z0-9]/;//限制输入
Ext.form.VTypes['pathText'] = '格式有误：只能输入1-20个以字母“/”、“_”、“.”的字串';//错误信息
Ext.form.VTypes['path'] = function(v){//测试函数
    return Ext.form.VTypes['pathVal'].test(v);
};

Ext.form.VTypes['numberVal'] = /^[0-9]{0,15}$/;//数字
Ext.form.VTypes['numberMask'] = /[0-9]/;//限制输入
Ext.form.VTypes['numberText'] = '必须是1位至5位0以上数字';//错误信息
Ext.form.VTypes['number'] = function(v){//测试函数
    var vCheckResult =  Ext.form.VTypes['numberVal'].test(v);
    if(vCheckResult) {
        return !(v == "0" || v == "00" || v == "000" || v == "0000" || v == "00000");
    }else{
        return vCheckResult;
    }
};
Ext.apply(Ext.form.VTypes,{
    confirmPassword:function(val,field){//val指这里的文本框值，field指这个文本框组件，大家要明白这个意思
       if(field.confirmTo){//confirmTo是我们自定义的配置参数，一般用来保存另外的组件的id值
           var pwd=Ext.getCmp(field.confirmTo);//取得confirmTo的那个id的值
//           outDebugMsg("检查用户口令："+val+"=="+pwd.getValue());
           return (val==pwd.getValue());
       }else{
//           outDebugMsg("confirmTo为空！")
       }
       return true;
    }
});