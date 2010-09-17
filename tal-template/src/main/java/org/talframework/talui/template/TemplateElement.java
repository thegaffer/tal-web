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

package org.talframework.talui.template;

import java.util.List;
import java.util.Map;

/**
 * This interface represents a part of the original template.
 * These elements are one of 3 things:
 * 
 * <p>A template - which is a set of properties and property
 * groups. Often a template is based on a particular object
 * class.</p>
 * 
 * <p>A template group - which is a set of properties and 
 * other groups that are connected to one another when 
 * rendered.</p>
 * 
 * <p>A template property - something that represents a 
 * field or similar inside the template. Template properties
 * will generate a dynamic value at render time.</p>
 * 
 * @author Tom Spencer
 */
public interface TemplateElement {

	/**
	 * @return The name of the element
	 */
	public String getName();
	
	/**
	 * Call to obtain the type of element. This string tells the 
	 * compiler how to deal with this element. There are built-in
	 * types, but new ones can be created.
	 * 
	 * @return The type of the element
	 */
	public String getType();
	
	/**
	 * Called to get the behaviour. Although this class will typically
	 * also dynamically implement this interface the compilers use
	 * this method to get a behaviour in case the behaviour is only
	 * exhibited when certain settings are made.
	 * 
	 * @param behaviour The behaviour to get
	 * @return This instance as the given behaviour if it is supported
	 */
	public <T> T getBehaviour(Class<T> behaviour);
	
	/**
	 * This method is called after constructing a template
	 * to allow the template to initialise itself. This 
	 * allows the element class to initialise any auto
	 * properties.
	 * 
	 * @param template The template the element belongs to
	 * @param children The child template elements
	 */
	public void init(Template template, List<TemplateElement> children);
	
	/**
	 * Template elements support optional sets of properties
	 * that can be supplied to specific render elements. This
	 * method provides access to them.
	 * 
	 * @param name The name of the property set
	 * @return The set of custom properties for the render element to use
	 */
	public Map<String, String> getPropertySet(String name);
	
	/**
	 * Allows a client to test for the existence of a setting
	 * (see getSetting). This method does not throw an 
	 * exception.
	 * 
	 * @param name The name of the setting to test
	 * @return True if it is a valid setting (doesn't mean it's set though!)
	 */
	public boolean hasSetting(String name);
	
	/**
	 * Templated method that allows a mold to get a setting
	 * of the template element. A setting is typically something
	 * that is a member property of the actual class 
	 * implementing this interface. By dynamically providing
	 * them through settings we get a bit more re-use from the
	 * molds and ensure that we do not create behaviour 
	 * interfaces for specific template elements. 
	 * 
	 * @param <T>
	 * @param name The name of the setting
	 * @param expected The expected type of the setting
	 * @return The value of the setting
	 * @throws ClassCaseException Cannot convert value to expected type
	 * @throws IllegalArgumentException The name of the setting is invalid
	 */
	public <T> T getSetting(String name, Class<T> expected);
}
