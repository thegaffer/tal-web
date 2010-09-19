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

import java.lang.reflect.Proxy;

import org.talframework.tal.aspects.annotations.Trace;

/**
 * This class creates a new instance of the given bean or if
 * it is an interface, creates a proxy around it.
 *
 * @author Tom Spencer
 */
public class ObjectCreator {
	
	@Trace
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
		    throw new IllegalArgumentException("Cannot create object [" + expected + "] due to access exception", e);
		}
		catch( InstantiationException e ) {
		    throw new IllegalArgumentException("Cannot create object [" + expected + "] due to instantiation exception", e);
		}
	}
}
