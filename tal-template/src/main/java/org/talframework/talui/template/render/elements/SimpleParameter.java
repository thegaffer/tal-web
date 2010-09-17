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

package org.talframework.talui.template.render.elements;

import org.talframework.talui.template.RenderModel;

/**
 * A simple parameter does not change at render time.
 * 
 * @author Tom Spencer
 */
public final class SimpleParameter implements RenderParameter {
	
	private final String value;
	
	/**
	 * Constructs a simple parameter with the given value
	 * 
	 * @param value The value to use at render time
	 */
	public SimpleParameter(String value) {
		this.value = value;
	}
	
	/**
	 * @return The value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Just returns the value unchanged
	 */
	public String getValue(RenderModel model) {
		return value;
	}
	
	@Override
	public String toString() {
		return "SimpleParameter: value=" + getValue();
	}
}
