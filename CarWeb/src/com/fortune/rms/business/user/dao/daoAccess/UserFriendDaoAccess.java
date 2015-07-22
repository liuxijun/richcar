package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.user.dao.daoInterface.UserFriendDaoInterface;
import com.fortune.rms.business.user.logic.logicImpl.XmlHelper;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.UserFriend;
import com.fortune.threeScreen.serverMessage.ServerMessager;
import com.fortune.util.SpringUtils;
import org.dom4j.Node;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-19
 * Time: 下午3:59
 */
@Repository
public class UserFriendDaoAccess extends BaseDaoAccess<UserFriend, Long> implements UserFriendDaoInterface {

    public UserFriendDaoAccess() {
        super(UserFriend.class);
    }


    public static Connection getConnection()
            throws Exception {

        return DataSourceUtils.getConnection((DataSource) SpringUtils.getBean("dataSource"));
    }



    public AllResp getAllFriendFromDb(String userName, Integer userType) {
        AllResp allResp = new AllResp();
        //String hql = "from UserFriend f where f.userId in (select userId = '"+userName+"')";
        String sql = "select u1.id,u1.friend_id,u1.friend_name,u1.user_id from frd_user_friend u1 inner join frd_user u2 on u1.user_id = u2.id and u2.user_name = '"+userName+"'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserFriend> userFriends = new ArrayList<UserFriend>();
        try{
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            UserFriend userFriend = null;
            while(rs.next()){
                userFriend = new UserFriend();
                userFriend.setId(rs.getLong("id"));
                userFriend.setFriendId(rs.getString("friend_id"));
                userFriend.setFriendName(rs.getString("friend_name"));
                userFriend.setUserId(rs.getString("user_id"));
                userFriends.add(userFriend);
            }
            if(userFriend!=null){
                allResp.setList(userFriends);
            }
            allResp.setResultCode(1);
            allResp.setReason("读取数据成功");
        }catch (Exception e){
            allResp.setResultCode(2);
            allResp.setReason("读取数据失败");
        }
        return allResp;
    }


    public AllResp getAllFriendFromHttp(String userId, Integer userType) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<GetAllFriendReq><UserID>"+userId+"</UserID><UserType>"+userType+"</UserType></GetAllFriendReq>";
        String urlStr = ConfigManager.getInstance().getConfig("Ericsson.friend.listUrl","http://61.55.150.16/IShareFT/Fortune.Interface/FT_GetAllFriendReq.aspx");
        ServerMessager serverMessage = new ServerMessager();
        String resp = serverMessage.postToHost(urlStr,xml);
        org.dom4j.Element root = com.fortune.util.XmlUtils.getRootFromXmlStr(resp);
        AllResp result = new XmlHelper().getResult(root);
        if(root!=null){
            List<Node> friendNodeList = root.selectNodes("FriendList/FriendItem");
            List<UserFriend> userFriends = new ArrayList<UserFriend>();
            for(Node node :friendNodeList){
                userFriends.add(new UserFriend(node));
            }
            result.setList(userFriends);
        }
        return result;
    }
}
