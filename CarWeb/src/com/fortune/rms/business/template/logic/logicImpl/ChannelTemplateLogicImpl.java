package com.fortune.rms.business.template.logic.logicImpl;


import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.ChannelDaoInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.template.dao.daoInterface.ChannelTemplateDaoInterface;
import com.fortune.rms.business.template.dao.daoInterface.TemplateDaoInterface;
import com.fortune.rms.business.template.logic.logicInterface.ChannelTemplateLogicInterface;
import com.fortune.rms.business.template.model.ChannelTemplate;
import com.fortune.rms.business.template.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("channelTemplateLogicInterface")
public class ChannelTemplateLogicImpl extends BaseLogicImpl<ChannelTemplate>
		implements
        ChannelTemplateLogicInterface {

    private ChannelTemplateDaoInterface channelTemplateDaoInterface;
    private ChannelDaoInterface channelDaoInterface;
    private TemplateDaoInterface templateDaoInterface;


            /**
        * @param channelTemplateDaoInterface the channelTemplateDaoInterface to set
        */
            @Autowired
            public void setChannelTemplateDaoInterface(ChannelTemplateDaoInterface channelTemplateDaoInterface) {
                this.channelTemplateDaoInterface = channelTemplateDaoInterface;
                baseDaoInterface=(BaseDaoInterface)channelTemplateDaoInterface;
            }
    @Autowired
    public void setChannelDaoInterface(ChannelDaoInterface channelDaoInterface) {
        this.channelDaoInterface = channelDaoInterface;
    }
    @Autowired
    public void setTemplateDaoInterface(TemplateDaoInterface templateDaoInterface) {
        this.templateDaoInterface = templateDaoInterface;
    }

    public Template getChannelIndexTemplate(Long channelId){
        return getChannelTemplate(channelId,TEMPLATE_TYPE_INDEX);
    }

    public Template getChannelListTemplate(Long channelId){
        return getChannelTemplate(channelId,TEMPLATE_TYPE_LIST);
    }

    public Template getChannelDetailTemplate(Long channelId){
        return getChannelTemplate(channelId,TEMPLATE_TYPE_DETAIL);
    }

    public Template getChannelTemplate(Long channelId,int templateType ){
        ChannelTemplate ct = null;
        try {
            ct = get(channelId);
        } catch (Exception e) {
            logger.error("没有找到这个频道对应的模版："+channelId);
        }
        if(ct==null){
            Channel c = channelDaoInterface.get(channelId);
            if(c.getParentId()!=null&&c.getParentId()>0){
                return getChannelTemplate(c.getParentId(),templateType);
            }else{
                return null;
            }
        }else{
            logger.debug("找到一个模版，频道ID:"+channelId);
            if(templateType==TEMPLATE_TYPE_INDEX){
                return templateDaoInterface.get(ct.getIndexTemplate());
            }else if(templateType==TEMPLATE_TYPE_LIST){
                return templateDaoInterface.get(ct.getListTemplate());
            }
            return templateDaoInterface.get(ct.getDetailTemplate());
        }
    }
   
  

}