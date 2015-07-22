package com.fortune.rms.business.content.model;

import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.user.model.BookMark;

import java.util.List;

/**
 * Created by ����· on 2014/12/22.
 * ��Ƶ����
 */
public class ContentDetailDTO implements Cloneable{
    public  static  final int _CONTENT_TYPE_VOD = 1;    //�㲥
    public  static  final int _CONTENT_TYPE_LIVE = 2;   // ֱ��

    private Long id;                    // Id
    private String title;               // ����
    private String actor;               // ����
    private String poster;              // С����
    private String bigPoster;           // �󺣱�
    private String intro;               // ������Ϣ
    private Long like;                  // �޵ĸ���
    private Long dislike;               // �ȵĸ���
    private Long visitCount;            // �ܷ��ʴ���
    private Long favorite;              // �ղ���
    private Boolean bookmarked;          // �Ƿ��Ѿ��ղ�
    //private BookMark bookMark;          //��ǩ
    private int contentType;                   // �������ͣ���㲥��ֱ����

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    private List<Channel> channelPath;              // �Ӹ���Ŀ����Ƶ������Ŀ��·��
    private List<ContentTidyDTO> similarContents;  // �����Ƶ  ****Ĭ�ϳ�ʼ������****
    private List<EpisodeDTO> episodeList;           // ѡ����Ϣ

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
