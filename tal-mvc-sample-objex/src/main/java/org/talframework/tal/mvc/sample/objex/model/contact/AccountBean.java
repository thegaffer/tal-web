package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.talframework.objexj.annotations.ObjexRefProp;
import org.talframework.objexj.annotations.ObjexStateBean;
import org.tpspencer.tal.mvc.sample.model.common.Address;

@ObjexStateBean(name="Account")
public class AccountBean {
	private final static long serialVersionUID = 1L;
	
	private String accountNos = null;

	private String company = null;
	
	@ObjexRefProp(owned=true, type=Address.class, newType="Address")
	private String address = null;
}
