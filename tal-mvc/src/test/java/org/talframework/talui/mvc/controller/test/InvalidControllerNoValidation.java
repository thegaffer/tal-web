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

import org.talframework.talui.mvc.controller.annotations.Action;
import org.talframework.talui.mvc.controller.annotations.Controller;
import org.talframework.talui.mvc.controller.annotations.Input;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This controller is invalid because it has no validation method
 * 
 * @author Tom Spencer
 */
@Controller
public interface InvalidControllerNoValidation {

	@Action(validationMethod="validateSmoke")
	public String someSmoke(@Input InputModel input);
	
	public String validateMirrors(InputModel input);
}
