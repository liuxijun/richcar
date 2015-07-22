package com.fortune.rms.web.user;

import com.fortune.common.web.base.BaseAction;
import com.fortune.common.web.base.FortuneAction;
import com.fortune.rms.business.user.logic.logicInterface.UserFriendLogicInterface;
import com.fortune.rms.business.user.model.AllResp;

import com.fortune.rms.business.user.model.UserFriend;
import com.fortune.threeScreen.common.Constants;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.*;

@Namespace("/web")
@ParentPackage("threeScreen")
@Results({
        @Result(name = "list",location = "/common/threeScreenData.jsp")
})
@Action(value="userFriend-*")
public class UserFriendAction extends UnicomAction<UserFriend> {
    private static final long serialVersionUID = 3243534534534534l;
    private UserFriendLogicInterface userFriendLogicInterface;
    private String playUserId;

    public UserFriendAction(Class<UserFriend> persistentClass) {
        super(persistentClass);
    }


    public void setUserFriendLogicInterface(UserFriendLogicInterface userFriendLogicInterface) {
        this.userFriendLogicInterface = userFriendLogicInterface;
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
        if(userId==null){
            userId= playUserId;
        }
        AllResp allFriendResp = userFriendLogicInterface.getAllFriend(userId, userType);
        jsonList = JSONObject.fromObject(allFriendResp);
//        System.out.println(jsonList);
        log.debug(userId+"的好友列表:"+jsonList);
        return "list";
    }

    public String getPlayUserId() {
        return playUserId;
    }

    public void setPlayUserId(String playUserId) {
        this.playUserId = playUserId;
    }
}
