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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This simple object represents the input from a user
 * (directly or indirectly) into a request. Typically 
 * this is the request parameters, but they are placed
 * inside this class to provide easier access.
 * 
 * @author Tom Spencer
 */
public final class WebInputModel implements InputModel {

	/** Member holds the input parameters */
	private final Map<String, String[]> parameters;
	
	/**
	 * Constructs an InputModel given the parameters
	 * 
	 * @param parameters The input parameters
	 */
	public WebInputModel(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Returns the parameter if it has a value, otherwise
	 * null. Note: We always return empty strings as null
	 */
	public String getParameter(String name) {
		if( parameters == null ) return null;
		
		String ret = null;
		
		String[] p = parameters.get(name);
		if( p != null && p.length > 0 ) ret = p[0];
		return (ret != null && ret.length() > 0) ? ret : null;
	}
	
	/**
	 * Returns the parameter as getParameter(name)
	 */
	public Object getParameterObject(String name) {
		return getParameter(name);
	}
	
	/**
	 * Returns the native value for a parameter as a
	 * string array
	 */
	public String[] getParameterValues(String name) {
		if( parameters == null ) return null;
		return parameters.get(name);
	}
	
	/**
	 * Returns the parameters
	 */
	public Map<String, String[]> getParameters() {
		return parameters;
	}
	
	/**
	 * Manually goes through the input and determines all
	 * those that begin with prefix. As with getParameter
	 * any empty strings are nulled.
	 * 
	 * Note: I am not sure about the performance of this.
	 */
	public Map<String, ?> getParameters(String prefix) {
		if( parameters == null ) return null;
		if( prefix == null || prefix.length() == 0 ) return getParameters();
		
		Map<String, String[]> ret = null;
		prefix = prefix + ".";
		int ln = prefix.length();
		Iterator<String> it = parameters.keySet().iterator();
		while( it.hasNext() ) {
			String k = it.next();
			if( k.startsWith(prefix) ) {
				String[] val = parameters.get(k);
				if( val != null && val.length == 1 && val[0].length() == 0 ) val = null;
				if( val != null ) {
					if( ret == null ) ret = new HashMap<String, String[]>();
					ret.put(k.substring(ln), val);
				}
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
		String[] p = parameters.get(name);
		if( p != null && p.length > 1 ) return true;
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("WebInputModel [params={");
		if( parameters != null ) {
			Iterator<String> it = parameters.keySet().iterator();
			boolean first = true;
			while( it.hasNext() ) {
				String k = it.next();
				String[] v = parameters.get(k);
				if( v != null && v.length > 1 ) {
					if( !first ) buf.append(", "); first = false;
					buf.append(k).append("={");
					for( int i = 0 ; i < v.length ; i++ ) {
						if( i > 0 ) buf.append(", ");
						buf.append(v[i]);
					}
					buf.append('}');
				}
				else if( v != null ) {
					if( !first ) buf.append(", "); first = false;
					buf.append(k);
					if( v.length > 0 ) {
						buf.append('=').append(v[0]);
					}
				}
			}
		}
		buf.append("}]");
		
		return buf.toString();
	}
}
