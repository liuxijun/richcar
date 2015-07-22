package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.BookMark;
import com.fortune.rms.business.user.model.HistoryDTO;
import com.fortune.util.PageBean;
import com.opensymphony.module.sitemesh.Page;

import java.util.List;


public interface BookMarkLogicInterface extends BaseLogicInterface<BookMark> {
    public AllResp getAllBookMark(String userId, Integer userType, Integer bookMarkType);
    public AllResp addBookMark(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue);
    public AllResp deleteBookMark(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList);
    public BookMark saveBookmark(String userId, String contentId, String subContentId, String bookMarkValue);
    public int updateBookmark(long id, String userId, String contentId, String subContentId, String bookMarkValue);
    public List<Content> getBookMarkOfUser(String userTel, PageBean pageBean);
    public int getBookMarkCountOfUser(String userTel);
    public boolean removeBookMark(String userId, Long contentId);
    public List<HistoryDTO> redexGetHistoryList(String userId, PageBean pageBean);
    public BookMark getBookMarkOfUser(String userId, Long contentId);
}
