<%@ taglib prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
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
          width:650px;
          height:30px;
      }
      .repairInfo{
          height:30px;
          width:250px;
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
        <li class="active">${folderName}</li><li class="active">${functionName}</li>
      </ul>
      <!-- /.breadcrumb -->


      <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-car"></i>${functionName}
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
                          <td align="right" width="100"><label for="fileNo">档案编号：</label></td>
                          <td><input type="text" class="repairInfo" id="fileNo" name="obj.fileNo"></td>
                      </tr>
                      <tr>
                          <td align="right"><label for="carNo">车牌照：</label></td>
                          <td><input type="text" class="repairInfo"  id="carNo" name="obj.carNo"></td>
                      </tr>
                      <tr>
                          <td align="right"><label for="modifyDate">编辑日期：</label></td>
                          <td><input type="text" class="repairInfo"  id="modifyDate" name="obj.modifyDate"></td>
                      </tr>
                      <tr>
                          <td colspan="2">
                              <table>
                                  <tr>
                                      <td colspan="2">养护项目：</td>
                                  </tr>
                                  <tbody id="itemsBody">
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
                                      <td align="right" id="totalPrice">￥0.00</td>
                                      <td align="right" id="totalManHour">￥0.00</td>
                                  </tr>
                              </table>
                              <br/>
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="in_time">入店时间：</label>
                          </td>
                          <td>
                              <input id="in_time" name="obj.inTime" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="out_time">预计交车时间：</label>
                          </td>
                          <td>
                              <input id="out_time" name="obj.outTime" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="out_time">施工班组：</label>
                          </td>
                          <td>
                              <input id="workers" name="obj.workers" class="repairInfo">
                          </td>
                      </tr>
                      <tr>
                          <td align="right">
                              <label for="out_time">质检员：</label>
                          </td>
                          <td>
                              <input id="qc" name="obj.qc" class="repairInfo">
                          </td>
                      </tr>
                  </table>
              </div>
                <div class="space-6"></div>
                <div class="row">
                    <div class="col-md-2"><a class="btn btn-green btn-big" href="#" onclick="saveRepair()">保存</a></div>
                    <div class="col-md-2"><a class="btn btn-gray btn-big" href="#" onclick="resetRepair()">重置</a></div>
                </div>
            </div>
              <input type="hidden" name="obj.id" id="id">
              <input type="hidden" name="obj.createTime" id="createTime">
              <input type="hidden" name="obj.modifyTime" id="modifyTime">
              <input type="hidden" name="obj.status" id="status">

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
                            window.location.href="repairList.jsp";
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
        displayParts(obj['parts']);
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
    function calculatePrice(){

    }
    function calculateManHourPrice(){

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
    ];
    function displayItems(displayCount,obj){
        var result = '';
        var itemCount = 12;
        for(var i=0;i<itemCount;i++){
            var type='text';
            var cls = '';
            if(i>displayCount){
                type = 'hidden';
                cls = 'style="display:none;"';
            }
            result+='<tr id="itemRow_'+i+'"' +cls+
                    '>\n<td align="right" width="80">'+(i+1)+'、</td><td>'+
                            '<input type="' +type+'" id="obj_item' +i+'" name="obj.item' +i+'"' +
                    ' value="' +obj['obj.item'+i]+
                    '" class="repairItem">'
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
                var fieldValue = part[fieldName];
                if(fieldValue==null||typeof(fieldValue)=='undefined'){
                    fieldValue = '';
                }
                if(fieldType!='hidden'){
                    htmlResult+="<td>";
                }
                htmlResult+='<input id="parts_'+i+'_'+fieldName+'" name="parts['+i+'].'
                        +fieldName+'" type="' +fieldType+'" value="'+fieldValue+'">';
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
        var parts = __system_obj_data['parts'];
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
                part[fieldName]=$('#parts_'+i+'_'+fieldName).val();
            }
        }
        __system_obj_data['parts'] = parts;
    }
    function deleteParts(){
        if(confirm("您确定要删除选中的数据记录吗？")){
            var parts = __system_obj_data['parts'];
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
        var parts = __system_obj_data['parts'];
        if(parts==null){
            parts = [];
        }
        collectParts();
        parts.push({name:'',id:'-1',homeland:'',level:'',price:'0.00',manHour:'0.00',priceDiscount:0,
            manHourDiscount:0});
        __system_obj_data['parts']=parts;
        displayParts(parts);
    }
    loadData();
  </script>

</body>
</html>
