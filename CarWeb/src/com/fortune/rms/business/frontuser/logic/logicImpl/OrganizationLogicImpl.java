package com.fortune.rms.business.frontuser.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.frontuser.dao.daoInterface.FrontUserDaoInterface;
import com.fortune.rms.business.frontuser.dao.daoInterface.OrganizationChannelDaoInterface;
import com.fortune.rms.business.frontuser.dao.daoInterface.OrganizationDaoInterface;
import com.fortune.rms.business.frontuser.logic.logicInterface.OrganizationLogicInterface;
import com.fortune.rms.business.frontuser.model.OrgChannelMap;
import com.fortune.rms.business.frontuser.model.Organization;
import com.fortune.rms.business.frontuser.model.OrganizationChannel;
import com.fortune.rms.business.publish.dao.daoInterface.ChannelDaoInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 15:25:57
 * 组织管理逻辑实现
 */
@Service("organizationLogicInterface")
public class OrganizationLogicImpl extends BaseLogicImpl<Organization>
        implements
        OrganizationLogicInterface {
    
    @Autowired
    public void setOrganizationDaoInterface(OrganizationDaoInterface organizationDaoInterface) {
        this.organizationDaoInterface = organizationDaoInterface;
        this.baseDaoInterface =(BaseDaoInterface) organizationDaoInterface;
    }

    private OrganizationDaoInterface organizationDaoInterface;
    private OrganizationChannelDaoInterface organizationChannelDaoInterface;
    private FrontUserDaoInterface frontUserDaoInterface;
    private ChannelDaoInterface channelDaoInterface;

    @Autowired
    public void setChannelDaoInterface(ChannelDaoInterface channelDaoInterface) {
        this.channelDaoInterface = channelDaoInterface;
    }

    @Autowired
    public void setFrontUserDaoInterface(FrontUserDaoInterface frontUserDaoInterface) {
        this.frontUserDaoInterface = frontUserDaoInterface;
    }

    @Autowired
    public void setOrganizationChannelDaoInterface(OrganizationChannelDaoInterface organizationChannelDaoInterface) {
        this.organizationChannelDaoInterface = organizationChannelDaoInterface;
    }

    public List<Organization> getOrgList(String parentId){
        return organizationDaoInterface.getChildrenOrganization(parentId);
    }

    public List<Organization> getOrganizationByName(String name) {
        if(name==null||name.isEmpty()){
            return new ArrayList<Organization>(0);
        }
        Organization org = new Organization();
        org.setName(name);
        return search(org,false);
    }

    @SuppressWarnings("unchecked")
    public List<OrgChannelMap> getOrgChannelMap(Long by){
        List<OrgChannelMap> mapList = new ArrayList();

        List<OrganizationChannel> list = organizationChannelDaoInterface.getAll();
        if( OrganizationLogicInterface.MAP_BY_ORG.equals(by) ){
             for( OrganizationChannel o : list ){
                OrgChannelMap m = inList( o.getOrganizationId(), mapList);
                if( m != null){
                    m.appendValue(o.getChannelId());
                }else{
                    OrgChannelMap map = new OrgChannelMap(o.getOrganizationId());
                    map.appendValue(o.getChannelId());
                    mapList.add(map);
                }
             }
        }else{
            for( OrganizationChannel o : list ){
               OrgChannelMap m = inList( o.getChannelId(), mapList);
               if( m != null){
                   m.appendValue(o.getOrganizationId());
               }else{
                   OrgChannelMap map = new OrgChannelMap(o.getChannelId());
                   map.appendValue(o.getOrganizationId());
                   mapList.add(map);
               }
            }
        }

        return mapList;
    }

    public List<Organization> saveOrganizations(List<Organization> orgList) {
        List<Organization> oldOrganizations = getAll();
        List<Organization> result = new ArrayList<Organization>(orgList.size());
        for(int i=0,l=orgList.size();i<l;i++){
            Organization c = orgList.get(i);
            long id = c.getId();
            if(id>0){
                for(Organization oc:oldOrganizations){
                    if(id==oc.getId()){
                        oldOrganizations.remove(oc);
                        break;
                    }
                }
            }
            Organization newO = save(c);
            c.setId(newO.getId());
            List<OrganizationChannel> ocs = saveOrgChannels(c);
            long newId = newO.getId(); //保存后，如果该频道不存在，newChannelId和channelId就会不一样。
            if(newId!=id){
                for(int j=i+1;j<l;j++){
                    //我们假设父节点肯定在前面，也就是还没有保存过。
                    // 如果这里是乱序的，就要再次遍历result列表中数据，重新修改parentId，然后保存
                    Organization child = orgList.get(j);
                    if(child.getParentId()==id){
                        child.setParentId(newId);
                    }
                }
            }
            result.add(c);
        }
        //如果有些频道需要删除，就在这里进行处理
        for(Organization willDeleteOrg:oldOrganizations){
            remove(willDeleteOrg);
        }
        return result;
    }

    public List<OrganizationChannel> saveOrgChannels(Organization organization) {
        OrganizationChannel oc = new OrganizationChannel();
        oc.setOrganizationId(organization.getId());
        List<OrganizationChannel> result = new ArrayList<OrganizationChannel>();
        try {
            List<OrganizationChannel> oldData = organizationChannelDaoInterface.getObjects(oc);
            String newIds = organization.getChannels();
            if(newIds!=null){
                String[] ids = newIds.split(",");
                for(String id:ids){
                    long channelId= StringUtils.string2long(id,-1);
                    boolean foundOld = false;
                    if(channelId>0){
                        for(OrganizationChannel data:oldData){
                            if(channelId == data.getChannelId()){
                                oldData.remove(data);
                                result.add(data);
                                foundOld = true;
                                break;
                            }
                        }
                        if(!foundOld){
                            OrganizationChannel data=new OrganizationChannel();
                            data.setOrganizationId(organization.getId());
                            data.setChannelId(channelId);
                            result.add(organizationChannelDaoInterface.save(data));
                        }
                    }

                }
            }
            for(OrganizationChannel willDelData:oldData){
                organizationChannelDaoInterface.remove(willDelData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrgChannelMap inList(Long id, List<OrgChannelMap> l){
        if( l == null ) return null;

        for(OrgChannelMap m : l){
            if( m.getId().equals(id) ) return m;
        }
        
        return null;
    }

    /**
     * 获得组织下的用户个数，不含子子组织
     * @param orgId  组织Id
     * @return  人数
     */
    public int getOrganizationUserCount(Long orgId){
        return frontUserDaoInterface.getUserCountByOrg(orgId, false);
    }

    /**
     * 授权组织观看频道
     * @param orgId                 组织Id
     * @param channelId             频道Id
     * @param childrenIncluded      是否包含子组织
     */
    public void referenceChannelOrg(long orgId, long channelId, boolean childrenIncluded){
        // 检查栏目是否有效
        Channel channel = channelDaoInterface.get(channelId);
        if(channel == null) return;

        addChannelOrgRef(orgId, channelId);
        if( childrenIncluded ){
            List<Organization> orgList = TreeUtils.getInstance().getAllChildOf(Organization.class, orgId, 9999);
            if( orgList != null){
                for(Organization o : orgList){
                    addChannelOrgRef(o.getId(), channelId);
                }
            }
        }
    }

    /**
     * 将栏目关联到所有组织
     * @param channelId 栏目Id
     */
    public void referenceChannelAllOrg(long channelId){
        // 检查栏目是否有效
        Channel channel = channelDaoInterface.get(channelId);
        if(channel == null) return;

        // 清除栏目原有的关联
        OrganizationChannel oc = new OrganizationChannel();
        oc.setChannelId(channelId);
        organizationChannelDaoInterface.clearChannelReference(channelId);
        // 添加所有组织的关联
        List<Organization> orgList = organizationDaoInterface.getAll();
        if( orgList != null ){
            for(Organization o : orgList){
                oc.setOrganizationId(o.getId());
                organizationChannelDaoInterface.save(oc);
            }
        }
    }

    /**
     * 添加组织和频道关联关系
     * @param orgId         组织id
     * @param channelId     栏目Id
     */
    private void addChannelOrgRef(long orgId, long channelId){
        OrganizationChannel oc = new OrganizationChannel();
        oc.setChannelId(channelId);
        oc.setOrganizationId(orgId);
        try {
            List l = organizationChannelDaoInterface.getObjects(oc);
            if( l != null && l.size() > 0) return;  // 已经存在了
            organizationChannelDaoInterface.save(oc);
        }catch(Exception e){
            e.printStackTrace();
            // 发生异常就算了
        }

    }

    public List<Organization> getOrganes(int parentId){
        return  organizationDaoInterface.getOrganes(parentId);
    }
}
