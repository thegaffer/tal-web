package org.tpspencer.tal.mvc.sample.objex.model.order;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class OrderSummaryStrategy implements ObjectStrategy {

	public String getTypeName() {
		return "OrderSummary";
	}
	
	public ObjexIDStrategy getIdStrategy() {
		return null;
	}
	
	public Class<? extends ObjexObjStateBean> getStateClass() {
		return OrderSummaryBean.class;
	}
	
	public ObjexObjStateBean getNewStateInstance(ObjexID parent) {
		return new OrderSummaryBean(parent);
	}
	
	public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean copy) {
		return new OrderSummaryBean((OrderSummaryBean)copy);
	}
	
	public ObjexObj getObjexObjInstance(Container container, ObjexID parentId, ObjexID id, ObjexObjStateBean state) {
		OrderSummaryImpl ret = new OrderSummaryImpl((OrderSummaryBean)state);
		ret.init(container, id, parentId);
		return ret;
	}
}
