/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document.spec.render;

import java.io.IOException;

import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.AbstractRenderElement;

/**
 * This class is a render element that will create a new
 * table in the output document within which rows are
 * created for each element inside that group.
 * 
 * @author Tom Spencer
 */
public class SpecTableRenderElement extends AbstractRenderElement {

	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		DocumentWriter writer = (DocumentWriter)model.getObject("documentWriter");
		if( writer == null ) throw new IllegalArgumentException("The document writer is not present so cannot render out template table");
		
		// TODO: Is there a prelim to writing out this table??
		writer.startTable(new String[]{"view.fields.name.title", "view.fields.group.title", "view.fields.type.title", "view.fields.settings.title"});
		
		return super.preRender(model);
	}
	
	@Override
	protected void postRender(RenderModel model) throws IOException {
		DocumentWriter writer = (DocumentWriter)model.getObject("documentWriter");
		if( writer == null ) throw new IllegalArgumentException("The document writer is not present so cannot render out template table");
		
		writer.endTable();
	}
}
