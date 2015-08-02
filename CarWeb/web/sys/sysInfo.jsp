<%@ page
        import="com.fortune.common.business.security.model.Admin" %><%@ page
        import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page import="java.util.Map" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ taglib prefix="s" uri="/struts-tags" %><%
    Admin operatorLogin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
    if(operatorLogin==null){
        response.sendRedirect("/login.jsp");
        return;
    }
    Integer liveServerCount = 0,liveChannelCount=0;
    Integer encoderCount = 0;
    Integer waitingTaskCount=0;
    Integer runningTaskCount = 0;
    Integer finishedCount=0;
    Integer otherCount = 0;
    int playerCount;
    int licenseCount;
    DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
    List<MonitorWorker> workers = new ArrayList<MonitorWorker>();
    List<Device> devices = deviceLogicInterface.getAll();
    EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface",session.getServletContext());
    if(operatorLogin.getId()==1){
        Map<Integer,Integer> serverCount = deviceLogicInterface.getServerCount();
        encoderCount = serverCount.get((int)DeviceLogicInterface.DEVICE_TYPE_ENCODER);
        liveServerCount = serverCount.get((int)DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE);
        if(liveServerCount==null){
            liveServerCount=0;
        }
        if(encoderCount==null){
            encoderCount=0;
        }
        Map<Integer,Integer> encodeTaskCount = encoderTaskLogicInterface.getTaskCount(-1L,-1L,-1L);
        otherCount = 0;
        for(Integer key:encodeTaskCount.keySet()){
            if(EncoderTaskLogicInterface.STATUS_WAITING.equals(key)){
                waitingTaskCount = encodeTaskCount.get(key);
            }else if(EncoderTaskLogicInterface.STATUS_RUNNING.equals(key)){
                runningTaskCount = encodeTaskCount.get(key);
            }else if(EncoderTaskLogicInterface.STATUS_FINISHED.equals(key)){
                finishedCount = encodeTaskCount.get(key);
            }else{
                Integer v = encodeTaskCount.get(key);
                if(v!=null){
                    otherCount+= v;
                }
            }
        }
        if(waitingTaskCount==null){
            waitingTaskCount = 0;
        }
        if(runningTaskCount==null){
            runningTaskCount = 0;
        }
    }
    boolean allInOne=false;
    Device webServer = null;
    for(Device device:devices){
        if(DeviceLogicInterface.DEVICE_TYPE_WEB ==device.getType()){
            webServer = device;
        }
        if(device.getUrl().startsWith("http://serverIP:serverPort")){
            if(encoderCount<=1&&devices.size()<=4){//最多还有一个直播，一共4个
                allInOne = true;
            }
        }
    }
    if(allInOne&&webServer!=null){
        devices.clear();
        webServer.setName("RedexPro一体机");
        webServer.setIp("127.0.0.1");
        webServer.setType(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD);
        devices.add(webServer);
    }
    for(Device device:devices){
        if(DeviceLogicInterface.DEVICE_ONLINE==device.getStatus()){
            MonitorWorker worker = new MonitorWorker(device);
            worker.start();
            workers.add(worker);
        }
    }
    int contentCount = getIntSqlResult("select count(*) from Content c");
    int offlineCount =getIntSqlResult("select count(*) from Content c where c.status="+ContentLogicInterface.STATUS_CP_OFFLINE);
    int encodingCount =getIntSqlResult("select count(*) from Content c where c.status="+ContentLogicInterface.STATUS_ENCODING);
    int onlineCount = getIntSqlResult("select count(*) from Content c where c.status=2 and c.id in (select cc.content_id from content_csp cc where cc.status=2)");
    int deleteCount = getIntSqlResult("select count(*) from Content c where c.status="+ ContentLogicInterface.STATUS_DELETE);
    //等待访问远端服务器线程结束
    boolean isRunning = true;
    while(isRunning){
        isRunning = false;
        for(MonitorWorker worker:workers){
            if(worker.stileRunning()){
                isRunning = true;
                break;
            }
        }
    }
    Map<Device ,String> monitResult = new HashMap<Device, String>();
    for(MonitorWorker worker:workers){
        systemInfoJsonlogger.debug("监控结果："+worker.getDevice().getName()+"="+worker.getResult());
        monitResult.put(worker.getDevice(),worker.getResult());
    }


%><%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-8
  Time: 16:49:08
  管理员首页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>系统一栏 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="/inc/displayCssJsLib.jsp"%>
    <style type="text/css">
        .main-upload{
        }
        .main-film{
            background-color:#4ecc6e;
        }
        .main-cog{
            background-color:#feb448
        }
        .main-user{
            background-color:#9564e2;
        }
    </style>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<%@include file="/inc/displayHeader.jsp"%>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try {
        ace.settings.check('main-container', 'fixed')
    } catch (e) {
    }
</script>

<%@include file="/inc/displayMenu.jsp"%>
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
            <li class="active">
                当前位置:
                <a href="../man.jsp"> 管理首页</a>
            </li>
            <li class="active">${folderName}</li><li class="active">${functionName}</li>
        </ul>
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-header">
        <h1>
            <i class="ace-icon fa fa-desktop"></i>系统一览

        </h1>
    </div>
    <!-- /.page-header -->
    <div class="page-content">

        <div class="page-content-area">


            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
<%--
                    <div class="alert alert-block alert-success">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>

                        <i class="ace-icon fa fa-check green"></i>

                        <%=AppConfigurator.getInstance().getConfig("system.welcomeMessage","欢迎来到Redex Server,管理中心。")%>
                    </div>
--%>

                    <div class="row">
                        <div class="col-xs-12   widget-container-col">
                            <!-- #section:custom/widget-box -->
                            <div class="widget-box widget-box1">
<%
    if(operatorLogin!=null&&operatorLogin.getId()==1){//只有root可见
%>
                                <div class="widget-header">
                                    <h5 class="widget-title">系统状态数据一览</h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <ul>
                                            <li><i class="ace-icon fa fa-play-circle-o"></i>
                                                <span class="red3">“<%=contentCount%>”</span>个媒体文件，<span class="red3">“<%=onlineCount%>”</span>
                                                个在线”，<span class="red3">“<%=encodingCount%>”</span>个在转码
                                                ，<span class="red3">“<%=offlineCount%>”</span>个下线，
                                                <span class="red3">“<%=deleteCount%>”</span>个标记为删除。
                                            </li>
                                            <li><i class="ace-icon fa fa-tasks"></i>
                                                <span class="red3">“<%=encoderCount%>”</span> 台转码服务器， <span class="red3">“<%=runningTaskCount%>”</span>
                                                个转码任务进行中， <span class="red3">“<%=waitingTaskCount%>” </span> 个在排队，
                                                <span class="red3">“<%=finishedCount%>” </span> 个已经完成，
                                                <span class="red3">“<%=otherCount%>” </span> 个异常。
                                                <a class="btn btn-btn" href="encodeTasks.jsp">转码任务</a> </li>
<%
    if(encoderCount>1){
        List<Device> encoders = deviceLogicInterface.getDevicesOfType(DeviceLogicInterface.DEVICE_TYPE_ENCODER,DeviceLogicInterface.DEVICE_ONLINE,-1L);
        if(encoders!=null&&encoders.size()>0){
%>                                          <li style="height:<%=encoders.size()%>00%">
                                            <table border="1" style="margin-left:100px;">
                                                    <tr>
                                                        <td style="width:200px;text-align: center;">编码器</td>
                                                        <td style="width:80px;text-align: center">完成任务数</td>
                                                        <td style="width:80px;text-align: center">等待</td>
                                                        <td style="width:80px;text-align: center">正在执行</td>
                                                        <td style="width:80px;text-align: center">错误数</td>
                                                    </tr>
                                                    <%
            for(Device device:encoders){
                Map<Integer,Integer> encodeTaskCount = encoderTaskLogicInterface.getTaskCount(device.getId(),-1L,-1L);
                int allCount = 0;
                for(Integer key:encodeTaskCount.keySet()){
                    allCount+=encodeTaskCount.get(key);
                }
                Integer finished=encodeTaskCount.get(EncoderTaskLogicInterface.STATUS_FINISHED);
                Integer waiting = encodeTaskCount.get(EncoderTaskLogicInterface.STATUS_WAITING);
                Integer running = encodeTaskCount.get(EncoderTaskLogicInterface.STATUS_RUNNING);
                if(finished==null)finished=0;
                if(waiting==null) waiting=0;
                if(running==null) running=0;

%>                                           <tr>
                                                    <td style=""><%=device.getName()%></td>
                                                    <td style="width:80px;text-align: center"><%=finished%></td>
                                                    <td style="width:80px;text-align: center"><%=waiting%></td>
                                                    <td style="width:80px;text-align: center"><%=running%></td>
                                                    <td style="width:80px;text-align: center"><%=(allCount-finished-waiting-running)%></td>
                                                </tr>
                                                    <%
            }
%>                                           </table></li><%
        }
    }
%>

                                            <li><i class="ace-icon fa fa-pie-chart"></i>
                                                共 <span class="red3"> “<%=devices.size()%>”</span>台服务器， 详情如下：</li>
                                            <li style="height:auto">
                                                <table border="1" style="margin-left: 100px;">
                                                    <tr>
                                                        <td style="width:200px;text-align: center;" rowspan="2">服务器</td>
                                                        <td style="width: 400px;text-align:center" colspan="4">空间使用情况</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="text-align: center">挂载点</td>
                                                        <td style="text-align: center">空间大小</td>
                                                        <td style="text-align: center">已用大小</td>
                                                        <td style="text-align: center">已用百分比</td>
                                                    </tr>
                                                <%
                                                    licenseCount=0;
                                                    playerCount=0;
                                                    for(Device device:devices){
                                                        if(device.getType()==DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE){
                                                            continue;
                                                        }
                                                        if(device.getType()==DeviceLogicInterface.DEVICE_TYPE_HLS_VOD){
                                                            licenseCount+=device.getMaxTask();
                                                        }
                                                        String monitorXml = monitResult.get(device);
                                                        String spaceBody = "";
                                                        int rowSpan=0;
                                                        if(monitorXml!=null){
                                                            Element root = XmlUtils.getRootFromXmlStr(monitorXml);
                                                            if(root!=null){
                                                                if(device.getType()==DeviceLogicInterface.DEVICE_TYPE_HLS_VOD){
                                                                    //用ftpPort暂时表示当前并发数量
                                                                    int clientCount = XmlUtils.getIntValue(root.selectSingleNode("sysInfo"), "@clientCount", 0);
                                                                    device.setFtpPort(clientCount*1L);
                                                                    playerCount+=clientCount;
                                                                }
                                                                List disks = root.selectNodes("disk-info/disk");
                                                                if(disks!=null&&disks.size()>0){
                                                                    for(Object obj:disks){
                                                                        Node disk = (Node) obj;
                                                                        String name = XmlUtils.getValue(disk, "@name","");
                                                                        if(name.startsWith("/dev")||name.startsWith("/run")||name.startsWith("/boot")){
                                                                            continue;
                                                                        }
                                                                        long total = XmlUtils.getLongValue(disk, "@total",0)/1024/1024/1024;
                                                                        //long free = XmlUtils.getLongValue(disk, "@free",0);
                                                                        long used = XmlUtils.getLongValue(disk, "@used",0)/1024/1024/1024;
                                                                        if(total==0){
                                                                            total=20;
                                                                        }
                                                                        if(!spaceBody.equals("")){
                                                                            spaceBody+="<tr>";
                                                                        }
                                                                        spaceBody +="<td style='margin-left:20px;'>"+name+
                                                                                "</td><td align='center'>"+
                                                                                total+"GB</td><td align='center'>"+used+"GB</td>";
                                                                        spaceBody+="<td align='center'>"+(used*100/total)+"%</td></tr>";
                                                                        rowSpan++;
                                                                    }

                                                                }
                                                            }                                                        }
                                                        if(rowSpan==0){
                                                            rowSpan=1;
                                                        }
                                                        out.print("<tr><td style='text-align:center' rowspan='" +rowSpan+
                                                                "'>"+device.getName()+"</td>");
                                                        if(spaceBody.equals("")){
                                                            spaceBody="<td colspan='4'>&nbsp;监控数据有误</td></tr>";
                                                        }
                                                        out.print(spaceBody);
                                                    }
                                                %>
                                                </table>
                                            </li>
                                            <li><i class="ace-icon fa fa-eye"></i>
                                                <span class="red3"> “<%=playerCount%>”</span> 人正在观看视频，共 <span
                                                        class="red3">“<%=licenseCount%>”</span> 授权
                                            </li>
                                            <li style="height:auto;">

                                                <table border="1" style="margin-left: 100px;">
                                                    <tr>
                                                        <td style="width:300px;text-align: center;">名称</td>
                                                        <td style="width: 100px;text-align:center">最大并发</td>
                                                        <td style="width: 100px;text-align:center">当前并发</td>
                                                    </tr>
<%
    for(Device device:devices){
        if(device.getType()==DeviceLogicInterface.DEVICE_TYPE_HLS_VOD/*||device.getType()==DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE*/){
            %>                                      <tr>
                                                    <td style="text-align:center"><%=device.getName()%></td>
                                                    <td style="text-align:center"><%=device.getMaxTask()%></td>
                                                    <td style="text-align:center"><%=device.getFtpPort()%></td>
            </tr>
                                                    <%
        }
    }
%>
                                                    </table>
                                            </li>
                                        </ul>

                                    </div>
                                </div>

                                <%
    }
%>                            </div>
                        </div>
                    </div>
                    <div class="space"></div>
                    <div class="row">
                    </div>
                    <div>
                        <p>&nbsp;</p>
                    </div>

                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->
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
<%@include file="/inc/displayFooter.jsp"%>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function ($) {
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


    })
</script>

</body>
</html>
<%@include file="sysSql.jsp"%>