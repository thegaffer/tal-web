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

import java.io.IOException;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.html.attributes.IDAttribute;
import org.tpspencer.tal.util.htmlhelper.HtmlConstants;

/**
 * This class generates out a Html label element
 * 
 * @author Tom Spencer
 */
public class Label extends AbstractHtmlElement {

	private String label = null;
	
	public Label(String labelMessage) {
		super(HtmlConstants.ELEM_LABEL);
		this.label = labelMessage;
	}
	
	/**
	 * Overridden to add in the label to the body of the element
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret ) model.getWriter().append(model.getMessage(label, label));
		return ret;
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

	/**
	 * @param forField the forField to set
	 */
	public void setForField(String forField) {
		addAttribute(new IDAttribute(HtmlConstants.ATTR_FOR, forField));
	}
}
