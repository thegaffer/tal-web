package org.tpspencer.tal.mvc.sample.contact.views;

import org.tpspencer.tal.mvc.commons.views.table.TableView;
import org.tpspencer.tal.mvc.sample.model.account.Account;

public class AccountLookupView extends TableView {

	public AccountLookupView() {
		super();
		setViewName("accountLookup");
		setViewBeanName("accounts");
		setPrimaryBean(Account.class);
		setTemplateFile("/org/tpspencer/tal/mvc/sample/contact/AccountList.xml");
		
		init();
	}
}
