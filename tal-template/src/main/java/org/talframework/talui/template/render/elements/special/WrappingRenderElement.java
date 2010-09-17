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

package org.talframework.talui.template.render.elements.special;

import java.io.IOException;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;

/**
 * This render element is used in rare circumstances where
 * we may want to wrap a render element with another render
 * element, but we want all children to be rendered inside
 * the original element.
 * 
 * @author Tom Spencer
 */
public class WrappingRenderElement implements RenderElement {

	private RenderElement primaryElement = null;
	private RenderElement secondaryElement = null;
	
	/**
	 * Constructs a new WrappingRenderElement
	 * 
	 * @param wrapper The wrapper
	 * @param inner The inner element
	 */
	public WrappingRenderElement(RenderElement wrapper, RenderElement inner) {
		if( wrapper == null ) throw new IllegalArgumentException("You must provide a primary wrapping element for a WrappingRenderElement");
		if( inner == null ) throw new IllegalArgumentException("You must provide a inner element for a WrappingRenderElement");
		
		this.primaryElement = wrapper;
		this.secondaryElement = inner;
		this.primaryElement.addElement(inner);
	}
	
	/**
	 * Adds all children to the inner element
	 */
	public void addElement(RenderElement element) {
		secondaryElement.addElement(element);
	}
	
	/**
	 * Renders the primary element (which contains the 
	 * inner element)
	 */
	public void render(RenderModel model) throws IOException {
		primaryElement.render(model);
	}
}
