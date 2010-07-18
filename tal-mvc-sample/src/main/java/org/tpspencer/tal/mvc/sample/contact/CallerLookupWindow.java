package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.sample.model.contact.Caller;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

public class CallerLookupWindow {

	/** The contact, set by other windows, for us to extract search from */
	@ModelAttr(flash=true, evantable=true, aliasExpected=true)
	@OnChange(action="contactUpdated", newValueParam="contact")
	public Contact contact;
	/** The caller set when selected for other windows to event */
	@ModelAttr(flash=true, evantable=true, aliasExpected=true)
	public Caller caller;
	/** Model attribute to retreive callers given other model attributes */
	@ModelAttr
	@Resource(name="callerSearchResolver")
	public ModelResolver callers;
	/** The extracted first name we will search on */
	@ModelAttr
	public String firstName;
	/** The extracted last name we will search on */
	@ModelAttr
	public String lastName;
	
	
	@WindowView(name="searchView")
	@Resource(name="callerLookupView")
	public View searchView;
	
	
	@When(action="selectCaller")
	@Resource(name="selectCallerController")
	public Object selectCaller;
	
	@When(action="contactUpdated")
	@Resource(name="callerContactUpdatedController")
	public Object contactUpdated;
}
