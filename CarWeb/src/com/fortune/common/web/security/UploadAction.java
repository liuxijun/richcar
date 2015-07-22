package com.fortune.common.web.security;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport {
	private static final long serialVersionUID = 572146812454l;
	private static final int BUFFER_SIZE = 16 * 1024;

	private File myFile;
	private String fileName;
	private String imageFileName;
	private String savePath;
	private String typeName;
	private String errorMsg;
	private boolean uploadSuccess = false;

	public void setMyFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	private boolean copy(File src, File dst) {
        boolean result = false;
		try {
			InputStream in = null;
			OutputStream out = null;
            String filePath = dst.getAbsolutePath();
            int i = filePath.lastIndexOf(File.separator);
            if(i>0){
                filePath = filePath.substring(0,i);
                File fileDir = new File(filePath);
                if(!fileDir.exists()){
                   if( fileDir.mkdirs()){

                   }else{
                       errorMsg="不能建立目录！";
                       return false;
                   }

                }
            }
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
                result = true;
                errorMsg = "上传完成！";
            }catch(Exception e){
                errorMsg = e.getMessage();
                result = false;
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            errorMsg = e.getMessage();
            return false;
		}
        return result;
	}

	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	@Override
	public String execute() {
        String extName = getExtention(fileName);
        if(extName ==null){
           extName = ""; 
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/yyyyMM/yyyyMMdd/");
        Date nowTime = new Date();
		imageFileName = formater.format(nowTime)+ nowTime.getTime()+"_"+
                Math.round(Math.random()*100000)+extName ;
		File imageFile = new File(ServletActionContext.getServletContext()
				.getRealPath(this.getSavePath()+ "/" + imageFileName));
		copy(myFile, imageFile);
		this.uploadSuccess = true;
		return SUCCESS;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isUploadSuccess() {
		return uploadSuccess;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    public String getErrorMsg(){
        return errorMsg;
    }
}
