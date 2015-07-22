<%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.Date" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.encoder.model.EncoderTask" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %><%@ page
        import="com.fortune.rms.business.encoder.model.EncoderTemplate" %><%@ page
        import="com.fortune.util.*" %><%@ page import="com.fortune.rms.business.syn.model.SynTask" %><%@ page
        import="com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface" %><%@ page
        import="com.fortune.rms.business.syn.model.SynFile" %><%@ page
        import="com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface" %><%@ page
        import="com.fortune.server.message.ServerMessager" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-1-9
  Time: 上午11:22
  转码任务的接口
  每当系统启动时，会启动EncodeTaskRemoteManager实例，在其中启动相应数量的线程，向远端的
  这个encoderSession.jsp发起请求，包括注册、请求分配任务、任务进度汇报、下线等操作。
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    Map<String,Object> result = new HashMap<String,Object>();
    String command = request.getParameter("command");
    if(command==null){
        String queryString = request.getQueryString();
        logger.debug("request:"+queryString);
        command = StringUtils.getParameter(queryString,"command");
        if(command==null){
            command = "";
        }
    }
    String remoteAddr = request.getRemoteAddr();
    EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface",
            session.getServletContext());
    final SynFileLogicInterface synFileLogicInterface = (SynFileLogicInterface)SpringUtils.getBean("synFileLogicInterface",session.getServletContext());
    final SynTaskLogicInterface synTaskLogicInterface = (SynTaskLogicInterface)SpringUtils.getBean("synTaskLogicInterface",session.getServletContext());
    final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface)SpringUtils.getBean("deviceLogicInterface",
            session.getServletContext());
    final EncoderTemplateLogicInterface encoderTemplateLogicInterface = (EncoderTemplateLogicInterface) SpringUtils.getBean("encoderTemplateLogicInterface",
            session.getServletContext());
    int resultCode = 0;
    String resultString = "";
    long streamServerId =StringUtils.string2long(request.getParameter("streamServerId"),-1);
    Device encoder = getTranscoder(remoteAddr,deviceLogicInterface);
    if(encoder==null){
        long encoderId = StringUtils.string2long(request.getParameter("encoderId"),-1);
        if(encoderId>0){
            try {
                encoder = deviceLogicInterface.get(encoderId);
            } catch (Exception e) {
                encoder = null;
            }
        }
    }
    if(encoder!=null){
        if(!"get".equals(command)){
            logger.debug("转码器"+encoder.getName()+"发来命令："+command+",参数包括："+request.getQueryString());
        }
        //logger.debug("Has a encoder incoming:"+remoteAddr+","+encoder.getName()+",streamServerId="+streamServerId+",command="+command);
    }else{
        logger.debug("Has a encoder incoming:"+remoteAddr+",Can't found it by IP or ID,,streamServerId="+streamServerId+",command="+command);
    }
    if("get".equals(command)){
        if(encoder!=null){
            //每次请求，给一个任务。多线程情况下，就多次访问
            EncoderTask task = selectWaitingTask(encoderTaskLogicInterface,
                    deviceLogicInterface,encoderTemplateLogicInterface,encoder,
                    streamServerId);
            if(task!=null){
                resultCode = 200;
                result.put("task",task);
            }else{
                resultCode = 404;
            }
        }else{
            resultCode = 4004;
            resultString = "没有找到ip对应的编码器："+remoteAddr;
        }
    }else if("setDuration".equals(command)){
        EncoderTask task = getTask(encoderTaskLogicInterface,request);
        if(task!=null){
            int duration = StringUtils.string2int(request.getParameter("duration"),-1);
            if(duration>=0){
                task.setFileDate(new Date(StringUtils.string2long(request.getParameter("date"),-1)));
                task.setFileSize(StringUtils.string2long(request.getParameter("size"),-1));
                task.setLength(duration);
                encoderTaskLogicInterface.save(task);
            }
        }
    }else if("startup".equals(command)){
        if(encoder!=null){
            logger.debug("转码器上线了："+encoder.getName());
            //把所有该转码器显示是正在执行的任务都复位为等待
            PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.startTime","asc");
            List<EncoderTask> tasks = encoderTaskLogicInterface.getTasks(encoder.getId(),-1L,
                    EncoderTaskLogicInterface.STATUS_RUNNING,pageBean);
            for(EncoderTask task:tasks){
                task.setStatus(EncoderTaskLogicInterface.STATUS_WAITING);
                encoderTaskLogicInterface.save(task);
            }
        }else{
            logger.error("未知转码器上线了！！来自IP:"+request.getRemoteAddr());
        }
    }else if("shutdown".equals(command)){
        if(encoder!=null){
            logger.debug("转码器下线了："+encoder.getName());
        }else{
            logger.error("未知转码器下线了！！来自IP:"+request.getRemoteAddr());
        }
    }else if("distribute".equals(command)){
        try {
            final EncoderTask task = getTask(encoderTaskLogicInterface,request);
            resultCode = 404;
            if(task!=null){
                EncoderTemplate template = task.getTemplate();
                if(template==null){
                    template = getCachedTempalte(encoderTemplateLogicInterface,task);
                    task.setTemplate(template);
                }
                resultString = distributeMovie(task,template,request.getServerName(),deviceLogicInterface,synTaskLogicInterface,synFileLogicInterface);
                encoderTaskLogicInterface.updateLog(task,StringUtils.date2string(new Date())+" - 启动分发："+resultString);
                logger.debug(resultString);
                resultCode=200;
            }else{
                resultString=("无法找到任务：queryString="+request.getQueryString());
            }
        } catch (Exception e) {
            resultCode  = 500;
            resultString="尝试分发时发生异常："+e.getMessage();
        }
    }else if("finished".equals(command)){
        final EncoderTask task = getTask(encoderTaskLogicInterface,request);
        if(task!=null){
            int status = StringUtils.string2int(request.getParameter("status"),-1);
            if(status>=0){
                if(EncoderTaskLogicInterface.STATUS_RUNNING.equals(status)){
                    String logs = "任务发送来的状态信息错误，不应该是正在执行！手动进行修正，改为已经完成！";
                    logger.warn(logs);
                    encoderTaskLogicInterface.updateLog(task,logs);
                    status = EncoderTaskLogicInterface.STATUS_FINISHED;
                }
                task.setStopTime(new Date());
                task.setStatus(status);
                if(!EncoderTaskLogicInterface.STATUS_FINISHED.equals(status)){
                    String logs = "任务未能完成："+task.getName()+"发过来的状态："+status+","+MediaUtils.errorCodes.get(status);
                    logger.error(logs);
                    encoderTaskLogicInterface.updateLog(task, logs);
                }else {
                    logger.debug("任务已经完成："+task.getName());
                    task.setProcess(100);
                }
                encoderTaskLogicInterface.save(task);
                EncoderTemplate template = task.getTemplate();
                if(template==null){
                    template = getCachedTempalte(encoderTemplateLogicInterface,task);
                    task.setTemplate(template);
                }
                encoderTaskLogicInterface.taskFinished(task);
                if(EncoderTaskLogicInterface.STATUS_FINISHED.equals(status)){
                    //转码完成，准备进行分发
                    String disLog = distributeMovie(task,template,request.getServerName(),deviceLogicInterface,synTaskLogicInterface,synFileLogicInterface);
                    encoderTaskLogicInterface.updateLog(task,StringUtils.date2string(new Date())+" - 启动分发："+disLog);
                }
            }else{
                logger.error("任务状态输入有问题，输入的状态时："+request.getParameter("status"));
            }
        }
    }else if("beforeStart".equals(command)){
        EncoderTask task = getTask(encoderTaskLogicInterface,request);
        if(task!=null){
            encoderTaskLogicInterface.onTaskStart(task);
        }
    }else if("afterStart".equals(command)){
    }else if("process".equals(command)){
        EncoderTask task = getTask(encoderTaskLogicInterface,request);
        if(task!=null){
            int process = StringUtils.string2int(request.getParameter("process"),-1);
            if(process>0){
                task.setProcess(process);
                encoderTaskLogicInterface.update(task);
            }
        }else{
            resultCode=404;
        }
    }else if("log".equals(command)){
        EncoderTask task = getTask(encoderTaskLogicInterface,request);
        if(task!=null){
            String log = request.getParameter("log");
            resultCode = 404;
            resultString = "log参数没有输入";
            if(log!=null){
                log = log.trim();
                if(!"".equals(log)){
                    resultString = encoderTaskLogicInterface.updateLog(task,log);
                    resultCode = 200;
                }
            }
            //logger.debug("updateLog -- "+log);
        }else{
            resultCode=404;
        }
    }else if("error".equals(command)){

    }else{
        logger.debug("Unkown command:'" +command+"'");
        resultString = "暂时还不能处理的命令："+command;
    }

    Date stopTime = new Date();
    result.put("startTime",startTime);
    result.put("stopTime",stopTime);
    result.put("result",resultCode);
    result.put("command",command);
    result.put("resultString",resultString);
    String jsonResult = JsonUtils.getJsonString(result);
%><%=jsonResult%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.encoderSession.jsp");
    public EncoderTask getTask(EncoderTaskLogicInterface encoderTaskLogicInterface,HttpServletRequest request){
        EncoderTask task = null;
        long taskId = StringUtils.string2long(request.getParameter("taskId"),-1);
        if(taskId>0){
            try {
                task = encoderTaskLogicInterface.get(taskId);

            } catch (Exception e) {

            }
        }
        return task;
    }

    public synchronized EncoderTask  selectWaitingTask(EncoderTaskLogicInterface encoderTaskLogicInterface,
                                                       final DeviceLogicInterface deviceLogicInterface,
                                                       final EncoderTemplateLogicInterface encoderTemplateLogicInterface,
                                                       Device encoder,
                                                       final long streamServerId){
        PageBean pageBean = new PageBean(0,2,"o1.startTime","asc");
        List<EncoderTask> tasks = encoderTaskLogicInterface.getWaitingTasks(-1L,streamServerId,-1L,pageBean);
        if(tasks!=null&&tasks.size()>0){
            EncoderTask result= tasks.get(0);
            if(result!=null){
                Device server = result.getStreamServer();
                if(server==null){
                    server = (Device)CacheUtils.get(result.getStreamServerId(),"deviceCache",new DataInitWorker(){
                        public Object init(Object key,String cacheName){
                            return deviceLogicInterface.get((Long) key);
                        }
                    });

                }

                if(server!=null){
                    result.setEncoderId(encoder.getId());
                    result.setStatus(EncoderTaskLogicInterface.STATUS_RUNNING);
                    result.setProcess(0);
                    result.setStartTime(new Date());
                    EncoderTemplate template = result.getTemplate();
                    result = encoderTaskLogicInterface.save(result);
                    //保存后，template和streamServer都会被清空
                    if(template==null){
                        template = getCachedTempalte(encoderTemplateLogicInterface,result);
                    }
                    result.setTemplate(template);
                    result.setStreamServer(server);
                    encoderTaskLogicInterface.updateLog(result,"任务分配给了："+encoder.getName());
                    return result;
                }
            }
        }
        return null;
    }
    public Device getTranscoder(String ip,final DeviceLogicInterface deviceLogicInterface){
        return (Device) CacheUtils.get(ip,"devicesTranscodersByIp_Cache",new DataInitWorker(){
           public Object init(Object key,String cacheName){
               Device bean = new Device();
               bean.setIp(key.toString());
               bean.setType(DeviceLogicInterface.DEVICE_TYPE_ENCODER);
               List<Device> devices = deviceLogicInterface.search(bean,false);
               if(devices!=null&&devices.size()>0){
                   return devices.get(0);
               }
               return null;
           }
        });
    }

    public EncoderTemplate getCachedTempalte(final EncoderTemplateLogicInterface encoderTemplateLogicInterface,EncoderTask task){
        return (EncoderTemplate) CacheUtils.get(task.getTemplateId(), "templateCache", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return encoderTemplateLogicInterface.get((Long) key);
            }
        });
    }
    public String distributeMovie(final EncoderTask task,final EncoderTemplate threadUseVarOfTemplate,
                                  final String clientRequestServerIp,
                                  final DeviceLogicInterface deviceLogicInterface,
                                  final SynTaskLogicInterface synTaskLogicInterface,
                                  final SynFileLogicInterface synFileLogicInterface){
        String result;
        if(AppConfigurator.getInstance().getBoolConfig("system.encode.autoDistributeWhenFinished",false)){
            result = "根据配置的设置需要进行分发！";
            //这里是要考虑cspId的。在紧凑模式就不用考虑了
            List<Device> servers = deviceLogicInterface.getDevicesOfType(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD,
                    DeviceLogicInterface.DEVICE_ONLINE,-1L);
            Long serverId= task.getStreamServerId();
            if(serverId!=null){
                long fileId;
                Device sourceServer = deviceLogicInterface.get(serverId);
                //按道理讲，servers的数量应该在2个以上才需要分发。但是可能只有一个
                if(servers!=null&&servers.size()>=1){
                    result+="共有"+servers.size()+"个服务器要进行检查是否需要分发。";
                    SynFile synFile = new SynFile();
                    synFile.setStartTime(new Date());
                    String mediaUrl = task.getDesertFileName();
                    while(mediaUrl.startsWith("/")&&mediaUrl.length()>1){
                        mediaUrl=mediaUrl.substring(1);
                    }
                    String serverUrl = sourceServer.getUrl();
                    int i=0;
                    while(serverUrl.endsWith("/")&&i<5){
                        serverUrl = serverUrl.substring(0,serverUrl.length()-1);
                        i++;
                    }
                    mediaUrl = serverUrl+"/"+mediaUrl;
                    mediaUrl = mediaUrl.replace("serverIp",clientRequestServerIp);
                    if(threadUseVarOfTemplate!=null&&"m3u8".equalsIgnoreCase(threadUseVarOfTemplate.getFileFormat())){
                        mediaUrl+=".m3u8";
/*
                        if(!mediaUrl.endsWith(".m3u8")){
                        }
*/
                    }
                    if(mediaUrl.startsWith("http://hls.")){//如果是需要调度的连接，就直接用ip和端口替换
                        mediaUrl =StringUtils.getClearURL(mediaUrl);
                        if(!mediaUrl.startsWith("/")){
                            mediaUrl = "/"+mediaUrl;
                        }
                        mediaUrl="http://"+sourceServer.getIp()+":"+sourceServer.getMonitorPort()+mediaUrl;
                    }
                    synFile.setUrl(mediaUrl);
                    synFile.setName("分发转码后的文件："+task.getName());
                    synFile = synFileLogicInterface.save(synFile);
                    fileId = synFile.getId();
                    ServerMessager messager = new ServerMessager();
                    boolean willRmoteSynFile = true;
                    for(Device server:servers){
                        //自身，就不分发了
                        if(server.getId()==serverId){
                            continue;
                        }
                        SynTask synTask = new SynTask();
                        synTask.setStartPos(0L);
                        synTask.setEndPos(100L);
                        synTask.setStartTime(new Date());
                        synTask.setDeviceId(server.getId());
                        synTask.setSynStatus(SynTaskLogicInterface.STATUS_WAITING);
                        synTask.setSynFileId(fileId);
                        synTask = synTaskLogicInterface.save(synTask);
                        String serverIp = server.getIp();
                        if("".equals(serverIp)||null==serverIp||"serverIp".equals(serverIp)||"serverIP".equals(serverIp)){
                            serverIp="127.0.0.1";
                        }
                        String url = "http://"+serverIp+":"+server.getMonitorPort()+
                                "/interface/files.jsp?contentPropertyId="+task.getClipId()
                                +"&encoderTaskId="+task.getId();
                        String pushResult = messager.postToHost(url,"command=push&url="+synFile.getUrl()+"&rootPath="+
                                server.getLocalPath()+"&taskId="+synTask.getId(),"UTF-8");
                        result+="服务器分发完毕："+server.getName()+","+pushResult+"!";
                        willRmoteSynFile = false;
                        logger.debug("分发的结果："+pushResult);
                    }
                    if(willRmoteSynFile){
                        result+="没有做任何的分发，添加的同步文件信息删除！";
                        synFileLogicInterface.remove(synFile);
                    }else{
                        result+="分发过程完成！";
                    }
                }else{
                    result+=("该转码任务对应的分发服务器数量不够，不用分发："+task.getName()+",id="+task.getId());
                }
            }else{
                result+=("无法获取转码任务对应的服务器ID，这不科学："+task.getName()+",id="+task.getId());
            }
        }else{
            result="根据配置设定，不用自动分发！";
        }
        return result;
    }
%>