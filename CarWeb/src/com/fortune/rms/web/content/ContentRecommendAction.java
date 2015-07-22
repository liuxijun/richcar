package com.fortune.rms.web.content;

import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.CacheUtils;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Namespace("/content")
@ParentPackage("default")
@Results({
        @Result(name = "mobile_index", location = "/page/3Screen/mobile_index.jsp"),
        @Result(name = "mobile_list", location = "/page/3Screen/mobile_list.jsp"),
        @Result(name = "mobile_detail", location = "/page/3Screen/mobile_detail.jsp"),
        @Result(name = "iphone_mobile_index", location = "/page/3Screen/iphone/mobile_index.jsp"),
        @Result(name = "iphone_mobile_list", location = "/page/3Screen/iphone/mobile_list.jsp"),
        @Result(name = "iphone_mobile_detail", location = "/page/3Screen/iphone/mobile_detail.jsp")
})
@Action(value = "contentRecommend")
public class ContentRecommendAction extends BaseAction<ContentRecommend> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private RecommendLogicInterface recommendLogicInterface;
    private List<Map<String, Object>> contentRecommendList;
    private List<Content> contentList;
    private List<Content> relatedContentList;
    private Channel channel;
    private Content content;
    private List<Channel> channelList;

    @SuppressWarnings("unchecked")
    public ContentRecommendAction() {
        super(ContentRecommend.class);
    }

    /**
     * @param contentRecommendLogicInterface the contentRecommendLogicInterface to set
     */

    public void setContentRecommendLogicInterface(
            ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
        setBaseLogicInterface(contentRecommendLogicInterface);
    }


    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public void setRecommendLogicInterface(RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
    }

    public String getContentRecommendListByRecommendCode() {
        String iphone = getRequestParam("iphone", "");
        String recommendCodes = "mobile_index_jctj,mobile_index_rqph,mobile_callery,mobile_sgtj,mobile_dptj,mobile_yltj,mobile_zytj,mobile_cqtj,mobile_online";
        contentRecommendList = contentRecommendLogicInterface.getContentRecommendListByRecommendCode(recommendCodes);
        if (iphone != null && !"".equals(iphone)) {
            return "iphone_mobile_index";
        } else {
            return "mobile_index";
        }
    }

    public String getContentListByChannelId() {
        String iphone = getRequestParam("iphone", "");
        long sortNum = getRequestIntParam("sortNum", 0);
        long cspId = getRequestIntParam("cspId", 0);
        long channelId = getRequestIntParam("channelId", 0);
        String contentName = getRequestParam("contentName", "");
        String directors = getRequestParam("directors", "");
        List<ContentProperty> searchValues = new ArrayList<ContentProperty>();
        String actors = getRequestParam("actors", "");
        int limit = getRequestIntParam("limit", 6);
        int start = (pageBean.getPageNo() - 1) * limit;

        pageBean = new PageBean((start + limit-1) / limit, limit, getRequestParam("orderBy", ""), getRequestParam("orderDir", ""));
        contentList = contentLogicInterface.list(cspId, 2L, channelId, contentName, directors, actors, searchValues, pageBean);
        contentRecommendList = contentRecommendLogicInterface.getContentRecommendListByRecommendCode("mobile_callery");
        channel = contentLogicInterface.getContentBindChannel(channelId, -1L);
        TreeUtils tu = TreeUtils.getInstance();

        tu.initCache(Channel.class);

        channelList = tu.getSonOf(Channel.class, channelId, null);


        if (channelList == null) {

            channelList = new ArrayList<Channel>();

        }
        if (iphone != null && iphone != "") {
            return "iphone_mobile_list";
        } else {
            return "mobile_list";
        }
    }
    public String saveContentRecommends(){
        setSuccess(false);
        String logMessage="保存推荐";
        if(recommendId>0&&keys!=null&&keys.size()>0){
            Recommend recommend = recommendLogicInterface.get(recommendId);
            logMessage="保存推荐，推荐名："+recommend.getName();
            logMessage+=",其中媒体有";
            List<Content> contents = new ArrayList<Content>();
            for(String key:keys){
                long id =StringUtils.string2long(key,-1);
                if(id>0){
                    Content content = contentLogicInterface.get(id);
                    logMessage+=","+content.getName();
                    contents.add(content);
                }
            }
            List<ContentRecommend> result = contentRecommendLogicInterface.saveContentRecommend(contents,new Recommend(recommendId));
            logMessage +="已经添加推荐绑定："+result.size()+"个";
            addActionMessage(logMessage);
            writeSysLog(logMessage);
            CacheUtils.clearAll();
            setSuccess(true);
        }else{
            if(recommendId<=0){
                logMessage+=",输入的参数有误：recommendId不正确："+recommendId;
            }else{
                logMessage+=(",输入的参数有误：keyIds不正确，为空或者参数个数是0!");
            }
            addActionError(logMessage);
        }
        return "success";
    }
    public String getContentDetail() {
        String iphone = getRequestParam("iphone", "");
        long cspId = getRequestIntParam("cspId", 0);
        long channelId = getRequestIntParam("channelId", 0);
        long contentId = getRequestIntParam("contentId", 0);
        content = contentLogicInterface.getCachedContent(contentId);
        relatedContentList = contentLogicInterface.getRelatedContents(contentId, cspId, null);
        channel = contentLogicInterface.getContentBindChannel(channelId, contentId);
        if (iphone != null && !"".equals(iphone)) {
            return "iphone_mobile_detail";
        } else {
            return "mobile_detail";
        }
    }


    public List<Map<String, Object>> getContentRecommendList() {
        return contentRecommendList;
    }

    public void setContentRecommendList(List<Map<String, Object>> contentRecommendList) {
        this.contentRecommendList = contentRecommendList;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public List<Content> getRelatedContentList() {
        return relatedContentList;
    }

    public void setRelatedContentList(List<Content> relatedContentList) {
        this.relatedContentList = relatedContentList;
    }
    private Long recommendId;

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }
}
