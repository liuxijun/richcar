package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ContentDaoAccess extends BaseDaoAccess<Content, Long>
        implements
        ContentDaoInterface {

    public ContentDaoAccess() {
        super(Content.class);
    }

    private String getBaseHql(Long cspId, Long contentCspStatus, Long channelId) {
        String hql = "";
        if ((cspId != null && cspId > 0) || (contentCspStatus != null && contentCspStatus > 0)) {
            String csHql = "";
            if (cspId != null && cspId > 0) {
                csHql = "cs.cspId=" + cspId;
            }
            if (contentCspStatus != null && contentCspStatus > 0) {
                if (!"".equals(csHql)) {
                    csHql += " and ";
                }
                csHql += " cs.status=" + contentCspStatus;
            }
            hql += " and c.id in(select cs.contentId from ContentCsp cs where " + csHql +
                    ") ";
            if(ContentCspLogicInterface.STATUS_ONLINE.equals(contentCspStatus)||
                ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED.equals(contentCspStatus)){
                hql+=" and c.status="+ContentLogicInterface.STATUS_CP_ONLINE;
            }
        }
        if (channelId!=null && channelId > 0) {
            hql += " and c.id not in(select cr.contentId from ContentRecommend cr where cr.channelId in("+channelId+"))";
            hql += " and c.id in(select cc.contentId from ContentChannel cc where cc.channelId in(";
            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
            if (allChilds != null) {
                hql += channelId;
                for (Object allChild : allChilds) {
                    Channel child = (Channel) allChild;
                    hql += "," + child.getId();
                }
            }
            hql += "))";
        }
        hql = hql.trim();
        if(hql.startsWith("and ")){
            hql = hql.substring(4);
        }
        if(!hql.equals("")){
            hql = " where "+hql;
        }else{
            hql = " where 1=1 ";
        }
        return "from Content c"+hql;
    }

    @SuppressWarnings("unchecked")
    private String getBaseHql(Long cspId, Long contentCspStatus, String channelIds,String systemChannelId) {
        String hql = "from Content c where 1=1";
        if ((cspId != null && cspId > 0) || (contentCspStatus != null && contentCspStatus > 0)) {
            String csHql = "";
            if (cspId != null && cspId > 0) {
                csHql = "cs.cspId=" + cspId;
            }
            if (contentCspStatus != null && contentCspStatus > 0) {
                if (!"".equals(csHql)) {
                    csHql += " and ";
                }
                csHql += " cs.status=" + contentCspStatus;
                if(ContentCspLogicInterface.STATUS_ONLINE.equals(contentCspStatus)||
                        ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED.equals(contentCspStatus)){
                    hql+=" and c.status="+ContentLogicInterface.STATUS_CP_ONLINE;
                }
            }
            hql += " and c.id in(select cs.contentId from ContentCsp cs where " + csHql +
                    ") ";
        }

        if(channelIds!=null&&!"".equals(channelIds.trim())){
            hql += " and c.id in(select cc.contentId from ContentChannel cc where cc.channelId in("+channelIds;
            String[] channelIdArray = channelIds.split(",");
            if(channelIdArray.length>0){
                TreeUtils tu = TreeUtils.getInstance();
                List allChilds;
                long channelId;
                for (String aChannelIdArray : channelIdArray) {
                    channelId = Long.parseLong(aChannelIdArray);
                    allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
                    if (allChilds != null) {
                        //hql += channelId;
                        for (Object allChild : allChilds) {
                            Channel child = (Channel) allChild;
                            hql += "," + child.getId();
                        }
                    }
                }
            }
            hql += "))";
        }else {
            if(systemChannelId!=null&&!"".equals(systemChannelId)){
                TreeUtils tu = TreeUtils.getInstance();
                String channelSearchCondition=systemChannelId;
                Long channelId = StringUtils.string2long(systemChannelId,-9876543);
                if(channelId!=-9876543){
                    List<Channel> allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
                    if (allChilds != null) {
                        if(!"".equals(channelSearchCondition)){
                            channelSearchCondition+=",";
                        }
                        channelSearchCondition += channelId;
                        for (Object allChild : allChilds) {
                            Channel child = (Channel) allChild;
                            if(!"".equals(channelSearchCondition)){
                                channelSearchCondition+=",";
                            }
                            channelSearchCondition += child.getId();
                        }
                    }
                }
                hql += " and c.id in(select cc.contentId from ContentChannel cc where cc.channelId in(";
                hql+=channelSearchCondition;
                hql += "))";
            }
        }

        return hql;
    }

    @SuppressWarnings("unchecked")
    public List<Content> recommendList(Long channelId){
        String hql="from Content c,ContentRecommend  cr where c.status=" +ContentLogicInterface.STATUS_CP_ONLINE+
                " and c.id in ( select cc.contentId from ContentCsp cc where cc.status="+
                ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED+
                ") and c.id =cr.contentId and cr.channelId ="+channelId+" order by cr.displayOrder";
        Session session =  getSession();
        try{
            Query query   =   session.createQuery(hql);
            query.setMaxResults(100);
            List<Object> result=query.list();
            List<Content> r = new ArrayList<Content>();
            for(Object o:result){
                if(o instanceof Object[]){
                    Object[] data = (Object[])o;
                    r.add((Content)data[0]);
                }
            }
            return r;
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName,
                              String directors, String actors,
                              List<ContentProperty> searchValues, PageBean pageBean) {
        return list(cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues, pageBean, null, -1);
    }

    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors, String actors, List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId) {
        return list(cspId, contentCspStatus, channelIds, contentName, directors, actors, searchValues, pageBean, systemChannelId, null, -1);
    }

    /**
     *
     * @param contentCspStatus
     * @param contentName
     * @param directors
     * @param actors
     * @param searchValues
     * @param pageBean
     * @return
     */
    public List<Content> list(Long contentCspStatus, String contentName, String directors, String actors, List<ContentProperty> searchValues, PageBean pageBean) {
        return list(-1L,contentCspStatus,"",contentName,directors,actors,searchValues,pageBean,null);
    }

    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean) {
        return list(cspId, contentCspStatus, channelId, searchValue, pageBean, null , -1);
    }

    public List<Content> list(Long contentCspStatus, String searchValue, PageBean pageBean) {
        // 调用脚下的
        return list(contentCspStatus, searchValue, pageBean, null, -1);
    }

    /**
     * 头顶上那个list增加了栏目过滤和用户类型过滤
     * @param contentCspStatus  状态
     * @param searchValue       查询
     * @param pageBean          分页
     * @param channelIdList     栏目过滤
     * @param userType          用户类型
     * @return content列表
     */
    public List<Content> list(Long contentCspStatus,String searchValue,PageBean pageBean, List<Long> channelIdList, long userType){
        return list(-1L,contentCspStatus,null,searchValue,pageBean,channelIdList,userType);
    }

    public String appendUserTypeSearchCondition(List<Long> channelIdList, long userType, String hql){
        if(channelIdList != null && channelIdList.size() > 0){
            String ids = "";
            for(Long id : channelIdList){
                ids += "".equals(ids)? id : ","+id;
            }
            hql += " and c.id in ( select cc.contentId from ContentChannel cc where cc.channelId in (" + ids + "))";
        }
        if( userType > 0){
            hql += " and (c.userTypes like '%," + userType + ",%')";
        }
        return hql;
    }
    /**
     * 增加了频道和用户类型的查询
     * @param cspId csp的ID
     * @param contentCspStatus 发布状态，一般都是ONLINE_PUBLISHED,2
     * @param channelIds 频道ID
     * @param contentName 影片名
     * @param directors 导演
     * @param actors 主演
     * @param searchValues 复合搜索的列表
     * @param pageBean 分页信息
     * @param channelIdList 频道列表
     * @param userType 用户类型
     * @return 搜索结果
     */
    public List<Content> list(Long cspId,Long contentCspStatus,String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId,
                              List<Long> channelIdList, long userType,String excludeIds){

        String hql = getBaseHql(cspId, contentCspStatus, channelIds,systemChannelId);
        List<Object> parameters = new ArrayList<Object>();
        if (contentName != null&&!"".equals(contentName.trim())) {
            hql += " and c.name like ? ";
            parameters.add("%" + contentName.trim() + "%");
        }
        if (actors != null&&!"".equals(actors.trim())) {
            hql += " and c.actors like ?";
            parameters.add("%" + actors + "%");
        }
        if (directors != null&&!"".equals(directors.trim())) {
            hql += " and c.directors like ?";
            parameters.add("%" + directors + "%");
        }
        if(excludeIds!=null&&!"".equals(excludeIds.trim())){
            hql+= " and c.id not in("+excludeIds+")";
        }
        if (searchValues != null && searchValues.size() > 0) {
            //String propertySearchHql = "";
            for (ContentProperty cp : searchValues) {
                Long propertyId = cp.getPropertyId();
                if (propertyId != null) {
                    Long isMain = cp.getExtraInt();
                    boolean inContentTable = isMain!=null&&isMain.intValue()==1;
                    //搜索条件中可以加入竖线，来形成一个多条件搜索，例如MEDIA_TIME中可以搜索2009%|2010%|2011%
                    String stringValue = cp.getStringValue();
                    if(stringValue!=null&&!"".equals(stringValue)){
                        String searchHql = "";
                        String[] values = stringValue.split("\\|");
                        for(String v:values){
                            if(!"".equals(searchHql)){
                                searchHql +=" or ";
                            }
                            if(inContentTable){
                                searchHql+=" c."+cp.getExtraData()+" like ?";
                                parameters.add("%"+v+"%");
                                //20150621,xjliu添加了模糊条件，需要注意的是，目前只支持字符串搜索
                            }else{
                                searchHql+=" c.id in(select cp.contentId from ContentProperty cp where " +
                                        " cp.propertyId=" + propertyId + " and cp.stringValue like ? )";
                                parameters.add("%"+v+"%");
                            }
                        }

                        if(!"".equals(searchHql)){
                            hql+=" and ("+searchHql+" )";
                        }
                    }
                }
            }
        }
        hql = appendUserTypeSearchCondition(channelIdList, userType, hql);
        try {
            return getObjects(hql, parameters.toArray(), pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }

    }
    public List<Content> list(Long cspId,Long contentCspStatus,String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId,
                              List<Long> channelIdList, long userType){
        return list(cspId,contentCspStatus,channelIds,contentName,directors,actors,searchValues,pageBean,systemChannelId,channelIdList,userType,null);
    }

    /**
     * added by mlwang 增加了频道过滤和用户类型过滤
     * @param cspId
     * @param contentCspStatus
     * @param channelId
     * @param searchValue
     * @param pageBean
     * @param channelIdList
     * @param userType
     * @return
     */
    public List<Content> list(Long cspId,Long contentCspStatus,Long channelId,String searchValue,PageBean pageBean, List<Long> channelIdList, long userType){
        String hql = getBaseHql(cspId, contentCspStatus, channelId);
        List<String> parameters = new ArrayList<String>();
        if(searchValue!=null&&!"".equals(searchValue)){
            searchValue = "%" + searchValue + "%";
            hql += " and ( c.name like ? ";
            parameters.add(searchValue);
            hql += " or c.actors like ?";
            parameters.add(searchValue);
            hql += " or c.directors like ?";
            parameters.add(searchValue);
            hql += ")";
        }
        hql = appendUserTypeSearchCondition(channelIdList, userType, hql);
        try {
            return getObjects(hql, parameters.toArray(), pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    /**
     * added by mlwang ， 增加了频道过滤和用户类型过滤
     * @param cspId csp的ID
     * @param contentCspStatus 发布状态，一般都是ONLINE_PUBLISHED,2
     * @param channelId 频道ID
     * @param contentName 影片名
     * @param directors 导演
     * @param actors 主演
     * @param searchValues 复合搜索的列表
     * @param pageBean 分页信息
     * @param channelIdList 频道列表
     * @param userType 用户类型
     * @param excludeIds 需要剔除的内容
     * @return 搜索结果列表
     */
    public List<Content> list(Long cspId,Long contentCspStatus,Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, long userType,String excludeIds){
        return list(cspId,contentCspStatus,""+channelId,contentName,directors,actors,searchValues,pageBean,null,channelIdList,userType,excludeIds);
    }
    public List<Content> list(Long cspId,Long contentCspStatus,Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, long userType){
        return list(cspId,contentCspStatus,""+channelId,contentName,directors,actors,searchValues,pageBean,null,channelIdList,userType);
    }


    public Long getContentIdBySubContentId(String subContentId, Long propertyId) {
        String hql = "select cp.contentId from ContentProperty cp where cp.propertyId = " + propertyId + " and cp.stringValue like ?";
        List tempResult = this.getHibernateTemplate().find(hql, new Object[]{subContentId});
        if (tempResult != null && tempResult.size() > 0) {
            return (Long) tempResult.get(0);
        }
        return null;
    }

    public Long getContentIdByContentChannelId(Long contentChannelId) {
        String hql = "select cc.contentId from ContentChannel cc where cc.id = " + contentChannelId;
        List tempResult = this.getHibernateTemplate().find(hql);
        if (tempResult != null && tempResult.size() > 0) {
            return (Long) tempResult.get(0);
        }
        return null;
    }

    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean) {
        return list(cspId,contentCspStatus,channelId,null,pageBean);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list2(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean) {
        Session session = getSession();
        try{
            String sql ="select id,name from CONTENT c where 1=1 and c.ID " +
                    "in(select cs.CONTENT_ID from CONTENT_CSP cs where  cs.STATUS=" + contentCspStatus;
            if(cspId > 1){
                sql += " and cs.CSP_ID=" + cspId;
            }
            sql += ")  and c.ID in ( select cc.CONTENT_ID from CONTENT_CHANNEL cc where cc.CHANNEL_ID=" + channelId + ")";
//            sql += " and rownum <= "+pageBean.getPageSize();
            Query query = session.createSQLQuery(sql);
            query.setFirstResult(0);
            query.setMaxResults(pageBean.getPageSize());
            return query.list();
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }


    public boolean existContentId(String contentId,Long cspId) {
        boolean isExist = false;
        List<Content> contents = getContent(contentId, cspId);
        if(contents!= null&&contents.size()>0){
           isExist = true;
        }
        return isExist;
    }

    @SuppressWarnings("unchecked")
    public List<Content> getContent(String contentId, Long cspId) {
        return getContent(null,contentId,cspId);
    }

    @SuppressWarnings("unchecked")
    public List<Content> getContent(String name,String contentId,Long cspId){

        try{

            String hql = "from Content c where 1=1";
            if(contentId!=null&&!"".equals(contentId.trim())){
                hql += " and c.contentId = '"+contentId+"'";
            }
            if(name!=null&&!"".equals(name.trim())){
                hql += " and c.name = '"+name+"'";
            }

            if(cspId!=null&&cspId>0){
                hql += " and c.cspId="+cspId;
            }
            hql += " order by c.createTime desc";
            return this.getHibernateTemplate().find(hql);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return new ArrayList<Content>();
    }
    public void updateContentByContentId(Content content) {
        List<Content> contents = getContent(content.getContentId(), null);
        for(Content c:contents){
            content.setId(c.getId());
            this.update(content);
        }
    }
    public String getContentChannel(Long contentId){
        return "from ContentChannel cc where cc.contentId="+contentId;
    }
    public String getContentRecommend(Long contentId){
        return "from ContentRecommend cr where cr.contentId=" + contentId;
    }
    public String getContentServiceProduct(Long contentId){
        return "from ContentServiceProduct csp where csp.contentId=" + contentId;

    }
    public void removeContentByContentId(Content content) {
        List<Content> contents = getContent(content.getContentId(), null);
        for(Content c:contents){
            this.remove(c.getId());
        }
    }

    public int setContentStatus(Long id,Long status){
        String hql = "update Content c set c.status=" + status+
                " where c.id="+id;
        return executeUpdate(hql);
    }
    public List searchContentParameter(String name,long channelId,Long cspId,long contentCspStatus,PageBean pageBean) {
        List list = new ArrayList();
        String hql = "select c.name,cc.channelId,c.id,c.cspId,cl.name from Content c,ContentChannel cc,Channel cl,ContentCsp cp where c.id=cc.contentId and cc.channelId=cl.id and c.id=cp.contentId and c.status=2 and cp.cspId="+cspId;
        String hql_count = "select * from Content c,ContentChannel cc,Channel cl,ContentCsp cp where c.id=cc.contentId and cc.channelId=cl.id and c.id = cp.contentId and c.status=2 and cp.cspId="+cspId;

        if(contentCspStatus > 0) {
            hql += " and cp.status="+contentCspStatus;
            hql_count +=" and cp.status="+contentCspStatus;
        }

        if(name !=null && !name.trim().isEmpty()) {
            hql += " and c.name like '%"+name+"%'";
            hql_count += " and c.name like '%"+name+"%'";
        }

        if(channelId > 0) {
            hql += " and cc.channelId="+channelId;
            hql_count += " and cc.channelId="+channelId;
        }

        if (pageBean != null) {
            if (pageBean.getOrderBy() != null && !pageBean.getOrderBy().trim().isEmpty()) {
                hql += " order by " + pageBean.getOrderBy();
                if (pageBean.getOrderDir() != null && !pageBean.getOrderDir().trim().isEmpty()) {
                    hql += " " + pageBean.getOrderDir();
                }
            }
        }

        Session session = getSession();
        Query query_count = session.createQuery(hql_count);
        int allCount = new Integer(query_count.list().get(0).toString());
        if(pageBean!=null) pageBean.setRowCount(allCount);

        try {
            Query query = session.createQuery(hql);
            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }

            list = query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            session.close();
        }

        return list;
    }


    public List getContentsOfVipChannels(long channelId) {
        List list = null;
        Session session = getSession();
        try {
            list = new ArrayList<Content>();
            String hqlStr = "select * from Content ct where ct.id in " +
                    "(select cc.content_id from content_csp cc where cc.content_id in " +
                    "(select cc.CONTENT_ID from content_channel cc where cc.CHANNEL_ID in " +
                    "(select c.id from channel c start with  c.id ="+channelId+" connect by prior c.id = c.PARENT_ID)) and cc.status = 2)";
            Query query = session.createSQLQuery(hqlStr);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            session.close();
        }
        return  list;
    }

    @SuppressWarnings("unchecked")
    public List<Content> getContentsOfExpired(Long cspId) {
        String hql = "from Content c where c.validEndTime<? and c.status="+ ContentLogicInterface.STATUS_CP_ONLINE;
        if(cspId!=null&& cspId>0){
            hql += " and c.cspId = "+cspId;
        }
        return this.getHibernateTemplate().find(hql,new Object[]{new Date()});
    }

    @SuppressWarnings("unchecked")
    public List<Content> getContentsByCspId(Long cspId) {
        String hql = "from Content where id in (select c.id from Content c,ContentCsp cc where  c.id = cc.contentId and cc.cspId ="+cspId+" and cc.status = 2)";  //2Ϊsp����״̬
        return  this.getHibernateTemplate().find(hql);
    }

    public List<Content> getContentsByCspId(Long cspId,PageBean pageBean) {
        String hql = "from Content where (select c.id from Content c,ContentCsp cc where  c.id = cc.contentId and cc.cspId ="+cspId+" and cc.status = 2)";
        try {
            return getObjects(hql,pageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getCountByCspId(Long cspId) {
        String hql = "select * from Content ct where ct.id in (select c.id from Content c,ContentCsp cc where c.id = cc.contentId and cc.cspId ="+cspId+" and cc.status = 2)";
        List list = this.getHibernateTemplate().find(hql);

        return (list != null && list.size() > 0 && list.get(0) !=null)?Long.valueOf(list.get(0).toString()):0;
    }


    public List<Content> getContentsBySpecial(PageBean pageBean){
        String hql = "from Content where isSpecial=1 and id in (select c.id from Content c,ContentProperty cp where  c.id = cp.contentId and c.status = 2) and post1Url is not null and post2Url is not null";
        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    public List<Content> getContentsOfChannelAndCp(long channelId, Long cspId, String name, String actors,
                                                   String directors, Long status, int publishStatus,
                                                   PageBean pageBean) {
        String hql = "from Content c where 1=1";
        if(channelId>0){
            hql+=" and c.id in (select cc.contentId from ContentChannel cc where cc.channelId="+channelId+")";
        }
        if(status!=null&&status>0){
            hql += " and c.status="+status;
        }
        if(publishStatus>0){
            hql+=" and c.id in (select cs.contentId from ContentCsp cs where cs.status="+publishStatus+")";
        }
        if(cspId>0){
            hql += " and c.cspId="+cspId;
        }
        List<Object> parameters = new ArrayList<Object>();
        if (name != null&&!"".equals(name.trim())) {
            hql += " and c.name like ? ";
            parameters.add("%" + name.trim() + "%");
        }
        if (actors != null&&!"".equals(actors.trim())) {
            hql += " and c.actors like ?";
            parameters.add("%" + actors + "%");
        }
        if (directors != null&&!"".equals(directors.trim())) {
            hql += " and c.directors like ?";
            parameters.add("%" + directors + "%");
        }
        try {
            return getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return new ArrayList<Content>(0);
    }

    public void remove(Content content){
        if(content==null)return;
        logger.debug(removeRelatedData(new String[]{
                //"AD_LOG",
                "CONTENT_AUDIT",
                "CONTENT_CHANNEL",
                "CONTENT_PROPERTY",
                "CONTENT_RECOMMEND",
                "CONTENT_RELATED",
                "RELATED_PROPERTY_CONTENT",
                //"USER_BUY",
                "USER_FAVORITES",
                "USER_RECOMMAND",
                "USER_REVIEW",
                "USER_SCORING",
                //"VISIT_DAY_CONTENT_LOG",
                //"VISIT_LOG",
                //"WEB_VISIT_LOG",
                //"CONTENT_ZT_LOG",
                "CONTENT_CSP",
                "CONTENT_SERVICE_PRODUCT",
                "FRD_BOOKMARK",
                "FRD_RECOMMEND"},content.getName(), "CONTENT_ID", content.getId()));
        super.remove(content);
    }

    public void remove(long id){
        if(id<=0){
            return;
        }
        Content content = get(id);
        if(content!=null){
            remove(content);
        }
    }

    public List<Content> getContents(Long cspId,Long contentCspStatus,Long channelId,String searchValue,PageBean pageBean,String code,String code_1) {
        //Use directors field as content template status
        String hql = "from Content c where c.directors in  (select ps.id from PropertySelect ps where (ps.code = ?";
        List<String> parameters = new ArrayList<String>();
        if(code_1 != null && !code_1.trim().isEmpty()) {
            hql += " or ps.code = ?";
            parameters.add(code_1);
        }
        hql += "))";

        if ((cspId != null && cspId > 0) || (contentCspStatus != null && contentCspStatus > 0)) {
            String csHql = "";
            if (cspId != null && cspId > 0) {
                csHql = "cs.cspId=" + cspId;
            }
            if (contentCspStatus != null && contentCspStatus > 0) {
                if (!"".equals(csHql)) {
                    csHql += " and ";
                }
                csHql += " cs.status=" + contentCspStatus;
            }
            hql += " and c.id in(select cs.contentId from ContentCsp cs where " + csHql +
                    ") ";
        }


        if (channelId > 0) {
            hql += " and c.id not in(select cr.contentId from ContentRecommend cr where cr.channelId in("+channelId+"))";
            hql += " and c.id in(select cc.contentId from ContentChannel cc where cc.channelId in(";
            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
            if (allChilds != null) {
                hql += channelId;
                for (Object allChild : allChilds) {
                    Channel child = (Channel) allChild;
                    hql += "," + child.getId();
                }
            }
            hql += "))";
        }


        parameters.add(code);
        if(searchValue!=null&&!"".equals(searchValue)){
            searchValue = "%" + searchValue + "%";
            hql += " and ( c.name like ? ";
            parameters.add(searchValue);
            hql += ")";
        }
        try {
            return getObjects(hql, parameters.toArray(), pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    public int incAllVisitCount(long contentId) {
        return executeSQLUpdate("update CONTENT set ALL_VISIT_COUNT=ALL_VISIT_COUNT+1 where ID="+contentId);
    }


    public List<Content> list(Long cspId,Long contentCspStatus,Long channelId,String searchValue,PageBean pageBean,Long type) {
        String hql = getBaseHql(cspId, contentCspStatus, channelId);
        List<String> parameters = new ArrayList<String>();
        if(searchValue!=null&&!"".equals(searchValue)){
            searchValue = "%" + searchValue + "%";
            hql += " and ( c.name like ? ";
            parameters.add(searchValue);
            hql += ")";
        }

        if(type == 1) { //in living , for the reason of content template, directors is the status of living , 0 not start ,1 started
            hql += " and (directors = 0 or directors = 1)";
        }

        if(type == 2){ //for live look back
            hql += " and directors = 2";
        }


        try {
            return getObjects(hql, parameters.toArray(), pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    public List<Content> getContentByName(String homeClubName,String guestClubName,long channelId,PageBean pageBean,long cspId) {
         String hql = "from Content c where c.id in(select cs.contentId from ContentCsp cs where cs.cspId="+cspId+" and  cs.status=2) and c.status=2" +
                 " and c.id not in(select cr.contentId from ContentRecommend cr where cr.channelId in("+channelId+")) " +
                 "and c.id in(select cc.contentId from ContentChannel cc where cc.channelId in("+channelId+")) " +
                 "and (c.name like '%"+homeClubName+"%' or c.name like '%"+guestClubName+"%') order by createTime desc";


        try {
            //logger.debug("will list data for hql="+hql);
            return getObjects(hql,pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }


     /*   Session session = getSession();
        Query query = session.createQuery(hql);
        query.setFirstResult(start);
        query.setMaxResults(number);
        return query.list();*/
    }


    public List<Content> getUnAuditContents(Long channelId, String searchValue,PageBean pageBean){
        String hql = "from Content c where c.status=" + ContentLogicInterface.STATUS_WAITING_FOR_AUDIT;
        if(channelId > 0){
            hql += " and c.id in (select cc.contentId from ContentChannel cc where cc.channelId in(" + channelId;

            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
            if (allChilds != null) {
                for (Object allChild : allChilds) {
                    Channel child = (Channel) allChild;
                    hql += "," + child.getId();
                }
            }
            hql += "))";
        }

        if(searchValue!=null&&!"".equals(searchValue)){
            hql += " and ( c.name like '%" + searchValue + "%')";
        }


        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    /**
     * 查询视频
     *
     * @param channelId 频道
     * @param searchValue 查询条件
     * @param pageBean  分页信息
     * @param adminId   发起查询的管理员Id，如果<0，忽略该条件
     * @return
     */
    public List<Content> getContents(Long channelId, String searchValue,Date startTime,Date stopTime, PageBean pageBean, Integer adminId){
        String hql = "from Content c where (c.status <>" + ContentLogicInterface.STATUS_RECYCLE + " and c.status <> " +
                     ContentLogicInterface.STATUS_DELETE +")";
        List<Object> args = new ArrayList<Object>();
        if(channelId > 0){
            hql += " and c.id in (select cc.contentId from ContentChannel cc where cc.channelId in(" + channelId;

            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
            if (allChilds != null) {
                for (Object allChild : allChilds) {
                    Channel child = (Channel) allChild;
                    hql += "," + child.getId();
                }
            }
            hql += "))";
        }

        if(searchValue!=null&&!"".equals(searchValue)){
            hql += " and ( c.name like '%" + searchValue + "%')";
        }
        if(startTime!=null){
            hql +=" and c.createTime > ?";
            args.add(startTime);
        }
        if(stopTime!=null){
            hql +=" and c.createTime <= ?";
            args.add(stopTime);
        }
        if(adminId > 0){
            hql += " and c.id in (select cc.contentId from " +
                    "ContentChannel cc where cc.channelId in(" +
                    "select ac.channelId from AdminChannel ac" +
                    " where ac.adminId=" + adminId + "))";
        }

        try {
            return getObjects(hql,args.toArray(),pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

    /**
     * 限定了搜索范围的top10
     * @param channelIdList
     * @param userType
     * @return
     */
    public List<Content> top10( List<Long> channelIdList, long userType){
/*
        String hql = "from Content c where (c.status <>" + ContentLogicInterface.STATUS_RECYCLE + " and c.status <> " +
                     ContentLogicInterface.STATUS_DELETE +") ";
*/
        String hql = "from Content c where (c.status =" + ContentLogicInterface.STATUS_CP_ONLINE + ") ";

        hql = appendUserTypeSearchCondition(channelIdList, userType, hql);
        try {
            return getObjects(hql, new PageBean(0,10,"c.allVisitCount","desc"));
        } catch (Exception e) {
            return new ArrayList<Content>();
        }

    }

    /**
     * 获取管理员发布的转码失败任务
     * @param adminId 管理员Id
     * @return 各种字段列表
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getTransAbnormalList(Integer adminId){
        // select a.id as task_id, a.clip_id, a.status,c.id as content_id, c.name from encoder_task a,
        // content_property b, content c where a.clip_id=b.id and b.content_id=c.id and c.creator_admin_id=3
        String hql = "select a.id, a.clipId, a.status, c.id, c.name from EncoderTask a, ContentProperty b, Content c" +
                " where a.clipId=b.id and b.contentId=c.id and c.creatorAdminId=" + adminId +
                " and a.status <>" + EncoderTaskLogicInterface.STATUS_RUNNING +
                " and a.status <>" + EncoderTaskLogicInterface.STATUS_WAITING +
                " and a.status <>" + EncoderTaskLogicInterface.STATUS_FINISHED +
                " and c.status <>" + ContentLogicInterface.STATUS_DELETE +
                " and c.status <>" + ContentLogicInterface.STATUS_RECYCLE;
        return this.getHibernateTemplate().find(hql);
    }

    /**
     * 查询指定管理员发布的审核未通过的内容
     * @param adminId 管理员Id
     * @return Content对象列表
     */
    @SuppressWarnings("unchecked")
    public List<Content> getRejectContents(Integer adminId){
        String hql = "from Content c where c.status=" + ContentLogicInterface.STATUS_AUDIT_REJECTED +
                " and c.creatorAdminId=" + adminId;
        return this.getHibernateTemplate().find(hql);
    }

    /**
     * 为频道影片增加用户类型
     * @param userTypeId 要增加的用户类型
     * @param channels   栏目Id，用逗号分隔
     */
    public void addUserType(long userTypeId, String channels){
        String hql = "update Content c set c.userTypes=concat(c.userTypes," + userTypeId + ",',') where " +
                "c.id in (select contentId from ContentChannel cc where cc.channelId in (" + channels + ")) " +
                "and c.userTypes not like '%," + userTypeId + ",%'";
        executeUpdate(hql);
    }

    /**
     * 查询最新视频
     * @param channelIds    栏目ID列表
     * @param userType      用户类型
     * @param count         取得条数
     * @return  content列表
     */
    public List<Content> getRedexNewest(String channelIds,Long userType, int count){
        String condition = " where c.status=" +
                ContentLogicInterface.STATUS_CP_ONLINE ;
        if(userType > 0){
            condition += " and c.userTypes like '%," + userType + ",%'";
        }
        if( channelIds != null && channelIds.length() > 0){
            condition += " and c.id in ( select cc.contentId from ContentChannel cc " +
                    "where cc.channelId in (" + channelIds + "))";
        }
        Session session = getSession();

        PageBean pageBean = new PageBean();
        pageBean.setPageNo(1);
        pageBean.setPageSize(count);
        String hql = "from Content c" + condition +
                " order by createTime desc";
        Query recordQuery = session.createQuery(hql);
        recordQuery.setFirstResult(pageBean.getStartRow());
        recordQuery.setMaxResults(pageBean.getPageSize());
        List l = recordQuery.list();

        if( session.isOpen() ){
            session.close();
        }

        return l;
    }

    /**
     * redex标准版前台查询浏览栏目内容
     * @param channelId     栏目Id
     * @param searchWord    查询条件
     * @param channelIdString 用户可以观看的栏目序列
     * @param userType       用户类型
     * @param pageBean       分页信息
     * @return                Content字段列表
     */
    public List<Object[]> getRedexContents(Long channelId, String searchWord, String channelIdString, Long userType, PageBean pageBean){
        return getRedexContents(channelId, searchWord, channelIdString, userType, pageBean, -1L);
    }

    /**
     * redex标准版前台查询浏览栏目内容
     * @param channelId     栏目Id
     * @param searchWord    查询条件
     * @param channelIdString 用户可以观看的栏目序列
     * @param userType       用户类型
     * @param pageBean       分页信息
     * @param type            内容类型
     * @return                Content字段列表
     */
    public List<Object[]> getRedexContents(Long channelId, String searchWord, String channelIdString, Long userType, PageBean pageBean, long type){
        // fuck hibernate
        // 1 union不支持
        // 2 left join只能在类的定义中使用了类的关联many to one等定义才可以用
        // so 使用sql语句进行查询
        // 先查询推荐的视频，确保推荐的排在前面
        String condition = "c.status=" + ContentLogicInterface.STATUS_CP_ONLINE;
        if( channelId > 0 ){
            condition += " and c.id in (select cc.content_id from content_channel cc where cc.channel_id in(" +
                    channelId ;
            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Channel.class, channelId, 0);
            if (allChilds != null) {
                for (Object allChild : allChilds) {
                    Channel child = (Channel) allChild;
                    condition += "," + child.getId();
                }
            }
            condition += "))";
        }
        if( searchWord != null && !searchWord.isEmpty()){
            condition += " and (c.name like '%" + searchWord + "%' or c.actors like '%" + searchWord +
                    "%' or c.intro like '%" + searchWord + "%')";
        }
        if( channelIdString != null && channelIdString.length() > 0){
            condition += " and c.id in ( select cc.content_id from content_channel cc " +
                    "where cc.channel_id in (" + channelIdString + "))";
        }
        if(userType > 0){
            condition += " and c.user_types like '%," + userType + ",%'";
        }
        // 类型
        if(type > 0){
            condition += " and c.type=" + type;
        }


        String sql = "select c.id, c.name, c.post1_url,c.actors,c.create_time,cr.display_order as _order from " +
                "content c left join content_recommend cr on c.id=cr.content_id where " + condition + " order by -_order desc";
        if(pageBean != null){
            if( "time".equalsIgnoreCase(pageBean.getOrderBy())){
                sql += ",create_time desc";
            }else{
                sql += ",all_visit_count desc";
            }
        }

        if(pageBean != null){
            pageBean.setOrderBy(null);
        }

        logger.debug(sql);

        List l = null;
        try{
            l = sql(sql, null, pageBean);
        }catch (Exception e){
            e.printStackTrace();
        }

        return l;

    }

    public List<Content> getPicContent(long channelId,long modelId) {
        Session session = getSession();
        List<Content> list = new ArrayList<Content>();
        try {
            String hql = "from Content c where c.id not in (select cc.contentId from ContentChannel cc where cc.channelId ="+channelId+") and c.moduleId="+modelId+" and c.status = 2 order by c.createTime desc";
            Query query = session.createQuery(hql);
            query.setFirstResult(0);
            query.setMaxResults(10);
            list = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            session.close();
        }
        return list;
    }

    public List<Content> getContentsByChannelId(long channelId,PageBean pageBean) {
        Session session = getSession();
        List<Content> list = new ArrayList<Content>();
        try {
            String countSql = "select count(*) from (select c.id from content c where c.id in (select cc.content_Id from Content_Channel cc where cc.channel_Id ="+channelId+") and c.status = 2) as count";
            Query countQuery = session.createSQLQuery(countSql);
            pageBean.setRowCount(new Integer(countQuery.list().get(0).toString()));

            String hql = "from Content c where c.id in (select cc.contentId from ContentChannel cc where cc.channelId ="+channelId+") and c.status = 2 order by c.createTime desc";
            Query query = session.createQuery(hql);
            query.setFirstResult(pageBean.getStartRow());
            query.setMaxResults(pageBean.getPageSize());
            list = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            session.close();
        }
        return list;
    }

    public List<Content> getContentsByStatus(long status,String searchValue,Date startTime,Date stopTime,PageBean pageBean) {
        String hql = "from Content c where c.status = 9";
        List<Object> args = new ArrayList<Object>();
        if(searchValue!=null&&!"".equals(searchValue)){
            hql += " and ( c.name like '%" + searchValue + "%')";
        }
        if(startTime!=null){
            hql +=" and c.createTime > ?";
            args.add(startTime);
        }
        if(stopTime!=null){
            hql +=" and c.createTime <= ?";
            args.add(stopTime);
        }
        try {
            return getObjects(hql,args.toArray(),pageBean);
        } catch (Exception e) {
            return new ArrayList<Content>();
        }
    }

}