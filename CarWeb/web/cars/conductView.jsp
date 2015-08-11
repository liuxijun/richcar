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
  <title>管理首页-车辆管理-检查报表</title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp" %>
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

          <form role="form" class="form-horizontal" action="car!save.action"
                method="POST" enctype="multipart/form-data" id="carDetail" name="carDetail">
            <div class="col-xs-12 no-padding car-info">
              <div class="tabbable" style="float:left;" id="viewMainBody">
              </div>
            </div>
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
        systemIsReady = true;
        //tryRender();
    });
    var systemIsReady=false;
    var dictIsReady = false;
    var viewReadOnly = false;
    dictUtils.init(dictReady);
    var conductViewer={
        items:[],
        init:function(conductId,carId){
            $.ajax({
                url:'../conduct/conductValue!listItems.action?obj.conductId='+conductId+"&carId="+carId,
                dataType:'json',
                success:function(data){
                    conductViewer.render(data);
                }
            });
        },
        render:function(data){
            var items = data['items'];
            conductViewer.items = items;
            var obj = $("#viewMainBody");
            obj.html('');
            for(var i= 0,l=items.length;i<l;i++){
                var item = items[i];
                getColCount(item);
                getRowCount(item);
                obj.append('<div style="width:90%">'+getConductItemStr(item,0)+'</div>');
            }
        }
    };
    $.ajax({
       url:'../conduct/conduct!viewItems.action',
        data:{keyId:keyId,carId:carId},
        dataType:'json',
        success:function(data){
            conductViewer.render(data['obj.items']);
        }
    });
    conductViewer.render({items:[
        {
            id:1,
            name:'油液检测',
            items:[
                {
                    id:2,
                    name:'发动机机油',
                    items:[
                        {id:3,name:'密度',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                        ,{id:4,name:'粘度指数',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                        ,{id:5,name:'闪点',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                    ]
                },
                {
                    id:6,
                    name:'制动油',
                    items:[
                        {id:7,name:'平衡回流沸点',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                        ,{id:8,name:'湿平衡回流沸点',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                        ,{id:9,name:'-40℃运动黏度',standValueDesp:'标准值',currentValueDesp:'当前检测值',errorRangeDesp:'误差范围'}
                    ]
                }
            ]
        }
    ]});
    function ConductItem(item){
        this.name = item['name'];
        this.id = item['id'];
        this.level = item['level'];
        this.parentId=item['parentId'];
        this.standValue=item['standValue'];
        this.errorRange = item['errorRange'];
        this.currentValue = item['currentValue'];
        this.standValueDesp = item['standValueDesp'];
        this.errorRangeDesp = item['errorRangeDesp'];
        this.type = item['type'];
        this.colCount = item['colCount'];
        this.rowCount = item['rowCount'];
        if(this.colCount==null||typeof(this.colCount)=='undefined'){
            this.colCount = getColCount(item);
        }
        if(this.rowCount==null||typeof(this.rowCount)=='undefined'){
            this.rowCount = getRowCount(item);
        }
        if(this.level==null||typeof(this.level)=='undefined'){
            this.level = getItemLevel(item);
        }
        this.items=[];
        var subItems = item['items'];
        if(subItems!=null){
            for(var i= 0,l=subItems.length;i<l;i++){
                this.items.push(new ConductItem(subItems[i]));
            }
        }
        return this;
    }
    function getParameter(item,name,defaultVal){
        var result = item[name];
        if(result==null||result==''||typeof(result)=='undefined'){
            return defaultVal;
        }
        return result;
    }
    function getConductItemStr(item,level){
        var items = item['items'];
        var allColCount = 4;
        var allWidth=900;
        var lineHeight = 30;
        var itemWidth = 200;
        if(items==null||typeof(items)=='undefined'||items.length==0){
            var childWidth = '100px;';
            return '<div style="outline:1px solid gray;text-align:center;height:100%;width:' +itemWidth+'px;float:left;'+
                    '"><div style="width:'+(itemWidth)+'px;float:left;margin-top:5px;">' +item['name']+'</div>' +
                    '<div style="width:'+itemWidth+'px;float:left;">' +item['standValueDesp']+':' +
                    getParameter(item,'standValue','')+'，' +item['errorRangeDesp']+'：' +
                    getParameter(item,'errorRange','')+
                    '</div>'+
                    '<div style="width:'+itemWidth+'px;float:left;">' +item['currentValueDesp']+'' +
                    '<input style="width:'+childWidth+'"></div>'+
                    '</div>';
        }else{
            var rowCount = item['rowCount'];
            var colCount = item['colCount'];
            var height= rowCount*30;
            var width = 60;
            var boxWidth=allWidth-level*width;
            if(colCount==2){//数据显示前一列
                width = (allColCount-level)*50;
            }
            var result = '<div class="box" style="width:'+boxWidth+'px;float:left;height:'+height+'px;"><div style="text-align:center;line-height:'+height+'px;width:' +
                    width+'px;outline:solid 1px gray;float:left;">'+item['name']+'</div>';
            for(var i= 0,l=items.length;i<l;i++){
                result+=getConductItemStr(items[i],level+1);
            }
            result+='</div>';
            return result;
        }
    }
    //级别，根节点是0，下一级是1
    function getItemLevel(item,parentLevel){
        var level = item['level'];
        if(level==null||typeof(level)=='undefined'){
            var items = item['items'];
            level = parentLevel+1;
            if(items==null||typeof(items)=='undefined'||items.length==0){

            }else{
                for(var i= 0,l=items.length;i<l;i++){
                    var tempLevel = getItemLevel(items[i],parentLevel+1);
                }
            }
            item['level'] = level;
        }
        return level;
    }
    function getColCount(item){
        var colCount = item['colCount'];
        if(colCount==null||typeof(colCount)=='undefined'){
            var items = item['items'];
            if(items==null||typeof(items)=='undefined'||items.length==0){
                colCount = 1;
            }else{
                var childColCount = 1;
                for(var i= 0,l=items.length;i<l;i++){
                    var tempColCount = getColCount(items[i]);
                    if(tempColCount>childColCount){
                        childColCount = tempColCount;
                    }
                }
                colCount=childColCount+1;
            }
            item['colCount']=colCount;
        }
        return colCount;
    }
    function getRowCount(item){
        var rowCount = item['rowCount'];
        if(rowCount==null||typeof(rowCount)=='undefined'){
            var items = item['items'];
            if(items==null||typeof(items)=='undefined'||items.length==0){
                rowCount = 1;
            }else{
                rowCount = 0;
                for(var i= 0,l=items.length;i<l;i++){
                    rowCount+=getRowCount(items[i]);
                }
            }
            item['rowCount']=rowCount;
        }
        return rowCount;
    }
    function saveCar(){
        if(confirm("您确认要保存当前录入的车辆信息吗？")){
            var logs =fortuneCarViewer.checkForm(fortuneCarViewer.items);
            if(logs==''){
                $("#carDetail").submit();
            }else{
                alert(logs+'\r\n请详细检查表单中标红的区域！谢谢！');
            }
        }
    }
    function cancelCar(){
        if(confirm("您确认~不~保存当前编辑的车辆信息吗？数据珍贵，谨慎操作！")){

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
    var __system_obj_data=null;
    var __system_form_will_fill=false;
    var __system_obj_filled=false;
    function fillForm(obj){
        if(!__system_render_finished){
            return;
        }
        if(obj!=null){
            for(var p in obj){
                if(obj.hasOwnProperty(p)){
                    var v = obj[p];
                    if(v==null||v=='null'||typeof(v)=='undefined'){
                        v = '';
                    }
                    var pId = p.replace(/\./g,'_');
                    try {
                        if(p.indexOf("obj.carPicture")==0&&v!=''){
                            var picId = 'previewImage_'+pId;
                            var picEle = document.getElementById(picId);
                            if(picEle!=null){
                                picEle.src = v;
                            }else{
                                alert('未发现图片元素：'+pId);
                            }
                            $("#fileOrgValue_"+pId).val(v);
                        }else{
                            var ele = document.getElementById(pId);
                            if(ele!=null){
                                $("#"+pId).val(v);
                            }
                        }
                    } catch (e) {
                        alert("无法设置属性："+pId+","+ e.description);
                    }

                }
            }
        }
        __system_obj_filled = true;
    }
    function loadData(){
        var keyId = parseInt($.getQuery("keyId",-1));
        if(keyId>0){
            __system_form_will_fill=true;
            $.ajax({
                url:'car!view.action?keyId='+keyId,
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
    var fortuneCarViewer = new FortuneView(viewCar);
    var __system_render_finished=false;
    function tryRender(){
          if(systemIsReady&&dictIsReady){
              fortuneCarViewer.renderTo('viewMainBody');
              __system_render_finished = true;
              if(__system_form_will_fill){
                  fillForm(__system_obj_data);
              }
          }
    }
    loadData();
  </script>

</body>
</html>
