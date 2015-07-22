package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentServiceProductDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentServiceProductLogicInterface;
import com.fortune.rms.business.content.model.ContentServiceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contentServiceProductLogicInterface")
public class ContentServiceProductLogicImpl
		extends
			BaseLogicImpl<ContentServiceProduct>
		implements
			ContentServiceProductLogicInterface {
	private ContentServiceProductDaoInterface contentServiceProductDaoInterface;

	/**
	 * @param contentServiceProductDaoInterface the contentServiceProductDaoInterface to set
	 */
    @Autowired
	public void setContentServiceProductDaoInterface(
			ContentServiceProductDaoInterface contentServiceProductDaoInterface) {
		this.contentServiceProductDaoInterface = contentServiceProductDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.contentServiceProductDaoInterface;
	}

    public List<ContentServiceProduct> getContentServiceProductsByContentIdAndCspId(Long contentId,Long cspId) {
         return contentServiceProductDaoInterface.getContentServiceProductsByContentIdAndCspId(contentId,cspId);
    }
}
