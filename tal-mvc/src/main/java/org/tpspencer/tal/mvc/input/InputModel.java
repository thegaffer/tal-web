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

package org.tpspencer.tal.mvc.input;

import java.util.Map;


/**
 * This simple object represents the input from a user
 * (directly or indirectly) into a request. Typically 
 * this is the request parameters, but they are placed
 * inside this class to provide easier access.
 * 
 * @author Tom Spencer
 */
public interface InputModel {

	/**
	 * Call to get a parameter as a string. This mimics
	 * request.getParameter in a web model. If the parameter
	 * is not a string, but exists it is converted to a
	 * string by the implementation of this interface (the
	 * simplest mechanism here is toString()). 
	 * 
	 * @param name The name of the parameter
	 * @return The parameters value or its first value if its a String[]
	 */
	public String getParameter(String name);
	
	/**
	 * Call to get a parameter in its natural form. In
	 * the input is coming direct from a web request then
	 * the return type will be a String[], otherwise its
	 * natural type.
	 * 
	 * @param name The name of the parameter
	 * @return The parameters value
	 */
	public Object getParameterObject(String name);
	
	/**
	 * This method is an alternative for getParameterObject
	 * when dealing with a request from a browser. It will
	 * throw an unsupported exception otherwise. This method
	 * is equivilent to request.getParamterValues(param).
	 * 
	 * @param name The name of the parameter
	 * @return The values of the parameter (may be null)
	 */
	public String[] getParameterValues(String name);
	
	/**
	 * Call to get all the input parameters in a map. 
	 * These are returned as a map of whatever the
	 * attributes are. In a web request these will
	 * always be String[] instances.
	 * 
	 * @return The input parameters in a map
	 */
	public Map<String, ?> getParameters();
	
	/**
	 * Call to get all input parameters that begin with
	 * the given prefix into the a new map.
	 * 
	 * @param prefix The prefix
	 * @return The parameters with that prefix
	 */
	public Map<String, ?> getParameters(String prefix);
	
	/**
	 * Helper method to determine if the given parameter
	 * is present
	 * 
	 * @param name The name of the parameter
	 * @return True if it exists, false otherwise
	 */
	public boolean hasParameter(String name);
	
	/**
	 * Helper method to determine if a given parameter
	 * has multi values.
	 * 
	 * @param name The name of the parameter
	 * @return True if the parameter has more than one value
	 */
	public boolean hasMultiValue(String name);
}
