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

package org.tpspencer.tal.template.render.codes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ResourceCodeType implements CodeType {
	
	/** Holds the type of the code type */
	private final String name;
	/** Holds the resource base name */
	private final ResourceBundle bundle;
	
	/**
	 * Simple constructor - tests the resource exists.
	 * 
	 * @param resource Constructs 
	 */
	public ResourceCodeType(String type, ResourceBundle bundle) {
		this.name = type;
		this.bundle = bundle;
	}
	
	/**
	 * Just returns the name of the code type
	 */
	public String getType() {
		return name;
	}
	
	/**
	 * Gets the keys and returns them.
	 */
	public String[] getCodes() {
		try {
			List<String> ret = new ArrayList<String>();
			Enumeration<String> e = bundle.getKeys();
			while( e.hasMoreElements() ) {
				ret.add(e.nextElement());
			}
			return ret.toArray(new String[ret.size()]);
		}
		catch( MissingResourceException e ) {
			// Can't happen, see constructor
			return null;
		}
	}
	
	/**
	 * Gets the code form the resource bundle. Returns code if this
	 * fails.
	 */
	public String getCodeDescription(String code) {
		if( code == null ) return null;
		
		try {
			return bundle.getString(code);
		}
		catch( MissingResourceException e ) {	
			return code;
		}
	}
	
	@Override
	public String toString() {
		return "ResourceCodeType name=" + name + ", bundle=" + bundle.toString();
	}
}
