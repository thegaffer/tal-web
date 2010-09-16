package org.tpspencer.tal.mvc.sample.contact.views;

import org.tpspencer.tal.mvc.commons.views.table.TableView;
import org.tpspencer.tal.mvc.sample.model.contact.Caller;

public class CallerLookupView extends TableView {

	public CallerLookupView() {
		super();
		setViewName("callerLookup");
		setViewBeanName("callers");
		setPrimaryBean(Caller.class);
		setTemplateFile("/org/tpspencer/tal/mvc/sample/contact/CallerList.xml");
		
		init();
	}
}
