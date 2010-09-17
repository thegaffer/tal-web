/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.render.codes;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ResourceCodeTypeFactory implements CodeTypeFactory {
	
	/** The name of the code type */
	private final String name;
	/** Holds the resource base name */
	private final String resource;
	
	/**
	 * Simple constructor - tests the resource exists.
	 * 
	 * @param resource Constructs 
	 */
	public ResourceCodeTypeFactory(String name, String resource) {
		if( name == null ) throw new IllegalArgumentException("A name for the resource code type must be provided");
		// Test the resource
		try {
			ResourceBundle.getBundle(resource);
		}
		catch( MissingResourceException e ) {
			throw new IllegalArgumentException("The resource for the resource code lookup does not appear to exist in the default locale: " + e.getMessage());
		}
		
		this.name = name;
		this.resource = resource;
		
		CodeTypeFactoryLocator.getInstance().addCodeFactory(name, this);
	}
	
	/**
	 * Creates a new instance of the resource code type
	 */
	public CodeType getCodeType(Locale locale, Principal user, Map<String, String> params) {
		try {
			return new ResourceCodeType(name, ResourceBundle.getBundle(resource, locale));
		}
		catch( MissingResourceException e ) {
			// Should not happen because of constructor, but just in case.
			throw new IllegalArgumentException("The resource for the resource code lookup does not appear to exist in the default locale: " + e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "ResourceCodeTypeFactory name=" + name + ", res=" + resource;
	}
}
