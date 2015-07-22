package com.fortune.rms.web.module;

import com.fortune.common.Constants;
import com.fortune.rms.business.module.logic.logicInterface.PropertySelectLogicInterface;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Namespace("/module")
@ParentPackage("default")
@Action(value="propertySelect")
public class PropertySelectAction extends BaseAction<PropertySelect> {
	private static final long serialVersionUID = 3243534534534534l;
    private String moveTypeId;
	private PropertySelectLogicInterface propertySelectLogicInterface;
	@SuppressWarnings("unchecked")
	public PropertySelectAction() {
		super(PropertySelect.class);
	}
	/**
	 * @param propertySelectLogicInterface the propertySelectLogicInterface to set
	 */
    @Autowired
	public void setPropertySelectLogicInterface(
			PropertySelectLogicInterface propertySelectLogicInterface) {
		this.propertySelectLogicInterface = propertySelectLogicInterface;
		setBaseLogicInterface(propertySelectLogicInterface);
	}

    public String getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(String moveTypeId) {
        this.moveTypeId = moveTypeId;
    }
    public void  selectDifferenceType(String moveTypeId){
        List<PropertySelect> propertySelectList=propertySelectLogicInterface.searchMoveType(moveTypeId);
        int i=0;
        String result = "{ \"totalCount\":\"" + i + "\",\"success\":\"true\",\"objs\":[";
        if(propertySelectList.size()>=1){
            for(PropertySelect propertySelect:propertySelectList){
                 String name=propertySelect.getName();
                 Long selectId=propertySelect.getId();
                 int selectIdInt=selectId.intValue();
                if(selectIdInt!=24053&&selectIdInt!=24115&&selectIdInt!=24116&&selectIdInt!=24068&&selectIdInt!=24069&&selectIdInt!=24070&&selectIdInt!=24071&&selectIdInt!=24072&&selectIdInt!=24073&&selectIdInt!=24074
                        &&selectIdInt!=24075&&selectIdInt!=24076&&selectIdInt!=24077&&selectIdInt!=24078){
                        i++;
                        result += "{" +
                        "\"id\":\"" + selectIdInt+ "\"," +
                        "\"name\":\"" + name + "\"" +
                        "},";
                      }
            }
            result = result.substring(0, result.length() - 1);
        }
        result += "]}";
        //要重新申明一个字符串，不然替换不会改变
       String resultStr=result.replace("{ \"totalCount\":\"" + 0+ "\",","{ \"totalCount\":\"" + i+ "\",");
       directOut(resultStr);
    }
    public String searchMoveType(){
        selectDifferenceType(moveTypeId);
        return null;
    }
    public String changeDisplayOrder() {
        log.debug("propertySelect change display order");
        try {
            String uploadData = getRequestParam("uploadData","");
            if (!"".equals(uploadData)){
                String ss[] = uploadData.split(",");
                for (int i=0; i<ss.length; i++){
                    if (ss[i]!=null && ss[i].length()>0){
                        String sss[] = ss[i].split("_");
                        long key = Long.parseLong(sss[0]);
                        long displayOrder = Long.parseLong(sss[1]);
                        PropertySelect ps = propertySelectLogicInterface.get(key);
                        ps.setDisplayOrder(displayOrder);
                        propertySelectLogicInterface.save(ps);
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

    private Long moduleId;
    public String getPropertySelectsOfModule(){
        objs = propertySelectLogicInterface.getPropertySelectsOfModule(moduleId);
        pageBean.setRowCount(objs.size());
        return "list";
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

}
