package org.tpspencer.tal.mvc.sample.contact.views;

import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.sample.model.contact.Caller;

public class CallerView extends FormView {

	public CallerView() {
		super();
		setViewName("callerView");
		setPrimaryBean(Caller.class);
		setInitialFormBean("caller");
		setAsForm(false);
		
		init();
	}
}
