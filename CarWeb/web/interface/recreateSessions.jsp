<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map,java.util.HashMap,java.util.Date" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.content.model.ContentProperty" %>
<%@ page import="com.fortune.rms.business.encoder.model.EncoderTask" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/1/28
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%><%
  EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface", session.getServletContext());
  ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
  String logs = recreateSessions(encoderTaskLogicInterface,contentPropertyLogicInterface);

%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body><pre><%=logs%></pre>
</body>
</html><%!
  public String recreateSessions(EncoderTaskLogicInterface encoderTaskLogicInterface,ContentPropertyLogicInterface contentPropertyLogicInterface){
    String sql = "select cp.ID,cp.CONTENT_ID,c.MODULE_ID,c.NAME,cp.INT_VALUE,c.DEVICE_ID from content_property cp,content c where " +
            "c.create_time > to_date('2014-12-01','YYYY-MM-DD') and cp.property_id=15884973 and " +
              "(cp.extra_int=0 or cp.extra_int is null) and cp.content_id=c.id";
    List<Object> result = executeSql(sql,null,null,null,null);
    String logs = StringUtils.date2string(new Date())+"重建转码任务过程启动!\r\n";
    List<Long> clips = new ArrayList<Long>();
    int taskCount=0;
    for(Object sqlR:result){
      if(sqlR instanceof String){
        logs+=sqlR+"\r\n";
        continue;
      }
      if(sqlR instanceof Object[]){
        for(Object data:(Object[]) sqlR){
          if(data instanceof ArrayList){
            ArrayList rows = (ArrayList)data;
            int size = rows.size();
            int idx = 0;
            logger.debug("将会处理"+size+"条数据，可能会有"+size*3+"个任务生成");
            for(Object r:(ArrayList) data){
              idx++;
              if(idx%10==0){
                logger.debug("已经处理了"+idx+"条数据，占比"+(idx*100/size)+"%，生成了" +taskCount+
                        "个任务...");
              }
              Map<String,String> row = (Map<String,String>)r;
              Long id = StringUtils.string2long(row.get("ID"),-1);
              Long contentId=StringUtils.string2long(row.get("CONTENT_ID"),-1);
              if(id>0){
                Content content = new Content();
                ContentProperty cp = contentPropertyLogicInterface.get(id);
                content.setId(contentId);
                content.setName(row.get("NAME"));
                content.setDeviceId(StringUtils.string2long(row.get("DEVICE_ID"),-1));
                content.setModuleId(StringUtils.string2long(row.get("MODULE_ID"),-1));
                if(content.getModuleId()>0){
                  if(content.getDeviceId()>0){
                    try {
                      List<EncoderTask> tasks = encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content,cp);
                      for(EncoderTask task:tasks){
                        taskCount++;
                        logs+="添加任务成功："+task.getName()+"\r\n";
                      }
                    } catch (Exception e) {
                      logs+="创建任务时发生异常：" +e.getMessage()+"\r\n";
                    }
                  }else{
                    logs+="无法创建任务，因为DeviceId不对："+row.get("NAME")+","+row.get("DEVICE_ID");
                  }
                }else{
                  logs+="无法创建任务，因为MODULE_ID不对："+row.get("NAME")+","+row.get("MODULE_ID");
                }
              }
            }
          }
        }
      }
    }
    logger.debug("累计转码任务添加了" +taskCount+
            "个");
    logs += StringUtils.date2string(new Date())+" 重建转码任务过程结束!累计转码任务添加了" +taskCount+
            "个";
    return logs;
  }
%><%@include file="../admin/sqlBase.jsp"%>
