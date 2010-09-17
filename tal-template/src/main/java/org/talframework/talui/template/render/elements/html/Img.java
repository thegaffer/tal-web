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
import org.talframework.talui.template.render.elements.RenderParameter;
import org.talframework.talui.util.htmlhelper.GenericElement;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

/**
 * This class represents an Image HTML element
 * 
 * @author Tom Spencer
 */
public class Img extends AbstractHtmlElement {
	
	private RenderParameter resource = null;

	public Img(String id) {
		super(HtmlConstants.ELEM_IMG, id);
	}
	
	@Override
	protected void addAttributes(RenderModel model, GenericElement elem) {
		super.addAttributes(model, elem);
		elem.addResourceAttribute(HtmlConstants.ATTR_SRC, resource.getValue(model));
	}

	/**
	 * @return The current resource render parameter
	 */
	public RenderParameter getResource() {
		return resource;
	}
	
	/**
	 * Call to set the resource for the image source
	 * 
	 * @param resource The image source
	 */
	public void setResource(RenderParameter resource) {
		this.resource = resource;
	}
}
