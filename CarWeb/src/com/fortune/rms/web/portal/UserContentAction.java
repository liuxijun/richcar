package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentDTO;
import com.fortune.rms.business.content.model.ContentDetailDTO;
import com.fortune.rms.business.content.model.ContentTidyDTO;
import com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.portal.model.UserContentStatus;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.ChannelDTO;
import com.fortune.rms.business.publish.model.RecommendDTO;
import com.fortune.util.AppConfigurator;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ����· on 2014/12/10.
 * ǰ̨�û���ȡ��Ƶ��Action
 */
@Namespace("/uc")
@ParentPackage("default")
public class UserContentAction extends BaseAction<Content> {
    String CACHE_NAME = "channelRecommendContents";
    String CAROUSEL_CHANNEL_CODE = "slider";
    private List<ContentDTO> contentList;
    private List<ContentTidyDTO> tidyContentList;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;
    private RecommendLogicInterface recommendLogicInterface;
    private VisitLogLogicInterface visitLogLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private FrontUserLogicInterface frontUserLogicInterface;
    private UserFavoritesLogicInterface userFavoritesLogicInterface;

    public void setUserFavoritesLogicInterface(UserFavoritesLogicInterface userFavoritesLogicInterface) {
        this.userFavoritesLogicInterface = userFavoritesLogicInterface;
    }

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    private ChannelLogicInterface channelLogicInterface;
    private Long level; // ��ȡƵ���б�ʱ��ȡ��һ����ͬ��������һ��
    private Long channelId; // ��ȡƵ����Ϣʱ��Ƶ��Id
    private String searchWord;  // ��ѯ����
    private Long contentId;      // ��ȡ����ƵId
    private int similarityPriority; // �����Ƶ��ѯ���ȼ�

    public int getSimilarityPriority() {
        return similarityPriority;
    }

    public void setSimilarityPriority(int similarityPriority) {
        this.similarityPriority = similarityPriority;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public void setFrontUserLogicInterface(FrontUserLogicInterface frontUserLogicInterface) {
        this.frontUserLogicInterface = frontUserLogicInterface;
    }

    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    // �Ȳ������µĸ���
   private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setVisitLogLogicInterface(VisitLogLogicInterface visitLogLogicInterface) {
        this.visitLogLogicInterface = visitLogLogicInterface;
    }

    public void setRecommendLogicInterface(RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
    }

    public List<ContentDTO> getContentList() {
        return contentList;
    }

    public List<ContentTidyDTO> getTidyContentList() {
        return tidyContentList;
    }

    public UserContentAction() {
        super(Content.class);
    }

    /**
     * json��ʽ��������Ϣ
     */
    @Action(value = "carousel")
    @SuppressWarnings("unchecked")
    public void getCarousel() {
        // ����Ƿ��Ѿ���¼��ʹ�ò�ͬ��ԭҳ���key
        String cacheKey = "carousel";
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        contentList = (List<ContentDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                List<Content> contents = contentRecommendLogicInterface.getContents(CAROUSEL_CHANNEL_CODE, channelIdList, frontUser);

                List<ContentDTO> list = new ArrayList();
                for (Content c : contents) {
                    list.add(new ContentDTO(c));
                }
                return list;
            }
        });
        //return "carousel";
        directOut(com.fortune.util.JsonUtils.getListJsonString("contents", contentList, "totalCount", contentList.size()));
    }

    /**
     * �û����Թۿ���һ����Ŀ�������Ŀ��һ��ֻ��һ�������صڶ���
     * ����û�δ��¼���ɲ鿴������Ŀ
     */
    @Action(value = "topLvChannel")
    public void getTopLevelChannel(){
        String cacheKey = "topLevelChannel";
        FrontUser user = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }

        List<Channel> visibleChannelList = (List<Channel>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                if( session.get(Constants.SESSION_FRONT_USER_CHANNEL) != null){
                    //FrontUser frontUser = (FrontUser)session.get(Constants.SESSION_FRONT_USER);
                    return frontUserLogicInterface.getTopLevelChannel(
                            (List)session.get(Constants.SESSION_FRONT_USER_CHANNEL));
                }else{
                    // �������е�һ����Ŀ
                    return frontUserLogicInterface.getAllTopLevelChannel();
                }
            }
        });;

        directOut(JsonUtils.getListJsonString("channels", visibleChannelList, "totalCount",
                visibleChannelList == null ? 0 : visibleChannelList.size()));
    }

    /**
     * ��ҳ��Ŀ�Ƽ��б�����
     */
    @Action(value = "recommend")
    public void getChannelRecommend(){
        //��ѯ�û����Թۿ�����Ŀ�Ƽ�
        // ����Ƿ��Ѿ���¼��ʹ�ò�ͬ��ԭҳ���key
        String cacheKey = "recommend_list";
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        List<RecommendDTO> recommendList = (List<RecommendDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return recommendLogicInterface.getChannelRecommendList(frontUser, channelIdList);
            }
        });

        // ��ѯ�Ƽ��ľ�������
        if( recommendList != null && recommendList.size() > 0 ){
            for( final RecommendDTO recommendDTO : recommendList){
                cacheKey = "recommend_" + recommendDTO.getCode();
                if(frontUser != null) cacheKey += "_" + frontUser.getOrganizationId() + "_" + frontUser.getTypeId();
                List<ContentTidyDTO> contentTidyDTOList = (List<ContentTidyDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
                    public Object init(Object key, String cacheName) {
                        List<Content> contentList = contentRecommendLogicInterface.getContents(recommendDTO.getCode(), channelIdList, frontUser);
                        if( contentList != null){
                            List<ContentTidyDTO> list = new ArrayList<ContentTidyDTO>();
                            for(Content content: contentList){
                                list.add(new ContentTidyDTO(content));
                            }
                            return list;
                        }else{
                            return null;
                        }
                    }
                });
                recommendDTO.setContentList(contentTidyDTOList);
            }
        }


        directOut(com.fortune.util.JsonUtils.getListJsonString("recommends", recommendList, "totalCount", recommendList.size()));
    }
    public void setContentRecommendLogicInterface(ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
    }

    /**
     * ��ȡ���ȷ���ǰn����ͳ��ʱ�䳤�ȼ�������
     */
    @Action(value = "top")
    public void topN(){
        String cacheKey = "top10";
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        if(count == null || count == 0) count = AppConfigurator.getInstance().getIntConfig("redex.stat.count", 10);
        final int c = count;
        List<ContentDTO> dtoList = (List<ContentDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return visitLogLogicInterface.getTop(channelIdList,
                    (frontUser == null)? -1 : frontUser.getTypeId(),
                     AppConfigurator.getInstance().getIntConfig("redex.top.stat.days", 30),
                        c
                        );
            }
        });

        directOut(com.fortune.util.JsonUtils.getListJsonString("top", dtoList, "totalCount", dtoList.size()));
    }

    /**
     * ����ֱ�����б�
     */
    @Action(value = "living")
    public void livingList(){
        String cacheKey = "living";
        if(searchWord != null && !searchWord.isEmpty()){
            cacheKey += "_" + searchWord;
        }
        cacheKey += "_" + pageBean.getPageNo();
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        String jsonData = (String)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return com.fortune.util.JsonUtils.getListJsonString("contents",contentLogicInterface.getRedexContents(channelId,
                        searchWord, channelIdList, (frontUser == null)? -1 : frontUser.getTypeId(),
                        pageBean), "totalCount", pageBean.getRowCount());
            }
        });

        directOut(jsonData);
    }

    /*
    * ��ȡ���·�������
    * */

    @Action(value = "newest")
    public void newest(){
        String cacheKey = "newest";
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        if(count == null || count == 0) count = AppConfigurator.getInstance().getIntConfig("redex.stat.count", 10);
        final int c = count;
        List<ContentDTO> dtoList = (List<ContentDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return contentLogicInterface.getNewest(channelIdList,
                        (frontUser == null) ? -1 : frontUser.getTypeId(),
                        count
                );
            }
        });

        directOut(com.fortune.util.JsonUtils.getListJsonString("newest", dtoList, "totalCount", dtoList.size()));
    }

    /**
     * ��ȡ��Ŀ�ǣ�channelIdΪƵ��Id��levelΪҪ��ȡ�Ĳ��
     * level>0 ��ȡchannelId�ĸ��ڵ�͸��ڵ���ֵ���Ŀ
     * level=0 ��ȡchannelId��Ӧ����Ŀ��ͬ����Ŀ
     * level<0 ��ȡchannelId����Ŀ
     */
    @Action(value = "channels")
    public void getChannels(){
        // ��Ŀ����TreeUtils�л��棬��ʹ��CacheUtils
        List channelList = null;
        if (session.get(Constants.SESSION_FRONT_USER_CHANNEL) != null) {
            channelList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            ChannelDTO channelDTO = channelLogicInterface.getChannelDTO(channelId, level, channelList);
            directOut(com.fortune.util.JsonUtils.getJsonString(channelDTO, ""));
        }
    }

    /**
     * ��ѯ/�����Ŀ���ݣ����ܵ�����������channelId��searchWord��pageBean
     * ����������Ⱥ����£�Ĭ������
     */
    @Action(value = "contents")
    public void getContents(){
        String cacheKey = "contents";
        if(channelId != null && channelId > 0){
            cacheKey += "_" + channelId;
        }
        if(searchWord != null && !searchWord.isEmpty()){
            cacheKey += "_" + searchWord;
        }
        cacheKey += "_" + pageBean.getPageNo();
        FrontUser user = null;
        List<Long> idList = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        // Ϊ���㣬��json��Ĵ�д��cache
        /*
        List<ContentTidyDTO> dtoList = (List<ContentTidyDTO>)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return contentLogicInterface.getRedexContents(channelId,
                        searchWord, channelIdList, (frontUser == null)? -1 : frontUser.getTypeId(),
                        pageBean);
            }
        });
        */
        String jsonData = (String)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return com.fortune.util.JsonUtils.getListJsonString("contents",contentLogicInterface.getRedexContents(channelId,
                        searchWord, channelIdList, (frontUser == null)? -1 : frontUser.getTypeId(),
                        pageBean), "totalCount", pageBean.getRowCount());
            }
        });

        directOut(jsonData);
    }

    /**
     * ���һ����Ƶ����ϸ����
     */
    @Action(value = "detail")
    public void getContentDetail(){
        String cacheKey = "content_detail";
        if(contentId != null && contentId > 0){
            cacheKey += "_" + contentId;
        }
        FrontUser user = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
        }

        String jsonData = (String)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                ContentDetailDTO detailDTO = contentLogicInterface.getContentDetail(contentId, channelId);
//                detailDTO.setBookMark(bookMarkLogicInterface.getBookMarkOfUser());
                return com.fortune.util.JsonUtils.getJsonString(detailDTO);
            }
        });

        directOut(jsonData);
    }

    /**
     * ��ȡ�����Ƶ�б�
     */
    @Action(value="related")
    public void getRelatedContent(){
        String cacheKey = "related_" + contentId + "_" + similarityPriority;

        List<Long> idList = null;
        FrontUser user = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            idList = (List) session.get(Constants.SESSION_FRONT_USER_CHANNEL);
            cacheKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        final FrontUser frontUser = user;
        final List<Long> channelIdList = idList;

        String jsonData = (String)CacheUtils.get(cacheKey, CACHE_NAME, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                Content content = contentLogicInterface.get(contentId);
                List<ContentTidyDTO> dtoList = contentLogicInterface.getSimilarContents(content,
                        similarityPriority,
                        channelIdList,
                        (frontUser == null) ? -1 : frontUser.getTypeId()
                );
                return com.fortune.util.JsonUtils.getListJsonString("related", dtoList, "totalCount", dtoList.size());
            }
        });

        directOut(jsonData);
    }
}