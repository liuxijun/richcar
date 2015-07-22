<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/2
  Time: 9:16
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="footer">
    <div class="footer-inner">
        <!-- #section:basics/footer -->
        <div class="footer-content">
						<span class="bigger-120">
							<%=com.fortune.util.AppConfigurator.getInstance().getConfig("system.copyRight","© 2014 复全网络版权所有")%>
						</span>
        </div>
        <!-- /section:basics/footer -->
    </div>
</div>
<!--loading-->
<div id="loading_container" class="mask">
    <div id="loading" class="central_loading">
        <i class="fa fa-spin fa-spinner bigger-300"></i>
        <i style="display:block;margin:10px auto">正在加载</i>
    </div>
</div>
<!-- basic scripts -->
<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='../scripts/jquery.min.js'>" + "<" + "/script>");
</script>
<!-- <![endif]-->
<!--[if IE]>
<script type="text/javascript">
window.jQuery || document.write("<script src='../scripts/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='scripts/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="../scripts/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="../scripts/excanvas.min.js"></script>
<![endif]-->
<script src="../scripts/jquery-ui.custom.min.js"></script>
<script src="../scripts/jquery.ui.touch-punch.min.js"></script>
<script src="../scripts/chosen.jquery.min.js"></script>
<script src="../scripts/fuelux/fuelux.spinner.min.js"></script>
<script src="../scripts/date-time/bootstrap-datepicker.min.js"></script>
<script src="../scripts/date-time/bootstrap-timepicker.min.js"></script>
<script src="../scripts/date-time/moment.min.js"></script>
<script src="../scripts/date-time/daterangepicker.min.js"></script>
<script src="../scripts/date-time/bootstrap-datetimepicker.min.js"></script>
<script src="../scripts/bootstrap-colorpicker.min.js"></script>
<script src="../scripts/jquery.knob.min.js"></script>
<script src="../scripts/jquery.autosize.min.js"></script>
<script src="../scripts/jquery.inputlimiter.1.3.1.min.js"></script>
<script src="../scripts/jquery.maskedinput.min.js"></script>
<script src="../scripts/bootstrap-tag.min.js"></script>
<script src="../scripts/typeahead.jquery.min.js"></script>
<script src="../scripts/fuelux/fuelux.tree.min.js?v=4.23.428"></script>
<script src="../scripts/jquery.nestable.js"></script>
<script src="../scripts/ace-elements.min.js"></script>
<script src="../scripts/ace.min.js"></script>

<script src="../scripts/redex_page.min.js"></script>
<script src="../scripts/redex_utils.js"></script>
<script src="../js/md5.js"></script>


