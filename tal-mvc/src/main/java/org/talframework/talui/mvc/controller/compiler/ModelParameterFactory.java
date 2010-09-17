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
import java.util.Map;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.controller.ParameterBinding;
import org.talframework.talui.mvc.controller.annotations.ErrorInput;
import org.talframework.talui.mvc.controller.annotations.ModelInput;
import org.talframework.talui.mvc.input.InputModel;

public final class ModelParameterFactory extends BaseParameterFactory implements ParameterBindingFactory {

	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> expected, Annotation[] annotations) {
		ModelInput annotation = getAnnotation(annotations, ModelInput.class);
		if( annotation != null ) {
			String attribute = getValue(annotation.value());
			if( attribute == null ) testAssignable(method, expected, Map.class);
		
			return new ModelBinding(attribute);
		}
		
		ErrorInput error = getAnnotation(annotations, ErrorInput.class);
		if( error != null ) {
			String errorAttribute = getErrorAttribute(controllerClass);
			return new ModelBinding(errorAttribute);
		}
		
		return null;
	}
	
	/**
	 * Inner class is the actual model binding class.
	 * 
	 * @author Tom Spencer
	 */
	public final static class ModelBinding implements ParameterBinding {
		
		/** Holds the attribute in model (if any) to bind to */
		private final String attribute;
		
		public ModelBinding(String attribute) {
			this.attribute = attribute;
		}
		
		public Object bind(Model model, InputModel input) {
			if( attribute != null ) return model.getAttribute(attribute);
			else return model;
		}
	}
}
