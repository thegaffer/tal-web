/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.render.elements;

import java.lang.reflect.Method;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.TemplateElement;

/**
 * A dynamic parameter is one which is obtained
 * from the template element at render time supplying
 * a single parameter, that of the render model.
 * 
 * @author Tom Spencer
 */
public final class DynamicParameter implements RenderParameter {

	public final TemplateElement element;
	public final Method method;
	
	/**
	 * Constructs a dynamic parameter to call the given
	 * method name on the given template element. 
	 * 
	 * @param element The element to invoke
	 * @param method The method to invoke
	 */
	public DynamicParameter(TemplateElement element, String method) {
		this.element = element;
		try {
			this.method = element.getClass().getMethod(method, RenderModel.class);
		}
		catch( NoSuchMethodException e ) {
			throw new IllegalArgumentException("There is no such method on the template element to call at render time - method must take a single parameter of a Model");
		}
	}
	
	/**
	 * Simply invokes the method determined in the constructor
	 */
	public String getValue(RenderModel model) {
		Object ret = null;
		try {
			ret = method.invoke(element, model);
		}
		catch( Exception e) {
			// FUTURE: This should be a custom exception
			throw new IllegalArgumentException("The dynamic attribute cannot be resolved at render time: " + e);
		}
		return ret != null ? ret.toString() : null;
	}
	
	/**
	 * @return The element that is called at render time
	 */
	public TemplateElement getElement() {
		return element;
	}
	
	/**
	 * @return The method that is called at render time
	 */
	public Method getMethod() {
		return method;
	}
	
	@Override
	public String toString() {
		return "DynamicParameter: method=" + getMethod().getName() + "element=" + getElement();
	}
}
