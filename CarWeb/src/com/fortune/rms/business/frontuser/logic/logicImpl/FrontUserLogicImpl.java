package com.fortune.rms.business.frontuser.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.frontuser.dao.daoInterface.FrontUserDaoInterface;
import com.fortune.rms.business.frontuser.dao.daoInterface.OrganizationChannelDaoInterface;
import com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.frontuser.model.OrganizationChannel;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-29
 * Time: 13:16:20
 * 前台用户
 */
@Service("frontUserLogicInterface")
public class FrontUserLogicImpl  extends BaseLogicImpl<FrontUser>
        implements
        FrontUserLogicInterface {
    public FrontUserLogicImpl() {
    }

    private FrontUserDaoInterface frontUserDaoInterface;
    private OrganizationChannelDaoInterface organizationChannelDaoInterface;
    private ContentLogicInterface contentLogicInterface;
    private ChannelLogicInterface channelLogicInterface;

    @Autowired
    public void setOrganizationChannelDaoInterface(OrganizationChannelDaoInterface organizationChannelDaoInterface) {
        this.organizationChannelDaoInterface = organizationChannelDaoInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    @Autowired
    public void setFrontUserDaoInterface(FrontUserDaoInterface frontUserDaoInterface) {
        this.frontUserDaoInterface = frontUserDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)frontUserDaoInterface;
    }

    /**
     * 搜索用户
     * @param orgId         用户所属的机构，包含子机构
     * @param searchValue   查询字串
     * @param pageBean      分页和排序信息
     * @return              符合条件的分页数据
     */
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean){
        return frontUserDaoInterface.searchUsers(orgId, searchValue, pageBean);
    }

    /**
     * 搜索待审用户
     * @param searchValue   查询条件
     * @param pageBean      分页
     * @return 待审用户列表
     */
    public List<FrontUser> searchUnAuditUsers(String searchValue, PageBean pageBean){
        return frontUserDaoInterface.searchUsers(-1L, searchValue, pageBean, FrontUser.USER_STATUS_UN_AUDITED);
    }
    /**
     * 设置用户状态，用于锁定/解锁账号
     * @param userId   用户Id
     * @param status   状态
     */
    public void updateUserStatus(String userId, Long status){
        frontUserDaoInterface.setUserStatus(userId, status);
    }

    public long getRootChannelId(){
        long systemRootId = channelLogicInterface.getSystemChannelId();
        return AppConfigurator.getInstance().getLongConfig("system.portal.RootChannelId", systemRootId);
    }
    public List<Channel> getExtraChannels(){
        String extraChannelIds = AppConfigurator.getInstance().getConfig("system.portal.extraChannelIds",null);
        List<Channel> result = new ArrayList<Channel>();
        if(extraChannelIds!=null&&!"".equals(extraChannelIds)){
            String[] ids = extraChannelIds.split(",");
            for(String id:ids){
                Long channelId = StringUtils.string2long(id,0);
                if(channelId>0){
                    result.add((Channel)TreeUtils.getInstance().getObject(Channel.class,channelId));
                }
            }
        }
        return result;
    }
    /**
     * 获取用户
     * @param user 前台用户对象
     * @return 频道id列表
     */
    @SuppressWarnings("unchecked")
    public List<Long> getUserAuthorizedChannel(FrontUser user){
        if(user == null) return null;
        List<Long> channelIdList  = new ArrayList();
        if(FrontUser.USER_LOGIN_TYPE_SMS.equals(user.getLoginType())){
            TreeUtils channelTree = TreeUtils.getInstance();
            List<Channel> channels = channelTree.getAllChildOf(Channel.class,getRootChannelId(),0);
            channels.addAll(getExtraChannels());
            for(Channel channel:channels){
                channelIdList.add(channel.getId());
            }
            return channelIdList;
        }
        OrganizationChannel oc = new OrganizationChannel();
        oc.setOrganizationId( user.getOrganizationId());
        try{
            List<Long> leafIdList = new ArrayList();
            List<OrganizationChannel> ocList = organizationChannelDaoInterface.getObjects( oc );
            if( ocList != null && ocList.size() > 0){
                for( OrganizationChannel o : ocList){
                    leafIdList.add(o.getChannelId());
                }
            }

            // 找到已经节点的父节点，一并加到列表中
            TreeUtils orgTree = TreeUtils.getInstance();
            for(Long id: leafIdList){
                List<Channel> parents = orgTree.getParents(Channel.class, id);

                channelIdList.add(id);
                for( Channel ch : parents){
                    if( !channelIdList.contains( ch.getId()) ) channelIdList.add( ch.getId() );
                }
            }

            return channelIdList;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取前台用户可见的顶层栏目列表
     * @param channelIdList 用户可见的栏目列表
     * @return channel List
     */
    @SuppressWarnings("unchecked")
    public List<Channel> getTopLevelChannel(List channelIdList){
        if(channelIdList == null) return null;

        TreeUtils channelTree = TreeUtils.getInstance();
        long systemRootId = getRootChannelId();
        List<Channel> list = channelTree.getSonOf(Channel.class, systemRootId, null);
        list.addAll(getExtraChannels());

        List<Channel> visibleChannelList = new ArrayList();
        if (list.size() == 1){
            Long id = list.get(0).getId();
            list = channelTree.getSonOf(Channel.class, id, null);
        }
        for(Channel c : list){
            if( channelIdList.contains(c.getId()) ){
                visibleChannelList.add( c );
            }
        }
        Collections.sort(visibleChannelList, new ComparatorChannel());
        return visibleChannelList;
    }

    public class ComparatorChannel implements Comparator {
        public int compare(Object arg0, Object arg1) {
            if(arg0==null){
                return -1;
            }
            if(arg1==null){
                return 1;
            }
            Channel ch0 = (Channel) arg0;
            Channel ch1 = (Channel) arg1;

            //根据grade排序
            Long grade0 = ch0.getGrade();
            Long grade1 = ch1.getGrade();
            if(grade0==null){
                return -1;
            }
            if(grade1==null){
                return 1;
            }
            return grade0.compareTo(grade1);
        }

    }

    /**
     * 获取所有的一级栏目
     * @return 栏目列表
     */
    @SuppressWarnings("unchecked")
    public List<Channel> getAllTopLevelChannel(){
        TreeUtils channelTree = TreeUtils.getInstance();
        long systemRootId = getRootChannelId();
        List<Channel> list = channelTree.getSonOf(Channel.class, systemRootId, null);
        list.addAll(getExtraChannels());
        if (list.size() == 1){
            Long id = list.get(0).getId();
            list = channelTree.getSonOf(Channel.class, id, null);
        }
        Collections.sort(list, new ComparatorChannel());

        return list;
    }

    public boolean checkLoginName(FrontUser frontUser) {
        boolean isExisted = false;
        try {
            List<FrontUser> userList = this.frontUserDaoInterface.getObjects(frontUser);
            if(userList!=null && userList.size()>0){
                isExisted = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isExisted;
    }

    public boolean isUserExists(String userId) {

            FrontUser searchBean = new FrontUser();
            searchBean.setUserId(userId);
            List<FrontUser> list = search(searchBean,false);
            if (list != null && list.size() > 0) {
                logger.warn("这个用户Login'" + userId + "'已经存在！");
                return true;
            } else {
                logger.warn("这个用户Login'" + userId + "'不存在！");
                return false;
            }
    }

    /**
     * 获取用户总数
     * @return 数量
     */
    public int getTotalUserCount(){
        return frontUserDaoInterface.getUserCountByStatus(null);
    }

    /**
     * 待审用户数
     * @return 数量
     */
    public int getUnAuditUserCount(){
        return frontUserDaoInterface.getUserCountByStatus(FrontUser.USER_STATUS_UN_AUDITED);
    }

    //延安start
    public List<Map<String,Object>> getIndexList(String channelIds,PageBean pageBean){
       List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
       if(channelIds != null && !channelIds.trim().isEmpty()) {
           String[] ids = channelIds.split(",");
           if(ids.length > 0) {
               for(String channelId : ids) {
                   long id = Long.valueOf(channelId);
                   Map<String,Object> map = new HashMap<String,Object>();
                   map.put("channelId",channelId);
                   map.put("contents",getContentsByChannelId(id,pageBean));
                   if(Long.valueOf(channelId) == 474431622) {//今日头条频道需要查询子频道
                       List<Map<String,Object>> subChannelContents = new ArrayList<Map<String, Object>>();
                       List<Channel> subChannels = getSubChannelByParentId(id);
                       if(subChannels != null && subChannels.size() > 0) {
                             for(Channel subChannel : subChannels) {
                                 Map<String,Object> subMap = new HashMap<String, Object>();
                                 subMap.put("subChannelId",subChannel.getId());
                                 subMap.put("subContentList",getContentsByChannelId(subChannel.getId(),pageBean));
                                 subChannelContents.add(subMap);
                             }
                       }
                       map.put("subContents",subChannelContents);
                   }
                  data.add(map);
               }
           }
       }

        return  data;
    }

    public List<Content> getContentsByChannelId(long channelId,PageBean pageBean) {
       return contentLogicInterface.getContentsByChannelId(channelId,pageBean);
    }

    public List<Channel> getSubChannelByParentId(long parentId) {
        return  channelLogicInterface.getChannelList(parentId);
    }


    public List<Map<String,Object>> getColumnList(long channelId,PageBean pageBean) {
        String channelIds="";
        List<Channel> subChannels = getSubChannelByParentId(channelId);
        if(subChannels != null && subChannels.size() > 0) {
            for(Channel c : subChannels) {
                 channelIds += c.getId()+",";
            }
        }
        return     getIndexList(channelIds.substring(0,channelIds.length()-1),pageBean);
    }
    //延安end
}
