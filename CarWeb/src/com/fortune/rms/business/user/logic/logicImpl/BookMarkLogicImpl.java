package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.user.dao.daoInterface.BookMarkDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.BookMarkLogicInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.BookMark;
import com.fortune.rms.business.user.model.HistoryDTO;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("bookMarkLogicInterface")
public class BookMarkLogicImpl extends BaseLogicImpl<BookMark> implements BookMarkLogicInterface {

    private BookMarkDaoInterface bookMarkDaoInterface;
    private ContentLogicInterface contentLogicInterface;

    @Autowired
    public void setBookMarkDaoInterface(BookMarkDaoInterface bookMarkDaoInterface) {
        this.bookMarkDaoInterface = bookMarkDaoInterface;
        baseDaoInterface = (BaseDaoInterface) bookMarkDaoInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @SuppressWarnings("unchecked")
    public AllResp getAllBookMark(String userId, Integer userType, Integer bookMarkType) {
        if (ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)) {
            return this.bookMarkDaoInterface.getAllBookMarkFromDb(userId, userType, bookMarkType);
        } else {
            return this.bookMarkDaoInterface.getAllBookMarkFromHttp(userId, userType, bookMarkType);
        }

    }

    public AllResp addBookMark(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue) {
        if (ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)) {
            return this.bookMarkDaoInterface.addBookMarkFromDb(userId, userType, contentId, subContentId, subContentName, subContentType, serviceType, bookMarkType, bookMarkValue);
        } else {
            return this.bookMarkDaoInterface.addBookMarkFromHttp(userId, userType, contentId, subContentId, subContentName, subContentType, serviceType, bookMarkType, bookMarkValue);
        }
    }


    public AllResp deleteBookMark(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList) {
        if (ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)) {
            return this.bookMarkDaoInterface.deleteBookMarkFromDb(userId, userType, operationType, bookMarkType, bookMarkIdList);
        } else {
            return this.bookMarkDaoInterface.deleteBookMarkFromHttp(userId, userType, operationType, bookMarkType, bookMarkIdList);
        }
    }
    public int getBookMarkCountOfUser(String userTel){
      return bookMarkDaoInterface.getBookMarkCountOfUserFromDb(userTel);
    }
    public List<Content> getBookMarkOfUser(String userTel, PageBean pageBean) {
        BookMark bean = new BookMark();
        bean.setUserId(userTel);
        List<BookMark> bookMarks = search(bean,pageBean);
        List<Content> contents = new ArrayList<Content>();
        for (BookMark bookMark : bookMarks) {
            String contentId = bookMark.getContentId();
            String markTime=bookMark.getBookMarkValue();
            Content content = contentLogicInterface.getCachedContent(Long.parseLong(contentId));
            if (content != null) {
                contents.add(content);
                content.setBookMark(bookMark);
            }
        }
        return contents;
    }
    public List<BookMark> getBookMarkTime(String userTel){
        List<BookMark> bookMarks=new ArrayList<BookMark>();
        BookMark bookMark=new BookMark();
        bookMark.setUserId(userTel);
        bookMarks= search(bookMark);
        return  bookMarks;
    }

    public BookMark getBookMark(String userId, Long contentId) {
        BookMark bean = new BookMark();
        bean.setUserId(userId);
        bean.setContentId(contentId + "");
        List<BookMark> bookMark = search(bean);
        if (bookMark != null && bookMark.size() > 0) {
            return bookMark.get(0);
        }
        return null;
    }

    //取消收藏
    public boolean removeBookMark(String userId, Long contentId) {
        boolean flog = false;
        BookMark bean = getBookMark(userId, contentId);
        if (bean != null) {
            remove(bean);
            flog = true;
        } else {
            logger.warn("没有找到用(" + userId + ")对应的播放记录（" + contentId + "）");
        }
        return flog;
    }

    public int updateBookmark(long id,String userId, String contentId, String subContentId, String bookMarkValue) {
        if (ConfigManager.getInstance().getConfig("threeScreen.interface.fromDb", false)) {
            return this.bookMarkDaoInterface.updateBookmarkToDb(id, userId, contentId, subContentId, bookMarkValue);
        } else {
            return this.bookMarkDaoInterface.updateBookmarkToHttp(id, userId, contentId, subContentId, bookMarkValue);
        }
    }

    public BookMark saveBookmark(String userId, String contentId, String subContentId, String bookMarkValue) {
        if(subContentId != null && contentId != null && userId != null) {
            BookMark bookMark = new BookMark();
            bookMark.setUserId(userId);
            bookMark.setContentId(contentId);
            List<BookMark> bookMarks = search(bookMark, false);
            int count = bookMarks.size();
            if (count > 0) {
                bookMark = bookMarks.get(0);
                if (count >= 1) {
                    int i = 0;
                    while (i < count) {
                        if(i==0){
                            bookMark = bookMarks.get(0);
                        }else{
                            remove(bookMarks.get(i));
                        }
                        i++;
                    }
                }
            }
            String markValue=bookMarkValue;
            if(bookMarkValue.indexOf(".")>0){
                markValue = bookMarkValue.substring(0, bookMarkValue.lastIndexOf("."));
            }
            bookMark.setBookMarkValue(markValue);
            bookMark.setSubContentId(subContentId);

            //if(bookMark.getCreateTime() == null) {
            bookMark.setCreateTime(new Date());
            //}

            return save(bookMark);
            //logger.debug("保存成功！");
        }else{
            logger.warn("保存失败：userId="+userId+",contentId="+contentId+",subContentId="+subContentId);
        }
        return null;
    }

    /**
     * 查询用户的观看历史
     * @param userId        用户Id
     * @param pageBean      分页信息
     * @return               HistoryDTO列表
     */
    public List<HistoryDTO> redexGetHistoryList(String userId, PageBean pageBean){
        if(userId == null  || userId.isEmpty()) return new ArrayList<HistoryDTO>();

        BookMark bean = new BookMark();
        bean.setUserId(userId);
        if(pageBean != null){
            pageBean.setOrderBy("o1.createTime");
            pageBean.setOrderDir("desc");
        }
        List<BookMark> bookMarks = search(bean,pageBean);
        List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
        for (BookMark bookMark : bookMarks) {
            HistoryDTO dto = new HistoryDTO(bookMark);
            Content content = contentLogicInterface.getCachedContent(Long.parseLong(bookMark.getContentId()));
            if (content != null) {
                dto.setContentTitle(content.getName());
                dto.setContentPoster(content.getPost1Url());
            }
            historyList.add(dto);
        }
        return historyList;
    }
    public BookMark getBookMarkOfUser(String userId,Long contentId){
        if(userId!=null&&contentId!=null&&!"".equals(userId)&&contentId>0){
            BookMark bookMark = new BookMark();
            bookMark.setContentId(""+contentId);
            bookMark.setUserId(userId);
            List<BookMark> results = search(bookMark,false);
            if(results!=null&&results.size()>0){
                return results.get(0);
            }
        }
        return null;
    }
}

    