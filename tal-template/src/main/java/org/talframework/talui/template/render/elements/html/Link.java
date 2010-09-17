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
import java.util.Map;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.CommandElement;
import org.talframework.talui.util.htmlhelper.GenericElement;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

public class Link extends AbstractHtmlElement {

	private String message = null;
	private final CommandElement command;
	
	public Link(String name, CommandElement command) {
		super(HtmlConstants.ELEM_LINK, name);
		this.command = command;
	}
	
	/**
	 * Adds on the property value if there is a dynamic property
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret && message != null ) {
			model.getWriter().append(model.getMessage(message, message));
		}
		return ret;
	}
	
	@Override
	protected void addAttributes(RenderModel model, GenericElement elem) {
		// Add on the href
		String action = command.getAction(model);
		Map<String, String> params = command.getActionParameters(model);
		
		elem.addUrlAttribute(HtmlConstants.ATTR_HREF, action, params);
		
		// Any other attrs
		super.addAttributes(model, elem);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
