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

package org.talframework.talui.mvc.controller.test;

import java.util.List;
import java.util.Map;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.controller.annotations.Action;
import org.talframework.talui.mvc.controller.annotations.Controller;
import org.talframework.talui.mvc.controller.annotations.Input;
import org.talframework.talui.mvc.controller.annotations.ModelInput;
import org.talframework.talui.mvc.input.InputModel;

/**
 * A multi-action controller without binding
 * 
 * @author Tom Spencer
 */
@Controller(subActionParameter="subAction")
public interface ComplexController {

	@Action
	public String defaultAction(@Input InputModel input);
	
	@Action(action="another", validationMethod="validateMethod")
	public String anotherAction(@ModelInput Model model, @Input InputModel input);
	
	@Action(action="alt", result="success", errorResult="fail")
	public void alternativeAction(@ModelInput Map<String, Object> model, @Input Map<String, ?> input);
	
	public List<Object> validateMethod(Model model, InputModel input);
}
