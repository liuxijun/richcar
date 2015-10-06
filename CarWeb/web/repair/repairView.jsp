<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    int type = StringUtils.string2int(request.getParameter("type"), 1);
    String carNo = request.getParameter("carNo");
    if(carNo==null){
        carNo = "";
    }
    String itemName = "";
    switch(type){
        case 1:
            itemName = "养护方案";
            break;
        case 2:
            itemName = "维修方案";
            break;
        case 3:
            itemName = "事故维修方案";
            break;
        default:
            itemName = "养护方案";
            break;
    }
    Date now = new Date();
    String defaultFileId = StringUtils.date2string(now, "yyyyMMddHHmmss");
    String nowStr = StringUtils.date2string(now);
    request.setAttribute("itemName",itemName);
%><!DOCTYPE html>
<html lang="zh_CN">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>管理首页-车辆管理</title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp" %>

    <link rel="stylesheet" href="../style/bootstrap-datetimepicker.min.css"/>
  <style type="text/css">
    .pictureDiv{
      width:280px;
      height:200px;
    }
    .hidden{
      display: none;
    }
    .showing{
      min-height:160px;
      display:block;
    }
    .previewImage{
      width:280px;
      height:200px;
    }

  </style>
  <script src="../js/dict.js"></script>
  <style type="text/css">
    .hover{
      cursor:pointer;
      background: white;
      color:#000000;
    }
    .asc{
      background: url('/images/sort_asc.png') no-repeat 50px;

    }
    .desc{
      background: url('/images/sort_desc.png') no-repeat 50px;
    }
      .partsName{
          width:100%;
      }
      .partsInfo{
          width:100%;
      }
      .repairItem{
          width:300px;
          height:30px;
      }
      .repairInfo{
          height:30px;
          width:300px;
      }
  </style>
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
        <li class="active">${folderName}</li><li class="active">${itemName}</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-car"></i>${itemName}
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">
      <div class="page-content-area">
        <div class="row page-content-main">

          <form role="form" class="form-horizontal" action="repair!save.action"
                method="POST" enctype="multipart/form-data" id="repairDetail" name="repairDetailForm">
            <div class="col-xs-12 no-padding repair-info">
              <div class="tabbable" id="viewMainBody">
                  <table  cellspacing="0">
                      <tr>
                          <td align="right" width="100"><label for="obj_fileId">档案编号：</label></td>
                          <td><input type="text" class="repairInfo" id="obj_fileId" name="obj.fileId"></td>
                      </tr>
                      <tr>
                          <td align="right"><label for="obj_carNo">车牌照：</label></td>
                          <td><input type="text" class="repairInfo"  id="obj_carNo" name="obj.carNo"></td>
                      </tr>
<%--
                      <tr>
                          <td align="right"><label for="obj_modifyDate">编辑日期：</label></td>
                          <td><input type="text" class="repairInfo"  id="obj_modifyDate" name="obj.modifyDate"></td>
                      </tr>
--%>
                      <tr>
                          <td colspan="2">
                              <table>
                                  <tbody id="itemsBody">
                                  <tr>
                                      <td colspan="2">养护项目：</td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_0">1、</label><input id="item_0" name="obj.item_0" class="repairItem">
                                      </td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_1">2、</label><input id="item_1" name="obj.item_1" class="repairItem">
                                      </td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_2">3、</label><input id="item_2" name="obj.item_2" class="repairItem">
                                      </td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_3">4、</label><input id="item_3" name="obj.item_3" class="repairItem">
                                      </td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_4">5、</label><input id="item_4" name="obj.item_4" class="repairItem">
                                      </td>
                                  </tr>
                                  <tr>
                                      <td colspan="2">
                                          <label for="item_5">6、</label><input id="item_5" name="obj.item_5" class="repairItem">
                                      </td>
                                  </tr>
                                  </tbody>
                              </table>
                          </td>
                      </tr>
                      <tr>
                          <td colspan="2">
                          <br/>
                              <table cellspacing="0" border="1" width="90%">
                                  <tr>
                                      <td width="5%"><input type="checkbox" id="selectAll"></td>
                                      <td width="30%" align="center">材料名称</td>
                                      <td width="30%" align="center">产地</td>
                                      <td width="20%" align="center">质量标准</td>
                                      <td width="5%" align="center">价格</td>
                                      <td width="5%" align="center">工时费</td>
                                  </tr>
                                  <tbody id="partsBody">
<%--
                                  <tr>
                                      <td>
                                          <input type="text" id="parts_0_name" name="parts[0].name" class="partsName">
                                      </td>
                                      <td>
                                          <input type="text" id="parts_0_homeland" name="parts[0].homeland" class="partsInfo">
                                      </td>
                                      <td>
                                          <input type="text" id="parts_0_level" name="parts[0].level" class="partsInfo">
                                      </td>
                                      <td align="right">
                                          <input type="text" id="parts_0_price" name="parts[0].price" class="partsInfo"
                                                  onchange="calculatePrice()">
                                      </td>
                                      <td align="right">
                                          <input type="text" id="parts_0_manhour" name="parts[0].manHour" class="partsInfo"
                                                 onchange="calculateManHourPrice()">
                                      </td>
                                  </tr>
--%>
                                  </tbody>
                                  <tr>
                                      <td colspan="3">
                                          <div  style="cursor: pointer;color:blue;float:left;" onclick="newParts()">添加配件</div>
                                          <div  style="margin-left:80px;cursor: pointer;color:blue;float:left;" onclick="deleteParts()">删除选中的配件</div>&nbsp;&nbsp;
                                      </td>
                                      <td align="right">总计</td>
                                      <td align="right" id="priceTotal">￥0.00</td>
                                      <td align="right" id="manHourTotal">￥0.00</td>
                                  </tr>
                              </table>
                              <br/>
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="obj_inTime">入店时间：</label>
                          </td>
                          <td>
                              <input id="obj_inTime" name="obj.inTime" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="obj_outTime">预计交车时间：</label>
                          </td>
                          <td>
                              <input id="obj_outTime" name="obj.outTime" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="obj_inTime">当前状态：</label>
                          </td>
                          <td>
                              <select class="repairInfo" id="obj_status" name="obj.status">
                                  <option value="1">排队中</option>
                                  <option value="2">进行中</option>
                                  <option value="3">已经完成</option>
                                  <option value="4">其他</option>
                              </select>
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="obj_workers">施工班组：</label>
                          </td>
                          <td>
                              <input id="obj_workers" name="obj.workers" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="obj_qc">质检员：</label>
                          </td>
                          <td>
                              <input id="obj_qc" name="obj.qc" class="repairInfo">
                          </td>
                      </tr>
                  </table>
              </div>
                <div class="space-6"></div>
                <div class="row">
                    <div class="col-md-2"><a class="btn btn-green btn-big" onclick="saveRepair()">保存</a></div>
                    <div class="col-md-2"><a class="btn btn-gray btn-big" onclick="resetRepair()">重置</a></div>
                </div>
            </div>
              <input type="hidden" name="obj.id" id="obj_id">
              <input type="hidden" name="obj.createTime" id="obj_createTime">
              <input type="hidden" name="obj.modifyTime" id="obj_modifyTime">
<%--
              <input type="hidden" name="obj.status" id="obj_status">
--%>
              <input type="hidden" name="obj.type" id="obj_type">

              <!-- /.row -->
          </form>
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
  <div id="modalDialog">
  </div>
  <!-- inline scripts related to this page -->
    <script type="text/javascript" src="../scripts/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script src="../scripts/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">
    var page_index = 1;
    var page_size = 10;
    var itemName;
    var displayFault = false;
    function setByType(type){
        if(type==null||typeof(type)=='undefined'){
            type = <%=type%>;
        }
        switch(type){
            case 2:
                itemName = "维修项目";
                break;
            case 3:
                itemName = "维修项目";
                break;
            default:
                itemName = "养护项目";
                break;
        }
        displayFault = type==2;
    }

    jQuery(function ($) {
      $('.scrollable').each(function () {
        var $this = $(this);
        $(this).ace_scroll({
          size: $this.data('size') || 100
          //styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
        });
      });
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

      $("#open-movie-title-boxs").click(function () {
        var $a = $(".movie-title-boxs")
        $this = $(this);
        if ($a.is(':visible')) {
          $a.slideUp(300, function () {
            $this.html("查看更多文件");
          });
        } else {
          $a.slideDown(300, function () {
            $this.html("收起");
          });


        }
      });

      $('#id-input-file-1,#id-input-file-2').ace_file_input({
        no_file: '未选择文件 ...',
        btn_choose: '选择',
        btn_change: '选择',
        droppable: false,
        onchange: null,
        thumbnail: false //| true | large
        //whitelist:'gif|png|jpg|jpeg'
        //blacklist:'exe|php'
        //onchange:''
        //
      });


      var DataSourceTree = function (options) {
        this._data = options.data;
        this._delay = options.delay;
      };

      DataSourceTree.prototype.data = function (options, callback) {
        var self = this;
        var $data = null;

        if (!("name" in options) && !("type" in options)) {
          $data = this._data;//the root tree
          callback({ data: $data });
          return;
        }
        else if ("type" in options && options.type == "folder") {
          if ("additionalParameters" in options && "children" in options.additionalParameters)
            $data = options.additionalParameters.children;
          else $data = {};//no data
        }

        if ($data != null)//this setTimeout is only for mimicking some random delay
          setTimeout(function () {
            callback({ data: $data });
          }, parseInt(Math.random() * 500) + 200);

        //we have used static data here
        //but you can retrieve your data dynamically from a server using ajax call
        //checkout examples/treeview.html and examples/treeview.js for more info
      };

      $(".btn-dropdown").click(function () {
        $("#tree1").slideDown();
      });
      $("#tree1").mouseleave(function () {
        $(this).slideUp();

      });
      //Android's default browser somehow is confused when tapping on label which will lead to dragging the task
      //so disable dragging when clicking on label
      var agent = navigator.userAgent.toLowerCase();
      if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
        $('#tasks').on('touchstart', function (e) {
          var li = $(e.target).closest('#tasks li');
          if (li.length == 0)return;
          var label = li.find('label.inline').get(0);
          if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
        });
//    list.initFolders();
        $("#selectAll").click(function(){
            if(this.checked){
                $("#partsBody :checkbox").attr("checked", true);
            }else{
                $("#partsBody :checkbox").attr("checked", false);
            }
        });
        systemIsReady = true;
        tryRender();
    });
    var systemIsReady=false;
    var dictIsReady = false;
    var viewReadOnly = false;
    dictUtils.init(dictReady);
    function checkForm(){
        var result = "";
        if($("#obj_fileId").val()==''){
            result+="\n档案号不能为空！";
        }
        if($("#obj_carNo").val()==''){
            result+="\n车牌号不能为空！";
        }
        if($("#obj_workers").val()==''){
            result+="\n施工班组不能为空！";
        }

        return result;
    }
    function saveRepair(){
        if(confirm("您确认要保存当前录入的信息吗？")){
            var logs =checkForm();
            if(logs==''){
                var data = $("#repairDetail").serialize();
                $.ajax({
                    data:data,
                    method:'post',
                    dataType:'json',
                    url:"repair!save.action",//默认是form action
                    success:function(data){
                        if(data['success']){
                            alert('保存成功！');
                            window.location.href="repairList.jsp?type="+$("#obj_type").val();
                        }
                    }
                });
            }else{
                alert(logs+'\r\n请详细检查表单中有问题区域！谢谢！');
            }
        }
    }
    function resetRepair(){
        if(confirm("您确认~不~保存当前编辑的信息吗？数据珍贵，谨慎操作！")){
            loadData();
        }
    }
    function productChanged(){
      var code = $("#obj_product").val();
      var productType = $("#obj_productType");
      var productTypeVal = productType.val();
      productType.empty();
      var items = dictUtils.getDict(code);
      if(items!=null){
        productType.append(appendOptions(items,productTypeVal));
      }
    }
    var __system_obj_data={};
    var __system_form_will_fill=false;
    var __system_obj_filled=false;

    function fillForm(obj){
        if(obj==null){
            obj = {};
        }
        setByType(obj['obj.type']);
        __system_obj_data = obj;
        for(var i= 0,l=repairFields.length;i<l;i++){
            var field = repairFields[i];
            var fieldId = field['name'];
            var fieldType = field['type'];
            var id = 'obj_'+fieldId;
            var val = obj['obj.'+fieldId];
            if(val==null||typeof(val)=='undefined'){
                val = '';
            }
            $('#'+id).val(val);
        }
        displayItems(obj,6,displayFault);
        displayParts(obj['obj.parts']);
    }
    function loadData(){
        var keyId = parseInt($.getQuery("keyId",-1));
        if(keyId>0){
            __system_form_will_fill=true;
            $.ajax({
                url:'repair!view.action?keyId='+keyId,
                dataType:'json',
                success:function(jsonData){
                    var obj = jsonData['data'];
                    __system_obj_data = obj;
                    fillForm(obj);
                }
            });
        }else{
            fillForm({'obj.fileId':'<%=defaultFileId%>','obj.createTime':'<%=nowStr%>',
                    'obj.inTime':'<%=nowStr%>',
                'obj.outTime':'<%=StringUtils.date2string(new Date(now.getTime()+2*3600*1000L))%>',
                'obj.carNo':'<%=carNo%>','obj.id':'-1','obj.type':<%=type%>,'obj.status':1,
                'obj.parts':[{name:'',repairId:-1,price:'0.00',manHour:'0.00'}]});
        }
    }

    function dictReady(){
        dictIsReady = true;
        tryRender();
    }

    var __system_render_finished=false;
    function tryRender(){
          if(systemIsReady&&dictIsReady){
              __system_render_finished = true;
              if(__system_form_will_fill){
                  fillForm(__system_obj_data);
              }
          }
    }
    function calculateField(name){
        collectParts();
        var parts = __system_obj_data['obj.parts'];
        if(parts==null){
            parts = [];
        }
        var result = 0;
        for(var i= 0,l=parts.length;i<l;i++){
            var p = parts[i];
            result += parseFloat(p[name]);
        }
        $("#"+name+"Total").html("￥"+result);
    }
    function calculatePrice(){
        calculateField('price');
    }
    function calculateManHourPrice(){
        calculateField('manHour');
    }
    var partsFields = [
        {name:'name',type:'text',allowBlank:false}
        ,{name:'homeland',type:'text',allowBlank:true}
        ,{name:'level',type:'text',allowBlank:true}
        ,{name:'price',type:'number',allowBlank:true}
        ,{name:'manHour',type:'number',allowBlank:true}
        ,{name:'id',type:'hidden',allowBlank:true}
        ,{name:'repairId',type:'hidden',allowBlank:true}
        ,{name:'priceDiscount',type:'hidden',allowBlank:true}
        ,{name:'manHourDiscount',type:'hidden',allowBlank:true}
        ,{name:'status',type:'hidden',allowBlank:true}
    ];
    var repairFields = [
        {name:'id',type:'hidden'}
        ,{name:'fileId'},{name:'createTime',type:'hidden'}
        ,{name:'modifyTime',type:'hidden'},{name:'carNo'}
        ,{name:'fault0'},{name:'fault1'},{name:'fault2'}
        ,{name:'fault3'},{name:'fault4'},{name:'fault5'}
        ,{name:'fault6'},{name:'fault7'},{name:'fault8'}
        ,{name:'fault9'},{name:'fault10'},{name:'fault11'}
        ,{name:'item0'},{name:'item1'},{name:'item2'}
        ,{name:'item3'},{name:'item4'},{name:'item5'}
        ,{name:'item6'},{name:'item7'},{name:'item8'}
        ,{name:'item9'},{name:'item10'},{name:'item11'}
        ,{name:'inTime',type:'datetime'},{name:'outTime',type:'datetime'}
        ,{name:'reception'},{name:'workers'},{name:'qc'},{name:'status',type:'hidden'}
            ,{name:'type',type:'hidden'}
    ];
    function getValue(val){
        if(val==null||typeof(val)=='undefined'){
            return '';
        }
        return val;
    }
    function displayItems(obj,displayCount,displayFault){
        var result = '<table><tr><td colspan="2" style="padding-left: 30px;font-size:14px;">';
        if(displayFault){
            result+='故障现象：<td><td colspan="2">维修项目：';
        }else{
            result+=itemName+'：';
        }
        result+='</td></tr>';
        var itemCount = 12;
        var numberWidth=100;
        for(var i=0;i<itemCount;i++){
            var type='text';
            var cls = '';
            if(i>=displayCount){
                type = 'hidden';
                cls = 'style="display:none;"';
            }
            result+='<tr id="itemRow_'+i+'"' +cls+
                    '>\n';
            if(displayFault){
                result+='<td align="right" width="'+numberWidth+'">'+(i+1)+'、</td><td>'+
                        '<input type="' +type+'" id="obj_fault' +i+'" name="obj.fault' +i+'"' +
                        ' value="' +getValue(obj['obj.fault'+i])+
                        '" class="repairItem">'+
                        '</td>';
            }else{
                result+='<input type="hidden" id="obj_fault' +i+'" name="obj.fault' +i+'"' +
                        ' value="' +getValue(obj['obj.fault'+i])+
                        '" class="repairItem">';
            }
            result+='<td align="right" width="'+numberWidth+'">'+(i+1)+'、</td><td>'+
                            '<input type="' +type+'" id="obj_item' +i+'" name="obj.item' +i+'"' +
                    ' value="' +getValue(obj['obj.item'+i])+
                    '" class="repairItem">'+
                    '</td>\n<tr>\n';
        }
        $("#itemsBody").html(result);
    }
    function displayParts(parts){
        if(parts==null){
            parts = [];
        }
        var htmlResult = '';
        for(var i= 0,l=parts.length;i<l;i++){
            var part = parts[i];
            if(part==null){
                part = {};
                parts[i]=part;
            }
            htmlResult +='<tr>\n<td><input type="checkbox" id="partsIdx_'+i+'" value="'+i+'"></td>';
            for(var j= 0,jl=partsFields.length;j<jl;j++){
                var field = partsFields[j];
                var fieldName = field['name'];
                var fieldType = field['type'];
                if(fieldType == 'number'){
                    fieldType = 'text';
                }
                var onChangeEvent = "";
                if(fieldName == 'price'||fieldName=='manHour'){
                    onChangeEvent = ' onchange="calculateField(\''+fieldName+'\')"';
                }
                var fieldValue = part[fieldName];
                if(fieldValue==null||typeof(fieldValue)=='undefined'){
                    fieldValue = '';
                }
                if(fieldType!='hidden'){
                    htmlResult+="<td>";
                }
                htmlResult+='<input id="obj_parts_'+i+'_'+fieldName+'" name="obj.parts['+i+'].'
                        +fieldName+'" type="' +fieldType+'" value="'+fieldValue+'"' +
                                onChangeEvent+
                        '>';
                if(fieldType!='hidden'){
                    htmlResult+="</td>";
                }
                htmlResult+="\n";
            }
            htmlResult += '</tr>';
        }
        htmlResult+='';
        $("#partsBody").html(htmlResult);
        calculateManHourPrice();
        calculatePrice();
    }
    function collectParts(){
        var parts = __system_obj_data['obj.parts'];
        if(parts==null){
            parts = [];
        }
        for(var i= 0,l=parts.length;i<l;i++){
            var part = parts[i];
            if(part==null){
                part = {};
                parts[i]=part;
            }
            for(var j= 0,jl=partsFields.length;j<jl;j++){
                var field = partsFields[j];
                var fieldType = field['type'];
                if(fieldType == 'number'){
                    fieldType = 'text';
                }
                var fieldName = field['name'];
                part[fieldName]=$('#obj_parts_'+i+'_'+fieldName).val();
            }
        }
        __system_obj_data['obj.parts'] = parts;
    }
    function deleteParts(){
        if(confirm("您确定要删除选中的数据记录吗？")){
            var parts = __system_obj_data['obj.parts'];
            if(parts==null){
                parts = [];
            }
            var logs = '';
            collectParts();
            for(var l=parts.length-1;l>=0;l--){
                var part = parts[l];
                var ckbox = document.getElementById("partsIdx_"+l);
                if(ckbox.checked){
                    logs+=','+part['name'];
                    parts.splice(l,1);
                }
            }
            if(logs==''){
                logs='未删除任何数据！';
            }else{
                logs = '已经删除如下'+logs;
            }
            displayParts(parts);
            alert(logs);
        }
    }
    function newParts(){
        var parts = __system_obj_data['obj.parts'];
        if(parts==null){
            parts = [];
        }
        collectParts();
        parts.push({name:'',id:'-1',homeland:'',level:'',price:'0.00',manHour:'0.00',priceDiscount:0,
            manHourDiscount:0});
        __system_obj_data['obj.parts']=parts;
        displayParts(parts);
    }
    loadData();
  </script>

</body>
</html>
