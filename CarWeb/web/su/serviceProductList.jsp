<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 14-12-8
  Time: 上午9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <style type="text/css">
        .selected {
            background-color: #808080;
            color: #000000;
            cursor: pointer;
        }

        .unselected {
            background-color: white;
            color: #808080;
            cursor: pointer;
        }

        .li {
            height: 35px;
            font-size: 13px;
        }

        .public {
            border-width: thin;
            border-style: ridge;
            border-color: #dcdcdc;
            width: 200px;
            height: 300px;
            float: left;
        }
    </style>
</head>
<body class="no-skin">
<%@include file="../inc/displayHeader.jsp" %>
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <%@include file="/inc/displayMenu.jsp" %>
    <div class="main-content">
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try {
                    ace.settings.check('breadcrumbs', 'fixed')
                } catch (e) {
                }
            </script>

            <ul class="breadcrumb">
                <li>
                    当前位置:
                    <a href="../man.jsp"> 服务产品管理管理</a>
                </li>
                <li class="active">${folderName}</li>
                <li class="active">${functionName}</li>
            </ul>
        </div>
        <div class="page-header">
            <h1>
                <i class="ace-icon fa fa-user"></i>服务产品管理列表
            </h1>
        </div>
        <div class="page-content">
            <div class="page-content-area">
                <div class="row page-content-main">
                    <div class="input-group pull-right search-group" style="width:300px;height: 30px; margin: 9px;">
                        <div style="width: 150px;height: 30px;float: left;">
                            <input type="text" id="search_word" placeholder="名称" class="form-control search-query" >
                        </div>
                        <div style="width: 100px;height: 30px;float: left;">
                            <select id="search_status" class="form-control search-query" >
                                <option value="" onclick='SpcList.goToPage(1)'>分类</option>
                                <option value="0" onclick='SpcList.goToPage(1)'>失效</option>
                                <option value="1" onclick='SpcList.goToPage(1)'>有效</option>
                                <option value="11" onclick='SpcList.goToPage(1)'>可订</option>
                                <option value="10" onclick='SpcList.goToPage(1)'>可退</option>
                                <option value="12" onclick='SpcList.goToPage(1)'>展示</option>
                            </select>
                        </div>
                            <span class="input-group-btn">
                                <button class="btn btn-sm" type="button" id="btn_search">
                                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                                </button>
                            </span>
                    </div>
                    <div class="col-xs-12 no-padding movie-info">
                        <div class="tabbable">
                            <table class="table table-striped table-bordered table-hover table-30">
                                <thead>
                                <tr>
                                    <th class="center">名称</th>
                                    <th class="center">类型</th>
                                    <th class="center">CspId</th>
                                    <th class="center">产品名称</th>
                                    <th class="center">当前状态</th>
                                    <th class="center">是否免费</th>
                                    <th class="center">是否显示赠送</th>
                                    <th class="center">赠送服务产品</th>
                                    <th class="center">管理</th>
                                </tr>
                                </thead>
                                <tbody id="ServiceProductsContain">

                                </tbody>
                            </table>
                        </div>
                        <div class="space-6"></div>
                        <div class="row">
                            <div class="col-md-2">
                                <a class="btn btn-green btn-big" onclick="SpcList.showSpcDetail(-1)">新增设备</a>
                            </div>
                            <div class="col-md-6 col-md-offset-4">
                                <ul id="page-nav" class="pagination pull-right"></ul>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div>
    <%@include file="../inc/displayFooter.jsp" %>
     <div id="SpcDetailLog"></div>
     <div id="addGiftSpcDetailLog"></div>
     <div id="addTimeDetailLog"></div>
</div>

<!-- inline scripts related to this page -->
<script type="text/javascript">
var page_index = 1;
var page_size = 5;
jQuery(function ($) {
    $('[data-rel=tooltip]').tooltip();
    $('[data-rel=popover]').popover({html: true});
    $('textarea[class*=autosize]').autosize({append: "\n"});
    $(document).ajaxStart(function(){
        $("#loading_container").show();
    });

    $(document).ajaxStop(function(){
        setTimeout(function(){$("#loading_container").hide();}, 200);
    });


    $('textarea.limited').inputlimiter({
        remText: '%n character%s remaining...',
        limitText: 'max allowed : %n.'
    });

    $(".btn-dropdown").click(function () {
        $("#tree1").slideDown();
    });
    $("#tree1").mouseleave(function () {
        $(this).slideUp();

    });
    $("#close").click(function(){
        $("#serviceProductDetailModal").modal('hide');
    });
    $("#btn_search").click(function(){
        SpcList.goToPage(1);
    });
    SpcList.goToPage(1);

});
var SpcList;
SpcList = {
    limit:10,
    currentPage:-1,
   type:[
       {name:"按月",value:"1"},
       {name:"按次",value:"2"}
   ],
    status:[
        {name:"失效",value:"1"},
        {name:"有效",value:"0"},
        {name:"可订",value:"11"},
        {name:"可退",value:"10"},
        {name:"展示",value:"12"}
    ],
    isFree:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    isDisplayMsg:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    isDisplayGift:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    lengthUnit:[
        {name:"小时",value:"2"},
        {name:"天",value:"1"}
    ],
    autoPay:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    searchExtra:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    discount:[
        {name:"六折",value:"4"},
        {name:"七折",value:"3"},
        {name:"八折",value:"2"},
        {name:"九折",value:"1"}
    ],
    isDisplayGift:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    isDisplayMsg:[
        {name:"是",value:"1"},
        {name:"否",value:"2"}
    ],
    cspId:[
        {name:"2",value:"2"}
    ],
    getType:function(type){
        return SpcList.getTextOfArray(type,SpcList.type,'value','name');
    },
    getStatus:function(status){
        return SpcList.getTextOfArray(status,SpcList.status,'value','name');
    },
    getIsFree:function(isFree){
        return SpcList.getTextOfArray(isFree,SpcList.isFree,'value','name');
    },
    getIsDisplayGift:function(isDisplayGift){
        return SpcList.getTextOfArray(isDisplayGift,SpcList.isDisplayGift,'value','name');
    },
    getCspId:function(cspId){
        return SpcList.getTextOfArray(cspId,SpcList.cspId,'value','name');
    },
    getIsDisplayManager:function(type,id){
        if(type != 2){
            return '<a style="cursor: pointer" onclick="SpcList.giftSpcBinding('+id+')">'+'管理'+'</a>';
        }else{
            return '  ';
        }
    },
    renderSpc:function (jsonData) {
        rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /SpcList.limit),
                SpcList.currentPage,"SpcList.goToPage", jsonData['totalCount']);
        var spcs = jsonData['objs'];
        var result = '';
        if (spcs != null) {
            var i = 0, l = spcs.length;
            for (; i < l; i++) {
                var sp = spcs[i];
                result +=
                        '<tr>' +
                                '<td class="center">' + sp.name + '</td>' +
                                '<td class="center">' + SpcList.getType(sp['type'])+'</td>' +
                                '<td class="center">' + SpcList.getCspId(sp['cspId']) + '</td>' +
                                '<td class="center">' + sp.productName + '</td>' +
                                '<td class="center">' + SpcList.getStatus(sp['status'])+'</td>' +
                                '<td class="center">' + SpcList.getIsFree(sp['isFree'])+'</td>' +
                                '<td class="center">' + SpcList.getIsDisplayGift(sp['isDisplayGift'])+'</td>' +
                                '<td class="center">'+SpcList.getIsDisplayManager(sp['type'],sp['id'])+'</td>'+
                                '<td class="center"><a class="btn btn-grey btn-xs"  onclick="SpcList.showSpcDetail(' + sp.id +
                                ');return false;">' +
                                '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                                '<a class="btn btn-grey btn-xs" onclick="SpcList.deleteProduct('+sp.id+
                                ');return false;">'+
                                '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                '</a></td>' +
                                '</tr>';
            }
        }
        $('#ServiceProductsContain').html(result);
    },
    listSpc:function () {
        $.ajax({
            url:'/product/serviceProduct!list.action',
            data:({"obj.name":$("#search_word").val(),"obj.status":$("#search_status").val(),limit:SpcList.limit, start:(SpcList.currentPage - 1) * SpcList.limit }),
            method:'post',
            dataType:'json',
            success:function (jsonData) {
                SpcList.renderSpc(jsonData);

            }
        });
    },
    loadProductName:function (id){
        $.ajax({
            url:'/product/product!list.action?keyId='+id,
            type:'post',
            dataType:'text',
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                for( var i=0; i< response.objs.length; i++){
                    var product = response.objs[i];
                    if(product){
                        $("#productId").append($("<option/>", {
                            value: product.id,
                            text: product.name
                        }));
                    }
                }
            }
        });

    },

    showSpcDetail:function(id){
            SpcList.currentProductId = id;
        showDialog({id:'serviceProductDetailModal', renderTo:'SpcDetailLog', title:'绑定设备',
             items:[
                 {html: '<div><table style="height:350px">'+
                '<tr><td align="right">名称:<input style="width: 150px;"id="name">'+
                '<td align="right">打折:<select id="discount" style="width:150px;"></select></td>'+
                '</tr><tr>'+
                '<td align="right">类型:<select id="type" style="width: 150px"></select></td>'+
                '<td align="right">打折起始日期:<input style="width: 150px;"id="discountStartTime"></td>'+
                '</tr><tr>'+
                '<td align="right">产品名称:<select style="width: 150px" id="productId"></select></td>'+
                '<td align="right">打折结束日期:<input style="width: 150px;" id="discountEndTime"></td>'+
                '</tr><tr>'+
                '<td align="right">有效时长:<input style="width: 150px" id="validLength"></td>'+
                '<td align="right">是否免费:<select style="width: 150px;" id="isFree"></select></td>'+
                '</tr><tr> '+
                '<td align="right">计时单元:<select style=" width: 150px" id="lengthUnit"></select></td> '+
                '<td align="right">免费起始时间:<input style="width: 150px" id="freeStartTime"></td>'+
                '</tr><tr>'+
                '<td align="right">当前状态:<select style="width: 150px" id="status"></select></td>'+
                '<td align="right">免费结束时间:<input style="width: 150px" id="freeEndTime"></td>'+
                '</tr><tr> '+
                '<td align="right">自动续订:<select style="width: 150px" id="autoPay"></select></td>'+
                '<td align="right">需要显示的信息:<input style="width: 150px" id="msg"></td> '+
                '</tr><tr>'+
                '<td align="right">扩展搜索:<select id="searchExtra" style="width: 150px"></select></td> '+
               '<td align="right">信息提示起始时间:<input style="width: 150px" id="msgStartTime"></td>'+
                '</tr><tr>'+
                '<td align="right">是否显示赠送:<select style="width: 150px" id="isDisplayGift"></select></td>'+
                '<td align="right">信息提示结束时间:<input style="width: 150px" id="msgEndTime"></td>'+
                '</tr><tr>'+
                '<td align="right">信息显示?:<select style="width: 150px" id="isDisplayMsg"></select></td>'+
                '<td align="right"><input style="width: 150px" id="cspId" value="2" type="hidden"></td>'+
                '</tr></table></div>'}
             ] ,
        buttons:[
            {text:'确定', cls:'btn-blue', handler:SpcList.saveSpc},
            {text:'关闭', style:'margin-left:250px;', handler:function () {
                $("#serviceProductDetailModal").modal('hide');
            }}
        ]// ,
       //     hiddenItems:[{id:'cspId',value:2}]
        });
        $.ajax({
            url:'/product/serviceProduct!view.action?keyId='+id,
            dataType:'json',
            success:function (jsonData) {
                var data = jsonData['data'];
                if (data != null) {
                    $("#name").val(data['obj.name']);
                    SpcList.createSelect('discount',SpcList.discount,data['obj.discount']);
                    SpcList.createSelect('type',SpcList.type,data['obj.type']);
                    $("#discountStartTime").val(data['obj.discountStartTime']);
                    $("#productId").val(data['obj.productId']);
                    $("#discountEndTime").val(data['obj.discountEndTime']);
                    $("#validLength").val(data['obj.validLength']);
                    SpcList.createSelect('isFree',SpcList.isFree,data['obj.isFree']);
                    SpcList.createSelect('lengthUnit',SpcList.lengthUnit,data['obj.lengthUnit']);
                    $("#freeStartTime").val(data['obj.freeStartTime']);
                    SpcList.createSelect('status',SpcList.status,data['obj.status']);
                    $("#freeEndTime").val(data['obj.freeEndTime']);
                    SpcList.createSelect('autoPay',SpcList.autoPay,data['obj.autoPay']);
                    $("#msg").val(data['obj.msg']);
                    SpcList.createSelect('searchExtra',SpcList.searchExtra,data['obj.searchExtra']);
                    $("#msgStartTime").val(data['obj.msgStartTime']);
                    SpcList.createSelect('isDisplayGift',SpcList.isDisplayGift,data['obj.isDisplayGift']);
                    $("#msgEndTime").val(data['obj.msgEndTime']);
                    SpcList.createSelect('isDisplayMsg',SpcList.isDisplayMsg,data['obj.isDisplayMsg']);

                }
            }
        });
        SpcList.loadProductName(id);
        },
    saveSpc:function(){
        var id=SpcList.currentProductId;
        if (id == null || typeof(id) == 'undefined' || id == '-1' || id == '0' || id == 0) {
            id = -1;
        }
        if (id != -1 && !confirm("您确认您要保存吗？\r\n")) {
            return;
        }
        var cspId = $("#cspId").val();
        var name = $("#name").val();
        var discount = $("#discount").val();
        var type = $("#type").val();
        var discountStartTime = $("#discountStartTime").val();
        var productId = $("#productId").val();
        var discountEndTime = $("#discountEndTime").val();
        var validLength = $("#validLength").val();
        var isFree = $("#isFree").val();
        var lengthUnit = $("#lengthUnit").val();
        var freeStartTime = $("#freeStartTime").val();
        var status = $("#status").val();
        var freeEndTime = $("#freeEndTime").val();
        var autoPay = $("#autoPay").val();
        var msg = $("#msg").val();
        var searchExtra = $("#searchExtra").val();
        var msgStartTime = $("#msgStartTime").val();
        var isDisplayGift = $("#isDisplayGift").val();
        var msgEndTime = $("#msgEndTime").val();
        var isDisplayMsg = $("#isDisplayMsg").val();
        $.ajax({
            url:'/product/serviceProduct!save.action',
            method:'post',
            data:{
                'obj.cspId':cspId,
                'obj.id':id,
                'obj.name':name,
                'obj.discount':discount,
                'obj.type':type,
                'obj.discountStartTime':discountStartTime,
                'obj.productId':productId,
                'obj.discountEndTime':discountEndTime,
                'obj.validLength':validLength,
                'obj.isFree':isFree,
                'obj.lengthUnit':lengthUnit,
                'obj.freeStartTime':freeStartTime,
                'obj.status':status,
                'obj.freeEndTime':freeEndTime,
                'obj.autoPay':autoPay,
                'obj.msg':msg,
                'obj.searchExtra':searchExtra,
                'obj.msgStartTime':msgStartTime,
                'obj.isDisplayGift':isDisplayGift,
                'obj.msgEndTime':msgEndTime,
                'obj.isDisplayMsg':isDisplayMsg
            },
            dataType:'text',
            success:function () {
                alert("已保存！");
                $("#serviceProductDetailModal").modal("hide");
                SpcList.goToPage(1);
            }
        }).fail(function () {
                    alert("失败！");
                });
    },
    deleteProduct:function (id) {
            if (confirm("您确认要删除该记录吗？")) {
                $.ajax({
                    url:"/product/serviceProduct!delete.action",
                    data:{keyId:id},
                    dataType:'json',
                    success:function (jsonData) {
                        alert("删除完毕！");
                        SpcList.goToPage(1);
                    }
                });
            }
    },
    currentProductId:-1,
    giftSpcBinding:function(id){
        SpcList.currentProductId = id;
        showDialog({id:'addGiftModal', renderTo:'addGiftSpcDetailLog', title:'赠送服务产品添加',width:'500',
            items:[
                {html:'<div class="public">' +
                        '<span><p class="center">可选服务产品名称</p></span>' +
                        '<div>' +
                        '<ul id="canBeSelectSpc" style=" max-height:300;overflow-y:auto;list-style:none;margin:0px; "></ul>' +
                        '</div></div>' +
                        '<div style="width: 30px;height: 300px;float: left;">' +
                        '<button type="button"style="margin-top: 100px;margin-left: 10px;" onclick="SpcList.moveToRight()"><img src="/images/report/g4.jpg"></button>' +
                        '<button type="button" style="margin-top: 50px;margin-left: 10px;"onclick="SpcList.moveToLeft()"><img src="/images/report/g3.jpg"></button>' +
                        '</div>' +
                        '<div class="public" style="margin-left: 20px">' +
                        '<span><p class="center">已选服务产品</p></span>' +
                        '<span class="btn btn-grey" style="width: 200px" onclick="SpcList.empty()">清空</span>' + "<div>" +
                        '<ul id="selectedSpc" style=" max-height:300;overflow-y:auto;list-style:none;margin:0px; "></ul>' +
                        '</div>' +
                        '</div>'}
            ],
            buttons:[
                {text:'确定', cls:'btn-blue', handler:SpcList.saveGiftSpc},
                {text:'关闭', style:'margin-left:250px;', handler:function () {
                    $("#addGiftModal").modal('hide');
                }}
            ]
        });
        $.ajax({
            url:'/product/serviceProduct!searchServiceProduct.action',
            dataType:'text',
            success:function (msg) {
                var response = eval("(function(){return " + msg + ";})()");
                SpcList.spc = response.objs;
                SpcList.renderAddSpc();
            }
        });
    },
    spc:[],
    renderAddSpc:function () {
        var canBeSelectSpcUl = $("#canBeSelectSpc");
        canBeSelectSpcUl.html('');
        var selectedSpcUI = $("#selectedSpc");
        selectedSpcUI.html('');
        var products = SpcList.spc;
        for (var i = 0; i < products.length; i++) {
            var pc = products[i];
            pc['clicked'] = false;
            if (pc['selected'] == false) {
                canBeSelectSpcUl.append("<li class='li'><div class='unselected' id='m" + pc['id'] + "' onclick='SpcList.selectSpc(" + pc['id'] +
                        "," + i + ")'>" + pc['name'] +
                        "</div></li>");
            }else{
                selectedSpcUI.append("<li class='li' id='" + pc['id'] + "'><div class='unselected' id='m" + pc['id'] + "' onclick='SpcList.selectSpc(" + pc['id'] +
                        "," + i + ")'>" + pc['name'] +
                        "<button type='button' onclick='SpcList.addTime()'>添加时间</button></div></li>");
            }
        }
    },
    addTime:function(){
        showDialog({id:'addTimeModal', renderTo:'addTimeDetailLog', title:'添加时间',width:450, height:400,
            items:[
                {fieldLabel:'开始时间',id:'startTime',allowBlank:false},
                {fieldLabel:'结束时间',id:'endTime',allowBlank:false}
            ],
            buttons:[
                {text:'确定', cls:'btn-blue', handler:SpcList.saveAddTime},
                {text:'关闭',  handler:function () {
                    $("#addTimeModal").modal('hide');
                }}
            ]
        });
    },
    saveAddTime:function(){
        var id = SpcList.currentProductId;
        var startTime = $("#startTime");
        var endTime = $("#endTime");
        $.ajax({
            url:'/product/serviceProductGift!saveServiceProductGift.action?keyId='+id,
            method:'post',
            dataType:'text',
            data:{
                'obj.id':id,
                'obj.startTime':startTime,
                'obj.endTime':endTime
            },
            success:function (msg) {
                alert("保存");
                $("#addTimeModal").modal("hide");
            }
        });
    },
    selectSpc:function (productId, idx) {
        var hasBeenClicked = SpcList.spc[idx]['clicked'];
        if (hasBeenClicked) {
            $("#m" + productId).removeClass("selected");
            $("#m" + productId).addClass("unselected");
            SpcList.spc[idx]['clicked'] = false;
        } else {
            $("#m" + productId).removeClass("unselected");
            $("#m" + productId).addClass("selected");
            SpcList.spc[idx]['clicked'] = true;
        }
    },
    empty:function () {
        $("#selectedSpc").empty();
    },
    moveToRight:function () {
        var i = 0, l = SpcList.spc.length;
        for (; i < l; i++) {
            var d = SpcList.spc[i];
            if (d['clicked']) {
                if (!d['selected']) {
                    d['selected'] = true;
                }
            } else {
            }
        }
        SpcList.renderAddSpc();
    },
    moveToLeft:function () {
        var i = 0, l = SpcList.spc.length;
        for (; i < l; i++) {
            var d = SpcList.spc[i];
            if (d['clicked']) {
                if (d['selected']) {
                    d['selected'] = false;
                } else {
                }
            }
        }
        SpcList.renderAddSpc();
    },
    saveGiftSpc:function(){
        var Spc = [];
        var keyId = SpcList.currentProductId;
        var serviceProductGiftString = "";
        var spcs = SpcList.spc;
        for (var i = 0; i < spcs.length; i++) {
            var spc = spcs[i];
            if (serviceProductGiftString == "") {
                serviceProductGiftString = spc['id'];
            } else {
                serviceProductGiftString +=  spc['id'];
            }
            var selectedSpc = $("#m" +spc['id'] + "");
            if (spc['selected'] == true) {
                serviceProductGiftString += "_"+spc['startTime']+"_"+spc['endTime'];
            } else {
            }
            serviceProductGiftString += ",";
        }
        serviceProductGiftString = serviceProductGiftString.substr(0, serviceProductGiftString.length - 1);

        //遍历出selectedDevices中li标签的id
        var subObjs = $("#selectedSpc li").each(function () {
            spcs.push(this.id);
        });
        $.ajax({
            url:'/product/serviceProductGift!saveServiceProductGift.action',
            method:'post',
            dataType:'text',
            data:{
                'obj.id':keyId,
                'serviceProductGiftString':serviceProductGiftString
            },
            success:function (msg) {
                alert("已保存！");
                SpcList.goToPage(1);
                $("#addGiftModal").modal("hide");
            }
        });
    },

    goToPage:function (pageNo) {
        SpcList.currentPage = pageNo;
        SpcList.listSpc();
    },
    createSelect:function (id, options, value) {
        value = parseInt(value);
        var selectCmp = $("#" + id);
        selectCmp.html('');
        var i = 0, l = options.length;
        for (; i < l; i++) {
            var option = options[i];
            var selected = false;
            var val = parseInt(option['value']);
            if (val == value) {
                selected = true;

            }
            selectCmp.append($("<option/>", {
                value:val,
                selected:selected,
                text:option.name
            }));
        }
    },
    getTextOfArray:function(val,data,valueField,displayField){
        var i= 0,l=data.length;
        for(;i<l;i++){
            var m = data[i];
            if(m[valueField]==val){
                return m[displayField];


            }
        }
        return '其他';
    }
};
</script>

</body>
</html>