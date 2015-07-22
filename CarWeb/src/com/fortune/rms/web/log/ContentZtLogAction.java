package com.fortune.rms.web.log;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.log.logic.logicInterface.ContentZtLogLogicInterface;
import com.fortune.rms.business.log.model.ContentZtLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Namespace("/log")
@ParentPackage("default")
@Action(value="contentZtLog")
public class ContentZtLogAction extends BaseAction<ContentZtLog>{
    private static final long serialVersionUID = 3243534534534534l;
    private ContentZtLogLogicInterface contentZtLogLogicInterface;
    private long contentId;
    private long cspId;

    public ContentZtLogAction() {
        super(ContentZtLog.class);
    }

    @SuppressWarnings("unchecked")
    @Autowired
    public void setContentZtLogLogicInterface(ContentZtLogLogicInterface contentZtLogLogicInterface) {
        this.contentZtLogLogicInterface = contentZtLogLogicInterface;
        setBaseLogicInterface(contentZtLogLogicInterface);
    }

    public String save(){
        log.debug("in contentZtLog");
        long type = getRequestIntParam("type",0);
        ContentZtLog contentZtLog = new ContentZtLog();
        contentZtLog.setContentId(contentId);
        contentZtLog.setCreateTime(new Date());
        contentZtLog.setIsSpecial(1);
        contentZtLog.setCspId(cspId);
        contentZtLog.setType(type);
        contentZtLogLogicInterface.save(contentZtLog);
        return "success";
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getCspId() {
        return cspId;
    }

    public void setCspId(long cspId) {
        this.cspId = cspId;
    }
}
