package com.fortune.rms.business.content.model;

import com.fortune.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-10
 * Time: 11:33:48
 * 发布视频时上传/选择的视频文件信息
 */
public class ContentFile {
    private String name;
    private Long seq;
    private String path;
    private String origFileName;
    private Long resolutionWidth;
    private Long resolutionHeight;
    private Long duration;
    private String thumbPic;
    private String durationFormatted;

    public String getDurationFormatted() {
        return durationFormatted;
    }

    public String getName() {
        return name;
    }

    public ContentFile() {
    }

    public void setName(String name) {
        this.name = name;

    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrigFileName() {
        return origFileName;
    }

    public void setOrigFileName(String origFileName) {
        this.origFileName = origFileName;
    }

    public Long getResolutionWidth() {
        return resolutionWidth;
    }

    public void setResolutionWidth(Long resolutionWidth) {
        this.resolutionWidth = resolutionWidth;
    }

    public Long getResolutionHeight() {
        return resolutionHeight;
    }

    public void setResolutionHeight(Long resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
        durationFormatted = StringUtils.getTimeStr(duration*1000);
    }

    public String getThumbPic() {
        return thumbPic;
    }

    public void setThumbPic(String thumbPic) {
        this.thumbPic = thumbPic;
    }

    public ContentFile(String name, Long seq, String path, String origFileName, Long resolutionWidth, Long resolutionHeight, Long duration,String thumbPic) {
        this.name = name;
        this.seq = seq;
        this.path = path;
        this.origFileName = origFileName;
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.duration = duration;
        this.thumbPic = thumbPic;
        durationFormatted = StringUtils.getTimeStr(duration*1000);
    }
}
