package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.sample.model.account.Account;
import org.tpspencer.tal.mvc.sample.model.contact.Caller;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * This class represents the contact window
 * 
 * @author Tom Spencer
 */
public class ContactWindow {
	
	/** The contact object for other windows to listen for changes on */
	@ModelAttr(flash=true, evantable=true)
	public Contact contact;
	/** The selected caller (only last as it is selected) */
	@ModelAttr(flash=true, evantable=true)
	@OnChange(action="callerSelected", newValueParam="caller")
	public Caller caller;
	/** The selected account (only last as it is selected) */
	@ModelAttr(flash=true, evantable=true)
	@OnChange(action="accountSelected", newValueParam="account")
	public Account account;
	@ModelAttr
	public String selectedContact;
	@ModelAttr
	@Resource(name="currentContactResolver")
	public ModelResolver currentContact;

	
	@WindowView(name="contactView")
	@Resource(name="contactView")
	public View contactView;
	
	
	@When(action="submitContact")
	@Resource(name="submitContactController")
	public Object submitContact;
	
	@When(action="callerSelected")
	@Resource(name="callerSelectedController")
	public Object callerSelected;
	
	@When(action="accountSelected")
	@Resource(name="accountSelectedController")
	public Object accountSelected;
}
