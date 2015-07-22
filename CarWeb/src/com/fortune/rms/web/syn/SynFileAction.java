package com.fortune.rms.web.syn;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.syn.logic.logicImpl.SynFileLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.util.*;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 12:03:23
 */
@Namespace("/syn")
@ParentPackage("default")
@Action(value="synFile")
public class SynFileAction extends BaseAction<SynFile> {
    private File uploadFile;
    private String uploadFileLocal;
    private String uploadFileServer;
    private String fileNameString;
    private List<String> fileNames;
    private SynFileLogicInterface synFileLogicInterface;
    private SynTaskLogicInterface synTaskLogicInterface;
    private TemplateLogicInterface templateLogicInterface;
    @Autowired
    public void setSynFileLogicInterface(SynFileLogicInterface synFileLogicInterface) {
        this.synFileLogicInterface = synFileLogicInterface;
        setBaseLogicInterface(synFileLogicInterface);
    }
    @Autowired
    public void setSynTaskLogicInterface(SynTaskLogicInterface synTaskLogicInterface) {
        this.synTaskLogicInterface = synTaskLogicInterface;
    }
    @Autowired
    public void setTemplateLogicInterface(TemplateLogicInterface templateLogicInterface) {
        this.templateLogicInterface = templateLogicInterface;
    }

    public SynFileAction() {
        super(SynFile.class);
    }

    public String upload() {
        String urlFileName = "/page/";
        Map<String, Object> session = (Map<String, Object>) ActionContext.getContext().getSession();
        String alias="";
        long cspId = -1;
        int isRoot = 0;
        long synLevel = 0l;
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
               cspId = admin.getCspId();
               isRoot = admin.getIsRoot();
            }
        }
        if (isRoot == 1) {
            urlFileName = "/";
            synLevel = 1;
        }else if (isRoot != 0 && cspId > 0){
            alias = this.templateLogicInterface.getCspAliasByCspId(cspId);
            if (alias != null) {

                urlFileName += alias;

            }
        }else {
            setSuccess(false);
        }
        ServletContext context = ServletActionContext.getServletContext();
        String targetFileName = "";
        urlFileName += uploadFileServer;
        String oldFileName = "";
        String fileSuffix = "";
        if (urlFileName.indexOf(".") != -1) {
            oldFileName = uploadFileServer.substring(uploadFileServer.lastIndexOf("/")+1,uploadFileServer.lastIndexOf("."));
            fileSuffix = uploadFileServer.substring(uploadFileServer.lastIndexOf(".")+1);
            String existFileDirectory = context.getRealPath(urlFileName);

            File file = new File(existFileDirectory);
            if(file.exists()){
                file.delete();
            }
            urlFileName = urlFileName.substring(0, urlFileName.lastIndexOf("/"));
        }

        if (uploadFile != null) {
            String targetDirectory = "d:/temp/";

            if (context != null) {
                targetDirectory = context.getRealPath(urlFileName);
            }

            File aFile = new File(uploadFileLocal);
            targetFileName = aFile.getName();

            if(oldFileName!=""&&fileSuffix!=""){
               targetFileName = oldFileName+"."+fileSuffix;
            }
            urlFileName += "/" + targetFileName;
            try {
                org.apache.commons.io.FileUtils.forceMkdir(new File(targetDirectory));
                File target = new File(targetDirectory, targetFileName);
                org.apache.commons.io.FileUtils.copyFile(uploadFile, target);
                String fileType = targetFileName.split("\\.")[1];
                if (fileType.equals("zip")) {
                    ZipUtils.unZip(target.toString(), targetDirectory+"\\");
                    //unzip(target.toString(), targetDirectory);

                }

            } catch (IOException ioe) {
                log.error("发生异常" + ioe.getMessage());
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String md5;
        try {
            md5 = MD5Utils.getFileMD5String(uploadFile);
            obj.setMd5(md5);
        } catch (IOException ioe) {
            log.error("发生异常" + ioe.getMessage());
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            log.error("发生异常" + e.getMessage());
            e.printStackTrace();
        }
        obj.setSpId(cspId);
        obj.setName(targetFileName);
        obj.setType(SynFileLogicImpl.SYNFILE_ADD);
        obj.setStartTime(new Date());
        obj.setUrl(urlFileName);
        obj = synFileLogicInterface.save(obj);
        synTaskLogicInterface.addSynTask(obj.getId(),synLevel);
          writeSysLog("保存上传文件： "+obj.getName()+",cspId="+cspId);
        return Constants.ACTION_SAVE;
    }

    public String isExistFile() {
        log.debug("fileName----->>"+uploadFileLocal);
        String urlFileName = "";
        String targetFileName = "";
        urlFileName = uploadFileServer;
        if (urlFileName.indexOf(".") != -1) {
            urlFileName = urlFileName.substring(0, urlFileName.lastIndexOf("/"));
        }
        String targetDirectory = "d:/temp/";

        ServletContext context = ServletActionContext.getServletContext();

        if (context != null) {
            targetDirectory = context.getRealPath(urlFileName);
        }

        File newFile = new File(targetDirectory + "\\" + uploadFileLocal);
        if (newFile.exists()) {
            obj.setType("1");
            return "error";
        }
        obj.setType("2");
        return "success";
    }

    public String del() {
        String urlFileName = "/page/";
        Map<String, Object> session = (Map<String, Object>) ActionContext.getContext().getSession();
        String alias="";
        long cspId = -1;
        int isRoot = 0;
        long synLevel = 0l;
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
               cspId = admin.getCspId();
               isRoot = admin.getIsRoot();
            }
        }
        if (isRoot == 1) {
            urlFileName = "/";
            synLevel = 1;
        }else if(isRoot == 0 && cspId > 0){
            alias = this.templateLogicInterface.getCspAliasByCspId(cspId);
            if (alias != null) {

                urlFileName += alias;

            }
        }else {
            setSuccess(false);
        }
        if (fileNames != null || fileNames.size() != 0) {
            String targetDirectory = "d:/temp/";
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                ServletContext context = ServletActionContext.getServletContext();

                targetDirectory = context.getRealPath(urlFileName);
                String urlString = targetDirectory + fileName;
                File file = new File(urlString);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                String targetFileName = urlFileName +fileNames.get(i).substring(1,fileNames.get(i).length());
                obj.setName(fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length()));
                obj.setStartTime(new Date());
                obj.setUrl(targetFileName);
                obj.setType(SynFileLogicImpl.SYNFILE_DEL);
                obj = synFileLogicInterface.save(obj);
                synTaskLogicInterface.addSynTask(obj.getId(),synLevel);


            }

        }
        writeSysLog("文件删除： "+obj.getId()+","+obj.getName());
        return Constants.ACTION_DELETE;

    }

    public void unzip(String zipFileName, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
            //获取ZipInputStream中的ZipEntry条目，一个zip文件可以能包含多个ZipEntry，
            //当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            //输入流读取完成
            ZipEntry z = in.getNextEntry();
            while (z != null) {
                System.out.println("unziping " + z.getName());
                //创建以zip包文件名为目录名的根目录
                File f = new File(outputDirectory);
                f.mkdir();
                if (z.isDirectory()) {
                    String name = z.getName();
                    name = name.substring(0, name.length() - 1);
                    System.out.println("name " + name);
                    f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    System.out.println("mkdir " + outputDirectory + File.separator + name);
                } else {
                    f = new File(outputDirectory + File.separator + z.getName());
                    f.createNewFile();
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
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileLocal() {
        return uploadFileLocal;
    }

    public void setUploadFileLocal(String uploadFileLocal) {
        this.uploadFileLocal = uploadFileLocal;
    }

    public String getUploadFileServer() {
        return uploadFileServer;
    }

    public void setUploadFileServer(String uploadFileServer) {
        this.uploadFileServer = uploadFileServer;
    }

    public String getFileNameString() {
        return fileNameString;
    }

    public void setFileNameString(String fileNameString) {
        this.fileNameString = fileNameString;
        if (fileNameString != null) {
            String[] fileIdArray = fileNameString.split(";");
            if (fileIdArray.length != 0) {
                fileNames = new ArrayList<String>();
                for (int i = 0; i < fileIdArray.length; i++) {
                    fileNames.add(fileIdArray[i]);
                }
            }
        }
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;

    }
}
