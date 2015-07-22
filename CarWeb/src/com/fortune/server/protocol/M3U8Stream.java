package com.fortune.server.protocol;

import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjliu on 2014/7/13.
 * Stream
 */
public class M3U8Stream {
    public static final String TARGET_DURATION="EXT-X-TARGETDURATION";
    public static final String MEDIA_SEQUENCE="EXT-X-MEDIA-SEQUENCE";
    public static final String EXT_INF="EXTINF";
    public static final String END_LIST="EXT-X-ENDLIST";
    public static final String EXT_X_VERSION="EXT-X-VERSION";
    public static final String EXT_X_ALLOW_CACHE="EXT-X-ALLOW-CACHE";
    private int bandwidth;
    private int programId;
    private float targetDuration;
    private int mediaSequence;
    private boolean isLiveStream;
    private String url;
    private String version;
    private String allowCache;
    private float allDuration;

    private List<M3U8Segment> segments=new ArrayList<M3U8Segment>();

    private Logger logger = Logger.getLogger(this.getClass());
    public M3U8Stream() {
    }

    public M3U8Stream(int bandwidth, int programId, String url,String m3u8) {
        this.bandwidth = bandwidth;
        this.programId = programId;
        this.url = url;
        this.allDuration = 0.0f;
        this.setLive(false);
        if(m3u8!=null){
            m3u8 = m3u8.trim();
            if(m3u8.startsWith("#EXTM3U")){//嗯，这是一个m3u8
                String[] lines = m3u8.split("\n");
                float segmentDuration = 0;
                String segmentUrl;
                for(int i=1,l=lines.length;i<l;i++){
                    //第一行跳过
                    String line = lines[i].trim();
                    if(line.startsWith("#"+TARGET_DURATION+":")){
                        line = line.substring(TARGET_DURATION.length()+2);
                        try {
                            targetDuration = Float.parseFloat(line);
                        } catch (NumberFormatException e) {
                            segmentDuration = 22.222f;
                        }
                    }else if(line.startsWith("#"+MEDIA_SEQUENCE+":")){
                        mediaSequence = StringUtils.string2int(line.substring(MEDIA_SEQUENCE.length()+2),-1);
                    }else if(line.startsWith("#"+EXT_X_VERSION+":")){
                        version = line.substring(EXT_X_VERSION.length()+2);
                    }else if(line.startsWith("#"+EXT_X_ALLOW_CACHE+":")){
                        allowCache = line.substring(EXT_X_ALLOW_CACHE.length()+2);
                    }else if(line.startsWith("#"+EXT_INF+":")){
                        line = line.substring(EXT_INF.length()+2);
                        try {
                            segmentDuration =Float.parseFloat(line.substring(0,line.length()-1));
                        } catch (NumberFormatException e) {
                            segmentDuration = 0;
                        }
                    }else if(line.startsWith("#"+END_LIST)){
//                        logger.debug("已到结尾，不再处理");
                        isLiveStream = false;
                        break;
                    }else if(line.startsWith("#")){
                        logger.warn("暂时还不支持的指令："+line);
                    }else{
                        //
                        segmentUrl = line;
                        if(!segmentUrl.startsWith("http://")){
                            int p=segmentUrl.indexOf("/");
                            if(p==0){//如果是绝对地址，就要把stream.url
                                String clearUrl = StringUtils.getClearURL(url);
                                segmentUrl = url.substring(0,url.length()-clearUrl.length())+segmentUrl;
                            }else{
                                p = url.lastIndexOf("/");
                                if(p>0){
                                    segmentUrl = url.substring(0,p)+"/"+segmentUrl;
                                }
                            }
                        }

                        if(segmentDuration>=0){
                            if(targetDuration<segmentDuration){
                                targetDuration = segmentDuration;
                            }
                            allDuration+=segmentDuration;
                            segments.add(new M3U8Segment(segmentDuration,segmentUrl));
                            segmentDuration = 0;
                            //segmentUrl = "";
                        }
                    }
                }
            }
        }
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<M3U8Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<M3U8Segment> segments) {
        this.segments = segments;
    }

    public float getTargetDuration() {
        return targetDuration;
    }

    public void setTargetDuration(float targetDuration) {
        this.targetDuration = targetDuration;
    }

    public int getMediaSequence() {
        return mediaSequence;
    }

    public void setMediaSequence(int mediaSequence) {
        this.mediaSequence = mediaSequence;
    }

    public boolean isLive() {
        return isLiveStream;
    }

    public void setLive(boolean live) {
        isLiveStream = live;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAllowCache() {
        return allowCache;
    }

    public void setAllowCache(String allowCache) {
        this.allowCache = allowCache;
    }

    public float getAllDuration() {
        return allDuration;
    }

    public void setAllDuration(float allDuration) {
        this.allDuration = allDuration;
    }
}
