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
import org.talframework.talui.mvc.controller.annotations.Input;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This factory handles the input binding
 * 
 * @author Tom Spencer
 */
public final class InputParameterFactory extends BaseParameterFactory implements ParameterBindingFactory {
	
	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> parameter, Annotation[] annotations) {
		Input input = getAnnotation(annotations, Input.class);
		if( input == null ) return null;
		
		if( getValue(input.param()) != null ) {
			return new InputParameterBinding(getValue(input.param()), parameter.isArray());
		}
		else if( getValue(input.prefix()) != null ) {
			testExpected(method, parameter, Map.class);
			return new InputBinding(getValue(input.param()), true);
		}
		else {
			if( testAssignable(method , parameter, InputModel.class) ) return new InputBinding(getValue(input.param()), false); 
			else {
				testExpected(method, parameter, Map.class);
				return new InputBinding(getValue(input.param()), true);
			}
		}
	}

	/**
	 * Parameter binding simply gets the parameter from the
	 * input. Only processing is to return it as an array or
	 * as a single scalar value.
	 * 
	 * @author Tom Spencer
	 */
	public final static class InputParameterBinding implements ParameterBinding {
		
		private final String parameter;
		private final boolean asArray;
		
		public InputParameterBinding(String parameter, boolean asArray) {
			this.parameter = parameter;
			this.asArray = asArray;
		}
		
		public Object bind(Model model, InputModel input) {
			Object obj = input.getParameterObject(parameter);
			if( !asArray && obj instanceof String[] && ((String[])obj).length > 0 ) {
				obj = ((String[])obj)[0];
			}
			return obj;
		}
	}
	
	/**
	 * Parameter binding returns the input directly or will
	 * get a map of all parameters starting with a given prefix.
	 * 
	 * @author Tom Spencer
	 */
	public final static class InputBinding implements ParameterBinding {
		
		private final boolean asMap;
		private final String prefix;
		
		public InputBinding(String prefix, boolean asMap) {
			this.prefix = prefix;
			this.asMap = asMap;
		}
		
		public Object bind(Model model, InputModel input) {
			if( prefix == null ) {
				if( asMap ) return input.getParameters();
				else return input;
			}
			else {
				return input.getParameters(prefix);
			}
		}
	}
}
