package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.objexj.annotations.ObjexRefProp;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

@ObjexStateBean(name="Contact")
public class ContactBean {
	private final static long serialVersionUID = 1L;

	private String firstName = null;
	
	private String lastName = null;
	
	private String account = null;
	
	private String company = null;
	
	@ObjexRefProp(owned=true, type=Address.class, newType="Address")
	private String address = null;
	
	private String previousCrn = null;
	
}
