package com.fortune.rms.business.user.dao.daoInterface;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.Stb;

/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ионГ10:02
 * To change this template use File | Settings | File Templates.
 */
public interface StbDaoInterface extends BaseDaoInterface<Stb,Long>{
    public boolean selectStb(String srialNo);
}
