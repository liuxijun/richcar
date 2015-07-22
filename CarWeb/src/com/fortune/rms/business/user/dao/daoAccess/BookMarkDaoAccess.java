package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.user.dao.daoInterface.BookMarkDaoInterface;
import com.fortune.rms.business.user.logic.logicImpl.XmlHelper;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.BookMark;
import com.fortune.threeScreen.serverMessage.ServerMessager;
import com.fortune.util.XmlUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-19
 * Time: 上午9:36
 */
@Repository
public class BookMarkDaoAccess extends BaseDaoAccess<BookMark, Long> implements BookMarkDaoInterface {

    public BookMarkDaoAccess() {
        super(BookMark.class);
    }

    @SuppressWarnings("unchecked")
    public AllResp getAllBookMarkFromDb(String userId, Integer userType, Integer bookMarkType) {
        AllResp allResp = new AllResp();
        try {
            String hql = "from BookMark bm where bm.userId ='" + userId + "' and bm.userType = " + userType + " and bm.bookMarkType = " + bookMarkType + "";
            List<BookMark> bookMarks = this.getHibernateTemplate().find(hql);
            allResp.setList(bookMarks);
            allResp.setResultCode(1);
            allResp.setReason("读取数据成功");

        } catch (Exception e) {
            logger.error(e.getMessage());
            allResp.setResultCode(2);
            allResp.setReason("读取数据失败");
        }
        return allResp;

    }
    @SuppressWarnings("unchecked")
    public AllResp addBookMarkFromDb(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue) {
        AllResp allResp = new AllResp();
        BookMark bookMark = new BookMark();
        bookMark.setUserId(userId);
        bookMark.setUserType(userType);
        bookMark.setContentId(contentId);
        bookMark.setSubContentId(subContentId);
        bookMark.setServiceType(subContentType);
        bookMark.setSubContentName(subContentName);
        bookMark.setSubContentType(subContentType);
        bookMark.setBookMarkType(bookMarkType);
        bookMark.setBookMarkValue(bookMarkValue);
        Long bookMarkId;
        try {
            String hql = "from BookMark b where b.contentId = '" + contentId + "'";
            List<BookMark> bookMarks = this.getHibernateTemplate().find(hql);
            if (bookMarks != null && bookMarks.size() == 1) {
                bookMarkId = bookMarks.get(0).getId();
                bookMark.setId(bookMarkId);
                bookMark.setBookMarkId(String.valueOf(bookMarkId));
                this.getHibernateTemplate().update(bookMark);

            } else {
                bookMarkId = (Long) this.getHibernateTemplate().save(bookMark);
                bookMark.setBookMarkId(String.valueOf(bookMarkId));
                this.getHibernateTemplate().update(bookMark);
            }

            allResp.setResultCode(0);
            allResp.setReason("保存数签成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            allResp.setResultCode(2);
            allResp.setReason("数据保存失败");
        }

        return allResp;
    }

    public int updateBookmarkToDb(long id, String userId, String contentId, String subContentId, String bookMarkValue) {
        return executeUpdate("update frd_bookmark set user_id='"+userId+"',content_id='"+contentId+"'," +
                "subcontent_id='"+subContentId+"',bookmark_value='"+bookMarkValue+"' where id="+id);
    }

    public int updateBookmarkToHttp(long id, String userId, String contentId, String subContentId, String bookMarkValue) {
        return 0;
    }

    public AllResp deleteBookMarkFromDb(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList) {
        AllResp allResp = new AllResp();
        BookMark bookMark = new BookMark();
        bookMark.setUserId(userId);
        bookMark.setUserType(userType);
        bookMark.setId(Long.parseLong(bookMarkIdList));
        bookMark.setBookMarkType(bookMarkType);
        try {
            this.getHibernateTemplate().delete(bookMark);
            allResp.setResultCode(0);
            allResp.setReason("删除书签成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            allResp.setResultCode(2);
            allResp.setReason("删除书签失败");
        }
        return allResp;
    }

    @SuppressWarnings("unchecked")
    public AllResp getAllBookMarkFromHttp(String userId, Integer userType, Integer bookMarkType) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<GetAllBookMarkReq><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType><BookMarkType>" + bookMarkType + "</BookMarkType></GetAllBookMarkReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.bookMark.listUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_GetAllBookMarkReq.aspx");
        ServerMessager serverMessage = new ServerMessager();
        String resp = serverMessage.postToHost(urlStr, xml);
        logger.debug("书签列表返回=" + resp);
        Element root = XmlUtils.getRootFromXmlStr(resp);
        AllResp allBook = new XmlHelper().getResult(root);
        if (root != null) {
            List<Node> bookmarkNodeList = root.selectNodes("BookMarkList/BookMark");
            List<BookMark> bookmarks = new ArrayList<BookMark>();
            for (Node node : bookmarkNodeList) {
                bookmarks.add(new BookMark(node));
            }
            allBook.setList(bookmarks);
        }
        return allBook;

    }

    public AllResp addBookMarkFromHttp(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><AddBookMarkReq><SubContentName>" + subContentName + "</SubContentName><SubContentType>" + subContentType + "</SubContentType><ServiceType>" + serviceType + "</ServiceType><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType><SubContentID>" + subContentId + "</SubContentID><BookMarkType>" + bookMarkType + "</BookMarkType><ContentID>" + contentId + "</ContentID><BookMarkValue>" + bookMarkValue + "</BookMarkValue></AddBookMarkReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.bookMark.saveUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_AddBookMarkReq.aspx");
        return new XmlHelper().getSimpleResult(urlStr, xml);
    }

    public AllResp deleteBookMarkFromHttp(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<DeleteBookMarkReq><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType><BookMarkType>" + bookMarkType + "</BookMarkType><BookMarkIDList>" + bookMarkIdList + "</BookMarkIDList><OperationType>" + operationType + "</OperationType></DeleteBookMarkReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.bookMark.delUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_DeleteBookMarkReq.aspx");
        return new XmlHelper().getSimpleResult(urlStr, xml);
    }
    public List<BookMark> getBookMarkOfUserId(String userId){
        Session session = getSession();
        List<BookMark> bookMark=null;
        String hql="from BookMark where userId="+userId;
        Query query = session.createQuery(hql);
        query = setQueryParameter(query, null);
        bookMark = query.list();
        return bookMark;
    }

    public int getBookMarkCountOfUserFromDb(String userTel) {
        String hql = "select count(*)  from BookMark fbm where fbm.userId='" + userTel + "'";
        Query query = getSession().createQuery(hql);
        return ((Number) query.iterate().next()).intValue();
    }

    public BookMark getBookMarkOfUser(String userId, Long contentId) {
        return null;
    }
}
