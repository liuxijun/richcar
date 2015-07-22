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
 * ��֯�������
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
    private long channelId;     // ������֯����ĿId
    private boolean childrenIncluded;   // �Ƿ����������֯

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
        // ��ѯ�����е��û���֯��Ƶ���Ķ�Ӧ��ϵ
        List<OrgChannelMap> mapList = organizationLogicInterface.getOrgChannelMap( OrganizationLogicInterface.MAP_BY_ORG );
        // ��ѯ�����е��û�����û������������ж��Ƿ����ɾ���û��飩
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
    List<Organization> orgList;      // Ҫ�ύ��Logic�������֯������Ϣ
    @SuppressWarnings("unchecked")
    public String synchronize(){
        if(serialisedOrg != null){
            // ������json���󣬷ŵ������к����ݿ�ͬ��
            // serialisedChannel��һ����[]������json����
            log.debug(serialisedOrg);
            try{
                orgList = new ArrayList();
                //JSONObject channelJson = new JSONObject("{\"channels\":" + serialisedChannel + "}");
                JSONArray jsonArray = new JSONArray(serialisedOrg);
                log.debug("array length:" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    parseOrganization(-1L, (JSONObject) jsonArray.get(i));
                }

                // ���������ı��棬������**********##########��������������������������
                //channelLogicInterface.saveChannels(channelList,cspId);
                orgList = organizationLogicInterface.saveOrganizations(orgList);
                setSuccess(true);

                writeSysLog("�޸���֯����");

                //addActionError("�㶨!");
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

            log.debug("�ѡ�" + orgName + "����ӵ��б����ˣ� �����У�" + orgList.size() + " ����֯�ˡ�");

            // ������Ƶ��
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
     * ������Ŀ����֯�����ܰ���������֯
     * @return SUCCESS
     */
    public String referenceChannelOrg(){
        if( obj != null ){
            setSuccess(true);
            writeSysLog("��Ŀ�󶨵���֯��������ĿID:" + channelId + ",��֯����ID:" + childrenIncluded);
            organizationLogicInterface.referenceChannelOrg(obj.getId(), channelId, childrenIncluded);
        }else{
            setSuccess(false);
            obj = new Organization();
            obj.setId(428L);
        }
        return SUCCESS;
    }

    /**
     * ������Ŀ��������֯
     * @return SUCCESS
     */
    public  String referenceChannelAllOrg(){
        writeSysLog("��Ŀ�󶨵�������֯��������ĿID:"+channelId);
        organizationLogicInterface.referenceChannelAllOrg(channelId);
        setSuccess(true);
        if(obj == null){ // ��ֹjson��objΪ��ʱ���ص����ݲ��Ϸ�
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
