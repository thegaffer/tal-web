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
 * This attributes wraps any other other attribute and applies
 * a condition at render time which must evaluate to true to
 * proceed.
 * 
 * @author Tom Spencer
 */
public class ConditionalAttribute implements HtmlAttribute {
	private final String condition;
	private final HtmlAttribute attr;

	public ConditionalAttribute(String condition, HtmlAttribute attr) {
		this.condition = condition;
		this.attr = attr;
	}
	
	public String getName() {
		return attr.getName();
	}
	
	public void addAttribute(RenderModel model, GenericElement elem) {
		if( model.evaluateExpression(condition, Boolean.class).booleanValue() ) {
			attr.addAttribute(model, elem);
		}
	}
}
