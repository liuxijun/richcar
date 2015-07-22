package com.fortune.rms.web.module;

import com.fortune.common.Constants;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.util.HibernateUtils;
import com.fortune.util.PageBean;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Namespace("/module")
@ParentPackage("default")
@Action(value="property")
public class PropertyAction extends BaseAction<Property> {
	private static final long serialVersionUID = 3243534534534534l;
	private PropertyLogicInterface propertyLogicInterface;
    
	@SuppressWarnings("unchecked")
	public PropertyAction() {
		super(Property.class);
	}
	/**
	 * @param propertyLogicInterface the propertyLogicInterface to set
	 */
	public void setPropertyLogicInterface(
			PropertyLogicInterface propertyLogicInterface) {
		this.propertyLogicInterface = propertyLogicInterface;
		setBaseLogicInterface(propertyLogicInterface);
	}


    private EncoderTemplateLogicInterface encoderTemplateLogicInterface;

    public void setEncoderTemplateLogicInterface(EncoderTemplateLogicInterface encoderTemplateLogicInterface) {
        this.encoderTemplateLogicInterface = encoderTemplateLogicInterface;
    }


    public String deleteSelected() {
        log.debug("in deleteSelected");
        String dealMessage = "";

        String keyIds = this.getRequestParam("keyIds","");


        try{
            HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from PropertySelect ps where ps.propertyId in ("+keyIds+")");
        }catch(Exception e){
            e.printStackTrace();
            super.addActionError("无法删除数据PropertySelect" );

        }

        try{
            HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from Property p where p.id in ("+keyIds+")");
        }catch(Exception e){
            e.printStackTrace();
            super.addActionError("无法删除数据Property" );

        }
            writeSysLog("删除所选属性： "+keyIds);
        super.addActionMessage("已经成功删除选择的数据(" + keyIds + ")");
        setSuccess(true);
          
        return Constants.ACTION_DELETE;
    }
    private List<PropertySelect> propertySelects;
    public String saveWithPropertySelects(){
        obj = propertyLogicInterface.saveWithPropertySelects(obj,propertySelects);
        return Constants.ACTION_SAVE;
    }

    public String changeDisplayOrder() {
        log.debug("property change display order");
        try {
            String uploadData = getRequestParam("uploadData","");
            if (!"".equals(uploadData)){
                String ss[] = uploadData.split(",");
                for (int i=0; i<ss.length; i++){
                    if (ss[i]!=null && ss[i].length()>0){
                        String sss[] = ss[i].split("_");
                        long key = Long.parseLong(sss[0]);
                        long displayOrder = Long.parseLong(sss[1]);
                        Property p = propertyLogicInterface.get(key);
                        p.setDisplayOrder(displayOrder);
                        propertyLogicInterface.save(p);
                    }
                }
            }
            writeSysLog("保存"+guessName(obj));
            super.addActionMessage("成功保存数据！");
        } catch (Exception e) {
            super.addActionError("保存数据发生异常：" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_SAVE;
    }

    //转码管理获取相对应的propertyIds
    public String getPropertyIdsByDataType() {
        //8、9、10分别为WMV，AVI，MP4的数据类型，注意这里的顺序、逻辑层里的参数不对应
        objs =  propertyLogicInterface.getPropertyIdsByDataType(8,9,10);
        return Constants.ACTION_LIST;
    }

    public String getPropertiesOfModule(){
        pageBean = new PageBean(0,10000,"o1.displayOrder asc,o1.name","asc");
        objs = propertyLogicInterface.getPropertiesOfModule(obj.getModuleId(),obj.getStatus(), obj.getDataType(), pageBean);
        return "list";
    }

    public String list(){
        objs = propertyLogicInterface.getPropertiesExcludeSomeProperties(obj,excludePropertyIds,pageBean);
        return "list";
    }
    private String excludePropertyIds;

    public String getExcludePropertyIds() {
        return excludePropertyIds;
    }

    public void setExcludePropertyIds(String excludePropertyIds) {
        this.excludePropertyIds = excludePropertyIds;
    }

    public List<PropertySelect> getPropertySelects() {
        return propertySelects;
    }

    public void setPropertySelects(List<PropertySelect> propertySelects) {
        this.propertySelects = propertySelects;
    }

}
