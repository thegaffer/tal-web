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

package org.tpspencer.tal.mvc.controller;

import java.util.List;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This interface represents something that is able
 * to convert an InputModel into an object. Binding
 * an object from input is typically done through a
 * number of frameworks, such as BeanUtils from
 * Apache or the Spring DataBinder. However, Web MVC
 * makes no use of these frameworks so it defines
 * this interface to abstract the binding 
 * infrastructure. 
 * 
 * <p>The web-mvc-spring library does contain an 
 * implementation of this interface using the spring 
 * utilities.</p>
 * 
 * @author Tom Spencer
 */
public interface InputBinder {
	
	/**
	 * Determines if the binder supports the given type
	 * 
	 * @param expected The type to test
	 * @return True the binder will convert, false otherwise
	 */
	public boolean supports(Class<?> expected);
	
	/**
	 * Call to bind the input into the given object. Although
	 * the bind object will not be null any objects it creates
	 * may be.
	 * 
	 * @param model The model
	 * @param input The input
	 * @param prefix The name of the object to bind
	 * @param obj The object to bind into (sub-objects within here may be null)
	 * @return Any errors that occurred (null if all is ok)
	 */
	public List<Object> bind(Model model, InputModel input, String prefix, Object obj);
}
