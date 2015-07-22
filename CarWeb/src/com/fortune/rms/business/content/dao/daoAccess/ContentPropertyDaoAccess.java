package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentPropertyDaoInterface;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContentPropertyDaoAccess
		extends
			BaseDaoAccess<ContentProperty, Long>
		implements
			ContentPropertyDaoInterface {

	public ContentPropertyDaoAccess() {
		super(ContentProperty.class);
	}

    public boolean existsSubContentId(String subContentId) {
        boolean isExist = false; 
        ContentProperty contentProperty = getContentPropertyBySubContentId(subContentId);
        if(contentProperty!=null){
            isExist = true;
        }
        return isExist;
    }

    @SuppressWarnings("unchecked")
    public ContentProperty getContentPropertyBySubContentId(String subContentId) {
        ContentProperty contentProperty = null;
        try{
            String hql = "from ContentProperty c where c.subContentId = '"+subContentId+"'";
            List<ContentProperty> contentProperties = this.getHibernateTemplate().find(hql);
            if (contentProperties != null && contentProperties.size() >= 1) {
                contentProperty = contentProperties.get(0);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return contentProperty;
    }

    public void updateContentPropertyBySubContentId(ContentProperty contentProperty) {
        ContentProperty contentProperty1 = getContentPropertyBySubContentId(contentProperty.getSubContentId());
        contentProperty.setId(contentProperty1.getId());
        this.update(contentProperty);
    }

    public void removeContentPropertyBySubContentId(ContentProperty contentProperty) {
        ContentProperty contentProperty1 = getContentPropertyBySubContentId(contentProperty.getSubContentId());
        this.remove(contentProperty1.getId());
    }

    public List<ContentProperty> getContentPropertiesByContentIdAndPropertyIds(long contentId, Long[] propertyIds) {
        String hql = "from ContentProperty cp where cp.contentId="+contentId;
        if(propertyIds.length>0){
            hql+=" and cp.propertyId in(";
            for(Long id:propertyIds){
                hql+=id+",";
            }
            hql = hql.substring(0,hql.length()-1);
            hql+=")";
        }
        try {
            return getObjects(hql,new PageBean(0,Integer.MAX_VALUE,"cp.intValue","asc"));
        } catch (Exception e) {
            logger.error("无法获取ContentProperty数据："+e.getMessage()+",hql="+hql);
        }
        return new ArrayList<ContentProperty>(0);
    }

    public List<ContentProperty> getContentProperties(long contentId, long propertyId,long intValue) {
        String hql = "from ContentProperty cp where cp.contentId="+contentId+" and cp.propertyId ="+propertyId;
        hql+=" and cp.intValue="+intValue;
        try {
            return getObjects(hql,new PageBean(0,Integer.MAX_VALUE,"cp.intValue","asc"));
        } catch (Exception e) {
            logger.error("无法获取ContentProperty数据："+e.getMessage()+",hql="+hql);
        }
        return new ArrayList<ContentProperty>(0);
    }

    public int updateThumbPic(ContentProperty clip) {
        long id = clip.getId();
        if(id>0){
            return executeSQLUpdate("update CONTENT_PROPERTY set THUMB_PIC='"+clip.getThumbPic()+"' where ID = "+id);
        }else{
            return -1;
        }
    }
    public int updateStringValue(String contentId,String propertyId,String stringValue){
        //首先判断数据库是否存在这样的海报关系没有就要手动加入，有的话就修改
        String hqlStr="select * from CONTENT_PROPERTY  where CONTENT_ID="+contentId+" and PROPERTY_ID="+propertyId;
       int selectNum=executeSQLUpdate(hqlStr);
        if(selectNum>0){
            return executeSQLUpdate("update CONTENT_PROPERTY set STRING_VALUE='"+stringValue+"' where CONTENT_ID="+contentId+" and PROPERTY_ID="+propertyId);
        }
        return  executeSQLUpdate("insert into CONTENT_PROPERTY (id,content_id,property_id,string_value) values(FORTUNE_GLOBAL_SEQ.nextval,"+contentId+","+propertyId+",'"+stringValue+"')");
    }


    public ContentProperty getContentDownLoadUrl(long contentId,long intValue,long propertyId) {
        String hql = "from ContentProperty where contentId="+contentId+" and intValue="+intValue+" and propertyId="+propertyId;
        List list =  getHibernateTemplate().find(hql);
        return  (list != null&& list.size()>0)?(ContentProperty)list.get(0):null;
    }
}
