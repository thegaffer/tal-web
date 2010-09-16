/*
 * Copyright 2005 Thomas Spencer
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

package org.tpspencer.tal.util.htmlhelper;

import java.util.Map;

/**
 * The idea of the attribute adaptor interface is it allows a client of 
 * the Html Helper library to adapt attribute values within the context 
 * of its environment. Typically this is to prefix, suffix or otherwise
 * change an attribute.
 * 
 * @author Tom Spencer
 */
public interface AttributeAdaptor {
	
	/**
	 * Called by generic element when it has an id element
	 * to output. The adaptor can then add in any known prefix
	 * to the ID to make it unique. Typically this may be
	 * a namespace and if we are walking through a model of
	 * objects the name of the current object.
	 * 
	 * @param in The raw id value
	 * @return The adapted ID value to use
	 */
	public String adaptId(String in);
	
	/**
	 * Called by generic element when it has a name element
	 * to output. The adaptor can add in any known prefix, such
	 * as where we are in a model of objects.
	 * 
	 * @param name The raw name attribute
	 * @return The adapted name value to use
	 */
	public String adaptName(String name);
	
	/**
	 * Called by the generic element which is has a function
	 * to output. Typically the name of the function is prefixed
	 * by any namespace that exists.
	 * 
	 * @param func The raw function (does not include the 'return')
	 * @return The adapted function
	 */
	public String adaptFunction(String func);
	
	/**
	 * Called by the generic element when it has a resource
	 * to output. The adaptor should turn this into a string
	 * that will point to the resource. If we are talking about
	 * the servlet world, then this should be turned into a
	 * complete url.
	 * 
	 * @param res The raw resource string
	 * @return The adapted function
	 */
	public String adaptResource(String res);
	
	/**
	 * Called by the generic element when it has an action to
	 * output. This method should turn that into a string that
	 * points to that action. If we are talking about the
	 * servlet world then this should be a full url to that 
	 * action and the supplied parameters.
	 * 
	 * @param action The action
	 * @param params The parameters
	 * @return A url that contains the action url in its path
	 */
	public String adaptActionUrl(String action, Map<String, String> params);
}
