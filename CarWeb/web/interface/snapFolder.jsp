<%@ page import="java.io.File" %><%@ page import="com.fortune.util.FileType" %><%@ page import="com.fortune.util.FileUtils" %><%@ page import="com.fortune.util.MediaUtils" %><%@ page import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/23
  Time: 19:13
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String path = request.getParameter("path");
    String picPath = request.getParameter("picPath");
    if(path==null){
        path = "/home/fortune/movie/in/上线";
    }
    if(picPath==null){
        picPath="/poster/";
    }
    String command = request.getParameter("command");
    if("doSnap".equals(command)){
        snapFolder(path,path,application.getRealPath(picPath));
    }
%><html>
<head>
    <title>自动截图</title>
</head>
<body>
  <form action="?command=doSnap" method="post">
      <table>
          <tr>
              <td>截图源文件目录：</td>
              <td><input type="text" name="path" value="<%=path%>"></td>
          </tr>
          <tr>
              <td>图片保存目录：</td>
              <td><input type="text" name="picPath" value="<%=picPath%>"></td>
          </tr>
          <tr>
              <td colspan="2"><input type="submit" value="启动截图"></td>
          </tr>
      </table>

  </form>
</body>
</html>
<%!
    private Logger logger=Logger.getLogger("com.fortune.rms.jsp.snapFolder.jsp");
    public int snapFolder(String path,String rootDir,String picRoot){
        int result = 0;
        File folder = new File(path);
        if(folder.exists()){
            logger.debug("准备扫描目录："+folder.getAbsolutePath());
            if(folder.isDirectory()){
                File[] files = folder.listFiles();
                if(files!=null){
                    logger.debug("目录："+folder.getAbsolutePath()+"下共有文件："+files.length+"个");
                    for(File file:files){
                        String name =file.getName();
                        if(".".equals(name)||"..".equals(name)){
                            continue;
                        }
                        if(file.isDirectory()){
                            snapFolder(file.getAbsolutePath(),rootDir,picRoot);
                        }else{
                            result+=snapFile(file,rootDir,picRoot);
                        }
                    }
                }
            }else{
               logger.warn("这不是一个目录："+folder.getAbsolutePath());
            }
        }else{
            logger.error("目录不存在："+folder.getAbsolutePath());
        }
        return result;
    }
    public int snapFile(File file,String rootDir,String picRoot){
        if(file.exists()){
            FileType fileType = FileUtils.getFileType(file.getName());
            if(FileType.video.equals(fileType)){
                MediaUtils mu = new MediaUtils();
                int[] times = new int[]{10,30,50,80};
                for(int t:times){
                    String fileName = file.getAbsolutePath().substring(rootDir.length())+"."+t+".jpg";
                    try {
                        mu.snap(file.getAbsolutePath(),picRoot+"/"+fileName,t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return 0;
    }
%>