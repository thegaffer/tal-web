package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.talframework.objexj.annotations.ObjexStateBean;

@ObjexStateBean(name="Caller")
public class CallerBean {
	private final static long serialVersionUID = 1L;

	private String firstName = null;
	
	private String lastName = null;
	
	// TODO: This is a foreign reference??
	private String account = null;
	
}
