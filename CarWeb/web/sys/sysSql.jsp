<%@ page import="com.fortune.rms.business.system.model.Device" %>
<%@ page import="org.dom4j.Element" %>
<%@ page import="com.fortune.util.XmlUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="org.dom4j.Node" %>
<%@ page import="com.fortune.server.message.ServerMessager" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-12-2
  Time: 上午8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

</body>
</html>
<%!
    Logger systemInfoJsonlogger = Logger.getLogger("com.fortune.rms.jsp.systemInfoJson.jsp");
    Map<String,Object> getDeviceMap(Device device,String monitorResult){
        Map<String,Object> result = new HashMap<String,Object>();
        if(device!=null){
            result.put("name",device.getName());
            result.put("id",device.getId());
            result.put("type",device.getType());
            result.put("maxTask",device.getMaxTask());
            result.put("status",device.getStatus());
            if(monitorResult!=null){
                Element root = XmlUtils.getRootFromXmlStr(monitorResult);
                if(root!=null){
                    List disks = root.selectNodes("disk-info/disk");
                    if(disks!=null&&disks.size()>0){
                        List<Map<String,Object>> diskMaps = new ArrayList<Map<String,Object>>();
                        for(Object obj:disks){
                            Node disk = (Node) obj;
                            String name = XmlUtils.getValue(disk, "@name","");
                            if(name.startsWith("/dev")||name.startsWith("/run")||name.startsWith("/boot")){
                                continue;
                            }
                            long total = XmlUtils.getLongValue(disk, "@total",0)/1024/1024/1024;
                            //long free = XmlUtils.getLongValue(disk, "@free",0);
                            long used = XmlUtils.getLongValue(disk, "@used",0)/1024/1024/1024;
                            Map<String,Object> diskMap = new HashMap<String, Object>();
                            diskMap.put("name",name);
                            if(total==0){
                                total=20;
                            }
                            diskMap.put("total",total);
                            diskMap.put("used",used);
                            diskMap.put("rate",used*100/total);
                            diskMaps.add(diskMap);
                        }
                        result.put("disks",diskMaps);
                    }
                }
            }else{
                result.put("success",false);
            }
        }else{
            return null;
        }
        return result;
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
                systemInfoJsonlogger.debug("准备监控："+device.getName()+","+device.getUrl());
                String url = "http://"+device.getIp()+":"+device.getMonitorPort();
                result = new ServerMessager().postToHost(url+"/interface/getServerMessage.jsp", null);
                if(result!=null&&!"".equals(result)){
                    systemInfoJsonlogger.debug("监控："+device.getName()+" 完毕:\n"+result);
                }else{
                    systemInfoJsonlogger.error("监控失败："+device.getName());
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
        List<MonitorWorker> workers = new ArrayList<MonitorWorker>(devices.size());
        for(Device device:devices){
            if(DeviceLogicInterface.DEVICE_ONLINE==device.getStatus()){
                MonitorWorker worker = new MonitorWorker(device);
                worker.start();
                workers.add(worker);
            }
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
            systemInfoJsonlogger.debug("监控结果："+worker.getDevice().getName()+"="+worker.getResult());
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

%><%@include file="../admin/sqlBase.jsp"%>
