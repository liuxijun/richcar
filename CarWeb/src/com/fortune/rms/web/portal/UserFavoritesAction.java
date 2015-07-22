package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.util.JsonUtils;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Namespace("/portal")
@ParentPackage("default")
@Action(value = "userFavorites")
@Results({
        @Result(name = "userFavorites", location = "/page/hbMobile/userListContents.jsp")

})
public class UserFavoritesAction extends BaseAction<UserFavorites> {
    private static final long serialVersionUID = 3243534534534534l;
    private UserFavoritesLogicInterface userFavoritesLogicInterface;
    private String userTel;
    private UserLogicInterface userLogicInterface;

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    @SuppressWarnings("unchecked")
    public UserFavoritesAction() {
        super(UserFavorites.class);
    }

    public UserLogicInterface getUserLogicInterface() {
        return userLogicInterface;
    }

    @Autowired
    public void setUserLogicInterface(UserLogicInterface userLogicInterface) {
        this.userLogicInterface = userLogicInterface;
    }

    /**
     * @param userFavoritesLogicInterface the userFavoritesLogicInterface to set
     */
    @Autowired
    public void setUserFavoritesLogicInterface(
            UserFavoritesLogicInterface userFavoritesLogicInterface) {
        this.userFavoritesLogicInterface = userFavoritesLogicInterface;
        setBaseLogicInterface(userFavoritesLogicInterface);
    }
    public String save() {
        //http://localhost:8080/portal/userFavorites!save.action?obj.userId=telphoneNumber&obj.cotentId=1234
        if(obj.getUserId()==null){
            obj.setUserId((String)session.get(Constants.USER_PHONE_NUMBER));
        }
        if(!userFavoritesLogicInterface.hasFavoriteIt(obj.getUserId(),obj.getContentId())){
            obj.setUserIp(ServletActionContext.getRequest().getRemoteAddr());
            obj.setTime(new Date());
            obj = baseLogicInterface.save(obj);
            log.debug("用户的收藏已经保存：userId="+obj.getUserId()+",contentId="+obj.getContentId());
            setSuccess(true);
            super.addActionMessage("收藏成功！");
        }else{
            log.warn("用户的收藏已经存在：userId="+obj.getUserId()+",contentId="+obj.getContentId());
            setSuccess(false);
            super.addActionMessage("收藏失败！");
        }
        return "view";
    }
    public String  remove(){
        if(obj.getUserId()==null){
            obj.setUserId((String)session.get(Constants.USER_PHONE_NUMBER));
        }
        setSuccess(userFavoritesLogicInterface.removeFavorite(obj.getUserId(),obj.getContentId()));
        super.addActionMessage("取消收藏成功！");
        return "view";
    }

    public String hasFavoriteIt(){
        setSuccess(userFavoritesLogicInterface.hasFavoriteIt(obj.getUserId(),obj.getContentId()));
        return "view";
    }
    public String list() {
        objs = userFavoritesLogicInterface.getAllUserFavorites(obj, pageBean);
        return "list";
    }
    //删除保存
    private String  userTelephone;
    private Long  contentId;

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String removeFavorite(){
        if(userFavoritesLogicInterface.removeFavorite(userTelephone,contentId)){
                 getContentsOfUser();
        }
        return null;
    }


    public String searchUserFavoritesCount() {
        SearchResult<UserFavorites> searchResult = userFavoritesLogicInterface.getUserFavoritesCount(obj, pageBean);
        pageBean.setRowCount(searchResult.getRowCount());
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("objs", searchResult.getRows(), "totalCount", searchResult.getRowCount()));
        return null;
    }
    //查询个人收藏组合json格式传到页面
    public String getContentsOfUser(){
        contentsOfUser();
        return null;
        }
    public void contentsOfUser(){
        List<Content> list=userFavoritesLogicInterface.getContentsOfUser(userTelephone,new PageBean(0,10,"o1.time","asc"));
        String result = "{ \"totalCount\":\"" + list.size() + "\",\"success\":\"true\",\"objs\":[";
        if(list.size()>=1){
        for (Content contentMessage:list) {
            Long id=contentMessage.getId();
            String name = contentMessage.getName();
            String actors=contentMessage.getActors();
            String directors=contentMessage.getDirectors();
           // String mediaIntro = contentMessage.getIntro();
            Map<String,Object> properties = contentMessage.getProperties();
            String moveLength=(String)properties.get("MEDIA_LENGTH");
            if(StringUtils.string2int(moveLength, 0)>180){
                moveLength= StringUtils.formatTime(StringUtils.string2int(moveLength, 0));
            } else {
                moveLength= StringUtils.formatTime(StringUtils.string2int(moveLength, 0)*60);
            }
            if(name==null){
                name="";
            }
            if(name.length()>7){
                name=name.substring(0,7)+"..";
            }
            if(actors==null){
                actors = "无";
            }
            if(actors.length()>11){
                actors=actors.substring(0,10)+"..";
            }
            String postUrl =(String) properties.get("PC_MEDIA_POSTER_BIG");
            if(postUrl==null||"".equals(postUrl.trim())){
                postUrl =(String) properties.get("PC_MEDIA_POSTER_HORIZONTAL_BIG");
            }
            if(postUrl !=null&&!"".equals(postUrl.trim())){
            }else{
                postUrl = "";
            }
            result += "{" +
                    "\"id\":\"" + id+ "\"," +
                    "\"name\":\"" + name+ "\"," +
                    "\"actors\":\"" + actors + "\"," +
                    "\"moveLength\":\"" + moveLength + "\"," +
                    "\"directors\":\"" + directors + "\"," +
                    "\"postUrl\":\"" + postUrl + "\"" +
                    "},";
        }
        result = result.substring(0, result.length() - 1);
        }
        result += "]}";
        directOut(result);
    }
}
