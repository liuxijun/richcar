package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.IndividualDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.IndividualLogicInterface;
import com.fortune.rms.business.system.model.Individual;
import com.fortune.util.IndividualUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 王明路 on 2014/11/28.
 */
@Service("individualLogicInterface")
public class IndividualLogicImpl extends BaseLogicImpl<Individual>
        implements
        IndividualLogicInterface {
    private IndividualDaoInterface individualDaoInterface;

    @Autowired
    public void setIndividualDaoInterface(IndividualDaoInterface individualDaoInterface) {
        this.individualDaoInterface = individualDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)individualDaoInterface;
    }

    /**
     * 保存个性化信息
     * @param logoPath  logo路径
     * @param name       系统名称
     * @return 保存结果
     */
    public boolean saveIndividual(String logoPath, String name){
        Individual individual = new Individual();
        individual.setLogoPath(logoPath);
        individual.setName(name);

        return saveIndividual(individual);
    }

    /**
     * 保存个性化，并更新内存中保存的信息
     * @param individual 个性化对象
     * @return 设置是否成功
     */
    public boolean saveIndividual(Individual individual){
        // 检查是否已经有个性化设置，如果有，覆盖
        List<Individual> list = individualDaoInterface.getAll();

        Individual ind = new Individual();
        if(list != null && list.size() > 0){
            //ind.setId( list.get(0).getId());
            ind = list.get(0);
        }
        ind.setLogoPath(individual.getLogoPath());
        ind.setMobileLogoPath(individual.getMobileLogoPath());
        ind.setName(individual.getName());
        individualDaoInterface.save(ind);
        // 保存到内存中
        IndividualUtils.getInstance().setLogoURL(individual.getLogoPath());
        IndividualUtils.getInstance().setMobileLogoURL(individual.getMobileLogoPath());
        IndividualUtils.getInstance().setName(individual.getName());

        return true;
    }
}
