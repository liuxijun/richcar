package com.fortune.rms.business.user.logic.logicInterface;
import com.fortune.rms.business.user.model.Stb;
import com.fortune.common.business.base.logic.BaseLogicInterface;
/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ионГ10:09
 * To change this template use File | Settings | File Templates.
 */
public interface StbLogicInterface extends BaseLogicInterface<Stb> {
     public static final Integer STATUS_OK=1;
    public boolean selectStb(Stb s);
}
