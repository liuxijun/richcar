package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentChannelDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service("contentChannelLogicInterface")
public class ContentChannelLogicImpl extends BaseLogicImpl<ContentChannel>
        implements
        ContentChannelLogicInterface {
    private ContentChannelDaoInterface contentChannelDaoInterface;
    private ContentLogicInterface contentLogicInterface;
    private ChannelLogicInterface channelLogicInterface;
    private static List<String> exportLogs = new ArrayList<String>();
    private static long willCopyMediaFileSize = 0;
    private static int exportedMediaFileCount = 0,exportedPicFileCount = 0,willCopyMediaFileCount;
    private static long exportedMediaFileSize = 0,exportedPicFileSize = 0;
    /**
     * @param contentChannelDaoInterface the contentChannelDaoInterface to set
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public void setContentChannelDaoInterface(
            ContentChannelDaoInterface contentChannelDaoInterface) {
        this.contentChannelDaoInterface = contentChannelDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentChannelDaoInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface){
        this.channelLogicInterface = channelLogicInterface;
    }

    public List<Content> list(Long cspId, Long channelId, String contentName, String directors, String actors,
                              List<ContentProperty> searchValues, PageBean pageBean) {
        List<ContentChannel> tempResult = contentChannelDaoInterface.list(cspId,
                channelId, contentName, directors, actors, searchValues, pageBean);

        List<Content> result = new ArrayList<Content>();
        for (ContentChannel cc : tempResult) {
            Content c = contentLogicInterface.getCachedContent(cc.getContentId());
            result.add(c);
        }
        return result;
    }

    public List<Channel> getChannelsByContentId(Long contentId) {
        return this.contentChannelDaoInterface.getChannelsByContentId(contentId);
    }

    public ContentChannel getOldData(long contentId, long channelId) {
        if(contentId<=0||channelId<=0){
            return null;
        }
        ContentChannel cc = new ContentChannel();
        cc.setChannelId(channelId);
        cc.setContentId(contentId);
        List<ContentChannel> hasData = search(cc);
        if (hasData != null && hasData.size() > 0) {
            return hasData.get(0);
        }
        return null;
    }

    public boolean setPublishStatus(long contentId, long channelId, long status) {
        if (!ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED.equals(status)) {
            return unPublishContent(contentId, channelId);
        } else {
            return publishContent(contentId, channelId);
        }
    }

    public boolean publishContent(long contentId, long channelId) {
        if(channelId<=0||contentId<=0){
            return false;
        }
        ContentChannel cc = getOldData(contentId, channelId);
        if (cc == null) {
            cc = new ContentChannel();
            cc.setChannelId(channelId);
            cc.setContentId(contentId);
            save(cc);
            return true;
        }
        return false;
    }

    public boolean unPublishContent(long contentId, long channelId) {
        if(channelId>0&&contentId>0){
            ContentChannel cc = getOldData(contentId, channelId);
            if (cc != null) {
                logger.debug("已经下线：contentId="+contentId+",channelId="+channelId);
                remove(cc);
                return true;
            }
        }else{
            if(contentId>0){
                logger.warn("没有输入合适的频道ID，按照约定，删除所有发布频道信息");
                List<ContentChannel> allPbulishedChannels = getContentPublishedChannels(contentId);
                for(ContentChannel cc:allPbulishedChannels){
                    remove(cc);
                    logger.debug("已经下线：contentId="+cc.getContentId()+",channelId="+cc.getChannelId());
                }
                return true;
            }else{
                logger.error("频道ID或媒体ID是负数，无法进行下线操作：contentId="+contentId+",channelId="+channelId);
            }
        }
        return false;
    }

    public boolean isExists(Long channelId, Long contentId) {
        ContentChannel cc = new ContentChannel();
        cc.setChannelId(channelId);
        cc.setContentId(contentId);
        List<ContentChannel> data = search(cc);
        return data != null && data.size() > 0;
    }

    public String getPosterPicName(String contentName,String propertyCode,String oldFileName){
        return contentName+"_"+propertyCode+"."+FileUtils.getFileExtName(oldFileName);
    }

    @SuppressWarnings("unchecked")
    public List<Content> scanChannelContents(List<Channel> channels,PageBean exportPageBean,String[] mediaUrlPropertyCodes){
        int maxMediaCountOfChannel = exportPageBean.getPageSize();
        debugLog("准备启动扫描");
        willCopyMediaFileSize = 0;
        willCopyMediaFileCount = 0;
        List<Content> scanContents=new ArrayList<Content>();
        //先扫描一遍所有要导出的媒体，然后进行进度显示
        for (Channel channel : channels) {
            PageBean pageBean = new PageBean(exportPageBean.getPageNo(),exportPageBean.getPageSize()*10,
                    exportPageBean.getOrderBy(),exportPageBean.getOrderDir());
            int exportedContentCountOfChannel=0;
            while(exportedContentCountOfChannel<maxMediaCountOfChannel&&pageBean.getPageNo()<50){
                debugLog("准备搜索频道“" + channel.getName() + "”下的媒体：No."+pageBean.getStartRow()+"->" + pageBean.getEndRow());
                List<Content> contents = contentLogicInterface.list(
                        -1L, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED,
                        channel.getId(),
                        null, pageBean);
                debugLog("搜索出频道“" + channel.getName() + "”下的媒体" + contents.size() + "个");
                for (Content content : contents) {
                    if(exportedContentCountOfChannel>maxMediaCountOfChannel){
                        break;
                    }
                    //导出视频文件、海报等信息，然后保存xml文件。
                    //文件都保存在exportDir+channel.name目录下，和影片名一致。后缀名分别是.mp4和.jpg等等原始的后缀名
                    Map<String, Object> properties = content.getProperties();
                    Device server = contentLogicInterface.getDevice(content.getDeviceId());
                    String mediaFilePath=null;
                    if (server != null) {
                        mediaFilePath = server.getLocalPath();
                    }
                    if(mediaFilePath==null){
                        errorLog(content.getName()+"无法获取服务器信息");
                    }
                    boolean mediaExists = false;

                    for (String urlProperty : mediaUrlPropertyCodes) {
                        List<Map<String,String>> clips = new ArrayList<Map<String,String>>();
                        Object clipsOfContent = properties.get(urlProperty);
                        if(clipsOfContent instanceof Map){
                            clips.add((Map<String,String>)clipsOfContent);
                        }else if(clipsOfContent instanceof List){
                            clips.addAll((List<Map<String,String>>)clipsOfContent);
                        }
                        debugLog("扫描《" +content.getName()+
                                "》媒体片段："+urlProperty);
                        properties.put(urlProperty,clips);
                        for(Map<String,String> clip:clips){
                            String url = clip.get("playUrl");
                            if (url != null) {
                                String fileName = mediaFilePath+"/"+url;
                                int p=fileName.indexOf("?");
                                if(p>0){
                                    fileName = fileName.substring(0,p);
                                }
                                File file = new File(fileName);
                                if (file.exists()&&file.length()>1024*2) {
                                    mediaExists = true;
                                    willCopyMediaFileSize += file.length();
                                    willCopyMediaFileCount++;
                                } else {
                                    errorLog(content.getName() + "媒体文件缺失：" + urlProperty + "," + file.getAbsolutePath());
                                }
                            } else {
                                errorLog(content.getName() + "没有媒体文件数据：" + urlProperty);
                            }
                        }

                    }
                    if(mediaExists){
                        exportedContentCountOfChannel++;
                        content.getProperties().put("channel",channel);
                        scanContents.add(content);
                    }
                }
                if(contents.size()<pageBean.getPageSize()){
                    break;
                }
                //如果pageSize个数据还没有凑足要求的个数，就再搜索下一页的数据
                pageBean.setPageNo(pageBean.getPageNo()+1);
            }
            //result.addAll(contents);
        }
        debugLog("扫描过程结束");
        return scanContents;
    }

    public String[] getExportMediaUrls(){
        return AppConfigurator.getInstance().getConfig("export.mediaUrlPropertyCodes", "Media_Url_768k").split(",");
    }

    public List<Content> scanChannelContents(Long[] channelIds, PageBean exportPageBean) {
        return scanChannelContents(getChannels(channelIds),exportPageBean,getExportMediaUrls());
    }

    /**
     * 导出一个频道列表内所有的媒体，包括海报和媒体文件
     * @param channels   频道列表
     * @param exportDir  输出目录
     * @param exportPageBean   数量以及排序等信息s
     * @param posterDir  海报保存的目录
     * @return  导出所有媒体列表
     */
    @SuppressWarnings("unchecked")
    public List<Content> exportContents(List<Channel> channels, String exportDir, PageBean exportPageBean, String posterDir) {
        exportLogs.clear();
        exportLogs.add(StringUtils.date2string(new Date())+"-启动导出程序！");
        //因为会存在有些影片缺失片段的现象，所以影片取出10倍的量进行筛选，凑够数量为止。
        AppConfigurator config = AppConfigurator.getInstance();
        Map<String,String> channelPaths = new HashMap<String,String>();
        channelPaths.put("movie","movie");
        channelPaths.put("电影","movie");
        channelPaths.put("电视剧","theater");
        channelPaths.put("风尚","fashion");
        channelPaths.put("纪实","nature");
        channelPaths.put("名栏目","variety");
        channelPaths.put("娱乐","amusement");
        channelPaths.put("教育","child");
        channelPaths.put("新闻","news");
        channelPaths.put("音乐","mtv");
        channelPaths.put("体育","sport");

        String[] posterPropertyCodes = config.getConfig("export.posterPropertyCodes",
                "PC_MEDIA_POSTER_BIG,PC_MEDIA_POSTER_HORIZONTAL_BIG,MEDIA_PIC_RECOM2").split(",");
        String[] mediaUrlPropertyCodes =getExportMediaUrls();
        List<Content> contents = scanChannelContents(channels, exportPageBean, mediaUrlPropertyCodes);
        List<Content> result= new ArrayList<Content>();
        MediaUtils mediaUtils = new MediaUtils();
        exportDir=new File(exportDir).getAbsolutePath();
        for (Content content : contents) {
            //导出视频文件、海报等信息，然后保存xml文件。
            //文件都保存在exportDir+channel.name目录下，和影片名一致。后缀名分别是.mp4和.jpg等等原始的后缀名
            Map<String, Object> properties = content.getProperties();
            boolean posterExported=false;
            Channel channel =(Channel) properties.get("channel");
            if(channel==null){
                errorLog(content.getName()+"没有配置频道信息！");
                continue;
            }
            String contentName = content.getName();
            debugLog("媒体《" +content.getName()+
                    "》导出开始");
            contentName = contentName.replace(' ','-').replace(';','-').replace('?','-')
                    .replace('&','-').replace('%','-').replace('\\','-');
            contentName = contentName.replace('/','-').replace('*','-').replace('`','-')
                    .replace('~','-').replace('!','-').replace('$','-')
                    .replace('^','-').replace('(','-').replace(')','-')
                    .replace('+','-').replace('=','-').replace('\r','-')
                    .replace('\n','-').replace('\t','-').replace('\0','-')
                    .replace('\b','-');
            contentName = contentName.replaceAll("，","-").replaceAll("”","-").replaceAll("。","-")
                    .replaceAll("‘", "").replaceAll("’","").replaceAll("·","-").replaceAll("？","-")
                    .replaceAll("、", "-").replaceAll("》","").replaceAll("《", "").replaceAll("“","-").replaceAll("：","");
            String fullSpellContentName = HzUtils.getFullSpell(contentName);
            String tempStr = "";
            for(int i=0,l=fullSpellContentName.length();i<l;i++){
                char ch = fullSpellContentName.charAt(i);
                if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='_'||ch=='-'){
                    tempStr+=ch;
                }else{
                    tempStr+="_";
                }
            }
            fullSpellContentName = tempStr;
            tempStr = "";
            String pathName = channelPaths.get(channel.getName());
            if(pathName == null){
                pathName=HzUtils.getFullSpell(channel.getName());
                for(int i=0,l=pathName.length();i<l;i++){
                    char ch = pathName.charAt(i);
                    if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='_'||ch=='-'||ch=='/'){
                        tempStr+=ch;
                    }else{
                        tempStr+="_";
                    }
                }
                pathName = tempStr;
            }
            pathName +="/"+fullSpellContentName;
            String contentFileDir = new File(exportDir + "/" +pathName).getAbsolutePath()+"/";
            Device server = contentLogicInterface.getDevice(content.getDeviceId());
            String mediaFilePath = posterDir;
            if (server != null) {
                mediaFilePath = server.getLocalPath();
            }
            boolean mediaExported = false;

            for (String urlProperty : mediaUrlPropertyCodes) {
                List<Map<String,String>> clips = new ArrayList<Map<String,String>>();
                Object clipsOfContent = properties.get(urlProperty);
                if(clipsOfContent instanceof Map){
                    clips.add((Map<String,String>)clipsOfContent);
                }else if(clipsOfContent instanceof List){
                    clips.addAll((List<Map<String,String>>)clipsOfContent);
                }
                debugLog("尝试导出《" +content.getName()+
                        "》媒体片段："+urlProperty);
                properties.put(urlProperty,clips);
                for(Map<String,String> clip:clips){
                    String url = clip.get("playUrl");
                    String no = clip.get("no");
                    if (url != null) {
                        String fileName = mediaFilePath+"/"+url;
                        int p=fileName.indexOf("?");
                        if(p>0){
                            fileName = fileName.substring(0,p);
                        }
                        File file = new File(fileName);

                        if (file.exists()) {
                            String newFileName = fullSpellContentName+"_clip_"+urlProperty + "_vol_" + no+"."+ FileUtils.getFileExtName(file.getName());

                            File mediaFile=new File(contentFileDir+newFileName);
                            if((!mediaFile.exists())||mediaFile.length()!=file.length()){
                                debugLog("正在复制文件："+mediaFile.getAbsolutePath());
                                mediaFile= FileUtils.copy(file, contentFileDir, newFileName);
                            }
                            if(mediaFile!=null&&mediaFile.exists()&&mediaFile.length()>0){
                                debugLog("复制文件成功："+mediaFile.getAbsolutePath());
                                exportedMediaFileCount++;
                                exportedMediaFileSize += mediaFile.length();
                                mediaExported=true;
                                //修正连接，指向这个文件
                                url = mediaFile.getAbsolutePath().substring(exportDir.length()).replace('\\','/');
                                clip.put("playUrl",url);
                                //如果媒体文件复制成功，则开始考虑海报
                                if(!posterExported){
                                    for (String posterProperty : posterPropertyCodes) {
                                        String poster = (String) properties.get(posterProperty);
                                        boolean thisPosterExported = false;
                                        if (poster != null) {
                                            File posterFile = new File(posterDir + "/" + poster);
                                            if (posterFile.exists()) {
                                                newFileName = getPosterPicName(fullSpellContentName,posterProperty,poster);
                                                File picFile = FileUtils.copy(posterFile, contentFileDir, newFileName);
                                                if(picFile!=null&&picFile.exists()&&picFile.length()>0){
                                                    posterExported=true;
                                                    exportedPicFileCount++;
                                                    exportedPicFileSize+=picFile.length();
                                                    //修正content海报连接，指向这个文件
                                                    poster = picFile.getAbsolutePath().substring(exportDir.length()).replace('\\','/');
                                                    properties.put(posterProperty,poster);
                                                    thisPosterExported=true;
                                                }else{
                                                    errorLog(contentName+"海报复制过程失败！");
                                                }

                                            } else {
                                                errorLog(contentName + "海报文件缺失：" + posterProperty + "," + file.getAbsolutePath());
                                            }
                                        } else {
                                            errorLog(contentName + "没有海报数据：" + posterProperty);
                                        }
                                        //如果没有导出这个海报，就截图一个
                                        if(!thisPosterExported){
                                            debugLog(contentName+"尝试截图形成海报："+posterProperty);
                                            String newSnapPicFileName = new File(contentFileDir+ getPosterPicName(fullSpellContentName
                                                    ,posterProperty,"file.jpg")).getAbsolutePath();
                                            File posterFile = new File(newSnapPicFileName);
                                            if(posterFile.exists()&&posterFile.length()>10){
                                                posterExported = true;
                                                debugLog("海报文件已经存在，无需再创建："+posterFile.getAbsolutePath());
                                            }else{
                                                posterExported = mediaUtils.snapContentPosterFromMediaFile(mediaFile.getAbsolutePath(),
                                                        posterFile.getAbsolutePath(),
                                                        config.getIntConfig("export.snap.PosterWidth"+posterProperty,640),
                                                        config.getIntConfig("export.snap.PosterHeight"+posterProperty,480),
                                                        config.getIntConfig("export.snap.posterStartSeconds"+posterProperty,300));
                                            }
                                            File snapFile = new File(newSnapPicFileName);
                                            if(snapFile.exists()&&snapFile.length()>10){
                                                exportedPicFileCount++;
                                                exportedPicFileSize+=snapFile.length();
                                                properties.put(posterProperty,snapFile.getAbsolutePath().substring(exportDir.length()).replace('\\','/'));
                                            }
                                        }

                                    }
                                }
                            }else{
                                errorLog(content.getName()+"媒体文件" +contentFileDir+newFileName+
                                        "复制过程失败！");
                            }
                        } else {
                            errorLog(content.getName() + "媒体文件缺失：" + urlProperty + "," + file.getAbsolutePath());
                        }
                    } else {
                        errorLog(content.getName() + "没有媒体文件数据：" + urlProperty);
                    }
                }

            }
            if(mediaExported){
                debugLog("媒体输出正常！");
                //把其他的媒体播放类型去掉
                List<Map<String,String>> playUrlTypes =(List<Map<String,String>>) properties.get("playUrlTypes");
                if(playUrlTypes!=null){
                    for(Map<String,String> type:playUrlTypes){
                        String code = type.get("code");
                        boolean willExported = false;
                        for(String urlPropertyCode:mediaUrlPropertyCodes){
                            if(urlPropertyCode.equals(code)){
                                willExported = true;
                                break;
                            }
                        }
                        if(!willExported){
                            properties.remove(code);
                        }
                    }
                }
                result.add(content);
                List<Channel> publishedChannels = new ArrayList<Channel>();
                publishedChannels.add(channel);
                properties.put("channels",publishedChannels);
            }else{
                errorLog(content.getName() + " 没有导出任何视频文件！");
            }
            debugLog("媒体《" +content.getName()+
                    "》导出结束");
        }

        contentLogicInterface.saveToXml(exportDir+"/meta.xml",result,5000L);
        String message =("结束导出程序，导出媒体文件："+exportedMediaFileCount+"个，共"+
            StringUtils.formatBytes(exportedMediaFileSize)+",图片文件"+exportedPicFileCount+"个，"+StringUtils.formatBytes(exportedPicFileSize));
        FileUtils.writeContinue(exportDir,"meta.xml","\r\n<!--\r\n"+message+"\r\n-->");
        exportLogs.add("done");
        return result;
    }
    public List<Channel> getChannels(Long[] channelIds){
        List<Channel> channels = new ArrayList<Channel>();
        for(Long channelId:channelIds){
            if(channelId!=null){
                Channel channel = null;
                try {
                    channel = channelLogicInterface.get(channelId);
                } catch (Exception e) {
                    errorLog("无法初始化频道："+channelId);
                }
                if(channel!=null){
                    channels.add(channel);
                }
            }
        }
        return channels;
    }
    public List<Content> exportContents(Long[] channelIds, String exportDir, PageBean pageBean, String posterDir){
        return exportContents(getChannels(channelIds),exportDir,pageBean,posterDir);
    }

    public void errorLog(String message){
        logger.error(message);
        exportLog(message);
    }

    public void exportLog(String message){
        exportLogs.add(StringUtils.date2string(new Date())+"-"+message);
    }
    public void debugLog(String message){
        logger.debug(message);
        exportLog(message);
    }
    public int getProcess(){
        if(willCopyMediaFileSize==0){
           return 0;
        }
        return (int)(100*exportedMediaFileSize/willCopyMediaFileSize);
    }

    public long getWillCopyMediaFileSize() {
        return willCopyMediaFileSize;
    }

    @SuppressWarnings("unused")
    public  void setWillCopyMediaFileSize(long willCopyMediaFileSize) {
        ContentChannelLogicImpl.willCopyMediaFileSize = willCopyMediaFileSize;
    }

    @SuppressWarnings("unused")
    public  int getExportedMediaFileCount() {
        return exportedMediaFileCount;
    }

    @SuppressWarnings("unused")
    public  void setExportedMediaFileCount(int mediaFileCount) {
        exportedMediaFileCount = mediaFileCount;
    }

    public  int getExportedPicFileCount() {
        return exportedPicFileCount;
    }

    public List<ContentChannel> getContentPublishedChannels(long contentId) {
        if(contentId<=0){
            return new ArrayList<ContentChannel>(0);
        }
        ContentChannel bean = new ContentChannel();
        bean.setContentId(contentId);
        return search(bean);
    }

    @SuppressWarnings("unused")
    public void setExportedPicFileCount(int picFileCount) {
        ContentChannelLogicImpl.exportedPicFileCount = picFileCount;
    }

    public  long getExportedMediaFileSize() {
        return exportedMediaFileSize;
    }

    public long getWillCopyMediaFileCount(){
        return willCopyMediaFileCount;
    }
    @SuppressWarnings("unused")
    public void setMediaFileSize(long mediaFileSize) {
        exportedMediaFileSize = mediaFileSize;
    }

    @SuppressWarnings("unused")
    public long getPicFileSize() {
        return exportedPicFileSize;
    }

    @SuppressWarnings("unused")
    public void setPicFileSize(long picFileSize) {
        ContentChannelLogicImpl.exportedPicFileSize = picFileSize;
    }

    public List<String> getExportLogs(){
        return exportLogs;
    }
    public static void main(String[] args){
        ContentChannelLogicInterface logic = (ContentChannelLogicInterface) SpringUtils.getBeanForApp("contentChannelLogicInterface");
        if(logic!=null){
            logic.exportContents(new Long[]{
                    15884424L,//	电影
                    15884426L,//	电视剧
                    15884428L,//	新闻
                    15884430L,//	体育
                    15884432L,//	娱乐
                    15884434L,//	名栏目
                    15884436L,//	纪实
                    15884438L,//	音乐
                    15884440L,//	风尚
                    15884442L//	教育
          },"C:/exportSD",new PageBean(0,2,null,null),"C:/poster");
        }
    }
}
