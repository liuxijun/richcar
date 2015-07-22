package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentRecommendDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.publish.dao.daoInterface.RecommendDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.rms.business.publish.model.RecommendDTO;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.util.AppConfigurator;
import com.fortune.util.JsonUtils;
import com.fortune.util.PageBean;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Service("recommendLogicInterface")
public class RecommendLogicImpl extends BaseLogicImpl<Recommend>
        implements
        RecommendLogicInterface {
    private RecommendDaoInterface recommendDaoInterface;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;

    @Autowired
    public void setContentRecommendInterface(ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
    }

    private TemplateLogicInterface templateLogicInterface;

    /**
     * @param recommendDaoInterface the recommendDaoInterface to set
     */
    @Autowired
    public void setRecommendDaoInterface(
            RecommendDaoInterface recommendDaoInterface) {
        this.recommendDaoInterface = recommendDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.recommendDaoInterface;
    }
    @Autowired
    public void setTemplateLogicInterface(TemplateLogicInterface templateLogicInterface) {
        this.templateLogicInterface = templateLogicInterface;
    }

    public void createJsFile(long cspId) {
        List<Recommend> objs = this.recommendDaoInterface.getRecommendsByCspId(cspId);
        JsonUtils jsonUtils = new JsonUtils();
        String jsonString = jsonUtils.getListJson(objs);
        ServletContext context = ServletActionContext.getServletContext();
        String alias = this.templateLogicInterface.getCspAliasByCspId(cspId);
        String targetDirectory = context.getRealPath("/page/" + alias + "/recommend/recommendCode.js");

        try {
            createFile(targetDirectory);
            FileWriter fw = new FileWriter(targetDirectory);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(jsonString);
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Recommend> getRecommendsByCspId(Long cspId){
        return recommendDaoInterface.getRecommendsByCspId(cspId);
    }


    public long getCspIdByCode(String code) {
        return recommendDaoInterface.getCspIdByCode(code);
    }

    public static File createFile(String path) {
        File file = new File(path);

        File parent = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));

        if (!parent.exists()) {
            createFile(parent.getPath());

            parent.mkdirs();
        }
        return file;
    }

    public List<Recommend> getRecommend(Long cspId,Long type,String name,String code, PageBean pageBean)    {
         return recommendDaoInterface.getRecommend(cspId,type,name,code,pageBean);
     }


    public Recommend getRecommendByChannelId(Long channelId) {
         return recommendDaoInterface.getRecommendByChannelId(channelId);
    }

    /**
     * 前台用户user可以观看的栏目推荐列表
     * @param user 前台用户
     * @param channelIdList 用户可以观看的频道列表，如果为空则不限
     * @return 推荐列表，推荐中的内容没有初始化
     */
    public List<RecommendDTO> getChannelRecommendList(FrontUser user, List<Long> channelIdList){
        String recommendCodes = AppConfigurator.getInstance().getConfig("system.page.index.recommendCodes","slider,news,culture");
        List<Recommend> recommendList=new ArrayList<Recommend>();
        List<RecommendDTO> recommendDTOList = new ArrayList<RecommendDTO>();
        if(null!=recommendCodes){
            String[] codes = recommendCodes.split(",");
            for(String code:codes){
                Recommend recommend = contentRecommendLogicInterface.getRecommendByCode(code);
                if(recommend!=null){
                    recommendList.add(recommend);
                }else{
                    logger.error("没有找到推荐，推荐代码："+code);
                }

            }
        }else{
            recommendList = recommendDaoInterface.getChannelRecommendByChannelList(channelIdList);
            if(recommendList == null || recommendList.size() == 0) return null;

        }
        for( Recommend recommend : recommendList){
            RecommendDTO recommendDTO = new RecommendDTO(recommend);
            recommendDTOList.add(recommendDTO);
        }
        return recommendDTOList;
    }

}
