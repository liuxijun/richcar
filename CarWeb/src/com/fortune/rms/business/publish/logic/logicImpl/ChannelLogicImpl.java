package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.logic.logicInterface.CspChannelLogicInterface;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.dao.daoInterface.ChannelDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.ChannelDTO;
import com.fortune.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;
@Service("channelLogicInterface")
public class ChannelLogicImpl extends BaseLogicImpl<Channel>
		implements
			ChannelLogicInterface {
	private ChannelDaoInterface channelDaoInterface;
    private CspChannelLogicInterface cspChannelLogicInterface;
	/**
	 * @param channelDaoInterface the channelDaoInterface to set
	 */
    @Autowired
	public void setChannelDaoInterface(ChannelDaoInterface channelDaoInterface) {
		this.channelDaoInterface = channelDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.channelDaoInterface;
	}

    @Autowired
    public void setCspChannelLogicInterface(CspChannelLogicInterface cspChannelLogicInterface) {
        this.cspChannelLogicInterface = cspChannelLogicInterface;
    }

    public List<Channel> getChildren(long parentId,List<Channel> channels){
        List<Channel> result = new ArrayList<Channel>();
        if (channels!=null){
            for (Channel c : channels) {
                if (c.getParentId() == parentId) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    public List<Channel> getAllChildren(long parentId,List channels){
        List<Channel> result = new ArrayList<Channel>();
        if (channels!=null){
            for (int i=0; i<channels.size(); i++){
                Channel c = (Channel)channels.get(i);
                if (c.getParentId() == parentId){
                    result.add(c);
                    result.addAll( getAllChildren(c.getId(),channels) );
                }
            }
        }
        return result;
    }

    public String getAllChildIds(long parentId){
        String result = "";
        List<Channel> channels = getAll();
        List<Channel> subChannels = getAllChildren(parentId,channels);
        for (Channel subChannel : subChannels) {
            result += subChannel.getId() + ",";
        }
        if (!"".equals(result)){
            result = result.substring(0,result.length()-1);
        }
        return result;        
    }

    @SuppressWarnings("unchecked")
    //��ȡcsp�󶨵�channel
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId){
        TreeUtils treeUtils = TreeUtils.getInstance();
        List<CspChannel> bindChannels = cspChannelLogicInterface.getCspChannelByCspId(cspId,type);
        List<Channel> result = new ArrayList<Channel>();
        for(CspChannel cc:bindChannels){
            Long channelId = cc.getChannelId();
            Channel channel = (Channel) BeanUtils.clone(treeUtils.getObject(Channel.class,channelId));
            List<Channel> channels = treeUtils.getAllChildOf(Channel.class,channelId,0);
            result.add(channel);
            List<Channel> allParents = treeUtils.getParents(Channel.class,channel.getId());
            boolean parentBounded = false;
            for(Channel parent:allParents){
                for(CspChannel cspChannel:bindChannels){
                    if(parent.getId()==cspChannel.getChannelId()){
                        parentBounded = true;
                        break;
                    }
                }
            }
            if(parentBounded){

            }else{
                channel.setParentId(-1L);
            }
            result.addAll(channels);
        }
        //�ҵ�csp�Լ���Ƶ��
        result.addAll(getCspChannel(cspId));
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId,int status){
        TreeUtils treeUtils = TreeUtils.getInstance();
        List<CspChannel> bindChannels = cspChannelLogicInterface.getCspChannelByCspId(cspId,type,status);
        List<Channel> result = new ArrayList<Channel>();
        for(CspChannel cc:bindChannels){
            Long channelId = cc.getChannelId();
            Channel channel = (Channel) BeanUtils.clone(treeUtils.getObject(Channel.class,channelId));
            List<Channel> channels = treeUtils.getAllChildOf(Channel.class,channelId,0);
            result.add(channel);
            List<Channel> allParents = treeUtils.getParents(Channel.class,channel.getId());
            boolean parentBounded = false;
            for(Channel parent:allParents){
                for(CspChannel cspChannel:bindChannels){
                    if(parent.getId()==cspChannel.getChannelId()){
                        parentBounded = true;
                        break;
                    }
                }
            }
            if(parentBounded){

            }else{
                channel.setParentId(-1L);
            }
            result.addAll(channels);
        }
        //�ҵ�csp�Լ���Ƶ��
        result.addAll(getCspChannel(cspId,status));
        return result;
    }

    public List<Channel> getCspChannel(int cspId,int status){
        //����cspId��cspChannel��ѯ�й���Ϣ
        return this.channelDaoInterface.getCspChannel(cspId,status);
    }
    public List<Channel> getCspChannel(int cspId){
        //����cspId��cspChannel��ѯ�й���Ϣ
        return this.channelDaoInterface.getChannelsByCspId(cspId);
    }
    public boolean hasPrivilegeToChannel(long cspId,long channelId){
        return cspChannelLogicInterface.hasPrivilegeToChannel(cspId,channelId);
    }

    public boolean hasPrivilegeToChannelSX(long cspId,long channelId) {
        return channelDaoInterface.hasPrivilegeToChannelSX(cspId,channelId);
    }

    public boolean isLeafChannel(Long channelId) {
        Boolean result = (Boolean) CacheUtils.get(channelId,"channelLeafCache",new DataInitWorker(){
            public Object init(Object keyId,String cacheName){
                TreeUtils tu = TreeUtils.getInstance();
                List children = tu.getAllChildOf(Channel.class,keyId,0);

                return children == null || children.size()==0;
            }
        });
        if(result==null){
            return true;
        }
        return result;
    }
    public List<Channel> getChannelList(Long parentId){
        return cspChannelLogicInterface.getParentToChannel(parentId);
    }

    /**
     * ��ȡ��Ƶ��
     * @param parentId ��Ƶ��Id
     * @param visibleChannelIdList ǰ̨�û��ɼ���Ƶ��Id�б�
     * @return ����������Ƶ���б�
     */
    public List<Channel> getChannelList(Long parentId, List visibleChannelIdList){
        if( visibleChannelIdList == null ) return null;
        
        List<Channel> channels = cspChannelLogicInterface.getParentToChannel(parentId);
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            List<Channel> finalList = new ArrayList<Channel>();
            for(Channel c : channels){
                if( visibleChannelIdList.contains(c.getId())){
                    finalList.add(c);
                }
            }
            return finalList;
        }else{
            return channels;
        }
    }


    public List getSonChannelId(Long channelId){
       List list = channelDaoInterface.getSonChannelId(channelId);
       return list;
    }

    /**
     * ����Ƶ���б�
     * @param channels ��ʽ���õ�Ƶ����ע�⣬���ڵ�Ҫ��ǰ��
     * @param cspId cspId
     * @return ����Ľ��
     */
    public String saveChannels(List<Channel> channels,long cspId) {
        Channel channel = new Channel();
        channel.setCspId(cspId);
        List<Channel> oldChannels = search(channel);
        List<Channel> result = new ArrayList<Channel>(channels.size());
        String logs = "Ҫ�������Ŀ����"+channels.size();
        int modifyCount = 0;
        for(int i=0,l=channels.size();i<l;i++){
            Channel c = channels.get(i);
            long channelId = c.getId();
            boolean modified = false;
            if(channelId>0){
                for(Channel oc:oldChannels){
                    if(channelId==oc.getId()){
                        if(!oc.getName().equals(c.getName())){
                            logs+="���޸������ƣ�"+oc.getName()+"->"+c.getName();
                            oc.setName(c.getName());
                            modified = true;
                        }
                        if(!oc.getParentId().equals(c.getParentId())){
                            logs+="���޸���" +c.getName()+
                                    "�ĸ��ڵ㣺"+oc.getParentId()+"->"+c.getParentId();
                            oc.setParentId(c.getParentId());
                            modified = true;
                        }
                        if(!oc.getAuditFlag().equals(c.getAuditFlag())){
                            logs+="���޸���" +c.getName()+
                                    "������״̬��ԭ����"+ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(oc.getAuditFlag())
                                    +"��������"+ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(c.getAuditFlag());
                            oc.setAuditFlag(c.getAuditFlag());
                            modified = true;
                        }
                        c = oc;
                        oldChannels.remove(oc);
                        break;
                    }
                }
            }
            Long channelCspId = c.getCspId();
            Long typeId = c.getType();
            if(channelCspId==null||channelCspId<=0){
                modified = true;
                c.setCspId(cspId);
            }
            if(typeId==null||typeId<=0){
                modified = true;
                c.setType(1L);
            }
            if(c.getStatus()==null){
                modified = true;
                c.setStatus(1L);
            }
            Long grade = (long)i;
            if(!grade.equals(c.getGrade())){
                logs+="���޸�����Ŀ" +c.getName()+"������"+c.getGrade()+"->"+grade;
                modified = true;
                c.setGrade(grade);
            }
/*
            if(c.getGrade()==null){
            }
*/
            if(c.getCode()==null){
                modified = true;
                c.setCode("channelCode_"+i);
            }
            c = save(c);
            long newChannelId = c.getId(); //����������Ƶ�������ڣ�newChannelId��channelId�ͻ᲻һ����
            if(newChannelId!=channelId){
                modified = true;
                logs+="���½���Ŀ��"+c.getName();
                for(int j=i+1;j<l;j++){
                //���Ǽ��踸�ڵ�϶���ǰ�棬Ҳ���ǻ�û�б������
                // �������������ģ���Ҫ�ٴα���result�б������ݣ������޸�parentId��Ȼ�󱣴�
                    Channel child = channels.get(j);
                    if(child!=null){
                        Long parentId = child.getParentId();
                        if(parentId!=null&&parentId==channelId){
                           child.setParentId(c.getId());
                        }
                    }
                }
            }
            if(modified){
                modifyCount++;
            }
            result.add(c);
        }
        //�����ЩƵ����Ҫɾ��������������д���
        if(oldChannels.size()>0){
            logs+="��ɾ������Ŀ��";
            for(Channel willDeleteChannel:oldChannels){
                logs+=","+willDeleteChannel.getName();
                remove(willDeleteChannel);
            }
        }
        logs+=",�޸���Ŀ����"+modifyCount+"��";
        channels.clear();
        channels.addAll(result);
        return logs;
    }

    /**
     * Ƶ���б���ָ����Ƶ���Ƿ���Ҫ���
     * @param channels Ƶ���б����ŷָ�
     * @return true/false
     */
    public boolean channelNeedAudit(String channels){
        if( channels == null || channels.isEmpty()) return false;

        String[] channelArray = channels.split(",");
        for(String id : channelArray){
            Channel c = channelDaoInterface.get( Long.parseLong(id));
            if( c == null) continue;

            if(!ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(c.getAuditFlag())) return true;  // ��һ��Ƶ����Ҫ��ˣ��򷵻������
        }

        return false;
    }

    public Long getSystemChannelId() {
        Channel channel = new Channel();
        channel.setType(1l);
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            channel.setCspId(2l);
        }else{
            channel.setCspId(1l);
        }
        channel.setParentId(-1l);
        //channel.setName("��Ŀ¼");
        Long systemChannelId = null;
        List<Channel> channels = search(channel);
        if (channels != null && channels.size() > 0) {
            for(Channel c:channels){
                if("��Ŀ¼".equals(c.getName())||"��ҳ".equals(c.getName())){
                    systemChannelId = c.getId();
                    break;
                }
            }
        }
        if(systemChannelId==null){
            systemChannelId = -1L;
        }
        return systemChannelId;
    }

    /**
     * ��ȡ��Ŀ����ͬ�����������Ӽ��ڵ�
     * @param channelId ��ĿId
     * @param level Ҫ��ȡ����Ŀ���� >0��ʾ�ϼ� <0ΪҶ�� 0Ϊͬ��
     * @param channelList ��Ч��Ƶ��Id��ɵ�List
     * @return ChannelDTO����
     */
    public ChannelDTO getChannelDTO(Long channelId, long level, List channelList){
        Channel channel = (Channel)TreeUtils.getInstance().getObject(Channel.class, channelId);
        if(channel == null) return null;

        Channel parentChannel = null;
        try {
            parentChannel = (Channel) TreeUtils.getInstance().getObject(Channel.class, channel.getParentId());
        }catch (Exception e){e.printStackTrace();}

        ChannelDTO channelDTO = new ChannelDTO();
        if( level == 0 ){
            channelDTO.setChannel(channel);
            // ���ͬ���ڵ�
            if( parentChannel == null ){
                // WTF��û���ĺ��ӣ������еĸ��ڵ㶼����
                channelDTO.setChannelList( filterChannelList(
                                (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, -1, null),
                                channelList)
                );
                channelDTO.setTop(true);
            }else{
                channelDTO.setChannelList(filterChannelList(
                                (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, parentChannel.getId(), null),
                                channelList)
                );
                channelDTO.setTop(false);
            }
        }else if( level > 0){
            if( parentChannel == null ){
                // û���ҵ����ڵ�
                return null;
            }

            channelDTO.setChannel(parentChannel);
            // ��ø��ڵ㼰���ڵ��ͬ���ڵ�
            Channel grandChannel = null;
            try {
                grandChannel = (Channel) TreeUtils.getInstance().getObject(Channel.class, parentChannel.getParentId());
            }catch (Exception e){e.printStackTrace();}
            if( grandChannel == null ){
                // WTF��û���ĺ��ӣ������еĸ��ڵ㶼����
                channelDTO.setChannelList(filterChannelList(
                                (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, -1, null),
                                channelList)
                );
                channelDTO.setTop(true);
            }else{
                channelDTO.setChannelList(filterChannelList(
                                (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, grandChannel.getId(), null),
                                channelList)
                );
                channelDTO.setTop(false);
            }
        }else{
            // level < 0����ȡ�ӽڵ�
            channelDTO.setChannel(channel);
            channelDTO.setChannelList(filterChannelList(
                    (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, channel.getId(), null), channelList
            ));
            channelDTO.setTop(channel.getParentId() < 0);
        }

        return channelDTO;
    }

    /**
     * ����Ƶ���б�ֻ������Ч��Ƶ��
     * @param channelList   �����˵��б�
     * @param validList ��Ч���б�
     * @return  �������б�����Ч����Ŀ��ɵ��б�
     */
    private List<Channel> filterChannelList(List<Channel> channelList, List<Long> validList){
        if(validList == null) return channelList;

        List<Channel> list = new ArrayList<Channel>();
        for(Channel c : channelList){
            if( validList.contains(c.getId())){
                list.add(c);
            }
        }

        return list;
    }
}
