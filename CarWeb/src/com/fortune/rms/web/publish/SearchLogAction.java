package com.fortune.rms.web.publish;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.publish.logic.logicInterface.SearchLogicInterface;
import com.fortune.rms.business.publish.model.SearchLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: wang
 * Date: 13-10-23
 * Time: 下午5:25
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/publish")
@ParentPackage("default")
@Action(value="searchLog")
public class SearchLogAction  extends BaseAction<SearchLog> {

    private SearchLogicInterface searchLogicInterface;
    private String searchValue;
    private Long userTel;
    @SuppressWarnings("unchecked")
    public SearchLogAction() {
        super(SearchLog.class);
    }
    public SearchLogicInterface getSearchLogicInterface() {
        return searchLogicInterface;
    }
    /**
     * @param searchLogicInterface the userHotSearchLogicInterface to set
     */
    @Autowired
    public void setSearchLogicInterface(SearchLogicInterface searchLogicInterface) {
        this.searchLogicInterface = searchLogicInterface;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Long getUserTel() {
        return userTel;
    }

    public void setUserTel(Long userTel) {
        this.userTel = userTel;
    }
    public String addSearchLog(){
        if(userTel == null) {
            userTel=-1l;
        }
        boolean success=searchLogicInterface.addSearchLog(searchValue,userTel);
        if(success){
        String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"true\",\"error\":\"\",\"objs\":[]}";
        directOut(result);
        } else{
            String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":添加热词出现错误\"\",\"objs\":[]}";
            directOut(result);
        }
        return  null;
    }
}
