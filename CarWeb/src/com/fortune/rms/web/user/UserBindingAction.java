package com.fortune.rms.web.user;

import com.fortune.common.web.base.FortuneAction;
import com.fortune.rms.business.user.logic.logicInterface.UserBindingInterface;

import com.fortune.rms.business.user.model.AllResp;
import com.fortune.threeScreen.common.Constants;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.*;


@Namespace("/web")
@ParentPackage("threeScreen")
@Results({
        @Result(name = "list",location = "/common/threeScreenData.jsp")
})
@Action(value="userBinding-*")
public class UserBindingAction extends FortuneAction {
    private static final long serialVersionUID = 3243534534534534l;
    private UserBindingInterface userBindingInterface;
    
    public UserBindingInterface getUserBindingInterface() {
        return userBindingInterface;
    }

    public void setUserBindingInterface(UserBindingInterface userBindingInterface) {
        this.userBindingInterface = userBindingInterface;
    }

    public JSONObject jsonList;

    public JSONObject getJsonList() {
        return jsonList;
    }

    public void setJsonList(JSONObject jsonList) {
        this.jsonList = jsonList;
    }

    public String list(){
        int userType = Constants.USER_TYPE_PC;
        AllResp allResp = userBindingInterface.userBindingCheckRequest(userId,userType);
        jsonList = JSONObject.fromObject(allResp);
//        System.out.println(jsonList);
        return "list";
    }

}
