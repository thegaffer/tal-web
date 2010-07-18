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

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.AbstractRenderElement;

public class SpecGroupRenderElement extends AbstractRenderElement {
	
	private final String name;
	private String currentGroup = null;
	
	public SpecGroupRenderElement(String name) {
		this.name = name;
	}

	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		currentGroup = (String)model.getObject("currentGroup");
		model.setObject("currentGroup", name);
		return super.preRender(model);
	}
	
	@Override
	protected void postRender(RenderModel model) throws IOException {
		model.setObject("currentGroup", currentGroup);
		super.postRender(model);
	}
}
