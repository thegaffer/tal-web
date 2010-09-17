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
import org.talframework.talui.util.htmlhelper.HtmlCharStripper;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

/**
 * This class generates out a HTML text area
 * 
 * @author Tom Spencer
 */
public class TextArea extends AbstractHtmlElement {
	
	private final DynamicProperty prop;

	public TextArea(DynamicProperty prop) {
		super(HtmlConstants.ELEM_TEXTAREA, prop.getName());
		this.prop = prop;
		
		// Rows and Cols!?!
	}
	
	/**
	 * Adds on the value inbetween the element start/end
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		if( ret ) {
			Object val = prop.getValue(model);
			if( val != null ) HtmlCharStripper.strip(val.toString(), model.getWriter());
		}
		return ret;
	}

	/**
	 * @return the prop
	 */
	public DynamicProperty getProp() {
		return prop;
	}
}
