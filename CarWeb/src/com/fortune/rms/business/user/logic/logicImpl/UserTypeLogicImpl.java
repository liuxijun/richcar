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
 * Time: ����2:17
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
     * ��������û����ͼ�ÿ�����͵�����
     * @return UserTypeDetail�б�
     */
    @SuppressWarnings("unchecked")
    public List<UserTypeDetail> getUserType(){
        List<UserType> l = userTypeDaoInterface.getAll();
        if(l == null || l.size() == 0) return null;

        List<UserTypeDetail> userTypeDetailList = new ArrayList();
        // ��ѯÿ�����͵�����
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
     * Ϊ�û����͹���������Ŀ
     * @param typeId    �û�����Id
     * @param channels  Ҫ��������Ŀid���ö��ŷָ�
     * @return �ɹ����
     */
    public boolean referenceChannels(long typeId, String channels){
        // ��ʽ��һ��channels����ֹ�в��Ϸ�����
        String[] channelArray = channels.split(",");
        String formattedChannels = "";
        for(String s : channelArray){
            long v = StringUtils.string2long(s, 0);
            formattedChannels += formattedChannels.isEmpty()? v : "," + v;
        }
        contentDaoInterface.addUserType(typeId, formattedChannels);
        // û��ʲô�ɹ���񣬷���true
        return true;
    }
}
