package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * This class represents the account lookup window
 * 
 * @author Tom Spencer
 */
public class AccountLookupWindow {
	
	@Resource(name="accountLookupWindowModel")
	public ModelConfiguration model;

	@WindowView(name="searchView")
	@Resource(name="accountLookupView")
	public View searchView;
	
	
	@When(action="selectAccount")
	@Resource(name="selectAccountController")
	public Object selectAccount;
	
	@When(action="contactUpdated")
	@Resource(name="accountContactUpdatedController")
	@OnChange(attribute="contact", newValueParam="contact")
	public Object contactUpdated;
}
