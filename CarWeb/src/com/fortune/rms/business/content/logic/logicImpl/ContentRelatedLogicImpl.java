package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentRelatedDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentRelatedLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRelated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("contentRelatedLogicInterface")
public class ContentRelatedLogicImpl extends BaseLogicImpl<ContentRelated>
		implements
			ContentRelatedLogicInterface {
	private ContentRelatedDaoInterface contentRelatedDaoInterface;
    private ContentLogicInterface contentLogicInterface;

	/**
	 * @param contentRelatedDaoInterface the contentRelatedDaoInterface to set
	 */
    @Autowired
	public void setContentRelatedDaoInterface(
			ContentRelatedDaoInterface contentRelatedDaoInterface) {
		this.contentRelatedDaoInterface = contentRelatedDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.contentRelatedDaoInterface;
	}
    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public List<Content> getRelatedContents(Long contentId){
        List<ContentRelated> tempResult = contentRelatedDaoInterface.getRelatedContents(contentId);
        List<Content> result = new ArrayList<Content>();
        if(tempResult!=null){
            for(ContentRelated cr:tempResult){
                Content c = contentLogicInterface.getCachedContent(cr.getContentId());
                result.add(c);
            }
        }
        return result;
    }
}
