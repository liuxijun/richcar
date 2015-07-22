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
 * ��֯�����߼�ʵ��
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
            long newId = newO.getId(); //����������Ƶ�������ڣ�newChannelId��channelId�ͻ᲻һ����
            if(newId!=id){
                for(int j=i+1;j<l;j++){
                    //���Ǽ��踸�ڵ�϶���ǰ�棬Ҳ���ǻ�û�б������
                    // �������������ģ���Ҫ�ٴα���result�б������ݣ������޸�parentId��Ȼ�󱣴�
                    Organization child = orgList.get(j);
                    if(child.getParentId()==id){
                        child.setParentId(newId);
                    }
                }
            }
            result.add(c);
        }
        //�����ЩƵ����Ҫɾ��������������д���
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
     * �����֯�µ��û�����������������֯
     * @param orgId  ��֯Id
     * @return  ����
     */
    public int getOrganizationUserCount(Long orgId){
        return frontUserDaoInterface.getUserCountByOrg(orgId, false);
    }

    /**
     * ��Ȩ��֯�ۿ�Ƶ��
     * @param orgId                 ��֯Id
     * @param channelId             Ƶ��Id
     * @param childrenIncluded      �Ƿ��������֯
     */
    public void referenceChannelOrg(long orgId, long channelId, boolean childrenIncluded){
        // �����Ŀ�Ƿ���Ч
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
     * ����Ŀ������������֯
     * @param channelId ��ĿId
     */
    public void referenceChannelAllOrg(long channelId){
        // �����Ŀ�Ƿ���Ч
        Channel channel = channelDaoInterface.get(channelId);
        if(channel == null) return;

        // �����Ŀԭ�еĹ���
        OrganizationChannel oc = new OrganizationChannel();
        oc.setChannelId(channelId);
        organizationChannelDaoInterface.clearChannelReference(channelId);
        // ���������֯�Ĺ���
        List<Organization> orgList = organizationDaoInterface.getAll();
        if( orgList != null ){
            for(Organization o : orgList){
                oc.setOrganizationId(o.getId());
                organizationChannelDaoInterface.save(oc);
            }
        }
    }

    /**
     * �����֯��Ƶ��������ϵ
     * @param orgId         ��֯id
     * @param channelId     ��ĿId
     */
    private void addChannelOrgRef(long orgId, long channelId){
        OrganizationChannel oc = new OrganizationChannel();
        oc.setChannelId(channelId);
        oc.setOrganizationId(orgId);
        try {
            List l = organizationChannelDaoInterface.getObjects(oc);
            if( l != null && l.size() > 0) return;  // �Ѿ�������
            organizationChannelDaoInterface.save(oc);
        }catch(Exception e){
            e.printStackTrace();
            // �����쳣������
        }

    }

    public List<Organization> getOrganes(int parentId){
        return  organizationDaoInterface.getOrganes(parentId);
    }
}
