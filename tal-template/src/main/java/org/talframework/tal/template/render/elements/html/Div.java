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
import org.tpspencer.tal.template.render.elements.RenderParameter;
import org.tpspencer.tal.util.htmlhelper.HtmlCharStripper;
import org.tpspencer.tal.util.htmlhelper.HtmlConstants;

/**
 * This class generates out a HTML div or span
 * element. 
 * 
 * @author Tom Spencer
 */
public class Div extends AbstractHtmlElement {
	
	/** Holds the dynamic content of the element (if there is any) */
	private final RenderParameter content;
	/** Determines the type of element */
	private boolean asDiv = true;
	
	public Div(String id) {
		super(HtmlConstants.ELEM_DIV, id);
		this.content = null;
	}
	
	public Div(String id, RenderParameter content) {
		super(HtmlConstants.ELEM_DIV, id);
		this.content = content;
	}
	
	/**
	 * Adds on the property value if there is a dynamic property
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret ) {
			if( content != null ) {
				Object val = content.getValue(model);
				if( val != null ) HtmlCharStripper.strip(val.toString(), model.getWriter());
				else model.getWriter().append("&nbsp;");
			}
		}
		
		return ret;
	}
	
	/**
	 * @return the asDiv
	 */
	public boolean isAsDiv() {
		return asDiv;
	}

	/**
	 * @param asDiv the asDiv to set
	 */
	public void setAsDiv(boolean asDiv) {
		this.asDiv = asDiv;
		setElementName(asDiv ? HtmlConstants.ELEM_DIV : HtmlConstants.ELEM_SPAN);
	}
}
