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
 * This class is a render element that adds a row to
 * a table detailing the contained element.
 * 
 * @author Tom Spencer
 */
public final class SpecEntryRenderElement extends AbstractRenderElement {

	/** Holds the name column */
	private final String name;
	/** Holds the type column */
	private final String type;
	/** Holds the description column */
	private final String description;
	
	/**
	 * Constructs the entry render element
	 * 
	 * @param name The name column
	 * @param type The type column
	 * @param desc The description (resource)
	 */
	public SpecEntryRenderElement(String name, String type, String desc) {
		this.name = name;
		this.type = type;
		this.description = desc;
	}
	
	/**
	 * Overridden to add in the table row
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		DocumentWriter writer = (DocumentWriter)model.getObject("documentWriter");
		if( writer == null ) throw new IllegalArgumentException("The document writer is not present so cannot render out entry");
		
		String group = (String)model.getObject("currentGroup");
		if( group == null ) group = "";
		
		writer.addTableRow(new String[]{name, group, type, description});

		return super.preRender(model);
	}
	
	/**
	 * Overridden to end the group (if it is a group)
	 */
	@Override
	protected void postRender(RenderModel model) throws IOException {
		// TODO: If a group, end it!?!
		super.postRender(model);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
