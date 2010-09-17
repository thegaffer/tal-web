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

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This interface represents a class that knows how to bind 
 * an parameter, typically on an action method, from the
 * inputs it has available (i.e. what has been submitted by
 * the user and what is in the model).
 * 
 * @author Tom Spencer
 */
public interface ParameterBinding {

	/**
	 * Called at runtime to create a parameter based on the
	 * input.
	 * 
	 * @param model The current model
	 * @param input The input to the controller
	 * @return The parameter
	 */
	public Object bind(Model model, InputModel input);
}
