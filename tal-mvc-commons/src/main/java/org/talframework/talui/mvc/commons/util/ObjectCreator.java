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

package org.talframework.talui.mvc.commons.util;

import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.mvc.controller.InterfaceAdaptor;

public class ObjectCreator {
	private final static Log logger = LogFactory.getLog(ObjectCreator.class);

	public static <T> T createObject(Class<T> expected) {
		try {
			if( expected.isInterface() ) {
				return expected.cast(Proxy.newProxyInstance(
						Thread.currentThread().getContextClassLoader(), 
						new Class[]{expected}, 
						new InterfaceAdaptor()));
			}
			else {
				return expected.newInstance();
			}
		}
		catch( IllegalAccessException e ) {
			if( logger.isDebugEnabled() ) logger.debug("!!! Failed to create object due to access error: " + e.getMessage());
			if( logger.isDebugEnabled() ) e.printStackTrace();
		}
		catch( InstantiationException e ) {
			if( logger.isDebugEnabled() ) logger.debug("!!! Failed to create object due to instantiation error: " + e.getMessage());
			if( logger.isDebugEnabled() ) e.printStackTrace();
		}
		
		return null;
	}
}
