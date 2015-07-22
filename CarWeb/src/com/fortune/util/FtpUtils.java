package com.fortune.util;

import com.fortune.rms.business.system.model.Device;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.io.Util;
import org.apache.log4j.Logger;
import sun.net.ftp.FtpClient;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>Title: RMS </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Fortune network</p>
 *
 * @author 刘喜军
 * @version 4.5
 */
public class FtpUtils {
    private FTPClient ftpClient;
    private Logger logger = Logger.getLogger(this.getClass());
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
    public String controlEncoding;
    public String pathNameEncoding;
    // path should not the path from root index
    // or some FTP server would go to root as '/'.
/*
    public void connectServer(FtpConfig ftpConfig) throws SocketException,
            IOException {
        String server = ftpConfig.getServer();
        int port = ftpConfig.getPort();
        String user = ftpConfig.getUsername();
        String password = ftpConfig.getPassword();
        String location = ftpConfig.getLocation();
        connectServer(server, port, user, password, location);
    }
*/


    public boolean connectServer(String server, int port, String user,
                              String password, String path) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        controlEncoding  = AppConfigurator.getInstance().getConfig("ftpUtils.ControlEncoding","UTF-8");   //GBK
        pathNameEncoding = AppConfigurator.getInstance().getConfig("ftpUtils.PathNameEncoding",null);//"ISO-8859-1");  //ISO-8859-1
        ftpClient.setControlEncoding(controlEncoding);
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");
        ftpClient.enterLocalPassiveMode();
/*
*/

        boolean result = ftpClient.login(user, password);
        ftpClient.setDataTimeout(120000);
        int reply = ftpClient.getReplyCode();
        logger.debug("Connected to " + server + ".");
        logger.debug(reply);
        if(!FTPReply.isPositiveCompletion(reply)){
            ftpClient.disconnect();
            logger.error("FTP server refused connection:" + server + ":" + port);
            return false;
        }         // Path is the sub-path of the FTP path
        if (path.length() != 0) {
            ftpClient.changeWorkingDirectory(path);
        }
        return result;
    }

    //FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE
    // Set transform type
    public void setFileType(int fileType) throws IOException {
        ftpClient.setFileType(fileType);
    }

    public void closeServer() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }
    //=======================================================================
    //==         About directory       =====
    // The following method using relative path better.
    //=======================================================================

    public boolean changeDirectory(String path) throws IOException {
        return ftpClient.changeWorkingDirectory(path);
    }

    public boolean createDirectory(String pathName) throws IOException {
        return ftpClient.makeDirectory(pathName);
    }

    public void createDirectories(String pathName) throws IOException {
        String paths[] = pathName.split("/");
        for (int i=0; i<paths.length; i++){
            if (!"".equals(paths[i])){
                ftpClient.makeDirectory(paths[i]);
                ftpClient.changeWorkingDirectory(paths[i]);
                //System.out.println( ftpClient.printWorkingDirectory() );
            }
        }
    }

    public String getPWD(){
        try{
            return ftpClient.printWorkingDirectory();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeDirectory(String path) throws IOException {
        return ftpClient.removeDirectory(path);
    }

    // delete all subDirectory and files.
    public boolean removeDirectory(String path, boolean isAll)
            throws IOException {

        if (!isAll) {
            return removeDirectory(path);
        }

        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr == null || ftpFileArr.length == 0) {
            return removeDirectory(path);
        }
        //
        for (FTPFile ftpFile : ftpFileArr) {
            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                logger.debug("* [sD]Delete subPath [" + path + "/" + name + "]");
                removeDirectory(path + "/" + name, true);
            } else if (ftpFile.isFile()) {
                logger.debug("* [sF]Delete file [" + path + "/" + name + "]");
                deleteFile(path + "/" + name);
            } else if (ftpFile.isSymbolicLink()) {

            } else if (ftpFile.isUnknown()) {

            }
        }
        return ftpClient.removeDirectory(path);
    }

    // Check the path is exist; exist return true, else false.
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory()
                    && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //=======================================================================
    //==         About file        =====
    // Download and Upload file using
    // ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE) better!
    //=======================================================================

    // #1. list & delete operation
    // Not contains directory

    public FTPFile[] getFileListEx(String path) throws IOException {
        return ftpClient.listFiles(path);
    }

    public SearchResult<FTPFile> getFileListEx(String path,String fileNameRegEx, String orderBy, String orderDir,
                                               int startRow, int rowCount) throws IOException {
        if(path==null){
            path = "";
        }
        if(fileNameRegEx==null){
            fileNameRegEx = "";
        }
        logger.debug("prepare list '"+path+"' of '"+fileNameRegEx+"',orderBy " + orderBy
                +" ,dir "+orderDir+" ...");
        if(null!=pathNameEncoding){
            try {
                path = new String(path.getBytes(),pathNameEncoding);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if(ftpClient.changeWorkingDirectory(path)){
            FTPFile[] files=new FTPFile[0];
            try {
                if(!"".equals(fileNameRegEx.trim())){
                    files = ftpClient.listFiles(fileNameRegEx);
                }else{
                    files = ftpClient.listFiles();
                }
            } catch (IOException e) {
                logger.error("列取目录时发生IO异常："+path+",错误："+e);
            } catch (Exception e) {
                logger.error("列取目录时发生异常："+path+",错误："+e);
            }
            List<FTPFile> allFiles = this.orderFiles(files, orderBy, orderDir);
            SearchResult<FTPFile> result = new SearchResult<FTPFile>();
            result.setRows(new ArrayList<FTPFile>());
            result.setRowCount(allFiles.size());
            for (int i = startRow; i < startRow+rowCount && i<allFiles.size(); i++) {
                result.getRows().add(allFiles.get(i));
            }
            logger.debug("file count:"+result.getRowCount());
            return result;
        }else{
            try {
                logger.error("Can not change to the path of:"+path);
                throw new IOException("Can not change to dir："+path);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getFileList(String path) throws IOException {
        // listFiles return contains directory and file, it's FTPFile instance
        // listNames() contains directory, so using following to filer directory.
        //String[] fileNameArr = ftpClient.listNames(path);
        FTPFile[] ftpFiles = ftpClient.listFiles(path);

        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    public boolean deleteFile(String pathName) throws IOException {
        return ftpClient.deleteFile(pathName);
    }

    // #2. upload to ftp server
    // InputStream <------> byte[]  simple and See API

    public boolean uploadFile(String fileName, String newName)
            throws IOException {
        boolean flag = false;
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(fileName);
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    public boolean uploadFile(String fileName) throws IOException {
        return uploadFile(fileName, fileName);
    }

    public boolean uploadFile(InputStream iStream, String newName)
            throws IOException {
        boolean flag = false;
        try {
            // can execute [OutputStream storeFileStream(String remote)]
            // Above method return's value is the local file stream.
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    // #3. Down load

    public boolean download(String remoteFileName, String localFileName)
            throws IOException {
        boolean flag = false;
        File outfile = new File(localFileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outfile);
            flag = ftpClient.retrieveFile(remoteFileName, oStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            try {
                if (oStream != null) {
                    oStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public InputStream downFile(String sourceFileName) throws IOException {
        return ftpClient.retrieveFileStream(sourceFileName);
    }

    @SuppressWarnings("unchecked")
    public List<FTPFile> orderFiles(FTPFile[] fileArray, String orderBy, String orderDir) {
        List<FTPFile> files = new ArrayList();
        List<FTPFile> dirs = new ArrayList();
        for (FTPFile file : fileArray) {
            if (file.getType() == FTPFile.DIRECTORY_TYPE) {
                dirs.add(file);
            } else {
                files.add(file);
            }
        }
        dirs = SortUtils.sortArray(dirs, orderBy, orderDir);
        files = SortUtils.sortArray(files, orderBy, orderDir);
        List<FTPFile> result = new ArrayList();
        result.addAll(dirs);
        result.addAll(files);
        return result;
    }

    public static void main(String[] args){
        FtpUtils ftpUtils = new FtpUtils();
        long deviceId = 2;
        Device encoder = new Device();
        encoder.setId(deviceId);
        encoder.setName("测试服务器");
        encoder.setFtpPath("/");
        encoder.setFtpUser("siva");
        encoder.setFtpPort(21L);
        encoder.setFtpPwd("itislxj");
        encoder.setIp("127.0.0.1");
        PageBean pageBean = new PageBean(0,1000,null,null);
        Logger logger = Logger.getLogger(ftpUtils.getClass());
        String path = "中文目录";
        String name="*.*";
        List<SimpleFileInfo> result = new ArrayList<SimpleFileInfo>();
        boolean absDir = false;
        try {
            boolean connectResult = ftpUtils.connectServer(encoder.getIp(),encoder.getFtpPort().intValue(),encoder.getFtpUser(),
                    encoder.getFtpPwd(),encoder.getFtpPath());
            //修正一下orderby
            if(!connectResult){
                logger.error("无法连接到FTP服务器："+encoder.getName()+","+encoder.getIp()+":"
                        +encoder.getFtpPort()+",login:"+encoder.getFtpUser()+"/"+encoder.getFtpPwd());
                return;
            }
            String orderBy = pageBean.getOrderBy();
            if(orderBy!=null && !"".equals(orderBy)){
                if(orderBy.startsWith("o1.")){
                    logger.debug("order by 已经被修正！原来的："+orderBy);
                    orderBy = orderBy.substring(3);
                }
            }
            if(path==null){
                path = "";
            }
            if(!absDir){
                String serverPath = encoder.getFtpPath();
                if(serverPath==null||"".equals(serverPath.trim())){
                    serverPath = "";
                }
                if(!serverPath.endsWith("/")){
                    serverPath=serverPath+"/";
                }
                path = serverPath+path;
            }
            while(path.contains("//")){
                path = path.replace("//","/");
            }
            logger.debug("准备列取目录："+path);
            SearchResult<FTPFile> files = ftpUtils.getFileListEx(path,name,orderBy,pageBean.getOrderDir(),pageBean.getStartRow(),
                    pageBean.getPageSize());
            if(files!=null){
                for(FTPFile file:files.getRows()){
                    result.add(new SimpleFileInfo(file.getName(),file.getSize(),file.getTimestamp().getTime(),
                            file.isDirectory(),FileUtils.getFileType(file.getName())));
                }
                pageBean.setRowCount(files.getRowCount());
            }
        } catch (IOException e) {
            logger.error("连接到【ID=" +deviceId+
                    "‘"+encoder.getName()+"(" +encoder.getIp()+":"+ encoder.getFtpPort()+
                    ")’】发生错误："+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("连接到【ID=" +deviceId+
                    "‘"+encoder.getName()+"(" +encoder.getIp()+":"+ encoder.getFtpPort()+
                    ")’】发生错误："+e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                ftpUtils.closeServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(SimpleFileInfo fileInfo :result){
            System.out.println(fileInfo.toString());
        }
   }
}