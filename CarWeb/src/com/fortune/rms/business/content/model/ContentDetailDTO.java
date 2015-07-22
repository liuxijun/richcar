package com.fortune.rms.business.content.model;

import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.user.model.BookMark;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/22.
 * 视频详情
 */
public class ContentDetailDTO implements Cloneable{
    public  static  final int _CONTENT_TYPE_VOD = 1;    //点播
    public  static  final int _CONTENT_TYPE_LIVE = 2;   // 直播

    private Long id;                    // Id
    private String title;               // 标题
    private String actor;               // 主创
    private String poster;              // 小海报
    private String bigPoster;           // 大海报
    private String intro;               // 介绍信息
    private Long like;                  // 赞的个数
    private Long dislike;               // 踩的个数
    private Long visitCount;            // 总访问次数
    private Long favorite;              // 收藏数
    private Boolean bookmarked;          // 是否已经收藏
    //private BookMark bookMark;          //书签
    private int contentType;                   // 内容类型，如点播、直播等

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    private List<Channel> channelPath;              // 从根栏目到视频所属栏目的路径
    private List<ContentTidyDTO> similarContents;  // 相关视频  ****默认初始化不含****
    private List<EpisodeDTO> episodeList;           // 选集信息

    public ContentDetailDTO() {
    }

    public ContentDetailDTO(Content content){
        id = content.getId();
        title = content.getName();
        actor = content.getActors();
        intro = content.getIntro();
        poster = content.getPost1Url();
        bigPoster = content.getPost2Url();
        like = 0L;
        dislike = 0L;
        favorite = 0L;
        bookmarked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBigPoster() {
        return bigPoster;
    }

    public void setBigPoster(String bigPoster) {
        this.bigPoster = bigPoster;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public Long getDislike() {
        return dislike;
    }

    public void setDislike(Long dislike) {
        this.dislike = dislike;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getFavorite() {
        return favorite;
    }

    public void setFavorite(Long favorite) {
        this.favorite = favorite;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public List<Channel> getChannelPath() {
        return channelPath;
    }

    public void setChannelPath(List<Channel> channelPath) {
        this.channelPath = channelPath;
    }

    public List<ContentTidyDTO> getSimilarContents() {
        return similarContents;
    }

    public void setSimilarContents(List<ContentTidyDTO> similarContents) {
        this.similarContents = similarContents;
    }

    public List<EpisodeDTO> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<EpisodeDTO> episodeList) {
        this.episodeList = episodeList;
    }

/*
    public BookMark getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
    }
*/

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    /**
     * toString return json format string of this bean
     * @return String
     */
    public String toString() {
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, "");
    }
}
