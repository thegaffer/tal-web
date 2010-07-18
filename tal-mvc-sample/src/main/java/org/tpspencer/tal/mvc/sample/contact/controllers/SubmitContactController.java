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
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.sample.service.ContactService;
import org.tpspencer.tal.mvc.spring.controller.SpringInputBinder;

/**
 * This controller handles a submit of the contact form.
 * It is bound to the contact flash attribute for events.
 * The controller itself just updates the repository
 * (good or bad contacts are stored).
 * 
 * @author Tom Spencer
 */
@Controller(binderType=SpringInputBinder.class)
public class SubmitContactController {
	
	/** Holds the contact service, which performs the business logic */
	private ContactService service = null;

	@Action(result="contactSubmitted", errorResult="contactSubmitFailed")
	public void submit(
			@BindInput(prefix="form", modelAttribute="contact") Contact contact, 
			@ModelInput Map<String, Object> model) {
		contact = service.saveContact(contact);
		
		model.put("selectedContact", contact.getId());
	}
	
	@Action(action="cancel", result="contactCancelled")
	public void cancel(
			@ModelInput Map<String, Object> model) {
		model.remove("selectedContact");
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
