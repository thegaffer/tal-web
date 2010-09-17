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

package org.talframework.talui.mvc.model;

import org.talframework.talui.mvc.Model;

/**
 * This interface represents something that is capable of 
 * resolving or getting a model attribute. A resolver is
 * given full access to the current model to get hold of
 * the attribute along with configuration attributes so
 * that the same resolver can be used for multiple
 * attributes.
 * 
 * <p>As with most interfaces inside the the MVC framework
 * there is no need to implement this interface, instead
 * well formed methods or annotations can be used. See the
 * online documentation for more information.</p> 
 * 
 * @author Tom Spencer
 */
public interface ModelResolver {

	/**
	 * Called to get hold of the model attribute specified in
	 * the attribute parameter.
	 * 
	 * @param model The model the attribute is part of
	 * @param name The name of the attribute
	 * @param param The parameter configured against the attribute
	 * @return The attribute
	 */
	public Object getModelAttribute(Model model, String name, Object param);
	
	/**
	 * This allows the resolver to determine if it is ok to nest
	 * access to this resolver when trying to determine another
	 * resolved attribute. Normally once in a nested resolver you
	 * cannot normally access another resolved attribute from the
	 * model. This helps to ensure we don't have a recursive 
	 * model attribute lookup and ensure we don't do multiple 
	 * potentially expensive operations. Returning true from this
	 * method breaks that protection so you should only do it
	 * with care.
	 * 
	 * @return True if it's ok to nest access to this resolver.
	 */
	public boolean canNestResolver();
}
