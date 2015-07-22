package com.fortune.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-10-4
 * Time: 20:26:18
 * 文件处理有关的工具
 */
public class FileUtils {
    private static Log logger = LogFactory.getLog(FileUtils.class);
    private static final int BUFFER_SIZE = 16 * 1024;
    public static String srcEnc = AppConfigurator.getInstance().getConfig("system.fileSystem.srcEncoding", null);
    public static String dstEnc = AppConfigurator.getInstance().getConfig("system.fileSystem.dstEncoding", null);
    public static String srcNativeFileSystemEnc = AppConfigurator.getInstance().getConfig("system.fileSystem.srcNativeFileSystemEnc",null);
    public static String dstNativeFileSystemEnc = AppConfigurator.getInstance().getConfig("system.fileSystem.dstNativeFileSystemEnc", null);

    public static String translateString(String fileName){
        return translateString(fileName,srcEnc,dstEnc);
    }

    public static String translateStringToNativeFileSystem(String fileName){
        return translateString(fileName,srcNativeFileSystemEnc,dstNativeFileSystemEnc);
    }

    public static String translateString(String fileName,String srcEnc,String dstEnc){
        if(srcEnc==null&&dstEnc==null){
            return fileName;
        }
        try {
            byte[] srcBytes;
            if(null==srcEnc||"".equals(srcEnc)||"null".equals(srcEnc)){
                srcBytes=fileName.getBytes();
            }else{
                srcBytes = fileName.getBytes(srcEnc);
            }
            if(null==dstEnc||"".equals(dstEnc)||"null".equals(dstEnc)){
                fileName=new String(srcBytes);
            }else{
                fileName = new String(srcBytes,dstEnc);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("居然不支持文件名内码转换："+srcEnc+"->"+dstEnc);
        }
        return fileName;
    }

    public static boolean fileExists(String fileName){
        File file = newFile(fileName);
        return file.exists() && !file.isDirectory();
    }
    public static String extractFileName(String filePath,String separator){
        if(filePath == null)return null;
        String result = filePath;
        int i= filePath.lastIndexOf(separator);
        if(i>=0){
            if(i<filePath.length()){
                result = filePath.substring(i+1);
            }
        }else{
            i = result.lastIndexOf("\\");
            if(i>0&&i<result.length()){
                result = result.substring(i+1);
            }
        }
        return result;
    }

    public static String extractFilePath(String fullFileName,String separator){
        if(fullFileName == null)return null;
        String result = fullFileName;
        int i= fullFileName.lastIndexOf(separator);
        if(i>0){
            result = fullFileName.substring(0,i);
        }else if(i==0){
            return "";
        }else{
            i = result.lastIndexOf("\\");
            if(i>=0){
                result = fullFileName.substring(0,i);
            }else{
                return "";
            }
        }
        return result;
    }

    @SuppressWarnings("unused")
    public static boolean copyFile(String sourceFile,String desertFile){
        File source=newFile(sourceFile);
        if(source.exists()){
            try {
                if(createDir(extractFilePath(desertFile,"/"))){
                    File desert = newFile(desertFile);
                    if(!desert.exists()){
                        return                     source.renameTo(desert);
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
    public static boolean createDir(String dirName)throws IOException {
        boolean result;
        File file = newFile(dirName);
        if(file.isFile()) return false;
        if(file.exists()) return true;
        result = file.mkdirs();
        return result;
    }

    public static List<File> listDir(String dir,String extName,boolean withSubDir){
        List<File> result = new ArrayList<File>();
        if (dir == null) {
            logger.error("目录输入为空");
            return result;
        } else {
            dir = dir.replaceAll("//", "/");
            if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                dir = dir.replace("/","\\");
            }
            logger.debug("列目录：" + dir);
        }

        String[] dirs = dir.split(":");
        if(dirs.length <= 1&&!dir.startsWith("\\\\")) {
            dir = "/"+dir;
        }
        File dirFile = newFile(dir);
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

    public static List<SimpleFileInfo> listFiles(String dir, String extName, PageBean pageBean,boolean withSubDir) {
        if(pageBean == null){
            pageBean = new PageBean();
        }
        List<SimpleFileInfo> all = listFiles(dir,extName,pageBean.getOrderBy(),pageBean.getOrderDir(),withSubDir);
        if(all==null){
            pageBean.setRowCount(0);
            return new ArrayList<SimpleFileInfo>();
        }
        pageBean.setRowCount(all.size());
        int startIndex = pageBean.getStartRow();
        int stopIndex = startIndex+pageBean.getPageSize();
        if(startIndex>=pageBean.getRowCount()){
            startIndex = pageBean.getRowCount()-1;
        }
        if(startIndex<=0){
            startIndex = 0;
        }
        if(stopIndex<startIndex){
            stopIndex = startIndex+1;
        }
        if(stopIndex>=pageBean.getRowCount()){
            stopIndex = pageBean.getRowCount();
        }
        if(stopIndex<0){
            stopIndex = 0;
        }
        List<SimpleFileInfo> result;
        if(stopIndex>startIndex){
            result = all.subList(startIndex,stopIndex);
        }else{
            result = new ArrayList<SimpleFileInfo>();
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    public static List<SimpleFileInfo> listFiles(String dir, String extName, String orderBy, String orderDir,boolean withSubDir) {
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
        List<File> filesArray = listDir(dir,extName,withSubDir);
        File dirFile = newFile(dir);
        dir = dirFile.getAbsolutePath();
        if (filesArray != null) {
            for (File file : filesArray) {
                String fileName = file.getAbsolutePath();
                if(fileName!=null){
                    fileName = fileName.replace("\\","/");
                    fileName = fileName.substring(dir.length());
                    while(fileName.startsWith("/")&&fileName.length()>1){
                        fileName=fileName.substring(1);
                    }
                }
                result.add(new SimpleFileInfo(fileName, file.length(), new Date(file.lastModified()), file.isDirectory(), getFileType(file.getName())));
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

    public static int listFiles(File dir, boolean searchSubDir, FileUtilsDispatcher fileDispatcher) throws IOException {
        int result = 0;
        if (dir == null) return 0;
        if (dir.isFile()) return 0;
        if (fileDispatcher == null) {
            throw new IOException("未定义回调接口");
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files!=null){
                for (File file : files) {
                    if (file.isFile()) {
                        result += fileDispatcher.onFileFound(file);
                    } else if (file.isDirectory()) {
                        fileDispatcher.onDirectoryFound(file);
                        if (searchSubDir) {
                            result += listFiles(file, searchSubDir, fileDispatcher);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static int listFiles(String dirName, boolean searchSubDir, FileUtilsDispatcher fileDispatcher) throws Exception {
        File dir = newFile(dirName);
        if (dir.exists()) {
            return listFiles(dir, searchSubDir, fileDispatcher);
        }
        return 0;
    }
    public static FileType getFileType(String fileName) {
        if (fileName == null) {
            return FileType.unknown;
        }
        AppConfigurator config = AppConfigurator.getInstance();
        String imageExtNames = config.getConfig("system.default.imageExtNames",";jpg;gif;png;jpeg;bmp;");
        String videoExtNames = config.getConfig("system.default.videoExtNames",";mp4;avi;mkv;264;wmv;mpg;rm;rmvb;vob;ts;mpeg;mkv;mov;flv;f4v;asf;");
        String soundExtNames = config.getConfig("system.default.soundExtNames",";mp3;wav;mp2;aac;snd;wma;");
        String flashExtNames = config.getConfig("system.default.flashExtNames",";swf;");
        String docExtNames = config.getConfig("system.default.docExtNames",";doc;ppt;txt;");
        fileName = fileName.toLowerCase();
        String fileExt = getFileExtName(fileName);
        if(fileExt!=null){
            fileExt = ";" + fileExt.toLowerCase() + ";";
        }else{
            return FileType.unknown;
        }
        if (imageExtNames.contains(fileExt)) {
            return FileType.image;
        }
        if (videoExtNames.contains(fileExt)) {
            return FileType.video;
        }
        if (soundExtNames.contains(fileExt)) {
            return FileType.sound;
        }
        if (flashExtNames.contains(fileExt)) {
            return FileType.flash;
        }
        if (docExtNames.contains(fileExt)) {
            return FileType.doc;
        }
        return FileType.unknown;
    }

    public static String getFileExtName(String fileName) {
        if (fileName == null) {
            return "";
        }
        int i = fileName.lastIndexOf(".");
        if (i >= 0) {
            if (i < fileName.length() - 1) {
                return fileName.substring(i + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @SuppressWarnings("unused")
    public static long getFileSize(String fullFilePath){
        List<SimpleFileInfo> results = listFiles(extractFilePath(fullFilePath,"/"),extractFileName(fullFilePath,"/"),"","",false);
        if(results!=null&&results.size()>0){
            SimpleFileInfo result = results.get(0);
            return result.getSize();
        }
        return -1;
    }

    @SuppressWarnings("unused")
    public static long getDirSize(String dirName){
        List<SimpleFileInfo> results = listFiles(dirName,"*.*","","",true);
        long result = 0;
        for(SimpleFileInfo file:results){
            if(file.getSize()>0&&!file.isDirectory()){
                result+=file.getSize();
            }
        }
        return result;
    }
    public static boolean setFileMediaInfo(String fullFileName,SimpleFileInfo fileInfo){
        FileType fileType = getFileType(fullFileName);
        return setFileMediaInfo(fullFileName, fileInfo,fileType);
    }

    public static File newFile(File parent,String child){
        return new File(parent,translateStringToNativeFileSystem(child));
    }
    public static File newFile(String fullFileName){
        return new File(translateStringToNativeFileSystem(fullFileName));
    }
    public static boolean setFileMediaInfo(String fullFileName,SimpleFileInfo fileInfo,FileType fileType){
        boolean result = false;
        if(fileInfo==null){
            return false;
        }
        File file = newFile(fullFileName);
        if(!file.exists()){
            fileInfo.setLength(-1);
            return false;
        }
        fullFileName = file.getAbsolutePath();
        if(fileType.equals(FileType.video)){
            SimpleFileInfo r=(MediaUtils.getMediaFileInfoByFFPROBE(file,fileInfo));
            if(r!=null){
                BeanUtils.copyProperties(r,fileInfo);
                return true;
            }
            String cmdOfGetFileInfo = AppConfigurator.getInstance().getConfig("system.tools.cmdOfGetFileInfo",
                    "cmd.exe /c C:\\FFModules\\mplayer.exe -identify \"%sourceFile%\" -nosound -vc dummy -vo null");
            CommandRunner runner = new CommandRunner();
            try {
                String cmdLine = cmdOfGetFileInfo.replace("%sourceFile%",fullFileName);
                //logger.debug("将要执行："+cmdLine);
                List<String> runResult =new ArrayList<String>();
                try {
                    AppConfigurator config = AppConfigurator.getInstance();
                    boolean shouldWriteDebugFile = config.getBoolConfig("system.debug.writeSetFileInfo",true);
                    String debugFileName = StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_"+Math.round(Math.random()*100000)
                            +"_"+file.getName().replace('.','_')+".log";
                    String debugFilePath =config.getConfig("system.debug.path","C:/debug/");
                    if(shouldWriteDebugFile){
                        FileUtils.writeNew(debugFilePath,debugFileName,StringUtils.date2string(new Date())+" - 正在扫描"+fullFileName+",此文件会被自动删除，如果没有删除，是有问题了！\r\n"+cmdLine);
                    }
                    logger.debug("即将执行脚本："+cmdLine);
                    runResult.addAll( runner.runCommand(cmdLine,null,10000,null));
                    logger.debug("执行脚本完毕："+cmdLine);
                    if(shouldWriteDebugFile){
                        File debugFile = newFile(debugFilePath+"/"+debugFileName);
                        if(debugFile.exists()){
                            if(!debugFile.delete()){
                                logger.error("无法删除文件："+debugFile.getAbsolutePath());
                            }
                        }
                    }
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    logger.error("超时错误："+e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error("中断错误：" + e.getMessage());
                }
                Map<String,String> results = new HashMap<String,String>();
                for(String msg:runResult){
                    if(msg==null){
                        continue;
                    }
                    String[] datas = msg.split("=");
                    //logger.error(msg);
                    if(datas.length>1){
                        results.put(datas[0],datas[1]);
                    }
                }
                int height = StringUtils.string2int(results.get("ID_VIDEO_HEIGHT"), -1);
                if(height==-1){
                    height = StringUtils.string2int(results.get("height"),-1);
                }
                fileInfo.setHeight(height);
                int width = StringUtils.string2int(results.get("ID_VIDEO_WIDTH"),-1);
                if(width==-1){
                    width = StringUtils.string2int(results.get("width"),-1);
                }
                fileInfo.setWidth(width);
                String length = results.get("ID_LENGTH");
                if(length==null){
                    length = results.get("duration");
                }
                if(length!=null){
                    fileInfo.setLength(Float.parseFloat(length));
                }else{
                    fileInfo.setLength(-1);
                }
                result = true;
            } catch (IOException e) {
                logger.error("获取文件信息时发生错误：" + e.getMessage());
                e.printStackTrace();
            }
        }else if(fileType.equals(FileType.image)){
            try {
                logger.debug("准备读取文件：" + fullFileName);
                InputStream img = new FileInputStream(fullFileName);
                ImageInputStream iis = ImageIO.createImageInputStream(img);

                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                if(readers.hasNext()){
                    ImageReader reader = readers.next();
                    reader.setInput(iis);
                    logger.debug("width=" + reader.getWidth(0) + ",height=" + reader.getHeight(0) + ",mime=" + reader.getFormatName());
                    fileInfo.setHeight(reader.getHeight(0));
                    fileInfo.setWidth(reader.getWidth(0));
                }else{
                    logger.error("没有找到合适的文件读取器：" + fullFileName);
                }
                img.close();
                result = true;
            } catch (IOException e) {
                logger.error("获取文件信息时发生错误：" + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    public static File copy(File src, String dstPath, String fileName) {
        InputStream in = null;
        OutputStream out = null;

        try {
            File parent = newFile(dstPath);

            if (!parent.isDirectory()) {
                if(parent.mkdirs()){

                }
            }
            File dst = newFile(parent, fileName);
            if(dst.createNewFile()){

            }
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst),
                    BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            return dst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int getLineCount(String fileName){
        return getLineCount(newFile(fileName));
    }
    public static int getLineCount(File file){
        try {
            if(file.exists()){
                LineNumberReader reader = new LineNumberReader(new FileReader(file));
                reader.skip(file.length());
                return reader.getLineNumber();
            }else{
                return -404;
            }
        } catch (FileNotFoundException e) {
            return -404;
        } catch (IOException e) {
            return -500;
        }
    }
    public static File writeContinue(String dstPath, String fileName, String info) {
        File parent = newFile(dstPath);
        if (!parent.isDirectory()) {
            if(parent.mkdir()){

            }
        }
        //FileWriter theFile = null;
        OutputStreamWriter pwriter = null;
        try {
            File file = newFile(parent, fileName);
            if (file.exists()) {
                //logger.debug("文件存在");
                // return false;
            } else {
                logger.debug("文件不存在，正在创建...");
                if (file.createNewFile()) {
                    logger.debug("文件创建成功！");
                } else {
                    logger.debug("文件创建失败！");
                    return null;
                }
            }
            String fileEncoding = System.getProperty("file.encoding");
            if(fileEncoding!=null&&!"".equals(fileEncoding)){
                pwriter = new OutputStreamWriter(new FileOutputStream(file,true),fileEncoding);
            }else{
                pwriter = new OutputStreamWriter(new FileOutputStream(file,true));
            }
            //pwriter = new PrintWriter(theFile);
            pwriter.append(info);
            //pwriter.print(info + "\n\r");

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (pwriter != null) {
                try {
                    pwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SuppressWarnings("unused")
    public static File writeNew(String filePath, String fileName, String info) {
        File parent = newFile(filePath);
        if (!parent.isDirectory()) {
            if(!parent.mkdirs()){
                logger.error("无法创建目录："+parent.getAbsolutePath());
            }
        }
        OutputStreamWriter output = null;
        File file = newFile(parent, fileName);
        try {
            if (file.exists()) {
                logger.debug("文件存在");
                //return null;
            } else {
                logger.debug("文件不存在，正在创建...");
                if (file.createNewFile()) {
                    logger.debug("文件创建成功！");
                } else {
                    logger.debug("文件创建失败！");
                    return null;
                }
            }
            String fileEncoding = System.getProperty("file.encoding");
            if(fileEncoding!=null&&!"".equals(fileEncoding)){
                output = new OutputStreamWriter(new FileOutputStream(file),fileEncoding);
            }else{
                output = new OutputStreamWriter(new FileOutputStream(file));
            }
            output.write(info);
            return file;
        } catch (IOException e) {
            logger.error("无法写入文件："+file.getAbsolutePath()+",错误信息："+e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error("无法写入文件："+file.getAbsolutePath()+",错误信息："+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    public static File writeNew(String filePath, String info) {

        OutputStreamWriter output = null;
        try {
            File file = newFile(filePath);
            if (file.exists()) {
                logger.debug("文件存在，重写覆盖");
                //return null;
            } else {
                logger.debug("文件不存在，正在创建...");
                if (file.createNewFile()) {
                    logger.debug("文件创建成功！");
                } else {
                    logger.debug("文件创建失败！");
                    return null;
                }
            }
            String fileEncoding = System.getProperty("file.encoding");
            if(fileEncoding!=null&&!"".equals(fileEncoding)){
                output = new OutputStreamWriter(new FileOutputStream(file),fileEncoding);
            }else{
                output = new OutputStreamWriter(new FileOutputStream(file));
            }
            output.write(info);

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String readFileInfo(String file) {
        String s;
        StringBuilder sb = new StringBuilder();
        File f = newFile(file);
        if (f.exists()) {
            logger.debug("文件存在");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f)));

                while ((s = br.readLine()) != null) {
                    sb.append(s).append("\n");
                }
                //logger.debug(sb);
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
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
            return null;
        }

    }

    public static String readFileInfo(String file,String encode) {
        String s;
        StringBuilder sb = new StringBuilder();
        File f = newFile(file);
        if (f.exists()) {
            logger.debug("文件存在");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f),encode));

                while ((s = br.readLine()) != null) {
                    sb.append(s).append("\r\n");
                }
                //logger.debug(sb);
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
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
            return null;
        }

    }

    @SuppressWarnings("unused")
    public static long getFileSize(File file){
        if (file!=null&&file.isFile()){
            FileInputStream fi = null;
            try {
                fi = new FileInputStream(file);
                return fi.available();
            } catch (IOException e) {
                logger.info("can not get the file size.");
                e.printStackTrace();
                return 0l;
            }finally{
                if (fi!=null){
                    try {
                        fi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else
            return 0l;
    }


    @SuppressWarnings("unused")
    public static void unZIP(String zipFileName, String outputDirectory){
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
            //获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            //当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            //输入流读取完成；
            ZipEntry z = in.getNextEntry();
            while (z != null) {
                logger.debug("unziping " + z.getName());

                //创建以zip包文件名为目录名的根目录
                File f = newFile(outputDirectory);
                if(f.mkdir()){

                }
                if (z.isDirectory()) {
                    String name = z.getName();
                    name = name.substring(0, name.length() - 1);
                    logger.debug("name " + name);
                    f = newFile(outputDirectory + File.separator + name);
                    if(f.mkdir()){

                    }
                    logger.debug("mkdir " + outputDirectory + File.separator + name);
                } else {
                    f = newFile(outputDirectory + File.separator + z.getName());
                    if(f.createNewFile()){

                    }
                    FileOutputStream out = new FileOutputStream(f);
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    out.close();
                }
                //读取下一个ZipEntry
                z = in.getNextEntry();
            }
            in.close();
        }
        catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }
    //调用这个方法，传四个参数：1、原图片绝对路径  2、缩略图绝对路径  3、生成缩略图后的高度   4、生成缩略图后的宽度
    public static void createFixedBoundImg(String OriFilePath, String TargetFilePath, int height, int width) throws Exception {
        double Ratio = 0.0;
        File f = newFile(OriFilePath);
        Image src = ImageIO.read(f);
        int oriWidth = src.getWidth(null);
        int oriHeight = src.getHeight(null);
        int tagWidth, tagHeight;
        if (oriWidth > width || oriHeight > height) {
            if (oriHeight > oriWidth) {
                Ratio = (new Integer(height)).doubleValue() / oriHeight;
                tagHeight = height;
                tagWidth = (int) (oriWidth * Ratio);
            }
            else {
                Ratio = (new Integer(width)).doubleValue() / oriWidth;
                tagHeight = (int) (oriHeight * Ratio);
                tagWidth = width;
            }
        }
        else {
            tagHeight = oriHeight;
            tagWidth = oriWidth;
        }
        BufferedImage target = new BufferedImage(tagWidth, tagHeight, BufferedImage.TYPE_INT_RGB);
        target.getGraphics().drawImage(src, 0, 0, tagWidth, tagHeight, null);
        FileOutputStream out = new FileOutputStream(TargetFilePath);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(target);
        out.close();
    }
    public static void main(String args[]) {
        SimpleFileInfo fileInfo = new SimpleFileInfo(new File("E:\\temp\\_temp20130625093949_9360.t.mp4"));
        System.out.println("处理结果："+FileUtils.setFileMediaInfo(fileInfo.getName(),fileInfo,fileInfo.getType()));
        System.out.println(fileInfo);
    }
}
