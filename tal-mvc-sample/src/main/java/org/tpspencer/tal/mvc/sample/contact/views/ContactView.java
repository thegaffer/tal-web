package org.tpspencer.tal.mvc.sample.contact.views;

import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;

public class ContactView extends FormView {

	public ContactView() {
		super();
		setViewName("contact");
		setPrimaryBean(Contact.class);
		setInitialFormBean("currentContact");
		setTemplateFile("/org/tpspencer/tal/mvc/sample/contact/Contact.xml");
		
		init();
	}
}
