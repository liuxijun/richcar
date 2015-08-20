<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<%@include file="../inc/displayHeader.jsp" %>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>
<%@include file="/inc/displayMenu.jsp" %>
<div class="main-content">
    <!-- #section:basics/content.breadcrumbs -->
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
                <a href="../man.jsp"> 网站首页</a>
            </li>
            <li class="active">${folderName}</li><li class="active">${functionName}</li>
        </ul>
        <!-- /.breadcrumb -->


        <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-user"></i>产品管理
        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
        <div class="page-content-area">
            <div class="row page-content-main">
                <div class="input-group pull-right search-group" style="width: 450px;height: 30px; margin: 9px;">
                    <div style="width: 150px;height: 30px;float: left;">
                        <input type="text" id="search_word" placeholder="名称" class="form-control" >
                    </div>
                    <div style="width: 150px;height: 30px;float: left;">
                        <input type="text" id="search_num" placeholder="信息层编号" class="form-control" >
                    </div>
                    <div style="width: 100px;height: 30px;float: left;">
                        <select id="search_status" class="form-control search-query" >
                            <option value="" onclick='productList.goToPage(1)'>分类</option>
                            <option value="0" onclick='productList.goToPage(1)'>失效</option>
                            <option value="1" onclick='productList.goToPage(1)'>有效</option>
                            <option value="11" onclick='productList.goToPage(1)'>可订</option>
                            <option value="10" onclick='productList.goToPage(1)'>可退</option>
                            <option value="12" onclick='productList.goToPage(1)'>展示</option>
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
                        <table class="table table-striped table-bordered table-hover table-30" id="tabAuditProduct">
                            <thead>
                            <tr>
                            <%--    <th width="55px" class="center"><label>
                                    <input type="checkbox" class="ace" name="form-field-checkbox" id="select_all">
                                    <span class="lbl"></span>
                                </label></th>--%>
                                <th class="center"><a href="#" onclick='productList.orderBy("name")'>名称</a></th>
                                <th class="center"><a href="#" onclick='productList.orderBy("payProductNo")'>信息层编号</a></th>
                                <th class="center"><a href="#" onclick='productList.orderBy("price")'>价格</a></th>
                                <th class="center"><a href="#" onclick='productList.orderBy("type")'>类型</a></th>
                                <th class="center"><a href="#" onclick='productList.orderBy("validLength")'>有效期</a></th>
                                <th class="center"><a href="#" onclick='productList.orderBy("status")'>当前状态</a></th>
                                <th class="center">管理</th>
                            </tr>
                            </thead>
                            <tbody id="productsContain">
                            </tbody>
                        </table>
                    </div>
                    <div class="space-6"></div>
                    <div class="row">
                        <div class="col-md-2">
                            <a class="btn btn-green btn-big" onclick="productList.showProductsDetail(-1)">新增设备</a>
                        </div>
                        <div class="col-md-6 col-md-offset-4">
                            <ul id="page-nav" class="pagination pull-right">
                                <li class="disabled">
                                    <a href="#">
                                        首页
                                    </a>
                                </li>
                                <li class="disabled">
                                    <a href="#">
                                        上一步
                                    </a>
                                </li>
                                <li class="active">
                                    <a href="#">1</a>
                                </li>

                                <li>
                                    <a href="#">2</a>
                                </li>

                                <li>
                                    <a href="#">3</a>
                                </li>

                                <li>
                                    <a href="#">4</a>
                                </li>

                                <li>
                                    <a href="#">5</a>
                                </li>

                                <li>
                                    <a href="#">
                                        下一步
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        尾页
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content-area -->
        </div>
        <!-- /.page-content -->
    </div>
    <!-- /.main-content -->


    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div>
<!-- /.main-container -->
<%@include file="../inc/displayFooter.jsp" %>
    <!-- Modal -->
 <div id="productDetailLog"></div>

<%--suppress HtmlUnknownAttribute --%>
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
      /*  $("#select_all").click(function(){
            var checkboxes= $("#tabAuditProduct tbody input:checkbox");
            if(checkboxes){
                if ($(this).prop('checked')) {
                    checkboxes.prop('checked', true);
                } else {
                    checkboxes.prop('checked', false);
                }
            }
        });*/
        $("#btn_search").click(function(){
            productList.goToPage(1);
        });
        productList.goToPage(1);
    });

    var productList;
    productList = {
        limit:10,
        currentPage:-1,
        pros:[],
        order:'id',
        type:[
            {name:"包月",value:"1"},
            {name:"按次",value:"2"}
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
        status:[
            {name:"失效",value:"0"},
            {name:"有效",value:"1"},
            {name:"可订",value:"11"},
            {name:"可退",value:"10"},
            {name:"展示",value:"12"}
        ],
        costType:[
            {name:"人民币",value:"1"},
            {name:"M值",value:"2"}
        ],
        mobileProduct:[
            {name:"是",value:"1"},
            {name:"否",value:"2"}
        ],
        getStatus:function(status){
            return productList.getTextOfArray(status,productList.status,'value','name');
        },
        getType:function(type){
            return productList.getTextOfArray(type,productList.type,'value','name');
        },
        renderProducts:function (jsonData) {
            rebuild_page_nav($("#page-nav"), Math.ceil(jsonData['totalCount'] /productList.limit),
                    productList.currentPage,"productList.goToPage", jsonData['totalCount']);
            var products = jsonData['objs'];
            var result = '';

            if (products != null) {
                productList.pros = products;
                var i = 0, l = products.length;
                for (; i < l; i++) {
                    var product = products[i];
                    result +=
                            '<tr>' +
                                   /* '<td class="center">' +
                                    '<label><input type="checkbox" class="ace" name="rp-item-checkbox" value=' + product.id + '>' +
                                    '<span class="lbl"></span>' +
                                    '</label></td>' +*/
                                    '<td class="center">' + product.name + '</td>' +
                                    '<td class="center">' + product.payProductNo + '</td>' +
                                   '<td class="center">' + product.price + '</td>' +
                                    '<td class="center">' + productList.getType(product['type'])+'</td>' +
                                    '<td class="center">' + product.validLength + '</td>' +
                                    '<td class="center">' + productList.getStatus(product['status'])+'</td>' +
                                    '<td class="center"><a class="btn btn-grey btn-xs"  onclick="productList.showProductsDetail(' + product.id +
                                    ');return false;">' +
                                    '<i class="ace-icon fa fa-edit bigger-110 icon-only"></i>' +
                                    '<a class="btn btn-grey btn-xs" onclick="productList.deleteProduct('+product.id+
                                    ');return false;">'+
                                    '<i class="ace-icon fa fa-trash-o bigger-110 icon-only"></i>'+
                                    '</a></td>' +
                                    '</tr>';
                }
            }
            $('#productsContain').html(result);
        },
        orderBy:function (v) {
            if (v == productList.order) {
                productList.dir = ( productList.dir == "asc") ? "desc" : "asc";
            } else {
                productList.order = v;
                productList.dir = "desc";
            }
            productList.listProducts();
        },
        listProducts:function (toUrl) {
          //  $("#select_all").prop("checked",false);
            var _order = "o1.name";
            switch (productList.order) {
                case "name":
                    _order = "o1.name";
                    break;
                case "payProductNo":
                    _order = "o1.payProductNo";
                    break;
                case "price":
                    _order = "o1.price";
                    break;
                case "type":
                    _order = "o1.type";
                    break;
                case "validLength":
                    _order = "o1.validLength";
                    break;
                case "status":
                    _order = "o1.status";
                    break;
            }

            var parameters ={ "obj.name":$("#search_word").val(),"obj.status":$("#search_status").val(),"obj.payProductNo":$("#search_num").val(),
                    "pageBean.pageSize":productList.limit,"pageBean.pageNo":page_index, "pageBean.orderBy":_order,
                    "pageBean.orderDir":productList.dir,
                    limit:productList.limit, start:(productList.currentPage - 1) * productList.limit };
            if(typeof(toUrl)=='undefined'||toUrl==null){
                $.ajax({
                    url:'../product/product!list.action',
                    data:parameters,
                    method:'post',
                    dataType:'json',
                    success:function (jsonData) {
                        productList.renderProducts(jsonData);
                    }
                });
            }else{
                var i=toUrl.indexOf("?");
                if(i>0){
                    toUrl+="&";
                }else{
                    toUrl +="?";
                }
                for(var p in parameters){
                    if(parameters.hasOwnProperty(p)){
                        toUrl+=p+"="+encodeURI(encodeURI(parameters[p]))+"&";
                    }
                }
                window.location.href = toUrl;
            }
        },
        showProductsDetail:function (id) {
            productList.currentProductId = id;
            showDialog({id:'productDetailModal', renderTo:'productDetailLog', title:'Product基本信息',
                items:[
                   {fieldLabel:'名称', id:'name', allowBlank:false},
                    {fieldLabel:'信息层编号', id:'payProductNo'},
                    {fieldLabel:'价格', id:'price'},
                    {fieldLabel:'有效期', id:'validLength',renderer:'productList.displayLengthUnit'},
                            {html:
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">当前状态</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="status">'+
                            '<option value="0">失效</option><option value="1">有效</option><option value="11">可订</option><option value="10">可退</option><option value="12">展示</option>'+
                            '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">计时单元</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="lengthUnit">'+
                            '<option value="1">天</option><option value="2">小时</option>'+
                            '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">自动续订</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="autoPay">'+
                            '<option value="1">是</option><option value="2">否</option>'+
                           '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">查询信息层</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="searchExtra">'+
                            '<option value="1">是</option><option value="2">否</option>'+
                            '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">类型</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="type">'+
                            '<option value="1">天</option><option value="2">小时</option>'+
                            '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">付费类型</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="costType">'+
                            '<option value="1">人民币</option><option value="2">M值</option>'+
                            '</select></div></div>'+
                            '<div class="form-group">'+
                            '<label class="col-sm-3 control-label no-padding-right filed-need">手机产品</label>'+
                            '<div class="col-sm-7">'+
                            '<select class="form-control" id="mobileProduct">'+
                            '<option value="1">是</option><option value="2">否</option>'+
                            '</select></div></div>'
                    },
                    {fieldLabel:'产品描述', id:'description',type:'textarea'}
            ],
                hiddenItems:[{id:'id'}],
                buttons:[
                    {text:'确定', cls:'btn-blue', handler:productList.saveProduct},
                    {text:'关闭', style:'margin-left:100px;', handler:function () {
                        $("#productDetailModal").modal('hide');
                    }}
                ]
            });
            if (id > 0) {
                $.ajax({
                    url:'/product/product!view.action?keyId='+id,
                    dataType:'json',
                    success:function (jsonData) {
                        var data = jsonData['data'];
                        if (data != null) {
                            $("#name").val(data['obj.name']);
                            $("#payProductNo").val(data['obj.payProductNo']);
                            $("#price").val(data['obj.price']);
                            productList.createSelect('type',productList.type,data['obj.type']);
                            $("#validLength").val(data['obj.validLength']);
                            productList.createSelect('lengthUnit',productList.lengthUnit,data['obj.lengthUnit']);
                            productList.createSelect('autoPay',productList.autoPay, data['obj.autoPay']);
                            productList.createSelect('searchExtra', productList.searchExtra, data['obj.searchExtra']);
                            productList.createSelect('status', productList.status, data['obj.status']);
                            productList.createSelect('costType', productList.costType, data['obj.costType']);
                            productList.createSelect('mobileProduct', productList.mobileProduct, data['obj.mobileProduct']);
                            $("#description").val(data['obj.description']);
                        }
                    }
                });
            }
        },
        deleteProduct:function (id) {
            if (confirm("您确认要删除该记录吗？")) {
                $.ajax({
                    url:"/product/product!delete.action",
                    data:{keyId:id},
                    dataType:'json',
                    success:function (jsonData) {
                        alert("删除完毕！");
                        productList.goToPage(1);
                    }
                });
            }
        },
        saveProduct:function () {
            var id = productList.currentProductId;
            if (id == null || typeof(id) == 'undefined' || id == '-1' || id == '0' || id == 0) {
                id = -1;
            }
            if (id != -1 && !confirm("您确认您要保存吗？\r\n")) {
                return;
            }
            var name = $("#name").val();
            var payProductNo = $("#payProductNo").val();
            var price = $("#price").val();
            var type = $("#type").val();
            var validLength = $("#validLength").val();
            var lengthUnit = $("#lengthUnit").val();
            var autoPay = $("#autoPay").val();
            var searchExtra = $("#searchExtra").val();
            var status = $("#status").val();
            var costType = $("#costType").val();
            var mobileProduct = $("#mobileProduct").val();
            var description = $("#description").val();
            $.ajax({
                url:'../product/product!save.action',
                method:'post',
                data:{
                    'obj.id':id,
                    'obj.name':name,
                    'obj.payProductNo':payProductNo,
                    'obj.price':price,
                    'obj.type':type,
                    'obj.validLength':validLength,
                    'obj.lengthUnit':lengthUnit,
                    'obj.autoPay':autoPay,
                    'obj.searchExtra':searchExtra,
                    'obj.status':status,
                    'obj.costType':costType,
                    'obj.mobileProduct':mobileProduct,
                    'obj.description':description
                },
                dataType:'text',
                success:function () {
                    alert("已保存！");
                    $("#productDetailModal").modal("hide");
                    productList.goToPage(1);
                }
            }).fail(function () {
                        alert("失败！");
                    });
        },
        goToPage:function (pageNo) {
            productList.currentPage = pageNo;
            productList.listProducts();
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