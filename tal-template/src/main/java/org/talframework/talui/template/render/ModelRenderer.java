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

package org.talframework.talui.template.render;

import java.io.IOException;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.Renderer;

/**
 * This class is a model renderer. By that it starts 
 * with one root render element and follows that in
 * conjunction with the model objects. The root template
 * provided at construction contains all the other
 * render elements (including those for other templates).
 * 
 * @author Tom Spencer
 */
public class ModelRenderer implements Renderer {
	
	/** Member holds the root template */
	private final RenderElement rootTemplate;
	
	/**
	 * Call to construct a generic renderer
	 * 
	 * @param root The root template any rendering should being with
	 * @param templates The compiled templates
	 */
	public ModelRenderer(RenderElement root) {
		if( root == null ) throw new IllegalArgumentException("You must supply a root template for the generic renderer");
		
		this.rootTemplate = root;
	}
	
	/**
	 * Always returns true
	 */
	public boolean isModelRenderer() {
		return true;
	}

	/**
	 * Simply invokes render on the root template
	 */
	public void render(RenderModel model) throws IOException {
		rootTemplate.render(model);
	}
}
