package org.tpspencer.tal.mvc.sample.objex.model.common;

import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.objexj.annotations.ObjexObj;

@ObjexObj(AddressBean.class)
public class AddressImpl implements Address {

	private AddressBean bean;
	
	public AddressImpl(AddressBean bean) {
		this.bean = bean;
	}
}
