package com.fortune.rms.web.template;

import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.JsonUtils;
import com.fortune.util.SimpleFileInfo;
import com.opensymphony.xwork2.ActionContext;
import com.fortune.util.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.Map;
@Namespace("/template")
@ParentPackage("default")
@Results({
        @Result(name = "listFiles",location = "/template/TemplateJsonList.jsp")
})
@Action(value="template")
public class TemplateAction extends BaseAction<Template> {
    private static final long serialVersionUID = 3243534534534534l;
    private TemplateLogicInterface templateLogicInterface;

    public String filePath;
    public String filter;

    List<SimpleFileInfo> list;

    @SuppressWarnings("unchecked")
    public TemplateAction() {
        super(Template.class);
    }

    /**
     * @param templateLogicInterface the templateLogicInterface to set
     */
    @Autowired
    public void setTemplateLogicInterface(TemplateLogicInterface templateLogicInterface) {
        this.templateLogicInterface = templateLogicInterface;
        setBaseLogicInterface(templateLogicInterface);
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String listFiles() {

        String newFilePath = "page/";
        String alias ="";
        Map<String, Object> session = ActionContext.getContext().getSession();
        long cspId = -1;
        int isRoot = 0;
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                cspId = admin.getCspId();
                isRoot = admin.getIsRoot();
            }
        }

        if (isRoot == 1) {
            newFilePath = "/";
        }else if(isRoot != 1 && cspId > 0){
            alias = this.templateLogicInterface.getCspAliasByCspId(cspId);
            if (alias != null) {

                newFilePath += alias;

            }
        } else{
            setSuccess(false);
        }
        newFilePath +=  filePath;

        ServletContext application = ServletActionContext.getServletContext();
        try {
            org.apache.commons.io.FileUtils.forceMkdir(new File(application.getRealPath(newFilePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (filter == null) {
            filter = "*.*";
        }
        if (pageBean.getOrderBy() == null) {
            pageBean.setOrderBy("name");
        }
        list = FileUtils.listFiles(application.getRealPath(newFilePath), filter, pageBean, false);
        return "listFiles";
    }

    public String getListJson() {
        return JsonUtils.getListJsonString("objs", list, "totalCount", pageBean.getRowCount());
    }
}