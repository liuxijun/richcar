<%@ page import="java.util.List" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %>
<%@ page import="com.fortune.rms.business.system.model.Device" %>
<%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %>
<%@ page import="com.fortune.rms.business.encoder.model.EncoderTask" %>
<%@ page import="com.fortune.rms.business.encoder.model.EncoderTemplate" %>
<%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.io.File" %>
<%@ page import="com.fortune.util.*" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-29
  Time: 下午8:02
  转码完成有一个BUG，会导编码后把结果给了其他的媒体文件。在这里进行修正，扫描所有的编码完成文件，重新整理数据
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairEncodeResult.jsp");
    EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface",session.getServletContext());
    final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
/*
    String propertyCode = "Media_Url_Source";
    PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
    Property property = propertyLogicInterface.getByCode(propertyCode);
    ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
*/
    final EncoderTemplateLogicInterface encoderTemplateLogicInterface = (EncoderTemplateLogicInterface) SpringUtils.getBean("encoderTemplateLogicInterface",session.getServletContext());
    int taskCount = 0;
    //下面语句可能会有内存问题，6万调数据，不知道会不会溢出
    List<EncoderTask> allTasks = encoderTaskLogicInterface.getAll();
    //
    for(EncoderTask task:allTasks){
        Long streamingServerId = task.getStreamServerId();
        //如果是等待状态的任务，mp4文件肯定是无效的，不用考虑。一旦开始执行就会将其抹杀
        if(EncoderTaskLogicInterface.STATUS_WAITING.equals(task.getStatus())){
            continue;
        }
        Device server =(Device) CacheUtils.get(streamingServerId,"device",new DataInitWorker(){
           public Object init(Object keyId,String cacheName){
               return  deviceLogicInterface.get((Long)keyId);
           }
        });
        if(server!=null){
            File file = new File(server.getLocalPath()+"/"+task.getDesertFileName());
            if(file.exists()){
                SimpleFileInfo fileInfo = new SimpleFileInfo(file.getName(), file.length(),new Date(file.lastModified()),false, FileType.video);
                if(FileUtils.setFileMediaInfo(file.getAbsolutePath(),fileInfo)){
                    //文件可以播放
                    task.setStatus(EncoderTaskLogicInterface.STATUS_FINISHED);
                    task.setTemplate((EncoderTemplate) CacheUtils.get(task.getTemplateId(), "encoderTemplate", new DataInitWorker() {
                        public Object init(Object keyId, String cacheName) {
                            return encoderTemplateLogicInterface.get((Long) keyId);
                        }
                    }));
                    task.setLength(fileInfo.getLength());
                    encoderTaskLogicInterface.taskFinished(task);
                    taskCount ++;
                }
            }
        }
    }

    logger.debug("累计添加任务："+taskCount);
%><html>
<head>
    <title>修复编码任务</title>
</head>
<body>
<p>累计修复<%=taskCount%>个任务！<a href="../encoder/encoderTaskListCanOrder.jsp">查看</a></p>
</body>
</html>