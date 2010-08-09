package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class AccountStrategy implements ObjectStrategy {

	public String getTypeName() {
		return "Account";
	}
	
	public ObjexIDStrategy getIdStrategy() {
		return null;
	}
	
	public Class<? extends ObjexObjStateBean> getStateClass() {
		return AccountBean.class;
	}
	
	public ObjexObjStateBean getNewStateInstance(ObjexID parent) {
		return new AccountBean(parent);
	}
	
	public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean copy) {
		return new AccountBean((AccountBean)copy);
	}
	
	public ObjexObj getObjexObjInstance(Container container, ObjexID parentId, ObjexID id, ObjexObjStateBean state) {
		AccountImpl ret = new AccountImpl((AccountBean)state);
		ret.init(container, id, parentId);
		return ret;
	}
}
