<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/11/26
  Time: 14:13
  个性化，通过个性化可以设置logo和名称
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>个性化 - <%=IndividualUtils.getInstance().getName()%></title>
  <meta name="description" content="overview &amp; stats"/>
  <%@include file="../inc/displayCssJsLib.jsp" %>
  <link rel="stylesheet" href="../style/vendor/jquery.Jcrop.css"/>
</head>
<body class="no-skin">

<%@include file="../inc/displayHeader.jsp" %>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
  <script type="text/javascript">
    try {
      ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
  </script>

  <!-- #section:basics/sidebar -->
  <%@include file="/inc/displayMenu.jsp" %>
<!-- /section:basics/sidebar -->
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
        <li>系统管理</li>
        <li class="active">个性化</li>
      </ul>
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
      <h1>
        <i class="ace-icon fa fa-gear"></i>系统个性化

      </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

      <div class="page-content-area">


        <div class="row page-content-main">
          <form role="form" class="form-horizontal">


            <div class="col-xs-12 no-padding"   >
              <hr>
              <div class="form-group">
                <label   class="col-sm-2 control-label no-padding-right filed-need">电脑和手机Logo</label>
                <div class="col-sm-10">
                  <div class="col-sm-6 no-padding"  >
                    <div id="preview-container" style="margin-bottom: 8px;background: #DDDADA"><span></span></div>
                    <div class="alert alert-block alert-info" style="padding:6px 20px 6px 16px">
                      <i class="ace-icon fa fa-image blue bigger-120"></i>
                      <span id="logo-meta">图片大小</span>
                    </div>
                    <!--<h6 class="tjsd">建议：200x60，透明底色png格式图片，要<a href="#" id="btn-crop-logo" style="text-decoration: underline">剪切logo按这里</a>。</h6>
                    //-->
                    <input type="file" id="file-logo" />
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label   class="col-sm-2 control-label no-padding-right filed-need">平板Logo</label>
                <div class="col-sm-10">
                  <div class="col-sm-6 no-padding"  >
                    <div id="preview-container-mobile" style="margin-bottom: 8px;background: #DDDADA"><span></span></div>
                    <div class="alert alert-block alert-info" style="padding:6px 20px 6px 16px;">
                      <i class="ace-icon fa fa-image blue bigger-120"></i>
                      <span id="logo-meta-mobile">图片大小</span>
                    </div>
                    <input type="file" id="file-logo-mobile" />
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label   class="col-sm-2 control-label no-padding-right filed-need">名称</label>
                <div class="col-sm-10">
                  <input type="text" class="col-sm-5" placeholder="不长于20字" id="input-name" >
                </div>
              </div>




              <div class="space"></div>
              <button class="btn btn-blue" type="button" id="btn-ok">确定 </button>


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
  <!-- basic scripts -->
  <div class="modal fade modal-class" id="editLogoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="position: absolute; top: 50%; left: 50%; margin-left: -300px; margin-top: -120px; width:600px;">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
          <h4 class="modal-title" id="editOrgModalLabel">剪切Logo</h4>
        </div>
        <div class="modal-body">
          <form class="form-horizontal">
            <div class="col-xs-12" style="margin: 0 0 20px 0">
              <div class="row" id="crop-container" style="max-width: 400px;max-height: 300px;background: #DDDADA">
                  <span>content here</span>
              </div>
              <div class="space"></div>
            </div>
            <button class="btn btn-blue btn-big2 margin0" type="button" id="saveCrop">
              确定
            </button>
          </form>
        </div>
        <div class="modal-footer" style="display: none;">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
    </div>
  </div>

  <!-- inline scripts related to this page -->
  <script src="../scripts/load-image.all.min.js"></script>
  <script src="../scripts/vendor/jquery.Jcrop.js"></script>
  <script type="text/javascript">
    jQuery(function ($) {

      $('#file-logo').ace_file_input({
        no_file:'未选择文件 ...',
        btn_choose:'选择',
        btn_change:'选择',
        droppable:false,
        onchange:null,
        thumbnail:true,
        whitelist:'png',
        //blacklist:'exe|php'
        before_change:function(files, dropped) {
          if (files.length > 0) {
            var file = files[0];

            if (typeof file === 'string') {
            } else if ('File' in window && file instanceof window.File) {
              individualUtils.previewImageFile(this);
            }
          }
          return true;
        }
      });

      $('#file-logo-mobile').ace_file_input({
        no_file:'未选择文件 ...',
        btn_choose:'选择',
        btn_change:'选择',
        droppable:false,
        onchange:null,
        thumbnail:true,
        whitelist:'png',
        //blacklist:'exe|php'
        before_change:function(files, dropped) {
          if (files.length > 0) {
            var file = files[0];

            if (typeof file === 'string') {
            } else if ('File' in window && file instanceof window.File) {
              individualUtils.previewMobileLogo(this);
            }
          }
          return true;
        }
      });

      $(document).ajaxStart(function(){
        $("#loading_container").show();
      });

      $(document).ajaxStop(function(){
        setTimeout(function(){$("#loading_container").hide();}, 200);
      });

      $("#btn-crop-logo").click(function(event){
        individualUtils.openCrop();
        //individualUtils.showCropBox(event);
      });
      $('#editLogoModal').on('shown.bs.modal', function() {
        // 显示crop框
        individualUtils.showCropBox();
      });

      $("#btn-edit").click(function(event){
        individualUtils.showCropBox(event);
      });

      $("#saveCrop").click(function(event){
        individualUtils.doCrop(event);
        $("#editLogoModal").modal("hide");
      });

      //$("#preview-container").css("width", individualUtils.previewMaxWidth);
      $("#btn-ok").click(function(){
        individualUtils.saveLogo();
      });

      individualUtils.getIndividual();
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



    });

    var individualUtils = {
      currentFile: null,
      maxWidth : 560, /*crop max width*/
      maxHeight : 300, /*corop max height*/
      previewMaxWidth : 99999,
      previewMaxHeight : 99999,  /*preview size*/
      recommendLogoWidth: 325,  /*推荐logo宽*/
      recommendLogoHeight: 55,
      recommendMobileLogoWidth: 234,  /*手机版logo宽*/
      recommendMobileLogoHeight: 39,
      logoURL : null,
      mobileLogoURL : null,
      coordinates : null,
      editPanel : $("#crop-container"),
      previewPanel : $("#preview-container"),
      mobileLogoPanel :$("#preview-container-mobile"),
      defaultLogo : "/page/redex/assets/images/logo.png",
      defaultMobileLogo:"/page/redex/assets/images/logo2.png",
      renderPanel : null,/*panel to show image*/
      logoWidth: 0,
      logoHeight: 0,
      //panel : $("#preview-container"),
      previewImageFile: function (input) {
        if (input.files && input.files[0]) {
          var file = input.files[0],
                  options = {
                    maxWidth: individualUtils.previewMaxWidth,
                    maxHeight: individualUtils.previewMaxHeight,
                    canvas: true
                  };
          individualUtils.displayImage(file, options, individualUtils.previewPanel);
        }
      },
      previewMobileLogo: function (input) {
        if (input.files && input.files[0]) {
          var file = input.files[0],
                  options = {
                    maxWidth: individualUtils.previewMaxWidth,
                    maxHeight: individualUtils.previewMaxHeight,
                    canvas: true
                  };
          individualUtils.displayImage(file, options, individualUtils.mobileLogoPanel);
        }
      },
      showImageMeta: function(){
        // 显示PC版Logo现在尺寸和格式
        var imgNode = individualUtils.previewPanel.find('img, canvas');
        if(imgNode) {
          var img = imgNode[0],
                  msg = "图片尺寸 " + img.width + " x " + img.height + ",  推荐大小 " +
                  individualUtils.recommendLogoWidth + " x " +
                  individualUtils.recommendLogoHeight;
          if( Math.abs(img.width - individualUtils.recommendLogoWidth)*100/individualUtils.recommendLogoWidth > 15 ||
                  Math.abs(img.height - individualUtils.recommendLogoHeight)*100/individualUtils.recommendLogoHeight > 15){
            msg += ", 建议调整尺寸";
            $("#logo-meta").parent().removeClass("alert-info").addClass("alert-warning");
          }else{
            msg += "，图片满足要求";
            $("#logo-meta").parent().removeClass("alert-warning").addClass("alert-info");
          }
          $("#logo-meta").html(msg);
        }
      },
      showMobileLogoMeta: function(){
        // 显示手机版现在尺寸和格式
        var imgNode = individualUtils.mobileLogoPanel.find('img, canvas');
        if(imgNode) {
          var img = imgNode[0],
                  msg = "图片尺寸 " + img.width + " x " + img.height + ",  推荐大小 " +
                          individualUtils.recommendMobileLogoWidth + " x " +
                  individualUtils.recommendMobileLogoHeight;
          if( Math.abs(img.width - individualUtils.recommendMobileLogoWidth)*100/individualUtils.recommendMobileLogoWidth > 15 ||
                  Math.abs(img.height - individualUtils.recommendMobileLogoHeight)*100/individualUtils.recommendMobileLogoHeight > 15){
            msg += ", 建议调整尺寸";
            $("#logo-meta-mobile").parent().removeClass("alert-info").addClass("alert-warning");
          }else{
            msg += "，图片满足要求";
            $("#logo-meta-mobile").parent().removeClass("alert-warning").addClass("alert-info");
          }

          $("#logo-meta-mobile").html(msg);
        }
      },

      replaceResults: function (img) {
        var content;
        if (!(img.src || img instanceof HTMLCanvasElement)) {
          content = $('<span>Loading image file failed</span>');
        } else {
          content = $('<span>').append(img).attr('href', img.src || img.toDataURL());
        }
        var panel = individualUtils.renderPanel;

        if(panel == null) panel = individualUtils.editPanel;

        panel.children().replaceWith(content);
        if (img.getContext) {
          //actionsNode.show();
        }
      },
      displayImage: function (file, options,panel) {
        individualUtils.currentFile = file;
        individualUtils.renderPanel = panel;

        var loadingImage = loadImage(
                file,
                individualUtils.replaceResults,
                options
        );
        if( !loadingImage ){
          panel.children().replaceWith(
                  $('<span>Your browser does not support the URL or FileReader API.</span>')
          );
        }


        if( panel == individualUtils.previewPanel ) {
          setTimeout("individualUtils.showImageMeta()", 500);
        }else if(panel ==individualUtils.mobileLogoPanel){
          setTimeout("individualUtils.showMobileLogoMeta()", 500);
        }
      },

      displayImageURL: function (url, options,panel) {
        individualUtils.renderPanel = panel;

        var loadingImage = loadImage(
                url,
                individualUtils.replaceResults,
                options
        );
        if( !loadingImage ){
          panel.children().replaceWith(
                  $('<span>Your browser does not support the URL or FileReader API.</span>')
          );
        }

        if( panel == individualUtils.previewPanel ) {
          setTimeout("individualUtils.showImageMeta()", 500);
        }else if(panel ==individualUtils.mobileLogoPanel){
          setTimeout("individualUtils.showMobileLogoMeta()", 500);
        }
      },
      openCrop: function () {
        var options = {
                  maxWidth: individualUtils.maxWidth,
                  maxHeight : individualUtils.maxHeight,
                  canvas: true
                };

        var imgNode = individualUtils.previewPanel.find('img, canvas');
                if(imgNode){
                  var img = imgNode[0];
                  individualUtils.renderPanel = individualUtils.editPanel;
                  individualUtils.replaceResults(loadImage.scale(img,options));
                  $("#editLogoModal").modal('show');
                }

      },

      showCropBox : function(event){
        //event.preventDefault();
        var imgNode = individualUtils.editPanel.find('img, canvas'),
                img = imgNode[0];
        //console.info("width:" + img.width + " height:" + img.height);

        imgNode.Jcrop({
          setSelect: [0, 0, img.width, img.height],
          bgColor: 'transparent',
          onSelect: function (coords) {
            individualUtils.coordinates = coords;
          },
          onRelease: function () {
            individualUtils.coordinates = null;
          }
        }).parent().on('click', function (event) {
          event.preventDefault();
        });

      },

      doCrop : function(event){
        // 执行剪切操作，将结果显示在预览框里
        event.preventDefault();
        var img = individualUtils.editPanel.find('img, canvas')[0];
        if (img && individualUtils.coordinates) {
          individualUtils.renderPanel = individualUtils.previewPanel;
          individualUtils.replaceResults(loadImage.scale(img, {
            left: individualUtils.coordinates.x,
            top: individualUtils.coordinates.y,
            sourceWidth: individualUtils.coordinates.w,
            sourceHeight: individualUtils.coordinates.h,
            maxHeight: individualUtils.previewMaxHeight,
            maxWidth : individualUtils.previewMaxWidth
          }));
          individualUtils.coordinates = null;
          individualUtils.showImageMeta();
        }

      },

      saveLogo : function(){
        var imgNode = individualUtils.previewPanel.find('img, canvas'),
                mobileLogo = individualUtils.mobileLogoPanel.find('img, canvas') ;
        if(!imgNode){
          alert("请选择电脑版Logo！");
        }else if(!mobileLogo) {
          alert("请选择手机版Logo！");
        } else{
            var imgData = imgNode.get(0).toDataURL(), mobileLogoData = mobileLogo[0].toDataURL();
            $.ajax({
              type: 'POST',
              url: '/system/logoData.action',
              data: { "logoBase64Data" : imgData, "mobileLogoBase64Data": mobileLogoData },
              //contentType: 'application/json; charset=utf-8',
              dataType: 'text',
              success: function (msg) {
                var response = JSON.parse(msg);
                if(response.success){
                  // 提交其他数据
                  individualUtils.saveIndividual(response.data["obj.logoPath"], response.data["obj.mobileLogoPath"]);
                }else{
                  alert($.isArray(response.error)? response.error[0] : response.error);
                }
              }
            });
        }
      },

      saveIndividual : function(logoPath, mobileLogoPath){
        var name = $.trim($("#input-name").val());
        $.ajax({
          type: 'POST',
          url: '/system/individual!saveIndividual.action',
          data: { "obj.logoPath" : logoPath, "obj.mobileLogoPath": mobileLogoPath,"obj.name" : name },
          dataType: 'text',
          success: function (msg) {
            var response = JSON.parse(msg);
            if(response.success){
              // 提交其他数据
              alert("个性化设置成功，请刷新页面查看效果！");
            }else{
              alert($.isArray(response.error)? response.error[0] : response.error);
            }
          }
        });
      },

      logoLoadError: function(imgObj){
        //console.info("装载logo失败");
      },

      getIndividual : function(){
        $.ajax({
          type: 'POST',
          url: '/system/individual!getIndividual.action',
          dataType: 'text',
          success: function (msg) {
            var response = jQuery.parseJSON(msg);
            if(response.success){
              var logoURL = response.data["obj.logoPath"],
                      mobileLogoURL = response.data["obj.mobileLogoPath"],
                      name = response.data["obj.name"];
              if( logoURL ) {
                individualUtils.logoURL = logoURL;
                individualUtils.displayImageURL(logoURL,
                        {
                          maxWidth: individualUtils.previewMaxWidth,
                          maxHeight: individualUtils.previewMaxHeight,
                          canvas: true
                        }, individualUtils.previewPanel);
              }
                /*
                var img = new Image();
                img.onload = function () {
                  $("#preview-container").append($('<img>',
                          {
                            id: 'logo-preview',
                            src: logoURL,
                            width: this.width,
                            height: this.height
                          }).load(function () {
                            individualUtils.showImageMeta();
                          }));

                };
                img.onerror = function () {
                  // 路径错误或其他错误引起，初始化对象
                  $("#preview-container").append($('<img>',
                          {
                            id: 'logo-preview',
                            src: individualUtils.defaultLogo,
                            load: function () {
                              individualUtils.showImageMeta();
                            }
                          }));

                };
                img.src = logoURL;
              }
              */
              if( mobileLogoURL ) {
                individualUtils.mobileLogoURL = mobileLogoURL;
                /*立即执行load会影响电脑版的解析，原因尚不明*/
                setTimeout('individualUtils.showMobileLogo()', 200);
              }
              $("#input-name").val(name);
            }
          }
        });
      },


      showMobileLogo : function(){
        individualUtils.displayImageURL(individualUtils.mobileLogoURL,
                {
                  maxWidth: individualUtils.previewMaxWidth,
                  maxHeight: individualUtils.previewMaxHeight,
                  canvas: true
                }, individualUtils.mobileLogoPanel);
      }
    }
  </script>

</body>
</html>

