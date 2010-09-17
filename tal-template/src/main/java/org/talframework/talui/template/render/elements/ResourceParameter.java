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
 * This class implements the render parameter interface
 * to resolve a parameter using the resource bundle
 * associated with the render.
 * 
 * @author Tom Spencer
 */
public final class ResourceParameter implements RenderParameter {

	private final String resourceKey;
	private final String defaultKey;
	
	/**
	 * Constructs a ResourceParameter with a key. The key is
	 * also the default if the resource is not available.
	 * 
	 * @param key
	 */
	public ResourceParameter(String key) {
		this.resourceKey = key;
		this.defaultKey = key;
	}
	
	/**
	 * Constructs a ResourceParameter with key and default
	 * value
	 * 
	 * @param key
	 * @param def
	 */
	public ResourceParameter(String key, String def) {
		this.resourceKey = key;
		this.defaultKey = def;
	}
	
	/**
	 * Converts the resource to a locale specific message
	 */
	public String getValue(RenderModel model) {
		return model.getMessage(resourceKey, defaultKey);
	}
	
	/**
	 * @return The resource to resolve
	 */
	public String getResourceKey() {
		return resourceKey;
	}
	
	/**
	 * @return The default value to use if resource is not defined
	 */
	public String getDefaultKey() {
		return defaultKey;
	}
	
	@Override
	public String toString() {
		return "ResourceParameter: res=" + getResourceKey();
	}
}
