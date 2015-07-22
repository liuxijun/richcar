package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.BookMark;

import java.util.List;


public interface BookMarkDaoInterface extends BaseDaoInterface<BookMark,Long> {
    public List<BookMark> getBookMarkOfUserId(String userId);
    public int getBookMarkCountOfUserFromDb(String userTel);
    public BookMark getBookMarkOfUser(String userId, Long contentId);
    public AllResp getAllBookMarkFromDb(String userId, Integer userType, Integer bookMarkType);
    public AllResp addBookMarkFromDb(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue);
    public int updateBookmarkToDb(long id, String userId, String contentId, String subContentId, String bookMarkValue);
    public int updateBookmarkToHttp(long id, String userId, String contentId, String subContentId, String bookMarkValue);
    public AllResp deleteBookMarkFromDb(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList);
    public AllResp getAllBookMarkFromHttp(String userId, Integer userType, Integer bookMarkType);
    public AllResp addBookMarkFromHttp(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer bookMarkType, String bookMarkValue);
    public AllResp deleteBookMarkFromHttp(String userId, Integer userType, Integer operationType, Integer bookMarkType, String bookMarkIdList);
 }