package com.fortune.server.protocol;

/**
 * Created by xjliu on 2014/7/13.
 * M3U8中的一个Segment结构
 */
public class M3U8Segment {
    private float duration;
    private String url;

    public M3U8Segment() {
    }

    public M3U8Segment(float duration, String url) {
        this.duration = duration;
        this.url = url;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
