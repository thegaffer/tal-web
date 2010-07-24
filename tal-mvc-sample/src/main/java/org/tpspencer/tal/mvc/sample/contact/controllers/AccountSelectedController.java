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

package org.tpspencer.tal.mvc.sample.contact.controllers;

import java.util.Map;

import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.Input;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.sample.model.account.Account;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.sample.service.ContactService;

/**
 * Event handler when shared account is updated. This
 * controller will update the current contact (if 
 * there is one) with the account info
 * 
 * @author Tom Spencer
 */
@Controller
public class AccountSelectedController {
	
	/** Holds the contact service, which performs the business logic */
	private ContactService service = null;
	
	@Action(result="contactUpdated")
	public void contactUpdated(
			@Input(param="account") Account account, 
			@ModelInput Map<String, Object> model) {
		
		String contactId = (String)model.get("selectedContact");
		Contact contact = service.updateContactAccount(contactId, account);
		model.put("selectedContact", contact.getId());
		model.put("contact", contact);
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
