package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewKeywordLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.SearchResult;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
@Namespace("/portal")
@ParentPackage("default")
@Action(value="userReviewKeyword")
public class UserReviewKeywordAction extends BaseAction<UserReviewKeyword> {
	private static final long serialVersionUID = 3243534534534534l;
	private UserReviewKeywordLogicInterface userReviewKeywordLogicInterface;
    private UserReviewKeyword userReviewKeyword;
	@SuppressWarnings("unchecked")
	public UserReviewKeywordAction() {
		super(UserReviewKeyword.class);
	}
	/**
	 * @param userReviewKeywordLogicInterface the userReviewKeywordLogicInterface to set
	 */
    @Autowired
	public void setUserReviewKeywordLogicInterface(
			UserReviewKeywordLogicInterface userReviewKeywordLogicInterface) {
		this.userReviewKeywordLogicInterface = userReviewKeywordLogicInterface;
		setBaseLogicInterface(userReviewKeywordLogicInterface);
	}

    public String save(){
       boolean isExistUserReviewKeyword = userReviewKeywordLogicInterface.isExistUserReviewKeyword(obj.getWord());
       if(isExistUserReviewKeyword){
           writeSysLog("影评关键字保存： "+obj.getId()+","+obj.getCspName()+","+obj.getWord());
            return  super.save();
       }else{
           super.addActionError("重复关键字");
           return Constants.ACTION_VIEW;
       }

    }

    public String list(){
        objs = userReviewKeywordLogicInterface.getAllUserReviewKeywords(obj,pageBean);
        return "list";
    }

    public UserReviewKeyword getUserReviewKeyword() {
        return userReviewKeyword;
    }

    public void setUserReviewKeyword(UserReviewKeyword userReviewKeyword) {
        this.userReviewKeyword = userReviewKeyword;
    }
}
