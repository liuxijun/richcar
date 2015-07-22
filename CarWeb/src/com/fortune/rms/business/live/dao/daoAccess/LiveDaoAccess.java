package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.LiveDaoInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2015/2/26.
 * 直播Dao实现
 */
@Repository
public class LiveDaoAccess   extends BaseDaoAccess<Live, Long>
        implements
        LiveDaoInterface {
    public LiveDaoAccess() {
        super(Live.class);
    }

    /**
     * 查询直播
     * @param channels      查询范围，频道Id分隔
     * @param searchValue   查询关键词
     * @param pageBean       分页信息
     * @return  直播列表
     */
    public List<Live> searchLive(String channels, String searchValue, PageBean pageBean){
        String hql = "from Live live where live.isLive>0";
        if(channels != null && !channels.isEmpty()){
            hql += " and live.id in (select lc.liveId from LiveChannel lc where lc.channelId in (" +
                    channels + "))";
        }

        if(searchValue != null && !searchValue.isEmpty()){
            hql += " and (live.title like '%" + searchValue + "%' or " +
                    "live.actor like '%" + searchValue + "%' or " +
                    "live.intro like '%" + searchValue + "%'" +
                    ")";
        }

        if("name".equals(pageBean.getOrderBy())){
            pageBean.setOrderBy("live.title");
        }else if("status".equals(pageBean.getOrderBy())){
            pageBean.setOrderBy("live.status");
        }else if("type".equals(pageBean.getOrderBy())) {
            pageBean.setOrderBy("live.type");
        }else{
            pageBean.setOrderBy("live.createTime");
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Live>();
        }
    }

    /**
     * 查询直播
     * @param channels      查询范围，频道Id分隔
     * @param searchValue   查询关键词
     * @param pageBean       分页信息
     * @return  直播列表
     */
    public List<Live> searchRecord(String channels, String searchValue, PageBean pageBean){
        String hql = "from Live live where live.isLive=0";
        if(channels != null && !channels.isEmpty()){
            hql += " and live.id in (select lc.liveId from LiveChannel lc where lc.channelId in (" +
                    channels + "))";
        }

        if(searchValue != null && !searchValue.isEmpty()){
            hql += " and (live.title like '%" + searchValue + "%' or " +
                    "live.actor like '%" + searchValue + "%' or " +
                    "live.intro like '%" + searchValue + "%'" +
                    ")";
        }

        if("name".equals(pageBean.getOrderBy())){
            pageBean.setOrderBy("live.title");
        }else if("status".equals(pageBean.getOrderBy())){
            pageBean.setOrderBy("live.status");
        }else if("type".equals(pageBean.getOrderBy())) {
            pageBean.setOrderBy("live.type");
        }else{
            pageBean.setOrderBy("live.createTime");
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Live>();
        }
    }

    /**
     * 根据直播智能流名称获得直播，如果没有，返回null
     * @param streamName 智能流名称
     * @return 直播
     */
    public Live getLiveByStreamName(String streamName){
        String hql = "from Live l where l.channel='" + streamName + "'";
        try {
            List<Live> liveList = getObjects(hql, null);
            return liveList != null && liveList.size() > 0 ? liveList.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据任务id获取直播列表
     * @param taskId 任务Id
     * @param statusArray 状态列表，有效的状态
     * @return 直播列表
     */
    @SuppressWarnings("unchecked")
    public List<Live> getLiveByTaskId(Long taskId, long[] statusArray){
        String hql = "from Live l where l.taskId=" + taskId;
        if(statusArray != null && statusArray.length > 0) {
            hql += " and (";
            for(int i=0; i<statusArray.length; i++){
                hql += "l.status=" + statusArray[i];
                if( i!= statusArray.length-1) hql += " or ";
            }
            hql += ")";
        }
        return this.getHibernateTemplate().find(hql);
    }
}
