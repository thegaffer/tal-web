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

package org.talframework.talui.mvc.controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.talframework.tal.aspects.annotations.TraceWarn;

/**
 * This class is used to create bind objects to an interface
 * where the bind class is not specified
 * 
 * FUTURE: Could work out and cache all methods up front
 * 
 * @author Tom Spencer
 */
public class InterfaceAdaptor implements InvocationHandler {

	/** Member holds the properties of the interface */
	private Map<String, Object> props = null;
	
	/**
	 * Handles if the method is a simple get, set or is.
	 * Does not currently handle indexed getter/setter
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		
		Object ret = null;
		if( name.startsWith("get") ) {
			name = name.substring(3);
			if( props != null ) ret = props.get(name);
			if( ret == null ) ret = getDefaultReturn(method.getReturnType());
		}
		else if( name.startsWith("is") ) {
			name = name.substring(2);
			if( props != null ) ret = props.get(name);
			if( ret == null ) ret = false;
		}
		else if( name.startsWith("set") ) {
			name = name.substring(3);
			Object val = args.length > 0 ? args[0] : null;
			if( val != null ) {
				if( props == null ) props = new HashMap<String, Object>();
				props.put(name, val);
			}
			else if( props != null ) {
				props.remove(name);
			}
		}
		else if( name.equals("toString") ) {
			return props != null ? props.toString() : "Empty Proxy";
		}
		else if( name.equals("hashCode") ) {
			return props != null ? props.hashCode() : 30;
		}
		else if( name.equals("equals") ) {
			return props != null ? props.equals(args[0]) : false;
		}
		else {
		    cannotProcessMethod(method);
			return null;
		}
		
		return ret;
	}
	
	/**
	 * Helper to get the default return which is null unless the
	 * return type is a primitive
	 * 
	 * @param returnType The return type
	 * @return The value to return by default
	 */
	private Object getDefaultReturn(Class<?> returnType) {
		Object ret = null;
		
		// Default primitives
		if( returnType.isPrimitive() ) {
			if( returnType.equals(Boolean.TYPE) ) ret = false;
			if( returnType.equals(Long.TYPE) ) ret = (long)0;
			if( returnType.equals(Integer.TYPE) ) ret = 0;
			if( returnType.equals(Double.TYPE) ) ret = 0.0;
			if( returnType.equals(Character.TYPE) ) ret = (char)0;
			if( returnType.equals(Short.TYPE) ) ret = (short)0;
			if( returnType.equals(Byte.TYPE) ) ret = (byte)0;
			if( returnType.equals(Float.TYPE) ) ret = 0.0f;
		}
		
		return ret;
	}

	/**
	 * Method is present to call so the aspect loggers can log it.
	 */
	@TraceWarn
	private void cannotProcessMethod(Method method) {
	}
}
