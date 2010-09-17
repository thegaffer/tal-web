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

package org.talframework.talui.template.render.elements.js;

import java.io.IOException;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.render.elements.SimpleRenderElementBase;

/**
 * This JavaScript render element will render out commands to
 * attach the a popup div, activated after a set time that will
 * refer the reader to another page of the same app showing more
 * details about the element they are hovering over.
 * 
 * @author Tom Spencer
 */
public class ReferencePageAttachment extends SimpleRenderElementBase {

	private final String type;
	private final String role;
	
	public ReferencePageAttachment(String type) {
		this.type = type;
		this.role = null;
	}
	
	/**
	 * Renders out the function to attach the dynamic title
	 */
	public void render(RenderModel model) throws IOException {
		StringBuilder buf = model.getTempBuffer();
		buf.append("dynamicOnLoad(function() {\n");
		buf.append("\tdynamicTitleAttach(");
		buf.append("'").append(type).append("', ");
		if( this.role != null ) buf.append("'").append(role).append("'");
		else buf.append("null");
		buf.append(");\n");
		buf.append("});");
		model.getWriter().write(buf.toString());
	}
}
