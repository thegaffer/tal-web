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
import org.talframework.talui.util.htmlhelper.HtmlConstants;

public class Cell extends AbstractHtmlElement {
	
	private String message = null;

	public Cell(String id) {
		super(HtmlConstants.ELEM_CELL, id);
	}
	
	public Cell(String id, boolean heading) {
		super(heading ? HtmlConstants.ELEM_HEADING : HtmlConstants.ELEM_CELL, id);
	}
	
	/**
	 * Adds in the static message if there is one
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret && message != null ) {
			model.getWriter().append(model.getMessage(message, message));
		}
		return ret;
	}
	
	/**
	 * Call to make this cell a heading or not
	 * after the constructor
	 * 
	 * @param heading True if cell is a heading
	 */
	public void setHeading(boolean heading) {
		setElementName(heading ? HtmlConstants.ELEM_HEADING : HtmlConstants.ELEM_CELL);
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
