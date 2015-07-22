package com.fortune.rms.web.file;


import com.fortune.common.Constants;
import com.fortune.common.web.base.FortuneAction;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.util.AppConfigurator;
import com.fortune.util.FileUtils;
import org.apache.struts2.convention.annotation.*;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 12-3-28
 * Time: ����5:07
 */

@Namespace("/file")
@ParentPackage("default")
@Results({
        @Result(name = "delete", location = "/common/jsonMessages.jsp"),
        @Result(name = "fileCopy", location = "/common/jsonMessages.jsp")
})
@Action(value = "file")
public class FileAction extends FortuneAction {
    private String fileNames;
    private String fileCopyName;
    private long fileType;

    public FileAction() {
        super();
    }

    public String getFileCopyName() {
        return fileCopyName;
    }

    public void setFileCopyName(String fileCopyName) {
        this.fileCopyName = fileCopyName;
    }

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public long getFileType() {
        return fileType;
    }

    public void setFileType(long fileType) {
        this.fileType = fileType;
    }

    public String delFile() {
        try {
            Csp csp = (Csp) session.get(Constants.SESSION_ADMIN_CSP);

            if (csp == null) {
                super.addActionError("�ļ����Ʒ����쳣��CSP��Ϣ���󣬿�����û����ȷ��¼��" );
            }else{
                String rootDir = AppConfigurator.getInstance().getConfig("file.copy.srcDir", "D:/") +"/"+ csp.getAlias() + "/";
                String targetDir = AppConfigurator.getInstance().getConfig("file.copy.targetDir", "F:/")+"/" + csp.getAlias() + "/";
                String[] files = fileNames.split("\\*");
                File delFile = null;

                for (int i = 0; i < files.length; i++) {
                    if(fileType==3){
                        delFile=new File(targetDir+files[i]);
                    } else{
                        delFile = new File(rootDir + files[i]);
                    }

                    if (!delFile.exists()) {
                        log.debug(delFile+"�ļ�ɾ��ʧ��; " );
                    } else {
                        if (delFile.delete()) {
                            this.addActionMessage("ɾ���ɹ�");
                            setSuccess(true);
                        } else {
                            setSuccess(false);
                            this.addActionError("�޷�ɾ����");
                        }
                    }
                }
            }
        } catch (Exception e) {
            super.addActionError("ɾ�����ݷ����쳣��" + e.getMessage());
            e.printStackTrace();
        }
        return "delete";
    }

    public String fileCopy() {
        try {
            Csp csp = (Csp) session.get(Constants.SESSION_ADMIN_CSP);

            if (csp == null) {
                super.addActionError("�ļ����Ʒ����쳣��CSP��Ϣ���󣬿�����û����ȷ��¼��" );
            }else{
                String sourceDir = AppConfigurator.getInstance().getConfig("file.copy.srcDir", "D:/") +"/"+ csp.getAlias() + "/";
                String targetDir = AppConfigurator.getInstance().getConfig("file.copy.targetDir", "F:/")+"/" + csp.getAlias() + "/";
                String[] files = fileCopyName.split("\\*");
                for (int i = 0; i < files.length; i++) {
                    File in = new File(sourceDir + files[i]);
                    File targetFie = new File(targetDir + files[i]);
                    files[i] = files[i].substring(2, files[i].length());

                    if (targetFie.exists()) {

                        targetFie.delete();

                    } else {

                    }
                    FileUtils.copy(in, FileUtils.extractFilePath(targetDir + "/" + files[i], "/"), in.getName());
                }
            }
        } catch (Exception e) {
            super.addActionError("�ļ����Ʒ����쳣��" + e.getMessage());
            e.printStackTrace();
        }

        return "fileCopy";
    }


}
