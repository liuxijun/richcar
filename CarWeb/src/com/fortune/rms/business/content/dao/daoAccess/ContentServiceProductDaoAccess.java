package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentServiceProductDaoInterface;
import com.fortune.rms.business.content.model.ContentServiceProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentServiceProductDaoAccess
		extends
			BaseDaoAccess<ContentServiceProduct, Long>
		implements
			ContentServiceProductDaoInterface {

	public ContentServiceProductDaoAccess() {
		super(ContentServiceProduct.class);
	}


    public List<ContentServiceProduct> getContentServiceProductsByContentIdAndCspId(Long contentId,Long cspId) {
        String hql = "from ContentServiceProduct where contentId ="+contentId+" and serviceProductId in (select id from ServiceProduct where cspId ="+cspId+" )";
        return this.getHibernateTemplate().find(hql);
    }

}
