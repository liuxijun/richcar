package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.frontuser.dao.daoInterface.FrontUserDaoInterface;
import com.fortune.rms.business.user.dao.daoInterface.UserTypeDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserTypeLogicInterface;
import com.fortune.rms.business.user.model.UserType;
import com.fortune.rms.business.user.model.UserTypeDetail;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */

@Service("userTypeLogicInterface")
public class UserTypeLogicImpl extends BaseLogicImpl<UserType> implements UserTypeLogicInterface {
    private UserTypeDaoInterface userTypeDaoInterface;
    private FrontUserDaoInterface frontUserDaoInterface;
    private ContentDaoInterface contentDaoInterface;

    @Autowired
    public void setContentDaoInterface(ContentDaoInterface contentDaoInterface) {
        this.contentDaoInterface = contentDaoInterface;
    }

    @Autowired
    public void setFrontUserDaoInterface(FrontUserDaoInterface frontUserDaoInterface) {
        this.frontUserDaoInterface = frontUserDaoInterface;
    }

    @Autowired
    public void setUserTypeDaoInterface(UserTypeDaoInterface userTypeDaoInterface) {
        this.userTypeDaoInterface = userTypeDaoInterface;
        baseDaoInterface = (BaseDaoInterface) userTypeDaoInterface;
    }

    /**
     * 获得所有用户类型及每个类型的人数
     * @return UserTypeDetail列表
     */
    @SuppressWarnings("unchecked")
    public List<UserTypeDetail> getUserType(){
        List<UserType> l = userTypeDaoInterface.getAll();
        if(l == null || l.size() == 0) return null;

        List<UserTypeDetail> userTypeDetailList = new ArrayList();
        // 查询每个类型的人数
        for(UserType ut : l){
            UserTypeDetail utd = new UserTypeDetail(ut);
            utd.setUserCount( (long)(frontUserDaoInterface.getUserCountByType(ut.getId())) );
            userTypeDetailList.add(utd);
        }

        return userTypeDetailList;
    }

    public void removeUserType(Long id, Long altId){
        if( altId > 0 ){
            userTypeDaoInterface.changeUserType(id, altId);
        }

        userTypeDaoInterface.remove(id);
    }

    /**
     * 为用户类型关联已有栏目
     * @param typeId    用户类型Id
     * @param channels  要关联的栏目id，用逗号分隔
     * @return 成功与否
     */
    public boolean referenceChannels(long typeId, String channels){
        // 格式化一下channels，防止有不合法内容
        String[] channelArray = channels.split(",");
        String formattedChannels = "";
        for(String s : channelArray){
            long v = StringUtils.string2long(s, 0);
            formattedChannels += formattedChannels.isEmpty()? v : "," + v;
        }
        contentDaoInterface.addUserType(typeId, formattedChannels);
        // 没有什么成功与否，返回true
        return true;
    }
}
