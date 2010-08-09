package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class CallerStrategy implements ObjectStrategy {

	public String getTypeName() {
		return "Caller";
	}
	
	public ObjexIDStrategy getIdStrategy() {
		return null;
	}
	
	public Class<? extends ObjexObjStateBean> getStateClass() {
		return CallerBean.class;
	}
	
	public ObjexObjStateBean getNewStateInstance(ObjexID parent) {
		return new CallerBean(parent);
	}
	
	public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean copy) {
		return new CallerBean((CallerBean)copy);
	}
	
	public ObjexObj getObjexObjInstance(Container container, ObjexID parentId, ObjexID id, ObjexObjStateBean state) {
		CallerImpl ret = new CallerImpl((CallerBean)state);
		ret.init(container, id, parentId);
		return ret;
	}
}
