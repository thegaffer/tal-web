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

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.controller.InputBinder;
import org.talframework.talui.mvc.input.InputModel;

/**
 * A stub for the input binder. This has to be a stub as it is
 * created by the GenericController based on its class name.
 * 
 * @author Tom Spencer
 */
public class InputBinderStub implements InputBinder {
	
	public boolean supports(Class<?> expected) {
		return true;
	}
	
	/**
	 * Does nothing!
	 */
	public List<Object> bind(Model model, InputModel input, String prefix,
			Object obj) {
		return null;
	}
}
