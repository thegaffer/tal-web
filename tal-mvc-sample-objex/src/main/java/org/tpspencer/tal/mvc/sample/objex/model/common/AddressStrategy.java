package org.tpspencer.tal.mvc.sample.objex.model.common;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class AddressStrategy implements ObjectStrategy {

	public String getTypeName() {
		return "Address";
	}
	
	public ObjexIDStrategy getIdStrategy() {
		return null;
	}
	
	public Class<? extends ObjexObjStateBean> getStateClass() {
		return AddressBean.class;
	}
	
	public ObjexObjStateBean getNewStateInstance(ObjexID parent) {
		return new AddressBean(parent);
	}
	
	public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean copy) {
		return new AddressBean((AddressBean)copy);
	}
	
	public ObjexObj getObjexObjInstance(Container container, ObjexID parentId, ObjexID id, ObjexObjStateBean state) {
		AddressImpl ret = new AddressImpl((AddressBean)state);
		ret.init(container, id, parentId);
		return ret;
	}
}
