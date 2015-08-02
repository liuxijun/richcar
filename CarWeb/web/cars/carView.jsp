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
  <title>管理首页-车辆管理</title>
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

          <form role="form" class="form-horizontal">
            <div class="col-xs-12 no-padding car-info">
              <div class="tabbable" id="viewMainBody">
                <div class="space-6"></div>
                <div class="row">
                  <div class="col-md-2"><a class="btn btn-green btn-big" onclick="list.showDetail(-1)">新增条目</a></div>
                </div>
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
        tryRender();
    });
    var systemIsReady=false;
    var dictIsReady = false;
    var viewReadOnly = false;
    dictUtils.init(dictReady);

    var viewCar={
      items:[
        {name:'obj.id',type:'hidden',value:'-1'},
        {fieldLabel:'创建时间',type:'hidden',name:'obj.createTime',readOnly:viewReadOnly},
        {fieldLabel:'创建者',name:'obj.creator',type:'hidden',readOnly:viewReadOnly},
        {fieldLabel:'档案编号',name:'obj.sn',readOnly:viewReadOnly},
        {fieldLabel:'车牌号码',name:'obj.carNo',readOnly:viewReadOnly},
        {fieldLabel:'客户名称',name:'obj.userId',readOnly:viewReadOnly},
        {fieldLabel:'联系方式',name:'obj.tel',readOnly:viewReadOnly},
        {fieldLabel:'车辆品牌',name:'obj.product',readOnly:viewReadOnly,type:'select',onchange:productChanged},
        {fieldLabel:'车辆型号',name:'obj.productType',readOnly:viewReadOnly,type:'select',parentStoreCode:'product'},
        {fieldLabel:'产    地',name:'obj.productHometown',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'销售单位',name:'obj.salesCompany',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'VIN码',name:'obj.vinCode',readOnly:viewReadOnly},
        {fieldLabel:'发动机号',name:'obj.engineCode',readOnly:viewReadOnly},
        {fieldLabel:'发动机型号',name:'obj.enineType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'变速箱类型',name:'obj.gearbox',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'长宽高',name:'obj.lengthWidthHeight',readOnly:viewReadOnly},
        {fieldLabel:'车身颜色',name:'obj.carColor',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'内饰颜色',name:'obj.innerColor',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'燃油种类',name:'obj.gasType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'排放标准',name:'obj.emissionType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'排    量',name:'obj.emission',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'轮胎类型',name:'obj.tyreType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'车辆用途',name:'obj.carType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'行驶里程',name:'obj.mileage',readOnly:viewReadOnly},
        {fieldLabel:'保养次数',name:'obj.maintainTimes',readOnly:viewReadOnly},
        {fieldLabel:'车检起始',name:'obj.motStime',readOnly:viewReadOnly,type:'date'},
        {fieldLabel:'车检截至',name:'obj.motEtime',readOnly:viewReadOnly,type:'date'},
        {fieldLabel:'出厂日期',name:'obj.productionDate',readOnly:viewReadOnly,type:'date'},
        {fieldLabel:'保险起始',name:'obj.insureStime',readOnly:viewReadOnly,type:'date'},
        {fieldLabel:'保险截至',name:'obj.insureEtime',readOnly:viewReadOnly,type:'date'},
        {fieldLabel:'险种',name:'obj.insureType',readOnly:viewReadOnly,type:'select'},
        {fieldLabel:'保险公司',name:'obj.insureCompany',readOnly:viewReadOnly,type:'select'},
        {type:'newLine'},
        {fieldLabel:'顶部照片',name:'obj.carPictureTop',readOnly:viewReadOnly,type:'image',value:'/upload/car/top.jpg'},
        {fieldLabel:'左侧照片',name:'obj.carPictureLeft',readOnly:viewReadOnly,type:'image',value:'/upload/car/left.jpg'},
        {fieldLabel:'前面照片',name:'obj.carPictureFront',readOnly:viewReadOnly,type:'image',value:'/upload/car/front.jpg'},
        {fieldLabel:'底部照片',name:'obj.carPictureBottom',readOnly:viewReadOnly,type:'image',value:'/upload/car/bottom.jpg'},
        {fieldLabel:'右侧照片',name:'obj.carPictureRight',readOnly:viewReadOnly,type:'image',value:'/upload/car/right.jpg'},
        {fieldLabel:'后面照片',name:'obj.carPictureBack',readOnly:viewReadOnly,type:'image',value:'/upload/car/back.jpg'}
      ]
    };
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
    function loadData(){
        var keyId = parseInt($.getQuery("keyId",-1));
        if(keyId>0){
            $.ajax({
                url:'car!view.action?keyId='+keyId,
                dataType:'json',
                success:function(jsonData){
                    var obj = jsonData['data'];
                    if(obj!=null){
                        for(var p in obj){
                            if(obj.hasOwnProperty(p)){
                                var v = obj[p];
                                if(v==null||v=='null'||typeof(v)=='undefined'){
                                    v = '';
                                }
                                var pId = p.replace(/\./g,'_');
                                $("#"+pId).val(v);
                                if(p.indexOf("carPicture")==0&&v!=''){
                                    $("#previewPic_"+pId).attr("src",v);
                                }

                            }
                        }
                    }
                }
            });
        }
    }
    function dictReady(){
        dictIsReady = true;
        tryRender();
    }
      function tryRender(){
          if(systemIsReady&&dictIsReady){
              var fortuneCarViewer = new FortuneView(viewCar);
              fortuneCarViewer.renderTo('viewMainBody');
          }
      }
  </script>

</body>
</html>
