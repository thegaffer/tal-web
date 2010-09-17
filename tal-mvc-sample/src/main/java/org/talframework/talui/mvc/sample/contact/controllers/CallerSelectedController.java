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

package org.talframework.talui.mvc.sample.contact.controllers;

import java.util.Map;

import org.talframework.talui.mvc.controller.annotations.Action;
import org.talframework.talui.mvc.controller.annotations.Controller;
import org.talframework.talui.mvc.controller.annotations.Input;
import org.talframework.talui.mvc.controller.annotations.ModelInput;
import org.talframework.talui.mvc.sample.model.contact.Caller;
import org.talframework.talui.mvc.sample.service.ContactService;
import org.talframework.talui.mvc.sample.service.transfer.SaveContactResult;

/**
 * Event handler when shared caller is updated. This
 * controller will update the current contact (if 
 * there is one) with the caller info
 * 
 * @author Tom Spencer
 */
@Controller
public class CallerSelectedController {
	
	/** Holds the contact service, which performs the business logic */
	private ContactService service = null;

	@Action(result="contactUpdated")
	public void contactUpdated(
			@Input(param="caller") Caller caller, 
			@ModelInput Map<String, Object> model) {
		
		String contactId = (String)model.get("selectedContact");
		SaveContactResult res = service.updateContactCaller(contactId, caller);
		model.put("selectedContact", res.getContactId());
		model.put("contact", res.getContact());
	}

	/**
	 * @return the service
	 */
	public ContactService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ContactService service) {
		this.service = service;
	}
}
