package com.fortune.rms.business.template.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.template.dao.daoInterface.TemplateDaoInterface;
import com.fortune.rms.business.template.model.Template;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TemplateDaoAccess extends BaseDaoAccess<Template, Long>
        implements
        TemplateDaoInterface {

    public TemplateDaoAccess() {
        super(Template.class);
    }

    @SuppressWarnings("unchecked")
    public Long getPageSizeByChannelId(long channelId,int defaultSize) {
        long pageSize = -1;
        if (channelId < 0) {
            return (long) defaultSize;
        }
        List<Template> templates;
        String hql = "from Template t where t.id =(select ct.listTemplate " +
                "from ChannelTemplate ct where ct.channelId =" + channelId + ")";
        templates = this.getHibernateTemplate().find(hql);
        if (templates.size() != 0) {
            Template template = templates.get(0);
            if (template.getPageSize() <= 0) {
                pageSize = defaultSize;
            } else {
                pageSize = template.getPageSize();
            }
        } else {
            List<Channel> channels;
            hql = "from Channel c where c.id = " + channelId + "";
            channels = this.getHibernateTemplate().find(hql);
            if (channels.size() != 0) {
                Channel channel = channels.get(0);
                long parentId = channel.getParentId();
                if (parentId != -1) {
                    pageSize = getPageSizeByChannelId(parentId, ConfigManager.getInstance().getConfig("system.default.channelPageSize",10));
                } else {
                    pageSize = defaultSize;
                }
            }
        }
        return pageSize;
    }

}