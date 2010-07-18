package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.sample.model.account.Account;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * This class represents the account lookup window
 * 
 * @author Tom Spencer
 */
public class AccountLookupWindow {

	/** The contact, set by other windows, for us to extract search from */
	@ModelAttr(flash=true, evantable=true, aliasExpected=true)
	@OnChange(action="contactUpdated", newValueParam="contact")
	public Contact contact;
	/** The account set when selected for other windows to event */
	@ModelAttr(flash=true, evantable=true, aliasExpected=true)
	public Account account;
	/** The accounts found against criteria */
	@ModelAttr
	@Resource(name="accountSearchResolver")
	public ModelResolver accounts;
	/** The extracted account ID we will search on */
	@ModelAttr
	public String accountId;
	/** The extracted company name we will search on */
	@ModelAttr
	public String companyName;
	
	
	@WindowView(name="searchView")
	@Resource(name="accountLookupView")
	public View searchView;
	
	
	@When(action="selectAccount")
	@Resource(name="selectAccountController")
	public Object selectAccount;
	
	@When(action="contactUpdated")
	@Resource(name="accountContactUpdatedController")
	public Object contactUpdated;
}
