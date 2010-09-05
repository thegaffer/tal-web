package org.tpspencer.tal.mvc.sample.objex.model.common;

import org.talframework.objexj.annotations.ObjexObj;
import org.tpspencer.tal.mvc.sample.model.common.Address;

@ObjexObj(AddressBean.class)
public class AddressImpl implements Address {

	private AddressBean bean;
	
	public AddressImpl(AddressBean bean) {
		this.bean = bean;
	}
}
