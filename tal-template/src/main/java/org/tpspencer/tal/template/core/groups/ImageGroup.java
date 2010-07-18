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

package org.tpspencer.tal.template.core.groups;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.supporting.ImageElement;

public class ImageGroup extends SimpleGroup implements ImageElement {
	
	private String resource = null;
	
	/**
	 * Simple returns the resource
	 */
	public String getImageResource(RenderModel model) {
		return resource;
	}

	@Override
	public String getType() {
		return "image-group";
	}
	
	@Override
	public String toString() {
		return "ImageGroup: name=" + getName() + ", res=" + resource;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
}
