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

package org.tpspencer.tal.template;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an abstract template that we want
 * to render. Templates are run through compilers that produce
 * a renderer to be used at run time. The same template may run
 * through several different compilers to produce different
 * renderers. There may be one for HTML, another for Javascript,
 * another for XML, etc, etc. Even within the same output the same
 * template may be compiled more than once - for instance in HTML
 * a template may be rendered into both a HTML Form and a HTML
 * table.
 * 
 * <p>Templates consist of properties, but further these properties
 * may be organised into groups. These are collectively refered
 * to as template elements. These templates typically exhibit 
 * various behaviour that allows the compilers to deal with them
 * appropriately. Behaviours can be added in, but the inbuilt 
 * behaviours include whether the element contains other elements,
 * whether it represents a dynamic property or whether it represents
 * a member property that in turn includes another template.</p>
 * 
 * @sa TemplateElement
 * @sa Compiler
 * @sa Renderer 
 * @author Tom Spencer
 */
public interface Template {

	/**
	 * @return The name of the element
	 */
	public String getName();
	
	/**
	 * Although not mandatory many templates represent a
	 * particular class of object/bean.
	 * 
	 * @return The class of object (bean) the template is for (or null)
	 */
	public Class<?> getTemplateClass();
	
	/**
	 * This method is called after constructing a template
	 * to allow the template to initialise itself. For
	 * instance of the template represents a class of 
	 * object and the implementing class automatically adds
	 * in properties of that class then it will be done in
	 * the bean method.
	 * 
	 * @param children The child template elements
	 */
	public void init(List<TemplateElement> children);
	
	/**
	 * Template elements support optional sets of properties
	 * that can be supplied to specific render elements. This
	 * method provides access to them.
	 * 
	 * @param name The name of the property set (prefix to the attribute)
	 * @return The set of custom properties for the render element to use
	 */
	public Map<String, String> getPropertySet(String name);
	
	/**
	 * @return The (ordered) list of top-level elements inside the template
	 */
	public List<TemplateElement> getElements();
}
