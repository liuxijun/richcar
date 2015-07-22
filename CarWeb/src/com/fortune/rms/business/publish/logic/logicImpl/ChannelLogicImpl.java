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
    //获取csp绑定的channel
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
        //找到csp自己的频道
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
        //找到csp自己的频道
        result.addAll(getCspChannel(cspId,status));
        return result;
    }

    public List<Channel> getCspChannel(int cspId,int status){
        //根据cspId到cspChannel查询有关信息
        return this.channelDaoInterface.getCspChannel(cspId,status);
    }
    public List<Channel> getCspChannel(int cspId){
        //根据cspId到cspChannel查询有关信息
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
     * 获取子频道
     * @param parentId 父频道Id
     * @param visibleChannelIdList 前台用户可见的频道Id列表
     * @return 符合条件的频道列表
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
     * 保存频道列表
     * @param channels 格式化好的频道，注意，父节点要在前面
     * @param cspId cspId
     * @return 保存的结果
     */
    public String saveChannels(List<Channel> channels,long cspId) {
        Channel channel = new Channel();
        channel.setCspId(cspId);
        List<Channel> oldChannels = search(channel);
        List<Channel> result = new ArrayList<Channel>(channels.size());
        String logs = "要保存的栏目数："+channels.size();
        int modifyCount = 0;
        for(int i=0,l=channels.size();i<l;i++){
            Channel c = channels.get(i);
            long channelId = c.getId();
            boolean modified = false;
            if(channelId>0){
                for(Channel oc:oldChannels){
                    if(channelId==oc.getId()){
                        if(!oc.getName().equals(c.getName())){
                            logs+="，修改了名称："+oc.getName()+"->"+c.getName();
                            oc.setName(c.getName());
                            modified = true;
                        }
                        if(!oc.getParentId().equals(c.getParentId())){
                            logs+="，修改了" +c.getName()+
                                    "的父节点："+oc.getParentId()+"->"+c.getParentId();
                            oc.setParentId(c.getParentId());
                            modified = true;
                        }
                        if(!oc.getAuditFlag().equals(c.getAuditFlag())){
                            logs+="，修改了" +c.getName()+
                                    "的免审状态：原来是"+ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(oc.getAuditFlag())
                                    +"，现在是"+ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(c.getAuditFlag());
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
                logs+="，修改了栏目" +c.getName()+"的排序，"+c.getGrade()+"->"+grade;
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
            long newChannelId = c.getId(); //保存后，如果该频道不存在，newChannelId和channelId就会不一样。
            if(newChannelId!=channelId){
                modified = true;
                logs+="，新建栏目："+c.getName();
                for(int j=i+1;j<l;j++){
                //我们假设父节点肯定在前面，也就是还没有保存过。
                // 如果这里是乱序的，就要再次遍历result列表中数据，重新修改parentId，然后保存
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
        //如果有些频道需要删除，就在这里进行处理
        if(oldChannels.size()>0){
            logs+="，删除了栏目：";
            for(Channel willDeleteChannel:oldChannels){
                logs+=","+willDeleteChannel.getName();
                remove(willDeleteChannel);
            }
        }
        logs+=",修改栏目数："+modifyCount+"个";
        channels.clear();
        channels.addAll(result);
        return logs;
    }

    /**
     * 频道列表中指定的频道是否需要审核
     * @param channels 频道列表，逗号分隔
     * @return true/false
     */
    public boolean channelNeedAudit(String channels){
        if( channels == null || channels.isEmpty()) return false;

        String[] channelArray = channels.split(",");
        for(String id : channelArray){
            Channel c = channelDaoInterface.get( Long.parseLong(id));
            if( c == null) continue;

            if(!ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(c.getAuditFlag())) return true;  // 有一个频道需要审核，则返回需审核
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
        //channel.setName("根目录");
        Long systemChannelId = null;
        List<Channel> channels = search(channel);
        if (channels != null && channels.size() > 0) {
            for(Channel c:channels){
                if("根目录".equals(c.getName())||"首页".equals(c.getName())){
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
     * 获取栏目及其同级、父级或子级节点
     * @param channelId 栏目Id
     * @param level 要获取的栏目级别 >0表示上级 <0为叶子 0为同级
     * @param channelList 有效的频道Id组成的List
     * @return ChannelDTO对象
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
            // 获得同级节点
            if( parentChannel == null ){
                // WTF，没爹的孩子，把所有的跟节点都给你
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
                // 没有找到父节点
                return null;
            }

            channelDTO.setChannel(parentChannel);
            // 获得父节点及父节点的同级节点
            Channel grandChannel = null;
            try {
                grandChannel = (Channel) TreeUtils.getInstance().getObject(Channel.class, parentChannel.getParentId());
            }catch (Exception e){e.printStackTrace();}
            if( grandChannel == null ){
                // WTF，没爹的孩子，把所有的跟节点都给你
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
            // level < 0，获取子节点
            channelDTO.setChannel(channel);
            channelDTO.setChannelList(filterChannelList(
                    (List<Channel>) TreeUtils.getInstance().getSonOf(Channel.class, channel.getId(), null), channelList
            ));
            channelDTO.setTop(channel.getParentId() < 0);
        }

        return channelDTO;
    }

    /**
     * 过滤频道列表，只返回有效的频道
     * @param channelList   待过滤的列表
     * @param validList 有效的列表
     * @return  待过滤列表中有效的栏目组成的列表
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
