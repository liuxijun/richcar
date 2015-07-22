package com.fortune.util;

import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.content.logic.logicImpl.ContentRecommendLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.publish.dao.daoInterface.ChannelDaoInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.syn.logic.logicImpl.SynFileLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.rms.timer.DelayRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 11-9-20
 * Time: 下午4:50
 * 生成json格式的js文件
 */
public class JsUtils {
    protected Log log = LogFactory.getLog(this.getClass());

    public void createRecommendJsFile(String recommendCode, String fullName) {
        String contentStr = getIndexContent(recommendCode);
        FileWriter fw = null;
        try {
            File filePath = new File(FileUtils.extractFilePath(fullName, "/"));
            try {
                if (filePath.mkdirs()) {
                }
            } catch (Exception e) {
                log.error(e);
            }
            fw = new FileWriter(fullName);
            fw.write(contentStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createRecommendJsFile(String recommendId, long cspId, String fullFilePath) throws Exception {

        if(recommendId.indexOf("_")!=-1){
           recommendId = recommendId.split("_")[1];
        }
        long id = Long.parseLong(recommendId);
        String fileName;
        String fullName = "";
        String filePath;
        String fileUrl = "";
        String contentStr;

        String recommendCode = getRecommendCodeById(id);
        fileName = recommendCode + ".js";

        String alias = getSpAliasByCspId(cspId);
        if (alias != null) {
            fileUrl = "/page/" + alias + "/js/" + fileName;
            filePath = fullFilePath + "/page/" + alias + "/js";
            fullName = filePath + "/" + fileName;
            File dirFile = new File(filePath);
            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                dirFile.mkdirs();
            }
        }
        File file = new File(fullName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentStr = getIndexContent(recommendCode);


        FileWriter fw = null;
        try {
            fw = new FileWriter(fullName);
            fw.write(contentStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!fileUrl.equals("")) {

            SynFile synFile = createSynFile(fileName, fullName, fileUrl,cspId);
            if (synFile != null) {
                long synFileId = saveSynFile(synFile);
                if (synFileId > 0) {
                    saveSynTask(synFileId);
                    pushSynFile(synFileId);
                }
            }

        }
    }

    public void addChannelIdToRunner(String channelIds, long cspId, String fullFilePath) {
        String[] channelTemp = channelIds.split(",");
        for (String channelId : channelTemp) {
            DelayRunner.getInstance().startSession("list_" + channelId, cspId, fullFilePath);
        }
    }


    public void createChannelJsFile(long channelId, long cspId, String fullNamePrefix, PageBean pageBean) {
        String contentStr;
        int maxPageSize = 10;
        int pageCount = 0;
        for (int i = 1; i <= maxPageSize; i++) {
            String fullName = fullNamePrefix + "_" + i + ".js";
            pageBean = new PageBean(i, pageBean.getPageSize(), pageBean.getOrderBy(), pageBean.getOrderDir());
            contentStr = getListContent(cspId, 2L, channelId, pageBean);
            pageCount = pageBean.getPageCount();
            if (i > pageCount) {
                break;
            }
            if (contentStr != null) {
                FileWriter fw = null;
                try {
                    File file = new File(fullName);
                    File parentDir = file.getParentFile();
                    if(!parentDir.exists()){
                        if(parentDir.mkdirs()){

                        }
                    }
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    fw = new FileWriter(fullName);
                    fw.write(contentStr);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


    }

    public void createListJsFile(String channelCode, long cspId, String fullFilePath) throws Exception {
        long channelId = Long.parseLong(channelCode.split("_")[1]);
        String filePath;
        String fileName;
        String fullNamePrefix = "";
        String fileUrl = "";
        String fullName = "";
        String contentStr;
        String alias = getSpAliasByCspId(cspId);

        fileName = channelCode;


        if (alias != null) {
            fileUrl = "/page/" + alias + "/js/" + fileName;
            filePath = fullFilePath + "/page/" + alias + "/js";
            fullNamePrefix = filePath + "/" + fileName;
            File dirFile = new File(filePath);
            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                dirFile.mkdirs();
            }
        }

        PageBean pageBean;
        int maxPageSize = 10;
        int pageCount = 0;
        for (int i = 1; i <= maxPageSize; i++) {

            pageBean = new PageBean(i, 30, null, null);
            contentStr = getListContent(cspId, 2L, channelId, pageBean);
            pageCount = pageBean.getPageCount();
            if (contentStr != null) {
                if (i > pageCount) {
                    break;
                }
                fileName = channelCode + "_" + i + ".js";
                fullName = fullNamePrefix + "_" + i + ".js";
                fileUrl = "/page/" + alias + "/js/" + fileName;
                FileWriter fw = null;
                try {
                    File file = new File(fullName);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    fw = new FileWriter(fullName);
                    fw.write(contentStr);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!fileUrl.equals("")) {

                SynFile synFile = createSynFile(fileName, fullName, fileUrl,cspId);
                if (synFile != null) {
                    long synFileId = saveSynFile(synFile);
                    if (synFileId > 0) {
                        saveSynTask(synFileId);
                        pushSynFile(synFileId);
                    }
                }

            }

        }


    }


    public static String getSpAliasByCspId(long cspId) {
        String alias = null;
        if (cspId > 0) {
            TemplateLogicInterface templateLogicInterface = (TemplateLogicInterface) SpringUtils.getBeanForApp("templateLogicInterface");
            alias = templateLogicInterface.getCspAliasByCspId(cspId);
        }

        return alias;
    }

    public static long getPageSizeByCspId(long channelId) {
        long pageSize = -1;
        if (channelId > 0) {
            TemplateLogicInterface templateLogicInterface = (TemplateLogicInterface) SpringUtils.getBeanForApp("templateLogicInterface");
            pageSize = templateLogicInterface.getPageSizeByChannelId(channelId);
        }
        return pageSize;
    }

    public static String getRecommendCodeById(long recommendId) {
        ContentRecommendLogicInterface contentRecommendLogicInterface = (ContentRecommendLogicImpl) SpringUtils.getBeanForApp("contentRecommendLogicInterface");

        return contentRecommendLogicInterface.getRecommendCodeById(recommendId);
    }

    public static String getIndexContent(String recommendCode) {
        ContentRecommendLogicInterface contentRecommendLogicInterface = (ContentRecommendLogicImpl) SpringUtils.getBeanForApp("contentRecommendLogicInterface");
        Map<String, Object> result = new HashMap<String, Object>();
        List<Content> contents = contentRecommendLogicInterface.getContents(recommendCode);
        result.put("movies", contents);
        result.put("topicId", recommendCode);
        JsonUtils jsonUtils = new JsonUtils();
        return jsonUtils.getJson(result);
    }

    public static String getListContent(final long cspId, final long contentCspStatus,final  long channelId,final PageBean pageBean) {
        return (String) CacheUtils.get(""+cspId+"_"+contentCspStatus+"_"+channelId+"_"+pageBean.getPageNo()+"_"+pageBean.getPageSize()+
                pageBean.getOrderBy()+"_"+pageBean.getOrderDir(),"jsonListCache",new DataInitWorker(){
             public Object init(Object key,String cacheName){
                 int pageSize = pageBean.getPageSize();
                 if(pageSize<=0){
                     pageSize =(int) getPageSizeByCspId(channelId);
                 }
                 pageBean.setPageSize(pageSize);
                 ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBeanForApp("contentLogicInterface");
                 Map<String, Object> objs = new HashMap<String, Object>();
                 List<Content> tempResult = contentLogicInterface.list(cspId, contentCspStatus, channelId, pageBean);
                 List<Content> result = new ArrayList<Content>();
                 for (Content c : tempResult) {
                     result.add(contentLogicInterface.getCachedContent(c.getId()));
                 }
                 objs.put("total", pageBean.getRowCount());
                 objs.put("objs", result);
                 JsonUtils jsonUtils = new JsonUtils();
                 return jsonUtils.getJson(objs);
             }
        });
    }

    public static List<Channel> getChannelsByCspId(long cspId) {
        ChannelDaoInterface channelDaoInterface = (ChannelDaoInterface) SpringUtils.getBeanForApp("channelDaoInterface");
        return channelDaoInterface.getChannelsByCspId(cspId);
    }

    public SynFile createSynFile(String fileName, String fullName, String fileUrl,long spId) {
        SynFile syn = new SynFile();
        syn.setName(fileName);
        syn.setUrl(fileUrl);
        try {
            String md5 = MD5Utils.getFileMD5String(fullName);
            syn.setMd5(md5);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        syn.setStartTime(new Date());
        syn.setType(SynFileLogicImpl.SYNFILE_ADD);
        syn.setSpId(spId);
        return syn;
    }

    public long saveSynFile(SynFile synFile) {
        SynFileLogicInterface synFileLogicInterface = (SynFileLogicInterface) SpringUtils.getBeanForApp("synFileLogicInterface");
        synFile = synFileLogicInterface.save(synFile);
/*
        SynTaskLogicInterface synTaskLogicInterface = (SynTaskLogicInterface) SpringUtils.getBeanForApp("synTaskLogicInterface");
        synTaskLogicInterface.addSynTask(synFile.getId(),0);
*/
        return synFile.getId();
    }

    public void saveSynFiles(List<SynFile> synFiles) {
        SynFileLogicInterface synFileLogicInterface = (SynFileLogicInterface) SpringUtils.getBeanForApp("synFileLogicInterface");
        for (SynFile synFile : synFiles) {
            synFileLogicInterface.save(synFile);
        }

    }

    public void saveSynTask(long synFileId) {
        SynTaskLogicInterface synTaskLogicInterface = (SynTaskLogicInterface) SpringUtils.getBeanForApp("synTaskLogicInterface");
        synTaskLogicInterface.addSynTask(synFileId,0);
    }

    public void pushSynFile(long synFileId) throws Exception {
        SynTaskLogicInterface synTaskLogicInterface = (SynTaskLogicInterface) SpringUtils.getBeanForApp("synTaskLogicInterface");
        synTaskLogicInterface.pushCurrentTask(synFileId);
    }

    public void saveAndPushSynFile(String fileName, String fullName, String fileUrl,long cspId) throws Exception {
        SynFile synFile = createSynFile(fileName,fullName,fileUrl,cspId);
        long synId = saveSynFile(synFile);
        saveSynTask(synId);
        pushSynFile(synId);
    }



}
