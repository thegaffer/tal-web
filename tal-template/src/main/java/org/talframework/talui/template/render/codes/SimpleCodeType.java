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

import java.util.Map;

/**
 * The simplest form of code type is a map of codes to descriptions.
 * 
 * @author Tom Spencer
 */
public final class SimpleCodeType implements CodeType {

	/** The name of the code type */
	private final String name;
	/** The codes and their descriptions */
	private final Map<String, String> codes;
	
	/**
	 * Simple constructor
	 * 
	 * @param type The name of the code type
	 * @param codes The codes
	 */
	public SimpleCodeType(String type, Map<String, String> codes) {
		this.name = type;
		this.codes = codes;
	}
	
	/**
	 * Returns the configured type
	 */
	public String getType() {
		return name;
	}
	
	/**
	 * Returns back all codes.
	 */
	public String[] getCodes() {
		return codes.keySet().toArray(new String[codes.size()]);
	}
	
	/**
	 * Returns the description if held or the code
	 */
	public String getCodeDescription(String code) {
		if( code == null ) return null;
		if( codes.containsKey(code) ) return codes.get(code);
		else return code;
	}
	
	@Override
	public String toString() {
		return "SimpleCodeType: name=" + name + ", codes=" + codes.keySet();
	}
}
