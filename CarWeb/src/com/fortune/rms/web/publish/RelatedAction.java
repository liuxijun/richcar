package com.fortune.rms.web.publish;

import com.fortune.rms.business.publish.logic.logicInterface.RelatedLogicInterface;
import com.fortune.rms.business.publish.model.Related;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.SearchResult;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/publish")
@ParentPackage("default")
@Action(value="related")
public class RelatedAction extends BaseAction<Related> {
	private static final long serialVersionUID = 3243534534534534l;
	private RelatedLogicInterface relatedLogicInterface;
	@SuppressWarnings("unchecked")
	public RelatedAction() {
		super(Related.class);
	}
	/**
	 * @param relatedLogicInterface the relatedLogicInterface to set
	 */
    @Autowired
	public void setRelatedLogicInterface(
			RelatedLogicInterface relatedLogicInterface) {
		this.relatedLogicInterface = relatedLogicInterface;
		setBaseLogicInterface(relatedLogicInterface);
	}

    public String getRelatedProperty(){
        try{
            String sqlTable = "select " +
                    "rp.id,rp.relatedId,rp.propertyId,rp.propertyValue,p.name,p.dataType" +
                    " from " +
                    "com.fortune.rms.business.publish.model.RelatedProperty rp,"+
                    "com.fortune.rms.business.module.model.Property p "+
                    " where rp.propertyId=p.id ";

            String[][] params = new String[][]{
                    {"rp.relatedId",         "",           "=",    ""},
                    {"rp.propertyId",         "",          "=",    ""},
                    {"rp.propertyValue",       "",         "=",    ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable ,params);

            String output = searchObjectsJbon(sqlTable,searchResult);

            directOut(output);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
