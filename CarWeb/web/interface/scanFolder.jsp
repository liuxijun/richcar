<%@ page import="java.io.File" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.util.XmlUtils" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="java.io.FileWriter" %><%@ page
        import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/5/12
  Time: 7:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    String format = request.getParameter("format");
    if(format==null){
        format = "insert into files(idx,path,file_size,modify_date) values(%idx%,'%path%',%size%," +
                "to_date('%date%','YYYY-MM-DD HH24:MI:SS')); ";
    }
    int startIdx = StringUtils.string2int(request.getParameter("startIdx"),1);
    String path = request.getParameter("path");
    List<File> files = null;
    if(path!=null&&!"".equals(path.trim())){
        path = path.trim();
        files = listDir(new File(path));
    }
    String sourceEnc = request.getParameter("sourceEnc");
    String dstEnc = request.getParameter("dstEnc");
    if(path==null){
        path = "\\\\192.168.10.153\\pool1_vod\\cntv";
    }
    String xmlFileName =application.getRealPath("/files_" +path.replace('\\','_').replace('/','_')
            .replace(' ', '_').replace(':','_').replace('?','_').replace('*','_')+
            ".xml");
    File xmlFile = new File(xmlFileName);
    if(files==null){
        if(xmlFile.exists()&&xmlFile.length()>0){
            Element root = null;
            try {
                root  = XmlUtils.getRootFromXmlFile(xmlFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(root!=null){
                files = new ArrayList<File>();
                List<Node> xmlFiles = root.selectNodes("file");
                if(xmlFiles!=null){
                    for(Node file:xmlFiles){
                        String fileName = XmlUtils.getValue(file,"@name",null);
                        if(fileName!=null){
                            File f = new File(fileName);
                            files.add(f);
                        }
                    }
                }
            }
        }
    }else{
        FileWriter fw = new FileWriter(xmlFile);
        fw.write("<?xml version=\"1.0\"?>\r\n<files>\r\n");
        for(File f:files){
            String name = f.getAbsolutePath();
            byte[] src;
            if(sourceEnc!=null&&!"".equals(sourceEnc)){
                src = name.getBytes(sourceEnc);
            }else{
                src = name.getBytes();
            }
            String dst ;
            if(dstEnc!=null&&!"".equals(dstEnc)){
                dst = new String(src,dstEnc);
            }else{
                dst = new String(src);
            }
            fw.write("\t<file path=\""+dst+"\" size=\""+ f.length()+"\" date=\""+StringUtils.date2string(f.lastModified())+"\"/>\r\n");
        }
        fw.write("</files>");
        fw.close();
    }
%><html>
<head>
    <title>扫描文件</title>
</head>
<body>
<form action="?" method="post">
    <table>
        <tr>
            <td><label for="path">目录：</label></td>
            <td><input type="text" name="path" value="<%=path%>" id="path"></td>
        </tr>
        <tr>
            <td><label for="format">格式：</label></td>
            <td><input type="text" name="format" value="<%=format%>" id="format"></td>
        </tr>
        <tr>
            <td><label for="path">起始：</label></td>
            <td><input type="text" name="startIdx" value="<%=startIdx%>" id="startIdx"></td>
        </tr>
        <tr>
            <td><label for="sourceEnc">源码：</label></td>
            <td><input type="text" name="sourceEnc" value="<%=sourceEnc==null?"":sourceEnc%>" id="sourceEnc"></td>
        </tr>
        <tr>
            <td><label for="dstEnc">标码：</label></td>
            <td><input type="text" name="dstEnc" value="<%=dstEnc==null?"":dstEnc%>" id="dstEnc"></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="开始扫描">
            </td>
        </tr>
    </table>
</form>
<pre>
<%
    if(files!=null&&files.size()>0){
        int idx = startIdx;
        for(File file:files){
            String result = format;
            String name = file.getAbsolutePath();
            byte[] src;
            if(sourceEnc!=null&&!"".equals(sourceEnc)){
                src = name.getBytes(sourceEnc);
            }else{
                src = name.getBytes();
            }
            String dst ;
            if(dstEnc!=null&&!"".equals(dstEnc)){
                dst = new String(src,dstEnc);
            }else{
                dst = new String(src);
            }
            result = result.replace("%path%",dst);
            result = result.replace("%idx%",""+idx);
            idx++;
            result = result.replace("%size%",""+file.length());
            result = result.replace("%date%", StringUtils.date2string(file.lastModified()));
            out.println(result);
        }
    }else{
        out.println("没有找到任何的文件或者还未启动扫描："+path);
    }
    Date stopTime = new Date();
    out.println("启动时间："+StringUtils.date2string(startTime));
    out.println("完成时间："+StringUtils.date2string(stopTime));
    out.println("使用时间："+(stopTime.getTime()-startTime.getTime())+"毫秒");

%>
</pre>
</body>
</html>
<%!
    List<File> listDir(File path){
        List<File> result = new ArrayList<File>();
        if(path.exists()&&path.isDirectory()){
            File[] files = path.listFiles();
            if(files!=null&&files.length>0){
                for(File file:files){
                    String name = file.getName();
                    if(".".equals(name)||"..".equals(name)||name.contains("encode")){
                        continue;
                    }
                    if(file.isDirectory()){
                        result.addAll(listDir(file));
                    }else{
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }
%>