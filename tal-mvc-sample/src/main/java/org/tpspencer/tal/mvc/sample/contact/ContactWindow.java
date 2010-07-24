package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * This class represents the contact window
 * 
 * @author Tom Spencer
 */
public class ContactWindow {
	
	@Resource(name="contactWindowModel")
	public ModelConfiguration model;
	

	@WindowView(name="contactView")
	@Resource(name="contactView")
	public View contactView;
	
	
	@When(action="submitContact")
	@Resource(name="submitContactController")
	public Object submitContact;
	
	@When(action="callerSelected")
	@Resource(name="callerSelectedController")
	@OnChange(attribute="caller", newValueParam="caller")
	public Object callerSelected;
	
	@When(action="accountSelected")
	@Resource(name="accountSelectedController")
	@OnChange(attribute="account", newValueParam="account")
	public Object accountSelected;
}
