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

package org.talframework.talui.mvc.model;

/**
 * This runtime exception is thrown when an attempt is made
 * to access an attribute for which there is no definition.
 * 
 * @author Tom Spencer
 */
public class UnsupportedModelAttributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/** Member holds the name of the attribute */
	private String name = null;
	
	/**
	 * Constructs an instance with the name of the illegal
	 * attribute referenced.
	 * 
	 * @param name The attribute
	 */
	public UnsupportedModelAttributeException(String name) {
		this.name = name;
	}
	
	/**
	 * Returns a message including the name of the attribute
	 */
	public String getMessage() {
		return "The attribute [" + name + "] is not supported in any model configuration";
	}
}
