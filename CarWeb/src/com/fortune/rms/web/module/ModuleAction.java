package com.fortune.rms.web.module;

import com.fortune.common.Constants;
import com.fortune.rms.business.module.logic.logicInterface.ModuleLogicInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.HibernateUtils;
import com.fortune.util.JsonUtils;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Namespace("/module")
@ParentPackage("default")
@Action(value="module")
public class ModuleAction extends BaseAction<Module> {
	private static final long serialVersionUID = 3243534534534534l;
	private ModuleLogicInterface moduleLogicInterface;
    //
	@SuppressWarnings("unchecked")
	public ModuleAction() {
		super(Module.class);
	}
	/**
	 * @param moduleLogicInterface the moduleLogicInterface to set
	 */
	public void setModuleLogicInterface(
			ModuleLogicInterface moduleLogicInterface) {
		this.moduleLogicInterface = moduleLogicInterface;
		setBaseLogicInterface(moduleLogicInterface);
	}

    public String save(){
        if(keys==null){
            keys = new ArrayList<String>();
        }
        obj = moduleLogicInterface.saveModule(obj,keys);
        setSuccess(true);
        writeSysLog("保存模版："+obj.getName()+",绑定了："+keys.size()+"个属性");
        return "save";
    }
    /**
     * 获取所有模板信息
     */
    public String searchModule(){
        pageBean.setOrderBy("id");
        objs = moduleLogicInterface.getModulesOfStatus(obj, pageBean);
        return Constants.ACTION_LIST;
    }

    public String deleteSelected() {
        log.debug("in deleteSelected");
        String dealMessage = "";

        String keyIds = this.getRequestParam("keyIds","");



        try{
            HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from PropertySelect ps where ps.propertyId in (select p.id from Property p where p.moduleId in ("+keyIds+"))");
        }catch(Exception e){
            e.printStackTrace();
            super.addActionError("无法删除数据PropertySelect" );
        }

        try{
            HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from Property p where p.moduleId in ("+keyIds+")");
        }catch(Exception e){
            e.printStackTrace();
            super.addActionError("无法删除数据Property" );

        }

        try{
            HibernateUtils.deleteAll(this.baseLogicInterface.getSession(),"delete from Module m where m.id in ("+keyIds+")");
        }catch(Exception e){
            e.printStackTrace();
            super.addActionError("无法删除数据Module" );

        }
           writeSysLog("删除所选资源模板： "+keyIds);
        super.addActionMessage("已经成功删除选择的数据(" + keyIds + ")");
        setSuccess(true);

        return Constants.ACTION_DELETE;
    }

    public String getCspModuleList(){
        long cspId = getRequestIntParam("cpId",0);
        try{
            objs = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from Module m where m.status=1 and m.id in (select cm.moduleId from CspModule cm where cm.cspId="+cspId+")");
        }catch(Exception e){
            e.printStackTrace();
        }
        return Constants.ACTION_LIST;
    }
    public String getCspModuleListV2(){
        long cspId = admin.getCspId();
        try{
            objs = HibernateUtils.findAll(this.baseLogicInterface.getSession(),"from Module m where m.status=1 and m.id in (select cm.moduleId from CspModule cm where cm.cspId="+cspId+")");
            if(objs == null){
                objs = new ArrayList<Module>(0);
            }
            if(pageBean == null){
                pageBean = new PageBean();
            }
            pageBean.setRowCount(objs.size());
        }catch(Exception e){
            e.printStackTrace();
        }
        return Constants.ACTION_LIST;
    }
}
