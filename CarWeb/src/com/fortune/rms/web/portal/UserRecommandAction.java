package com.fortune.rms.web.portal;

import com.fortune.rms.business.portal.logic.logicInterface.UserRecommandLogicInterface;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.SearchResult;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
@Namespace("/portal")
@ParentPackage("default")
@Action(value="userRecommand")
public class UserRecommandAction extends BaseAction<UserRecommand> {
	private static final long serialVersionUID = 3243534534534534l;
	private UserRecommandLogicInterface userRecommandLogicInterface;
	@SuppressWarnings("unchecked")
	public UserRecommandAction() {
		super(UserRecommand.class);
	}
	/**
	 * @param userRecommandLogicInterface the userRecommandLogicInterface to set
	 */
    @Autowired
	public void setUserRecommandLogicInterface(
			UserRecommandLogicInterface userRecommandLogicInterface) {
		this.userRecommandLogicInterface = userRecommandLogicInterface;
		setBaseLogicInterface(userRecommandLogicInterface);
	}

        public String save(){
        try{
            InetAddress address = InetAddress.getLocalHost();
             obj.setUserIp(address.getHostAddress());
            obj.setTime(new Date());
           writeSysLog("±£´æ"+obj.getContentName()+","+obj.getCspName()+","+obj.getUserIp());
        }catch(UnknownHostException e){
             e.printStackTrace();
        }
        return super.save();
    }

    public String list(){
         objs = userRecommandLogicInterface.getAllUserRecommand(obj,pageBean);
        return "list";
    }


    public String searchUserRecommandCount(){
       SearchResult<UserRecommand> searchResult=userRecommandLogicInterface.getUserRecommandCount(obj,pageBean);
        pageBean.setRowCount(searchResult.getRowCount());
        JsonUtils jsonUtils=new JsonUtils();
        directOut(jsonUtils.getListJson("objs",searchResult.getRows(),"totalCount",searchResult.getRowCount()));
        return null;
    }

}
