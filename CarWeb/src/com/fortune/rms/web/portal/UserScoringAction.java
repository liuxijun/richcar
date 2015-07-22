package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.rms.business.portal.logic.logicInterface.UserScoringLogicInterface;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.SearchResult;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
@Namespace("/portal")
@ParentPackage("default")
@Action(value="userScoring")
public class UserScoringAction extends BaseAction<UserScoring> {
    private static final long serialVersionUID = 3243534534534534l;
    private UserScoringLogicInterface userScoringLogicInterface;


    @SuppressWarnings("unchecked")
    public UserScoringAction() {
        super(UserScoring.class);
    }

    /**
     * @param userScoringLogicInterface the userScoringLogicInterface to set
     */
    @Autowired
    public void setUserScoringLogicInterface(
            UserScoringLogicInterface userScoringLogicInterface) {
        this.userScoringLogicInterface = userScoringLogicInterface;
        setBaseLogicInterface(userScoringLogicInterface);
    }


    public String save() {
        HttpServletRequest request = ServletActionContext.getRequest();
        obj.setUserIp(request.getRemoteAddr());
        obj.setTime(new Date());
        obj = userScoringLogicInterface.save(obj);
        writeSysLog("保存评分信息： "+obj.getContentName()+",userScoringId="+obj.getId()+",cspId="+obj.getCspId());
        super.addActionMessage("保存数据成功");
        return Constants.ACTION_SAVE ;

    }

    public String getUserIp(){
        HttpServletRequest request = ServletActionContext.getRequest();
        obj.setUserIp(request.getRemoteAddr());
        return Constants.ACTION_ADD;
    }
    @SuppressWarnings("unused")
    public String searchUserScoringCountByContentIdAndCspId() {
        Map<String, String> userScoringCounts = this.userScoringLogicInterface.getUserScoringCountByContentIdAndCspId(obj.getContentId(),obj.getCspId());
        JsonUtils jsonUtils = new JsonUtils();
        String temp = jsonUtils.getJson(userScoringCounts);
        directOut(temp);
        return null;
    }

    @SuppressWarnings("unused")
    public String searchUserScoringCount() {
        SearchResult<UserScoring> searchResult = userScoringLogicInterface.getAllUserScoringCount(obj, pageBean);
        pageBean.setRowCount(searchResult.getRowCount());
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("objs", searchResult.getRows(), "totalCount", searchResult.getRowCount()));
        return null;
    }

    public String list() {
        objs = userScoringLogicInterface.getUserScoringByContentIdAndCspId(obj, pageBean);
        return "list";
    }


    public String getUserScoring(){
        obj=userScoringLogicInterface.getUserScoring(obj.getId());
        return "view";
    }

}



