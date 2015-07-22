package com.fortune.rms.business.user.logic.logicImpl;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.user.logic.logicInterface.StbLogicInterface;
import com.fortune.rms.business.user.model.Stb;
import com.fortune.rms.business.user.dao.daoAccess.StbDaoAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ����10:17
 * ҵ���߼��ж�
 * selectStb==���������ݷ��ʲ�Ĵ���
 */
@Service("stbLogicImpl")
public class StbLogicImpl extends BaseLogicImpl<Stb> implements StbLogicInterface {
    private StbDaoAccess stbDaoAccess;

    @Autowired
    public void setStbDaoAccess(StbDaoAccess stbInfDaoAccess) {
        this.stbDaoAccess = stbInfDaoAccess;
        baseDaoInterface = (BaseDaoInterface) this.stbDaoAccess;
    }
    public boolean selectStb(Stb s){
        return stbDaoAccess.selectStb(s.getSerialNo());
    }
}
