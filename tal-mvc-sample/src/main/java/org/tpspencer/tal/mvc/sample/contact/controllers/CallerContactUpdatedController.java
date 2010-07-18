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

/**
 * Event handler when shared contact is updated. This
 * controller will update the first/last name search
 * params in the model.
 * 
 * @author Tom Spencer
 */
@Controller
public class CallerContactUpdatedController {

	@Action(result="callerLookupUpdated")
	public void contactUpdated(
			@BindInput(prefix="contact") Contact contact, 
			@ModelInput Map<String, Object> model) {
		model.put("firstName", contact != null ? contact.getFirstName() : null);
		model.put("lastName", contact != null ? contact.getLastName() : null);
	}
}
