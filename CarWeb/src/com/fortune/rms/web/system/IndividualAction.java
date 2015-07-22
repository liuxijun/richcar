package com.fortune.rms.web.system;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.system.logic.logicInterface.IndividualLogicInterface;
import com.fortune.rms.business.system.model.Individual;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 王明路 on 2014/11/27.
 * 系统个性化处理
 */
@Namespace("/system")
@ParentPackage("default")
@Action(value = "individual")
public class IndividualAction extends BaseAction<Individual> {
    public static final String INDIVIDUAL_LOGO_REL_PATH = "/images/individual/";
    public IndividualAction() {
        super(Individual.class);
    }

    private IndividualLogicInterface individualLogicInterface;

    public void setIndividualLogicInterface(IndividualLogicInterface individualLogicInterface) {
        this.individualLogicInterface = individualLogicInterface;
        setBaseLogicInterface(individualLogicInterface);
    }

    private String logoBase64Data;
    private String mobileLogoBase64Data;

    public String getMobileLogoBase64Data() {
        return mobileLogoBase64Data;
    }

    public void setMobileLogoBase64Data(String mobileLogoBase64Data) {
        this.mobileLogoBase64Data = mobileLogoBase64Data;
    }

    public void setLogoBase64Data(String logoBase64Data) {
        this.logoBase64Data = logoBase64Data;
    }

    /**
     * 保存logo数据，客户端将logo数据base64后，传进来，保存成文件，并记录路径
     * @return "logoData"
     */
    @Action(value = "logoData")
    public String uploadLogoData() throws IOException{
        if( logoBase64Data == null || mobileLogoBase64Data == null ){
            setSuccess(false);
            addActionError("Logo数据为空");
        }else{
            // 截掉头上的data:image/png;base64
            String base64 = logoBase64Data.substring(logoBase64Data.indexOf(",")+1);
            byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64 .getBytes());

            String logoPath = getLogoPath();
            // logoPath指定的目录的绝对目录
            ServletContext context = ServletActionContext.getServletContext();
            String absLogoPath = context.getRealPath(logoPath);
            FileUtils.writeByteArrayToFile(new File(absLogoPath), decoded);
            obj.setLogoPath(logoPath);

            // added by mlwang, @2015-2-25，增加手机版logo保存
            base64 = mobileLogoBase64Data.substring(mobileLogoBase64Data.indexOf(",")+1);
            decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64 .getBytes());

            String mobileLogoPath = getLogoPath();
            // logoPath指定的目录的绝对目录
            absLogoPath = context.getRealPath(mobileLogoPath);
            FileUtils.writeByteArrayToFile(new File(absLogoPath), decoded);
            obj.setMobileLogoPath(mobileLogoPath);

            setSuccess(true);

        }
        return SUCCESS;
    }

    /**
     * 在指定的位置新生成一个logo路径，不重复
     * @return String
     */
    private String getLogoPath() throws IOException{
        // 获取绝对路径
        ServletContext context = ServletActionContext.getServletContext();
        //String absLogoFolder = new File("/images/individual/").getAbsolutePath();
        String absLogoFolder = context.getRealPath(INDIVIDUAL_LOGO_REL_PATH);
        if( !absLogoFolder.endsWith("/") && !absLogoFolder.endsWith("\\")){
            absLogoFolder += File.separator;
        }
        FileUtils.forceMkdir(new File(absLogoFolder));
        // 用数字做后缀，直到找到没用到的
        String relPath;
        for(int i=1;;i++){
            relPath = i + ".png";
            if( !(new File(absLogoFolder + relPath)).exists()){
                break;
            }
        }

        // 返回相对路径
        return INDIVIDUAL_LOGO_REL_PATH + relPath;
    }

    /**
     * 保存个性化设置，包括logo和系统名称
     * @return SUCCESS
     */
    public String saveIndividual(){
        if( obj.getLogoPath() == null || obj.getMobileLogoPath() == null || obj.getName() == null){
            setSuccess(false);
            addActionError("logo路径或系统名称为空");
        }else{
            boolean hasError = !individualLogicInterface.saveIndividual(obj);
            setSuccess(!hasError);
            if(hasError) addActionError("保存个性化数据时发生错误");
        }

        return SUCCESS;
    }

    /**
     * 获得设置的个性化信息
     * @return success
     */
    public String getIndividual(){
        List<Individual> individualList = individualLogicInterface.getAll();
        if( individualList != null && individualList.size() > 0){
            obj = individualList.get(0);
        }else {
            // 返回空会导致json格式出现问题，data后 ，为空，Json无法解析
            //obj = null;
            obj = new Individual();
            obj.setLogoPath("/images/logo_redex.png");
            obj.setName("Redex");
        }

        return SUCCESS;
    }
}
