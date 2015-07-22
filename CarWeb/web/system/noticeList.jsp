<%@ page import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.common.business.security.model.Admin" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.server.message.ServerMessager" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.util.XmlUtils" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="com.fortune.tags.TagUtils" %><%@ page import="java.util.*" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    Admin admin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
    if (admin == null) {
        out.println("<html>\n<body>已经超时</body>\n</html>\n");
        return;
    }
    String remoteAddr = request.getRemoteAddr();
    String[] systemManageIPArray = AppConfigurator.getInstance().getConfig("system.config.manage.ip",
            "140.206.48.130,172.," +
                    "192.168.," +
                    "10.,"+
                    "127.0.0.1,localhost,0:0:0:0:0:0:0:1").split(",");
    boolean willDisplaySystemMonitorInfo = false;
    if("fortune".equals(admin.getLogin())||"root".equals(admin.getLogin())){
        willDisplaySystemMonitorInfo = true;
    }else{
        for(String ip:systemManageIPArray){
            if(remoteAddr.contains(ip)){
                willDisplaySystemMonitorInfo = true;
                break;
            }
        }
    }
    List<Device> devices =new ArrayList<Device>();
    if(willDisplaySystemMonitorInfo){
        Device device = new Device();
        device.setName("本机");
        String hostIp = request.getServerName();
        device.setUrl("http://127.0.0.1" +
                ":"+request.getServerPort()+"/");
        devices.add(device);
        List<Map<String,String>> info=getSqlResult("select url,name from DEVICE where type in("+ DeviceLogicInterface.DEVICE_TYPE_WEB+
                ","+DeviceLogicInterface.DEVICE_TYPE_ENCODER+
                ","+DeviceLogicInterface.DEVICE_TYPE_DATABASE+
                ") and status="+DeviceLogicInterface.DEVICE_ONLINE);
        for(Map<String,String> row:info){
            String url = row.get("URL");
            int p0 = url.indexOf("://");
            if(p0>0){
                p0+=5;
                while(url.charAt(p0)!='/'){
                    p0++;
                    if(p0>=url.length()){
                        break;
                    }
                }
                url = url.substring(0,p0);
            }
            //如果不是重复的探查本机，就放弃
            if(!url.contains(hostIp)){
                device = new Device();
                device.setName(row.get("NAME"));
                device.setUrl(url);
                devices.add(device);
            }
        }
    }else{
    }
    String currentDate = StringUtils.date2string(new Date(),"yyyy-MM-dd");
    String yesterday = StringUtils.date2string(new Date(System.currentTimeMillis()-24*3600*1000L),"yyyy-MM-dd");
    //首先要得到有多少服务器
    //得到服务器硬盘信息
    String cspIds = ""+admin.getCspId();
    if(willDisplaySystemMonitorInfo||("root".equals(admin.getLogin())&&"1".equals(cspIds))){
        cspIds  = AppConfigurator.getInstance().getConfig("system.config.manage.cspIds", "15905690,16241840,16241843");
    }
    Map<Device,String> monitorResult = getDevicesInfo(devices);
    String[][] days = new String[][]{{"今天数据",currentDate,null},{"昨天数据",yesterday,currentDate},{"累计数据",null,null}};
%><html>
<head>
    <title>系统情况汇总</title>
    <style type="text/css">
        * {
            margin:2px;
            padding:2px;
            font-size:12px;
            color:black;
        }
        .infoBox{
            width:500px;
            margin-left: auto;
            margin-right: auto;
            border:solid 1px blue;
            background-color: #F2F6F2;
            padding: 10px;
            height: auto;
        }
        .infoBody{
            margin-left: auto;
            margin-right: auto;
            width:380px;
        }
        .infoTitle{
            font-size:16px;
            text-align: center;
        }
        .infoList{

        }
        .infoLabel{
            width:100px;
            float:left;
        }
        .infoData{
            width:300px;
            text-align: left;
        }
        .processBackground{
            width: 230px;
            display:block;
            height:18px;
            color:#f5f5dc;
            background-color: #f3ebe6;
            text-decoration:none;
            border: 1px solid rgba(1, 0, 1, 0.87);
            border-radius:2px;-webkit-border-radius:2px;-moz-border-radius:2px;
        }
        .processBar{
            display:block;
            height:18px;
            margin-left: -2px;
            margin-top:-2px;
            color: #010101;
            background-color: #107D99;
            text-decoration:none;
            border-radius:2px;-webkit-border-radius:2px;-moz-border-radius:2px;
        }
        ul{
            list-style-type:none;
        }
        .mediaTable tr td{
            width: 100px;
        }
    </style>
</head>
<body>
<div align="center">
<div class="infoBox">
    <div class="infoTitle">媒体信息统计</div>
    <div class="infoBody">
        <ul class="infoList">
            <li class="infoData">当前日期：<%=currentDate%></li>
        </ul>
   <table class="mediaTable">
       <tr><td>影片发布商</td><td>总数量</td><td>今天添加</td><td>昨日添加</td></tr>
       <tr><td>累计影片</td><td><%=getIntSqlResult("select count(*) contentCount from CONTENT where csp_id in (" +
               cspIds+
               ")")%></td>
           <td><%=getIntSqlResult("select count(*) dc from CONTENT where create_time>" +toDate(currentDate,"YYYY-MM-DD") +
                   "and csp_id in (" +cspIds+
                   ")")%></td>
            <td><%=getIntSqlResult("select count(*) dc from CONTENT where create_time<" +toDate(currentDate,"YYYY-MM-DD")+" and csp_id in (" +
                     cspIds+
                    ")" +
                    "and create_time>" +toDate(yesterday,"YYYY-MM-DD"))%></td>
       </tr>
       <%
           //统计各个CSP的影片数量，以列表方式表现 。目前只有三个

           List<Map<String,String>> data = getSqlResult("select a.*,c.name from (select count(*) cc,csp_id from CONTENT where csp_id in (" +
                   cspIds+") group by csp_id) a ,CSP c where a.csp_id = c.id");
           for(Map<String,String> row:data){
               out.println("<tr><td>"+row.get("NAME")+"</td><td>"+row.get("CC")+"</td>" +
                       "<td>"+getIntSqlResult("select count(*) dc from CONTENT where csp_id=" + row.get("CSP_ID") +
                       " and create_time>" + toDate(currentDate))+
                       "</td><td>"+getIntSqlResult("select count(*) dc from CONTENT where csp_id=" + row.get("CSP_ID") +
                       " and create_time>" + toDate(yesterday , "YYYY-MM-DD")+
                       " and create_time<" + toDate(currentDate,"YYYY-MM-DD"))+"</td></tr>");
           }
       %>
   </table>
    </div>
</div>
<%
    if(willDisplaySystemMonitorInfo){
    //服务器情况统计，查询web服务器（86，87，89）服务器的空间情况，包括c盘，d盘，内存，cpu
    //图形化，百分比
%>
<div class="infoBox">
    <div class="infoTitle">服务器监控</div>
    <div class="infoBody">
        <%
            int i=0;
            int deviceIndex = 0;
            for (Device checkDevice:monitorResult.keySet()) {
                String hostInfo = monitorResult.get(checkDevice);
                if (hostInfo!=null && !"".equals(hostInfo.trim())) {
                    deviceIndex++;
        %>     <table style="background-color: <%=deviceIndex%2==0?"#808080":"#E8E8E8"%>">
        <tr>
            <td colspan="2">
                服务器：<%=checkDevice.getName()%>
            </td>
        </tr>
        <%
            Element root = XmlUtils.getRootFromXmlStr(hostInfo);
            List disks = root.selectNodes("disk-info/disk");
            if(disks!=null&&disks.size()>0){
        %>      <%
        for(Object obj:disks){
            Node disk = (Node) obj;
            long total = XmlUtils.getLongValue(disk, "@total",0)/1024/1024/1024;
            //long free = XmlUtils.getLongValue(disk, "@free",0);
            long used = XmlUtils.getLongValue(disk, "@used",0)/1024/1024/1024;
            String name = XmlUtils.getValue(disk, "@name","");
    %>      <tr>
        <td><%=name%></td><td><%=getProcessHtml(300,total,used,used+"GB/"+total+"GB",i)%></td>
    </tr>

        <%
                }
            }
            Node cpuInfo = root.selectSingleNode("cpu-info");
            String cpuUsed = XmlUtils.getValue(cpuInfo,"@used","0");
            float cpuUsedValue = Float.valueOf(cpuUsed);

        %>
        <tr>
            <td>CPU:</td><td><%=getProcessHtml(300,100,Math.round(cpuUsedValue),cpuUsed+"%",i)%></td>
        </tr>
    </table>
        <%
                }
            }
        %>
    </div>
</div>
<%
    }
        /**
         * 从encoder_task表里进行统计。status状态：1运行中，2队里中，3已完成，4以后都是错误代码
         */
    TagUtils tagUtils = TagUtils.getInstance();
%>
<div class="infoBox">
    <div class="infoTitle">转码信息统计</div>
    <table>
        <tr><td></td></tr>
    </table>
        <ul class="infoList">
            <%
                for (String[] day : days) {
                    String beginDay = day[1];
                    String endDay = day[2];
                    List<Map<String, String>> rows = getTaskState(beginDay, endDay,cspIds);
            %>
            <div style="background-color:rgba(27,37,35,0.25)">
            <li class="infoData" style="font-size: 18px;"><%=day[0]%>：<%=getTaskCount(beginDay, endDay,cspIds)%>个</li>
             <table>
            <%
                for (Map<String, String> row : rows) {
                    String STATUS = row.get("STATUS");
                    String count = row.get("SC");
                    String displayName = tagUtils.getDictName("encoderTaskStatus", STATUS);
                    int status = StringUtils.string2int(row.get("STATUS"), 0);
                    if (status > 3) {
            %>
            <tr><td  style="color: red;"><a href="../encoder/encoderTaskListCanOrder.jsp?status=<%=status%>&begin=<%=beginDay%>&end=<%=endDay%>"><%=displayName%>：</a></td><td  style="color: red;"><%=count%>个</td></tr>
            <%
            } else {
            %>
            <tr><td><%=displayName%>：</td><td><%=count%>个</td></tr>
            <%
                        }
                    }
            %>
             </table>
                  </div>
            <%
                }
            %>
        </ul>
    </div>
</div>
<div class="infoLabel processBackground processBar" style="display:none"></div>
</body>
</html><%@include file="../admin/sqlBase.jsp"%><%!
    public String getDayConditionSql(String sql,String beginDay,String endDay,String fieldName){
        String dayCondition = null;
        if(beginDay!=null){
            dayCondition= fieldName+" > "+toDate(beginDay);
        }
        if(endDay!=null){
            if(dayCondition!=null)dayCondition+=" and ";
            dayCondition += fieldName+" <= "+toDate(endDay);
        }
        if(dayCondition!=null){
            sql += " where "+dayCondition;
        }
        return sql;
    }
    public String getTaskConditionSql(String sql,String beginDay,String endDay,String cspIds){
        sql = getDayConditionSql(sql, beginDay,endDay,"start_time");
        if(cspIds!=null&&!"".equals(cspIds.trim())){
            if(!sql.toLowerCase().contains(" where")){
                sql += " where ";
            }else{
                sql+=" and ";
            }
            sql += "clip_id in (select id from CONTENT_PROPERTY where content_id in (" +
                    "select id from CONTENT where csp_id in ("+cspIds+")))";
        }
        return sql;
    }
    public int getTaskCount(String beginDay,String endDay,String cspIds){
        return getIntSqlResult(getTaskConditionSql("select count(*) from ENCODER_TASK",beginDay,endDay,cspIds));
    }

    public List<Map<String,String>> getTaskState(String beginDay,String endDay,String cspIds){
        String sql = "select count(*) SC,STATUS from ENCODER_TASK";
        return getSqlResult(getTaskConditionSql(sql,beginDay,endDay,cspIds)+" group by STATUS order by STATUS asc");
    }

    public int getIntSqlResult(String sql){
        List<Map<String,String>> dataRows = getSqlResult(sql);
        if(dataRows!=null&&dataRows.size()>0){
            return StringUtils.string2int(dataRows.get(0).values().iterator().next(),-1);
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,String>> getSqlResult(String sql){
        logger.debug("准备执行："+sql);
        List<Object> executeResult = executeSql(sql,null,null,null,null);
        if(executeResult!=null){
            for(Object result:executeResult){
                if(result instanceof Object[]){
                    Object[] dataResult = (Object[]) result;
                    return (List<Map<String,String>>) dataResult[1];
                }
            }
        }
        return new ArrayList<Map<String, String>>(0);
    }
    public String getProcessHtml(int length,long max,long pos,String label,int chooseColor){
        int barLength = 0;
        if(max>0){
            barLength = (int)(length * pos / max);
        }
        String processBarColor="#107D99";
        if(chooseColor%2==0){
            processBarColor="#3399FF";
        }
        //超过百分之80 会显示红色
        if(length-barLength<60){
            processBarColor="red";
        }
        return "<div class='processBackground' style='width:"+length+"'>" +
                   "<div class=processBar style='background-color: "+processBarColor+";width:" +barLength+
                "'>"+label+
                   "</div>"+
                "</div>";
    }
    public class MonitorWorker extends Thread{
        private Device device ;
        private boolean isRunning = true;
        private  String result = "";
        public MonitorWorker(Device device){
            isRunning = true;
            this.device =device;
        }

        public String getDeviceName(){
            if(device!=null)return device.getName();
            return "没有设备设置";
        }

        public void run(){
            isRunning = true;
            if(device!=null){
                logger.debug("准备监控："+device.getName()+","+device.getUrl());
                String url = device.getUrl();
                while(url.endsWith("/")){
                    url = url.substring(0,url.length()-1);
                }
                result = new ServerMessager().postToHost(url+"/interface/getServerMessage.jsp", null);
                if(result!=null&&!"".equals(result)){
                    logger.debug("监控："+device.getName()+" 完毕:\n"+result);
                }else{
                    logger.error("监控失败："+device.getName());
                }
            }
            isRunning = false;
        }
        public boolean stileRunning(){
            return isRunning;
        }
        public String getResult(){
            return  result;
        }
        public Device getDevice(){
            return device;
        }
        public Long getDeviceId(){
            if(device==null){
                return null;
            }
            return device.getId();
        }
    }
    public Map<Device,String> getDevicesInfo(List<Device> devices){
        int i=0;
        List<MonitorWorker> workers = new ArrayList<MonitorWorker>(devices.size());
        for(Device device:devices){
            MonitorWorker worker = new MonitorWorker(device);
            worker.start();
            workers.add(worker);
        }
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
        Map<Device ,String> result = new HashMap<Device, String>();
        for(MonitorWorker worker:workers){
            logger.debug("监控结果："+worker.getDevice().getName()+"="+worker.getResult());
            result.put(worker.getDevice(),worker.getResult());
        }
        return result;
    }
    public String toDate(String dateStr){
        return toDate(dateStr,"YYYY-MM-DD");
    }
    public String toDate(String dateStr,String dateFormat){
        com.fortune.util.AppConfigurator config = com.fortune.util.AppConfigurator.getInstance("/jdbc.properties");
        if(config.getConfig("jdbc.driverClassName","").contains("oracle")){
            return "to_date('"+dateStr+"','"+dateFormat+"')";
        }
        return "'"+dateStr+"'";
    }
%>