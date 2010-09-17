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
import org.talframework.talui.template.RenderNode;
import org.talframework.talui.util.htmlhelper.HtmlConstants;

public class Row extends AbstractHtmlElement {

	public Row(String id) {
		super(HtmlConstants.ELEM_ROW, id);
	}
	
	/**
	 * Overridden to add on odd/even if there is an index
	 * to model
	 */
	@Override
	protected String getStyleClasses(RenderModel model) {
		String ret = super.getStyleClasses(model);
		
		RenderNode node = model.getCurrentNode();
		if( node != null && node.getIndex() >= 0 ) {
			String toAdd = (node.getIndex() % 2 == 0) ?	"evenRow" : "oddRow";
			
			if( ret != null ) {
				StringBuilder buf = model.getTempBuffer();
				ret = buf.append(ret).append(' ').append(toAdd).toString();
			}
			else {
				ret = toAdd;
			}
		}
		
		return ret;
	}
}
