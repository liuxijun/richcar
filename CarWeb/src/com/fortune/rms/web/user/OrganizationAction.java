package com.fortune.rms.web.user;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.frontuser.logic.logicInterface.OrganizationLogicInterface;
import com.fortune.rms.business.frontuser.model.OrgChannelMap;
import com.fortune.rms.business.frontuser.model.Organization;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.AppConfigurator;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 15:15:37
 * 组织管理相关
 */
@Namespace("/user")
@ParentPackage("default")
@Action(value = "org")
@Results({
        @Result(name = "orgTree",location = "/sys/orgTree.jsp"),
        @Result(name = "success",location = "/common/jsonMessages.jsp")
})

public class OrganizationAction  extends BaseAction<Organization> {
    private OrganizationLogicInterface organizationLogicInterface;

    public void setOrganizationLogicInterface(OrganizationLogicInterface organizationLogicInterface) {
        this.organizationLogicInterface = organizationLogicInterface;
    }

    public OrganizationAction() {
        super(Organization.class);
    }

    private String orgTreeStr;
    private long channelId;     // 关联组织的栏目Id
    private boolean childrenIncluded;   // 是否包括其子组织

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public boolean isChildrenIncluded() {
        return childrenIncluded;
    }

    public void setChildrenIncluded(boolean childrenIncluded) {
        this.childrenIncluded = childrenIncluded;
    }

    public String getOrgTreeStr() {
        return orgTreeStr;
    }

    public String orgTree(){
        // 查询出所有的用户组织和频道的对应关系
        List<OrgChannelMap> mapList = organizationLogicInterface.getOrgChannelMap( OrganizationLogicInterface.MAP_BY_ORG );
        // 查询处所有的用户组的用户人数（用于判断是否可以删除用户组）
        orgTreeStr = JsonUtils.getJsonString(getTree(-1, mapList));
        return "orgTree";
    }

    public List<Map<String,Object>> getTree(long parentId, List<OrgChannelMap> mapList){
        List<Map<String,Object>> all = new ArrayList<Map<String, Object>>();

        List<Organization> organizations = organizationLogicInterface.getOrgList(""+parentId);
        for(Organization organization: organizations){
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("id",organization.getId());
            result.put("name",organization.getName());
            result.put("channels", getChannelList(mapList, organization.getId()));
            result.put("count", organizationLogicInterface.getOrganizationUserCount(organization.getId()));
            List<Map<String,Object>> children = getTree(organization.getId(), mapList);
            if(children!=null&&children.size()>0){
                result.put("children",children);
            }
            all.add(result);
        }
        return all;
    }

    private String getChannelList(List<OrgChannelMap> mapList, Long orgId){
        if(mapList == null || mapList.size() == 0) return "";

        for(OrgChannelMap m : mapList){
            if( m.getId().equals(orgId) ) return m.getValue();
        }

        return "";
    }

    public String getSerialisedOrg() {
        return serialisedOrg;
    }

    public void setSerialisedOrg(String serialisedOrg) {
        this.serialisedOrg = serialisedOrg;
    }

    private String serialisedOrg;
    List<Organization> orgList;      // 要提交给Logic处理的组织机构信息
    @SuppressWarnings("unchecked")
    public String synchronize(){
        if(serialisedOrg != null){
            // 解析成json对象，放到数组中和数据库同步
            // serialisedChannel是一个以[]包裹的json数组
            log.debug(serialisedOrg);
            try{
                orgList = new ArrayList();
                //JSONObject channelJson = new JSONObject("{\"channels\":" + serialisedChannel + "}");
                JSONArray jsonArray = new JSONArray(serialisedOrg);
                log.debug("array length:" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    parseOrganization(-1L, (JSONObject) jsonArray.get(i));
                }

                // 调用真正的保存，在这里**********##########￥￥￥￥￥￥￥￥￥￥￥￥￥
                //channelLogicInterface.saveChannels(channelList,cspId);
                orgList = organizationLogicInterface.saveOrganizations(orgList);
                setSuccess(true);

                writeSysLog("修改组织机构");

                //addActionError("搞定!");
            }catch(JSONException e){
                setSuccess(false);
                addActionError(e.getMessage());
            }
        }

        return "success";
    }

    private void parseOrganization(Long parentId, JSONObject orgObj){
        if( orgObj == null ) return;

        try{
            Long orgId = Long.parseLong(orgObj.get("id").toString());
            String orgName = "";
            try {
                orgName = java.net.URLDecoder.decode(orgObj.get("name").toString(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String channels = orgObj.get("channels").toString();
            
            Organization org = new Organization();
            org.setId(orgId);
            org.setName(orgName);
            org.setParentId(parentId);
            org.setChannels(channels);
            orgList.add(org);

            log.debug("把【" + orgName + "】添加到列表中了， 现在有：" + orgList.size() + " 个组织了。");

            // 解析子频道
            if(orgObj.has("children")){
                JSONArray jsonArray = new JSONArray(orgObj.get("children").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    parseOrganization(orgId, (JSONObject) jsonArray.get(i));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关联栏目到组织，可能包括其子组织
     * @return SUCCESS
     */
    public String referenceChannelOrg(){
        if( obj != null ){
            setSuccess(true);
            writeSysLog("栏目绑定到组织机构，栏目ID:" + channelId + ",组织机构ID:" + childrenIncluded);
            organizationLogicInterface.referenceChannelOrg(obj.getId(), channelId, childrenIncluded);
        }else{
            setSuccess(false);
            obj = new Organization();
            obj.setId(428L);
        }
        return SUCCESS;
    }

    /**
     * 关联栏目到所有组织
     * @return SUCCESS
     */
    public  String referenceChannelAllOrg(){
        writeSysLog("栏目绑定到所有组织机构，栏目ID:"+channelId);
        organizationLogicInterface.referenceChannelAllOrg(channelId);
        setSuccess(true);
        if(obj == null){ // 防止json在obj为空时返回的数据不合法
            obj = new Organization();
            obj.setId(428L);
        }
        return SUCCESS;
    }

    public int parentId;

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public String getOrgans(){
        objs = organizationLogicInterface.getOrganes(parentId);
        String organs =  JsonUtils.getJsonString(objs);
        directOut(organs);
        return  null;
    }
}
