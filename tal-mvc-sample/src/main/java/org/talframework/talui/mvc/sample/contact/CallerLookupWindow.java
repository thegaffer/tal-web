package org.tpspencer.tal.mvc.sample.contact;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.window.annotations.OnChange;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

public class CallerLookupWindow {

	@Resource(name="callerLookupWindowModel")
	public ModelConfiguration model;
	
	@WindowView(name="searchView")
	@Resource(name="callerLookupView")
	public View searchView;
	
	
	@When(action="selectCaller")
	@Resource(name="selectCallerController")
	public Object selectCaller;
	
	@When(action="contactUpdated")
	@Resource(name="callerContactUpdatedController")
	@OnChange(attribute="contact", newValueParam="contact")
	public Object contactUpdated;
}
