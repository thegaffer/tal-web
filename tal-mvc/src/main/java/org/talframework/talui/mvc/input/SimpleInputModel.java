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

package org.talframework.talui.mvc.input;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a simple input model with a
 * map of objects as the parameters
 * 
 * @author Tom Spencer
 */
public final class SimpleInputModel implements InputModel {

	/** Member holds the input parameters */
	private final Map<String, Object> parameters;
	
	/**
	 * Constructs an InputModel given the parameters
	 * 
	 * @param parameters The input parameters
	 */
	public SimpleInputModel(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Returns the parameter if it has a value, otherwise
	 * null. Note: We always return empty strings as null
	 */
	public String getParameter(String name) {
		if( parameters == null ) return null;
		
		Object p = parameters.get(name);
		if( p == null ) return null;
		if( p instanceof Object[] ) {
			Object[] arr = (Object[])p;
			if( arr.length > 0 && arr[0] != null ) return arr[0].toString();
			else return null;
		}
		else return p.toString();
	}
	
	/**
	 * Returns the parameter as getParameter(name)
	 */
	public Object getParameterObject(String name) {
		return parameters != null ? parameters.get(name) : null;
	}
	
	/**
	 * Returns the native value for a parameter as a
	 * string array
	 */
	public String[] getParameterValues(String name) {
		throw new UnsupportedOperationException("Get parameter values not supported in a simple input model");
	}
	
	/**
	 * Returns the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	/**
	 * Manually goes through the input and determines.
	 * Note: I am not sure about the performance of this.
	 */
	public Map<String, ?> getParameters(String prefix) {
		if( parameters == null ) return null;
		if( prefix == null || prefix.length() == 0 ) return getParameters();
		
		Map<String, Object> ret = null;
		prefix = prefix + ".";
		int ln = prefix.length();
		Iterator<String> it = parameters.keySet().iterator();
		while( it.hasNext() ) {
			String k = it.next();
			if( k.startsWith(prefix) ) {
				if( ret == null ) ret = new HashMap<String, Object>();
				ret.put(k.substring(ln), parameters.get(k));
			}
		}
		
		return ret;
	}
	
	/**
	 * Helper method to determine if the given parameter
	 * is present
	 * 
	 * @param name The name of the parameter
	 * @return True if it exists, false otherwise
	 */
	public boolean hasParameter(String name) {
		if( parameters == null ) return false;
		return parameters.containsKey(name);
	}
	
	/**
	 * Helper method to determine if a given parameter
	 * has multi values.
	 * 
	 * @param name The name of the parameter
	 * @return True if the parameter has more than one value
	 */
	public boolean hasMultiValue(String name) {
		if( parameters == null ) return false;
		Object p = parameters.get(name);
		if( p != null && p instanceof Object[] && ((Object[])p).length > 1 ) return true;
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleInputModel [params=" + parameters + "]";
	}
}
