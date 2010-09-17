/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.sample.contact;

import javax.annotation.Resource;

import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.window.annotations.OnChange;
import org.talframework.talui.mvc.window.annotations.When;
import org.talframework.talui.mvc.window.annotations.WindowView;

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
