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

package org.tpspencer.tal.template.render;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.Renderer;

public class TemplateRenderer implements Renderer {
	
	/** Member holds the map of templates */
	private final Map<String, RenderElement> templates;
	
	/**
	 * Constructs a template renderer
	 * 
	 * @param templates The templates - must not be null
	 */
	public TemplateRenderer(Map<String, RenderElement> templates) {
		if( templates == null ) throw new IllegalArgumentException("You must provide some templates to the TemplateRenderer");
		
		this.templates = templates;
	}
	
	/**
	 * Always returns false
	 */
	public boolean isModelRenderer() {
		return false;
	}

	/**
	 * Invokes render on each template
	 */
	public void render(RenderModel model) throws IOException {
		Iterator<RenderElement> it = templates.values().iterator();
		while( it.hasNext() ) {
			it.next().render(model);
		}
	}
}
