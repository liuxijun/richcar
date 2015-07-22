<%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.util.JsonUtils" %><%@ page
        import="com.fortune.util.CommandRunner" %><%@ page import="com.fortune.util.AppConfigurator" %><%@ page import="java.io.*" %><%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 14-1-17
  Time: 下午2:04
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/xml;charset=UTF-8" language="java" %><%
    loggers = "";
    boolean jsonFormat = "true".equals(request.getParameter("jsonFormat"));
    boolean checkCpu = ("true".equals(request.getParameter("checkCpu"))|| AppConfigurator.getInstance().getBoolConfig("system.monitor.checkCPU",true));
    Map<String,Object> result = new HashMap<String,Object>();
    List<Map<String,Object>> disks = getDiskInfo();
    double  cpu = checkCpu?getCpu():-1.0f;
    result.put("disks",disks);
    if(checkCpu){
        result.put("cpu",cpu);
    }
    if(jsonFormat){
        response.setContentType("text/html");
        String id = request.getParameter("id");
        if(id==null){
            id = request.getServerName().replace('.','_').replace(':','_');
        }
        result.put("id",id);
        String randomVarId = "jsonData_"+id+"_"+Math.round(Math.random()*100000);
        String callback = request.getParameter("callback");
        String jsonValue = JsonUtils.getJsonString(result);
        if(callback!=null){
            out.print("var " +randomVarId+
                    " = ");
        }
        out.print(jsonValue);
        if(callback!=null){
            out.print(";\n"+callback+"("+randomVarId+");\n");
        }
        return;
    }else{

    }
%><?xml version="1.0" encoding="UTF-8"?>
<body>
    <disk-info>
        <%
            for(Map<String,Object> disk:disks){
        %>     <disk name="<%=disk.get("name")%>" total="<%=disk.get("total")%>" free="<%=disk.get("free")%>" used ="<%=disk.get("used")%>"/>
        <%
            }
        %>
    </disk-info>
    <cpu-info used="<%=cpu%>"/>
    <sysInfo clientCount="<%=getClientCount(request.getServerPort())%>"/>
</body><%
    if("true".equals(request.getParameter("debug"))){
%><!--<%=request.getRemoteAddr()%>调试信息：
<%=loggers%>
--><%
    }
%><%!
    //获取本机计算机的磁盘信息
    private static final int CPUTIME = 100;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private String loggers = "";
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.getServerMessage.jsp");
    public List<Map<String,Object>> getDiskInfo(){
        if(isWindows()){
            return getDiskInfoForWin();
        }else{
            return getDiskInfoForLinux();
        }
    }
    public List<Map<String,Object>> getDiskInfoForWin(){
        //1.获得所有分区--------静态方法listRoots();
        File[] disks = File.listRoots();
        long freeSpace;
        long totalSpace;
        List<Map<String,Object>> diskList = new ArrayList<Map<String, Object>>(disks.length);
        loggers+=("发现磁盘："+disks.length+"个\n");
        for (File disk : disks) {
            //2.用isDirectory()判断是否为分区（有可能是设备文件）
            logger.debug("扫描信息："+disk.getAbsolutePath());
            if (disk.isDirectory()) {
                //3.用getTotalSpace()获得分区的总空间（单位：字节）
                totalSpace = disk.getTotalSpace();
                freeSpace = disk.getFreeSpace();
                Map<String,Object> diskInfo = new HashMap<String,Object>();
                diskInfo.put("name", StringUtils.escapeXMLTags(disk.getAbsolutePath()));
                diskInfo.put("total",totalSpace);
                diskInfo.put("free",freeSpace);
                diskInfo.put("used",totalSpace-freeSpace);
                diskList.add(diskInfo);
//            taskInfo+="<file>"+disk.getAbsolutePath()+"</file><all>"+totalSpace+"</all><allowance>"+freeSpace+"</allowance><used>"+usableSpace+"<used>";
            }else{
                loggers+=("这不是磁盘目录："+disk.getAbsolutePath()+"\n");
            }
        }
        return diskList;
    }
    public static String exec(String cmd) {
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getClientCount(int serverPort){
       // CommandRunner runner = new CommandRunner();
        String commandLine = AppConfigurator.getInstance().getConfig("system.getClientCountCmdLine","/home/fortune/bin/getClientCount.sh");
        String runResult =exec(commandLine);
        //String runResult =runner.run("/bin/netstat -nap | /bin/grep  | /bin/grep -v 8009 | /bin/grep ESTA | /bin/grep -c " +port,"/home/fortune/bin");
        logger.debug("获取的命令行：" +commandLine+
                "，结果数据："+runResult);
        if(runResult!=null){
            runResult = runResult.trim();
        }
        int result = StringUtils.string2int(runResult,-1);

        if(result<=0){
            result = 0;
        }else{
            //如果是80端口访问的，就会也占用一个并发
            if(serverPort==80){
                result --;
            }
        }
        return result;
    }

    public List<Map<String,Object>> getDiskInfoForLinux(){
        List<Map<String,Object>> diskList = new ArrayList<Map<String, Object>>();
        CommandRunner runner = new CommandRunner();
        String result = runner.run("df -k","/");
        loggers+=("df运行结果：\n"+result+"\n");
        String[] disks = result.split("\n");
        //从第二行开始遍历
        List<String> data = new ArrayList<String>(0);
        for(int i=1,l=disks.length;i<l;i++){
            String diskInfo = disks[i];
            if(data.size()<=1){
                data.addAll(getALine(diskInfo));
            }else{
            }
            if(data.size()==1){
                continue;
            }

            loggers+="扫描行："+diskInfo+"返回：" +data.size()+"\r\n";
            if(data.size()>=6){
                long total = StringUtils.string2long(data.get(1),-1)*1024L;
                long used = StringUtils.string2long(data.get(2),-1)*1024L;
                long free = StringUtils.string2long(data.get(3),-1)*1024L;
                if(total>0){
                    Map<String,Object> disk = new HashMap<String, Object>();
                    disk.put("name",data.get(5));
                    disk.put("total",total);
                    disk.put("free",free);
                    disk.put("used",used);
                    diskList.add(disk);
                }
            }
            data.clear();
        }
        return diskList;
    }
    public List<String> getALine(String line){
        List<String> result = new ArrayList<String>();
        if(line!=null){
            line = line.trim();
            if(!"".equals(line)){
                int i=0,l=line.length();
                String col = "";
                while(i<l){
                    char ch = line.charAt(i);
                    if(ch!='\n'&&ch!='\r'&&ch!='\t'&&ch!=' '&&ch!='\b'){
                        col+=ch;
                    }else{
                        if(!"".equals(col)){
                            result.add(col);
                            col = "";
                        }
                    }
                    i++;
                }
                if(!"".equals(col)){
                    result.add(col);
                }
            }
        }
        return result;
    }
    public boolean isWindows(){
        String osName = System.getProperties().getProperty("os.name");
        if (osName == null) {
            osName = "";
        }
        return osName.toLowerCase().contains("windows");
    }
    public double getCpu(){
        if(isWindows()){
            return getCpuRatioForWindows();
        }else{
            try {
                return getLinuxCpuUsage();
            } catch (Exception e) {
                return 0.0;
            }
        }
    }
    /**
     * 获得CPU使用率.
     */
    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idleTime = c1[0] - c0[0];
                long busyTime = c1[1] - c0[1];
                return (PERCENT * (busyTime) / (busyTime + idleTime));
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    /**
     * 读取CPU信息.win
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capIdx = line.indexOf("Caption");
            int cmdIdx = line.indexOf("CommandLine");
            int rocIdx = line.indexOf("ReadOperationCount");
            int umtIdx = line.indexOf("UserModeTime");
            int kmtIdx = line.indexOf("KernelModeTime");
            int wocIdx = line.indexOf("WriteOperationCount");
            long idleTime = 0;
            long kneTime = 0;
            long userTime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocIdx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = substring(line, capIdx, cmdIdx - 1).trim();
                String cmd = substring(line, cmdIdx, kmtIdx - 1).trim();
                if (cmd.contains("wmic.exe")) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idleTime += Long.valueOf(
                            substring(line, kmtIdx, rocIdx - 1).trim());
                    idleTime += Long.valueOf(
                            substring(line, umtIdx, wocIdx - 1).trim());
                    continue;
                }

                kneTime += Long.valueOf(
                        substring(line, kmtIdx, rocIdx - 1).trim());
                userTime += Long.valueOf(
                        substring(line, umtIdx, wocIdx - 1).trim());
            }
            retn[0] = idleTime;
            retn[1] = kneTime + userTime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }
    /**
     * 读取CPU信息.Linux
     */
    public static double getLinuxCpuUsage() throws Exception {
        double cpuUsed = 0.0;
        double idleUsed;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("top -b -n 1");// call "top" command in linux
        BufferedReader in;
        {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str;
            int lineCount = 0;
            while ((str = in.readLine()) != null) {
                lineCount++;
                if (lineCount == 3) {
                    String[] s = str.split("%");
                    String idlestr = s[3];
                    String idlestr1[] = idlestr.split(" ");
                    idleUsed = Double.parseDouble(idlestr1[idlestr1.length-1]);
                    cpuUsed  = Math.round(100-idleUsed);
                    break;
                }
           }
        }
        return cpuUsed;
    }
%>