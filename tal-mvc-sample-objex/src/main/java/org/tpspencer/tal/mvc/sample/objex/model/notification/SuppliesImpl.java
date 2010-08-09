package org.tpspencer.tal.mvc.sample.objex.model.notification;

import org.tpspencer.tal.mvc.sample.model.notification.Supplies;
import org.tpspencer.tal.objexj.annotations.ObjexObj;

//@ObjexObj(SuppliesBean.class)
public class SuppliesImpl implements Supplies {

	private SuppliesBean bean;
	
	public SuppliesImpl(SuppliesBean bean) { 
		this.bean = bean;
	}
}
