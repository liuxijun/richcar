package com.fortune.cars.web.cars;

import com.fortune.util.BeanUtils;
import com.fortune.util.FileUtils;
import com.fortune.util.MD5Utils;
import com.fortune.util.StringUtils;
import com.fortune.util.net.URLEncoder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface;
import com.fortune.cars.business.cars.model.Car;
import com.fortune.common.web.base.BaseAction;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Namespace("/cars")
@ParentPackage("default")
@Action(value = "car")
@SuppressWarnings("unused")
public class CarAction extends BaseAction<Car> {
	private static final long serialVersionUID = 32335395539539l;
	private CarLogicInterface carLogicInterface;
	@SuppressWarnings("unchecked")
	public CarAction() {
		super(Car.class);
	}
	/**
	 * @param carLogicInterface the carLogicInterface to set
	 */
	@SuppressWarnings("unchecked")
	public void setCarLogicInterface(CarLogicInterface carLogicInterface) {
		this.carLogicInterface = carLogicInterface;
		setBaseLogicInterface(carLogicInterface);
	}
    private String checkFile(File file,String propertyName,String fileName){
        String url = null;
        if(file!=null&&file.length()>0){
            if(fileName==null){
                fileName = "1.jpg";
            }
			try {
				fileName = FileUtils.extractFileName(fileName,"/");
				fileName = URLEncoder.encode(fileName,"UTF-8").replace("%","");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url = "/upload/"+StringUtils.date2string(new Date(),"yyyy/MM/dd")+"/"+Math.round(Math.random()*1000000)+"_"+ fileName;
            HttpServletRequest request = ServletActionContext.getRequest();
            File dstFile = new File(request.getRealPath(url));
            File path = dstFile.getParentFile();
            if(!path.exists()){
                if(!path.mkdirs()){
                    log.error("无法创建目录："+path.getAbsolutePath()+",上传的图片可能无法正确保存："+dstFile.getAbsolutePath());
                }
            }
            if(!file.renameTo(dstFile)){
                log.error("保存上传文件时发生异常，不能继续："+dstFile.getAbsolutePath());
            }else{
                log.debug("已经成功保存文件："+propertyName+"<-"+dstFile.getAbsolutePath());
                BeanUtils.setProperty(obj,propertyName,url,String.class);
            }
        }else{
            log.debug("没有上传"+propertyName);
        }
        return url;
    }
	public String save(){
        checkFile(fileOfCarPictureBack,"carPictureBack",fileNameOfCarPictureBack);
        checkFile(fileOfCarPictureFront,"carPictureFront",fileNameOfCarPictureFront);
        checkFile(fileOfCarPictureLeft,"carPictureLeft",fileNameOfCarPictureLeft);
        checkFile(fileOfCarPictureRight,"carPictureRight",fileNameOfCarPictureRight);
        checkFile(fileOfCarPictureTop,"carPictureTop",fileNameOfCarPictureTop);
        checkFile(fileOfCarPictureBottom, "carPictureBottom", fileNameOfCarPictureBottom);
        BeanUtils.setDefaultValue(obj,"createTime",new Date());
		if(obj.getPassword()==null||obj.getPassword().trim().equals("")){
			String phone = obj.getPhone();
			String defaultPwd="888888";
			if(phone!=null){
				int l = phone.length();
				if(l>6){
					defaultPwd = phone.substring(l-6);
				}else{
					defaultPwd = phone;
				}
			}
            try {
                log.debug("没有口令，设置为缺省口令："+defaultPwd);
                defaultPwd = MD5Utils.getMD5String(defaultPwd);
            } catch (NoSuchAlgorithmException e) {
                log.error("计算MD5时发生异常："+e.getLocalizedMessage());
            }
            obj.setPassword(defaultPwd);
		}
		log.debug("将保存：car=" + obj.getCarNo());
		super.save();
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("location","cars.jsp");
		return "redirect";
	}
	private File fileOfCarPictureTop;
	private File fileOfCarPictureLeft;
	private File fileOfCarPictureRight;
	private File fileOfCarPictureBottom;
	private File fileOfCarPictureFront;
	private File fileOfCarPictureBack;
	private String fileNameOfCarPictureTop;
	private String fileNameOfCarPictureLeft;
	private String fileNameOfCarPictureRight;
	private String fileNameOfCarPictureBottom;
	private String fileNameOfCarPictureFront;
	private String fileNameOfCarPictureBack;

	public File getFileOfCarPictureTop() {
		return fileOfCarPictureTop;
	}

	public void setFileOfCarPictureTop(File fileOfCarPictureTop) {
		this.fileOfCarPictureTop = fileOfCarPictureTop;
	}

	public File getFileOfCarPictureLeft() {
		return fileOfCarPictureLeft;
	}

	public void setFileOfCarPictureLeft(File fileOfCarPictureLeft) {
		this.fileOfCarPictureLeft = fileOfCarPictureLeft;
	}

	public File getFileOfCarPictureRight() {
		return fileOfCarPictureRight;
	}

	public void setFileOfCarPictureRight(File fileOfCarPictureRight) {
		this.fileOfCarPictureRight = fileOfCarPictureRight;
	}

	public File getFileOfCarPictureBottom() {
		return fileOfCarPictureBottom;
	}

	public void setFileOfCarPictureBottom(File fileOfCarPictureBottom) {
		this.fileOfCarPictureBottom = fileOfCarPictureBottom;
	}

	public File getFileOfCarPictureFront() {
		return fileOfCarPictureFront;
	}

	public void setFileOfCarPictureFront(File fileOfCarPictureFront) {
		this.fileOfCarPictureFront = fileOfCarPictureFront;
	}

	public File getFileOfCarPictureBack() {
		return fileOfCarPictureBack;
	}

	public void setFileOfCarPictureBack(File fileOfCarPictureBack) {
		this.fileOfCarPictureBack = fileOfCarPictureBack;
	}

	public String getFileNameOfCarPictureTop() {
		return fileNameOfCarPictureTop;
	}

	public void setFileNameOfCarPictureTop(String fileNameOfCarPictureTop) {
		this.fileNameOfCarPictureTop = fileNameOfCarPictureTop;
	}

	public String getFileNameOfCarPictureLeft() {
		return fileNameOfCarPictureLeft;
	}

	public void setFileNameOfCarPictureLeft(String fileNameOfCarPictureLeft) {
		this.fileNameOfCarPictureLeft = fileNameOfCarPictureLeft;
	}

	public String getFileNameOfCarPictureRight() {
		return fileNameOfCarPictureRight;
	}

	public void setFileNameOfCarPictureRight(String fileNameOfCarPictureRight) {
		this.fileNameOfCarPictureRight = fileNameOfCarPictureRight;
	}

	public String getFileNameOfCarPictureBottom() {
		return fileNameOfCarPictureBottom;
	}

	public void setFileNameOfCarPictureBottom(String fileNameOfCarPictureBottom) {
		this.fileNameOfCarPictureBottom = fileNameOfCarPictureBottom;
	}

	public String getFileNameOfCarPictureFront() {
		return fileNameOfCarPictureFront;
	}

	public void setFileNameOfCarPictureFront(String fileNameOfCarPictureFront) {
		this.fileNameOfCarPictureFront = fileNameOfCarPictureFront;
	}

	public String getFileNameOfCarPictureBack() {
		return fileNameOfCarPictureBack;
	}

	public void setFileNameOfCarPictureBack(String fileNameOfCarPictureBack) {
		this.fileNameOfCarPictureBack = fileNameOfCarPictureBack;
	}
}
