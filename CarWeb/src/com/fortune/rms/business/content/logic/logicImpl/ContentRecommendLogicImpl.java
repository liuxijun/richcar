package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentRecommendDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.PageBean;
import com.fortune.util.ThreadUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("contentRecommendLogicInterface")
public class ContentRecommendLogicImpl extends BaseLogicImpl<ContentRecommend>
		implements
			ContentRecommendLogicInterface {
	private ContentRecommendDaoInterface contentRecommendDaoInterface;
    private ContentLogicInterface contentLogicInterface;
    private RecommendLogicInterface recommendLogicInterface;
	/**
	 * @param contentRecommendDaoInterface the contentRecommendDaoInterface to set
	 */
    @Autowired
	public void setContentRecommendDaoInterface(
			ContentRecommendDaoInterface contentRecommendDaoInterface) {
		this.contentRecommendDaoInterface = contentRecommendDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.contentRecommendDaoInterface;
	}
    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setRecommendLogicInterface(RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
    }

    public Recommend getRecommendByCode(String recommendCode){
        PageBean pageBean = new PageBean();
        List<Recommend> recommends = recommendLogicInterface.getRecommend(null,null,null,recommendCode,pageBean);
        Recommend recommend=null;
        if(recommends!=null&&recommends.size()>0){
            recommend = recommends.get(0);
        }
        return recommend;
    }
    public List<Content> getContents(String recommendCode) {
        List<ContentRecommend> tempResult = contentRecommendDaoInterface.getContentIds(recommendCode,
                new PageBean(1,Integer.MAX_VALUE,null,null));
        List<Content> result = new ArrayList<Content>();
        if(tempResult!=null){
            Recommend recommend = getRecommendByCode(recommendCode);
            for(ContentRecommend cr:tempResult){
                Content c = contentLogicInterface.getCachedContent(cr.getContentId());
                if(c!=null){
                    Map<String,Object> properties = c.getProperties();
                    if(properties==null){
                        properties = new HashMap<String,Object>();
                        c.setProperties(properties);
                    }
                    Long channelId = cr.getChannelId();
                    if(channelId == null && recommend!=null){
                        channelId = recommend.getChannelId();
                    }
                    properties.put("channelId",channelId);
                    result.add(c);
                }else{
                    logger.error("有空数据出现："+cr.getContentId());
                }
            }
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Content> getContents(String recommendCode, List<Long> channelIdList, FrontUser user){
        //List<ContentRecommend> tempResult = contentRecommendDaoInterface.getContentIds(recommendCode,
        //        new PageBean(1,16,null,null));
        List<ContentRecommend> tempResult = contentRecommendDaoInterface.getContentIds(recommendCode,
                new PageBean(1,9999,null,null), channelIdList, (user == null)? -1: user.getTypeId());
        List<Content> result = new ArrayList<Content>();
        if(tempResult!=null){
            Recommend recommend = getRecommendByCode(recommendCode);
            for(ContentRecommend cr:tempResult){
                Content c = contentLogicInterface.getCachedContent(cr.getContentId());
                if(c!=null){
                    Map<String,Object> properties = c.getProperties();
                    if(properties==null){
                        properties = new HashMap<String,Object>();
                        c.setProperties(properties);
                    }
                    Long channelId = cr.getChannelId();
                    if(channelId == null && recommend!=null){
                        channelId = recommend.getChannelId();
                    }
                    properties.put("channelId",channelId);
                    result.add(c);
                }else{
                    logger.error("有空数据出现："+cr.getContentId());
                }
            }
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void clearAllCache(){
        getCache().removeAll();
    }
    public Cache getCache(){
        CacheManager cacheManager = CacheManager.getInstance();
        String cacheName = "recommendCache";
        Cache recommendCache = cacheManager.getCache(cacheName);
        if(recommendCache == null){
            recommendCache = new Cache(cacheName,1024,false,false,3600,3600);
            cacheManager.addCache(recommendCache);
        }
        return recommendCache;
    }

    public void putToCache(String id,Map<String,Object> result){
        Cache recommendCache = getCache();
        Element ele = recommendCache.get(id);
        if(ele!=null){
            recommendCache.remove(id);
        }
        ele = new Element(id,result);
        recommendCache.put(ele);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getFromCache(String id){
        Element ele = getCache().get(id);
        if(ele!=null){
           return (Map<String,Object>) ele.getValue();
        }
        return null;
    }

    public Map<String,Object> getRecommendMovies(String id){
        Map<String,Object> result = getFromCache(id);
        if(result==null){
            String keyName = "recommend_"+id;
            try {
                ThreadUtils.getInstance().acquire(keyName,100000);
                result = getFromCache(id);
                if(result!=null){ //如果在等待期间，有线程做了初始化工作，就直接返回
                    return result;
                }
                result = new HashMap<String,Object>();
                result.put("movies",getContents(id));
                result.put("topicId",id);
                //初始化电影信息
                //初始化频道信息
                //Channel channel = new Channel();
                //result.put("channel",channel);
                putToCache(id,result);
            } catch (Exception e) {
                System.err.println(e);
            } finally{
                ThreadUtils.getInstance().release(keyName);
            }
        }
        return result;
    }

    public String getRecommendCodeById(long recommendId) {
        return this.contentRecommendDaoInterface.getRecommendCodeById(recommendId);
    }

    public List<Map<String, Object>> getContentRecommendListByRecommendCode(String recommendCodes) {
        List<Map<String, Object>> contentRecommendMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> contentRecommendMaps;
        List<Content> contents;
        String recommendCode[] = recommendCodes.split(",");
        for (String aRecommendCode : recommendCode) {
            contentRecommendMaps = new HashMap<String, Object>();
            contents = getContents(aRecommendCode);
            contentRecommendMaps.put("movies", contents);
            contentRecommendMaps.put("topicId", aRecommendCode);
            contentRecommendMapList.add(contentRecommendMaps);
        }
        return contentRecommendMapList;
    }

    public List<ContentRecommend> getContentRecommendByDisplayOrder(long displayOrder,long recommend){
        return this.contentRecommendDaoInterface.getContentRecommendByDisplayOrder(displayOrder,recommend);
    }

    public List<ContentRecommend> getContentIdsOfRecommend(Long recommendId){
        ContentRecommend cr = new ContentRecommend();
        cr.setRecommendId(recommendId);
        return search(cr);
    }

    public List<ContentRecommend> saveContentRecommend(List<Content> contents,Recommend recommend){
        List<ContentRecommend> result = new ArrayList<ContentRecommend>();
        if(recommend!=null){
            List<ContentRecommend> oldData = getContentIdsOfRecommend(recommend.getId());
            long index=0;
            for(Content content:contents){
                ContentRecommend contentRecommend = null;
                for(int i=0,l=oldData.size()-1;l>=0;l--){
                    ContentRecommend oldCr = oldData.get(l);
                    Long contentId = oldCr.getContentId();
                    if(contentId!=null&&contentId.equals(content.getId())){
                        oldCr.setDisplayOrder(index);
                        contentRecommend = save(oldCr);
                        oldData.remove(oldCr);
                        break;
                    }
                }
                if(contentRecommend==null){
                    contentRecommend = new ContentRecommend();
                    contentRecommend.setDisplayOrder(index);
                    contentRecommend.setChannelId(recommend.getChannelId());
                    contentRecommend.setContentId(content.getId());
                    contentRecommend.setCspId(content.getCspId());
                    contentRecommend.setRecommendId(recommend.getId());
                    contentRecommend = (save(contentRecommend));
                }
                result.add(contentRecommend);
                index++;
            }
            if(oldData.size()>0){
                for(ContentRecommend cr:oldData){
                    remove(cr);
                }
            }
        }
        return result;
    }

    public List<ContentRecommend> saveContentRecommendOfChannel(List<Content> contents, long channelId){
        return saveContentRecommend(contents,recommendLogicInterface.getRecommendByChannelId(channelId));
    }

    public List<ContentRecommend> saveContentRecommend(List<Content> contents,String recommendCode){
        return saveContentRecommend(contents,getRecommendByCode(recommendCode));
    }
}
