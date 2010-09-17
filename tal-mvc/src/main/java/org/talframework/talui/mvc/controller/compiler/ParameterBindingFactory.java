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

package org.talframework.talui.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.talframework.talui.mvc.controller.ParameterBinding;

/**
 * This interface represents a class that can create 
 * {@link ParameterBinding} instances of a given type. It
 * is used during construction to create the bindings.
 * 
 * @author Tom Spencer
 */
public interface ParameterBindingFactory {

	/**
	 * This method is called by the compiler when it has an parameter
	 * to bind into. The compiler has a range of these factories and 
	 * the first one to answer with a non-null return wins!
	 * 
	 * @param controller The controller
	 * @param controllerClass The class the method is on (may be a interface or subclass of controller)
	 * @param method The method we are finding bindings for
	 * @param parameter The expected type of the parameter
	 * @param annotations The annotation the parameter has
	 * @return
	 */
	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> parameter, Annotation[] annotations);
}
