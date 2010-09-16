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

package org.tpspencer.tal.mvc.model;

public class NestedResolvedAttributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/** Member holds the attribute */
	private ModelAttribute attribute = null;
	
	/**
	 * Constructs an instance with the ModelAttribute we are
	 * trying to get through a resolver within another model
	 * attribute currently being resolved.
	 * 
	 * @param name The attribute
	 */
	public NestedResolvedAttributeException(ModelAttribute attribute) {
		this.attribute = attribute;
	}
	
	/**
	 * Returns a message including the name of the attribute
	 */
	public String getMessage() {
		return "The resolved attribute [" + attribute.getName() + "] cannot be obtained when resolving another attribute";
	}
}
