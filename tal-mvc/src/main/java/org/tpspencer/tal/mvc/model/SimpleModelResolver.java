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

package org.tpspencer.tal.mvc.model;


/**
 * This interface represents something that is capable of 
 * resolving or getting a model attribute. Unlike the full
 * ModelResolver interface this version is not given the
 * model when resolving its value only a configuration
 * parameter.
 * 
 * <p>As with most interfaces inside the the MVC framework
 * there is no need to implement this interface, instead
 * well formed methods or annotations can be used. See the
 * online documentation for more information.</p> 
 * 
 * @author Tom Spencer
 */
public interface SimpleModelResolver {

	/**
	 * Called to get hold of the model attribute specified in
	 * the attribute parameter.
	 * 
	 * @param name The name of the attribute required
	 * @param param The parameter for the attribute
	 * @return The attributes value
	 */
	public Object getModelAttribute(String name, Object param);
	
}
