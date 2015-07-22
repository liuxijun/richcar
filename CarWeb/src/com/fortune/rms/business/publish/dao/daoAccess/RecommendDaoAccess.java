package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.RecommendDaoInterface;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class RecommendDaoAccess extends BaseDaoAccess<Recommend, Long>
        implements
        RecommendDaoInterface {

    public RecommendDaoAccess() {
        super(Recommend.class);
    }

    public List<Recommend> getRecommendsByCspId(long cspId) {
        String hql = "from Recommend r where r.cspId =" + cspId + "";

        return this.getHibernateTemplate().find(hql);
    }

    public long getCspIdByCode(String code) {
        long cspId = -1;
        String hql = "from Recommend r where r.code='" + code + "'";
        try{
            List<Recommend> recommends = this.getHibernateTemplate().find(hql);
            if (recommends.size() != 0) {
                cspId = recommends.get(0).getCspId();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cspId;
    }


    public List<Recommend> getRecommend(Long cspId,Long type,String name,String code, PageBean pageBean) {
 
        String hql = "from Recommend o1 where 1=1";
        List<Object> args = new ArrayList<Object>();
          if (cspId != null && cspId != 0) {
                hql += " and o1.cspId=?";
                args.add(cspId);
            }
            if (type != null && type != 0) {
                hql += " and o1.type=?";
                args.add(type);
            }
            if (name!= null && !"".equals(name.trim())) {
                hql += " and o1.name like ?";
                args.add("%" + name.trim() + "%");
            }
            if (code!= null && !"".equals(code.trim())) {
                hql += " and o1.code like ?";
                args.add("%" +code.trim() + "%");
            }

        List result = null;
        try {
            if (args.size() > 0) {
                result = getObjects(hql, args.toArray(), pageBean);
            } else {
                result = getObjects(hql, null, pageBean);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Recommend>();
    }

    public Recommend getRecommendByChannelId(Long channelId) {
        String hql = "from Recommend r where r.channelId ="+channelId;
        List<Recommend> list = this.getHibernateTemplate().find(hql);
        return (list == null ||list.size() ==0 )?null:list.get(0);
    }

    /**
     * 查询在channelIdList范围内的频道推荐列表，如果channelIdList为null，则不限
     * @param channelIdList 查询的范围
     * @return 推荐列表
     */
    public List<Recommend> getChannelRecommendByChannelList(List<Long> channelIdList){
        String hql = "from Recommend r where r.type=" + Recommend.RECOMMEND_TYPE_CHANNEL;
        if( channelIdList != null && channelIdList.size() > 0 ){
            hql += " and (r.channelId in (";
            for(Long id : channelIdList){
                hql += hql.endsWith("(")? id : ","+id;
            }

            hql += ") or r.channelId is null)";
        }

        return getHibernateTemplate().find(hql);
    }

}
