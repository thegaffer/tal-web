package org.tpspencer.tal.mvc.sample.objex.model.order;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class OrderStrategy implements ObjectStrategy {

	public String getTypeName() {
		return "Order";
	}
	
	public ObjexIDStrategy getIdStrategy() {
		return null;
	}
	
	public Class<? extends ObjexObjStateBean> getStateClass() {
		return OrderBean.class;
	}
	
	public ObjexObjStateBean getNewStateInstance(ObjexID parent) {
		return new OrderBean(parent);
	}
	
	public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean copy) {
		return new OrderBean((OrderBean)copy);
	}
	
	public ObjexObj getObjexObjInstance(Container container, ObjexID parentId, ObjexID id, ObjexObjStateBean state) {
		OrderImpl ret = new OrderImpl((OrderBean)state);
		ret.init(container, id, parentId);
		return ret;
	}
}
