package com.fortune.rms.web.publish;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.model.ContentNotice;
import com.fortune.rms.business.publish.logic.logicInterface.UserHotSearchLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;
import com.fortune.util.HibernateUtils;
import com.fortune.util.SearchCondition;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Namespace("/publish")
@ParentPackage("default")
@Action(value="userHotSearch")
public class UserHotSearchAction extends BaseAction<UserHotSearch> {
    private static final long serialVersionUID = 3243534534534534l;
    private UserHotSearchLogicInterface userHotSearchLogicInterface;
    private Integer hotSearchId;
    public Integer getHotSearchId() {
        return hotSearchId;
    }

    public void setHotSearchId(Integer hotSearchId) {
        this.hotSearchId = hotSearchId;
    }

    @SuppressWarnings("unchecked")
    public UserHotSearchAction() {
        super(UserHotSearch.class);
    }
    /**
     * @param userHotSearchLogicInterface the userHotSearchLogicInterface to set
     */
    @Autowired
    public void setUserHotSearchLogicInterface(
            UserHotSearchLogicInterface userHotSearchLogicInterface) {
        this.userHotSearchLogicInterface = userHotSearchLogicInterface;
        setBaseLogicInterface(userHotSearchLogicInterface);
    }
    public String getUserHotSearch(){
        log.debug("in getUserHotSearch");
        //index页面查询调用的方法
        objs = userHotSearchLogicInterface.getUserHotSearch();
        return "list";
    }

    public String saveAllDate() {
        log.debug("in saveAllData");
        Integer keyId = hotSearchId;
        String content = obj.getContent();
        Long adminId = obj.getAdminId();
        Long updateCount = obj.getUpdateCount();
        Date createTime = obj.getCreateTime();
        //如果keyId小于零或者为空，为新增。
        if(keyId ==null||keyId<=0){
            UserHotSearch uhs = new UserHotSearch();
            uhs.setAdminId(adminId);
            uhs.setContent(content);
            uhs.setCreateTime(createTime);
            uhs.setUpdateCount(updateCount);
            uhs.setSearchCountStatus(1);
            userHotSearchLogicInterface.save(uhs);
        } else{
            UserHotSearch uhs = userHotSearchLogicInterface.get(new Long(keyId));
            uhs.setAdminId(adminId);
            uhs.setContent(content);
            uhs.setCreateTime(createTime);
            uhs.setUpdateCount(updateCount);
            uhs.setSearchCountStatus(1);
            userHotSearchLogicInterface.save(uhs);
        }
        return Constants.ACTION_SAVE;
    }

    public String UserSearchHot_delete(){
        try{
            String keyIds = getRequestParam("keyId","");
           if(keyIds!=""&&keyIds!=null){
                String[] keyId = keyIds.split(",");
                for(String id :keyId){
                   long userHotSearchId = StringUtils.string2long(id,-1);
                    if(userHotSearchId!=-1){
                        userHotSearchLogicInterface.remove(userHotSearchId);
                    }
                }
               return Constants.ACTION_DELETE;
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    public String userHotSearch() {
        try {
            long type = getRequestIntParam("type",0);
            String sqlTable="";
            if(1==type){
               sqlTable = "select " +
                    "vl.id,vl.content,vl.createTime,vl.updateCount,vl.searchCountStatus" +
                    " from " +
                    " com.fortune.rms.business.publish.model.UserHotSearch vl "  +
                    " where vl.searchCountStatus = 1"  ;
            }
            if(2==type){
                sqlTable = "select " +
                        "vl.id,vl.adminId,vl.content,vl.createTime,vl.searchCount,vl.searchWeekCount,vl.searchMonthCount,vl.searchCountStatus" +
                        " from " +
                        "com.fortune.rms.business.publish.model.UserHotSearch vl "  ;
            }
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String createTime = getRequestParam("createTime", "");
            if (!"".equals(createTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.createTime = ? ",
                        new Timestamp(StringUtils.string2date(createTime + " 00:00:00").getTime()),
                        new DateType());
            }

            String contentName = getRequestParam("content", "");
            if (!"".equals(contentName)) {
                searchCondition.appendAndSqlCondition(
                        " vl.content like ? ",
                        "%" + contentName + "%",
                        new StringType());
            }
            String sql = searchCondition.getSqlStr();

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by " + orderBy + " " + orderDir;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);

            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRows(list1);
            searchResult.setRowCount(list1.size());
            String output = null;
            if(1==type){
                output = searchObjectsJbon(new String[]{"id", "content","vl.createTime","updateCount","searchCountStatus"}, searchResult);
            }
            if(2==type) {
                output = searchObjectsJbon(new String[]{"id", "adminId", "content","createTime","searchCount","searchWeekCount","searchMonthCount","searchCountStatus"}, searchResult);
            }

            directOut(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
