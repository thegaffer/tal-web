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

import java.io.IOException;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.render.elements.html.attributes.NameAttribute;
import org.talframework.talui.template.render.elements.html.attributes.SimpleAttribute;
import org.talframework.talui.util.htmlhelper.GenericElement;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

public class ButtonInput extends AbstractHtmlElement {
	
	/** Holds the property we should test for true to set checked attribute */
	private DynamicProperty prop = null;
	/** The resource holding label of button */
	private String label = null;

	/**
	 * Creates a simple submit button with the given name.
	 * The value of the button will be its raw name.
	 */
	public ButtonInput(String name) {
		super(HtmlConstants.ELEM_BUTTON);
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, "submit", false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, name));
		// addAttribute(new SimpleAttribute(HtmlConstants.ATTR_VALUE, name, false));
	}
	
	/**
	 * Constructs a button with the given type and given
	 * name. The value of the button will be its raw name.
	 * 
	 * @param type
	 * @param name
	 */
	public ButtonInput(String type, String name) {
		super(HtmlConstants.ELEM_BUTTON);
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, type != null ? type : "submit", false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, name));
		// addAttribute(new SimpleAttribute(HtmlConstants.ATTR_VALUE, name, false));
	}
	
	/**
	 * Creates a checkbox property based on the dynamic property
	 * 
	 * @param prop The dynamic (boolean) property
	 */
	public ButtonInput(String value, DynamicProperty prop) {
		super(HtmlConstants.ELEM_INPUT);
		this.prop = prop;
		
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_TYPE, "checkbox", false));
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, prop.getName()));
		addAttribute(new SimpleAttribute(HtmlConstants.ATTR_VALUE, value, false));
	}
	
	/**
	 * Adds on the property value if there is a dynamic property
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret ) {
			if( label != null ) {
				model.getWriter().append(model.getMessage(label, label));
			}
		}
		
		return ret;
	}
	
	/**
	 * Adds in the checked attribute if appropriate
	 */
	@Override
	protected void addAttributes(RenderModel model, GenericElement elem) {
		if( prop != null ) {
			String val = prop.getValue(model);
			if( "true".equals(val) ) elem.addAttribute(HtmlConstants.ATTR_CHECKED);
		}

		super.addAttributes(model, elem);
	}

	/**
	 * @return the prop
	 */
	public DynamicProperty getProp() {
		return prop;
	}

	/**
	 * @param prop the prop to set
	 */
	public void setProp(DynamicProperty prop) {
		this.prop = prop;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
