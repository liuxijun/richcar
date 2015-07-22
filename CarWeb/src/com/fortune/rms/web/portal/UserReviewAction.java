package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.model.ContentServiceProduct;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewKeywordLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewLogicInterface;
import com.fortune.rms.business.portal.model.UserReview;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.SearchResult;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Namespace("/portal")
@ParentPackage("default")
@Action(value="userReview")
public class UserReviewAction extends BaseAction<UserReview> {
    private static final long serialVersionUID = 3243534534534534l;
    private UserReviewLogicInterface userReviewLogicInterface;

    private String verifyCode;
    private String contentName;
    private String desp;
    private Long status;
    private Long cspId;


    @SuppressWarnings("unchecked")
    public UserReviewAction() {
        super(UserReview.class);
    }

    /**
     * @param userReviewLogicInterface the userReviewLogicInterface to set
     */
    @Autowired
    public void setUserReviewLogicInterface(
            UserReviewLogicInterface userReviewLogicInterface) {
        this.userReviewLogicInterface = userReviewLogicInterface;
        setBaseLogicInterface(userReviewLogicInterface);
    }


    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }



    public String save() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String desp = "";
        String userId = "";

        if ((verifyCode == null || !verifyCode.equalsIgnoreCase((String) session.get("verifyCode")))) {

            setSuccess(false);
            return SUCCESS;
        } else {
            try {
                desp = new String(obj.getDesp().getBytes("iso8859-1"), "utf-8");
                userId = new String(obj.getUserId().getBytes("iso8859-1"), "utf-8");
            } catch (Exception e) {
            }
            obj.setDesp(desp);
            obj.setUserId(userId);
            obj.setUserIp(request.getRemoteAddr());
            obj.setTime(new Date());
            obj.setStatus(0L);
            userReviewLogicInterface.saveUserReviewByKeyWord(obj);
            writeSysLog("保存影评关键字： "+obj.getUserId()+",desp="+obj.getDesp()+",状态 "+obj.getStatus());
            setSuccess(true);
            return Constants.ACTION_SAVE;
        }
    }
    public String save1() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String desp = "";
        String userId = (String)session.get("userId");
        try {
            desp = new String(obj.getDesp().getBytes("iso8859-1"), "utf-8");
            //userId = new String(obj.getUserId().getBytes("iso8859-1"), "utf-8");
        } catch (Exception e) {
        }
        obj.setDesp(desp);
        obj.setUserId(userId);
        obj.setUserIp(request.getRemoteAddr());
        obj.setTime(new Date());
        obj.setStatus(0L);
        userReviewLogicInterface.saveUserReviewByKeyWord(obj);
//        writeSysLog("保存影评关键字： "+obj.getUserId()+",desp="+obj.getDesp()+",状态 "+obj.getStatus());
        setSuccess(true);
        return Constants.ACTION_SAVE;

    }

    public String changeUserReview(){
        if(obj.getStatus()==null){
            obj.setStatus(1L);
        }
         DateFormat df=new   SimpleDateFormat( "yyyy-MM-dd   hh:mm:ss ");
          String   from=df.format(obj.getTime());
         this.userReviewLogicInterface.changeUserReviewStatus(obj.getId(),obj.getStatus(),obj.getDesp(),from);
        writeSysLog("影评信息保存： "+obj.getId()+","+obj.getCspName()+","+obj.getContentName()+",当前状态"+obj.getStatus());
        return Constants.ACTION_SAVE;
    }

    public String searchUserReviewsByContentIdAndCspId(){
        objs = userReviewLogicInterface.searchUserReviewByContentIdAndCspId(obj.getContentId(),obj.getCspId());
        return Constants.ACTION_LIST;
    }

    public String searchUserReviewsByContentIdAndCspId1(){
        objs = userReviewLogicInterface.searchUserReviewByContentIdAndCspId1(obj.getContentId(),obj.getCspId());
        return Constants.ACTION_LIST;
    }

    public String searchLastUserReviewsByContentIdAndCspId(){
        obj  = userReviewLogicInterface.searchLastUserReviewsByContentIdAndCspId(obj.getContentId(),obj.getCspId());
        return Constants.ACTION_VIEW;
    }

    public Long getCspId() {
        return cspId;
    }

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    public String list() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if(spId == 1){
                    spId = -1;
                }else{
                    obj.setCspId(spId);
                }
            }
        }
        obj.setContentName(contentName);
        obj.setDesp(desp);
        obj.setStatus(status);
        objs = userReviewLogicInterface.getAllUserReviews(obj, pageBean);
        return "list";
    }


    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String  getUserReview(){
          obj=userReviewLogicInterface.getUserReview(obj.getId());
        return "view";
    }
}
