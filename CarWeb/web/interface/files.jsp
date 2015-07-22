<%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.server.message.ServerMessager" %><%@ page
        import="net.sf.json.JSONObject" %><%@ page
        import="net.sf.json.JSONArray" %><%@ page
        import="com.fortune.util.net.URLEncoder" %><%@ page
        import="java.io.UnsupportedEncodingException" %><%@ page import="java.net.URLDecoder" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.common.business.security.model.Admin" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.SystemLog" %><%@ page
        import="java.util.*" %><%@ page import="com.fortune.server.protocol.SaveM3U8" %><%@ page
        import="com.fortune.rms.business.syn.model.SynTask" %><%@ page
        import="com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface" %><%@ page import="java.io.File" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/8
  Time: 15:25
  列文件，分发文件，删除文件等接口。
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command = request.getParameter("command");
    String remoteAddr = request.getRemoteAddr();
    if(command==null){
        command="";
    }
    Map<String,Object> result =new HashMap<String,Object>();
    boolean debugMode = "true".equals(request.getParameter("debugMode"));
    String path = request.getParameter("filePath");
    if(path==null){
        path="/";
    }
    int repeatTimes=  0;
    while(path.contains("%")){
        path = URLDecoder.decode(path,"UTF-8");
        repeatTimes++;
        if(repeatTimes>5){
            break;
        }
    }
    String filter = request.getParameter("filter");
    if(filter==null||filter.equals("null")){
        filter="*.*";
    }
    logger.debug("尝试列取："+path+","+filter+",queryString="+request.getQueryString());
    PageBean pageBean = getPageBean(request);
    if("list".equals(command)) {
        logger.debug("正在执行命令：" + command);
        String orderBy = pageBean.getOrderBy();
        if(orderBy!=null){
            int p = orderBy.lastIndexOf(".");
            if(p>=0){
                orderBy = orderBy.substring(p+1);
                pageBean.setOrderBy(orderBy);
            }
        }
        long deviceId = StringUtils.string2long(request.getParameter("deviceId"),-1);
        long cspId  = StringUtils.string2long(request.getParameter("cspId"),-1);
        List<SimpleFileInfo> files = listFiles(deviceId, cspId, path, filter, pageBean, false);
        result.put("total", pageBean.getRowCount());
        result.put("objs", files);
    }else if("getServerLocalPath".equals(command)){
        DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface", request.getSession().getServletContext());
        Long cspId = StringUtils.string2long(request.getParameter("cspId"),-1);
        if(cspId<=0){
            Admin admin = (Admin) request.getSession().getAttribute(Constants.SESSION_ADMIN);
            if(admin!=null){
                if(admin.getCspId()!=null){
                    cspId = admin.getCspId().longValue();
                }
            }
        }
        result.put("rootPath",deviceLogicInterface.getServerLocalPath(StringUtils.string2long(request.getParameter("deviceId"),-1),cspId));
    }else if("remote".equals(command)){
        logger.debug("正在执行命令："+command);
        String ip =request.getParameter("ip");
        if(ip==null){
            ip=request.getServerName();
        }
        List<SimpleFileInfo> files = listRemoteFiles(ip,StringUtils.string2int(request.getParameter("port"),
                request.getServerPort()), path, filter, pageBean, false);
        result.put("total", pageBean.getRowCount());
        result.put("objs", files);
    }else if("push".equals(command)) {
        String url = request.getParameter("url");
        String rootPath = request.getParameter("rootPath");
        if (rootPath == null) {
            rootPath = AppConfigurator.getInstance().getConfig("system.movie.pathName", "/home/fortune/movie/");
        }
        //截掉rootPath尾部的in和out，否则内外服务器在同步过程中会有问题
        rootPath = cutTail(rootPath,new String[]{"/in","/in/","/out","/out/"});
        if (url != null) {
            int i = 0;
            while (url.contains("%")) {
                i++;
                url = URLDecoder.decode(url, "UTF-8");
                if (i > 5) {
                    break;
                }
            }
            String taskId = request.getParameter("taskId");
            if(taskId!=null){
                url+="?taskId="+taskId+"&"+request.getQueryString();
            }
            if(url.contains(".m3u8")){
                SaveM3U8 saveM3U8 = new SaveM3U8(url, rootPath);
                saveM3U8.start();
            }else{

            }
        }
    }else if("reportPushProcess".equals(command)){
        long taskId = StringUtils.string2long(request.getParameter("taskId"),-1);
        SynTaskLogicInterface synTaskLogicInterface = (SynTaskLogicInterface)SpringUtils.getBean("synTaskLogicInterface",session.getServletContext());
        SynTask task;
        try {
            Float duration = Float.parseFloat(request.getParameter("allDuration"));
            Float curPos = Float.parseFloat(request.getParameter("currentPos"));
            boolean finished = StringUtils.string2bool(request.getParameter("finished"));
            if(taskId>0){
                task = synTaskLogicInterface.get(taskId);
                task.setEndPos(duration.longValue());
                task.setStartPos(curPos.longValue());
                if(finished){
                    task.setSynStatus(SynTaskLogicInterface.STATUS_FINISHED);
                }else{
                    long taskStatus = StringUtils.string2long(request.getParameter("taskStatus"),-1);
                    if(taskStatus>=0){
                        task.setSynStatus(taskStatus);
                    }
                }
                synTaskLogicInterface.save(task);
                result.put("success",true);
            }else{
                result.put("success",false);
                result.put("message","输入的TASKID不正确："+taskId+
                        ",allDuration="+duration+",curPos="+curPos+",finished="+finished);
            }
        } catch (Exception e) {
            logger.error("尝试保存下载进度时发生异常："+e.getMessage());
            e.printStackTrace();
        }
    }else if("uploadLog".equals(command)){
        String ip = request.getParameter("ip");
        String logs = request.getParameter("logs");
        long adminId = StringUtils.string2int(request.getParameter("adminId"),-1);
        if(ip!=null&&adminId>0&&logs!=null){
            int i=0;
            while(logs.contains("%")){
                logs = URLDecoder.decode(logs,"UTF-8");
                i++;
                if(i>5){
                    break;
                }
            }
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface)
                    SpringUtils.getBean("systemLogLogicInterface",
                            session.getServletContext());
            systemLogLogicInterface.save(new SystemLog(-1,"com.fortune.rms.web.file.FileUpload",adminId,ip,new Date(),logs));
            result.put("success",true);
        }else{
            result.put("success",false);
            result.put("message","parameterError:ip="+ip+",adminId="+adminId+",logs="+logs);
        }
    }else{
        result.put("result","未知的命令："+command);
    }
    if (!debugMode) {
        out.print(JsonUtils.getJsonString(result));
        return;
    }
%><html>
<head>
    <title></title>
</head>
<body>
    <%=JsonUtils.getJsonString(result)%>
</body>
</html>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.files.jsp");
    public PageBean getPageBean(HttpServletRequest request){
        String startStr = request.getParameter("start");
        String pageNoStr = request.getParameter("pageNo");
        String limitStr = request.getParameter("limit");
        if(limitStr==null){
            limitStr = request.getParameter("pageSize");
            if(limitStr==null){
                request.getParameter("pageBean.pageSize");
            }
        }
        int limit = StringUtils.string2int(limitStr,10);
        int start = 0;
        if(startStr!=null){
            start = StringUtils.string2int(startStr,0);
        }else{
            if(pageNoStr==null){
                pageNoStr = request.getParameter("pageBean.pageNo");
            }
            start = StringUtils.string2int(pageNoStr,0);
            if(start>0){
                start = (start-1)*limit;
            }
        }
        String order = request.getParameter("order");
        if(order==null){
            order = request.getParameter("pageBean.orderBy");
            if(order==null){
                request.getParameter("orderBy");
            }
        }
        String dir = request.getParameter("dir");
        if(dir==null){
            dir = request.getParameter("pageBean.dir");
        }
        return new PageBean((start+limit)/limit,limit,order,dir);
    }

    private String getServerMessage(String parameters){
        ServerMessager messager = new ServerMessager();
        return messager.postToHost(AppConfigurator.getInstance().getConfig("system.movie.fileInterfaceUrl",
                "http://127.0.0.1/interface/files.jsp"),
                parameters,"UTF-8");

    }
    private String getServerLocalPath(long serverId,long cspId){
        String result = null;
        if(serverId>0){
            String serverData = getServerMessage("command=getServerLocalPath&deviceId=" +
                    serverId + "&cspId=" + cspId);
            if(serverData!=null&&!"".equals(serverData.trim())){
                JSONObject jsonObject = JsonUtils.getJsonObj(serverData);
                result = jsonObject.getString("rootPath");
            }
        }
        if(result==null||"".equals(result.trim())){
            return AppConfigurator.getInstance().getConfig("system.movie.pathName","/home/fortune/movie/");
        }
        return  result;

    }
    public List<File> listDir(String dir,String extName,boolean withSubDir){
        List<File> result = new ArrayList<File>();
        if (dir == null) {
            logger.error("目录输入为空");
            return result;
        } else {
            dir = dir.replaceAll("//", "/");
            if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                dir = dir.replace("/","\\");
            }
        }

        String[] dirs = dir.split(":");
        if(dirs.length <= 1&&!dir.startsWith("\\\\")) {
            dir = "/"+dir;
        }

        File dirFile = new File(FileUtils.translateStringToNativeFileSystem(dir));
        logger.debug("列目录："+dir+"->" + dirFile.getAbsolutePath());
        if (dirFile.exists()) {
            File[] filesArray = dirFile.listFiles(new ExtFileNameFilter(extName));
            if (filesArray != null) {
                for(File file:filesArray){
                    if(withSubDir && file.isDirectory()){
                        result.addAll(listDir(file.getAbsolutePath(),extName,withSubDir));
                    }else{
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public List<SimpleFileInfo> listFiles(String dir, String extName, String orderBy, String orderDir,boolean withSubDir) {
        if(dir==null){
            dir="";
        }
        if(!dir.startsWith("\\\\")){

            dir = dir.replace("\\","/");
            dir = dir.replace("//","/");
            dir = dir.replace("//","/");
            dir = dir.replace("//","/");
        }
        List<SimpleFileInfo> result = new ArrayList<SimpleFileInfo>();
        File dirFile = new File(dir);
        logger.debug("准备列目录："+dir+"->"+dirFile.getAbsolutePath());
        dir = dirFile.getAbsolutePath();
        List<File> filesArray = listDir(dir,extName,withSubDir);
        if (filesArray != null) {
            for (File file : filesArray) {
                String fileName =FileUtils.translateString( file.getAbsolutePath());

/*
                String[] enc = new String[]{"UTF-8","ISO-8859-1","GBK",null};
                for(String s:enc){
                    byte[] bs;
                    if(s!=null){
                        try {
                            bs = fileName.getBytes(s);
                        } catch (UnsupportedEncodingException e) {
                            logger.error("不支持内码"+s);
                            bs = fileName.getBytes();
                        }
                    }else{
                        bs = fileName.getBytes();
                    }
                    for(String d:enc){
                        try {
                            if(d!=null){
                                fileName = new String(bs,d);
                            }else{
                                fileName = new String(bs);
                            }
                        } catch (UnsupportedEncodingException e) {
                            logger.error("转码失败，不支持到目标内码："+d);
                            fileName = file.getAbsolutePath();
                        }
                        logger.debug("发现文件："+fileName+",s="+s+",d="+d);
                    }
                }
//*/
                fileName = fileName.replace("\\","/");
                fileName = fileName.substring(dir.length());
                while(fileName.startsWith("/")&&fileName.length()>1){
                    fileName=fileName.substring(1);
                }
                result.add(new SimpleFileInfo(fileName, file.length(), new Date(file.lastModified()), file.isDirectory(), FileUtils.getFileType(file.getName())));
            }
        }
        if (orderBy != null && !"".equals(orderBy.trim())) {
            logger.debug("按照目录和文件分开，单独排序");
            List<SimpleFileInfo> dirs = new ArrayList<SimpleFileInfo>();
            List<SimpleFileInfo> files = new ArrayList<SimpleFileInfo>();
            for (SimpleFileInfo file : result) {
                if (!file.isDirectory()) {
                    files.add(file);
                } else {
                    dirs.add(file);
                }
            }
            files = SortUtils.sortArray(files, orderBy, orderDir);
            dirs = SortUtils.sortArray(dirs, orderBy, orderDir);
            result.clear();
            result.addAll(dirs);
            result.addAll(files);
        } else {
            logger.debug("没有排序信息，直接返回");
        }
        return result;
    }


    public List<SimpleFileInfo> listFiles(long serverId,long cspId, String path, String fileNameRegEx, PageBean pageBean,boolean withSubDir) {
        List<SimpleFileInfo> results = new ArrayList<SimpleFileInfo>();
        if(path==null){
            path = "";
        }
        path = getServerLocalPath(serverId,cspId)+"/" + path;
        if(fileNameRegEx==null||"".equals(fileNameRegEx)){
            fileNameRegEx = "*";
        }
        if(pageBean==null){
            pageBean = new PageBean();
            pageBean.setPageSize(Integer.MAX_VALUE);
        }
        String orderBy = pageBean.getOrderBy();
        if(orderBy==null||"".equals(orderBy.trim())||"null".equals(orderBy)){
            orderBy = "name";
            pageBean.setOrderDir("asc");
        }
        if(orderBy.startsWith("o1.")){
            orderBy = orderBy.substring(3);
        }
        List<SimpleFileInfo> tempResults = listFiles(path, fileNameRegEx, orderBy, pageBean.getOrderDir(), withSubDir);

        pageBean.setRowCount(tempResults.size());
        logger.debug("列取了目录'" +path+
                "'下的'" + fileNameRegEx+
                "'数据，条目为："+pageBean.getRowCount()+",排序："+pageBean.getOrderBy()+","+pageBean.getOrderDir()+"," +
                "start:"+pageBean.getStartRow()+","+pageBean.getPageSize());
        for(int i=pageBean.getStartRow();i<pageBean.getStartRow()+pageBean.getPageSize();i++){
            if(i<0){
                i=0;
            }
            if(i>=tempResults.size()){
                break;
            }
            SimpleFileInfo fileInfo = tempResults.get(i);
            String fileName =path+"/"+fileInfo.getName();
            logger.debug("尝试获取媒体的详细信息："+fileName);
            FileUtils.setFileMediaInfo(fileName,fileInfo);
            results.add(fileInfo);
        }
        return results;
    }
    public List<SimpleFileInfo> listRemoteFiles(String ip,int port,String path,String fileNameRegEx,PageBean pageBean,boolean withSubDir){
        List<SimpleFileInfo> results = new ArrayList<SimpleFileInfo>();
        ServerMessager messager = new ServerMessager();
        try {
            path = URLEncoder.encode(URLEncoder.encode(path,"UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String remoteResult = messager.postToHost("http://"+ip+":"+port+"/interface/files.jsp?command=list&filePath="+path+"&filter="
                        +fileNameRegEx+"&order="+pageBean.getOrderBy()+"&dir="+pageBean.getOrderDir()+
                        "&start="+pageBean.getStartRow()+"&limit="+pageBean.getPageSize(),null,"UTF-8");
        logger.debug("服务器端返回："+remoteResult);
        if(remoteResult!=null&&!"".equals(remoteResult)){
            JSONObject result = JsonUtils.getJsonObj(remoteResult);
            int total = result.getInt("total");
            if(total >= 0){
                pageBean.setRowCount(total);
                logger.debug("共有数据："+total+"个");
                JSONArray objs = result.getJSONArray("objs");
                if(objs!=null){
                    for(int i=0,l=objs.size();i<l;i++){
                        JSONObject jsonObject = objs.getJSONObject(i);
                        try {
                            logger.debug("尝试恢复："+jsonObject.toString());
                            SimpleFileInfo file =(SimpleFileInfo) JSONObject.toBean(jsonObject,SimpleFileInfo.class);
                            if(file!=null){
                                results.add(file);
                            }
                        } catch (Exception e) {
                            logger.error("格式化数据时发生了异常："+e.getMessage()+",JSON数据是："+jsonObject.toString());
                            e.printStackTrace();
                        }
                    }
                }else{
                    logger.error("没有获取到任务");
                }
            }else{
                logger.debug("服务器" +ip+":"+port+
                        "发来的数据："+remoteResult+",没有任何文件");
            }
        }else{
            logger.error("无法获取远端数据");
        }
        return results;
    }
    public String cutTail(String str,String[] tails){
        if(tails!=null&&str!=null){
            for(String tail:tails){
                if(str.endsWith(tail)){
                    return str.substring(0,str.length()-tail.length());
                }
            }
        }
        return str;
    }
%>