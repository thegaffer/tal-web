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

package org.talframework.talui.template.render.elements.html.attributes;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.util.htmlhelper.GenericElement;

/**
 * Resolves and then writes out a label attribute
 * 
 * @author Tom Spencer
 */
public class ResourceAttribute implements HtmlAttribute {
	private String name = null;
	private String val = null;
	
	public ResourceAttribute(String name, String val) {
		this.name = name;
		this.val = val;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAttribute(RenderModel model, GenericElement elem) {
		elem.addResourceAttribute(name, val);
	}
}
