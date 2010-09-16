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

package org.tpspencer.tal.template.render.elements.html;

import org.tpspencer.tal.template.render.elements.html.attributes.ActionAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.NameAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.SimpleAttribute;
import org.tpspencer.tal.util.htmlhelper.HtmlConstants;

public class Form extends AbstractHtmlElement {

	public Form(String name, String action) {
		super(HtmlConstants.ELEM_FORM);
		setId(name);
		setName(name);
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_METHOD, "POST", false));
		setAction(action);
	}
	
	/**
	 * Sets the name attribute (the ID is left as originally
	 * specified in the constructor.
	 * 
	 * @param name The 
	 */
	public void setName(String name) {
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, name));
	}
	
	/**
	 * Sets the name attribute (the ID is left as originally
	 * specified in the constructor.
	 * 
	 * @param action The action
	 */
	public void setAction(String action) {
		addAttribute(new ActionAttribute(HtmlConstants.ATTR_ACTION, action, null));
	}
}
