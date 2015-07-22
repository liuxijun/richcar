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
 * Created by ����· on 2014/11/27.
 * ϵͳ���Ի�����
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
     * ����logo���ݣ��ͻ��˽�logo����base64�󣬴�������������ļ�������¼·��
     * @return "logoData"
     */
    @Action(value = "logoData")
    public String uploadLogoData() throws IOException{
        if( logoBase64Data == null || mobileLogoBase64Data == null ){
            setSuccess(false);
            addActionError("Logo����Ϊ��");
        }else{
            // �ص�ͷ�ϵ�data:image/png;base64
            String base64 = logoBase64Data.substring(logoBase64Data.indexOf(",")+1);
            byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64 .getBytes());

            String logoPath = getLogoPath();
            // logoPathָ����Ŀ¼�ľ���Ŀ¼
            ServletContext context = ServletActionContext.getServletContext();
            String absLogoPath = context.getRealPath(logoPath);
            FileUtils.writeByteArrayToFile(new File(absLogoPath), decoded);
            obj.setLogoPath(logoPath);

            // added by mlwang, @2015-2-25�������ֻ���logo����
            base64 = mobileLogoBase64Data.substring(mobileLogoBase64Data.indexOf(",")+1);
            decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64 .getBytes());

            String mobileLogoPath = getLogoPath();
            // logoPathָ����Ŀ¼�ľ���Ŀ¼
            absLogoPath = context.getRealPath(mobileLogoPath);
            FileUtils.writeByteArrayToFile(new File(absLogoPath), decoded);
            obj.setMobileLogoPath(mobileLogoPath);

            setSuccess(true);

        }
        return SUCCESS;
    }

    /**
     * ��ָ����λ��������һ��logo·�������ظ�
     * @return String
     */
    private String getLogoPath() throws IOException{
        // ��ȡ����·��
        ServletContext context = ServletActionContext.getServletContext();
        //String absLogoFolder = new File("/images/individual/").getAbsolutePath();
        String absLogoFolder = context.getRealPath(INDIVIDUAL_LOGO_REL_PATH);
        if( !absLogoFolder.endsWith("/") && !absLogoFolder.endsWith("\\")){
            absLogoFolder += File.separator;
        }
        FileUtils.forceMkdir(new File(absLogoFolder));
        // ����������׺��ֱ���ҵ�û�õ���
        String relPath;
        for(int i=1;;i++){
            relPath = i + ".png";
            if( !(new File(absLogoFolder + relPath)).exists()){
                break;
            }
        }

        // �������·��
        return INDIVIDUAL_LOGO_REL_PATH + relPath;
    }

    /**
     * ������Ի����ã�����logo��ϵͳ����
     * @return SUCCESS
     */
    public String saveIndividual(){
        if( obj.getLogoPath() == null || obj.getMobileLogoPath() == null || obj.getName() == null){
            setSuccess(false);
            addActionError("logo·����ϵͳ����Ϊ��");
        }else{
            boolean hasError = !individualLogicInterface.saveIndividual(obj);
            setSuccess(!hasError);
            if(hasError) addActionError("������Ի�����ʱ��������");
        }

        return SUCCESS;
    }

    /**
     * ������õĸ��Ի���Ϣ
     * @return success
     */
    public String getIndividual(){
        List<Individual> individualList = individualLogicInterface.getAll();
        if( individualList != null && individualList.size() > 0){
            obj = individualList.get(0);
        }else {
            // ���ؿջᵼ��json��ʽ�������⣬data�� ��Ϊ�գ�Json�޷�����
            //obj = null;
            obj = new Individual();
            obj.setLogoPath("/images/logo_redex.png");
            obj.setName("Redex");
        }

        return SUCCESS;
    }
}
