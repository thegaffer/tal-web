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

import org.springframework.beans.factory.annotation.Autowired;
import org.tpspencer.tal.mvc.controller.InputBinder;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ModelBindInput;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.sample.service.ContactService;
import org.tpspencer.tal.mvc.sample.service.transfer.SaveContactResult;

/**
 * This controller handles a submit of the contact form.
 * It is bound to the contact flash attribute for events.
 * The controller itself just updates the repository
 * (good or bad contacts are stored).
 * 
 * @author Tom Spencer
 */
@Controller(binder="binder")
public class SubmitContactController {
	
	/** Holds the contact service, which performs the business logic */
	private ContactService service = null;
	/** Holds the input binder for this controller */
	private InputBinder binder = null;

	@Action(result="contactSubmitted", errorResult="contactSubmitFailed")
	public void submit(
			@ModelBindInput(prefix="form", modelAttribute="contact") Contact contact, 
			@ModelInput Map<String, Object> model) {
		SaveContactResult res = service.saveContact(contact);
		
		model.put("selectedContact", res.getContactId());
		model.put("contact", res.getContact());
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
	@Autowired
	public void setService(ContactService service) {
		this.service = service;
	}

	/**
	 * @return the input binder for controller
	 */
	public InputBinder getBinder() {
		return binder;
	}

	/**
	 * @param binder The binder to use
	 */
	@Autowired
	public void setBinder(InputBinder binder) {
		this.binder = binder;
	}
}
