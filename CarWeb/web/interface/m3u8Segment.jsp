<%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="java.util.Date" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.util.ArrayList" %><%@ page import="java.io.*" %>
<%@ page import="java.util.concurrent.TimeoutException" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-24
  Time: 上午8:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command = request.getParameter("command");
    String result = "";
    final BatchWorker worker = BatchExecutor.getInstance().getWorker("scanContentsFileOnly",20);
    String workerLog = "";
    //String logFileName = "/logs/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".log";
    //final String exportSqlFileName = "/logs/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".sql";
    int count = 0;
    int finishedCount = 0;
    final String sqlHeader = "/logs/scanLogs/";
    AppConfigurator appConfigurator = AppConfigurator.getInstance();
    final String sqlResultDir = appConfigurator.getConfig("system.scan.sqlResultDir",application.getRealPath(sqlHeader));

    if("start".equals(command)){
        if(worker!=null){
            //boolean lastWorkTerminated = worker.isTerminated();
            //ExecutorService executorService = worker.getExecutor();
            List<Runnable> runnables = worker.getExecutor().shutdownNow();
            if(runnables!=null&&runnables.size()>0){
                result = "上次任务还没有结束,还有任务："+runnables.size();
                worker.reset();
                for(Runnable runnable:runnables){
                    worker.getExecutor().execute(runnable);
                }
            }else{
                worker.reset();
                List<File> files = FileUtils.listDir(sqlResultDir,"*.*",true);
                for(File file:files){
                    String filePath = file.getAbsolutePath();
/*
                    if(filePath.contains(webAppRoot)){
                        //如果在web工程目录里，必须检测，防止误删除所有文件
                        if(!filePath.contains("scanLogs")){
                            logger.warn("文件可能存在危险，放弃删除："+filePath);
                            continue;
                        }
                    }
*/
                    //必须检测，防止误删除所有文件，已经发生过这样的悲剧了
                    if(!filePath.contains("scanLogs")){
                        logger.warn("文件可能存在危险，放弃删除："+filePath);
                        continue;
                    }
                    if(file.delete()){

                    }
                }
                ServletContext servletContext = session.getServletContext();
                final ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)
                        SpringUtils.getBean("contentPropertyLogicInterface",servletContext);
                final PropertyLogicInterface  propertyLogicInterface = (PropertyLogicInterface)
                        SpringUtils.getBean("propertyLogicInterface",servletContext);
                final  ContentLogicInterface contentLogicInterface = (ContentLogicInterface)
                        SpringUtils.getBean("contentLogicInterface",servletContext);
                final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface)
                        SpringUtils.getBean("deviceLogicInterface",servletContext);
                final Property mediaLengthProperty = propertyLogicInterface.getByCode("MEDIA_LENGTH");
                final String[] propertyCodes = new String[]{"Media_Url_768k"};
                long[] spIds = new long[]{1};
                final List<Content> spContents = new ArrayList<Content>();
                for(long spId:spIds){
                    Content searchBean = new Content();
                    searchBean.setCspId(spId);
                    spContents.addAll(contentLogicInterface.search(searchBean));
                }

                result = "一共有" +spContents.size()+
                        "个媒体要处理！";
                logger.debug(result);
                Thread thread = new Thread(){
                    public void run(){
                        final List<ContentProperty> allClips = new ArrayList<ContentProperty>();
                        Long[] propertyIds = new Long[propertyCodes.length];
                        String ids = "";
                        for(int i=0,l=propertyCodes.length;i<l;i++){
                            String propertyCode=propertyCodes[i];
                            Property clipProperty = propertyLogicInterface.getByCode(propertyCode);
                            if(clipProperty!=null){
                                propertyIds[i]=clipProperty.getId();
                                ids+=propertyIds[i]+",";
                            }else{
                                //给个错误的值，让系统搜索不到数据
                                propertyIds[i]=1999999999L+i;
                            }


/*
                            ContentProperty clip = new ContentProperty();
                            clip.setPropertyId(clipProperty.getId());
                            clip.setContentId(-1L);
                            logger.debug("准备添加片段："+clipProperty.getCode()+","+clipProperty.getName());
                            List<ContentProperty> clips = contentPropertyLogicInterface.search(clip);
                            allClips.addAll(clips);
                            logger.debug("类型" +clipProperty.getName()+","+clipProperty.getCode()+"的媒体添加完毕，这个类型的媒体有"+clips.size()+"个,"+
                                    "累计添加片段"+allClips.size()+"个");
*/
                        }
                        logger.debug("获取到的propertyIds="+ids);
                        ContentProperty searchBean = new ContentProperty();
                        searchBean.setPropertyId(676496266L);
                        allClips.addAll(contentPropertyLogicInterface.search(searchBean));
                        int allCount = allClips.size();
                        if(allCount<=0){
                            logger.error("没有找到任何的片段！");
                        }else{
                            logger.debug("添加片段完毕，累计有片段"+allClips.size()+"个要进行扫描！");
                            for(final Content content:spContents){
                                //logger.debug("准备启动扫描：《"+content.getName()+"》的片段信息！");
                                final Device device = (Device)  CacheUtils.get(content.getDeviceId(), "deviceCache", new DataInitWorker() {
                                    public Object init(Object key, String cacheName) {
                                        try {
                                            return deviceLogicInterface.get((Long) key);
                                        } catch (Exception e) {
                                            return null;
                                        }
                                    }
                                });
                                if(device == null){
                                    logger.error("无法获取服务器数据，不能继续："+content.getDeviceId());
                                    continue;
                                }
                                //如果是直播，就跳过
                                if(device.getUrl().contains("live")){
                                    logger.debug("直播节目："+content.getName()+
                                            ",不用检查！");
                                    continue;
                                }
                                final List<ContentProperty> clips = new ArrayList<ContentProperty>();
                                Long contentId = content.getId();
                                if(contentId!=null){
                                    for(ContentProperty cp:allClips){
                                        Long cpContentId=  cp.getContentId();
                                        if(cpContentId!=null){
                                            if(cpContentId>0){
                                                if(contentId.equals(cpContentId))
                                                    clips.add(cp);
                                            }else{
                                                if(contentId.equals(0-cpContentId)){
                                                    clips.add(cp);
                                                }
                                            }
                                        }
                                    }
                                }
                                int clipCount = clips.size();
                                if(clipCount>0){
                                    logger.debug("准备启动扫描：《"+content.getName()+"》的片段信息，发现有："+clips.size()+"个片段！");

                                    final String sqlFileName = "/content_"+contentId+".sql";
                                    final String fullFileName = sqlResultDir+"/"+sqlFileName;
                                    BatchRunnable runnable = getRunnable(content, clips, device,mediaLengthProperty,
                                            contentPropertyLogicInterface,
                                            fullFileName);
                                    worker.execute(runnable);
                                }else{
                                    logger.warn("影片《" +content.getName()+
                                            "》没有任何片段信息");
                                }
                            }
                        }
                        worker.shutdown();
                    }
                };
                thread.start();
            }
        }
    }else if("saveLog".equals(command)){
        FileUtils.writeNew(sqlResultDir,worker.getLog());
    }else if("stopAll".equals(command)){
        List<Runnable> runnables = worker.getExecutor().shutdownNow();
        if(runnables!=null){
            result +="已经停止执行新的任务，队列中还有："+runnables.size()+"个尚未执行！";
        }else{
            result+="没有队列等待执行！";
        }
    }else if("combinFiles".equals(command)){
        response.setContentType("unknown");
        response.addHeader("Content-Disposition", "filename=\"updateStatus" + StringUtils.date2string(new Date(),"yyyyMMddHHmmss") + ".sql\"");
        List<File> files = FileUtils.listDir(sqlResultDir,"*.*",true);
        int i=0;
        for(File file:files){
            i++;
            out.print(readFileInfo(file.getAbsolutePath(), "UTF-8"));
            if(i%100==0){
                out.println("\r\ncommit;\r\n");
            }
        }
        out.println("\r\ncommit;\r\n");
        return;
    }

    if(worker!=null){
        String log = worker.getLog();
        int logLength = log.length();
        if(logLength>1000){
            workerLog = log.substring(logLength-1000);
        }else{
            workerLog = log;
        }
        count = worker.getCount();
        finishedCount = worker.getFinishedCount();
    }
%><html>
<head>
    <title>启动文件重新索引</title>
    <script type="text/javascript">
        function willGotoStop(){
            if(confirm("停止操作比较危险，您确认停止所有任务吗？")){
                window.location.href="?command=stopAll";
            }
        }
    </script>
</head>
<body>
<%
    if(!"start".equals(command)){
%><p><a href="?command=start">重新启动扫描</a></p><%
    }else {
%><p>处理结果：<%=result%></p><%
    }
    int totalLength =640;
    int finishedLength =0;
    if(count>0){
        finishedLength = totalLength * finishedCount/count;
    }
    int waitingLength = totalLength - finishedLength;
    if(!"saveLog".equals(command)){
%><p><a href="?command=saveLog">生成日志文件</a></p><%
}else{
%><%
    }
%><p><a href="?command=combinFiles">下载SQL文件</a></p><p><a href="#" onclick="willGotoStop()">停止并清空执行队列</a></p><table width="<%=totalLength%>" border="1" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2"><%=finishedCount%>/<%=count%>&nbsp;&nbsp;&nbsp;&nbsp;<a href="?command=list&time=<%=System.currentTimeMillis()%>">刷新</a></td>
    </tr>
    <tr bgcolor="red">
        <td bgcolor="#adff2f" width="<%=finishedLength%>"></td>
        <td width="<%=waitingLength%>">&nbsp;</td>
    </tr>
</table>
<label for="logs">日志信息：</label><br/>
<textarea id="logs" cols="80" rows="80" style="width:<%=totalLength%>px;height:360px;"><%=workerLog%></textarea>
</body>
</html><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.interface.scanMediaFiles.jsp");
    public BatchRunnable getRunnable(final Content content,final List<ContentProperty> clips,final Device device,
                                     final Property mediaLengthProperty,
                                     final ContentPropertyLogicInterface contentPropertyLogicInterface,
                                     final String outputFileName){
        final String msgHeader = content.getName()+" ";
        return new BatchRunnable() {
            int resultCode=-1;
            String log = StringUtils.date2string(new Date())+" - " +msgHeader+
                    "尚未启动";
            Date startTime=new Date();
            Date stopTime = new Date();
            public void shutdownNow(){

            }
            public long getDuration(){
                return stopTime.getTime()-startTime.getTime();
            }
            public String getLogs() {
                return log;
            }
            public int getResultCode() {
                return resultCode;
            }

            public void afterFinished() {
            }

            public void beforeStart() {
                startTime = new Date();
                log ="";//"\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+"已经启动";
            }
            public void run() {
                log = "";
                int mediaLength = 0;
                int outputLines = 0;
                for(ContentProperty clip:clips){
                    File mediaFile = new File(device.getLocalPath()+"/"+clip.getStringValue());
                    if(mediaFile.exists()){
                        if((mediaFile.length()>1024*128)){//至少大于128k
                            String savePathRoot = "/home/fortune/m3u8/";
                            String mediaFilePath =FileUtils.extractFileName(clip.getStringValue(),"/");
                            int p = mediaFilePath.lastIndexOf(".");
                            if(p>0){
                                mediaFilePath = mediaFilePath.substring(0,p);
                            }
                            mediaFilePath +="Segment";
                            String resultPath = savePathRoot+"/"+FileUtils.extractFilePath(clip.getStringValue(),"/")+
                                    "/"+mediaFilePath;

                            File resultPathFile = new File(resultPath);
                            if(!resultPathFile.exists()){
                                resultPathFile.mkdirs();
                            }
                            File m3u8File = new File(savePathRoot+"/"+clip.getStringValue()+".m3u8");
                            File resultTsFile = new File(resultPath+"/"+mediaFilePath);

                            String cmdLine = "/usr/local/bin/ffmpeg -re -i \"" + mediaFile.getAbsolutePath()+
                                    "\" -codec copy -map 0 -f segment" +
                                    " -segment_list \""+m3u8File.getAbsolutePath()+"\"" +
                                    " -segment_time 10 -segment_list_flags +live" +
                                    " -segment_list_entry_prefix \"" +mediaFilePath+"/\""+
                                    " -bsf h264_mp4toannexb \"" +resultTsFile.getAbsolutePath()+"%04d.ts\"";
                            CommandRunner runner = new CommandRunner();
                            try {
                                logger.debug("准备执行："+cmdLine);
                                log+="\r\n"+cmdLine;
                                runner.runCommand(cmdLine, resultPath, 72000000, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                                log+="\nIO Exception+"+e.getMessage();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                                log+="\nTimeOut Exception+"+e.getMessage();
                            } catch (InterruptedException e) {
                                log+="\nInterruptedException"+e.getMessage();
                                e.printStackTrace();
                            }
                            log+="\n"+cmdLine;
                            System.out.println(cmdLine);
                        }else{
                            resultCode = 505;
                            log+="\r\n--文件太小：媒体《"+content.getName()+",("+clip.getName()+",clipId="+clip.getId()+")》，大小："+
                                    StringUtils.formatBytes(mediaFile.length())+",文件名："+mediaFile.getAbsolutePath();
                        }
                    }else{
                        resultCode = 404;
                        log+="\r\n文件不存在"+mediaFile.getAbsolutePath();
                    }
                    clip.setExtraInt((long)resultCode);
                }
                logger.debug(log);
                stopTime = new Date();
            }
        };
    }

    public String readFileInfo(String file,String encode) {
        String s;
        StringBuilder sb = new StringBuilder();
        File f = new File(file);
        if (f.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f),encode));

                while ((s = br.readLine()) != null) {
                    s = s.trim();
                    if(s.startsWith("--")||"".equals(s)){
                        continue;
                    }
                    sb.append(s).append("\r\n");
                }
                //logger.debug(sb);
                return sb.toString();
                //return result.substring(0,result.length()-2);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            logger.error("文件不存在!");
            return "";
        }

    }


%>