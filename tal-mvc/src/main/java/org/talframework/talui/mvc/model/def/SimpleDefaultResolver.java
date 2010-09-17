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

package org.talframework.talui.mvc.model.def;

import java.lang.reflect.Proxy;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.controller.BindingException;
import org.talframework.talui.mvc.controller.InterfaceAdaptor;
import org.talframework.talui.mvc.model.DefaultModelResolver;

/**
 * This default resolver holds a type of the object to construct
 * and then constructs it when requested.
 * 
 * @author Tom Spencer
 */
public final class SimpleDefaultResolver implements DefaultModelResolver {
	
	/** The type of object to create */
	private Class<?> type;
	
	public SimpleDefaultResolver() {	
	}
	
	public SimpleDefaultResolver(Class<?> type) {
		this.type = type;
	}

	/**
	 * The embedded type
	 */
	public Class<?> getType() {
		return type;
	}
	
	/**
	 * @param type The new type to use
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	/**
	 * Constructs a new instance of the type
	 */
	public Object getDefault(Model model) {
		if( type == null ) return null;
		
		if( type.isInterface() ) {
			return Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader(), 
					new Class[]{type}, 
					new InterfaceAdaptor());
		}
		else {
			try {
				return type.newInstance();
			}
			catch( RuntimeException e ) {
				throw e;
			}
			catch( Exception e ) {
				throw new BindingException("Cannot create default value: " + type, e);
			}
		}
	}
}
