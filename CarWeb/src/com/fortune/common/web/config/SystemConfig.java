package com.fortune.common.web.config;

import com.fortune.common.Constants;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.common.web.base.FortuneAction;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-12-11
 * Time: 13:04:01
 * ϵͳ����
 */
public class SystemConfig  extends FortuneAction {
    private File file;
    private String saveToFile;

    @SkipValidation
    public String uploadFile(){
        if(session.get(Constants.SESSION_ADMIN)==null){
            return "login";
        }
        String result = "uploadSuccess";
        if(file==null){
            addActionError("�ϴ��ļ�����ȷ���ϴ���ϢΪ�գ�");
            result = "uploadFail";
        }else{
            String targetFileName;
            ServletContext context = ServletActionContext.getServletContext();
            if (context != null) {
                targetFileName = context.getRealPath(saveToFile);
            } else {
                targetFileName = ConfigManager.getInstance().getConfig("post.upload.dir", "c:/poster/") + saveToFile;
            }

            try {
                log.debug("Ŀ���ļ���" + targetFileName);
                FileUtils.forceMkdir(new File(com.fortune.util.FileUtils.extractFilePath(targetFileName,"/")));
                File target = new File(targetFileName);
                if(target.exists()){
                    //���ļ����ڣ�����һ������
                    String backupFile= targetFileName+"."+target.lastModified();
                    FileUtils.copyFile(target,new File(backupFile));
                }
                log.debug("�ϴ������ļ���" + file.getAbsolutePath());
                FileUtils.copyFile(file, target);
                addActionMessage("�ϴ���ɣ�"+target.getAbsolutePath());
            } catch (IOException e) {
                this.addActionError("�ϴ������쳣��"+e.getMessage());
                log.error("�����쳣��" + e.getMessage());
                result = "uploadFail";
                e.printStackTrace();
            }
        }
        return result;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(String saveToFile) {
        this.saveToFile = saveToFile;
    }
}
