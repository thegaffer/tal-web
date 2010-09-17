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
import org.talframework.talui.template.render.elements.RenderParameter;
import org.talframework.talui.util.htmlhelper.GenericElement;

/**
 * This class implements the HtmlAttribute interface to generate
 * the output as a render parameter
 * 
 * @author Tom Spencer
 */
public class RenderParameterAttribute implements HtmlAttribute {

	/** The name of the attribute */
	private final String name;
	/** The render parameter */
	private final RenderParameter parameter;
	
	public RenderParameterAttribute(String name, RenderParameter parameter) {
		this.name = name;
		this.parameter = parameter;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAttribute(RenderModel model, GenericElement elem) {
		elem.addAttribute(name, parameter.getValue(model), false);
	}
}
