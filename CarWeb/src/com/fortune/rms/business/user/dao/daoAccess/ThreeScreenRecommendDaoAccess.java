package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.user.dao.daoInterface.ThreeScreenRecommendDaoInterface;
import com.fortune.rms.business.user.logic.logicImpl.XmlHelper;
import com.fortune.rms.business.user.logic.logicInterface.RecommendFriendLogicInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.RecommendFriend;
import com.fortune.rms.business.user.model.ThreeScreenRecommend;
import com.fortune.rms.business.user.model.RecommendNotify;
import com.fortune.threeScreen.serverMessage.ServerMessager;
import com.fortune.util.SpringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-19
 * Time: 上午9:36
 */
@Repository
public class ThreeScreenRecommendDaoAccess extends BaseDaoAccess<ThreeScreenRecommend, Long> implements ThreeScreenRecommendDaoInterface {

    public ThreeScreenRecommendDaoAccess() {
        super(ThreeScreenRecommend.class);
    }

    private RecommendFriendLogicInterface recommendFriendLogicInterface;


    @Autowired
    public void setRecommendFriendLogicInterface(RecommendFriendLogicInterface recommendFriendLogicInterface) {
        this.recommendFriendLogicInterface = recommendFriendLogicInterface;
    }


    public AllResp addRecommendFromDb(String userName, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp) {
        AllResp allResp = new AllResp();

        //查询是否有已经已经推荐该影片，依据contentId,userName,friendIdList
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) as num from frd_recommend f inner join frd_recommend_friend f1 on f.id = f1.recommend_id" +
                " and f.recommender='" + userName + "' and f.content_id = '" + contentId + "' and f1.friend_id in ('" + friendIdList + "')";

        int num = 0;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            num = 0;
            while (rs.next()) {
                num = rs.getInt("num");
            }
        } catch (Exception e) {

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }


        }

        try {
            if (num == 0) {
                sql = "from ThreeScreenRecommend f where f.contentId = '" + contentId + "' and f.recommender = '" + userName + "'";
                List<ThreeScreenRecommend> threeScreenRecommends = this.getHibernateTemplate().find(sql);
                if (threeScreenRecommends != null && threeScreenRecommends.size() == 0) {
                    ThreeScreenRecommend threeScreenRecommend = new ThreeScreenRecommend();
                    threeScreenRecommend.setRecommender(userName);
                    threeScreenRecommend.setRecommenderName(userName);
                    threeScreenRecommend.setUserType(userType);
                    threeScreenRecommend.setContentId(contentId);
                    threeScreenRecommend.setSubContentId(subContentId);
                    threeScreenRecommend.setSubContentName(subContentName);
                    threeScreenRecommend.setSubContentType(subContentType);
                    threeScreenRecommend.setServiceType(serviceType);
                    threeScreenRecommend.setMode(mode);
                    threeScreenRecommend.setFriendIdList(friendIdList);
                    threeScreenRecommend.setInfo(info);
                    threeScreenRecommend.setTimeStamp(timeStamp);
                    long recommendId = (Long) this.getHibernateTemplate().save(threeScreenRecommend);
                    threeScreenRecommend.setRecommendedId(String.valueOf(recommendId));
                    this.getHibernateTemplate().update(threeScreenRecommend);
                    RecommendFriend recommendFriend = null;
                    String recommendIdTemp = String.valueOf(recommendId);
                    String friendIds[] = friendIdList.split(",");
                    if (friendIds != null && friendIds.length > 0) {
                        for (int i = 0; i < friendIds.length; i++) {
                            recommendFriend = new RecommendFriend();
                            recommendFriend.setRecommendId(recommendIdTemp);
                            recommendFriend.setFriendId(friendIds[i]);
                            recommendFriend.setStatus(1);
                            this.recommendFriendLogicInterface.save(recommendFriend);

                        }
                    } else {
                        recommendFriend = new RecommendFriend();
                        recommendFriend.setRecommendId(recommendIdTemp);
                        recommendFriend.setFriendId(friendIdList);
                        recommendFriend.setStatus(1);
                        this.recommendFriendLogicInterface.save(recommendFriend);
                    }

                } else if (threeScreenRecommends.size() > 0) {
                    ThreeScreenRecommend threeScreenRecommend = threeScreenRecommends.get(0);
                    threeScreenRecommend.setTimeStamp(timeStamp);
                    this.getHibernateTemplate().update(threeScreenRecommend);
                    RecommendFriend recommendFriend = null;

                    String friendIds[] = friendIdList.split(",");
                    if (friendIds != null && friendIds.length > 0) {
                        for (int i = 0; i < friendIds.length; i++) {
                            recommendFriend = new RecommendFriend();
                            recommendFriend.setRecommendId(String.valueOf(threeScreenRecommend.getId()));
                            recommendFriend.setFriendId(friendIds[i]);
                            recommendFriend.setStatus(1);
                            this.recommendFriendLogicInterface.save(recommendFriend);

                        }
                    } else {
                        recommendFriend = new RecommendFriend();
                        recommendFriend.setRecommendId(String.valueOf(threeScreenRecommend.getId()));
                        recommendFriend.setFriendId(friendIdList);
                        recommendFriend.setStatus(1);
                        this.recommendFriendLogicInterface.save(recommendFriend);
                    }


                }

            }

            allResp.setResultCode(1);
            allResp.setReason("保存数据成功");

        } catch (Exception e) {
            logger.error(e.getMessage());
            allResp.setResultCode(2);
            allResp.setReason("保存数据失败");
        }

        return allResp;
    }

    public static Connection getConnection()
            throws Exception {

        return DataSourceUtils.getConnection((DataSource) SpringUtils.getBean("dataSource"));
    }


    public AllResp getAllRecommendFromDb(String userName, Integer userType) {
        AllResp allResp = new AllResp();
        String sql = "select f.id,f.user_id,f.user_type,f.content_id,f.subcontent_id,f.subcontent_name,f.subcontent_type,f.mode1,f.friend_id_list,f.info," +
                "f.time_stamp,f.recommended_id,f.recommender,f.recommender_name,f1.status from frd_recommend f inner join frd_recommend_friend f1 on f.id = f1.recommend_id and f.user_type = " + userType + " inner join frd_user_friend f2 on f1.friend_id = f2.friend_id and f2.friend_name = '" + userName + "'";
        //String sql = "select f.id,f.user_id,f.user_type,f.content_id,f.subcontent_id,f.subcontent_name,f.subcontent_type,f.model1,f.friend_id_list,f.info" +
        //        "f.time_stamp,f.recommender_id,f.recommender,f.recommender_name,f1.status from frd_recommend f where f.user_type= "+userType+" and f.id = (select f1.recommend_id from frd_recommend_friend f1 where f1.friend_id = (select f2.friend_id from frd_friend f2 where f2.friend_name = '"+ userName +"'))";
        // String hql = "from ThreeScreenRecommend f where f.userType = "+userType+" and f.id = (select f1.recommendId from RecommendFriend f1 where f1.friendId = (select f2.friendId from UserFriend f2 where f2.userName='"+userName+"' ))";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ThreeScreenRecommend> threeScreenRecommends = new ArrayList<ThreeScreenRecommend>();
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ThreeScreenRecommend threeScreenRecommend = null;
            while (rs.next()) {
                threeScreenRecommend = new ThreeScreenRecommend();
                threeScreenRecommend.setId(rs.getLong("id"));
                threeScreenRecommend.setUserType(rs.getInt("user_type"));
                threeScreenRecommend.setContentId(rs.getString("content_id"));
                threeScreenRecommend.setSubContentId(rs.getString("subcontent_id"));
                threeScreenRecommend.setSubContentName(rs.getString("subcontent_name"));
                threeScreenRecommend.setSubContentType(rs.getInt("subcontent_type"));
                threeScreenRecommend.setMode(rs.getInt("mode1"));
                threeScreenRecommend.setFriendIdList(rs.getString("friend_id_list"));
                threeScreenRecommend.setInfo(rs.getString("info"));
                threeScreenRecommend.setTimeStamp(rs.getString("time_stamp"));
                threeScreenRecommend.setRecommendedId(rs.getString("recommended_id"));
                threeScreenRecommend.setRecommender(rs.getString("recommender"));
                threeScreenRecommend.setRecommenderName(rs.getString("recommender_name"));
                threeScreenRecommend.setStatus(rs.getInt("status"));
                threeScreenRecommends.add(threeScreenRecommend);
            }
            allResp.setList(threeScreenRecommends);
            allResp.setResultCode(1);
            allResp.setReason("读取数据成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                allResp.setResultCode(2);
                allResp.setReason("读取数据失败");
            }

        }

//        try{
//            List<ThreeScreenRecommend> frdRecommendList = this.getHibernateTemplate().find(hql);
//
//        }catch (Exception e){
//
//        }
        return allResp;
    }


    public AllResp updateOrDeleteRecommendNotifyFromDb(String userName, Integer userType, Integer operationType, Integer status, String recommendedIdList) {
        //通过userName：自己的名称，在frd_recommend_friend 对应friendId 指向frd_user表中的的username
        //推荐Id，recommendIdList 默认单个删除 ，对应recommendId
        //找到对应的recommend_friend 对象
        String sql = "select * from frd_recommend_friend f inner join frd_user u on f.friend_id = u.id and u.user_name = '" + userName + "' and f.recommend_id ='" + recommendedIdList + "'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RecommendFriend> recommendFriends = new ArrayList<RecommendFriend>();
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            RecommendFriend recommendFriend = null;
            while (rs.next()) {
                recommendFriend = new RecommendFriend();
                recommendFriend.setId(rs.getLong("id"));
                recommendFriend.setRecommendId(rs.getString("recommend_id"));
                recommendFriend.setFriendId(rs.getString("friend_id"));
                recommendFriend.setStatus(rs.getInt("status"));
                recommendFriends.add(recommendFriend);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }

        if (recommendFriends != null && recommendFriends.size() > 0) {
            RecommendFriend recommendFriend = null;
            if (operationType.equals(1)) {
                for (int i = 0; i < recommendFriends.size(); i++) {
                    recommendFriend = recommendFriends.get(i);
                    //delete
                    this.recommendFriendLogicInterface.remove(recommendFriend);

                }

            } else if (operationType.equals(3)) {
                for (int i = 0; i < recommendFriends.size(); i++) {
                    recommendFriend = recommendFriends.get(i);
                    //update
                    recommendFriend.setStatus(2);
                    this.recommendFriendLogicInterface.update(recommendFriend);
                }

            }

        }


        return getAllRecommendFromDb(userName, userType);

    }


    public RecommendNotify getRecommendNotifyFromDb(String result) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public AllResp addRecommendFromHttp(String userId, Integer userType, String contentId, String subContentId, String subContentName, Integer subContentType, Integer serviceType, Integer mode, String friendIdList, String info, String timeStamp) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<AddRecommendReq><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType><ContentID>" + contentId + "</ContentID><SubContentID>" + subContentId + "</SubContentID><SubContentName>" + subContentName + "</SubContentName><SubContentType>" + subContentType + "</SubContentType><ServiceType>" + serviceType + "</ServiceType><Mode>" + mode + "</Mode><FriendIDList>" + friendIdList + "</FriendIDList><Info>" + info + "</Info><TimeStamp>" + timeStamp + "</TimeStamp></AddRecommendReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.recommend.saveUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_AddRecommendReq.aspx");
        return new XmlHelper().getSimpleResult(urlStr, xml);
    }


    public AllResp getAllRecommendFromHttp(String userId, Integer userType) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<GetAllRecommendReq><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType></GetAllRecommendReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.recommend.listUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_GetAllRecommendReq.aspx");
        ServerMessager serverMessage = new ServerMessager();
        String resp = serverMessage.postToHost(urlStr, xml);
        org.dom4j.Element root = com.fortune.util.XmlUtils.getRootFromXmlStr(resp);
        AllResp result = new XmlHelper().getResult(root);
        if (root != null) {
            List<Node> recommendNodeList = root.selectNodes("RecommendedList/RecommendedItem");
            List<ThreeScreenRecommend> recommends = new ArrayList<ThreeScreenRecommend>();
            for (Node node : recommendNodeList) {
                recommends.add(new ThreeScreenRecommend(node));
            }
            result.setList(recommends);
        }
        return result;
    }


    public AllResp updateOrDeleteRecommendNotifyFromHttp(String userId, Integer userType, Integer operationType, Integer status, String recommendedIdList) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<UpdateAndDeleteRecommendReq><UserID>" + userId + "</UserID><UserType>" + userType + "</UserType><Status>" + status + "</Status><OperationType>" + operationType + "</OperationType><RecommendedIDList>" + recommendedIdList + "</RecommendedIDList></UpdateAndDeleteRecommendReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.recommend.updateAndDeleteUrl", "http://61.55.150.16/IShareFT/Fortune.Interface/FT_UpdateAndDeleteRecommendReq.aspx");
        return new XmlHelper().getSimpleResult(urlStr, xml);
    }


    public RecommendNotify getRecommendNotifyFromHttp(String result) {
        Element doc = com.fortune.util.XmlUtils.getRootFromXmlStr(result);
        List list = doc.elements();
        RecommendNotify recommendNotify = new RecommendNotify();
        recommendNotify.setUserId(list.get(0).toString());
        recommendNotify.setUserType(Integer.valueOf(list.get(1).toString()));
        return recommendNotify;
    }
}
