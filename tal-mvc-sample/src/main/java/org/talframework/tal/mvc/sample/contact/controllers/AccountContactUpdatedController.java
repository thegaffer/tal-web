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
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;

/**
 * Event handler when the shared/flash contact is updated.
 * Will update account name and id for search purposes.
 * 
 * @author Tom Spencer
 */
@Controller
public class AccountContactUpdatedController {

	@Action(result="accountLookupUpdated")
	public void contactUpdated(@ModelInput Map<String, Object> model) {
		Contact contact = (Contact)model.get("contact");
		model.put("accountId", contact != null ? contact.getAccount() : null);
		model.put("companyName", contact != null ? contact.getCompany() : null);
	}
}
