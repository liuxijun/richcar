package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentRelatedDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRelated;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ContentRelatedDaoAccess
		extends
			BaseDaoAccess<ContentRelated, Long>
		implements
			ContentRelatedDaoInterface {
	public ContentRelatedDaoAccess() {
		super(ContentRelated.class);
	}

    @SuppressWarnings("unchecked")
    public List<ContentRelated> getRelatedContents(Long contentId) {
        if(contentId!=null&&contentId>0){
            return this.getHibernateTemplate().find("from ContentRelated cr " +
                    " where cr.contentId=" + contentId+
                    " order by cr.displayOrder asc");
        }
        return new ArrayList<ContentRelated>();
    }
}
