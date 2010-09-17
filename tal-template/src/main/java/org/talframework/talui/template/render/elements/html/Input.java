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

package org.talframework.talui.template.render.elements.html;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.render.elements.DynamicParameter;
import org.talframework.talui.template.render.elements.RenderParameter;
import org.talframework.talui.template.render.elements.html.attributes.NameAttribute;
import org.talframework.talui.template.render.elements.html.attributes.SimpleAttribute;
import org.talframework.talui.util.htmlhelper.GenericElement;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

/**
 * This class renders out a html input field based on
 * a dynamic property
 * 
 * @author Tom Spencer
 */
public class Input extends AbstractHtmlElement {
	
	/** The optional dynamic value */
	private final RenderParameter value;
	
	public Input(DynamicProperty prop) {
		super(HtmlConstants.ELEM_INPUT, prop.getName());
		this.value = new DynamicParameter(prop, "getValue");
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, "text", false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, prop.getName()));
	}
	
	public Input(String type, DynamicProperty prop) {
		super(HtmlConstants.ELEM_INPUT, prop.getName());
		this.value = new DynamicParameter(prop, "getValue");
		if( type == null ) type = "text";
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, type, false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, prop.getName()));
	}
	
	public Input(String type, String name, RenderParameter value) {
		super(HtmlConstants.ELEM_INPUT, name);
		this.value = value;
		if( type == null ) type = "text";
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, type, false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, name));
	}

	/**
	 * Adds the name attribute on
	 */
	@Override
	protected void addAttributes(RenderModel model, GenericElement elem) {
		if( value != null ) elem.addAttribute(HtmlConstants.ATTR_VALUE, value.getValue(model), true);
		super.addAttributes(model, elem);
	}

	/**
	 * @return the dynamic value to use at render time
	 */
	public RenderParameter getValue() {
		return value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, name));
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, type, false));
	}
}
