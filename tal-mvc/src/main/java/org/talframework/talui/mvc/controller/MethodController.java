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

package org.talframework.talui.mvc.controller;

import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This controller will delegate to a single method on an
 * object that has the @When annotation.
 * 
 * @author Tom Spencer
 */
public class MethodController implements Controller {
	
	public final ControllerAction action;
	
	public MethodController(ControllerAction action) {
		this.action = action;
	}
	
	public String performAction(Model model, InputModel input) {
		return action.invokeAction(model, input);
	}
}
